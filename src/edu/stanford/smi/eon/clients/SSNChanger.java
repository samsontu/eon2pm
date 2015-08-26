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

public class SSNChanger {

  public SSNChanger() {
  }

  public static void main(String[] argv) {
    SSNChanger sSNChanger = new SSNChanger();
    String caseData=null;
    PCASession pca = null;
    PCAServer_i PCAImp = null;
    edu.stanford.smi.eon.PCAServerModule.PCAServer PCARef = null;

     if (argv.length < 3) {
        System.out.println("PCATest needs initialization, source case id and target id as arguments");
	      return;
    }
    String initfile = argv[0];
    String sourceID = argv[1];
    String targetID = argv[2];
    System.out.println("SSSChanger:initfile: " + initfile + " source SSN: "+sourceID+
      " target SSN: "+targetID);
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
      java.util.Date startKB = new java.util.Date();
      PCAImp.kbManager = new KBHandler(kbURL);
      java.util.Date finishedKB = new java.util.Date();
      PCAImp.kbURLString = kbURL;
      try {
        pca = PCAImp.open_pca_session("", "", "", initfile );
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
//*/
    try {
      pca.setGuideline(  guidelineId );
    } catch (Throwable e) {
      System.out.println("Problem creating PCA session");
      e.printStackTrace();
      System.exit(-1);
    }

    pca.resetAdvisories();
    pca.compliance(Compliance_Level.strict);
    try {
        pca.loadPrecomputedAdvisories(sourceID, "2001-02-23", outputdir);
    } catch (Exception e) {
        // no precomputed advisories
        System.out.println("No precomputed data: " + e.getMessage());
        System.exit(1);
    }
    try {
      pca.saveAdvisoriesAs(targetID, outputdir);
    } catch (Exception e) {

        System.out.println("Can't save advisories as : " +targetID +" "+ e.getMessage());
        System.exit(1);
    }
    pca.finishSession();
 /* end testing code */
  }
}
