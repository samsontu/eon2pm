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
public class Collateral_Action extends Auxiliary_Entity {

	public Collateral_Action(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}

	public void setmoodValue(Cls mood) {
		ModelUtilities.setOwnSlotValue(this, "mood", mood);	}
	public Cls getmoodValue() {
		return ((Cls) ModelUtilities.getOwnSlotValue(this, "mood"));
	}

	public void setactionsValue(Collection actions) {
		ModelUtilities.setOwnSlotValues(this, "actions", actions);	}
	public Collection getactionsValue(){
		return  ModelUtilities.getOwnSlotValues(this, "actions");
	}
	// __Code above is automatically generated. Do not change


	Collection<Action_Spec_Record> evaluate(GuidelineInterpreter interpreter) {
		Collection<Action_Spec_Record> evaluatedCollateralActions = new ArrayList<Action_Spec_Record>();
		Action_Spec_Record actionSpecRecord;

		if ((this.getactionsValue()) !=null) {
			for (Action_Specification acionSpec : (Collection<Action_Specification>)this.getactionsValue()) {
				actionSpecRecord = ((Action_Specification)acionSpec).evaluateActionSpec(interpreter);
				if (actionSpecRecord != null)
					evaluatedCollateralActions.add(actionSpecRecord);
			}
		}
		return evaluatedCollateralActions;
	}

	public void printContent(PrintWriter itsWriter, String delimiter, String context) {
		Object mood = getmoodValue();
		if (mood != null) context = context +"/" +((Cls)mood).getName()+"/"+getlabelValue();
		Collection actions = getactionsValue();
		if (actions != null) {
			for (Iterator i=actions.iterator(); i.hasNext();) {
				((Action_Specification)i.next()).printContent(itsWriter, delimiter, context, null, null);
			}
		}
	}
}
