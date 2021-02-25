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


package edu.stanford.smi.eon.guidelineinterpreter;

import java.util.*;

import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.eon.PCAServerModule.*;
import edu.stanford.smi.eon.kbhandler.*;
import edu.stanford.smi.eon.datahandler.*;
import edu.stanford.smi.eon.Dharma.* ;
import edu.stanford.smi.eon.util.* ;
import edu.stanford.smi.eon.criterion.* ;

import java.io.*;

import org.apache.log4j.*;


/**
 * @author swt
 *
 */
public class GuidelineInterpreter implements Serializable, PaddaGEE {
	public static GuidelineInterpreter currentGuidelineInterpreter=null;
	// Use of static variable (set in PAL_Criterion class) assumes that the process
	// is single-threaded

	static Logger logger = Logger.getLogger(GuidelineInterpreter.class);

	private KBHandler kbManager;
	private DataHandler dataManager;
	public CriteriaEvalManager evalManager;
	public Management_Guideline guideline;
	public String assumptionString = "none";
	protected Criteria_Evaluation assumption=null;
	protected Collection actionChoices;
	protected Collection activityChoices;
	protected Collection evaluatedChoices;
	public Collection scenarioChoices;
	protected Collection goals;
	protected Collection conclusions;
	protected Collection assessments;
	protected Collection questions;
	public Compliance_Level complianceLevel;
	private Map evaluations = new HashMap();
	public Set expandedChoices;  //choices that had been made
	private String pmStopTime = null;
	private String pmStartTime = null;

	public CriteriaEvalManager getEvalManager() {
		return evalManager;
	}
	public Set getExpandedChoices() {
		return expandedChoices;
	}

	public void setGuideline(Management_Guideline guideline) {
		this.guideline = guideline;
	}

	public void putKBmanager(KBHandler kbManager) {
		this.kbManager = kbManager;
	}

	public KBHandler getKBmanager() {
		return kbManager;
	}
	public void setDBmanager(DataHandler dbmanager) {
		this.dataManager = dbmanager;
	}

	public DataHandler getDBmanager() {
		return dataManager;
	}
	public String getCurrentGuidelineID() {
		return guideline.getlabelValue();
	}

	public ActivityEvaluation getEvaluation(Object key){
		return (ActivityEvaluation) evaluations.get(key);

	}

	public void putEvaluation(Object key, Object value) {
		evaluations.put(key, value);

	}

	public GuidelineInterpreter(KBHandler kbManager, DataHandler dataManager ,
			//PCASession pca_session,
			Management_Guideline guideline) {

		this.kbManager = kbManager;
		this.dataManager = dataManager;
		this.guideline = guideline;
		//   this.pca_session = pca_session;
		actionChoices =new ArrayList();
		scenarioChoices = new ArrayList();
		evaluatedChoices = new ArrayList();
		goals = new ArrayList();
		conclusions = new ArrayList();
		assessments = new ArrayList();
		questions = new ArrayList();
		evalManager = new  CriteriaEvalManager();
		expandedChoices =new HashSet();
		
	}

	public GuidelineInterpreter(KBHandler kbManager, DataHandler dataManager ,
			//PCASession pca_session,
			Management_Guideline guideline,
			Collection conclusions) {

		this.kbManager = kbManager;
		this.dataManager = dataManager;
		this.guideline = guideline;
		//    this.pca_session = pca_session;
		this.conclusions = conclusions;
		goals = new ArrayList();
		actionChoices =new ArrayList();
		activityChoices = new ArrayList();
		scenarioChoices = new ArrayList();
		evaluatedChoices = new ArrayList();
		assessments = new ArrayList();
		questions = new ArrayList();
		evalManager = new  CriteriaEvalManager();
		expandedChoices = new HashSet();
		
	}

