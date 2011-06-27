/*
 * Copyright (C) 2003-2008 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.wcm.connector.fckeditor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;

import org.exoplatform.commons.utils.ISO8601;
import org.exoplatform.commons.utils.PageList;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.component.RequestLifeCycle;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.portal.config.DataStorage;
import org.exoplatform.portal.config.Query;
import org.exoplatform.portal.config.UserACL;
import org.exoplatform.portal.config.UserPortalConfig;
import org.exoplatform.portal.config.UserPortalConfigService;
import org.exoplatform.portal.config.model.Page;
import org.exoplatform.portal.config.model.PortalConfig;
import org.exoplatform.portal.mop.SiteKey;
import org.exoplatform.portal.mop.Visibility;
import org.exoplatform.portal.mop.user.UserNavigation;
import org.exoplatform.portal.mop.user.UserNode;
import org.exoplatform.portal.mop.user.UserNodeFilterConfig;
import org.exoplatform.portal.mop.user.UserPortal;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.rest.resource.ResourceContainer;
import org.exoplatform.services.security.ConversationState;
import org.exoplatform.services.wcm.navigation.NavigationUtils;
import org.exoplatform.services.wcm.utils.WCMCoreUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/*
 * Created by The eXo Platform SAS Author : Anh Do Ngoc anh.do@exoplatform.com
 * Jul 11, 2008
 */
@SuppressWarnings("deprecation")
@Path("/portalLinks/")
public class PortalLinkConnector implements ResourceContainer {

  /** The PUBLI c_ access. */
  final private String PUBLIC_ACCESS       = "public".intern();

  /** The PRIVAT e_ access. */
  final private String PRIVATE_ACCESS      = "private".intern();

  /** The EVERYON e_ permission. */
  final private String EVERYONE_PERMISSION = "Everyone".intern();

  /** The RESOURC e_ type. */
  final private String RESOURCE_TYPE       = "PortalPageURI".intern();

  /** The portal data storage. */
  private DataStorage  portalDataStorage;

  /** The portal user acl. */
  private UserACL      portalUserACL;

  /** The servlet context. */
  private ServletContext servletContext;

  /** The log. */
  private static Log log = ExoLogger.getLogger(PortalLinkConnector.class);

  /** The Constant LAST_MODIFIED_PROPERTY. */
  private static final String LAST_MODIFIED_PROPERTY = "Last-Modified";

  /** The Constant IF_MODIFIED_SINCE_DATE_FORMAT. */
  private static final String IF_MODIFIED_SINCE_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss z";

  /**
   * Instantiates a new portal link connector.
   *
   * @param params the params
   * @param dataStorage the data storage
   * @param userACL the user acl
   * @param servletContext the servlet context
   *
   * @throws Exception the exception
   */
  public PortalLinkConnector(InitParams params,
                             DataStorage dataStorage,
                             UserACL userACL,
                             ServletContext servletContext) throws Exception {
    this.portalDataStorage = dataStorage;
    this.portalUserACL = userACL;
    this.servletContext = servletContext;
  }

  /**
   * Gets the page uri.
   *
   * @param currentFolder the current folder
   * @param command the command
   * @param type the type
   *
   * @return the page uri
   *
   * @throws Exception the exception
   */
  @GET
  @Path("/getFoldersAndFiles/")
//  @OutputTransformer(XMLOutputTransformer.class)
  public Response getPageURI(@QueryParam("currentFolder") String currentFolder,
                             @QueryParam("command") String command,
                             @QueryParam("type") String type) throws Exception {
    try {
      String userId = getCurrentUser();
      return buildReponse(currentFolder, command, userId);
    } catch (Exception e) {
      log.error("Error when perform getPageURI: ", e);
    }
    DateFormat dateFormat = new SimpleDateFormat(IF_MODIFIED_SINCE_DATE_FORMAT);
    return Response.ok().header(LAST_MODIFIED_PROPERTY, dateFormat.format(new Date())).build();
  }

  /**
   * Gets the current user.
   *
   * @return the current user
   */
  private String getCurrentUser() {
    try {
      ConversationState conversationState = ConversationState.getCurrent();
      return conversationState.getIdentity().getUserId();
    } catch (Exception e) {
      log.error("Error when perform getCurrentUser: ", e);
    }
    return null;
  }

