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
 *  Activity_Specification
 */
public class Guideline_Drug extends Drug_Usage {

	public Guideline_Drug(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}

	public void setrule_inValue(Instance rule_in) {
		ModelUtilities.setOwnSlotValue(this, "rule_in", rule_in);	}
	public Instance getrule_inValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "rule_in"));
	}

	public void setdose_level_rangesValue(Collection dose_level_ranges) {
		ModelUtilities.setOwnSlotValues(this, "dose_level_ranges", dose_level_ranges);	}
	public Collection getdose_level_rangesValue(){
		return  ModelUtilities.getOwnSlotValues(this, "dose_level_ranges");
	}

	public void setgeneric_drugValue(Cls generic_drug) {
		ModelUtilities.setOwnSlotValue(this, "generic_drug", generic_drug);	}
	public Cls getgeneric_drugValue() {
		return ((Cls) ModelUtilities.getOwnSlotValue(this, "generic_drug"));
	}

	public void setmax_recommended_dose_levelValue(Cls max_recommended_dose_level) {
		ModelUtilities.setOwnSlotValue(this, "max_recommended_dose_level", max_recommended_dose_level);	}
	public Cls getmax_recommended_dose_levelValue() {
		return ((Cls) ModelUtilities.getOwnSlotValue(this, "max_recommended_dose_level"));
	}

	public void setdrug_usageValue(Instance drug_usage) {
		ModelUtilities.setOwnSlotValue(this, "drug_usage", drug_usage);	}
	public Instance getdrug_usageValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "drug_usage"));
	}

	public void setincrease_dose_ceilingValue(float increase_dose_ceiling) {
		ModelUtilities.setOwnSlotValue(this, "increase_dose_ceiling", new Float(increase_dose_ceiling));	}
	public float getincrease_dose_ceilingValue() {
		return ((Float) ModelUtilities.getOwnSlotValue(this, "increase_dose_ceiling")).floatValue();
	}

	public void setmax_recommended_doseValue(float max_recommended_dose) {
		ModelUtilities.setOwnSlotValue(this, "max_recommended_dose", new Float(max_recommended_dose));	}
	public float getmax_recommended_doseValue() {
		return ((Float) ModelUtilities.getOwnSlotValue(this, "max_recommended_dose")).floatValue();
	}

	public void setstarting_doseValue(float starting_dose) {
		ModelUtilities.setOwnSlotValue(this, "starting_dose", new Float(starting_dose));	}
	public float getstarting_doseValue() {
		return ((Float) ModelUtilities.getOwnSlotValue(this, "starting_dose")).floatValue();
	}

	public void setrule_outValue(Instance rule_out) {
		ModelUtilities.setOwnSlotValue(this, "rule_out", rule_out);	}
	public Instance getrule_outValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "rule_out"));
	}

	public void setdose_strength_unitValue(Cls dose_strength_unit) {
		ModelUtilities.setOwnSlotValue(this, "dose_strength_unit", dose_strength_unit);	}
	public Cls getdose_strength_unitValue() {
		return ((Cls) ModelUtilities.getOwnSlotValue(this, "dose_strength_unit"));
	}

	public void setprescribable_itemsValue(Collection prescribable_items) {
		ModelUtilities.setOwnSlotValues(this, "prescribable_items", prescribable_items);	}
	public Collection getprescribable_itemsValue(){
		return  ModelUtilities.getOwnSlotValues(this, "prescribable_items");
	}
// __Code above is automatically generated. Do not change


  protected boolean ruled_out(GuidelineInterpreter interpreter) {
    Instance ruleOut = getrule_outValue();
    Criteria_Evaluation evaluation = null;
    if (ruleOut != null) {
      try {
        evaluation = (Criteria_Evaluation) ((Criterion)ruleOut).evaluate(interpreter, false);
        if (PCAInterfaceUtil.mapTruthValue(evaluation.truth_value))    // rule-in condition holds
        return true;
      } catch (Exception e) {
        e.printStackTrace();
        return false;
      }
    }
    return false;
  }

  protected boolean ruled_in(GuidelineInterpreter interpreter) {
    Instance ruleIn = getrule_inValue();
    Criteria_Evaluation evaluation = null;
    if (ruleIn != null) {
      try {
        evaluation = (Criteria_Evaluation) ((Criterion)ruleIn).evaluate(interpreter, false);
        if (PCAInterfaceUtil.mapTruthValue(evaluation.truth_value))    // rule-in condition holds
          return true;
      } catch (Exception e) {
        e.printStackTrace();
        return false;
      }
    }
    return false;
  }

  protected String getActivityName() {
    String drugName = getlabelValue();
    if ((drugName == null) && (getgeneric_drugValue() != null))
      drugName = getgeneric_drugValue().getName();
    return drugName;
  }

}
