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
package edu.stanford.smi.eon.clients;

import edu.stanford.smi.eon.Dharma.Modify_Activity;
import edu.stanford.smi.eon.Dharma.Action_Specification;
import edu.stanford.smi.eon.PCAServerModule.*;
import edu.stanford.smi.eon.criterion.Criterion;
import edu.stanford.smi.eon.guidelineinterpreter.DharmaPaddaConstants;
import edu.stanford.smi.eon.util.HelperFunctions;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import edu.stanford.smi.protege.model.*;

import org.apache.log4j.*;

public  class ClientUtil {
	private static int debugLevel = 4;     // 0: print debug information
	public ClientUtil() {
	}

	static Logger logger = Logger.getLogger(ClientUtil.class);


	private static String messageType(KnowledgeBase kb, Instance message)
	{
		String messageType = "";
		if (kb == null) {
			System.out.println("messageType: Null knowledge base argument!");
		} else {
			if (message != null) {
				if (message.hasOwnSlot(kb.getSlot("message_type"))) {
					Object value = message.getOwnSlotValue(kb
							.getSlot("message_type"));
					if ((value != null) && (value instanceof Instance)) {
						messageType = ((Instance) value).getName();
					} else {
						if ((value != null) && !(value instanceof Instance)) {
							System.out.println("messageType: " + value.toString()
									+ "is not a legal message type");
						}
					}
				} else {
					System.out.println("messageType: Instance "
							+ message.getBrowserText()
							+ " does not have message_type slot");
				}

			} else {
				System.out.println("messageType: null message argument");
			}}
		return messageType;
	}

	public static void showResultWithKB(Guideline_Service_Record dssOutput,
			PrintWriter itsWriter, KnowledgeBase kb) {
		//itsWriter.println("<hr>");
		/* itsWriter.println("<h2>Guideline Manager Output " // (compliance level: "+
                                                      // complianceLevel
    		+"</h2>");*/
		if (dssOutput == null) {
			System.out.println("Null dssOutput!");
			return;
		}
		String kbSource = kb.getProject().getProjectURI().getSchemeSpecificPart();
		itsWriter.println("<p><b> KB loaded from "+kbSource+"</b>(last modified: "+convertDate((new File(kbSource)).lastModified())+")</p><p></p>");
		printClassification(dssOutput, itsWriter);
		printAssumptions(dssOutput, itsWriter);
		printScenarioChoices(dssOutput,itsWriter);
		printGoals(dssOutput, itsWriter);
		//printMessageTypes(dssOutput, itsWriter, kb);
		printActionChoices(dssOutput,itsWriter, kb);
		printEvaluatedChoices(dssOutput, itsWriter, kb);

	}
	
	private static String convertDate(long datenum) {
		Date date = new Date(datenum);
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		return df.format(date);
	}

	private static void printClassification (Guideline_Service_Record dssOutput,
			PrintWriter itsWriter) {
		if (dssOutput.subject_classification != null) {
			itsWriter.println("<b>Patient classification:</b> <ul>");
			for (int i=0; i < dssOutput.subject_classification.length; i++) {
				if (dssOutput.subject_classification[i] != null) {
					Conclusion classification = (Conclusion)dssOutput.subject_classification[i];
					if (HelperFunctions.isDummyJustification(classification.justification)) {
						itsWriter.println("<li>"+ classification.parameter+": "+
								classification.value.toString());
					} else {
						itsWriter.println("<li>"+ classification.parameter+": "+
								classification.value.toString()+"[because <i> "+
								classification.justification.evaluation.criterion.name +
								" </i> evaluate to <b>" + classification.justification.evaluation.truth_value.toString() +"</b>" +
								(((classification.justification.evaluation.support != null) && 
										(!classification.justification.evaluation.support.equals("null")) ) ? 
												"<i>("+ classification.justification.evaluation.support+")</i>]" : "]"));
					}
				}
			}
			itsWriter.println("</ul>");
		}
	}

