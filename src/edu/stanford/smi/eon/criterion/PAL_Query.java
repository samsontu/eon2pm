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

// Created on Wed Jun 20 12:58:03 PDT 2001
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
public class PAL_Query extends Expression {

	public PAL_Query(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}

	static  Logger  logger = Logger.getLogger(PAL_Query.class);

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
	
	public void setPAL_queryValue(Instance PAL_query) {
		ModelUtilities.setOwnSlotValue(this, "PAL_query", PAL_query);	}
	public Instance getPAL_queryValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "PAL_query"));
	}

	public void setkey_slotValue(Instance key_slot) {
		ModelUtilities.setOwnSlotValue(this, "key_slot", key_slot);	}
	public Instance getkey_slotValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "key_slot"));
	}
	// __Code above is automatically generated. Do not change

	private Instance instantiateCase (Instance genericQuery,
			GuidelineInterpreter guidelineManager) throws PCA_Session_Exception{
		KnowledgeBase kb = this.getKnowledgeBase();
		Slot PALStatement = kb.getSlot(":PAL-STATEMENT");
		Slot PALRange=kb.getSlot(":PAL-RANGE");
		Slot PALName=kb.getSlot(":PAL-NAME");
		Object rawconstraintStatement = genericQuery.getOwnSlotValue(PALStatement);
		if (rawconstraintStatement != null) {
			String constraintStatement = (String)rawconstraintStatement;
			//logger.debug("PAL_Criterion.instantiateCase: constraint "+constraintStatement);
			String case_variableValue = getcase_variableValue();
			String caseID= guidelineManager.getDBmanager().getCaseID();
			constraintStatement = HelperFunctions.replaceSubstring(constraintStatement, case_variableValue,
					caseID);
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
			Instance newInstance = guidelineManager.getDBmanager().createRegisteredInstance("PAL-QUERY");
			newInstance.setOwnSlotValue(PALStatement, constraintStatement);
			newInstance.setOwnSlotValue(PALRange, genericQuery.getOwnSlotValue(PALRange));
			newInstance.setOwnSlotValue(PALName, genericQuery.getOwnSlotValue(PALName));
			//logger.debug("PAL_Criterion.instantiateCase: newconstraint "+constraintStatement);
			return newInstance;
		} else {
			throw new PCA_Session_Exception("Null PAL statement in "+this.getName());
		}

	}


	public Collection doQuery(GuidelineInterpreter guidelineManager)
			throws  PCA_Session_Exception {
		Collection activitiesToStopCollection = null;
		if ((activitiesToStopCollection = (Collection) guidelineManager.evalManager
				.ask(this)) != null) {
			if (activitiesToStopCollection.isEmpty()) {
				return null;
			} else {
				return activitiesToStopCollection;
			}
		} else {
			activitiesToStopCollection = new ArrayList();
			Instance query = getPAL_queryValue();
			if (query == null) {
				throw new PCA_Session_Exception("PAL_Query.doQuery: No PAL query in instance "+this.getName());
			} else {

				query = instantiateCase(query, guidelineManager);
				EvaluationPolicy evalPolicy = new EvaluationPolicy();
				QueryEngine evalEngine = new QueryEvaluationEngine(evalPolicy,
						guidelineManager.getKBmanager().getKB());
				GuidelineInterpreter.currentGuidelineInterpreter = guidelineManager;
				logger.debug("PAL_Query.doQuery "+ this.getBrowserText()+"/"+ this.getName());
				QueryEngineResponse response = evalEngine.askSingleQuery(
						query);
				Collection queryResult = null;

				if (response.queryHasAtLeastOneAnswer(query)) {
					queryResult = response.getQueryAnswers();
					for (Iterator i=queryResult.iterator(); i.hasNext();) {
						QueryAnswer result = (QueryAnswer)i.next();
						Collection allVariableBindings = result.getAllVariableValueBindings();
						for (Iterator k=allVariableBindings.iterator(); k.hasNext();) {
							VariableValueBinding binding = (VariableValueBinding)k.next();
							logger.debug("answer "+": binding - variable="+ binding.getVariableName() +
									" value="+binding.getVariableValue());
							Instance queryResultInstance = (Instance)binding.getVariableValue();
							Cls queryResultCls = queryResultInstance.getDirectType();
							if (getkey_slotValue() != null)  {
								Object value = queryResultInstance.getOwnSlotValue((Slot)getkey_slotValue());
								if (!activitiesToStopCollection.contains(value))
									activitiesToStopCollection.add(value);
							} else {
								if (!activitiesToStopCollection.contains(queryResultInstance) )
									activitiesToStopCollection.add(queryResultInstance);
							}
						} //for
					}//for
				} else {
					logger.warn("No results from evaluating "+this.getBrowserText()
					+ "' ("+this.getName()+") "+ "for case "+guidelineManager.getCaseID());
				}
				guidelineManager.evalManager.tell(this, activitiesToStopCollection);
				if (activitiesToStopCollection.isEmpty()) {
					return null;
				} else {
					return activitiesToStopCollection;
				}
			} //else   query != null
		}
	}

	public Expression ownEvaluateExpression(GuidelineInterpreter glmanager) {
		java.util.Date startTime = new java.util.Date();
		Set_Expression set = (Set_Expression)glmanager.getDBmanager().createRegisteredInstance(
				"Set_Expression");
		try {
			Collection result = doQuery(glmanager);
			if (result != null) {
				set.setset_elementsValue(result);
				//GuidelineInterpreter.currentGuidelineInterpreter = null;
				java.util.Date stopTime = new java.util.Date();
				logger.debug("PAL Query ownEvaluate() "+getlabelValue()+": taking @@@@@@@@@ "+ (stopTime.getTime() - startTime.getTime())+" @@@@@@@@@ milliseconds" );
				return set;
			} else return null;
		} catch (Exception e) {
			logger.debug("PAL_Query.evaluate_expression exception: "+e.getMessage(), e);
			e.printStackTrace();
			return null;
		}
	}

}
