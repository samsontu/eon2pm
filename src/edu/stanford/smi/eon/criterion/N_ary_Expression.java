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
import org.apache.log4j.*;

/** 
 */
public class N_ary_Expression extends Expression {

	public N_ary_Expression(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
  static Logger logger = Logger.getLogger(N_ary_Expression.class);
	public void setvaluesValue(Collection values) {
		ModelUtilities.setOwnSlotValues(this, "values", values);	}
	public Collection getvaluesValue(){
		return  ModelUtilities.getOwnSlotValues(this, "values");
	}

	public void setoperatorValue(String operator) {
		ModelUtilities.setOwnSlotValue(this, "operator", operator);	}
	public String getoperatorValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "operator"));
	}
// __Code above is automatically generated. Do not change

  public Expression ownEvaluateExpression(GuidelineInterpreter glmanager)
      throws PCA_Session_Exception {
    KBHandler kbmanager = glmanager.getKBmanager();
    edu.stanford.smi.eon.datahandler.DataHandler dbmanager = glmanager.getDBmanager();

    String compOperator = getoperatorValue();
    Expression expr;
    Collection values = getvaluesValue();
    Numeric_Constant result = null;

    if (compOperator == null) {
      throw new PCA_Session_Exception("Exception: null operator in "+this.toString());
    } else {
      if (compOperator.equals("*"))
            result = multiply(values, glmanager);
      else if (compOperator.equals("+"))
        result = add(values, glmanager);
      else {
    	  logger.error("Exception: no such operator '"+compOperator+"' in "+getlabelValue());
    	  throw new PCA_Session_Exception();
      }
    }
    return result;
  }

  public Numeric_Constant add (Collection args, GuidelineInterpreter glManager)
    throws PCA_Session_Exception {
    logger.debug("N_ary_Expression: add "+getlabelValue());
    Numeric_Constant result = (Numeric_Constant) glManager.getDBmanager().createRegisteredInstance("Numeric_Constant");
    result.setvalueValue((float)0.0);
    for (Iterator i=args.iterator(); i.hasNext();) {
    	Expression arg = (Expression)i.next();
      Expression argValue = arg.evaluate_expression(glManager);
       if (argValue != null) {
    	  if (ModelUtilities.getOwnSlotValue(argValue, "value") != null) {
           logger.debug(".......* "+((Numeric_Constant)argValue).getvalueValue());
    	  	result.setvalueValue(result.getvalueValue() + ((Numeric_Constant)argValue).getvalueValue());
      } } else throw new PCA_Session_Exception("Exception: no evaluated value for "+ arg.getBrowserText( )
    		  +"("+arg.getName()+")");
    }
    logger.debug("....... result: "+result.getvalueValue());
    return result;
  }

  public Numeric_Constant multiply (Collection args,
    GuidelineInterpreter glManager)
     throws PCA_Session_Exception {
    Numeric_Constant result =
        (Numeric_Constant) glManager.getDBmanager().createRegisteredInstance("Numeric_Constant");
        result.setvalueValue((float)1.0);
    Numeric_Constant evaluatedArg = null;
    logger.debug("N_ary_Expression: multiply "+getlabelValue());
    for (Iterator i=args.iterator(); i.hasNext();) {
      Expression argValue = ((Expression) i.next()).evaluate_expression(glManager);
      if (argValue != null) {
    	  // Allow for the possibility that argValue may be a Set containing one object
    	  if (argValue instanceof Set_Expression) {
    		  evaluatedArg = coerceToNumeric((Set_Expression)argValue, glManager);
    	  } else
    		  evaluatedArg = (Numeric_Constant)argValue;
        result.setvalueValue(result.getvalueValue() * evaluatedArg.getvalueValue());
        logger.debug(".......* "+evaluatedArg.getvalueValue());
      } else {
    	  logger.error("Evaluating expression returns null");
    	  throw new PCA_Session_Exception("Exception: null argument in "+this.toString());
      }
    }
    logger.debug("....... result: "+result.getvalueValue());
    return result;
  }
  
  protected Numeric_Constant coerceToNumeric(Set_Expression argValue, GuidelineInterpreter glManager) 
  	throws PCA_Session_Exception {
	  Numeric_Constant evaluatedArg = null;
	  Collection setValue = argValue.getset_elementsValue();
	  if (setValue.size() != 1) {
		  throw new PCA_Session_Exception("Number of entries in set is not 1");
	  } else {
		  evaluatedArg = (Numeric_Constant) glManager.getDBmanager().createRegisteredInstance("Numeric_Constant");
		  for (Iterator j=setValue.iterator(); j.hasNext();) {
			  Object num = j.next();
			  if (!(num instanceof Float)) {
				  throw new PCA_Session_Exception("Not a number!");
			  } else {
				  evaluatedArg.setvalueValue(((Float)num).floatValue());
			  }
		  }
	  }
	  return evaluatedArg;
  }

  private static int instanceCounter = 0;
}