	private static void printAssumptions(Guideline_Service_Record dssOutput,
			PrintWriter itsWriter) {
		if (!HelperFunctions.isDummyCriteriaEvaluation(dssOutput.assumption)) {
			itsWriter.println("<b> Assumption:</b> " +
					displayCriteriaEvaluation(dssOutput.assumption)+"<p>");
		}

	}

	private static void printScenarioChoices(Guideline_Service_Record dssOutput,
			PrintWriter itsWriter) {
		if (dssOutput.scenario_choices.length > 0) {
			for (int i=0; i < dssOutput.scenario_choices.length; i++) {
				if (dssOutput.scenario_choices[i] != null) {
					for (int j=0; j< dssOutput.scenario_choices[i].scenarios.length; j++) {
						if (dssOutput.scenario_choices[i].scenarios[j] != null) {
							if (dssOutput.scenario_choices[i].scenarios[j].preference.value() ==
								Preference.preferred.value()) {
								itsWriter.println("<b>Scenario choice:</b> "+
										dssOutput.scenario_choices[i].scenarios[j].scenario_id.name);
							}
						} else itsWriter.println("No scenario chosen");
					}
				} else itsWriter.println("No dssOutput.scenario_choices[" + i+ "]" );
			}
		} else itsWriter.println("No scenario chosen.");

	}