	public void resetAdvisories () {
		actionChoices =new ArrayList();
		activityChoices = new ArrayList();
		scenarioChoices = new ArrayList();
		evaluatedChoices = new ArrayList();
		goals = new ArrayList();
		conclusions = new ArrayList();
		assessments = new ArrayList();
		questions = new ArrayList();
		evalManager = new CriteriaEvalManager();
		expandedChoices = new HashSet();
		
	}

	public void setAssumption(Criteria_Evaluation assume, String assumptionString) {
		assumption = assume;
		this.assumptionString = assumptionString;
	}

	public Guideline_Service_Record returnAdvisory() {
		Guideline_Service_Record result = null;
		logger.debug("#actionchoices: " + actionChoices.size() + " " +
				"#evaluatedChoices: "+ evaluatedChoices.size() + " " +
				"#scenarioChoices: "+ scenarioChoices.size() + " "+
				"#goals: "+ goals.size()+
				"#conclusions: "+ conclusions.size()+
				"#questions: "+questions.size() );
		if (assumption == null) {
			assumption = HelperFunctions.dummyCriteriaEvaluation();
		}
		if (containsAdvisories()){
			//Guideline_Action_Choices[] actionChoicesArray = (Guideline_Action_Choices[]) actionChoices.toArray(new Guideline_Action_Choices[0]);
			//Guideline_Action_Choices[] test = new Guideline_Action_Choices[1];
			//test[0] = actionChoicesArray[2];

			result = new Guideline_Service_Record(assumption, //asumptions
					//new Guideline_Action_Choices[0],
					(Guideline_Action_Choices[]) actionChoices.toArray(new Guideline_Action_Choices[0]),
					//test,
					new Guideline_Activity_Choices[0],  //activity choices
					//(Guideline_Activity_Choices[]) activityChoices.toArray(new Guideline_Activity_Choices[0]),
					//new Guideline_Activity_Evaluations[0],
					(Guideline_Activity_Evaluations[]) evaluatedChoices.toArray(new Guideline_Activity_Evaluations[0]),
					//new Guideline_Scenario_Choices[0],
					(Guideline_Scenario_Choices[]) scenarioChoices.toArray(new Guideline_Scenario_Choices[0]) ,
					//new Guideline_Goal[0],
					(Guideline_Goal[]) goals.toArray(new Guideline_Goal[0]),
					//new Conclusion[0],
					(Conclusion[]) conclusions.toArray(new Conclusion[0]),   //subject classification (e.g., risk groups)
					//new Current_Activity_Assessment[0]
					(Current_Activity_Assessment[]) assessments.toArray(new Current_Activity_Assessment[0]),
					(Data_To_Collect[]) questions.toArray(new Data_To_Collect[0]),
					this.getCurrentGuidelineID()
					);
			return result;

		} else return null;
	}
	/*
  public Guideline_Goal evaluateGoal(Goal goalSpec, Guideline_Entity guideline)
    throws PCA_Session_Exception {

    return computeGoals(goalSpec.getgoalsValue(), guideline);

  }
	 */

