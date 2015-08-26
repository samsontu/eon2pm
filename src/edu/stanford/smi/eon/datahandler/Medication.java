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
import edu.stanford.smi.eon.time.*;
import java.beans.*;
import org.apache.log4j.*;

/** 
 */
public class Medication extends EPR_Entity {

	public Medication(KnowledgeBase kb, FrameID id) {
		super(kb, id);
	}
/*	public Medication(float daily_dose,
			String daily_dose_unit,
			String drug_name,
			int frequency,
			String frequency_unit, 
			Cls mood,
			String patient_id, 
			String prescription_id,
			String route,
			Instance valid_time) {
		super(KBHandler.kb, null);
		setDaily_dose(daily_dose);
		setDaily_dose_unit(daily_dose_unit);
		setDrug_name(drug_name);
		setFrequency(frequency);
		setFrequency_unit(frequency_unit);
		setMood(mood);
		setPatient_id(patient_id);
		setPrescription_id(prescription_id);
		setRoute(route);
		setValid_time(valid_time);
	}
*/  
  static Logger logger=Logger.getLogger(Medication.class);

	public void setValid_time(Instance valid_time) {
		ModelUtilities.setOwnSlotValue(this, "valid_time", valid_time);
		pcs.firePropertyChange("valid_time", ModelUtilities.getOwnSlotValue(this, "valid_time"), valid_time);
	}
	public Instance getValid_time() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "valid_time"));
	}

	public void setMood(Cls mood) {
		ModelUtilities.setOwnSlotValue(this, "mood", mood);
		pcs.firePropertyChange("mood", ModelUtilities.getOwnSlotValue(this, "mood"), mood);
	}
	public Cls getMood() {
		return ((Cls) ModelUtilities.getOwnSlotValue(this, "mood"));
	}

	public void setFrequency_unit(String frequency_unit) {
		ModelUtilities.setOwnSlotValue(this, "frequency_unit", frequency_unit);
		pcs.firePropertyChange("frequency_unit", ModelUtilities.getOwnSlotValue(this, "frequency_unit"), frequency_unit);
	}
	public String getFrequency_unit() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "frequency_unit"));
	}

	public void setFrequency(int frequency) {
		ModelUtilities.setOwnSlotValue(this, "frequency", new  Integer(frequency));
		pcs.firePropertyChange("frequency", ModelUtilities.getOwnSlotValue(this, "frequency"), new  Integer(frequency));
	}
	public int getFrequency() {
		return ((Integer) ModelUtilities.getOwnSlotValue(this, "frequency")).intValue();
	}

	public void setDaily_dose_unit(String daily_dose_unit) {
		ModelUtilities.setOwnSlotValue(this, "daily_dose_unit", daily_dose_unit);
		pcs.firePropertyChange("daily_dose_unit", ModelUtilities.getOwnSlotValue(this, "daily_dose_unit"), daily_dose_unit);
	}
	public String getDaily_dose_unit() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "daily_dose_unit"));
	}

	public void setDaily_dose(float daily_dose) {
		ModelUtilities.setOwnSlotValue(this, "daily_dose", new Float(daily_dose));
		pcs.firePropertyChange("daily_dose", ModelUtilities.getOwnSlotValue(this, "daily_dose"), new Float(daily_dose));
	}
	public float getDaily_dose() {
    if (ModelUtilities.getOwnSlotValue(this, "daily_dose") != null)
		  return ((Float) ModelUtilities.getOwnSlotValue(this, "daily_dose")).floatValue();
    else return (float)0.0;
	}

	public void setRoute(String route) {
		ModelUtilities.setOwnSlotValue(this, "route", route);
		pcs.firePropertyChange("route", ModelUtilities.getOwnSlotValue(this, "route"), route);
	}
	public String getRoute() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "route"));
	}

	public void setPrescription_id(String prescription_id) {
		ModelUtilities.setOwnSlotValue(this, "prescription_id", prescription_id);
		pcs.firePropertyChange("prescription_id", ModelUtilities.getOwnSlotValue(this, "prescription_id"), prescription_id);
	}
	public String getPrescription_id() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "prescription_id"));
	}

	public void setDrug_name(String drug_name) {
		ModelUtilities.setOwnSlotValue(this, "drug_name", drug_name);
		pcs.firePropertyChange("drug_name", ModelUtilities.getOwnSlotValue(this, "drug_name"), drug_name);
	}
	public String getDrug_name() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "drug_name"));
	}

	public void setStatus(String status) {
		ModelUtilities.setOwnSlotValue(this, "status", status);
		pcs.firePropertyChange("status", ModelUtilities.getOwnSlotValue(this, "status"), status);
	}
	public String getStatus() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "status"));
	}
	
	public void setMedicationPossessionRatio(float MPR) {
		ModelUtilities.setOwnSlotValue(this, "medication_possession_ratio", new Float(MPR));
		pcs.firePropertyChange("medication_possession_ratio", ModelUtilities.getOwnSlotValue(this, "medication_possession_ratio"), new Float(MPR));
	}
	public float getMedicationPossessionRatio() {
	    if (ModelUtilities.getOwnSlotValue(this, "medication_possession_ratio") != null)
			  return ((Float) ModelUtilities.getOwnSlotValue(this, "medication_possession_ratio")).floatValue();
	    else return (float)0.0;
		}

	public void setPRT(Instance PRT) {
		ModelUtilities.setOwnSlotValue(this, "present_release_time", PRT);
		pcs.firePropertyChange("present_release_time", ModelUtilities.getOwnSlotValue(this, "present_release_time"), PRT);
	}
	public Instance getPRT() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "present_release_time"));
	}

	private PropertyChangeSupport pcs = new PropertyChangeSupport(this); 

	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		pcs.addPropertyChangeListener(pcl);
	}
	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		pcs.removePropertyChangeListener(pcl); 
	} 
