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
<p>An Action Step describes a set of (instantaneous) action specifications that should be carried out. An Action Step is part of a clinical algorithm and cannot be reused in another clinical algorithm, whereas instances of Action_Specification are meant to be reusable.
 */
public class Action_Step extends Action_Like_Step {

	public Action_Step(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
  static Logger logger = Logger.getLogger(Action_Step.class);

	public void setstrength_of_recommendationValue(String strength_of_recommendation) {
		ModelUtilities.setOwnSlotValue(this, "strength_of_recommendation", strength_of_recommendation);	}
	public String getstrength_of_recommendationValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "strength_of_recommendation"));
	}

	public void setactionsValue(Collection actions) {
		ModelUtilities.setOwnSlotValues(this, "actions", actions);	}
	public Collection getactionsValue(){
		return  ModelUtilities.getOwnSlotValues(this, "actions");
	}

	public void setiteration_specValue(Instance iteration_spec) {
		ModelUtilities.setOwnSlotValue(this, "iteration_spec", iteration_spec);	}
	public Instance getiteration_specValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "iteration_spec"));
	}
// __Code above is automatically generated. Do not change

  public void tryNext(GuidelineInterpreter glmanager, Compliance_Level compliance)
    throws PCA_Session_Exception {
    logger.debug("Action_Step tryNext of "+ getlabelValue());

    Clinical_Algorithm_Entity nextStep = (Clinical_Algorithm_Entity)getfollowed_byValue();
    if (!nextStep.isNewEncounter())
      nextStep.doStep(glmanager, compliance);
    return;
  }

  public void doStep(GuidelineInterpreter glmanager, Compliance_Level compliance)
		  throws PCA_Session_Exception {
	  logger.info("Action_Step doStep of"+ getlabelValue());
	  Action_Spec_Record step;
	  Collection<Action_Spec_Record> steps = new ArrayList<Action_Spec_Record>();
	  for (Action_Specification i: (Collection<Action_Specification>)getactionsValue()) {
		  step = i.evaluateActionSpec(glmanager);
		  if (step != null) steps.add(step);
	  }
	  Action_To_Choose choice = new Action_To_Choose (getName(), Preference.preferred,
			  null, (Action_Spec_Record[]) steps.toArray(new Action_Spec_Record[0]),
			  makeGuideline_Entity(this.getName()), "");
	  ArrayList choices = new ArrayList();
	  choices.add(choice);
	  Action_To_Choose choiceArray[] = (Action_To_Choose[]) choices.toArray(new Action_To_Choose[0]);
	  glmanager.addActionChoice(new Guideline_Action_Choices("", SelectionAlternatives.one_of,
		      choiceArray , makeGuideline_Entity(glmanager.getCurrentGuidelineID()), false));
	  tryNext(glmanager, compliance);
	  return;
  }


}
