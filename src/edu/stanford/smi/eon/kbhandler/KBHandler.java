/*
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License");  you may not use this file except in 
 * compliance with the License.  You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 *
 * The Original Code is EON Guideline Execution Engine.
 *
 * The Initial Developer of the Original Code is Stanford University. Portions
 * created by Stanford University are Copyright (C) 2009.  All Rights Reserved.
 *
 * The EON Guideline Execution Engine was developed by Center for Biomedical
 * Informatics Research (http://www.bmir.stanford.edu) at the Stanford University
 * with support from the National Library of Medicine.  Current
 * information about the EON project can be obtained at 
 * http://www.smi.stanford.edu/projects/eon/
 *
 */
package edu.stanford.smi.eon.kbhandler;
import edu.stanford.smi.eon.PCAServerModule.*;
import edu.stanford.smi.eon.guidelineinterpreter.*;
import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.eon.util.*;
import java.util.*;
import java.io.Serializable;
import org.apache.log4j.*;

public class KBHandler implements Serializable {
  public KnowledgeBase kb;

  public KBHandler(String kbURL)
    throws PCA_Initialization_Exception {

    logger.info("kbURL: " +kbURL);
    try  {
      kb = loadProject(kbURL);
    } catch (Throwable e) {
      logger.error("Error loading project: " + kbURL);
      e.printStackTrace();
      throw new PCA_Initialization_Exception("knowledge base cannot be loaded");
    }
    
  }

  static Logger logger = Logger.getLogger(KBHandler.class);
  public KBHandler(KnowledgeBase guidelinekb)
    throws PCA_Initialization_Exception {
    kb = guidelinekb;
  }

  public Cls getCls(String className) {
    return kb.getCls(className);
  }

  public KnowledgeBase getKB() {
    return kb;
  }
  
  public KnowledgeBase loadProject(String fileToUse)
    throws Throwable
  {
    Project project;
    Collection error_messages  = new ArrayList();
    //edu.stanford.smi.protege.util.Log.setDisplayTrace(false);
    try {
      project = new Project(fileToUse, error_messages);
      return project.getKnowledgeBase();
    } catch (Throwable  e) {
      logger.error(e.getMessage(), e);
      throw e;
    }
  } // loadProject

  public String prettyName(Cls concept) throws Exception {
    Slot prettyNameSlot = (Slot) getKB().getInstance("PrettyName");
    Object classPrettyNameObj=null;
    String classPrettyName;
    if (concept != null) {
      if (concept.hasOwnSlot(prettyNameSlot)) {
        classPrettyNameObj = concept.getOwnSlotValue(prettyNameSlot);
        if (classPrettyNameObj != null)
          classPrettyName = (String)classPrettyNameObj;
        else classPrettyName = concept.getName();
      } else classPrettyName = concept.getName();
    } else throw new Exception("null concept in prettyName method");
    return classPrettyName;

  }

  public Collection findInstances(Cls currentClass, AbstractWhereFilter where)
    throws KnowledgeBaseException {
	  return findInstances( currentClass, where, null);
  }
  
  public Collection findInstances(String className, AbstractWhereFilter where)
    throws  KnowledgeBaseException {
	  return findInstances( className, where, null);
  }

  public Collection findInstances(Cls currentClass, AbstractWhereFilter where,
		  GuidelineInterpreter gi)
    throws KnowledgeBaseException {

    ArrayList selectedInstances = new ArrayList();

    Collection instances = null;
    if (currentClass != null) {
      instances = currentClass.getInstances();
      logger.debug("number of instances of class " + currentClass.getName() +"= "+ instances.size());;
      if (where != null) {
        for (Iterator instanceIterator = instances.iterator();
                          instanceIterator.hasNext();) {
          Object next = instanceIterator.next();
          try {
            if (where.evaluate((Instance) next, gi) == true) {
              
              selectedInstances.add(next);
            }
          } catch (KnowledgeBaseException e) {
            throw e;
          }
        }
        logger.debug("number of instances found " + currentClass.getName() + selectedInstances.size());
        return selectedInstances ;
      } else return instances;
    } else throw new KnowledgeBaseException("Null currentClass (Cls) in findInstances");

  }

  public Collection findInstances(String className, AbstractWhereFilter where,
		  GuidelineInterpreter gi)
    throws  KnowledgeBaseException {

    Cls currentClass = getCls(className);
    if (currentClass == null) {
    	logger.error("findInstances - "+
    			className+" not found");
    	throw new KnowledgeBaseException("findInstances - "+
    			className+" not found");
    }
    else return findInstances(currentClass, where, gi);
  }

  public boolean hasNamedType(Instance instance, String typeName) {
    Cls typeCls;
    if (instance == null) return false;
    boolean subclassp = false;
    if ((typeCls = kb.getCls(typeName)) != null) {
      subclassp = typeCls.getSubclasses().contains(instance.getDirectType());
      /*logger.debug("hasNamedType - typeName = " + typeName+ " Cls= "+typeCls +
        " instance type=" + instance.getDirectType()+ " "+instance.hasType(typeCls) +
        ", "+(subclassp || instance.getDirectType().equals(typeCls)), 2); */

      return (subclassp || instance.hasDirectType(typeCls));
      //return instance.hasType(typeCls);
    } else return false;
  }

  public int compareOrdinal(Cls o1, Cls o2) throws KnowledgeBaseException {
    logger.debug("KBHandler compareOrdinal: o1 has type "+o1.getDirectType().getName());
    logger.debug("KBHandler compareOrdinal: o2 has type "+o2.getDirectType().getName());
    if (!(o1.getDirectType().getName().equals("Ordered_List_Value")) ||
        !(o2.getDirectType().getName().equals("Ordered_List_Value")))
      throw new KnowledgeBaseException(o1.getName()+" or "+o2.getName()+" is not ordered");
    if (o1 == o2) return 0;
    else if (ordinalGreaterThan(o1, o2)) return 1;
         else if (ordinalGreaterThan(o2, o1)) return -1;
              else throw new KnowledgeBaseException(o1.getName()+" and "+
                o2.getName()+" are not comparable");
  }

  private boolean ordinalGreaterThan(Cls o1, Cls o2)
    throws KnowledgeBaseException {
    // o1 is greater than o2 if traversing the "next" slot of o2 ends up in o1
    // has to check for loop
    Collection hasSeen = new ArrayList();
    Slot next = kb.getSlot("next");
    boolean o1_gt_o2 = false;
    while (o2.getOwnSlotValue(next) != null) {
      if (o1 == o2.getOwnSlotValue(next)) {
        o1_gt_o2 = true;
        break;
      }
      else {
        o2 = (Cls)o2.getOwnSlotValue(next);
        if (!o2.hasType(kb.getCls("Ordered_List_Value")))
          throw new KnowledgeBaseException(o2.getName()+" is not ordered");
      }
    }
    return o1_gt_o2;
  }

  public Instance instantiate(Guideline_Entity ge) {
    return  kb.getInstance(ge.entity_id);
  }



}
