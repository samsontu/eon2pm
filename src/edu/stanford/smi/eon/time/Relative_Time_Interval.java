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

import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.util.*;
import edu.stanford.smi.eon.util.*;
import edu.stanford.smi.eon.kbhandler.*;
import edu.stanford.smi.eon.datahandler.DataHandler;
import org.apache.log4j.*;


/** 
 *  Interval of time defined by  a duration before or after an indefinite time point, e.g. 3 weeks before today
 */
public class Relative_Time_Interval extends Time_Interval {

	public Relative_Time_Interval(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
	static Logger logger = Logger.getLogger(Relative_Time_Interval.class);
	public void setrelative_time_pointValue(Instance relative_time_point) {
		ModelUtilities.setOwnSlotValue(this, "relative_time_point", relative_time_point);	}
	public Instance getrelative_time_pointValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "relative_time_point"));
	}

	public void setpolarityValue(String polarity) {
		ModelUtilities.setOwnSlotValue(this, "polarity", polarity);	}
	public String getpolarityValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "polarity"));
	}
	// __Code above is automatically generated. Do not change


	public Definite_Time_Interval resolveTime(String sessionTime, DataHandler dbmanager) {
		Definite_Time_Interval interval = null;
		KnowledgeBase kb = this.getKnowledgeBase();
		Relative_Time_Point anchor = (Relative_Time_Point)getrelative_time_pointValue();
		Definite_Time_Point current = null;
		if (anchor.getlabelValue().equals("Today")) {
			current = (Definite_Time_Point)dbmanager.createInstance("Definite_Time_Point");
			current.setSlotsValues(sessionTime);
		} else {
			current = (Definite_Time_Point)anchor.evaluate_expression(dbmanager);
		}
		/*      logger.error("Unknown relative time point: "+
        ((Relative_Time_Point)getrelative_time_pointValue()).getlabelValue());
		 */    
		try {
			interval = (Definite_Time_Interval)dbmanager.createInstance("Definite_Time_Interval");
			interval.setSlotsValues(gettime_unitValue(),
					gethow_manyValue(), getlabelValue(),
					getpolarityValue(), current, gettime_unitValue(), dbmanager);
		} catch (Exception e) {
			logger.error("Exception creating a Definite_Time_Interval", e);

		}
		return interval;
	}

}
