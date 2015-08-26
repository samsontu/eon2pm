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

// Created on Mon Sep 17 13:27:21 PDT 2001
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
 *  Management guidelines  model decisions and actions that lead to dependent changes in patient states over time. A management guideline has one main clinical algorithm (Management Diagram). 

 */
public class Management_Guideline extends Guideline {

	public Management_Guideline(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}

	public void settitleValue(String title) {
		ModelUtilities.setOwnSlotValue(this, "title", title);	}
	public String gettitleValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "title"));
	}

	public void setauthorsValue(Collection authors) {
		ModelUtilities.setOwnSlotValues(this, "authors", authors);	}
	public Collection getauthorsValue(){
		return  ModelUtilities.getOwnSlotValues(this, "authors");
	}

	public void setgoalValue(Collection goal) {
		ModelUtilities.setOwnSlotValues(this, "goal", goal);	}
	public Collection getgoalValue(){
		return  ModelUtilities.getOwnSlotValues(this, "goal");
	}

	public void setversionValue(String version) {
		ModelUtilities.setOwnSlotValue(this, "version", version);	}
	public String getversionValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "version"));
	}

	public void seteligibility_criteriaValue(Collection eligibility_criteria) {
		ModelUtilities.setOwnSlotValues(this, "eligibility_criteria", eligibility_criteria);	}
	public Collection geteligibility_criteriaValue(){
		return  ModelUtilities.getOwnSlotValues(this, "eligibility_criteria");
	}

	public void setpatient_characterizationValue(Collection patient_characterization) {
		ModelUtilities.setOwnSlotValues(this, "patient_characterization", patient_characterization);	}
	public Collection getpatient_characterizationValue(){
		return  ModelUtilities.getOwnSlotValues(this, "patient_characterization");
	}

	public void setclinical_algorithmValue(Instance clinical_algorithm) {
		ModelUtilities.setOwnSlotValue(this, "clinical_algorithm", clinical_algorithm);	}
	public Instance getclinical_algorithmValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "clinical_algorithm"));
	}
	
	public Collection<Goal> getalternative_goalsValue() {
		return ModelUtilities.getOwnSlotValues(this, "alternative_goals");
	}
