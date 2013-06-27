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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;

import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.application.portlet.PortletRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.form.UIForm;
import org.exoplatform.webui.form.UIFormCheckBoxInput;
import org.exoplatform.webui.form.UIFormDateTimeInput;
import org.exoplatform.webui.form.UIFormStringInput;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 26, 2013  
 */

@ComponentConfig (
   lifecycle = UIFormLifecycle.class,
   template  = "app:/groovy/webui/BasicPortlet/UIEditForm.gtmpl",
   events = {
     @EventConfig (listeners = UIBasicConfig.SaveActionListener.class),
     @EventConfig (listeners = UIBasicConfig.CancelActionListener.class)
   }
)
public class UIBasicConfig extends UIForm {
  public static final String TEXT_STRING_INPUT = "UIBasicPortletTextStringInput";
  public static final String TEXT_STRING_CHECKBOX = "UIBasicPortletCheckboxInput";
  public static final String TEXT_STRING_DATETIME = "UIBasicPortletDateTimeInput";
  
  public UIBasicConfig() {
    PortletRequestContext 
              pRequestContext = WebuiRequestContext.getCurrentInstance(); 
    PortletPreferences 
              preferences     = pRequestContext.getRequest().getPreferences();
    
    /* *********** CREATE INIT VALUES for rendering BEFORE EDIT ************/
    String text     = preferences.getValue(UIBasicPortlet.TEXT_PREFERENCE, null);
    String checkbox = preferences.getValue(UIBasicPortlet.CHECKBOX_PREFERENCE, null);
    String datetime = preferences.getValue(UIBasicPortlet.DATETIME_PREFERENCE, null);
    
    addChild(new UIFormStringInput(TEXT_STRING_INPUT, text));
    addChild(new UIFormCheckBoxInput(TEXT_STRING_CHECKBOX, null, checkbox) );
    
    addChild(new UIFormDateTimeInput(TEXT_STRING_DATETIME, null, null));
  }
  
  public static class SaveActionListener extends EventListener<UIBasicConfig> {

    @Override
    public void execute(Event<UIBasicConfig> event) throws Exception {
      // TODO Auto-generated method stub
      UIBasicConfig basicConfig = event.getSource();
      UIFormStringInput textStringInput = basicConfig.getUIStringInput(TEXT_STRING_INPUT);
      UIFormCheckBoxInput checkBoxInput = basicConfig.getUIFormCheckBoxInput(TEXT_STRING_CHECKBOX); 
      UIFormDateTimeInput dateTimeInput = basicConfig.getUIFormDateTimeInput(TEXT_STRING_DATETIME);
      
      PortletRequestContext 
          pRequestContext = WebuiRequestContext.getCurrentInstance();
      
      PortletPreferences 
          preferences     = pRequestContext.getRequest().getPreferences();
      
      preferences.setValue(UIBasicPortlet.TEXT_PREFERENCE, textStringInput.getValue());
      preferences.setValue(UIBasicPortlet.CHECKBOX_PREFERENCE, String.valueOf(checkBoxInput.isChecked()));
      preferences.setValue(UIBasicPortlet.DATETIME_PREFERENCE, dateTimeInput.getValue());
      preferences.store();
      
      PortletRequestContext context = (PortletRequestContext) event.getRequestContext();
      context.setApplicationMode(PortletMode.VIEW);
      
    }
    
    
  }
  public static class CancelActionListener extends EventListener<UIBasicConfig> {
    
    @Override
    public void execute(Event<UIBasicConfig> event) throws Exception {
      // TODO Auto-generated method stub
      PortletRequestContext 
      portletRequestContext = (PortletRequestContext) event.getRequestContext();
      portletRequestContext.setApplicationMode(PortletMode.VIEW);
    }
    
  }
}
