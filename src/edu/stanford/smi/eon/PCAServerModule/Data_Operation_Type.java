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
<li> <b>Java Class</b> edu.stanford.smi.eon.PCAServerModule.Data_Operation_Type
<li> <b>Source File</b> edu/stanford/smi/eon/PCAServerModule/Data_Operation_Type.java
<li> <b>IDL Source File</b> PCAServer.idl
<li> <b>IDL Absolute Name</b> ::PCAServerModule::Data_Operation_Type
<li> <b>Repository Identifier</b> IDL:PCAServerModule/Data_Operation_Type:1.0
</ul>
<b>IDL definition:</b>
<pre>
    #pragma prefix "PCAServerModule"
    enum Data_Operation_Type {
      add,
      modify,
      delete
    };
</pre>
</p>
*/
public class Data_Operation_Type implements java.io.Serializable {
  final public static int _add = 0;
  final public static int _modify = 1;
  final public static int _delete = 2;
  final public static edu.stanford.smi.eon.PCAServerModule.Data_Operation_Type add = new edu.stanford.smi.eon.PCAServerModule.Data_Operation_Type(_add);
  final public static edu.stanford.smi.eon.PCAServerModule.Data_Operation_Type modify = new edu.stanford.smi.eon.PCAServerModule.Data_Operation_Type(_modify);
  final public static edu.stanford.smi.eon.PCAServerModule.Data_Operation_Type delete = new edu.stanford.smi.eon.PCAServerModule.Data_Operation_Type(_delete);
  private int __value;
  protected Data_Operation_Type(int value) {
    this.__value = value;
  }
  public int value() {
    return __value;
  }
  public static edu.stanford.smi.eon.PCAServerModule.Data_Operation_Type from_int(int $value) throws Exception {
    switch($value) {
    case _add:
      return add;
    case _modify:
      return modify;
    case _delete:
      return delete;
    default:
      throw new Exception("Enum out of range: [0.." + (3 - 1) + "]: " + $value);
    }
  }
}
