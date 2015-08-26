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
public class Temporal_Criterion extends Criterion {

	public Temporal_Criterion(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
  static  Logger  logger = Logger.getLogger(Temporal_Criterion.class);


	public void setpresenceValue(boolean presence) {
		ModelUtilities.setOwnSlotValue(this, "presence", new  Boolean(presence));	}
	public boolean ispresenceValue() {
		if (ModelUtilities.getOwnSlotValue(this, "presence") == null) return false;
		else 
		return ((Boolean) ModelUtilities.getOwnSlotValue(this, "presence")).booleanValue();
	}

	public void setwhenValue(String when) {
		ModelUtilities.setOwnSlotValue(this, "when", when);	}
	public String getwhenValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "when_condition"));
	}

	public void setfromValue(String from) {
		ModelUtilities.setOwnSlotValue(this, "from", from);	}
	public String getfromValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "from"));
	}

	public void setwhereValue(String where) {
		ModelUtilities.setOwnSlotValue(this, "where", where);	}
	public String getwhereValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "where_condition"));
	}

	public void setcontextValue(String context) {
		ModelUtilities.setOwnSlotValue(this, "context", context);	}
	public String getcontextValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "context"));
	}

	public void setselectValue(String select) {
		ModelUtilities.setOwnSlotValue(this, "select", select);	}
	public String getselectValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "select"));
	}
// __Code above is automatically generated. Do not change



    public Criteria_Evaluation ownEvaluate(GuidelineInterpreter guidelineManager, boolean doAll)
    throws PCA_Session_Exception {
    	
    return	new Criteria_Evaluation(Logical_Operator.ATOMIC,
    		       Truth_Value._false,
    		      new Criteria_Evaluation[0],
    		      this.makeGuideline_Entity(),
    		      getlabelValue());
    }
    /* Try query first.
    boolean presence = false;
    Criteria_Evaluation evaluation = null;
    if ((getselectValue()== null)  || (getfromValue() == null))
      throw new PCA_Session_Exception("null select or from clause in "+getName() +
        " "+getlabelValue()); 

    logger.debug("Temporal_Criterion ownEvaluate - "+ getlabelValue()+ "construct temporal query");

    Temporal_Query criterionQuery = (Temporal_Query) guidelineManager.getDBmanager().createInstance("Temporal_Query");
    String query = "temporal select "+getselectValue()+" from "+getfromValue();
    if (getwhereValue() != null) query = query+" where "+getwhereValue();
    if (getwhenValue() != null) query = query+" when "+getwhenValue();
    criterionQuery.setqueryValue(query);
    logger.debug("Temporal_Criterion ownEvaluate query= "+query);
    int queryResult = criterionQuery.doQuery(guidelineManager);
    guidelineManager.getDBmanager().deleteInstance(criterionQuery);
    if (queryResult == 0) presence = false;
    else presence = true;
    evaluation = new Criteria_Evaluation(Logical_Operator.ATOMIC,
      (ispresenceValue()) ? ((presence) ? Truth_Value._true : Truth_Value._false)
         : ((!presence) ? Truth_Value._true : Truth_Value._false),
      new Criteria_Evaluation[0],
      this.makeGuideline_Entity(),
      getlabelValue());
    return evaluation;
  } */
}
