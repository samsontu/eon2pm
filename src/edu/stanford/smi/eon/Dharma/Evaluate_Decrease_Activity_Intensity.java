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

/** 
 */
public class Evaluate_Decrease_Activity_Intensity extends Evaluate_Modify_Activity {

	public Evaluate_Decrease_Activity_Intensity(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
// __Code above is automatically generated. Do not change

  public Cls getRecommendationMood(Preference preference) {
	  if (preference.equals(Preference.blocked))
		  return getKnowledgeBase().getCls("Blocked_Decrease");
	  else return getKnowledgeBase().getCls("Recommend_Decrease");
  }

  public Direction direction() {
    return Direction.down;
  }

  Cls getNextLevel(GuidelineInterpreter interpreter,
		    Cls currentActivityLevel, Instance evaluateObject) throws Exception {
			if (currentActivityLevel == null) return null;
		    Slot previous = getKnowledgeBase().getSlot("previous");
		    if (previous != null) {
		      Object nextLevel = currentActivityLevel.getOwnSlotValue(previous);
		      if (nextLevel != null) return (Cls)nextLevel;
		      else throw new Exception("Evaluate_Modify_Activity.getNextLevel: no next level");
		    } else throw new Exception("Evaluate_Modify_Activity.getNextLevel: no next slot");
		  }

}
