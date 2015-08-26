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
 *  Base concept for an entry in the medical record. A "logical" model against which queries are made.
 */
public abstract class EPR_Entity extends DefaultSimpleInstance {

	public EPR_Entity(KnowledgeBase kb, FrameID id) {
		super(kb, id);
	}
/*	public EPR_Entity(String patient_id, KnowledgeBase kb) {
		super(kb, null);
		setPatient_id(patient_id);
	}
*/
	public void setPatient_id(String patient_id) {
		ModelUtilities.setOwnSlotValue(this, "patient_id", patient_id);
		pcs.firePropertyChange("patient_id", ModelUtilities.getOwnSlotValue(this, "patient_id"), patient_id);
	}
	public String getPatient_id() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "patient_id"));
	}

	private PropertyChangeSupport pcs = new PropertyChangeSupport(this); 

	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		pcs.addPropertyChangeListener(pcl);
	}
	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		pcs.removePropertyChangeListener(pcl); 
	} 
// __Code above is automatically generated. Do not change
}
