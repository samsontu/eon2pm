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
<li> <b>Java Class</b> edu.stanford.smi.eon.PCAServerModule.Patient_Data_Type
<li> <b>Source File</b> edu/stanford/smi/eon/PCAServerModule/Patient_Data_Type.java
<li> <b>IDL Source File</b> PCAServer.idl
<li> <b>IDL Absolute Name</b> ::PCAServerModule::Patient_Data_Type
<li> <b>Repository Identifier</b> IDL:PCAServerModule/Patient_Data_Type:1.0
</ul>
<b>IDL definition:</b>
<pre>
    #pragma prefix "PCAServerModule"
    enum Patient_Data_Type {
      numeric_data,
      prescription_data,
      note_entry_data
    };
</pre>
</p>
*/
public class Patient_Data_Type implements java.io.Serializable {
  final public static int _numeric_data = 0;
  final public static int _prescription_data = 1;
  final public static int _note_entry_data = 2;
  final public static int _demographic_data = 3;
  final public static int _adverse_reaction = 4;
  final public static int _encounter = 5;
  final public static edu.stanford.smi.eon.PCAServerModule.Patient_Data_Type numeric_data = new edu.stanford.smi.eon.PCAServerModule.Patient_Data_Type(_numeric_data);
  final public static edu.stanford.smi.eon.PCAServerModule.Patient_Data_Type prescription_data = new edu.stanford.smi.eon.PCAServerModule.Patient_Data_Type(_prescription_data);
  final public static edu.stanford.smi.eon.PCAServerModule.Patient_Data_Type note_entry_data = new edu.stanford.smi.eon.PCAServerModule.Patient_Data_Type(_note_entry_data);
  final public static edu.stanford.smi.eon.PCAServerModule.Patient_Data_Type demographic_data = new edu.stanford.smi.eon.PCAServerModule.Patient_Data_Type(_demographic_data);
  final public static edu.stanford.smi.eon.PCAServerModule.Patient_Data_Type adverse_reaction = new edu.stanford.smi.eon.PCAServerModule.Patient_Data_Type(_adverse_reaction);
  final public static edu.stanford.smi.eon.PCAServerModule.Patient_Data_Type encounter = new edu.stanford.smi.eon.PCAServerModule.Patient_Data_Type(_encounter);
  private int __value;
  protected Patient_Data_Type(int value) {
    this.__value = value;
  }
  public int value() {
    return __value;
  }
  public static edu.stanford.smi.eon.PCAServerModule.Patient_Data_Type from_int(int $value) throws Exception {
    switch($value) {
    case _numeric_data:
      return numeric_data;
    case _prescription_data:
      return prescription_data;
    case _note_entry_data:
      return note_entry_data;
    case _demographic_data:
        return demographic_data;
      case _adverse_reaction:
        return adverse_reaction;
      case _encounter:
        return encounter;
    default:
      throw new Exception("Illegal datatype; Enum out of range: [0.." + (3 - 1) + "]: " + $value);
    }
  }

}
