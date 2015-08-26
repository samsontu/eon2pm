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
public class Unary_Expression extends Expression {

	public Unary_Expression(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}

	public void setargumentValue(Instance first_argument) {
		ModelUtilities.setOwnSlotValue(this, "argument", first_argument);	}
	public Instance getargumentValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "argument"));
	}



	public void setoperatorValue(String operator) {
		ModelUtilities.setOwnSlotValue(this, "operator", operator);	}
	public String getoperatorValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "operator"));
	}
// __Code above is automatically generated. Do not change

  public Expression evaluate_expression(GuidelineInterpreter glManager)
      throws PCA_Session_Exception {  

    String compOperator = getoperatorValue();
    Expression expr;
    Expression argument = (Expression) getargumentValue();
    
    if (argument != null) {
      argument = argument.evaluate_expression(glManager);
    } else {
      throw new PCA_Session_Exception("Exception: null argument in "+
        this.getBrowserText());
    }


    if (compOperator == null) {
      throw new PCA_Session_Exception("Exception: null operator in "+this.getBrowserText());
    }
   if (compOperator.equals("log")) {
      return ((Numeric_Constant)argument).log( glManager);
    } else if (compOperator.equals("exp")) {
      return ((Numeric_Constant)argument).exp(  glManager);
    }  else if (compOperator.equals("count")) {
    	if (argument instanceof Set_Expression) {
    		return ((Set_Expression)argument).size(glManager);
    	} else {
    		logger.error("Argument for the size of set is not a set: "+argument.toString());
    		throw new PCA_Session_Exception("Exception: unknown argument in "+this.getBrowserText());
    	}
    } else
      throw new PCA_Session_Exception("Exception: unknown operator in "+this.getBrowserText());
    
    }

}
