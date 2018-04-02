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

// Created on Mon Sep 17 13:27:22 PDT 2001
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
 *  Action_Like_Step (Superclass hint for Java class generation)
<p>This class is deprecated
<p>Class representing an alternative for a Choice_Step. 
 */
public class Action_Choice extends Action_Like_Step {

	public Action_Choice(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
  static Logger logger = Logger.getLogger(Action_Choice.class);

	public void setrule_in_conditionValue(Instance rule_in_condition) {
		ModelUtilities.setOwnSlotValue(this, "rule_in_condition", rule_in_condition);	}
	public Instance getrule_in_conditionValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "rule_in_condition"));
	}

	public void setrule_out_conditionValue(Instance rule_out_condition) {
		ModelUtilities.setOwnSlotValue(this, "rule_out_condition", rule_out_condition);	}
	public Instance getrule_out_conditionValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "rule_out_condition"));
	}

	public void setdefault_preferenceValue(String default_preference) {
		ModelUtilities.setOwnSlotValue(this, "default_preference", default_preference);	}
	public String getdefault_preferenceValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "default_preference"));
	}

	public void setstrict_rule_out_conditionValue(Instance strict_rule_out_condition) {
		ModelUtilities.setOwnSlotValue(this, "strict_rule_out_condition", strict_rule_out_condition);	}
	public Instance getstrict_rule_out_conditionValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "strict_rule_out_condition"));
	}

	public void setstrict_rule_in_conditionValue(Instance strict_rule_in_condition) {
		ModelUtilities.setOwnSlotValue(this, "strict_rule_in_condition", strict_rule_in_condition);	}
	public Instance getstrict_rule_in_conditionValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "strict_rule_in_condition"));
	}

	public void setactionsValue(Collection actions) {
		ModelUtilities.setOwnSlotValues(this, "actions", actions);	}
	public Collection getactionsValue(){
		return  ModelUtilities.getOwnSlotValues(this, "actions");
	}
// __Code above is automatically generated. Do not change

	public Action_To_Choose evaluateChoice(GuidelineInterpreter interpreter,
      Compliance_Level compliance ) throws PCA_Session_Exception
  {
    Preference preference =  PCAInterfaceUtil.mapPreference(getdefault_preferenceValue());
    Justification justification = HelperFunctions.dummyJustification();
    Criteria_Evaluation evaluation = HelperFunctions.dummyCriteriaEvaluation();
    // construct execution tree
    logger.debug("in Choice:evaluateChoice - "+this.getlabelValue()+" compliance level: "+
      compliance);
    if (compliance.equals(Compliance_Level.strict)) {
      if (getstrict_rule_out_conditionValue() != null) {
         evaluation = (Criteria_Evaluation) ((Criterion)getstrict_rule_out_conditionValue()).evaluate(interpreter, false);
         justification = new Justification(evaluation, "strict rule-out condition");
         if (PCAInterfaceUtil.mapTruthValue(evaluation.truth_value)) {   // rule-out condition holds, go away
          preference = Preference.ruled_out;
          return constructChoice(preference, justification, this, interpreter.getCurrentGuidelineID(),
            interpreter);
         }
      }
      // rule_out condition doesn't exist or doesn't hold
      if (getstrict_rule_in_conditionValue() != null) {
         //logger.debug(this.getlabelValue()+ " rule in " + getstrict_rule_in_conditionValue().getName());
         evaluation = (Criteria_Evaluation) ((Criterion)getstrict_rule_in_conditionValue()).evaluate(interpreter, false);
         justification = new Justification(evaluation, "strict rule-in condition");
         if (PCAInterfaceUtil.mapTruthValue(evaluation.truth_value))  {   // rule-in condition holds
          preference = Preference.preferred;
         }
      }

    } else {
      if (getrule_out_conditionValue() != null) {
         evaluation = (Criteria_Evaluation) ((Criterion)getrule_out_conditionValue()).evaluate(interpreter, false);
         justification = new Justification(evaluation, "permissive rule-out condition");
         if (PCAInterfaceUtil.mapTruthValue(evaluation.truth_value)) {   // rule-out condition holds, go away
          preference = Preference.ruled_out;
          return constructChoice(preference, justification, this, interpreter.getCurrentGuidelineID(),
            interpreter);
         }
      }
      // rule_out condition doesn't exist or doesn't hold
      if (getrule_in_conditionValue() != null) {
        //logger.debug(this.getlabelValue()+ " rule in " + getrule_in_conditionValue().getName());
         evaluation = (Criteria_Evaluation) ((Criterion)getrule_in_conditionValue()).evaluate(interpreter, false);
         justification = new Justification(evaluation, "permissive rule-in condition");
         if (PCAInterfaceUtil.mapTruthValue(evaluation.truth_value)) {   // rule-in condition holds
          preference = Preference.preferred;
          }
      }
    }
    // construct return type
    logger.debug("in Action_Choice:evaluateChoice - "+this.getlabelValue()+" preference: "+preference);
    return this.constructChoice(preference, justification, this, interpreter.getCurrentGuidelineID(),
      interpreter);
  }

    protected Action_To_Choose constructChoice(Preference preference,
    Justification support,
    Action_Choice actionChoice,
    String guidelineID,
    GuidelineInterpreter gmanager) {

    // Construct steps as an array of Action_Spec_Record
    Action_Spec_Record step;
    Collection<Action_Spec_Record> steps = new ArrayList<Action_Spec_Record>();
    if (preference.equals(Preference.preferred)) {
    		for (Action_Specification i: (Collection<Action_Specification>)getactionsValue()) {
    			step = i.evaluateActionSpec(gmanager);
    			if (step != null) steps.add(step);
    		}
    }
    Action_To_Choose choice = new Action_To_Choose (getName(), preference,
      support, (Action_Spec_Record[]) steps.toArray(new Action_Spec_Record[0]),
      makeGuideline_Entity(guidelineID), "");
    return choice;
  }
  public void tryNext(GuidelineInterpreter glmanager, Compliance_Level compliance)
    throws PCA_Session_Exception {

    Instance nextInstance = getfollowed_byValue();
    logger.debug("in action choice: tryNext of "+this.getlabelValue());
    if (nextInstance != null) {
      Clinical_Algorithm_Entity nextEntity = (Clinical_Algorithm_Entity)nextInstance;
      logger.debug("in action choice: tryNext nextEntity "+nextEntity.getBrowserText()+
    		  "/"+ nextEntity.getName());
      if (!nextEntity.isNewEncounter())
        nextEntity.doStep(glmanager, compliance);
    }
  }

  public void printContent(PrintWriter itsWriter, String delimiter) {
    Collection actions = getactionsValue();
    Object ruleIn = getstrict_rule_in_conditionValue();
    Object ruleOut = getstrict_rule_out_conditionValue();
    if (actions == null) {
      itsWriter.println(this.getName()+" contains no action");
    } else {
      for (Iterator i=actions.iterator(); i.hasNext();) {
        Action_Specification act = (Action_Specification)i.next();
        act.printContent(itsWriter, delimiter, getlabelValue(),
          (ruleIn !=null) ? (Criterion)ruleIn : null,
          (ruleOut !=null) ? (Criterion)ruleOut: null);
      }
    }
  }

}
