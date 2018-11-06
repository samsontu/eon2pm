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

import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.eon.guidelineinterpreter.*;
import edu.stanford.smi.eon.PCAServerModule.*;
import edu.stanford.smi.eon.criterion.Criterion;
import edu.stanford.smi.eon.kbhandler.*;
import edu.stanford.smi.eon.datahandler.*;

import org.apache.log4j.*;

/** 
 *  for selection of drug class (e.g. beta blocker)
 */
public class Drug_Usage extends Activity_Specification {
	static int debugFlag = 4;
	static Logger logger = Logger.getLogger(Drug_Usage.class);

	public Drug_Usage(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}

	public void setis_first_line_drug_forValue(Collection is_first_line_drug_for) {
		ModelUtilities.setOwnSlotValues(this, "is_first-line_drug_for", is_first_line_drug_for);	}
	public Collection getis_first_line_drug_forValue(){
		return  ModelUtilities.getOwnSlotValues(this, "is_first-line_drug_for");
	}

	public void setis_second_line_drug_forValue(Collection is_second_line_drug_for) {
		ModelUtilities.setOwnSlotValues(this, "is_second-line_drug_for", is_second_line_drug_for);	}
	public Collection getis_second_line_drug_forValue(){
		return  ModelUtilities.getOwnSlotValues(this, "is_second-line_drug_for");
	}

	public void setis_third_line_drug_forValue(Collection is_third_line_drug_for) {
		ModelUtilities.setOwnSlotValues(this, "is_third-line_drug_for", is_third_line_drug_for);	}
	public Collection getis_third_line_drug_forValue(){
		return  ModelUtilities.getOwnSlotValues(this, "is_third-line_drug_for");
	}

	public void setDrug_Partners_To_AvoidValue(Collection Drug_Partners_To_Avoid) {
		ModelUtilities.setOwnSlotValues(this, "Drug_Partners_To_Avoid", Drug_Partners_To_Avoid);	}
	public Collection getDrug_Partners_To_AvoidValue(){
		return  ModelUtilities.getOwnSlotValues(this, "Drug_Partners_To_Avoid");
	}

	public void setDrug_Class_NameValue(Cls Drug_Class_Name) {
		ModelUtilities.setOwnSlotValue(this, "Drug_Class_Name", Drug_Class_Name);	}
	public Cls getDrug_Class_NameValue() {
		return ((Cls) ModelUtilities.getOwnSlotValue(this, "Drug_Class_Name"));
	}

	public void setCompelling_IndicationsValue(Collection Compelling_Indications) {
		ModelUtilities.setOwnSlotValues(this, "Compelling_Indications", Compelling_Indications);	}
	public Collection getCompelling_IndicationsValue(){
		return  ModelUtilities.getOwnSlotValues(this, "Compelling_Indications");
	}

	public void setformulary_preferred_drug_in_classValue(Collection formulary_preferred_drug_in_class) {
		ModelUtilities.setOwnSlotValues(this, "formulary_preferred_drug_in_class", formulary_preferred_drug_in_class);	}
	public Collection getformulary_preferred_drug_in_classValue(){
		return  ModelUtilities.getOwnSlotValues(this, "formulary_preferred_drug_in_class");
	}

	public void setDrug_PartnersValue(Collection Drug_Partners) {
		ModelUtilities.setOwnSlotValues(this, "Drug_Partners", Drug_Partners);	}
	public Collection getDrug_PartnersValue(){
		return  ModelUtilities.getOwnSlotValues(this, "Drug_Partners");
	}

	public void setComplication_FactorValue(Collection Complication_Factor) {
		ModelUtilities.setOwnSlotValues(this, "Complication_Factor", Complication_Factor);	}
	public Collection getComplication_FactorValue(){
		return  ModelUtilities.getOwnSlotValues(this, "Complication_Factor");
	}

	public void setcomponentsValue(Collection components) {
		ModelUtilities.setOwnSlotValues(this, "components", components);	}
	public Collection getcomponentsValue(){
		return  ModelUtilities.getOwnSlotValues(this, "components");
	}

	public void setRelative_IndicationsValue(Collection Relative_Indications) {
		ModelUtilities.setOwnSlotValues(this, "Relative_Indications", Relative_Indications);	}
	public Collection getRelative_IndicationsValue(){
		return  ModelUtilities.getOwnSlotValues(this, "Relative_Indications");
	}

	public void setSide_EffectsValue(Collection Side_Effects) {
		ModelUtilities.setOwnSlotValues(this, "Side_Effects", Side_Effects);	}
	public Collection getSide_EffectsValue(){
		return  ModelUtilities.getOwnSlotValues(this, "Side_Effects");
	}

	public void setAbsolute_ContraindicationsValue(Collection Absolute_Contraindications) {
		ModelUtilities.setOwnSlotValues(this, "Absolute_Contraindications", Absolute_Contraindications);	}
	public Collection getAbsolute_ContraindicationsValue(){
		return  ModelUtilities.getOwnSlotValues(this, "Absolute_Contraindications");
	}

