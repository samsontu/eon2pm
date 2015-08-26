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
import edu.stanford.smi.eon.PCAServerModule.PCA_Session_Exception;
import edu.stanford.smi.eon.criterion.Expression;
import edu.stanford.smi.eon.datahandler.DataHandler;
import edu.stanford.smi.eon.guidelineinterpreter.GuidelineInterpreter;


/** 
 */
public abstract class Duration extends Time_Entity {

	public Duration(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
  static Logger logger = Logger.getLogger(Duration.class);
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
// __Code above is automatically generated. Do not change
  
  public int getRoundedDuration(String time_unit) {
    float duration = getDuration(time_unit);
    if (duration > -1) {
      return Math.round(duration);
    } else return -1;
  }
  
  public Expression evaluate_expression(GuidelineInterpreter guidelineManager)
  throws PCA_Session_Exception {
    return this;
  }

  public float getDuration(String time_unit) {
    float duration = Long.parseLong(gethow_manyValue());
    String storedUnit = gettime_unitValue();
    if (time_unit == null) {
      return -1;
    }
    if (storedUnit == null) {
      return -1;
    }
    if (storedUnit.equals(time_unit)) {
      return duration;
    } else if (storedUnit.equals("second")){
      if (time_unit.equals("minute")){
        return duration / 60;
      } else if (time_unit.equals("hour")){
        return duration / 360;
      } else if (time_unit.equals("day")){
        return duration /(24 * 360);
      } else if (time_unit.equals("week")){
        return duration / (24 * 360 * 7);
        
      } else if (time_unit.equals("month")){
        return duration /(24 * 360  * 30);
      } else if (time_unit.equals("year")){
        return duration /(24 * 360  * (float)365.2425);
      } else {
        logger.debug("Duration.getDuration: " +time_unit+" is not a legal time unit");
        return -1;
      }
      
    } else if (storedUnit.equals("minute")){
      if (time_unit.equals("second")){
        return duration * 60;
      } else if (time_unit.equals("hour")){
        return duration / 60;
      } else if (time_unit.equals("day")){
        return duration /(24 * 60);
      } else if (time_unit.equals("week")){
        return duration / (24 * 60 * 7);
        
      } else if (time_unit.equals("month")){
        return duration /(24 * 60  * 30);
      } else if (time_unit.equals("year")){
        return duration /(24 * 60  * (float)365.2425);
      } else {
        logger.debug("Duration.getDuration: " +time_unit+" is not a legal time unit");
        return -1;
      }

    } else if (storedUnit.equals("hour")){
      if (time_unit.equals("minute")){
        return duration * 60;
      } else if (time_unit.equals("second")){
        return duration * 360;
      } else if (time_unit.equals("day")){
        return duration /(24 );
      } else if (time_unit.equals("week")){
        return duration / (24  * 7);
        
      } else if (time_unit.equals("month")){
        return duration /(24  * 30);
      } else if (time_unit.equals("year")){
        return duration /(24  * (float)365.2425);
      } else {
        logger.debug("Duration.getDuration: " +time_unit+" is not a legal time unit");
        return -1;
      }

    } else if (storedUnit.equals("day")){
      if (time_unit.equals("minute")){
        return duration * 24* 60;
      } else if (time_unit.equals("hour")){
        return duration *24;
      } else if (time_unit.equals("second")){
        return duration * (24 * 360);
      } else if (time_unit.equals("week")){
        return duration /  7;
        
      } else if (time_unit.equals("month")){
        return duration /( 30);
      } else if (time_unit.equals("year")){
        return duration /( (float)365.2425);
      } else {
        logger.debug("Duration.getDuration: " +time_unit+" is not a legal time unit");
        return -1;
      }

    } else if (storedUnit.equals("week")){
      if (time_unit.equals("minute")){
        return duration * 7*24* 60;
      } else if (time_unit.equals("hour")){
        return duration *7 * 24;
      } else if (time_unit.equals("day")){
        return duration * 7;
      } else if (time_unit.equals("second")){
        return duration * 24 * 360 * 7;
        
      } else if (time_unit.equals("month")){
        return duration * 7 / 30 ;
      } else if (time_unit.equals("year")){
        return duration * 7 / (float)365.2425;
      } else {
        logger.debug("Duration.getDuration: " +time_unit+" is not a legal time unit");
        return -1;
      }

    } else if (storedUnit.equals("month")){
      if (time_unit.equals("minute")){
        return duration * 30 * 24* 60;
      } else if (time_unit.equals("hour")){
        return duration * 30 * 24;
      } else if (time_unit.equals("day")){
        return duration * 30;
      } else if (time_unit.equals("week")){
        return duration * 30 /7;
        
      } else if (time_unit.equals("second")){
        return duration * 30 * 24* 60 * 60;
      } else if (time_unit.equals("year")){
        return duration /12 ;
      } else {
        logger.debug("Duration.getDuration: " +time_unit+" is not a legal time unit");
        return -1;
      }

    } else if (storedUnit.equals("year")){
      if (time_unit.equals("minute")){
        return duration *(float)365.2425 * 24 * 60;
      } else if (time_unit.equals("hour")){
        return duration * (float)365.2425 * 24;
      } else if (time_unit.equals("day")){
        return duration * (float)365.2425;
      } else if (time_unit.equals("week")){
        return duration *  (float)365.2425 /  7;
        
      } else if (time_unit.equals("month")){
        return duration * 12;
      } else if (time_unit.equals("second")){
        return duration *(float)365.2425 * 24 * 60 * 60;
      } else {
        logger.debug("Duration.getDuration: " +time_unit+" is not a legal time unit");
        return -1;
      }

    } else {
      logger.debug("Duration.getDuration: " +storedUnit+" is not a legal time unit");
      return -1;
    }
      
   
    
  }
}
