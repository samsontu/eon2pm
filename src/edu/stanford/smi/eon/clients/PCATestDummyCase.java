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
import java.util.*;
import java.io.*;

public class PCATestDummyCase {

  public PCATestDummyCase() {
  }

  public static void main(String[] argv) {
	  PCATestDummyCase pCATestDummyCase = new PCATestDummyCase();
    String caseData=null;
    PrintWriter itsWriter = null;
    PCASession pca = null;
    Guideline_Service_Record[] dssOutput=null;
    PCAServer_i PCAImp = null;
    edu.stanford.smi.eon.PCAServerModule.PCAServer PCARef = null;

     if (argv.length < 1) {
        System.out.println("PCATest needs initialization file as argument");
	      return;
    }
    String initfile = argv[0];
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
    String guidelineId = settings.getProperty("GUIDELINEID", null);

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
//*/
    try {
      pca.setGuideline(  guidelineId );
    } catch (Throwable e) {
      System.out.println("Problem creating PCA session");
      e.printStackTrace();
      System.exit(-1);
    }

      dssOutput=null;
      pca.compliance(edu.stanford.smi.eon.PCAServerModule.Compliance_Level.strict);
      String patientId = pca.setDummyCase();

      Patient_Data[] data = new Patient_Data[5];
      data[0] = new Patient_Data();
      data[0].numeric(new Numeric_Data(Data_Operation_Type.add,
        "DB_Systolic_BP",  "mm/mm", "2002-10-23", "140"));
      data[1] = new Patient_Data();
      data[1].numeric(new Numeric_Data(Data_Operation_Type.add,
        "DB_Diastolic_BP",  "mm/mm", "2002-10-23", "95"));
      data[2] = new Patient_Data();
      data[2].note_data(new Note_Entry_Data(Data_Operation_Type.add,
        Entry_Type.problem,  "Hypertension", "", "", ""));
      data[3] = new Patient_Data();
      data[3].prescription(new Prescription_Data(Data_Operation_Type.add,
        "lisinopril", 10, "mg", "2002-10-01", "", ""));
      data[4] = new Patient_Data();
      data[4].numeric(new Numeric_Data(Data_Operation_Type.add,
        "Age",  "year", "2002-10-23", "45"));
      try {
        pca.updateData(data);
        dssOutput = pca.updateAdvisories();
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

        if (dssOutput != null) {
          for (int j=0; j< dssOutput.length; j++) {
            ClientUtil.showResultWithKB(dssOutput[j], itsWriter, PCAImp.kbManager.getKB());

          }
          itsWriter.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
      System.out.println("TestPadda.Main Completed case : "+patientId);
    pca.finishSession();
 /* end testing code */

  }

}