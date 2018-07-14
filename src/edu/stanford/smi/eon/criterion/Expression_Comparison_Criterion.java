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
/*
 * Created on Jul 25, 2006
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License");  you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 *
 * The Initial Developer of the Original Code is Stanford University. Portions
 * created by Stanford University are Copyright (C) 2004.  All Rights Reserved.
 *
 * This code was developed by Stanford Medical Informatics
 * (http://www.smi.stanford.edu) at the Stanford University School of Medicine
 *
 * Contributor(s):
 */
package edu.stanford.smi.eon.criterion;

import java.text.NumberFormat;
import java.util.Collection;

import org.apache.log4j.Logger;

import edu.stanford.smi.eon.PCAServerModule.Criteria_Evaluation;
import edu.stanford.smi.eon.PCAServerModule.Logical_Operator;
import edu.stanford.smi.eon.PCAServerModule.PCA_Session_Exception;
import edu.stanford.smi.eon.PCAServerModule.Truth_Value;
import edu.stanford.smi.eon.guidelineinterpreter.GuidelineInterpreter;
import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.DefaultCls;
import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.ModelUtilities;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.eon.criterion.Set_Expression;

public class Expression_Comparison_Criterion extends Comparison_Criterion {
	public Expression_Comparison_Criterion(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
	static  Logger  logger = Logger.getLogger(Expression_Comparison_Criterion.class);


	public void setoperatorValue(String operator) {
		ModelUtilities.setOwnSlotValue(this, "operator", operator); }
	public String getoperatorValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "operator"));
	}
	public void setvariableValue(Expression variable) {
		ModelUtilities.setOwnSlotValue(this, "variable", variable); }
	public Expression getvariableValue() {
		return ((Expression) ModelUtilities.getOwnSlotValue(this, "variable"));
	}
	public void setexpressionValue(Expression expression) {
		ModelUtilities.setOwnSlotValue(this, "expression", expression); }
	public Expression getexpressionValue() {
		return ((Expression) ModelUtilities.getOwnSlotValue(this, "expression"));
	}


	public Criteria_Evaluation ownEvaluate(GuidelineInterpreter guidelineManager, boolean doAll)
			throws PCA_Session_Exception {

		String compOperator = getoperatorValue();
		Object LHS = evaluateParameter(this.getvariableValue(), guidelineManager, compOperator);
		Object RHS = evaluateParameter(this.getexpressionValue(), guidelineManager, compOperator);
		Criteria_Evaluation evaluation = evaluateComparison(LHS, compOperator, RHS, guidelineManager);
		evaluation.setParameter((getvariableValue() != null) ? getvariableValue().getBrowserText() : null);
		return evaluation;
	}

	public Object evaluateParameter(Expression parameter, GuidelineInterpreter guidelineManager, String compOperator)  {
		// returns a single value of any elementary datatype, Cls, Numeric_Constant or Qualitative_Constant
		Expression result = null;
		/*		if ((compOperator == null) || compOperator.equals("member_of")){
			logger.error("Error: Null or unsupported 'member_of' comparison operator in "+ getBrowserText());
		 */		if (compOperator == null){
			 logger.error("Error: Null comparison operator in "+ getBrowserText());
			 return null;
		 }
		 if (parameter != null) {
			 try {
				 result = parameter.evaluate_expression(guidelineManager);
			 } catch (Exception e){
				 logger.error("Exception evaluating expression in "+parameter.getBrowserText());
				 e.printStackTrace();
			 }
//			 if (result == null) {
//				 logger.warn("Warning evaluating "+parameter.getBrowserText()+ ": No evaluation result");
//				 return null;
//			 }
			 /*			if (result instanceof Set_Expression) {
				Collection elements = ((Set_Expression)result).getset_elementsValue();
				if (elements.size() > 1) {
					logger.error("Error evaluating "+parameter.getBrowserText()+ ": have multiple values");
					return null;}
				if (elements.size() == 0) {
						logger.error("Error evaluating "+parameter.getBrowserText()+ ": have no values");
						return null;}
				for (Object obj : elements)
					return obj;
			}
			  */		} else logger.error("Error: Null expression in "+ getBrowserText());
		 return result;
	}

	private boolean numeric(String compOperator) {
		if (compOperator == null) {
			logger.error("Null comparison operator in "+this.getBrowserText());
			return false;
		}
		if (compOperator.equals("member_of")) {
			return false;
		}
		if (compOperator.equals("eq") || compOperator.equals("neq") || compOperator.equals("subclass_of"))
			return false;
		else return true;
	}

	private boolean qualitative(String compOperator) {
		if (compOperator == null) {
			logger.error("Null comparison operator in "+this.getBrowserText());
			return false;
		} else
			return (compOperator.equals("eq") || compOperator.equals("neq")) ;
	}

	private float parseNumericResult(Object value) throws Exception {
		float result = 0;
		if (value instanceof String)
			result = Float.parseFloat((String)value);
		else if (value instanceof Numeric_Constant)
			result = ((Numeric_Constant) value).getvalueValue();
		else if (value instanceof Float){
			result = ((Float)value).floatValue();
		} else if (value instanceof Set_Expression){
			return parseNumericResult(((Set_Expression)value).getSingleton());
		} else {
			logger.error("Unknown float type "+value+": "+value.getClass().toString());
			throw new Exception ("Unknown float type "+value);
		}
		return result;
	}

	private String parseQualitativeResult(Object value) throws Exception {
		String result = null;
		if (value instanceof String)
			result = (String)value;
		else if (value instanceof Qualitative_Constant)
			result = ((Qualitative_Constant) value).getvalueValue();
		else if (value instanceof Set_Expression){
			return parseQualitativeResult(((Set_Expression)value).getSingleton());
		} else
		{
			logger.error("Unknown qualitative constant type "+value);
			throw new Exception ("Unknown qualitative constant type "+value);
		}
		return result;
	}

	private Cls parseCls(Object value)  throws Exception {
		Cls result = null;
		if (value instanceof String) {
			result = this.getKnowledgeBase().getCls((String)value);
			if (result == null) {
				logger.error("Unknown Cls type"+value);
				throw new Exception ("Unknown Cls type "+value);
			}
		} else if (value instanceof Cls)
			result = (Cls)value;
		else if (value instanceof Set_Expression){
			return parseCls(((Set_Expression)value).getSingleton());
		} else {
			logger.error("Unknown Cls type"+value);
			throw new Exception ("Unknown Cls type "+value);
		}
		return result;
	}

	public  Criteria_Evaluation evaluateComparison(Object LHS, String compOperator, Object RHS, GuidelineInterpreter guidelineManager) {
		boolean truthValue = false;
		float LHSnumValue = 0;
		float RHSnumValue = 0;
		String LHSString = "";
		String RHSString="no threshold value";
		Cls LHSCls = null;
		Cls RHSCls = null;
		Criteria_Evaluation evaluation = new Criteria_Evaluation(Logical_Operator.ATOMIC,
				Truth_Value.unknown, new Criteria_Evaluation[0], this.makeGuideline_Entity(),
				null);
		try {
			if (numeric(compOperator)) {
				LHSnumValue = parseNumericResult(LHS);
				RHSnumValue = parseNumericResult(RHS);
				if (compOperator.equals(">")) {
					truthValue =  (LHSnumValue > RHSnumValue);
				} else if (compOperator.equals("<")) {
					truthValue =  (LHSnumValue < RHSnumValue);
				} else if (compOperator.equals("=")) {
					truthValue =  (LHSnumValue == RHSnumValue);
				} else if (compOperator.equals("<=")) {
					truthValue =  (LHSnumValue <= RHSnumValue);
				} else if (compOperator.equals(">=")) {
					truthValue =  (LHSnumValue >= RHSnumValue);
				} else if (compOperator.equals("!=")) {
					truthValue =  (LHSnumValue != RHSnumValue);
				} else throw new PCA_Session_Exception ("Cannot evaluate "+this.toString());
				evaluation.support = evaluation.support +"; "+LHSnumValue +compOperator+RHSnumValue +")";
			} else {  //non-numeric
				if (qualitative(compOperator))  {
					LHSString = parseQualitativeResult(LHS);
					RHSString = parseQualitativeResult(RHS);
					if (compOperator.equals("eq")) {
						truthValue =  LHSString.equals(RHSString);
					} else if (compOperator.equals("neq")) {
						truthValue =  (!LHSString.equals(RHSString));
					}
					evaluation.support = evaluation.support +"; "+LHSString +compOperator+RHSString +")";
				} else if (compOperator.equals("subclass_of")) {
					LHSCls = parseCls(LHS);
					RHSCls = parseCls(RHS);
					truthValue = (LHSCls.hasSuperclass(RHSCls) || LHSCls.equals(RHSCls));
				} else if (compOperator.equals("member_of")) {
					if (RHS instanceof Set_Expression) {
						if (LHS instanceof Qualitative_Constant)
							LHS = ((Qualitative_Constant) LHS).getvalueValue();
						truthValue = ((Set_Expression)RHS).getset_elementsValue().contains(LHS);
						evaluation.support = evaluation.support +"; "+LHS.toString()+" " +compOperator+" (or not) "+((Set_Expression)RHS).toString() +")";
					} else 
						logger.error(RHS.toString() + " is not a set, cannot compare member_of operator");
				} 
			}
		} catch (Exception e){

		}
		evaluation.truth_value = (truthValue) ? Truth_Value._true : Truth_Value._false;
		logger.debug("in Expression_Comparison_Criterion - criterion = "+this.getlabelValue()+ " return "
				+ evaluation.truth_value);
		return evaluation;
	}

	/*				if (value instanceof DefaultCls) {
					if (compOperator.equals("subclass_of")){
						queryCls = kb.getCls(valueString);
						if (queryCls == null) {
							logger.error(valueString +" is not the name of a class in "+this.getlabelValue());
							return null;
						}
					} else
						comparisonValue = ((Canonical_Terms_Metaclass) value).evaluate_expression(
								guidelineManager).getQualitativeConstantTerm();
				} else
					comparisonValue = (((Expression)RHS).evaluate_expression(
							guidelineManager)).getQualitativeConstantTerm();
			} catch (PCA_Session_Exception e) {
				logger.error("evaluating general comparison criterion - value field of "+
						this.toString() + " is not an expression");
				return null;
			}

			} else if (compOperator.equals("subclass_of")) {
				truthValue = (queryCls.hasSuperclass((Cls)RHS) || queryCls.equals((Cls)RHS));
			} else throw new PCA_Session_Exception ("Cannot evaluate "+this.toString());
	 */			

}
