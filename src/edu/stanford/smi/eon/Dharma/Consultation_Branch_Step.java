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
 */
public class Consultation_Branch_Step extends Consultation_Guideline_Entity {

	public Consultation_Branch_Step(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
	static Logger logger = Logger.getLogger(Consultation_Branch_Step.class);

	public void setselection_methodValue(String selection_method) {
		ModelUtilities.setOwnSlotValue(this, "selection_method", selection_method);	}
	public String getselection_methodValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "selection_method"));
	}

	public void setbranchesValue(Collection branches) {
		ModelUtilities.setOwnSlotValues(this, "branches", branches);	}
	public Collection getbranchesValue(){
		return  ModelUtilities.getOwnSlotValues(this, "branches");
	}

	public void setorder_constraintValue(String order_constraint) {
		ModelUtilities.setOwnSlotValue(this, "order_constraint", order_constraint);	}
	public String getorder_constraintValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "order_constraint"));
	}
	// __Code above is automatically generated. Do not change

	public void doBranchStep(
			GuidelineInterpreter gmanager,
			Guideline_Action_Choices choices) throws PCA_Session_Exception {

		Collection<Guideline_Action_Choices> choicesSeq =     new ArrayList<Guideline_Action_Choices>();  //  Each of them is a decision point and the choices associated with it.
		Collection<Action_To_Choose> actionChoices =  new ArrayList<Action_To_Choose>();  // collection of Action_To_Choose. Each Action_To_Choose corresponds to a KB Action_Choice(with its action specifications).
		Action_To_Choose actionToChoose;
		Guideline_Action_Choices newGuidelineActionChoices;

		logger.debug("In Consultation_Branch_Step: - doBranchStep "+getlabelValue());
		if ((choices != null) && (choices.current_location.entity_id == this.getName())) {
			// These have been shown before, show them again if not all of them
			// have been chosen.  2014/07/08: Not relevant to ATHENA
			choicesSeq.add(choices);
			gmanager.addActionChoices(choicesSeq);
		} else {  //new
			if (this.getbranchesValue() != null) {
				for (Consultation_Guideline_Entity step: (Collection<Consultation_Guideline_Entity>)getbranchesValue()) {
					try {
						logger.debug("In Consultation_Branch_Step: - doBranchStep "+step.getlabelValue());
						actionToChoose =  step.evaluateAction(gmanager);
						if (actionToChoose != null) {
							actionChoices.add(actionToChoose);
//							if (step instanceof Consultation_Branch_Step) 
//								((Consultation_Branch_Step)step).doBranchStep(gmanager, null); else 
							if (actionToChoose.preference.equals(Preference.preferred))
								step.tryNext(gmanager, Compliance_Level.strict);
						}
					} catch (Exception e) {
						logger.error("Consultation_Branch_Step.doBranchStep(): Exception evaluating "+step.getBrowserText(), e);
					}
				}

				logger.debug("In Consultation_Branch_Step: - doBranchStep: completed actions");
				logger.debug("In Consultation_Branch_Step: - doBranchStep:"+ gmanager.getCurrentGuidelineID());
				logger.debug("In Consultation_Branch_Step: - doBranchStep:"+ PCAInterfaceUtil.mapSelectionAlternatives(getselection_methodValue()));
				logger.debug("In Consultation_Branch_Step: - doBranchStep:"+ gmanager.getCurrentGuidelineID());

				if (!(actionChoices.isEmpty())) {
					newGuidelineActionChoices = new Guideline_Action_Choices(
							gmanager.getCurrentGuidelineID(),
							PCAInterfaceUtil.mapSelectionAlternatives(getselection_methodValue()),
							(Action_To_Choose[]) actionChoices.toArray(new Action_To_Choose[0]),
							this.makeGuideline_Entity(gmanager.getCurrentGuidelineID()),
							false
							);
					gmanager.expandedChoices.add(newGuidelineActionChoices);
					choicesSeq.add(newGuidelineActionChoices);
					//} catch (Exception e) {
					//  logger.error("Exception: "+this.getlabelValue() + ": "+ e.getMessage());
					//}
				}
//				if (choices != null) {
//					choicesSeq.add(choices);
//				}
			}
		}
		if (!(choicesSeq.isEmpty()))
			gmanager.addActionChoices(choicesSeq);

	}

	public Action_To_Choose  evaluateAction(GuidelineInterpreter gmanager) {
		try {
			doBranchStep(gmanager, null);
		} catch (PCA_Session_Exception e) {
			logger.error("Exception executing steps of "+getlabelValue(), e);

		}
		// Action_To_Choose evaluatedAction =HelperFunctions.dummyActionToChoose();
		return null;
	}

	public void printContent(PrintWriter itsWriter, String delimiter, String context) {
		Collection steps = getbranchesValue();
		if (steps != null) {
			for (Iterator i=steps.iterator(); i.hasNext();) {
				Object stepObj = i.next();
				((Consultation_Guideline_Entity) stepObj).printContent(itsWriter, delimiter, context) ;
			}
		}
	}

}
