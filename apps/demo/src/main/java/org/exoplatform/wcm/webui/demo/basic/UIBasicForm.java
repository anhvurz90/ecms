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

import javax.portlet.PortletPreferences;

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
  private String checboxName;
  private String datetime;
  private CheckboxInfo checkboxInfo;
  
  public void init() {
    PortletRequestContext portletRequestContext = WebuiRequestContext.getCurrentInstance();
    
    PortletPreferences preferences = portletRequestContext.getRequest().getPreferences();
    
    text        = preferences.getValue(UIBasicPortlet.TEXT_PREFERENCE, null);
    checboxName = preferences.getValue(UIBasicPortlet.CHECKBOX_PREFERENCE, String.valueOf(false));
    datetime    = preferences.getValue(UIBasicPortlet.DATETIME_PREFERENCE, new Date().toString());
        
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

  public String getChecboxName() {
    return checboxName;
  }

  public void setChecboxName(String checboxName) {
    this.checboxName = checboxName;
  }

  public String getDatetime() {
    return datetime;
  }

  public void setDatetime(String datetime) {
    this.datetime = datetime;
  }

  public CheckboxInfo getCheckboxInfo() {
    return checkboxInfo;
  }

  public void setCheckboxInfo(CheckboxInfo checkboxInfo) {
    this.checkboxInfo = checkboxInfo;
  }
}




