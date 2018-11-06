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
<li> <b>Java Class</b> edu.stanford.smi.eon.PCAServerModule.Prescription_Data
<li> <b>Source File</b> edu/stanford/smi/eon/PCAServerModule/Prescription_Data.java
<li> <b>IDL Source File</b> PCAServer.idl
<li> <b>IDL Absolute Name</b> ::PCAServerModule::Prescription_Data
<li> <b>Repository Identifier</b> IDL:PCAServerModule/Prescription_Data:1.0
</ul>
<b>IDL definition:</b>
<pre>
    #pragma prefix "PCAServerModule"
    struct Prescription_Data {
      ::PCAServerModule::Data_Operation_Type operation;
      string drug_name;
      float daily_dose;
      string daily_dose_unit;
      float medication_possession_ratio;
      string present_release_time;
      string start_time;
      string stop_time;
      string sig;
    };
</pre>
</p>
*/
final public class Prescription_Data implements java.io.Serializable {
  public edu.stanford.smi.eon.PCAServerModule.Data_Operation_Type operation;
  public java.lang.String drug_name;
  public float daily_dose;
  public java.lang.String daily_dose_unit;
  public int medication_possession_ratio;
  public java.lang.String present_release_time = null;
  public java.lang.String start_time;
  public java.lang.String stop_time;
  public java.lang.String sig;
  public Prescription_Data() {
  }
  public Prescription_Data(
    edu.stanford.smi.eon.PCAServerModule.Data_Operation_Type operation,
    java.lang.String drug_name,
    float daily_dose,
    java.lang.String daily_dose_unit,
    int medication_possession_ratio,
    java.lang.String present_release_time,
    java.lang.String start_time,
    java.lang.String stop_time,
    java.lang.String sig
  ) {
    this.operation = operation;
    this.drug_name = drug_name;
    this.daily_dose = daily_dose;
    this.daily_dose_unit = daily_dose_unit;
    this.medication_possession_ratio = medication_possession_ratio;
    this.present_release_time = present_release_time;
    this.start_time = start_time;
    this.stop_time = stop_time;
    this.sig = sig;
  }

  public Prescription_Data(
		    edu.stanford.smi.eon.PCAServerModule.Data_Operation_Type operation,
		    java.lang.String drug_name,
		    float daily_dose,
		    java.lang.String daily_dose_unit,
		    java.lang.String start_time,
		    java.lang.String stop_time,
		    java.lang.String sig
		  ) {
		    this.operation = operation;
		    this.drug_name = drug_name;
		    this.daily_dose = daily_dose;
		    this.daily_dose_unit = daily_dose_unit;
		    this.start_time = start_time;
		    this.stop_time = stop_time;
		    this.sig = sig;
		  }

}
