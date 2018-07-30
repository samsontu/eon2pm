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
import java.text.NumberFormat;
import java.text.ParseException;
import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.eon.util.*;
import edu.stanford.smi.eon.guidelineinterpreter.*;
import edu.stanford.smi.eon.PCAServerModule.*;
import edu.stanford.smi.eon.kbhandler.*;
import org.apache.log4j.*;
/** 
 */
public class Numeric_Term_Criterion extends Comparison_Criterion {

	public Numeric_Term_Criterion(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
	static  Logger  logger = Logger.getLogger(Numeric_Term_Criterion.class);

	public void setoperatorValue(String operator) {
		ModelUtilities.setOwnSlotValue(this, "operator", operator);	}
	public String getoperatorValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "operator"));
	}

	public void setdefault_valueValue(float default_value) {
		ModelUtilities.setOwnSlotValue(this, "default_value", new Float(default_value));	}
	public float getdefault_valueValue() {
		return ((Float) ModelUtilities.getOwnSlotValue(this, "default_value")).floatValue();
	}

	public void setvalueValue(float value) {
		ModelUtilities.setOwnSlotValue(this, "value", new Float(value));	}
	public float getvalueValue() {
		return ((Float) ModelUtilities.getOwnSlotValue(this, "value")).floatValue();
	}

	public void setaggregation_operatorValue(String aggregation_operator) {
		ModelUtilities.setOwnSlotValue(this, "aggregation_operator", aggregation_operator);	}
	public String getaggregation_operatorValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "aggregation_operator"));
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

	public void setnumeric_domain_termValue(Cls numeric_domain_term) {
		ModelUtilities.setOwnSlotValue(this, "numeric_domain_term", numeric_domain_term);	}
	public Cls getnumeric_domain_termValue() {
		return ((Cls) ModelUtilities.getOwnSlotValue(this, "numeric_domain_term"));
	}

	public void setmoodValue(Cls mood) {
		ModelUtilities.setOwnSlotValue(this, "mood", mood);	}
	public Cls getmoodValue() {
		return ((Cls) ModelUtilities.getOwnSlotValue(this, "mood"));
	}

	public void setunitValue(String unit) {
		ModelUtilities.setOwnSlotValue(this, "unit", unit);	}
	public String getunitValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "unit"));
	}
	// __Code above is automatically generated. Do not change
	
	public Criteria_Evaluation ownEvaluate(GuidelineInterpreter guidelineManager, boolean doAll)
			throws PCA_Session_Exception {
		edu.stanford.smi.eon.datahandler.DataElement queryValue;
		double termValue = 0;
		String assumption = getassume_if_no_valueValue();
		String compOperator = getoperatorValue();
		Cls domainTerm = this.getnumeric_domain_termValue();
		if (domainTerm == null) {
			logger.error("'"+this.getBrowserText() +"'null domain term");
			throw new PCA_Session_Exception("null domain term in "+this.getBrowserText()+"("+ this.getName()+")");   
		}

		String valueString="";
		//String queryString="";
		boolean truthValue = false;
		KnowledgeBase kb = guidelineManager.getKBmanager().getKB();
		KBHandler kbHandler = guidelineManager.getKBmanager();
		Object entryTypeObj = this.getOwnSlotValue(kb.getSlot("entry_type"));
		Cls entryType = null;
		if (entryTypeObj != null) entryType = (Cls)entryTypeObj;
		else {
			if (domainTerm.hasSuperclass(kb.getCls(
					DharmaPaddaConstants.TopMedicalConditionClass))) {
				entryType = kb.getCls("Note_Entry");
			} else if (domainTerm.hasSuperclass(kb.getCls(DharmaPaddaConstants.TopMedicationClass))) {
				entryType = kb.getCls("Medication");
			} else entryType = kb.getCls("Numeric_Entry");
		}


		Numeric_Term_Query criterionQuery = (Numeric_Term_Query) guidelineManager.getDBmanager().createInstance("Numeric_Term_Query");
		criterionQuery.setaggregation_operatorValue(getaggregation_operatorValue());
		criterionQuery.setperiodValue(getvalid_windowValue());
		criterionQuery.setnumeric_domain_termValue(domainTerm.getName());
		criterionQuery.setmoodValue(getmoodValue());
		criterionQuery.setentry_typeValue(entryType);

		queryValue = criterionQuery.doQuery(guidelineManager);
		guidelineManager.getDBmanager().deleteInstance(criterionQuery);
		Criteria_Evaluation evaluation = (Criteria_Evaluation) new
				Criteria_Evaluation(Logical_Operator.ATOMIC,
						Truth_Value.unknown, new Criteria_Evaluation[0], this.makeGuideline_Entity(),
						getnumeric_domain_termValue().getBrowserText());
		evaluation.setParameter(domainTerm.getBrowserText());
		NumberFormat nf = NumberFormat.getInstance();
		int precision = 0;
		Slot precisionSlot =  (Slot) kb.getSlot("Precision");
		if  (domainTerm.hasOwnSlot(precisionSlot)) {
			Object precisionObject =  domainTerm.getOwnSlotValue(precisionSlot);
			if (precisionObject != null) {
				precision = ((Integer) precisionObject).intValue();
				nf.setMaximumFractionDigits(precision);
			}
		}

//		logger.debug("'"+this.getBrowserText() +"' Domain term: "+domainTerm.getBrowserText()+ ", Direct type: "+domainTerm.getDirectType());
//		logger.debug("'"+this.getBrowserText() +"' Has own defintion slot?: "+ domainTerm.hasOwnSlot(kb.getSlot("definition")));
//		logger.debug("'"+this.getBrowserText() +"' Definition slot values "+domainTerm.getOwnSlotValues(kb.getSlot("definition")));

		
		//Check if LHS term has a query result (queryValue). If not, check whether the parameter (e.g., Treatment SBP) has alternative
		//expressions (e.g., DB SBP)
		if (queryValue == null) {
			if (domainTerm.hasOwnSlot(kb.getSlot("definition"))) {
				Numeric_Constant evaluatedValue;
				Collection queries = domainTerm.getOwnSlotValues(kb.getSlot("definition"));
				if (queries != null) {
					for (Iterator i=queries.iterator(); i.hasNext();) {
						Expression expr = (Expression) i.next();
						Expression resultExpr =  expr.evaluate_expression(guidelineManager);
						if (resultExpr != null)  {
							try {
								evaluatedValue = (Numeric_Constant) resultExpr;
//								valueString = nf.format(Double.toString(evaluatedValue.getvalueValue());
//								+nf.format(Double.parseDouble(valueString) )+")"
								valueString = nf.format(evaluatedValue.getvalueValue());
								if (expr.getlabelValue() != null)
									evaluation.support = evaluation.support + "/"+ expr.getlabelValue();
								break;
							} catch (Exception e) {
								logger.error("'"+this.getBrowserText() +"' " + resultExpr + " not a Numeric_Constant");
							}
						}
					}
				}
			}
		} else {
			valueString = queryValue.value;
		}
		logger.debug("'"+this.getBrowserText() +"'  - queryValue = " +valueString);
		if (queryValue == null && valueString.equals("")) {
			if ((assumption == null) || (assumption.equals(""))) {
				evaluation.support = evaluation.support +"("+DharmaPaddaConstants.MISSINGDATA+")";
				logger.debug("'"+this.getBrowserText() +"' Missing Data: return unknown ");
				return evaluation;
			} else {
				if (assumption.equals("assume_satisfied")) {
					evaluation.truth_value = Truth_Value._true;
					evaluation.support = evaluation.support +"("+DharmaPaddaConstants.MISSINGDATA+", assume satisfied)";
					logger.debug("'"+this.getBrowserText() +"' Missing Data: assume satisfied ");
					return evaluation;
				} else {
					if (assumption.equals("assume_unsatisfied")) {
						evaluation.truth_value = Truth_Value._false;
						evaluation.support = evaluation.support +"("+DharmaPaddaConstants.MISSINGDATA+", assume not satisfied)";
						logger.debug("'"+this.getBrowserText() +"' Missing Data: assume unsatisfied ");
						return evaluation;
					} else if (assumption.equals("use_default")) {
						valueString = (String) this.getOwnSlotValue(this.getKnowledgeBase().getSlot("default_value"));
						logger.debug("'"+this.getBrowserText() +"' Missing Data: use default value "+valueString);
						if (valueString == null) {
							throw new PCA_Session_Exception ("Cannot evaluate "+this.toString());
						} else {
							valueString = Float.toString(getdefault_valueValue());
							evaluation.support = evaluation.support +"("+DharmaPaddaConstants.MISSINGDATA+", use default value/" +valueString+")";
						}
					} else {
						evaluation.support = evaluation.support +"("+DharmaPaddaConstants.MISSINGDATA+")";
						logger.debug("'"+this.getBrowserText() +"' Missing Data: return unknown  ");
						return evaluation;
					}
				}
			}
		} else {
			if ((queryValue != null) && (queryValue.valid_time != null) &&
					(!queryValue.valid_time.equals(""))) {
				Date date;
				String displayedDate;
				try {
					date = HelperFunctions.internalDateFormatter.parse(queryValue.valid_time);
					displayedDate = HelperFunctions.formatDisplayDate(date);
				} catch (ParseException e) {
					logger.error("'"+this.getBrowserText() +"' "+queryValue.valid_time + " cannot be put into MM-dd-yyyy format");
					displayedDate = queryValue.valid_time;
				}
				evaluation.support = evaluation.support +"("+valueString +"@"+
						displayedDate+")";
			} else {
				evaluation.support = evaluation.support +"("+valueString+")";
			}
		}
		//
		double threshholdValue = (double) getvalueValue();

		termValue = Double.parseDouble(valueString);
		logger.debug("'"+this.getBrowserText() +"' - term = " +termValue+" threshholdValue= "+threshholdValue);

		if (compOperator.equals(">")) {
			truthValue =  (termValue - threshholdValue) > DharmaPaddaConstants.float_error;
		} else if (compOperator.equals("<")) {
			truthValue =  (threshholdValue - termValue  ) > DharmaPaddaConstants.float_error;
		} else if (compOperator.equals("=")) {
			truthValue =  Math.abs(termValue - threshholdValue) < DharmaPaddaConstants.float_error;
		} else if (compOperator.equals("<=")) {
			truthValue =  (threshholdValue - termValue) > (0.0 - DharmaPaddaConstants.float_error );
		} else if (compOperator.equals(">=")) {
			truthValue =  (termValue - threshholdValue) > (0.0 - DharmaPaddaConstants.float_error);
		} else if (compOperator.equals("!=")) {
			truthValue =  Math.abs(termValue - threshholdValue) > DharmaPaddaConstants.float_error;
		} else throw new PCA_Session_Exception ("Cannot evaluate "+this.toString());
		evaluation.truth_value = (truthValue) ? Truth_Value._true : Truth_Value._false ;
		logger.debug("'"+this.getBrowserText() +"' return "
				+ evaluation.truth_value);
		return evaluation;

	}
}
