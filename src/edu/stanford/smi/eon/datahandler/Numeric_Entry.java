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

// Created on Sat Apr 29 19:18:42 PDT 2000
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
 */
public class Numeric_Entry extends Observation {

	public Numeric_Entry(KnowledgeBase kb, FrameID id) {
		super(kb, id);
	}
/*	public Numeric_Entry(String domain_term,
			float lower_limit, 
			String patient_id, 
			String unit, 
			float upper_limit, 
			Instance valid_time, 
			float value) {
		super(KBHandler.kb, null);
		setDomain_term(domain_term);
		setLower_limit(lower_limit);
		setPatient_id(patient_id);
		setUnit(unit);
		setUpper_limit(upper_limit);
		setValid_time(valid_time);
		setValue(value);
	}
*/
	public void setValue(float value) {
		ModelUtilities.setOwnSlotValue(this, "value", new Float(value));
		pcs.firePropertyChange("value", ModelUtilities.getOwnSlotValue(this, "value"), new Float(value));
	}
	public float getValue() {
		return ((Float) ModelUtilities.getOwnSlotValue(this, "value")).floatValue();
	}

	public void setValid_time(Instance valid_time) {
		ModelUtilities.setOwnSlotValue(this, "valid_time", valid_time);
		pcs.firePropertyChange("valid_time", ModelUtilities.getOwnSlotValue(this, "valid_time"), valid_time);
	}
	public Instance getValid_time() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "valid_time"));
	}

	public void setUnit(String unit) {
		ModelUtilities.setOwnSlotValue(this, "unit", unit);
		pcs.firePropertyChange("unit", ModelUtilities.getOwnSlotValue(this, "unit"), unit);
	}
	public String getUnit() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "unit"));
	}

	public void setLower_limit(float lower_limit) {
		ModelUtilities.setOwnSlotValue(this, "lower_limit", new Float(lower_limit));
		pcs.firePropertyChange("lower_limit", ModelUtilities.getOwnSlotValue(this, "lower_limit"), new Float(lower_limit));
	}
	public float getLower_limit() {
		return ((Float) ModelUtilities.getOwnSlotValue(this, "lower_limit")).floatValue();
	}

	public void setUpper_limit(float upper_limit) {
		ModelUtilities.setOwnSlotValue(this, "upper_limit", new Float(upper_limit));
		pcs.firePropertyChange("upper_limit", ModelUtilities.getOwnSlotValue(this, "upper_limit"), new Float(upper_limit));
	}
	public float getUpper_limit() {
		return ((Float) ModelUtilities.getOwnSlotValue(this, "upper_limit")).floatValue();
	}

	public void setDomain_term(String domain_term) {
		ModelUtilities.setOwnSlotValue(this, "domain_term", domain_term);
		pcs.firePropertyChange("domain_term", ModelUtilities.getOwnSlotValue(this, "domain_term"), domain_term);
	}
	public String getDomain_term() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "domain_term"));
	}

	private PropertyChangeSupport pcs = new PropertyChangeSupport(this); 

	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		pcs.addPropertyChangeListener(pcl);
	}
	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		pcs.removePropertyChangeListener(pcl); 
	} 
// __Code above is automatically generated. Do not change
	public void setSlotsValues(String domain_term,
			float lower_limit, 
			String patient_id, 
			String unit, 
			float upper_limit, 
			Instance valid_time, 
			float value) {
		setDomain_term(domain_term);
		setLower_limit(lower_limit);
		setPatient_id(patient_id);
		setUnit(unit);
		setUpper_limit(upper_limit);
		setValid_time(valid_time);
		setValue(value);
	}

}
