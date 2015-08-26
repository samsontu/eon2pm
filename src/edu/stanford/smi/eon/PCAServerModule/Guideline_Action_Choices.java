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
<li> <b>Java Class</b> edu.stanford.smi.eon.PCAServerModule.Guideline_Action_Choices
<li> <b>Source File</b> edu/stanford/smi/eon/PCAServerModule/Guideline_Action_Choices.java
<li> <b>IDL Source File</b> PCAServer.idl
<li> <b>IDL Absolute Name</b> ::PCAServerModule::Guideline_Action_Choices
<li> <b>Repository Identifier</b> IDL:PCAServerModule/Guideline_Action_Choices:1.0
</ul>
<b>IDL definition:</b>
<pre>
    #pragma prefix "PCAServerModule"
    struct Guideline_Action_Choices {
      string guideline_id;
      ::PCAServerModule::SelectionAlternatives selection_method;
      ::PCAServerModule::Action_To_ChooseSeq action_choices;
      ::PCAServerModule::Guideline_Entity current_location;
      boolean preempt_other_choices;
    };
</pre>
</p>
*/
final public class Guideline_Action_Choices implements java.io.Serializable {
  public java.lang.String guideline_id;
  public edu.stanford.smi.eon.PCAServerModule.SelectionAlternatives selection_method;
  public edu.stanford.smi.eon.PCAServerModule.Action_To_Choose[] action_choices;
  public edu.stanford.smi.eon.PCAServerModule.Guideline_Entity current_location;
  public boolean preempt_other_choices;
  public Guideline_Action_Choices() {
  }
  public Guideline_Action_Choices(
    java.lang.String guideline_id,
    edu.stanford.smi.eon.PCAServerModule.SelectionAlternatives selection_method,
    edu.stanford.smi.eon.PCAServerModule.Action_To_Choose[] action_choices,
    edu.stanford.smi.eon.PCAServerModule.Guideline_Entity current_location,
    boolean preempt_other_choices
  ) {
    this.guideline_id = guideline_id;
    this.selection_method = selection_method;
    this.action_choices = action_choices;
    this.current_location = current_location;
    this.preempt_other_choices = preempt_other_choices;
  }

}