	public static void printGoals (Guideline_Service_Record dssOutput,
			PrintWriter itsWriter) {
		if (dssOutput.goals.length > 0) {
			for (int i = 0; i < dssOutput.goals.length; i++) {
				itsWriter.println("<p>");
				itsWriter.print("<b>Goal:</b> ");
				itsWriter.print(dssOutput.goals[i].goal.criterion.name);
				itsWriter.print("(");
				if ((dssOutput.goals[i].support_for_goal != null) && 
						(dssOutput.goals[i].support_for_goal.evaluation.criterion != null))
					itsWriter.print(dssOutput.goals[i].support_for_goal.evaluation.criterion.name);
				itsWriter.println(")");
				itsWriter.println("<p><b>Reached goal?</b> " +
						dssOutput.goals[i].achieved.toString() + 
						(dssOutput.goals[i].goal.support != null ? 
								"("+ dssOutput.goals[i].goal.support+")" : ""));
			}
		} //else { itsWriter.println("<p><b>No goal</b>"); }

	}
	
	
	private static void printActionChoices (Guideline_Service_Record dssOutput,
			PrintWriter itsWriter) {

		Action_To_Choose actionToChoose;
		String preferenceJustification="";
		Action_Spec_Record[] actionSpecs;
		if ((dssOutput.decision_points != null) 
				&& (dssOutput.decision_points.length > 0)) {
			//itsWriter.println("<hr>");
			itsWriter.println("<p><b>Action Choices</b>");
			itsWriter.println("<ul>");
			for (int i = 0; i < dssOutput.decision_points.length; i++) {
				itsWriter.println("<li><b>"+dssOutput.decision_points[i].current_location.name+"</b><br>");
				itsWriter.println("<ul>");
				for (int j=0; j < dssOutput.decision_points[i].action_choices.length;
				j++) {
					preferenceJustification = "";
					actionToChoose = dssOutput.decision_points[i].action_choices[j];
					if (!HelperFunctions.isDummyActionToChoose(actionToChoose)){
						if (actionToChoose.preference_justification !=null) {
							// if (!actionToChoose.preference_justification..equals("")) {
							preferenceJustification= "("+actionToChoose.preference_justification.role+
							displayCriteriaEvaluation(actionToChoose.preference_justification.evaluation) +")])";

						}
						if (actionToChoose.preference.value() == Preference.preferred.value()) {
							itsWriter.println("<li><b>"+actionToChoose.action_step.name+
									" <font color=FF0000>"+printPreferenceValue(actionToChoose.preference)+
									"</font>" +
									"</b>"+ preferenceJustification);
							actionSpecs = actionToChoose.action_specifications;
							for (int k=0; k< actionSpecs.length;k++) {
								if (actionSpecs[k].action_spec_class.equals("Message")) {
									String messageType = DharmaPaddaConstants.DefaultMessageType;
									if (((Message)actionSpecs[k]).message_type != null)
										messageType = ((Message)actionSpecs[k]).message_type;
									itsWriter.print("["+ messageType + "]");
									if (((Message)actionSpecs[k]).name != null)
										itsWriter.print("label: "+ ((Message)actionSpecs[k]).name + " ");
									itsWriter.println(((Message)actionSpecs[k]).message);
								} else
									itsWriter.println(actionSpecs[k].text);
								//itsWriter.println(actionSpecs[k].action_spec_class);
							}
						} else {
							logger.debug("actionToChoose: "+actionToChoose.action_step.name+
									" preference: "+printPreferenceValue(actionToChoose.preference) +
									" justification: "+ preferenceJustification);
						}
					}
				}
				itsWriter.println("</ul>");
			}
			itsWriter.println("</ul>");
		}
		
	}
	private static void printActionChoices (Guideline_Service_Record dssOutput,
			PrintWriter itsWriter, KnowledgeBase kb) {

		Action_To_Choose actionToChoose;
		String preferenceJustification="";
		Action_Spec_Record[] actionSpecs;
		if ((dssOutput.decision_points != null) 
				&& (dssOutput.decision_points.length > 0)) {
			//itsWriter.println("<hr>");
			itsWriter.println("<p><b>Action Choices</b>");
			itsWriter.println("<ul>");
			for (int i = 0; i < dssOutput.decision_points.length; i++) {
				itsWriter.println("<li><b>"+dssOutput.decision_points[i].current_location.name+"</b><br>");
				itsWriter.println("<ul>");
				for (int j=0; j < dssOutput.decision_points[i].action_choices.length;
				j++) {
					preferenceJustification = "";
					actionToChoose = dssOutput.decision_points[i].action_choices[j];
					if (!HelperFunctions.isDummyActionToChoose(actionToChoose)){
						if (actionToChoose.preference_justification !=null) {
							// if (!actionToChoose.preference_justification..equals("")) {
							preferenceJustification= "("+actionToChoose.preference_justification.role+
							displayCriteriaEvaluation(actionToChoose.preference_justification.evaluation) +")])";

						}
						if (actionToChoose.preference.value() == Preference.preferred.value()) {
							itsWriter.println("<li><b>"+actionToChoose.action_step.name+
									" <font color=FF0000>"+printPreferenceValue(actionToChoose.preference)+
									"</font>" +
									"</b>"+ preferenceJustification);
							actionSpecs = actionToChoose.action_specifications;
							if (actionSpecs.length > 0) {
								printActionSpecs(actionSpecs, itsWriter, kb);
							}
						} else {
							logger.debug("actionToChoose: "+actionToChoose.action_step.name+
									" preference: "+printPreferenceValue(actionToChoose.preference) +
									" justification: "+ preferenceJustification);
						}
					}
				}

				itsWriter.println("</ul>");
			}
			itsWriter.println("</ul>");
		}
	}
	
	private static void printActionSpecs(Action_Spec_Record[] actionSpecs,
			PrintWriter itsWriter, KnowledgeBase kb) {
		itsWriter.println("<ul>");
		for (int k=0; k< actionSpecs.length;k++) {
			itsWriter.print("<li>");
			itsWriter.print(printActionSpec(actionSpecs[k], kb));
			itsWriter.print("</li>");
		}
		itsWriter.println("</ul>");

		
	}

