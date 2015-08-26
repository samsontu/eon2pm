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

// Created on Tue Dec 12 11:31:16 PST 2000
// Copyright Stanford University 2000

package edu.stanford.smi.eon.athenaextensions;

import java.util.*;
import java.io.PrintWriter;

import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.util.*;
import edu.stanford.smi.eon.util.*;
import edu.stanford.smi.eon.guidelineinterpreter.*;
import edu.stanford.smi.eon.PCAServerModule.*;
import edu.stanford.smi.eon.PCAServerModule.Message;
import edu.stanford.smi.eon.kbhandler.*;
import edu.stanford.smi.eon.datahandler.*;
import edu.stanford.smi.eon.time.*;
import edu.stanford.smi.eon.criterion.*;
import edu.stanford.smi.eon.Dharma.*;

import org.apache.log4j.*;
/** 
 */
public class Presence_Message extends On_Screen_Message {

	public Presence_Message(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
	static Logger logger = Logger.getLogger(Presence_Message.class);

	public void setentry_typeValue(Cls entry_type) {
		ModelUtilities.setOwnSlotValue(this, "entry_type", entry_type);	}
	public Cls getentry_typeValue() {
		return ((Cls) ModelUtilities.getOwnSlotValue(this, "entry_type"));
	}

	public void setdomain_termValue(Cls domain_term) {
		ModelUtilities.setOwnSlotValue(this, "domain_term", domain_term);	}
	public Cls getdomain_termValue() {
		return ((Cls) ModelUtilities.getOwnSlotValue(this, "domain_term"));
	}

	public void setfilterValue(Instance filter) {
		ModelUtilities.setOwnSlotValue(this, "filter", filter);	}
	public Instance getfilterValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "filter"));
	}

	public void setassesed_statusValue(String assessed_status) {
		ModelUtilities.setOwnSlotValue(this, "assessed_status", assessed_status);	}
	public String getassessed_statusValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "assessed_status"));
	}
	public void setpresenceValue(boolean presence) {
		ModelUtilities.setOwnSlotValue(this, "presence", new  Boolean(presence));	}
	public boolean ispresenceValue() {
		if (ModelUtilities.getOwnSlotValue(this, "presence") == null) return false;
		else 
			return ((Boolean) ModelUtilities.getOwnSlotValue(this, "presence")).booleanValue();
	}
	// __Code above is automatically generated. Do not change



	public Action_Spec_Record  evaluateActionSpec(GuidelineInterpreter guidelineManager) {
		if (getrule_in_conditionValue() != null) {
			Criteria_Evaluation evaluation = HelperFunctions.dummyCriteriaEvaluation();
			try {
				evaluation = (Criteria_Evaluation) ((Criterion)getrule_in_conditionValue()).evaluate(guidelineManager, false);
				if (!(PCAInterfaceUtil.mapTruthValue(evaluation.truth_value)))  {   // rule-in condition does not hold
					return null;
				}
			} catch (PCA_Session_Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		String description = getdescriptionValue();
		String message ="";
		String entryType = getentry_typeValue().getName();

		KBHandler kbManager = guidelineManager.getKBmanager();
		edu.stanford.smi.eon.datahandler.DataHandler dataManager=
				guidelineManager.getDBmanager();
		logger.debug("Presence_Message.evaluateActionSpec "+
				getBrowserText()+"/"+getName());
		if (entryType != null) {
			if (entryType.equals("Medication")) {
				message = doMedication(guidelineManager, kbManager,dataManager);
			} else if (entryType.equals("Adverse_Reaction") ){
				message = doAdverseReaction(guidelineManager, kbManager,dataManager);
			} else  message = getmessageValue();
		} else message = getmessageValue();
		if (!message.equals("")) {
			Action_Spec_Record actionSpec = new Message(getlabelValue(),
					description,
					"Message", 0,
					this.makeGuideline_Entity(guidelineManager.getCurrentGuidelineID()),
					HelperFunctions.dummyJustification(), null, null, null, null,null, message, null, null, null,null
					);
			logger.debug("Presence_Message.evaluateActionSpec return message "+message);
			/*	    java.lang.String name,
	    java.lang.String text,
	    java.lang.String action_spec_class,
	    edu.stanford.smi.eon.PCAServerModule.Guideline_Entity action_spec,
	    edu.stanford.smi.eon.PCAServerModule.Justification justification,
	    java.lang.String level_of_evidence,
	    java.lang.String net_benefit,
	    java.lang.String overall_quality_of_evidence,
	    java.lang.String strength_of_recommendation,
	    Collection<String> references,
	    java.lang.String message,
	    int fine_grain_priority,
	    java.lang.String message_type,
	    Collection<java.lang.String> subsidiary_messages,
	    java.lang.String rule_in_criteria
			 */     return actionSpec;
		} else return null;
	}

	private String doFilter(Filter filter, Matched_Data presenceData, GuidelineInterpreter guidelineManager) {
		Collection matchedResult=null;
		String message="";
		try {
			if ((matchedResult =
					filter.evaluate(presenceData, guidelineManager)) != null){

				for (Iterator i = matchedResult.iterator(); i.hasNext();) {
					String condition="";
					Matched_Data result = (Matched_Data)i.next();

					for (int j = 0; j<result.data.length; j++) {
						condition = condition+ result.data[j];

						if ((j < (result.data.length - 1)) && !(condition.equals("")))
							condition = condition+", ";
					}
					if (!(condition.equals("")))
						condition = condition +
						" reported as an adverse reaction/allergy to "
						+ result.guideline_term+ ".";
					else condition = "adverse reaction/allergy of "+result.guideline_term+". ";
					message = "The patient had "+condition;
				}
				if (getmessageValue() != null)
					message = message + getmessageValue();
			}
		} catch (Exception ex){
			if (getmessageValue() != null)
				message = getmessageValue();
			logger.error("Presence_Message: evaluateActionSpec - Exception evaluating filter" + ex.getMessage(), ex);

		}
		return message;
	}

	private String doAdverseReaction(GuidelineInterpreter guidelineManager, KBHandler kbManager ,
			edu.stanford.smi.eon.datahandler.DataHandler dataManager) {
		String message = "";
		if (getdomain_termValue().getName() != null) {
			// domain_term should be a class of drug
			// find those instances of Advrese_Reaction such that substance is or is a subclass of
			// domain_term.
			Collection adverseReactions = dataManager.getAdverseEvents(getdomain_termValue());
			if ((adverseReactions !=null) && !adverseReactions.isEmpty()) {
				message = "The patient had ";
				for (Iterator i=adverseReactions.iterator(); i.hasNext();) {
					Adverse_Reaction adverseReaction = (Adverse_Reaction)i.next();
					String text = ((adverseReaction.getDomain_term() != null) &&
							!(adverseReaction.getDomain_term().equals(""))) ?
									adverseReaction.getDomain_term()+ " reported as an adverse reaction/allergy to " :
										" adverse reaction/allergy to ";
					message = message + text + adverseReaction.getSubstance();
					if (i.hasNext()) message = message + ", ";
					else message = message+". ";
				}
				if (getmessageValue() != null)
					message = message + getmessageValue();
			}
		}
		return message;
	}

	public void printContent(PrintWriter itsWriter, String delimiter, String context,
			Criterion ruleIn, Criterion ruleOut) {
		itsWriter.print(getName());
		itsWriter.print(delimiter);
		itsWriter.print(this.getDirectType().getName());
		itsWriter.print(delimiter);
		Object msgTypeObj = getmessage_typeValue();
		if (msgTypeObj != null) itsWriter.print(((Cls)msgTypeObj).getName());
		else itsWriter.print(" ");
		itsWriter.print(delimiter);
		itsWriter.print(context);
		itsWriter.print(delimiter);
		if (ruleIn != null) ruleIn.printContent(itsWriter);
		itsWriter.print(delimiter);
		if (ruleOut != null) ruleOut.printContent(itsWriter);
		itsWriter.print(delimiter);
		Object domain_term = getdomain_termValue();
		if (domain_term != null) itsWriter.print(((Cls)domain_term).getName());
		else itsWriter.print(" ");
		itsWriter.print(delimiter);
		Object msgObj = getmessageValue();
		if (msgObj != null) itsWriter.print((String)msgObj);
		itsWriter.println("");
	}

	private String doMedication(GuidelineInterpreter guidelineManager, KBHandler kbManager,
			edu.stanford.smi.eon.datahandler.DataHandler dataManager) {
		Matched_Data presenceData;
		String message=getmessageValue();
		String entryType = getentry_typeValue().getName();
		String assessedStatus = null;
		Instance filter=null;
		logger.debug("Presence_Message.doMedication "+getassessed_statusValue());
		if ((presenceData =
				guidelineManager.containSubclassOf(entryType,
						getdomain_termValue().getName(),
						dataManager.currentActivities("Medication", null))
				) != null) {
			if ((filter = getfilterValue()) !=null) {
				return doFilter((Filter)filter, presenceData, guidelineManager);
				// filter should only be used when doing join with other class of data
			} else {
				Collection criteria = new ArrayList();
				if (getassessed_statusValue() != null) {
					criteria.add(new  WhereComparisonFilter("prescription_id", "eq",
							getassessed_statusValue()));
				}
				if (getdomain_termValue() != null) {
					criteria.add(new  WhereComparisonFilter("patient_id", "eq",
							guidelineManager.getDBmanager().getCaseID()));
				}
				edu.stanford.smi.eon.kbhandler.WhereFilter  selectionCriteria = 
						new edu.stanford.smi.eon.kbhandler.WhereFilter(DharmaPaddaConstants.AND, criteria);
				Collection matchedMedications = guidelineManager.getKBmanager().findInstances(
						"Medication", selectionCriteria, guidelineManager);
				logger.debug("Presence_Message.doMedication matchedMedications # "+matchedMedications.size());
				if ((matchedMedications != null) && !(matchedMedications.isEmpty())) {
					// find <slotname>
					String slotName = null;
					while  ((slotName = getSlotName(message)) != null) {
						logger.debug("Presence_Message.doMedication slotName = "+slotName);
						message = replaceString(message, slotName, matchedMedications);
					}
					return message;
				}
			}
		}
		return message;
	}

	private String getSlotName(String message) {
		// find first slotname enclosed in "<" and ">"
		int rightDelimiter = 0;
		int leftDelimiter = message.indexOf('<');
		if (leftDelimiter < 0) return null;
		else {
			rightDelimiter = message.indexOf('>', leftDelimiter);
			if (rightDelimiter > leftDelimiter) {
				return message.substring(leftDelimiter+1, rightDelimiter);
			} else return null;
		}
	}

	private String replaceString(String message, String slotName, Collection
			matchedMedications) {

		Slot slot = getKnowledgeBase().getSlot(slotName);
		if (slot != null) {
			String replacementString = null;
			boolean first = true;
			for (Iterator i=matchedMedications.iterator(); i.hasNext();) {
				Instance inst = (Instance) i.next();
				if (first) {
					Object obj = inst.getOwnSlotValue(slot);
					if (obj != null)
						replacementString = obj.toString();
					else replacementString = "null slotName";
					first = false;
				} else replacementString = replacementString+", "+  inst.getOwnSlotValue(slot);
			}
			int leftDelimiter = message.indexOf('<');
			int rightDelimiter = message.indexOf('>');
			message = message.substring(0, leftDelimiter)+replacementString+
					message.substring(rightDelimiter+1, message.length());
		}
		return message;

	}

}

