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

// Created on Sun Jun 04 14:27:20 PDT 2000
// Copyright Stanford University 2000

package edu.stanford.smi.eon.Dharma;

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

/** 
 */
public class Goal_Criterion extends Criterion {

	public Goal_Criterion(KnowledgeBase kb, FrameID id) {
		super(kb, id);
	}

	public void setguideline_entityValue(Instance guideline_entity) {
		ModelUtilities.setOwnSlotValue(this, "guideline_entity", guideline_entity);	}
	public Instance getguideline_entityValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "guideline_entity"));
	}
// __Code above is automatically generated. Do not change

  public Criteria_Evaluation ownEvaluate(GuidelineInterpreter guidelineManager, boolean doAll)
    throws PCA_Session_Exception {
    // First get the goal associated with guideline_entity
    Criteria_Evaluation evaluation = null;

    Management_Guideline guideline = (Management_Guideline)getguideline_entityValue();
    if (guideline.getgoalValue() != null) {
      evaluation = guidelineManager.checkGoal(
                guideline.makeGuideline_Entity(guideline.getlabelValue()));
      if (evaluation != null) {
        return evaluation;
      } else {
        Collection guidelineGoals =guideline.getgoalValue();
        if ((guidelineGoals!= null) && (!guidelineGoals.isEmpty())) {
          Guideline_Goal evaluatedGoal= guidelineManager.computeConditionalGoals(guidelineGoals,
            guideline.makeGuideline_Entity(guideline.getName()));
          if (evaluatedGoal != null)
            return evaluatedGoal.goal;
          else throw new PCA_Session_Exception ("No evaluated goal for "+guideline.getlabelValue());
        }
      }
    } else throw new PCA_Session_Exception (guideline.getlabelValue()+" has no goal");
    return null;
    }

}