	public void determinePatientCharacteristics(Collection caseClassification,
			String patientId, String sessionTime)
					throws PCA_Session_Exception  {
		Slot criteriaSlot = kbManager.getKB().getSlot("DiagnosticCriteria");
		Slot expressionSlot = kbManager.getKB().getSlot("derivation_expression");
		logger.debug("**** GuidelineInterpreter determinePatientCharacteristics" );
		for (Iterator i=caseClassification.iterator(); i.hasNext();) {
			// each item is either a Diagnostic_Term_Metaclass or a Variable
			Instance characteristic = (Instance) i.next();
			if (characteristic.hasOwnSlot(criteriaSlot)) {
				Criteria_Evaluation eval = null;
				Collection<Criterion> definition = characteristic.getOwnSlotValues(criteriaSlot);
				if (definition != null) {
					for (Criterion def: definition) {
						eval = def.evaluate(this, false);
						if (eval.truth_value.equals(Truth_Value._true))
							break;
					}
					Justification justification = new Justification(eval, "definition criteria");
					logger.debug("GuidelineInterpreter determinePatientCharacteristics - "+
							characteristic.getName()+ " truthValue: 0=true, 1=false, 2=unknown "+eval.truth_value);
					java.lang.Object conclusionString = characteristic.getOwnSlotValue(kbManager.getKB().getSlot("PrettyName"));
					if (eval.truth_value.equals(Truth_Value._true)){
						addConclusion(new Conclusion(
								(patientId!=null) ? patientId : "",
										(sessionTime != null) ? sessionTime : "",
												(conclusionString!=null) ? (String)conclusionString : characteristic.getName(), "", justification));
					}
				}
			} else if (characteristic.hasOwnSlot(expressionSlot)) {
				java.lang.Object definitionObj = characteristic.getOwnSlotValue(expressionSlot);
				if (definitionObj != null) {
					Slot labelSlot = kbManager.getKB().getSlot("label");
					Expression eval = ((Expression) definitionObj).evaluate_expression(this);
					Justification justification = HelperFunctions.dummyJustification();
					java.lang.Object conclusionString = ((Variable)characteristic).getOwnSlotValue(labelSlot);
					if (eval != null){
						logger.debug("GuidelineInterpreter determinePatientCharacteristics - "+
								((Variable)characteristic).getOwnSlotValue(labelSlot)+ " Expression:  "+ eval.toString());
						addConclusion(new Conclusion(
								(patientId!=null) ? patientId : "",
										(sessionTime != null) ? sessionTime : "",
												(conclusionString!=null) ? (String)conclusionString : ((Variable)characteristic).getBrowserText(), eval.toString(), justification));
					}
					else logger.info("GuidelineInterpreter determinePatientCharacteristics - "+
							characteristic.getBrowserText() +" evalautes to null. It may be OK (e.g., precondition of conditional expression evaluates to false)");
				}
			}
		}
	}