	private static String printActionSpec(Action_Spec_Record actionSpec, KnowledgeBase kb) {
		String sentence = "";
		if (actionSpec.action_spec_class.equals("Message")) {
			String messageType = null;
			if ((actionSpec.action_spec.entity_id != null) && (kb != null)){
				Instance kbMessage = kb.getInstance(actionSpec.action_spec.entity_id);
				if (kbMessage != null) {
					Cls messageTypeCls = ((edu.stanford.smi.eon.Dharma.Message)kbMessage).getmessage_typeValue();
					if (messageTypeCls != null) {
						messageType = messageTypeCls.getName();
					} else
						messageType = DharmaPaddaConstants.DefaultMessageType;
				}
			}
			if (messageType != null)
				sentence += "["+messageType+"] ";
			sentence += ((Message)actionSpec).message;
		} else if (actionSpec.action_spec_class.equals("Order_TestProcedure")) {
			sentence += "Order test or procedure:" + ((Order_TestProcedure)actionSpec).test_or_procedure;
		} else {
			sentence += (actionSpec.action_spec_class != null) ? actionSpec.action_spec_class + " " : "";
			sentence += (actionSpec.name != null) ? actionSpec.name + " " : "";
			sentence += (actionSpec.text != null) ? actionSpec.text + " " : "";

		}
		if ((actionSpec.subsidiary_message != null) && !(actionSpec.subsidiary_message.isEmpty())){
			sentence += "<ul>";
			for (String msg : actionSpec.subsidiary_message) {
				sentence += "<li>" + msg+"</li>";
			}
			sentence += "</ul>";
		}
		return sentence;
	}

	private static String printPreferenceValue(Preference preference) {
		if (preference.value() == Preference.preferred.value())
			return "preferred";
		else return preference.toString();
	}
	private static void printEvaluatedChoices (Guideline_Service_Record dssOutput,
			PrintWriter itsWriter, KnowledgeBase kb) {

		boolean hasComment = false;

		if (dssOutput.evaluated_choices.length > 0) {
			logger.debug("Number of evaluate choices = "+dssOutput.evaluated_choices);
			//itsWriter.println("<hr>");
			for (int i=0; i< dssOutput.evaluated_choices.length; i++) {
				Guideline_Activity_Evaluations evaluations = dssOutput.evaluated_choices[i];
				Evaluation_Type evalType = evaluations.evaluation_type;
				// itsWriter.println("Evaluation "+
				// evaluations.activity_act.toString());
				switch (evalType.value()) {
				case Evaluation_Type._add:
					itsWriter.println("<b>Considerations for adding drug: </b><ul>");
					break;
				case Evaluation_Type._delete:
					itsWriter.println("<b>Considerations for deleting drug: </b><ul>");
					break;
				case Evaluation_Type._substitute:
					itsWriter.println("<b>Considerations for substituting drug: </b><ul>");
					break;
				case Evaluation_Type._change_attribute:
					itsWriter.println("<b>If changing attribute, consider: </b><ul>");
					break;
				default:
					itsWriter.println("Error: unknown action");
				}
				for (int j=0; j < evaluations.evaluations.length; j++) {

					hasComment = false;
					Choice_Evaluation evaluation = evaluations.evaluations[j];
					if (evaluation == null) System.out.println("Null evaluation!");
					switch (evaluation.discriminator().value()) {
					case Evaluation_Type._add:
						hasComment = printAdd(itsWriter, evaluation.add_eval(), kb);
						if (!hasComment) {
							itsWriter.println("<li>No comment");
						}
						itsWriter.println("</ul>");
						break;
					case Evaluation_Type._delete:
						hasComment=printDelete(itsWriter, evaluation.delete_eval(), kb) ;
						if (!hasComment) {
							itsWriter.println("<li>No comment");
						}
						itsWriter.println("</ul>");
						break;
					case Evaluation_Type._substitute:
						itsWriter.println("<li> Substitution: replace ");
						itsWriter.println("<ul>");
						for (int del=0; del < evaluation.substitute_eval().activities_to_replace.length; del++) {
							hasComment = printDelete(itsWriter,  evaluation.substitute_eval().activities_to_replace[del], kb);
							if (!hasComment) {
								itsWriter.println("<li>No comment");
							}
							itsWriter.println("</ul>");
						}

						itsWriter.print(" with ");
						for (int add=0; add < evaluation.substitute_eval().activities_to_start.length; add++) {
							hasComment = printAdd(itsWriter,evaluation.substitute_eval().activities_to_start[add], kb);
							if (!hasComment) {
								itsWriter.println("<li>No comment");
							}
							itsWriter.println("</ul>");
						}
						itsWriter.println("</ul>");
						itsWriter.println();
						break;
					case Evaluation_Type._change_attribute:
						itsWriter.println("<li>"+evaluation.change_attribute_eval().name+"(change "+
								evaluation.change_attribute_eval().attribute_name+ ": "+
								evaluation.change_attribute_eval().change_direction+ (evaluation.change_attribute_eval().level != "" ? " to ": "") +
								evaluation.change_attribute_eval().level +"<ul><li>" +
								evaluation.change_attribute_eval().preference);
						hasComment = printEvaluation(itsWriter, kb, evaluation.change_attribute_eval().beneficial_interactions, 
								evaluation.change_attribute_eval().harmful_interactions, evaluation.change_attribute_eval().compelling_indications, 
								evaluation.change_attribute_eval().contraindications, evaluation.change_attribute_eval().relative_indications, 
								evaluation.change_attribute_eval().relative_contraindications, evaluation.change_attribute_eval().adverse_reactions);
						String evalComment =  mkStringMatchedDataList(
								evaluation.change_attribute_eval().do_not_intensify_conditions);
						if (!evalComment.equals("")) {
							itsWriter.println("<li>do not intensify conditions: "+ evalComment);
						}
						String priorityText = " <li> fine-grained priority: "+evaluation.change_attribute_eval().fine_grain_priority;
						itsWriter.println(priorityText+"</ul>)");
						if (evaluation.change_attribute_eval().messages != null) {
							if (evaluation.change_attribute_eval().messages.length > 0) {
								hasComment = true;
								printActionSpecs(evaluation.change_attribute_eval().messages, itsWriter, kb);
							}
						}
						itsWriter.println();
						break;
					default:
						break;
					}
				}
				itsWriter.println("</ul>");
			}
		}

	}
	public static void showResult(Guideline_Service_Record dssOutput,
			PrintWriter itsWriter, Compliance_Level complianceLevel) {
		//itsWriter.println("<hr>");
//		itsWriter.println("<h2>Guideline Manager Output " // (compliance level: "+
		// complianceLevel
//		+"</h2>");
		if (dssOutput == null) {
			System.out.println("Null dssOutput!");
			return;
		}
		printClassification(dssOutput, itsWriter);
		printAssumptions(dssOutput, itsWriter);
		printScenarioChoices(dssOutput,itsWriter);
		printGoals(dssOutput, itsWriter);
		printActionChoices(dssOutput,itsWriter);
		printEvaluatedChoices(dssOutput, itsWriter, null);
	}

