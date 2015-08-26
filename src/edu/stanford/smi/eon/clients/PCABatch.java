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

/***
  We assume that we have a testResult subdirectory in ATHENA_TestEnvironment that 
  has two subdirectories working/ and archive/.

  We add  regression_batch.bat/.ini/.xml  files in in the batch/ini/data 
  directories respectively.  In the ATHENASharedKB folder we have a 
  ATHENA_CVD.pprj/pont/pins KB that includes the 5 cardiovascular/diabetes ATHENA KBs.

  regression_batch.ini, among other things, specifies which folder contains the "baseline" 
  output to be compared with the current output.
  
  Output is written to the working directory and an archive subdirectory indexed by
  the date when the batch program is written

  For example, on 2012/09/04, whenever you run regression_batch.bat, the system generates  
  and overwrite 20120904/TestCase1.html and working/TestCase1-new.html.
  20120904/TestCase1.html and working/TestCase1-new.html have exactly the same content.

  This way you always have the archive result for the day and know which file (-new) is 
  the most recent.


  ATHENA_TestEnvironment/
    batch/regression_batch.bat
    ini/regression_batch.ini
    data/regression_batch.xml (data for TestCase1, ...)
    testresult/
        working/
            TestCase1-new.html
            TestCase2-new.html
        archive/
            20120904/
            	TestCase1.html
            	TestCase2.html
            20120903/
            	TestCase1.html
            	TestCase2.html

  The batch program takes two arguments (initialization file and session time) as arguments

*/

import edu.stanford.smi.eon.PCAServerModule.*;
import edu.stanford.smi.eon.kbhandler.KBHandler;
import java.util.*;
import java.io.*;

public class PCABatch {
	private static String kbURL;
	private static String database;
	private static String outputdir;
	private static String user;
	private static String password;
	private static void printProperties() {
		String name = System.getProperty("java.vm.name");
		String version = System.getProperty("java.vm.version");
		String info = System.getProperty("java.vm.info");
		System.out.println("JVM: " + name + " - " + version + ", " + info);
	}

	public PCABatch() {
	}

	public static void main(String[] argv) {
		PCABatch PCABatch = new PCABatch();
		printProperties();
		String caseData=null;
		PrintWriter itsWriter = null;
		PCASession pca = null;
		Guideline_Service_Record[] dssOutput=null;
		new Properties();
		java.util.Date startTime = new java.util.Date();
		BufferedReader reader = null;
		PCAServer_i PCAImp = null;
		if (argv.length < 2) {
			System.out.println("PCATest needs initialization and session time as arguments");
			return;
		}
		String initfile = argv[0];
		String casesfile = argv[1];
		System.out.println("PCATest:initfile: " + initfile + " cases file: "+casesfile);
		Properties settings = new Properties();
		try {
			FileInputStream sf = new FileInputStream(initfile);
			settings.load(sf);
		} catch (Exception ex) {
			System.out.println("Exception during loading initialization file");
			System.out.println(ex.toString());
		}
		kbURL = settings.getProperty("SERVERKB", "");
		database = settings.getProperty("DATABASE", "");
		outputdir = settings.getProperty("PCAOUTPUTDIR", "");
		user = null;
		password = null;
		settings.getProperty("PCA_CORBA_SERVER", "");
		String guidelineId = settings.getProperty("GUIDELINEID", null);

		try {
			PCAImp = new PCAServer_i();
			java.util.Date startKB = new java.util.Date();
			System.out.println("start loading KB "+(startKB.getTime() -
					startTime.getTime())+ " milliseconds after start");
			PCAImp.kbManager = new KBHandler(kbURL);
			java.util.Date finishedKB = new java.util.Date();
			System.out.println("finished loading KB "+(finishedKB.getTime() -
					startTime.getTime())+ " milliseconds after start. ***Took "+
					(finishedKB.getTime()-startKB.getTime())+" milliseconds");
			PCAImp.kbURLString = kbURL;
			try {
				pca = PCAImp.open_pca_session(database, user, password, initfile);
			} catch (Throwable e) {
				System.out.println("Problem creating PCA session");
				e.printStackTrace();
				System.exit(-1);
			}

		} catch(Exception se) {
			System.out.println("Exception raised during creation of PCAServer_i " +
					se.toString());
			System.exit(1);
		}
//		*/
		try {
			pca.setGuideline(  guidelineId );
		} catch (Throwable e) {
			System.out.println("Problem creating PCA session");
			e.printStackTrace();
			System.exit(-1);
		}

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
			dssOutput=null;
			pca.resetAdvisories();
			pca.compliance(Compliance_Level.strict);
			System.out.println();
			System.out.println("Case id: "+patientId);
			try {
				pca.loadPrecomputedAdvisories(patientId, "2001-02-23", outputdir);
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
					System.out.println("finished loading case ***Took "+(finishedLoad.getTime() -
							startLoad.getTime())+ " milliseconds");
				} catch ( Exception e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
				java.util.Date startcomputeAdvisories = new java.util.Date();
				try {
					dssOutput = pca.computeAndStoreAdvisories(outputdir);
					java.util.Date finishedcomputeAdvisories = new java.util.Date();
					System.out.println("finished compute advisories. ***Took "+(finishedcomputeAdvisories.getTime() -
							startcomputeAdvisories.getTime())+ " milliseconds after start");
				} catch ( PCA_Session_Exception e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
			}
			caseData = pca.printData();
			String fileName = outputdir+patientId+".html";
			File outputFile = new File(fileName);;
			File oldFile = new File(outputdir+patientId+"~.html");;
			// Check to see if file already exists
			if (outputFile.exists()) {
				if (oldFile.exists()) {
					oldFile.delete();
				}
				if (!outputFile.renameTo(oldFile)) {
					System.out.println(oldFile + " problem saving old files! Exit.");
					System.exit(1);
				}
			}
			try {
				itsWriter = new PrintWriter (new FileWriter(outputFile), true);
				ClientUtil.printHeader(itsWriter, pca.patient_id());
				itsWriter.print(caseData);

				if (dssOutput != null) {
					for (int j=0; j< dssOutput.length; j++) {
						ClientUtil.showResultWithKB(dssOutput[j], itsWriter, PCAImp.kbManager.getKB());

					}
					itsWriter.close();
				}
			} catch (IOException e) {

			}

			System.out.println("Completed case");
			try {
				line = reader.readLine();
			} catch (IOException ioe) {
				System.out.println( "Error reading cases file ");
				ioe.printStackTrace();
				System.exit(-1);
			}
		} //while

	
	pca.finishSession();
}

}
