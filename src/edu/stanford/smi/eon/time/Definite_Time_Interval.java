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
import edu.stanford.smi.eon.datahandler.*;
import org.apache.log4j.*;

/** 
 */
public class Definite_Time_Interval extends Absolute_Time_Interval {

	public Definite_Time_Interval(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
	static Logger logger = Logger.getLogger(Definite_Time_Interval.class);
	public void settime_pointValue(Instance time_point) {
		ModelUtilities.setOwnSlotValue(this, "time_point", time_point);	}
	public Instance gettime_pointValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "time_point"));
	}
	public void setstart_timeValue(Instance time_point) {
		ModelUtilities.setOwnSlotValue(this, "start_time", time_point);	}
	public Instance getstart_timeValue() {
		Instance startTime = (Instance)  ModelUtilities.getOwnSlotValue(this, "start_time");
		if (startTime == null) {
			if (getpolarityValue() != null) {
				if (getpolarityValue().equals("Before"))
					startTime = otherEndPoint;
				else
					startTime = gettime_pointValue();
			}
		}
		return startTime;
	}
	public void setstop_timeValue(Instance time_point) {
		ModelUtilities.setOwnSlotValue(this, "stop_time", time_point);	}
	public Instance getstop_timeValue() {
		Instance stopTime = (Instance)  ModelUtilities.getOwnSlotValue(this, "stop_time");
		if (stopTime == null) {
			if (getpolarityValue() != null) {
				if (getpolarityValue().equals("After"))
					stopTime = otherEndPoint;
				else
					stopTime = gettime_pointValue();
			}
		}
		return stopTime;
	}
	// __Code above is automatically generated. Do not change
	private Definite_Time_Point otherEndPoint;



	/*public Definite_Time_Interval(String time_unit, String howMany, String label,
    String polarity, Definite_Time_Point timePoint, String timeUnit) throws Exception {

    super(KBHandler.kb, null);
    int lengthInDays = 0;
    //logger.debug("Definite_Time_Interval constructor - granularity: "+ granularity+" howMany: "+howMany+
    //  " label: "+label+" polarity: "+polarity);
    settime_unitValue(time_unit);
    sethow_manyValue(howMany);
    setlabelValue(label);
    setpolarityValue(polarity);
    settime_pointValue(timePoint);

    if (timeUnit != null) {
      settime_unitValue(timeUnit);
      if (howMany.equals("*")) lengthInDays = Integer.MAX_VALUE;
      else
        if (timeUnit.equals("day")) {
          lengthInDays = Integer.parseInt(howMany);
        } else if (timeUnit.equals("week")) {
            lengthInDays = 7 * Integer.parseInt(howMany);
          } else throw new Exception("time_unit "+timeUnit+" in "+this.getName()+" is not a legal value");
    } else throw new Exception("time_unit is not specificed in "+this.getName());

    if (getpolarityValue().equals("Before")) {
      otherEndPoint = (Definite_Time_Point)KBHandler.createKBInstance("Definite_Time_Point");
      otherEndPoint.setSlotsValues(((Definite_Time_Point)gettime_pointValue()).getsystem_timeValue()- lengthInDays);
    } else if (getpolarityValue().equals("After")) {
      otherEndPoint = (Definite_Time_Point)KBHandler.createKBInstance("Definite_Time_Point");
      otherEndPoint.setSlotsValues(((Definite_Time_Point)gettime_pointValue()).getsystem_timeValue()+ lengthInDays);
    } else throw new Exception("No valuid polarity in "+this.getName());
  }
	 */
	public void setSlotsValues(String time_unit, String howMany, String label,
			String polarity, Definite_Time_Point timePoint, String timeUnit,
			DataHandler dbManager) throws Exception {
		int lengthInDays = 0;

		//logger.debug("Definite_Time_Interval constructor - granularity: "+ granularity+" howMany: "+howMany+
		//  " label: "+label+" polarity: "+polarity);
		settime_unitValue(time_unit);
		sethow_manyValue(howMany);
		setlabelValue(label);
		setpolarityValue(polarity);
		settime_pointValue(timePoint);

		if (timeUnit != null) {
			settime_unitValue(timeUnit);
			if (howMany.equals("*")) lengthInDays = Integer.MAX_VALUE;
			else
				if (timeUnit.equals("day")) {
					lengthInDays = Integer.parseInt(howMany);
				} else if (timeUnit.equals("week")) {
					lengthInDays = 7 * Integer.parseInt(howMany);
				} else if (timeUnit.equals("year")){
					lengthInDays = 365 * Integer.parseInt(howMany);
				} else if (timeUnit.equals("month")) {
					lengthInDays = (int)(Math.round((365.0 /12.0) * Double.parseDouble(howMany)));
				} else throw new Exception("time_unit "+timeUnit+" in "+this.getName()+" is not a legal value");
		} else throw new Exception("time_unit is not specificed in "+this.getName());
		int systemTimeValue = ((Definite_Time_Point)gettime_pointValue()).getsystem_timeValue();
		if (getpolarityValue().equals("Before")) {
			setstop_timeValue(timePoint);
			otherEndPoint = (Definite_Time_Point)dbManager.createRegisteredInstance("Definite_Time_Point");
			otherEndPoint.setSlotsValues(systemTimeValue- lengthInDays);
			setstart_timeValue(otherEndPoint);
		} else if (getpolarityValue().equals("After")) {
			setstart_timeValue(timePoint);
			otherEndPoint = (Definite_Time_Point)dbManager.createRegisteredInstance("Definite_Time_Point");
			otherEndPoint.setSlotsValues(systemTimeValue+ lengthInDays); 
			setstop_timeValue(otherEndPoint);
		} else throw new Exception("No valid polarity in "+this.getName());

	}

	public void setSlotsValues(String label,
			String startTime, String stopTime, DataHandler dbManager) throws Exception {
		int lengthInDays = 0;

		if ((startTime == null) && (stopTime == null)) {
			logger.warn("Definite time interval "+label+" has null start and stop times");
		}
		//logger.debug("Definite_Time_Interval constructor - granularity: "+ granularity+" howMany: "+howMany+
		//  " label: "+label+" polarity: "+polarity);
		Definite_Time_Point startTimeInst = (startTime != null)? (Definite_Time_Point)dbManager.createRegisteredInstance("Definite_Time_Point"): null;
		Definite_Time_Point stopTimeInst = (stopTime != null)? (Definite_Time_Point)dbManager.createRegisteredInstance("Definite_Time_Point"): null;
		if (startTime != null) {
			startTimeInst.setSlotsValues(startTime);
			setstart_timeValue(startTimeInst);
		}
		if ((stopTime != null)&& !(stopTime.equals(""))) {
			stopTimeInst.setSlotsValues(stopTime);
			setstop_timeValue(stopTimeInst);
		}
		if ((startTime != null) && (stopTime != null) && !(stopTime.equals("")))
			lengthInDays = stopTimeInst.getsystem_timeValue() - startTimeInst.getsystem_timeValue();
		else lengthInDays = Integer.MAX_VALUE;
		settime_unitValue("day");
		sethow_manyValue(Integer.toString(lengthInDays));
		setlabelValue(label);
		if (startTime != null) {
			setpolarityValue("After");
			settime_pointValue(startTimeInst);
			otherEndPoint = stopTimeInst;
		} else if (stopTime != null) {
			setpolarityValue("Before");
			settime_pointValue(stopTimeInst);
		}
	}


	public boolean within(Definite_Time_Point timePoint) throws Exception {
		if (this.getstart_timeValue() != null) {
			if (this.getstop_timeValue() != null) {
				return (((Definite_Time_Point)getstart_timeValue()).beforeOrEqual(timePoint) &&
						((Definite_Time_Point)getstop_timeValue()).afterOrEqual(timePoint));
			} else {
				return ((Definite_Time_Point)getstart_timeValue()).beforeOrEqual(timePoint);
			}
		} else {
			if (getpolarityValue().equals("Before"))
				return (otherEndPoint.beforeOrEqual(timePoint)  && ((Definite_Time_Point)gettime_pointValue()).afterOrEqual(timePoint));
			else if (getpolarityValue().equals("After"))
				return (otherEndPoint.afterOrEqual(timePoint)  && ((Definite_Time_Point)gettime_pointValue()).beforeOrEqual(timePoint));
			else throw new Exception("No valuid polarity in "+this.getName());
		}
	}

	public Integer getHigh() {
		Instance stopTime = getstop_timeValue();
		if (stopTime != null) {
			return new Integer(((Definite_Time_Point)stopTime).getsystem_timeValue());
		}
		Integer high = null;
		if (getpolarityValue().equals("Before"))
			high = new Integer(((Definite_Time_Point)gettime_pointValue()).getsystem_timeValue());
		else high = new Integer(otherEndPoint.getsystem_timeValue());
		logger.debug("Definite_Time_Interval.getHigh() high="+high.toString());
		return high;
	}

	public Integer getLow() {
		Instance startTime = getstart_timeValue();
		if (startTime != null) {
			return new Integer(((Definite_Time_Point)startTime).getsystem_timeValue());
		}
		Integer low = null;
		if (getpolarityValue().equals("After"))
			low = new Integer(((Definite_Time_Point)gettime_pointValue()).getsystem_timeValue());
		else low = new Integer(otherEndPoint.getsystem_timeValue());
		logger.debug("Definite_Time_Interval.getLow() low="+low.toString());
		return low;
	}
}
