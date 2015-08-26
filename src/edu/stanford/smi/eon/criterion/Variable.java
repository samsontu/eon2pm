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
/*
 * Created on Jan 18, 2006
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License");  you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 *
 * The Initial Developer of the Original Code is Stanford University. Portions
 * created by Stanford University are Copyright (C) 2004.  All Rights Reserved.
 *
 * This code was developed by Stanford Medical Informatics
 * (http://www.smi.stanford.edu) at the Stanford University School of Medicine
 *
 * Contributor(s):
 */
package edu.stanford.smi.eon.criterion;

import java.util.Collection;

import edu.stanford.smi.eon.PCAServerModule.PCA_Session_Exception;
import edu.stanford.smi.eon.guidelineinterpreter.GuidelineInterpreter;
import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.ModelUtilities;
import edu.stanford.smi.protege.model.Instance;
import org.apache.log4j.*;

public class Variable extends Expression {
	public Variable(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}

  static Logger logger = Logger.getLogger(Variable.class);
	public void setderivation_expressionValue(Instance expression) {
		ModelUtilities.setOwnSlotValue(this, "derivation_expression", expression);	}
	public Instance getderivation_expressionValue(){
		return  (Instance) ModelUtilities.getOwnSlotValue(this, "derivation_expression");
	}
	
	public void setstring_nameValue(String value) {
		ModelUtilities.setOwnSlotValue(this, "string_name", value);	}
	public String getstring_nameValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "string_name"));
	}
//	 __Code above is automatically generated. Do not change

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
	public String substituteVariableValue(String input, GuidelineInterpreter glmanager){
		String outputString = input;
		String newString = "";
		try {
		  Expression result = evaluate_expression(glmanager);
		  if (result != null) {
		    logger.debug("Variable.substituteVariableValue: Evaluating variable "+
            getBrowserText()+" result in " +result.getDirectType().getName());
		    newString = result.toString();
		  } else 
		    logger.error("Variable.substituteVariableValue: Evaluating variable "+
		        getBrowserText()+" result in null");
		} catch (Exception e){
			logger.error("Exception Variable: substituteVariableValue: evaluating/converting derivation expression of " +
					getBrowserText()+" to string");
			
		}
		try {
		 if (newString != null) {
			  outputString = edu.stanford.smi.eon.util.HelperFunctions.replaceSubstringWOQuotes(input, getstring_nameValue(),  newString);
		 } else {
			 logger.error("Evaluating derivation expression of variable "+getBrowserText()+
					 " result in null string");
		 }
		} catch (Exception e) {
			 logger.error("Exception Variable: substituteVariableValue: replacing "+
					 getstring_nameValue()+" with "+newString + "in variable" +getBrowserText());
			
		}
		 return outputString;
	}
}