	public static String displayCriteriaEvaluation(Criteria_Evaluation eval) {
		String returnString = "<i> "+eval.criterion.name +
				"</i> evaluate to <b> "+eval.truth_value.toString()+ "</b> "+
				((eval.support != null && !eval.support.equals("") && !eval.support.equals("null")) ? "because <i>"+
				eval.support + "</i>": "");
		return returnString;
	}

	public static void printHeader(PrintWriter itsWriter, String patientId) {
		itsWriter.println("<html><body>"+ "<h1>Case: "+
				patientId+"</h1>");
	}
	
	public static String printHeader(String patientId, String currentTime) {
		String sentence = "<html><body>"+ "<h2>Case: "+
				patientId+"</h2>" + "<h3> CDS Time: "+currentTime+"</h3>"+"\n";
		return sentence;
	}

	public static void printCase(PrintWriter itsWriter, String patientId) {
		itsWriter.println("<h1>Case: " + patientId+ "</h1>");
	}
	
	public static void printHTMLHeader(PrintWriter itsWriter) {
		itsWriter.println("<html><body>");		
	}
	public static void printFooter(PrintWriter itsWriter) {
		itsWriter.println("</body></html>");
	}

	public static String mkStringMatchedDataList(Matched_Data[] matchedDataList) {
		String result="";
		if (matchedDataList != null) {
			for (int i=0; i< matchedDataList.length; i++) {
				if (matchedDataList[i] != null) {
					result= result+ " "+ matchedDataList[i].guideline_term;
					if (matchedDataList[i].data.length > 0) {
						result = result +"(";
						for (int j=0; j< matchedDataList[i].data.length; j++) {
							if (j>0) result=result+", ";
							result=result + matchedDataList[i].data[j];
						}
						result=result+") ";
					}
				}
			}
		}
		return result;
	}
	
