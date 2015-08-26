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
public class Query_Result_Criterion extends Criterion {

	public Query_Result_Criterion(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
	static  Logger  logger = Logger.getLogger(Query_Result_Criterion.class);



	public void setqueryValue(Instance query) {
		ModelUtilities.setOwnSlotValue(this, "query", query);	}
	public Expression getqueryValue() {
		return ((Expression) ModelUtilities.getOwnSlotValue(this, "query"));
	}

	public void setpresenceValue(boolean presence) {
		ModelUtilities.setOwnSlotValue(this, "presence", new  Boolean(presence));	}
	public boolean ispresenceValue() {
		if (ModelUtilities.getOwnSlotValue(this, "presence") == null) return false;
		else 
			return ((Boolean) ModelUtilities.getOwnSlotValue(this, "presence")).booleanValue();
	}
	// __Code above is automatically generated. Do not change


	public Criteria_Evaluation ownEvaluate(GuidelineInterpreter guidelineManager, boolean doAll)
			throws PCA_Session_Exception {

		// Try query first. If none found, then check if there is a definition.
		boolean presence = false;
		Expression query = getqueryValue();
		Expression result = null;
		String support = "";
		Criteria_Evaluation evaluation = new Criteria_Evaluation(Logical_Operator.ATOMIC,
				null,//truth value
				new Criteria_Evaluation[0],
				this.makeGuideline_Entity(),
				null ); //support
		// query can be a PAL_Query (which may tell evalManager an empty collection if query results in no instance) or 
		//   a Structured_Query (which tells evalManager a null if there is query results in no instance)
		Object cached = guidelineManager.evalManager.ask(query);
		if (cached == null) {
			result = query.evaluate_expression(guidelineManager);
			if (result != null) {
				presence = true;
				try { 
					support = query.getSupport(((Set_Expression)result).getset_elementsValue());
				} catch (Exception e) {
					// Not a set result, cannot generate support
				}
			}
		} else if (cached instanceof Collection) {
			if (((Collection)cached).isEmpty()) presence = false;
			else { 
				presence = true;
				try { 
					support = query.getSupport( (Collection)cached);
				} catch (Exception e) {
					// Not a set result, cannot generate support
				}
			}
		} else presence = true;
		evaluation.truth_value = 
				(ispresenceValue()) ? ((presence) ? Truth_Value._true : Truth_Value._false)
						: ((!presence) ? Truth_Value._true : Truth_Value._false);
		evaluation.support = support;
		return evaluation;
	}
	
}
