/*
 * Copyright (C) 2003-2013 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.exoplatform.services.wcm.demo.basic;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;

import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.core.ManageableRepository;
import org.exoplatform.services.jcr.ext.common.SessionProvider;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.wcm.utils.WCMCoreUtils;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 1, 2013  
 */
public class DemoServiceImpl implements DemoService {
  
  public static final String EMPLOYEE = "exo:employee";
  public static final String NAME = "exo:name";
  public static final String BIRTHDAY = "exo:dateOfBirth";
  public static final String SINGLE = "exo:single";
  public static final String SALARY = "exo:salary";
  public static final String TEAM = "exo:team";


  private Log log = ExoLogger.getLogger("wcm:DemoServiceImpl");
  
  private static final String REPOSITORY = "repository";
  
  private static final String WORKSPACE = "collaboration";
  
  private RepositoryService repositoryService;
  
  public DemoServiceImpl() {
    repositoryService = WCMCoreUtils.getService(RepositoryService.class);
  }
  
  @Override
  public Node getOneNode(String nodePath) {
    try {
      ManageableRepository repository = repositoryService.getRepository(REPOSITORY);
      SessionProvider sessionProvider = WCMCoreUtils.getUserSessionProvider();
      Session session = sessionProvider.getSession(WORKSPACE, repository);
      Node node = (Node) session.getItem(nodePath);
      return node;
    } catch (Exception e) {
      if (log.isErrorEnabled())
        log.error("An error is occured: ", e);
      return null;
    }
  }

  @Override
  public List<Node> getChildNode(String parentPath) {
    try {
      ManageableRepository repository = repositoryService.getRepository(REPOSITORY);
      SessionProvider sessionProvider = WCMCoreUtils.getUserSessionProvider();
      Session session = sessionProvider.getSession(WORKSPACE, repository);
      Node parent = (Node) session.getItem(parentPath);
      NodeIterator iterator = parent.getNodes();
      List<Node> nodes = new ArrayList<Node>();
      while (iterator.hasNext()) {
        nodes.add(iterator.nextNode());
      }
      return nodes;
    } catch (Exception e) {
      if (log.isErrorEnabled())
        log.error("An error is occured: ", e);
      return null;
    }
  }

  @Override
  public Node addNewNode(String nodeName, String parentPath) {
    try {
      ManageableRepository repository = repositoryService.getRepository(REPOSITORY);
      SessionProvider sessionProvider = WCMCoreUtils.getUserSessionProvider();
      Session session = sessionProvider.getSession(WORKSPACE, repository);
      Node parent = (Node) session.getItem(parentPath);
      Node node = parent.addNode(nodeName);
      session.save();
      return node;
    } catch (Exception e) {
      if (log.isErrorEnabled())
        log.error("An error is occured: ", e);
      return null;
    }

  }

  @Override
  public Node addNewNode(String nodeName,
                         String parentPath,
                         String birthday,
                         boolean single,
                         String salary,
                         String team) {
    
    try {
      ManageableRepository repository = repositoryService.getRepository(REPOSITORY);
      SessionProvider sessionProvider = WCMCoreUtils.getUserSessionProvider();
      Session session = sessionProvider.getSession(WORKSPACE, repository);
      Node parent = (Node) session.getItem(parentPath);
      
      Node employeeNode = null;
      // Check if the node name existed in this path.
      if (!parent.hasNode(nodeName)) {
        employeeNode = parent.addNode(nodeName);
        employeeNode.setProperty(NAME, nodeName);
        employeeNode.setProperty(BIRTHDAY, birthday);
        employeeNode.setProperty(SINGLE, single);
        employeeNode.setProperty(SALARY, salary);
        employeeNode.setProperty(TEAM, team);
        session.save();
        log.info("saved a node as a name: [" + nodeName + "] to path: [" + parentPath);
      } else {
        // TODO should display a dialog box to show error message.
        log.info("cannot save this node because of node name already existed.");
      }
      
      return employeeNode;
    } catch (Exception e) {
      if (log.isErrorEnabled())
        log.error("An error is occured: ", e);
      return null;
    }
  }

}
