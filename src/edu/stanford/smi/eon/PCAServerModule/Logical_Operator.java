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
<li> <b>Java Class</b> edu.stanford.smi.eon.PCAServerModule.Logical_Operator
<li> <b>Source File</b> edu/stanford/smi/eon/PCAServerModule/Logical_Operator.java
<li> <b>IDL Source File</b> PCAServer.idl
<li> <b>IDL Absolute Name</b> ::PCAServerModule::Logical_Operator
<li> <b>Repository Identifier</b> IDL:PCAServerModule/Logical_Operator:1.0
</ul>
<b>IDL definition:</b>
<pre>
    #pragma prefix "PCAServerModule"
    enum Logical_Operator {
      AND,
      OR,
      ATOMIC,
      NOT
    };
</pre>
</p>
*/
public class Logical_Operator implements java.io.Serializable {
  final public static int _AND = 0;
  final public static int _OR = 1;
  final public static int _ATOMIC = 2;
  final public static int _NOT = 3;
  final public static edu.stanford.smi.eon.PCAServerModule.Logical_Operator AND = new edu.stanford.smi.eon.PCAServerModule.Logical_Operator(_AND);
  final public static edu.stanford.smi.eon.PCAServerModule.Logical_Operator OR = new edu.stanford.smi.eon.PCAServerModule.Logical_Operator(_OR);
  final public static edu.stanford.smi.eon.PCAServerModule.Logical_Operator ATOMIC = new edu.stanford.smi.eon.PCAServerModule.Logical_Operator(_ATOMIC);
  final public static edu.stanford.smi.eon.PCAServerModule.Logical_Operator NOT = new edu.stanford.smi.eon.PCAServerModule.Logical_Operator(_NOT);
  private int __value;
  protected Logical_Operator(int value) {
    this.__value = value;
  }
  public int value() {
    return __value;
  }
  public static edu.stanford.smi.eon.PCAServerModule.Logical_Operator from_int(int $value) throws Exception {
    switch($value) {
    case _AND:
      return AND;
    case _OR:
      return OR;
    case _ATOMIC:
      return ATOMIC;
    case _NOT:
      return NOT;
    default:
      throw new Exception ("Illegal logical operator; Enum out of range: [0.." + (4 - 1) + "]: " + $value);
    }
  }

}
