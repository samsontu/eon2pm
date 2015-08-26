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
<li> <b>Java Class</b> edu.stanford.smi.eon.PCAServerModule.Selected_Action
<li> <b>Source File</b> edu/stanford/smi/eon/PCAServerModule/Selected_Action.java
<li> <b>IDL Source File</b> PCAServer.idl
<li> <b>IDL Absolute Name</b> ::PCAServerModule::Selected_Action
<li> <b>Repository Identifier</b> IDL:PCAServerModule/Selected_Action:1.0
</ul>
<b>IDL definition:</b>
<pre>
    #pragma prefix "PCAServerModule"
    struct Selected_Action {
      string name;
      ::PCAServerModule::Action_To_Choose selected_item;
      ::PCAServerModule::Guideline_Entity action_specification;
      ::PCAServerModule::Guideline_Action_Choices guideline_action_choice;
    };
</pre>
</p>
*/
final public class Selected_Action implements java.io.Serializable {
  public java.lang.String name;
  public edu.stanford.smi.eon.PCAServerModule.Action_To_Choose selected_item;
  public edu.stanford.smi.eon.PCAServerModule.Guideline_Entity action_specification;
  public edu.stanford.smi.eon.PCAServerModule.Guideline_Action_Choices guideline_action_choice;
  public Selected_Action() {
  }
  public Selected_Action(
    java.lang.String name,
    edu.stanford.smi.eon.PCAServerModule.Action_To_Choose selected_item,
    edu.stanford.smi.eon.PCAServerModule.Guideline_Entity action_specification,
    edu.stanford.smi.eon.PCAServerModule.Guideline_Action_Choices guideline_action_choice
  ) {
    this.name = name;
    this.selected_item = selected_item;
    this.action_specification = action_specification;
    this.guideline_action_choice = guideline_action_choice;
  }

}
