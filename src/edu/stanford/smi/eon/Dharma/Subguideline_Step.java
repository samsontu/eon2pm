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

// Created on Mon Sep 17 13:27:22 PDT 2001
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
 *  Action_Like_Step (Superclass hint for Java class generation)
<p>A step that decomposes into another subguideline.
 */
public class Subguideline_Step extends Action_Like_Step {

	public Subguideline_Step(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
  static Logger logger = Logger.getLogger(Subguideline_Step.class);

	public void setiteration_specValue(Instance iteration_spec) {
		ModelUtilities.setOwnSlotValue(this, "iteration_spec", iteration_spec);	}
	public Instance getiteration_specValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "iteration_spec"));
	}

	public void setsubguidelineValue(Instance subguideline) {
		ModelUtilities.setOwnSlotValue(this, "subguideline", subguideline);	}
	public Instance getsubguidelineValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "subguideline"));
	}
// __Code above is automatically generated. Do not change
	
	public void tryNext(GuidelineInterpreter glmanager, Compliance_Level compliance)
	throws PCA_Session_Exception {
		
		Instance nextInstance = getfollowed_byValue();
		logger.debug("in Subguideline step: tryNext of "+this.getlabelValue());
		if (nextInstance != null) {
			Clinical_Algorithm_Entity nextEntity = (Clinical_Algorithm_Entity)nextInstance;
			logger.debug("in Subguideline step: tryNext nextEntit y"+
					nextEntity.getBrowserText()+"/"+	nextEntity.getName());
			if (!nextEntity.isNewEncounter())
				nextEntity.doStep(glmanager, compliance);
		}
	}
	
	public void doStep(GuidelineInterpreter glmanager, Compliance_Level compliance)
	throws PCA_Session_Exception {
		Guideline_Service_Record advisories;
		Selected_Scenario scenario;
		logger.info("Subguideline_Step doStep of"+ getlabelValue());
		if (getsubguidelineValue() != null) {
			Management_Guideline mg = (Management_Guideline) getsubguidelineValue();
			Guideline_Scenario_Choices sc = mg.initializeScenarios(glmanager, glmanager.getKBmanager());
			glmanager.addScenarioChoice(sc);
			
		}
	}
}

