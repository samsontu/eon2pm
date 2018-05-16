package edu.stanford.smi.eon.PCAServerModule;

import gov.va.athena.advisory.Action;

import java.util.Collection;

public class Message extends Action_Spec_Record {
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	  //public int fine_grain_priority;
	  public java.lang.String message_type;
	  public java.lang.String rule_in_criteria;
	  public java.lang.String message;
	 
/*	    java.lang.String name,  //label
	    java.lang.String text,  //description
	    java.lang.String action_spec_class,
	    int fine_grain_priority,
	    edu.stanford.smi.eon.PCAServerModule.Guideline_Entity action_spec,
	    edu.stanford.smi.eon.PCAServerModule.Justification justification,
	    java.lang.String level_of_evidence,
	    java.lang.String net_benefit,
	    java.lang.String  quality_of_evidence,
	    java.lang.String strength_of_recommendation,
	    Collection<String> references
*/	  
	  public Message(
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
			    java.lang.String message,
			    java.lang.String message_type,
			    Collection<java.lang.String> subsidiary_message,
			    java.lang.String rule_in_criteria,
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
			    this.message = message;
			    this.message_type = message_type;
			    this.subsidiary_message = subsidiary_message;
			    this.rule_in_criteria = rule_in_criteria;
			    this.collateral_actions = collateral_actions;
			  }

	  public Action translateToAdvisoryFormat() {
		  gov.va.athena.advisory.Message action = new gov.va.athena.advisory.impl.DefaultMessage();
		  super.setActionProperties(action);
		  action.setMessage(this.message);
		  action.setMessage_type(this.message_type);
		  if (this.subsidiary_message != null)
			  action.setSubsidiary_message(this.subsidiary_message);
		  action.setRule_in_criteria(this.rule_in_criteria);
		  return action;
	  } 
	  
//	  public boolean equals(Message m) {
//		  return ((this.name == m.name) &&
//				  (this.text == m.text) &&
//				  (this.action_spec_class == m.action_spec_class) &&
//				  (this.message == m.message) &&
//				  (this.message_type == m.message_type));
//	  }
	  
}
