package edu.stanford.smi.eon.PCAServerModule;

import gov.va.athena.advisory.Action;

import java.util.Collection;

public class Referral extends Action_Spec_Record {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public java.lang.String who_to;
	public java.lang.String when;
	public  int when_lower_bound;
	public  int when_upper_bound;
	public  String when_lower_bound_unit;
	public  String when_upper_bound_unit;

	public Referral(
			java.lang.String name,
			java.lang.String text,
			java.lang.String action_spec_class,
			int fine_grain_priority,
			edu.stanford.smi.eon.PCAServerModule.Guideline_Entity action_spec,
			edu.stanford.smi.eon.PCAServerModule.Justification justification,
			java.lang.String level_of_evidence,
			java.lang.String net_benefit,
			java.lang.String overall_quality_of_evidence,
			java.lang.String strength_of_recommendation,
			Collection<String> references,
			Collection<String> subsidiary_message,
			java.lang.String who_to,
			String when,
			int when_lower_bound,
			int when_upper_bound,
			String when_lower_bound_unit,
			String when_upper_bound_unit,
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
		this.who_to = who_to;
		this.when = when;
		this.when_lower_bound = when_lower_bound;
		this.when_lower_bound_unit = when_lower_bound_unit;
		this.when_upper_bound_unit = when_lower_bound_unit;
		this.when_upper_bound = when_upper_bound;
		this.collateral_actions = collateral_actions;
	}

	public Action translateToAdvisoryFormat() {
		gov.va.athena.advisory.Referral action = new  gov.va.athena.advisory.impl.DefaultReferral();
		super.setActionProperties(action);
		action.setWho_to(this.who_to);
		action.setWhen(this.when);
		action.setWhen_lower_bound(this.when_lower_bound);
		action.setWhen_lower_bound_unit(this.when_lower_bound_unit);
		action.setWhen_upper_bound(this.when_upper_bound);
		action.setWhen_upper_bound_unit(this.when_upper_bound_unit);
		  if (this.subsidiary_message != null)
			  action.setSubsidiary_message(this.subsidiary_message);
		return action;
	} 


}
