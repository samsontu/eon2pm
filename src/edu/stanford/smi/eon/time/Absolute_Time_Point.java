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

import java.text.ParseException;

import edu.stanford.smi.eon.util.Day;
import edu.stanford.smi.eon.util.HelperFunctions;
import edu.stanford.smi.protege.model.*;

import org.apache.log4j.*;


/** 
 *  ;******************* Definition of time ************ Many models of time are possible. In Amar Das's model, an 'event' (e.g., a measurment of platelets) has an interval of uncertainty, where the bounds of the interval are calendate date-time. For simplicity, we define here a model of time in which time is an integer representing number of time units (default to days) after a reference time point. 
 */
public abstract class Absolute_Time_Point extends Abstract_Time_Point {

	public Absolute_Time_Point(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}

	static Logger logger = Logger.getLogger(Absolute_Time_Point.class);

	public void setmonth_nameValue(int month_name) {
		ModelUtilities.setOwnSlotValue(this, "month_name", new  Integer(month_name));	}
	public int getmonth_nameValue() {
		return ((Integer) ModelUtilities.getOwnSlotValue(this, "month_name")).intValue();
	}

	public void setsystem_timeValue(int system_time) {
		ModelUtilities.setOwnSlotValue(this, "system_time", new  Integer(system_time));	}
	public int getsystem_timeValue() {
		if (ModelUtilities.getOwnSlotValue(this, "system_time") != null) {
			return ((Integer) ModelUtilities.getOwnSlotValue(this, "system_time")).intValue();
		} else
			return 0;
	}

	public void setdayValue(int day) {
		ModelUtilities.setOwnSlotValue(this, "day", new  Integer(day));	}
	public int getdayValue() {
		return ((Integer) ModelUtilities.getOwnSlotValue(this, "day")).intValue();
	}

	public void setyear_CEValue(int year_CE) {
		ModelUtilities.setOwnSlotValue(this, "year_CE", new  Integer(year_CE));	}
	public int getyear_CEValue() {
		Integer yearCE = ((Integer) ModelUtilities.getOwnSlotValue(this, "year_CE"));
		if (yearCE != null)
			return yearCE.intValue();
		else {
			logger.error(this.getBrowserText()+" has null year CE");
			return 0;
		}
	}
	// __Code above is automatically generated. Do not change


	public void setDateValue(String date) {

		try {
			setsystem_timeValue((int) HelperFunctions.Day2Int2(date));
		} catch (ParseException e) {
			logger.error("Cannot parse '" + date +"' ");
			e.printStackTrace();
		}

		int firstdash = date.indexOf("-", 0);
		int seconddash = date.indexOf("-", firstdash+1);
		int firstblank = date.indexOf(" ",0);
		int limit = date.length();
		if (firstblank > 0) limit = firstblank;
		setlabelValue(date);
		setyear_CEValue(Integer.parseInt(date.substring(0, firstdash)));
		setdayValue(Integer.parseInt(date.substring(seconddash+1, limit )));
		setmonth_nameValue(Integer.parseInt(date.substring(firstdash+1, seconddash)));
	}


	/*  public void setDateValue(String date, String granule){
	  setDateValue(date);
	  if (granule == edu.stanford.smi.eon.datahandler.Constants.minute) {
		  setsystem_timeValue((int)HelperFunctions.Day2Int2(date) * 60 * 24);
	  }
  }
	 */
	public void setDateValue(int julianDay) {
		Day dayutil = new Day();
		dayutil.fromJulian(julianDay);
		setdayValue( dayutil.getDay());
		setyear_CEValue(dayutil.getYear());
		setsystem_timeValue(julianDay);
		setmonth_nameValue( dayutil.getMonth());
		setlabelValue(dayutil.toString());
		logger.debug("in Absolute_Time_Point: setDateValue "+ dayutil.toString());
	}

	public void setDateValue(String date, int sessionTime) {
		try {
			int systemTime = (int) HelperFunctions.Day2Int2(date); 
			this.setsystem_timeValue(systemTime);
			this.setdays_from_current_timeValue(sessionTime - systemTime);
		} catch (ParseException e) {
			logger.error("Cannot parse '" + date +"' ");
			e.printStackTrace();
		}

		int firstdash = date.indexOf("-", 0);
		int seconddash = date.indexOf("-", firstdash+1);
		int firstblank = date.indexOf(" ",0);
		int limit = date.length();
		if (firstblank > 0) limit = firstblank;
		setlabelValue(date);
		setyear_CEValue(Integer.parseInt(date.substring(0, firstdash)));
		setdayValue(Integer.parseInt(date.substring(seconddash+1, limit )));
		setmonth_nameValue(Integer.parseInt(date.substring(firstdash+1, seconddash)));
	}  

	public void setDateValue(int julianDay, int sessionTime) {
		Day dayutil = new Day();
		dayutil.fromJulian(julianDay);
		setdayValue( dayutil.getDay());
		setyear_CEValue(dayutil.getYear());
		setsystem_timeValue(julianDay);
		this.setdays_from_current_timeValue(sessionTime - julianDay);
		setmonth_nameValue( dayutil.getMonth());
		setlabelValue(dayutil.toString());
		//setlabelValue(dayutil.getMonth()+"/"+dayutil.getDay()+"/"+dayutil.getYear());
		logger.debug("in Absolute_Time_Point: setDateValue "+ dayutil.toString());
	}

	public static String getSystemTimeUnit() {
		return "day";
	}

	public String toString() {
		return getlabelValue();
	}


}
