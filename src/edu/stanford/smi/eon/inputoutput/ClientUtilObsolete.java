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
package edu.stanford.smi.eon.inputoutput;

import edu.stanford.smi.eon.PCAServerModule.*;
import edu.stanford.smi.eon.guidelineinterpreter.DharmaPaddaConstants;
import edu.stanford.smi.eon.util.HelperFunctions;

import java.io.*;
import java.util.*;

import edu.stanford.smi.protege.model.*;

import org.apache.log4j.*;

public  class ClientUtilObsolete {
	
	public ClientUtilObsolete() {
	}

	static Logger logger = Logger.getLogger(ClientUtilObsolete.class);


	public static String messageType(KnowledgeBase kb, Instance message)
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

	public static String showResultWithKB(Guideline_Service_Record dssOutput,
			KnowledgeBase kb) {
		//itsPrintWriter.println("<hr>");
		/* itsWriter.println("<h2>Guideline Manager Output " // (compliance level: "+
                                                      // complianceLevel
    		+"</h2>");*/
		String sentence = "";
		if (dssOutput == null) {
			System.out.println("Null dssOutput!");
			return sentence;
		}
		sentence += printClassification(dssOutput);
		sentence += printAssumptions(dssOutput);
		sentence += printScenarioChoices(dssOutput);
		sentence += printGoals(dssOutput);
		sentence += printMessageTypes(dssOutput, kb);
		sentence += printActionChoices(dssOutput, kb);
		sentence += printEvaluatedChoices(dssOutput);
		return sentence;
	}

	private static String printClassification (Guideline_Service_Record dssOutput)	{
		String sentence = "";
		if (dssOutput.subject_classification != null) {
			sentence += "<b>Patient classification:</b> " + "<ul>" + "\n";
			for (int i=0; i < dssOutput.subject_classification.length; i++) {
				if (dssOutput.subject_classification[i] != null) {
					Conclusion classification = (Conclusion)dssOutput.subject_classification[i];
					if (HelperFunctions.isDummyJustification(classification.justification)) {
						sentence += "<li>"+ classification.parameter+": "+
								classification.value.toString() + "\n";
					} else {
						sentence += "<li>"+ classification.parameter+": "+
								classification.value.toString()+"[because <i> "+
								classification.justification.evaluation.criterion.name +
								" </i> evaluate to <b>" + classification.justification.evaluation.truth_value.toString() +"</b><i>("+
								classification.justification.evaluation.support+")</i>]" + "\n";
					}
				}
			}
			sentence += "</ul>" + "\n";
		}
		return sentence;
	}

	private static String printAssumptions(Guideline_Service_Record dssOutput) {
		String sentence = "";
		if (!HelperFunctions.isDummyCriteriaEvaluation(dssOutput.assumption)) {
			sentence += "<b> Assumption:</b> " +
					displayCriteriaEvaluation(dssOutput.assumption)+"<p>" + "\n";
		}
		return sentence;
	}

	private static String printScenarioChoices(Guideline_Service_Record dssOutput) {
		String sentence = "";
		if (dssOutput.scenario_choices.length > 0) {
			for (int i=0; i < dssOutput.scenario_choices.length; i++) {
				if (dssOutput.scenario_choices[i] != null) {
					for (int j=0; j< dssOutput.scenario_choices[i].scenarios.length; j++) {
						if (dssOutput.scenario_choices[i].scenarios[j] != null) {
							if (dssOutput.scenario_choices[i].scenarios[j].preference.value() ==
								Preference.preferred.value()) {
								sentence += "<b>Scenario choice:</b> "+
										dssOutput.scenario_choices[i].scenarios[j].scenario_id.name + "\n";
							}
						} else {
							sentence += "No scenario chosen" + "\n";
							System.out.println("dssOutput.scenario_choices[i].scenarios[j] == null");
						}
					}
				} else sentence += "No dssOutput.scenario_choices[" + i+ "]" + "\n";
			}
		} else {
			sentence += "No scenario chosen." + "\n";
			System.out.println("dssOutput.scenario_choices.length <= 0");
		}
		return sentence;
	}

