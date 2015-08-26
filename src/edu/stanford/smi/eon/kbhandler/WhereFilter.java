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
import edu.stanford.smi.eon.guidelineinterpreter.*;
import edu.stanford.smi.protege.model.*;

import org.apache.log4j.*;

public class WhereFilter  implements AbstractWhereFilter {
	protected String  aggregation_operator;
	protected Collection children;
	static Logger logger = Logger.getLogger(WhereFilter.class);

	public WhereFilter(String aggregation_operator, Collection children) {
		this.aggregation_operator = aggregation_operator;
		this.children = children;
	}

	public boolean evaluate (Instance instance) throws KnowledgeBaseException {
		return evaluate(instance, null);
	}
	public boolean evaluate(Instance instance, GuidelineInterpreter gi) throws KnowledgeBaseException {
		boolean result= false;
		if (aggregation_operator != null) {
			if (instance == null) logger.error("WhereFilter.evaluate: null instance argument");
			else logger.debug("WhereFilter.evaluate: instance argument = "+
					instance.getBrowserText()+"/"+instance.getName());
			if ((children == null)  || children.isEmpty()) {
				logger.warn("Null or empty children in WhereFilter");
				return true;
			}
			if (aggregation_operator.equals(DharmaPaddaConstants.AND)) {
				result = true;
				for (Iterator i=children.iterator(); i.hasNext();) {
					AbstractWhereFilter filter = (AbstractWhereFilter)i.next();
					if (filter == null) logger.error("WhereFilter.evaluate: null filter variable!!!");
					else {
						if (!filter.evaluate(instance, gi)) {
							result = false;
							return result;
						}
					}
				}
			} else
				if (aggregation_operator.equals(DharmaPaddaConstants.OR)) {
					result = false;
					for (Iterator i=children.iterator(); i.hasNext();) {
						AbstractWhereFilter filter = (AbstractWhereFilter)i.next();
						if (filter == null) logger.error("WhereFilter.evaluate: null filter variable!!!");
						else {
							if (filter.evaluate(instance, gi)) {
								result = true;
								return result;
							}
						}
					}

				} else 
					if (aggregation_operator.equals(DharmaPaddaConstants.NOT))
						for (AbstractWhereFilter filter : (Collection<AbstractWhereFilter>)children) {
							if (filter.evaluate(instance, gi))
								return false;
							else
								return true;
						}
					else
						throw new KnowledgeBaseException(aggregation_operator + " is not a legal where aggregator");
		} else throw new KnowledgeBaseException("No where aggregator operator");
		return result;
	}

}
