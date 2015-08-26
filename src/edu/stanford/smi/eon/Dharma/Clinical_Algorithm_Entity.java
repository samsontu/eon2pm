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
 *  We model the decision-making and action-sequencing aspects of a guideline as parts of clinical algorithms. A clinical algorithm consists of a set of scenarios, action steps, decisions, and branching, synchronization, and repetition control nodes that are related by a followed_by relation. Decisions and action steps are steps in the clinical algorithm. Each step can have a goal associated with it.
A clinical algorithm can be specified for either consultation guidelines or management guidelines.  When management recommendations of a guideline span multiple encounters, actions described in the guideline can be partitioned into those that have implications over time and those that constitute best-practice consultation actions recommended that encounter. The latter steps can be modeled in separate consultation guidelines that are indexed by the scenarios at which they apply.

 */
public abstract class Clinical_Algorithm_Entity extends Guideline_Model_Entity {

	public Clinical_Algorithm_Entity(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
  static Logger logger = Logger.getLogger(Clinical_Algorithm_Entity.class);


	public void setreferenceValue(Collection reference) {
		ModelUtilities.setOwnSlotValues(this, "reference", reference);	}
	public Collection getreferenceValue(){
		return  ModelUtilities.getOwnSlotValues(this, "reference");
	}
// __Code above is automatically generated. Do not change


  public void tryNext(GuidelineInterpreter glmanager, Compliance_Level compliance)
    throws PCA_Session_Exception {
    logger.error("Using generic tryStep of"+ getlabelValue());
    // doStep(glmanager, compliance);
    return;
  }

  public void doStep(GuidelineInterpreter glmanager, Compliance_Level compliance)
    throws PCA_Session_Exception {
    logger.error("Using generic doStep! "+ getlabelValue());
    return;
  }

  public boolean isNewEncounter() {
    return false;
  }
  
}