	private static boolean printEvaluation(PrintWriter itsWriter, KnowledgeBase kb,
			Matched_Data[] beneficial_interactions, Matched_Data[] harmful_interactions, Matched_Data[] compelling_indications, 
			Matched_Data[] contraindications, Matched_Data[] relative_indications, Matched_Data[] relative_contraindications, 
			Matched_Data[] side_effects) {
		boolean hasComment = false;
		String evalComment;
		evalComment= mkStringMatchedDataList(beneficial_interactions);
		if (!evalComment.equals("")) {
			itsWriter.println("<li>Drugs to partner: "+ evalComment);
			hasComment = true;
		}
		evalComment = mkStringMatchedDataList(harmful_interactions);
		if (!evalComment.equals("")) {
			itsWriter.println("<li>Bad drug partner: "+ evalComment);
			hasComment = true;
		}
		evalComment =  mkStringMatchedDataList(compelling_indications);
		if (!evalComment.equals("")) {
			itsWriter.println("<li><font color=FF0000>Compelling indications: "+
					evalComment+"</font>");
			hasComment = true;
		}
		evalComment = mkStringMatchedDataList(contraindications);
		if (!evalComment.equals("")) {
			itsWriter.println("<li>Contraindications: "+ evalComment);
			hasComment = true;
		}
		evalComment =  mkStringMatchedDataList(relative_indications);
		if (!evalComment.equals("")) {
			itsWriter.println("<li>Relative indications: "+ evalComment);
			hasComment = true;
		}
		evalComment =  mkStringMatchedDataList(relative_contraindications);
		if (!evalComment.equals("")) {
			itsWriter.println("<li>Relative contraindications: "+ evalComment);
			hasComment = true;
		}
		evalComment =  mkADRMatchedDataList(side_effects);
		if (!evalComment.equals("")) {
			itsWriter.println("<li>Adverse reaction: "+ evalComment);
			hasComment = true;
		}

		return hasComment;
	}
	

	private static boolean printAdd(PrintWriter itsWriter, Add_Evaluation evaluation, KnowledgeBase kb) {
		boolean hasComment = false;
		String evalComment;
		itsWriter.println("<li>"+evaluation.name);
		// "(based on "+evaluation.guideline_id.entity_id+")");
		itsWriter.println("<ul>");
		if ((evaluation.specific_drug != null) && !evaluation.specific_drug.isEmpty()) {
			boolean firstDrug = true;
			for (String drug : evaluation.specific_drug) {
				if (firstDrug) {
					itsWriter.print("<li>preferred drug: "+drug);
					firstDrug = false;
				} else {
					itsWriter.print(", "+drug);
				}
			}
			itsWriter.println("</li>");
		}
		hasComment = printEvaluation(itsWriter, kb, evaluation.beneficial_interactions, evaluation.harmful_interactions, 
				evaluation.compelling_indications, evaluation.contraindications, evaluation.relative_indications, 
				evaluation.relative_contraindications, evaluation.side_effects);
		evalComment =  mkStringMatchedDataList(
				evaluation.do_not_start_controllable_conditions);
		if (!evalComment.equals("")) {
			itsWriter.println("<li>Do not add controllable conditions: "+ evalComment);
			hasComment = true;
		}
		evalComment =  mkStringMatchedDataList(
				evaluation.do_not_start_uncontrollable_conditions);
		if (!evalComment.equals("")) {
			itsWriter.println("<li>Do not add uncontrollable conditions: "+ evalComment);
			hasComment = true;
		}

		if (evaluation.prior_use.value() == Truth_Value._true.value()) {
			itsWriter.println("<li>Already being used");
			hasComment = true;
		}
		itsWriter.println("<li>preference: "+printPreferenceValue(evaluation.preference));
		itsWriter.println("<li>fine-grained priority: "+evaluation.fine_grain_priority);
		if (evaluation.messages != null) {
			if (evaluation.messages.length > 0) {
				hasComment = true;
				printActionSpecs(evaluation.messages, itsWriter, kb);
			}
		}

		return hasComment;

	}


