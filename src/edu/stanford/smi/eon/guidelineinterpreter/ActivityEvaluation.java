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
package edu.stanford.smi.eon.guidelineinterpreter;
import java.util.*;
import edu.stanford.smi.eon.PCAServerModule.Truth_Value;
import java.io.Serializable;

public class ActivityEvaluation implements Serializable {

	public Collection is_first_line_drug_for =null;
	public Collection is_second_line_drug_for = null;
	public Collection is_third_line_drug_for = null;
	public Collection beneficialInteractions =null;
	public Collection compellingIndications = null;
	public Collection contraindications = null;
	public Collection harmful_interactions = null;
	public Collection relative_contraindications = null;
	public Collection relative_indications = null;
	public Collection stop_add_controllable_conditions = null;
	public Collection stop_add_uncontrollable_conditions = null;
	public Collection stop_intensify_conditions = null;
	public Collection side_effects = null;
	public Collection current_use = null;
	public Truth_Value prior_use = Truth_Value.unknown;
	public Truth_Value at_maximum_intensity = Truth_Value.unknown;
	public Truth_Value at_minimum_intensity = Truth_Value.unknown;

	public ActivityEvaluation() {
	}
}