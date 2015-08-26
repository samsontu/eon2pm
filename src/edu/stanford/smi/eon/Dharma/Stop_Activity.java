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

// Created on Mon Sep 17 13:27:24 PDT 2001
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
public class Stop_Activity extends Activity_Act {

	public Stop_Activity(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
	// __Code above is automatically generated. Do not change


	public void setactivity_to_stopValue(Instance activity_to_stop) {
		ModelUtilities.setOwnSlotValue(this, "activity_to_stop", activity_to_stop);	}
	public Instance getactivity_to_stopValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "activity_to_stop"));
	}


	/*
public Action_Spec_Record(
java.lang.String name,  //label
java.lang.String text,  //description
java.lang.String action_spec_class,
int fine_grain_priority,
edu.stanford.smi.eon.PCAServerModule.Guideline_Entity action_spec,
edu.stanford.smi.eon.PCAServerModule.Justification justification,
java.lang.String level_of_evidence,
java.lang.String net_benefit,
java.lang.String  overall_quality_of_evidence,
java.lang.String strength_of_recommendation,
Collection<String> references,
Collection<String> subsidiary_message
) {
	 */

/*	public Action_Spec_Record  evaluateActionSpec(GuidelineInterpreter gmanager) {
		Collection<Instance> references = getreferencesValue();
		Collection<String> urls = new ArrayList();
		for (Instance reference : references) 
			urls.addAll(reference.getOwnSlotValues(this.getKnowledgeBase().getSlot("URL")));
		Action_Spec_Record actionSpec = new edu.stanford.smi.eon.PCAServerModule.Action_Spec_Record(
				getlabelValue(),
				getdescriptionValue(), "Stop_Activity",
				getfine_grain_priorityValue(),
				this.makeGuideline_Entity(gmanager.getCurrentGuidelineID()),
				HelperFunctions.dummyJustification(),
				(getlevel_of_evidenceValue() != null) ? getlevel_of_evidenceValue().getBrowserText() : null, 
						(getnet_benefitValue() != null) ? getnet_benefitValue().getBrowserText() : null, 
								(getquality_of_evidenceValue() != null) ? getquality_of_evidenceValue().getBrowserText() : null,
										(getstrength_of_recommendationValue() != null) ? getstrength_of_recommendationValue().getBrowserText(): null, 
												urls,
												(getsubsidiary_messageValue() != null) ?getsubsidiary_messageValue(): null
				);
	}
*/
	
/* This is the toplevel execution method */
	public void doAction(
			Action_To_Choose action,
			Guideline_Action_Choices currentDecision,
			GuidelineInterpreter interpreter){

		Collection<Choice_Evaluation> stopEvaluations = new ArrayList<Choice_Evaluation>();
		Guideline_Activity_Evaluations deleteEvaluations;
		if (getrule_in_conditionValue() != null) {
			Criteria_Evaluation evaluation = HelperFunctions.dummyCriteriaEvaluation();
			try {
				evaluation = (Criteria_Evaluation) ((Criterion)getrule_in_conditionValue()).evaluate(interpreter, false);
				if (!(PCAInterfaceUtil.mapTruthValue(evaluation.truth_value)))  {   // rule-in condition does not hold
					return ;
				}
			} catch (PCA_Session_Exception e) {
				e.printStackTrace();
				return ;
			}
		}

		try {
			stopEvaluations = currentActivitiesToStop( interpreter);
		} catch (PCA_Session_Exception e) {
			e.printStackTrace();
		}
		if (stopEvaluations.size ()> 0) {
			deleteEvaluations =
					new Guideline_Activity_Evaluations(
							this.makeGuideline_Entity(interpreter.getCurrentGuidelineID()),
							Evaluation_Type.delete,
							(Choice_Evaluation[])stopEvaluations.toArray(new Choice_Evaluation[0]),
							interpreter.getCurrentGuidelineID());
			interpreter.addEvaluatedChoice(deleteEvaluations);
		}
	}
	
	/*
	 *       Choice_Evaluation choiceEvaluation = new Choice_Evaluation();
      try {
        Add_Evaluation addEval =  activity.evaluateAdd(interpreter);
        if (addEval != null) {
          choiceEvaluation.add_eval(addEval);
          addEvaluations.add(choiceEvaluation);
        }
        logger.debug(activity.getlabelValue()+" got evaluated");
      } catch (Exception e) {
        logger.error(activity.getlabelValue()+" cannot be evaluated; "+e.getClass());
        e.printStackTrace();
      }

	 */

	private Collection<Choice_Evaluation> currentActivitiesToStop(GuidelineInterpreter guidelineManager)
			throws PCA_Session_Exception {
		// returns a collection of Delete_Evaluations
		logger.debug("Stop_Activity.evaluateActivitiesToDelete ");
		Collection<String> activitiesToStopStrings = null;
		Collection<Choice_Evaluation> currentActivitiesToStop=new ArrayList<Choice_Evaluation>();
		Delete_Evaluation delEval=null;

		//first find current activities to stop (as strings)
		Instance query = getactivity_to_stopValue();
		if (query == null) {
			logger.error("Stop_Activity.evaluateActivitiesToDelete: Null activity_to_stop slot in "+this.getBrowserText());
			throw new PCA_Session_Exception("Null activity_to_stop slot in "+this.getName());
		} else {
			if (query instanceof PAL_Query)
				activitiesToStopStrings = ((PAL_Query)query).doQuery(guidelineManager);
			else if (query instanceof Structured_Query)
				activitiesToStopStrings = ((Set_Expression)((Structured_Query)query).evaluate_expression(guidelineManager)).getset_elementsValue();
			if (activitiesToStopStrings == null)  {
				logger.error("Stop_Activity.evaluateActivitiesToDelete: No activities to stop");
				throw new PCA_Session_Exception("Evaluate_Substitution_Activity.evaluateActivitiesToDelete: No activities to stop");
			} 
		}
		for (String activityToStop : activitiesToStopStrings) {
			delEval = HelperFunctions.dummyDeleteEvaluation(activityToStop);
			delEval.preference = Preference.preferred;
			Choice_Evaluation choiceEvaluation = new Choice_Evaluation();
			choiceEvaluation.delete_eval(delEval);
			currentActivitiesToStop.add(choiceEvaluation);
		}
		return currentActivitiesToStop;
	}
}


