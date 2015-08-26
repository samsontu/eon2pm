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
package edu.stanford.smi.eon.PCAServerModule;

import java.util.Collection;
import java.util.Date;
import gov.va.athena.advisory.Advisory;

/**
<p>
<ul>
<li> <b>Java Class</b> edu.stanford.smi.eon.PCAServerModule.PCASession
<li> <b>Source File</b> edu/stanford/smi/eon/PCAServerModule/PCASession.java
<li> <b>IDL Source File</b> PCAServer.idl
<li> <b>IDL Absolute Name</b> ::PCAServerModule::PCASession
<li> <b>Repository Identifier</b> IDL:PCAServerModule/PCASession:1.0
</ul>
<b>IDL definition:</b>
<pre>
    #pragma prefix "PCAServerModule"
    interface PCASession {
      readonly attribute string database;
      readonly attribute string patient_id;
      readonly attribute string session_time;
      readonly attribute string guideline_name;
      attribute ::PCAServerModule::Compliance_Level compliance;
      void setGuideline(
        in string guideline_name
      )
      raises(
        ::PCAServerModule::PCA_Session_Exception,
        ::PCAServerModule::PCA_Initialization_Exception
      );
      void setCase(
        in string patient_id,
        in string session_time
      )
      raises(
        ::PCAServerModule::PCA_Session_Exception,
        ::PCAServerModule::PCA_Initialization_Exception
      );
      string setDummyCase();
      void saveAdvisoriesAs(
        in string patient_id,
        in string storageDirectory
      )
      raises(
        ::PCAServerModule::PCA_Session_Exception
      );
      ::PCAServerModule::Guideline_Service_RecordSeq computeAndStoreAdvisories(
        in string storageDirectory
      )
      raises(
        ::PCAServerModule::PCA_Session_Exception
      );
      void loadPrecomputedAdvisories(
        in string case_id,
        in string session_time,
        in string storageDirectory
      )
      raises(
        ::PCAServerModule::PCA_Session_Exception
      );
      void resetAdvisories();
      boolean containsAdvisories();
      ::PCAServerModule::Guideline_Service_RecordSeq getAdvisories();
      ::PCAServerModule::Guideline_Service_RecordSeq computeAdvisories(
      )
      raises(
        ::PCAServerModule::PCA_Session_Exception
      );
      ::PCAServerModule::Guideline_Service_RecordSeq updateAdvisories(
      )
      raises(
        ::PCAServerModule::PCA_Session_Exception
      );
      void updateData(
        in ::PCAServerModule::Patient_DataSeq data
      )
      raises(
        ::PCAServerModule::Improper_Data_Exception
      );
      string printData();
      void ping();
      void finishSession();
      void abortSession();
    };
</pre>
</p>
*/
public interface PCASession { //extends com.inprise.vbroker.CORBA.Object {
  /**
  <p>
  Reader for attribute: <b>::PCAServerModule::PCASession::database</b>.
  <pre>
    #pragma prefix "PCAServerModule/PCASession"
    readonly attribute string database;
  </pre>
  </p>
  */
  public java.lang.String database();
  /**
  <p>
  Reader for attribute: <b>::PCAServerModule::PCASession::patient_id</b>.
  <pre>
    #pragma prefix "PCAServerModule/PCASession"
    readonly attribute string patient_id;
  </pre>
  </p>
  */
  public java.lang.String patient_id();
  /**
  <p>
  Reader for attribute: <b>::PCAServerModule::PCASession::session_time</b>.
  <pre>
    #pragma prefix "PCAServerModule/PCASession"
    readonly attribute string session_time;
  </pre>
  </p>
  */
  public java.lang.String session_time();
  /**
  <p>
  Reader for attribute: <b>::PCAServerModule::PCASession::guideline_name</b>.
  <pre>
    #pragma prefix "PCAServerModule/PCASession"
    readonly attribute string guideline_name;
  </pre>
  </p>
  */
  public java.lang.String guideline_name();
  /**
  <p>
  Writer for attribute: <b>::PCAServerModule::PCASession::compliance</b>.
  <pre>
    #pragma prefix "PCAServerModule/PCASession"
    attribute ::PCAServerModule::Compliance_Level compliance;
  </pre>
  </p>
  */
  public void compliance(
    edu.stanford.smi.eon.PCAServerModule.Compliance_Level compliance
  );
  /**
  <p>
  Reader for attribute: <b>::PCAServerModule::PCASession::compliance</b>.
  <pre>
    #pragma prefix "PCAServerModule/PCASession"
    attribute ::PCAServerModule::Compliance_Level compliance;
  </pre>
  </p>
  */
  public edu.stanford.smi.eon.PCAServerModule.Compliance_Level compliance();
  /**
  <p>
  Operation: <b>::PCAServerModule::PCASession::setGuideline</b>.
  <pre>
    #pragma prefix "PCAServerModule/PCASession"
    void setGuideline(
      in string guideline_name
    )
    raises(
      ::PCAServerModule::PCA_Session_Exception,
      ::PCAServerModule::PCA_Initialization_Exception
    );
  </pre>
  </p>
  */
  public void setGuideline(
    java.lang.String guideline_name
  ) throws
    edu.stanford.smi.eon.PCAServerModule.PCA_Session_Exception,
    edu.stanford.smi.eon.PCAServerModule.PCA_Initialization_Exception;
  /**
  <p>
  Operation: <b>::PCAServerModule::PCASession::setCase</b>.
  <pre>
    #pragma prefix "PCAServerModule/PCASession"
    void setCase(
      in string patient_id,
      in string session_time
    )
    raises(
      ::PCAServerModule::PCA_Session_Exception,
      ::PCAServerModule::PCA_Initialization_Exception
    );
  </pre>
  </p>
  */
  public void setCase(
    java.lang.String patient_id,
    java.lang.String session_time
  ) throws
    edu.stanford.smi.eon.PCAServerModule.PCA_Session_Exception,
    edu.stanford.smi.eon.PCAServerModule.PCA_Initialization_Exception;
  /**
  <p>
  Operation: <b>::PCAServerModule::PCASession::setDummyCase</b>.
  <pre>
    #pragma prefix "PCAServerModule/PCASession"
    string setDummyCase();
  </pre>
  </p>
  */
  public java.lang.String setDummyCase();
  /**
  <p>
  Operation: <b>::PCAServerModule::PCASession::saveAdvisoriesAs</b>.
  <pre>
    #pragma prefix "PCAServerModule/PCASession"
    void saveAdvisoriesAs(
      in string patient_id,
      in string storageDirectory
    )
    raises(
      ::PCAServerModule::PCA_Session_Exception
    );
  </pre>
  </p>
  */
  public void saveAdvisoriesAs(
    java.lang.String patient_id,
    java.lang.String storageDirectory
  ) throws
    edu.stanford.smi.eon.PCAServerModule.PCA_Session_Exception;
  /**
  <p>
  Operation: <b>::PCAServerModule::PCASession::computeAndStoreAdvisories</b>.
  <pre>
    #pragma prefix "PCAServerModule/PCASession"
    ::PCAServerModule::Guideline_Service_RecordSeq computeAndStoreAdvisories(
      in string storageDirectory
    )
    raises(
      ::PCAServerModule::PCA_Session_Exception
    );
  </pre>
  </p>
  */
  public edu.stanford.smi.eon.PCAServerModule.Guideline_Service_Record[] computeAndStoreAdvisories(
    java.lang.String storageDirectory
  ) throws
    edu.stanford.smi.eon.PCAServerModule.PCA_Session_Exception;
  /**
  <p>
  Operation: <b>::PCAServerModule::PCASession::loadPrecomputedAdvisories</b>.
  <pre>
    #pragma prefix "PCAServerModule/PCASession"
    void loadPrecomputedAdvisories(
      in string case_id,
      in string session_time,
      in string storageDirectory
    )
    raises(
      ::PCAServerModule::PCA_Session_Exception
    );
  </pre>
  </p>
  */
  public void loadPrecomputedAdvisories(
    java.lang.String case_id,
    java.lang.String session_time,
    java.lang.String storageDirectory
  ) throws
    edu.stanford.smi.eon.PCAServerModule.PCA_Session_Exception;
  /**
  <p>
  Operation: <b>::PCAServerModule::PCASession::resetAdvisories</b>.
  <pre>
    #pragma prefix "PCAServerModule/PCASession"
    void resetAdvisories();
  </pre>
  </p>
  */
  public void resetAdvisories();
  /**
  <p>
  Operation: <b>::PCAServerModule::PCASession::containsAdvisories</b>.
  <pre>
    #pragma prefix "PCAServerModule/PCASession"
    boolean containsAdvisories();
  </pre>
  </p>
  */
  public boolean containsAdvisories();
  /**
  <p>
  Operation: <b>::PCAServerModule::PCASession::getAdvisories</b>.
  <pre>
    #pragma prefix "PCAServerModule/PCASession"
    ::PCAServerModule::Guideline_Service_RecordSeq getAdvisories();
  </pre>
  </p>
  */
  public edu.stanford.smi.eon.PCAServerModule.Guideline_Service_Record[] getAdvisories();
  /**
  <p>
  Operation: <b>::PCAServerModule::PCASession::computeAdvisories</b>.
  <pre>
    #pragma prefix "PCAServerModule/PCASession"
    ::PCAServerModule::Guideline_Service_RecordSeq computeAdvisories(
    )
    raises(
      ::PCAServerModule::PCA_Session_Exception
    );
  </pre>
  </p>
  */
  public edu.stanford.smi.eon.PCAServerModule.Guideline_Service_Record[] computeAdvisories(
  ) throws
    edu.stanford.smi.eon.PCAServerModule.PCA_Session_Exception;
  
