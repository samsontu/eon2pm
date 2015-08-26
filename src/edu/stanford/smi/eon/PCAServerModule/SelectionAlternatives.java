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
<li> <b>Java Class</b> edu.stanford.smi.eon.PCAServerModule.SelectionAlternatives
<li> <b>Source File</b> edu/stanford/smi/eon/PCAServerModule/SelectionAlternatives.java
<li> <b>IDL Source File</b> PCAServer.idl
<li> <b>IDL Absolute Name</b> ::PCAServerModule::SelectionAlternatives
<li> <b>Repository Identifier</b> IDL:PCAServerModule/SelectionAlternatives:1.0
</ul>
<b>IDL definition:</b>
<pre>
    #pragma prefix "PCAServerModule"
    enum SelectionAlternatives {
      all_of,
      some_of,
      one_of
    };
</pre>
</p>
*/
public class SelectionAlternatives implements java.io.Serializable {
  final public static int _all_of = 0;
  final public static int _some_of = 1;
  final public static int _one_of = 2;
  final public static edu.stanford.smi.eon.PCAServerModule.SelectionAlternatives all_of = new edu.stanford.smi.eon.PCAServerModule.SelectionAlternatives(_all_of);
  final public static edu.stanford.smi.eon.PCAServerModule.SelectionAlternatives some_of = new edu.stanford.smi.eon.PCAServerModule.SelectionAlternatives(_some_of);
  final public static edu.stanford.smi.eon.PCAServerModule.SelectionAlternatives one_of = new edu.stanford.smi.eon.PCAServerModule.SelectionAlternatives(_one_of);
  private int __value;
  protected SelectionAlternatives(int value) {
    this.__value = value;
  }
  public int value() {
    return __value;
  }
  public static edu.stanford.smi.eon.PCAServerModule.SelectionAlternatives from_int(int $value) {
    switch($value) {
    case _all_of:
      return all_of;
    case _some_of:
      return some_of;
    case _one_of:
      return one_of;
    default:
      throw new org.omg.CORBA.BAD_PARAM("Enum out of range: [0.." + (3 - 1) + "]: " + $value);
    }
  }

}
