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
 *  Find instance of qualifying_object_type where value of qualifying_slot equals value of joining_slot. Then get value of attribute in that instance.
 */
public class Joined_Attribute_Query extends Expression {

	public Joined_Attribute_Query(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
  static Logger logger = Logger.getLogger(Joined_Attribute_Query.class);

	public void setjoining_slotValue(Instance joining_slot) {
		ModelUtilities.setOwnSlotValue(this, "joining_slot", joining_slot);	}
	public Instance getjoining_slotValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "joining_slot"));
	}

	public void setattributeValue(Instance attribute) {
		ModelUtilities.setOwnSlotValue(this, "attribute", attribute);	}
	public Instance getattributeValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "attribute"));
	}

	public void setqualifying_object_typeValue(Cls qualifying_object_type) {
		ModelUtilities.setOwnSlotValue(this, "qualifying_object_type", qualifying_object_type);	}
	public Cls getqualifying_object_typeValue() {
		return ((Cls) ModelUtilities.getOwnSlotValue(this, "qualifying_object_type"));
	}

	public void setqualifying_slotValue(Instance qualifying_slot) {
		ModelUtilities.setOwnSlotValue(this, "qualifying_slot", qualifying_slot);	}
	public Instance getqualifying_slotValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "qualifying_slot"));
	}
// __Code above is automatically generated. Do not change

  public Expression evaluate_expression(GuidelineInterpreter glmanager,
    String join_value)
      throws PCA_Session_Exception {
    Qualitative_Constant qc=null;
    WhereComparisonFilter where = new WhereComparisonFilter(
      ((Instance)getqualifying_slotValue()).getName(), "eq", join_value);
    Collection instances =glmanager.getKBmanager().findInstances(
      getqualifying_object_typeValue(), where, glmanager);
    // find instances of qualifying object type (e.g., Guideline Drug)
    // whose qualifying slot value (e.g., generic_drug) equals join value
    // (forall (Guideline_Drug ?D)(D.generic_drug eq join_value)
    if ((instances != null) && (!instances.isEmpty())) {
      if (instances.size()>1) {
        throw new PCA_Session_Exception("Ambiguous Guideline_Drug instance "+
          " more than one matches "+ join_value);
      } else {
        Instance qualifyObj = (Instance)instances.iterator().next();
        Object obj = (Object) qualifyObj.getOwnSlotValue((Slot)getattributeValue());
        if (obj instanceof String) {
          qc = (Qualitative_Constant)glmanager.getDBmanager().createInstance("Qualitative_Constant");
          qc.setvalueValue((String)obj);
        } else if (obj instanceof Cls) {
            qc = (Qualitative_Constant)glmanager.getDBmanager().createInstance("Qualitative_Constant");
            qc.setvalueValue(((Cls)obj).getName());
        }
      }
    }
    if (qc != null)
      logger.debug("in joined attribute query evaluate, returning Qualitative Constant " +qc.getvalueValue());
    else
      logger.debug("in joined attribute query evaluate, returning null" );
    return qc;
  }
}
