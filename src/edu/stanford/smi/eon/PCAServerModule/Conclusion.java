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
<li> <b>Java Class</b> edu.stanford.smi.eon.PCAServerModule.Conclusion
<li> <b>Source File</b> edu/stanford/smi/eon/PCAServerModule/Conclusion.java
<li> <b>IDL Source File</b> PCAServer.idl
<li> <b>IDL Absolute Name</b> ::PCAServerModule::Conclusion
<li> <b>Repository Identifier</b> IDL:PCAServerModule/Conclusion:1.0
</ul>
<b>IDL definition:</b>
<pre>
    #pragma prefix "PCAServerModule"
    struct Conclusion {
      string case_id;
      string time;
      string parameter;
      string value;
      ::PCAServerModule::Justification justification;
    };
</pre>
</p>
*/
final public class Conclusion implements java.io.Serializable {
  public java.lang.String case_id;
  public java.lang.String time;
  public java.lang.String parameter;
  public java.lang.String value;
  public edu.stanford.smi.eon.PCAServerModule.Justification justification;
  public Conclusion() {
  }
  public Conclusion(
    java.lang.String case_id,
    java.lang.String time,
    java.lang.String parameter,
    java.lang.String value,
    edu.stanford.smi.eon.PCAServerModule.Justification justification
  ) {
    this.case_id = case_id;
    this.time = time;
    this.parameter = parameter;
    this.value = value;
    this.justification = justification;
  }
  /*
  public java.lang.String toString() {
    org.omg.CORBA.Any any = org.omg.CORBA.ORB.init().create_any();
    edu.stanford.smi.eon.PCAServerModule.ConclusionHelper.insert(any, this);
    return any.toString();
  }*/
}
