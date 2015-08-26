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

import org.apache.log4j.*;

/** 
 */
public class Substitute_Activity extends Activity_Act {

	public Substitute_Activity(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
	static Logger logger = Logger.getLogger(Substitute_Activity.class);

	public void setactivity_to_stopValue(Instance activity_to_stop) {
		ModelUtilities.setOwnSlotValue(this, "activity_to_stop", activity_to_stop);	}
	public Instance getactivity_to_stopValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "activity_to_stop"));
	}
	public void setactivityValue(Instance activity) {
		ModelUtilities.setOwnSlotValue(this, "activity", activity);	}
	public Instance getactivityValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "activity"));
	}
	// __Code above is automatically generated. Do not change
	private int debugLevel = 4;

	private Collection addEvaluations(GuidelineInterpreter interpreter) {
		// returns a collection of Add_Evaluations
		List addEvaluations = new ArrayList(); 
		Object activityObj= getactivityValue();
		if (activityObj != null) {
			Drug_Usage activity = (Drug_Usage) activityObj;

			// addEvaluations is a collection of Choice_Evaluation instances
			try {
				Add_Evaluation addEval =  activity.addActivity(interpreter,  this.getfine_grain_priorityValue());
				if (addEval != null) {
					if (this.getjustificationValue() != null)
						addEval.description =  this.getjustificationValue();
					activity.addMedicationInstance(interpreter, addEval, "Recommend_Add");
					addEval.preference = Preference.preferred;
					addEvaluations.add(addEval);
				}
				logger.debug("Substitute_Activity.addEvaluations: "+activity.getlabelValue()+" got added");
			} catch (Exception e) {
				logger.debug(activity.getlabelValue()+" cannot be evaluated; "+e.getClass());
				e.printStackTrace();
			}

		} else logger.debug("Substitute_Activity.addEvaluations: null activity to add");
		return addEvaluations;
	}

	private Collection currentActivitiesToStop(GuidelineInterpreter guidelineManager)
			throws PCA_Session_Exception {
		// returns a collection of Delete_Evaluations
		logger.debug("Substitution_Activity.evaluateActivitiesToDelete ");
		Collection activitiesToStopStrings = null;
		Collection activitiesToStop = null;
		Collection currentActivitiesToStop=new ArrayList();
		Delete_Evaluation delEval=null;

		//first find current activities to stop (as strings)
		Instance query = getactivity_to_stopValue();
		if (query == null) {
			throw new PCA_Session_Exception("Null activity_to_stop slot in "+this.getName());
		} else {
			if (query instanceof PAL_Query)
				activitiesToStopStrings = ((PAL_Query)query).doQuery(guidelineManager);
			else if (query instanceof Structured_Query)
				activitiesToStopStrings = ((Set_Expression)((Structured_Query)query).evaluate_expression(guidelineManager)).getset_elementsValue();
			if (activitiesToStopStrings == null)  {
				throw new PCA_Session_Exception("Evaluate_Substitution_Activity.evaluateActivitiesToDelete: No activities to stop");
			} else {

				logger.debug("Substitution_Activity.evaluateActivitiesToDelete: consider deleting "
						+ activitiesToStopStrings);

				// next find Drug classes so as to evaluate indications and contraindications
				WhereComparisonFilter compare =  new WhereComparisonFilter("Drug_Class_Name",
						DharmaPaddaConstants.superclass_of, null);
				Collection specForCurrentActivity= null;
				if ((activitiesToStopStrings != null) && (activitiesToStopStrings.size() > 0)) {
					for (Iterator i = activitiesToStopStrings.iterator();i.hasNext();) {  //for i
						String currentActivity = (String)i.next();
						compare.value = currentActivity;
						specForCurrentActivity = guidelineManager.getKBmanager().findInstances("Drug_Usage",
								compare, guidelineManager);
						logger.debug("Substitute_Activity.doAction "+compare.value+
								" specForCurrentActivities size:"+specForCurrentActivity.size());
						if ((specForCurrentActivity != null) && (specForCurrentActivity.size()>0)) {
							for (Iterator j=specForCurrentActivity.iterator(); j.hasNext();) {
								try {
									Drug_Usage evaluateObject = (Drug_Usage) j.next();
									logger.debug("Substitute_Activity.currentActivitiesToStop "+evaluateObject.getlabelValue());
									delEval = evaluateObject.evaluateDelete(guidelineManager, null, currentActivity, this.getfine_grain_priorityValue());

									logger.debug("Substitute_Activity.currentActivitiesToStop subEval "+ delEval);
									if (delEval == null) delEval = HelperFunctions.dummyDeleteEvaluation(currentActivity);
								} catch (Exception e) {
									logger.error("Substitute_Activity: Exception evaluating delete activity"+e.getMessage(), e);

								}

								currentActivitiesToStop.add(delEval);

							}
						} else { //No Drug_Usage for current activity, therefore no specific indication
							delEval = HelperFunctions.dummyDeleteEvaluation(currentActivity);
							currentActivitiesToStop.add(delEval);

						}
					} // end of for i
				}
				return currentActivitiesToStop;
				// end of method
			}
		}
	}

	public void doAction(
			Action_To_Choose action,
			Guideline_Action_Choices currentDecision,
			GuidelineInterpreter interpreter){
		Guideline_Activity_Evaluations substituteEvaluationSet;
		Collection evaluatedSubs = null;
		logger.debug("Substitute_Activity.doAction ");
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
			Collection activitiesToStop = currentActivitiesToStop(interpreter);
			Collection activitiesToAdd = addEvaluations(interpreter);
			Collection subEvalCollection = new ArrayList();

			Substitute_Evaluation subEval = new Substitute_Evaluation("", "substitute "+ "for",
					(Add_Evaluation[])activitiesToAdd.toArray(new Add_Evaluation[0]),
					(Delete_Evaluation[])activitiesToStop.toArray(new Delete_Evaluation[0]),
					Preference.preferred, this.getfine_grain_priorityValue());
			Choice_Evaluation choiceEvaluation = new Choice_Evaluation();
			choiceEvaluation.substitute_eval(subEval);
			subEvalCollection.add(choiceEvaluation) ;

			Choice_Evaluation[] subEvals = (Choice_Evaluation[])
					subEvalCollection.toArray(new Choice_Evaluation[0]);
			substituteEvaluationSet =
					new Guideline_Activity_Evaluations(
							this.makeGuideline_Entity(interpreter.getCurrentGuidelineID()),
							Evaluation_Type.substitute,
							subEvals,
							interpreter.getCurrentGuidelineID());
			interpreter.addEvaluatedChoice(substituteEvaluationSet);


			logger.debug("Substitution_Activity.doAction: end "+getlabelValue());
		} catch (Exception e){
			return;
		}
	}


}
