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

// Created on Tue May 22 14:50:56 PDT 2001
// Copyright Stanford University 2000

package edu.stanford.smi.eon.time;

import java.util.*;

import org.apache.log4j.Logger;

import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.util.*;
import edu.stanford.smi.eon.util.*;
import edu.stanford.smi.eon.kbhandler.*;
import edu.stanford.smi.eon.datahandler.DataHandler;


/** 
 *  Relative and absolute time point
 */
public abstract class Abstract_Time_Point extends Time_Entity {

	public Abstract_Time_Point(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
	static Logger logger = Logger.getLogger(Abstract_Time_Point.class);

	public void setdays_from_current_timeValue(int days_from_current_time) {
		ModelUtilities.setOwnSlotValue(this, "days_from_current_time", new  Integer(days_from_current_time));	}
	public int getdays_from_current_timeValue() {
		if (ModelUtilities.getOwnSlotValue(this, "days_from_current_time") != null)
			return ((Integer) ModelUtilities.getOwnSlotValue(this, "days_from_current_time")).intValue();
		else {
			logger.error("No days_from_current_timevalue in "+getlabelValue());
			return 0;
		}
	}
// __Code above is automatically generated. Do not change
}
