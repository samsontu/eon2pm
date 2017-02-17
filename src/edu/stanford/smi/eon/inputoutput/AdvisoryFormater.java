package edu.stanford.smi.eon.inputoutput;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.log4j.Logger;

import edu.stanford.smi.eon.PCAServerModule.*;
import edu.stanford.smi.eon.PCAServerModule.Message;
import edu.stanford.smi.eon.PCAServerModule.Order_TestProcedure;
import edu.stanford.smi.eon.PCAServerModule.Referral;
import edu.stanford.smi.eon.siteCustomization.BMIRDataSourceFactory;
import edu.stanford.smi.protege.model.KnowledgeBase;
import gov.va.athena.advisory.*;
import gov.va.athena.advisory.Conclusion;
import gov.va.athena.advisory.impl.*;


public class AdvisoryFormater {
	static Logger logger = Logger.getLogger(AdvisoryFormater.class);

	public static Collection<Advisory> javaTransform(Guideline_Service_Record[] eonAdvisory,
			String caseID, String episodeID, String guidelineName, String sessionTime, String startTime, String stopTime, 
			KnowledgeBase kb) {
		Collection<Advisory> advisories = new ArrayList<Advisory>();
		if (eonAdvisory != null) {
			for (int i = 0; i < Array.getLength(eonAdvisory); i++) {
				Advisory advisory = translateGuidelineServiceRecord(eonAdvisory[i], caseID, episodeID, guidelineName, sessionTime,
						startTime, stopTime);
				if (advisory != null) {
					StringWriter returnStringConstructor = new StringWriter();
					PrintWriter itsWriter = new PrintWriter(returnStringConstructor, true);										
					edu.stanford.smi.eon.clients.ClientUtil.showResultWithKB(eonAdvisory[i], itsWriter, kb);
					String oldHTML = returnStringConstructor.toString();
					advisory.setDebugHTML(oldHTML);
					//outputFile.delete();
					advisories.add(advisory);
				}
			}
		}
		if (advisories.isEmpty()) return null;
		else return advisories;
	}
	
	private static String readFile(File file) throws IOException {

	    StringBuilder fileContents = new StringBuilder((int)file.length());
	    Scanner scanner = new Scanner(file);
	    String lineSeparator = System.getProperty("line.separator");

	    try {
	        while(scanner.hasNextLine()) {        
	            fileContents.append(scanner.nextLine() + lineSeparator);
	        }
	        return fileContents.toString();
	    } finally {
	        scanner.close();
	    }
	}

	public static Advisory translateGuidelineServiceRecord(
			Guideline_Service_Record dssOutput, String caseID, String episodeID, String guidelineName, String sessionTime, 
			String startTime, String stopTime) {
		Advisory advisory = new DefaultAdvisory();
		advisory.setAdvisory_time(sessionTime);
		advisory.setAssumption((dssOutput.assumption == null) ? null : (dssOutput.assumption.support));
		advisory.setCase_id(caseID);
		advisory.setHospitalization_id(episodeID);
		advisory.setStart_time(startTime);
		advisory.setStop_time(stopTime);
		advisory.setGuideline_id(guidelineName);
		Collection<gov.va.athena.advisory.Guideline_Goal> goals = instantiateGuidelineGoals(dssOutput.goals);
		if (goals != null)
			advisory.setEvaluated_goal(goals);
		Collection<Drug_Recommendation> drugRecom = instantiateDrugRecommendations(dssOutput.evaluated_choices);
		if (drugRecom != null) 
			advisory.setDrug_recommendation(drugRecom);
		Collection<Conclusion> concluisons = instantiateConclusions(dssOutput.subject_classification);
		if (concluisons != null) 
			advisory.setPatient_characteristic(concluisons);
		Collection<Action> actions = instantiateActions(dssOutput.decision_points);
		if (actions != null) 
			advisory.setRecommended_action(actions);
		return advisory;
	}


