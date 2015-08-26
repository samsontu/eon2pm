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
public class Join_Filter extends Filter {

	public Join_Filter(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
	static  Logger  logger = Logger.getLogger(Join_Filter.class);

	public void setqueryValue(Instance value) {
		ModelUtilities.setOwnSlotValue(this, "query", value);	}
	public Instance getqueryValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "query"));
	}

	public void setattribute_comparisonsValue(Collection attribute_comparisons) {
		ModelUtilities.setOwnSlotValues(this, "attribute_comparisons", attribute_comparisons);	}
	public Collection getattribute_comparisonsValue() {
		return ( ModelUtilities.getOwnSlotValues(this, "attribute_comparisons"));
	}


	// __Code above is automatically generated. Do not change

	public AbstractWhereFilter  constructExpression(GuidelineInterpreter guidelineManager)
			throws PCA_Session_Exception {
		//logger.debug("Comparison Filter: entering with matches "+matches.toString());
		Instance queryExpr =   getqueryValue();
		if (queryExpr == null) {
			logger.error("query is null for "+this.getBrowserText());
			return null;
		}
		Collection attributeComparisons = getattribute_comparisonsValue();
		if ((attributeComparisons == null) || attributeComparisons.isEmpty()) {
			logger.error("attributeComparisons is null or empty for "+this.getBrowserText());
			return null;
		}
		Expression instances = ((Expression)queryExpr).evaluate_expression(guidelineManager);
		// The result of evaluating this query should be a Set of instances
		if (instances == null) {
			Collection nullSet = new ArrayList();
			return new WhereJoinFilter(getattribute_comparisonsValue(), nullSet);
		} else if (instances instanceof Set_Expression) {
			WhereJoinFilter filter = new WhereJoinFilter(getattribute_comparisonsValue(), 
					((Set_Expression)instances).getset_elementsValue());
			return filter;
		}
		else {
			logger.error(queryExpr.getBrowserText()+" did not return a set of instances (or null)");
			return null;
		}
	}
}
