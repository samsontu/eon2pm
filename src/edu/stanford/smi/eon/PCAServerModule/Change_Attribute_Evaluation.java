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
<li> <b>Java Class</b> edu.stanford.smi.eon.PCAServerModule.Change_Attribute_Evaluation
<li> <b>Source File</b> edu/stanford/smi/eon/PCAServerModule/Change_Attribute_Evaluation.java
<li> <b>IDL Source File</b> PCAServer.idl
<li> <b>IDL Absolute Name</b> ::PCAServerModule::Change_Attribute_Evaluation
<li> <b>Repository Identifier</b> IDL:PCAServerModule/Change_Attribute_Evaluation:1.0
</ul>
<b>IDL definition:</b>
<pre>
    #pragma prefix "PCAServerModule"
    struct Change_Attribute_Evaluation {
      string description;
      string name;
      string guideline_id;
      string attribute_name;
      string level;
      ::PCAServerModule::Matched_DataSeq side_effects;
      ::PCAServerModule::Direction change_direction;
      ::PCAServerModule::Action_Spec_RecordSeq messages;
      ::PCAServerModule::Preference preference;
    };
</pre>
</p>
*/
final public class Change_Attribute_Evaluation implements java.io.Serializable {
  public java.lang.String description;
  public java.lang.String name;
  public java.lang.String guideline_id;
  public java.lang.String attribute_name;
  public java.lang.String level;
  public edu.stanford.smi.eon.PCAServerModule.Matched_Data[] beneficial_interactions;
  public edu.stanford.smi.eon.PCAServerModule.Matched_Data[] compelling_indications;
  public edu.stanford.smi.eon.PCAServerModule.Matched_Data[] contraindications;
  public edu.stanford.smi.eon.PCAServerModule.Matched_Data[] harmful_interactions;
  public edu.stanford.smi.eon.PCAServerModule.Matched_Data[] relative_contraindications;
  public edu.stanford.smi.eon.PCAServerModule.Matched_Data[] relative_indications;
  public edu.stanford.smi.eon.PCAServerModule.Matched_Data[] adverse_reactions;
  public edu.stanford.smi.eon.PCAServerModule.Matched_Data[] do_not_intensify_conditions;
  public edu.stanford.smi.eon.PCAServerModule.Direction change_direction;
  public edu.stanford.smi.eon.PCAServerModule.Action_Spec_Record[] messages;
  public edu.stanford.smi.eon.PCAServerModule.Preference preference;
  public int fine_grain_priority;
  
  public Change_Attribute_Evaluation() {
  }
  public Change_Attribute_Evaluation(
    java.lang.String description,
    java.lang.String name,
    java.lang.String guideline_id,
    java.lang.String attribute_name,
    java.lang.String level,
    edu.stanford.smi.eon.PCAServerModule.Matched_Data[] beneficial_interactions,
    edu.stanford.smi.eon.PCAServerModule.Matched_Data[] compelling_indications,
    edu.stanford.smi.eon.PCAServerModule.Matched_Data[] contraindications,
    edu.stanford.smi.eon.PCAServerModule.Matched_Data[] harmful_interactions,
    edu.stanford.smi.eon.PCAServerModule.Matched_Data[] relative_contraindications,
    edu.stanford.smi.eon.PCAServerModule.Matched_Data[] relative_indications,
    edu.stanford.smi.eon.PCAServerModule.Matched_Data[] adverse_reactions,
    edu.stanford.smi.eon.PCAServerModule.Matched_Data[] do_not_intensify_conditions,
    edu.stanford.smi.eon.PCAServerModule.Direction change_direction,
    edu.stanford.smi.eon.PCAServerModule.Action_Spec_Record[] messages,
    edu.stanford.smi.eon.PCAServerModule.Preference preference,
    int fine_grain_priority
  ) {
    this.description = description;
    this.name = name;
    this.guideline_id = guideline_id;
    this.attribute_name = attribute_name;
    this.level = level;
    this.beneficial_interactions = beneficial_interactions;
    this.compelling_indications=compelling_indications;
    this.contraindications =contraindications;
    this.harmful_interactions = harmful_interactions;
    this.relative_contraindications =relative_contraindications;
    this.relative_indications = relative_indications;
    this.do_not_intensify_conditions = do_not_intensify_conditions;
    this.change_direction = change_direction;
    this.messages = messages;
    this.adverse_reactions = adverse_reactions;
    this.preference = preference;
    this.fine_grain_priority = fine_grain_priority;
  }

}
