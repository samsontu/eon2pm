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

import org.apache.log4j.Logger;

import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.util.*;
import edu.stanford.smi.eon.util.*;
import edu.stanford.smi.eon.guidelineinterpreter.*;
import edu.stanford.smi.eon.Dharma.Choice_Step;
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


public class Date_Expression extends Expression {
  
  static  Logger  logger = Logger.getLogger(Date_Expression.class);

	public Date_Expression(KnowledgeBase kb, FrameID id ) {
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

  public Expression ownEvaluateExpression(GuidelineInterpreter glManager)
      throws PCA_Session_Exception {  

    String operator = getoperatorValue();
    Expression expr;
    Expression first = (Expression) getfirst_argumentValue();
    Expression second = (Expression) getsecond_argumentValue();
    Absolute_Time_Point result = null;
    
    int timePoint = 0;
    Definite_Time_Point start = null;
    if (first != null) {
      first = first.evaluate_expression(glManager);
      if (first instanceof Set_Expression) {
    	  start = (Definite_Time_Point)((Set_Expression)first).getSingleton();
      } else start = (Definite_Time_Point)first;
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
    if ((start == null) || (second == null)) {
      throw new PCA_Session_Exception("Exception: null argument in "+this.toString());
    }

    if (operator == null) {
      throw new PCA_Session_Exception("Exception: null operator in "+this.toString());
    }
   logger.debug("Date_Expression.evaluate_expression: start system value = "+
       start.getsystem_timeValue() + " duration = " + 
       ((Duration) second).getRoundedDuration(Definite_Time_Point.getSystemTimeUnit()));
   if (operator.equals("+")) {
     
      timePoint = start.getsystem_timeValue() +
        ((Duration) second).getRoundedDuration(Definite_Time_Point.getSystemTimeUnit());
      result = (Definite_Time_Point)glManager.getDBmanager().createInstance("Definite_Time_Point");
      result.setDateValue(timePoint);
      return result;
      
    } else if (operator.equals("-")) {
      timePoint = ((Definite_Time_Point)first).getsystem_timeValue() - 
        ((Duration) second).getRoundedDuration(Absolute_Time_Point.getSystemTimeUnit());
      result = (Definite_Time_Point)glManager.getDBmanager().createInstance("Definite_Time_Point");
      result.setDateValue(timePoint);
      return result;
    } else
      throw new PCA_Session_Exception("Exception: unknown operator in "+this.toString());
    
  }

}
