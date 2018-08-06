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

import org.apache.log4j.*;

/** 
 */
public class Evaluate_Stop_Activity extends Evaluate_Activity_Act {

	public Evaluate_Stop_Activity(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
	static Logger logger = Logger.getLogger(Evaluate_Stop_Activity.class);

	public void setactivity_to_stopValue(Instance activity_to_stop) {
		ModelUtilities.setOwnSlotValue(this, "activity_to_stop", activity_to_stop);	}
	public Instance getactivity_to_stopValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "activity_to_stop"));
	}

	public void setdomain_termValue(Cls domain_term) {
		ModelUtilities.setOwnSlotValue(this, "domain_term", domain_term);	}
	public Cls getdomain_termValue() {
		return ((Cls) ModelUtilities.getOwnSlotValue(this, "domain_term"));
	}

	public void setactivity_spec_keyValue(Instance activity_spec_key) {
		ModelUtilities.setOwnSlotValue(this, "activity_spec_key", activity_spec_key);	}
	public Instance getactivity_spec_keyValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "activity_spec_key"));
	}

	public void setactivity_specValue(Cls activity_spec) {
		ModelUtilities.setOwnSlotValue(this, "activity_spec", activity_spec);	}
	public Cls getactivity_specValue() {
		return ((Cls) ModelUtilities.getOwnSlotValue(this, "activity_spec"));
	}
	// __Code above is automatically generated. Do not change

	public void doAction(
			Action_To_Choose action,
			Guideline_Action_Choices currentDecision,
			GuidelineInterpreter interpreter){
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
		String activityClass = (String) getactivity_classValue();
		Cls activitySpecClass = (Cls) getactivity_specValue();
		Slot activitySpecKey = (Slot)getactivity_spec_keyValue();
		WhereComparisonFilter compare = null;

		Collection currentActivities = null;
		// expect activityClass to be a string like "Authorized_Medication"
		if (activityClass !=null) {
			currentActivities = interpreter.getDBmanager().currentActivities(
					activityClass, null);
		} else {
			logger.error("No value for activity_class in "+getlabelValue());
			return;
		}
		if (activitySpecKey != null)
			compare = new WhereComparisonFilter(activitySpecKey.getName(),
					DharmaPaddaConstants.superclass_of, null);
		else {
			logger.error("No value for activity_spec_key in "+getlabelValue());
			return;
		}
		if (activitySpecClass == null) {
			logger.error("No value for activity_spec in "+getlabelValue());
			return;
		}

		Collection activitySpec =null;
		Collection stopEvaluations = new ArrayList(); //each entry is an instance of Delete_Evaluation
		Guideline_Activity_Evaluations deleteEvaluations;
		Delete_Evaluation delEval = null;
		String currentActivity=null;
		Matched_Data qualifiedActivities = null;

		Instance activityToStopQuery = getactivity_to_stopValue();
		if (activityToStopQuery != null) {
			try {
				qualifiedActivities = evaluateActivitiesToDelete(interpreter);
			} catch (Exception e) {
				logger.error("Problem evaluating activities to delete", e);

			}
		} else 	if (getdomain_termValue() != null) {
			qualifiedActivities = interpreter.containSubclassOf(activityClass,
					getdomain_termValue().getName(),currentActivities);
		} else logger.error("Domain term of "+this.getBrowserText()+ " instance is empty!");

		if (qualifiedActivities == null) {
			return;
		}

		if (qualifiedActivities.data.length > 0) {
			// find all instances of activity_spec that subsume current activity, and
			// construct Delete_Evaluation records
			for (int i = 0; i < qualifiedActivities.data.length ;i++) {
				Choice_Evaluation choiceEvaluation = new Choice_Evaluation();
				delEval = null;
				currentActivity = qualifiedActivities.data[i];
				//compare.value = interpreter.kbmanager.getCls(currentActivity);
				compare.value = currentActivity;
				logger.debug("Evaluate_Stop_Activity.doAction "+compare.value);
				activitySpec = interpreter.getKBmanager().findInstances(activitySpecClass, compare, interpreter);
				Drug_Usage evaluateObject = Evaluate_Substitution_Activity.getDrugUsage(currentActivity, activitySpec, interpreter);
				if (evaluateObject != null)
					try {
						delEval = evaluateObject.evaluateDelete(interpreter, delEval, currentActivity, this.getfine_grain_priorityValue());
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
				
				if (delEval!= null) {
					if ((delEval.contraindications != null) &&
							(delEval.contraindications.length > 0)) {
						choiceEvaluation.delete_eval(delEval);
						stopEvaluations.add(choiceEvaluation);
					}
				}
			}
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


	Collection<Drug_Usage> getGuidelineDrugUsages(GuidelineInterpreter interpreter) {
		return interpreter.guideline.getOwnSlotValues(interpreter.getKBmanager().getKB().getSlot(DharmaPaddaConstants.drugUsagesPropertyName));
	}

	private Matched_Data evaluateActivitiesToDelete(GuidelineInterpreter guidelineManager)
			throws PCA_Session_Exception {
		logger.debug("Evaluate_Substitution_Activity.evaluateActivitiesToDelete ");
		Collection<String> activitiesToStopCollection = null;
		Matched_Data activitiesToStopData = null;
		Instance query = getactivity_to_stopValue();
		if (query == null) {
			logger.warn("Null activity_to_stop slot in "+this.getBrowserText());
			return null;
		} else {
			logger.debug("Evaluate_Substitution_Activity.evaluateActivitiesToDelete: consider deleting "
					+ activitiesToStopCollection);
			if (query instanceof PAL_Query)
				activitiesToStopCollection = ((PAL_Query)query).doQuery(guidelineManager);
			else if (query instanceof Structured_Query)
				activitiesToStopCollection = ((Set_Expression)((Structured_Query)query).evaluate_expression(guidelineManager)).getset_elementsValue();
			if (activitiesToStopCollection == null)  {
				logger.warn("No activity to stop after evaluating "+query.getBrowserText());
				return null;
			} else {
				// remove duplicates
				Set<String> activitiesToStopSet = new HashSet<String>(activitiesToStopCollection);
				String[] activitiesToStop= (String[]) activitiesToStopSet.toArray( new String[0]);
				activitiesToStopData = new Matched_Data("", "", activitiesToStop);
				return activitiesToStopData;
			}
		}
	}

}
