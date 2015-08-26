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
package edu.stanford.smi.eon.Dharma;
// Created on Wed Nov 22 10:11:21 PST 2000
// Copyright Stanford University 2000

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
 *  Action_Like_Step (Superclass hint for Java class generation)
<p>This class is deprecated
<p>Class representing an alternative for a Choice_Step. 
 */
public class Connector extends DefaultSimpleInstance {

	public Connector(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}

	public void setfirst_objectValue(Instance obj) {
		ModelUtilities.setOwnSlotValue(this, "first_object", obj);	}
	public Instance getfirst_objectValue(){
		return  ((Instance)ModelUtilities.getOwnSlotValue(this, "first_object"));
	}

	public void setsecond_objectValue(Instance obj) {
		ModelUtilities.setOwnSlotValue(this, "second_object", obj);	}
	public Instance getsecond_objectValue(){
		return  ((Instance) ModelUtilities.getOwnSlotValue(this, "second_object"));
	}
// __Code above is automatically generated. Do not change
}
