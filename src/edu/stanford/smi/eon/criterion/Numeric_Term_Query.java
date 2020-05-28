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
public class Numeric_Term_Query extends EPR_Query {

	public Numeric_Term_Query(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}

  static  Logger  logger = Logger.getLogger(Numeric_Term_Query.class);

	public void setmoodValue(Cls mood) {
		ModelUtilities.setOwnSlotValue(this, "mood", mood);	}
	public Cls getmoodValue() {
		return ((Cls) ModelUtilities.getOwnSlotValue(this, "mood"));
	}

	public void setperiodValue(Instance period) {
		ModelUtilities.setOwnSlotValue(this, "period", period);	}
	public Instance getperiodValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "period"));
	}

	public void setentry_typeValue(Cls entry_type) {
		ModelUtilities.setOwnSlotValue(this, "entry_type", entry_type);	}
	public Cls getentry_typeValue() {
		return ((Cls) ModelUtilities.getOwnSlotValue(this, "entry_type"));
	}

	public void setaggregation_operatorValue(String aggregation_operator) {
		ModelUtilities.setOwnSlotValue(this, "aggregation_operator", aggregation_operator);	}
	public String getaggregation_operatorValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "aggregation_operator"));
	}

	public void setnumeric_domain_termValue(String numeric_domain_term) {
		ModelUtilities.setOwnSlotValue(this, "numeric_domain_term", numeric_domain_term);	}
	public String getnumeric_domain_termValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "numeric_domain_term"));
	}
// __Code above is automatically generated. Do not change

  public edu.stanford.smi.eon.datahandler.DataElement doQuery(
    GuidelineInterpreter guidelineManager) {
    
    KBHandler kbManager= guidelineManager.getKBmanager();
    edu.stanford.smi.eon.datahandler.DataHandler dataManager = guidelineManager.getDBmanager()  ;
    String entryType = getentry_typeValue().getName();
    String returnValueString="null";
    edu.stanford.smi.eon.datahandler.DataElement returnValue = null;
    try {
      returnValue = dataManager.doNumericQuery(guidelineManager, getnumeric_domain_termValue(),
        getaggregation_operatorValue(), getperiodValue(), entryType, getmoodValue());
    } catch (PCA_Session_Exception e) {
      logger.error(e.getMessage()+" "+getaggregation_operatorValue()+ " "+
    		  getnumeric_domain_termValue()+ ";");
    }
    if (returnValue != null) returnValueString=returnValue.toString();
    logger.debug("Numeric_Term_Query: "+getentry_typeValue().getName()+" "+
      getnumeric_domain_termValue()+" " +getaggregation_operatorValue() + " "+
      ((getperiodValue() != null)? getperiodValue().getBrowserText(): "no period")+" return value: "+returnValueString);
    return returnValue;
  }
  public Expression evaluate_expression(GuidelineInterpreter glmanager)
      throws PCA_Session_Exception {
    edu.stanford.smi.eon.datahandler.DataElement data = doQuery(glmanager);
    Numeric_Constant result = null;
    float queryResult = 0;

    if (data != null) {
      try {
        queryResult = Float.parseFloat(data.value);
        result = (Numeric_Constant) glmanager.getDBmanager().createRegisteredInstance("Numeric_Constant");
        result.setvalueValue(queryResult);
      } catch (Exception e) {
        logger.error("Numeric_Term_Query.evaluate: Cannot parse doQuery data value "+
          data.value +" as float");
        return null;
      }
    }
    return result;

  }
}
