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

// Created on Wed Jun 20 12:58:02 PDT 2001
// Copyright Stanford University 2000

package edu.stanford.smi.eon.criterion;

import java.util.*;
import java.io.*;
import java.text.NumberFormat;

import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.util.*;
import edu.stanford.smi.eon.util.*;
import edu.stanford.smi.eon.guidelineinterpreter.*;
import edu.stanford.smi.eon.PCAServerModule.*;
import edu.stanford.smi.eon.kbhandler.*;
import edu.stanford.smi.eon.datahandler.*;
import edu.stanford.smi.eon.time.*;
import edu.stanford.smi.protegex.pal.engine.*; 
import edu.stanford.smi.protegex.pal.parser.*;
import edu.stanford.smi.protegex.pal.relations.*;
import edu.stanford.smi.protegex.pal.*;

/** 
 */
public class Set_Expression extends Expression {

	public Set_Expression(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}

	public void setset_elementsValue(Collection set_elements) {
		ModelUtilities.setOwnSlotValues(this, "set_elements", set_elements);	}
	public Collection getset_elementsValue(){
		return  ModelUtilities.getOwnSlotValues(this, "set_elements");
	}
	// __Code above is automatically generated. Do not change

	public Expression ownEvaluateExpression(GuidelineInterpreter glmanager) {
		return this;
	}
	public Object getSingleton() {
		Object returnValue = null;
		Collection setValue = getset_elementsValue();
		if ((setValue != null ) && (!setValue.isEmpty())) {
			if (setValue.size() > 1) {
				logger.error("More than one element in set. Take first element of "+setValue.toString());
			}
			for (Iterator i = setValue.iterator(); i.hasNext();) {
				returnValue = i.next();
				break;
			}
			return returnValue;
		} else return null;
	}
	public String toString() {
		Collection elements = getset_elementsValue();
		String text = "";
		String elementText = "";
		boolean first = true;
		if (elements == null) return text;
		else {
			for (Iterator i = elements.iterator(); i.hasNext();) {
				Object element = i.next();
				if (element instanceof DefaultInstance) {
					elementText =((DefaultInstance)element).getBrowserText();
				} else if (element instanceof String)
					elementText = edu.stanford.smi.eon.util.HelperFunctions.getBrowserTextFromString((String)element, this.getKnowledgeBase());
				else elementText = element.toString();
				if ((elementText == null) || (elementText.equals(""))) {
					continue;
				}
				else {
					if (first) {
						first = false;
						text = text.concat(elementText);
					} else text = text.concat(", "+elementText);
				}
			}
			return text;			
		}
	}
	
	public Set_Expression subtract (Set_Expression second, GuidelineInterpreter glmanager) 
		throws PCA_Session_Exception {
		if (second instanceof Set_Expression) {
			Set_Expression result =  (Set_Expression)glmanager.getDBmanager().createRegisteredInstance(
					"Set_Expression");
			Collection resultCollection = new ArrayList();
			Collection<Object>secondCollection = second.getset_elementsValue();
			if (secondCollection.isEmpty()) return this;
			for (Object firstOne: this.getset_elementsValue())
				if (!secondCollection.contains(firstOne))
					resultCollection.add(firstOne);
			result.setset_elementsValue(resultCollection);
			return result;
		} else {
			throw new PCA_Session_Exception("This expression is not a set: "+this.getBrowserText());
		}
	}

	public Numeric_Constant size(GuidelineInterpreter glManager) {
		Collection setElements = getset_elementsValue();
		Numeric_Constant count = (Numeric_Constant)(glManager.getDBmanager().createRegisteredInstance("Numeric_Constant"));
		if (setElements == null) {
			count.setvalueValue((float) 0.0);
			return count;
		} else {
			count.setvalueValue((float)(setElements.size()));
			return count;
		}

	}
}