	private static String mkADRMatchedDataList(Matched_Data[] adverseReaction) {
		String result="";
		if (adverseReaction != null) {
			for (int i=0; i< adverseReaction.length; i++) {
				if (i > 0)
					result = result+", ";
				result= result+ adverseReaction[i].getDataClass();
				if (adverseReaction[i].getGuidelineTerm() != null)
					result = result+"("+ adverseReaction[i].getGuidelineTerm()+")";
			} 
		}
		return result;
		
	}

	private static boolean printDelete(PrintWriter itsWriter, Delete_Evaluation evaluation, KnowledgeBase kb) {
		boolean hasComment = false;
		String evalComment;
		if (evaluation == null) {
			System.out.println("printDelete: null Delete_Evaluation");
			return hasComment;
		} else {
			// itsWriter.println("<b>If deleting a drug, consider: </b><ul>");
			itsWriter.println("<li>"+evaluation.name+"("+
					evaluation.activity_to_delete +")");
			itsWriter.println("<ul>");
			hasComment = printEvaluation(itsWriter, kb, evaluation.beneficial_interactions, evaluation.harmful_interactions, 
					evaluation.compelling_indications, evaluation.contraindications, evaluation.relative_indications, 
					evaluation.relative_contraindications, evaluation.side_effects);
			itsWriter.println("<li>preference: "+printPreferenceValue(evaluation.preference));
			itsWriter.println("<li>fine-grained priority: "+evaluation.fine_grain_priority);
			if (evaluation.messages != null) {
				if (evaluation.messages.length > 0) {
					hasComment = true;
					printActionSpecs(evaluation.messages, itsWriter, kb);
				}
			}

/*			if (evaluation.messages.length > 0) {
				hasComment = true;
				Action_Spec_Record message;
				String messageText="";
				for (int k=0; k< evaluation.messages.length; k++) {
					message = evaluation.messages[k];
					if (message.text == null)
						messageText = message.name;
					else messageText = message.name+"("+message.text+")";
					itsWriter.println("<li>"+messageText);
				}
			}
*/			
			return hasComment;  }
	}

	public static Map collectMessages(Guideline_Service_Record dssOutput,
			KnowledgeBase kb) {
		Map messageSets = new HashMap();
		Action_To_Choose actionToChoose;
		Guideline_Entity guidelineEntity;
		Action_Spec_Record[] actionSpecs;
		String messageType =null;
		Collection messageTypeCollection=null;
		if (dssOutput.decision_points != null) {
			for (int i = 0; i < dssOutput.decision_points.length; i++) {
				for (int j = 0; j < dssOutput.decision_points[i].action_choices.length; j++) {
					actionToChoose = dssOutput.decision_points[i].action_choices[j];
					if (!HelperFunctions.isDummyActionToChoose(actionToChoose)) {
						if (actionToChoose.preference.value() == Preference.preferred
								.value()) {
							actionSpecs = actionToChoose.action_specifications;
							for (int k = 0; k < actionSpecs.length; k++) {
								guidelineEntity = actionSpecs[k].action_spec;
								if ((guidelineEntity != null) && (guidelineEntity.entity_id !=null)){
									Instance instance = kb.getInstance(guidelineEntity.entity_id);
									if ((instance !=null) && 
											(instance.hasOwnSlot(kb.getSlot("message_type")))) {
										messageType = messageType(kb, instance);
										// System.out.println("Message Type: |"+ messageType +"|");
										if (messageType != null){
											Object value = messageSets.get(messageType);
											if (value == null) {
												messageTypeCollection = new ArrayList();
											} else messageTypeCollection = (Collection)value;
											messageTypeCollection.add(actionSpecs[k]);
											messageSets.put(messageType, messageTypeCollection);
										}
									}
								}

							}
						}
					}
				}
			}
		}
		return messageSets;
	}
	private static void printMessageTypes(Guideline_Service_Record dssOutput,
			PrintWriter itsWriter, KnowledgeBase kb){
		Map messageSets = collectMessages(dssOutput, kb);
		boolean hasPrintedTitle = false;
		if (!messageSets.isEmpty()) {
			//itsWriter.println("<hr>");
			for (Iterator i=messageSets.keySet().iterator(); i.hasNext();){
				Object key = i.next();
				if (key.equals("")) {
					continue;// itsWriter.println("<li><b> No message type</b>");
				}
				else {
					if (!hasPrintedTitle) {
						itsWriter.println("<p><b>Messages by Types</b>");
						itsWriter.println("<ul>");
						hasPrintedTitle = true;
					}
					itsWriter.println("<li><b>"+ key.toString()+"</b>");
				}
				itsWriter.println("<ul>");
				Collection value = (Collection)messageSets.get(key);
				for (Iterator a=value.iterator(); a.hasNext();) {
					Message actionSpec = (Message)a.next();
					String references = "";
					boolean first = true;
					if ((actionSpec.references != null) && (!actionSpec.references.isEmpty())) {
					for (String url: actionSpec.references) {
						if (first) {
							references.concat(": "+url);
							first = false;
						} else
							references.concat(";"+url);
					}
					}
					itsWriter.println("<li>" +actionSpec.message+"("+ references+")");

				}
				if (hasPrintedTitle) itsWriter.println("</ul>");
			}
			itsWriter.println("</ul>");
		}



	}