  public Guideline_Service_Record[] computeAdvisories(String pid, String sessionTime, String guidelineName
			, boolean computeSubguidelines) throws
			PCA_Session_Exception;
/*  public Guideline_Service_Record[] computeAdvisoriesWithSubGuidelines(
		  String pid, String sessionTime, String guidelineName
  ) throws
    edu.stanford.smi.eon.PCAServerModule.PCA_Session_Exception;
*/
  public boolean computeEligibilityAndGoal(
  ) throws
    edu.stanford.smi.eon.PCAServerModule.PCA_Session_Exception;
  /**
  <p>
  Operation: <b>::PCAServerModule::PCASession::updateAdvisories</b>.
  <pre>
    #pragma prefix "PCAServerModule/PCASession"
    ::PCAServerModule::Guideline_Service_RecordSeq updateAdvisories(
    )
    raises(
      ::PCAServerModule::PCA_Session_Exception
    );
  </pre>
  </p>
  */
  public edu.stanford.smi.eon.PCAServerModule.Guideline_Service_Record[] updateAdvisories(
  ) throws
    edu.stanford.smi.eon.PCAServerModule.PCA_Session_Exception;
  /**
  <p>
  Operation: <b>::PCAServerModule::PCASession::updateData</b>.
  <pre>
    #pragma prefix "PCAServerModule/PCASession"
    void updateData(
      in ::PCAServerModule::Patient_DataSeq data
    )
    raises(
      ::PCAServerModule::Improper_Data_Exception
    );
  </pre>
  </p>
  */
  public void updateData(
    edu.stanford.smi.eon.PCAServerModule.Patient_Data[] data
  ) throws
    edu.stanford.smi.eon.PCAServerModule.Improper_Data_Exception;
  /**
  <p>
  Operation: <b>::PCAServerModule::PCASession::printData</b>.
  <pre>
    #pragma prefix "PCAServerModule/PCASession"
    string printData();
  </pre>
  </p>
  */
  public java.lang.String printData();
  /**
  <p>
  Operation: <b>::PCAServerModule::PCASession::ping</b>.
  <pre>
    #pragma prefix "PCAServerModule/PCASession"
    void ping();
  </pre>
  </p>
  */
  
