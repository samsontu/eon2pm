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
import org.apache.log4j.*;
/** 
 */
public class Qualitative_Term_Query extends EPR_Query {

	public Qualitative_Term_Query(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
  static  Logger  logger = Logger.getLogger(Qualitative_Term_Query.class);


	public void setperiodValue(Instance period) {
		ModelUtilities.setOwnSlotValue(this, "period", period);	}
	public Instance getperiodValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "period"));
	}

	public void setaggregation_operatorValue(String aggregation_operator) {
		ModelUtilities.setOwnSlotValue(this, "aggregation_operator", aggregation_operator);	}
	public String getaggregation_operatorValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "aggregation_operator"));
	}

	public void setqualitative_domain_termValue(String qualitative_domain_term) {
		ModelUtilities.setOwnSlotValue(this, "qualitative_domain_term", qualitative_domain_term);	}
	public String getqualitative_domain_termValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "qualitative_domain_term"));
	}
// __Code above is automatically generated. Do not change

  public edu.stanford.smi.eon.datahandler.DataElement doQuery(
    GuidelineInterpreter glmanager) {

    KBHandler kbManager = glmanager.getKBmanager();
    edu.stanford.smi.eon.datahandler.DataHandler dataManager = glmanager.getDBmanager();
    String returnValueString="null";
    edu.stanford.smi.eon.datahandler.DataElement returnValue = null;
    try {
      returnValue = dataManager.doQualitativeQuery(kbManager, getqualitative_domain_termValue(),
        getaggregation_operatorValue(), getperiodValue());
    } catch (PCA_Session_Exception e) {
      logger.error(e.getMessage());
    }
    if (returnValue != null) returnValueString=returnValue.domain_term + ":"+returnValue.value;
    logger.debug("Qualitative_Term_Query: "+" "+
      getqualitative_domain_termValue()+" " +getaggregation_operatorValue() + " "+
      getperiodValue()+" return value: "+returnValueString);
    return returnValue;
  }
  
	public Expression ownEvaluateExpression(GuidelineInterpreter glmanager)
	throws PCA_Session_Exception {
		edu.stanford.smi.eon.datahandler.DataElement queryResult = doQuery(glmanager);
		Qualitative_Constant result = (Qualitative_Constant)glmanager.getDBmanager().createRegisteredInstance(
		"Qualitative_Constant");
		result.setvalueValue(queryResult.domain_term);
		return result;
		
	}

}
