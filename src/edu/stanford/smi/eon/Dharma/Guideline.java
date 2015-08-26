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

// Created on Mon Sep 17 13:27:20 PDT 2001
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
 *  A class whose subclasses are the different types of guidelines in the model (e.g., Management_Guideline or Consultation_Guideline)
 */
public abstract class Guideline extends Guideline_Model_Entity {

	public Guideline(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}

	public void setreferenceValue(Collection reference) {
		ModelUtilities.setOwnSlotValues(this, "reference", reference);	}
	public Collection getreferenceValue(){
		return  ModelUtilities.getOwnSlotValues(this, "reference");
	}

	public void setprimaryValue(boolean primary) {
		ModelUtilities.setOwnSlotValue(this, "primary", new  Boolean(primary));	}
	public boolean isprimaryValue() {
		if (ModelUtilities.getOwnSlotValue(this, "primary") == null) return false;
		else 
		return ((Boolean) ModelUtilities.getOwnSlotValue(this, "primary")).booleanValue();
	}
// __Code above is automatically generated. Do not change
}
