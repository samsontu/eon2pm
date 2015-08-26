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

// Created on Tue May 22 14:50:55 PDT 2001
// Copyright Stanford University 2000

package edu.stanford.smi.eon.time;

import java.util.*;
import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.util.*;
import edu.stanford.smi.eon.util.*;
import edu.stanford.smi.eon.kbhandler.*;
import edu.stanford.smi.eon.datahandler.DataHandler;
import edu.stanford.smi.eon.guidelineinterpreter.GuidelineInterpreter;
import edu.stanford.smi.eon.PCAServerModule.PCA_Session_Exception;
import edu.stanford.smi.eon.criterion.*;


/** 
 *  This is the toplevel class for temporal entities
 */
public class Time_Entity extends Expression {

	public Time_Entity(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}

	public void setlabelValue(String label) {
		ModelUtilities.setOwnSlotValue(this, "label", label);	}
	public String getlabelValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "label"));
	}
// __Code above is automatically generated. Do not change
	
  public Expression evaluate_expression(GuidelineInterpreter guidelineManager)
  throws PCA_Session_Exception {
    return this;
  }

}
