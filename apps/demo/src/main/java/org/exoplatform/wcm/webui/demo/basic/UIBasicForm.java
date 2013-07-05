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
package org.exoplatform.wcm.webui.demo.basic;

import java.util.Date;
import java.util.List;

import javax.jcr.Node;
import javax.portlet.PortletPreferences;

import org.exoplatform.services.wcm.demo.basic.DemoService;
import org.exoplatform.services.wcm.utils.WCMCoreUtils;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.application.portlet.PortletRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.core.UIComponent;
import org.exoplatform.webui.core.lifecycle.Lifecycle;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 26, 2013  
 */
@ComponentConfig (
     lifecycle  = Lifecycle.class, 
     template   = "app:/groovy/webui/BasicPortlet/UIBasicForm.gtmpl")
public class UIBasicForm extends UIComponent {
  
  private String text;
  
  private Node node;
  private List<Node> childNode;
  private DemoService demoService;
  
  private String name;
  private Date dateOfBirth;
  private Boolean single;
  private Long salary;
  private String team;
  
  public UIBasicForm() {
    // create service by WCM util
    demoService = WCMCoreUtils.getService(DemoService.class);
  }

  public void init() {
    PortletRequestContext portletRequestContext = WebuiRequestContext.getCurrentInstance();
    
    PortletPreferences preferences = portletRequestContext.getRequest().getPreferences();
    
    text        = preferences.getValue(UIBasicPortlet.TEXT_PREFERENCE, null);
    
    String path = preferences.getValue(UIBasicPortlet.PATH_PREFERENCE, null);
    node        = demoService.getOneNode(path);
    childNode   = demoService.getChildNode(path);
    
    /*******EMPLOYEE node************/
    
    
  }
  
  public void processRender(WebuiRequestContext context) throws Exception {
    init();
    super.processRender(context);
  }
  
  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Node getNode() {
    return node;
  }

  public void setNode(Node node) {
    this.node = node;
  }

  public List<Node> getChildNode() {
    return childNode;
  }

  public void setChildNode(List<Node> childNode) {
    this.childNode = childNode;
  }

  public DemoService getDemoService() {
    return demoService;
  }

  public void setDemoService(DemoService demoService) {
    this.demoService = demoService;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(Date dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public Boolean getSingle() {
    return single;
  }

  public void setSingle(Boolean single) {
    this.single = single;
  }

  public Long getSalary() {
    return salary;
  }

  public void setSalary(Long salary) {
    this.salary = salary;
  }

  public String getTeam() {
    return team;
  }

  public void setTeam(String team) {
    this.team = team;
  }
}