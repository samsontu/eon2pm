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
<li> <b>Java Class</b> edu.stanford.smi.eon.PCAServerModule.Activity_Choice
<li> <b>Source File</b> edu/stanford/smi/eon/PCAServerModule/Activity_Choice.java
<li> <b>IDL Source File</b> PCAServer.idl
<li> <b>IDL Absolute Name</b> ::PCAServerModule::Activity_Choice
<li> <b>Repository Identifier</b> IDL:PCAServerModule/Activity_Choice:1.0
</ul>
<b>IDL definition:</b>
<pre>
    #pragma prefix "PCAServerModule"
    struct Activity_Choice {
      string name;
      ::PCAServerModule::Preference preference;
      ::PCAServerModule::Justification preference_justification;
      ::PCAServerModule::Guideline_Entity activity;
      ::PCAServerModule::Guideline_Entity activity_class;
      string description;
    };
</pre>
</p>
*/
final public class Activity_Choice implements java.io.Serializable {
  public java.lang.String name;
  public edu.stanford.smi.eon.PCAServerModule.Preference preference;
  public edu.stanford.smi.eon.PCAServerModule.Justification preference_justification;
  public edu.stanford.smi.eon.PCAServerModule.Guideline_Entity activity;
  public edu.stanford.smi.eon.PCAServerModule.Guideline_Entity activity_class;
  public java.lang.String description;
  public Activity_Choice() {
  }
  public Activity_Choice(
    java.lang.String name,
    edu.stanford.smi.eon.PCAServerModule.Preference preference,
    edu.stanford.smi.eon.PCAServerModule.Justification preference_justification,
    edu.stanford.smi.eon.PCAServerModule.Guideline_Entity activity,
    edu.stanford.smi.eon.PCAServerModule.Guideline_Entity activity_class,
    java.lang.String description
  ) {
    this.name = name;
    this.preference = preference;
    this.preference_justification = preference_justification;
    this.activity = activity;
    this.activity_class = activity_class;
    this.description = description;
  }
}
