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

//Created on Wed Jun 20 12:58:00 PDT 2001
//Copyright Stanford University 2000

package edu.stanford.smi.eon.criterion;

import java.util.*;
import java.io.*;
import java.text.NumberFormat;

import org.apache.log4j.Logger;

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
public class Expression extends DefaultSimpleInstance {

	public Expression(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}

	public void setlabelValue(String label) {
		ModelUtilities.setOwnSlotValue(this, "label", label);	}
	public String getlabelValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "label"));
	}
//	__Code above is automatically generated. Do not change

	static  Logger  logger = Logger.getLogger(Expression.class);

	public Guideline_Entity makeGuideline_Entity() {
		return new Guideline_Entity(getlabelValue(), "", "", getName());
	}
	
	public Collection doCollectionQuery(GuidelineInterpreter glmanager)
			  throws  PCA_Session_Exception {
		// Return result collection that can be modified
		Expression result = evaluate_expression(glmanager);
		if (result instanceof Set_Expression) {
			Collection resultCollection = new ArrayList();
			resultCollection.addAll(((Set_Expression)result).getset_elementsValue());
			return resultCollection;
		} else {
			Collection singleton = new ArrayList();
			singleton.add(result);
			return singleton;
		}
	}
	
	public String getSupport(Collection c) {
		// stub for getting support (e.g., relevant data) in evaluating criteria. See Structured_Query
		return "";
	}
	

	public Expression evaluate_expression(GuidelineInterpreter guidelineManager)
	throws PCA_Session_Exception {
		Expression evaluation = null;
//		if ((evaluation = (Expression)guidelineManager.evalManager.ask(this)) != null) {
//			return evaluation;
//		} else {
			logger.debug("Expression.evaluate_expression "+getlabelValue());
			evaluation = ownEvaluateExpression(guidelineManager);
//			if (evaluation != null) {
//				guidelineManager.evalManager.tell(this, evaluation);
//			}
			return evaluation;
//		}
	}

	protected Expression ownEvaluateExpression(GuidelineInterpreter guidelineManager) 
	throws PCA_Session_Exception {
		logger.error("The expression is not handled! "+this.getBrowserText());
		throw new PCA_Session_Exception("The expression is not handled! "+this.getBrowserText());
	}

    
public Expression evaluate_expression(GuidelineInterpreter guidelineManager,
		String join_value)
throws PCA_Session_Exception {
	throw new PCA_Session_Exception("The expression is not handled! "+this.getName());
}
public String getQualitativeConstantTerm() throws PCA_Session_Exception {
	throw new PCA_Session_Exception("This expression is not a qualitative constant! "+this.getName());
}

public boolean expressionEquals(Expression exp) {
	return this.equals(exp);
}

public String toString() {
	return getBrowserText();
}

public Expression substract(Expression second, GuidelineInterpreter glManager) throws PCA_Session_Exception {
	throw new PCA_Session_Exception("This expression is not something that can be subtracted from! "+this.getBrowserText());
}

}
