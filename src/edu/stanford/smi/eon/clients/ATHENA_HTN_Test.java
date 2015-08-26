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
import org.apache.log4j.*;

public class ATHENA_HTN_Test extends Object {
	private static String guidelineId = "VA/JNC-VII Hypertension Guideline";
	private static String fileExtension = "HTN.xml";
	public ATHENA_HTN_Test() {
	}

	static Logger logger = Logger.getLogger(ATHENA_HTN_Test.class);

	public static void main(String[] argv) {
		new ATHENA_HTN_Test();
		String caseData=null;
		// In the "old way" of invoking EON guideline execution engine, a client access 
		// a server, and get a "session" object through which it interacts with the execution engine.
		PCAServer_i PCAImp = null;
		PCASession pca = null;
		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.WARN);
		if (argv.length < 4) {
			logger.error("PCATest needs case id,  case data file, KB path, and output directory as arguments");
			return;
		}
		String pid = argv[0];
		String filePath = argv[1];
		String kbURL = argv[2]; 
		String outputdir = argv[3];

		logger.info("ATHENA HTN Test: "+filePath);
		java.util.Date startTime = new java.util.Date();

		try {
			PCAImp = new PCAServer_i();
			PCAImp.kbManager = new KBHandler(kbURL); //Sever loads the KB
			java.util.Date finishedKB = new java.util.Date();
			logger.warn("finished loading KB "+(finishedKB.getTime() -
					startTime.getTime())+ " milliseconds after start.");
			pca = PCAImp.open_pca_session();
			if ((guidelineId != null) && (!guidelineId.equals(""))) {
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
		try {
			recommendations = pca.topLevelComputeAdvisory( pid, caseData,  startTime.toString(), guidelineId );
			String fileName = outputdir+pid+fileExtension;
			File outputFile = new File(fileName);;
			if (outputFile.exists()) {
				outputFile.delete();
			}
			try {
				PrintWriter out = new PrintWriter(fileName);
				out.print(recommendations);
				out.flush();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} catch (PCA_Session_Exception e1) {
			e1.printStackTrace();
		}
		pca.finishSession();
		/* end testing code */
		

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

