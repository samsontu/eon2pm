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

/** 
 *  A reified "where" clause that operate on instances from a single class
 */
public abstract class Filter extends DefaultSimpleInstance {

	public Filter(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
	public void setlabelValue(String label) {
		ModelUtilities.setOwnSlotValue(this, "label", label);	}
	public String getlabelValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "label"));
	}

	
// __Code above is automatically generated. Do not change

	public AbstractWhereFilter constructExpression( GuidelineInterpreter glmanager)
	    throws PCA_Session_Exception {
	    throw new PCA_Session_Exception("Filter "+getName()+ " is abstract");
	}
	
  public Collection evaluate(Matched_Data matches, GuidelineInterpreter glmanager)
    throws PCA_Session_Exception {
    throw new PCA_Session_Exception("Filter "+getName()+ " not implemented");

  }
}
