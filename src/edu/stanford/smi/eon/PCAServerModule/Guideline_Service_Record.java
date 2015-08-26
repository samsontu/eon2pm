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
/**
<p>
<ul>
<li> <b>Java Class</b> edu.stanford.smi.eon.PCAServerModule.Guideline_Service_Record
<li> <b>Source File</b> edu/stanford/smi/eon/PCAServerModule/Guideline_Service_Record.java
<li> <b>IDL Source File</b> PCAServer.idl
<li> <b>IDL Absolute Name</b> ::PCAServerModule::Guideline_Service_Record
<li> <b>Repository Identifier</b> IDL:PCAServerModule/Guideline_Service_Record:1.0
</ul>
<b>IDL definition:</b>
<pre>
    #pragma prefix "PCAServerModule"
    struct Guideline_Service_Record {
      ::PCAServerModule::Criteria_Evaluation assumption;
      ::PCAServerModule::Guideline_Action_ChoicesSeq decision_points;
      ::PCAServerModule::Guideline_Activity_ChoicesSeq activity_choices;
      ::PCAServerModule::Guideline_Activity_EvaluationsSeq evaluated_choices;
      ::PCAServerModule::Guideline_Scenario_ChoicesSeq scenario_choices;
      ::PCAServerModule::Guideline_GoalSeq goals;
      ::PCAServerModule::ConclusionSeq subject_classification;
      ::PCAServerModule::Current_Activity_AssessmentSeq assessments;
      Data_to_CollectSeq questions;
    };
</pre>
</p>
*/
final public class Guideline_Service_Record implements java.io.Serializable {
  public edu.stanford.smi.eon.PCAServerModule.Criteria_Evaluation assumption;
  public edu.stanford.smi.eon.PCAServerModule.Guideline_Action_Choices[] decision_points;
  public edu.stanford.smi.eon.PCAServerModule.Guideline_Activity_Choices[] activity_choices;
  public edu.stanford.smi.eon.PCAServerModule.Guideline_Activity_Evaluations[] evaluated_choices;
  public edu.stanford.smi.eon.PCAServerModule.Guideline_Scenario_Choices[] scenario_choices;
  public edu.stanford.smi.eon.PCAServerModule.Guideline_Goal[] goals;
  public edu.stanford.smi.eon.PCAServerModule.Conclusion[] subject_classification;
  public edu.stanford.smi.eon.PCAServerModule.Current_Activity_Assessment[] assessments;
  public edu.stanford.smi.eon.PCAServerModule.Data_To_Collect[] questions;
  public String guidelineName;


  public Guideline_Service_Record() {
  }
  public Guideline_Service_Record(
    edu.stanford.smi.eon.PCAServerModule.Criteria_Evaluation assumption,
    edu.stanford.smi.eon.PCAServerModule.Guideline_Action_Choices[] decision_points,
    edu.stanford.smi.eon.PCAServerModule.Guideline_Activity_Choices[] activity_choices,
    edu.stanford.smi.eon.PCAServerModule.Guideline_Activity_Evaluations[] evaluated_choices,
    edu.stanford.smi.eon.PCAServerModule.Guideline_Scenario_Choices[] scenario_choices,
    edu.stanford.smi.eon.PCAServerModule.Guideline_Goal[] goals,
    edu.stanford.smi.eon.PCAServerModule.Conclusion[] subject_classification,
    edu.stanford.smi.eon.PCAServerModule.Current_Activity_Assessment[] assessments,
    edu.stanford.smi.eon.PCAServerModule.Data_To_Collect[] questions,
    String guidelineName
    
  ) {
    this.assumption = assumption;
    this.decision_points = decision_points;
    this.activity_choices = activity_choices;
    this.evaluated_choices = evaluated_choices;
    this.scenario_choices = scenario_choices;
    this.goals = goals;
    this.subject_classification = subject_classification;
    this.assessments = assessments;
    this.questions = questions;
    this.guidelineName = guidelineName;
    
  }
/*  public java.lang.String toString() {
    org.omg.CORBA.Any any = org.omg.CORBA.ORB.init().create_any();
    edu.stanford.smi.eon.PCAServerModule.Guideline_Service_RecordHelper.insert(any, this);
    return any.toString();
  } */
}
