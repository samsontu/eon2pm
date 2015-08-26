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

// Created on Mon Sep 17 13:27:25 PDT 2001
// Copyright Stanford University 2000

package edu.stanford.smi.eon.Dharma;

import java.util.*;
import java.io.PrintWriter;

import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.util.*;
import edu.stanford.smi.eon.util.*;
import edu.stanford.smi.eon.guidelineinterpreter.*;
import edu.stanford.smi.eon.PCAServerModule.*;
import edu.stanford.smi.eon.kbhandler.*;
import edu.stanford.smi.eon.datahandler.*;
import edu.stanford.smi.eon.time.*;
import edu.stanford.smi.eon.criterion.*;
import edu.stanford.smi.protegex.pal.engine.*; 
import edu.stanford.smi.protegex.pal.parser.*;
import edu.stanford.smi.protegex.pal.relations.*;
import edu.stanford.smi.protegex.pal.language.*;

import org.apache.log4j.*;

/** 
 */
public class Evaluate_Increase_Activity_Intensity extends Evaluate_Modify_Activity {

	public Evaluate_Increase_Activity_Intensity(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
  static Logger logger = Logger.getLogger(Evaluate_Increase_Activity_Intensity.class);

// __Code above is automatically generated. Do not change

  private int debugLevel = 4;
  
  public Cls getRecommendationMood(Preference preference) {
	  if (preference.equals(Preference.blocked))
		  return getKnowledgeBase().getCls("Blocked_Increase");
	  else return getKnowledgeBase().getCls("Recommend_Increase");
  }

  public Direction direction() {
    return Direction.up;
  }

    
  boolean changeToNextLevel(GuidelineInterpreter interpreter,
    Cls currentActivityLevel, Instance evaluateObject) throws Exception {
    if (evaluateObject != null) {
      Slot max = this.getKnowledgeBase().getSlot("max_recommended_dose_level");
      if (max != null) {
        Object maxLevel = evaluateObject.getOwnSlotValue(max);
        if (maxLevel != null) {
          Cls maxRecommendedLevel = (Cls)maxLevel;
          logger.debug("Evaluate_Modify_Activity_Intensity.changeToNextLevel: max_recommended_dose_level "+
                    maxRecommendedLevel);
          if (interpreter.getKBmanager().compareOrdinal(currentActivityLevel,
            maxRecommendedLevel) < 0) return true;
          else return false;
        } else throw new Exception("Evaluate_Modify_Activity.changeToNextLevel: no max level");
      } else throw new Exception("Evaluate_Modify_Activity.changeToNextLevel: no max level slot");
    } else throw new Exception("Evaluate_Modify_Activity.changeToNextLevel: No activity specification");
  }

  Cls getNextLevel(GuidelineInterpreter interpreter,
    Cls currentActivityLevel, Instance evaluateObject) throws Exception {
    Slot next = getKnowledgeBase().getSlot("next");
    if (next != null) {
      Object nextLevel = currentActivityLevel.getOwnSlotValue(next);
      if (nextLevel != null) return (Cls)nextLevel;
      else throw new Exception("Evaluate_Modify_Activity.getNextLevel: no next level");
    } else throw new Exception("Evaluate_Modify_Activity.getNextLevel: no next slot");
  }



}
