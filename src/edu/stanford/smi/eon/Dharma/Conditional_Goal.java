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
 */
public class Conditional_Goal extends General_Conditional_Goal {

	public Conditional_Goal(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}

	public void setcriterion_to_achieveValue(Instance criterion_to_achieve) {
		ModelUtilities.setOwnSlotValue(this, "criterion_to_achieve", criterion_to_achieve);	}
	public Instance getcriterion_to_achieveValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "criterion_to_achieve"));
	}

	public void setreferencesValue(Collection references) {
		ModelUtilities.setOwnSlotValues(this, "references", references);	}
	public Collection getreferencesValue(){
		return  ModelUtilities.getOwnSlotValues(this, "references");
	}

	public void setselection_criterionValue(Instance selection_criterion) {
		ModelUtilities.setOwnSlotValue(this, "selection_criterion", selection_criterion);	}
	public Instance getselection_criterionValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "selection_criterion"));
	}

	public void setsourceValue(String source) {
		ModelUtilities.setOwnSlotValue(this, "source", source);	}
	
	public String getsourceValue() {
		return (String)ModelUtilities.getOwnSlotValue(this, "source");
	}

	public void setgoalValue(Collection goal) {
		ModelUtilities.setOwnSlotValues(this, "goal", goal);	}
	public Collection getgoalValue(){
		return  ModelUtilities.getOwnSlotValues(this, "goal");
	}
	public void setfine_grain_priorityValue(int priority) {
		ModelUtilities.setOwnSlotValue(this, "fine-grain_priority", priority);	}
	public int getfine_grain_priorityValue(){
		Integer priority = (Integer) ModelUtilities.getOwnSlotValue(this, "fine-grain_priority");
		if (priority == null)
			return 0;
		else
			return  priority.intValue();
	}
// __Code above is automatically generated. Do not change
}