	/**
	 * 
	 * @param possibleGoals A collection conditional goals specific to a target
	 * @param guideline The guideline for which the goal is being computed
	 * @return Guideline_Goal
	 * @throws PCA_Session_Exception
	 */
	public Guideline_Goal computeConditionalGoals(Collection possibleGoals, 
			Guideline_Entity guideLine
			) throws PCA_Session_Exception {
		Criterion goalCriterion = null;
		Criteria_Evaluation goalAchievement = null;
		Justification support = null;
		Criteria_Evaluation goalSupport = null;
		Guideline_Goal  guidelineGoal = null;
		Conditional_Goal goal;
		int fine_grain_priority = 0;
		Collection<String> references = new ArrayList<String>();
		if ((possibleGoals != null) || !possibleGoals.isEmpty()) {
			for (Iterator i = possibleGoals.iterator(); i.hasNext();) {
				goal = (Conditional_Goal) i.next();
				if (goal.getselection_criterionValue() != null){
					goalSupport = ((Criterion) goal.getselection_criterionValue()).evaluate(this, false);
					fine_grain_priority = goal.getfine_grain_priorityValue();
					logger.debug("in computeGoals - evaluating "+
							((Criterion) goal.getselection_criterionValue()).getlabelValue()+
							" return: " + goalSupport.truth_value);
					if (PCAInterfaceUtil.mapTruthValue(goalSupport.truth_value)) {
						support = new Justification(goalSupport, "reason for goal");
						goalCriterion = (Criterion) goal.getcriterion_to_achieveValue();
						logger.debug("in computeGoals - goal to achieve: "+goalCriterion.getlabelValue());
						break;
					}
				} else goalCriterion = (Criterion) goal.getcriterion_to_achieveValue();
				for (Instance supportMaterial : (Collection<Instance>)goal.getreferencesValue()) {
					references.addAll(supportMaterial.getOwnSlotValues(kbManager.kb.getSlot("URL")));
				}
			}

			/*
			 *          edu.stanford.smi.eon.PCAServerModule.Guideline_Entity guideline_entity,
					    edu.stanford.smi.eon.PCAServerModule.Justification support_for_goal,
					    edu.stanford.smi.eon.PCAServerModule.Criteria_Evaluation goal,
					    edu.stanford.smi.eon.PCAServerModule.Goal_State achieved,
					    String data,
					    Collection<Action_Spec_Record> actions,
					    int fine_grain_priority,
					    Collection<String> references,
					    boolean primary

			 */

			guidelineGoal = new Guideline_Goal(guideLine, HelperFunctions.dummyJustification(),
					HelperFunctions.dummyCriteriaEvaluation(), Goal_State.none, null, null, 0, null, true, null, null, null);
			//logger.debug("[5] guideline_Goal");
			if (goalCriterion != null) {
				// logger.debug("this.getCurrentGuidelineID():" + "[" + this.getCurrentGuidelineID() + "]");
				goalAchievement = goalCriterion.evaluate(this, false);

				logger.debug("in computeGoals - evaluation result: truth_value=" +goalAchievement.truth_value );
				guidelineGoal.support_for_goal = support;
				guidelineGoal.goal = goalAchievement;
				if (support == null) 
					logger.info("support in evaluating guideline goal "+goalCriterion.getBrowserText()+" is null, probably because of null selection criteria");
				else if (support.evaluation == null) 
					logger.warn("support evaluation for guideline goal "+ goalCriterion.getBrowserText()+" is null");
				else 
					guidelineGoal.data = support.evaluation.support;

				guidelineGoal.fine_grain_priority = fine_grain_priority;

				//        logger.debug("guidelineGoal:" + guidelineGoal);
				if (goalAchievement.truth_value.equals(Truth_Value._true))
					guidelineGoal.achieved = Goal_State.achieved;
				else if (goalAchievement.truth_value.equals(Truth_Value._false))
					guidelineGoal.achieved = Goal_State.failed;
				else guidelineGoal.achieved = Goal_State.unknown;
				//        logger.debug("+guidelineGoal.achieved: [" + guidelineGoal.achieved + "]");
				goals.add(guidelineGoal);
			} else {
				logger.debug("in computeGoals - no goal criterion; assume achieved");
				guidelineGoal.achieved = Goal_State.achieved;
				guidelineGoal.references = references;
			}
		}
		return guidelineGoal;
	}

	public void retractGoals () {
		goals.clear();
	}
	public Criteria_Evaluation checkGoal(Guideline_Entity guideline) {
		Guideline_Goal guidelineGoal=null;
		for (Iterator i=goals.iterator(); i.hasNext();) {
			guidelineGoal = (Guideline_Goal) i.next();
			logger.debug( "in checkGoal - guideline: " +guidelineGoal.guideline_entity.name
					+ " goal id: "+ guidelineGoal.guideline_entity.entity_id + " target id: "+
					guideline.entity_id);
			if ((guidelineGoal.guideline_entity.entity_id.equals(guideline.entity_id)) &&
					(guidelineGoal.goal != null))
				return guidelineGoal.goal;
		}
		return null;
	}

	public Guideline_Scenario_Choices initializeScenarios()
			throws PCA_Session_Exception {
		Guideline_Scenario_Choices scenarios = guideline.initializeScenarios(this, kbManager);
		if (scenarios != null) scenarioChoices.add(scenarios);
		return scenarios;
	}


	/** Given a selected scenario, recompute advisories */
	public Guideline_Service_Record chosenScenario(Selected_Scenario selectedScenario,
			Compliance_Level compliance)
					throws PCA_Session_Exception {
		Scenario scenario = (Scenario) kbManager.instantiate(selectedScenario.selected_item);
		scenario.doStep(this, compliance);
		return returnAdvisory();
	}


	public boolean containsAdvisories() {

		return ((scenarioChoices.size()> 0) || (actionChoices.size()> 0) ||
				(evaluatedChoices.size() > 0) || conclusions.size() > 0);
	}


