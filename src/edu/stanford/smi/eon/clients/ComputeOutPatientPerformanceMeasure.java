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
import edu.stanford.smi.eon.kbhandler.KBHandler;
import gov.va.athena.advisory.Advisory;

import java.io.*;
import org.apache.log4j.*;

public class ComputeOutPatientPerformanceMeasure extends Object {
	private static String guidelineName = "Heart Failure Performance Measures";
	public ComputeOutPatientPerformanceMeasure() {
	}

	static Logger logger = Logger.getLogger(ComputeOutPatientPerformanceMeasure.class);

	public static void main(String[] argv) {
		String caseData=null;
		// In the "old way" of invoking EON guideline execution engine, a client access 
		// a server, and get a "session" object through which it interacts with the execution engine.
		PCAServer_i PCAImp = null;
		PCASession pca = null;
		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.WARN);
		if (argv.length < 6) {
			logger.error("ComputePerformanceMeasure needs case id,  session time, performance start and stop times, case data file, and KB path as arguments");
			return;
		}
		String pid = argv[0];
		String sessionTime = argv[1];
		String PMStartTime = argv[2];
		String PMStopTime = argv[3];
		String dataFilePath = argv[4];
		String kbURL = argv[5]; 

		logger.info("Performance Measure: "+dataFilePath);
		java.util.Date startTime = new java.util.Date();

		try {
			PCAImp = new PCAServer_i();
			PCAImp.kbManager = new KBHandler(kbURL); //Sever loads the KB
			java.util.Date finishedKB = new java.util.Date();
			logger.warn("finished loading KB "+(finishedKB.getTime() -
					startTime.getTime())+ " milliseconds after start.");
			pca = PCAImp.open_pca_session();
			if ((guidelineName != null) && (guidelineName != "")) {
				pca.setGuideline(guidelineName); // Specifies the guideline to use
			} else logger.error("No GUIDELINEID specified");
		} catch(Exception se) {
			logger.error("Exception raised during initialization " +
					se.toString());
			System.exit(1);
		}

		//		*/

		try {
			caseData =  readFileAsString(dataFilePath);
		} catch (java.io.IOException e) {
			logger.error("Error reading data file ", e);
			System.exit(-1);
		} 
		try {
			Advisory pmOutput = pca.computePerformanceMeasuresAdvisoryWithInputData(pid, caseData, null, sessionTime, guidelineName, 
					null, PMStartTime, PMStopTime);
			if (pmOutput != null)
				displayPMOutput(pmOutput);
			else
				System.out.println("No output!");
		} catch (PCA_Session_Exception e1) {
			e1.printStackTrace();
		} 
		/* end testing code */


	}

	private static void displayPMOutput(Advisory pmOutput) {
		System.out.println("Advisory time: "+ pmOutput.getAdvisory_time());
		System.out.println("Case ID: "+pmOutput.getCase_id());
		System.out.println("Start time: "+ pmOutput.getStart_time());
		System.out.println("Stop time: "+ pmOutput.getStop_time());
		System.out.println("GuidelineID: "+ pmOutput.getGuideline_id());
		System.out.println("_____________________");
		if (pmOutput.getEvaluated_goal() != null) {
			for (gov.va.athena.advisory.Guideline_Goal pm : pmOutput.getEvaluated_goal()) {
				System.out.println("Performance measure:   "+pm.getKb_goal_id());
				System.out.println("Denominator criteria:  "+pm.getReason_for_goal());
				System.out.println("Numerator criteria:    "+pm.getGoal());
				System.out.println("Satisfy numerator criteria:    "+pm.getAchieved());
				System.out.println("Data:    "+pm.getData());
				System.out.println("______________________");
			}
		}

	}

	private static String readFileAsString(String filePath) throws java.io.IOException{
		byte[] buffer = new byte[(int) new File(filePath).length()];
		BufferedInputStream f = null;
		try {
			f = new BufferedInputStream(new FileInputStream(filePath));
			f.read(buffer);
		} finally {
			if (f != null) try { f.close(); } catch (IOException ignored) { }
		}
		return new String(buffer);
	}


}

