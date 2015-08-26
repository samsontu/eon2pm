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

import java.util.*;

import edu.stanford.smi.eon.PCAServerModule.PCA_Session_Exception;
import edu.stanford.smi.eon.guidelineinterpreter.GuidelineInterpreter;
import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.ModelUtilities;

public class Parameterized_String extends Expression {
	public Parameterized_String(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}

	public void setvariablesValue(Collection variables) {
		ModelUtilities.setOwnSlotValues(this, "variables", variables);	}
	public Collection getvariablesValue(){
		return  ModelUtilities.getOwnSlotValues(this, "variables");
	}
	
	public void setvalueValue(String value) {
		ModelUtilities.setOwnSlotValue(this, "value", value);	}
	public String getvalueValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "value"));
	}
	public void setmultivalued_variablesValue(Instance value) {
		ModelUtilities.setOwnSlotValue(this, "multivalued_variables", value);	}
	public Instance getmultivalued_variablesValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "multivalued_variables"));
	}
//	 __Code above is automatically generated. Do not change
	
	public Expression ownEvaluateExpression(GuidelineInterpreter glmanager)
     {
		Qualitative_Constant result =
	        (Qualitative_Constant) glmanager.getDBmanager().createInstance("Qualitative_Constant");
		result.setvalueValue(getvalueValue());

		Collection variables = getvariablesValue();
		String newString=getvalueValue();
		if (variables != null) {
			for (Iterator i=variables.iterator(); i.hasNext();){
				Variable var = (Variable)i.next();
				newString = var.substituteVariableValue(newString, glmanager);
			}
		}
		if (getmultivalued_variablesValue() != null) {
			newString = ((Variable_ValueSet)getmultivalued_variablesValue()).substituteAttributeValues(newString, glmanager);
		}
		result.setvalueValue(newString);	
		return result;

		
	}
 
}
