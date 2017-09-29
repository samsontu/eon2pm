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
public class Evaluate_Substitution_Activity extends Evaluate_Activity_Act {

	public Evaluate_Substitution_Activity(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
	static Logger logger = Logger.getLogger(Evaluate_Substitution_Activity.class);

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

	public void setalternativesValue(Collection alternatives) {
		ModelUtilities.setOwnSlotValues(this, "alternatives", alternatives);	}
	public Collection getalternativesValue(){
		return  ModelUtilities.getOwnSlotValues(this, "alternatives");
	}

	public void setactivity_specValue(Cls activity_spec) {
		ModelUtilities.setOwnSlotValue(this, "activity_spec", activity_spec);	}
	public Cls getactivity_specValue() {
		return ((Cls) ModelUtilities.getOwnSlotValue(this, "activity_spec"));
	}
	
	public void setrecommendation_basisValue(Collection recommendationBais) {
		ModelUtilities.setOwnSlotValues(this, "recommendation_basis", recommendationBais);	}
	public Collection getrecommendation_basisValue(){
		return  ModelUtilities.getOwnSlotValues(this, "recommendation_basis");
	}
	// __Code above is automatically generated. Do not change

	private class Substitution_Pair {
		protected Collection currentActivities;
		protected Collection substituteActivities;
		protected Substitution_Pair(Collection currentMeds, Collection substituteMeds) {
			currentActivities = currentMeds;
			substituteActivities = substituteMeds;
		}
	}


	Delete_Evaluation[] currentActivitiesToStop(String[] currentActivities,
			GuidelineInterpreter interpreter, Compliance_Level compliance,
			WhereComparisonFilter compare, Cls activitySpecClass) {
		Collection<Delete_Evaluation> currentActivitiesToStop=new ArrayList<Delete_Evaluation>();
		Delete_Evaluation delEval=null;
		Collection<Drug_Usage> specForCurrentActivity =null;
		logger.debug("Evaluate_Substitution_Activity.currentActivitiesToStop: "+getlabelValue());
		if ((currentActivities != null) && (currentActivities.length > 0)) {
			for (int i = 0;i < currentActivities.length ;i++) {  //for i
				String currentActivity = currentActivities[i];
				compare.value = currentActivity;
				specForCurrentActivity = (Collection<Drug_Usage>)interpreter.getKBmanager().findInstances(activitySpecClass,
						compare, interpreter);
				logger.debug("Evaluate_Substitute_Activity.doAction "+compare.value+
						" specForCurrentActivities size:"+specForCurrentActivity.size());
				Drug_Usage evaluateObject = getDrugUsage( currentActivity,  specForCurrentActivity, interpreter);
				if (evaluateObject != null) {
					try {
						logger.debug("Evaluate_Substitute_Activity.currentActivitiesToStop "+evaluateObject.getlabelValue());
						delEval = evaluateObject.evaluateDelete(interpreter, null, currentActivity, this.getfine_grain_priorityValue());

						logger.debug("Evaluate_Substitute_Activity.currentActivitiesToStop subEval"+ delEval);
					} catch (Exception e) {
						logger.error("Evaluate_Substitute_Activity: Exception evaluating delete activity"+e.getMessage(), e);
					}
				} else {
					delEval = new Delete_Evaluation("",
						    currentActivity,
						    this.makeGuideline_Entity(interpreter.guideline.getName()),
						    null,
						    new Matched_Data[0],
						    new Matched_Data[0],
						    new Matched_Data[0],
						    new Matched_Data[0],
						    new Matched_Data[0],
						    new Matched_Data[0],
						    new Matched_Data[0],
						    new Matched_Data[0],
						    new Matched_Data[0],
						    new Matched_Data[0],
						    Truth_Value._true,
						    null,
						    Preference.preferred,
						    null,
						    0);
				}
				currentActivitiesToStop.add(delEval);
			}
		}
		Delete_Evaluation[] activitiesToStopArray=
				(Delete_Evaluation[])currentActivitiesToStop.toArray(new Delete_Evaluation[0]);
		logger.debug("Evaluate_Substitution_Activity.currentActivitiesToStop: end "+getlabelValue());
		return activitiesToStopArray;
	}  // end of method

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
		Collection substitutionCandidates = getalternativesValue();
		WhereComparisonFilter compare = null;

		logger.debug("Evaluate_Substitution_Activity.doAction: "+getlabelValue());

		// Error checking
		if (substitutionCandidates == null) {
			logger.error("No value for alternative activities in "+getlabelValue());
			return;
		}
		Collection currentActivities = null;
		// expect activityClass to be a string like "Medication"
		if (activityClass !=null) {
			currentActivities = interpreter.getDBmanager().currentActivities(
					activityClass, null);
		} else {
			logger.error("No value for activity_class in "+getlabelValue());
			return;
		}
		if (activitySpecKey != null) // e.g., Drug_Class_Name
			compare = new WhereComparisonFilter(activitySpecKey.getName(),
					DharmaPaddaConstants.superclass_of, null);
		else {
			logger.error("No value for activity_spec_key in "+getlabelValue());
			return;
		}
		if (activitySpecClass == null) {  //Drug_Usage class
			logger.error("No value for activity_spec in "+getlabelValue());
			return;
		}
		Matched_Data qualifiedActivities = null;
		Collection specForCurrentActivity =null;
		//each entry of substituteEvaluations is an instance of Substitute_Evaluation
		Collection substituteEvaluations = new ArrayList();
		Guideline_Activity_Evaluations substituteEvaluationSet;
		Collection alternativeWOCurrentActivities = new ArrayList();
		Collection evaluatedSubs = null;
		logger.debug("Evaluate_Substitute_Activity.doAction ");

		// Find those activities that are candidates for substitution
		// e.g., Don't want to substitute away from insulin when managing
		// hypertension
		// From candidate activities, select those that are contraindicated or are not indicated
		Instance activityToStopQuery = getactivity_to_stopValue();
		if (activityToStopQuery != null) {
			try {
				qualifiedActivities = evaluateActivitiesToDelete(interpreter);
			} catch (Exception e) {
				logger.error("Problem evaluating activities to delete", e);

			}
		} else qualifiedActivities = interpreter.containSubclassOf(activityClass,
				getdomain_termValue().getName(),currentActivities);

		if (qualifiedActivities != null) {
			if (qualifiedActivities.data.length > 0) {
				// find all instances of activity_spec that subsume current activity, and
				// construct Substitue_Evaluation records
				logger.debug("Evaluate_Substitute_Activity.doAction, currentActivities != null");
				Collection currentActivitiesSpec = new ArrayList();
				for (int i = 0;i < qualifiedActivities.data.length ;i++) {
					compare.value = qualifiedActivities.data[i];
					specForCurrentActivity = interpreter.getKBmanager().findInstances(activitySpecClass,
							compare, interpreter);
					if (specForCurrentActivity != null) currentActivitiesSpec.addAll(specForCurrentActivity);
				}
				//currentActivitiesSpec contains all instances of Drug_Usage whose Drug_Class_Name are superclasses of qualified activity
				for (Iterator i = substitutionCandidates.iterator();i.hasNext();) {
					Object temp = i.next();
					if (!currentActivitiesSpec.contains(temp)){
						alternativeWOCurrentActivities.add(temp);
					}
				}

				// alternativeWOCurrentActivities are substitution alternatives that are not drug usage instances of drugs being substituted away from
				Delete_Evaluation[] activitiesToStop = currentActivitiesToStop(qualifiedActivities.data, interpreter,
						interpreter.complianceLevel, compare, activitySpecClass);

				/* For each substitution candidate, see if the candidate can be  */
				for (int i = 0;i < activitiesToStop.length ;i++) {
					evaluatedSubs = null;
					logger.debug("Evaluate_Substitution_Activity.doAction: i= "+i+
							" activity to stop: "+activitiesToStop[i]);
					try {
						// returns a collection of add_evaluations
						evaluatedSubs = Drug_Usage.evaluateSubstitute(interpreter, activitiesToStop[i],
								alternativeWOCurrentActivities, this.getfine_grain_priorityValue(),
								this.getrecommendation_basisValue());
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
					logger.debug("Evaluate_Substitution_Activity.doAction: evaluatedSubs "+
							((evaluatedSubs == null) ? "is null" : evaluatedSubs.toString()));
					if (evaluatedSubs != null) {
						Substitution_Pair matchedAlternatives =
								compareAlternatives(evaluatedSubs, substituteEvaluations);
						if (matchedAlternatives != null) {
							//add current activity to the list to be substituted against
							matchedAlternatives.currentActivities.add(activitiesToStop[i]);
						}
						else {
							Collection currentMeds = new ArrayList();
							currentMeds.add(activitiesToStop[i]);
							Substitution_Pair subPair = new Substitution_Pair(currentMeds, evaluatedSubs);
							substituteEvaluations.add(subPair) ;
						}
					}
				}
			}
			logger.debug("Evaluate_Substitution_Activity.doAction: 2 "+getlabelValue());
			Collection subEvalCollection = new ArrayList();
			for (Iterator i = substituteEvaluations.iterator(); i.hasNext();) {
				Substitution_Pair addDelPair = (Substitution_Pair)i.next();
				Substitute_Evaluation subEval = new Substitute_Evaluation("", "substitute "+ "for",
						(Add_Evaluation[])addDelPair.substituteActivities.toArray(new Add_Evaluation[0]),
						(Delete_Evaluation[])addDelPair.currentActivities.toArray(new Delete_Evaluation[0]),
						Preference.neutral, this.getfine_grain_priorityValue());
				Choice_Evaluation choiceEvaluation = new Choice_Evaluation();
				choiceEvaluation.substitute_eval(subEval);
				subEvalCollection.add(choiceEvaluation) ;
			}
			Choice_Evaluation[] subEvals = (Choice_Evaluation[])
					subEvalCollection.toArray(new Choice_Evaluation[0]);

			if (substituteEvaluations.size ()> 0) {
				substituteEvaluationSet =
						new Guideline_Activity_Evaluations(
								this.makeGuideline_Entity(interpreter.getCurrentGuidelineID()),
								Evaluation_Type.substitute,
								subEvals,
								interpreter.getCurrentGuidelineID());

				interpreter.addEvaluatedChoice(substituteEvaluationSet);
			}

		}
		logger.debug("Evaluate_Substitution_Activity.doAction: end "+getlabelValue());
	}

	private Substitution_Pair compareAlternatives(Collection evaluatedAdds,
			Collection substituteEvaluations ) {
		Substitution_Pair subPair = null;
		// substituteEvaluations is a collection of Substitution_Pairs
		Collection addStrings = new ArrayList();
		for (Iterator i= evaluatedAdds.iterator(); i.hasNext();) {
			addStrings.add(((Add_Evaluation)i.next()).name);
		}
		for (Iterator i= substituteEvaluations.iterator(); i.hasNext();) {
			Substitution_Pair candidateMatch = (Substitution_Pair)i.next();
			Collection oldAddStrings = new ArrayList();
			for (Iterator j= candidateMatch.substituteActivities.iterator(); j.hasNext();) {
				oldAddStrings.add(((Add_Evaluation)j.next()).name);
			}
			if (oldAddStrings.containsAll(addStrings) && addStrings.containsAll(oldAddStrings)) {
				subPair = candidateMatch;
				break;
			}
		}
		return subPair;
	}

	private Matched_Data evaluateActivitiesToDelete(GuidelineInterpreter guidelineManager)
			throws PCA_Session_Exception {
		logger.debug("Evaluate_Substitution_Activity.evaluateActivitiesToDelete ");
		Collection activitiesToStopCollection = null;
		Matched_Data activitiesToStopData = null;
		Instance query = getactivity_to_stopValue();
		if (query == null) {
			throw new PCA_Session_Exception("Null activity_to_stop slot in "+this.getBrowserText());
		} else {
			logger.debug("Evaluate_Substitution_Activity.evaluateActivitiesToDelete: consider deleting "
					+ activitiesToStopCollection);
			activitiesToStopCollection = ((Expression)query).doCollectionQuery(guidelineManager);
			if (activitiesToStopCollection == null)  {
				logger.error("Evaluate_Substitution_Activity.evaluateActivitiesToDelete: No activities to stop");
				throw new PCA_Session_Exception("Evaluate_Substitution_Activity.evaluateActivitiesToDelete: No activities to stop");
			} else {
				String[] activitiesToStop= (String[]) activitiesToStopCollection.toArray( new String[0]);
				activitiesToStopData = new Matched_Data("", "", activitiesToStop);
				return activitiesToStopData;
			}
		}
	}
}