	public boolean hasUnexpandedPreferedChoice(Guideline_Service_Record advisories) {
		boolean hasUnexpandedPrefered = false;
		Guideline_Action_Choices currentDecision;
		Guideline_Scenario_Choices currentScenarioChoice;
		//for (Iterator i = expandedChoices.iterator(); i.hasNext();) {
		//  Guideline_Action_Choices decision = (Guideline_Action_Choices)i.next();
		//logger.debug("expanded choices: ", 0);
		//for (int j=0; j<decision.action_choices.length;j++) {
		// logger.debug("  "+decision.action_choices[j].name, 0);
		//}
		//}
		for (int i=0; i< advisories.scenario_choices.length; i++) {
			currentScenarioChoice = advisories.scenario_choices[i];
			for (int j=0; j < currentScenarioChoice.scenarios.length; j++){
				if ((currentScenarioChoice.scenarios[j].preference.equals(Preference.preferred)) &&
						(!expandedChoices.contains(currentScenarioChoice))) {
					hasUnexpandedPrefered = true;
					return hasUnexpandedPrefered;    			
				}
			}
		}

		for (int i=0; i< advisories.decision_points.length; i++) {
			currentDecision = advisories.decision_points[i];
			for (int j=0; j< currentDecision.action_choices.length;
					j++) {
				if ((currentDecision.action_choices[j].preference.equals(Preference.preferred)) &&
						(!expandedChoices.contains(currentDecision))) {
					//logger.debug("current choices: ", 0);
					//for (int k=0; k<currentDecision.action_choices.length;k++) {
					//  logger.debug("  "+currentDecision.action_choices[k].name, 0);
					//}
					hasUnexpandedPrefered = true;
					break;
				}
			}
		}
		return hasUnexpandedPrefered;
	}

	public Guideline_Service_Record chosenActions(Collection selectedActions) {
		logger.debug("in chosenActions: # selectedActions = " + selectedActions.size());
		Set currentExpandedChoices = new HashSet();
		for (Iterator i = selectedActions.iterator(); i.hasNext();) {
			Selected_Action selectedAction = (Selected_Action) i.next();
			//logger.debug("in chosenAction: found selected action" +
			//    selectedAction.action_specification.toString(), 3);
			expandedChoices.add(selectedAction.guideline_action_choice );
			currentExpandedChoices.add(selectedAction.selected_item);
			if (selectedAction.action_specification != null) {
				Action_Specification actionSpec = (Action_Specification) kbManager.instantiate(
						selectedAction.action_specification);
				logger.debug("GuidelineInterpreter.chosenActions: do action on " + actionSpec.getlabelValue());
				try {
					actionSpec.doAction(selectedAction.selected_item,
							selectedAction.guideline_action_choice, this);
				} catch (Exception e) {
					logger.error("Exception in doAction method of "+actionSpec.getName());
					logger.error(e.getMessage(), e);

				}
			}
		}
		// Continue traversing expanded choices
		for (Iterator i = currentExpandedChoices.iterator(); i.hasNext();) {
			Action_To_Choose chosen = (Action_To_Choose)i.next();
			Clinical_Algorithm_Entity chosenStep =
					(Clinical_Algorithm_Entity) kbManager.instantiate(chosen.action_step);
			try {
				chosenStep.tryNext(this, complianceLevel);
			} catch (Exception e) {
				logger.error("Exception in GuidelineInterpreter.chosenActions() tryNext() "+
						chosenStep.getBrowserText()+" "+e.getMessage());
			}
		}
		return returnAdvisory();
	}

	public Collection getEvaluatedChoices () {
		return evaluatedChoices;
	}

	public Collection getScenarioChoices () {
		return scenarioChoices;
	}
	public Collection getActionChoices () {
		return actionChoices;
	}
	public Collection addEvaluatedChoice (Guideline_Activity_Evaluations evaluation) {
		evaluatedChoices.add(evaluation);
		return evaluatedChoices;
	}