	public void setAdditional_ADR_checkValue(Collection additional_ADR_check) {
		ModelUtilities.setOwnSlotValues(this, "additional_ADR_check", additional_ADR_check);	}
	public Collection getAdditional_ADR_checkValue(){
		return  ModelUtilities.getOwnSlotValues(this, "additional_ADR_check");
	}

	public void setRelative_ContraindicationsValue(Collection Relative_Contraindications) {
		ModelUtilities.setOwnSlotValues(this, "Relative_Contraindications", Relative_Contraindications);	}
	public Collection getRelative_ContraindicationsValue(){
		return  ModelUtilities.getOwnSlotValues(this, "Relative_Contraindications");
	}

	public void setDo_Not_Start_ConditionsValue(Collection criteria) {
		ModelUtilities.setOwnSlotValues(this, "Do_Not_Start_Conditions", criteria);	}
	public Collection getDo_Not_Start_ConditionsValue(){
		return  ModelUtilities.getOwnSlotValues(this, "Do_Not_Start_Conditions");
	}

	public void setDo_Not_Intensify_ConditionsValue(Collection criteria) {
		ModelUtilities.setOwnSlotValues(this, "Do_Not_Intensify_Conditions", criteria);	}
	public Collection getDo_Not_Intensify_ConditionsValue(){
		return  ModelUtilities.getOwnSlotValues(this, "Do_Not_Intensify_Conditions");
	}

	public void setDo_Not_Decrease_Dose_ConditionsValue(Collection criteria) {
		ModelUtilities.setOwnSlotValues(this, "Do_Not_Decrease_Dose_Conditions", criteria);	}
	public Collection getDo_Not_Decrease_Dose_ConditionsValue(){
		return  ModelUtilities.getOwnSlotValues(this, "Do_Not_Decrease_Dose_Conditions");
	}

	public Collection getDo_Not_Start_Controllable_Conditions () {
		Collection<Instance> conditions = new ArrayList<Instance>();
		Cls metaClass = this.getKnowledgeBase().getCls("Diagnostic_Term_Metaclass");
		Slot controllableSlot = this.getKnowledgeBase().getSlot("controllable");
		if ((metaClass != null) && (controllableSlot != null)) {
			for (Object o: getDo_Not_Start_ConditionsValue()) {
				Boolean controllable = (Boolean) ((Frame)o).getOwnSlotValue(controllableSlot);
				if (((Instance) o).hasDirectType(metaClass) && (controllable != null && controllable.booleanValue())) {
					conditions.add((Instance)o);
				}
			}
		} else
			logger.error("Null Diagnostic_Term_Metaclass or null controllable slot");
		return conditions;
	}

	public Collection getDo_Not_Start_Uncontrollable_Conditions () {
		Collection<Instance> conditions = new ArrayList<Instance>();
		Cls metaClass = this.getKnowledgeBase().getCls("Diagnostic_Term_Metaclass");
		Slot controllableSlot = this.getKnowledgeBase().getSlot("controllable");
		if ((metaClass != null) && (controllableSlot != null)) {
			for (Object o: getDo_Not_Start_ConditionsValue()) {
				Boolean controllable = (Boolean) ((Frame)o).getOwnSlotValue(controllableSlot);
				if ((controllable == null) || (((Instance) o).hasDirectType(metaClass) && !controllable.booleanValue())) {
					conditions.add((Instance)o);
				}
			}
		} else
			logger.error("Null Diagnostic_Term_Metaclass or null controllable slot");
		return conditions;
	}

	public Collection getDo_Not_Intensify_Controllable_Conditions () {
		Collection<Instance> conditions = new ArrayList<Instance>();
		Cls metaClass = this.getKnowledgeBase().getCls("Diagnostic_Term_Metaclass");
		Slot controllableSlot = this.getKnowledgeBase().getSlot("controllable");
		if ((metaClass != null) && (controllableSlot != null)) {
			for (Object o: getDo_Not_Intensify_ConditionsValue()) {
				Boolean controllable = (Boolean) ((Frame)o).getOwnSlotValue(controllableSlot);
				if (((Instance) o).hasDirectType(metaClass) && (controllable != null && controllable.booleanValue())) {
					conditions.add((Instance)o);
				}
			}
		} else
			logger.error("Null Diagnostic_Term_Metaclass or null controllable slot");
		return conditions;
	}

	public Collection getDo_Not_Intensify_Uncontrollable_Conditions () {
		Collection<Instance> conditions = new ArrayList<Instance>();
		Cls metaClass = this.getKnowledgeBase().getCls("Diagnostic_Term_Metaclass");
		Slot controllableSlot = this.getKnowledgeBase().getSlot("controllable");
		if ((metaClass != null) && (controllableSlot != null)) {
			for (Object o: getDo_Not_Intensify_ConditionsValue()) {
				Boolean controllable = (Boolean) ((Frame)o).getOwnSlotValue(controllableSlot);
				if ((controllable == null) || (((Instance) o).hasDirectType(metaClass) && !controllable.booleanValue()))  {
					conditions.add((Instance)o);
				}
			}
		} else
			logger.error("Null Diagnostic_Term_Metaclass or null controllable slot");
		return conditions;

	}

