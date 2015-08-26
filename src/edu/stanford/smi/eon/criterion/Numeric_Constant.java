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
public class Numeric_Constant extends Expression {

	public Numeric_Constant(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}

	public void setvalueValue(float value) {
		ModelUtilities.setOwnSlotValue(this, "value", new Float(value));	}
	public float getvalueValue() {
		return ((Float) ModelUtilities.getOwnSlotValue(this, "value")).floatValue();
	}
// __Code above is automatically generated. Do not change

  public Expression ownEvaluateExpression(GuidelineInterpreter guidelineManager)
      throws PCA_Session_Exception {
      return this;
  }

  public Numeric_Constant subtract (Numeric_Constant arg, GuidelineInterpreter glManager) {
    Numeric_Constant result = (Numeric_Constant) glManager.getDBmanager().createInstance("Numeric_Constant");
    result.setvalueValue(this.getvalueValue() - arg.getvalueValue());
    return result;
  }
  public Numeric_Constant divide (Numeric_Constant arg, GuidelineInterpreter glManager) {
    Numeric_Constant result = (Numeric_Constant) glManager.getDBmanager().createInstance("Numeric_Constant");
    result.setvalueValue(this.getvalueValue() / arg.getvalueValue());
    return result;

  }
  public Numeric_Constant power (Numeric_Constant arg, GuidelineInterpreter glManager) {
    Numeric_Constant result = (Numeric_Constant) glManager.getDBmanager().createInstance("Numeric_Constant");
    result.setvalueValue((float)Math.pow((double)this.getvalueValue() , (double) arg.getvalueValue()));
    return result;

  }
  public Numeric_Constant log (GuidelineInterpreter glManager) {
    Numeric_Constant result = (Numeric_Constant) glManager.getDBmanager().createInstance("Numeric_Constant");
    result.setvalueValue((float)Math.log((double)this.getvalueValue()));
    return result;

  }
  public Numeric_Constant exp ( GuidelineInterpreter glManager) {
    Numeric_Constant result = (Numeric_Constant) glManager.getDBmanager().createInstance("Numeric_Constant");
    result.setvalueValue((float)Math.exp((double)this.getvalueValue()));
    return result;

  }

  public boolean expressionEquals(Expression e) {
    if (e.getDirectType().getName().equals("Numeric_Constant"))
      return getvalueValue() == ((Numeric_Constant)e).getvalueValue();
    else return false;
  }
  public boolean eq(Float f) {
    return (f.floatValue() == getvalueValue());
  }
  public boolean gt(Float f) {
    return (getvalueValue() > f.floatValue());
  }
  public boolean ge(Float f) {
    return (getvalueValue() >= f.floatValue());
  }
  public boolean lt(Float f) {
    return (getvalueValue() < f.floatValue());
  }
  public boolean le(Float f) {
    return (getvalueValue() <= f.floatValue());
  }
  
  public String toString() {
	  return Float.toString(getvalueValue());
  }

  private static int instanceCounter = 0;
}