	private static String printGoals (Guideline_Service_Record dssOutput) {
		String sentence = "";
		if (dssOutput.goals.length > 0) {
			for (int i = 0; i < dssOutput.goals.length; i++) {
				sentence += "<p>" + "\n";
				sentence += "<b>Goal:</b> ";
				sentence += dssOutput.goals[i].goal.criterion.name;
				sentence += "(";
				sentence += dssOutput.goals[i].support_for_goal.evaluation.criterion.name;
				sentence += ")" + "\n";
				sentence += "<p><b>Reached goal?</b> " +
						dssOutput.goals[i].achieved.toString() + "("+
						dssOutput.goals[i].goal.support+")" + "\n";
			}
		} //else { itsWriter.println("<p><b>No goal</b>"); }
		return sentence;
	}
	
	private static String printActionChoices (Guideline_Service_Record dssOutput) {
		String sentence = "";
		Action_To_Choose actionToChoose;
		String preferenceJustification="";
		Action_Spec_Record[] actionSpecs;
		if ((dssOutput.decision_points != null) 
				&& (dssOutput.decision_points.length > 0)) {
			//itsWriter.println("<hr>");
			sentence += "<p><b>Action Choices</b>" + "\n";
			sentence += "<ul>" + "\n";
			for (int i = 0; i < dssOutput.decision_points.length; i++) {
				sentence += "<li><b>"+dssOutput.decision_points[i].current_location.name+"</b><br>" + "\n";
				sentence += "<ul>" + "\n";
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
							sentence += "<li><b>"+actionToChoose.action_step.name+
									" <font color=FF0000>"+printPreferenceValue(actionToChoose.preference)+
									"</font>" +
									"</b>"+ preferenceJustification + "\n";
							sentence += "<ul>" + "\n";
							actionSpecs = actionToChoose.action_specifications;
							for (int k=0; k< actionSpecs.length;k++) {
								sentence = sentence += "<li>"+printActionSpec(actionSpecs[k], null) +"</li><\n>";
								//itsWriter.println(actionSpecs[k].action_spec_class);
							}
							sentence += "</ul>" + "\n";
						} else {
							logger.debug("actionToChoose: "+actionToChoose.action_step.name+
									" preference: "+printPreferenceValue(actionToChoose.preference) +
									" justification: "+ preferenceJustification);
						}
					}
				}
				sentence += "</ul>" + "\n";
			}
			sentence += "</ul>" + "\n";
		}
		return sentence;
	}
	private static String printActionChoices (Guideline_Service_Record dssOutput,
			KnowledgeBase kb) {
		String sentence = "";
		Action_To_Choose actionToChoose;
		String preferenceJustification="";
		Action_Spec_Record[] actionSpecs;
		if ((dssOutput.decision_points != null) 
				&& (dssOutput.decision_points.length > 0)) {
			//itsWriter.println("<hr>");
			sentence += "<p><b>Action Choices</b>" + "\n";
			sentence += "<ul>" + "\n";
			for (int i = 0; i < dssOutput.decision_points.length; i++) {
				sentence += "<li><b>"+dssOutput.decision_points[i].current_location.name+"</b><br>" + "\n";
				sentence += "<ul>" + "\n";
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
							sentence += "<li><b>"+actionToChoose.action_step.name+
									" <font color=FF0000>"+printPreferenceValue(actionToChoose.preference)+
									"</font>" +
									"</b>"+ preferenceJustification + "\n";
							actionSpecs = actionToChoose.action_specifications;
							sentence += "<ul>" + "\n";
							for (int k=0; k< actionSpecs.length;k++) {
								sentence += "<li>"+ printActionSpec(actionSpecs[k], kb) +"\n";
								//itsWriter.println(actionSpecs[k].action_spec_class);
								//Action_Specification actionSpec = (Action_Specification)kb.getInstance(actionSpecs[k].action_spec.entity_id);
								//actionSpec.printContent( itsWriter,  "",  null,
								//	     null,  null);
							}
							sentence += "</ul>" + "\n";
						} else {
							logger.debug("actionToChoose: "+actionToChoose.action_step.name+
									" preference: "+printPreferenceValue(actionToChoose.preference) +
									" justification: "+ preferenceJustification);
						}
					}
				}
				
				

				sentence += "</ul>" + "\n";
			}
			sentence += "</ul>" + "\n";
		}
		return sentence;
	}

	private static String printPreferenceValue(Preference preference) {
		if (preference.value() == Preference.preferred.value())
			return "preferred";
		else return preference.toString();
	}
	private static String printEvaluatedChoices (Guideline_Service_Record dssOutput) {
		String sentence = "";
		boolean hasComment = false;
		Vector<Object> hasCommentAndHTMLContent =null;
		if (dssOutput.evaluated_choices.length > 0) {
			//itsWriter.println("<hr>");
			for (int i=0; i< dssOutput.evaluated_choices.length; i++) {
				Guideline_Activity_Evaluations evaluations = dssOutput.evaluated_choices[i];
				Evaluation_Type evalType = evaluations.evaluation_type;
				// itsWriter.println("Evaluation "+
				// evaluations.activity_act.toString());
				switch (evalType.value()) {
				case Evaluation_Type._add:
					sentence += "<b>Considerations for adding drug: </b><ul>";
					break;
				case Evaluation_Type._delete:
					sentence += "<b>Considerations for deleting drug: </b><ul>"+"<br>";
					break;
				case Evaluation_Type._substitute:
					sentence += "<b>Considerations for substituting drug: </b><ul>";
					break;
				case Evaluation_Type._change_attribute:
					sentence += "<b>If changing attribute, consider: </b><ul>";
					break;
				default:
					sentence += "Error: unknown action";
				}
				for (int j=0; j < evaluations.evaluations.length; j++) {

					hasComment = false;
					Choice_Evaluation evaluation = evaluations.evaluations[j];
					if (evaluation == null) System.out.println("Null evaluation!");
					switch (evaluation.discriminator().value()) {
					case Evaluation_Type._add:
						hasCommentAndHTMLContent = printAdd(evaluation.add_eval());
						if (hasCommentAndHTMLContent == null) {
							hasComment = false;
						} else {
							hasComment = ((Boolean)hasCommentAndHTMLContent.firstElement());
							sentence += (String)hasCommentAndHTMLContent.lastElement();
						}
						if (!hasComment) {
							sentence += "<li>No comment";
						}
						sentence += "</ul>";
						break;
					case Evaluation_Type._delete:
						hasCommentAndHTMLContent=printDelete(evaluation.delete_eval()) ;
						if (hasCommentAndHTMLContent == null) {
							hasComment = false;
						} else {
							hasComment = ((Boolean)hasCommentAndHTMLContent.firstElement());
							sentence += (String)hasCommentAndHTMLContent.lastElement();
						}
						if (!hasComment) {
							sentence += "<li>No comment";
						}
						sentence += "</ul>";
						break;
					case Evaluation_Type._substitute:
						sentence += "<li> Substitution: replace ";
						sentence += "<ul>";
						for (int del=0; del < evaluation.substitute_eval().activities_to_replace.length; del++) {
							hasCommentAndHTMLContent = printDelete(evaluation.substitute_eval().activities_to_replace[del]);
							if (hasCommentAndHTMLContent == null) {
								hasComment = false;
							} else {
								hasComment = ((Boolean)hasCommentAndHTMLContent.firstElement());
								sentence += (String)hasCommentAndHTMLContent.lastElement();
							}
							if (!hasComment) {
								sentence += "<li>No comment";
							}
							sentence += "</ul>";
						}

						sentence += " with ";
						for (int add=0; add < evaluation.substitute_eval().activities_to_start.length; add++) {
							hasCommentAndHTMLContent = printAdd(evaluation.substitute_eval().activities_to_start[add]);
							if (hasCommentAndHTMLContent == null) {
								hasComment = false;
							} else {
								hasComment = ((Boolean)hasCommentAndHTMLContent.firstElement());
								sentence += (String)hasCommentAndHTMLContent.lastElement();
							}
							if (!hasComment) {
								sentence += "<li>No comment";
							}
							sentence += "</ul>";
						}
						sentence += "</ul>";
						
						break;
					case Evaluation_Type._change_attribute:
						sentence += "<li>"+evaluation.change_attribute_eval().name+"(change "+
								evaluation.change_attribute_eval().attribute_name+ ": "+
								evaluation.change_attribute_eval().change_direction+ " to " +
								evaluation.change_attribute_eval().level + ")"+"<br>";
						sentence += "<br>";
						break;
					default:
						break;
					}
				}
				sentence += "</ul>";
			}
		}
		return sentence;
	}
	public static void showResult(Guideline_Service_Record dssOutput,
			Writer itsWriter, Compliance_Level complianceLevel) {
		String sentence = "";
		if (dssOutput == null) {
			System.out.println("showResult: Null dssOutput!");
		} else {
			sentence += printClassification(dssOutput);
			sentence += printAssumptions(dssOutput);
			sentence += printScenarioChoices(dssOutput);
			sentence += printGoals(dssOutput);
			sentence += printActionChoices(dssOutput);
			sentence += printEvaluatedChoices(dssOutput);
			try {
				itsWriter.write(sentence);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static String displayCriteriaEvaluation(Criteria_Evaluation eval) {
		return "<i> "+eval.criterion.name +
		"</i> evaluate to <b> "+eval.truth_value.toString()+ 
			((eval.support != null && !eval.support.equals("")) ? "</b> because <i>"+
			eval.support : "</b>") +"</i>";
	}

	public static String printHeader(String patientId, String currentTime) {
		String sentence = "<html><body>"+ "<h2>Case: "+
				patientId+"</h2>" + "<h3> CDS Time: "+currentTime+"</h3>"+"\n";
		return sentence;
	}
	public static void printFooter(PrintWriter itsWriter) {
		itsWriter.println("</body></html>");
	}

	public static String mkAdverseEffect(Matched_Data[] matchedDataList) {
		String result="";
		if (matchedDataList != null) {
			for (int i=0; i< matchedDataList.length; i++) {
				if (i > 0) result= result+ ",";
				result = result+" " + matchedDataList[i].guideline_term +"(";
				for (int j=0; j< matchedDataList[i].data.length; j++) {
					if (j>0) result=result+", ";
					result=result + matchedDataList[i].data[j];
				}
				result=result+") ";
			}
		}
		return result;

	}
	public static String mkStringMatchedDataList(Matched_Data[] matchedDataList) {
		String result="";
		if (matchedDataList != null) {
			for (int i=0; i< matchedDataList.length; i++) {
				if (i > 0)
					result= result+ ", "+ matchedDataList[i].guideline_term ; //+"(";
				else 
					result = result+" " + matchedDataList[i].guideline_term ;
//				for (int j=0; j< matchedDataList[i].data.length; j++) {
//					if (j>0) result=result+", ";
//					result=result + matchedDataList[i].data[j];
//				}
//				result=result+") ";
			}
		}
		return result;
	}
	
	private static Vector<Object> printAdd( Add_Evaluation evaluation) {
		boolean hasComment = false;
		String content = "";
		String evalComment;
		Vector<Object> hasCommentAndHTMLContent = new Vector(2);
		content += "<li>"+evaluation.name;
		// "(based on "+evaluation.guideline_id.entity_id+")");
		content += "<ul>";
		if (evaluation.prior_use.value() == Truth_Value._true.value()) {
			content += "<li>Already being used";
			hasComment = true;
		}
		evalComment= mkStringMatchedDataList(
				evaluation.beneficial_interactions);
		if (!evalComment.equals("")) {
			content += "<li>Drugs to partner: "+ evalComment;
			hasComment = true;
		}
		evalComment = mkStringMatchedDataList(
				evaluation.harmful_interactions);
		if (!evalComment.equals("")) {
			content += "<li>Drugs with bad interactions: "+ evalComment;
			hasComment = true;
		}
		evalComment =  mkStringMatchedDataList(
				evaluation.compelling_indications);
		if (!evalComment.equals("")) {
			content += "<li><font color=FF0000>Compelling indications: "+
					evalComment+"</font>";
			hasComment = true;
		}
		evalComment = mkStringMatchedDataList(
				evaluation.contraindications);
		if (!evalComment.equals("")) {
			content += "<li>Contraindications: "+ evalComment;
			hasComment = true;
		}
		evalComment =  mkStringMatchedDataList(
				evaluation.relative_indications);
		if (!evalComment.equals("")) {
			content += "<li>Relative indications: "+ evalComment;
			hasComment = true;
		}
		evalComment =  mkStringMatchedDataList(
				evaluation.relative_contraindications);
		if (!evalComment.equals("")) {
			content += "<li>Relative contraindications: "+ evalComment;
			hasComment = true;
		}
		evalComment =  mkAdverseEffect(
				evaluation.side_effects);
		if (!evalComment.equals("")) {
			content += "<li>Adverse events: "+ evalComment;
			hasComment = true;
		}
		if (evaluation.messages != null) {
			if (evaluation.messages.length > 0) {
				hasComment = true;
				Action_Spec_Record message;
				String messageText = "";
				content += "<ul>\n";
				for (int k = 0; k < evaluation.messages.length; k++) {
					content += "<li>" + printActionSpec(evaluation.messages[k], null);
				}
				content += "</ul>\n";
			}
		}
		content += "<li>preference: "+printPreferenceValue(evaluation.preference);
		
		hasCommentAndHTMLContent.addElement(new Boolean(hasComment));
		hasCommentAndHTMLContent.addElement(content);
		return hasCommentAndHTMLContent; 

	}

	private static Vector<Object> printDelete( Delete_Evaluation evaluation) {
		boolean hasComment = false;
		Vector<Object> hasCommentAndHTMLContent = new Vector(2);
		String content = "";
		String evalComment;
		if (evaluation == null) {
			System.out.println("printDelete: null Delete_Evaluation");
			return null;
		} else {
			// itsWriter.println("<b>If deleting a drug, consider: </b><ul>");
			content += "<li>"+evaluation.name+"("+
					evaluation.activity_to_delete +")";
			content += "<ul>";
			evalComment= mkAdverseEffect(
					evaluation.side_effects);
			if (!evalComment.equals("")) {
				content += "<li>Adverse reaction: ";
				hasComment = true;
			}
			evalComment= mkStringMatchedDataList(
					evaluation.beneficial_interactions);
			if (!evalComment.equals("")) {
				content += "<li>Drugs to partner: ";
				hasComment = true;
			}
			evalComment = mkStringMatchedDataList(
					evaluation.harmful_interactions);
			if (!evalComment.equals("")) {
				content += "<li>Drugs with bad interactions: ";
				hasComment = true;
			}
			evalComment =  mkStringMatchedDataList(
					evaluation.compelling_indications);
			if (!evalComment.equals("")) {
				content += "<li>Compelling indications: ";
				hasComment = true;
			}
			evalComment = mkStringMatchedDataList(
					evaluation.contraindications);
			if (!evalComment.equals("")) {
				content += "<li>Contraindications: ";
				hasComment = true;
			}
			evalComment =  mkStringMatchedDataList(
					evaluation.relative_indications);
			if (!evalComment.equals("")) {
				content += "<li>Relative indications: ";
				hasComment = true;
			}
			evalComment =  mkStringMatchedDataList(
					evaluation.relative_contraindications);
			if (!evalComment.equals("")) {
				content += "<li>Relative contraindications: ";
				hasComment = true;
			}
			if (evaluation.messages.length > 0) {
				hasComment = true;
				Action_Spec_Record message;
				String messageText="";
				for (int k=0; k< evaluation.messages.length; k++) {
					message = evaluation.messages[k];
					if (message.text == null)
						messageText = message.name;
					else messageText = message.name+"("+message.text+")";
					content += "<li>"+messageText;
				}
			}
			hasCommentAndHTMLContent.addElement(new Boolean(hasComment));
			hasCommentAndHTMLContent.addElement(content);
			return hasCommentAndHTMLContent;  
		}
	}



	protected static Map collectMessages(Guideline_Service_Record dssOutput,
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
	protected static String printMessageTypes(Guideline_Service_Record dssOutput,
			KnowledgeBase kb)	{
		String sentence = "";
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
						sentence += "<p><b>Messages by Types</b>" +"\n";
						sentence += "<ul>" + "\n";
						hasPrintedTitle = true;
					}
					sentence += "<li><b>"+ key.toString()+"</b>" +"\n";
				}
				sentence += "<ul>" + "\n";
				Collection value = (Collection)messageSets.get(key);
				for (Iterator a=value.iterator(); a.hasNext();) {
					Action_Spec_Record actionSpec = (Action_Spec_Record)a.next();
					sentence = sentence += "<li>"+printActionSpec(actionSpec, kb) +"</li>";
				}
				if (hasPrintedTitle) sentence += "</ul>" + "\n";
			}
			sentence += "</ul>" + "\n";
		}
		return sentence;
	}
	
	private static String printActionSpec(Action_Spec_Record actionSpec, KnowledgeBase kb) {
		String sentence = "";
		if (actionSpec.action_spec_class.equals("Message")) {
			String messageType = DharmaPaddaConstants.DefaultMessageType;;
			if (actionSpec.action_spec.entity_id != null) {
				if (kb != null) {
					Instance kbMessage = kb.getInstance(actionSpec.action_spec.entity_id);
					if (kbMessage != null) {
						Cls messageTypeCls = ((edu.stanford.smi.eon.Dharma.Message)kbMessage).getmessage_typeValue();
						if (messageTypeCls != null) {
							messageType = messageTypeCls.getName();
						}
					}
				}
			}
			if (messageType != null)
				sentence += "["+messageType+"] ";
			sentence += ((Message)actionSpec).message;
		} else
			sentence += actionSpec.text;
		return sentence;
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