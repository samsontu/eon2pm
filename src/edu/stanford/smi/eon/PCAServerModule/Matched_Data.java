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
<li> <b>Java Class</b> edu.stanford.smi.eon.PCAServerModule.Matched_Data
<li> <b>Source File</b> edu/stanford/smi/eon/PCAServerModule/Matched_Data.java
<li> <b>IDL Source File</b> PCAServer.idl
<li> <b>IDL Absolute Name</b> ::PCAServerModule::Matched_Data
<li> <b>Repository Identifier</b> IDL:PCAServerModule/Matched_Data:1.0
</ul>
<b>IDL definition:</b>
<pre>
    #pragma prefix "PCAServerModule"
    struct Matched_Data {
      string data_class;
      string guideline_term;
      ::PCAServerModule::stringSeq data;
    };
</pre>
</p>
*/
final public class Matched_Data implements java.io.Serializable {
  public java.lang.String data_class;
  public java.lang.String guideline_term;
  public java.lang.String[] data;
  public Matched_Data() {
  }
  public Matched_Data(
    java.lang.String data_class,
    java.lang.String guideline_term,
    java.lang.String[] data
  ) {
    this.data_class = data_class;
    this.guideline_term = guideline_term;
    this.data = data;
  }
  
//A technically valid, but unsatisfying, equals method


   public String getDataClass() {
       return data_class;
   }

   public String getGuidelineTerm() {
       return guideline_term;
   }
   
   public String[] getData() {
       return data;
   }

   @Override public boolean equals(Object other) {
       boolean result = false;
       if (other instanceof Matched_Data) {
    	   Matched_Data that = (Matched_Data) other;
           result = (this.getDataClass().equals(that.getDataClass()) && this.getGuidelineTerm().equals(that.getGuidelineTerm())
                   && this.data.equals(that.data) && this.getClass().equals(that.getClass()));
       }
       return result;
   }

   @Override public int hashCode() {
       return 41 * (41 * (41 + getDataClass().hashCode()) + getGuidelineTerm().hashCode()) + getData().hashCode();
   }
}


