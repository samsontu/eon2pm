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
public abstract class Evaluate_Activity_Act extends Action_Specification {

	public Evaluate_Activity_Act(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}

	public void setresponsesValue(Collection responses) {
		ModelUtilities.setOwnSlotValues(this, "responses", responses);	}
	public Collection getresponsesValue(){
		return  ModelUtilities.getOwnSlotValues(this, "responses");
	}

	public void setactivity_classValue(String activity_class) {
		ModelUtilities.setOwnSlotValue(this, "activity_class", activity_class);	}
	public String getactivity_classValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "activity_class"));
	}
// __Code above is automatically generated. Do not change
	
	public static Drug_Usage getDrugUsage(String currentActivity, Collection<Drug_Usage> specForCurrentActivity,
			GuidelineInterpreter interpreter) {
		Drug_Usage evaluateObject = null;
		if ((specForCurrentActivity != null) && (specForCurrentActivity.size()>0)) {
			if (specForCurrentActivity.size() == 1) 
				for (Drug_Usage d : specForCurrentActivity)
					evaluateObject = d;
			else {
				Slot drugUsagesSlot = interpreter.getKBmanager().getKB().getSlot(DharmaPaddaConstants.drugUsagesPropertyName);
				if (drugUsagesSlot != null) {
					Collection<Drug_Usage>guidelineDUs = (Collection<Drug_Usage>)interpreter.guideline.getOwnSlotValues(drugUsagesSlot);
					if ((guidelineDUs != null) && !guidelineDUs.isEmpty()) {
						specForCurrentActivity.retainAll(guidelineDUs);
						if (specForCurrentActivity.size() == 0) {
							logger.warn("No drug usage for "+currentActivity + " after taking into account guideline drug usage instances");
						} else {
							if (specForCurrentActivity.size() > 1) {
								logger.error("More than one drug usages for "+currentActivity + " after taking into account guideline drug usage instances. Selecting a random drug usage instance");
							}
							for (Drug_Usage d : specForCurrentActivity)
								evaluateObject = d;
						}
					} else {
						logger.error("No guideline drug usage and more than one drug usage possibilities for "+currentActivity + " Selecting a random drug usage instance");
						for (Drug_Usage d : specForCurrentActivity)
							evaluateObject = d;
					}
				} else {
					logger.error("No drug_usages slot and more than one drug usage possibilities for "+currentActivity+" Selecting a random drug usage" );
					for (Drug_Usage d : specForCurrentActivity)
						evaluateObject = d;
				}
			}
		} else { // no specForCurrentActivity 
			logger.warn("No drug usage for "+currentActivity);
		}
		return evaluateObject;
	}

}
