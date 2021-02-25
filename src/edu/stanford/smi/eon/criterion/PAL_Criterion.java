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

// Created on Wed Jun 20 12:58:01 PDT 2001
// Copyright Stanford University 2000

package edu.stanford.smi.eon.criterion;

import java.util.*;
import java.io.*;
import java.text.NumberFormat;
import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.util.*;
import edu.stanford.smi.eon.util.*;
import edu.stanford.smi.eon.guidelineinterpreter.*;
import edu.stanford.smi.eon.PCAServerModule.*;
import edu.stanford.smi.eon.kbhandler.*;
import edu.stanford.smi.eon.datahandler.*;
import edu.stanford.smi.eon.time.*;
import edu.stanford.smi.protegex.pal.engine.*; 
import edu.stanford.smi.protegex.pal.parser.*;
import edu.stanford.smi.protegex.pal.relations.*;
import edu.stanford.smi.protegex.pal.*;
import org.apache.log4j.*;
/** 
 */
public class PAL_Criterion extends Criterion {

	public PAL_Criterion(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
	static  Logger  logger = Logger.getLogger(PAL_Criterion.class);

	public void setcase_variableValue(String case_variable) {
		ModelUtilities.setOwnSlotValue(this, "case_variable", case_variable);	}
	public String getcase_variableValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "case_variable"));
	}

	public void setsession_time_variableValue(String session_time_variable) {
		ModelUtilities.setOwnSlotValue(this, "session_time_variable", session_time_variable);	}
	public String getsession_time_variableValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "session_time_variable"));
	}

	public void setPAL_constraintValue(Instance PAL_constraint) {
		ModelUtilities.setOwnSlotValue(this, "PAL_constraint", PAL_constraint);	}
	public Instance getPAL_constraintValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "PAL_constraint"));
	}
	// __Code above is automatically generated. Do not change


	public synchronized Criteria_Evaluation ownEvaluate(GuidelineInterpreter guidelineManager, boolean doAll)
			throws PCA_Session_Exception {
		Criteria_Evaluation evaluation = (Criteria_Evaluation) guidelineManager.evalManager.ask(this);
		if (evaluation  != null) return evaluation;
		// evaluation == null
		java.util.Date startTime = new java.util.Date();
		GuidelineInterpreter.currentGuidelineInterpreter = guidelineManager;
		EvaluationPolicy evalPolicy = new EvaluationPolicy();
		//evalPolicy.setTracingEnabled(true);
		//evalPolicy.addPredicateToTraceList("exists");
		ConstraintEvaluationEngine evalEngine = new ConstraintEvaluationEngine(evalPolicy,
				guidelineManager.getKBmanager().getKB());
		ConstraintEngineResponse response = null;
		if (getcase_variableValue() != null) {
			response = evalEngine.checkSingleStatement(instantiateCase(getPAL_constraintValue(),
					guidelineManager));
		} else {
			response = evalEngine.checkSingleStatement(
					getPAL_constraintValue());
		}

		if (response.areThereValidationErrors() || response.areThereRuntimeErrors()) {
			Collection errors = null;
			if (response.areThereValidationErrors()) {
				errors = response.getValidationErrors();
				if ((errors != null) && (!errors.isEmpty())) {
					for (Iterator i=errors.iterator(); i.hasNext();) {
						ValidationError error = (ValidationError)i.next();
						logger.error(error.getDescription() + "for case "+guidelineManager.getCaseID());
					}
				}
				logger.error(" PAL validation error in "+this.getName()+ "; "+this.getBrowserText()+ "for case "+guidelineManager.getCaseID());
				throw new PCA_Session_Exception(" PAL validation error in "+this.getName());
			} 
			if (response.areThereRuntimeErrors()) {
				errors = response.getRuntimeErrors();
				if ((errors != null) && (!errors.isEmpty())) {
					for (Iterator i=errors.iterator(); i.hasNext();) {
						RuntimeError error = (RuntimeError)i.next();
						logger.error(error.getDescription()+ "for case "+guidelineManager.getCaseID());  
					}
				}
				logger.error(" PAL runtime error in "+this.getName()+ "; "+this.getBrowserText()+ "for case "+guidelineManager.getCaseID());
				throw new PCA_Session_Exception("PAL runtime error in "+this.getName());
			} 
		} else {
			if (response.areThereConstraintViolations()) {
				// the predicate evaluate to false
				logger.debug("PAL_Criterion ownEvaluate() "+getlabelValue()+" has constraint violation");
				evaluation = new Criteria_Evaluation(Logical_Operator.ATOMIC,
						Truth_Value._false,
						new Criteria_Evaluation[0],
						this.makeGuideline_Entity(),
						null);
			} else {
				logger.debug("PAL_Criterion ownEvaluate() "+getlabelValue()+" has no constraint violation");
				evaluation = new Criteria_Evaluation(Logical_Operator.ATOMIC,
						Truth_Value._true,
						new Criteria_Evaluation[0],
						this.makeGuideline_Entity(),
						null);
			}
		}
		//GuidelineInterpreter.currentGuidelineInterpreter = null;
		java.util.Date stopTime = new java.util.Date();
		logger.debug("PAL Criterion ownEvaluate() "+getlabelValue()+": taking @@@@@@@@@ "+ (stopTime.getTime() - startTime.getTime())+" @@@@@@@@@ milliseconds" );
		guidelineManager.evalManager.tell(this, evaluation);
		return evaluation;
	}

	private Instance instantiateCase (Instance genericConstraint,
			GuidelineInterpreter guidelineManager) throws PCA_Session_Exception{
		KnowledgeBase kb = this.getKnowledgeBase();
		Slot PALStatement = kb.getSlot(":PAL-STATEMENT");
		Slot PALRange=kb.getSlot(":PAL-RANGE");
		Slot PALName=kb.getSlot(":PAL-NAME");
		Object rawconstraintStatement = genericConstraint.getOwnSlotValue(PALStatement);
		//logger.debug("PAL_Criterion instantiateCase() 1 "+getlabelValue());
		if (rawconstraintStatement != null) {
			String constraintStatement = (String)rawconstraintStatement;

			constraintStatement = HelperFunctions.replaceSubstring(constraintStatement, getcase_variableValue(),
					guidelineManager.getDBmanager().getCaseID());
			String sessionTimeVariable = getsession_time_variableValue();
			if (sessionTimeVariable != null) {
				String sessionTime = guidelineManager.getDBmanager().getSessionTime();
				try {
					int sessionTimeNumeric = HelperFunctions.Day2Int2(sessionTime);
					String sessionTimeNumericString = Integer.toString(sessionTimeNumeric);
					constraintStatement = constraintStatement.replace(
						getsession_time_variableValue(), sessionTimeNumericString);
				} catch (Exception e1){
					logger.error("Incorrect session time format("+sessionTime+"); PAL criterion "+PALName+" may not evaluate correctly" + "for case "+guidelineManager.getCaseID());
				}
			}
			Instance newInstance = guidelineManager.getDBmanager().createRegisteredInstance(":PAL-CONSTRAINT");
			newInstance.setOwnSlotValue(PALStatement, constraintStatement);
			newInstance.setOwnSlotValue(PALRange, genericConstraint.getOwnSlotValue(PALRange));
			newInstance.setOwnSlotValue(PALName, genericConstraint.getOwnSlotValue(PALName));
			guidelineManager.getDBmanager().registerInstance(newInstance);
			//("PAL_Criterion.instantiateCase: newconstraint "+constraintStatement);
			//logger.debug("PAL_Criterion instantiateCase() 2 "+getlabelValue());

			return newInstance;
		} else {
			throw new PCA_Session_Exception("Null PAL statement in "+this.getName());
		}

	}

}
