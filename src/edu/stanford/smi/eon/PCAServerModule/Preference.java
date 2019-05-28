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
<li> <b>Java Class</b> edu.stanford.smi.eon.PCAServerModule.Preference
<li> <b>Source File</b> edu/stanford/smi/eon/PCAServerModule/Preference.java
<li> <b>IDL Source File</b> PCAServer.idl
<li> <b>IDL Absolute Name</b> ::PCAServerModule::Preference
<li> <b>Repository Identifier</b> IDL:PCAServerModule/Preference:1.0
</ul>
<b>IDL definition:</b>
<pre>
    #pragma prefix "PCAServerModule"
    enum Preference {
      preferred,
      neutral,
      ruled_out
    };
</pre>
</p>
*/
public class Preference implements java.io.Serializable {
  final public static int _preferred = 0;
  final public static int _neutral = 1;
  final public static int _ruled_out = 2;
  final public static int _blocked = 3;
  final public static edu.stanford.smi.eon.PCAServerModule.Preference preferred = new edu.stanford.smi.eon.PCAServerModule.Preference(_preferred);
  final public static edu.stanford.smi.eon.PCAServerModule.Preference neutral = new edu.stanford.smi.eon.PCAServerModule.Preference(_neutral);
  final public static edu.stanford.smi.eon.PCAServerModule.Preference ruled_out = new edu.stanford.smi.eon.PCAServerModule.Preference(_ruled_out);
  final public static edu.stanford.smi.eon.PCAServerModule.Preference blocked = new edu.stanford.smi.eon.PCAServerModule.Preference(_blocked);
  private int __value;
  protected Preference(int value) {
    this.__value = value;
  }
  public int value() {
    return __value;
  }
  public static edu.stanford.smi.eon.PCAServerModule.Preference from_int(int $value) throws Exception {
    switch($value) {
    case _preferred:
      return preferred;
    case _neutral:
      return neutral;
    case _ruled_out:
      return ruled_out;
    case _blocked:
        return blocked;
    default:
      throw new Exception("Bad parameter for Preference"+"Enum out of range: [0.." + (3 ) + "]: " + $value);
    }
  }

  public java.lang.String toString() {
	    if (this.__value == _preferred) return "preferred";
	    else if (this.__value == _neutral) return "neutral"; 
	    else if (this.__value == _blocked) return "blocked";
	    else return "ruled out";
  }
}
