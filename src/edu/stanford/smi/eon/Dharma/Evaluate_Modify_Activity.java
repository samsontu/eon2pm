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

	public void setactivity_specValue(Cls activity_spec) {
		ModelUtilities.setOwnSlotValue(this, "activity_spec", activity_spec);	}
	public Cls getactivity_specValue() {
		return ((Cls) ModelUtilities.getOwnSlotValue(this, "activity_spec"));
	}
	// __Code above is automatically generated. Do not change

	private int debugLevel = 4;

	public Collection activitiesToModify(GuidelineInterpreter interpreter) {
		logger.debug("Evaluate_Modify_Activity.activitiesToModify");
		Collection activitiesToModify = null;
		if (getactivities_to_modifyValue() != null) {
			try {
				activitiesToModify = evaluateActivitiesToModify(interpreter);
			} catch (Exception e) {
				logger.error("Problem evaluating activities to delete", e);

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

	private Collection evaluateActivitiesToModify(GuidelineInterpreter interpreter)
			throws PCA_Session_Exception {

		logger.debug("Evaluate_Modify_Activity.evaluateActivitiesToModify ");
		Collection activitiesToModifyCollection = null;
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


	public void doAction (Action_To_Choose action,
			Guideline_Action_Choices currentDecision,
			GuidelineInterpreter interpreter) {
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
		if (insufficientSpec())
			return;
		else {
			Collection activitiesToModify = activitiesToModify(interpreter);
			Collection changeEvaluations = new ArrayList(); //each entry is an instance of Change_Attribute_Evaluation
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
						for (Iterator i = activitiesToModify.iterator();i.hasNext();) {
							Change_Attribute_Evaluation changeEval = null;
							String currentActivity = (String)i.next();
							logger.debug("Evaluate_Modify_Activity_Intensity.doAction: current actvitity "+
									currentActivity + " activitySpecClass "+activitySpecClass.getName());
							compare.value = currentActivity;
							Collection activitySpec = interpreter.getKBmanager().findInstances(
									activitySpecClass, compare, interpreter);

							if ((activitySpec != null) && (activitySpec.size() > 0)) {
								Choice_Evaluation choiceEvaluation = new Choice_Evaluation();
								Cls currentActivityLevel = getCurrentActivityLevel(interpreter,
										getactivity_classValue(), currentActivity);
								if (currentActivityLevel != null) {
									logger.debug("Evaluate_Modify_Activity_Intensity.doAction: actvitity level Cls "+
											currentActivityLevel.getName());
									for (Iterator j=activitySpec.iterator(); j.hasNext();) {
										Instance evaluateObject = (Instance)j.next();
										try {
											// if current activity level not is less than the maximum level
											// then
											if (changeToNextLevel(interpreter, currentActivityLevel, evaluateObject)) {
												Collection<Matched_Data> adverseReactionCollection  = null;
												Collection<Matched_Data> stopControllableIntensifyConditionCollection = null;
												Collection<Matched_Data> stopUncontrollableIntensifyConditionCollection = null;
												Collection<Matched_Data> stopIntensifyConditionCollection = new ArrayList<Matched_Data>();
												Matched_Data[] addverseReactionArray = null;
												Matched_Data[] stopIntensifyConditionArray = null;
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
													stopIntensifyConditionArray = stopIntensifyConditionCollection.toArray(new Matched_Data[stopIntensifyConditionCollection.size()]);
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
																	addverseReactionArray,  //side_effects
																	stopIntensifyConditionArray, 
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


		} //*/
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
}
