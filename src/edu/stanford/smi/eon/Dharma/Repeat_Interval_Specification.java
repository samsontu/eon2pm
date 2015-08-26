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

// Created on Mon Sep 17 13:27:23 PDT 2001
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
 *  Repeat_Interval_Specification allows a guideline author to specify that certain action or activity should be repeated every so often (interval) for how long (duration) unless abort_condition is true. The attriubute allowed_off set 

Compared with Asbru, this iteration step does not have times_attempted (number of times permitted) and times_completed (number of times required).
 */
public class Repeat_Interval_Specification extends Iteration_Specification {

	public Repeat_Interval_Specification(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}

	public void setintervalValue(Instance interval) {
		ModelUtilities.setOwnSlotValue(this, "interval", interval);	}
	public Instance getintervalValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "interval"));
	}

	public void setdurationValue(Instance duration) {
		ModelUtilities.setOwnSlotValue(this, "duration", duration);	}
	public Instance getdurationValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "duration"));
	}

	public void setat_timeValue(String at_time) {
		ModelUtilities.setOwnSlotValue(this, "at_time", at_time);	}
	public String getat_timeValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "at_time"));
	}

	public void setallowed_offsetValue(Instance allowed_offset) {
		ModelUtilities.setOwnSlotValue(this, "allowed_offset", allowed_offset);	}
	public Instance getallowed_offsetValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "allowed_offset"));
	}
// __Code above is automatically generated. Do not change
}