	private static Collection<Action> instantiateActions(
			Guideline_Action_Choices[] decisionPoints) {
		Collection<Action> advisoryActions = new ArrayList<Action>();
		if (decisionPoints != null) {
			for (Guideline_Action_Choices decisionPoint : decisionPoints) {
				if (decisionPoint.action_choices != null) {
					for (Action_To_Choose aChoice : decisionPoint.action_choices) {
						if (aChoice.preference.value() == Preference.preferred.value())
							if (aChoice.action_specifications != null) {
								for (Action_Spec_Record actionSpec : aChoice.action_specifications) {
									Action action = null;
									if (actionSpec instanceof edu.stanford.smi.eon.PCAServerModule.Message)
										action = ((edu.stanford.smi.eon.PCAServerModule.Message)actionSpec).translateToAdvisoryFormat();
									else if (actionSpec instanceof edu.stanford.smi.eon.PCAServerModule.Referral)
										action = ((edu.stanford.smi.eon.PCAServerModule.Referral)actionSpec).translateToAdvisoryFormat();
									else if (actionSpec instanceof edu.stanford.smi.eon.PCAServerModule.Order_TestProcedure)
										action = ((edu.stanford.smi.eon.PCAServerModule.Order_TestProcedure)actionSpec).translateToAdvisoryFormat();
									else
										action = actionSpec.translateToAdvisoryFormat();
									if (!advisoryActions.contains(action))
										advisoryActions.add(action);
								}
							}
					}
				}
			}
		}
		if (!advisoryActions.isEmpty())
			return advisoryActions;
		else return null;
	}

/*	private static Collection<Action> removeDuplicateActions(
			Collection<Action> advisoryActions) {
		Collection<Action> uniqueActions = new ArrayList<Action>();
		for (Action a : advisoryActions) {
			if (!isMember(a, uniqueActions))
				uniqueActions.add(a);�
		}
		if (uniqueActions.isEmpty())
			return null;
			else return uniqueActions;
	}

	private static boolean isMember(Action a, Collection<Action> uniqueActions) {
		boolean isMember = false;
		for (Action ua : uniqueActions) {
			if (actionsEqual(a, ua)) {
				isMember = true;
				break;
			}
		}
		return isMember;
	}

	private static boolean actionsEqual(Action a, Action ua) {
		if ((a instanceof gov.va.athena.advisory.Message) && (ua instanceof gov.va.athena.advisory.Message))
			return	 ((gov.va.athena.advisory.Message)a).getMessage().
						equals(((gov.va.athena.advisory.Message)ua).getMessage());
		else if ((a instanceof gov.va.athena.advisory.Order_TestProcedure) && (ua instanceof gov.va.athena.advisory.Order_TestProcedure))
			return	 ((gov.va.athena.advisory.Order_TestProcedure)a).getTest_or_procedure().
						equals(((gov.va.athena.advisory.Order_TestProcedure)ua).getTest_or_procedure());
		else return ((gov.va.athena.advisory.Action)a).getAction_class().equals(((gov.va.athena.advisory.Action)ua).getAction_class()) && 
				((gov.va.athena.advisory.Action)a).getLabel().equals(((gov.va.athena.advisory.Action)ua).getLabel());
	}
*/
	private static Collection<Conclusion> instantiateConclusions(
			edu.stanford.smi.eon.PCAServerModule.Conclusion[] subject_classification) {
		Collection<gov.va.athena.advisory.Conclusion> advisoryConclusions = new ArrayList<gov.va.athena.advisory.Conclusion>();
		if (subject_classification != null) {
			for (edu.stanford.smi.eon.PCAServerModule.Conclusion GSRconclusion : subject_classification) {
				gov.va.athena.advisory.Conclusion advisoryConclusion = new DefaultConclusion();
				advisoryConclusion.setParameter(GSRconclusion.parameter);
				advisoryConclusion.setValue(GSRconclusion.value);
				advisoryConclusions.add(advisoryConclusion);
			}
		}
		if (!advisoryConclusions.isEmpty()) 
			return advisoryConclusions;
		else return null;

	}

