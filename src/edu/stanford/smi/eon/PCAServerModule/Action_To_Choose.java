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
<li> <b>Java Class</b> edu.stanford.smi.eon.PCAServerModule.Action_To_Choose
<li> <b>Source File</b> edu/stanford/smi/eon/PCAServerModule/Action_To_Choose.java
<li> <b>IDL Source File</b> PCAServer.idl
<li> <b>IDL Absolute Name</b> ::PCAServerModule::Action_To_Choose
<li> <b>Repository Identifier</b> IDL:PCAServerModule/Action_To_Choose:1.0
</ul>
<b>IDL definition:</b>
<pre>
    #pragma prefix "PCAServerModule"
    struct Action_To_Choose {
      string name;
      ::PCAServerModule::Preference preference;
      ::PCAServerModule::Justification preference_justification;
      ::PCAServerModule::Action_Spec_RecordSeq action_specifications;
      ::PCAServerModule::Guideline_Entity action_step;
      string description;
    };
</pre>
</p>
*/
final public class Action_To_Choose implements java.io.Serializable {
  public java.lang.String name;
  public edu.stanford.smi.eon.PCAServerModule.Preference preference;
  public edu.stanford.smi.eon.PCAServerModule.Justification preference_justification;
  public edu.stanford.smi.eon.PCAServerModule.Action_Spec_Record[] action_specifications;
  public edu.stanford.smi.eon.PCAServerModule.Guideline_Entity action_step;
  public java.lang.String description;
  public Action_To_Choose() {
  }
  public Action_To_Choose(
    java.lang.String name,
    edu.stanford.smi.eon.PCAServerModule.Preference preference,
    edu.stanford.smi.eon.PCAServerModule.Justification preference_justification,
    edu.stanford.smi.eon.PCAServerModule.Action_Spec_Record[] action_specifications,
    edu.stanford.smi.eon.PCAServerModule.Guideline_Entity action_step,
    java.lang.String description
  ) {
    this.name = name;
    this.preference = preference;
    this.preference_justification = preference_justification;
    this.action_specifications = action_specifications;
    this.action_step = action_step;
    this.description = description;
  }
  
}
