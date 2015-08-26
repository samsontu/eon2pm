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
public class Presence_Query extends EPR_Query {
	
	public Presence_Query(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
	
  static  Logger  logger = Logger.getLogger(Presence_Query.class);
  public void setfilterValue(Instance filter) {
		ModelUtilities.setOwnSlotValue(this, "filter", filter);	}
	public Instance getfilterValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "filter"));
	}
	
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
	
	public void setqualitative_domain_termValue(String qualitative_domain_term) {
		ModelUtilities.setOwnSlotValue(this, "qualitative_domain_term", qualitative_domain_term);	}
	public String getqualitative_domain_termValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "qualitative_domain_term"));
	}
//	__Code above is automatically generated. Do not change
	
	public String doQuery(GuidelineInterpreter guidelineManager) {
		
		edu.stanford.smi.eon.datahandler.DataHandler dataManager= 
			guidelineManager.getDBmanager();
		String entryType = getentry_typeValue().getName();
		String returnValue="false";
		Matched_Data presenceData;
		Instance filter=null;
		
		logger.debug("in Presence_Query.doQuery() - " + getqualitative_domain_termValue() +
				"(entryType: "+ entryType +", period: "+ this.getperiodValue()+")");
		if (entryType != null) {
			if (entryType.equals("Medication"))  {
				if ((presenceData =
					guidelineManager.containSubclassOf( entryType,
							getqualitative_domain_termValue(),
							dataManager.currentActivities("Medication", getmoodValue()))
				) != null) {
					if ((filter = getfilterValue()) !=null) {
						try {
							logger.debug("Presence_Query doQuery: "+ getlabelValue()+ " "+ presenceData.guideline_term
									+ " filter "+ ((Filter)filter).getlabelValue());
							if (((Filter)filter).evaluate(presenceData, guidelineManager) != null){
								returnValue = "true";
								logger.debug("Presence_Query doQuery: "+ getlabelValue()+ " "+ presenceData.guideline_term
										+ " filter "+ ((Filter)filter).getlabelValue()+" ("+ returnValue+")");
								
							} else {
								returnValue = "false";
								logger.debug("Presence_Query doQuery: "+ getlabelValue()+" "+presenceData.guideline_term
										+ " filter "+ ((Filter)filter).getlabelValue()+" ("+  returnValue+")");
							}
						} catch (Exception ex){
							returnValue = "false";
							logger.error("Presence_Query: doQuery - Exception evaluating filter " + ex.getMessage(), ex);
							
						}
					} else returnValue = "true";
				} else returnValue = "false";
			} else {
				if ((entryType.equals("Problem_List_Entry")) || entryType.equals("Note_Entry")) {
					Collection currentProblems = null;
					Instance timeInterval = this.getperiodValue();
					if (timeInterval != null)
						if (timeInterval instanceof Relative_Time_Interval) {
						currentProblems=dataManager.currentProblems((Relative_Time_Interval)timeInterval);
						} else if (timeInterval instanceof Definite_Time_Interval) {
							currentProblems = dataManager.currentProblems((Definite_Time_Interval)timeInterval);
						} else {
							logger.error(timeInterval.getBrowserText()+" is not a relative or definite time interval");
							currentProblems =dataManager.currentProblems();
						}
					else currentProblems =dataManager.currentProblems();
					if ((presenceData = guidelineManager.containSubclassOf(entryType,
							getqualitative_domain_termValue(),
							currentProblems)) != null) {
						if ((filter = getfilterValue()) !=null) {
							logger.debug("Presence_Query doQuery: "+ getlabelValue()+ " "+ presenceData.guideline_term
									+ " filter "+ ((Filter)filter).getlabelValue());
							try {
								if (((Filter)filter).evaluate(presenceData, guidelineManager) != null){
									returnValue = "true";
									logger.debug("Presence_Query doQuery: "+ getlabelValue()+ " "+ presenceData.guideline_term
											+ " filter "+ ((Filter)filter).getlabelValue()+" ("+ returnValue+")");
									
								} else {
									returnValue = "false";
									logger.debug("Presence_Query doQuery: "+ getlabelValue()+" "+presenceData.guideline_term
											+ " filter "+ ((Filter)filter).getlabelValue()+" ("+  returnValue+")");
								}
							} catch (Exception ex){
								returnValue = "false";
								logger.error("Presence_Query: doQuery - Exception evaluating filter " + ex.getMessage(), ex);
								
							}
						} else returnValue = "true";
						//  returnValue = "true";
					} else returnValue = "false";
				} else if (entryType.equals("Numeric_Entry")) {
					try {
						if (dataManager.doNumericQuery(guidelineManager, getqualitative_domain_termValue(),
								null, getperiodValue(), "Numeric_Entry", getmoodValue()) != null) returnValue = "true";
						else returnValue= "false";
					} catch (Exception e) {
						logger.error("Error in Presence_Query.doquery "+e.getMessage());
						returnValue = "false";
					}
				} else if (entryType.equals("Patient")) {
					if (dataManager.getDemographics(getqualitative_domain_termValue()) != null) {
						returnValue = "true";
					} else returnValue = "false";
				} else if (entryType.equals("Adverse_Reaction")) {
					Cls domainTerm = guidelineManager.getKBmanager().getCls(getqualitative_domain_termValue());
					if (domainTerm != null) {
						Collection queryResult = guidelineManager.matchAdverseEvents(domainTerm);
						if (!queryResult.isEmpty())
							return "true";
						else return "false";
					} else return "false";
				} else return "false";
			}
		} else {
			returnValue = "false";
		}
		logger.debug("in Presence_Query.doQuery() - " + getqualitative_domain_termValue() +
				"(entryType: "+ entryType +") "+ " returns " + returnValue);
		return returnValue;
		
	}
	
	public Expression ownEvaluateExpression(GuidelineInterpreter glmanager)
	throws PCA_Session_Exception {
		String queryResult = doQuery(glmanager);
		Qualitative_Constant result = (Qualitative_Constant)glmanager.getDBmanager().createInstance(
		"Qualitative_Constant");
		result.setvalueValue(queryResult);
		return result;
		
	}

}