// __Code above is automatically generated. Do not change

  public void setSlotsValues(float daily_dose,
			String daily_dose_unit,
			String drug_name,
			int frequency,
			String frequency_unit,
			Cls mood,
			String patient_id,
			String prescription_id,
			String route,
			String status,
			Instance valid_time, float MPR, Instance PRT) {
		setDaily_dose(daily_dose);
		setDaily_dose_unit(daily_dose_unit);
		setDrug_name(drug_name);
		setFrequency(frequency);
		setFrequency_unit(frequency_unit);
		setMood(mood);
		setPatient_id(patient_id);
		setPrescription_id(prescription_id);
		setRoute(route);
		setStatus(status);
		setValid_time(valid_time);
		setMedicationPossessionRatio(MPR);
		setPRT(PRT);
	}

  protected ProtegeInstanceStub mkInstanceStub () {
    Map slots = new HashMap();
    if (getPatient_id() != null) slots.put("patient_id", getPatient_id());
    if (getDrug_name() != null) slots.put("drug_name", getDrug_name());
    if (getDaily_dose() > 0.0) slots.put("daily_dose", new Float(getDaily_dose()));
//    if (getDaily_dose_unit() != null) slots.put("daily_dose_unit", getDaily_dose_unit());
//    if (getFrequency() > 0) slots.put("frequency", new Integer(getFrequency()));
//    if (getFrequency_unit() != null) slots.put("frequency_unit", getFrequency_unit());
    if (getMood() != null) slots.put("mood", getMood().getName());
    if (getPrescription_id() != null) slots.put("prescription_id", getPrescription_id());
//    if (getRoute() != null) slots.put("route", getRoute());
    ProtegeInstanceStub stub = new ProtegeInstanceStub(getName(), "Medication", slots);
    return stub;
  }

  static Medication mkInstanceFromStub(KBHandler kbmanager,  DataHandler dbmanager, Object fromStorage) {
    logger.debug("Medication.mkInstanceFromStub fromStorage"+ fromStorage.toString());
    if (fromStorage instanceof Medication) return (Medication)fromStorage;
    ProtegeInstanceStub stub = (ProtegeInstanceStub) fromStorage;
    Medication med = (Medication)dbmanager.createInstance("Medication");
    logger.debug("Medication.mkInstanceFromStub med"+ med.toString());
    Map slotsValues = stub.getSlotValues();
    
    if (slotsValues != null) {
      for (Iterator i=slotsValues.keySet().iterator(); i.hasNext();) {
        String slotName = (String)i.next();
        Object value = slotsValues.get(slotName);
        if (slotName.equals("drug_name"))
          med.setDrug_name((String)value);
        else if (slotName.equals("daily_dose"))
          med.setDaily_dose(((Float)value).floatValue());
        else if (slotName.equals("patient_id"))
          med.setPatient_id((String)value);
        else if (slotName.equals("prescription_id"))
          med.setPrescription_id((String)value);
        else if (slotName.equals("mood")) {
          med.setMood(kbmanager.getCls((String)value));

        } 
      }
    }
    return med;
  }


}
