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

public class WhereJoinFilter implements AbstractWhereFilter {
	private Collection<Attributes_Comparison> attributeComparisons;
	private Collection<Instance> instancesToCompare;
	static Logger logger =  Logger.getLogger(WhereJoinFilter.class);
	public WhereJoinFilter(Collection<Attributes_Comparison> attributeComparisons,
			Collection<Instance> instancesToCompare) {
		this.attributeComparisons = attributeComparisons;
		this.instancesToCompare = instancesToCompare;
	}
/*
 * 
 */


	public boolean evaluate (Instance instance) throws KnowledgeBaseException {
		return evaluate(instance, null);
	}
	public boolean evaluate(Instance instance, GuidelineInterpreter guidelineManager) throws KnowledgeBaseException {
		WhereComparisonFilter compareFilter = new WhereComparisonFilter(null, null, null);
		Collection<WhereComparisonFilter> naryFilter = new ArrayList<WhereComparisonFilter>();
		naryFilter.add(compareFilter);
		WhereFilter negatedFilter = new WhereFilter(DharmaPaddaConstants.NOT, naryFilter);
		//check that there exists one instance to compare where all joining attribute comparisons return true
		for (Instance instanceToCompare : instancesToCompare) {
			boolean existMatch = true;
			boolean result = false;
			for (Attributes_Comparison attributeComparison : attributeComparisons) {
				compareFilter.attribute = attributeComparison.getattributeValue().getName();
				boolean cardinalityMultiple = instanceToCompare.getDirectType().getTemplateSlotAllowsMultipleValues(attributeComparison.getattribute_to_compareValue());
				if (cardinalityMultiple)
					compareFilter.value = instanceToCompare.getOwnSlotValues(attributeComparison.getattribute_to_compareValue());
				else {
					compareFilter.value = instanceToCompare.getOwnSlotValue(attributeComparison.getattribute_to_compareValue());
					if (compareFilter.value == null) {
						logger.error(instanceToCompare.getBrowserText()+"("+instanceToCompare.getName()+") has null value for the attribute "+attributeComparison.getattribute_to_compareValue().getName());
						existMatch= false;
						break;
					}
				}
//				logger.debug(compareFilter.attribute + " "+ compareFilter.comparison_operator+" "+ compareFilter.value.toString());
				if (negatedComparisonOperator(attributeComparison.getoperatorValue())) {
					compareFilter.comparison_operator = getNegatedComparisonOperator(attributeComparison.getoperatorValue());
					result = negatedFilter.evaluate(instance);
				} else {
					compareFilter.comparison_operator = attributeComparison.getoperatorValue();
					result = compareFilter.evaluate(instance, guidelineManager);
				}
				logger.debug("result: "+result);
				if (!result) {
					existMatch = false;
					break;
				}
			}
			if (existMatch) {
				return true;
			}
		}
		return false;
	}
	
	private boolean negatedComparisonOperator(String operator) {
		return operator.equals(DharmaPaddaConstants.not_subclass_of) || operator.equals(DharmaPaddaConstants.not_member_of);
	}

	private String getNegatedComparisonOperator(String operator) {
		if (operator.equals(DharmaPaddaConstants.not_subclass_of))
			return DharmaPaddaConstants.subclass_of;
		else if (operator.equals(DharmaPaddaConstants.not_member_of))
			return DharmaPaddaConstants.member_of;
		else
			return null;
	}
}

