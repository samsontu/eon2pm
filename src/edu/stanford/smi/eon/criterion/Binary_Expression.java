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

/** 
 */
public class Binary_Expression extends Expression {

	public Binary_Expression(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}

	public void setfirst_argumentValue(Instance first_argument) {
		ModelUtilities.setOwnSlotValue(this, "first_argument", first_argument);	}
	public Instance getfirst_argumentValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "first_argument"));
	}

	public void setsecond_argumentValue(Instance second_argument) {
		ModelUtilities.setOwnSlotValue(this, "second_argument", second_argument);	}
	public Instance getsecond_argumentValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "second_argument"));
	}

	public void setoperatorValue(String operator) {
		ModelUtilities.setOwnSlotValue(this, "operator", operator);	}
	public String getoperatorValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "operator"));
	}
	// __Code above is automatically generated. Do not change

	protected Expression ownEvaluateExpression(GuidelineInterpreter glManager) 
			throws PCA_Session_Exception {  

		String compOperator = getoperatorValue();
		Expression expr;
		Expression first = (Expression) getfirst_argumentValue();
		Expression second = (Expression) getsecond_argumentValue();

		if (first != null) {
			first = first.evaluate_expression(glManager);
		} else {
			throw new PCA_Session_Exception("Exception: null first argument in "+
					this.toString());
		}
		if (second != null) {
			second = second.evaluate_expression(glManager);
		} else {
			throw new PCA_Session_Exception("Exception: null second argument in "+
					this.toString());
		}
		if (first == null) {
			logger.error("Exception: null first argument in "+this.toString());
			throw new PCA_Session_Exception("Exception: null argument in "+this.toString());
		}

		if (compOperator == null) {
			throw new PCA_Session_Exception("Exception: null operator in "+this.toString());
		}
		if (second == null ) {
			if ((compOperator.equals("-")) && (first instanceof Set_Expression)){
				return first;
			} else {
			logger.error("second argument of " +this.toString() +" is null");
			throw new PCA_Session_Exception("second argument of " +this.toString() +" is null");
			}
		}
		if (compOperator.equals("/")) {
			return ((Numeric_Constant)first).divide((Numeric_Constant) second, glManager);
		} else if (compOperator.equals("-")) {
			return generalSubtraction(first, second, glManager);
		} else if (compOperator.equals("^")){
			return ((Numeric_Constant)first).power((Numeric_Constant) second, glManager);
		}  else
			throw new PCA_Session_Exception("Exception: unknown operator in "+this.toString());

	}

	public Expression generalSubtraction(Expression first, Expression second, GuidelineInterpreter glManager) {
		try {
			Expression result = null;
			if ((first instanceof Numeric_Constant) && (second instanceof Numeric_Constant)) {
				result = ((Numeric_Constant)first).subtract((Numeric_Constant)second, glManager);
			} else if ((first instanceof Definite_Time_Point) && (second instanceof Duration)) {
				result = ((Definite_Time_Point)first).subtract((Duration)second, glManager);
			} else if ((first instanceof Set_Expression) && (second instanceof Set_Expression))
				result = ((Set_Expression)first).subtract((Set_Expression)second, glManager);
			//Expression result = first.substract(second, glManager);
			return result;
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
		/*		if ((first instanceof Numeric_Constant) && (second instanceof Numeric_Constant)) {
			result = ((Numeric_Constant)first).subtract((Numeric_Constant)second, glManager);
		} else if ((first instanceof Definite_Time_Point) && (second instanceof Duration)) {
			result = ((Definite_Time_Point)first).subtract((Duration)second, glManager);
		}
		 */		


	}


}
