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
import edu.stanford.smi.eon.datahandler.Constants;
import edu.stanford.smi.eon.time.*;
import edu.stanford.smi.eon.criterion.*;
import edu.stanford.smi.protegex.pal.engine.*; 
import edu.stanford.smi.protegex.pal.parser.*;
import edu.stanford.smi.protegex.pal.relations.*;
import edu.stanford.smi.protegex.pal.language.*;

import org.apache.log4j.*;

/** 
 */
public class Evaluate_Modify_Activity extends Evaluate_Activity_Act {

	public Evaluate_Modify_Activity(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
	static Logger logger = Logger.getLogger(Evaluate_Modify_Activity.class);

	public void setattributeValue(String attribute) {
		ModelUtilities.setOwnSlotValue(this, "attribute", attribute);	}
	public String getattributeValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "attribute"));
	}

	public void setexcluded_activitiesValue(Collection excluded_activities) {
		ModelUtilities.setOwnSlotValues(this, "excluded_activities", excluded_activities);	}
	public Collection getexcluded_activitiesValue(){
		return  ModelUtilities.getOwnSlotValues(this, "excluded_activities");
	}

	public void setactivity_spec_keyValue(Instance activity_spec_key) {
		ModelUtilities.setOwnSlotValue(this, "activity_spec_key", activity_spec_key);	}
	public Instance getactivity_spec_keyValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "activity_spec_key"));
	}

	public void setactivities_to_modifyValue(Instance activities_to_modify) {
		ModelUtilities.setOwnSlotValue(this, "activities_to_modify", activities_to_modify);	}
	public Instance getactivities_to_modifyValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "activities_to_modify"));
	}

	public void setactivity_spec_candidateValue(Instance activity_spec_candidate) {
		ModelUtilities.setOwnSlotValue(this, "activity_spec_candidate", activity_spec_candidate);	}
	public Instance getactivity_spec_candidateValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "activity_spec_candidate"));
	}
	public void setactivity_specValue(Cls activity_spec) {
		ModelUtilities.setOwnSlotValue(this, "activity_spec", activity_spec);	}
	public Cls getactivity_specValue() {
		return ((Cls) ModelUtilities.getOwnSlotValue(this, "activity_spec"));
	}
	// __Code above is automatically generated. Do not change

	/*
	 * Activities to modify can be defined either (1) using an expression in the slot activities_to_modify or (2) using activity_class
	 * 	(e.g., all instances of Medication) and excluded_activities. Using an activities_to_modify expression is preferred.
	 */

	public Collection<String> activitiesToModify(GuidelineInterpreter interpreter) {
		logger.debug("Evaluate_Modify_Activity.activitiesToModify");
		Collection<String> activitiesToModify = null;
		if (getactivities_to_modifyValue() != null) {
			try {
				activitiesToModify = evaluateActivitiesToModify(interpreter);
			} catch (Exception e) {
				logger.error("Problem evaluating activities to delete query", e);

			}
		} else {
			String activityClass = (String) getactivity_classValue(); //e.g., Medication
			activitiesToModify = interpreter.getDBmanager().currentActivities(
					activityClass, null);

			Matched_Data excludedActivities = null;
			Collection excludedActivityClasses = getexcluded_activitiesValue();

			if ((excludedActivityClasses != null) && (excludedActivityClasses.size() > 0)) {
				for (Iterator i = excludedActivityClasses.iterator(); i.hasNext();) {
					Cls excludedClass = (Cls)i.next();
					logger.debug("Evaluate_Modify_Activity.activitiesToModify: excluded class"+excludedClass.getName());
					excludedActivities = interpreter.containSubclassOf(activityClass,
							excludedClass.getName(),activitiesToModify);
					if ((excludedActivities != null) && (excludedActivities.data.length > 0)) {
						for (int j=0; j< excludedActivities.data.length; j++) {
							logger.debug("Evaluate_Modify_Activity.activitiesToModify: excluded activity"+excludedActivities.data[j]);
							activitiesToModify.remove(excludedActivities.data[j]);
						}
					}
				}
			}
		}
		return activitiesToModify;

	}

	private Collection<String> evaluateActivitiesToModify(GuidelineInterpreter interpreter)
			throws PCA_Session_Exception {

		logger.debug("Evaluate_Modify_Activity.evaluateActivitiesToModify ");
		Collection<String> activitiesToModifyCollection = null;
		Instance query = getactivities_to_modifyValue();
		if (query == null) {
			throw new PCA_Session_Exception("Null activity_to_modify in "+this.getName());
		} else {
			if (query instanceof PAL_Query)
				activitiesToModifyCollection = ((PAL_Query)query).doQuery(interpreter);
			else if (query instanceof Structured_Query)
				activitiesToModifyCollection = ((Set_Expression)((Structured_Query)query).evaluate_expression(interpreter)).getset_elementsValue();
			if (activitiesToModifyCollection == null)  {
				logger.warn("No activity to modify after evaluating "+query.getBrowserText());
			}
			return activitiesToModifyCollection;
		}
	}

	private Cls getCurrentActivityLevel(GuidelineInterpreter interpreter,
			String activityClass, String currentActivity) {
		Object currentActivityLevelString =null;
		try {
			currentActivityLevelString =
					interpreter.getDBmanager().findAttributeValue(
							interpreter, activityClass, currentActivity, "dose_level" );
		} catch (Exception e) {};
		if (currentActivityLevelString != null) {
			logger.debug("Evaluate_Modify_Activity_Intensity.doAction: actvitity level "+
					(String)currentActivityLevelString);
			return interpreter.getKBmanager().getCls(
					(String)currentActivityLevelString);
		} else return null;
	}

	private boolean insufficientSpec() {
		if ((getactivity_classValue() ==null) && (getactivities_to_modifyValue() == null)) {
			logger.error("Evaluate_Modify_Activity_Intensity.doAction: No value for activity_class in "+getlabelValue());
			return true;
		}
		if (getactivity_spec_keyValue() == null) {
			logger.error("Evaluate_Modify_Activity_Intensity.doAction: No value for activity_spec_key in "+getlabelValue());
			return true;
		}
		if (getactivity_specValue() == null) {
			logger.error("Evaluate_Modify_Activity_Intensity.doAction: No value for activity_spec in "+getlabelValue());
			return true;
		}
		return false;
	}

	private boolean isRuledIn(GuidelineInterpreter interpreter) {
		if (getrule_in_conditionValue() != null) {
			Criteria_Evaluation evaluation = HelperFunctions.dummyCriteriaEvaluation();
			try {
				evaluation = (Criteria_Evaluation) ((Criterion)getrule_in_conditionValue()).evaluate(interpreter, false);
				if (!(PCAInterfaceUtil.mapTruthValue(evaluation.truth_value)))  {   // rule-in condition does not hold
					return false;
				}
			} catch (PCA_Session_Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}


	public void doAction (Action_To_Choose action,
			Guideline_Action_Choices currentDecision,
			GuidelineInterpreter interpreter) {
		if (!isRuledIn(interpreter) || (insufficientSpec()))
			return;
		else {
			Collection<String> activitiesToModify = activitiesToModify(interpreter);
			if (getactivity_spec_candidateValue() == null) {
				doActionWithoutActionSpecCandidateQuery(activitiesToModify, interpreter);
			} else {
				doActionWithActionSpecCandidateQuery(activitiesToModify, interpreter);
			}
		}
	}
	
	private void doActionWithActionSpecCandidateQuery(Collection<String> activitiesToModify, GuidelineInterpreter interpreter) {
		System.out.println("Making action spec candidate query");
		Collection<Choice_Evaluation> changeEvaluations = new ArrayList<Choice_Evaluation>(); 
		Collection<Instance> actionSpecCandidates = null;
		try {
			actionSpecCandidates = getActionSpecCandidates(interpreter);
		} catch (Exception e) {
			logger.error("No action spec candidates for "+this.getBrowserText());
			return;
		}
		for (String activityToModify : activitiesToModify) {
			Instance activitySpec = matchActivityToModifyWithActivitySpec(activityToModify, actionSpecCandidates, interpreter);
			if (activitySpec == null) {
				logger.error("No activity specification for activity to modify"+activityToModify);
			} else {
				Collection<Instance> dummyList = new ArrayList<Instance>();
				dummyList.add(activitySpec);
				Change_Attribute_Evaluation changeEval = evaluateModifyActivityIntensity(dummyList, interpreter, null, activityToModify);	
				Choice_Evaluation choiceEvaluation = new Choice_Evaluation();
				if (changeEval != null) {
					choiceEvaluation.change_attribute_eval(changeEval);
					changeEvaluations.add(choiceEvaluation);
				}
			} 
		}
		addChangeEvaluation( changeEvaluations,  interpreter);
	}
	
	private Collection<Instance>  getActionSpecCandidates(GuidelineInterpreter interpreter) 
			throws PCA_Session_Exception {
		Collection<Instance> actionSpecCandidates = null;
		logger.debug("Evaluate_Modify_Activity.evaluateActivitiesToModify ");
		Instance query = getactivity_spec_candidateValue();
		if (query == null) {
			throw new PCA_Session_Exception("Null activity spec candidate query in "+this.getName());
		} else {
			if (query instanceof PAL_Query)
				actionSpecCandidates = ((PAL_Query)query).doQuery(interpreter);
			else if (query instanceof Structured_Query)
				actionSpecCandidates = ((Set_Expression)((Structured_Query)query).evaluate_expression(interpreter)).getset_elementsValue();
			if (actionSpecCandidates == null)  {
				logger.warn("No activity spec after evaluating "+query.getBrowserText());
			}
			return actionSpecCandidates;
		}

	}
	
	private Instance matchActivityToModifyWithActivitySpec(String activityToModify, Collection<Instance> actionSpecCandidates, GuidelineInterpreter interpreter) {
		for (Instance actionSpecCandidate : actionSpecCandidates) {
			// generic drug of Guideline Drug
			Cls matchingCls = (Cls)(actionSpecCandidate.getOwnSlotValue((Slot)getactivity_spec_keyValue()));
			//if activityToModify is a subclass of matchingCls
			KnowledgeBase kb = interpreter.getKBmanager().getKB();
			Cls activityToModifyCls = kb.getCls(activityToModify);
			if (activityToModifyCls == (matchingCls) || activityToModifyCls.hasSuperclass(matchingCls))
				return actionSpecCandidate;
		}
		return null;
	}
			
	private void doActionWithoutActionSpecCandidateQuery(Collection<String> activitiesToModify, GuidelineInterpreter interpreter) {
			Collection changeEvaluations = new ArrayList(); 
			Cls activitySpecClass = (Cls) getactivity_specValue();    //e.g., Guideline_Drug
			Slot activitySpecKey = (Slot)getactivity_spec_keyValue(); //e.g., drug_name
			WhereComparisonFilter compare = new WhereComparisonFilter(activitySpecKey.getName(),
					"eq", null);;

					// find all instances of activity_spec that subsume current activity, and
					// construct Change_Attribute_Evaluation records
					logger.debug("Evaluate_Modify_Activity.doAction: has sufficient spec");
					if (activitiesToModify == null) {
						logger.error("Evaluate_Modify_Activity_Intensity.doAction:Potential Error: No activity to modify");
					} else {
						for (Object i :  activitiesToModify) {
							Change_Attribute_Evaluation changeEval = null;
							String currentActivity = (String)i;
							logger.debug("Evaluate_Modify_Activity_Intensity.doAction: current actvitity "+
									currentActivity + " activitySpecClass "+activitySpecClass.getName());
							compare.value = currentActivity;
							Collection<Instance> activitySpec = interpreter.getKBmanager().findInstances(
									activitySpecClass, compare, interpreter);
							if ((activitySpec != null) && (activitySpec.size() > 0)) {
								Choice_Evaluation choiceEvaluation = new Choice_Evaluation();
								Cls currentActivityLevel = getCurrentActivityLevel(interpreter,
										getactivity_classValue(), currentActivity);
								if (currentActivityLevel != null) {
									logger.debug("Evaluate_Modify_Activity_Intensity.doAction: actvitity level Cls "+
											currentActivityLevel.getName());
									changeEval = evaluateModifyActivityIntensity(activitySpec, interpreter, currentActivityLevel, currentActivity);									
									if (changeEval != null) {
										choiceEvaluation.change_attribute_eval(changeEval);
										changeEvaluations.add(choiceEvaluation);
									}
								} else logger.error("Evaluate_Modify_Activity_Intensity.doAction: "+
										currentActivity+" currentActivityLevel is null");
							}
							else logger.error("Evaluate_Modify_Activity_Intensity.doAction: "+
									currentActivity+" activitySpec is null");
						}
					}
					addChangeEvaluation( changeEvaluations,  interpreter);


		} //*/
	
	private void addChangeEvaluation(Collection changeEvaluations, GuidelineInterpreter interpreter) {
		if (changeEvaluations.size ()> 0) {
			logger.debug("Evaluate_Modify_Activity_Intensity.doAction: "+
					"****Add evaluated choice number=" + changeEvaluations.size ());
			Guideline_Activity_Evaluations modifyEvaluations =
					new Guideline_Activity_Evaluations(
							this.makeGuideline_Entity(interpreter.getCurrentGuidelineID()),
							Evaluation_Type.change_attribute,
							(Choice_Evaluation[])changeEvaluations.toArray(new Choice_Evaluation[0]),
							interpreter.getCurrentGuidelineID());
			interpreter.addEvaluatedChoice(modifyEvaluations);
		} else logger.warn("Evaluate_Modify_Activity_Intensity.doAction: "+
				"No dose change record");
		
	}


	boolean changeToNextLevel(GuidelineInterpreter interpreter,
			Cls currentActivityLevel, Instance evaluateObject) throws Exception {

		throw new Exception("Evaluate_Modify_Activity.direction: not implemented");
	}

	Direction direction() throws Exception {
		throw new Exception("Evaluate_Modify_Activity.direction: not implemented");
	}

	Cls getRecommendationMood(Preference pref) throws Exception {
		throw new Exception("Evaluate_Modify_Activity.getRecommendationMood: not implemented");
	}

	Cls getNextLevel(GuidelineInterpreter interpreter,
			Cls currentActivityLevel, Instance evaluateObject) throws Exception {
		throw new Exception("Evaluate_Modify_Activity.direction: not implemented");
	}

	private Collection<Drug_Usage>getDrugUsages(String currentActivity, GuidelineInterpreter interpreter) {
		WhereComparisonFilter filter=  new WhereComparisonFilter("Drug_Class_Name", DharmaPaddaConstants.superclass_of,
				currentActivity);
		Collection<Drug_Usage> superActivitySpec = interpreter.getKBmanager().findInstances(
				"Drug_Usage", filter, interpreter);
		return superActivitySpec;
	}

	private Collection<Matched_Data> matchDoNotIntensifyConditions(String currentActivity, GuidelineInterpreter interpreter, boolean controllable) {
		Collection<Drug_Usage> drugUsages = getDrugUsages( currentActivity,  interpreter);
		Collection<Matched_Data> doNotIntensify= new ArrayList<Matched_Data>();
		for (Drug_Usage drugUsage: drugUsages) {
			Collection<Matched_Data> matched = interpreter.matchData("",
					drugUsage.getDo_Not_Intensify_Conditions(controllable), interpreter.getDBmanager().currentProblems());
			doNotIntensify.addAll(matched);
		}
		if (!doNotIntensify.isEmpty())
			return doNotIntensify;
		else return null;
	}



	private Action_Spec_Record[] generateMessages (GuidelineInterpreter interpreter,
			String currentActivity, Instance spec, Preference preference) {
		Guideline_Drug drugSpec = (Guideline_Drug) spec;
		Collection messages = new ArrayList();
		String recommendationMood =null;
		try {
			recommendationMood = getRecommendationMood(preference).getName();
			// first evaluate messages associated with drugSpec, if any
			Collection evaluatedCollateralIncreaseActions =
					drugSpec.evaluateCollateralActions(interpreter,
							drugSpec.getCollateralActionsByType(recommendationMood));
			messages.addAll(evaluatedCollateralIncreaseActions);
			if (preference.equals(Preference.blocked))
				messages.addAll(drugSpec.evaluateCollateralActions(interpreter,
						drugSpec.getCollateralActionsByType(getRecommendationMood(Preference.preferred).getName())));
			WhereComparisonFilter filter=  new WhereComparisonFilter("Drug_Class_Name", DharmaPaddaConstants.superclass_of,
					currentActivity);
			Collection superActivitySpec = interpreter.getKBmanager().findInstances(
					"Drug_Usage", filter, interpreter);
			Drug_Usage drugUsage = getDrugUsage( currentActivity,  superActivitySpec, interpreter);
			// Do not add "blocked" collateral actions from drug usage
			messages.addAll(drugUsage.evaluateCollateralActions(interpreter,
					drugUsage.getCollateralActionsByType(getRecommendationMood(Preference.preferred).getName())));
			return (Action_Spec_Record[])messages.toArray(new Action_Spec_Record[0]);
		} catch (Exception e) {
			return new Action_Spec_Record[0];
		}

	}

	private Change_Attribute_Evaluation evaluateModifyActivityIntensity(Collection<Instance> activitySpec, GuidelineInterpreter interpreter, Cls currentActivityLevel, String currentActivity) {
		Change_Attribute_Evaluation changeEval = null;
		for (Instance evaluateObject :  activitySpec) {
			try {
				Drug_Usage drugUsage = (Drug_Usage)((Guideline_Drug)evaluateObject).getdrug_usageValue();
				ActivityEvaluation activityEval = interpreter.getEvaluation(drugUsage);
				if (activityEval == null) {
					activityEval = drugUsage.evaluate(interpreter);
					//check the case where the bad partner drug is the medication whose dose under consideration
					checkPartner(activityEval, currentActivity);
				}
				// if current activity level not is less than the maximum level
				// then
				if ((currentActivityLevel == null) || (changeToNextLevel(interpreter, currentActivityLevel, evaluateObject))) {
					Collection<Matched_Data> adverseReactionCollection  = null;
					Collection<Matched_Data> stopControllableIntensifyConditionCollection = null;
					Collection<Matched_Data> stopUncontrollableIntensifyConditionCollection = null;
					Collection<Matched_Data> stopIntensifyConditionCollection = new ArrayList<Matched_Data>();
					Matched_Data[] addverseReactionArray = null;

					Cls nextLevel = getNextLevel(interpreter, currentActivityLevel, evaluateObject);
					Cls specificDrugCls = interpreter.getKBmanager().getCls(currentActivity);
					if (specificDrugCls != null) {
						adverseReactionCollection =  interpreter.matchAdverseEvents(specificDrugCls);
						stopControllableIntensifyConditionCollection = matchDoNotIntensifyConditions(currentActivity, interpreter, true); ;
						stopUncontrollableIntensifyConditionCollection = matchDoNotIntensifyConditions(currentActivity, interpreter, false); ;
						if (adverseReactionCollection != null) {
							addverseReactionArray = adverseReactionCollection.toArray(new Matched_Data[adverseReactionCollection.size()]);
						}
						if (stopControllableIntensifyConditionCollection != null)
							stopIntensifyConditionCollection.addAll(stopControllableIntensifyConditionCollection);
						if (stopUncontrollableIntensifyConditionCollection != null)
							stopIntensifyConditionCollection.addAll(stopUncontrollableIntensifyConditionCollection);
					}
					if (stopUncontrollableIntensifyConditionCollection == null) {
						Preference preference = null;
						Cls mood = null;
						if (stopControllableIntensifyConditionCollection == null) {
							preference = Preference.preferred;
						} else {
							preference = Preference.blocked;
						}
						mood = getRecommendationMood(preference);
						changeEval = new Change_Attribute_Evaluation("",
								currentActivity,
								interpreter.getCurrentGuidelineID(),
								getattributeValue(),
								(nextLevel != null) ? nextLevel.getName() : "",
								(activityEval != null) ? (Matched_Data[]) activityEval.beneficialInteractions.toArray(new Matched_Data[0]) : null,
								(activityEval != null) ? (Matched_Data[]) activityEval.compellingIndications.toArray(new Matched_Data[0]) : null,
								(activityEval != null) ? (Matched_Data[]) activityEval.contraindications.toArray(new Matched_Data[0]) : null,
								(activityEval != null) ? (Matched_Data[]) activityEval.harmful_interactions.toArray(new Matched_Data[0]) : null,
								(activityEval != null) ? (Matched_Data[]) activityEval.relative_contraindications.toArray(new Matched_Data[0]) : null,
								(activityEval != null) ? (Matched_Data[]) activityEval.relative_indications.toArray(new Matched_Data[0]) : null,
								addverseReactionArray,  //side_effects
								( Matched_Data[])stopIntensifyConditionCollection.toArray(new Matched_Data[stopIntensifyConditionCollection.size()]), 
								direction(), //Truth_Value._true,
								generateMessages(interpreter, currentActivity, evaluateObject, preference),
								preference, this.getfine_grain_priorityValue());
						String patient_id = interpreter.getDBmanager().getCaseID();
						Medication addedMed = (Medication)interpreter.getDBmanager().createInstance("Medication");
						addedMed.setSlotsValues((float)0.0, "",
								currentActivity, 0, "",
								mood,
								patient_id, "", "", Constants.active, null, (float)0.0, null );
					} else {
						String uncontrollableCondition = "";
						boolean first = true;
						for (Matched_Data m : stopUncontrollableIntensifyConditionCollection) {
							if (first) {
								uncontrollableCondition = uncontrollableCondition+ (m.guideline_term);
								first = false;
							} else
								uncontrollableCondition = uncontrollableCondition+", "+ (m.guideline_term);
						}
						logger.warn("Uncontrollable do not intensify condition is not null: "+uncontrollableCondition
								+". No increase dose recommendation)");
					}
					break;
				}

			} catch (Exception e) {
				logger.error("Evaluate_Modify_Activity_Intensity.doAction: exception looking at maximum dose level of "+
						evaluateObject.getName()+": "+e.getMessage());
			}
		} //for
		return changeEval;
	}
	private void checkPartner(ActivityEvaluation activityEval, String currentActivity) {
		Collection<Matched_Data> badPartners = activityEval.harmful_interactions;
		Collection<Matched_Data> checkedBadPartners = new ArrayList<Matched_Data>();
		for (Object badPartner : HelperFunctions.safeCollection(badPartners)) {
			String[] matchedData = ((Matched_Data)badPartner).getData();
			List<String> list = new ArrayList<String>(Arrays.asList(matchedData));
			list.remove(currentActivity);
			if (!list.isEmpty()) {
				((Matched_Data)badPartner).data = list.toArray(new String[0]);
				checkedBadPartners.add((Matched_Data)badPartner);
			}
		activityEval.harmful_interactions	= checkedBadPartners;
		}
	}
}