	public Collection getDo_Not_Decrease_Dose_Controllable_Conditions () {
		Collection<Instance> conditions = new ArrayList<Instance>();
		Cls metaClass = this.getKnowledgeBase().getCls("Diagnostic_Term_Metaclass");
		Slot controllableSlot = this.getKnowledgeBase().getSlot("controllable");
		if ((metaClass != null) && (controllableSlot != null)) {
			for (Object o: getDo_Not_Decrease_Dose_ConditionsValue()) {
				Boolean controllable = (Boolean) ((Frame)o).getOwnSlotValue(controllableSlot);
				if (((Instance) o).hasDirectType(metaClass) && (controllable != null && controllable.booleanValue())) {
					conditions.add((Instance)o);
				}
			}
		} else
			logger.error("Null Diagnostic_Term_Metaclass or null controllable slot");
		return conditions;
	}

	public Collection getDo_Not_Decrease_Dose_Uncontrollable_Conditions () {
		Collection<Instance> conditions = new ArrayList<Instance>();
		Cls metaClass = this.getKnowledgeBase().getCls("Diagnostic_Term_Metaclass");
		Slot controllableSlot = this.getKnowledgeBase().getSlot("controllable");
		if ((metaClass != null) && (controllableSlot != null)) {
			for (Object o: getDo_Not_Decrease_Dose_ConditionsValue()) {
				Boolean controllable = (Boolean) ((Frame)o).getOwnSlotValue(controllableSlot);
				if ((controllable == null) || (((Instance) o).hasDirectType(metaClass) && !controllable.booleanValue()))  {
					conditions.add((Instance)o);
				}
			}
		} else
			logger.error("Null Diagnostic_Term_Metaclass or null controllable slot");
		return conditions;

	}

	ActivityEvaluation evaluate(GuidelineInterpreter interpreter) {
		ActivityEvaluation evaluation = new ActivityEvaluation();

		logger.debug("Drug_Usage evaluate: "+this.getDrug_Class_NameValue());
		KBHandler kb = interpreter.getKBmanager();
		Collection currentProblems = interpreter.getDBmanager().currentProblems();
		Collection currentMeds = interpreter.getDBmanager().currentActivities(
				"Medication", null);
		Collection thisDrugClass = new ArrayList();
		thisDrugClass.add(getDrug_Class_NameValue());
		logger.debug(getDrug_Class_NameValue().getName());
		Collection complicatingFactor = interpreter.matchData("",
				getComplication_FactorValue(), currentProblems);
		logger.debug("Drug_Usage evaluate: start "+this.getDrug_Class_NameValue());
		if (complicatingFactor.isEmpty()) {
			evaluation.is_first_line_drug_for =interpreter.matchData("",
					getis_first_line_drug_forValue(), currentProblems);
			evaluation.is_second_line_drug_for = interpreter.matchData("",
					getis_second_line_drug_forValue(), currentProblems);
			evaluation.is_third_line_drug_for = interpreter.matchData("",
					getis_third_line_drug_forValue(), currentProblems);
			evaluation.beneficialInteractions =interpreter.matchData("",
					getDrug_PartnersValue(), currentMeds);
			evaluation.compellingIndications = interpreter.matchData("",
					getCompelling_IndicationsValue(), currentProblems);
			evaluation.contraindications = interpreter.matchData("",
					getAbsolute_ContraindicationsValue(), currentProblems);
			evaluation.harmful_interactions = interpreter.matchData("",
					getDrug_Partners_To_AvoidValue(), currentMeds);
			evaluation.relative_contraindications = interpreter.matchData("",
					getRelative_ContraindicationsValue(), currentProblems);
			evaluation.relative_indications = interpreter.matchData("",
					getRelative_IndicationsValue(), currentProblems);
			evaluation.stop_add_controllable_conditions = interpreter.matchData("",
					getDo_Not_Start_Controllable_Conditions(), currentProblems);
			evaluation.stop_add_uncontrollable_conditions = interpreter.matchData("",
					getDo_Not_Start_Uncontrollable_Conditions(), currentProblems);
			evaluation.stop_intensify_conditions = interpreter.matchData("",
					getDo_Not_Intensify_ConditionsValue(), currentProblems);
			evaluation.side_effects = matchAllAdverseEvents(interpreter);
			evaluation.current_use = interpreter.matchData("",thisDrugClass, currentMeds);
			interpreter.putEvaluation(this, evaluation);
			logger.debug("Drug_Usage evaluate: "+this.getDrug_Class_NameValue()+
					" completed evaluation");
			return evaluation;
		} else return null;
	}


