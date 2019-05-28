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
<li> <b>Java Class</b> edu.stanford.smi.eon.PCAServerModule.Truth_Value
<li> <b>Source File</b> edu/stanford/smi/eon/PCAServerModule/Truth_Value.java
<li> <b>IDL Source File</b> PCAServer.idl
<li> <b>IDL Absolute Name</b> ::PCAServerModule::Truth_Value
<li> <b>Repository Identifier</b> IDL:PCAServerModule/Truth_Value:1.0
</ul>
<b>IDL definition:</b>
<pre>
    #pragma prefix "PCAServerModule"
    enum Truth_Value {
      true,
      false,
      unknown
    };
</pre>
</p>
*/
public class Truth_Value implements java.io.Serializable {
  final public static int __true = 0;
  final public static int __false = 1;
  final public static int _unknown = 2;
  final public static edu.stanford.smi.eon.PCAServerModule.Truth_Value _true = new edu.stanford.smi.eon.PCAServerModule.Truth_Value(__true);
  final public static edu.stanford.smi.eon.PCAServerModule.Truth_Value _false = new edu.stanford.smi.eon.PCAServerModule.Truth_Value(__false);
  final public static edu.stanford.smi.eon.PCAServerModule.Truth_Value unknown = new edu.stanford.smi.eon.PCAServerModule.Truth_Value(_unknown);
  private int __value;
  protected Truth_Value(int value) {
    this.__value = value;
  }
  public int value() {
    return __value;
  }
  public static edu.stanford.smi.eon.PCAServerModule.Truth_Value from_int(int $value) throws Exception {
    switch($value) {
    case __true:
      return _true;
    case __false:
      return _false;
    case _unknown:
      return unknown;
    default:
		throw new Exception("Enum out of range: [0.." + (3 - 1) + "]: " + $value);
    }
  }
  public java.lang.String toString() {
	    if (this.__value == __true) return "true";
	    else if (this.__value == __false) return "false"; 
	    else return "unknown";
}

}