	private static Collection<Drug_Recommendation> instantiateDrugRecommendations(
			Guideline_Activity_Evaluations[] evaluated_choices) {
		Collection<Drug_Recommendation> drugRecommendations = new ArrayList<Drug_Recommendation>();
		if ((evaluated_choices != null) && (evaluated_choices.length > 0)) {
			for (int i=0; i< evaluated_choices.length; i++) {
				Guideline_Activity_Evaluations evaluations = evaluated_choices[i];
				//Each "evaluations" corresponds to a guideline Action_Specification to evaluate a collection of drugs for, e.g., addition
				//In ATHENA Advisory, there is no such grouping of drug actions. If there are two Action Specification to evaluate 2 sets of drugs
				// the drug actions are combined.
				//Evaluation_Type evalType = evaluations.evaluation_type;
				for (int j=0; j < evaluations.evaluations.length; j++) {
					Choice_Evaluation evaluation = evaluations.evaluations[j];
					Drug_Recommendation drugRec = null;
					switch (evaluation.discriminator().value()) {
					case Evaluation_Type._add:
						if (!(evaluation.add_eval().preference
								.equals(Preference.ruled_out))) {
							drugRec = new DefaultAdd_Delete_Drug_Recommendation();
							setAddEvaluation(
									(Add_Delete_Drug_Recommendation) drugRec,
									evaluation.add_eval(), null);
							if (!drugRecommendations.contains(drugRec))
								drugRecommendations.add(drugRec);
						}
						break;
					case Evaluation_Type._delete:
						drugRec = new DefaultAdd_Delete_Drug_Recommendation();
						setDeleteEvaluation((Add_Delete_Drug_Recommendation)drugRec, evaluation.delete_eval(), null);
						if (!drugRecommendations.contains(drugRec))
							drugRecommendations.add(drugRec);
						break;
					case Evaluation_Type._substitute:
						for (int del=0; del < evaluation.substitute_eval().activities_to_replace.length; del++) {
							Delete_Evaluation delEval = evaluation.substitute_eval().activities_to_replace[del];
							for (int add=0; add < evaluation.substitute_eval().activities_to_start.length; add++) {
								Add_Evaluation addEval = evaluation.substitute_eval().activities_to_start[add];
								if (!(addEval.preference.equals(Preference.ruled_out))) {
									Add_Delete_Drug_Recommendation toDelete = new DefaultAdd_Delete_Drug_Recommendation();
									Add_Delete_Drug_Recommendation toAdd = new DefaultAdd_Delete_Drug_Recommendation();
									setAddEvaluation(toAdd, addEval, toDelete);
									setDeleteEvaluation(toDelete, delEval, toAdd);								
									if (!drugRecommendations.contains(drugRec))
										drugRecommendations.add(toDelete);
//									if (!drugRecommendations.contains(drugRec))
//										drugRecommendations.add(toAdd);
								}
							}
						}
						break;
					case Evaluation_Type._change_attribute:
						drugRec = new DefaultIncrease_Decrease_Dose_Recommendation();
						setChangeDoseEvaluation((Increase_Decrease_Dose_Recommendation)drugRec, evaluation.change_attribute_eval());
						drugRecommendations.add(drugRec);
						break;
					default:
						logger.error("Error: unknown evaluated action type: " + evaluation.discriminator().value());
						break;
					}
				}
			}
			
		}
		if (drugRecommendations.isEmpty())
			return null;
		else
			return drugRecommendations;

	}
	
