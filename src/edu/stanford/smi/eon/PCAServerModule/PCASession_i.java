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
 <li> <b>Java Class</b> edu.stanford.smi.eon.PCAServerModule._example_PCASession
 <li> <b>Source File</b> edu/stanford/smi/eon/PCAServerModule/_example_PCASession.java
 <li> <b>IDL Source File</b> PCAServer.idl
 <li> <b>IDL Absolute Name</b> ::PCAServerModule::PCASession
 <li> <b>Repository Identifier</b> IDL:PCAServerModule/PCASession:1.0
 </ul>
 <b>IDL definition:</b>
 <pre>
 #pragma prefix "PCAServerModule"
 interface PCASession {
 attribute string database;
 attribute string kbURL;
 attribute string patient_id;
 ::PCAServerModule::Guideline_Service_Record initializeSession();
 ::PCAServerModule::Guideline_Service_Record updateRecommendations();
 void finishSession();
 void abortSession();
 };
 </pre>
 </p>
 */
import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.eon.util.*;
import edu.stanford.smi.eon.guidelineinterpreter.*;
import edu.stanford.smi.eon.Dharma.*;
import edu.stanford.smi.eon.time.*;
import edu.stanford.smi.eon.criterion.*;
import edu.stanford.smi.eon.kbhandler.*;
import edu.stanford.smi.eon.datahandler.*;

import java.util.*;
import java.lang.reflect.*;
import java.io.*;
import java.text.NumberFormat;

import org.apache.log4j.*;
import org.omg.CORBA.*;

import edu.stanford.smi.eon.pca.*;
import gov.va.athena.advisory.Advisory;
import gov.va.athena.advisory.impl.*;

