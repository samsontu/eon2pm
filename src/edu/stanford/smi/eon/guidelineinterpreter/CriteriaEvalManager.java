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
package edu.stanford.smi.eon.guidelineinterpreter;

import java.util.*;
import java.io.Serializable;
import edu.stanford.smi.eon.criterion.Expression;
import edu.stanford.smi.eon.PCAServerModule.Criteria_Evaluation;
import edu.stanford.smi.eon.PCAServerModule.PCA_Session_Exception;


public class CriteriaEvalManager  implements Serializable {
  private Collection assumptions;
  private Map criteriaEvaluations;
  private Map dependencies;

  public CriteriaEvalManager() {
    assumptions = new ArrayList();
    criteriaEvaluations = new HashMap();
    dependencies = new HashMap();
  }
  

  public void tell(Expression expression, Object eval)
    throws  PCA_Session_Exception {
	  if (eval != null) criteriaEvaluations.put(expression, eval);
	  else throw new PCA_Session_Exception("CriteriaEvalManager.tell: "+
			  expression.getBrowserText()+" evaluate to nil");
  }

  public  Object ask(Expression expression) {
    return criteriaEvaluations.get(expression);
  }

  public void assume (Expression criterion, Criteria_Evaluation eval)   {
    //throws  PCA_Session_Exception {
    //Object oldeval = criteriaEvaluations.get(criterion);
    //if (oldeval != null) {
    //  if (!eval.truth_value.equals(((Criteria_Evaluation)oldeval).truth_value)) {
    //    throw new PCA_Session_Exception(eval.criterion.name + " contradition!");
    //  }
    //} else {
      criteriaEvaluations.put(criterion, eval);
      assumptions.add(criterion);
    //}
  }

  public void retract (Expression criterion) {
    assumptions.remove(criterion);
    criteriaEvaluations.remove(criterion);
  }

  public Collection getAssumptions() {
    // return a collection of Criteria_Evaluations
    Collection evals = new ArrayList();
    for (Iterator i=assumptions.iterator(); i.hasNext();) {
      evals.add(criteriaEvaluations.get((Expression)i.next()));
    }
    return evals;
  }

} 