	private Collection<Matched_Data> matchAllAdverseEvents(GuidelineInterpreter interpreter) {
		Collection<Matched_Data> allAdverseReaction = interpreter.matchAdverseEvents(this.getDrug_Class_NameValue());
		Collection relevantDrugClasses =  this.getAdditional_ADR_checkValue();
		if (relevantDrugClasses != null) {
			for (Cls drugCls : ((Collection <Cls>)relevantDrugClasses)) {
				Collection<Matched_Data> adverseReaction = interpreter.matchAdverseEvents(drugCls);
				allAdverseReaction.addAll(adverseReaction);
			}
		}
		return allAdverseReaction;
	}


	public Add_Evaluation evaluateAdd (GuidelineInterpreter interpreter, int fine_grain_priority, 
			Delete_Evaluation delEval, Collection<Slot> recommendationBasis)
			throws PCA_Session_Exception {
		Collection evaluatedAddCollateralActions = evaluateCollateralActions(interpreter, getCollateralActionsByType("Recommend_Add"));
		Collection evaluatedBlockedCollateralActions = evaluateCollateralActions(interpreter,getCollateralActionsByType("Blocked_Add"));
		evaluatedBlockedCollateralActions.addAll(evaluatedAddCollateralActions);

		Add_Evaluation addEval = addActivity( interpreter, fine_grain_priority);
		if (addEval != null)  {
			if ((addEval.contraindications.length > 0 ) || 
					(hasHarmfulInteractions(addEval, delEval)) || 
					(addEval.prior_use.equals(Truth_Value._true) ) || 
					(addEval.do_not_start_uncontrollable_conditions.length > 0))
				addEval.preference = Preference.ruled_out;
			else if (isRecommended(addEval, recommendationBasis, interpreter)) {
				if (addEval.do_not_start_controllable_conditions.length > 0) {
					addMedicationInstance(interpreter, addEval, "Blocked_Add");
					addEval.preference = Preference.blocked;
					addEval.messages =  (Action_Spec_Record[]) evaluatedBlockedCollateralActions.toArray(new Action_Spec_Record[0]);
				} else {
					addEval.preference = Preference.preferred;
					logger.debug("in Drug_Usage.evaluateAdd: creating Medication recom "+
							"instance for " + getDrug_Class_NameValue().getName());
					addMedicationInstance(interpreter, addEval, "Recommend_Add");
					addEval.messages =  (Action_Spec_Record[]) evaluatedAddCollateralActions.toArray(new Action_Spec_Record[0]);
				}
			} else
				addEval.preference = Preference.neutral;
			return addEval;
		}
		else  return null;
	}
	
	public boolean isRecommended(Add_Evaluation addEval, Collection<Slot> recommendationBasis,
			GuidelineInterpreter interpreter) {
		boolean isRecommended = false;
		KnowledgeBase kb = interpreter.getKBmanager().getKB();
		Collection<Slot> augmentedRecommendationBasis = new ArrayList<Slot>();
		if (recommendationBasis.isEmpty()) {
			augmentedRecommendationBasis.add(kb.getSlot("Compelling_Indications"));
			augmentedRecommendationBasis.add(kb.getSlot("Relative_Indications"));
		} else {
			augmentedRecommendationBasis.addAll(recommendationBasis);
		}
		for (Slot recommBasis : augmentedRecommendationBasis) {
			switch(recommBasis.getName()) {
			case "Compelling_Indications" :
				if (addEval.compelling_indications.length > 0) {
					isRecommended = true;
					addEval.recommendationBasis="compelling indication";
				}
				break;
			case "Relative_Indications" :
				if (addEval.relative_indications.length > 0) {
					isRecommended = true;
					addEval.recommendationBasis="relative indication";
				}
				break;
			case "is_first-line_drug_for" :
				if (addEval.is_first_line_drug_for.length > 0) {
					isRecommended = true;
					addEval.recommendationBasis="is first-line drug for";
				}
				break;
			case "is_second-line_drug_for" :
				if (addEval.is_second_line_drug_for.length > 0) {
					isRecommended = true;
					addEval.recommendationBasis="is second-line drug for";
				}
				break;
			case "is_third-line_drug_for" :
				if (addEval.is_third_line_drug_for.length > 0) {
					isRecommended = true;
					addEval.recommendationBasis="is third-line drug for";
				}
				break;
			}
		}
		return isRecommended;
	}
	
