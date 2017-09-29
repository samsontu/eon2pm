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

import java.util.Collection;

/**
<p>
<ul>
<li> <b>Java Class</b> edu.stanford.smi.eon.PCAServerModule.Add_Evaluation
<li> <b>Source File</b> edu/stanford/smi/eon/PCAServerModule/Add_Evaluation.java
<li> <b>IDL Source File</b> PCAServer.idl
<li> <b>IDL Absolute Name</b> ::PCAServerModule::Add_Evaluation
<li> <b>Repository Identifier</b> IDL:PCAServerModule/Add_Evaluation:1.0
</ul>
<b>IDL definition:</b>
<pre>
    #pragma prefix "PCAServerModule"
    struct Add_Evaluation {
      string description;
      string name;
      ::PCAServerModule::Guideline_Entity guideline_id;
      string activity_to_start;
      ::PCAServerModule::Matched_DataSeq beneficial_interactions;
      ::PCAServerModule::Matched_DataSeq compelling_indications;
      ::PCAServerModule::Matched_DataSeq contraindications;
      ::PCAServerModule::Matched_DataSeq harmful_interactions;
      ::PCAServerModule::Matched_DataSeq relative_contraindications;
      ::PCAServerModule::Matched_DataSeq relative_indications;
      ::PCAServerModule::Truth_Value prior_use;
      ::PCAServerModule::Action_Spec_RecordSeq messages;
      ::PCAServerModule::Matched_DataSeq side_effects;
      ::PCAServerModule::Preference preference;
    };
</pre>
</p>
*/
final public class Add_Evaluation implements java.io.Serializable {
  public java.lang.String description; //drug_to_add_string
  public java.lang.String name; //drug class pretty name
  public edu.stanford.smi.eon.PCAServerModule.Guideline_Entity guideline_id;
  public java.lang.String activity_to_start;  //drug_class :NAME
  public edu.stanford.smi.eon.PCAServerModule.Matched_Data[] is_first_line_drug_for;
  public edu.stanford.smi.eon.PCAServerModule.Matched_Data[] is_second_line_drug_for;
  public edu.stanford.smi.eon.PCAServerModule.Matched_Data[] is_third_line_drug_for;
  public edu.stanford.smi.eon.PCAServerModule.Matched_Data[] beneficial_interactions;
  public edu.stanford.smi.eon.PCAServerModule.Matched_Data[] compelling_indications;
  public edu.stanford.smi.eon.PCAServerModule.Matched_Data[] contraindications;
  public edu.stanford.smi.eon.PCAServerModule.Matched_Data[] harmful_interactions;
  public edu.stanford.smi.eon.PCAServerModule.Matched_Data[] relative_contraindications;
  public edu.stanford.smi.eon.PCAServerModule.Matched_Data[] relative_indications;
  public edu.stanford.smi.eon.PCAServerModule.Truth_Value prior_use;
  public edu.stanford.smi.eon.PCAServerModule.Matched_Data[] side_effects;
  public edu.stanford.smi.eon.PCAServerModule.Matched_Data[] do_not_start_controllable_conditions;
  public edu.stanford.smi.eon.PCAServerModule.Matched_Data[] do_not_start_uncontrollable_conditions;
  public edu.stanford.smi.eon.PCAServerModule.Preference preference;
  public edu.stanford.smi.eon.PCAServerModule.Action_Spec_Record[] messages;
  public Collection<String> specific_drug;
  public int fine_grain_priority;
  public String recommendationBasis;
  
  public Add_Evaluation() {
  }
  public Add_Evaluation(
    java.lang.String description,
    java.lang.String name,
    edu.stanford.smi.eon.PCAServerModule.Guideline_Entity guideline_id,
    java.lang.String activity_to_start,
    edu.stanford.smi.eon.PCAServerModule.Matched_Data[] is_first_line_drug_for,
    edu.stanford.smi.eon.PCAServerModule.Matched_Data[] is_second_line_drug_for,
    edu.stanford.smi.eon.PCAServerModule.Matched_Data[] is_third_line_drug_for,
     edu.stanford.smi.eon.PCAServerModule.Matched_Data[] beneficial_interactions,
    edu.stanford.smi.eon.PCAServerModule.Matched_Data[] compelling_indications,
    edu.stanford.smi.eon.PCAServerModule.Matched_Data[] contraindications,
    edu.stanford.smi.eon.PCAServerModule.Matched_Data[] harmful_interactions,
    edu.stanford.smi.eon.PCAServerModule.Matched_Data[] relative_contraindications,
    edu.stanford.smi.eon.PCAServerModule.Matched_Data[] relative_indications,
    edu.stanford.smi.eon.PCAServerModule.Truth_Value prior_use,
    edu.stanford.smi.eon.PCAServerModule.Action_Spec_Record[] messages,
    edu.stanford.smi.eon.PCAServerModule.Matched_Data[] side_effects,
    edu.stanford.smi.eon.PCAServerModule.Matched_Data[] do_not_start_controllable_conditions,
    edu.stanford.smi.eon.PCAServerModule.Matched_Data[] do_not_start_uncontrollable_conditions,
    edu.stanford.smi.eon.PCAServerModule.Preference preference,
    Collection<String> specific_drug,
    int fine_grain_priority,
    String recommendationBasis
  ) {
    this.description = description;
    this.name = name;
    this.guideline_id = guideline_id;
    this.activity_to_start = activity_to_start;
    this.is_first_line_drug_for = is_first_line_drug_for;
    this.is_second_line_drug_for = is_second_line_drug_for;
    this.is_third_line_drug_for = is_third_line_drug_for;
    this.beneficial_interactions = beneficial_interactions;
    this.compelling_indications = compelling_indications;
    this.contraindications = contraindications;
    this.harmful_interactions = harmful_interactions;
    this.relative_contraindications = relative_contraindications;
    this.relative_indications = relative_indications;
    this.prior_use = prior_use;
    this.messages = messages;
    this.side_effects = side_effects;
    this.preference = preference;
    this.specific_drug = specific_drug;
    this.do_not_start_controllable_conditions = do_not_start_controllable_conditions;
    this.do_not_start_uncontrollable_conditions = do_not_start_uncontrollable_conditions;
    this.fine_grain_priority = fine_grain_priority;
    this.recommendationBasis = recommendationBasis;
  }

}
