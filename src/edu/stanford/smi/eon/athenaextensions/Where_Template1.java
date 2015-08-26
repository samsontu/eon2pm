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

// Created on Tue Dec 12 11:31:16 PST 2000
// Copyright Stanford University 2000

package edu.stanford.smi.eon.athenaextensions;

import java.util.*;
import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.util.*;
import edu.stanford.smi.eon.util.*;
import edu.stanford.smi.eon.guidelineinterpreter.*;
import edu.stanford.smi.eon.PCAServerModule.*;
import edu.stanford.smi.eon.kbhandler.*;
import edu.stanford.smi.eon.datahandler.*;
import edu.stanford.smi.eon.time.*;
import edu.stanford.smi.eon.criterion.*;
import edu.stanford.smi.eon.Dharma.*;
import org.apache.log4j.*;
/** 
 *  This class models the following where relations:

There exists d of type R1 *where*
forall D and C, 
1. d is subsumed by D 
2. (C  r D) 
3. (there does not exist p of type R2 such that  p is subsumed by C)

D is primary subsuming class [e.g., Drug_Usage]
r is qualifying slot [e.g., compelling indication], C is value of qualifying slot, and p is an instance of qualifying object type

Example
There exists a medication that is not compellingly indicated

In Jess, 

(Medication (drug_name ?drug_name))
(Drug_Usage (Drug_Class_Name ?drug_class)
                          (Compelling_Indication ?indications)
(not (Problem (domain_term ?problem)))
(test (subsumed ?drug_name ?drug_class))
(test (subsumed_by_any ?problem ?indications))

All compellingly indicated drugs are used

(Medication (drug_name ?drug_name))
(Drug_Usage (Drug_Class_Name ?drug_class)
                          (Compelling_Indication ?indications)
                          (Absolute_Contraindications ?contras)
                          (Relative_Contraindications ?relativecontras))
(Problem (domain_term ?problem1))
(not (Problem (domain_term ?problem2))
(test (subsumed ?drug_name ?drug_class))
(test (subsumed_by_any ?problem1 ?indications))
(test (subsumed_by_any ?problem2 (union ?contras ?relativecontras)))
 */
public class Where_Template1 extends Filter {

	public Where_Template1(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
  static Logger logger = Logger.getLogger(Where_Template1.class);

	public void setpresence_of_qualifying_objectValue(boolean presence_of_qualifying_object) {
		ModelUtilities.setOwnSlotValue(this, "presence_of_qualifying_object", new  Boolean(presence_of_qualifying_object));	}
	public boolean ispresence_of_qualifying_objectValue() {
		if (ModelUtilities.getOwnSlotValue(this, "presence_of_qualifying_object") == null) return false;
		else 
		return ((Boolean) ModelUtilities.getOwnSlotValue(this, "presence_of_qualifying_object")).booleanValue();
	}

	public void setqualifying_object_typeValue(Cls qualifying_object_type) {
		ModelUtilities.setOwnSlotValue(this, "qualifying_object_type", qualifying_object_type);	}
	public Cls getqualifying_object_typeValue() {
		return ((Cls) ModelUtilities.getOwnSlotValue(this, "qualifying_object_type"));
	}

	public void setprimary_subsuming_class_keyValue(Instance primary_subsuming_class_key) {
		ModelUtilities.setOwnSlotValue(this, "primary_subsuming_class_key", primary_subsuming_class_key);	}
	public Instance getprimary_subsuming_class_keyValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "primary_subsuming_class_key"));
	}

	public void setqualifying_slotValue(Instance qualifying_slot) {
		ModelUtilities.setOwnSlotValue(this, "qualifying_slot", qualifying_slot);	}
	public Instance getqualifying_slotValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "qualifying_slot"));
	}

	public void setprimary_subsuming_classValue(Cls primary_subsuming_class) {
		ModelUtilities.setOwnSlotValue(this, "primary_subsuming_class", primary_subsuming_class);	}
	public Cls getprimary_subsuming_classValue() {
		return ((Cls) ModelUtilities.getOwnSlotValue(this, "primary_subsuming_class"));
	}
// __Code above is automatically generated. Do not change

  public Collection evaluate(Matched_Data matches, GuidelineInterpreter glmanager)
    throws PCA_Session_Exception {
    
    Collection returnedMatches = new ArrayList();
    if (matches == null) {
      logger.debug("Where_Template1 evaluate: matches is null");
      return null;
    }
    else {
      /*We are trying to evaluate whether there is a current drug(matches) that
        is or isn't compellingly/relatively indicated
        1. For each matched medication find the Drug Usage class that subsume medications
        2.     For that Drug Usage class, find compelling indications
        3.     For each compelling indication, find patient problems that match
        4.     if there exists matched problem,and we are checking presence of indication
                  then add this medication to returning matched set
                  if we are checking for absence of indication, then do nothing
                if there is no matched problem,and we are checking presence of indication
                  then do nothing
                  if we are checking for absence of indication, the add this medication to
                              returning matched set
      */
      boolean qualifyerPresence = ispresence_of_qualifying_objectValue();

      for (int i=0; i < matches.data.length; i++) {
        // find Drug_Usage classes that subsume matched medications
        Collection foundDrugUsages = glmanager.getKBmanager().findInstances(getprimary_subsuming_classValue(),
          new WhereComparisonFilter(
        		  matches.data[i], DharmaPaddaConstants.subclass_of, 
        		  getprimary_subsuming_class_keyValue().getName()), glmanager); // check arguments of subclass_of

        if (foundDrugUsages.isEmpty()) {
          if (!qualifyerPresence)
            returnedMatches.add(new Matched_Data(matches.data_class, matches.data[i], new String[0]));
        } else {
          Collection returnMatch = new ArrayList();
          for (Iterator k=foundDrugUsages.iterator(); k.hasNext();) {
            Instance inst = (Instance)k.next();
            Collection indications = inst.getOwnSlotValues((Slot)getqualifying_slotValue());
            // (contra)indications of a Drug_Usage class
            Matched_Data problems=null;
            for (Iterator j=indications.iterator(); j.hasNext();) {
              Cls indication = (Cls)j.next();
              problems = glmanager.containSubclassOf("", indication.getName(),
                glmanager.getDBmanager().currentProblems());
              if (problems != null) {
                String classPrettyName = "";
                try {
                  classPrettyName = glmanager.getKBmanager().prettyName(indication);
                } catch (Exception e) {
                  classPrettyName = indication.getName();
                }
                returnMatch.add(classPrettyName) ;
              }
            }
          }
          String classPrettyName = "";
          try {
            Cls cls = glmanager.getKBmanager().getCls(matches.data[i]);
            classPrettyName = glmanager.getKBmanager().prettyName(cls);
          } catch (Exception e) {
            classPrettyName = matches.data[i];
          }
          if (qualifyerPresence && (returnMatch.size()>0)) {
            returnedMatches.add(new Matched_Data("", classPrettyName,
                (String[])  returnMatch.toArray(new String[0])));
          } else if (!qualifyerPresence && (returnMatch.size()==0)) {
            returnedMatches.add(new Matched_Data("", classPrettyName, new String[0]));
          }
        }
      }
      if (returnedMatches.isEmpty()) {
        logger.debug("Where_Template1 evaluate returns null");
        return null;
      }
      else {
        
        for (Iterator i=returnedMatches.iterator(); i.hasNext();)
          logger.debug("Where_Template1 evaluate returns: "+((Matched_Data)i.next()).toString());
        return returnedMatches;
      }
    }

  }
}