	public Add_Evaluation processAddActivity (GuidelineInterpreter interpreter, int fine_grain_priority, Delete_Evaluation delEval) {

		Collection evaluatedAddCollateralActions = evaluateCollateralActions(interpreter, getCollateralActionsByType("Recommend_Add"));
		Collection evaluatedBlockedCollateralActions = evaluateCollateralActions(interpreter,getCollateralActionsByType("Blocked_Add"));
		evaluatedBlockedCollateralActions.addAll(evaluatedAddCollateralActions);

		Add_Evaluation addEval = addActivity( interpreter, fine_grain_priority);
		if (addEval != null)  {
			if ((addEval.contraindications.length > 0 ) || 
					(hasHarmfulInteractions(addEval, delEval)) || 
					(addEval.prior_use.equals(Truth_Value._true) ) || 
					(addEval.do_not_start_uncontrollable_conditions.length > 0))
				addEval.preference = Preference.ruled_out;
			else if (addEval.do_not_start_controllable_conditions.length > 0) {
				addMedicationInstance(interpreter, addEval, "Blocked_Add");
				addEval.preference = Preference.blocked;
				addEval.messages =  (Action_Spec_Record[]) evaluatedBlockedCollateralActions.toArray(new Action_Spec_Record[0]);
			} else {
				addEval.preference = Preference.preferred;
				logger.debug("in Drug_Usage.evaluateAdd: creating Medication recom "+
						"instance for " + getDrug_Class_NameValue().getName());
				addMedicationInstance(interpreter, addEval, "Recommend_Add");
				addEval.messages =  (Action_Spec_Record[]) evaluatedAddCollateralActions.toArray(new Action_Spec_Record[0]);
			} 
			return addEval;
		} else  return null;
	}

	public Add_Evaluation addActivity(GuidelineInterpreter interpreter, int fine_grain_priority) {
		// an activity is a Drug Usage.
		ActivityEvaluation eval = interpreter.getEvaluation(this);
		if (eval == null) {
			eval = evaluate(interpreter);
		}
		if (eval !=null) {
			DataHandler dataHandler = interpreter.getDBmanager();
			Collection<Guideline_Drug> possibleDrugs = getPossbileDrugs(interpreter);
			String prettyName = getDrugClassPrettyName();
			logger.debug("in Drug_Usage.addActivity: creating Medication recom "+
					"instance for " + getDrug_Class_NameValue().getName() );
			String preferredDrugsString = getPreferredDrugsString(possibleDrugs, prettyName, interpreter);
			Add_Evaluation addEval= new Add_Evaluation(
					prettyName+ " "+ (preferredDrugsString.equals("") ? "" : "("+preferredDrugsString +")"), //description
					prettyName,                                                                         //name
					this.makeGuideline_Entity(interpreter.guideline.getName()), 
					getDrug_Class_NameValue().getName(),                                                //activity to start
					(Matched_Data[]) eval.is_first_line_drug_for.toArray(new Matched_Data[0]),
					(Matched_Data[]) eval.is_second_line_drug_for.toArray(new Matched_Data[0]) ,
					(Matched_Data[]) eval.is_third_line_drug_for.toArray(new Matched_Data[0]),
					(Matched_Data[]) eval.beneficialInteractions.toArray(new Matched_Data[0]),
					(Matched_Data[]) eval.compellingIndications.toArray(new Matched_Data[0]),
					(Matched_Data[]) eval.contraindications.toArray(new Matched_Data[0]),
					(Matched_Data[]) eval.harmful_interactions.toArray(new Matched_Data[0]),
					(Matched_Data[]) eval.relative_contraindications.toArray(new Matched_Data[0]),
					(Matched_Data[]) eval.relative_indications.toArray(new Matched_Data[0]),
					eval.current_use.isEmpty()? Truth_Value._false :Truth_Value._true,
							null,
							(Matched_Data[]) eval.side_effects.toArray(new Matched_Data[0]),
							(Matched_Data[])eval.stop_add_controllable_conditions.toArray(new Matched_Data[0]),
							(Matched_Data[])eval.stop_add_uncontrollable_conditions.toArray(new Matched_Data[0]),
							Preference.neutral, getPreferredDrugs(possibleDrugs, interpreter), fine_grain_priority, null);
			/*			Medication addedMed =  (Medication)dataHandler.createInstance("Medication");
			addedMed.setSlotsValues( (float)0.0,"",
					getDrug_Class_NameValue().getName(), 0, "",
					getKnowledgeBase().getCls("Recommend_Add"),
					dataHandler.getCaseID(), "", "",   null);
			 */			return addEval;
		} else {
			logger.error("Drug_Usage.addActivity : null activity evaluation for "+this.getBrowserText());
			return null;
		}
	}

	private Collection<String> getPreferredDrugs(Collection<Guideline_Drug> possibleDrugs, GuidelineInterpreter interpreter) {
		Collection<String> drugs = new ArrayList<String>();
		for (Guideline_Drug drug : possibleDrugs) {
			if (drug.preferred(interpreter)) {
				drugs.add(drug.getActivityName() + " (preferred)");
			} else
				drugs.add(drug.getActivityName());
		}
		if (!drugs.isEmpty()) 
			return drugs;	
		else
			return null;
	}