	private static void setAddEvaluation(Add_Delete_Drug_Recommendation toAdd, Add_Evaluation addEval, Add_Delete_Drug_Recommendation replaced) {
		toAdd.setDrug_action_type("add");
		toAdd.setPreference(addEval.preference.toString());
		if (addEval.specific_drug != null)
			toAdd.setSpecific_drug(addEval.specific_drug);
		Collection<Action> collateralActions = new ArrayList<Action>();
		if (addEval.messages != null)
			for (Action_Spec_Record actionSpec : addEval.messages) {
				Action action = actionSpec.translateToAdvisoryFormat();
				collateralActions.add(action);
			}
		if (!collateralActions.isEmpty())
			toAdd.setCollateral_action(collateralActions);
		toAdd.setAssociated_substitution_drug(replaced);
		toAdd.setDrug_class(addEval.name);
		toAdd.setDrug_to_add_string(addEval.description);
		toAdd.setFine_grain_priority(addEval.fine_grain_priority);
		Collection<Evaluated_Drug_Relation> evalDrugRels = translateEvaluatedDrugRelations(addEval.compelling_indications,
				addEval.relative_indications, addEval.contraindications, addEval.relative_contraindications, addEval.beneficial_interactions,
				addEval.harmful_interactions, addEval.side_effects, addEval.do_not_start_controllable_conditions, addEval.do_not_start_uncontrollable_conditions);
		if ((evalDrugRels != null) && (!evalDrugRels.isEmpty()))
			toAdd.setEvaluated_drug_relation(evalDrugRels);
	}

	private static Collection<Evaluated_Drug_Relation> translateEvaluatedDrugRelations(
			Matched_Data[] compelling_indications,
			Matched_Data[] relative_indications,
			Matched_Data[] contraindications,
			Matched_Data[] relative_contraindications,
			Matched_Data[] good_drug_partners,
			Matched_Data[] bad_drug_partners, 
			Matched_Data[] side_effects,
			Matched_Data[] do_not_start_controllable_conditions,
			Matched_Data[] do_not_start_uncontrollable_conditions) {
		Collection<Evaluated_Drug_Relation> drugRels = new ArrayList<Evaluated_Drug_Relation>();
		for (Matched_Data source : compelling_indications) {
			//System.out.println("compelling indication: "+ getCondition(source)) ;
			Evaluated_Drug_Relation rel = new DefaultEvaluated_Drug_Relation();
			rel.setCondition_or_drug(getCondition(source));
			rel.setRelation_type("compelling_indication");
			drugRels.add(rel);
		}
		for (Matched_Data source : relative_indications) {
			//System.out.println("relative indication: "+ getCondition(source)) ;
			Evaluated_Drug_Relation rel = new DefaultEvaluated_Drug_Relation();
			rel.setCondition_or_drug(getCondition(source));
			rel.setRelation_type("relative_indication");
			drugRels.add(rel);
		}
		for (Matched_Data source : contraindications) {
			Evaluated_Drug_Relation rel = new DefaultEvaluated_Drug_Relation();
			rel.setCondition_or_drug(getCondition(source));
			rel.setRelation_type("strong_contraindication");
			drugRels.add(rel);
		}
		for (Matched_Data source : relative_contraindications) {
			Evaluated_Drug_Relation rel = new DefaultEvaluated_Drug_Relation();
			rel.setCondition_or_drug(getCondition(source));
			rel.setRelation_type("relative_contraindication");
			drugRels.add(rel);
		}
		for (Matched_Data source : good_drug_partners) {
			Evaluated_Drug_Relation rel = new DefaultEvaluated_Drug_Relation();
			rel.setCondition_or_drug(getCondition(source));
			rel.setRelation_type("good_drug_partner");
			drugRels.add(rel);
		}
		for (Matched_Data source : bad_drug_partners) {
			Evaluated_Drug_Relation rel = new DefaultEvaluated_Drug_Relation();
			rel.setCondition_or_drug(getCondition(source));
			rel.setRelation_type("bad_drug_partner");
			drugRels.add(rel);
		}
		for (Matched_Data source : side_effects) {
			Evaluated_Drug_Relation rel = new DefaultEvaluated_Drug_Relation();
			rel.setCondition_or_drug(getCondition(source));
			// ToDo
			rel.setSubstance(getSubstance(source));
			rel.setRelation_type("adverse_reaction");
			drugRels.add(rel);
		}
		if (do_not_start_controllable_conditions != null) {
			for (Matched_Data source : do_not_start_controllable_conditions) {
				Evaluated_Drug_Relation rel = new DefaultEvaluated_Drug_Relation();
				rel.setCondition_or_drug(getCondition(source));
				rel.setRelation_type("do_not_add_controllable_condition");
				drugRels.add(rel);
			}
		}
		if (do_not_start_uncontrollable_conditions != null)
			for (Matched_Data source : do_not_start_uncontrollable_conditions) {
				Evaluated_Drug_Relation rel = new DefaultEvaluated_Drug_Relation();
				rel.setCondition_or_drug(getCondition(source));
				rel.setRelation_type("do_not_add_uncontrollable_condition");
				drugRels.add(rel);
			}
		if (drugRels.isEmpty())
			return null;
		else
			return drugRels;
	}
		
