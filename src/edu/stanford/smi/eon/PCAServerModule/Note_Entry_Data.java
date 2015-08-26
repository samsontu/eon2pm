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
<li> <b>Java Class</b> edu.stanford.smi.eon.PCAServerModule.Note_Entry_Data
<li> <b>Source File</b> edu/stanford/smi/eon/PCAServerModule/Note_Entry_Data.java
<li> <b>IDL Source File</b> PCAServer.idl
<li> <b>IDL Absolute Name</b> ::PCAServerModule::Note_Entry_Data
<li> <b>Repository Identifier</b> IDL:PCAServerModule/Note_Entry_Data:1.0
</ul>
<b>IDL definition:</b>
<pre>
    #pragma prefix "PCAServerModule"
    struct Note_Entry_Data {
      ::PCAServerModule::Data_Operation_Type operation;
      ::PCAServerModule::Entry_Type entry_type;
      string domain_term;
      string value;
      string valid_time; //most recent record
      string start_time; //earliest

    };
</pre>
</p>
*/
final public class Note_Entry_Data implements java.io.Serializable {
  public edu.stanford.smi.eon.PCAServerModule.Data_Operation_Type operation;
  public edu.stanford.smi.eon.PCAServerModule.Entry_Type entry_type;
  public java.lang.String domain_term;
  public java.lang.String value;
  public java.lang.String valid_time;
  public java.lang.String start_time;
  public Note_Entry_Data() {
  }
  public Note_Entry_Data(
    edu.stanford.smi.eon.PCAServerModule.Data_Operation_Type operation,
    edu.stanford.smi.eon.PCAServerModule.Entry_Type entry_type,
    java.lang.String domain_term,
    java.lang.String value,
    java.lang.String valid_time,
    java.lang.String start_time
  ) {
    this.operation = operation;
    this.entry_type = entry_type;
    this.domain_term = domain_term;
    this.value = value;
    this.valid_time = valid_time;
    this.start_time = start_time;
  }

}
