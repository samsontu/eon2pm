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
/*
 * Created on Feb 19, 2006
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License");  you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 *
 * The Initial Developer of the Original Code is Stanford University. Portions
 * created by Stanford University are Copyright (C) 2004.  All Rights Reserved.
 *
 * This code was developed by Stanford Medical Informatics
 * (http://www.smi.stanford.edu) at the Stanford University School of Medicine
 *
 * Contributor(s):
 */
package edu.stanford.smi.eon.pca;

import edu.stanford.smi.eon.inputoutput.AdvisoryFormater;
import edu.stanford.smi.eon.inputoutput.EONXSDConstants;
import gov.va.test.opioidtesttool.PatientDataStore;
import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.eon.util.*;
import edu.stanford.smi.eon.guidelineinterpreter.*;
import edu.stanford.smi.eon.Dharma.*;
import edu.stanford.smi.eon.time.*;
import edu.stanford.smi.eon.clients.ClientUtil;
import edu.stanford.smi.eon.criterion.*;
import edu.stanford.smi.eon.kbhandler.*;
import edu.stanford.smi.eon.datahandler.*;
import edu.stanford.smi.eon.execEngine.EONConstants;
import edu.stanford.smi.eon.execEngine.IEON;

import java.util.*;
import java.lang.reflect.*;
import java.io.*;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import edu.stanford.smi.eon.PCAServerModule.*;
import gov.va.athena.advisory.Action;
import gov.va.athena.advisory.Add_Delete_Drug_Recommendation;
import gov.va.athena.advisory.Advisory;
import gov.va.athena.advisory.Criterion_Evaluation;
import gov.va.athena.advisory.Drug_Recommendation;
import gov.va.athena.advisory.Evaluated_Drug_Relation;
import gov.va.athena.advisory.Increase_Decrease_Dose_Recommendation;
import gov.va.athena.advisory.Missing_Data;
import gov.va.test.opioidtesttool.GlobalVars;

import org.apache.log4j.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Attr;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

public class PCASession_Imp {

	private String database = null;
	private String patient_id;
	private String session_time;
	private String user = null;
	private String password = null;
	private Management_Guideline guideline;
	private String initFile = null;
	private PaddaGEE currentGuidelineManager;
	public KBHandler kbManager;
	public DataHandler dataManager;
	public Map guidelineManagers;
	public Compliance_Level compliance = Compliance_Level.strict;
	private boolean cumulativeFlag = false;
	private String fileExtension = ".data";
	private String PERFORMANCE_MEASURE_PERIOD = "PerformanceMeasurementPeriod";
	private String BEFORE_END_OF_PERFORMANCE_MEASURE_PERIOD = "OnOrBeforePerformancePeriod";
	private String labs[] =
		{"Diabetes Testing",
			"    HbA1C",
			"    Glycosylated_Hemoglobin",
			"    PPG",
			"Blood Chemistry",
			"   Chem7",
			"      Sodium",
			"      Potassium",
			"      Chloride",
			"      Bicarbonate",
			"      BUN",
			"      Creatinine",
			"      eGFR",
			"      Glucose",
			"   Lipid Panel",
			"      Total_Cholesterol",
			"      HDL_Cholesterol",
			"      LDL_Cholesterol",
			"         Calculated_LDL",
			"         Direct_Measure_LDL",
			"      Triglycerides",
			"    Liver panel",
			"        Albumin",
			"        Alkaline_phosphatase",
			"        AST",
			"        ALT",
			"        Total_Bilirubin",
			"    Chemistry panel misc",
			"        Calcium",
			"        Creatine_Phosphokinase_CPK_or_CK",
			"        Phosphorus",
			"        Uric_Acid",
			"        B 12",
			"Urine chemistry",
			"    Creatinine_Clearance",
			"    Microalbumin",
			"CHF tests",
			"    BNP",
			"    NT_proBNP",
			"Hematology",
			"    AGN",
			"    CBC",
			"    HCT",
			"    HGB",
			"    PLT",
			"    WBC",
			"Microbiology_Tests",
			"    Hepatitis_B_Surface_Antigen",
			"    Hepatitis_C_Status",
			"    HIV_Test",
			"Pulmonary_Function_Studies",
			"    DCL",
			"    DLCO",
			"    FEV1",
			"    FVC",
			"respiratory gas exchange",
			"target_HBA1c",
			"Urinalysis",
			"    Urine_Alb/creat_Ratio_24h",
			"    Urine_ALB/CREAT_RATIO_spot",
			"    UrineAlbumin_24H",
			"    UrineAlbumin_SPOT",
			"    UrineProtein_12h",
			"    UrineProtein_24h",
			"    UrineProtein_Qualitative",
			"    UrineProtein_Spot",			
			"Vital_Signs",
			"    Diastolic_BP",
			"        Treatment_Diastolic_BP",
			"        DB_Diastolic_BP",
			"    Systolic_BP",
			"        Treatment_Systolic_BP",
			"        DB_Systolic_BP",
			"    BMI",
			"    Height",
			"    High_Cardiovascular_Risk",
			"    Metabolic_Syndrome",
			"    Pulse",
			"    Weight"

		};



	static Logger logger = Logger.getLogger(PCASession_Imp.class);

	public PCASession_Imp() {
		Management_Guideline guideline = null;
		guidelineManagers = new HashMap();
		database = null;
		this.user = null;
		this.password = null;
		this.initFile = null;

	}

	// should throw exceptions: (1) cannot connect to database
	// (2) cannot load KB
	// (3) guideline not found in KB

	public PCASession_Imp(String databaseString, String user, String password,
			String initFile)
					throws PCA_Initialization_Exception {

		// kbManager = new KBHandler(kbURLString);
		Management_Guideline guideline = null;
		guidelineManagers = new HashMap();
		database = databaseString;
		this.user = user;
		this.password = password;
		this.initFile = initFile;
		logger.debug("constructor-------------------------------");
	}

	public void setSessionId(String sessionID) {
	}

	private void logln(String line) {
		logger.debug(line);

	}

	private Date startTiming(String text) {
		java.util.Date startTime = new java.util.Date();
		logln("\t" + startTime.toString() + text);
		return startTime;
	}

	private Date stopTiming(String text, Date startTime) {
		java.util.Date stopTime = new java.util.Date();
		logln("\t" + stopTime.toString() + text+"\t" + (stopTime.getTime() - startTime.getTime())
				+ " milliseconds");
		return stopTime;
	}

	public PCASession_Imp(KBHandler kbhandler, String databaseString,
			String user, String pword, String initfile)
					throws PCA_Initialization_Exception {

		kbManager = kbhandler;
		dataManager = new DataHandler(databaseString, kbManager, user, pword,
				initfile);
		Management_Guideline guideline = null;
		guidelineManagers = new HashMap();
		database = databaseString;
		this.user = user;
		this.initFile = initFile;
		currentGuidelineManager = new GuidelineInterpreter(kbManager,
				dataManager,
				// this,
				null);
		guidelineManagers.put("None", currentGuidelineManager);

	}

	public void initPCASesion(KBHandler kbhandler)
			throws PCA_Initialization_Exception {
		kbManager = kbhandler;
		dataManager = new DataHandler(database, kbManager, user, password,
				initFile);
		currentGuidelineManager = new GuidelineInterpreter(kbManager,
				dataManager,
				// this,
				null);
		guidelineManagers.put("None", currentGuidelineManager);
	}

	public void initPCASesion(KBHandler kbhandler, DataHandler dataHandler)
			throws PCA_Initialization_Exception {
		kbManager = kbhandler;
		dataManager = dataHandler;

		currentGuidelineManager = new GuidelineInterpreter(kbManager,
				dataManager,
				// this,
				null);
		guidelineManagers.put("None", currentGuidelineManager);
	}



	/**
	 * <p>
	 * Reader for attribute: <b>::PCAServerModule::PCASession::database</b>.
	 * 
	 * <pre>
	 * 	    #pragma prefix &quot;PCAServerModule/PCASession&quot;
	 * 	    attribute string database;
	 * 	
	 * </pre>
	 * 
	 * </p>
	 */
	public java.lang.String database() {
		return database;
	}

	/**
	 * <p>
	 * Reader for attribute: <b>::PCAServerModule::PCASession::session_time</b>.
	 * 
	 * <pre>
	 * 	    #pragma prefix &quot;PCAServerModule/PCASession&quot;
	 * 	    attribute string session_time;
	 * 	
	 * </pre>
	 * 
	 * </p>
	 */
	public java.lang.String session_time() {
		return session_time;
	}

	/**
	 * <p>
	 * Reader for attribute: <b>::PCAServerModule::PCASession::patient_id</b>.
	 * 
	 * <pre>
	 * 	    #pragma prefix &quot;PCAServerModule/PCASession&quot;
	 * 	    attribute string patient_id;
	 * 	
	 * </pre>
	 * 
	 * </p>
	 */
	public java.lang.String patient_id() {
		return patient_id;
	}

	/**
	 * <p>
	 * Reader for attribute: <b>::PCAServerModule::PCASession::guideline_name</b>.
	 * 
	 * <pre>
	 * 	    #pragma prefix &quot;PCAServerModule/PCASession&quot;
	 * 	    readonly attribute string guideline_name;
	 * 	
	 * </pre>
	 * 
	 * </p>
	 */
	public java.lang.String guideline_name() {
		return this.guideline.getlabelValue();
	}

	/**
	 * <p>
	 * Operation: <b>::PCAServerModule::PCASession::setGuideline</b>.
	 * 
	 * <pre>
	 * 	    #pragma prefix &quot;PCAServerModule/PCASession&quot;
	 * 	    void setGuideline(
	 * 	      in string guideline_name
	 * 	    )
	 * 	    raises(
	 * 	      ::PCAServerModule::PCA_Session_Exception,
	 * 	      ::PCAServerModule::PCA_Initialization_Exception
	 * 	    );
	 * 	
	 * </pre>
	 * 
	 * </p>
	 */
	public void setGuideline(java.lang.String guideline_name)
			throws PCA_Session_Exception,
			PCA_Initialization_Exception {

		// GenDrugRelations.genAllDrugRelations(kbManager.kb);
		// GenDrugRelations.moveAllDrugRelations(kbManager.kb);
		logger.debug("setGuideline()--------------");
		logger.debug("In PCASession: " + this.toString() + " setGuideline: "
				+ guideline_name);
		java.lang.Object[] guidelines = (kbManager.findInstances(
				"Management_Guideline", new WhereComparisonFilter("label",
						"eq", guideline_name),
						(GuidelineInterpreter) currentGuidelineManager)).toArray();

		logger.debug(Array.getLength(guidelines));

		if (Array.getLength(guidelines) == 0) {
			logger.error("Guideline "+guideline_name+" not found");
			throw new PCA_Initialization_Exception("guideline not found");
		}
		if (Array.getLength(guidelines) > 1) {
			throw new PCA_Initialization_Exception("guideline ambiguous");
		} else {
			logger.debug("guidelines[0]: " + guidelines[0].toString()
					+ "Direct class:" + guidelines[0].getClass().toString());
			guideline = (Management_Guideline) guidelines[0];

			PaddaGEE guidelineManager;
			for (Iterator i = guidelineManagers.values().iterator(); i
					.hasNext();) {
				guidelineManager = (PaddaGEE) i.next();
				guidelineManager.resetAdvisories();
				guidelineManager.setGuideline(guideline);
			}
		}

	}

	public void ping() {
		logln("PCAServer getting pinged");
	}

	/**
	 * <p>
	 * Operation: <b>::PCAServerModule::PCASession::finishSession</b>.
	 * 
	 * <pre>
	 * 	    #pragma prefix &quot;PCAServerModule/PCASession&quot;
	 * 	    void finishSession();
	 * 	
	 * </pre>
	 * 
	 * </p>
	 */
	public void finishSession() {
		dataManager.clearRuntimeInstances();
		logln("Session finished");
	}

	/**
	 * <p>
	 * Operation: <b>::PCAServerModule::PCASession::abortSession</b>.
	 * 
	 * <pre>
	 * 	    #pragma prefix &quot;PCAServerModule/PCASession&quot;
	 * 	    void abortSession();
	 * 	
	 * </pre>
	 * 
	 * </p>
	 */
	public void abortSession() {
		dataManager.clearRuntimeInstances();
		logln("Session aborted");
	}

