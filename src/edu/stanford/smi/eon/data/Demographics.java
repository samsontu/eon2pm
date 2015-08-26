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

/** 
 */
public class Demographics extends Data {

	private String patient_id = null;
	private int age = 0;
	private String dob = null;
	private String sex = null;

	public void Patient(int age, 
			String dob, 
			String patient_id, 
			String sex) {
		this.age = age;
		this.dob = dob;
		this.patient_id = patient_id;
		this.sex =sex;
	}

	public String getSex() {
		return sex;
	}

	public int getAge() {
		return age;
	}

	public String getDob() {
		return dob;
	}
	
	public String getPatientId() {
		return patient_id;
	}

	@Override
	public void printData(PrintWriter out) {
		// TODO Auto-generated method stub
		
	}


}
