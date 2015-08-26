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

// Created on Mon Sep 17 13:27:25 PDT 2001
// Copyright Stanford University 2000

package edu.stanford.smi.eon.Dharma;

import java.util.*;
import java.io.PrintWriter;

import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.util.*;
import edu.stanford.smi.eon.util.*;
import edu.stanford.smi.eon.guidelineinterpreter.*;
import edu.stanford.smi.eon.PCAServerModule.*;
import edu.stanford.smi.eon.kbhandler.*;
import edu.stanford.smi.eon.datahandler.*;
import edu.stanford.smi.eon.time.*;
import edu.stanford.smi.eon.criterion.*;
import edu.stanford.smi.protegex.pal.engine.*; 
import edu.stanford.smi.protegex.pal.parser.*;
import edu.stanford.smi.protegex.pal.relations.*;
import edu.stanford.smi.protegex.pal.language.*;

/** 
 */
public class Referral extends Action_Specification {

	public Referral(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
	public void setwhenValue(Instance when) {
		ModelUtilities.setOwnSlotValue(this, "when", when);	}
	public Instance getwhenValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "when"));
	}

	public void setwho_toValue(Instance who_to) {
		ModelUtilities.setOwnSlotValue(this, "who_to", who_to);	}
	public Instance getwho_toValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "who_to"));
	}
// __Code above is automatically generated. Do not change

	public Action_Spec_Record  evaluateActionSpec(GuidelineInterpreter gmanager) {
		if (getrule_in_conditionValue() != null) {
			Criteria_Evaluation evaluation = HelperFunctions.dummyCriteriaEvaluation();
			try {
				evaluation = (Criteria_Evaluation) ((Criterion)getrule_in_conditionValue()).evaluate(gmanager, false);
				if (!(PCAInterfaceUtil.mapTruthValue(evaluation.truth_value)))  {   // rule-in condition does not hold
					return null;
				}
			} catch (PCA_Session_Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	    Collection<Instance> references = getreferencesValue();
	    Collection<String> urls = new ArrayList();
	    for (Instance reference : references) 
	    	urls.addAll(reference.getOwnSlotValues(this.getKnowledgeBase().getSlot("URL")));
		Action_Spec_Record actionSpec = new edu.stanford.smi.eon.PCAServerModule.Referral(
				getlabelValue(),
				getdescriptionValue(), "Referral",
				getfine_grain_priorityValue(),
				this.makeGuideline_Entity(gmanager.getCurrentGuidelineID()),
				HelperFunctions.dummyJustification(),
				(getlevel_of_evidenceValue() != null) ? getlevel_of_evidenceValue().getBrowserText() : null, 
				(getnet_benefitValue() != null) ? getnet_benefitValue().getBrowserText() : null, 
				(getquality_of_evidenceValue() != null) ? getquality_of_evidenceValue().getBrowserText() : null,
				(getstrength_of_recommendationValue() != null) ? getstrength_of_recommendationValue().getBrowserText(): null, 
				urls,
				(getsubsidiary_messageValue() != null) ?getsubsidiary_messageValue(): null,
				(getwho_toValue() != null) ? getwho_toValue().getBrowserText() : null,
				getWhen(getwhenValue()) ,
				getwhen_lower_boundValue(),
				getwhen_upper_boundValue(),
				getwhen_lower_bound_unitValue(),
				getwhen_upper_bound_unitValue(),
				getCollateralActions(gmanager)
				);
/* Referral (		    java.lang.String name,
			    java.lang.String text,
			    java.lang.String action_spec_class,
			    int fine_grain_priority,
			    edu.stanford.smi.eon.PCAServerModule.Guideline_Entity action_spec,
			    edu.stanford.smi.eon.PCAServerModule.Justification justification,
			    java.lang.String level_of_evidence,
			    java.lang.String net_benefit,
			    java.lang.String quality_of_evidence,
			    java.lang.String strength_of_recommendation,
			    Collection<String> references,
			    java.lang.String who_to,
			    String when,
			    int when_lower_bound,
			    int when_upper_bound,
			    String when_lower_bound_unit,
			    String when_upper_bound_unit

*/
		return actionSpec;
	}

private String getwhen_upper_bound_unitValue() {
	return ((String) ModelUtilities.getOwnSlotValue(this, "when_upper_bound_unit"));
	}

private String getwhen_lower_bound_unitValue() {
	return ((String) ModelUtilities.getOwnSlotValue(this, "when_lower_bound_unit"));
	}

private int getwhen_upper_boundValue() {
	if ((Integer) ModelUtilities.getOwnSlotValue(this, "when_upper_bound") != null)
		return ((Integer) ModelUtilities.getOwnSlotValue(this, "when_upper_bound")).intValue();
	else 
		return 0;
	}

private int getwhen_lower_boundValue() {
	if ((Integer) ModelUtilities.getOwnSlotValue(this, "when_lower_bound") != null)
		return ((Integer) ModelUtilities.getOwnSlotValue(this, "when_lower_bound")).intValue();
	else 
		return 0;
	}

private String getWhen(Instance getwhenValue) {
		// TODO Auto-generated method stub
		return null;
	}

private String gettest_or_procedureValue() {
	return ((String) ModelUtilities.getOwnSlotValue(this, "test_or_procedure"));
	}


private String getcoding_systemValue() {
	return ((String) ModelUtilities.getOwnSlotValue(this, "coding_system"));
	}


private String getcodeValue() {
	return ((String) ModelUtilities.getOwnSlotValue(this, "code"));
	}


}
