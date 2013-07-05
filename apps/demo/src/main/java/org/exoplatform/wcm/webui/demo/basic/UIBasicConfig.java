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

import javax.jcr.Node;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;

import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.wcm.demo.basic.DemoService;
import org.exoplatform.services.wcm.utils.WCMCoreUtils;
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
 
@ComponentConfig(
    lifecycle = UIFormLifecycle.class,
    template = "system:/groovy/webui/form/UIForm.gtmpl",
    events = {
        @EventConfig(listeners = UIBasicConfig.SaveActionListener.class),
        @EventConfig(listeners = UIBasicConfig.CancelActionListener.class),
        @EventConfig(listeners = UIBasicConfig.ShowNodesActionListener.class)
    }
)
public class UIBasicConfig extends UIForm {
  
  private static final Log LOG = ExoLogger.getExoLogger(UIBasicConfig.class.getName());
 
  public static final String TEXT_STRING_INPUT    = "UIBasicPortletTextStringInput";
  
  public static final String PATH_STRING_INPUT    = "UIBasicPortletPathStringInput";
  
  public static final String NAME_STRING_INPUT    = "UIBasicPortletNameStringInput";
  
  public static final String DATE_DATE_INPUT      = "UIBasicPortletBirthdayDateInput";
  
  public static final String SINGLE_CHECKBOX_INPUT  = "UIBasicPortletSingleBooleanInput";
  
  public static final String SALARY_LONG_INPUT    = "UIBasicPortletSalaryLongInput";
  
  public static final String TEAM_STRING_INPUT    = "UIBasicPortletTeamStringInput";
  
  private DemoService demoService;
 
    public UIBasicConfig() {
      
      demoService = WCMCoreUtils.getService(DemoService.class);
      
      PortletRequestContext portletRequestContext = WebuiRequestContext.getCurrentInstance();
      PortletPreferences preferences = portletRequestContext.getRequest().getPreferences();
      
      String text = preferences.getValue(UIBasicPortlet.TEXT_PREFERENCE, null);
      String path = preferences.getValue(UIBasicPortlet.PATH_PREFERENCE, null);
      String name = preferences.getValue(UIBasicPortlet.NAME_PREFERENCE, null);
      String dateOfBirth = preferences.getValue(UIBasicPortlet.DATE_PREFERENCE, null);
      String single = preferences.getValue(UIBasicPortlet.SINGLE_PREFERENCE, null);
      String salary = preferences.getValue(UIBasicPortlet.SALARY_PREFERENCE, null);
      String team = preferences.getValue(UIBasicPortlet.TEAM_PREFERENCE, null);
      
      LOG.info("name: " + name + ", birthday: " + dateOfBirth + ", single: " + single +
               ", salary: " + salary + ", team: "+ team);
      
      addChild(new UIFormStringInput(TEXT_STRING_INPUT, text));
      addChild(new UIFormStringInput(PATH_STRING_INPUT, path));
      addChild(new UIFormStringInput(NAME_STRING_INPUT, name));
      
      SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
      Date date = null;
      try {
        date = sdf.parse(dateOfBirth);
      } catch (ParseException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      
      addChild(new UIFormDateTimeInput(DATE_DATE_INPUT, DATE_DATE_INPUT, new Date()));
      addChild(new UIFormCheckBoxInput(SINGLE_CHECKBOX_INPUT, null, single));
      addChild(new UIFormStringInput(SALARY_LONG_INPUT, salary));
      addChild(new UIFormStringInput(TEAM_STRING_INPUT, team));
    }
    
    public static class ShowNodesActionListener extends EventListener<UIBasicConfig> {
      public void execute(Event<UIBasicConfig> event) throws Exception {
        PortletRequestContext context = (PortletRequestContext) event.getRequestContext();
        context.setApplicationMode(PortletMode.VIEW);
      }
    }
 
    public static class SaveActionListener extends EventListener<UIBasicConfig> {

      @Override
      public void execute(Event<UIBasicConfig> event) throws Exception {
        UIBasicConfig basicConfig = event.getSource();
        UIFormStringInput textStringInput = basicConfig.getUIStringInput(TEXT_STRING_INPUT);
        UIFormStringInput pathStringInput = basicConfig.getUIStringInput(PATH_STRING_INPUT);
        
        UIFormStringInput   name          = basicConfig.getUIStringInput(NAME_STRING_INPUT);
        UIFormDateTimeInput dateOfBirth   = basicConfig.getUIFormDateTimeInput(DATE_DATE_INPUT);
        UIFormCheckBoxInput single        = basicConfig.getUIFormCheckBoxInput(SINGLE_CHECKBOX_INPUT);
        UIFormStringInput   salary        = basicConfig.getUIStringInput(SALARY_LONG_INPUT);
        UIFormStringInput   team          = basicConfig.getUIStringInput(TEAM_STRING_INPUT);
        
        LOG.info("name: " + name.getValue() + ", birthday: " + dateOfBirth.getValue() + ", single: " + single.isChecked() +
                 ", salary: " + salary.getValue() + ", team: "+ team.getValue());
        
        PortletRequestContext portletRequestContext = WebuiRequestContext.getCurrentInstance();
        PortletPreferences preferences = portletRequestContext.getRequest().getPreferences();
        preferences.setValue(UIBasicPortlet.TEXT_PREFERENCE, textStringInput.getValue());
        preferences.setValue(UIBasicPortlet.PATH_PREFERENCE, pathStringInput.getValue());
        
        preferences.setValue(UIBasicPortlet.NAME_PREFERENCE, name.getValue());
        preferences.setValue(UIBasicPortlet.DATE_PREFERENCE, dateOfBirth.getValue());
        preferences.setValue(UIBasicPortlet.SINGLE_PREFERENCE, String.valueOf(single.isChecked()));
        preferences.setValue(UIBasicPortlet.SALARY_PREFERENCE, salary.getValue());
        preferences.setValue(UIBasicPortlet.TEAM_PREFERENCE, team.getValue());
        
        preferences.store();
        
        //Node employeeNode = basicConfig.demoService.addNewNode(name.getValue(), pathStringInput.getValue());
        Node addedNode = basicConfig.demoService.addNewNode(name.getValue(), pathStringInput.getValue(), dateOfBirth.getValue(),
                                           single.isChecked(), salary.getValue(), team.getValue());
        LOG.info("add node with name: " + name.getValue() + ", path: " + pathStringInput.getValue());
        
        if (addedNode == null) {
          // TODO should display a dialog box to show error message.
        }
        
        PortletRequestContext context = (PortletRequestContext) event.getRequestContext();
        context.setApplicationMode(PortletMode.VIEW);
      }
    }
 
    public static class CancelActionListener extends EventListener<UIBasicConfig> {
        public void execute(Event<UIBasicConfig> event) throws Exception {
          PortletRequestContext context = (PortletRequestContext) event.getRequestContext();
          context.setApplicationMode(PortletMode.VIEW);
        }
    }
}
