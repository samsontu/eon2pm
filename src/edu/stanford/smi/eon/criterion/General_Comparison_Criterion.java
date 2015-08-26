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

// Created on Wed Jun 20 12:58:00 PDT 2001
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
 *  if (value of domain_term  operator Get_Slot_Value) then true, else false
 */
public class General_Comparison_Criterion extends Comparison_Criterion {

	public General_Comparison_Criterion(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
	static  Logger  logger = Logger.getLogger(General_Comparison_Criterion.class);


	public void setoperatorValue(String operator) {
		ModelUtilities.setOwnSlotValue(this, "operator", operator);	}
	public String getoperatorValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "operator"));
	}

	public void setdefault_valueValue(String default_value) {
		ModelUtilities.setOwnSlotValue(this, "default_value", default_value);	}
	public String getdefault_valueValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "default_value"));
	}

	public void setvalueValue(Instance value) {
		ModelUtilities.setOwnSlotValue(this, "value", value);	}
	public Instance getvalueValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "value"));
	}

	public void setaggregation_operatorValue(String aggregation_operator) {
		ModelUtilities.setOwnSlotValue(this, "aggregation_operator", aggregation_operator);	}
	public String getaggregation_operatorValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "aggregation_operator"));
	}

	public void setentry_typeValue(Cls entry_type) {
		ModelUtilities.setOwnSlotValue(this, "entry_type", entry_type);	}
	public Cls getentry_typeValue() {
		return ((Cls) ModelUtilities.getOwnSlotValue(this, "entry_type"));
	}

	public void setassume_if_no_valueValue(String assume_if_no_value) {
		ModelUtilities.setOwnSlotValue(this, "assume_if_no_value", assume_if_no_value);	}
	public String getassume_if_no_valueValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "assume_if_no_value"));
	}

	public void setvalid_windowValue(Instance valid_window) {
		ModelUtilities.setOwnSlotValue(this, "valid_window", valid_window);	}
	public Instance getvalid_windowValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "valid_window"));
	}

	public void setdomain_termValue(Cls domain_term) {
		ModelUtilities.setOwnSlotValue(this, "domain_term", domain_term);	}
	public Cls getdomain_termValue() {
		return ((Cls) ModelUtilities.getOwnSlotValue(this, "domain_term"));
	}

	public void setmoodValue(Cls mood) {
		ModelUtilities.setOwnSlotValue(this, "mood", mood);	}
	public Cls getmoodValue() {
		return ((Cls) ModelUtilities.getOwnSlotValue(this, "mood"));
	}
	// __Code above is automatically generated. Do not change


	public Criteria_Evaluation ownEvaluate(GuidelineInterpreter guidelineManager, boolean doAll)
			throws PCA_Session_Exception {


		edu.stanford.smi.eon.datahandler.DataElement queryValue;
		float termValue = 0;
		float threshholdValue = 0;
		String thresholdValueString = "no threshold value";
		String valueString="";
		String comparisonValue = "";

		String assumption = getassume_if_no_valueValue();
		String compOperator = getoperatorValue();
		Cls domainTerm = this.getdomain_termValue();
		KnowledgeBase kb = this.getKnowledgeBase();
		Cls entryType = getentry_typeValue();
		Cls queryCls = null;

		Criteria_Evaluation evaluation = new Criteria_Evaluation(Logical_Operator.ATOMIC,
				Truth_Value.unknown, new Criteria_Evaluation[0], this.makeGuideline_Entity(),
				(domainTerm != null) ? domainTerm.getBrowserText() : null);
		evaluation.setParameter((domainTerm != null) ? domainTerm.getBrowserText() : null);
		Expression expr = null;
		boolean truthValue = false;
		boolean numeric = true;

		if (compOperator == null) {
			logger.error("No comparison operator in "+this.getBrowserText()
					+ "/"+this.getName());
			throw new PCA_Session_Exception();
		}
		if (domainTerm == null) {
			logger.error("No domain term in "+this.getBrowserText()
					+ "/"+this.getName());
			throw new PCA_Session_Exception();
		}
		if (compOperator.equals("eq") || compOperator.equals("neq") || compOperator.equals("subclass_of"))
			numeric = false;
		else numeric = true;

		NumberFormat nf = null;
		int precision = 0;
		Slot precisionSlot =  (Slot) kb.getSlot("Precision");
		if  (domainTerm.hasOwnSlot(precisionSlot)) {
			Object precisionObject =  domainTerm.getOwnSlotValue(precisionSlot);
			if (precisionObject != null) {
				nf = NumberFormat.getInstance();
				precision = ((Integer) precisionObject).intValue();
				nf.setMaximumFractionDigits(precision);
			}
		}

		if (!numeric) {
			Qualitative_Term_Query criterionQuery =
					(Qualitative_Term_Query) guidelineManager.getDBmanager().createInstance("Qualitative_Term_Query");
			criterionQuery.setaggregation_operatorValue(getaggregation_operatorValue());
			criterionQuery.setperiodValue(getvalid_windowValue());
			criterionQuery.setqualitative_domain_termValue(domainTerm.getName());
			queryValue = criterionQuery.doQuery(guidelineManager);

			guidelineManager.getDBmanager().deleteInstance(criterionQuery);

		} else { // not dealing with "member_of" yet
			Numeric_Term_Query criterionQuery =
					(Numeric_Term_Query) guidelineManager.getDBmanager().createInstance("Numeric_Term_Query");
			criterionQuery.setaggregation_operatorValue(getaggregation_operatorValue());
			criterionQuery.setperiodValue(getvalid_windowValue());
			criterionQuery.setnumeric_domain_termValue(domainTerm.getName());
			//if (domainTerm.hasSuperclass(kb.getCls("Note_Entry"))) {
			criterionQuery.setentry_typeValue(entryType);
			//} else if (domainTerm.hasSuperclass(kb.getCls("Medication"))) {
			//    criterionQuery.setentry_typeValue(kb.getCls("Medication"));
			//} else criterionQuery.setentry_typeValue(kb.getCls("Numeric_Entry"));

			queryValue = criterionQuery.doQuery(guidelineManager);
			guidelineManager.getDBmanager().deleteInstance(criterionQuery);
		}

		if (queryValue == null){
			logger.debug("'" + this.getBrowserText() +
					"' - queryValue = null");
			if ((assumption == null) || (assumption.equals("")) ||
					(assumption.equals("no_assumption")) ){
				evaluation.support = evaluation.support +"(no current value)";
				logger.debug("'"+this.getBrowserText() +"' Missing Data: return unknown ");
				return evaluation;
			}
			if (assumption.equals("assume_satisfied")) {
				evaluation.truth_value = Truth_Value._true;
				evaluation.support = evaluation.support +"(no current value, assume satisfied)";
				logger.debug("'"+this.getBrowserText() +"' Missing Data: assume satisfied (return true)");
				return evaluation;
			}
			if (assumption.equals("assume_unsatisfied")) {
				evaluation.truth_value = Truth_Value._false;
				evaluation.support = evaluation.support +"(no current value, assume not satisfied)";
				logger.debug("'"+this.getBrowserText() +"' Missing Data: assume unsatisfied (return false)");
				return evaluation;
			}
			if (assumption.equals("use_default")) {
				valueString = getdefault_valueValue();
				logger.debug("'"+this.getBrowserText() +"' Missing Data: use default value "+valueString);
				if (valueString == null) {
					evaluation.truth_value = Truth_Value.unknown;
					evaluation.support = evaluation.support +"(no current value)";
					return evaluation;
				} else {
					evaluation.support = evaluation.support +"(use default value/"+valueString;
				}
			}
		} else { //queryValue is not null
			logger.debug("in general comparison _criterion " + this.getlabelValue() +
					" - queryValue.value = " + queryValue.value);
			valueString = queryValue.value;
			if (nf != null) {

				valueString = nf.format(Double.parseDouble(valueString));
			}
			if ((queryValue.valid_time != null) && (!queryValue.valid_time.equals("")))
				evaluation.support = evaluation.support +"("+valueString+"/"+queryValue.valid_time;
			else evaluation.support = evaluation.support +"("+valueString;
		}
		// valueString is the data value

		if (numeric) {
			try {
				expr = (Numeric_Constant) ((Expression)getvalueValue()).evaluate_expression(
						guidelineManager);

			} catch (PCA_Session_Exception e) {
				logger.error("evaluating general comparison criterion - value field of "+
						this.toString() + " is not a number - " + e.msg);
				return null;
			}
			termValue = Float.parseFloat(valueString);
			if (expr == null) {
				logger.warn("Null expression for value of "+this.getBrowserText());
				evaluation.support = evaluation.support +"; target:"+thresholdValueString+")";
				return evaluation;
			} else {
				threshholdValue = ((Numeric_Constant) expr).getvalueValue();
				thresholdValueString = Float.toString(threshholdValue);
				if (compOperator.equals(">")) {
					truthValue =  (termValue > threshholdValue);
				} else if (compOperator.equals("<")) {
					truthValue =  (termValue < threshholdValue);
				} else if (compOperator.equals("=")) {
					truthValue =  (termValue == threshholdValue);
				} else if (compOperator.equals("<=")) {
					truthValue =  (termValue <= threshholdValue);
				} else if (compOperator.equals(">=")) {
					truthValue =  (termValue >= threshholdValue);
				} else if (compOperator.equals("!=")) {
					truthValue =  (termValue != threshholdValue);
				} else throw new PCA_Session_Exception ("Cannot evaluate "+this.toString());
			}
			evaluation.truth_value = (truthValue) ? Truth_Value._true : Truth_Value._false;
			logger.debug("'"+this.getBrowserText()+ "' return "+ evaluation.truth_value);
			evaluation.support = evaluation.support +"; target:"+thresholdValueString+")";
			return evaluation;
		} else {  //non-numeric
			try {
				Object value = getvalueValue();
				if (value instanceof DefaultCls) {
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
					comparisonValue = (((Expression)getvalueValue()).evaluate_expression(
							guidelineManager)).getQualitativeConstantTerm();
			} catch (PCA_Session_Exception e) {
				logger.error("'"+this.getBrowserText()+"' - value field is not an expression");
				return null;
			}
			if (compOperator.equals("eq")) {
				truthValue =  valueString.equals(comparisonValue);
			} else if (compOperator.equals("neq")) {
				truthValue =  (!valueString.equals(comparisonValue));
			} else if (compOperator.equals("subclass_of")) {
				truthValue = (queryCls.hasSuperclass((Cls)getvalueValue()) || queryCls.equals((Cls)getvalueValue()));
			} else throw new PCA_Session_Exception ("Cannot evaluate "+this.toString());
			evaluation.truth_value = (truthValue) ? Truth_Value._true : Truth_Value._false;
			logger.debug("'"+this.getlabelValue()+ "' return "	+ evaluation.truth_value);
			evaluation.support = evaluation.support +" compared to "+comparisonValue+")";
			return evaluation;
		}
	}

}
