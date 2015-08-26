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

/** 
 */
public class On_Screen_Message extends edu.stanford.smi.eon.Dharma.Message {


	public On_Screen_Message(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}

	public void setmessageValue(String message) {
		ModelUtilities.setOwnSlotValue(this, "message", message);	}
	public String getmessageValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "message"));
	}



	// __Code above is automatically generated. Do not change

	public Action_Spec_Record  evaluateActionSpec(GuidelineInterpreter gmanager) {
		if (getrule_in_conditionValue() != null) {
			Criteria_Evaluation evaluation = HelperFunctions.dummyCriteriaEvaluation();
			try {
				evaluation = (Criteria_Evaluation) ((Criterion)getrule_in_conditionValue()).evaluate(gmanager, false);
				if (!(PCAInterfaceUtil.mapTruthValue(evaluation.truth_value)))  {   // rule-in condition does not hold
					return null;
				}
			} catch (PCA_Session_Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		String message =  getmessageValue();
	    Collection<Instance> references = getreferencesValue();
	    Collection<String> urls = new ArrayList<String>();
	    for (Instance reference : references) 
	    	urls.addAll(reference.getOwnSlotValues(this.getKnowledgeBase().getSlot("URL")));
		Action_Spec_Record actionSpec = new edu.stanford.smi.eon.PCAServerModule.Message(
				getlabelValue(),
				getdescriptionValue(), "Message",
				getfine_grain_priorityValue(),
				this.makeGuideline_Entity(gmanager.getCurrentGuidelineID()),
				HelperFunctions.dummyJustification(),
				(getlevel_of_evidenceValue() != null) ? getlevel_of_evidenceValue().getBrowserText() : null, 
				(getnet_benefitValue() != null) ? getnet_benefitValue().getBrowserText() : null, 
				(getquality_of_evidenceValue() != null) ? getquality_of_evidenceValue().getBrowserText() : null,
				(getstrength_of_recommendationValue() != null) ? getstrength_of_recommendationValue().getBrowserText(): null, 
				urls,
				message = ((message != null) ? message : ""),
				(getmessage_typeValue() != null) ? getmessage_typeValue().getName(): DharmaPaddaConstants.DefaultMessageType,
				getsubsidiary_messageValue(),
				null,
				getCollateralActions(gmanager)
				);
		//	    java.lang.String name,
		//	    java.lang.String text,
		//	    java.lang.String action_spec_class,
		//	    edu.stanford.smi.eon.PCAServerModule.Guideline_Entity action_spec,
		//	    edu.stanford.smi.eon.PCAServerModule.Justification justification,
		//	    java.lang.String level_of_evidence,
		//	    java.lang.String net_benefit,
		//	    java.lang.String quality_of_evidence,
		//	    java.lang.String strength_of_recommendation,
		//	    Collection<String> references,
		//	    java.lang.String message,
		//	    int fine_grain_priority,
		//	    java.lang.String message_type,
		//	    Collection<java.lang.String> subsidiary_messages,
		//	    java.lang.String rule_in_criteria

		return actionSpec;
	}

	//  java.lang.String name,
	//  java.lang.String text,
	//  java.lang.String action_spec_class,
	//  edu.stanford.smi.eon.PCAServerModule.Guideline_Entity action_spec,
	//  edu.stanford.smi.eon.PCAServerModule.Justification justification,
	//  java.lang.String level_of_evidence,
	//  java.lang.String net_benefit,
	//  java.lang.String quality_of_evidence,
	//  java.lang.String strength_of_recommendation,
	//  Collection<String> references,
	//  String message,  
	//  int fine_grain_priority,
	//  java.lang.String message_type,
	//  Collection<java.lang.String> subsidiary_messages,
	//  java.lang.String rule_in_criteria


	public void printContent(PrintWriter itsWriter, String delimiter, String context,
			Criterion ruleIn, Criterion ruleOut) {

		// action_class type, context, rule-in, rule-out, additional_condition, text

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
		itsWriter.print(delimiter);
		Object msgObj = getmessageValue();
		if (msgObj != null) itsWriter.print((String)msgObj);
		itsWriter.println("");
	}

}
