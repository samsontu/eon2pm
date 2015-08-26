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
public class Predicate_Filter extends Filter {

	public Predicate_Filter(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
	static  Logger  logger = Logger.getLogger(Predicate_Filter.class);

	public void setpredicateValue(String value) {
		ModelUtilities.setOwnSlotValue(this, "predicate", value);	}
	public String getpredicateValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "predicate"));
	}

	public void setquantifierValue(String operator) {
		ModelUtilities.setOwnSlotValue(this, "quantifier", operator);	}
	public String getquantifierValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "quantifier"));
	}

	public void setattributeValue(Instance attribute) {
		ModelUtilities.setOwnSlotValue(this, "attribute", attribute);	}
	public Instance getattributeValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "attribute"));
	}
	// __Code above is automatically generated. Do not change

	public AbstractWhereFilter  constructExpression(GuidelineInterpreter guidelineManager)
			throws PCA_Session_Exception {
		//logger.debug("Comparison Filter: entering with matches "+matches.toString());
		WherePredicateFilter filter = null;
		if (this.getattributeValue() != null) {
			filter = new WherePredicateFilter(this.getattributeValue().getName(), this.getpredicateValue(), 
				this.getquantifierValue());
		} else
			filter = new WherePredicateFilter(null, this.getpredicateValue(), 
					this.getquantifierValue());
		return filter;
	}
}
