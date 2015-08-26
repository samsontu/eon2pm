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
<li> <b>Java Class</b> edu.stanford.smi.eon.PCAServerModule.Current_Activity_Assessment
<li> <b>Source File</b> edu/stanford/smi/eon/PCAServerModule/Current_Activity_Assessment.java
<li> <b>IDL Source File</b> PCAServer.idl
<li> <b>IDL Absolute Name</b> ::PCAServerModule::Current_Activity_Assessment
<li> <b>Repository Identifier</b> IDL:PCAServerModule/Current_Activity_Assessment:1.0
</ul>
<b>IDL definition:</b>
<pre>
    #pragma prefix "PCAServerModule"
    struct Current_Activity_Assessment {
      string description;
      string name;
      ::PCAServerModule::Guideline_Entity guideline_id;
      string activity_class;
      string activity_level;
      ::PCAServerModule::Matched_DataSeq beneficial_interactions;
      ::PCAServerModule::Matched_DataSeq compelling_indications;
      ::PCAServerModule::Matched_DataSeq contraindications;
      ::PCAServerModule::Matched_DataSeq harmful_interactions;
      ::PCAServerModule::Matched_DataSeq relative_contraindications;
      ::PCAServerModule::Matched_DataSeq relative_indications;
      ::PCAServerModule::Matched_DataSeq side_effects;
    };
</pre>
</p>
*/
final public class Current_Activity_Assessment implements java.io.Serializable {
  public java.lang.String description;
  public java.lang.String name;
  public edu.stanford.smi.eon.PCAServerModule.Guideline_Entity guideline_id;
  public java.lang.String activity_class;
  public java.lang.String activity_level;
  public edu.stanford.smi.eon.PCAServerModule.Matched_Data[] beneficial_interactions;
  public edu.stanford.smi.eon.PCAServerModule.Matched_Data[] compelling_indications;
  public edu.stanford.smi.eon.PCAServerModule.Matched_Data[] contraindications;
  public edu.stanford.smi.eon.PCAServerModule.Matched_Data[] harmful_interactions;
  public edu.stanford.smi.eon.PCAServerModule.Matched_Data[] relative_contraindications;
  public edu.stanford.smi.eon.PCAServerModule.Matched_Data[] relative_indications;
  public edu.stanford.smi.eon.PCAServerModule.Matched_Data[] side_effects;
  public Current_Activity_Assessment() {
  }
  public Current_Activity_Assessment(
    java.lang.String description,
    java.lang.String name,
    edu.stanford.smi.eon.PCAServerModule.Guideline_Entity guideline_id,
    java.lang.String activity_class,
    java.lang.String activity_level,
    edu.stanford.smi.eon.PCAServerModule.Matched_Data[] beneficial_interactions,
    edu.stanford.smi.eon.PCAServerModule.Matched_Data[] compelling_indications,
    edu.stanford.smi.eon.PCAServerModule.Matched_Data[] contraindications,
    edu.stanford.smi.eon.PCAServerModule.Matched_Data[] harmful_interactions,
    edu.stanford.smi.eon.PCAServerModule.Matched_Data[] relative_contraindications,
    edu.stanford.smi.eon.PCAServerModule.Matched_Data[] relative_indications,
    edu.stanford.smi.eon.PCAServerModule.Matched_Data[] side_effects
  ) {
    this.description = description;
    this.name = name;
    this.guideline_id = guideline_id;
    this.activity_class = activity_class;
    this.activity_level = activity_level;
    this.beneficial_interactions = beneficial_interactions;
    this.compelling_indications = compelling_indications;
    this.contraindications = contraindications;
    this.harmful_interactions = harmful_interactions;
    this.relative_contraindications = relative_contraindications;
    this.relative_indications = relative_indications;
    this.side_effects = side_effects;
  }

}
