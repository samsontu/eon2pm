package edu.stanford.smi.eon.criterion;

import java.util.Collection;

import org.apache.log4j.Logger;

import edu.stanford.smi.eon.PCAServerModule.PCA_Session_Exception;
import edu.stanford.smi.eon.guidelineinterpreter.GuidelineInterpreter;
import edu.stanford.smi.eon.util.HelperFunctions;
import edu.stanford.smi.protege.model.*;

public class Variable_ValueSet extends DefaultSimpleInstance {

	public Variable_ValueSet(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
	static  Logger  logger = Logger.getLogger(Variable_ValueSet.class);

	public void setderivation_expressionValue(Instance expression) {
		ModelUtilities.setOwnSlotValue(this, "derivation_expression", expression);	}
	public Instance getderivation_expressionValue(){
		return  (Instance) ModelUtilities.getOwnSlotValue(this, "derivation_expression");
	}
	public void setlabelValue(String label) {
		ModelUtilities.setOwnSlotValue(this, "label", label);	}
	public String getlabelValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "label"));
	}
	
	public void setproperty_values_to_getValue(Collection<Instance> value) {
		ModelUtilities.setOwnSlotValues(this, "property_values_to_get", value);	}
	public Collection<Instance> getproperty_values_to_getValue() {
		return ((Collection<Instance>) ModelUtilities.getOwnSlotValues(this, "property_values_to_get"));
	}
	public String substituteAttributeValues(String inputString, GuidelineInterpreter glmanager) {
		// TODO Auto-generated method stub
		String outputString = "";
		try {
			Expression evaluatedResult = ownEvaluateExpression( glmanager);
			//Must return a set of instances
			if (evaluatedResult != null) {
				if (evaluatedResult instanceof Set_Expression) {
					for (Object inst : ((Set_Expression)evaluatedResult).getset_elementsValue()) {
						boolean first = true;
						if (inst instanceof Instance) {
							if (first) {
								outputString = outputString + substitutePropertyValues(inputString, (Instance)inst, glmanager);
								first = false;
							} else 
								outputString = outputString + " "+ substitutePropertyValues(inputString, (Instance)inst, glmanager);
						} else {
							logger.error("Result of evaluating "+inputString+" should be instances" );			
						}
					}
				} else {
					logger.error("Result of evaluating "+inputString+" should be a set of instances" );
				}
			} else
				logger.error(this.getBrowserText()+ "'s derivation expression evaluates to null");
		} catch (PCA_Session_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return outputString;
	}
	
	private String substitutePropertyValues(String inputString, Instance evaluatedResult,
			GuidelineInterpreter glmanager) {
		// evaluatedResult is an instances from which slot values will be retrieved
		// and substituted into the inputString
		Collection<Instance> propertyValues = getproperty_values_to_getValue() ;
		for (Instance propertyValue : propertyValues) {
			Slot slot = ((Property_Value)propertyValue).getslotValue();
			Object value = evaluatedResult.getOwnSlotValue(slot);
			String newString = "";
			if (value != null) {
				if (value instanceof Frame) 
					newString = ((Instance)value).getBrowserText();
				else if (value instanceof String) {
					newString = edu.stanford.smi.eon.util.HelperFunctions.getBrowserTextFromString((String)value, this.getKnowledgeBase());
				}
				else 	newString = value.toString();
				// if value has the format "yyyy-MM-DD",change it to "MM/dd/yyyy"; otherwise no change
				//newString = HelperFunctions.formatDisplayDate(newString);
				inputString = HelperFunctions.replaceSubstringWOQuotes(inputString, ((Property_Value)propertyValue).getstring_nameValue(),  newString);
			}
		}
		return inputString;
	}
	
	public Expression ownEvaluateExpression(GuidelineInterpreter glmanager)
		      throws PCA_Session_Exception {
				if (getderivation_expressionValue() != null) {
				  try {
				    Expression result = ((Expression)getderivation_expressionValue()).evaluate_expression(glmanager);
				    return result;
				  } catch (Exception e){
				    logger.error("Exception evaluating variable derivation expression "+getBrowserText());
				    e.printStackTrace();
				    throw new PCA_Session_Exception("Exception Variable.evaluate_expression Exception evaluating variable derivation expression of "+
				        getBrowserText());
				  }

				} else throw new PCA_Session_Exception("Exception Variable.evaluate_expression no derivation expression in"+
						getBrowserText());
				
			}


}
