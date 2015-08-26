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
package edu.stanford.smi.eon.PCAServerModule;
/**
<p>
<ul>
<li> <b>Java Class</b> edu.stanford.smi.eon.PCAServerModule.Substitute_Evaluation
<li> <b>Source File</b> edu/stanford/smi/eon/PCAServerModule/Substitute_Evaluation.java
<li> <b>IDL Source File</b> PCAServer.idl
<li> <b>IDL Absolute Name</b> ::PCAServerModule::Substitute_Evaluation
<li> <b>Repository Identifier</b> IDL:PCAServerModule/Substitute_Evaluation:1.0
</ul>
<b>IDL definition:</b>
<pre>
    #pragma prefix "PCAServerModule"
    struct Substitute_Evaluation {
      string description;
      string name;
      ::PCAServerModule::Add_EvaluationSeq activities_to_start;
      ::PCAServerModule::Delete_EvaluationSeq activities_to_replace;
      ::PCAServerModule::Preference preference;
    };
</pre>
</p>
 */
final public class Substitute_Evaluation implements java.io.Serializable {
	public java.lang.String description;
	public java.lang.String name;
	public edu.stanford.smi.eon.PCAServerModule.Add_Evaluation[] activities_to_start;
	public edu.stanford.smi.eon.PCAServerModule.Delete_Evaluation[] activities_to_replace;
	public edu.stanford.smi.eon.PCAServerModule.Preference preference;
	public int fine_grain_priority;

	public Substitute_Evaluation() {
	}
	public Substitute_Evaluation(
			java.lang.String description,
			java.lang.String name,
			edu.stanford.smi.eon.PCAServerModule.Add_Evaluation[] activities_to_start,
			edu.stanford.smi.eon.PCAServerModule.Delete_Evaluation[] activities_to_replace,
			edu.stanford.smi.eon.PCAServerModule.Preference preference,
			int fine_grain_priority
			) {
		this.description = description;
		this.name = name;
		this.activities_to_start = activities_to_start;
		this.activities_to_replace = activities_to_replace;
		this.preference = preference;
		this.fine_grain_priority = fine_grain_priority;
	}

}
