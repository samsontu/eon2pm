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

import gov.va.athena.advisory.Action;

import java.util.ArrayList;
import java.util.Collection;

/**
<p>
<ul>
<li> <b>Java Class</b> edu.stanford.smi.eon.PCAServerModule.Action_Spec_Record
<li> <b>Source File</b> edu/stanford/smi/eon/PCAServerModule/Action_Spec_Record.java
<li> <b>IDL Source File</b> PCAServer.idl
<li> <b>IDL Absolute Name</b> ::PCAServerModule::Action_Spec_Record
<li> <b>Repository Identifier</b> IDL:PCAServerModule/Action_Spec_Record:1.0
</ul>
<b>IDL definition:</b>
<pre>
    #pragma prefix "PCAServerModule"
    struct Action_Spec_Record {
      string name;
      string text;
      string action_spec_class;
      ::PCAServerModule::Guideline_Entity action_spec;
      ::PCAServerModule::Justification justification;
    };
</pre>
</p>
 */
public class Action_Spec_Record implements java.io.Serializable {
	public java.lang.String name;  //label
	public java.lang.String text;  //description
	public java.lang.String action_spec_class;  //action_class
	public edu.stanford.smi.eon.PCAServerModule.Guideline_Entity action_spec; //kb_instance_id
	public edu.stanford.smi.eon.PCAServerModule.Justification justification;
	public java.lang.String level_of_evidence;
	public java.lang.String net_benefit;
	public java.lang.String  overall_quality_of_evidence;
	public java.lang.String strength_of_recommendation;
	public Collection<String> references;
	public int fine_grain_priority;
	public Collection<Action_Spec_Record> collateral_actions;
	public Collection<String> subsidiary_message;
	public Action_Spec_Record() {
	}
	public Action_Spec_Record(
			java.lang.String name,  //label
			java.lang.String text,  //description
			java.lang.String action_spec_class,
			int fine_grain_priority,
			edu.stanford.smi.eon.PCAServerModule.Guideline_Entity action_spec,
			edu.stanford.smi.eon.PCAServerModule.Justification justification,
			java.lang.String level_of_evidence,
			java.lang.String net_benefit,
			java.lang.String  overall_quality_of_evidence,
			java.lang.String strength_of_recommendation,
			Collection<String> references,
			Collection<String> subsidiary_message,
			Collection<Action_Spec_Record> collateral_actions
			) {
		this.name = name;
		this.text = text;
		this.action_spec_class = action_spec_class;
		this.action_spec = action_spec;
		this.fine_grain_priority = fine_grain_priority;
		this.justification = justification;
		this.level_of_evidence= level_of_evidence ;
		this.net_benefit= net_benefit;
		this.overall_quality_of_evidence= overall_quality_of_evidence;
		this.strength_of_recommendation= strength_of_recommendation;
		this.references= references;
		this.subsidiary_message = subsidiary_message;
		this.collateral_actions = collateral_actions;
	}
	public Action translateToAdvisoryFormat() {
		Action action = new gov.va.athena.advisory.impl.DefaultAction();
		if (this.subsidiary_message != null)
			action.setSubsidiary_message(this.subsidiary_message);
		setActionProperties(action);
		return action;
	}
	protected void setActionProperties(Action action) {
		action.setDescription(this.text);
		action.setAction_class(this.action_spec_class);
		action.setFine_grain_priority(this.fine_grain_priority);
		action.setLabel(this.name);
		action.setGrade_recommendation(this.strength_of_recommendation);
		action.setNet_benefit(this.net_benefit);
		action.setQuality_of_evidence(this.overall_quality_of_evidence);
		action.setOverall_quality_evidence(this.level_of_evidence);
		Collection<Action> collateralActions = new ArrayList<Action>();
		if (this.collateral_actions != null) {
			for (Action_Spec_Record a : this.collateral_actions) {
				collateralActions.add(a.translateToAdvisoryFormat());
			}
			action.setCollateral_action(collateralActions);
		}
		if (this.references != null)
			action.setReference(this.references);

		
	}

}
