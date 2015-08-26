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

// Created on Wed Jun 20 12:58:02 PDT 2001
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
public class Interval extends Expression {

	public Interval(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}

	public void sethighValue(Expression high) {
		ModelUtilities.setOwnSlotValue(this, "high", high);	}
	public Expression gethighValue() {
		return ((Expression) ModelUtilities.getOwnSlotValue(this, "high"));
	}
	public void setlowValue(Expression low) {
		ModelUtilities.setOwnSlotValue(this, "low", low);	}
	public Expression getlowValue() {
		return ((Expression) ModelUtilities.getOwnSlotValue(this, "low"));
	}
	public void sethigh_closedValue(boolean high_closed) {
		ModelUtilities.setOwnSlotValue(this, "high_closed", new  Boolean(high_closed));	}
	public boolean gethigh_closedValue() {
		return ((Boolean) ModelUtilities.getOwnSlotValue(this, "high_closed")).booleanValue();
	}
	public void setlow_closedValue(boolean low_closed) {
		ModelUtilities.setOwnSlotValue(this, "low_closed", new  Boolean(low_closed));	}
	public boolean getlow_closedValue() {
		return ((Boolean) ModelUtilities.getOwnSlotValue(this, "low_closed")).booleanValue();
	}
// __Code above is automatically generated. Do not change

  public Expression ownEvaluateExpression(GuidelineInterpreter guidelineManager)
      throws PCA_Session_Exception {
	  Expression high = gethighValue().evaluate_expression(guidelineManager);
	  Expression low = getlowValue().evaluate_expression(guidelineManager);
	  Interval newInterval = (Interval)guidelineManager.getDBmanager().createInstance("Interval");
	  newInterval.sethigh_closedValue(gethigh_closedValue());
	  newInterval.setlow_closedValue(getlow_closedValue());
	  newInterval.sethighValue(high);
	  newInterval.setlowValue(low);
      return newInterval;
  }

  
  public String toString() {
	  return (getlow_closedValue()? "[" : "(") + getlowValue().toString()+ " to " + 
	  	gethighValue().toString()+(gethigh_closedValue()? "]" : ")");
  }

}
