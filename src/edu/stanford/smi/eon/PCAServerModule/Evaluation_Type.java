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
<li> <b>Java Class</b> edu.stanford.smi.eon.PCAServerModule.Evaluation_Type
<li> <b>Source File</b> edu/stanford/smi/eon/PCAServerModule/Evaluation_Type.java
<li> <b>IDL Source File</b> PCAServer.idl
<li> <b>IDL Absolute Name</b> ::PCAServerModule::Evaluation_Type
<li> <b>Repository Identifier</b> IDL:PCAServerModule/Evaluation_Type:1.0
</ul>
<b>IDL definition:</b>
<pre>
    #pragma prefix "PCAServerModule"
    enum Evaluation_Type {
      add,
      delete,
      change_attribute,
      substitute
    };
</pre>
</p>
*/
public class Evaluation_Type implements java.io.Serializable {
  final public static int _add = 0;
  final public static int _delete = 1;
  final public static int _change_attribute = 2;
  final public static int _substitute = 3;
  final public static edu.stanford.smi.eon.PCAServerModule.Evaluation_Type add = new edu.stanford.smi.eon.PCAServerModule.Evaluation_Type(_add);
  final public static edu.stanford.smi.eon.PCAServerModule.Evaluation_Type delete = new edu.stanford.smi.eon.PCAServerModule.Evaluation_Type(_delete);
  final public static edu.stanford.smi.eon.PCAServerModule.Evaluation_Type change_attribute = new edu.stanford.smi.eon.PCAServerModule.Evaluation_Type(_change_attribute);
  final public static edu.stanford.smi.eon.PCAServerModule.Evaluation_Type substitute = new edu.stanford.smi.eon.PCAServerModule.Evaluation_Type(_substitute);
  private int __value;
  protected Evaluation_Type(int value) {
    this.__value = value;
  }
  public int value() {
    return __value;
  }
  public static edu.stanford.smi.eon.PCAServerModule.Evaluation_Type from_int(int $value) throws Exception {
    switch($value) {
    case _add:
      return add;
    case _delete:
      return delete;
    case _change_attribute:
      return change_attribute;
    case _substitute:
      return substitute;
    default:
      throw new Exception("Illegal action type: Enum out of range: [0.." + (4 - 1) + "]: " + $value);
    }
  }
  public java.lang.String toString() {
	    if (this.__value == _add) return "add";
	    else if (this.__value == _delete) return "delete"; 
	    else if (this.__value == _change_attribute) return "change attribute";
	    else return "substitute";
  }

}