	private static String getCondition(Matched_Data source) {
		String result = source.guideline_term;
/*		String data = "";
		for (int j= 0; j< source.data.length; j++) {
			if (j>0) data=data+", ";
			data=data + source.data[j];
		};
		if ((source.data.length >0) && !data.equals(result))
			return result+"("+data+")";
		else 
			return result;
*/	
		return result;
		}
	
	private static String getSubstance(Matched_Data source) {
		return source.data_class;
	}

	private static void setDeleteEvaluation(Add_Delete_Drug_Recommendation toDelete, Delete_Evaluation delEval, 
			Add_Delete_Drug_Recommendation replacing) {
		toDelete.setDrug_action_type(GlobalVars.discontinue);
		if (delEval.name != null) {
			toDelete.setPreference(delEval.preference.toString());
			Collection<String> drugToDelete = new ArrayList<String>();
			drugToDelete.add(delEval.name);
			toDelete.setSpecific_drug(drugToDelete);
			Collection<Action> collateralActions = new ArrayList<Action>();
			if (delEval.messages != null)
				for (Action_Spec_Record actionSpec : delEval.messages) {
					Action action = actionSpec.translateToAdvisoryFormat();
					collateralActions.add(action);
				}
			if (!collateralActions.isEmpty())
				toDelete.setCollateral_action(collateralActions);
			toDelete.setAssociated_substitution_drug(replacing);
			toDelete.setDrug_class(delEval.activity_to_delete);
			toDelete.setFine_grain_priority(delEval.fine_grain_priority);
			Collection<Evaluated_Drug_Relation> evalDrugRels = translateEvaluatedDrugRelations(delEval.compelling_indications,
					delEval.relative_indications, delEval.contraindications, delEval.relative_contraindications, delEval.beneficial_interactions,
					delEval.harmful_interactions, delEval.side_effects, null, null);
			if (evalDrugRels != null && !evalDrugRels.isEmpty())
				toDelete.setEvaluated_drug_relation(evalDrugRels);
		} else {
			logger.error("Null specific drug in Delete evaluation");
		}

	}

	private static void setChangeDoseEvaluation(Increase_Decrease_Dose_Recommendation toChange, Change_Attribute_Evaluation changeEval) {
		toChange.setPreference(changeEval.preference.toString());
		toChange.setFine_grain_priority(changeEval.fine_grain_priority);
		Collection<Evaluated_Drug_Relation> drugRels = translateEvaluatedDrugRelations(changeEval.compelling_indications,
				changeEval.relative_indications, changeEval.contraindications, changeEval.relative_contraindications, changeEval.beneficial_interactions,
				changeEval.harmful_interactions, changeEval.adverse_reactions, null, null);
		// add adverse reactions to specific drug
		for (Matched_Data source : changeEval.do_not_intensify_conditions) {
			Evaluated_Drug_Relation rel = new DefaultEvaluated_Drug_Relation();
			rel.setCondition_or_drug(getCondition(source));
			rel.setRelation_type("do_not_intensify_condition");
			drugRels.add(rel);
		}
		if (changeEval.change_direction.equals(Direction.up))
			toChange.setDrug_action_type("increase_dose");
		else
			toChange.setDrug_action_type("decrease_dose");
		Collection<String>drugs = new ArrayList<String>();
		drugs.add(changeEval.name);
		toChange.setSpecific_drug(drugs);
		if (!drugRels.isEmpty())
			toChange.setEvaluated_drug_relation(drugRels);
		Collection<Action> collateralActions = new ArrayList<Action>();
		if (changeEval.messages != null)
			for (Action_Spec_Record actionSpec : changeEval.messages) {
				Action action = actionSpec.translateToAdvisoryFormat();
				collateralActions.add(action);
			}
		if (!collateralActions.isEmpty())
			toChange.setCollateral_action(collateralActions);

	}

