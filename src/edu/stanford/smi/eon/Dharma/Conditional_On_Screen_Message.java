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
public class Conditional_On_Screen_Message extends On_Screen_Message {

	public Conditional_On_Screen_Message(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
  static Logger logger = Logger.getLogger(Conditional_On_Screen_Message.class);

	public void setrule_in_conditionValue(Instance rule_in_condition) {
		ModelUtilities.setOwnSlotValue(this, "rule_in_condition", rule_in_condition);	}
	public Instance getrule_in_conditionValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "rule_in_condition"));
	}
// __Code above is automatically generated. Do not change
  public Action_Spec_Record  evaluateActionSpec(GuidelineInterpreter gmanager) {
    Action_Spec_Record actionSpec = null;
    String criterionText = null;
    try {
      Criteria_Evaluation eval = null;
      Instance criterion = getrule_in_conditionValue();
      if (criterion !=null)  {
         eval = ((Criterion) criterion).evaluate(
              gmanager, false);
         criterionText = criterion.getBrowserText();
        logger.debug("Conditional_On_Screen_Message.evaluateActionSpec: "+eval.support
        +" : "+eval.truth_value);
      }
      if ((criterion == null) || (eval.truth_value.equals(Truth_Value._true))) {
        String message =  getmessageValue();
        message = (message != null) ? message : "";
	    Collection<Instance> references = getreferencesValue();
	    Collection<String> urls = new ArrayList<String>();
	    for (Instance reference : references) 
	    	urls.addAll(reference.getOwnSlotValues(this.getKnowledgeBase().getSlot("URL")));
		actionSpec = new edu.stanford.smi.eon.PCAServerModule.Message(
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
				criterionText,
				getCollateralActions(gmanager)
				);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return actionSpec;
  }
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
    Object moreRuleIn = getrule_in_conditionValue();
    if (moreRuleIn != null) ((Criterion)moreRuleIn).printContent(itsWriter);
    itsWriter.print(delimiter);
    Object msgObj = getmessageValue();
    if (msgObj != null) itsWriter.print((String)msgObj);
    itsWriter.println("");
  }


}