	/**
	 * <p>
	 * Operation: <b>::PCAServerModule::PCASession::useCase</b>.
	 * 
	 * <pre>
	 * 	    #pragma prefix &quot;PCAServerModule/PCASession&quot;
	 * 	    void useCase(
	 * 	      in string patient_id,
	 * 	      in string session_time
	 * 	    )
	 * 	    raises(
	 * 	      ::PCAServerModule::PCA_Session_Exception
	 * 	    );
	 * 	
	 * </pre>
	 * 
	 * </p>
	 */

	/**
	 * <p>
	 * Reader for attribute: <b>::PCAServerModule::PCASession::compliance</b>.
	 * 
	 * <pre>
	 * 	    #pragma prefix &quot;PCAServerModule/PCASession&quot;
	 * 	    readonly attribute ::PCAServerModule::Compliance_Level compliance;
	 * 	
	 * </pre>
	 * 
	 * </p>
	 */
	public Compliance_Level compliance() {
		return this.compliance;
	}

	/**
	 * <p>
	 * Writer for attribute: <b>::PCAServerModule::PCASession::compliance</b>.
	 * 
	 * <pre>
	 * 	    #pragma prefix &quot;PCAServerModule/PCASession&quot;
	 * 	    attribute ::PCAServerModule::Compliance_Level compliance;
	 * 	
	 * </pre>
	 * 
	 * </p>
	 */
	public void compliance(
			Compliance_Level compliance) {
		this.compliance = compliance;
	}

	public String setDummyCase() {
		Date currentTime = new java.util.Date();
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
				"yyyy-MM-dd");
		String patient_id = Long.toString(currentTime.getTime());
		dataManager.changeCase(patient_id, formatter.format(currentTime));
		this.patient_id = patient_id;
		this.session_time = formatter.format(currentTime);
		PaddaGEE guidelineManager;

