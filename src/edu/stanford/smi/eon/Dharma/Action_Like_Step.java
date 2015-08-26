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

// Created on Mon Sep 17 13:27:22 PDT 2001
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
 *  A class that group together all steps that are kinds of action step (e.g., subguideline_step)
 */
public abstract class Action_Like_Step extends Management_Algorithm_Entity {

	public Action_Like_Step(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}

	public void setstart_constraintValue(Instance start_constraint) {
		ModelUtilities.setOwnSlotValue(this, "start_constraint", start_constraint);	}
	public Instance getstart_constraintValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "start_constraint"));
	}

	public void setfollowed_byValue(Instance followed_by) {
		ModelUtilities.setOwnSlotValue(this, "followed_by", followed_by);	}
	public Instance getfollowed_byValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "followed_by"));
	}
// __Code above is automatically generated. Do not change
   public Action_To_Choose  evaluateAction(GuidelineInterpreter gmanager) {
    Action_To_Choose evaluatedAction = null;
    return evaluatedAction;
  }


}
