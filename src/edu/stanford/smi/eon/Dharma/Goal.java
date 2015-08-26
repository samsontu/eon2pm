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

// Created on Mon Sep 17 13:27:23 PDT 2001
// Copyright Stanford University 2000

package edu.stanford.smi.eon.Dharma;

import java.util.*;
import java.io.PrintWriter;

import org.apache.log4j.Logger;

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

/** 
 */
public class Goal extends Goal_Entity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(Goal.class);


	public Goal(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}

	public void setconditional_goalsValue(Collection<?> conditionalGoals) {
		ModelUtilities.setOwnSlotValue(this, "conditional_goals", conditionalGoals);	}
	public Collection<Instance> getconditional_goalsValue() {
		return ((Collection<Instance>) ModelUtilities.getOwnSlotValues(this, "conditional_goals"));
	}

	public void setfocusValue(Collection<Cls> focus) {
		ModelUtilities.setOwnSlotValue(this, "focus", focus);	}
	public Collection<Cls> getfocusValues(){
		return  ModelUtilities.getOwnSlotValues(this, "focus");
	}

	public void setsourceValue(String source) {
		ModelUtilities.setOwnSlotValue(this, "source", source);	}
	public String getsourceValue(){
		return  (String)ModelUtilities.getOwnSlotValue(this, "source");
	}

	public void setexclusion_criteriaValue(Collection<Criterion> exclusions) {
		ModelUtilities.setOwnSlotValue(this, "exclusion_criteria", exclusions);	}
	public Collection<Criterion> getexclusion_criteriaValues(){
		return  ModelUtilities.getOwnSlotValues(this, "exclusion_criteria");
	}

	public void setinclusion_criteriaValue(Collection<Criterion> inclusions) {
		ModelUtilities.setOwnSlotValue(this, "inclusion_criteria", inclusions);	}
	public Collection<Criterion> getinclusion_criteriaValues(){
		return  ModelUtilities.getOwnSlotValues(this, "inclusion_criteria");
	}
	/*
	 *         Guideline_Entity guideline_entity,
               Justification support_for_goal,
			   Criteria_Evaluation goal,
 			   Goal_State achieved,
			   String data,
			   Collection<Action_Spec_Record> actions,
			   int fine_grain_priority,
			   Collection<String> references,
			   boolean primary, 
			   Collection<Criteria_Evaluation> criteriaEvaluation,
			   Collection missingData

	 */

	public Guideline_Goal  computePerformanceMeasure(GuidelineInterpreter interpreter, String sessionTime) {
		Guideline_Goal pmGoal ;
		
/*	    edu.stanford.smi.eon.PCAServerModule.Guideline_Entity guideline_entity,
	    edu.stanford.smi.eon.PCAServerModule.Justification support_for_goal,
	    edu.stanford.smi.eon.PCAServerModule.Criteria_Evaluation goal,
	    edu.stanford.smi.eon.PCAServerModule.Goal_State achieved,
	    String data,
	    Collection<Action_Spec_Record> actions,
	    int fine_grain_priority,
	    Collection<String> references,
	    boolean primary,
	    String kb_goal_id,
	    Collection<Criteria_Evaluation> criteria_evaluation,
	    Collection missing_data
*/
		Guideline_Goal pmResult  = new Guideline_Goal(
				new Guideline_Entity(getlabelValue(), "", "", getName()),  //guideline_entity
				null,                                      //Justification support_for_goal
				null,                                      //Criteria_Evaluation goal
				null,                                      //Goal_State achieved
				null,                                      //String data
				null,                 //actions
				0,                    // fine-grain priority
				null,                 // references
				false,                // primary
				this.getlabelValue(), //kb_goal_id
				null,                 // <Criteria_Evaluation> criteria_evaluation
				null);                // Collection missing_data
	    
	 // Check exclusions and inclusions
		EvaluationResults exclusionEvals = 
				evaluateCriteria (interpreter, this.getexclusion_criteriaValues(), 
						DharmaPaddaConstants.AnyTrue, DharmaPaddaConstants.EXCLUSION);
		EvaluationResults inclusionEvals = 
				evaluateCriteria (interpreter, this.getinclusion_criteriaValues(), 
						DharmaPaddaConstants.AnyNotTrue, DharmaPaddaConstants.INCLUSION);
		exclusionEvals.evaluations.addAll(inclusionEvals.evaluations);
		pmResult.criteria_evaluation = exclusionEvals.evaluations; //may still be null
		if (!exclusionEvals.excluded && !inclusionEvals.excluded) {
			for (Instance cg: this.getconditional_goalsValue()) {
				// If all selection criteria evaluate to true, then use this conditional goal
				EvaluationResults selection = evaluateCriteria (interpreter, 
						((General_Conditional_Goal)cg).getselection_criteriaValue(), 
						DharmaPaddaConstants.AnyNotTrue, DharmaPaddaConstants.SUBPOPSELECTION); 
				if (!selection.excluded) {
					//all evaluates to true. Case/Hospitalization belongs to this subpopulation
					pmResult = this.checkCriterionToAchieve(interpreter, 
							((General_Conditional_Goal)cg), pmResult);
					return pmResult;
				}
			}
			//No sub-population selection criteria satisfied, fail to satisfy numerator criteria
			pmResult.achieved = Goal_State.failed;
		} 
		return pmResult;
	}


	/* Check to see whether the case satisfies any of the selection criterion of
	 * Conditional_Goals. If yes, returns null; otherwise returns all of the results 
	 * of evaluating the selection criteria
	 */



	public EvaluationResults evaluateCriteria (
			GuidelineInterpreter interpreter, Collection<Criterion> criteria, 
			String whenToExclude, String criterionType) {
		EvaluationResults evalResults = new EvaluationResults();
		evalResults.evaluations = new ArrayList<Criteria_Evaluation>();
		Truth_Value excluded = null;
		for (Criterion criterion : criteria) {
			try {
				Criteria_Evaluation evaluation =  criterion.evaluate(interpreter, true);
				evaluation.setContext(this.getlabelValue());
				evaluation.setCriterionType(criterionType);
				evalResults.evaluations.add(evaluation);

				if ((whenToExclude.equals(DharmaPaddaConstants.AnyNotTrue) &&
						(!(evaluation.truth_value.equals(Truth_Value._true)))) ||
						(whenToExclude.equals(DharmaPaddaConstants.AnyTrue) &&
								(evaluation.truth_value.equals(Truth_Value._true)))) {
					if (excluded == null) {
						excluded = Truth_Value.from_int(Truth_Value.__true);
					}
				}
				// Check for missing data evaluations
				evaluation.missingDataCriteriaEval = findMissingValueCriteria(evaluation);
			} catch (Exception e) {
				logger.error("Exception evaluating inclusion criteria");
				e.printStackTrace();
			}
		}
		if (excluded != null && excluded.equals(Truth_Value._true)) {
			evalResults.excluded = true;
		}

		return evalResults;
	}

	private Collection<Criteria_Evaluation> findMissingValueCriteria(Criteria_Evaluation eval) {
		Collection <Criteria_Evaluation> missingValueCriteria = new ArrayList<Criteria_Evaluation>();
		if ((eval.support != null) && (eval.support.contains(DharmaPaddaConstants.MISSINGDATA)) && 
				((eval.children == null) || eval.children.length == 0)) {
			missingValueCriteria.add(eval);
		} else 
			if ((eval.children != null) && (eval.children.length > 0)) {
				for (Criteria_Evaluation childEval : eval.children) {
					missingValueCriteria.addAll(findMissingValueCriteria(childEval));
				}
			}
		return missingValueCriteria;
		// Always returns a non-null missingValueCriteria collection, which may be empty
	}

	public Guideline_Goal checkCriterionToAchieve(
			GuidelineInterpreter interpreter,
			General_Conditional_Goal cg, Guideline_Goal pmResult ) {
		// Patient/Hospitalization belongs to this denominator population
		Collection<Criterion> toAchieveCriteria = cg.getcriteria_to_achieveValue();
		Collection<Criteria_Evaluation> numeratorCriteriaEvaluations = new ArrayList<Criteria_Evaluation>();
		for (Criterion criterion : toAchieveCriteria) {
			try {
				Criteria_Evaluation toAchieveCriterionEval = criterion.
						evaluate(interpreter, true);
				toAchieveCriterionEval.setCriterionType(DharmaPaddaConstants.NUMERATOR);
				toAchieveCriterionEval.setContext(cg.getlabelValue());
				numeratorCriteriaEvaluations.add(toAchieveCriterionEval);
				if (toAchieveCriterionEval.truth_value.equals(Truth_Value._true))
					pmResult.achieved = Goal_State.achieved;
			} catch (PCA_Session_Exception e) {
				e.printStackTrace();
			}
		}
		if (pmResult.achieved == null)
			pmResult.achieved = Goal_State.failed;
		if (pmResult.criteria_evaluation == null) {
			pmResult.criteria_evaluation = numeratorCriteriaEvaluations;
		} else {
			pmResult.criteria_evaluation.addAll(numeratorCriteriaEvaluations);
		}
		return pmResult;
	}

	private class EvaluationResults {
		// start stepping through the array from the beginning
		public boolean excluded = false;
		public Collection<Criteria_Evaluation> evaluations = null;
		public Collection<Criteria_Evaluation> missingDataEvaluations = null;

	}





}