	private static Collection<gov.va.athena.advisory.Guideline_Goal> instantiateGuidelineGoals(
			edu.stanford.smi.eon.PCAServerModule.Guideline_Goal[] goals) {
		Collection<gov.va.athena.advisory.Guideline_Goal> advisoryGoals = new ArrayList<gov.va.athena.advisory.Guideline_Goal>();
		if (goals != null) {
			for (edu.stanford.smi.eon.PCAServerModule.Guideline_Goal goal : goals) {
				gov.va.athena.advisory.Guideline_Goal advisoryGoal = new DefaultGuideline_Goal();
				if (goal.achieved != null) advisoryGoal.setAchieved(goal.achieved.toString());
				advisoryGoal.setData(goal.goal.support); //goals[i].goal.support+
				if (goal.goal != null) advisoryGoal.setGoal(goal.goal.criterion.name); // For CDS KB, goal.goal is the criterion to achieve
				else if (goal.guideline_entity != null) 
					advisoryGoal.setGoal(goal.guideline_entity.name); //For performance measures, There may be multiple criteria to achieve. 
				else advisoryGoal.setGoal(goal.kb_goal_id);
				if (goal.support_for_goal != null) advisoryGoal.setReason_for_goal(goal.support_for_goal.evaluation.criterion.name);
				advisoryGoal.setFine_grain_priority(goal.fine_grain_priority);
				advisoryGoal.setPrimary(goal.primary);
				advisoryGoal.setKb_goal_id(goal.kb_goal_id);
				advisoryGoal.setCriterion_Evaluation(instantiateCriterionEvaluation(goal.criteria_evaluation));
				advisoryGoals.add(advisoryGoal);
			}
		} else {
			logger.error("Null goals");
		}
		if (!advisoryGoals.isEmpty()) 
			return advisoryGoals;
		else return null;
	}

	private static Collection<gov.va.athena.advisory.Missing_Data> instantiateMissingData(Collection<Criteria_Evaluation> evals) {
		Collection<gov.va.athena.advisory.Missing_Data> pmMissingData = new ArrayList<gov.va.athena.advisory.Missing_Data>();
		if ((evals != null) && (!evals.isEmpty())) {
			for (Criteria_Evaluation eval: evals){
				gov.va.athena.advisory.Missing_Data pmMissingDatum = new DefaultMissing_Data();
				pmMissingDatum.setCriterion_evaluation_result(eval.truth_value.toString());
				pmMissingDatum.setCriterion_id(eval.criterion.name);
				pmMissingDatum.setParameter(eval.parameter);
				pmMissingData.add(pmMissingDatum);
			}
		}
		return pmMissingData;
	}

	private static Collection<gov.va.athena.advisory.Criterion_Evaluation> instantiateCriterionEvaluation(Collection<Criteria_Evaluation> evals) {
		Collection<gov.va.athena.advisory.Criterion_Evaluation> pmEvals = new ArrayList<gov.va.athena.advisory.Criterion_Evaluation>();
		if (evals != null) {
			for (Criteria_Evaluation eval: evals){
				gov.va.athena.advisory.Criterion_Evaluation pmEval = new DefaultCriterion_Evaluation();
				pmEval.setMissing_Data(instantiateMissingData(eval.missingDataCriteriaEval));
				pmEval.setCriterion_evaluation_result(eval.truth_value.toString());
				pmEval.setCriterion_id(eval.criterion.name);
				pmEval.setCriterion_type(eval.criterion_type);
				pmEval.setData(eval.support);
				pmEval.setKb_goal_id(eval.parameter);
				pmEvals.add(pmEval);
			}
		}
		return pmEvals;

	}

	

}