  public String printAdvisory(Advisory advisory, String format) ;
  
  public void ping();
  /**
  <p>
  Operation: <b>::PCAServerModule::PCASession::finishSession</b>.
  <pre>
    #pragma prefix "PCAServerModule/PCASession"
    void finishSession();
  </pre>
  </p>
  */
  public void finishSession();
  /**
  <p>
  Operation: <b>::PCAServerModule::PCASession::abortSession</b>.
  <pre>
    #pragma prefix "PCAServerModule/PCASession"
    void abortSession();
  </pre>
  </p>
  */
  public void abortSession();
  
  public String topLevelComputeAdvisory(String pid, String caseData, String startTime, String guidelineName) throws PCA_Session_Exception;

  public String topLevelComputeAdvisory(String pid, String caseData, String startTime, String guidelineName, String htmlFile) 
		  throws PCA_Session_Exception;

//  public Guideline_Service_Record[] computeAdvisory(String pid, String startTime, String guidelineName) throws PCA_Session_Exception;

  public String getExplanationURLs();
  
  public String setDummyCase(String patient_id);
  
  public edu.stanford.smi.eon.PCAServerModule.Guideline_Service_Record computePerformanceMeasures(String pid, String hospitalizationID, 
		  	String sessionTime, String guidelineName, String PMName, String startTime, String stopTime
		  ) throws
		    edu.stanford.smi.eon.PCAServerModule.PCA_Session_Exception;
  

	public Advisory computePerformanceMeasuresAdvisory(String pid,
			String hospitalizationID, String sessionTime, String guidelineName, String PMName,
			String startTime, String stopTime) throws PCA_Session_Exception ;
	
	public String computePerformanceMeasuresXMLWithInputData(String pid,String patientData,
			String hospitalizationID, String sessionTime, String guidelineName, String PMName,
			String startTime, String stopTime) throws PCA_Session_Exception ;
	
	public Advisory computePerformanceMeasuresAdvisoryWithInputData(String pid,String patientData,
			String hospitalizationID, String sessionTime, String guidelineName, String PMName,
			String startTime, String stopTime) throws PCA_Session_Exception ;
	
	public boolean getCumulativeFlag() ;
	
	public void setCumulativeFlag(boolean cumulativeFlag) ;


}