		for (Iterator i = guidelineManagers.values().iterator(); i.hasNext();) {
			guidelineManager = (PaddaGEE) i.next();
			guidelineManager.resetAdvisories();
			guidelineManager.setDBmanager(dataManager);
		}
		return patient_id;
	}

	public String setDummyCase(String patient_id) {
		Date currentTime = new java.util.Date();
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
				"yyyy-MM-dd");
		dataManager.changeCase(patient_id, formatter.format(currentTime));
		this.patient_id = patient_id;
		this.session_time = formatter.format(currentTime);
		PaddaGEE guidelineManager;

		for (Iterator i = guidelineManagers.values().iterator(); i.hasNext();) {
			guidelineManager = (PaddaGEE) i.next();
			guidelineManager.resetAdvisories();
			guidelineManager.setDBmanager(dataManager);
		}
		return patient_id;
	}
	private boolean fromFile(String patientCase) {
		File caseFile = new File(patientCase);
		if (caseFile.canRead())
			return true;
		else
			return false;
	}

	private String getPID(String patientCase) {
		File caseFile = new File(patientCase);
		String fileName = caseFile.getName();
		int index = fileName.lastIndexOf(fileExtension);
		if (index != -1) {
			return fileName.substring(0, index);
		} else
			return fileName;
	}

	private String getDirectory(String patientCase) {
		File caseFile = new File(patientCase);
		return caseFile.getParent();
	}

	public void setCase(java.lang.String patient_id,
			java.lang.String session_time)
					throws PCA_Initialization_Exception,
					PCA_Initialization_Exception {
		boolean fromFile = false;
		String storageDirectory = null;
		if (fromFile(patient_id)) {
			fromFile = true;
			String completePath = patient_id;
			patient_id = getPID(completePath);
			storageDirectory = getDirectory(completePath);
			this.logger.info("Loading patient case " + patient_id
					+ " from file");
		}
		dataManager.changeCase(patient_id, session_time);
		this.patient_id = patient_id;
		this.session_time = session_time;
		this.logln("PID" + patient_id + "\t[" + session_time
				+ "]");
		try {
			if (fromFile) {
				Date startTime = startTiming("\tstarting loading case");
				dataManager.fetchDataFromFile(storageDirectory, fileExtension);
				stopTiming("\tfinished loading case, taking @@@@@@@@@ ", startTime);
			} else {
				//logger.info("setCase no longer loads data from database. Equivalent to setDummyCase, but with patient id and session time");
				dataManager.loadData();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn(
					"Problem loading patient data for " + patient_id+" using old load method. Assume data is being loaded using modern method");
			
		}


		// reinitialize Guideline Manager
		PaddaGEE guidelineManager;
		for (Iterator i = guidelineManagers.values().iterator(); i.hasNext();) {
			guidelineManager = (PaddaGEE) i.next();
			guidelineManager.resetAdvisories();
			guidelineManager.setDBmanager(dataManager);
		}
		Runtime rt = Runtime.getRuntime();
		/*		logger.info("PCASession_i.setCase() Free memory: " + rt.freeMemory()
				+ " Total memory: " + rt.totalMemory());
		 */		rt.gc();
		 		logger.debug("PCASession_i.setCase() Post GC Free memory: "
		 				+ rt.freeMemory() + " Total memory: " + rt.totalMemory());
	}

	/**
	 * <p>
	 * Operation: <b>::PCAServerModule::PCASession::initializeSession</b>.
	 * 
	 * <pre>
	 * 	    #pragma prefix &quot;PCAServerModule/PCASession&quot;
	 * 	    ::PCAServerModule::Guideline_Service_Record initializeSession();
	 * 	
	 * </pre>
	 * 
	 * This method fetches precomputed advisories
	 * </p>
	 */
	public void loadPrecomputedAdvisories(String patient_id,
			String session_time, String storageDirectory)
					throws PCA_Session_Exception {
		// currentGuidelineManager.loadPrecomputedAdvisories();
		this.patient_id = patient_id;
		this.session_time = session_time;
		Date currentTime = new Date();
		this.logln("PID" + patient_id + "\t" + currentTime.toString());
		dataManager.changeCase(patient_id, session_time);
		Date startComputeTime = startTiming("\tstart attempting reloading precomputed advisories "
				+ storageDirectory + patient_id + "strict");

		File StorageLocation = new File(storageDirectory, patient_id + "strict");
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
					StorageLocation));
			guidelineManagers = new HashMap();
			try {
				while (true) {
					PaddaGEE guidelineManager = new GuidelineInterpreter(
							kbManager, dataManager, guideline);
					String key = (String) in.readObject();
					logger.debug("entry key: " + key);
					guidelineManager.restoreAdvisories(in);
					guidelineManagers.put(key, guidelineManager);
					logger
					.debug("PCASession_i.loadPrecomputedAdvisories put away: "
							+ key);
				}
			} catch (Exception e) {
				logger.info("\tfinished restoring saved recommendations ");
				// e.printStackTrace();
			}
			in.close();
		} catch (Exception e) {
			// e.printStackTrace();
			logger.error("problem opening precomputed file");
			throw new PCA_Session_Exception(e.getMessage());
		} finally {
			stopTiming(
					"\tFinished attempting reloading precomputed advisories, taking @@@@@@@@@ ",
					startComputeTime);
			Runtime rt = Runtime.getRuntime();
			// logger.debug("PCASession_i.setCase() Free memory: "+
			// rt.freeMemory()+" Total memory: "+
			// rt.totalMemory(), 0);
			rt.gc();
			logger.debug("PCASession_i.setCase() Post GC Free memory: "
					+ rt.freeMemory() + " Total memory: " + rt.totalMemory());

		}
	}

	public void saveAdvisoriesAs(String patient_id, String storageDirectory)
			throws PCA_Session_Exception {
		this.patient_id = patient_id;
		Date currentTime = new Date();
		this.logln("PID" + patient_id + "\t" + currentTime.toString());
		dataManager.renameCase(patient_id);
		logger.debug("PCASession_i.saveAdvisoriesAs ");
		File StorageLocation = new File(storageDirectory, patient_id
				+ compliance);
		try {
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(StorageLocation));
			for (Iterator i = guidelineManagers.entrySet().iterator(); i
					.hasNext();) {
				Map.Entry entry = (Map.Entry) i.next();
				logger.debug("entry key: " + entry.getKey());
				out.writeObject(entry.getKey());
				((PaddaGEE) entry.getValue()).serializeAdvisories(out);
				logger.debug("put away: " + entry.getKey());

			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new PCA_Session_Exception(e.getMessage());
		}
	}

	/**
	 * <p>
	 * Operation: <b>::PCAServerModule::PCASession::updateRecommendations</b>.
	 * 
	 * <pre>
	 * 	    #pragma prefix &quot;PCAServerModule/PCASession&quot;
	 * 	    ::PCAServerModule::Guideline_Service_Record updateRecommendations();
	 * 	
	 * </pre>
	 * 
	 * </p>
	 */
	/**
	 * <p>
	 * Operation: <b>::PCAServerModule::PCASession::containsAdvisories</b>.
	 * 
	 * <pre>
	 * 	    #pragma prefix &quot;PCAServerModule/PCASession&quot;
	 * 	    boolean containsAdvisories();
	 * 	
	 * </pre>
	 * 
	 * </p>
	 */
	public boolean containsAdvisories() {
		Collection managers = guidelineManagers.values();
		boolean hasAdvisories = false;
		for (Iterator i = managers.iterator(); i.hasNext();) {
			if (((PaddaGEE) i.next()).containsAdvisories())
				hasAdvisories = true;
		}
		if (hasAdvisories)
			logger.debug("Has advisory ready");
		else
			logger.debug("Does not have advisory ready");
		return hasAdvisories;
	}

	public void resetAdvisories() {
		Collection managers = guidelineManagers.values();
		PaddaGEE guidelineManager;
		for (Iterator i = managers.iterator(); i.hasNext();) {
			guidelineManager = (PaddaGEE) i.next();
			guidelineManager.resetAdvisories();
		}
	}

	/**
	 * <p>
	 * Operation: <b>::PCAServerModule::PCASession::getAdvisories</b>.
	 * 
	 * <pre>
	 * 	    #pragma prefix &quot;PCAServerModule/PCASession&quot;
	 * 	    ::PCAServerModule::Guideline_Service_Record getAdvisories();
	 * 	
	 * </pre>
	 */
	public Guideline_Service_Record[] getAdvisories() {
		Collection managers = guidelineManagers.values();
		Collection results = new ArrayList();
		for (Iterator i = managers.iterator(); i.hasNext();) {
			PaddaGEE g = (PaddaGEE) i.next();
			if (g.containsAdvisories())
				results.add(g.returnAdvisory());
		}

		return (Guideline_Service_Record[]) results
				.toArray(new Guideline_Service_Record[0]);
	}

	public Guideline_Service_Record[] updateAdvisories() {
		// Date startTime = startTiming("\tstart update advisories");
		//		logger.info("kbmanag: "+kbManager.toString());
		try {
			//
			Guideline_Service_Record[] adv = computeAdvisories();

			// stopTiming("\tfinished update advisories", startTime);
			return adv;
		} catch (Exception e) {
			logger.error("Error ComputeAdvisory ", e);
			return null;
		}
	}

	private void testServiceRecord(Guideline_Service_Record gsr) {
		// gsr.activity_choices = new Guideline_Activity_Choices[0];
		// gsr.decision_points = new Guideline_Action_Choices[0];
		// gsr.evaluated_choices= new Guideline_Activity_Evaluations[0];
		// gsr.scenario_choices = new Guideline_Scenario_Choices[0] ;
		// gsr.goals = new Guideline_Goal[0];
		// gsr.subject_classification = new Conclusion[0];
		for (int j = 0; j < gsr.subject_classification.length; j++) {
			gsr.subject_classification[j].justification = edu.stanford.smi.eon.util.HelperFunctions
					.dummyJustification();
			logger.debug("case_id: " + gsr.subject_classification[j].case_id
					+ " time: " + gsr.subject_classification[j].time
					+ " parameter: " + gsr.subject_classification[j].parameter
					+ " value: " + gsr.subject_classification[j].value);
		}
		// gsr.assessments = new Current_Activity_Assessment[0];
		// gsr.assumption = dummyCriteria_Evaluation();
		/*
		 * gsr.assumption = dummyCriteria_Evaluation(Logical_Operator.ATOMIC,
		 * Truth_Value.unknown, new Criteria_Evaluation[0], new
		 * Guideline_Entity("", "", "", ""), "");
		 */
	}

	public Guideline_Service_Record[] computeAndStoreAdvisories(
			String storageDirectory) throws PCA_Session_Exception {
		Guideline_Service_Record[] advisories;
		File StorageLocation = new File(storageDirectory, patient_id
				+ compliance);
		logger.info("Storage location:" + StorageLocation);
		advisories = computeAdvisories();
		try {
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(StorageLocation));
			for (Iterator i = guidelineManagers.entrySet().iterator(); i
					.hasNext();) {
				Map.Entry entry = (Map.Entry) i.next();
				logger.debug("entry key: " + entry.getKey());
				out.writeObject(entry.getKey());
				((PaddaGEE) entry.getValue()).serializeAdvisories(out);
				logger.debug("put away: " + entry.getKey());

			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new PCA_Session_Exception(e.getMessage());
		}
		getDataManager().storeDataToFile(storageDirectory, fileExtension);
		return advisories;
	}



	public String topLevelComputeAdvisory(String ptID, String patientData, String currentTime, String guidelineName) {
		String XMLAdvisory = null;
		Collection<Patient_Data> ptDataArray = readPatientData(patientData, ptID).create_patient_data_array();
		Guideline_Service_Record[] dssOutput =null;
		if (ptDataArray != null) {
			Patient_Data[] data = (Patient_Data[]) ptDataArray.toArray(new Patient_Data[0]);
			try {
				setCompliance(Compliance_Level.strict);
				resetAdvisories();
				setCase(ptID, currentTime);
				updateData(data);
				dssOutput = updateAdvisories();
				XMLAdvisory = generateXMLAdvisory(ptID, dssOutput, kbManager, currentTime, guidelineName);
				finishSession();
			}
			catch (Exception e) {
				System.out.println("computeAndSaveAdvisory error for  patient: " + ptID);
				e.printStackTrace();
			}

		} else {
			System.out.println("ptDataArray is null for patient case "+ptID);
		}
		return XMLAdvisory;
	}


	public String topLevelComputeAdvisory (String ptID, String patientData, String currentTime, String guidelineName,
			String filename) {
		String XMLAdvisory = null;
		Collection<Patient_Data> ptDataArray = readPatientData(patientData, ptID).create_patient_data_array();
		Guideline_Service_Record[] dssOutput =null;
		if (ptDataArray != null) {
			Patient_Data[] data = (Patient_Data[]) ptDataArray.toArray(new Patient_Data[0]);
			File htmlFile = new File(filename);;
			if (htmlFile.exists()) {
				htmlFile.delete();
			}
			try {
				setCompliance(Compliance_Level.strict);
				resetAdvisories();
				setCase(ptID, currentTime);
				updateData(data);
				dssOutput = updateAdvisories();
				XMLAdvisory = generateXMLAdvisory(ptID, dssOutput, kbManager, currentTime, guidelineName);
				PrintWriter out = new PrintWriter(htmlFile);
				ClientUtil.printHeader(out, ptID);
				out.print(this.printData(IEON.HTML));
				if (dssOutput != null) {
					for (int j=0; j< dssOutput.length; j++) {
						ClientUtil.showResultWithKB(dssOutput[j], out, kbManager.getKB());
					}
					out.close();
				}
				finishSession();
			}
			catch (Exception e) {
				System.out.println("configuredTopLevelComputeAdvisory error for  patient: " + ptID);
				e.printStackTrace();
			}

		} else {
			System.out.println("ptDataArray is null for patient case "+ptID);
		}
		return XMLAdvisory;		

	}

	public String getExplanationURLs() {
		DocumentBuilder docBuilder =null;
		boolean validating = true;
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		docFactory.setValidating(validating);
		try {
			docBuilder = docFactory.newDocumentBuilder();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("explanation_relations");
		doc.appendChild(rootElement);
		Cls drugRelationCls = kbManager.getCls("Drug_Indication_Relation");
		Slot supportSlot = kbManager.getKB().getSlot("supporting_materials");
		Collection<Instance> instances= drugRelationCls.getInstances();
		for (Instance inst : instances) {
			Instance support = (Instance) inst.getOwnSlotValue(supportSlot);
			if (support != null) {
				doc = getXMLRelation(inst, support, doc, rootElement);
			}
		}
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		String explanatoryRelations = null;
		Transformer transformer;
		try {
			transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			Writer outWriter = new StringWriter();  
			StreamResult result = new StreamResult( outWriter ); 
			transformer.transform(source, result);
			explanatoryRelations = result.getWriter().toString();
		} catch (TransformerConfigurationException e1) {

			e1.printStackTrace();
		} catch (TransformerException e) {

			e.printStackTrace();
		}
		return explanatoryRelations;

	}

	Document getXMLRelation(Instance inst, Instance support, Document doc, Element root){
		Slot indication = kbManager.getKB().getSlot("indication");
		Slot drug  = kbManager.getKB().getSlot("Drug");
		Slot degree =  kbManager.getKB().getSlot("degree");
		Slot URL = kbManager.getKB().getSlot("URL");
		String indicationString = ((Cls)inst.getOwnSlotValue(indication)).getName();
		String degreeString = (String)inst.getOwnSlotValue(degree);
		String drugString = ((Cls)inst.getOwnSlotValue(drug)).getName();
		String relation = null;
		if (degreeString.equals("relative"))
			relation = "relative_indication";
		else relation = "compelling_indication";
		Element relationElement = doc.createElement(relation);
		String URLValue = (String)support.getOwnSlotValue(URL);
		root.appendChild(relationElement);
		addSimpleValue(doc, relationElement, EONXSDConstants.ACTIVITY, drugString);
		addSimpleValue(doc, relationElement, "condition", indicationString);
		addSimpleValue(doc, relationElement, "URL", URLValue);
		return doc;

	}



	private Element addSimpleValue(Document doc, Element root,
			String property, String value) {
		Element elt = null;
		if (value != null) {
			elt = doc.createElement(property);
			Text data = doc.createTextNode(value);
			elt.appendChild(data);
			root.appendChild(elt);
		}	
		return elt;

	}

	public Guideline_Service_Record[] computeAdvisories()
			throws PCA_Session_Exception {
		Guideline_Service_Record[] advisorySet;
		Collection conclusions = new ArrayList();
		Guideline_Goal evaluatedGoal = null;
		logger.debug("+++++++ --- Computer Advisories");
		Date startTime = startTiming("\tstart compute advisories");

		if (guideline == null) {
			logger
			.error("PCASession_i-computeAdvisories Exception: Null guideline!!");
			throw new PCA_Session_Exception(
					"PCASession_i-computeAdvisories: Null guideline!!");
		}
		// clean up previous computations, drop previous assumption-based
		// recommendatiaons
		try {
			guidelineManagers = new HashMap();

			//			logger.debug("guideline++++++++-------"+ kbManager + " " 
			//					+ dataManager + " " + guideline);
			currentGuidelineManager = new GuidelineInterpreter(kbManager,
					dataManager, guideline);			

			// currentGuidelineManager.resetAdvisories();
			guidelineManagers.put("None", currentGuidelineManager);

			logger.debug("[1] guideline++++++++-------"+ guideline);
			dataManager.deleteRecommendations();

			// First, check eligibility
			// construct an AND criteria
			Conclusion eligibility = currentGuidelineManager
					.checkGuidelineApplicability(guideline, patient_id,
							session_time);

			if (eligibility != null) {
				conclusions.add(eligibility);
				currentGuidelineManager.addConclusions(conclusions);

				if (eligibility.justification.evaluation.truth_value
						.equals(Truth_Value._false)) {
					// Not eligible
					advisorySet = new Guideline_Service_Record[1];
					advisorySet[0] = new Guideline_Service_Record(
							HelperFunctions.dummyCriteriaEvaluation(),
							new Guideline_Action_Choices[0],
							new Guideline_Activity_Choices[0],
							new Guideline_Activity_Evaluations[0],
							new Guideline_Scenario_Choices[0],
							new Guideline_Goal[0], (Conclusion[]) conclusions
							.toArray(new Conclusion[0]),
							new Current_Activity_Assessment[0],
							new Data_To_Collect[0],
							currentGuidelineManager.getCurrentGuidelineID()
							);
					return advisorySet;
				}
			}
			// **************** Eligible, or there is no eligibility criteria,
			// evaluate goals
			Collection guidelineGoals = guideline.getgoalValue();
			Slot slot = null;
			String assumption = "";
			if ((slot = kbManager.getKB().getSlot("assume_if_no_goal_value")) == null) {
				assumption = "make_alternative_assumptions"; // guidelineGoals.getassume_if_unknownValue();
			} else if (guideline.getOwnSlotValue(slot) != null)
				assumption = (String) guideline.getOwnSlotValue(slot);
			else
				assumption = "no_assumption";
			logger.debug("computeAdvisory: assumption: " + assumption);
			if ((guidelineGoals != null) && (!guidelineGoals.isEmpty())) {
				evaluatedGoal = currentGuidelineManager.computeConditionalGoals(
						guidelineGoals, guideline
						.makeGuideline_Entity(guideline.getName()));
				// if goal is unknown, then make alternative assumptions
				if (evaluatedGoal != null) {
					if (evaluatedGoal.achieved.equals(Goal_State.unknown)) {
						if (assumption != null) {
							if (assumption
									.equals("make_alternative_assumptions")) {
								advisorySet = new Guideline_Service_Record[2];
								currentGuidelineManager.retractGoals();
								setAssumption(evaluatedGoal,
										currentGuidelineManager,
										Truth_Value._true, "assume_satisfied");
								advisorySet[0] = computeAdvisory(currentGuidelineManager);
								guidelineManagers.clear();
								guidelineManagers.put("assume_satisfied",
										currentGuidelineManager);

								currentGuidelineManager = new GuidelineInterpreter(
										kbManager, dataManager, guideline,
										conclusions);
								setAssumption(evaluatedGoal,
										currentGuidelineManager,
										Truth_Value._false,
										"assume_unsatisfied");
								advisorySet[1] = computeAdvisory(currentGuidelineManager);
								guidelineManagers.put("assume_unsatisfied",
										currentGuidelineManager);
								return returnAdvisorySet(advisorySet);

							} else if (assumption.equals("assume_satisfied")) {
								currentGuidelineManager.retractGoals();
								setAssumption(evaluatedGoal,
										currentGuidelineManager,
										Truth_Value._true, "assume_satisfied");
								advisorySet = new Guideline_Service_Record[1];
								advisorySet[0] = computeAdvisory(currentGuidelineManager);
								guidelineManagers.clear();
								guidelineManagers.put("assume_satisfied",
										currentGuidelineManager);
								return returnAdvisorySet(advisorySet);
							} else if (assumption.equals("assume_unsatisfied")) {
								currentGuidelineManager.retractGoals();
								setAssumption(evaluatedGoal,
										currentGuidelineManager,
										Truth_Value._true, "assume_satisfied");
								advisorySet = new Guideline_Service_Record[1];
								advisorySet[0] = computeAdvisory(currentGuidelineManager);
								guidelineManagers.clear();
								guidelineManagers.put("assume_unsatisfied",
										currentGuidelineManager);
								return returnAdvisorySet(advisorySet);
							}
						}
					}
				}
			}
			advisorySet = new Guideline_Service_Record[1];
			advisorySet[0] = computeAdvisory(currentGuidelineManager);
			stopTiming("\t finished computing advisories, taking @@@@@@@@@ ", startTime);
			return returnAdvisorySet(advisorySet);
		} catch (Throwable e) {
			logger.error("PCASession_i-computeAdvisories Exception!!", e);

			throw new PCA_Session_Exception(e.getMessage());

		}

	}

	public String generateXMLAdvisory(String ptID, Guideline_Service_Record[] dssOutput,
			KBHandler kbmanager, String currentTime, String guidelineName ) {
		Document xmlDoc = edu.stanford.smi.eon.inputoutput.ClientUtilXML.makeXMLOutput( dssOutput, ptID, guidelineName, kbmanager.getKB());											
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		String advisoryXML = null;
		Transformer transformer;
		try {
			transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(xmlDoc);
			Writer outWriter = new StringWriter();  
			StreamResult result = new StreamResult( outWriter ); 
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(source, result);
			advisoryXML = result.getWriter().toString();
		} catch (TransformerConfigurationException e1) {

			e1.printStackTrace();
		} catch (TransformerException e) {

			e.printStackTrace();
		}
		return advisoryXML;

	}


	public PatientDataStore readPatientData(String xmlString, String ptID)  {
		PatientDataStore pdss = null;
		Document doc = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		StringReader reader = new StringReader(xmlString);
		InputSource inputSource = new InputSource(reader);

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			//doc = builder.parse( new File(GlobalVars.XMLDataFile) ); 
			doc = builder.parse(inputSource);

			if (doc == null)
				System.out.println("EONGEEMethod.PatientDataStore: data input is NULL ------------!");

			Element root = doc.getDocumentElement();

			Element e = null;
			NodeList nl = root.getElementsByTagName("patient");
			for (int i =0; i< nl.getLength(); i++) {
				Attr ia = ((Element)nl.item(i)).getAttributeNode("name");
				if ((ia != null) && (ia.getValue() !=null) && ia.getValue().equals(ptID))
					e = (Element)nl.item(i);
			}
			if (e != null)
				pdss = new PatientDataStore(e);
			else {
				logger.error("No data for patient case "+ptID);
				throw new Improper_Data_Exception();
			}
		} catch (Exception e) {
			System.err.println("Error READING xml patient data file");
			System.err.println("Going to default to no patients read");
			e.printStackTrace();
		}
		reader.close();
		return pdss;
	}

	public void updateData(
			Patient_Data[] data)
					throws Improper_Data_Exception {
		
		for (int i = 0; i < Array.getLength(data); i++) {
			Patient_Data dataElement = data[i];
			switch (dataElement.discriminator().value()) {
			case Patient_Data_Type._numeric_data:
				logger.debug("in updateData - Numeric Data: operation "
						+ dataElement.numeric().operation + " domain term:"
						+ dataElement.numeric().domain_term + "; value: "
						+ dataElement.numeric().value + "; unit: "
						+ dataElement.numeric().unit + "; time: "
						+ dataElement.numeric().valid_time);
				if ((dataElement.numeric().valid_time != null) && !dataElement.numeric().valid_time.equals("")) {
					try {
						int numericTime = (int) (HelperFunctions.Day2Int2(dataElement.numeric().valid_time));
						if (numericTime <= ((int) HelperFunctions.Day2Int2(this.session_time))) {
							dataManager.updateNumericData(dataElement.numeric().operation,
									dataElement.numeric().domain_term.trim(),
									dataElement.numeric().unit,
									dataElement.numeric().valid_time,
									dataElement.numeric().value);
						} else
							logger.info(dataElement.numeric().domain_term + "'s valid time is after session time");
					} catch (ParseException e) {
						logger.error("Either session time "+this.session_time()+ " or "+ dataElement.numeric().domain_term+" time "+dataElement.numeric().valid_time +
								" is not valid");
					}
				} else {
					logger.info(dataElement.numeric().domain_term  + " has null or empty valid time");
				}
				break;
			case Patient_Data_Type._prescription_data:
				if (dataElement.prescription() != null) {
					logger.debug("in updateData -Prescription Data: drug_name:"
							+ dataElement.prescription().drug_name + "; dose: "
							+ dataElement.prescription().daily_dose
							+ "; unit: "
							+ dataElement.prescription().daily_dose_unit
							+ "; start time: "
							+ dataElement.prescription().start_time  
							+ "; stop time: "
							+ dataElement.prescription().stop_time+ "; sig: "
							+ dataElement.prescription().sig);
					if ((dataElement.prescription().start_time != null) && !dataElement.prescription().start_time.equals("")) {
						try {
							int prescriptionStartTime = (int) (HelperFunctions.Day2Int2(dataElement.prescription().start_time));
							if (prescriptionStartTime <= ((int) HelperFunctions.Day2Int2(this.session_time))) {
								dataManager.updatePrescription(
										dataElement.prescription().operation,
										dataElement.prescription().drug_name.trim(),
										dataElement.prescription().daily_dose,
										dataElement.prescription().daily_dose_unit,
										dataElement.prescription().start_time,
										dataElement.prescription().stop_time,
										getCumulativeFlag(),
										dataElement.prescription().sig,
										dataElement.prescription().medication_possession_ratio,
										dataElement.prescription().present_release_time);
							} else
								logger.info(dataElement.prescription().drug_name.trim() + "'s start time is after session time");
						} catch (ParseException e) {
							logger.error("Either session time "+this.session_time()+ " or "+ dataElement.prescription().drug_name.trim()+" start time "+dataElement.prescription().start_time +
									" is not valid");
						}
					} else {
						dataManager.updatePrescription(
								dataElement.prescription().operation,
								dataElement.prescription().drug_name.trim(),
								dataElement.prescription().daily_dose,
								dataElement.prescription().daily_dose_unit,
								dataElement.prescription().start_time,
								dataElement.prescription().stop_time,
								getCumulativeFlag(),
								dataElement.prescription().sig,
								dataElement.prescription().medication_possession_ratio,
								dataElement.prescription().present_release_time);
					}
				} else
					logger.warn("in updateData - null prescription");
				break;
			case Patient_Data_Type._note_entry_data:
				logger.debug("in updateData - Note Etnry Data: operation "
						+ dataElement.note_data().operation + " entry_type:"
						+ dataElement.note_data().entry_type + " domain term:"
						+ dataElement.note_data().domain_term + "; value: "
						+ dataElement.note_data().value);
				if ((dataElement.note_data().valid_time != null) && !dataElement.note_data().valid_time.equals("")) {
					int noteTime = (int) (HelperFunctions.DateTime2Int(dataElement.note_data().valid_time)/(60 * 24));
					try {
						if (noteTime <= ((int) HelperFunctions.Day2Int2(this.session_time))) {
							dataManager.updateNoteEntry(dataElement.note_data().operation,
									dataElement.note_data().entry_type,
									dataElement.note_data().domain_term.trim(),
									dataElement.note_data().value,
									dataElement.note_data().valid_time);
						} else
							logger.info(dataElement.note_data().domain_term + "'s valid time is after session time");
					} catch (ParseException e) {
						logger.error("Either session time "+this.session_time()+ " or "+ dataElement.note_data().domain_term+" time "+dataElement.note_data().valid_time +
								" is not valid");
					}
				} else {
					if ((!dataElement.note_data().domain_term.equals(DharmaPaddaConstants.Sex) && 
							!dataElement.note_data().domain_term.equals(DharmaPaddaConstants.Race)))
							logger.info(dataElement.note_data().domain_term  + " has null or empty valid time");
					//Sex and Race has no valid time
					dataManager.updateNoteEntry(dataElement.note_data().operation,
							dataElement.note_data().entry_type,
							dataElement.note_data().domain_term.trim(),
							dataElement.note_data().value,
							null);
				}

				break;
			default:
				;
			} // switch
		} // for

	}

	public boolean getCumulativeFlag() {
		return cumulativeFlag;
	}
	
	public void setCumulativeFlag(boolean cumulativeFlag) {
		this.cumulativeFlag = cumulativeFlag;
	}

	//////////////////////////////////////////////
	// Inserted for computing eligibility and Goal-satisfied
	//
	public boolean computeEligibilityAndGoal()	
			throws PCA_Session_Exception {
		Guideline_Service_Record[] advisorySet;
		Collection conclusions = new ArrayList();
		Guideline_Goal evaluatedGoal = null;

		logger.debug("[computeEligibilityAndGoal] started--- ");
		Date startTime = startTiming("\tstart compute advisories");

		if (guideline == null) {
			logger
			.error("PCASession_i-computeAdvisories Exception: Null guideline!!");
			throw new PCA_Session_Exception(
					"PCASession_i-computeAdvisories: Null guideline!!");
		}
		// clean up previous computations, drop previous assumption-based
		// recommendatiaons
		try {
			logger.debug("start body of computeelibigility----");
			guidelineManagers = new HashMap();
			logger.debug("@@ kbManager: "+ kbManager.toString());
			logger.debug("@@ dataManager: "+ dataManager.getCaseID());
			logger.debug("@@ guideline: "+ guideline.getName());
			currentGuidelineManager = new GuidelineInterpreter(kbManager,
					dataManager, guideline);
			// currentGuidelineManager.resetAdvisories();
			guidelineManagers.put("None", currentGuidelineManager);
			dataManager.deleteRecommendations();
			logger.debug("[2]start body of computeelibigility----");
			// First, check eligibility
			// construct an AND criteria
			logger.debug("@@ guideline: "+ guideline);
			logger.debug("@@ patient_id: "+ patient_id);
			logger.debug("@@ session_time: "+ session_time);
			Conclusion eligibility = currentGuidelineManager
					.checkGuidelineApplicability(guideline, patient_id,
							session_time);
			logger.debug("@@ eligibility: "+ eligibility.value + " "+  eligibility.toString());
			if (eligibility != null) {
				conclusions.add(eligibility);
				currentGuidelineManager.addConclusions(conclusions);
			}


			// evaluate goals
			Collection guidelineGoals = guideline.getgoalValue();
			Slot slot = null;
			String assumption = "";
			if ((slot = kbManager.getKB().getSlot("assume_if_no_goal_value")) == null) {
				assumption = "make_alternative_assumptions"; // guidelineGoals.getassume_if_unknownValue();
			} else if (guideline.getOwnSlotValue(slot) != null)
				assumption = (String) guideline.getOwnSlotValue(slot);
			else
				assumption = "no_assumption";
			logger.debug("computeEligibiligty and Goal: assumption: " + assumption);

			logger.debug("[3]start body of computeElibigility----");
			if ((guidelineGoals!= null) && (!guidelineGoals.isEmpty())) {
				logger.debug("[4]start body of computeElibigility----");
				logger.debug("guidelineGoals:" + guidelineGoals);
				logger.debug("guideline.getName(): " + guideline.getName());
				evaluatedGoal = currentGuidelineManager.computeConditionalGoals(
						guidelineGoals, guideline
						.makeGuideline_Entity(guideline.getName()));
				logger.debug(evaluatedGoal);
				// if goal is unknown, then make alternative assumptions
				if (evaluatedGoal != null) {
					logger.debug("[5]start body of computeElibigility----");
					logger.debug("+evaluatedGoal.achieved: [" +evaluatedGoal.achieved + "]");
					if (evaluatedGoal.achieved.equals(Goal_State.achieved)) 
						return true;
					else
						return false;
				}
			}
			return false;
		} catch (Throwable e) {
			logger.error("PCASession_i-computeAdvisories Exception!!", e);

			throw new PCA_Session_Exception(e.getMessage());

		}

	}	

	////////////////////////////////////////////////

	private Guideline_Service_Record[] returnAdvisorySet(
			Guideline_Service_Record[] advisorySet) {

		logger.debug("Returning advisorySet");
		// currentGuidelineManager.kbmanager.deleteRuntimeInstances();

		return advisorySet;
	}

	/*
	 *     edu.stanford.smi.eon.PCAServerModule.Guideline_Entity guideline_entity,
    edu.stanford.smi.eon.PCAServerModule.Justification support_for_goal,
    edu.stanford.smi.eon.PCAServerModule.Criteria_Evaluation goal,
    edu.stanford.smi.eon.PCAServerModule.Goal_State achieved,
    String data,
    Collection<Action_Spec_Record> actions,
    int fine_grain_priority,
    Collection<String> references,
    boolean primary
	 */
	private void setAssumption(Guideline_Goal evaluatedGoal,
			PaddaGEE guidelineManager, Truth_Value truthValue,
			String assumptionString) throws PCA_Session_Exception {

		Criteria_Evaluation newCriteriaEvaluation = new Criteria_Evaluation(
				evaluatedGoal.goal._operator, truthValue,
				evaluatedGoal.goal.children, evaluatedGoal.goal.criterion,
				"assumption");
		Guideline_Goal newGoal;
		guidelineManager.setAssumption(newCriteriaEvaluation, assumptionString);

		if (truthValue.equals(Truth_Value._true)) {
			newGoal = new Guideline_Goal(evaluatedGoal.guideline_entity,
					evaluatedGoal.support_for_goal, newCriteriaEvaluation,
					Goal_State.achieved, null, null, 0,  null, true, null, null, null);
		} else
			newGoal = new Guideline_Goal(evaluatedGoal.guideline_entity,
					evaluatedGoal.support_for_goal, newCriteriaEvaluation,
					Goal_State.failed,  null, null, 0, null, true, null, null, null);
		guidelineManager.addGoal(newGoal);
		guidelineManager.getEvalManager().assume(
				(Criterion) guidelineManager.getKBmanager().instantiate(
						evaluatedGoal.goal.criterion), newCriteriaEvaluation);
	}

	Guideline_Service_Record computeAdvisory(PaddaGEE guidelineManager)
			throws PCA_Session_Exception {
		//		logger.info("computeADvisory for "+patient_id()+ "and guideline "+ guideline_name()+" ############################");
		Guideline_Service_Record advisories = null;
		Selected_Scenario scenario;
		/**
		 * 1. First compute patient characteristics 2. Call
		 * guidelineManager.initializeScenarios() 3. Call
		 * guidelineManager.chosenScenario(,) 4. While
		 * (hasUnexpandedPreferedChoice) makeRecommendedChoices
		 */
		// computen case characteristics (in Athena, determine risk group)
		// logger.debug("PCASession_i.computeAdvisory 1");
		guidelineManager.determinePatientCharacteristics(guideline
				.getpatient_characterizationValue(), patient_id(),
				session_time());
		// initializeScenarios returns a Guideline_Scenario_Choice
		// In other applications, the choice is presented to the user
		// logger.debug("PCASession_i.computeAdvisory 2");
		Guideline_Scenario_Choices sc = currentGuidelineManager
				.initializeScenarios();

		if (sc == null)	logger.warn(sc + "Scenario Choice is NULL");

		boolean hasUnexpandedPreferedChoice = true;

		// int i = 0;
		if (sc != null) {
			guidelineManager.getExpandedChoices().add(sc);
			if ((scenario = selectScenario(sc)) != null) {
				logger
				.debug("PCASession_i.computeAdvisory: selected scenario not null");
				advisories = guidelineManager.chosenScenario(scenario,
						compliance);
				// Go through each choices. keep expanding choices that are not
				// ruled out
				while (hasUnexpandedPreferedChoice) {
					// i = i+1;
					logger
					.debug("PCASession_i.computeAdvisory: has unexpanded Preferred Choice");

					// if (i > 5) break;
					advisories = makeRecommendedChoices(advisories,
							guidelineManager);
					hasUnexpandedPreferedChoice = guidelineManager
							.hasUnexpandedPreferedChoice(advisories);
				}
				return advisories;
			} else {
				logger.warn("No patient scenario chosen!");
				return guidelineManager.returnAdvisory();
			}
		} else {
			logger.warn("No patient scenario!");
			return guidelineManager.returnAdvisory();
		}

	}

	protected Guideline_Service_Record makeRecommendedChoices(
			Guideline_Service_Record advisories, PaddaGEE guidelineManager) {
		Guideline_Service_Record revisedAdvisories;
		ArrayList selections = new ArrayList();
		Selected_Action selectedAction;
		Guideline_Action_Choices currentDecision;
		Action_To_Choose currentActionChoice;
		Guideline_Scenario_Choices currentScenarioChoice;
		Selected_Scenario scenario;
		// First deal with the case where there is an scenario choice
		for (int i = 0; i < advisories.scenario_choices.length; i++) {
			currentScenarioChoice = advisories.scenario_choices[i];
			if (!guidelineManager.getExpandedChoices().contains(
					currentScenarioChoice)) {
				guidelineManager.getExpandedChoices()
				.add(currentScenarioChoice);
				if ((scenario = selectScenario(currentScenarioChoice)) != null) {
					try {
						// logger.debug("PCASession_i.computeAdvisory 3");
						advisories = guidelineManager.chosenScenario(scenario,
								compliance);
					} catch (PCA_Session_Exception e) {

						e.printStackTrace();					}				}			}		}
		for (int i = 0; i < advisories.decision_points.length; i++) {
			currentDecision = advisories.decision_points[i];
			if (!guidelineManager.getExpandedChoices()
					.contains(currentDecision)) {
				for (int j = 0; j < advisories.decision_points[i].action_choices.length; j++) {
					currentActionChoice = currentDecision.action_choices[j];
					if (currentActionChoice.preference
							.equals(Preference.preferred)) {
						if (currentActionChoice.action_specifications.length == 0) {
							selectedAction = new Selected_Action(
									currentActionChoice.name,
									currentActionChoice, null, currentDecision);
							logger.debug("PCASession_i.makeRecommendedChoices "
									+ currentActionChoice.name);
							selections.add(selectedAction);
						} else {
							for (int k = 0; k < currentActionChoice.action_specifications.length; k++) {
								logger
								.debug("PCASession-I.makeRecommendedChoices: "
										+ i
										+ ", "
										+ j
										+ ", "
										+ k
										+ " "
										+ currentActionChoice.action_specifications[k].name);
								selectedAction = new Selected_Action(
										currentActionChoice.action_specifications[k].name,
										currentActionChoice,
										currentActionChoice.action_specifications[k].action_spec,
										currentDecision);
								selections.add(selectedAction);
							}
						}
					}
				}
			}
		}
		// now have a list of Seleted_Actions
		revisedAdvisories = guidelineManager.chosenActions(selections);

		revisedAdvisories.subject_classification = (Conclusion[]) guidelineManager
				.getConclusions().toArray(new Conclusion[0]);

		return revisedAdvisories;
	}

	protected Selected_Scenario selectScenario(
			Guideline_Scenario_Choices scenarioChoices) {
		Selected_Scenario selection = null;

		for (int i = 0; i < Array.getLength(scenarioChoices.scenarios); i++) {
			// select the first scenario that is prefered
			if (scenarioChoices.scenarios[i] != null) {
				if (scenarioChoices.scenarios[i].preference == Preference.preferred) {
					logger.debug("Selecting prefered scenario: "
							+ scenarioChoices.scenarios[i].scenario_id);
					selection = new Selected_Scenario(
							scenarioChoices.scenarios[i].scenario_id);
					return selection;
				}
			} else
				break;
		}
		return selection;
	}

	/**
	 * <p>
	 * Operation: <b>::PCAServerModule::PCASession::printData</b>.
	 * 
	 * <pre>
	 * 	    #pragma prefix &quot;PCAServerModule/PCASession&quot;
	 * 	    void printData();
	 * 	
	 * </pre>
	 * 
	 * </p>
	 */
	public String printData() {
		StringWriter returnStringConstructor = new StringWriter();
		PrintWriter itsWriter = new PrintWriter(returnStringConstructor);

		printDemographics(itsWriter);
		printLabs1(itsWriter, IEON.HTML);
		printMedProblems(itsWriter, IEON.HTML);
		printAllergies(itsWriter);
		printHospitalizationPeriod(itsWriter);
		return returnStringConstructor.toString();
		// return "<html></html>";

	}

	private void printHospitalizationPeriod(PrintWriter itsWriter) {
		Instance hospPeriod = kbManager.kb.getInstance("FocusAdmissionPeriod");
		if (hospPeriod != null) {
			Definite_Time_Point start = (Definite_Time_Point)(((Definite_Time_Interval)hospPeriod).getstart_timeValue());
			Definite_Time_Point stop = (Definite_Time_Point)(((Definite_Time_Interval)hospPeriod).getstop_timeValue());
			itsWriter.println("<p><b>Hospitalization Period:</b> ["+ ((start != null) ? start.getlabelValue() + ":"+
					start.getsystem_timeValue()  : "")+
					","+ ((stop != null) ? stop.getlabelValue()+":"+stop.getsystem_timeValue() : "")+"]");
		}

	}

	private void printLabs1(PrintWriter itsWriter, String flag) {
		// for backward compatibility
		printLabsVitals(itsWriter, flag);
	}

	private int numberOfLeadingBlanks(String str){
		int charCount = 0;
		char temp;
		for( int i = 0; i < str.length( ); i++ )
		{
			temp = str.charAt( i );
			if( temp == ' ' )
				charCount++;
			else break;
		}
		return charCount;
	}

	private String HTMLSpaces(int numberOfSpaces) {
		String space = "&nbsp;";
		String finalString = "";
		for (int i = 0; i< numberOfSpaces; i++) 
			finalString = finalString +space;
		return finalString;
	}

	private void printLabsVitals(PrintWriter itsWriter, String flag) {
		Collection<String> printedParameters = new ArrayList<String>();
		itsWriter.println("<p><b>Numeric Data Values:</b></p>");
		if (flag.equals(IEON.HTML))
			itsWriter.println("<table>");
		for (String labPrintName : labs) {
			printedParameters.add(labPrintName.trim());
			TreeMap results = (TreeMap) dataManager
					.getNumericEventsOfLabel(labPrintName.trim());
			printLabVitalEntry(itsWriter, flag, results, labPrintName);
		}
		printRemainders(itsWriter,  flag, printedParameters);
		if (flag.equals(IEON.HTML))
			itsWriter.println("</table>");
	}

	private void printRemainders(PrintWriter itsWriter, String flag,Collection<String> printedParameters) {
		Set<String> labSet = (Set<String>)dataManager.getNumericEventKeys();
		if (flag.equals(IEON.HTML)) 
			itsWriter.println("<tr><td width=\"200\"><p>Others:</td></tr>") ;
		else
			itsWriter.println("<p><b>Others:</b>");
		for (String labName : labSet) {
			if (!printedParameters.contains(labName)) {
				TreeMap results = (TreeMap) dataManager.getNumericEventsOfLabel(labName);	
				printLabVitalEntry(itsWriter, flag, results, "    "+labName);
			}
		}
	}
	private NumberFormat getNumberFormat(String labName){
		int precision = 0;
		java.lang.Object precisionObj;
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(0);
		nf.setMinimumFractionDigits(0);
		Cls cls = kbManager.getCls(labName);
		if (cls == null) 
			logger.warn("getNumberFormat: "+ labName + " is not in KB");
		else
			if (cls.hasType(kbManager.getCls("Interval-Valued_AtomicTest_Metaclass"))) { 
			// is an instance of Interval-Valued_AtomicTest_Metaclass
				precisionObj = cls.getOwnSlotValue(kbManager.getKB().getSlot("Precision"));
				if (precisionObj != null) { // has up precision, if not defined, then 0
					precision = ((Integer) precisionObj).intValue();
					nf.setMaximumFractionDigits(precision);
					nf.setMinimumFractionDigits(precision);
				} else 
					logger.info("getNumberFormat: "+ labName
							+ " is an instance of Interval-Valued_AtomicTest_Metaclass but the 'precision' slot is not set");
			} else
				logger.info("getNumberFormat: "+ labName
					+ " is not an instance of Interval-Valued_AtomicTest_Metaclass, and has no precision setting");
		return nf;
	}

	private boolean isIntervalValued(String labName) {
		Cls cls = kbManager.getCls(labName);
		if ((cls != null)	&& (cls.hasType(kbManager.getCls("Interval-Valued_AtomicTest_Metaclass")))) { 
			// is an instance of Interval-Valued_AtomicTest_Metaclass
			return true;
		} else 
			return false;
	}

	private void printLabVitalEntry(PrintWriter itsWriter, String flag, TreeMap results, String labPrintName) {
		String labName = labPrintName.trim();
		String valueString;
		String timestamp = "";
		Long key;
		java.lang.Object keyObj;
		Absolute_Time_Point timePoint = null;
		if (results == null) {
			if (flag.equals(IEON.HTML)) {
				itsWriter.println("<tr><td width=\"200\">" + HTMLSpaces( numberOfLeadingBlanks(labPrintName))+labName
						+ "</td><td width=\"200\"></td><td width=\"200\"><td></tr>");
			} else
				itsWriter.println(HTMLSpaces( numberOfLeadingBlanks(labPrintName))+labName +"<br>");
		} else {
			NumberFormat nf = getNumberFormat(labName);
			for (Iterator i = results.keySet().iterator(); i.hasNext();) {
				boolean first = true;
				keyObj = i.next();
				if (keyObj instanceof Integer) {
					key = new Long(((Integer) keyObj).longValue());
				} else
					key = (Long) keyObj;
				timePoint = (Definite_Time_Point) dataManager.createInstance("Definite_Time_Point");
				timePoint.setsystem_timeValue((int) (key.longValue() / 1440));
				timePoint.setDateValue((int) (key.longValue() / 1440));
				if (isIntervalValued(labName)) {
					valueString = nf.format(((Float) results.get(key)).floatValue());
				} else {
					valueString = results.get(key).toString();
				}
				timestamp = ((Absolute_Time_Point) timePoint).stringValue();
				if (first) {
					first = false;
					if (flag.equals(IEON.HTML)) {
						itsWriter.println("<tr><td width=\"200\">" + HTMLSpaces( numberOfLeadingBlanks(labPrintName))+"<b>"+labName
								+ "</b></td><td width=\"200\">" + valueString
								+ "</td><td width=\"200\">" + timestamp 	+ "</td></tr>");
					} else 
						itsWriter.println(HTMLSpaces( numberOfLeadingBlanks(labPrintName))+"<b>"+labName + "</b>: " + valueString + " (" + timestamp +")<br>");						
				} else if (flag.equals(IEON.HTML)) {
					itsWriter
					.println("<tr><td width=\"200\"></td><td width=\"200\">"
							+ valueString
							+ "</td><td width=\"200\">"
							+ timestamp  + "</td></tr>");
				} else
					itsWriter.println( valueString+ " ("+ timestamp +")<br>" );
			}
		}
		if (timePoint != null)
			dataManager.deleteInstance(timePoint);
	}



	private void printNumericEventsInTable(PrintWriter itsWriter, String flag) {
		NumberFormat nf = NumberFormat.getInstance();
		String valueString;
		String timestamp = "";
		boolean first = true;
		boolean isNumericData = false;

		Set labSet = dataManager.getNumericEventKeys();
		itsWriter.println("<p><b>Numeric Data Values:</b> <table>");
		itsWriter
		.println("<b><tr><td width=\"200\">Lab</td><td width=\"200\">Value</td><td width=\"200\">Date</td></tr></b>");
		for (Iterator labList = labSet.iterator(); labList.hasNext();) {
			isNumericData = false;
			first = true;
			String labName = (String) labList.next();
			TreeMap results = (TreeMap) dataManager
					.getNumericEventsOfLabel(labName);
			Long key;
			java.lang.Object keyObj;
			java.lang.Object precisionObj;
			Absolute_Time_Point timePoint;
			int precision = 0;
			if (results != null) {
				nf.setMaximumFractionDigits(0);
				nf.setMinimumFractionDigits(0);
				Cls cls = kbManager.getCls(labName);
				if ((cls != null)
						&& (cls
								.hasType(kbManager
										.getCls("Interval-Valued_AtomicTest_Metaclass")))) { // is
					// an
					// instance
					// of
					// Interval-Valued_AtomicTest_Metaclass
					isNumericData = true;
					precisionObj = cls.getOwnSlotValue(kbManager.getKB()
							.getSlot("Precision"));
					if (precisionObj != null) { // has up precision, if not
						// defined, then 0
						precision = ((Integer) precisionObj).intValue();
						nf.setMaximumFractionDigits(precision);
						nf.setMinimumFractionDigits(precision);
					}
				}
				for (Iterator i = results.keySet().iterator(); i.hasNext();) {
					keyObj = i.next();
					// key = (Long)keyObj;
					if (keyObj instanceof Integer) {
						key = new Long(((Integer) keyObj).longValue());
					} else
						key = (Long) keyObj;
					timePoint = (Definite_Time_Point) dataManager
							.createInstance("Definite_Time_Point");
					timePoint
					.setsystem_timeValue((int) (key.longValue() / 1440));
					timePoint.setDateValue((int) (key.longValue() / 1440));
					// if (keyObj instanceof Integer) {
					// valueString =
					// formatter[j].format(((Float)results.get((Integer)keyObj)).floatValue());
					// } else
					if (isNumericData) {
						valueString = nf.format(((Float) results.get(key))
								.floatValue());
					} else {
						logger
						.info("PrintLab1: "
								+ labName
								+ " is either not in KB or not an instance of Interval-Valued_AtomicTest_Metaclass");
						valueString = results.get(key).toString();
					}
					timestamp = ((Absolute_Time_Point) timePoint).stringValue();
					if (first) {
						first = false;
						itsWriter.println("<tr><td width=\"200\">" + labName
								+ "</td><td width=\"200\">" + valueString
								+ "</td><td width=\"200\">" + timestamp +" ("+timePoint.getsystem_timeValue()+")"
								+ "</td></tr>");
					} else {
						itsWriter
						.println("<tr><td width=\"200\"></td><td width=\"200\">"
								+ valueString
								+ "</td><td width=\"200\">"
								+ timestamp  +" ("+timePoint.getsystem_timeValue()+")"
								+ "</td></tr>");
					}
				}
			}
		}

		// Cls metaclass =
		// kbManager.getCls("Ordinal-Valu;ed_AtomicTest_Metaclass");

		// if (noteEntryCls != null) {
		// Collection qualitativeLabs = noteEntryCls.getInstances();
		// for (Iterator j=qualitativeLabs.iterator(); j.hasNext();) {
		// Instance qualitativeLab = (Instance) j.next();
		// String qualitativeLabLabel = qualitativeLab.getName();
		//		
		// WhereFilter where =
		// dataManager.caseSpecificWhere(DharmaPaddaConstants.AND,
		// new WhereComparisonFilter("domain_term", DharmaPaddaConstants.eq,
		// qualitativeLabLabel));
		itsWriter.println("</table>");
	}

	private void printDemographics(PrintWriter itsWriter) {
		// print patient id, age, sex, race
		String race = dataManager.getDemographics("Race");
		String ageInYears = "";
		String sex = dataManager.getDemographics("Sex");
		String demographicsString = "";
		try {
			DataElement age = dataManager.doNumericQuery(
					currentGuidelineManager, "Age", null, null, null, null);
			if (age != null)
				ageInYears = age.value + " year old ";
		} catch (Exception e) {
			logger.warn("Fail to query for Age");
		}
		demographicsString = ageInYears + ((race != null) ? race : "" );
		demographicsString = demographicsString + ((sex != null) ? " "+sex : "");
		itsWriter.println(demographicsString);
	}

	private void printAllergies(PrintWriter itsWriter) {
		Collection adverseEvents = kbManager.findInstances("Adverse_Reaction",
				this.getDataManager().getCaseSelector(),
				(GuidelineInterpreter) currentGuidelineManager);
		if (adverseEvents != null && !adverseEvents.isEmpty()) {
			itsWriter
			.println("<p><b>Allergies and Adverse Reactions:</b> <ul>");
			for (Iterator event = adverseEvents.iterator(); event.hasNext();) {
				Adverse_Reaction value = (Adverse_Reaction) event.next();
				String symptom = value.getDomain_term();
				if (symptom == null)
					symptom = "";
				String substance = value.getSubstance();
				itsWriter.println("<li>Substance: " + substance + " Reaction: "
						+ symptom);
			}
			itsWriter.println(" </ul>");
		}
	}

	private void printMed(PrintWriter itsWriter) {
		itsWriter.print("<p><b>Medications:</b> ");
		WhereComparisonFilter where = dataManager.getCaseSelector();
		Collection meds = kbManager.findInstances("Medication", where,
				(GuidelineInterpreter) currentGuidelineManager);
		for (Object med : meds) {
			Medication aMed = (Medication)med;
			if  ((aMed.getMood() == null) || aMed.getMood().getName().equals("Authorized")) {
				Definite_Time_Interval validTime = null;
				String timeString = "";
				String mprString = "";
				Instance validTimeObj = aMed.getValid_time();
				if (validTimeObj != null) {
					validTime = (Definite_Time_Interval) validTimeObj;
					Instance start = validTime.getstart_timeValue();
					Instance stop = validTime.getstop_timeValue();
					if ((start != null) && !(start.equals("")) && (((Definite_Time_Point) start).getlabelValue() != null))
						timeString = "["
								+ ((Definite_Time_Point) start).getlabelValue()
//								+ ":"+ ((Definite_Time_Point) start).getsystem_timeValue() 
								+ ", ";
					else
						timeString = "[  , ";
					if ((stop != null) && !(stop.equals("")) && (((Definite_Time_Point) stop).getlabelValue() != null))
						timeString = timeString
						+ ((Definite_Time_Point) stop).getlabelValue()
//						+ ":"+ ((Definite_Time_Point) stop).getsystem_timeValue() 
						+ "]";
					else
						timeString = timeString + " ]";
					if (aMed.getMedicationPossessionRatio() != (float)0.0) {
						mprString = "{"+aMed.getMedicationPossessionRatio()+
								((aMed.getPRT() !=null) ? ", "+ ((Definite_Time_Point)aMed.getPRT()).getlabelValue() : "") + "}";
					}
				}
				itsWriter.print(aMed.getDrug_name() + "("
						+ aMed.getDaily_dose() + timeString + mprString+ ") ");
			}
		}

	}

	private void printMedProblems(PrintWriter itsWriter, String flag) {
		printMed( itsWriter);
		printProblems(itsWriter, flag);
	}

	private void printProblems(PrintWriter itsWriter, String flag) {
		WhereComparisonFilter where = dataManager.getCaseSelector();
		String timestamp = "";
		itsWriter.println("");
		if (flag.equals(IEON.HTML)) {
			itsWriter.println("<p><b>Qualitative Data:</b> </p><table>");
		} else
			itsWriter.println("<p><b>Qualitative Data:</b> </p>");
		Collection notes = kbManager.findInstances("Note_Entry", where,
				(GuidelineInterpreter) currentGuidelineManager);
		
		if ((notes != null) || !notes.isEmpty()) {
			ArrayList<Note_Entry> sortedNotes = new ArrayList<Note_Entry>();
			for (Object obj : notes) {
				sortedNotes.add((Note_Entry)obj);
			}
			Collections.sort(sortedNotes);
			for (Iterator i = sortedNotes.iterator(); i.hasNext();) {
				Note_Entry QualitativeEntry = (Note_Entry) i.next();
				String domainTerm = QualitativeEntry.getDomain_term();
				Cls domainCls = kbManager.getCls(domainTerm);
				if (domainCls != null) domainTerm = domainCls.getBrowserText();
				if (QualitativeEntry.getValue() != null) {
					timestamp = "";
					if (QualitativeEntry.getValid_time() != null) {
						timestamp = ((Definite_Time_Point) QualitativeEntry
								.getValid_time()).stringValue();
					}
					if (flag.equals(IEON.HTML)) {
						itsWriter.println("<tr><td width=\"200\">"
								+ domainTerm	+ "</td><td width=\"200\">"
								+ (QualitativeEntry.getValue().equals(domainTerm) ? "" : QualitativeEntry.getValue())
								+ "</td><td width=\"200\">" + timestamp
								+ "</td></tr>");
					} else
						itsWriter.println("<b>"
								+ domainTerm
								+ " </b> "
								+ (QualitativeEntry.getValue().equals(domainTerm) ? "" : QualitativeEntry.getValue())
								+ " ("+timestamp+")<br>");
				}
			} // for
		}
		if (flag.equals(IEON.HTML)) 
			itsWriter.println("</table><p>");

	}

	public Compliance_Level getCompliance() {
		return compliance;
	}

	/**
	 * @return currentGuidelineManager
	 */
	public PaddaGEE getCurrentGuidelineManager() {
		return currentGuidelineManager;
	}

	/**
	 * @return database
	 */
	public String getDatabase() {
		return database;
	}

	/**
	 * @return dataManager
	 */
	public DataHandler getDataManager() {
		return dataManager;
	}

	/**
	 * @return debug
	 */


	/**
	 * @return guideline
	 */
	public Management_Guideline getGuideline() {
		return guideline;
	}

	/**
	 * @return guidelineManagers
	 */
	public Map getGuidelineManagers() {
		return guidelineManagers;
	}

	/**
	 * @return initFile
	 */
	public String getInitFile() {
		return initFile;
	}

	/**
	 * @return kbManager
	 */
	public KBHandler getKbManager() {
		return kbManager;
	}

	/**
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return patient_id
	 */
	public String getPatient_id() {
		return patient_id;
	}

	/**
	 * @return session_time
	 */
	public String getSession_time() {
		return session_time;
	}

	/**
	 * @return user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param level
	 */
	public void setCompliance(Compliance_Level level) {
		compliance = level;
	}

	/**
	 * @param paddaGEE
	 */
	public void setCurrentGuidelineManager(PaddaGEE paddaGEE) {
		currentGuidelineManager = paddaGEE;
	}

	/**
	 * @param string
	 */
	public void setDatabase(String string) {
		database = string;
	}

	/**
	 * @param handler
	 */
	public void setDataManager(DataHandler handler) {
		dataManager = handler;
	}

	/**
	 * @param guideline
	 */
	public void setGuideline(Management_Guideline guideline) {
		this.guideline = guideline;
	}

	/**
	 * @param map
	 */
	public void setGuidelineManagers(Map map) {
		guidelineManagers = map;
	}

	/**
	 * @param string
	 */
	public void setInitFile(String string) {
		initFile = string;
	}

	/**
	 * @param handler
	 */
	public void setKbManager(KBHandler handler) {
		kbManager = handler;
	}

	/**
	 * @param string
	 */
	public void setPassword(String string) {
		password = string;
	}

	/**
	 * @param string
	 */
	public void setPatient_id(String string) {
		patient_id = string;
	}

	/**
	 * @param string
	 */
	public void setSession_time(String string) {
		session_time = string;
	}

	/**
	 * @param string
	 */
	public void setUser(String string) {
		user = string;
	}

	/*	public Collection<Advisory> computeAdvisory(String pid, String currentTime,
			String guidelineName) {

		Collection<Advisory> advisories = null;
		Guideline_Service_Record[] dssOutput =null;
		try {
			setCompliance(Compliance_Level.strict);
			setCase(pid, currentTime);
			setGuideline(guidelineName);
			dssOutput = computeAdvisories();
			advisories = AdvisoryFormater.javaTransform(dssOutput, pid, null, guidelineName, session_time,
					"", "");
		}
		catch (PCA_Session_Exception e){
			System.out.println("PCA_Session_Exception error for  patient: " + pid);
			e.printStackTrace();
		}
		catch (Exception e) {
			System.out.println(" error for  patient: " + pid);
			e.printStackTrace();
		}
		return advisories;

	}
	 */
	public String computePerformanceMeasuresXMLWithInputData(String pid,String patientData,
			String hospitalizationID, String sessionTime, String guidelineName,
			String PMName, String startTime, String stopTime) throws PCA_Session_Exception  {

		String XMLAdvisory = null;
		Collection<Patient_Data> ptDataArray = readPatientData(patientData, pid).create_patient_data_array();
		Guideline_Service_Record[] dssOutput =new Guideline_Service_Record[1];
		if (ptDataArray != null) {
			Patient_Data[] data = (Patient_Data[]) ptDataArray.toArray(new Patient_Data[0]);
			try {
				setCompliance(Compliance_Level.strict);
				resetAdvisories();
				setCase(pid, sessionTime);
				updateData(data);
				dssOutput[0] = computePerformanceMeasures(pid, hospitalizationID, sessionTime,
						guidelineName, PMName, startTime, stopTime);
				XMLAdvisory = generateXMLAdvisory(pid, dssOutput, kbManager, sessionTime, guidelineName);

			}
			catch (Exception e) {
				System.out.println("computeAndSaveAdvisory error for  patient: " + pid);
				e.printStackTrace();
			}

		} else {
			System.out.println("ptDataArray is null for patient case "+pid);
		}
		return XMLAdvisory;
	}

	public Advisory computePerformanceMeasuresAdvisoryWithInputData(String pid,String patientData,
			String hospitalizationID, String sessionTime, String guidelineName,
			String PMName, String startTime, String stopTime) throws PCA_Session_Exception  {
		Collection<Patient_Data> ptDataArray = readPatientData(patientData, pid).create_patient_data_array();
		Guideline_Service_Record gsr =null;
		if (ptDataArray != null) {
			Patient_Data[] data = (Patient_Data[]) ptDataArray.toArray(new Patient_Data[0]);
			try {
				setCompliance(Compliance_Level.strict);
				resetAdvisories();
				setCase(pid, sessionTime);
				updateData(data);
				gsr = computePerformanceMeasures(pid, hospitalizationID, sessionTime,
						guidelineName,PMName, startTime, stopTime);
				return AdvisoryFormater.translateGuidelineServiceRecord(gsr, pid, hospitalizationID, guidelineName, session_time,
						startTime, stopTime);
			}
			catch (Exception e) {
				System.out.println("computeAndSaveAdvisory error for  patient: " + pid);
				e.printStackTrace();
			}

		} else {
			System.out.println("ptDataArray is null for patient case "+pid);
		}
		return  null;
	}

	// computePerformanceMeasuresAdvisory assumes that data have already been set up 
	public Advisory computePerformanceMeasuresAdvisory(String pid,
			String hospitalizationID, String sessionTime, String guidelineName, String PMName,
			String startTime, String stopTime) throws PCA_Session_Exception {
		Guideline_Service_Record advisory = computePerformanceMeasures(pid, hospitalizationID,
				sessionTime, guidelineName, PMName, startTime, stopTime);
		return AdvisoryFormater.translateGuidelineServiceRecord(advisory, pid, hospitalizationID, guidelineName, sessionTime,
				startTime, stopTime);
	}

	// computePerformanceMeasures assumes that data have already been set up
	public Guideline_Service_Record computePerformanceMeasures(String pid,
			String hospitalizationID, String sessionTime, String guidelineName, String PMName,
			String startTime, String stopTime) throws PCA_Session_Exception {
		Guideline_Service_Record advisory;
		setCompliance(Compliance_Level.strict);
		try {
			setGuideline(guidelineName);
			guidelineManagers = new HashMap();
			currentGuidelineManager = new GuidelineInterpreter(kbManager,
					dataManager, guideline);			
			guidelineManagers.put("None", currentGuidelineManager);
			dataManager.deleteRecommendations();
			if (guideline == null) {
				logger
				.error("PCASession_i-computeAdvisories Exception: Null guideline!!");
				throw new PCA_Session_Exception(
						"PCASession_i-computeAdvisories: Null guideline!!");
			}
			putPerformanceMeasurementPeriod(startTime, stopTime);
			// evaluate performance measures
			advisory = currentGuidelineManager.computePerformanceMeasures(
					pid, hospitalizationID, sessionTime, PMName, startTime, stopTime);
			return advisory;
		} catch (Throwable e) {
			logger.error("PCASession_Imp-computePerformanceMeasures Exception!!", e);
			e.printStackTrace();
			throw new PCA_Session_Exception(e.getMessage());

		}

	}

	private void putPerformanceMeasurementPeriod(String startTime,
			String stopTime) {
		Definite_Time_Point start = (Definite_Time_Point)dataManager.createInstance("Definite_Time_Point");
		start.setDateValue(startTime);
		Definite_Time_Point stop = (Definite_Time_Point)dataManager.createInstance("Definite_Time_Point");
		stop.setDateValue(stopTime);
		Variable var = (Variable)dataManager.createInstance("Variable");
		Definite_Time_Interval pmp = (Definite_Time_Interval)dataManager.createInstance("Definite_Time_Interval");
		pmp.setstart_timeValue(start);
		pmp.setstop_timeValue(stop);
		var.setderivation_expressionValue(pmp);
	}




	public String printData(String format) {
		StringWriter returnStringConstructor = new StringWriter();
		PrintWriter itsWriter = new PrintWriter(returnStringConstructor);
		if (format.equals(IEON.HTML) || format.equals(IEON.HTML_NOTABLE)) {
			printDemographics(itsWriter);
			if (format.equals(IEON.HTML)){
				printLabs1(itsWriter, IEON.HTML);
				printMedProblems(itsWriter, IEON.HTML);
			} else {
				printLabs1(itsWriter, IEON.HTML_NOTABLE);
				printMedProblems(itsWriter, IEON.HTML_NOTABLE);
			}
			printAllergies(itsWriter);
			printHospitalizationPeriod(itsWriter);
			return returnStringConstructor.toString();
		}
		else
			logger.error("Only HTML or HTML_NOTABLE format has been implemented");
		return null;
	}


	public Advisory computeAndStoreAdvisory(String storageDirectory) {
		// TODO Auto-generated method stub
		return null;
	}

	/*public Guideline_Service_Record[] computeAdvisoriesWithSubGuidelines(
			String pid, String sessionTime, String guidelineName) {
		Guideline_Service_Record[] dssOutput =null;
		try {
			logger.info("Starting evaluating case "+pid );
			logger.info("Start applying guideline: "+guidelineName);
			setCompliance(Compliance_Level.strict);
			setGuideline(guidelineName);
			dssOutput = computeAdvisories();
			if (isEligible(dssOutput, guidelineName)){
			}
		}

		catch (PCA_Session_Exception e){
			System.out.println("PCA_Session_Exception error for  patient: " + pid);
			e.printStackTrace();
		}
		catch (Exception e) {
			System.out.println(" error for  patient: " + pid);
			e.printStackTrace();
		}
		return dssOutput;
	}*/

	public Guideline_Service_Record[] computeSubGuidelineAdvisories(
			String pid, String sessionTime, String guidelineName) {
		Collection<Management_Guideline> subguidelines = subGuidelines(this.kbManager , guidelineName);
		Guideline_Service_Record[] allsuboutput = new Guideline_Service_Record[0];
		if (subguidelines != null ) {
			for (Management_Guideline sub : subguidelines) {
				try {
					logger.info("Start applying subguideline: "+getGuidelineId(sub));
					this.setGuideline(getGuidelineId(sub)); 
					Guideline_Service_Record[] subdssOutput = this.updateAdvisories();
					if (subdssOutput != null) {
						allsuboutput = concat(allsuboutput, subdssOutput);
					}
				} catch (Exception e) {
					logger.error("Exception in computing advisory for "+ getGuidelineId(sub));
					logger.error(e.getMessage());
					e.printStackTrace();
				}
			}
		}
		return allsuboutput;

	}


	public <T> T[] concat(T[] a, T[] b) {
		final int alen = a.length;
		final int blen = b.length;
		final T[] result = (T[]) java.lang.reflect.Array.
				newInstance(a.getClass().getComponentType(), alen + blen);
		System.arraycopy(a, 0, result, 0, alen);
		System.arraycopy(b, 0, result, alen, blen);
		return result;
	}

	private boolean isEligible( Guideline_Service_Record[] dssOutput, String guidelineName) {
		boolean isEligible = false;
		for (int j=0; j < dssOutput.length; j=j+1){
			if (dssOutput[j].subject_classification != null) {
				for (int i=0; i < dssOutput[j].subject_classification.length; i++) {
					if (dssOutput[j].subject_classification[i] != null) {
						Conclusion classification = (Conclusion)dssOutput[j].subject_classification[i];
						logger.debug("parameter: " +classification.parameter);
						if (classification.parameter.equals(guidelineName)){
							logger.debug("Client.isEligible "+classification.value);
							isEligible = classification.value.equals(Truth_Value._true.toString());
							return isEligible;
						}
					}
				}
			}
		}
		return isEligible;
	}


	private String getGuidelineId( Management_Guideline guideline) {
		logger.debug("gov.va.test.opioidtesttool.pca.Client.getGuidelienId: "+guideline.getBrowserText()+".");
		return guideline.getBrowserText();
	}

	private Collection subGuidelines( KBHandler kbManager, String guidelineId) {
		Management_Guideline topGL = getGuideline(kbManager, guidelineId);
		logger.debug("topguideline: " + topGL);
		logger.debug(kbManager.getKB().getSlot("subguidelines").toString());
		return topGL.getOwnSlotValues(kbManager.getKB().getSlot("subguidelines"));
	}

	static Management_Guideline getGuideline(KBHandler kbManager, String guidelineId) {
		Management_Guideline gl = null;
		Collection guidelines  =  (kbManager.findInstances("Management_Guideline",
				new WhereComparisonFilter("label", "eq", guidelineId), null));
		if (guidelines == null || guidelines.isEmpty()) {
			logger.error("guideline not found"+ guidelineId);
		}
		if (guidelines.size() > 1) {
			logger.error("guideline ambiguous" + guidelineId);
		} else {
			return (Management_Guideline) (guidelines.toArray())[0];
		}
		return null;
	}

	public String printAdvisory(Advisory advisory, String format) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintWriter itsWriter = null;
		itsWriter = new PrintWriter(outputStream, true);

		itsWriter.println("<h2>Session time: "+ advisory.getAdvisory_time()+"</h2>");
		if (advisory.getStart_time() != null) 
			itsWriter.println("<h2>Performance measure evaluation start time: "+ advisory.getStart_time()+"</h2>");
		if (advisory.getStop_time() != null)
			itsWriter.println("<h2>Performance measure evaluation stop time: "+ advisory.getStop_time()+"</h2>");
		itsWriter.println("<h2>GuidelineID: "+ advisory.getGuideline_id()+"</h2>");
		if (advisory.getPatient_characteristic() != null)
			printPatientCharacteristics(itsWriter, advisory.getPatient_characteristic(), advisory.getGuideline_id());
		if (advisory.getEvaluated_goal() != null) 
			printGuidelineGoals(itsWriter, advisory.getEvaluated_goal());
		if (advisory.getRecommended_action() != null) 
			printRecommendedActions(itsWriter, advisory.getRecommended_action());
		if (advisory.getDrug_recommendation() != null) 
			printDrugRecommendations(itsWriter, advisory.getDrug_recommendation());
		return outputStream.toString();
	}

	private void printDrugRecommendations(PrintWriter itsWriter,
			Collection<Drug_Recommendation> drug_recommendation) {
		itsWriter.println("<h4>Therapeutic Choices</h4>");
		itsWriter.println("<pre>");
		for (Drug_Recommendation dr : drug_recommendation) {
			itsWriter.println();
			if (dr instanceof Add_Delete_Drug_Recommendation) {
				printAddDeleteDrug(itsWriter, (Add_Delete_Drug_Recommendation)dr);
			} else if (dr instanceof Increase_Decrease_Dose_Recommendation) {
				printChangeDose( itsWriter, (Increase_Decrease_Dose_Recommendation)dr);
			} else
				logger.error("Unknown drug recommendation type: "+dr.getClass().getName());
		}
		itsWriter.println("</pre>");
	}

	private void printAddDeleteDrug(PrintWriter itsWriter, Add_Delete_Drug_Recommendation rec) {
		if (rec.getDrug_action_type().equals("add")){
			itsWriter.println("<b>"+rec.getDrug_action_type()+": </b>"+ 
					((rec.getDrug_to_add_string()!= null) ? rec.getDrug_to_add_string() : ""));
			itsWriter.println("<b>Preference: </b>"+rec.getPreference());
		} else if (rec.getDrug_action_type().equals(GlobalVars.discontinue)){
			itsWriter.print("<b>"+rec.getDrug_action_type()+": </b>");
			for (String del : rec.getSpecific_drug() ) 
				itsWriter.print(del);
			itsWriter.println("");

		}
		itsWriter.println("<b>Fine-grain priority: </b>"+rec.getFine_grain_priority());

		if (rec.getEvaluated_drug_relation() != null) 
			printEvaluatedDrugRelatins(itsWriter, rec.getEvaluated_drug_relation());
		if (rec.getCollateral_action() != null) {
			itsWriter.println("<b>Collateral Actions: </b>");
			for (Action action : rec.getCollateral_action()) {
				printAction( itsWriter,  action, 5);
			}
		}

		if (rec.getAssociated_substitution_drug() != null) {
			if (rec.getAssociated_substitution_drug().getDrug_action_type().equals("add")) {
				itsWriter.print("<b>Associated Drug Action:</b> "+rec.getAssociated_substitution_drug().getDrug_action_type());
				itsWriter.println(" "+rec.getAssociated_substitution_drug().getDrug_to_add_string());
			} else {
				itsWriter.print("<b>Associated Drug Action:</b> "+rec.getAssociated_substitution_drug().getDrug_action_type());
				itsWriter.println(" "+rec.getAssociated_substitution_drug().getSpecific_drug().toString());
			}
		}
		//		if (rec.getDrug_class() != null)
		//			itsWriter.println("<b>Drug Class: </b>"+rec.getDrug_class());
		//		if (rec.getSpecific_drug() != null) {
		//			for (String sd : rec.getSpecific_drug()) {
		//				itsWriter.println("Specific Drug: "+ sd);
		//			}
		//		}
	}

	private void printEvaluatedDrugRelatins(PrintWriter itsWriter, Collection<Evaluated_Drug_Relation> evals) {
		List<String> reltypes = Arrays.asList("compelling_indication", "relative_indication", 
				"strong_contraindication", "relative_contraindication", "bad_drug_partner", "good_drug_partner", 
				"do_not_add_controllable_condition", "do_not_add_uncontrollable_condition", "do_not_intensify_condition",
				"is_first_line_drug_for", "is_second_line_drug_for", "is_third_line_drug_for");
		for (String reltype : reltypes ) {
			String rels = "";
			boolean first = true;
			for (Evaluated_Drug_Relation eval : evals) {
				//System.out.println("Evaluaed drug relation: "+eval.getRelation_type() + " "+eval.getCondition_or_drug());
				if (eval.getRelation_type().equals(reltype))
					if (first) {
						rels = eval.getCondition_or_drug();
						first = false;
					} else 
						rels = rels+", "+ eval.getCondition_or_drug();
			}
			if (!rels.equals(""))
				itsWriter.println("<b>"+reltype+"</b>: "+rels);
		}
		String adverseReactionString = printAdverseReactions(evals);
		if (!adverseReactionString.equals(""))
			itsWriter.println("<b>Adverse Reaction:</b>"+adverseReactionString);
	}

	private String printAdverseReactions(Collection<Evaluated_Drug_Relation> rels) {
		boolean first = true;
		String adverseReaction = "";
		for (Evaluated_Drug_Relation eval : rels) {
			if (eval.getRelation_type().equals("adverse_reaction"))
				if (first) {
					adverseReaction = eval.getSubstance() + "("+eval.getCondition_or_drug() + ")";
					first = false;
				} else 
					adverseReaction = adverseReaction+", "+ eval.getSubstance() + "("+eval.getCondition_or_drug() + ")";
		}
		return adverseReaction;

	}

	private void printChangeDose(PrintWriter itsWriter, Increase_Decrease_Dose_Recommendation rec) {
		itsWriter.println("<b>Drug Action Type:</b> "+rec.getDrug_action_type());
		itsWriter.println("<b>Preference: </b>"+rec.getPreference());
		itsWriter.println("<b>Fine-grain Priority: </b>"+rec.getFine_grain_priority());
		if (rec.getSpecific_drug() != null) {
			for (String sd : rec.getSpecific_drug()) {
				itsWriter.println("Specific Drug: "+ sd);
			}
		}
		if (rec.getEvaluated_drug_relation() != null){
			printEvaluatedDrugRelatins(itsWriter, rec.getEvaluated_drug_relation());
//			itsWriter.println("<b>Adverse Reactions: </b>");
//			itsWriter.println(printAdverseReactions(rec.getEvaluated_drug_relation()));
		}
		if (rec.getCollateral_action() != null) {
			itsWriter.println("<b>Collateral Actions: </b>");
			for (Action action : rec.getCollateral_action()) {
				printAction( itsWriter,  action, 5);
			}
		}

	}

	private void printRecommendedActions(PrintWriter itsWriter,
			Collection<Action> recommended_action) {
		itsWriter.println("<h4>Recommended Actions</h4>");
		itsWriter.println("<pre>");
		for (Action action: recommended_action) {
			printAction( itsWriter,  action, 0);
		}
		itsWriter.println("</pre>");

	}

	private void printlnWithIndent(PrintWriter itsWriter, String text, int indent) {
		for (int i = 0; i < indent+1; i++) {
			itsWriter.print(" ");
		}
		itsWriter.println(text);
	}

	private void printAction(PrintWriter itsWriter, Action action, int indent) {
		if (action == null) {
			logger.error("Null action!");
		} else {
			if (action.getLabel() != null)
				printlnWithIndent(itsWriter, "<b>"+action.getAction_class()+": "+action.getLabel() +"</b>", indent);
			if ((action.getDescription() != null) && !(action.getDescription().equals("")))
				printlnWithIndent(itsWriter, "Description: "+action.getDescription(), indent);
			if (action instanceof gov.va.athena.advisory.Message) {
				gov.va.athena.advisory.Message m = (gov.va.athena.advisory.Message)action;
				if (m.getMessage_type() != null)
					printlnWithIndent(itsWriter, "Message Type: " + m.getMessage_type(), indent);
				if (m.getMessage() != null)
					printlnWithIndent(itsWriter, "Message: " + m.getMessage() , indent);
			} else if (action instanceof gov.va.athena.advisory.Referral) {
				gov.va.athena.advisory.Referral referral = (gov.va.athena.advisory.Referral)action;
				printlnWithIndent(itsWriter, "Refer to: "+referral.getWho_to(), indent);
				if (referral.getWhen() != null)
					printlnWithIndent(itsWriter, "When: "+referral.getWhen(), indent);
			} else if (action instanceof gov.va.athena.advisory.Order_TestProcedure) {
				gov.va.athena.advisory.Order_TestProcedure test = (gov.va.athena.advisory.Order_TestProcedure)action;
				printlnWithIndent(itsWriter, "Test or Procedure: "+test.getTest_or_procedure(), indent);
				if (test.getWhen() != null)
					printlnWithIndent(itsWriter, "When: "+test.getWhen(), indent);
			}
			//			printlnWithIndent(itsWriter, "Fine-grain priority "+action.getFine_grain_priority(), indent);
			if (action.getSubsidiary_message() != null) 
				for (String m : action.getSubsidiary_message()) {
					printlnWithIndent(itsWriter, "Subsidiary message: "+m, indent);
				}
		}

	}



	private void printPatientCharacteristics(PrintWriter itsWriter,
			Collection<gov.va.athena.advisory.Conclusion> patient_characteristic,String guidelineID) {
		itsWriter.println("<h4>System Conclusions</h4>");
		itsWriter.println("<pre>");
		for (gov.va.athena.advisory.Conclusion conclusion : patient_characteristic) {
			if ((conclusion.getValue() != null) && (!(conclusion.getValue().equals(""))))
				if (conclusion.getParameter().equals(guidelineID)) {
					itsWriter.println("Conclusion: "+conclusion.getParameter() + " Value: "+conclusion.getValue());
					if (conclusion.getValue().equals(EONConstants.FALSE)) 
						itsWriter.println("Case is not eligible");
					else
						itsWriter.println("Case is eligible");
				} else
					itsWriter.println("Conclusion: "+conclusion.getParameter() + " Value: "+conclusion.getValue());
			else 
				itsWriter.println("Conclusion: "+conclusion.getParameter());
		}
		itsWriter.println("</pre>");
	}

	private void printGuidelineGoals(PrintWriter itsWriter, Collection<gov.va.athena.advisory.Guideline_Goal> goals) {
		for (gov.va.athena.advisory.Guideline_Goal pm : goals) {
			boolean isPerformanceMeasure = !pm.getPrimary();
			if (!isPerformanceMeasure) 
				printGoal(itsWriter, pm);
			else {
				printPM(itsWriter, pm);
			}
		}

	}

	private void printGoal(PrintWriter itsWriter, gov.va.athena.advisory.Guideline_Goal goal) {
		itsWriter.println("<p>");
		itsWriter.println("<b>Guideline Goal: "+ goal.getGoal()+ "</b>"); 
		itsWriter.print("("+ goal.getReason_for_goal() + ")");
		itsWriter.println("<p><b>Reached goal?</b> " +
				goal.getAchieved()+ "("+((goal.getData() != null) ? goal.getData() : "") +")");
	} //else { itsWriter.println("<p><b>No goal</b>"); }

	private void printPM(PrintWriter itsWriter, gov.va.athena.advisory.Guideline_Goal pm) {
		itsWriter.println("<h3>Performance measure:   "+pm.getKb_goal_id()+"</h3>");
		itsWriter.println("<pre>");
		if (pm.getCriterion_Evaluation() != null){
			for (Criterion_Evaluation eval : pm.getCriterion_Evaluation()) {
				itsWriter.println("Criterion type: "+eval.getCriterion_type());
				itsWriter.println("Criterion name: "+eval.getCriterion_id());
				itsWriter.println("Evaluation result:   "+eval.getCriterion_evaluation_result());
				if ((eval.getData() != null) && !(eval.getData().equals("null"))) itsWriter.println("Data:                "+eval.getData());
				Collection<Missing_Data> missingData = eval.getMissing_Data();
				if ((missingData != null ) && !missingData.isEmpty()) {
					for (Missing_Data md: missingData) {
						itsWriter.println("	Missing data criterion:  "+md.getCriterion_id());
						itsWriter.println("	Missing data parameter:  "+md.getParameter());
						itsWriter.println("	Missing data result:  "+md.getCriterion_evaluation_result());
					}

				}
				itsWriter.println("------------------------");
			}
		}
		if (pm.getAchieved() != null) {
			itsWriter.println("Satisfy performance measure? Satisfy denominator criteria. Numerator criteria: "+pm.getAchieved());
		} else
			itsWriter.println("Satisfy performance measure? Not satisfy denominator criteria. Numerator criteria not computed.");
		itsWriter.println("</pre>");
	}
	//setGuideline must have been called before calling computeAdvisories
	public Guideline_Service_Record[] computeAdvisories(String pid,
			String sessionTime, String guidelineName,
			boolean computeSubguidelines) {
		Guideline_Service_Record[] advisory= null;
		try {
			advisory = computeAdvisories();
			if (computeSubguidelines) {
				Guideline_Service_Record[] subAdvisories = computeSubGuidelineAdvisories(
						pid,  sessionTime,  guidelineName);
				advisory = concat(advisory, subAdvisories);
			}
		} catch (PCA_Session_Exception e) {
			e.printStackTrace();
		} 
		return advisory;
	}


}




