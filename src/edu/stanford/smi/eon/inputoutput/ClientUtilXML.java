package edu.stanford.smi.eon.inputoutput;

import edu.stanford.smi.eon.inputoutput.EONXSDConstants;
import edu.stanford.smi.eon.PCAServerModule.*;
import edu.stanford.smi.eon.clients.ClientUtil;
import edu.stanford.smi.eon.util.HelperFunctions;
import edu.stanford.smi.protege.model.KnowledgeBase;

import java.util.*;

import org.apache.log4j.*;
import org.w3c.dom.*;

import javax.xml.parsers.*;


public class ClientUtilXML  {

	public ClientUtilXML() {
	}

	static Logger logger = Logger.getLogger(ClientUtil.class);


	public static Document makeXMLOutput(Guideline_Service_Record[] dssOutput, String caseID, String guidelineID, KnowledgeBase kb) {
		//itsWriter.println("<hr>");
		//		itsWriter.println("<h2>Guideline Manager Output " // (compliance level: "+
		// complianceLevel
		//		+"</h2>");
		DocumentBuilder docBuilder =null;
		if (dssOutput == null) {
			System.out.println("Null dssOutput!");
			return null;
		}
		boolean validating = false;

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		docFactory.setValidating(validating);
		try {
			docBuilder = docFactory.newDocumentBuilder();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement(EONXSDConstants.ROOT);
		doc.appendChild(rootElement);
		rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance" );
		for (int j=0; j< dssOutput.length; j++) {
			Element advisoryElement = doc.createElement(EONXSDConstants.ADVISORY);
			rootElement.appendChild(advisoryElement);
			makeAdvisoryXML(doc,advisoryElement,  dssOutput[j], guidelineID, kb);
		}
		addSimpleValue(doc, rootElement, EONXSDConstants.CASEID, caseID);

		return doc;
	}

	public static Element makeAdvisoryXML(Document doc, Element advisoryElement, Guideline_Service_Record advisory, String guidelineID, KnowledgeBase kb) {
		makeXMLAssumptions(doc, advisoryElement, advisory, guidelineID);
		makeXMLClassification(doc, advisoryElement, advisory, guidelineID);
		makeXMLGoals(doc, advisoryElement, advisory, guidelineID);
		makeXMLScenarioChoices(doc, advisoryElement, advisory, guidelineID);
		makeActionNode(doc, advisoryElement, advisory, guidelineID, kb);
		makeXMLEvaluatedInterventions(doc, advisoryElement, advisory, guidelineID);
//		makeXMLExclusions(doc, advisoryElement, advisory, guidelineID);
		return advisoryElement;
	}

/*	private static void makeXMLExclusions(Document doc,
			Element advisoryRoot, Guideline_Service_Record advisory,
			String guidelineID) {
		Exclusion[] exclusions = getExclusions(advisory);
		if (exclusions!=null) {
			for (int i = 0; i < exclusions.length; i++) {
				Element exclusionElement = doc.createElement(EONXSDConstants.EXCLUSION); 

				addSimpleValue(doc, exclusionElement, EONXSDConstants.CRITERION, advisory.exclusions[i].criterionID);
				addSimpleValue(doc, exclusionElement, EONXSDConstants.CRITERIONTYPE, advisory.exclusions[i].criterionType);
				addSimpleValue(doc, exclusionElement, EONXSDConstants.DATA, advisory.exclusions[i].data);
				addSimpleValue(doc, exclusionElement, EONXSDConstants.EVALUATIONRESULT, advisory.exclusions[i].evaluationResult.toString());
				addSimpleValue(doc, exclusionElement, EONXSDConstants.GUIDELINEID, guidelineID);
				if (advisory.goals[i].kb_goal_id != null) {
					addSimpleValue(doc, exclusionElement, EONXSDConstants.PERFORMANCEMEASURE, advisory.goals[i].kb_goal_id);
				}
				advisoryRoot.appendChild(exclusionElement);
			}
		}
		
	}
*/
	public static void makeXMLAssumptions(Document doc, Element advisoryRoot, Guideline_Service_Record advisory, String guidelineID) {
		Element assumptionElement = null;
		if (advisory.assumption != null) {
		if (!HelperFunctions.isDummyCriteriaEvaluation(advisory.assumption)) {
			assumptionElement = doc.createElement(EONXSDConstants.ASSUMPTION);
			String assumptionCE = makeXMLCriteriaEvaluation(advisory.assumption);
			if (assumptionCE != null) {
				addSimpleValue(doc, assumptionElement, EONXSDConstants.ASSUMPTIONFORGUIDELINE, assumptionCE);
				addSimpleValue(doc, assumptionElement, EONXSDConstants.GUIDELINEID, guidelineID);
			}
		}
		}
	}

	public static void  makeXMLEvaluatedInterventions(Document doc, Element advisoryRoot, Guideline_Service_Record advisory, String guidelineID) {
		HashMap<String, Collection<Choice_Evaluation>> activitiesEvals = new HashMap<String, Collection<Choice_Evaluation>>(); /* index by activity */
		indexEvaluations(advisory, activitiesEvals);

		for (String activity : activitiesEvals.keySet()) {
			Element activityElement = doc.createElement(EONXSDConstants.EVALUATEDINTERVENTION);
			Collection<Choice_Evaluation> evaluations = activitiesEvals.get(activity);
			Element activiityEvaluations = doc.createElement(EONXSDConstants.EVALUATIONS);
			advisoryRoot.appendChild(activityElement);
			addSimpleValue(doc, activityElement, EONXSDConstants.ACTIVITY, activity);
			activityElement.appendChild(activiityEvaluations);
			for (Choice_Evaluation evaluation : evaluations) {

				switch (evaluation.discriminator().value()) {
				case Evaluation_Type._add:
					Element addElement = doc.createElement(EONXSDConstants.ADD);
					createAddEvaluation(doc, addElement, activity, evaluation.add_eval(), guidelineID);
					activiityEvaluations.appendChild(addElement);
					break;
				case Evaluation_Type._delete:
					Element deleteElement = doc.createElement(EONXSDConstants.DELETE);
					createDeleteEvaluation(doc, deleteElement, activity , evaluation.delete_eval(), guidelineID);
					activiityEvaluations.appendChild(deleteElement);	
					break;
				case Evaluation_Type._change_attribute:
					createChangeAttributeEvaluation(doc, activiityEvaluations, activity , evaluation.change_attribute_eval(), guidelineID);
					break;
				case Evaluation_Type._substitute:
					if (toBeDeleted(activity, evaluation.substitute_eval())) 
						createReplacedByEvaluation( doc,  activiityEvaluations, activity,  
								evaluation.substitute_eval(),  guidelineID);
					else
						createReplacingEvaluation( doc,  activiityEvaluations, activity,  evaluation.substitute_eval(),  guidelineID);
					break;
				default: break;
				}
			}
		}

	}

	private static void createAddEvaluation(Document doc, Element activityRoot,  String activity, Add_Evaluation evaluation, String guidelineID) {
//		
		addSimpleValue(doc, activityRoot, EONXSDConstants.PRIORUSE, evaluation.prior_use.toString() );
		addSimpleValue(doc, activityRoot, EONXSDConstants.PREFERENCE, evaluation.preference.toString() );
		addCommonEvaluations(doc,activityRoot, evaluation.name, evaluation.activity_to_start, evaluation.compelling_indications, evaluation.relative_indications,
				evaluation.contraindications, evaluation.relative_contraindications, evaluation.side_effects,
				evaluation.beneficial_interactions, evaluation.harmful_interactions, evaluation.messages, guidelineID);
//		
	}

	private static void createDeleteEvaluation(Document doc, Element activityRoot, String activity,  Delete_Evaluation evaluation, String guidelineID) {
		//
		//addSimpleValue(doc, Element, EONXSDConstants.PRIORUSE, evaluation.prior_use.toString() );
		addSimpleValue(doc, activityRoot, EONXSDConstants.PREFERENCE, evaluation.preference.toString() );
		addCommonEvaluations(doc,activityRoot, evaluation.name, evaluation.activity_to_delete, evaluation.compelling_indications, evaluation.relative_indications,
				evaluation.contraindications, evaluation.relative_contraindications, evaluation.side_effects,
				evaluation.beneficial_interactions, evaluation.harmful_interactions, evaluation.messages, guidelineID);
		//
	}

	private static void createChangeAttributeEvaluation(Document doc, Element activityRoot, String activity,  Change_Attribute_Evaluation evaluation, String guidelineID) {
		Element changeAttributeElement = doc.createElement(EONXSDConstants.CHANGEATTRIBUTE);
		addSimpleValue(doc, changeAttributeElement, EONXSDConstants.NAME, evaluation.name );
		addSimpleValue(doc, changeAttributeElement, EONXSDConstants.DESCRIPTION, evaluation.description );
		addSimpleValue(doc, changeAttributeElement, EONXSDConstants.ATTRIBUTENAME, evaluation.attribute_name );
		addSimpleValue(doc, changeAttributeElement, EONXSDConstants.LEVEL, evaluation.level );
		addSimpleValue(doc, changeAttributeElement, EONXSDConstants.CHANGEDIRECTION, evaluation.change_direction.toString() );
		addMessages(doc, changeAttributeElement, evaluation.messages );			
		addSimpleValue(doc, changeAttributeElement, EONXSDConstants.PREFERENCE, evaluation.preference.toString() );
		addSimpleValue(doc, changeAttributeElement, EONXSDConstants.GUIDELINEID, evaluation.guideline_id );
		activityRoot.appendChild(changeAttributeElement);	
	}

	private static boolean toBeDeleted (String activity, Substitute_Evaluation substituteEval) {
		boolean toBeDeleted = false;
		for (Delete_Evaluation toDelete: substituteEval.activities_to_replace) {
			if (toDelete.activity_to_delete.equals(activity)) {
				toBeDeleted = true;
				break;
			}
		}
		return toBeDeleted;
	}

	private static void createReplacingEvaluation(Document doc, Element activityRoot,String activity,  
			Substitute_Evaluation evaluation, String guidelineID) {
		// activity will be added 
		Delete_Evaluation[] toDelete = evaluation.activities_to_replace;
		Add_Evaluation toAdd =null;
		for (Add_Evaluation add : evaluation.activities_to_start){
			// get the evaluation corresponding to activity
			if (activity.equals(add.activity_to_start)){
				toAdd = add;
				break;
			}
		}
		for (Delete_Evaluation del: toDelete) {
			Element replacesElement = doc.createElement(EONXSDConstants.REPLACEACTIVITY);
			activityRoot.appendChild(replacesElement);
			addSimpleValue(doc,replacesElement,EONXSDConstants.ACTIVITYTODELETE, del.activity_to_delete );
			Element replacedEvalElement = doc.createElement(EONXSDConstants.REPLACEDACTIVITYEVALUATION);
			replacesElement.appendChild(replacedEvalElement);
			createDeleteEvaluation(doc, replacedEvalElement, del.activity_to_delete, del, guidelineID);
			Element replacingEvalElement = doc.createElement(EONXSDConstants.REPLACINGACTIVITYEVALUATION);
			replacesElement.appendChild(replacingEvalElement);
			createAddEvaluation(doc, replacingEvalElement, del.activity_to_delete, toAdd, guidelineID);
		}
	}

	private static void createReplacedByEvaluation(Document doc, Element activityRoot,String activity,  
			Substitute_Evaluation evaluation, String guidelineID) {
		Delete_Evaluation toDelete = null;
		Add_Evaluation[] toAdd = evaluation.activities_to_start;

		for (Delete_Evaluation del : evaluation.activities_to_replace){
			// get the evaluation corresponding to activity
			if (activity.equals(del.activity_to_delete)){
				toDelete = del;
				break;
			}
		}
		for (Add_Evaluation add : toAdd) {
			Element replaceByElement = doc.createElement(EONXSDConstants.REPLACEBYACTIVITY);
			activityRoot.appendChild(replaceByElement);
			addSimpleValue(doc,replaceByElement,EONXSDConstants.ACTIVITYTOADD, add.activity_to_start);
			Element replacedEvalElement = doc.createElement(EONXSDConstants.REPLACEDACTIVITYEVALUATION);
			replaceByElement.appendChild(replacedEvalElement);
			createDeleteEvaluation(doc, replacedEvalElement, toDelete.activity_to_delete, toDelete, guidelineID);
			Element replacingEvalElement = doc.createElement(EONXSDConstants.REPLACINGACTIVITYEVALUATION);
			replaceByElement.appendChild(replacingEvalElement);
			createAddEvaluation(doc, replacingEvalElement, add.activity_to_start, add, guidelineID);
		}

	}

	private static void indexEvaluations(Guideline_Service_Record advisory,HashMap<String, Collection<Choice_Evaluation>> activitiesEvals ) {
		if (advisory.evaluated_choices != null) {
		for (int i=0; i< advisory.evaluated_choices.length; i++) {
			Guideline_Activity_Evaluations evaluations = advisory.evaluated_choices[i];

			for (int j=0; j < evaluations.evaluations.length; j++) {

				Choice_Evaluation evaluation = evaluations.evaluations[j];

				switch (evaluation.discriminator().value()) {
				case Evaluation_Type._add:
					indexByActivity(evaluation.add_eval().activity_to_start,  activitiesEvals, evaluation);
					break;
				case Evaluation_Type._delete:
					indexByActivity(evaluation.delete_eval().activity_to_delete,  activitiesEvals, evaluation);
					break;
				case Evaluation_Type._change_attribute:
					indexByActivity(evaluation.change_attribute_eval().name,  activitiesEvals, evaluation);
					break;
				case Evaluation_Type._substitute:
					for (int del=0; del < evaluation.substitute_eval().activities_to_replace.length; del++) {
						indexByActivity(evaluation.substitute_eval().activities_to_replace[del].activity_to_delete,  activitiesEvals, 
								evaluation);
					}
					for (int add=0; add < evaluation.substitute_eval().activities_to_start.length; add++) {
						indexByActivity(evaluation.substitute_eval().activities_to_start[add].activity_to_start,  activitiesEvals, 
								evaluation);
					}
					break;
				default: break;
				}
			}
		}
		}
	}

	private static void indexByActivity(String activity, HashMap<String, Collection<Choice_Evaluation>> activitiesEvals, Choice_Evaluation evaluation) {
		if (activitiesEvals.get(activity) != null) {
			((Collection<Choice_Evaluation>)activitiesEvals.get(activity)).add(evaluation);
		} else {
			Collection<Choice_Evaluation> evals = new ArrayList<Choice_Evaluation>();
			evals.add(evaluation);
			activitiesEvals.put(activity, evals);
		}
	}

	public static void 		addCommonEvaluations(Document doc, Element activityElement, String name, String activity,
			Matched_Data[] compelling_indications, Matched_Data[] relative_indications,
			Matched_Data[] contraindications, Matched_Data[] relative_contraindications, Matched_Data[] side_effects,
			Matched_Data[] beneficial_interactions, Matched_Data[] harmful_interactions, 
			Action_Spec_Record[] messages, String guidelineID) {
		addSimpleValue(doc, activityElement, EONXSDConstants.NAME, name);
		addSimpleValue(doc, activityElement, EONXSDConstants.ACTIVITY, activity);
		addSimpleValues(doc, activityElement, EONXSDConstants.COMPELLINGINDICATIONS, getMatchedGuidelineTerm(compelling_indications));
		addSimpleValues(doc, activityElement, EONXSDConstants.RELATIVEINDICATIONS, getMatchedGuidelineTerm(relative_indications));
		addSimpleValues(doc, activityElement, EONXSDConstants.CONTRAINDICATIONS, getMatchedGuidelineTerm(contraindications));
		addSimpleValues(doc, activityElement, EONXSDConstants.RELATIVECONTRAINDICATIONS, getMatchedGuidelineTerm(relative_contraindications));
		addSimpleValues(doc, activityElement, EONXSDConstants.HARMFULINTERACTIONS, getMatchedGuidelineTerm(harmful_interactions));
		addSimpleValues(doc, activityElement, EONXSDConstants.BENEFICIALINTERACTIONS, getMatchedGuidelineTerm(beneficial_interactions));
		addSimpleValues(doc, activityElement, EONXSDConstants.SIDEEFFECTS, getMatchedGuidelineTerm(side_effects));
		addMessages(doc, activityElement, messages);
		addSimpleValue(doc, activityElement, EONXSDConstants.GUIDELINEID, guidelineID);
	}
// Process collateral actions
	public static void 	addMessages(Document doc, Element activityElement, Action_Spec_Record[] messages ) {
		for (Action_Spec_Record action: messages ) {
//			addSimpleValue(doc, activityElement, EONXSDConstants.MESSAGES, message.text );
			addOneActionNode(doc, activityElement, action);
		}
	}

	private static Collection<String> getMatchedGuidelineTerm (Matched_Data[] matchedData) {
		Collection<String> matchedGuidelineTerms = new ArrayList<String>();
		for (Matched_Data matchedDatum : matchedData) {
			matchedGuidelineTerms.add(matchedDatum.guideline_term);
		}
		if (matchedGuidelineTerms.isEmpty())
			return null;
		else return matchedGuidelineTerms;
	}
	
	public static void addOneActionNode (Document doc, Element activityElement, Action_Spec_Record action) {
		Element actionElt = doc.createElement(EONXSDConstants.ACTION);
		addActionNodeElements(doc, action, actionElt);
		if (action.action_spec_class.equals(EONXSDConstants.MESSAGECLASS)) {
			addMessageElements(doc, (Message)action, actionElt);
			activityElement.appendChild(actionElt);
		} else if (action.action_spec_class.equals(EONXSDConstants.ORDERTESTPROCEDURECLASS)) {
			addOrderTestProcedureElements(doc, (Order_TestProcedure)action, actionElt);
			activityElement.appendChild(actionElt);
		} else if (action.action_spec_class.equals(EONXSDConstants.REFERRALCLASS)) {
			addReferralElements(doc, (Referral)action, actionElt);
			activityElement.appendChild(actionElt);
		}

	}
	
	public static void makeActionNode(Document doc, Element advisoryRoot, Guideline_Service_Record advisory, 
			String guidelineID, KnowledgeBase kb) {
		if ((advisory.decision_points != null) && (advisory.decision_points.length > 0))
			for (int i = 0; i < advisory.decision_points.length; i++) {
				for (Action_To_Choose actionToChoose :advisory.decision_points[i].action_choices) {
					if (actionToChoose.preference.value() == Preference.preferred.value()) 
						if (actionToChoose.action_specifications != null)
							for (Action_Spec_Record action : actionToChoose.action_specifications) {
								addOneActionNode(doc, advisoryRoot, action);
							}
				}
			}

	}
	
	private static void addActionNodeElements(Document doc, Action_Spec_Record action, Element actionElt){
		if ((action.action_spec_class != null) && (!action.action_spec_class.equals(""))) {
			actionElt.setAttribute("xsi:type", action.action_spec_class);
			addSimpleValue(doc, actionElt, EONXSDConstants.ACTIONCLASS,action.action_spec_class);
		}
		addSimpleValue(doc, actionElt, EONXSDConstants.FINEGRAINPRIORTY,Integer.toString(action.fine_grain_priority));
		if ((action.strength_of_recommendation != null) && (!action.strength_of_recommendation.equals("")))
			addSimpleValue(doc, actionElt, EONXSDConstants.GRADERECOMMENDATION,action.strength_of_recommendation);
		if ((action.name != null) && (!action.name.equals("")))
			addSimpleValue(doc, actionElt, EONXSDConstants.NAME,action.name);
		if ((action.net_benefit != null) && (!action.net_benefit.equals("")))
			addSimpleValue(doc, actionElt, EONXSDConstants.NETBENEFIT,action.net_benefit);
		if ((action.level_of_evidence != null) && (!action.level_of_evidence.equals("")))
			addSimpleValue(doc, actionElt, EONXSDConstants.OVERALLQUALITYOFEVIDENCE,action.level_of_evidence);
		if ((action.overall_quality_of_evidence != null) && (!action.overall_quality_of_evidence.equals("")))
			addSimpleValue(doc, actionElt, EONXSDConstants.QUALITYOFEVIDENCE,action.overall_quality_of_evidence);
		if ((action.references != null) && (!action.references.isEmpty())) {
			for (String reference: action.references)
				addSimpleValue(doc, actionElt, EONXSDConstants.REFERENCE, reference);
		}
	}
	
	private static void addMessageElements(Document doc, Message message, Element messageElt) {
		if ((message.message != null) && (!message.message.equals("")))
			addSimpleValue(doc, messageElt, EONXSDConstants.MESSAGE,message.message);
		if ((message.rule_in_criteria != null) && (!message.rule_in_criteria.equals("")))
			addSimpleValue(doc, messageElt, EONXSDConstants.RULEINCRITERIA,message.rule_in_criteria);
		if ((message.message_type != null) && (!message.message_type.equals("")))
			addSimpleValue(doc, messageElt, EONXSDConstants.MESSAGETYPE,message.message_type);

	}

	private static void addOrderTestProcedureElements(Document doc, Order_TestProcedure action, Element actionElt) {
		if ((action.code != null) && (!action.code.equals("")))
			addSimpleValue(doc, actionElt, EONXSDConstants.CODE,action.code);
		if ((action.coding_system != null) && (!action.coding_system.equals("")))
			addSimpleValue(doc, actionElt, EONXSDConstants.CODINGSYSTEM,action.coding_system);
		if ((action.test_or_procedure != null) && (!action.test_or_procedure.equals("")))
			addSimpleValue(doc, actionElt, EONXSDConstants.TESTORPROCEDURE,action.test_or_procedure	);
		if ((action.when != null) && (!action.when.equals("")))
			addSimpleValue(doc, actionElt, EONXSDConstants.WHEN,action.when);
		addSimpleValue(doc, actionElt, EONXSDConstants.WHENLOWERBOUND, Integer.toString(action.when_lower_bound));
		if ((action.when_lower_bound_unit != null) && (!action.when_lower_bound_unit.equals("")))
			addSimpleValue(doc, actionElt, EONXSDConstants.WHENLOWERBOUNDUNIT,action.when_lower_bound_unit);
		addSimpleValue(doc, actionElt, EONXSDConstants.WHENUPPERBOUND,Integer.toString(action.when_upper_bound));
		if ((action.when_upper_bound_unit != null) && (!action.when_upper_bound_unit.equals("")))
			addSimpleValue(doc, actionElt, EONXSDConstants.WHENUPPERBOUNDUNIT,action.when_upper_bound_unit);

	}

	private static void addReferralElements(Document doc, Referral action, Element actionElt) {
		if ((action.when != null) && (!action.when.equals("")))
			addSimpleValue(doc, actionElt, EONXSDConstants.WHEN,action.when);
		addSimpleValue(doc, actionElt, EONXSDConstants.WHENLOWERBOUND, Integer.toString(action.when_lower_bound));
		if ((action.when_lower_bound_unit != null) && (!action.when_lower_bound_unit.equals("")))
			addSimpleValue(doc, actionElt, EONXSDConstants.WHENLOWERBOUNDUNIT,action.when_lower_bound_unit);
		addSimpleValue(doc, actionElt, EONXSDConstants.WHENUPPERBOUND,Integer.toString(action.when_upper_bound));
		if ((action.when_upper_bound_unit != null) && (!action.when_upper_bound_unit.equals("")))
			addSimpleValue(doc, actionElt, EONXSDConstants.WHENUPPERBOUNDUNIT,action.when_upper_bound_unit);
		if ((action.who_to != null) && (!action.who_to.equals("")))
			addSimpleValue(doc, actionElt, EONXSDConstants.WHOTO,action.who_to);

	}
	public static void makeClinicalAlgorithmNode(Document doc, Element advisoryRoot, Guideline_Service_Record advisory, 
			String guidelineID, KnowledgeBase kb){
		Map messageSets = ClientUtil.collectMessages(advisory, kb);
		try {
			if (!messageSets.isEmpty()) {
				//itsWriter.println("<hr>");
				for (Iterator i=messageSets.keySet().iterator(); i.hasNext();){
					Object key = i.next();
					if (key.equals("")) {
						continue;// itsWriter.println("<li><b> No message type</b>");
					}
					Collection value = (Collection)messageSets.get(key);
					for (Iterator a=value.iterator(); a.hasNext();) {
						Message message = (Message)a.next();
						Element messageElt = doc.createElement(EONXSDConstants.MESSAGEELEMENT);
						if ((message.action_spec_class != null) && (!message.action_spec_class.equals(""))) {
							messageElt.setAttribute("type", message.action_spec_class);
							addSimpleValue(doc, messageElt, EONXSDConstants.ACTIONCLASS,message.action_spec_class);
						}
						addSimpleValue(doc, messageElt, EONXSDConstants.FINEGRAINPRIORTY,Integer.toString(message.fine_grain_priority));
						if ((message.strength_of_recommendation != null) && (!message.strength_of_recommendation.equals("")))
							addSimpleValue(doc, messageElt, EONXSDConstants.GRADERECOMMENDATION,message.strength_of_recommendation);
						if ((message.name != null) && (!message.name.equals("")))
							addSimpleValue(doc, messageElt, EONXSDConstants.NAME,message.name);
						if ((message.net_benefit != null) && (!message.net_benefit.equals("")))
							addSimpleValue(doc, messageElt, EONXSDConstants.NETBENEFIT,message.net_benefit);
						if ((message.level_of_evidence != null) && (!message.level_of_evidence.equals("")))
							addSimpleValue(doc, messageElt, EONXSDConstants.OVERALLQUALITYOFEVIDENCE,message.level_of_evidence);
						if ((message.overall_quality_of_evidence != null) && (!message.overall_quality_of_evidence.equals("")))
							addSimpleValue(doc, messageElt, EONXSDConstants.QUALITYOFEVIDENCE,message.overall_quality_of_evidence);
						if ((message.message != null) && (!message.message.equals("")))
							addSimpleValue(doc, messageElt, EONXSDConstants.MESSAGE,message.message);
						if ((message.rule_in_criteria != null) && (!message.rule_in_criteria.equals("")))
							addSimpleValue(doc, messageElt, EONXSDConstants.RULEINCRITERIA,message.rule_in_criteria);
						if ((message.message_type != null) && (!message.message_type.equals("")))
							addSimpleValue(doc, messageElt, EONXSDConstants.MESSAGETYPE,message.message_type);
						if ((message.references != null) && (!message.references.isEmpty())) {
							for (String reference: message.references)
								addSimpleValue(doc, messageElt, EONXSDConstants.REFERENCE, reference);
						}
						if ((message.subsidiary_message != null) && (!message.subsidiary_message.isEmpty())) {
							for (String subMsg: message.subsidiary_message)
								addSimpleValue(doc, messageElt, EONXSDConstants.SUBSIDIARYMESSAGE, subMsg);
						}
						advisoryRoot.appendChild(messageElt);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void  makeXMLScenarioChoices(Document doc, Element advisoryRoot, Guideline_Service_Record advisory, String guidelineID) {
		try {
			Scenario_Choice selectedSC = getSelectedScenarioChoice(advisory);
			if (selectedSC!= null) {
				Element scElement = doc.createElement(EONXSDConstants.SCENARIOCHOICE);

				addSimpleValue(doc, scElement, EONXSDConstants.NAME,selectedSC.scenario_id.name);
				addSimpleValue(doc, scElement, EONXSDConstants.SCENARIOID,selectedSC.scenario_id.entity_id);
				addSimpleValue(doc, scElement, EONXSDConstants.DESCRIPTION, selectedSC.description);
				addSimpleValue(doc, scElement, EONXSDConstants.PREFERENCE, selectedSC.preference.toString());
				if (selectedSC.preference_justification != null)
					makeXMLSupport(doc, scElement, EONXSDConstants.SUPPORT, selectedSC.preference_justification.evaluation, guidelineID);
				addSimpleValue(doc, scElement, EONXSDConstants.GUIDELINEID, guidelineID);

				advisoryRoot.appendChild(scElement);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void makeXMLGoals(Document doc, Element advisoryRoot, Guideline_Service_Record advisory, String guidelineID) {
		Guideline_Goal[] goals = getGuidelineGoals(advisory);
		if (goals!=null) {
			for (int i = 0; i < goals.length; i++) {
				Element goalElement = doc.createElement(EONXSDConstants.EVALUATEDGOAL); 

				addSimpleValue(doc, goalElement, EONXSDConstants.REASONFORGOAL, advisory.goals[i].support_for_goal.evaluation.criterion.name);
				addSimpleValue(doc, goalElement, EONXSDConstants.GOAL, advisory.goals[i].goal.criterion.name);
				addSimpleValue(doc, goalElement, EONXSDConstants.ACHIEVED, advisory.goals[i].achieved.toString());
				addSimpleValue(doc, goalElement, EONXSDConstants.SUPPORTINGDATA, advisory.goals[i].goal.support);
				addSimpleValue(doc, goalElement, EONXSDConstants.GUIDELINEID, guidelineID);
				if (advisory.goals[i].kb_goal_id != null) {
					addSimpleValue(doc, goalElement, EONXSDConstants.PERFORMANCEMEASURE, advisory.goals[i].kb_goal_id);
				}

				advisoryRoot.appendChild(goalElement);
			}
		}
	} 

	public static void makeXMLSupport(Document doc, Element scElement, String tag, Criteria_Evaluation evaluation, String guidelineID) {

	}

	public static Element addSimpleValue(Document doc, Element root, String property, String value) {
		Element elt = null;
		if (value != null) {
			elt = doc.createElement(property);
			Text data = doc.createTextNode(value);
			elt.appendChild(data);
			root.appendChild(elt);
		}	
		return elt;
	}

	public static void addSimpleValues(Document doc, Element root, String property, Collection<String> values) {
		if (values != null) {
			for (String value : values) {
				addSimpleValue(doc, root, property, value);
			}
		}
	}	

	public static void makeXMLClassification(Document doc, Element advisoryRoot, Guideline_Service_Record advisory, String guidelineID) {
		if (advisory.subject_classification != null) {
			for (int i=0; i < advisory.subject_classification.length; i++) {
				if (advisory.subject_classification[i] != null) {
					Conclusion conclusion = advisory.subject_classification[i];
					Element classificationElement = doc.createElement(EONXSDConstants.SUBJECTCLASSIFICATION);
					addSimpleValue(doc, classificationElement, EONXSDConstants.PARAMETER,conclusion.parameter);
					addSimpleValue(doc, classificationElement, EONXSDConstants.VALUE,conclusion.value);
					if (conclusion.justification != null)
						makeXMLSupport(doc, classificationElement, EONXSDConstants.SUPPORT, conclusion.justification.evaluation, guidelineID);
					addSimpleValue(doc, classificationElement, EONXSDConstants.GUIDELINEID, guidelineID);
					advisoryRoot.appendChild(classificationElement);
				}
			}
		}
	} 


	public static String makeXMLCriteriaEvaluation(Criteria_Evaluation eval) {
		return "<i> "+eval.criterion.name +
				"</i> evaluate to <b> "+eval.truth_value.toString()+ 
				((eval.support != null && !eval.support.equals("")) ? "</b> because <i>"+
						eval.support : "") +"</i>";
	}

	public static Scenario_Choice getSelectedScenarioChoice(Guideline_Service_Record dssOutput) 
			throws Exception {
		Scenario_Choice sc = null;
		if (dssOutput.scenario_choices != null) {
		if (dssOutput.scenario_choices.length > 0) {
			for (int i=0; i < dssOutput.scenario_choices.length; i++) {
				if (dssOutput.scenario_choices[i] != null) {
					for (int j=0; j< dssOutput.scenario_choices[i].scenarios.length; j++) {
						if (dssOutput.scenario_choices[i].scenarios[j] != null) {
							if (dssOutput.scenario_choices[i].scenarios[j].preference.value() ==
									Preference.preferred.value()) {								
								sc = dssOutput.scenario_choices[i].scenarios[j];
							}}}}}}
		}
		return sc;
	}

	private static Guideline_Goal[] getGuidelineGoals(Guideline_Service_Record dssOutput) {
		if ((dssOutput.goals != null) && (dssOutput.goals.length > 0))
			return dssOutput.goals;
		else return null;	
	}
/*	private static Exclusion[] getExclusions(Guideline_Service_Record dssOutput) {
		if ((dssOutput.exclusions != null) && (dssOutput.exclusions.length > 0))
			return dssOutput.exclusions;
		else return null;	
	}
*/}
