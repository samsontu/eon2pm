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

// Created on Mon Sep 17 13:27:21 PDT 2001
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
 *  Clinical_Algorithm_Entity (Superclass hint for Java class generation)
A scenario is a partial characterization of the state of a patient. For example, the asthma medications that the patient is taking, such as low-dose inhaled steroids, may define a patient scenario. A scenario has an informal textual description and eligibility conditions that specify the necessary conditions for a patient to be in this scenario. Scenarios allow a clinician to synchronize the management of a patient to situations handled by a guideline.
 */
public class Scenario extends Management_Algorithm_Entity {

	public Scenario(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
  static Logger logger = Logger.getLogger(Scenario.class);

	public void setnew_encounterValue(boolean new_encounter) {
		ModelUtilities.setOwnSlotValue(this, "new_encounter", new  Boolean(new_encounter));	}
	public boolean isnew_encounterValue() {
		if (ModelUtilities.getOwnSlotValue(this, "new_encounter") == null) return false;
		else 
		return ((Boolean) ModelUtilities.getOwnSlotValue(this, "new_encounter")).booleanValue();
	}

	public void setconsultation_templateValue(Instance consultation_template) {
		ModelUtilities.setOwnSlotValue(this, "consultation_template", consultation_template);	}
	public Instance getconsultation_templateValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "consultation_template"));
	}

	public void setfollowed_byValue(Instance followed_by) {
		ModelUtilities.setOwnSlotValue(this, "followed_by", followed_by);	}
	public Instance getfollowed_byValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "followed_by"));
	}

	public void setpreconditionValue(Instance precondition) {
		ModelUtilities.setOwnSlotValue(this, "precondition", precondition);	}
	public Instance getpreconditionValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "precondition"));
	}
// __Code above is automatically generated. Do not change

  public void tryNext(GuidelineInterpreter glmanager, Compliance_Level compliance)
    throws PCA_Session_Exception {
    logger.debug("in scenario tryNext = " + this.getlabelValue());
    Clinical_Algorithm_Entity nextStep = (Clinical_Algorithm_Entity)
                getfollowed_byValue();
    if (nextStep != null) {
      if (!nextStep.isNewEncounter())
       nextStep.doStep(glmanager, compliance);
    }
  }
  public void doStep(GuidelineInterpreter glmanager, Compliance_Level compliance)
		  throws PCA_Session_Exception {
	  logger.debug("in scenario doStep = " + this.getlabelValue());

	  Criteria_Evaluation evaluation = null;

	  if (getpreconditionValue() != null) {
		  evaluation = ((Criterion) getpreconditionValue()).evaluate(glmanager, false);   
		  if (!PCAInterfaceUtil.mapTruthValue(evaluation.truth_value)) 
			  return;       
	  }
	  if (getconsultation_templateValue() != null) {
		  //doConsultation method adds a set of Guideline_Action_Choices  to
		  // guidelineinterpreter
		  ((Consultation_Guideline) getconsultation_templateValue()).doConsultation(glmanager,
				  compliance);
	  }
	  tryNext(glmanager, compliance);
  }

  public boolean isNewEncounter() {
    return isnew_encounterValue();
  }
  
  protected Scenario_Choice evaluateScenario(Guideline guideline, GuidelineInterpreter gInterpreter)
  throws PCA_Session_Exception {
  Preference preference =  Preference.neutral;
  Justification justification = HelperFunctions.dummyJustification();
  Criteria_Evaluation evaluation = null;

  if (getpreconditionValue() != null) {
     evaluation = ((Criterion) getpreconditionValue()).evaluate(gInterpreter, false);
          
     if (PCAInterfaceUtil.mapTruthValue(evaluation.truth_value)) {
      preference = Preference.preferred;
      justification = new Justification(evaluation, "scenario choice");
     }

  }

  // construct return type
  Scenario_Choice choice = new Scenario_Choice
    (preference, getName(),justification,
     makeGuideline_Entity(guideline.getName()), "");
  return choice;
}


  public void printContent(PrintWriter itsWriter, String delimiter) {
    /*itsWriter.print(this.getDirectType().getName()+delimiter+ getlabelValue() + delimiter);
    Object preconditionObj = getpreconditionValue();
    if (preconditionObj != null) {
      ((Criterion)preconditionObj).printContent(itsWriter);
    }
    itsWriter.println(""); */
    Object consultationGuideline = getconsultation_templateValue();
    if (consultationGuideline != null)
      ((Consultation_Guideline)consultationGuideline).printContent(itsWriter, delimiter, getlabelValue());
  }
}
