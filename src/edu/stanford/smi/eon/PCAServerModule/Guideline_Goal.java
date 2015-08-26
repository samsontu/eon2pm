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
import edu.stanford.smi.eon.guidelineinterpreter.GuidelineInterpreter;

/**
<p>
<ul>
<li> <b>Java Class</b> edu.stanford.smi.eon.PCAServerModule.Guideline_Goal
<li> <b>Source File</b> edu/stanford/smi/eon/PCAServerModule/Guideline_Goal.java
<li> <b>IDL Source File</b> PCAServer.idl
<li> <b>IDL Absolute Name</b> ::PCAServerModule::Guideline_Goal
<li> <b>Repository Identifier</b> IDL:PCAServerModule/Guideline_Goal:1.0
</ul>
<b>IDL definition:</b>
<pre>
    #pragma prefix "PCAServerModule"
    struct Guideline_Goal {
      ::PCAServerModule::Guideline_Entity guideline_entity;
      ::PCAServerModule::Justification support_for_goal;
      ::PCAServerModule::Criteria_Evaluation goal;
      ::PCAServerModule::Goal_State achieved;
    };
</pre>
</p>
*/
final public class Guideline_Goal implements java.io.Serializable {
  public edu.stanford.smi.eon.PCAServerModule.Guideline_Entity guideline_entity;
  public edu.stanford.smi.eon.PCAServerModule.Justification support_for_goal;
  public edu.stanford.smi.eon.PCAServerModule.Criteria_Evaluation goal;
  public edu.stanford.smi.eon.PCAServerModule.Goal_State achieved;
  public String data;
  public Collection<Action_Spec_Record> actions;
  public int fine_grain_priority;
  public Collection<String> references;
  public boolean primary;
  public String kb_goal_id;  //name of performance measure
  public Collection missing_data;
  public Collection<Criteria_Evaluation> criteria_evaluation;
  
  public Guideline_Goal() {
  }
  public Guideline_Goal(
    edu.stanford.smi.eon.PCAServerModule.Guideline_Entity guideline_entity,
    edu.stanford.smi.eon.PCAServerModule.Justification support_for_goal,
    edu.stanford.smi.eon.PCAServerModule.Criteria_Evaluation goal,
    edu.stanford.smi.eon.PCAServerModule.Goal_State achieved,
    String data,
    Collection<Action_Spec_Record> actions,
    int fine_grain_priority,
    Collection<String> references,
    boolean primary,
    String kb_goal_id,
    Collection<Criteria_Evaluation> criteria_evaluation,
    Collection missing_data
  ) {
    this.guideline_entity = guideline_entity;
    this.support_for_goal = support_for_goal;
    this.goal = goal;
    this.achieved = achieved;
    this.data = data;
    this.actions = actions;
    this.fine_grain_priority = fine_grain_priority;
    this.references = references;
    this.primary = primary;
    this.kb_goal_id = kb_goal_id;
    this.criteria_evaluation = criteria_evaluation;
    this.missing_data = missing_data;
  }

}