  /**
   * Builds the reponse.
   *
   * @param currentFolder the current folder
   * @param command the command
   * @param userId the user id
   *
   * @return the response
   *
   * @throws Exception the exception
   */
  private Response buildReponse(String currentFolder, String command, String userId) throws Exception {
    Document document = null;
    if (currentFolder == null || "/".equals(currentFolder)) {
      document = buildPortalXMLResponse("/", command, userId);
    } else {
      document = buildNavigationXMLResponse(currentFolder, command, userId);
    }
    CacheControl cacheControl = new CacheControl();
    cacheControl.setNoCache(true);
    cacheControl.setNoStore(true);
    DateFormat dateFormat = new SimpleDateFormat(IF_MODIFIED_SINCE_DATE_FORMAT);
    return Response.ok(new DOMSource(document), MediaType.TEXT_XML)
                   .cacheControl(cacheControl)
                   .header(LAST_MODIFIED_PROPERTY, dateFormat.format(new Date()))
                   .build();
  }

  /**
   * Builds the portal xml response.
   *
   * @param currentFolder the current folder
   * @param command the command
   * @param userId the user id
   *
   * @return the document
   *
   * @throws Exception the exception
   */
  @SuppressWarnings("unchecked")
  private Document buildPortalXMLResponse(String currentFolder, String command, String userId) throws Exception {
    Element rootElement = initRootElement(command, currentFolder);
    Query<PortalConfig> query = new Query<PortalConfig>(null, null, null, null, PortalConfig.class);
    PageList pageList = portalDataStorage.find(query, new Comparator<PortalConfig>() {
      public int compare(PortalConfig pconfig1, PortalConfig pconfig2) {
        return pconfig1.getName().compareTo(pconfig2.getName());
      }
    });
    // should use PermissionManager to check access permission
    Element foldersElement = rootElement.getOwnerDocument().createElement("Folders");
    rootElement.appendChild(foldersElement);
    for (Object object : pageList.getAll()) {
      PortalConfig config = (PortalConfig) object;
//      if (!portalUserACL.hasPermission(config, userId)) {
      if (!portalUserACL.hasPermission(config)) {
        continue;
      }
      Element folderElement = rootElement.getOwnerDocument().createElement("Folder");
      folderElement.setAttribute("name", config.getName());
      folderElement.setAttribute("url", "");
      folderElement.setAttribute("folderType", "");
      foldersElement.appendChild(folderElement);
    }
    return rootElement.getOwnerDocument();
  }

  /**
   * Builds the navigation xml response.
   *
   * @param currentFolder the current folder
   * @param command the command
   * @param userId the user id
   *
   * @return the document
   *
   * @throws Exception the exception
   */
  private Document buildNavigationXMLResponse(String currentFolder, String command, String userId) throws Exception {
    PortalContainer container = PortalContainer.getInstance();
    RequestLifeCycle.begin(container);
    String portalName = currentFolder.substring(1, currentFolder.indexOf('/', 1));
    String pageNodeUri = currentFolder.substring(portalName.length() + 1);

    // init the return value
    Element rootElement = initRootElement(command, currentFolder);
    Element foldersElement = rootElement.getOwnerDocument().createElement("Folders");
    Element filesElement = rootElement.getOwnerDocument().createElement("Files");
    rootElement.appendChild(foldersElement);
    rootElement.appendChild(filesElement);

    // get navigation data
    UserPortalConfigService pConfig = WCMCoreUtils.getService(UserPortalConfigService.class);
    UserPortalConfig userPortalCfg = pConfig.getUserPortalConfig(portalName,
                                                                 userId,
                                                                 null);
    UserPortal userPortal = userPortalCfg.getUserPortal();
    UserNavigation navigation = userPortal.getNavigation(SiteKey.portal(portalName));
    UserNode userNode = null;

    if (pageNodeUri == null) {
      RequestLifeCycle.end();
      return rootElement.getOwnerDocument();
    }
    
    //filter nodes
    UserNodeFilterConfig.Builder filterConfigBuilder = UserNodeFilterConfig.builder();
    filterConfigBuilder.withAuthorizationCheck().withVisibility(Visibility.DISPLAYED, Visibility.TEMPORAL);
    filterConfigBuilder.withTemporalCheck();
    UserNodeFilterConfig filterConfig = filterConfigBuilder.build();

    if ("/".equals(pageNodeUri)) {
      userNode = userPortal.getNode(navigation, NavigationUtils.ECMS_NAVIGATION_SCOPE, filterConfig, null);
    } else {
      pageNodeUri = pageNodeUri.substring(1, pageNodeUri.length() - 1);
      userNode = userPortal.resolvePath(navigation, filterConfig, pageNodeUri);

      if (userNode != null) {
        userPortal.updateNode(userNode, NavigationUtils.ECMS_NAVIGATION_SCOPE, null);
      }
    }

    if (userNode != null) {
      // expand root node
      Iterator<UserNode> childrenIter = userNode.getChildren().iterator();
      while (childrenIter.hasNext()) {
        UserNode child = childrenIter.next();
        processPageNode(portalName, child, foldersElement, filesElement, userId, pConfig);
      }
    }
    RequestLifeCycle.end();
    return rootElement.getOwnerDocument();
  }

