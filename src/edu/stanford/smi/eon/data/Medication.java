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

// Created on Sat Apr 29 19:18:42 PDT 2000
// Copyright Stanford University 2000

package edu.stanford.smi.eon.data;

import java.io.PrintWriter;
import java.util.Collection;

import org.apache.log4j.*;

/** 
 */
public class Medication extends Data {
	private float daily_dose = (float) 0.0;
	private String daily_dose_unit= null;
	private String drug_name= null;
	private String mood= null;
	private String patient_id= null; 
	private String prescription_id= null;
	private String route= null;
	private String valid_time = null;
	private String status = null;

	public Medication(float daily_dose,
			String daily_dose_unit,
			String drug_name,
			String mood,
			String patient_id, 
			String prescription_id,
			String route,
			String status,
			String valid_time) {
		this.daily_dose= daily_dose;
		this.daily_dose_unit = daily_dose_unit;
		this.drug_name =drug_name;
		this.mood =mood;
		this.patient_id =patient_id;
		this.prescription_id =prescription_id;
		this.route =route;
		this.valid_time =valid_time;
		this.status = status;
	}
  
  static Logger logger=Logger.getLogger(Medication.class);

	public String getValid_time() {
		return valid_time;
	}

	public String getMood() {
		return mood;
	}


	public String getDaily_dose_unit() {
		return daily_dose_unit;
	}

	public float getDaily_dose() {
	    return daily_dose;
	}

	public String getRoute() {
		return route;
	}

	public String getPrescription_id() {
		return prescription_id;
	}

	public String getDrug_name() {
		return drug_name;
	}

	public String getStatus() {
		return status;
	}

	public String getPatient_id() {
		return patient_id;
	}

	@Override
	public void printData(PrintWriter out) {
			out.println("<p>Med: "+patient_id + ", "+drug_name +", "+ ((valid_time != null) ? valid_time : "null") + ", "+
					daily_dose + "</p>");
	}	
		

}
