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
<p>Action steps that are part of a consultation guideline
 */
public class Consultation_Action_Step extends Consultation_Guideline_Entity {

	public Consultation_Action_Step(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
  static Logger logger = Logger.getLogger(Consultation_Action_Step.class);

	public void setrule_outValue(Instance rule_out) {
		ModelUtilities.setOwnSlotValue(this, "rule_out", rule_out);	}
	public Instance getrule_outValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "rule_out"));
	}

	public void setactionsValue(Collection actions) {
		ModelUtilities.setOwnSlotValues(this, "actions", actions);	}
	public Collection<Action_Specification> getactionsValue(){
		return  ModelUtilities.getOwnSlotValues(this, "actions");
	}

	public void setfollowed_byValue(Instance followed_by) {
		ModelUtilities.setOwnSlotValue(this, "followed_by", followed_by);	}
	public Instance getfollowed_byValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "followed_by"));
	}

	public void setrule_inValue(Instance rule_in) {
		ModelUtilities.setOwnSlotValue(this, "rule_in", rule_in);	}
	public Instance getrule_inValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "rule_in"));
	}
// __Code above is automatically generated. Do not change

  public Action_To_Choose evaluateAction(GuidelineInterpreter gmanager) {
    // "choices" is the guideline_action_choice of which this action step
    // is a part. If "choices" is null, then create a new one.
    // returns a collection of Guideline_Action_Choices, the current decision
    // point and maybe the one (if one exists) following this action step,

    logger.debug("In Consultation_Action_Step: evaluateAction - "+getlabelValue());
    boolean doIt = true;
    Collection<Action_Specification> actionSpecs; // Collection of Action_Specification
    Collection<Action_Spec_Record> actionSpecRecords = new ArrayList<Action_Spec_Record>();  //collection of Action_Spec_Record
    Action_Spec_Record actionSpecRecord;
    Criteria_Evaluation actionEval = null;
    Justification justification = HelperFunctions.dummyJustification();
    Action_To_Choose choice = null;
    Preference preference = Preference.neutral;
    if (getrule_outValue() != null) {
      // evaluate ruleout condition
      try {
        actionEval = ((Criterion)getrule_outValue()).evaluate(gmanager, false);
        if (actionEval != null) {
          if (actionEval.truth_value.equals(Truth_Value._true)) {
            doIt=false;
          }
        }
      } catch (PCA_Session_Exception e) {
        logger.error("Error in Consultation_Action_Step "+ getlabelValue()+": evaluating ruleout: "+e.getMessage());
      }
    }
    if (getrule_inValue() != null) {
        logger.debug("ConsultationActionStep: "+((Criterion)getrule_inValue()).getlabelValue());
      try {
        actionEval = ((Criterion)getrule_inValue()).evaluate(gmanager, false);
        if (actionEval != null) {
          // logger.debug("ConsultationActionStep: "+((Criterion)getrule_inValue()).getlabelValue()+ actionEval.toString());
          justification = new Justification(actionEval, "rule in criterion");
          if (actionEval.truth_value.equals(Truth_Value._true)) {
            logger.debug("ConsultationActionStep: "+((Criterion)getrule_inValue()).getlabelValue()+ " evaluate to true");
            preference=Preference.preferred;
          } else {
            logger.debug("ConsultationActionStep: "+((Criterion)getrule_inValue()).getlabelValue()+ " does not evaluate to true");
            doIt = false;
          }
        } else {
            logger.debug("ConsultationActionStep: "+((Criterion)getrule_inValue()).getlabelValue()+ " evaluate to null");

        }
      } catch (PCA_Session_Exception e) {
        logger.error("Error in Consultation_Action_Step "+ getlabelValue()+": evaluating rulein "+e.getMessage());
      }
    }
    if (doIt == true) {
    	actionSpecs = getactionsValue();
    	if (actionSpecs != null) {
    		// check to see if any actions has criterion that need to be evaluated
    		// Construct the action_specs as an array of Guideline_Entity
    		for (Action_Specification act : actionSpecs) {
    			if ((actionSpecRecord =act.evaluateActionSpec(gmanager)) != null){
    				actionSpecRecords.add(actionSpecRecord);
    			}
    			try {
    				act.doAction(null, null, gmanager);
    			} catch (PCA_Session_Exception e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		}
    	}
    	choice = new Action_To_Choose(
    			getName(),
    			preference,
    			justification,
    			(Action_Spec_Record[]) actionSpecRecords.toArray( new Action_Spec_Record[0]),
    			makeGuideline_Entity(gmanager.getCurrentGuidelineID()),
    			"");

    }
    return choice;
  }
  
  



/*  public void  doStep(GuidelineInterpreter gmanager,
      Compliance_Level compliance)throws PCA_Session_Exception  {
    tryNext(gmanager, compliance);
  }
*/
  public void tryNext(GuidelineInterpreter glmanager, Compliance_Level compliance)
    throws PCA_Session_Exception {

    Instance nextInstance = getfollowed_byValue();
    logger.debug("in consultation action step: tryNext of "+this.getlabelValue());
    if (nextInstance != null) {
      Clinical_Algorithm_Entity nextEntity = (Clinical_Algorithm_Entity)nextInstance;
      if (nextEntity instanceof Consultation_Branch_Step) {
    	  ((Consultation_Branch_Step)nextEntity).doBranchStep(glmanager, null);
      } else 
      logger.error("Consultation_Action.tryNext" + nextEntity.getlabelValue()+ " should be a Consultation Branch Step");
     
    }
  }

  public void printContent(PrintWriter itsWriter, String delimiter, String context) {
      Instance rule_inObj = getrule_inValue();
      Instance rule_outObj = getrule_outValue();
      Instance followed_byObj = getfollowed_byValue();
      Collection actionsObj = getactionsValue();
      Object labelObj = getlabelValue();
      String label = "";
      if (actionsObj != null) {
        for (Iterator action=actionsObj.iterator(); action.hasNext();) {
          Action_Specification actionSpec = (Action_Specification)action.next();
          if (labelObj != null) label = (String)labelObj;
          actionSpec.printContent(itsWriter, delimiter, context + "/"+label ,
            (rule_inObj != null) ? (Criterion)rule_inObj : null,
            (rule_outObj != null) ? (Criterion)rule_outObj  : null);
        }
      }
      if (followed_byObj != null)
        ((Consultation_Action_Step)followed_byObj).printContent(itsWriter, delimiter, context);


  }


}
