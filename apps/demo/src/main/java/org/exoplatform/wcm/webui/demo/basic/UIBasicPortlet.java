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

import javax.portlet.PortletMode;

import org.exoplatform.webui.application.WebuiApplication;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.application.portlet.PortletRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.core.UIPortletApplication;
import org.exoplatform.webui.core.lifecycle.UIApplicationLifecycle;

/**
 * Created by The eXo Platform SAS Author : eXoPlatform exo@exoplatform.com Jun
 * 26, 2013
 */
@ComponentConfig(lifecycle = UIApplicationLifecycle.class)
public class UIBasicPortlet extends UIPortletApplication {

  public static final String TEXT_PREFERENCE = "text";
  public static final String CHECKBOX_PREFERENCE = "checkbox";
  public static final String DATETIME_PREFERENCE = "datetime";

  public UIBasicPortlet() throws Exception {
    super();
    // TODO Auto-generated constructor stub
  }

  public void processRender(WebuiApplication app, WebuiRequestContext context) throws Exception {
    getChildren().clear();
    PortletRequestContext pContext = (PortletRequestContext) context;

    PortletMode currentMode = pContext.getApplicationMode();
    if (PortletMode.VIEW.equals(currentMode)) {
      if (getChild(UIBasicForm.class) == null) addChild(UIBasicForm.class, null, null);
    } else {
      if (getChild(UIBasicConfig.class) == null) addChild(UIBasicConfig.class, null, null);
    }
    super.processRender(app, context);
  }

}
