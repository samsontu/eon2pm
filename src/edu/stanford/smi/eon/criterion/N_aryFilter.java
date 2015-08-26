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

// Created on Wed Jun 20 12:58:01 PDT 2001
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
import org.apache.log4j.*;
/** 
 */
public class N_aryFilter extends  Filter {

	public N_aryFilter(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
  static  Logger  logger = Logger.getLogger(N_aryFilter.class);

	public void setoperatorValue(String operator) {
		ModelUtilities.setOwnSlotValue(this, "operator", operator);	}
	public String getoperatorValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "operator"));
	}

	public void setfiltersValue(Collection filters) {
		ModelUtilities.setOwnSlotValues(this, "filters", filters);	}
	public Collection getfiltersValue(){
		return  ModelUtilities.getOwnSlotValues(this, "filters");
	}
// __Code above is automatically generated. Do not change
	
	public AbstractWhereFilter  constructExpression(GuidelineInterpreter guidelineManager)
	throws PCA_Session_Exception {
		//logger.debug("N_aryFilter: );
		Collection restrictions = new ArrayList();
		if (getfiltersValue() == null) {
			logger.error("N_aryFilter.constructExpression: null filters slot!!!");
			throw new PCA_Session_Exception("Null filters in N_aryFilter");
		}
        for (Iterator i=getfiltersValue().iterator(); i.hasNext();) {
            Filter filter = (Filter)i.next();
            restrictions.add(filter.constructExpression(guidelineManager));
        }

		WhereFilter  wherefilter= 
			new WhereFilter(getoperatorValue(), restrictions);
		logger.debug("ComparisonFilter: constructExpression, returning WhereFilter"+
				getoperatorValue()+ " " + restrictions.toString());
		return wherefilter;
	}
	

}
