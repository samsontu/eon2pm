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
import org.apache.log4j.*;
/**
<p>
<ul>
<li> <b>Java Class</b> edu.stanford.smi.eon.PCAServerModule.Compliance_Level
<li> <b>Source File</b> edu/stanford/smi/eon/PCAServerModule/Compliance_Level.java
<li> <b>IDL Source File</b> PCAServer.idl
<li> <b>IDL Absolute Name</b> ::PCAServerModule::Compliance_Level
<li> <b>Repository Identifier</b> IDL:PCAServerModule/Compliance_Level:1.0
</ul>
<b>IDL definition:</b>
<pre>
    #pragma prefix "PCAServerModule"
    enum Compliance_Level {
      strict,
      permissive
    };
</pre>
</p>
*/
public class Compliance_Level implements java.io.Serializable {
  static Logger logger = Logger.getLogger(Compliance_Level.class);
  final public static int _strict = 0;
  final public static int _permissive = 1;
  final public static edu.stanford.smi.eon.PCAServerModule.Compliance_Level strict = new edu.stanford.smi.eon.PCAServerModule.Compliance_Level(_strict);
  final public static edu.stanford.smi.eon.PCAServerModule.Compliance_Level permissive = new edu.stanford.smi.eon.PCAServerModule.Compliance_Level(_permissive);
  private int __value;
  protected Compliance_Level(int value) {
    this.__value = value;
  }
  public int value() {
    return __value;
  }
  public static edu.stanford.smi.eon.PCAServerModule.Compliance_Level from_int(int $value) {
    switch($value) {
    case _strict:
      return strict;
    case _permissive:
      return permissive;
    default:
    	//Compliance level is no longer used. The execution engine always operates in strict mode
    	logger.error("Bad parameter: Enum out of range: [0.." + (2 - 1) + "]: " + $value);
    	return strict;
      //throw new org.omg.CORBA.BAD_PARAM("Enum out of range: [0.." + (2 - 1) + "]: " + $value);
    }
  }
  public java.lang.String toString() {
	    if (this.__value == _strict) return "strict";
	    else return "permissive";
}

}
