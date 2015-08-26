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
 *  _interface_
 */
public abstract class Consultation_Guideline_Entity extends Clinical_Algorithm_Entity {

	public Consultation_Guideline_Entity(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
  static Logger logger = Logger.getLogger(Consultation_Guideline_Entity.class);

// __Code above is automatically generated. Do not change

  public void printContent(PrintWriter itsWriter, String delimiter, String context) {
    logger.error("Consultation_Guideline_Entity.printContent: Should not be invoking this method!");
      }
  public Action_To_Choose  evaluateAction(GuidelineInterpreter gmanager) {
    logger.error("Consultation_Guideline_Entity.evaluateAction: Should not be executing this method");
    return HelperFunctions.dummyActionToChoose();
  }

}
