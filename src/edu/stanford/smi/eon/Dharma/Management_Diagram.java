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
 *  Guideline_Model_Entity (Superclass hint for Java class generation)

 */
public class Management_Diagram extends Guideline_Model_Entity {

	public Management_Diagram(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
  static Logger logger = Logger.getLogger(Management_Diagram.class);
	public void setcompletion_criteriaValue(Instance completion_criteria) {
		ModelUtilities.setOwnSlotValue(this, "completion_criteria", completion_criteria);	}
	public Instance getcompletion_criteriaValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "completion_criteria"));
	}

	public void setstepsValue(Collection steps) {
		ModelUtilities.setOwnSlotValues(this, "steps", steps);	}
	public Collection getstepsValue(){
		return  ModelUtilities.getOwnSlotValues(this, "steps");
	}

	public void setlast_stepValue(Instance last_step) {
		ModelUtilities.setOwnSlotValue(this, "last_step", last_step);	}
	public Instance getlast_stepValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "last_step"));
	}

	public void setfirst_stepValue(Instance first_step) {
		ModelUtilities.setOwnSlotValue(this, "first_step", first_step);	}
	public Instance getfirst_stepValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "first_step"));
	}
// __Code above is automatically generated. Do not change
	
	public boolean useGraphWidget() {
		KnowledgeBase kb = this.getKnowledgeBase();
		return ((kb.getSlot("transitions")!=null) && 
				(getOwnSlotValues(kb.getSlot("transitions")) != null));
	}

	public void removeForwardReferences() {
		 KnowledgeBase kb = this.getKnowledgeBase();
		Slot branches = kb.getSlot("branches");
		Slot followed_by = kb.getSlot("followed_by");
		//logger.debug("Slot "+branches.getName());
		//logger.debug("Slot "+followed_by.getName());
		Slot label=kb.getSlot("label");
		Collection steps = getOwnSlotValues(kb.getSlot("steps"));
		for (Iterator iterater = steps.iterator(); iterater.hasNext();){
			Instance step = (Instance)iterater.next();
			//logger.debug("removeForwardReferences: node ="+step.getOwnSlotValue(label));
			/*for (Iterator slots=step.getOwnSlots().iterator(); slots.hasNext();){
				logger.debug("has slot "+((Slot)slots.next()).getName());
			}*/
			try {
			if (step.hasOwnSlot(branches)){
				/*logger.debug("removeForwardReferences: "+ step.getOwnSlotValue(label)+ 
				" has branches"); */
				step.setOwnSlotValues(branches, new ArrayList());
			} else if (step.hasOwnSlot(followed_by)) {
				/* logger.debug("removeForwardReferences: "+ step.getOwnSlotValue(label)+ 
				" has followed_by value "+step.getOwnSlotValue(followed_by));*/
				step.setOwnSlotValue(followed_by, null);
			}				
			else logger.error("removeForwardReferences: "+ step.getOwnSlotValue(label)+
					"has no followed_by or branches slot");
			}
			catch (Exception e) {
			
				logger.error("Exception making "+step.getOwnSlotValue(label)+" null, Message: "+
						e.getMessage(), e);
				
			}
		}
		
	}
	public void addForwardReferences() {
		KnowledgeBase kb = this.getKnowledgeBase();
		Collection transitions = getOwnSlotValues(kb.getSlot("transitions"));
		Slot fromSlot = kb.getSlot(":FROM");
		Slot toSlot = kb.getSlot(":TO");
		Instance from = null;
		Instance to = null;
		Slot branches = kb.getSlot("branches");
		Slot followed_by = kb.getSlot("followed_by");
		Slot label=kb.getSlot("label");
		for (Iterator transition = transitions.iterator(); transition.hasNext();){
			Instance inst = (Instance)transition.next();
			from = (Instance)inst.getOwnSlotValue(fromSlot);
			if (from != null) {
				to = (Instance)inst.getOwnSlotValue(toSlot); 
				if (to != null) {
					try {
					if (from.hasOwnSlot(branches)){
						/*logger.debug("addForwardReferences: "+ from.getOwnSlotValue(label)+ 
						" has branches"); */
						if ((from.getOwnSlotValues(branches) == null) ||
						    (!from.getOwnSlotValues(branches).contains(to)))
						    from.addOwnSlotValue(branches, to); 
					} else if (from.hasOwnSlot(followed_by)) {
						/*logger.debug("addForwardReferences: "+ from.getOwnSlotValue(label)+ 
						" has followed by");*/
						if (!(to.equals(from.getOwnSlotValue(followed_by))))
						from.setOwnSlotValue(followed_by, to);
					}				
					else logger.error("addForwardReferences: "+ from.getOwnSlotValue(label)+
							"has no followed_by or branches slot");
					} catch (Exception e){
						logger.error("Exception adding "+to.getOwnSlotValue(label)+ " to "
								+from.getOwnSlotValue(label));
					}
					

				}
			}
		}
	}
  public void printContent(PrintWriter itsWriter, String delimiter) {
    Collection steps = getstepsValue();
    if (steps != null) {
      for (Iterator i=steps.iterator(); i.hasNext();) {
        Object step = i.next();
        if (step instanceof Scenario ) {
          ((Scenario)step).printContent(itsWriter, delimiter);
        }
      }
      itsWriter.println();
      for (Iterator j=steps.iterator(); j.hasNext();) {
        Object step = j.next();
        if (step instanceof Action_Choice) {
          ((Action_Choice)step).printContent(itsWriter, delimiter);
        }
      }
    }
  }
}