	public void addMedicationInstance(GuidelineInterpreter interpreter, Add_Evaluation addEval, 
			String medMood) {
		//		Collection collateralStartActions = getCollateralActionsByType("Recommend_Add");
		//		Collection evaluatedCollateralStartActions = evaluateCollateralActions(interpreter, collateralStartActions);
		//		addEval.messages = (Action_Spec_Record[]) evaluatedCollateralStartActions.toArray(new Action_Spec_Record[0]);
		DataHandler dataHandler = interpreter.getDBmanager();
		logger.debug("in Drug_Usage.addActivity: creating Medication recom "+
				"instance for " + getDrug_Class_NameValue().getName() );
		Medication addedMed =  (Medication)dataHandler.createInstance("Medication");
		addedMed.setSlotsValues( (float)0.0,"",
				getDrug_Class_NameValue().getName(), 0, "",
				getKnowledgeBase().getCls(medMood),
				dataHandler.getCaseID(), "", "", Constants.active,   null, (int)0, null);


	}

	String getDrugClassPrettyName() {
		String prettyName = null;
		Slot prettyNameSlot = this.getKnowledgeBase().getSlot("PrettyName");
		Object prettyNameObj = (getDrug_Class_NameValue().hasOwnSlot(prettyNameSlot))? getDrug_Class_NameValue().getOwnSlotValue(prettyNameSlot) : null;
		if (prettyNameObj != null)
			prettyName = (String)prettyNameObj;
		else {
			prettyName = (getlabelValue() != null) ?
					getlabelValue() :
						getDrug_Class_NameValue().getName();
		}
		return prettyName;
	}


	public String getPreferredDrugsString(Collection<Guideline_Drug> possibleDrugs, String drugClassName, GuidelineInterpreter interpreter) {
		boolean firstPossible = true;
		String preferredDrugsString ="";
		String drugLabel = "";
		for (Guideline_Drug drug : possibleDrugs) {
			if (drug.getActivityName().equals(drugClassName))
				continue;
			if (drug.preferred(interpreter)) {
				drugLabel = drug.getActivityName() + " (preferred)";
			} else
				drugLabel = drug.getActivityName();
			if (firstPossible) {
				preferredDrugsString = preferredDrugsString + drugLabel;
				firstPossible = false;
			} else
				preferredDrugsString = preferredDrugsString + ", "+drugLabel;
		}
		return preferredDrugsString;	

	}

	

	/**
	 * 
	 * @param drugs 
	 * @param interpreter
	 * @return A (possibly empty) collection of preferred or default guideline drugs
	 * 
	 * If there is at least one drug that is ruled in and not ruled out, return only these drugs
	 * else return those that are not ruled out.
	 */
	public Collection<Guideline_Drug> getPossbileDrugs(GuidelineInterpreter interpreter) {
		Collection<Guideline_Drug> listedDrugs = (Collection<Guideline_Drug>)getformulary_preferred_drug_in_classValue();
		Collection <Guideline_Drug> possibleDrugs = new ArrayList<Guideline_Drug>();
		Collection <Guideline_Drug> preferredDrugs = new ArrayList<Guideline_Drug>();
		if (listedDrugs != null) {
			for (Guideline_Drug drugObj: listedDrugs) {
				if (drugObj.ruled_out(interpreter) != true) {
					possibleDrugs.add(drugObj);
					if (drugObj.ruled_in(interpreter)) {
						preferredDrugs.add(drugObj);
					}
				}
			}
			if (!preferredDrugs.isEmpty())
				return preferredDrugs;
			else
				return possibleDrugs;
		} 
		return possibleDrugs;
	}

