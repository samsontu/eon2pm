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
 *  if precondition, then value = expression
 */
public class Conditional_Expression extends Expression {

	public Conditional_Expression(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
  static  Logger  logger = Logger.getLogger(Conditional_Expression.class);

	public void setpreconditionValue(Instance precondition) {
		ModelUtilities.setOwnSlotValue(this, "precondition", precondition);	}
	public Instance getpreconditionValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "precondition"));
	}

	public void setvalueValue(Instance value) {
		ModelUtilities.setOwnSlotValue(this, "value", value);	}
	public Instance getvalueValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "value"));
	}
// __Code above is automatically generated. Do not change
	private int debug=4;
	protected Expression ownEvaluateExpression(GuidelineInterpreter guidelineManager) 
	throws PCA_Session_Exception {
		Instance preconditionInst = getpreconditionValue();
		Instance value = getvalueValue();
		if (value == null) {
			throw new PCA_Session_Exception("Exception: no value in "+this.toString());
		}
		logger.debug("Conditional_Expression: ");
		if (preconditionInst != null) {
			Criterion precondition = (Criterion)preconditionInst;
			Criteria_Evaluation preconditionResult = precondition.evaluate(guidelineManager, false);
			logger.debug("Conditional_Expression: preconditionResult "+preconditionResult.truth_value);
			if ((preconditionResult != null) && (preconditionResult.truth_value.equals(Truth_Value._true))) {
				logger.debug("Conditional_Expression: expression value = "+ 
						((Expression) value).evaluate_expression(guidelineManager));
				return ((Expression) value).evaluate_expression(guidelineManager);
			} else {
				return null;
			}
		} else return ((Expression) value).evaluate_expression(guidelineManager);

	}
}
