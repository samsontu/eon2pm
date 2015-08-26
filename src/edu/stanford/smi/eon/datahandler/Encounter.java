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

// Created on Sat Apr 29 19:18:41 PDT 2000
// Copyright Stanford University 2000

package edu.stanford.smi.eon.datahandler;

import java.util.*;
import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.util.*;
import edu.stanford.smi.eon.util.*;
import edu.stanford.smi.eon.kbhandler.*;
import edu.stanford.smi.eon.datahandler.*;
import edu.stanford.smi.eon.Dharma.*;
import java.beans.*;

/** 
 *  consultation between patient and care provider
 */
public class Encounter extends EPR_Entity {

	public Encounter(KnowledgeBase kb, FrameID id) {
		super(kb, id);
	}
	public Encounter(String patient_id, 
			Instance when, 
			Instance with_HCP, KnowledgeBase kb) {
		super(kb, null);
		setPatient_id(patient_id);
		setWhen(when);
		setWith_HCP(with_HCP);
	}

	public void setWhen(Instance when) {
		ModelUtilities.setOwnSlotValue(this, "when", when);
		pcs.firePropertyChange("when", ModelUtilities.getOwnSlotValue(this, "when"), when);
	}
	public Instance getWhen() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "when"));
	}

	public void setWith_HCP(Instance with_HCP) {
		ModelUtilities.setOwnSlotValue(this, "with_HCP", with_HCP);
		pcs.firePropertyChange("with_HCP", ModelUtilities.getOwnSlotValue(this, "with_HCP"), with_HCP);
	}
	public Instance getWith_HCP() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "with_HCP"));
	}

	private PropertyChangeSupport pcs = new PropertyChangeSupport(this); 

	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		pcs.addPropertyChangeListener(pcl);
	}
	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		pcs.removePropertyChangeListener(pcl); 
	} 
// __Code above is automatically generated. Do not change

	public void setSlotsValues (String patient_id,
			Instance when, 
			Instance with_HCP) {
		setPatient_id(patient_id);
		setWhen(when);
		setWith_HCP(with_HCP);
	}

}