public class PCASession_i implements
		edu.stanford.smi.eon.PCAServerModule.PCASession {
	/*
	 * private String database; private String patient_id; private String
	 * session_time; private String user; private String password; private
	 * Management_Guideline guideline; private String initFile; private String
	 * sessionId = "";
	 */
	// private PaddaGEE currentGuidelineManager;

	public KBHandler kbManager;
	public DataHandler dataManager;
	public Map guidelineManagers;
	public Compliance_Level compliance;
	private PCASession_Imp pcaSessionImp = null;
	static Logger logger = Logger.getLogger(PCASession_i.class);

	public PCASession_i() {
		pcaSessionImp = new PCASession_Imp();
	}

	// should throw exceptions: (1) cannot connect to database
	// (2) cannot load KB
	// (3) guideline not found in KB

	public PCASession_i(String databaseString, String user, String password,
			String initFile)
			throws edu.stanford.smi.eon.PCAServerModule.PCA_Initialization_Exception {

		// kbManager = new KBHandler(kbURLString);
		// Management_Guideline guideline = null;
		// guidelineManagers = new HashMap();
		// database = databaseString;
		// this.user = user;
		// this.password = password;
		// this.initFile = initFile;

		pcaSessionImp = new PCASession_Imp(databaseString, user, password,
				initFile);
		guidelineManagers = pcaSessionImp.guidelineManagers;

	}
	
	public boolean getCumulativeFlag() {
		return pcaSessionImp.getCumulativeFlag();
	}
	
	public void setCumulativeFlag(boolean cumulativeFlag) {
		pcaSessionImp.setCumulativeFlag(cumulativeFlag);
	}


	public void setSessionId(String sessionID) {
		pcaSessionImp.setSessionId(sessionID);
		// this.sessionId=sessionID;
	}

	// private void logln(String line) {
	// logger.debug(line);
	// }
	/*
	 * private Date startTiming(String text) { java.util.Date startTime = new
	 * java.util.Date(); logln("\t"+startTime.toString()+ text); return
	 * startTime; }
	 * 
	 * private Date stopTiming(String text, Date startTime) { java.util.Date
	 * stopTime = new java.util.Date(); logln("\t"+stopTime.toString()+ text);
	 * logln("\t"+ (stopTime.getTime() -
	 * startTime.getTime())+"\t difference in milliseconds"); return stopTime; }
	 */
	public PCASession_i(KBHandler kbhandler, String databaseString,
			String user, String pword, String initfile)
			throws edu.stanford.smi.eon.PCAServerModule.PCA_Initialization_Exception {

		kbManager = kbhandler;
		// dataManager = new DataHandler(databaseString, kbhandler, user, pword,
		// initfile);
		// Management_Guideline guideline = null; */
		// guidelineManagers = new HashMap();
		// currentGuidelineManager = new GuidelineInterpreter(kbManager,
		// dataManager,
		// this,
		// null );
		// guidelineManagers.put("None", currentGuidelineManager);
		/*
		 * database = databaseString; this.user = user; this.sessionId="";
		 * this.initFile = initFile;
		 */

		pcaSessionImp = new PCASession_Imp(kbhandler, databaseString, user,
				pword, initfile);
		dataManager = pcaSessionImp.dataManager;
		guidelineManagers = pcaSessionImp.guidelineManagers;
	}

	public void initPCASesion(KBHandler kbhandler)
			throws edu.stanford.smi.eon.PCAServerModule.PCA_Initialization_Exception {

		kbManager = kbhandler;
		// dataManager = new DataHandler(database, kbManager, user, password,
		// initFile);
		// currentGuidelineManager = new GuidelineInterpreter(kbManager,
		// dataManager,
		// this,
		// null );
		// guidelineManagers.put("None", currentGuidelineManager);
		pcaSessionImp.initPCASesion(kbhandler);
		dataManager = pcaSessionImp.dataManager;
		guidelineManagers = pcaSessionImp.guidelineManagers;
	}

	/**
	 * <p>
	 * Reader for attribute: <b>::PCAServerModule::PCASession::database</b>.
	 * 
	 * <pre>
	 *     #pragma prefix "PCAServerModule/PCASession"
	 *     attribute string database;
	 * </pre>
	 * 
	 * </p>
	 */
	public java.lang.String database() {
		return pcaSessionImp.database();
	}

	/**
	 * <p>
	 * Reader for attribute: <b>::PCAServerModule::PCASession::session_time</b>.
	 * 
	 * <pre>
	 *     #pragma prefix "PCAServerModule/PCASession"
	 *     attribute string session_time;
	 * </pre>
	 * 
	 * </p>
	 */
	public java.lang.String session_time() {
		return pcaSessionImp.session_time();
	}

	/**
	 * <p>
	 * Reader for attribute: <b>::PCAServerModule::PCASession::patient_id</b>.
	 * 
	 * <pre>
	 *     #pragma prefix "PCAServerModule/PCASession"
	 *     attribute string patient_id;
	 * </pre>
	 * 
	 * </p>
	 */
	public java.lang.String patient_id() {
		return pcaSessionImp.patient_id();
	}

	/**
	 * <p>
	 * Reader for attribute:
	 * <b>::PCAServerModule::PCASession::guideline_name</b>.
	 * 
	 * <pre>
	 *     #pragma prefix "PCAServerModule/PCASession"
	 *     readonly attribute string guideline_name;
	 * </pre>
	 * 
	 * </p>
	 */
	public java.lang.String guideline_name() {
		return pcaSessionImp.guideline_name(); // this.guideline.getlabelValue();
	}

	/**
	 * <p>
	 * Operation: <b>::PCAServerModule::PCASession::setGuideline</b>.
	 * 
	 * <pre>
	 *     #pragma prefix "PCAServerModule/PCASession"
	 *     void setGuideline(
	 *       in string guideline_name
	 *     )
	 *     raises(
	 *       ::PCAServerModule::PCA_Session_Exception,
	 *       ::PCAServerModule::PCA_Initialization_Exception
	 *     );
	 * </pre>
	 * 
	 * </p>
	 */
	public void setGuideline(java.lang.String guideline_name)
			throws edu.stanford.smi.eon.PCAServerModule.PCA_Session_Exception,
			edu.stanford.smi.eon.PCAServerModule.PCA_Initialization_Exception {

		// GenDrugRelations.genAllDrugRelations(kbManager.kb);
		// GenDrugRelations.moveAllDrugRelations(kbManager.kb);
		pcaSessionImp.setGuideline(guideline_name);
		/*
		 * logger.debug("In PCASession: "+
		 * this.toString()+" setGuideline: "+guideline_name, debugLevel);
		 * java.lang.Object[] guidelines =
		 * (kbManager.findInstances("Management_Guideline", new
		 * WhereComparisonFilter("label", "eq", guideline_name))).toArray(); if
		 * (Array.getLength(guidelines) == 0) { throw new
		 * PCA_Initialization_Exception("guideline not found"); } if
		 * (Array.getLength(guidelines) > 1) { throw new
		 * PCA_Initialization_Exception("guideline ambiguous"); } else {
		 * logger.debug("guidelines[0]: " + guidelines[0].toString() +
		 * "Direct class:"+ guidelines[0].getClass().toString(), debugLevel);
		 * guideline = (Management_Guideline) guidelines[0];
		 * 
		 * PaddaGEE guidelineManager; for (Iterator
		 * i=guidelineManagers.values().iterator(); i.hasNext();) {
		 * guidelineManager = (PaddaGEE)i.next();
		 * guidelineManager.resetAdvisories();
		 * guidelineManager.setGuideline(guideline); } }
		 */
	}

	public void ping() {
		// logln("PCAServer getting pinged");
		pcaSessionImp.ping();
	}

	/**
	 * <p>
	 * Operation: <b>::PCAServerModule::PCASession::finishSession</b>.
	 * 
	 * <pre>
	 *     #pragma prefix "PCAServerModule/PCASession"
	 *     void finishSession();
	 * </pre>
	 * 
	 * </p>
	 */
	public void finishSession() {
		// logln("Session finished");
		pcaSessionImp.finishSession();
	}

	/**
	 * <p>
	 * Operation: <b>::PCAServerModule::PCASession::abortSession</b>.
	 * 
	 * <pre>
	 *     #pragma prefix "PCAServerModule/PCASession"
	 *     void abortSession();
	 * </pre>
	 * 
	 * </p>
	 */
	public void abortSession() {
		// logln("Session aborted");
		pcaSessionImp.abortSession();
	}

	/**
	 * <p>
	 * Operation: <b>::PCAServerModule::PCASession::useCase</b>.
	 * 
	 * <pre>
	 *     #pragma prefix "PCAServerModule/PCASession"
	 *     void useCase(
	 *       in string patient_id,
	 *       in string session_time
	 *     )
	 *     raises(
	 *       ::PCAServerModule::PCA_Session_Exception
	 *     );
	 * </pre>
	 * 
	 * </p>
	 */

	/**
	 * <p>
	 * Reader for attribute: <b>::PCAServerModule::PCASession::compliance</b>.
	 * 
	 * <pre>
	 *     #pragma prefix "PCAServerModule/PCASession"
	 *     readonly attribute ::PCAServerModule::Compliance_Level compliance;
	 * </pre>
	 * 
	 * </p>
	 */
	public edu.stanford.smi.eon.PCAServerModule.Compliance_Level compliance() {
		return pcaSessionImp.compliance();
	}

	/**
	 * <p>
	 * Writer for attribute: <b>::PCAServerModule::PCASession::compliance</b>.
	 * 
	 * <pre>
	 *     #pragma prefix "PCAServerModule/PCASession"
	 *     attribute ::PCAServerModule::Compliance_Level compliance;
	 * </pre>
	 * 
	 * </p>
	 */
	public void compliance(
			edu.stanford.smi.eon.PCAServerModule.Compliance_Level compliance) {
		pcaSessionImp.compliance = compliance;
	}

	public String setDummyCase() {
		String patient_id = pcaSessionImp.setDummyCase();
		dataManager = pcaSessionImp.getDataManager();
		guidelineManagers = pcaSessionImp.guidelineManagers;

		return patient_id;
		/*
		 * Date currentTime = new java.util.Date(); java.text.SimpleDateFormat
		 * formatter = new java.text.SimpleDateFormat ("yyyy-MM-dd"); String
		 * patient_id = Long.toString(currentTime.getTime()) ;
		 * dataManager.changeCase(patient_id, formatter.format(currentTime));
		 * this.patient_id=patient_id; this.session_time =
		 * formatter.format(currentTime); PaddaGEE guidelineManager; for
		 * (Iterator i=guidelineManagers.values().iterator(); i.hasNext();) {
		 * guidelineManager = (PaddaGEE)i.next();
		 * guidelineManager.resetAdvisories();
		 * guidelineManager.setDBmanager(dataManager); } return patient_id;
		 */
	}

	public String setDummyCase(String patient_id) {
		pcaSessionImp.setDummyCase(patient_id);
		dataManager = pcaSessionImp.getDataManager();
		guidelineManagers = pcaSessionImp.guidelineManagers;

		return patient_id;
	}

	public void setCase(java.lang.String patient_id,
			java.lang.String session_time)
			throws edu.stanford.smi.eon.PCAServerModule.PCA_Initialization_Exception,
			edu.stanford.smi.eon.PCAServerModule.PCA_Initialization_Exception {

		pcaSessionImp.setCase(patient_id, session_time);
		dataManager = pcaSessionImp.getDataManager();
		guidelineManagers = pcaSessionImp.guidelineManagers;
		/*
		 * dataManager.changeCase(patient_id, session_time);
		 * this.patient_id=patient_id; this.session_time = session_time; Date
		 * currentTime = new java.util.Date(); java.text.SimpleDateFormat
		 * formatter = new java.text.SimpleDateFormat ("yyyy-MM-dd");
		 * this.logln("PID"+patient_id+"\t["+formatter.format(currentTime)+"]");
		 * Date startTime = startTiming("\tstarting loading case"); try {
		 * dataManager.loadData(); } catch (Exception e) { e.printStackTrace();
		 * throw new PCA_Initialization_Exception
		 * ("Problem loading patient data for "+patient_id); }
		 * 
		 * stopTiming("\tfinished loading case", startTime);
		 * 
		 * // reinitialize Guideline Manager PaddaGEE guidelineManager; for
		 * (Iterator i=guidelineManagers.values().iterator(); i.hasNext();) {
		 * guidelineManager = (PaddaGEE)i.next();
		 * guidelineManager.resetAdvisories();
		 * guidelineManager.setDBmanager(dataManager); } Runtime rt =
		 * Runtime.getRuntime();
		 * logger.debug("PCASession_i.setCase() Free memory: "+
		 * rt.freeMemory()+" Total memory: "+ rt.totalMemory(), 0); rt.gc();
		 * logger.debug("PCASession_i.setCase() Post GC Free memory: "+
		 * rt.freeMemory()+" Total memory: "+ rt.totalMemory(), 0);
		 */}

	/**
	 * <p>
	 * Operation: <b>::PCAServerModule::PCASession::initializeSession</b>.
	 * 
	 * <pre>
	 *     #pragma prefix "PCAServerModule/PCASession"
	 *     ::PCAServerModule::Guideline_Service_Record initializeSession();
	 * </pre>
	 * 
	 * This method fetches precomputed advisories
	 * </p>
	 */
	public void loadPrecomputedAdvisories(String patient_id,
			String session_time, String storageDirectory)
			throws PCA_Session_Exception {
		// currentGuidelineManager.loadPrecomputedAdvisories();
		pcaSessionImp.loadPrecomputedAdvisories(patient_id, session_time,
				storageDirectory);
		dataManager = pcaSessionImp.getDataManager();
		guidelineManagers = pcaSessionImp.guidelineManagers;

		/*
		 * this.patient_id = patient_id; this.session_time = session_time; Date
		 * currentTime = new Date();
		 * this.logln("PID"+patient_id+"\t"+currentTime.toString());
		 * dataManager.changeCase(patient_id, ""); Date startComputeTime =
		 * startTiming
		 * ("\tstart attempting reloading precomputed advisories "+storageDirectory
		 * +patient_id+"strict");
		 * 
		 * File StorageLocation = new File(storageDirectory,
		 * patient_id+"strict"); try { ObjectInputStream in = new
		 * ObjectInputStream(new FileInputStream(StorageLocation));
		 * guidelineManagers = new HashMap(); try { while (true) { PaddaGEE
		 * guidelineManager = new GuidelineInterpreter( kbManager, dataManager,
		 * guideline); String key = (String)in.readObject();
		 * logger.debug("entry key: "+key, debugLevel);
		 * guidelineManager.restoreAdvisories(in); guidelineManagers.put(key,
		 * guidelineManager);
		 * logger.debug("PCASession_i.loadPrecomputedAdvisories put away: "+key,
		 * debugLevel); } } catch (Exception e) {
		 * logger.error("\tfinished restoring saved recommendations " );
		 * //e.printStackTrace(); } in.close(); } catch (Exception e) {
		 * //e.printStackTrace();
		 * logger.error("problem opening precomputed file" ); throw new
		 * PCA_Session_Exception(e.getMessage()); } finally {
		 * stopTiming("\tFinished attempting reloading precomputed advisories",
		 * startComputeTime); Runtime rt = Runtime.getRuntime();
		 * //logger.debug("PCASession_i.setCase() Free memory: "+
		 * rt.freeMemory()+" Total memory: "+ // rt.totalMemory(), 0); rt.gc();
		 * logger.debug("PCASession_i.setCase() Post GC Free memory: "+
		 * rt.freeMemory()+" Total memory: "+ rt.totalMemory(), 0);
		 * 
		 * }
		 */
	}

	public void saveAdvisoriesAs(String patient_id, String storageDirectory)
			throws PCA_Session_Exception {

		pcaSessionImp.saveAdvisoriesAs(patient_id, storageDirectory);
		/*
		 * this.patient_id = patient_id; Date currentTime = new Date();
		 * this.logln("PID"+patient_id+"\t"+currentTime.toString());
		 * dataManager.renameCase(patient_id);
		 * logger.debug("PCASession_i.saveAdvisoriesAs ", debugLevel); File
		 * StorageLocation = new File(storageDirectory, patient_id+compliance);
		 * try { ObjectOutputStream out = new ObjectOutputStream(new
		 * FileOutputStream(StorageLocation)); for (Iterator
		 * i=guidelineManagers.entrySet().iterator(); i.hasNext();) { Map.Entry
		 * entry = (Map.Entry)i.next();
		 * logger.debug("entry key: "+entry.getKey(), debugLevel);
		 * out.writeObject(entry.getKey());
		 * ((PaddaGEE)entry.getValue()).serializeAdvisories(out);
		 * logger.debug("put away: "+entry.getKey(), debugLevel);
		 * 
		 * } out.close(); } catch (Exception e) { e.printStackTrace(); throw new
		 * PCA_Session_Exception(e.getMessage()); }
		 */
	}

	/**
	 * <p>
	 * Operation: <b>::PCAServerModule::PCASession::updateRecommendations</b>.
	 * 
	 * <pre>
	 *     #pragma prefix "PCAServerModule/PCASession"
	 *     ::PCAServerModule::Guideline_Service_Record updateRecommendations();
	 * </pre>
	 * 
	 * </p>
	 */
	/**
	 * <p>
	 * Operation: <b>::PCAServerModule::PCASession::containsAdvisories</b>.
	 * 
	 * <pre>
	 *     #pragma prefix "PCAServerModule/PCASession"
	 *     boolean containsAdvisories();
	 * </pre>
	 * 
	 * </p>
	 */
	public boolean containsAdvisories() {
		return pcaSessionImp.containsAdvisories();
		/*
		 * Collection managers = guidelineManagers.values(); boolean
		 * hasAdvisories = false; for (Iterator i= managers.iterator();
		 * i.hasNext();) { if (((PaddaGEE)i.next()).containsAdvisories())
		 * hasAdvisories = true; } if (hasAdvisories)
		 * logger.debug("Has advisory ready", debugLevel); else
		 * logger.debug("Does not have advisory ready", debugLevel); return
		 * hasAdvisories;
		 */
	}

	public void resetAdvisories() {
		pcaSessionImp.resetAdvisories();
		/*
		 * Collection managers = guidelineManagers.values(); PaddaGEE
		 * guidelineManager; for (Iterator i= managers.iterator(); i.hasNext();)
		 * { guidelineManager = (PaddaGEE)i.next();
		 * guidelineManager.resetAdvisories(); }
		 */
	}

	/**
	 * <p>
	 * Operation: <b>::PCAServerModule::PCASession::getAdvisories</b>.
	 * 
	 * <pre>
	 *     #pragma prefix "PCAServerModule/PCASession"
	 *     ::PCAServerModule::Guideline_Service_Record getAdvisories();
	 * </pre>
	 * 
	 * </p>
	 */
	public edu.stanford.smi.eon.PCAServerModule.Guideline_Service_Record[] getAdvisories() {
		return pcaSessionImp.getAdvisories();
		/*
		 * Collection managers = guidelineManagers.values(); Collection results
		 * = new ArrayList(); for (Iterator i= managers.iterator();
		 * i.hasNext();) { PaddaGEE g = (PaddaGEE) i.next(); if
		 * (g.containsAdvisories()) results.add(g.returnAdvisory()); }
		 * 
		 * return (Guideline_Service_Record[]) results.toArray(new
		 * Guideline_Service_Record[0]);
		 */}

	public Guideline_Service_Record[] updateAdvisories() {
		return pcaSessionImp.updateAdvisories();
		/*
		 * Date startTime = startTiming("\tstart update advisories"); try { //
		 * Guideline_Service_Record[] adv = computeAdvisories();
		 * logger.error("Recomputed advisories. Length:" +adv.length); for (int
		 * i = 0; i< adv.length;i++) { testServiceRecord(adv[i]); }
		 * stopTiming("\tfinished update advisories", startTime); return adv; }
		 * catch (Exception e) { e.printStackTrace(); return null; }
		 */
	}

	/*
	 * private void testServiceRecord(Guideline_Service_Record gsr) {
	 * //gsr.activity_choices = new Guideline_Activity_Choices[0];
	 * //gsr.decision_points = new Guideline_Action_Choices[0];
	 * //gsr.evaluated_choices= new Guideline_Activity_Evaluations[0];
	 * //gsr.scenario_choices = new Guideline_Scenario_Choices[0] ; //gsr.goals
	 * = new Guideline_Goal[0]; //gsr.subject_classification = new
	 * Conclusion[0]; for (int j=0; j< gsr.subject_classification.length; j++) {
	 * gsr.subject_classification[j].justification =
	 * edu.stanford.smi.eon.util.HelperFunctions.dummyJustification();
	 * logger.debug("case_id: "+gsr.subject_classification[j].case_id+
	 * " time: "+gsr.subject_classification[j].time+
	 * " parameter: "+gsr.subject_classification[j].parameter+
	 * " value: "+gsr.subject_classification[j].value, debugLevel); }
	 * //gsr.assessments = new Current_Activity_Assessment[0]; //gsr.assumption
	 * = dummyCriteria_Evaluation(); // gsr.assumption =
	 * dummyCriteria_Evaluation(Logical_Operator.ATOMIC, // Truth_Value.unknown,
	 * // new Criteria_Evaluation[0], // new Guideline_Entity("", "", "", ""),
	 * ""); }
	 */

	public Guideline_Service_Record[] computeAndStoreAdvisories(
			String storageDirectory) throws PCA_Session_Exception {
		Guideline_Service_Record[] advisories = pcaSessionImp
				.computeAndStoreAdvisories(storageDirectory);
		guidelineManagers = pcaSessionImp.guidelineManagers;
		return advisories;
		/*
		 * Guideline_Service_Record[] advisories; File StorageLocation = new
		 * File(storageDirectory, patient_id+compliance); advisories =
		 * computeAdvisories(); try { ObjectOutputStream out = new
		 * ObjectOutputStream(new FileOutputStream(StorageLocation)); for
		 * (Iterator i=guidelineManagers.entrySet().iterator(); i.hasNext();) {
		 * Map.Entry entry = (Map.Entry)i.next();
		 * logger.debug("entry key: "+entry.getKey(), debugLevel);
		 * out.writeObject(entry.getKey());
		 * ((PaddaGEE)entry.getValue()).serializeAdvisories(out);
		 * logger.debug("put away: "+entry.getKey(), debugLevel);
		 * 
		 * } out.close(); } catch (Exception e) { e.printStackTrace(); throw new
		 * PCA_Session_Exception(e.getMessage()); } return advisories;
		 */
	}

	public boolean computeEligibilityAndGoal() throws PCA_Session_Exception {
		boolean elibilityAndGoal = pcaSessionImp.computeEligibilityAndGoal();
		guidelineManagers = pcaSessionImp.guidelineManagers;
		return elibilityAndGoal;
	}

	public Guideline_Service_Record[] computeAdvisories()
			throws PCA_Session_Exception {
		Guideline_Service_Record[] advisories = pcaSessionImp
				.computeAdvisories();
		guidelineManagers = pcaSessionImp.guidelineManagers;
		return advisories;
	}

	public String topLevelComputeAdvisory(String ptID, String patientData,
			String currentTime, String guidelineName) throws PCA_Session_Exception {

		return pcaSessionImp.topLevelComputeAdvisory(ptID, patientData, currentTime, guidelineName);
	}
	

	public String topLevelComputeAdvisory(String ptID, String patientData,
			String currentTime, String guidelineName, String htmlFile)
			throws PCA_Session_Exception {
		return pcaSessionImp.topLevelComputeAdvisory(ptID, patientData, currentTime, guidelineName, htmlFile);
	}


	public String getExplanationURLs()  {

		return pcaSessionImp.getExplanationURLs();
	}

	public void updateData(
			edu.stanford.smi.eon.PCAServerModule.Patient_Data[] data)
			throws edu.stanford.smi.eon.PCAServerModule.Improper_Data_Exception {
		pcaSessionImp.updateData(data);
	}
	

//	public void updateData(
//			edu.stanford.smi.eon.PCAServerModule.Patient_Data[] data)
//			throws edu.stanford.smi.eon.PCAServerModule.Improper_Data_Exception {
//		for (int i = 0; i < Array.getLength(data); i++) {
//			Patient_Data dataElement = data[i];
//			switch (dataElement.discriminator().value()) {
//			case Patient_Data_Type._numeric_data:
//				logger.debug("in updateData - Numeric Data: operation "
//						+ dataElement.numeric().operation + " domain term:"
//						+ dataElement.numeric().domain_term + "; value: "
//						+ dataElement.numeric().value + "; unit: "
//						+ dataElement.numeric().unit + "; time: "
//						+ dataElement.numeric().valid_time);
//				dataManager.updateNumericData(dataElement.numeric().operation,
//						dataElement.numeric().domain_term.trim(),
//						dataElement.numeric().unit,
//						dataElement.numeric().valid_time,
//						dataElement.numeric().value);
//				break;
//			case Patient_Data_Type._prescription_data:
//				if (dataElement.prescription() != null) {
//					logger.debug("in updateData -Prescription Data: drug_name:"
//							+ dataElement.prescription().drug_name + "; dose: "
//							+ dataElement.prescription().daily_dose
//							+ "; unit: "
//							+ dataElement.prescription().daily_dose_unit
//							+ "; time: "
//							+ dataElement.prescription().start_time + "; sig: "
//							+ dataElement.prescription().sig);
//					dataManager.updatePrescription(
//							dataElement.prescription().operation,
//							dataElement.prescription().drug_name.trim(),
//							dataElement.prescription().daily_dose,
//							dataElement.prescription().daily_dose_unit,
//							dataElement.prescription().start_time,
//							dataElement.prescription().sig);
//				} else
//					logger.warn("in updateData - null prescription");
//				break;
//			case Patient_Data_Type._note_entry_data:
//				logger.debug("in updateData - Note Etnry Data: operation "
//						+ dataElement.note_data().operation + " entry_type:"
//						+ dataElement.note_data().entry_type + " domain term:"
//						+ dataElement.note_data().domain_term + "; value: "
//						+ dataElement.note_data().value);
//				dataManager.updateNoteEntry(dataElement.note_data().operation,
//						dataElement.note_data().entry_type,
//						dataElement.note_data().domain_term.trim(),
//						dataElement.note_data().value,
//						dataElement.note_data().valid_time);
//				break;
//			default:
//				;
//			} // switch
//		} // for
//
//	}

	/**
	 * <p>
	 * Operation: <b>::PCAServerModule::PCASession::printData</b>.
	 * 
	 * <pre>
	 *     #pragma prefix "PCAServerModule/PCASession"
	 *     void printData();
	 * </pre>
	 * 
	 * </p>
	 */
	public String printData() {
		return pcaSessionImp.printData();

	}

	public GuidelineInterpreter getCurrentGuidelineManager() {
		return (GuidelineInterpreter) pcaSessionImp
				.getCurrentGuidelineManager();
	}

/*	public Guideline_Service_Record[] computeAdvisoriesWithSubGuidelines(
			String pid, String sessionTime, String guidelineName)
			throws PCA_Session_Exception {
		// TODO Auto-generated method stub
		return pcaSessionImp.computeAdvisoriesWithSubGuidelines(pid, sessionTime,
				guidelineName);
	}
*/
/*	public Collection<Advisory> computeAdvisory(String pid, String startTime,
			String guidelineName) throws PCA_Session_Exception {
		return (Collection<Advisory>) pcaSessionImp
				.computeAdvisory(pid, startTime, guidelineName);
	}
*/
	public Guideline_Service_Record computePerformanceMeasures(String pid,
			String hospitalizationID, String sessionTime, String guidelineName, String PMName,
			String startTime, String stopTime) throws PCA_Session_Exception {
		
		return pcaSessionImp
				.computePerformanceMeasures(pid, hospitalizationID, sessionTime, guidelineName,
						 PMName, startTime,  stopTime);
	}
	
	public Advisory computePerformanceMeasuresAdvisory(String pid,
			String hospitalizationID, String sessionTime, String guidelineName, String PMName, 
			String startTime, String stopTime) throws PCA_Session_Exception {
		return pcaSessionImp.computePerformanceMeasuresAdvisory(pid, 
				hospitalizationID, sessionTime, guidelineName, PMName, startTime, stopTime);
	}
	
	public String computePerformanceMeasuresXMLWithInputData(String pid,String patientData,
			String hospitalizationID, String sessionTime, String guidelineName, String PMName,
			String startTime, String stopTime) throws PCA_Session_Exception {
		return pcaSessionImp.computePerformanceMeasuresXMLWithInputData(pid, patientData, hospitalizationID, 
				sessionTime, guidelineName, PMName, startTime, stopTime);
	}

	public Advisory computePerformanceMeasuresAdvisoryWithInputData(String pid,String patientData,
			String hospitalizationID, String sessionTime, String guidelineName, String PMName,
			String startTime, String stopTime) throws PCA_Session_Exception {
		return pcaSessionImp.computePerformanceMeasuresAdvisoryWithInputData(pid, patientData, hospitalizationID, 
				sessionTime, guidelineName, PMName, startTime, stopTime);
	}

	public String printAdvisory(Advisory advisory, String format) {
		return pcaSessionImp.printAdvisory(advisory, format);
	}

	public Guideline_Service_Record[] computeAdvisories(String pid,
			String sessionTime, String guidelineName,
			boolean computeSubguidelines) throws PCA_Session_Exception {
		
		return pcaSessionImp.computeAdvisories(pid, sessionTime, guidelineName, computeSubguidelines);
	}



}
