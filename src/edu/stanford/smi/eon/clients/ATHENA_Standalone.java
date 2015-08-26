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
import java.io.*;
import java.util.Collection;

import org.apache.log4j.*;

public class ATHENA_Standalone extends Object {
	public ATHENA_Standalone() {
	}

	static Logger logger = Logger.getLogger(ATHENA_Standalone.class);

	public static void main(String[] argv) {
		new ATHENA_Standalone();
		String caseData=null;
		// In the "old way" of invoking EON guideline execution engine, a client access 
		// a server, and get a "session" object through which it interacts with the execution engine.
		PCAServer_i PCAImp = null;
		PCASession pca = null;
		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.WARN);
		if (argv.length < 6) {
			logger.error("ATHENA_Standalone needs case id,  case data file, KB path,  output directory, guideline name, and file extension as arguments");
			return;
		}
		String pid = argv[0];
		String filePath = argv[1];
		String kbURL = argv[2]; 
		String outputdir = argv[3];
		String guidelineId = argv[4];
		String fileExtension = argv[5];

		logger.info("ATHENA Standalone: "+filePath);
		java.util.Date startTime = new java.util.Date();

		try {
			PCAImp = new PCAServer_i();
			PCAImp.kbManager = new KBHandler(kbURL); //Sever loads the KB
			java.util.Date finishedKB = new java.util.Date();
			logger.warn("finished loading KB "+(finishedKB.getTime() -
					startTime.getTime())+ " milliseconds after start.");
			pca = PCAImp.open_pca_session();
			if ((guidelineId != null) && (guidelineId != "")) {
				pca.setGuideline(guidelineId); // Specifies the guideline to use
			} else logger.error("No GUIDELINEID specified");
		} catch(Exception se) {
			logger.error("Exception raised during initialization " +
					se.toString());
			System.exit(1);
		}

//		*/

		try {
			caseData =  readFileAsString(filePath);
		} catch (java.io.IOException e) {
			logger.error("Error reading data file ", e);
			System.exit(-1);
		} 
		String recommendations = null;
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
				"yyyy-MM-dd");
		String currentTime = formatter.format(startTime);

		try {
			String htmlFile = outputdir+pid+ guidelineId.replace(' ', '_').replace('\\',  '_').replace('/', '_') +".html";
			recommendations = pca.topLevelComputeAdvisory( pid, caseData,  currentTime, guidelineId, htmlFile );
//			String explanationURLs = pca.getExplanationURLs();
			String fileName = outputdir+pid+fileExtension;
//			String explanationURLsFile = outputdir+"explanationURL"+fileExtension;
			writeToFile(fileName, recommendations);
//			writeToFile(explanationURLsFile, explanationURLs);
		} catch (PCA_Session_Exception e1) {
			e1.printStackTrace();
		}
		pca.finishSession();
		/* end testing code */
		

	}
	
	private static void writeToFile(String filePath, String string) {
		File outputFile = new File(filePath);;
		if (outputFile.exists()) {
			outputFile.delete();
		}
		try {
			PrintWriter out = new PrintWriter(filePath);
			out.print(string);
			out.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
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