// __Code above is automatically generated. Do not change

	static final Logger logger = Logger.getLogger(Management_Guideline.class);
  public Guideline_Scenario_Choices initializeScenarios(GuidelineInterpreter gInterpreter,
  		KBHandler kbManager){
    Collection scenarioChoicesInGuideline = new ArrayList();
    Collection steps = null;
    Guideline_Scenario_Choices scenarios = null;    
    // Load stored guideline states
    // not done yet;
    // Find scenarios in clinical algorithm

    Instance algorithm = getclinical_algorithmValue();
    if (algorithm != null)  {
    	if  (((Management_Diagram)algorithm).useGraphWidget()) {
    	((Management_Diagram) algorithm).removeForwardReferences();
    	((Management_Diagram) algorithm).addForwardReferences(); }
      steps = ((Management_Diagram) algorithm).getstepsValue();
      Iterator stepsIterator = steps.iterator();
      while (stepsIterator.hasNext()) {
        Instance step = (Instance) stepsIterator.next();
        // if step is a scenario, then evaluate its precondition and add it to the list
        if (kbManager.hasNamedType(step, "Scenario") &&
            (((Scenario)step).isnew_encounterValue())){
          // evaluate the precondition of the scenario
          logger.debug("initializeScenarios: scenario =  " + step.toString());
          try {
			scenarioChoicesInGuideline.add(((Scenario) step).evaluateScenario(this, gInterpreter));
		} catch (PCA_Session_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        }
      }
      if (scenarioChoicesInGuideline.isEmpty()) {
        	 logger.warn("initializeScenario: no valid scenario");
      	scenarioChoicesInGuideline.add(HelperFunctions.dummyScenarioChoice());
      }  
    } else {
    	logger.warn("initializeScenarios: No clinical algorithm");
     	scenarioChoicesInGuideline.add(HelperFunctions.dummyScenarioChoice());
    }
    scenarios = new Guideline_Scenario_Choices(getName(),
   	     (Scenario_Choice[]) scenarioChoicesInGuideline.toArray(new Scenario_Choice[0]));      
    return scenarios;

  }
  public void printDrugClasses (PrintWriter itsWriter, String delimiter) {
    KnowledgeBase kb = this.getKnowledgeBase();
    Cls cls = kb.getCls("Drug_Usage");
    Collection drugUsageSlots = cls.getTemplateSlots();
    Collection drugUsages = cls.getInstances();
    if ((drugUsageSlots != null) && (!drugUsageSlots.isEmpty())
        && (drugUsages != null) && (!drugUsages.isEmpty()))
      for (Iterator j=drugUsageSlots.iterator(); j.hasNext();) {
        Slot slot = (Slot)j.next();
        itsWriter.println(slot.getName());
        for (Iterator i=drugUsages.iterator(); i.hasNext();) {
          Drug_Usage drugUsage = (Drug_Usage)i.next();
          ValueType valueType = drugUsage.getOwnSlotValueType(slot);
          itsWriter.print(drugUsage.getBrowserText()+": ");
          if (cls.getTemplateSlotAllowsMultipleValues(slot)) {
            Collection values = drugUsage.getOwnSlotValues(slot);
            if ((values != null) && (!values.isEmpty())) {
              boolean first = true;
              for (Iterator k=values.iterator(); k.hasNext();) {
                String valueString = getProtegeObjectString(valueType, k.next());
                if (first) {
                  itsWriter.print(valueString);
                  first = false;
                } else itsWriter.print(", "+ valueString);
              }
              itsWriter.println();
            } else itsWriter.println("none");
          } else {
            Object value = drugUsage.getOwnSlotValue(slot);
            if (value != null) {
              itsWriter.print("     "+slot.getName()+": ");
              itsWriter.print(getProtegeObjectString(valueType, value));
              itsWriter.println();
            }
          }
          itsWriter.println();
        }
      }

        /*
        Collection messages = drugUsage.getcollateral_actionsValue();
        if (messages != null) {
          for (Iterator j=messages.iterator(); j.hasNext();) {
            Collateral_Action collaction = (Collateral_Action)j.next();
            collaction.printContent(itsWriter, delimiter, drugUsage.getDrug_Class_NameValue().getName());
          }
        }*/
    
    
  }

  public String getProtegeObjectString (ValueType valueType, Object value) {
    if (valueType.equals(ValueType.INSTANCE))
       return ((Instance)value).getBrowserText();
    else if (valueType.equals(ValueType.CLS))
      return ((Cls)value).getName();
    else return value.toString();
  }

  public void printContent(PrintWriter itsWriter, String delimiter) {
    
    itsWriter.println(gettitleValue());

    itsWriter.println();
    itsWriter.println("Applicability criteria");
    Collection eligCriteria = geteligibility_criteriaValue();
    if (eligCriteria !=null) {
      for (Iterator i=eligCriteria.iterator(); i.hasNext();) {
        itsWriter.print(((Criterion)i.next()).getName());
        itsWriter.print(delimiter);
        ((Criterion)i.next()).printContent(itsWriter);
        itsWriter.println();
      }
    }

    Object algorithmObj = getclinical_algorithmValue();
    if (algorithmObj != null) {
      Management_Diagram algorithm = (Management_Diagram) algorithmObj;
      algorithm.printContent(itsWriter, delimiter);
    }
    itsWriter.println();
    itsWriter.println();
    KnowledgeBase kb = this.getKnowledgeBase();
    Collection drugUsages = kb.getCls("Drug_Usage").getInstances();
    if (drugUsages != null) {
      for (Iterator i=drugUsages.iterator(); i.hasNext();) {
        Drug_Usage drugUsage = (Drug_Usage)i.next();
        Collection messages = drugUsage.getcollateral_actionsValue();
        if (messages != null) {
          for (Iterator j=messages.iterator(); j.hasNext();) {
            Collateral_Action collaction = (Collateral_Action)j.next();
            collaction.printContent(itsWriter, delimiter, drugUsage.getDrug_Class_NameValue().getName());
          }
        }
      }

    }
  }

}