  /**
   * Inits the root element.
   *
   * @param commandStr the command str
   * @param currentPath the current path
   *
   * @return the element
   *
   * @throws ParserConfigurationException the parser configuration exception
   */
  private Element initRootElement(String commandStr, String currentPath) throws ParserConfigurationException {
    Document doc = null;
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    doc = builder.newDocument();
    Element rootElement = doc.createElement("Connector");
    doc.appendChild(rootElement);
    rootElement.setAttribute("command", commandStr);
    rootElement.setAttribute("resourceType", RESOURCE_TYPE);
    Element myEl = doc.createElement("CurrentFolder");
    myEl.setAttribute("path", currentPath);
    myEl.setAttribute("url", "");
    rootElement.appendChild(myEl);
    return rootElement;
  }

  /**
   * Process page node.
   *
   * @param portalName the portal name
   * @param userNode the user node
   * @param foldersElement the root element
   * @param filesElement
   * @param userId the user id
   * @param portalConfigService the portal config service
   *
   * @throws Exception the exception
   */
  private void processPageNode(String portalName,
                               UserNode userNode,
                               Element foldersElement,
                               Element filesElement,
                               String userId,
                               UserPortalConfigService portalConfigService) throws Exception {
    String pageId = userNode.getPageRef();
    Page page = portalConfigService.getPage(pageId, userId);
    String pageUri = "";
    if (page == null) {
    pageUri = "/";
    Element folderElement = foldersElement.getOwnerDocument().createElement("Folder");
      folderElement.setAttribute("name", userNode.getName());
      folderElement.setAttribute("folderType", "");
      folderElement.setAttribute("url", pageUri);
      foldersElement.appendChild(folderElement);
    } else {
      String accessMode = PRIVATE_ACCESS;
      for (String role : page.getAccessPermissions()) {
        if (EVERYONE_PERMISSION.equalsIgnoreCase(role)) {
          accessMode = PUBLIC_ACCESS;
          break;
        }
      }
      pageUri = "/" + servletContext.getServletContextName() + "/" + accessMode + "/" + portalName + "/" + userNode.getURI();

      Element folderElement = foldersElement.getOwnerDocument().createElement("Folder");
      folderElement.setAttribute("name", userNode.getName());
      folderElement.setAttribute("folderType", "");
      folderElement.setAttribute("url", pageUri);
      foldersElement.appendChild(folderElement);

      SimpleDateFormat formatter = new SimpleDateFormat(ISO8601.SIMPLE_DATETIME_FORMAT);
      String datetime = formatter.format(new Date());
      Element fileElement = filesElement.getOwnerDocument().createElement("File");
      fileElement.setAttribute("name", userNode.getName());
      fileElement.setAttribute("dateCreated", datetime);
      fileElement.setAttribute("fileType", "page node");
      fileElement.setAttribute("url", pageUri);
      fileElement.setAttribute("size", "");
      filesElement.appendChild(fileElement);
    }
  }

  /**
   * Gets the page node.
   *
   * @param root the root
   * @param uri the uri
   *
   * @return the page node
   */
  private UserNode getUserNode(UserNode root, String uri) {
    if (uri.equals("/" + root.getURI() + "/")) {
      return root;
    }
    Iterator<UserNode> childrenIter = root.getChildren().iterator();
    if (childrenIter == null) {
      return null;
    }
    while (childrenIter.hasNext()) {
      UserNode child = childrenIter.next();
      if (uri.equals("/" + child.getURI() + "/")) {
        return child;
      }
      UserNode deepChild = getUserNode(child, uri);
      if (deepChild != null) {
        return deepChild;
      }
    }
    return null;
  }
}
