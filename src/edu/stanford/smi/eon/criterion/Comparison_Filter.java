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
public class Comparison_Filter extends Filter {

	public Comparison_Filter(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
	static  Logger  logger = Logger.getLogger(Comparison_Filter.class);

	public void setvalue_expressionValue(Instance value) {
		ModelUtilities.setOwnSlotValue(this, "value_expression", value);	}
	public Instance getvalue_expressionValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "value_expression"));
	}

	public void setoperatorValue(String operator) {
		ModelUtilities.setOwnSlotValue(this, "operator", operator);	}
	public String getoperatorValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "operator"));
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
		Instance valueExpr =   getvalue_expressionValue();
		if (valueExpr == null) {
			logger.error("value expression is null for "+this.getBrowserText());
			return null;
		}
		Expression value = null;
		if (valueExpr.hasType(this.getKnowledgeBase().getCls("Canonical_Terms_Metaclass")) ) {
			//value = ((Canonical_Terms_Metaclass)valueExpr).evaluate_expression(guidelineManager);
			//Canonical_Terms_Metaclass cls = new Canonical_Terms_Metaclass(this.getKnowledgeBase(), null);
			//cls.setClsName(valueExpr.getName());
			//value = cls;
			edu.stanford.smi.eon.datahandler.DataHandler dbmanager = guidelineManager.getDBmanager();
			Qualitative_Constant c =(Qualitative_Constant) dbmanager.createRegisteredInstance("Qualitative_Constant");
			c.setvalueValue(valueExpr.getName());
			value = c;
		} else value = ((Expression)valueExpr).evaluate_expression(guidelineManager);
		String attribute = (getattributeValue() == null ? DharmaPaddaConstants.NoAttribute : getattributeValue().getName());
		WhereComparisonFilter  filter= 
				new WhereComparisonFilter(attribute, getoperatorValue(), value);
/*		logger.debug("ComparisonFilter: constructExpression, returning WhereComparisonFilter "+
				getattributeValue().getName()+" "+ getoperatorValue()+ " " +
				value.toString());
*/		return filter;
	}
}
