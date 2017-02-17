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

// Created on Mon Sep 17 13:27:24 PDT 2001
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
import edu.stanford.smi.eon.datahandler.Constants;
import edu.stanford.smi.eon.time.*;
import edu.stanford.smi.eon.criterion.*;
import edu.stanford.smi.protegex.pal.engine.*; 
import edu.stanford.smi.protegex.pal.parser.*;
import edu.stanford.smi.protegex.pal.relations.*;
import edu.stanford.smi.protegex.pal.language.*;

import org.apache.log4j.*;

/** 
 */
public class Modify_Activity extends Activity_Act {

	public Modify_Activity(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
  static Logger logger = Logger.getLogger(Modify_Activity.class);

	public void setactivity_to_modifyValue(Instance activity_to_modify) {
		ModelUtilities.setOwnSlotValue(this, "activity_to_modify", activity_to_modify);	}
	public Instance getactivity_to_modifyValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "activity_to_modify"));
	}

	public void setvalueValue(Instance value) {
		ModelUtilities.setOwnSlotValue(this, "value", value);	}
	public Instance getvalueValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "value"));
	}

	public void setattributeValue(String attribute) {
		ModelUtilities.setOwnSlotValue(this, "attribute", attribute);	}
	public String getattributeValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "attribute"));
	}

		public void setdirectionValue(String direction) {
			ModelUtilities.setOwnSlotValue(this, "direction", direction);	}
		public String getdirectionValue() {
			return ((String) ModelUtilities.getOwnSlotValue(this, "direction"));
}
// __Code above is automatically generated. Do not change

public Cls getRecommendationMood() {
	if (getdirectionValue().equals("up")) {
		return getKnowledgeBase().getCls("Recommend_Increase");
	} else {return getKnowledgeBase().getCls("Recommend_Decrease");
	}
}

private Direction getDirection(String direction) {
	return (direction.equals("up") ? Direction.up : Direction.down);
}

public void doAction (Action_To_Choose action,
	    Guideline_Action_Choices currentDecision,
	    GuidelineInterpreter interpreter) {
	Matched_Data[] beneficial_interactions =null;
	Matched_Data[] compelling_indications=null;
	Matched_Data[] contraindications=null;
	Matched_Data[] harmful_interactions=null;
	Matched_Data[] relative_contraindications=null;
	Matched_Data[] relative_indications=null;

	if (getrule_in_conditionValue() != null) {
		Criteria_Evaluation evaluation = HelperFunctions.dummyCriteriaEvaluation();
		try {
			evaluation = (Criteria_Evaluation) ((Criterion)getrule_in_conditionValue()).evaluate(interpreter, false);
			if (!(PCAInterfaceUtil.mapTruthValue(evaluation.truth_value)))  {   // rule-in condition does not hold
				return ;
			}
		} catch (PCA_Session_Exception e) {
			e.printStackTrace();
			return ;
		}
	}
	Instance activtyToModify = getactivity_to_modifyValue();
	Change_Attribute_Evaluation changeEval = null;
	Collection changeEvaluations = new ArrayList();
	Collection activitiesToModify = null;
	if (activtyToModify != null) {
		try {
			activitiesToModify = ((PAL_Query)activtyToModify).doQuery(interpreter) ; 
		} catch (Exception e) {
			logger.error("Modify_Activity.doAction: Exception evaluating (PAL_Query)activtyToModify).doQuery(interpreter)", e);
		}
	    if (activitiesToModify == null) {
	    	logger.error("Modify_Activity.doAction:Potential Error: No activity to modify");
	    } else {
	      for (Iterator i = activitiesToModify.iterator();i.hasNext();) {
	        String currentActivity = (String)i.next();
	        logger.debug("Modify_Activity.doAction: current actvitity "+ currentActivity );
	        Choice_Evaluation choiceEvaluation = new Choice_Evaluation();
	        String level = "";
	        try {
	        	 level = ((Expression)getvalueValue()).evaluate_expression(interpreter).toString();
	        } catch (Exception e) {
				logger.error("Modify_Activity.doAction: Exception evaluating (((Expression)getvalueValue()).evaluate_expression(interpreter)", e);
					        	
	        }
	        changeEval = new Change_Attribute_Evaluation("",
	                    currentActivity,
	                    interpreter.getCurrentGuidelineID(),
	                    getattributeValue(),
	                    level,
						beneficial_interactions,
						compelling_indications,
						contraindications,
						harmful_interactions,
						relative_contraindications,
						relative_indications,
	                    new Matched_Data[0],
	                    new Matched_Data[0],
	                    getDirection(getdirectionValue()), //Truth_Value._true,
	                    new Action_Spec_Record[0],
	                    Preference.neutral, this.getfine_grain_priorityValue());
	         String patient_id = interpreter.getDBmanager().getCaseID();
	         Medication addedMed = (Medication)interpreter.getDBmanager().createInstance("Medication");
	         addedMed.setSlotsValues(0, "",
	                    currentActivity, 0, "",
	                    getRecommendationMood(),
	                    patient_id, "", "", Constants.active, null, (float)0.0, null );
	         
	         choiceEvaluation.change_attribute_eval(changeEval);
	         changeEvaluations.add(choiceEvaluation);
	     } /* for */
	    }
        if (changeEvaluations.size ()> 0) {
          logger.debug("Modify_Activity_Intensity.doAction: "+
            "****Add evaluated choice number=" + changeEvaluations.size ());
          Guideline_Activity_Evaluations modifyEvaluations =
            new Guideline_Activity_Evaluations(
              this.makeGuideline_Entity(interpreter.getCurrentGuidelineID()),
              Evaluation_Type.change_attribute,
              (Choice_Evaluation[])changeEvaluations.toArray(new Choice_Evaluation[0]),
              interpreter.getCurrentGuidelineID());
          interpreter.addEvaluatedChoice(modifyEvaluations);
        } else logger.error("Modify_Activiy.doAction: "+
            "No dose change record");
      
	}
}
	     //*/
}