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
public class Data_To_Collect_Type implements java.io.Serializable {
  final public static int _data_entry = 0;
  final public static int _template = 1;
  final public static Data_To_Collect_Type data_entry = new Data_To_Collect_Type(_data_entry);
  final public static Data_To_Collect_Type template = new Data_To_Collect_Type(_template);
  private int __value;
  protected Data_To_Collect_Type(int value) {
    this.__value = value;
  }
  public int value() {
    return __value;
  }
  public static Data_To_Collect_Type from_int(int $value) {
    switch($value) {
    case _data_entry:
      return data_entry;
    case _template:
      return template;
    default:
      throw new org.omg.CORBA.BAD_PARAM("Enum out of range: [0.." + (3 - 1) + "]: " + $value);
    }
  }

}
