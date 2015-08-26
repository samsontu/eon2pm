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
import edu.stanford.smi.eon.execEngine.EONConstants;
import edu.stanford.smi.eon.execEngine.IEON;
import edu.stanford.smi.eon.kbhandler.KBHandler;
import gov.va.athena.advisory.Advisory;

import java.io.*;
import java.util.Properties;

import org.apache.log4j.*;

public class ComputePerformanceMeasureXML extends Object {
	private static String guidelineName = "Heart Failure Performance Measures";
	private static String fileExtension = "HF.html";
	public ComputePerformanceMeasureXML() {
	}

	static Logger logger = Logger.getLogger(ComputePerformanceMeasureXML.class);

	public static void main(String[] argv) {
		String caseData=null;
		// In the "old way" of invoking EON guideline execution engine, a client access 
		// a server, and get a "session" object through which it interacts with the execution engine.
		PCAServer_i PCAImp = null;
		PCASession pca = null;

		if (argv.length < 4) {
			System.out.println("ComputePerformanceMeasure needs (1) path to init file, (2) session time,  (3, 4) performance measure period's start and stop times as arguments");
			return;
		}
		String initFileFullPath = argv[0];
		String sessionTime = argv[1];
		String PMStartTime = argv[2];
		String PMStopTime = argv[3];

		Properties settings = new Properties();
		try {
			FileInputStream sf = new FileInputStream(initFileFullPath);
			settings.load(sf);
		} catch (Exception ex) {
			logger.error("Exception during loading initialization file");
			logger.error(ex.toString());
		}
		logger.info("Input parameters: "+ initFileFullPath+" "+sessionTime+" "+ PMStartTime +" "+ PMStopTime); 

		String guidelineName  = settings.getProperty("GUIDELINENAME", "");
		String context =settings.getProperty("CONTEXT", "");
		String kbURL =  settings.getProperty("PMKB", "");
		String outputdir = settings.getProperty("OUTPUTDIR", "C:/Dropbox/");
		String pid = settings.getProperty("PATIENTID", "");
		String dataFilePath = settings.getProperty("DATAFILEPATH", "");
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
			e.printStackTrace();
			System.exit(-1);
		} 
		try {
			Advisory pmOutput = pca.computePerformanceMeasuresAdvisoryWithInputData(pid, caseData, null, sessionTime, guidelineName, 
					null, PMStartTime, PMStopTime);
			String fileName = outputdir+pid+fileExtension;
			File outputFile = new File(fileName);;
			if (outputFile.exists()) {
				outputFile.delete();
			}
			try {
				PrintWriter out = new PrintWriter(fileName);
				out.println("<html><head></head><body>");
				out.println("<h1>Case ID: "+pid+"</h1>");
				out.print(pca.printData());
				out.print(pca.printAdvisory(pmOutput, IEON.HTML));
				out.println("</body></html>");
				out.flush();
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} catch (PCA_Session_Exception e1) {
			e1.printStackTrace();
		} finally {
			pca.finishSession();
		}

		/* end testing code */


	}

	private static String readFileAsString(String filePath) throws java.io.IOException{
		byte[] buffer = new byte[(int) new File(filePath).length()];
		BufferedInputStream f = null;
		try {
			File inputFile = new File(filePath); 
			if (inputFile.canRead()) {
				f = new BufferedInputStream(new FileInputStream(inputFile));
				f.read(buffer);
			} else {
				logger.error("Can't read "+filePath);
				throw new java.io.IOException("Can't read "+filePath);
			}
		} finally {
			if (f != null) try { f.close(); } catch (IOException ignored) { }
		}
		return new String(buffer);
	}


}

