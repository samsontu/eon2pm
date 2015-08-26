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
public class N_ary_Criterion extends Criterion {

	public N_ary_Criterion(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
	static  Logger  logger = Logger.getLogger(N_ary_Criterion.class);

	public void setoperatorValue(String operator) {
		ModelUtilities.setOwnSlotValue(this, "operator", operator);	}
	public String getoperatorValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "operator"));
	}

	public void setcriteriaValue(Collection criteria) {
		ModelUtilities.setOwnSlotValues(this, "criteria", criteria);	}
	public Collection getcriteriaValue(){
		return  ModelUtilities.getOwnSlotValues(this, "criteria");
	}
	// __Code above is automatically generated. Do not change


	static private Truth_Value[][] threeValueAndTable = {{Truth_Value._true, Truth_Value._false, Truth_Value.unknown},
		{Truth_Value._false, Truth_Value._false, Truth_Value._false},
		{Truth_Value.unknown, Truth_Value._false, Truth_Value.unknown}};
	static private Truth_Value[][] threeValueOrTable= {{Truth_Value._true, Truth_Value._true, Truth_Value._true},
		{Truth_Value._true, Truth_Value._false, Truth_Value.unknown},
		{Truth_Value._true, Truth_Value.unknown, Truth_Value.unknown}};
	static private Truth_Value[] threeValueNotTable = {Truth_Value._false, Truth_Value._true, Truth_Value.unknown};

	public Criteria_Evaluation ownEvaluate(GuidelineInterpreter guidelineManager,
			boolean doAll)
					throws PCA_Session_Exception {
		// throws PCA_Session_Exception {
		Criteria_Evaluation evaluation = null;

		String booleanOperator = getoperatorValue();
		Collection criteria = getcriteriaValue();
		Collection childrenEvaluation;
		Criteria_Evaluation childEvaluation;
		String[] supportString = {"", "", ""};
		boolean[] first = {true, true, true};
		Truth_Value resultValue;

		if ((booleanOperator == null) || criteria.isEmpty()) {
			String message = "Boolean operator is null ("+booleanOperator +") or criteria ("+criteria+") is empty in "
					+getBrowserText()+"("+getName()+")";
			logger.error(message);
			throw new PCA_Session_Exception(message);
		}
		if ((booleanOperator.equals("AND")) ||(booleanOperator.equals("OR")))  {

			if (booleanOperator.equals("AND"))
				resultValue = Truth_Value._true;
			else resultValue = Truth_Value._false;

			evaluation = new Criteria_Evaluation(PCAInterfaceUtil.mapLogicalOperator(
					booleanOperator), Truth_Value._true, null, this.makeGuideline_Entity(), "");
			childrenEvaluation =  new ArrayList();
			for (Iterator i=criteria.iterator(); i.hasNext();) {
				Object obj = i.next();
				Criterion next = null;
				if (obj instanceof Criterion)
					next = (Criterion)obj;
				else {
					logger.error(((Instance)obj).getName() + " is not a Java Criterion");
					continue;
				}
				childEvaluation = next.evaluate(guidelineManager, doAll);
				if (booleanOperator.equals("AND"))
					resultValue = N_ary_Criterion.threeValueAnd(resultValue, childEvaluation.truth_value);
				else if (booleanOperator.equals("OR"))
					resultValue = N_ary_Criterion.threeValueOr(resultValue, childEvaluation.truth_value);

				if (first[childEvaluation.truth_value.value()]) {
					if ((childEvaluation.support != null) && (!childEvaluation.support.equals("")) && (!childEvaluation.support.equals("null")))
						supportString[childEvaluation.truth_value.value()] = childEvaluation.support;
					else supportString[childEvaluation.truth_value.value()] = "";
					first[childEvaluation.truth_value.value()] = false;
				} else {
					if ((childEvaluation.support != null) && (!childEvaluation.support.equals("")) && 
							(!childEvaluation.support.equals("null")))
						supportString[childEvaluation.truth_value.value()] =
						(supportString[childEvaluation.truth_value.value()].equals("")) ? childEvaluation.support : 
							supportString[childEvaluation.truth_value.value()] +
							" && "+childEvaluation.support;
				}
				childrenEvaluation.add(childEvaluation);
				if (!doAll) {
					if (((booleanOperator.equals("AND")) &&
							(resultValue.equals(Truth_Value._false))) ||
							((booleanOperator.equals("OR")) &&
									(resultValue.equals(Truth_Value._true)))) {
						break;
					}
				}
			}
			evaluation.truth_value = resultValue;
			evaluation.support = evaluation.support + supportString[resultValue.value()];
			evaluation.children = (Criteria_Evaluation[]) childrenEvaluation.toArray(new Criteria_Evaluation[0]);
			logger.debug("in N_ary_Criterion - return: "+evaluation.truth_value+
					" Name: "+this.getlabelValue());
			return evaluation;
		} else {
			if (booleanOperator.equals("NOT")) {
				if (criteria.size() > 1) {
					throw new PCA_Session_Exception("More than one clause in NOT " + criteria.toString());
				} else {
					evaluation = new Criteria_Evaluation(PCAInterfaceUtil.mapLogicalOperator(
							booleanOperator), null, null, this.makeGuideline_Entity(),
							this.getlabelValue());
					childrenEvaluation =  new ArrayList();
					for (Iterator i=criteria.iterator(); i.hasNext();) {
						Object next =  i.next();
						childEvaluation = ((Criterion)next).evaluate(guidelineManager, doAll);
						evaluation.truth_value = N_ary_Criterion.threeValueNot(childEvaluation.truth_value);
						evaluation.support = evaluation.support + childEvaluation.support ;
						childrenEvaluation.add(childEvaluation);
					}
					evaluation.children = (Criteria_Evaluation[]) childrenEvaluation.toArray(new Criteria_Evaluation[0]);
					logger.debug("in N_ary_Criterion - return: "+evaluation.truth_value+
							" Name: "+this.getlabelValue());

					return evaluation;
				}
			} else {
				throw new PCA_Session_Exception("Illegal boolean operator " + booleanOperator);
			}
		}

	}


	public static Truth_Value threeValueAnd(Truth_Value p1, Truth_Value p2) {
		return N_ary_Criterion.threeValueAndTable[p1.value()][p2.value()];
	}

	public static Truth_Value threeValueOr(Truth_Value p1, Truth_Value p2) {
		return N_ary_Criterion.threeValueOrTable[p1.value()][p2.value()];
	}
	public static Truth_Value threeValueNot(Truth_Value p1) {
		return N_ary_Criterion.threeValueNotTable[p1.value()];
	}

	public void printContent(PrintWriter itsWriter) {
		itsWriter.print("(");
		String op = getoperatorValue();
		Collection children = getcriteriaValue();
		boolean first = true;
		if (children == null) {
			itsWriter.print("Not well-formed criterion");
		} else {
			if (op.equals("NOT")) {
				itsWriter.print("NOT ");
				for (Iterator i=children.iterator(); i.hasNext();) {
					Criterion criterion = (Criterion)i.next();
					if (first == true) {
						criterion.printContent(itsWriter);
						first = false;
					}
				}
			} else {
				for (Iterator i=children.iterator(); i.hasNext();) {
					Criterion criterion = (Criterion)i.next();
					if (first != true)
						itsWriter.print(" "+op+" ");
					criterion.printContent(itsWriter);
					first = false;
				}
			}
		}
		itsWriter.print(")");
	}
}
