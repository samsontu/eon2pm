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
<li> <b>Java Class</b> edu.stanford.smi.eon.PCAServerModule.Goal_State
<li> <b>Source File</b> edu/stanford/smi/eon/PCAServerModule/Goal_State.java
<li> <b>IDL Source File</b> PCAServer.idl
<li> <b>IDL Absolute Name</b> ::PCAServerModule::Goal_State
<li> <b>Repository Identifier</b> IDL:PCAServerModule/Goal_State:1.0
</ul>
<b>IDL definition:</b>
<pre>
    #pragma prefix "PCAServerModule"
    enum Goal_State {
      achieved,
      failed,
      none,
      unknown
    };
</pre>
</p>
*/
public class Goal_State implements java.io.Serializable {
  final public static int _achieved = 0;
  final public static int _failed = 1;
  final public static int _none = 2;
  final public static int _unknown = 3;
  final public static edu.stanford.smi.eon.PCAServerModule.Goal_State achieved = new edu.stanford.smi.eon.PCAServerModule.Goal_State(_achieved);
  final public static edu.stanford.smi.eon.PCAServerModule.Goal_State failed = new edu.stanford.smi.eon.PCAServerModule.Goal_State(_failed);
  final public static edu.stanford.smi.eon.PCAServerModule.Goal_State none = new edu.stanford.smi.eon.PCAServerModule.Goal_State(_none);
  final public static edu.stanford.smi.eon.PCAServerModule.Goal_State unknown = new edu.stanford.smi.eon.PCAServerModule.Goal_State(_unknown);
  private int __value;
  protected Goal_State(int value) {
    this.__value = value;
  }
  public int value() {
    return __value;
  }
  public static edu.stanford.smi.eon.PCAServerModule.Goal_State from_int(int $value) {
    switch($value) {
    case _achieved:
      return achieved;
    case _failed:
      return failed;
    case _none:
      return none;
    case _unknown:
      return unknown;
    default:
      throw new org.omg.CORBA.BAD_PARAM("Enum out of range: [0.." + (4 - 1) + "]: " + $value);
    }
  }
  public java.lang.String toString() {
	    if (this.__value == _achieved) return "achieved";
	    else if (this.__value == _failed) return "failed"; 
	    else if (this.__value == _none) return "none";
	    else return "unknown";
  }
}