	Delete_Evaluation evaluateDelete(GuidelineInterpreter interpreter,
			Delete_Evaluation result, String currentDrug, int fine_grain_priority)
					throws PCA_Session_Exception {
		ActivityEvaluation eval = interpreter.getEvaluation(this);

		if (eval == null) {
			eval = evaluate(interpreter);
		}
		if (eval != null) {
			if (result == null) {
				Collection evaluatedCollateralDeleteActions = evaluateCollateralActions(interpreter,
						getCollateralActionsByType("Recommend_Delete"));
				result = new Delete_Evaluation("",
						currentDrug,
						this.makeGuideline_Entity(interpreter.guideline.getName()),
						getDrug_Class_NameValue().getName(),
						(Matched_Data[]) eval.is_first_line_drug_for.toArray(new Matched_Data[0]),
						(Matched_Data[]) eval.is_second_line_drug_for.toArray(new Matched_Data[0]) ,
						(Matched_Data[]) eval.is_third_line_drug_for.toArray(new Matched_Data[0]),
						(Matched_Data[]) eval.beneficialInteractions.toArray(new Matched_Data[0]),
						(Matched_Data[]) eval.compellingIndications.toArray(new Matched_Data[0]),
						(Matched_Data[]) eval.contraindications.toArray(new Matched_Data[0]),
						(Matched_Data[]) eval.harmful_interactions.toArray(new Matched_Data[0]),
						(Matched_Data[]) eval.relative_contraindications.toArray(new Matched_Data[0]),
						(Matched_Data[]) eval.relative_indications.toArray(new Matched_Data[0]),
						(Matched_Data[]) eval.side_effects.toArray(new Matched_Data[0]),
						eval.current_use.isEmpty()? Truth_Value._false :Truth_Value._true,
								((Action_Spec_Record[]) evaluatedCollateralDeleteActions.toArray(new Action_Spec_Record[0])),
								eval.contraindications.isEmpty() ? Preference.neutral : Preference.preferred,
										null, fine_grain_priority);
				//				  public java.lang.String description;
				//				  public java.lang.String name;
				//				  public edu.stanford.smi.eon.PCAServerModule.Guideline_Entity guideline_id;
				//				  public java.lang.String activity_to_delete;
				//				  public edu.stanford.smi.eon.PCAServerModule.Matched_Data[] beneficial_interactions;
				//				  public edu.stanford.smi.eon.PCAServerModule.Matched_Data[] compelling_indications;
				//				  public edu.stanford.smi.eon.PCAServerModule.Matched_Data[] contraindications;
				//				  public edu.stanford.smi.eon.PCAServerModule.Matched_Data[] harmful_interactions;
				//				  public edu.stanford.smi.eon.PCAServerModule.Matched_Data[] relative_contraindications;
				//				  public edu.stanford.smi.eon.PCAServerModule.Matched_Data[] relative_indications;
				//				  public edu.stanford.smi.eon.PCAServerModule.Matched_Data[] side_effects;
				//				  public edu.stanford.smi.eon.PCAServerModule.Truth_Value prior_use;
				//				  public edu.stanford.smi.eon.PCAServerModule.Preference preference;
				//				  public edu.stanford.smi.eon.PCAServerModule.Action_Spec_Record[] messages;
				//				  public Collection<String> specific_drug;

			} else {
				result.name=result.name+ ", "+getDrug_Class_NameValue().getName();
				result.activity_to_delete  = result.activity_to_delete +", "+ getDrug_Class_NameValue().getName();
			}
			//logger.debug("in evaluateAdd: " + result.toString() , 3);
			return result;
		} else {
			logger.debug("Drug_Usage evaluateDelete: Cannot evaluate "+
					getBrowserText()+"/"+getName());

			return null;
		}
	}

	private boolean indicateAdd (Add_Evaluation addEval,
			Delete_Evaluation delEval) {
		return ((((addEval.compelling_indications != null) && (addEval.compelling_indications.length > 0)) ||
				((addEval.relative_indications != null) && (addEval.relative_indications.length > 0))) &&
				((addEval.contraindications == null) ||
						(addEval.contraindications.length == 0)) &&
						(addEval.prior_use.equals(Truth_Value._false)) &&
						(!hasHarmfulInteractions(addEval, delEval)));

	}

	private boolean compellinglyIndicateAdd ( Add_Evaluation addEval,
			Delete_Evaluation delEval) {
		return (((addEval.compelling_indications != null) &&
				(addEval.compelling_indications.length > 0))  &&
				((addEval.contraindications == null) ||
						(addEval.contraindications.length == 0)) &&
						(addEval.prior_use.equals(Truth_Value._false )) &&
						(!blocked(addEval, delEval)) &&
						(!hasHarmfulInteractions(addEval, delEval)));
	}

	private boolean blocked(Add_Evaluation addEval,
			Delete_Evaluation delEval) {
		return (addEval.do_not_start_controllable_conditions != null && addEval.do_not_start_controllable_conditions.length>0);
	}

	private boolean contraIndicatedOrHarmfulInteractionsOrPriorUse(Add_Evaluation addEval,
			Delete_Evaluation delEval) {
		return (((addEval.contraindications != null) &&
				(addEval.contraindications.length > 0)) ||
				(addEval.prior_use.equals(Truth_Value._true )) ||
				(hasHarmfulInteractions(addEval, delEval)));
	}