	public static void showEligResult(PrintWriter itsWriter, Conclusion[] eligResult) {
//		itsWriter.println("<hr>");
//		itsWriter.println("<h2>Yenta Eligibility-Determination Manager Output
//		</h2>");
		if (eligResult == null) {
			System.out.println("Null eligResult!");
			return;
		}
//		itsWriter.println("<b>Eligibility Result:</b> <ul type=\"SQUARE\">");
		String eligibility = "";
		itsWriter.println("<ul type=\"SQUARE\">");
		for (int i=0; i < eligResult.length; i++) {
			itsWriter.println("<p>");
			if (eligResult[i] != null) {
				Conclusion classification = (Conclusion)eligResult[i];
				if (classification.value.equals("true")) eligibility = "eligible";
				else if (classification.value.equals("false")) eligibility = "ineligible";
				else eligibility = "unknown";
				itsWriter.println("<li>Eligibility for "+
						classification.parameter+": <b>" + eligibility + "</b> because "+
						classification.justification.evaluation.criterion.name +
						" </i> evaluate to <b>" + classification.justification.evaluation.truth_value.toString() +"</b>");
				Criteria_Evaluation[] children = classification.justification.evaluation.children;
				itsWriter.println(" <p> Inclusion Criteria:");
				itsWriter.println("<ol>");
				for (int j=0; j< children.length; j++) {
					if (children[j].criterion.name == null) {
						System.out.println(children[j].criterion + " has no name ");
						continue;
					}
					if (!children[j].criterion.name.equals("Negated exclusion criteria")) {
						itsWriter.print("<li><i>"+ children[j].criterion.name+"</i> evaluates to <b>"+
								children[j].truth_value.toString()+ "</b>");
						if (!children[j].support.equals("")) {
							itsWriter.println("(" +children[j].support+")");
						} else itsWriter.println("");
					} else {
						if ((children[j].children.length > 0)&&
								(children[j].children[0].children.length > 0)) {
							itsWriter.println("</ol>  Exclusion Criteria:<ol>");

							for (int k=0; k<children[j].children[0].children.length;k++) {
								itsWriter.print("<li><i>"+ children[j].children[0].children[k].criterion.name+"</i> evaluates to <b>"+
										children[j].children[0].children[k].truth_value.toString()+"</b>");
								if (!children[j].children[0].children[k].support.equals("")) {
									itsWriter.println("(" +children[j].children[0].children[k].support+")");
								} else itsWriter.println("");
							}
						}
					}
				}
				itsWriter.println("</ol>");
			}

		}
		itsWriter.println("</ul>");
	}

}