	public Collection addScenarioChoice (Guideline_Scenario_Choices choices) {
		scenarioChoices.add(choices);
		return scenarioChoices;
	}
	public Collection addActionChoice (Guideline_Action_Choices choices) {
		actionChoices.add(choices);
		return actionChoices;
	}
	public Collection addActivityChoice(Guideline_Activity_Choices choices) {
		activityChoices.add(choices);
		return activityChoices;
	}
	public Collection addActionChoices (Collection choices) {
		actionChoices.addAll(choices);
		return actionChoices;
	}

	public Collection addConclusions (Collection conclusions) {
		this.conclusions.addAll(conclusions);
		return this.conclusions;
	}

	public Collection addConclusion (Conclusion conclusion) {
		conclusions.add(conclusion);
		return conclusions;
	}
	public Collection getConclusions () {
		return conclusions;
	}

	public void addGoal(Guideline_Goal goal) {
		goals.add(goal);
	}

	public void addQuestion(Data_To_Collect question) {
		questions.add(question);
	}

	public void addQuestions(Collection questions) {
		this.questions.addAll(questions);
	}

	public Collection getQuestions() {
		return questions;
	}
	
	public String getPerformanceMeasureStartTime() {
		return this.pmStartTime;
	}

	public String getPerformanceMeasureStopTime() {
		return this.pmStopTime;
	}
	
	public String getCaseID() {
		return getDBmanager().getCaseID();
	}

	public Collection matchData(String dataClass, Collection concepts, Collection data) {
		Collection matchedDataCollection = new ArrayList();
		Matched_Data matchedData;

		for (Iterator i = concepts.iterator(); i.hasNext();) {
			Cls concept = ((Cls) i.next());
			logger.debug("in matchData: concept = '" +concept+"'");
			if (concept != null) {
				matchedData = containSubclassOf(dataClass, concept.getName(), data);
				if (matchedData != null) {
					logger.debug("in matchData: matchedData = " + matchedData.toString());
					matchedDataCollection.add(matchedData);
				}

			}  // Need to put in an exception here
		}
		//Never return null
		return matchedDataCollection;
	}

	public Collection matchAdverseEvents(Cls drugClass) {
		Collection<Matched_Data> matchedAdverseEvents = new ArrayList<Matched_Data>();
		Collection adverseEvents = kbManager.findInstances("Adverse_Reaction",
				this.getDBmanager().getCaseSelector(), this);
		for (Iterator event=adverseEvents.iterator(); event.hasNext();) {
			Adverse_Reaction value = (Adverse_Reaction) event.next();
			String symptom =  value.getDomain_term();
			//if (symptom == null) continue;
			String substance = value.getSubstance();
			Cls substanceCls = kbManager.getCls(substance);
			// If substance is a subclass of drugClass, then given warning
			if (substanceCls != null) {
				if (substanceCls.hasSuperclass(drugClass) || substanceCls == drugClass) {
					logger.debug("GuidelineIterpreter matchAdverseEvents: "+ substance +
							" is a subclass of "+ drugClass.getName());
					String[] notUsed = new String[0];
//					drugClassName[0] = drugClass.getBrowserText();
					Matched_Data resultMatch= new Matched_Data(substanceCls.getBrowserText(), symptom, notUsed);
					matchedAdverseEvents.add(resultMatch);
				} else logger.debug("GuidelineIterpreter matchAdverseEvents: "+ substance
						+ " is not a subclass of "+ drugClass.getName());
			} else logger.error("GuidelineIterpreter matchAdverseEvents: "+substance+" is not a known class");
		}
		return matchedAdverseEvents;
	}



	/**
    For a concept name and "data", a set of concept names, return a "Matched_Data"
    structure consisting of those concept names in "data" that are names of
    subclasses of the given concept.
	 */

