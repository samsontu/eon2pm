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
<li> <b>Java Class</b> edu.stanford.smi.eon.PCAServerModule.Numeric_Data
<li> <b>Source File</b> edu/stanford/smi/eon/PCAServerModule/Numeric_Data.java
<li> <b>IDL Source File</b> PCAServer.idl
<li> <b>IDL Absolute Name</b> ::PCAServerModule::Numeric_Data
<li> <b>Repository Identifier</b> IDL:PCAServerModule/Numeric_Data:1.0
</ul>
<b>IDL definition:</b>
<pre>
    #pragma prefix "PCAServerModule"
    struct Numeric_Data {
      ::PCAServerModule::Data_Operation_Type operation;
      string domain_term;
      string unit;
      string valid_time;
      string value;
    };
</pre>
</p>
*/
final public class Numeric_Data implements java.io.Serializable {
  public edu.stanford.smi.eon.PCAServerModule.Data_Operation_Type operation;
  public java.lang.String domain_term;
  public java.lang.String unit;
  public java.lang.String valid_time;
  public java.lang.String value;
  public Numeric_Data() {
  }
  public Numeric_Data(
    edu.stanford.smi.eon.PCAServerModule.Data_Operation_Type operation,
    java.lang.String domain_term,
    java.lang.String unit,
    java.lang.String valid_time,
    java.lang.String value
  ) {
    this.operation = operation;
    this.domain_term = domain_term;
    this.unit = unit;
    this.valid_time = valid_time;
    this.value = value;
  }

}
