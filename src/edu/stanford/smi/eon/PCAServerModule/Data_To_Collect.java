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
<li> <b>Java Class</b> edu.stanford.smi.eon.PCAServerModule.Patient_Data
<li> <b>Source File</b> edu/stanford/smi/eon/PCAServerModule/Patient_Data.java
<li> <b>IDL Source File</b> PCAServer.idl
<li> <b>IDL Absolute Name</b> ::PCAServerModule::Patient_Data
<li> <b>Repository Identifier</b> IDL:PCAServerModule/Patient_Data:1.0
</ul>
<b>IDL definition:</b>
<pre>
    #pragma prefix "PCAServerModule"
    union Patient_Data switch(::PCAServerModule::Data_To_Collect_Type) {
      case ::PCAServerModule::data_entry:
        ::PCAServerModule::Data_Entry data_entry;
      case ::PCAServerModule::template:
        ::PCAServerModule::Data_Entry_Template template;
    };
</pre>
</p>
*/
final public class Data_To_Collect implements java.io.Serializable {
  private java.lang.Object _object;
  private edu.stanford.smi.eon.PCAServerModule.Data_To_Collect_Type _disc;
  public Data_To_Collect() {
  }
  public edu.stanford.smi.eon.PCAServerModule.Data_To_Collect_Type discriminator() {
    return _disc;
  }
  public edu.stanford.smi.eon.PCAServerModule.Data_Entry data_entry() {
    if(
      _disc.value() != (int) edu.stanford.smi.eon.PCAServerModule.Data_To_Collect_Type.data_entry.value() &&
      true
    ) {
      throw new org.omg.CORBA.BAD_OPERATION("data_entry");
    }
    return (edu.stanford.smi.eon.PCAServerModule.Data_Entry) _object;
  }
  public edu.stanford.smi.eon.PCAServerModule.Data_Entry_Template template() {
    if(
      _disc.value() != (int) edu.stanford.smi.eon.PCAServerModule.Data_To_Collect_Type.template.value() &&
      true
    ) {
      throw new org.omg.CORBA.BAD_OPERATION("template");
    }
    return (edu.stanford.smi.eon.PCAServerModule.Data_Entry_Template) _object;
  }
  public void data_entry(edu.stanford.smi.eon.PCAServerModule.Data_Entry value) {
    _disc = (edu.stanford.smi.eon.PCAServerModule.Data_To_Collect_Type) edu.stanford.smi.eon.PCAServerModule.Data_To_Collect_Type.data_entry;
    _object = value;
  }
  public void prescription(edu.stanford.smi.eon.PCAServerModule.Data_Entry_Template value) {
    _disc = (edu.stanford.smi.eon.PCAServerModule.Data_To_Collect_Type) edu.stanford.smi.eon.PCAServerModule.Data_To_Collect_Type.template;
    _object = value;
  }

}
