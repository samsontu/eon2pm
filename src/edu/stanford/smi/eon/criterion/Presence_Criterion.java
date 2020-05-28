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

// Created on Wed Jun 20 12:58:01 PDT 2001
// Copyright Stanford University 2000

package edu.stanford.smi.eon.criterion;

import java.util.*;
import java.io.*;
import java.text.NumberFormat;
import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.util.*;
import edu.stanford.smi.eon.util.*;
import edu.stanford.smi.eon.guidelineinterpreter.*;
import edu.stanford.smi.eon.PCAServerModule.*;
import edu.stanford.smi.eon.kbhandler.*;
import edu.stanford.smi.eon.datahandler.*;
import edu.stanford.smi.eon.time.*;
import edu.stanford.smi.protegex.pal.engine.*; 
import edu.stanford.smi.protegex.pal.parser.*;
import edu.stanford.smi.protegex.pal.relations.*;
import edu.stanford.smi.protegex.pal.*;
import org.apache.log4j.*;
/** 
 */
public class Presence_Criterion extends Criterion {

	public Presence_Criterion(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
  static  Logger  logger = Logger.getLogger(Presence_Criterion.class);


	public void setmoodValue(Cls mood) {
		ModelUtilities.setOwnSlotValue(this, "mood", mood);	}
	public Cls getmoodValue() {
		return ((Cls) ModelUtilities.getOwnSlotValue(this, "mood"));
	}


	public void setperiodValue(Instance period) {
		ModelUtilities.setOwnSlotValue(this, "period", period);	}
	public Instance getperiodValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "period"));
	}

	public void setpresenceValue(boolean presence) {
		ModelUtilities.setOwnSlotValue(this, "presence", new  Boolean(presence));	}
	public boolean ispresenceValue() {
		if (ModelUtilities.getOwnSlotValue(this, "presence") == null) return false;
		else 
		return ((Boolean) ModelUtilities.getOwnSlotValue(this, "presence")).booleanValue();
	}

	public void setdomain_termValue(Cls domain_term) {
		ModelUtilities.setOwnSlotValue(this, "domain_term", domain_term);	}
	public Cls getdomain_termValue() {
		return ((Cls) ModelUtilities.getOwnSlotValue(this, "domain_term"));
	}

	public void setentry_typeValue(Cls entry_type) {
		ModelUtilities.setOwnSlotValue(this, "entry_type", entry_type);	}
	public Cls getentry_typeValue() {
		return ((Cls) ModelUtilities.getOwnSlotValue(this, "entry_type"));
	}
public Criteria_Evaluation ownEvaluate(GuidelineInterpreter guidelineManager, boolean doAll)
    throws PCA_Session_Exception {

    // Try query first. If none found, then check if there is a definition.
    boolean presence = false;
    Collection<Criterion> definition = null;
    Criteria_Evaluation evaluation, criteriaEvaluation = null;
    Criteria_Evaluation[] evaluationArray = new Criteria_Evaluation[1];
    logger.debug("Presence_Criterion ownEvaluate - "+ getlabelValue()+ "construct presence query");

    Presence_Query criterionQuery = (Presence_Query) guidelineManager.getDBmanager().createRegisteredInstance("Presence_Query");
    Cls domainTerm = getdomain_termValue();
    if (getentry_typeValue() == null) {
      presence = false;
    } else {
      criterionQuery.setentry_typeValue(getentry_typeValue());
      criterionQuery.setperiodValue(getperiodValue());
      criterionQuery.setmoodValue(getmoodValue());
      if (domainTerm != null) {
        criterionQuery.setqualitative_domain_termValue(domainTerm.getName());
      } else  criterionQuery.setqualitative_domain_termValue(null);
      presence = (Boolean.valueOf(
        criterionQuery.doQuery(guidelineManager)).booleanValue());
      logger.debug("Presence_Criterion ownEvaluate - "+ getlabelValue()+ " **doQuery returns: " +
        presence+ "** presence/absence criterion "+ispresenceValue());
    }
    guidelineManager.getDBmanager().deleteInstance(criterionQuery);
    //
    // Backward chain if criterion is available   ** filter not being used!!?
    if ((presence == false)&& (domainTerm != null)) {
    	if (domainTerm.hasOwnSlot((Slot)this.getKnowledgeBase().getFrame("DiagnosticCriteria"))) {
    		definition = ModelUtilities.getOwnSlotValues(domainTerm,"DiagnosticCriteria");
    		if (definition != null) {
    			for (Criterion def : definition) {
    				criteriaEvaluation = def.evaluate(guidelineManager, doAll);
    				if (criteriaEvaluation !=null) {
    					if (criteriaEvaluation.truth_value.equals(Truth_Value._true)) {
    						break;
    					}
    				}
    			}
    			// if unknown, assume absent
    			if (criteriaEvaluation.truth_value.equals(Truth_Value.unknown))
    				criteriaEvaluation.truth_value = Truth_Value._false;
    			evaluationArray[0] = criteriaEvaluation;
    			evaluation = new Criteria_Evaluation(Logical_Operator.ATOMIC,
    					(ispresenceValue()) ? criteriaEvaluation.truth_value :
    						N_ary_Criterion.threeValueNot(criteriaEvaluation.truth_value),
    						evaluationArray,
    						this.makeGuideline_Entity(),
    						criteriaEvaluation.support);
    			return evaluation;
    		}
    	}
    }
    evaluation = new Criteria_Evaluation(Logical_Operator.ATOMIC,
      (ispresenceValue()) ? ((presence) ? Truth_Value._true : Truth_Value._false)
         : ((!presence) ? Truth_Value._true : Truth_Value._false),
      new Criteria_Evaluation[0],
      this.makeGuideline_Entity(),
      null);
    return evaluation;
  }

}
