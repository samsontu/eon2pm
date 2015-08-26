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
package edu.stanford.smi.eon.clients;
import edu.stanford.smi.eon.PCAServerModule.*;
import edu.stanford.smi.eon.guidelineinterpreter.GuidelineInterpreter;
import edu.stanford.smi.eon.kbhandler.KBHandler;
import edu.stanford.smi.eon.kbhandler.WhereComparisonFilter;
import edu.stanford.smi.eon.servers.PCAServer;
import edu.stanford.smi.eon.util.HelperFunctions;
import edu.stanford.smi.eon.Dharma.Management_Guideline;

import java.util.*;
import java.io.*;
import java.lang.reflect.Array;

import org.apache.log4j.*;

public class PCA_XMLTest extends Object {
	private boolean invokedStandalone = true;
	private static String kbURL;
	private static String database;
	private static String outputdir;
	private static String guidelineId;
	private static String user ;
	private static String password;
	private static boolean useCORBA = false;
	private static Compliance_Level complianceLevel = Compliance_Level.strict;
	public PCA_XMLTest() {
	}


	private static void printProperties() {
		String name = System.getProperty("java.vm.name");
		String version = System.getProperty("java.vm.version");
		String info = System.getProperty("java.vm.info");
		logger.info("JVM: " + name + " - " + version + ", " + info);
	}
	static Logger logger = Logger.getLogger(PCA_XMLTest.class);

