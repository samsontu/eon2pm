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
import org.apache.log4j.*;

/** 
 *  

 */
public class Range_Mapping_Entry extends Auxiliary_Entity {

	public Range_Mapping_Entry(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
  static Logger logger = Logger.getLogger(Range_Mapping_Entry.class);

	public void setabstract_valueValue(Cls abstract_value) {
		ModelUtilities.setOwnSlotValue(this, "abstract_value", abstract_value);	}
	public Cls getabstract_valueValue() {
		return ((Cls) ModelUtilities.getOwnSlotValue(this, "abstract_value"));
	}

	public void setlower_limitValue(float lower_limit) {
		ModelUtilities.setOwnSlotValue(this, "lower_limit", new Float(lower_limit));	}
	public float getlower_limitValue() {
		return ((Float) ModelUtilities.getOwnSlotValue(this, "lower_limit")).floatValue();
	}

	public void setupper_limitValue(float upper_limit) {
		ModelUtilities.setOwnSlotValue(this, "upper_limit", new Float(upper_limit));	}
	public float getupper_limitValue() {
		return ((Float) ModelUtilities.getOwnSlotValue(this, "upper_limit")).floatValue();
	}
// __Code above is automatically generated. Do not change

  public boolean withinLimits(float number) {
    logger.debug("Range_Mapping_Entry: withinLimits value="+number+
      " lower: "+getlower_limitValue()+" upper: "+getupper_limitValue());
    return ((number >= getlower_limitValue()) &&
            (number <= getupper_limitValue()));
  }
}
