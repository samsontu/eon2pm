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
<li> <b>Java Class</b> edu.stanford.smi.eon.PCAServerModule.Data_Entry_Template
<li> <b>Source File</b> edu/stanford/smi/eon/PCAServerModule/Data_Entry_Template.java
<li> <b>IDL Source File</b> PCAServer.idl
<li> <b>IDL Absolute Name</b> ::PCAServerModule::Data_Entry_Template
<li> <b>Repository Identifier</b> IDL:PCAServerModule/Data_Entry_Template:1.0
</ul>
<b>IDL definition:</b>
<pre>
    #pragma prefix "PCAServerModule"
    struct Data_Entry_Template {
    	Guideline_Entity data_entry_template;
    	sequence<Data_Entry> data_to_collect;
    	Preference preference;
    	string label;
    	Justification preference_justication;
    };
</pre>
</p>
*/
final public class Data_Entry_Template implements java.io.Serializable {
  public edu.stanford.smi.eon.PCAServerModule.Justification preference_justication;
  public edu.stanford.smi.eon.PCAServerModule.Preference preference;
  public Data_Entry[] data_to_collect;
  public edu.stanford.smi.eon.PCAServerModule.Guideline_Entity data_entry_template;
  public java.lang.String label;
  public Data_Entry_Template() {
  }
  public Data_Entry_Template(
	java.lang.String label,
	Data_Entry[] data_to_collect,
    edu.stanford.smi.eon.PCAServerModule.Preference preference,
    edu.stanford.smi.eon.PCAServerModule.Justification preference_justication,
    edu.stanford.smi.eon.PCAServerModule.Guideline_Entity data_entry_template
  ) {
    this.preference_justication = preference_justication;
    this.preference = preference;
    this.data_to_collect = data_to_collect;
    this.data_entry_template = data_entry_template;
    this.label = label;
  }
}
