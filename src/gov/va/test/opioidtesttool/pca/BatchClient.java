package gov.va.test.opioidtesttool.pca;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import org.apache.log4j.PropertyConfigurator;

import edu.stanford.smi.eon.PCAServerModule.Compliance_Level;
import edu.stanford.smi.eon.PCAServerModule.Guideline_Service_Record;
import edu.stanford.smi.eon.PCAServerModule.PCAServer_i;
import edu.stanford.smi.eon.PCAServerModule.PCASession;
import edu.stanford.smi.eon.PCAServerModule.PCA_Session_Exception;
import edu.stanford.smi.eon.clients.ClientUtil;
import edu.stanford.smi.protege.model.Project;
import gov.va.test.opioidtesttool.*;
import gov.va.test.opioidtesttool.degui.MainEntryPanel;

public class BatchClient {

	public void initialize() {
		// Get Init File from environment
		String str = System.getProperty("TestEnvironmentINI");
		if (str == null) {
			System.err.println("NO TESTING ENVIRONMENT INI FILE SET!@%$!@$!");
			return;
		}
		GlobalVars.initFile = new String(str);
		System.out.println("Initializing TestingEnvironment");
		loadInitFile();
		loadPatientData();
		GlobalVars.standAlone = false;

	}

	public static void loadInitFile() {
		System.out.println("Initfile: " + GlobalVars.initFile);
		// Load init file
		GlobalVars.settings = new Properties();
		PropertyConfigurator.configure(GlobalVars.initFile);

		try {
			FileInputStream sf = new FileInputStream(GlobalVars.initFile);
			GlobalVars.settings.load(sf);
		} catch (Exception ex) {
			System.out.println("Exception during loading initialization file");
			System.out.println(ex.toString());
			System.exit(2);
		}

		/*
		 * String outdir = System.getProperty("OutputDir"); if ( outdir == null ) {
		 * System.err.println("NO OUTPUT DIRECTORY SET!@%$!@$!"); return; }
		 * outdir = outdir.replace( '\\', '/'); GlobalVars.outputdir = new
		 * String(outdir); System.out.println("Output directory: "+
		 * GlobalVars.outputdir);
		 */

		Properties p = GlobalVars.settings;
		GlobalVars.kbURL = p.getProperty("SERVERKB", "");
		GlobalVars.outputdir = p.getProperty("PCAOUTPUTDIR", "");
		GlobalVars.guidelineName = p.getProperty("GUIDELINEID", "");
		GlobalVars.database = p.getProperty("DATABASE", "");
		GlobalVars.XMLDataFile = p.getProperty("XMLDATAFILE", "");
		String cumulateDoseStr = p.getProperty("CUMULATEDOSE");
		if ((cumulateDoseStr == null) || cumulateDoseStr.equals(""))
			GlobalVars.cumulateDose = true;
		else
			GlobalVars.cumulateDose=cumulateDoseStr.equals("TRUE");

		// Check for setting knowledge base entry points into
		// different data entry panels
		if (p.containsKey("DRUG_ROOT")) {
			String tmp = new String(p.getProperty("DRUG_ROOT"));
			MainEntryPanel.DRUG_ROOT = tmp.split(",");
		}
		if (p.containsKey("DX_ROOT")) {
			String tmp = new String(p.getProperty("DX_ROOT"));
			MainEntryPanel.DX_ROOT = tmp.split(",");
		}
		if (p.containsKey("LABS_ROOT")) {
			String tmp = new String(p.getProperty("LABS_ROOT"));
			MainEntryPanel.LABS_ROOT = tmp.split(",");
		}
		if (p.containsKey("SIGNS_ROOT")) {
			String tmp = new String(p.getProperty("SIGNS_ROOT"));
			MainEntryPanel.SIGNS_ROOT = tmp.split(",");
		}
		if (p.containsKey("DEMOGRAPHICS_ROOT")) {
			String tmp = new String(p.getProperty("DEMOGRAPHICS_ROOT"));
			MainEntryPanel.DEMOGRAPHICS_ROOT = tmp.split(",");
		}
		if (p.containsKey("ADR_ROOT")) {
			String tmp = new String(p.getProperty("ADR_ROOT"));
			MainEntryPanel.ADR_ROOT = tmp.split(",");
		}

		/*
		 * Data structure typing We specify default data structures to send to
		 * the the guideline interpreter by default Here we load any over-rides --
		 * that depend on domain term Start with DATA_TYPE_OVERRIDE_1 , continue
		 * as long as DATA_TYPE_OVERRIDE_N exists.
		 */
		int num = 1;
		GlobalVars.dtor = new DataTypeOverRides();
		String key_nam = "DATA_TYPE_OVERRIDE_" + num;
		while (p.containsKey(key_nam)) {
			String tmp = new String(p.getProperty(key_nam));
			GlobalVars.dtor.add(tmp);
			num++;
			key_nam = new String("DATA_TYPE_OVERRIDE_" + num);
		}
	}

