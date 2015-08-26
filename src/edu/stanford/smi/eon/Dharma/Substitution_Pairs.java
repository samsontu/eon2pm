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

// Created on Mon Sep 17 13:27:24 PDT 2001
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
public class Substitution_Pairs extends Auxiliary_Entity {

	public Substitution_Pairs(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}

	public void setactivity_to_replaceValue(Instance activity_to_replace) {
		ModelUtilities.setOwnSlotValue(this, "activity_to_replace", activity_to_replace);	}
	public Instance getactivity_to_replaceValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "activity_to_replace"));
	}

	public void setreplacing_activityValue(Instance replacing_activity) {
		ModelUtilities.setOwnSlotValue(this, "replacing_activity", replacing_activity);	}
	public Instance getreplacing_activityValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "replacing_activity"));
	}
// __Code above is automatically generated. Do not change
}
