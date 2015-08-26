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

// Created on Mon Sep 17 13:27:21 PDT 2001
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
 *  Clinical_Algorithm_Entity (Superclass hint for Java class generation)
Branch step should be used only in situation where true concurrent action sequences are possible.
 */
public class Branch_Step extends Management_Algorithm_Entity {

	public Branch_Step(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}

	public void setstart_constraintValue(Instance start_constraint) {
		ModelUtilities.setOwnSlotValue(this, "start_constraint", start_constraint);	}
	public Instance getstart_constraintValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "start_constraint"));
	}

	public void setselection_methodValue(String selection_method) {
		ModelUtilities.setOwnSlotValue(this, "selection_method", selection_method);	}
	public String getselection_methodValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "selection_method"));
	}

	public void setbranchesValue(Collection branches) {
		ModelUtilities.setOwnSlotValues(this, "branches", branches);	}
	public Collection getbranchesValue(){
		return  ModelUtilities.getOwnSlotValues(this, "branches");
	}
// __Code above is automatically generated. Do not change
}
