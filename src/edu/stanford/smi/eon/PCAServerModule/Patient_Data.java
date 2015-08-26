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
    union Patient_Data switch(::PCAServerModule::Patient_Data_Type) {
      case ::PCAServerModule::numeric_data:
        ::PCAServerModule::Numeric_Data numeric;
      case ::PCAServerModule::prescription_data:
        ::PCAServerModule::Prescription_Data prescription;
      case ::PCAServerModule::note_entry_data:
        ::PCAServerModule::Note_Entry_Data note_data;
    };
</pre>
</p>
*/
final public class Patient_Data implements java.io.Serializable {
  private java.lang.Object _object;
  private edu.stanford.smi.eon.PCAServerModule.Patient_Data_Type _disc;
  public Patient_Data() {
  }
  public edu.stanford.smi.eon.PCAServerModule.Patient_Data_Type discriminator() {
    return _disc;
  }
  public edu.stanford.smi.eon.PCAServerModule.Numeric_Data numeric() {
    if(
      _disc.value() != (int) edu.stanford.smi.eon.PCAServerModule.Patient_Data_Type.numeric_data.value() &&
      true
    ) {
      throw new org.omg.CORBA.BAD_OPERATION("numeric");
    }
    return (edu.stanford.smi.eon.PCAServerModule.Numeric_Data) _object;
  }
  public edu.stanford.smi.eon.PCAServerModule.Prescription_Data prescription() {
    if(
      _disc.value() != (int) edu.stanford.smi.eon.PCAServerModule.Patient_Data_Type.prescription_data.value() &&
      true
    ) {
      throw new org.omg.CORBA.BAD_OPERATION("prescription");
    }
    return (edu.stanford.smi.eon.PCAServerModule.Prescription_Data) _object;
  }
  public edu.stanford.smi.eon.PCAServerModule.Note_Entry_Data note_data() {
    if(
      _disc.value() != (int) edu.stanford.smi.eon.PCAServerModule.Patient_Data_Type.note_entry_data.value() &&
      true
    ) {
      throw new org.omg.CORBA.BAD_OPERATION("note_data");
    }
    return (edu.stanford.smi.eon.PCAServerModule.Note_Entry_Data) _object;
  }
  public void numeric(edu.stanford.smi.eon.PCAServerModule.Numeric_Data value) {
    _disc = (edu.stanford.smi.eon.PCAServerModule.Patient_Data_Type) edu.stanford.smi.eon.PCAServerModule.Patient_Data_Type.numeric_data;
    _object = value;
  }
  public void prescription(edu.stanford.smi.eon.PCAServerModule.Prescription_Data value) {
    _disc = (edu.stanford.smi.eon.PCAServerModule.Patient_Data_Type) edu.stanford.smi.eon.PCAServerModule.Patient_Data_Type.prescription_data;
    _object = value;
  }
  public void note_data(edu.stanford.smi.eon.PCAServerModule.Note_Entry_Data value) {
    _disc = (edu.stanford.smi.eon.PCAServerModule.Patient_Data_Type) edu.stanford.smi.eon.PCAServerModule.Patient_Data_Type.note_entry_data;
    _object = value;
  }

}
