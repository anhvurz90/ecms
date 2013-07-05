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

import java.util.List;

import javax.jcr.Node;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 1, 2013  
 */
public interface DemoService {
  
  public Node getOneNode(String nodePath) ;
  
  public List<Node> getChildNode(String parentPath) ;

  public Node addNewNode(String nodeName, String parentPath) ;
  
  public Node addNewNode(String nodeName, String parentPath, String birthday, boolean single, String salary, String team) ;
}