	public Matched_Data containSubclassOf(String data_category, String className,
			Collection data) {

		Collection subclassNames = new ArrayList();
		String classPrettyName= "";
		Collection evaluatedMatches = new ArrayList();
		Cls concept = null;

		if (className != null) {
			concept = kbManager.getKB().getCls(className);
			try {
				classPrettyName = kbManager.prettyName(concept);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Cls subclass;
			Collection properSubclasses = concept.getSubclasses();
			Slot diagnosticCriteriaSlot = (Slot) kbManager.getKB().getInstance("DiagnosticCriteria");
			Collection diagnosticCriteria;
			Criteria_Evaluation eval;


			Collection subclasses = new ArrayList();
			subclasses.addAll( properSubclasses);
			subclasses.add(concept);  // include the concept itself


			for (Iterator i = subclasses.iterator(); i.hasNext();) {
				subclass = (Cls) i.next();
				subclassNames.add(subclass.getName());
				if (subclass.hasOwnSlot(diagnosticCriteriaSlot)) {
					diagnosticCriteria = subclass.getOwnSlotValues(diagnosticCriteriaSlot);
					if ( diagnosticCriteria != null) {
						for (Object def : diagnosticCriteria) {
							if (def instanceof Criterion){
								try {
									eval = ((Criterion) def).evaluate(this, false);
									if ((eval != null) && (eval.truth_value.equals(Truth_Value._true))){
										String subclassPrettyName = "";
										try {
											subclassPrettyName = kbManager.prettyName(subclass);
											if (subclassPrettyName.equals(classPrettyName) )
												subclassPrettyName = "";
										} catch (Exception e) {
											logger.debug("No name for concept");
										}
										evaluatedMatches.add(subclassPrettyName+ 
												((eval.support != null) ? "["+eval.support+"]" : ""));
										break;
									}
								} catch (Exception e) {
									logger.error("Error evaluating criteria "+
											((Criterion) def).getName() + e.getMessage(), e);
								}
							} else {
								logger.error(((Instance)diagnosticCriteria).getName()+ " is not an instance of Criterion");
							} 
						}
					}
				}
			}
			if (data != null)
				subclassNames.retainAll(data);
			if (!evaluatedMatches.isEmpty()) {
				subclassNames.addAll(evaluatedMatches);
			}
			// logger.debug("subclasses of "+className+" in intersection:"+subclassNames.toString());
		} else // className is null
			if (data != null) 
				subclassNames = data;
		/*
    if (subclassNames.size() > 0) {
      String classPrettyName = "";
      try {
        classPrettyName = kbManager.prettyName(concept);
      } catch (Exception e) {
        classPrettyName = "";
      }
      return new Matched_Data(data_category, classPrettyName,
        (String[]) subclassNames.toArray(new String[0]));
    } else return null;
		 */
		if (subclassNames.size() > 0) {
			return new Matched_Data(data_category, classPrettyName,
					(String[]) subclassNames.toArray(new String[0]));
		} else return null;

	}

	public void serializeAdvisories(ObjectOutputStream out) {
		try {
			out.writeObject(actionChoices);
			out.writeObject(assumptionString);
			out.writeObject(assumption);
			out.writeObject(conclusions);
			out.writeObject(evaluatedChoices);
			out.writeObject(goals);
			out.writeObject(guideline.getlabelValue());
			out.writeObject(scenarioChoices);
			dataManager.serializeData(out);
		} catch (Exception e) {
			logger.error("Problem serializing " +e.getMessage());
		}
	}

	public void restoreAdvisories(ObjectInputStream in) throws java.io.IOException,
	java.lang.ClassNotFoundException, PCA_Session_Exception {
		String guidelineName;
		actionChoices = (Collection) in.readObject();
		assumptionString = (String) in.readObject();
		assumption = (Criteria_Evaluation) in.readObject();
		conclusions = (Collection)in.readObject();
		evaluatedChoices = (Collection)in.readObject();
		goals = (Collection)in.readObject();
		guidelineName = (String)in.readObject();
		scenarioChoices = (Collection)in.readObject();
		logger.info("GuidelineInterpreter.restoreAdvisories: Finished restoring advisories");
		try {
			dataManager.restoreData(in);
		} catch (Exception e){
			logger.error("Problem restoring data", e);
		}
	}

	public Conclusion checkGuidelineApplicability (Management_Guideline guideline,
			String patient_id, String session_time) {
		Justification support=null;
		Criteria_Evaluation eligibilityEvaluation=null;
		//logger.debug("CheckGuidelineApplicability: guideline "+guideline);
		Collection criteria=guideline.geteligibility_criteriaValue();
		if (!criteria.isEmpty()) {
			eligibilityEvaluation = evaluateApplicabilityCriteria(criteria, patient_id,
					session_time);
			if (eligibilityEvaluation == null) {
				return null;
			} else {
				if (eligibilityEvaluation.truth_value.equals(Truth_Value._false)) {
					if (eligibilityEvaluation.support != null && !eligibilityEvaluation.support.equals(""))
						eligibilityEvaluation.support = "It is not the case that "+eligibilityEvaluation.support;
				}
				support = new Justification(eligibilityEvaluation, "eligibility evaluation");
				return  new Conclusion(
						(patient_id!=null) ? patient_id : "",
								(session_time != null) ? session_time : "",
										guideline.getlabelValue(),
										eligibilityEvaluation.truth_value.toString(), support);
			}
		} else {
			return null;
		}
	}

	private Criteria_Evaluation evaluateApplicabilityCriteria(Collection criteria,
			String patient_id, String session_time) {
		Justification support=null;
		logger.debug("GuidelineInterpreter: EvaluateApplicabilityCriteria: begin");
		N_ary_Criterion eligibilityCriteria = (N_ary_Criterion)dataManager.createRegisteredInstance("N_ary_Criterion");
		eligibilityCriteria.setcriteriaValue(criteria);
		eligibilityCriteria.setoperatorValue("AND");
		eligibilityCriteria.setlabelValue("Eligibility criteria");
		try {
			Criteria_Evaluation eligibilityEvaluation =  eligibilityCriteria.evaluate(this, true);
			logger.debug("GuidelineInterpreter: EvaluateApplicabilityCriteria: end");
			return eligibilityEvaluation;
		} catch (Exception e) {
			logger.error("Exception evaluating eligibility criteria");
			e.printStackTrace();
			return null;
		}


	}


	public Guideline_Service_Record computePerformanceMeasures(String pid,
			String hospitalizationID, String sessionTime, String PMName,
			String startTime, String stopTime) {
		this.pmStartTime = startTime;
		this.pmStopTime = stopTime;
		dataManager.setEpisodePeriod(DharmaPaddaConstants.meaurementPeriodName, startTime, stopTime);
		Collection<Goal> pms = guideline.getalternative_goalsValue();
		for (Goal pm: pms) {
			if (PMName != null) {
				if (pm.getlabelValue() != null) {
					logger.debug("PMName: "+PMName+"Candidate PM name:"+pm.getlabelValue());
				} else {
					logger.error("Candidate PM "+pm.getBrowserText()+ " has no label value!");
					break;
				}
			}
			if ((PMName == null) ||  
					((PMName != null) && (pm.getlabelValue() != null) && (pm.getlabelValue().equals(PMName)))) {
				Guideline_Goal pmResult = pm.computePerformanceMeasure(this, 
						sessionTime);
				this.goals.add(pmResult);
			}
		}
		Guideline_Service_Record pmResults = new Guideline_Service_Record(
				null,
				null, null, null, null, goals.isEmpty() ? null : (Guideline_Goal[]) goals.toArray(new Guideline_Goal[goals.size()]),
						null, null, null, this.getCurrentGuidelineID()
				);
		return pmResults;
	}
}
