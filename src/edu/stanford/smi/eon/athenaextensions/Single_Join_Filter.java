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

// Created on Tue Dec 12 11:31:16 PST 2000
// Copyright Stanford University 2000

package edu.stanford.smi.eon.athenaextensions;

import java.util.*;
import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.util.*;
import edu.stanford.smi.eon.util.*;
import edu.stanford.smi.eon.guidelineinterpreter.*;
import edu.stanford.smi.eon.PCAServerModule.*;
import edu.stanford.smi.eon.kbhandler.*;
import edu.stanford.smi.eon.datahandler.*;
import edu.stanford.smi.eon.time.*;
import edu.stanford.smi.eon.criterion.*;
import edu.stanford.smi.eon.Dharma.*;
import org.apache.log4j.*;
/** 
 */
public class Single_Join_Filter extends Filter {

	public Single_Join_Filter(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
  static Logger logger = Logger.getLogger(Single_Join_Filter.class);

	public void setjoining_slotValue(Instance joining_slot) {
		ModelUtilities.setOwnSlotValue(this, "joining_slot", joining_slot);	}
	public Instance getjoining_slotValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "joining_slot"));
	}

	public void setqualifying_object_typeValue(Cls qualifying_object_type) {
		ModelUtilities.setOwnSlotValue(this, "qualifying_object_type", qualifying_object_type);	}
	public Cls getqualifying_object_typeValue() {
		return ((Cls) ModelUtilities.getOwnSlotValue(this, "qualifying_object_type"));
	}

	public void setoperatorValue(String operator) {
		ModelUtilities.setOwnSlotValue(this, "operator", operator);	}
	public String getoperatorValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "operator"));
	}

	public void setqualifying_slotValue(Instance qualifying_slot) {
		ModelUtilities.setOwnSlotValue(this, "qualifying_slot", qualifying_slot);	}
	public Instance getqualifying_slotValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "qualifying_slot"));
	}
// __Code above is automatically generated. Do not change

  public Collection  evaluate(Matched_Data matches, GuidelineInterpreter glmanager)

    throws PCA_Session_Exception {

    logger.debug("Single_Join_Filter: entering with matches "+matches.toString()
      );

    Collection returnedMatches = new ArrayList();

    if (matches == null) return null;
    else {
      Collection qualifyObjects = glmanager.getKBmanager().findInstances(
        getqualifying_object_typeValue(), null, glmanager);

      for (Iterator rhs=qualifyObjects.iterator(); rhs.hasNext();) {
        Instance value = (Instance) rhs.next();
        String symptom = (String)value.getOwnSlotValue((Slot)getjoining_slotValue());
        Object slotvalue = value.getOwnSlotValue((Slot)getqualifying_slotValue());
        logger.debug("Single_Join_Filter evaluate: qualifyObject "+ slotvalue +
          "symptom="+symptom+" length of matches.data = "+ matches.data.length);
        for (int lhs = 0; lhs < matches.data.length; lhs++) {
          switch (DharmaPaddaConstants.convertCompareToInt(getoperatorValue())) {
            case DharmaPaddaConstants.equal:
              break;
            case DharmaPaddaConstants.greater_than:
              break;
            case DharmaPaddaConstants.greater_than_or_equal:
              break;
            case DharmaPaddaConstants.kind_of:
              Cls pClass;
              if (slotvalue instanceof Cls)
                pClass = (Cls) slotvalue;
              else {
                pClass = getKnowledgeBase().getCls((String)slotvalue);
                if (pClass == null) {
                  logger.error("Single_Join_Filter.evaluate: "+ (String)slotvalue+
                    " is not the name of a class. Can't check whether it is a superclass of "+
                    "patient data");
                  return null;
                }
              }
              Cls lhsCls  = getKnowledgeBase().getCls(matches.data[lhs]);
              if  (lhsCls != null)  {
                if ((lhsCls.hasSuperclass(pClass)) ||  (lhsCls==pClass)) {
                    logger.debug("Single_Join_Filter evaluate: "+ matches.data[lhs] +
                    " is a subclass of "+ slotvalue);
                    String[] symptoms = new String[1];
                    symptoms[0] = symptom;
                    Matched_Data resultMatch= new Matched_Data("", pClass.getName(), symptoms);
                    returnedMatches.add(resultMatch);
                } else logger.debug("Single_Join_Filter evaluate1: "+ matches.data[lhs] +
                  " is NOT a subclass of "+ slotvalue);
              } else  logger.error("Exception Single_Join_Filter evaluate2: "+ matches.data[lhs] +
                  " is NOT a subclass of "+ slotvalue);
              break;
            case DharmaPaddaConstants.less_than:
              break;
            case DharmaPaddaConstants.less_than_or_equal:
              break;
            case DharmaPaddaConstants.not_equal:
              break;
            case DharmaPaddaConstants.numeric_equal:
              break;
            case DharmaPaddaConstants.numeric_nequal:
              break;
            default:
              throw new PCA_Session_Exception("Unknown operator "+ getoperatorValue());
          }
        }
      }
      if (returnedMatches.isEmpty()) return null;
      else return returnedMatches;
    }
  }
}
