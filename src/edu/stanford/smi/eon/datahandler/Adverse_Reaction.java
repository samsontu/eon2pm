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
import edu.stanford.smi.eon.time.*;
import java.beans.*;
import org.apache.log4j.*;

/** 
 */
public class Adverse_Reaction extends Observation {

	public Adverse_Reaction(KnowledgeBase kb, FrameID id) {
		super(kb, id);
	}
  static Logger logger = Logger.getLogger(Adverse_Reaction.class);
/*	public Adverse_Reaction(String domain_term, 
			String patient_id, 
			String substance, 
			Instance valid_time) {
		super(KBHandler.kb, null);
		setDomain_term(domain_term);
		setPatient_id(patient_id);
		setSubstance(substance);
		setValid_time(valid_time);
	}
*/
	public void setValid_time(Instance valid_time) {
		ModelUtilities.setOwnSlotValue(this, "valid_time", valid_time);
		pcs.firePropertyChange("valid_time", ModelUtilities.getOwnSlotValue(this, "valid_time"), valid_time);
	}
	public Instance getValid_time() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "valid_time"));
	}

	public void setDomain_term(String domain_term) {
		ModelUtilities.setOwnSlotValue(this, "domain_term", domain_term);
		pcs.firePropertyChange("domain_term", ModelUtilities.getOwnSlotValue(this, "domain_term"), domain_term);
	}
	public String getDomain_term() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "domain_term"));
	}

	public void setSubstance(String substance) {
		ModelUtilities.setOwnSlotValue(this, "substance", substance);
		pcs.firePropertyChange("substance", ModelUtilities.getOwnSlotValue(this, "substance"), substance);
	}
	public String getSubstance() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "substance"));
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
			String patient_id, 
			String substance, 
			Instance valid_time) {
		setDomain_term(domain_term);
		setPatient_id(patient_id);
		setSubstance(substance);
		setValid_time(valid_time);
	}

  protected ProtegeInstanceStub mkInstanceStub () {
    Map slots = new HashMap();
    if (getDomain_term() != null) slots.put("domain_term", getDomain_term());
    if (getSubstance() != null) slots.put("substance", getSubstance());
    if (getPatient_id() != null) slots.put("patient_id", getPatient_id());
    if (getValid_time() != null) slots.put("valid_time",
      new Integer(((Absolute_Time_Point)getValid_time()).getsystem_timeValue()));
    ProtegeInstanceStub stub = new ProtegeInstanceStub(getName(), "Adverse_Reaction", slots);
    return stub;
  }
  static Adverse_Reaction mkInstanceFromStub(KBHandler kbmanager, DataHandler dbmanager, Object fromStorage) {
    ProtegeInstanceStub stub = (ProtegeInstanceStub) fromStorage;
    Map slotsValues = stub.getSlotValues();
    Adverse_Reaction react=null;
    if (slotsValues != null) {
      react = (Adverse_Reaction)dbmanager.createRegisteredInstance("Adverse_Reaction");
      for (Iterator i=slotsValues.keySet().iterator(); i.hasNext();) {
        String slotName = (String)i.next();
        Object value = slotsValues.get(slotName);
        if (slotName.equals("domain_term"))
          react.setDomain_term((String)value);
        else if (slotName.equals("substance"))
          react.setSubstance((String)value);
        else if (slotName.equals("patient_id"))
          react.setPatient_id((String)value);
        else if (slotName.equals("valid_time")) {
          Definite_Time_Point time = (Definite_Time_Point)dbmanager.createRegisteredInstance("Definite_Time_Point");
          time.setsystem_timeValue(((Integer)value).intValue());
          react.setValid_time(time);

        } else logger.error("No such slot "+slotName+" in Adverse_Reaction");
      }
    }
    return react;
  }

}
