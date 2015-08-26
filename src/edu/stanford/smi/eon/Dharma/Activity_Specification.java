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

import org.apache.log4j.*;

/** 
 *  Activities are processes that take place over time. They have states that can change from time to time. Such state changes can be the result of actions specified in a guideline. Some activities are abstract processes that are realized by more concrete processes. 
The set of activities specified in the model is extensible. Currently we only model prescribable items that authorize the administration of drugs and related items. A drug regime models an abstract class of prescribable item (e.g., the class of possible calcium blocker prescription).  The states of a drug regime are characterized by a set of attributes, such as dose level (e.g., low, high, medium) and frequency (e.g., twice a day). 

 */
public abstract class Activity_Specification extends Guideline_Model_Entity {

	public Activity_Specification(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
	static Logger logger = Logger.getLogger(Activity_Specification.class);
	public void setreferencesValue(Collection reference) {
		ModelUtilities.setOwnSlotValues(this, "references", reference);	}
	public Collection getreferencesValue(){
		return  ModelUtilities.getOwnSlotValues(this, "references");
	}

	public void setcollateral_actionsValue(Collection collateral_actions) {
		ModelUtilities.setOwnSlotValues(this, "collateral_actions", collateral_actions);	}
	public Collection getcollateral_actionsValue(){
		return  ModelUtilities.getOwnSlotValues(this, "collateral_actions");
	}

	public void setduration_constraintValue(String duration_constraint) {
		ModelUtilities.setOwnSlotValue(this, "duration_constraint", duration_constraint);	}
	public String getduration_constraintValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "duration_constraint"));
	}
	// __Code above is automatically generated. Do not change

	protected Collection getCollateralActionsByType(String actionType) {
		Collection selectedActions = new ArrayList();
		Collection collateralActions = getcollateral_actionsValue();
		if (collateralActions != null)
			for (Iterator i = collateralActions.iterator(); i.hasNext();) {
				Collateral_Action ca = (Collateral_Action)i.next();
				Cls mood = ca.getmoodValue();
				if (mood != null) {
					if ((mood.getName().equals("Medication_Mood")) || (mood.getName().equals(actionType)))
						selectedActions.add(ca);
				} else logger.warn("Drug_Usage.getCollateralActionsByType: "+ca.getlabelValue()+
						"has no specified mood");
			}
		return selectedActions;
	}

	protected Collection evaluateCollateralActions(GuidelineInterpreter interpreter,
			Collection<Collateral_Action> collateralActions) {
		Collection<Action_Spec_Record> evaluatedCollateralActions = new ArrayList<Action_Spec_Record>();
		if (collateralActions != null) {
			for (Collateral_Action collateralAction: collateralActions) {
				evaluatedCollateralActions.addAll(collateralAction.evaluate(interpreter));
			}
		}
		return evaluatedCollateralActions;
	}

protected boolean ruled_out(GuidelineInterpreter interpreter) {
	return false;
}

protected boolean ruled_in(GuidelineInterpreter interpreter) {
	return false;
}

protected String getActivityName() {
	return "";
}
}