	static Collection  evaluateSubstitute(GuidelineInterpreter interpreter,
			Delete_Evaluation delEval,
			Collection<Drug_Usage> substitutionCandidates, int fine_grain_priority, 
			Collection<Slot>  recommendationBasis)
					throws PCA_Session_Exception {

		//returns a collection of ADD_Evaluations
		Collection addEvals = new ArrayList();
		Add_Evaluation addEval = null;
		DataHandler dataHandler = interpreter.getDBmanager();
		boolean hasSubstitute = false;
		if ( delEval == null) {
			logger.debug("Drug_Usage evaluateSubstitute: No current activity in evaluateSubstitute");
			throw new PCA_Session_Exception ("No current activity in evaluateSubstitute");
		}
		logger.debug("Drug_Usage.evaluateSubstitute: current drug "+delEval.name);
		if (( substitutionCandidates == null) || (substitutionCandidates.size() ==0)) {
			logger.warn("Drug_Usage evaluateSubstitute: No substitution candidate in evaluateSubstitute" );
			 throw new PCA_Session_Exception ("No substitution candidate in evaluateSubstitute");
		}
		logger.debug("Drug_Usage.evaluateSubstitute 2: delEval "+delEval.toString());

		//		if ((delEval.contraindications != null) &&
		//				(delEval.contraindications.length > 0)) { 
		for (Drug_Usage drugToAdd : substitutionCandidates){
			//Drug_Usage drugToAdd = (Drug_Usage)candidate.next();
			logger.debug("Drug_Usage.evaluateSubstitute: current drug contraindicated, checking " + drugToAdd.getlabelValue());
			addEval = drugToAdd.evaluateAdd(interpreter, fine_grain_priority, delEval, recommendationBasis);
			/*				if (addEval != null) {
					logger.debug("Drug_Usage.evaluateSubstitute: addEval 0 candidate = " +
							addEval.activity_to_start + " compelling_ind # "+
							addEval.compelling_indications.length+ " relative ind # "+
							addEval.relative_indications.length);
					// if drug is compellingly or relatively indicated and not contraindicated
					if (drugToAdd.indicateAdd(addEval, delEval)) {
						addEval.preference = Preference.preferred;
						drugToAdd.addMedicationInstance(interpreter, addEval, "Recommend_Add");

						hasSubstitute = true;
					} else if (drugToAdd.contraIndicatedOrHarmfulInteractionsOrPriorUse(addEval, delEval)){
						addEval.preference = Preference.ruled_out;
					}
			 */				
			if (((addEval.preference.equals(Preference.preferred))	|| (addEval.preference.equals(Preference.blocked)))){
				hasSubstitute = true;
				addEvals.add(addEval);
			}
		} //for
		/*		} else { // Case 2 current drug not indicated
			if ((delEval.compelling_indications == null) ||
					(delEval.compelling_indications.length == 0)) {   
			logger.debug("Drug_Usage: Evaluating substitution candidates");
			for (Iterator candidate = substitutionCandidates.iterator();
					candidate.hasNext();) {
				Drug_Usage drugToAdd = (Drug_Usage)candidate.next();
				logger.debug("Drug_Usage.evaluateSubstitute "+drugToAdd.getlabelValue());
				addEval = drugToAdd.addActivity(interpreter, fine_grain_priority);
				if (addEval != null) {
					logger.debug("Drug_Usage evaluateSubstitute: addEval 1 candidate = " +
							addEval.activity_to_start + " compelling_ind # "+
							addEval.compelling_indications.length+ " relative ind # "+
							addEval.relative_indications.length);
					// if drug is compellingly  and not contraindicated
					if (drugToAdd.compellinglyIndicateAdd(addEval, delEval)) {
						addEval.preference = Preference.preferred;
						drugToAdd.addMedicationInstance(interpreter, addEval, "Recommend_Add");
						hasSubstitute = true;
					} else if (drugToAdd.blocked(addEval, delEval)) {
						addEval.preference = Preference.blocked;
						drugToAdd.addMedicationInstance(interpreter, addEval, "Blocked");
					} else if (drugToAdd.contraIndicatedOrHarmfulInteractionsOrPriorUse(addEval, delEval)){
						addEval.preference = Preference.ruled_out;
					}
					addEvals.add(addEval);
				}
			} //for*/
		/*			} else {
				logger.error("Drug_Usage.evaluateSubstitute(): "+delEval.activity_to_delete +
						" not contraindicated and has compelling indication");        	
			}
			 	}*/	
		if (hasSubstitute) {
			Medication deletedMed =  (Medication)interpreter.getDBmanager().createInstance("Medication");
			deletedMed.setSlotsValues(
					0, "", delEval.name,
					0, "",interpreter.getKBmanager().getKB().getCls("Recommend_Delete"),
					interpreter.getDBmanager().getCaseID(), "", "", Constants.active, null, (int)0, null );
		}
		if (addEvals.size()> 0) return  addEvals;
		else return null;

	}


	private boolean hasHarmfulInteractions(Add_Evaluation addEval,
			Delete_Evaluation delEval) {
		boolean notOK = false;
		if ((addEval.harmful_interactions != null ) &&
				(addEval.harmful_interactions.length > 0) ) {
			notOK = true;
			if ((addEval.harmful_interactions.length == 1 ) &&
					(addEval.harmful_interactions[0].data.length == 1) &&
					(delEval != null) &&
					addEval.harmful_interactions[0].data[0].equals(delEval.name)) {
				notOK = false;
			}
		}
		return notOK;
	}


	protected String getActivityName() {
		return getDrugClassPrettyName() ;
	}

	public Collection getDo_Not_Intensify_Conditions(boolean controllable) {
		// Return Do Not Intensify condition (controllable or uncontrollable)
		return controllable ? this.getDo_Not_Intensify_Controllable_Conditions() : this.getDo_Not_Intensify_Uncontrollable_Conditions();
	}
	
	public Collection getDo_Not_Decrease_Dose_Conditions(boolean controllable) {
		// Return Do Not Intensify condition (controllable or uncontrollable)
		return controllable ? this.getDo_Not_Decrease_Dose_Controllable_Conditions() : this.getDo_Not_Decrease_Dose_Uncontrollable_Conditions();
	}
}
