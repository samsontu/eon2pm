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
import edu.stanford.smi.eon.time.*;
import edu.stanford.smi.eon.criterion.*;
import edu.stanford.smi.protegex.pal.engine.*; 
import edu.stanford.smi.protegex.pal.parser.*;
import edu.stanford.smi.protegex.pal.relations.*;
import edu.stanford.smi.protegex.pal.language.*;

import org.apache.log4j.*;

/** 
 */
public class Start_Activity extends Activity_Act {

	public Start_Activity(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
  static Logger logger = Logger.getLogger(Start_Activity.class);

	public void setactivityValue(Instance activity) {
		ModelUtilities.setOwnSlotValue(this, "activity", activity);	}
	public Instance getactivityValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "activity"));
		}

	public void setexcluded_subactivitiesValue(Collection excluded_subactivities) {
		ModelUtilities.setOwnSlotValues(this, "excluded_subactivities", excluded_subactivities);	}
	public Collection getexcluded_subactivitiesValue(){
		return  ModelUtilities.getOwnSlotValues(this, "excluded_subactivities");
	}
// __Code above is automatically generated. Do not change
  
  public void doAction(
    Action_To_Choose action,
    Guideline_Action_Choices currentDecision,
    GuidelineInterpreter interpreter ){
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

    Object activityObj= getactivityValue();
    if (activityObj != null) {
      Drug_Usage activity = (Drug_Usage) activityObj;
      List addEvaluations = new ArrayList();
      // addEvaluations is a collection of Choice_Evaluation instances
      Choice_Evaluation choiceEvaluation = new Choice_Evaluation();
      try {
    	
        Add_Evaluation addEval =  activity.processAddActivity(interpreter, this.getfine_grain_priorityValue(), null);
        addEval.preference = Preference.preferred;
        activity.addMedicationInstance(interpreter, addEval, "Recommend_Add");
        if (addEval != null) {
          if (this.getjustificationValue() != null)
            addEval.description =  this.getjustificationValue();
          choiceEvaluation.add_eval(addEval);
          addEvaluations.add(choiceEvaluation);
        }
        logger.debug("Start_Activity.doAction: "+activity.getlabelValue()+" got added");
      } catch (Exception e) {
        logger.error(activity.getlabelValue()+" cannot be evaluated; "+e.getClass(), e);
        
      }

      interpreter.addEvaluatedChoice(
        new Guideline_Activity_Evaluations(
          this.makeGuideline_Entity(interpreter.getCurrentGuidelineID()),
          Evaluation_Type.add,
          (Choice_Evaluation[])addEvaluations.toArray(new Choice_Evaluation[0]),
          interpreter.getCurrentGuidelineID())
      );
    } else {
      logger.debug("Start Activity.doAction: null activity in "+
    		  this.getBrowserText()+"/"+this.getName());
    }

  }

}
