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
package edu.stanford.smi.eon.PCAServerModule;

import java.util.Collection;

/**
<p>
<ul>
<li> <b>Java Class</b> edu.stanford.smi.eon.PCAServerModule.Criteria_Evaluation
<li> <b>Source File</b> edu/stanford/smi/eon/PCAServerModule/Criteria_Evaluation.java
<li> <b>IDL Source File</b> PCAServer.idl
<li> <b>IDL Absolute Name</b> ::PCAServerModule::Criteria_Evaluation
<li> <b>Repository Identifier</b> IDL:PCAServerModule/Criteria_Evaluation:1.0
</ul>
<b>IDL definition:</b>
<pre>
    #pragma prefix "PCAServerModule"
    struct Criteria_Evaluation {
      ::PCAServerModule::Logical_Operator operator;
      ::PCAServerModule::Truth_Value truth_value;
      sequence&lt::PCAServerModule::Criteria_Evaluation&gt children;
      ::PCAServerModule::Guideline_Entity criterion;
      string support;
    };
</pre>
</p>
*/
final public class Criteria_Evaluation implements java.io.Serializable {
  public edu.stanford.smi.eon.PCAServerModule.Logical_Operator _operator;
  public edu.stanford.smi.eon.PCAServerModule.Truth_Value truth_value;
  public edu.stanford.smi.eon.PCAServerModule.Criteria_Evaluation[] children;
  public edu.stanford.smi.eon.PCAServerModule.Guideline_Entity criterion;
  public java.lang.String support; 
  public String criterion_type = null; // type of criteria in a performance measure
  									   // inclusion, exclusion, subpopulation_selection, numerator
  public String context = null; //performance measure of which the criterion is a part
  public String parameter = null; //The parameter whose value is being evaluated in comparison criteria
  public Collection<Criteria_Evaluation> missingDataCriteriaEval = null;
  
  public Criteria_Evaluation() {
  }
  public Criteria_Evaluation(
    edu.stanford.smi.eon.PCAServerModule.Logical_Operator _operator,
    edu.stanford.smi.eon.PCAServerModule.Truth_Value truth_value,
    edu.stanford.smi.eon.PCAServerModule.Criteria_Evaluation[] children,
    edu.stanford.smi.eon.PCAServerModule.Guideline_Entity criterion,
    java.lang.String support
  ) {
    this._operator = _operator;
    this.truth_value = truth_value;
    this.children = children;
    this.criterion = criterion;
    this.support = support;
  }
  
  public void setCriterionType(String criterion_type) {
	  this.criterion_type = criterion_type;
  }
  
  public void setContext(String context){
	  this.context = context;
  }
  public void setParameter(String parameter){
	  this.parameter = parameter;
  }
  
  public void setMissingDataEvaluations(Collection<Criteria_Evaluation> missingDataCriteriaEval){
	  this.missingDataCriteriaEval = missingDataCriteriaEval;
  }

}
