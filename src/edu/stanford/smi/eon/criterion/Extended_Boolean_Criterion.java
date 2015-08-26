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
public class Extended_Boolean_Criterion extends Criterion {

	public Extended_Boolean_Criterion(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
  static  Logger  logger = Logger.getLogger(Extended_Boolean_Criterion.class);

// __Code above is automatically generated. Do not change

  private static Expression trueInstance = null;
  private static Expression falseInstance = null;

  public Criteria_Evaluation ownEvaluate(GuidelineInterpreter guidelineManager, boolean doAll)
    throws PCA_Session_Exception {
    edu.stanford.smi.eon.datahandler.DataElement queryValue;
    double termValue = 0;
    if (getlabelValue() == null) {
      logger.error(this.getName() +" has null label");
      throw new PCA_Session_Exception(this.getName()+
      " has null label");
    } else {
      Criteria_Evaluation evaluation = new Criteria_Evaluation(Logical_Operator.ATOMIC,
        (getlabelValue().equals("true")) ? Truth_Value._true :
        ((getlabelValue().equals("false")) ? Truth_Value._false : Truth_Value.unknown),
        new Criteria_Evaluation[0],
        this.makeGuideline_Entity(),
        "");
      return evaluation;
    }

  }

    public Expression ownEvaluateExpression(GuidelineInterpreter guidelineManager)
      throws PCA_Session_Exception {
      Cls cls = this.getKnowledgeBase().getCls("Extended_Boolean_Criterion");
      if ((trueInstance == null) || falseInstance == null) {
        Collection instances = cls.getDirectInstances();
        for (Iterator i=instances.iterator(); i.hasNext();) {
          Extended_Boolean_Criterion instance = (Extended_Boolean_Criterion)i.next();
      logger.debug("Extended_Boolean_Criterion.evaluate_expression: instance "+instance.getlabelValue());
          if (instance.getlabelValue().equals("true")) trueInstance = instance;
          else if (instance.getlabelValue().equals("false")) falseInstance = instance;
        }
      }
      if ((trueInstance == null) || (falseInstance == null))
        throw new PCA_Session_Exception("Null true or false instances!!");
      logger.debug("Extended_Boolean_Criterion.evaluate_expression: ");
      Criteria_Evaluation result = ownEvaluate(guidelineManager, false);
      logger.debug("Extended_Boolean_Criterion.evaluate_expression: result: "+result.support);
      if (result.truth_value.equals(Truth_Value._true)) return trueInstance;
      else return falseInstance;
    }

    public boolean equals(Extended_Boolean_Criterion criterion) {
      return (super.equals(criterion)  || (this.getlabelValue().equals(criterion.getlabelValue())));
    }

    public String toString() {
  	  return this.getlabelValue();
    }
    
}
