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
<li> <b>Java Class</b> edu.stanford.smi.eon.PCAServerModule.User_Entry_Record
<li> <b>Source File</b> edu/stanford/smi/eon/PCAServerModule/User_Entry_Record.java
<li> <b>IDL Source File</b> PCAServer.idl
<li> <b>IDL Absolute Name</b> ::PCAServerModule::User_Entry_Record
<li> <b>Repository Identifier</b> IDL:PCAServerModule/User_Entry_Record:1.0
</ul>
<b>IDL definition:</b>
<pre>
    #pragma prefix "PCAServerModule"
    struct User_Entry_Record {
      ::PCAServerModule::Selected_ScenarioSeq selected_scenarios;
      ::PCAServerModule::Selected_ActivitySeq selected_activities;
      ::PCAServerModule::Selected_ActionSeq selected_actions;
    };
</pre>
</p>
*/
final public class User_Entry_Record implements java.io.Serializable {
  public edu.stanford.smi.eon.PCAServerModule.Selected_Scenario[] selected_scenarios;
  public edu.stanford.smi.eon.PCAServerModule.Selected_Activity[] selected_activities;
  public edu.stanford.smi.eon.PCAServerModule.Selected_Action[] selected_actions;
  public User_Entry_Record() {
  }
  public User_Entry_Record(
    edu.stanford.smi.eon.PCAServerModule.Selected_Scenario[] selected_scenarios,
    edu.stanford.smi.eon.PCAServerModule.Selected_Activity[] selected_activities,
    edu.stanford.smi.eon.PCAServerModule.Selected_Action[] selected_actions
  ) {
    this.selected_scenarios = selected_scenarios;
    this.selected_activities = selected_activities;
    this.selected_actions = selected_actions;
  }

}
