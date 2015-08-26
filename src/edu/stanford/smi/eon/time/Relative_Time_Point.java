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

//Created on Tue May 22 14:50:56 PDT 2001
//Copyright Stanford University 2000

package edu.stanford.smi.eon.time;

import java.util.*;

import org.apache.log4j.Logger;

import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.util.*;
import edu.stanford.smi.eon.util.*;
import edu.stanford.smi.eon.kbhandler.*;
import edu.stanford.smi.eon.criterion.Date_Expression;
import edu.stanford.smi.eon.criterion.Expression;
import edu.stanford.smi.eon.criterion.Set_Expression;
import edu.stanford.smi.eon.datahandler.DataHandler;
import edu.stanford.smi.eon.guidelineinterpreter.GuidelineInterpreter;


/** 
 *  e.g. 'Today' or 'now' is a relative time point.
 */
public class Relative_Time_Point extends Abstract_Time_Point {

	public Relative_Time_Point(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}

	public void setpolarityValue(String polarity) {
		ModelUtilities.setOwnSlotValue(this, "polarity", polarity);	}
	public String getpolarityValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "polarity"));
	}

	public void sethow_manyValue(String how_many) {
		ModelUtilities.setOwnSlotValue(this, "how_many", how_many);	}
	public String gethow_manyValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "how_many"));
	}

	public void settime_unitValue(String time_unit) {
		ModelUtilities.setOwnSlotValue(this, "time_unit", time_unit);	}
	public String gettime_unitValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "time_unit"));
	}


	//	__Code above is automatically generated. Do not change

	static  Logger  logger = Logger.getLogger(Relative_Time_Point.class);
	public Expression evaluate_expression(GuidelineInterpreter glmanager) {
		return evaluate_expression(glmanager.getDBmanager());
	}
	public Expression evaluate_expression(DataHandler dataHandler) {
		Definite_Time_Point date = (Definite_Time_Point)dataHandler.createInstance(
				"Definite_Time_Point");
		date.setSlotsValues(dataHandler.getSessionTime());
		try {
			String label = getlabelValue();
			if (label.equals("Today")) {
				return date;
			} else if (getdays_from_current_timeValue() != 0) {
				int daysFromCurrentTime = getdays_from_current_timeValue();
				int  timePoint ;
				if (getpolarityValue().equals("After")) timePoint = ((Definite_Time_Point)date).getsystem_timeValue() + daysFromCurrentTime;
				else timePoint = ((Definite_Time_Point)date).getsystem_timeValue() - daysFromCurrentTime;
				date.setSlotsValues(timePoint);
				return date;
			} else {
				logger.error("Relative_Time_Point only implements days from current time right now");
				return null;
			}
		} catch (Exception e) {
			logger.error("Relative_Time_Point.evaluate_expression exception: "+e.getMessage(), e);

			return null;
		}
	}

}
