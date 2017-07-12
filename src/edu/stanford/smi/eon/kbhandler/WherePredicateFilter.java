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


package edu.stanford.smi.eon.kbhandler;
import java.util.*;

import edu.stanford.smi.eon.util.*;
import edu.stanford.smi.eon.datahandler.Constants;
import edu.stanford.smi.eon.guidelineinterpreter.*;
import edu.stanford.smi.eon.time.*;
import edu.stanford.smi.eon.PCAServerModule.Criteria_Evaluation;
import edu.stanford.smi.eon.PCAServerModule.PCA_Session_Exception;
import edu.stanford.smi.eon.PCAServerModule.Truth_Value;
import edu.stanford.smi.eon.criterion.*;
import edu.stanford.smi.protege.model.*;

import org.apache.log4j.*;

public class WherePredicateFilter implements AbstractWhereFilter {
	public String attribute, predicate, quantifier;

	static Logger logger =  Logger.getLogger(WherePredicateFilter.class);
	public WherePredicateFilter(String attribute, String predicate,
			String quantifier) {
		this.attribute = attribute;
		this.quantifier = quantifier;
		this.predicate = predicate;
		logger.debug(attribute +" : "+quantifier +" : "+ predicate);
	}
/*
 * An instance i passes a PredicateFilter if some or all of the values of attribute satisfies the predicate 
 * (e.g., if attriubute = absolute contraindication, predicate = derivable, quantifier = exists, then 
 * some of the absolute contraindications must be derivable: 
 * 
 * (exists ?constra (and (absolute_contraindication ?i ?contra)(derivable ?contra)))
 */


	public boolean evaluate (Instance instance) throws KnowledgeBaseException {
		return evaluate(instance, null);
	}
	public boolean evaluate(Instance instance, GuidelineInterpreter guidelineManager) throws KnowledgeBaseException {
		KnowledgeBase kb = instance.getKnowledgeBase();
		Slot attributeSlot = null;
		if (this.predicate.equals("derivable") && (this.quantifier.equals("exists"))) {
			if (attribute != null) {
				attributeSlot = kb.getSlot(attribute);
				if (attributeSlot == null) {
					logger.error(attribute +" is not a value attribute");
					return false;
				}
				Collection clses = instance.getOwnSlotValues(attributeSlot);
				for (Object value : clses) {
					if (value instanceof Cls) {
					  boolean result = evaluatePredicate((Cls)value, guidelineManager);
					  if (result) return true;
					}
				}
			} else
				if (instance instanceof Cls) {
					return evaluatePredicate((Cls)instance, guidelineManager);
				} 
		}
		return false;
	}
	
	private boolean evaluatePredicate(Cls classInstance, GuidelineInterpreter guidelineManager) {
		  Criteria_Evaluation criteriaEvaluation = null;
		  //Checks that there is a definition associated with this class
		  if (classInstance != null) {
			  logger.debug("IsDerivable holds: class="+classInstance.getName());
			  if (guidelineManager == null) {
				  logger.error("IsDerivable holds: guidelineManager is null");
				  return false;
			  }
			  Slot definitionSlot = (Slot)classInstance.getKnowledgeBase().getFrame("DiagnosticCriteria");
			  if (classInstance.hasOwnSlot(definitionSlot)) {
				  Collection<Criterion> definition = classInstance.getOwnSlotValues(definitionSlot);
				  if (definition != null) {
					  logger.debug("IsDerivable holds: class="+classInstance.getName()+" has definition");
					  for (Criterion def : definition) {
						  try {
							  //For n-ary criteria, do not need to evaluate all of subcriteria. Modified 2017/07/12
							  criteriaEvaluation = def.evaluate(guidelineManager, false);
							  if (criteriaEvaluation !=null) {
								  logger.debug("IsDerivable holds: class="+classInstance.getName()+ " "+def.getBrowserText()+
										  " evaluation "+criteriaEvaluation.truth_value);
								  if (criteriaEvaluation.truth_value.equals(Truth_Value._true)) {
									  return true;
								  }
							  }
						  } catch (PCA_Session_Exception e) {
							  logger.debug("IsDerivable holds: class="+classInstance.getName()+ " "+def.getBrowserText()+
									  " evaluation throws an exception. Continuing to evaluate PAL criterion/query");
							  e.printStackTrace();
						  }
					  }
				  }
			  }
		  }
		return false;

	}
}

