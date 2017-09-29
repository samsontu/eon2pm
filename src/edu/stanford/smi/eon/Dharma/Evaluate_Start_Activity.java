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
import edu.stanford.smi.eon.siteCustomization.CompareEvals;
import edu.stanford.smi.eon.time.*;
import edu.stanford.smi.eon.criterion.*;
import edu.stanford.smi.protegex.pal.engine.*; 
import edu.stanford.smi.protegex.pal.parser.*;
import edu.stanford.smi.protegex.pal.relations.*;
import edu.stanford.smi.protegex.pal.language.*;

import org.apache.log4j.*;

/** 
 */
public class Evaluate_Start_Activity extends Evaluate_Activity_Act {

	public Evaluate_Start_Activity(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
	static Logger logger = Logger.getLogger(Evaluate_Start_Activity.class);

	public void setalternativesValue(Collection alternatives) {
		ModelUtilities.setOwnSlotValues(this, "alternatives", alternatives);	}
	public Collection getalternativesValue(){
		return  ModelUtilities.getOwnSlotValues(this, "alternatives");
	}
	public void setrecommendation_basisValue(Collection recommendationBais) {
		ModelUtilities.setOwnSlotValues(this, "recommendation_basis", recommendationBais);	}
	public Collection getrecommendation_basisValue(){
		return  ModelUtilities.getOwnSlotValues(this, "recommendation_basis");
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

		// each item in alternatives is a Drug Usage.
		// each item in addEvaluation is an Add_Evaluation structure
		Collection<Slot> recommendationBais = getrecommendation_basisValue();
		Collection alternatives = getalternativesValue();
		List addEvaluations = new ArrayList();
		// addEvaluations is a collection of Choice_Evaluation instances
		for (Iterator i = alternatives.iterator();i.hasNext();) {
			Drug_Usage activity = (Drug_Usage) i.next();
			Choice_Evaluation choiceEvaluation = new Choice_Evaluation();
			try {
				Add_Evaluation addEval =  activity.evaluateAdd(interpreter, 
						this.getfine_grain_priorityValue(), null, recommendationBais);
				if (addEval != null) {
					choiceEvaluation.add_eval(addEval);
					addEvaluations.add(choiceEvaluation);
				}
				logger.debug(activity.getlabelValue()+" got evaluated");
			} catch (Exception e) {
				logger.error(activity.getlabelValue()+" cannot be evaluated; "+e.getClass());
				e.printStackTrace();
			}
		}

		Collections.sort(addEvaluations, new CompareEvals());

		interpreter.addEvaluatedChoice(
				new Guideline_Activity_Evaluations(
						this.makeGuideline_Entity(interpreter.getCurrentGuidelineID()),
						Evaluation_Type.add,
						(Choice_Evaluation[])addEvaluations.toArray(new Choice_Evaluation[0]),
						interpreter.getCurrentGuidelineID())
				);

	}


}
