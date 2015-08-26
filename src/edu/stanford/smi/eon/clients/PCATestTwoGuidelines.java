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
 * Created on Feb 10, 2006
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
package edu.stanford.smi.eon.clients;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import edu.stanford.smi.eon.PCAServerModule.Compliance_Level;
import edu.stanford.smi.eon.PCAServerModule.Data_Operation_Type;
import edu.stanford.smi.eon.PCAServerModule.Entry_Type;
import edu.stanford.smi.eon.PCAServerModule.Guideline_Service_Record;
import edu.stanford.smi.eon.PCAServerModule.Note_Entry_Data;
import edu.stanford.smi.eon.PCAServerModule.Numeric_Data;
import edu.stanford.smi.eon.PCAServerModule.PCAServer_i;
import edu.stanford.smi.eon.PCAServerModule.PCASession;
import edu.stanford.smi.eon.PCAServerModule.Patient_Data;
import edu.stanford.smi.eon.kbhandler.KBHandler;

public class PCATestTwoGuidelines {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	    PCATestTwoGuidelines pCATestDummyCase = new PCATestTwoGuidelines();
	    String caseData=null;
	    PrintWriter itsWriter = null;
	    PCASession pca = null;
	    Guideline_Service_Record[] dssOutput=null;
	    PCAServer_i PCAImp = null;
	    edu.stanford.smi.eon.PCAServerModule.PCAServer PCARef = null;

	     if (args.length < 1) {
	        System.out.println("PCATest needs initialization file as argument");
		      return;
	    }
	    String initfile = args[0];
	    System.out.println("PCATest:initfile: " + initfile);
	    Properties settings = new Properties();
	    try {
	      FileInputStream sf = new FileInputStream(initfile);
	      settings.load(sf);
	    } catch (Exception ex) {
	        System.out.println("Exception during loading initialization file");
	        System.out.println(ex.toString());
	    }
	    String kbURL = settings.getProperty("SERVERKB", "");
	    String outputdir = settings.getProperty("PCAOUTPUTDIR", "");
	    String guidelineLabel=settings.getProperty("GuidelineLabel", "");
	    System.out.println("output directory: " + outputdir);

	    try {
	        PCAImp = new PCAServer_i();
	        PCAImp.kbManager = new KBHandler(kbURL);
	        PCAImp.kbURLString = kbURL;
	        try {
	          pca = PCAImp.open_pca_session("", "", "", initfile );
	        } catch (Throwable e) {
	          System.out.println("Problem creating PCA session");
	          e.printStackTrace();
	          //System.exit(-1);
	        }
	    } catch(Exception se) {
	        System.out.println("Exception raised during creation of PCAServer_i " +
	                       se.toString());
	        System.exit(1);
	    }
//	*/

	      dssOutput=null;
	      pca.compliance(edu.stanford.smi.eon.PCAServerModule.Compliance_Level.strict);
	      String patientId = pca.setDummyCase();

	      Patient_Data[] data = new Patient_Data[2];
	      data[0] = new Patient_Data();
	      data[0].numeric(new Numeric_Data(Data_Operation_Type.add,
	        "Age",  "year", "2002-10-23", "45"));
			data[1] = new Patient_Data();
			data[1].note_data(new Note_Entry_Data(Data_Operation_Type.add,
					Entry_Type.problem, "Diabetes_Mellitus",  "", "", ""));
	      try {
	        pca.updateData(data);
	        
	      } catch (Exception e) {
	        e.printStackTrace();
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
		    try {
			      System.out.println("Setting guideline to be " + guidelineLabel);
			      pca.setGuideline(  guidelineLabel);
			    } catch (Throwable e) {
			      System.out.println("Problem creating PCA session");
			      e.printStackTrace();
			      System.exit(-1);
			    }

	        dssOutput = pca.updateAdvisories();
	        if (dssOutput != null) {
	          for (int j=0; j< dssOutput.length; j++) {
	            ClientUtil.showResultWithKB(dssOutput[j], itsWriter, PCAImp.kbManager.getKB());

	          }
	        }
		    try {
			      System.out.println("Setting guideline to be " + guidelineLabel);
			      pca.setGuideline(  "NCEPATPIII");
			    } catch (Throwable e) {
			      System.out.println("Problem creating PCA session");
			      e.printStackTrace();
			      System.exit(-1);
			    }
		        dssOutput = pca.updateAdvisories();
		        if (dssOutput != null) {
		          for (int j=0; j< dssOutput.length; j++) {
		            ClientUtil.showResultWithKB(dssOutput[j], itsWriter,  PCAImp.kbManager.getKB());

		          }
		        }

	      } catch (Exception e) {
	        e.printStackTrace();
	      } finally {
	          itsWriter.close();
	      }
	      
	      System.out.println("TestPadda.Main Completed case : "+patientId);
	    pca.finishSession();
	 /* end testing code */
	}

}
