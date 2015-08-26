package edu.stanford.smi.eon.PCAServerModule;

import gov.va.athena.advisory.Action;

import java.util.Collection;

public class Order_TestProcedure extends Action_Spec_Record {
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String code;
	  public java.lang.String coding_system;
	  public java.lang.String test_or_procedure;
	  public java.lang.String when;
	  public  int when_lower_bound;
	  public  int when_upper_bound;
	  public  String when_lower_bound_unit;
	  public  String when_upper_bound_unit;

	  
	  public Order_TestProcedure(
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
			    String code,
			    String coding_system,
			    String test_or_procedure,
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
			    this.fine_grain_priority = fine_grain_priority;
			    this.action_spec = action_spec;
			    this.justification = justification;
			    this.level_of_evidence= level_of_evidence ;
			    this.net_benefit= net_benefit;
			    this.overall_quality_of_evidence= overall_quality_of_evidence;
			    this.strength_of_recommendation= strength_of_recommendation;
			    this.references= references;
			    this.subsidiary_message = subsidiary_message;
			    this.code = code;
			    this.coding_system = coding_system;
			    this.test_or_procedure = test_or_procedure;
			    this.when = when;
			    this.when_lower_bound = when_lower_bound;
			    this.when_lower_bound_unit = when_lower_bound_unit;
			    this.when_upper_bound_unit = when_lower_bound_unit;
			    this.when_upper_bound = when_upper_bound;
			    this.collateral_actions = collateral_actions;
			  };


	  public Action translateToAdvisoryFormat() {
		  gov.va.athena.advisory.Order_TestProcedure action = new gov.va.athena.advisory.impl.DefaultOrder_TestProcedure();
		  super.setActionProperties(action);
		  action.setCode(this.code);
		  action.setCoding_system(this.coding_system);
		  action.setTest_or_procedure(this.test_or_procedure);
		  if (this.subsidiary_message != null)
			  action.setSubsidiary_message(this.subsidiary_message);
		  action.setWhen(this.when);
		  action.setWhen_lower_bound(this.when_lower_bound);
		  action.setWhen_lower_bound_unit(this.when_lower_bound_unit);
		  action.setWhen_upper_bound(this.when_upper_bound);
		  action.setWhen_upper_bound_unit(this.when_upper_bound_unit);
		  return action;
		} 
}
