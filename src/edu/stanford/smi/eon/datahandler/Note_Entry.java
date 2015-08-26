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
public class Note_Entry extends Observation implements Comparator<Note_Entry>{

	public Note_Entry(KnowledgeBase kb, FrameID id) {
		super(kb, id);
	}
/*	public Note_Entry(String domain_term, 
			String patient_id, 
			Instance valid_time, 
			String value) {
		super(KBHandler.kb, null);
		setDomain_term(domain_term);
		setPatient_id(patient_id);
		setValid_time(valid_time);
		setValue(value);
	}
*/
  static Logger logger = Logger.getLogger(Note_Entry.class);
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

	public void setValue(String value) {
		ModelUtilities.setOwnSlotValue(this, "value", value);
		pcs.firePropertyChange("value", ModelUtilities.getOwnSlotValue(this, "value"), value);
	}
	public String getValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "value"));
	}

	private PropertyChangeSupport pcs = new PropertyChangeSupport(this); 

	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		pcs.addPropertyChangeListener(pcl);
	}
	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		pcs.removePropertyChangeListener(pcl); 
	} 
// __Code above is automatically generated. Do not change

	static int debugLevel=4;
	public void setSlotsValues (String domain_term,
			String patient_id, 
			Instance valid_time, 
			String value) {
		setDomain_term(domain_term);
		setPatient_id(patient_id);
		setValid_time(valid_time);
		setValue(value); 
	}

   static Object mkInstanceFromStub(KBHandler kbmanager, DataHandler dbmanager,
    Object fromStorage, String patient_id) {
		Map.Entry problem = (Map.Entry) fromStorage;
		String date = null;
		Definite_Time_Point timePoint = null;
		Note_Entry noteEntry = null;

		Object value = problem.getValue();
		if (value != null)
			date = (String) value;
		//if (!dbmanager.isCurrentProblem((String) problem.getKey(), date)) {
			noteEntry = (Note_Entry) dbmanager.createInstance("Note_Entry");
			if ((date != null) && !(date.equals(""))) {
				timePoint = (Definite_Time_Point) dbmanager
						.createInstance("Definite_Time_Point");
				try {
					timePoint.setDateValue(date);
					noteEntry.setSlotsValues((String) problem.getKey(),
							patient_id, timePoint, null);
				} catch (Exception e) {
					logger.error("Note_Entry.mkInstanceFromStub(): improper date format "
									+ date);
					noteEntry.setSlotsValues((String) problem.getKey(),
							patient_id, null, null);
				}
			} else {
				noteEntry.setSlotsValues((String) problem.getKey(), patient_id,
						null, null);
			}
//		}
		return noteEntry;
  }



  static Note_Entry mkQualDataInstanceFromStub(KBHandler kbmanager, DataHandler dbmanager,
    Object fromStorage) {

    ProtegeInstanceStub stub = (ProtegeInstanceStub)fromStorage;
    logger.debug("Note_Entry.mkInstanceFromStub fromStorage"+ stub.toString());
    Note_Entry noteEntry = (Note_Entry)dbmanager.createInstance("Note_Entry");
    Map slotsValues = stub.getSlotValues();

    if (slotsValues != null) {
      for (Iterator i=slotsValues.keySet().iterator(); i.hasNext();) {
        String slotName = (String)i.next();
        Object value = slotsValues.get(slotName);
        logger.debug("Note_Entry.mkQualDataInstanceFromStub slotname: "
        		+ slotName);
        if (value != null) {
        	logger.debug("Note_Entry.mkQualDataInstanceFromStub value: "
        			+ value.toString());
        	} else
        		logger.error("Note_Entry.mkQualDataInstanceFromStub slotname has null value!!");
        		
        if (slotName.equals("domain_term"))
          noteEntry.setDomain_term((String)value);
        else if (slotName.equals("value"))
          noteEntry.setValue((String)value);
        else if (slotName.equals("patient_id"))
          noteEntry.setPatient_id((String)value);
        else if (slotName.equals("valid_time")) {
          Definite_Time_Point timePoint = (Definite_Time_Point)
                        dbmanager.createInstance("Definite_Time_Point");
          timePoint.setDateValue(((Integer)value).intValue());
          noteEntry.setValid_time(timePoint);

        } //else logger.error("No such slot "+slotName+" in Medication");
      }
    }
    return noteEntry;
  }


  protected ProtegeInstanceStub makeInstanceStub () {
    Map slots = new HashMap();
    if (getPatient_id() != null) slots.put("patient_id", getPatient_id());
    if (getDomain_term() != null) slots.put("domain_term", getDomain_term());
    if (getValue() != null) slots.put("value", getValue());
    if (getValid_time() !=null) {
      slots.put("valid_time",  new Integer( ((Definite_Time_Point)getValid_time()).getsystem_timeValue()  ));
    }

    ProtegeInstanceStub stub = new ProtegeInstanceStub(getName(), "Note_Entry", slots);
    return stub;
  }

public int compare(Note_Entry n1, Note_Entry n2) {
	return n1.getDomain_term().compareTo(n2.getDomain_term());
}

}

