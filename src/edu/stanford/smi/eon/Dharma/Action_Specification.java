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

// Created on Mon Sep 17 13:27:24 PDT 2001
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
 *  Tasks
Guidelines suggest instantaneous actions, including those that cause transitions among states of activities. An action can change the state of an activity by starting it, stopping it, or changing one of its attribute values. Other actions that can be specified in a guideline include acts such as collecting patient data, printing patient information leaflet, referring to other clinicians, or scheduling a follow-up appointment.
 */
public abstract class Action_Specification extends Guideline_Model_Entity {

	public Action_Specification(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
	static Logger logger = Logger.getLogger(Action_Specification.class);

	public void setreferencesValue(Collection references) {
		ModelUtilities.setOwnSlotValues(this, "references", references);	}
	public Collection getreferencesValue(){
		return  ModelUtilities.getOwnSlotValues(this, "references");
	}

	public void setsubsidiary_messageValue(Collection messages) {
		ModelUtilities.setOwnSlotValues(this, "subsidiary_message", messages);	}
	public Collection getsubsidiary_messageValue(){
		return  ModelUtilities.getOwnSlotValues(this, "subsidiary_message");
	}
	public void setdescriptionValue(String description) {
		ModelUtilities.setOwnSlotValue(this, "description", description);	}
	public String getdescriptionValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "description"));
	}

	public void setrule_in_conditionValue(Instance rulein) {
		ModelUtilities.setOwnSlotValue(this, "rule_in_condition", rulein);	}
	public Instance getrule_in_conditionValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "rule_in_condition"));
	}

	public void setlevel_of_evidenceValue(Cls level_of_evidence) {
		ModelUtilities.setOwnSlotValue(this, "level_of_evidence", level_of_evidence);	}
	public Cls getlevel_of_evidenceValue() {
		Object level_of_evidence = ModelUtilities.getOwnSlotValue(this, "level_of_evidence");
		if (level_of_evidence != null)
			return ((Cls) level_of_evidence);
		else return null;
	}

	public void setquality_of_evidenceValue(Cls quality_of_evidence) {
		ModelUtilities.setOwnSlotValue(this, "quality_of_evidence", quality_of_evidence);	}
	public Cls getquality_of_evidenceValue() {
		Object quality_of_evidence = ModelUtilities.getOwnSlotValue(this, "overall_quality_of_evidence");
		if (quality_of_evidence != null)
			return ((Cls) quality_of_evidence);
		else return null;
	}

	public void setstrength_of_recommendationValue(Cls strength_of_recommendation) {
		ModelUtilities.setOwnSlotValue(this, "strength_of_recommendation", strength_of_recommendation);	}
	public Cls getstrength_of_recommendationValue() {
		Object strength_of_recommendation = ModelUtilities.getOwnSlotValue(this, "strength_of_recommendation");
		if (strength_of_recommendation != null)
			return ((Cls) strength_of_recommendation);
		else return null;
	}

	public void setnet_benefitValue(Cls net_benefit) {
		ModelUtilities.setOwnSlotValue(this, "net_benefit", net_benefit);	}
	public Cls getnet_benefitValue() {
		Object net_benefit = ModelUtilities.getOwnSlotValue(this, "net_benefit");
		if (net_benefit != null)
			return ((Cls) net_benefit);
		else return null;
	}

	public void setfine_grain_priorityValue(int fine_grain_priority) {
		ModelUtilities.setOwnSlotValue(this, "fine-grain_priority", fine_grain_priority);	}
	public int getfine_grain_priorityValue(){
		Integer value = (Integer)ModelUtilities.getOwnSlotValue(this, "fine-grain_priority");
		if (value != null)
			return  value.intValue();
		else
			return 0;
	}
	
	public void setcollateral_actionsValue(Collection collateral_actions) {
		ModelUtilities.setOwnSlotValues(this, "collateral_actions", collateral_actions);	}
	public Collection getcollateral_actionsValue(){
		return  ModelUtilities.getOwnSlotValues(this, "collateral_actions");
	}


	public void doAction(
			Action_To_Choose action,
			Guideline_Action_Choices currentDecision,
			GuidelineInterpreter interpreter)
					throws PCA_Session_Exception {
		logger.debug("In "+getlabelValue()+".doAction:");
	}


	public Action_Spec_Record  evaluateActionSpec(GuidelineInterpreter gmanager) {
		if (getrule_in_conditionValue() != null) {
			Criteria_Evaluation evaluation = HelperFunctions.dummyCriteriaEvaluation();
			try {
				evaluation = (Criteria_Evaluation) ((Criterion)getrule_in_conditionValue()).evaluate(gmanager, false);
				if (!(PCAInterfaceUtil.mapTruthValue(evaluation.truth_value)))  {   // rule-in condition does not hold
					return null;
				}
			} catch (PCA_Session_Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		String description="";
		String label="";
		if (getdescriptionValue() != null) description = getdescriptionValue();
		if (getlabelValue() != null) label = getlabelValue();
		Collection<Instance> references = getreferencesValue();
		Collection<String> urls = new ArrayList();
		for (Instance reference : references) 
			urls.addAll(reference.getOwnSlotValues(this.getKnowledgeBase().getSlot("URL")));
		Action_Spec_Record actionSpec = new Action_Spec_Record(label,
				description, this.getDirectType().getName(), 
				this.getfine_grain_priorityValue(), 
				this.makeGuideline_Entity(gmanager.getCurrentGuidelineID()),
				HelperFunctions.dummyJustification(), 
				(getlevel_of_evidenceValue() != null) ? getlevel_of_evidenceValue().getBrowserText() : null, 
						(getnet_benefitValue() != null) ? getnet_benefitValue().getBrowserText() : null, 
								(getquality_of_evidenceValue() != null) ? getquality_of_evidenceValue().getBrowserText() : null,
										(getstrength_of_recommendationValue() != null) ? getstrength_of_recommendationValue().getBrowserText(): null, 
												urls, (getsubsidiary_messageValue() != null) ?getsubsidiary_messageValue(): null, null
				);

		//    java.lang.String name,  //label
		//    java.lang.String text,  //description
		//    java.lang.String action_spec_class,
		//    edu.stanford.smi.eon.PCAServerModule.Guideline_Entity action_spec,
		//    edu.stanford.smi.eon.PCAServerModule.Justification justification,
		//    java.lang.String level_of_evidence,
		//    java.lang.String net_benefit,
		//    java.lang.String  quality_of_evidence,
		//    java.lang.String strength_of_recommendation,
		//    Collection<String> references

		return actionSpec;
	}
	
	protected Collection<Action_Spec_Record> getCollateralActions(GuidelineInterpreter gmanager) {
		Collection<Action_Spec_Record> evaluatedCollateralActions = new ArrayList<Action_Spec_Record>();
		if (getcollateral_actionsValue() != null) {
			for (Collateral_Action collateralAction: (Collection<Collateral_Action>)getcollateral_actionsValue()) {
				evaluatedCollateralActions.addAll(collateralAction.evaluate(gmanager));
			}
		}
		if (evaluatedCollateralActions.isEmpty())
			return null;
		else return evaluatedCollateralActions;
	}

	public void printContent(PrintWriter itsWriter, String delimiter, String context,
			Criterion ruleIn, Criterion ruleOut) {

		// action_class type, context, rule-in, rule-out, additional_condition, text
		/*
    itsWriter.print(getName());
    itsWriter.print(delimiter);
    itsWriter.print(this.getDirectType().getName());
    itsWriter.print(delimiter);
    itsWriter.print(delimiter);
    itsWriter.print(context);
    itsWriter.print(delimiter);
    if (ruleIn != null) ruleIn.printContent(itsWriter);
    itsWriter.print(delimiter);
    if (ruleOut != null) ruleOut.printContent(itsWriter);
    itsWriter.print(delimiter);
    itsWriter.print(delimiter);
    itsWriter.println();     */
	}
}
