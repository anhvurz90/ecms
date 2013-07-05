///*
// * Copyright (C) 2003-2013 eXo Platform SAS.
// *
// * This program is free software: you can redistribute it and/or modify
// * it under the terms of the GNU Affero General Public License as published by
// * the Free Software Foundation, either version 3 of the License, or
// * (at your option) any later version.
// *
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU Affero General Public License for more details.
// *
// * You should have received a copy of the GNU Affero General Public License
// * along with this program. If not, see <http://www.gnu.org/licenses/>.
// */
//
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.GregorianCalendar;
//
//import javax.jcr.Node;
//import javax.jcr.NodeIterator;
//import javax.jcr.Value;
//import javax.jcr.query.Query;
//import javax.jcr.query.QueryManager;
//
//import org.exoplatform.services.wcm.BaseWCMTestCase;
//
///**
// * Created by The eXo Platform SAS Author : eXoPlatform exo@exoplatform.com Jun
// * 28, 2013
// */
//public class TestJCR extends BaseWCMTestCase {
//
//  private static final String EXO        = "eXo";
//
//  private static final String ECM        = "ECM";
//
//  private static final String PORTAL     = "Portal";
//
//  private static final String SUPPORT    = "Support";
//
//  private static final String PERSON     = "exo:person";
//
//  private static final String NAME       = "exo:name";
//
//  private static final String AGE        = "exo:age";
//
//  private static final String GRADUATION = "exo:dateOfGraduation";
//
//  private static final String MARRIED    = "exo:married";
//
//  private static final String HOBBIES    = "exo:hobbies";
//
//  private static final String TEAM       = "exo:team";
//
//  private static final String TEAM_NAME  = "exo:teamName";
//
//  public void setUp() throws Exception {
//    super.setUp();
//    Node rootNode = session.getRootNode();
//    Node exoNode = rootNode.addNode(EXO);
//    rootNode.save();
//    Node ecmNode = addTeam(exoNode, ECM);
//    addPerson(ecmNode, "Ivan", "Ivan Ivanovich", 25, "ECM", new GregorianCalendar(1985, 10, 25), true, new String[] {"Chinese chess", "computer game"});
//    addPerson(ecmNode, "Peter", "Peter Pan", 26, "ECM", new GregorianCalendar(1984, 9, 26), false, new String[] {"swimming", "dancing"});
//    addPerson(ecmNode, "Muller", "Andy Muller", 27, "ECM", new GregorianCalendar(1983, 8, 27), true, new String[] {"reading books", "coding"});
//
//    Node portalNode = addTeam(exoNode, PORTAL);
//    addPerson(portalNode, "Trong", "Nguyen Van Trong", 28, PORTAL, new GregorianCalendar(1981, 1, 21), true, new String[] {"Yoga", "Meditation"});
//    addPerson(portalNode, "Khoi", "Nguyen Van Khoi", 26, PORTAL, new GregorianCalendar(1985, 3, 26), false, new String[] {"travelling", "films"});
//
//    Node supportNode = addTeam(exoNode, SUPPORT);
//    addPerson(supportNode, "Trang", "Nguyen Thi Trang", 30, SUPPORT, new GregorianCalendar(1981, 1, 21), true, new String[] {"Ikebana", "Cooking"});
//    addPerson(supportNode, "Hoa", "Tran Thi Hoa", 26, SUPPORT, new GregorianCalendar(1985, 3, 26), false, new String[]{"travelling", "sleeping"});
//
//    session.save();
//    
////    System.out.println("Initial Data: ");
////    printData();
//    }
//
//  // add a team into company
//  private Node addTeam(Node companyNode, String teamName) throws Exception {
//    Node teamNode = companyNode.addNode(teamName);
//    teamNode.setProperty(TEAM_NAME, teamName);
//    session.save();
//    return teamNode;
//  }
//
//  // add a person into a team
//  private Node addPerson(Node teamNode,
//                         String nameNode,
//                         String fullName,
//                         long age,
//                         String team,
//                         Calendar dateOfGraduation,
//                         boolean married,
//                         String[] hobbies) throws Exception {
//    Node personNode = teamNode.addNode(nameNode, PERSON);
//    personNode.setProperty(NAME, fullName);
//    personNode.setProperty(AGE, age);
//    personNode.setProperty(TEAM, team);
//    personNode.setProperty(GRADUATION, dateOfGraduation);
//    personNode.setProperty(MARRIED, married);
//    personNode.setProperty(HOBBIES, hobbies);
//    session.save();
//    return personNode;
//  }
//
//  private void printProcess() throws Exception {
//    for (int i = 0; i < 20; i++) {
//      System.out.print('.');
//      Thread.sleep(100);
//    }
//    System.out.println();
//  }
//
//  private void printData() throws Exception {
//    printTree(session.getRootNode().getNode(EXO), "");
//  }
//
//  private void printTree(Node node, String indent) throws Exception {
//    System.out.println(indent + node.getName());
//    String newIndent = indent + " ";
//    if (node.isNodeType(PERSON)) {
//      printPersonData(node, newIndent);
//      return;
//    }
//    
//    // in case a node as many child nodes.
//    NodeIterator iter = node.getNodes();
//    while (iter.hasNext()) {
//      printTree(iter.nextNode(), newIndent);
//    }
//  }
//  
//  private void printPersonData (Node personNode, String intent) throws Exception {
//    System.out.print(intent + "Name: " + personNode.getProperty(NAME).getString());
//    System.out.print(intent + ", Age: " + personNode.getProperty(AGE).getString());
//    
//    SimpleDateFormat format = new SimpleDateFormat("dd:MM:yyyy");
//    String dateOfGraduation = format.format(personNode.getProperty(GRADUATION).getDate().getTime());
//    System.out.print(intent + ", Date of graduation: " + dateOfGraduation);
//    System.out.print(intent + ", Married: " + personNode.getProperty(MARRIED).getBoolean());
//    
//    System.out.print(intent + ", Hobbies: " + "");
//    Value[] hobbies = personNode.getProperty(HOBBIES).getValues();
//    for (int i = 0; i < hobbies.length; i++) {
//      System.out.print(hobbies[i].getString() );
//      if (i == hobbies.length - 1) 
//        System.out.print(".");
//      else
//        System.out.print(", ");
//    }
//    System.out.println();
//  }
//
//  @Override
//  public void tearDown() throws Exception {
//    Node exoNode = session.getRootNode().getNode(EXO);
//    exoNode.remove();
//    session.save();
//  }
//  
//  /******************************** TEST methods *******************************/
//  public void _testViewData() throws Exception {
//    printProcess();
//    System.out.println("Data after created:");
//    printData();
//    
//  }
//  
//  public void _testQuery () throws Exception {
//    System.out.println("Trying to find all employees at age 26");
//    printProcess();
//    
//    // using nodetype configuration file as a database to query by SQL 
//    QueryManager qManager = session.getWorkspace().getQueryManager();
//    Query query = qManager.createQuery("SELECT * FROM exo:person WHERE exo:name like '%Peter%'", Query.SQL);
//    
//    NodeIterator iter = query.execute().getNodes();
//    
//    System.out.println("Result: ");
//    while (iter.hasNext()) {
//      printPersonData(iter.nextNode(), "");
//    }
//  }
//  
//  public void _testCopyNode() throws Exception {
//    System.out.println("Data before coyping: ");
//    printProcess();
//    printData();
//    
//    // To copy/paste a node. need to get path of node (from root) first.
//    System.out.println("Trying to copy Ivan from ECM team to Support Team");
//    String srcAbsPath = session.getRootNode().getNode(EXO).getNode(ECM).getNode("Ivan").getPath();
//    String destAbsPath = session.getRootNode().getNode(EXO).getNode(SUPPORT).getPath() + "/Ivan";
//    session.getWorkspace().copy(srcAbsPath, destAbsPath);
//    
//    session.save();
//    System.out.println("Data after copying:");
//    printProcess();
//    printData();
//  }
//
//  public void testMoveNode() throws Exception {
//    System.out.println("Data before moving: ");
//    printProcess();
//    printData();
//    
//    
//    System.out.println("Trying to move Muller from ECM team to Portal team");
//    
//    String srcAbsPath = session.getRootNode().getNode(EXO).getNode(ECM).getNode("Muller").getPath();
//    String destAbsPath = session.getRootNode().getNode(EXO).getNode(PORTAL).getPath() + "/Muller";
//    session.getWorkspace().move(srcAbsPath, destAbsPath);
//    
//    session.save();
//    
//    System.out.println("Data after moving:");
//    printProcess();
//    printData();
//  }
//  
//  public void testRemoveNode() throws Exception {
//    System.out.println("Data before removing: ");
//    printProcess();
//    printData();
//    
//    System.out.println("Trying to remove Hoa from Support team");
//    
//    // To remove a node, get node and just call remove()
//    session.getRootNode().getNode(EXO).getNode(SUPPORT).getNode("Hoa").remove();
//    session.save();
//    
//    System.out.println("Data after moving:");
//    printProcess();
//    printData();
//  }
//
//
//
//}
