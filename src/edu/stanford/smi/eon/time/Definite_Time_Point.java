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
import edu.stanford.smi.eon.criterion.Expression;
import edu.stanford.smi.eon.datahandler.DataHandler;
import edu.stanford.smi.eon.guidelineinterpreter.GuidelineInterpreter;


/** 
 */
public class Definite_Time_Point extends Absolute_Time_Point {

	public Definite_Time_Point(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
// __Code above is automatically generated. Do not change

  static Logger logger = Logger.getLogger(Definite_Time_Point.class);
/*  public Definite_Time_Point(int system_time) {
    super(KBHandler.kb, null);
    setsystem_timeValue(system_time);
    setDateValue(system_time);
  }
  public Definite_Time_Point(String time) {
    super(KBHandler.kb, null);
    setDateValue(time);
  }
*/
  public void setSlotsValues(String time) {
    if ((time != null ) && !(time.equals("")))
    	setDateValue(time);
  }
  public void setSlotsValues(String time, int sessionTime) {
	    if ((time != null ) && !(time.equals("")))
	    	setDateValue(time, sessionTime);
	  }
 public void setSlotsValues(int system_time) {
    setsystem_timeValue(system_time);
    setDateValue(system_time);
 }
 
 public void setSlotsValues(int system_time, int sessionTime) {
	    setsystem_timeValue(system_time);
	    setDateValue(system_time, sessionTime);
	 }
 public boolean before(Definite_Time_Point timeToCompare) {
  logger.debug(getsystem_timeValue()+ " before " + timeToCompare.getsystem_timeValue()+" ?");
  return (getsystem_timeValue()  < timeToCompare.getsystem_timeValue());
 }
 public boolean beforeOrEqual(Definite_Time_Point timeToCompare) {
  logger.debug(getsystem_timeValue()+ " before or equal " + timeToCompare.getsystem_timeValue()+" ?");
  return ((getsystem_timeValue()  < timeToCompare.getsystem_timeValue()) || 
		  (getsystem_timeValue()  == timeToCompare.getsystem_timeValue()));
 }
 public boolean afterOrEqual(Definite_Time_Point timeToCompare) {
  logger.debug(getsystem_timeValue()+ " after or equal " + timeToCompare.getsystem_timeValue()+" ?");
  return (timeToCompare.beforeOrEqual(this));
 }

 public boolean after(Definite_Time_Point timeToCompare) {
  return (timeToCompare.before(this));
 }

 public Expression subtract(Duration duration, GuidelineInterpreter glManager) {
	Definite_Time_Point result = null;
	int newsystemTime = 0;
	if (duration.gettime_unitValue() != null ) {
		newsystemTime = (int)(getsystem_timeValue() - duration.getDuration(getSystemTimeUnit()));
		result = (Definite_Time_Point)glManager.getDBmanager().createInstance("Definite_Time_Point");
		result.setSlotsValues(newsystemTime);
	} else
		logger.error("Duration instance "+ duration.getBrowserText()+" does not have a time unit!");
	return result;
	
 }
}
