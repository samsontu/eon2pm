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
<li> <b>Java Class</b> edu.stanford.smi.eon.PCAServerModule.Entry_Type
<li> <b>Source File</b> edu/stanford/smi/eon/PCAServerModule/Entry_Type.java
<li> <b>IDL Source File</b> PCAServer.idl
<li> <b>IDL Absolute Name</b> ::PCAServerModule::Entry_Type
<li> <b>Repository Identifier</b> IDL:PCAServerModule/Entry_Type:1.0
</ul>
<b>IDL definition:</b>
<pre>
    #pragma prefix "PCAServerModule"
    enum Entry_Type {
      adverse_event,
      problem
    };
</pre>
</p>
*/
public class Entry_Type implements java.io.Serializable {
  final public static int _adverse_event = 0;
  final public static int _problem = 1;
  final public static edu.stanford.smi.eon.PCAServerModule.Entry_Type adverse_event = new edu.stanford.smi.eon.PCAServerModule.Entry_Type(_adverse_event);
  final public static edu.stanford.smi.eon.PCAServerModule.Entry_Type problem = new edu.stanford.smi.eon.PCAServerModule.Entry_Type(_problem);
  private int __value;
  protected Entry_Type(int value) {
    this.__value = value;
  }
  public int value() {
    return __value;
  }
  public static edu.stanford.smi.eon.PCAServerModule.Entry_Type from_int(int $value) throws Exception {
    switch($value) {
    case _adverse_event:
      return adverse_event;
    case _problem:
      return problem;
    default:
      throw new Exception("Bad parameter: Entry type enum out of range: [0.." + (2 - 1) + "]: " + $value);
    }
  }
  public java.lang.String toString() {
	    if (this.__value == _adverse_event) return "adverse event";
	    else return "problem";
}

}
