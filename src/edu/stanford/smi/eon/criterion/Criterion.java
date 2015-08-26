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
 */
public class Criterion extends Expression {

	public Criterion(KnowledgeBase kb, FrameID id) {
		super(kb, id);
	}

	static Logger logger = Logger.getLogger(Criterion.class);

	// __Code above is automatically generated. Do not change

	private static Expression trueInstance = null;
	private static Expression falseInstance = null;

	public Criteria_Evaluation evaluate(GuidelineInterpreter guidelineManager,
			boolean doAll) throws PCA_Session_Exception {
		Criteria_Evaluation evaluation;
		//System.out.println("Criteria(this.getlabelValue):"+ this.getlabelValue());
		if ((evaluation = (Criteria_Evaluation) guidelineManager.evalManager
				.ask(this)) != null) {
			
			//System.out.println("evaluation->" + evaluation.support);
			return evaluation;
		} else {
			logger.debug("Criterion.evaluate " + getlabelValue());
			evaluation = ownEvaluate(guidelineManager, doAll);
			if (evaluation != null) {
				guidelineManager.evalManager.tell(this, evaluation);
			}
			return evaluation;
		}
	}

	protected Criteria_Evaluation ownEvaluate(
			GuidelineInterpreter guidelineManager, boolean doAll)
			throws PCA_Session_Exception {

		logger.debug("in Criterion.ownEvaluate: " + this.getlabelValue()
				+ " using generic ownEvaluate method");
		Truth_Value truthValue = Truth_Value.unknown;
		if (this.getlabelValue().equals("true")) {
			truthValue = Truth_Value._true;
		}
		if (this.getlabelValue().equals("false")) {
			truthValue = Truth_Value._false;
		}
		Criteria_Evaluation evaluation = new Criteria_Evaluation(
				Logical_Operator.ATOMIC, truthValue,
				new Criteria_Evaluation[0], this.makeGuideline_Entity(), "");
		return evaluation;
	}

	public void printContent(PrintWriter itsWriter) {
		itsWriter.print("(" + getlabelValue() + ")");
	}

	/* 
	 public Expression evaluate_expression(GuidelineInterpreter glmanager)
	 throws PCA_Session_Exception {
	  Criteria_Evaluation eval = evaluate(glmanager, false);
	  Qualitative_Constant expr;
	  if (eval != null) {
	      expr = (Qualitative_Constant)glmanager.getDBmanager().createInstance("Qualitative_Constant");
	      expr.setvalueValue(eval.truth_value.toString());
	      return expr;
	    } else {
	      throw new PCA_Session_Exception("Exception: no evaluated result in "+this.getBrowserText());
	    }
	  }

	 */

	protected Expression ownEvaluateExpression(
			GuidelineInterpreter guidelineManager) throws PCA_Session_Exception {
		Cls cls = this.getKnowledgeBase().getCls("Criterion");
		if ((trueInstance == null) || falseInstance == null) {
			Collection instances = cls.getDirectInstances();
			for (Iterator i = instances.iterator(); i.hasNext();) {
				Criterion instance = (Criterion) i.next();
				logger.debug("Criterion.evaluate_expression: instance "
						+ instance.getlabelValue());
				if ((instance.getlabelValue() != null) && (instance.getlabelValue().equals("true")))
					trueInstance = instance;
				else if ((instance.getlabelValue() != null) && (instance.getlabelValue().equals("false")))
					falseInstance = instance;
			}
		}
		if ((trueInstance == null) || (falseInstance == null)) {
			logger.error("Error evaluating as expression: " + getBrowserText());
			throw new PCA_Session_Exception("Null true or false instances!!");
		}
		logger.debug("Criterion.evaluate_expression: ");
		Criteria_Evaluation result = ownEvaluate(guidelineManager, false);
		logger
				.debug("Criterion.evaluate_expression: result: "
						+ result.support);
		if (result.truth_value.equals(Truth_Value._true))
			return trueInstance;
		else
			return falseInstance;
	}

	public boolean equals(Criterion criterion) {
		return (super.equals(criterion) || (this.getlabelValue()
				.equals(criterion.getlabelValue())));
	}

	public String toString() {
		return this.getlabelValue();
	}

}