	public static void main(String[] argv) {
		PCA_XMLTest pCATest = new PCA_XMLTest();
		printProperties();
		String caseData=null;
		PrintWriter itsWriter = null;
		PCASession pca = null;
		Guideline_Service_Record[] dssOutput=null;
		Properties orbProperties = new Properties();
		java.util.Date startTime = new java.util.Date();
		BufferedReader reader = null;
		boolean usingCORBA = false;
		PCAServer_i PCAImp = null;
		edu.stanford.smi.eon.PCAServerModule.PCAServer PCARef = null;

		if (argv.length < 2) {
			logger.error("PCATest needs initialization and case id files as arguments");
			return;
		}
		String initfile = argv[0];
		String casesfile = argv[1];
		logger.info("PCATest:initfile: " + initfile + " cases file: "+casesfile);
		Properties settings = new Properties();
		try {
			FileInputStream sf = new FileInputStream(initfile);
			settings.load(sf);
		} catch (Exception ex) {
			logger.error("Exception during loading initialization file");
			logger.error(ex.toString());
		}
		kbURL = settings.getProperty("SERVERKB", "");
		database = settings.getProperty("DATABASE", "");
		guidelineId = settings.getProperty("GUIDELINEID");
		outputdir = settings.getProperty("PCAOUTPUTDIR", "");
		PropertyConfigurator.configure(initfile);
		System.out.println("Guideline ID: "+guidelineId);

		user = null;
		password = null;
		String usingCORBAString = settings.getProperty("PCA_CORBA_SERVER", "");
		String PCAServerName =  settings.getProperty("PCAServerName", "");
		String serverLogFile = settings.getProperty("SERVER_LOGFILE");




		try {
			PCAImp = new PCAServer_i();
			java.util.Date startKB = new java.util.Date();
			logger.info("start loading KB "+(startKB.getTime() -
					startTime.getTime())+ " milliseconds after start");
			PCAImp.kbManager = new KBHandler(kbURL);
			if ((serverLogFile != null) && (!serverLogFile.equals("")) &&
					(!serverLogFile.equals("no"))){
				logger.info("serverLogFile: "+serverLogFile);
				PCAImp.setServerLog(serverLogFile);
			}
			java.util.Date finishedKB = new java.util.Date();
			logger.info("finished loading KB "+(finishedKB.getTime() -
					startTime.getTime())+ " milliseconds after start. ***Took "+
					(finishedKB.getTime()-startKB.getTime())+" milliseconds");
			PCAImp.kbURLString = kbURL;
			try {
				pca = PCAImp.open_pca_session(database, user, password, initfile );
			} catch (Throwable e) {
				logger.error("Problem creating PCA session");
				e.printStackTrace();
				//System.exit(-1);
			}

		} catch(Exception se) {
			logger.error("Exception raised during creation of PCAServer_i " +
					se.toString());
			System.exit(1);
		}

//		*/
		try {
			if ((guidelineId != "") && (guidelineId != null)) {
				pca.setGuideline(guidelineId); 
			} else logger.error("No GUIDELINEID in the ini file");
		} catch (Throwable e) {
			logger.error("Problem creating PCA session", e);

			System.exit(-1);
		}

		try {
			reader = new BufferedReader(new FileReader(casesfile));

		} catch (FileNotFoundException e) {
			logger.error("Error opening cases file ", e);

			System.exit(-1);
		} // try

		String line = "";
		try {
			line = reader.readLine();
		} catch (Exception io) {
			logger.error("Error reading first line from casesfile");
			System.exit(-1);
		}

		if (line == null) {
			logger.error("No patient ids found in file:" + casesfile);
		} // if

		String patientId;
		while (line != null) {
			patientId = line;
			dssOutput=null;
			pca.resetAdvisories();
			pca.compliance(Compliance_Level.strict);
			try {
				logger.info("patientId: "+patientId +"; outputdir: "+outputdir);
				if ((database != null)&& (!database.equals(""))) {
					java.util.Date startDB = new java.util.Date();
					pca.setCase(patientId, "2008-06-30");
					java.util.Date endDB = new java.util.Date();
					logger.info("Loaded DB, loading case took "+(endDB.getTime() -
							startDB.getTime())+ " milliseconds");
				} else {
					String possibleDataFile=outputdir+patientId+".dat";
					File caseFile = new File(possibleDataFile);
					if (caseFile.canRead()) 
						pca.setCase(possibleDataFile, "2008-07-21");
					else 
						//pca.setCase(patientId, "2006-12-12");
						pca.loadPrecomputedAdvisories(patientId, "2008-07-21", outputdir);
				}
			} catch (Exception  e) {
				// no precomputed advisories
				logger.error("No precomputed data: " + e.getMessage());
			}


//			Patient_Data[] data = new Patient_Data[5];
//			data[0] = new Patient_Data();
//			data[0].note_data(new Note_Entry_Data(Data_Operation_Type.add,
//					Entry_Type.problem, "UDS", "POS", "2007-12-14", ""));
//			data[1] = new Patient_Data();
//			data[1].numeric(new Numeric_Data(Data_Operation_Type.add,
//					"Potassium",  "mm/mm", "2007-02-14", "2.0"));
//			data[2] = new Patient_Data();
//			data[2].prescription(new Prescription_Data(Data_Operation_Type.add,
//					"lisinopril",  100, "","", "", ""));
//			data[3] = new Patient_Data();
//			data[3].prescription(new Prescription_Data(Data_Operation_Type.add,
//					"hydrochlorothiazide",  50, "", "", "", ""));
//			data[4] = new Patient_Data();
//			data[4].note_data(new Note_Entry_Data(Data_Operation_Type.add,
//					Entry_Type.problem, "Hypertension", "", "2007-12-14", ""));
//			while (null == null) {
			try {
//				pca.updateData(data);
				dssOutput = pca.updateAdvisories();
//				dssOutput =pca.computeAndStoreAdvisories(outputdir);
			} catch (Exception e) {
				e.printStackTrace();
			} 

			/*			java.util.Date startcomputeAdvisories = new java.util.Date();
			try {
				dssOutput = pca.computeAdvisories();
				java.util.Date finishedcomputeAdvisories = new java.util.Date();
				logger.info("finished compute advisories. ***Took "+(finishedcomputeAdvisories.getTime() -
						startcomputeAdvisories.getTime())+ " milliseconds after start");
			} catch ( PCA_Session_Exception e) {
				logger.error(e.getMessage(), e);

			} 
			 */


			//     }
			try {
				caseData = pca.printData();
			} catch (Exception e) {
				logger.error("Error printing data");
				e.printStackTrace();
			}
			String fileName = outputdir+patientId+".html";
			File outputFile = new File(fileName);;
			File oldFile = new File(outputdir+patientId+"~.html");;
			// Check to see if file already exists
			if (outputFile.exists()) {
				if (oldFile.exists()) {
					oldFile.delete();
				}
				if (!outputFile.renameTo(oldFile)) {
					logger.error(oldFile + " problem saving old files! Exit.");
					System.exit(1);
				}
			}
			try {
				itsWriter = new PrintWriter (new FileWriter(outputFile), true);
				ClientUtil.printHeader(itsWriter, pca.patient_id());
				itsWriter.print(caseData);

				//If the patient is eligible and the guideline has subguidelines, then do each
				if (dssOutput != null) {
					for (int j=0; j< dssOutput.length; j++) {
						ClientUtil.showResultWithKB(dssOutput[j], itsWriter, PCAImp.kbManager.getKB());

					}
					// Patient is eligible
					if (isEligible(dssOutput, guidelineId)){
						Collection subguidelines = subGuidelines(PCAImp.kbManager, guidelineId);
						if (subguidelines != null ) {
							for (Iterator i=subguidelines.iterator(); i.hasNext(); ) {
								Management_Guideline sub=(Management_Guideline)i.next();
								try {
									logger.error("executing subguideline: "+getGuidelineId(sub));
									pca.setGuideline(getGuidelineId(sub)); 
									dssOutput = pca.updateAdvisories();
									if (dssOutput != null) {
										itsWriter.println(getGuidelineId(sub));
										for (int j=0; j< dssOutput.length; j++) {
											ClientUtil.showResultWithKB(dssOutput[j], itsWriter, PCAImp.kbManager.getKB());
										}
									}
								} catch (Exception e) {
									logger.error("Exception in computing advisory for "+ getGuidelineId(sub));
									logger.error(e.getMessage());
									e.printStackTrace();
								}
							}
						}
					}
					itsWriter.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			logger.info("TestPadda.Main Completed case : "+patientId);
			try {
				line = reader.readLine();
			} catch (IOException ioe) {
				logger.error( "Error reading cases file ", ioe);

				System.exit(-1);
			}
			//} //while

			pca.finishSession();
			/* end testing code */
		}

	}

	static boolean isEligible( Guideline_Service_Record[] dssOutput, String guidelineName) {
		boolean isEligible = false;
		for (int j=0; j < dssOutput.length; j=j+1){
			if (dssOutput[j].subject_classification != null) {
				for (int i=0; i < dssOutput[j].subject_classification.length; i++) {
					if (dssOutput[j].subject_classification[i] != null) {
						Conclusion classification = (Conclusion)dssOutput[j].subject_classification[i];
						if (classification.parameter.equals(guidelineName)){
							isEligible = classification.value.equals(Truth_Value._true.toString());
							return isEligible;
						}
					}
				}
			}
		}
		return isEligible;
	}

	static String getGuidelineId( Management_Guideline guideline) {
		return guideline.getBrowserText();
	}

	static Collection subGuidelines( KBHandler kbManager, String guidelineId) {
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
}

