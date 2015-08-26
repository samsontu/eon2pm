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
 *  Decision (Superclass hint for Java class generation)
Making a choice among the alternatives is aided by preferences as determined by rule-in and rule-out conditions associated with instances of Selection class. If a rule-out condition evaluates to true, then an alternative is rejected. If the rule-out condition does not apply and a rule-in condition evaluates to true, then the alternative is marked as preferred. If neither evaluates to true, then the preference for the choice can be determined by a default preference associated with the alternatives. 
 */
public class Choice_Step extends Decision {

	public Choice_Step(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}

  static Logger logger = Logger.getLogger(Choice_Step.class);

	public void setprevious_scenariosValue(Collection previous_scenarios) {
		ModelUtilities.setOwnSlotValues(this, "previous_scenarios", previous_scenarios);	}
	public Collection getprevious_scenariosValue(){
		return  ModelUtilities.getOwnSlotValues(this, "previous_scenarios");
	}

	public void setbranchesValue(Collection branches) {
		ModelUtilities.setOwnSlotValues(this, "branches", branches);	}
	public Collection getbranchesValue(){
		return  ModelUtilities.getOwnSlotValues(this, "branches");
	}
// __Code above is automatically generated. Do not change

  /** Given a scenario, compute a set of Guideline_Action_Choices */
  public void doStep(GuidelineInterpreter glmanager ,
       Compliance_Level compliance) throws PCA_Session_Exception {
    glmanager.complianceLevel = compliance;
    ArrayList choices = new ArrayList();
    // Assume for now that clinEntity is a Choice_Step
    Collection branches = getbranchesValue();
    // Evaluate actions associated with each choice
    for (Iterator i= branches.iterator();i.hasNext();){
      Instance choice = (Instance) i.next();
      logger.debug("in Choice_Step doNext: choice = " +choice.getBrowserText()+"/"+
    		  choice.getName());
      Action_To_Choose action_choice =  ((Action_Choice) choice).evaluateChoice(glmanager, compliance);
      if (action_choice != null) {
        choices.add(action_choice);
      }
    }
    logger.debug("in Choice_Step doNext: # choices = " + choices.size());
    Action_To_Choose choiceArray[] = (Action_To_Choose[]) choices.toArray(new Action_To_Choose[0]);
    glmanager.addActionChoice(new Guideline_Action_Choices("", SelectionAlternatives.one_of,
      choiceArray , makeGuideline_Entity(glmanager.getCurrentGuidelineID()), false));
  }
}