	public static void loadPatientData() {
		GlobalVars.pds = new ArrayList();

		// load data from xml
		XMLInterface.read();

		// no data in xml file
		if (GlobalVars.pds.isEmpty()) {
			System.err.println("NO XML DATA FILE -- loading default patient");
			GlobalVars.pds.add(new PatientDataStore("John Doe"));
		}

		// Set current patient to first PatientData from DataStore
		Iterator pdsIt = GlobalVars.pds.iterator();
		PatientDataStore pds = (PatientDataStore) pdsIt.next();
		GlobalVars.currentPatient = pds;
	}


	public static void main(String[] args) {

		String caseData = null;
		PrintWriter itsWriter = null;
		PCASession pca = null;
		Guideline_Service_Record[] dssOutput = null;
		java.util.Date startTime = new java.util.Date();
		BufferedReader reader = null;
		PCAServer_i PCAImp = null;
		edu.stanford.smi.eon.PCAServerModule.PCAServer PCARef = null;

		if (args.length < 2) {
			System.out
					.println("BatchClient needs initialization and case id files as arguments");
			return;
		}
		String casesfile = args[1];
		// Set everything up
		GlobalVars.initFile = new String(args[0]);
		System.out.println("PCATest:initfile: " + GlobalVars.initFile
				+ " cases file: " + casesfile);
		loadInitFile();
		loadPatientData();
		GlobalVars.standAlone = true;
		// we now initialize PCA interface right before computing
		// recommendations, this allows us to see if we updated
		// the knowledge base and reload the new one
		// PCAInterface.initialize();

		Collection error_messages = new Vector();

		System.out.println("Loading project " + GlobalVars.kbURL);
		GlobalVars.project = new Project(GlobalVars.kbURL, error_messages);
		GlobalVars.kb = GlobalVars.project.getKnowledgeBase();

		try {
			reader = new BufferedReader(new FileReader(casesfile));

		} catch (FileNotFoundException e) {
			System.out.println("Error opening cases file ");
			e.printStackTrace();
			System.exit(-1);
		} // try

		String line = "";
		try {
			line = reader.readLine();
		} catch (Exception io) {
			System.out.println("Error reading first line from casesfile");
			System.exit(-1);
		}

		if (line == null) {
			System.out.println("No patient ids found in file:" + casesfile);
		} // if

		String patientId;
		while (line != null) {
			patientId = line;
			dssOutput = null;
			pca.setCumulativeFlag(GlobalVars.cumulateDose);
			pca.resetAdvisories();
			pca.compliance(Compliance_Level.strict);
			System.out.println();
			System.out.println("Case id: " + patientId);
			try {
				pca.loadPrecomputedAdvisories(patientId, "2001-02-23",
						GlobalVars.outputdir);
			} catch (Exception e) {
				// no precomputed advisories
				System.out.println("No precomputed data: " + e.getMessage());
			}

			if (pca.containsAdvisories()) {
				dssOutput = pca.getAdvisories();
			} else {
				java.util.Date startLoad = new java.util.Date();
				try {
					pca.setCase(patientId, "1999-12-23");
					java.util.Date finishedLoad = new java.util.Date();
					System.out.println("finished loading case ***Took "
							+ (finishedLoad.getTime() - startLoad.getTime())
							+ " milliseconds");
				} catch (Exception e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
				java.util.Date startcomputeAdvisories = new java.util.Date();
				try {
					dssOutput = pca.computeAndStoreAdvisories(GlobalVars.outputdir);
					java.util.Date finishedcomputeAdvisories = new java.util.Date();
					System.out
							.println("finished compute advisories. ***Took "
									+ (finishedcomputeAdvisories.getTime() - startcomputeAdvisories
											.getTime())
									+ " milliseconds after start");
				} catch (PCA_Session_Exception e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
			}
			caseData = pca.printData();
			String fileName = GlobalVars.outputdir + patientId + ".html";
			File outputFile = new File(fileName);
			;
			File oldFile = new File(GlobalVars.outputdir + patientId + "~.html");
			;
			// Check to see if file already exists
			if (outputFile.exists()) {
				if (oldFile.exists()) {
					oldFile.delete();
				}
				if (!outputFile.renameTo(oldFile)) {
					System.out.println(oldFile
							+ " problem saving old files! Exit.");
					System.exit(1);
				}
			}
			try {
				itsWriter = new PrintWriter(new FileWriter(outputFile), true);
				ClientUtil.printHeader(itsWriter, pca.patient_id());
				itsWriter.print(caseData);

				if (dssOutput != null) {
					for (int j = 0; j < dssOutput.length; j++) {
						ClientUtil.showResultWithKB(dssOutput[j], itsWriter,
								PCAImp.kbManager.getKB());

					}
					itsWriter.close();
				}
			} catch (IOException e) {

			}

			System.out.println("Completed case");
			try {
				line = reader.readLine();
			} catch (IOException ioe) {
				System.out.println("Error reading cases file ");
				ioe.printStackTrace();
				System.exit(-1);
			}
		} // while

	}

}
