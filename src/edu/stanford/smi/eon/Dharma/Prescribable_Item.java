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
public class Prescribable_Item extends Activity_Specification {

	public Prescribable_Item(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}

	public void setminimum_daily_doseValue(float minimum_daily_dose) {
		ModelUtilities.setOwnSlotValue(this, "minimum_daily_dose", new Float(minimum_daily_dose));	}
	public float getminimum_daily_doseValue() {
		return ((Float) ModelUtilities.getOwnSlotValue(this, "minimum_daily_dose")).floatValue();
	}

	public void setgeneric_drugValue(Cls generic_drug) {
		ModelUtilities.setOwnSlotValue(this, "generic_drug", generic_drug);	}
	public Cls getgeneric_drugValue() {
		return ((Cls) ModelUtilities.getOwnSlotValue(this, "generic_drug"));
	}

	public void setdaily_dose_unitValue(String daily_dose_unit) {
		ModelUtilities.setOwnSlotValue(this, "daily_dose_unit", daily_dose_unit);	}
	public String getdaily_dose_unitValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "daily_dose_unit"));
	}

	public void setquantity_per_prescription_unitValue(String quantity_per_prescription_unit) {
		ModelUtilities.setOwnSlotValue(this, "quantity_per_prescription_unit", quantity_per_prescription_unit);	}
	public String getquantity_per_prescription_unitValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "quantity_per_prescription_unit"));
	}

	public void setdrug_administrationValue(String drug_administration) {
		ModelUtilities.setOwnSlotValue(this, "drug_administration", drug_administration);	}
	public String getdrug_administrationValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "drug_administration"));
	}

	public void setdaily_doseValue(float daily_dose) {
		ModelUtilities.setOwnSlotValue(this, "daily_dose", new Float(daily_dose));	}
	public float getdaily_doseValue() {
		return ((Float) ModelUtilities.getOwnSlotValue(this, "daily_dose")).floatValue();
	}

	public void setdrug_routeValue(String drug_route) {
		ModelUtilities.setOwnSlotValue(this, "drug_route", drug_route);	}
	public String getdrug_routeValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "drug_route"));
	}

	public void setquantity_per_prescriptionValue(int quantity_per_prescription) {
		ModelUtilities.setOwnSlotValue(this, "quantity_per_prescription", new  Integer(quantity_per_prescription));	}
	public int getquantity_per_prescriptionValue() {
		return ((Integer) ModelUtilities.getOwnSlotValue(this, "quantity_per_prescription")).intValue();
	}

	public void setdurationValue(Instance duration) {
		ModelUtilities.setOwnSlotValue(this, "duration", duration);	}
	public Instance getdurationValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "duration"));
	}

	public void setdaily_drug_frequencyValue(int daily_drug_frequency) {
		ModelUtilities.setOwnSlotValue(this, "daily_drug_frequency", new  Integer(daily_drug_frequency));	}
	public int getdaily_drug_frequencyValue() {
		return ((Integer) ModelUtilities.getOwnSlotValue(this, "daily_drug_frequency")).intValue();
	}
// __Code above is automatically generated. Do not change
}
