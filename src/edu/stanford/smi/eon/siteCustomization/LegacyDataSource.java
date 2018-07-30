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
package edu.stanford.smi.eon.siteCustomization;
import edu.stanford.smi.eon.PCAServerModule.*;
//import edu.stanford.smi.eon.ChronusServer.*;
import edu.stanford.smi.eon.datahandler.DataHandler;
import edu.stanford.smi.eon.util.*;
import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;
import org.apache.log4j.*;

public class LegacyDataSource {
//  private ChronusSession chronus2 = null;
  private String dbname = null;
  private boolean fromFile=false;
  private boolean m_isHandlingOpioidTherapy = false;
  private DataHandler glDataHandler = null;
  private SimpleDateFormat defaultDateFormat =
      new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss.SSS");


  public LegacyDataSource(String dbname, String user,
                     String password, String initFile, DataHandler glDataHandler) throws PCA_Initialization_Exception {
    this.glDataHandler =  glDataHandler;

  }
  static Logger logger = Logger.getLogger(LegacyDataSource.class);

/*  public ChronusSession getChronusSession() {
	  return chronus2;
  }
*/
  /*
   * Determine the flag:m_isHandlingOpioidTherapy
   * As of 2006/09/19, the algorithm consists of checking the property
   * SERVERKB for the string "Opioid"
   */
  private void checkIfHandlingOpioidTherapy(String initFile) {
    logger.info(" checkIfHandlingOpioidTherapy " + initFile);
	m_isHandlingOpioidTherapy = false;
	if (initFile == null || initFile.length() <= 0) {
      return;
    }

	Properties settings = new Properties();
    try {
      FileInputStream fis = new FileInputStream(initFile);
      settings.load(fis);
    }
    catch (Exception ex) {
      logger.error( "checkIfHandlingOpioidTherapy error", ex);
      ex.printStackTrace();
      return;
    }

	String kbPath = settings.getProperty("SERVERKB", "");
	logger.info("checkIfHandlingOpioidTherapy() - kbPath: " + kbPath);
	if (kbPath == null || kbPath.length() <= 0) {
      return;
    }
    kbPath = kbPath.toUpperCase();

    int searchVal = kbPath.indexOf("OPIOID");
    if (searchVal > 0)
      m_isHandlingOpioidTherapy = true;

    logger.debug(" checkIfHandlingOpioidTherapy: m_isHandlingOpioidTherapy " + m_isHandlingOpioidTherapy );
	System.out.println("checkIfHandlingOpioidTherapy() - m_isHandlingOpioidTherapy: " + m_isHandlingOpioidTherapy);
    return;
  }


  public int PresenceQuery(String query) {
    int count = 0;
/*    try {
      edu.stanford.smi.eon.ChronusII.TemporalResult result = chronus2.processCommand(query);
      if (result != null) {
        while (result.next()) {
          count = count+1;
        }
        result.close();
      }
      //logger.debug("problems: "+currentProblems.toString());
    } catch (Throwable e) {
      logger.error("Error with query "+query, e);

    }
*/    return count;
  }


    public void loadData(String patient_id, String sessionTime) {
    	
    	
    }
   /*
    try {
      chronus2.checkDatabaseConnection();
    } catch (Exception e) {
      logger.error("No valid Chronus connection!!!"+e.getMessage());
      return;
    }
    java.util.Date startLoadTime = new java.util.Date();
    java.util.Date completeLoadTime = null;
    String problemsQuery ="";
    if (m_isHandlingOpioidTherapy) {
      problemsQuery = "temporal select kbname from Conditions, KBMap " +
      "where SSN = '" + patient_id + "' and ICD9_1=code";
    } else {
      problemsQuery = "temporal select ICD9_1, last_date from Conditions " +
          "where SSN = '" + patient_id + "'";
    }
    String athenaConditionQuery = "select condition, start_time from ATHENA_Conditions where stop_time is NULL and SSN = '" + patient_id + "'";
    String medicationsQuery = "select ingredient, daily_dose, sig, rxno, fill_type from Prescriptions where SSN = '" + patient_id + "' order by ingredient, rxno";
    String studiesQuery = "temporal select  target_class, value, unit from Studies, LabMapping where source_name = lab and SSN = '"
      + patient_id +"'" ;
//    String studiesQuery = "temporal select  lab, value, unit from Studies, LabMapping where SSN = '"
//      + patient_id +"'" ;
    String vitalsQuery = "temporal select  target_class, value from PatientHistoryMapping, Vitals where measurement = source_string and SSN = '"
      + patient_id + "'";
    String DOBSexQuery = "select DOB,  target_class from Demographics, PatientHistoryMapping where "+
      "sex = source_string and SSN = '" + patient_id + "'";
    String raceQuery = "select target_class from Demographics, PatientHistoryMapping "+
      " where race = source_string and SSN = '" + patient_id + "'";
    String findingsQuery = "temporal select name, value from Findings where SSN = '" + patient_id + "'";
    String adverseEventsQuery = "temporal select name , reaction from Adverse_Events, Drugs where drugid=ID  and SSN= '" + patient_id + "'";
    String adverseEventsQuery2 = "temporal select ProtegeClass , reaction from Adverse_Events, VA2ProtegeDrugClassMapping where Adverse_Events.VAClass=VA2ProtegeDrugClassMapping.VAClass and "+
       "substance = '' and SSN= '" + patient_id + "'";
    String proceduresQuery = "temporal select name from Procedures where SSN = '" + patient_id + "'";
    String encountersQuery  = "temporal select provider_ID from Encounters where SSN = '" + patient_id + "'";
    String adverseEventsQuery = "temporal select drug, symptom_ID from Adverse_Events where SSN = '" + patient_id + "'";

    //logger.debug("Loading data", 0);
    try {
	  System.out.println(" DataSource: before executing problems Query");
      edu.stanford.smi.eon.ChronusII.TemporalResult problems = chronus2.processCommand(problemsQuery);

      Collection validPeriods;
      String problem = null;
      String lastDate = null;

      if (problems != null && m_isHandlingOpioidTherapy) {
        while (problems.next()) {
          problem = problems.getString("kbname").trim();
          if (glDataHandler.getKBHandler().getCls(problem) != null) {
		    lastDate = "";
            glDataHandler.cacheNoteEntry(problem, lastDate, problems.getValidTime());
          }
        }
        problems.close();
      }

      if (problems != null && !m_isHandlingOpioidTherapy) {
        while (problems.next()) {
          problem = problems.getString("ICD9_1").trim();
          if (glDataHandler.getKBHandler().getCls(problem) != null) {
            lastDate =  problems.getString("last_date");
            if (lastDate != null )
              lastDate.trim().substring(0,11) ;
            else
              lastDate="";
            glDataHandler.cacheNoteEntry(problem, lastDate, problems.getValidTime());
          }
        }
        problems.close();
      }

    } catch (Throwable e) {
      logger.error("Error with problemsQuery", e);
    }
*//*    try {
      //logger.info("DataSource.loadData: "+athenaConditionQuery);
      edu.stanford.smi.eon.ChronusII.TemporalResult conditions = chronus2.processCommand(athenaConditionQuery);

      String condition, start_time;
      if (conditions != null) {
        while (conditions.next()) {
          condition = conditions.getString("condition").trim();
          start_time =  conditions.getString("start_time");
          if (start_time == null) start_time = "";
          //logger.info("DataSource.loadData: "+condition+" "+start_time);
          glDataHandler.cacheNoteEntry(condition, start_time);
        }
        conditions.close();
      }
      //logger.debug("problems: "+currentProblems.toString());
    } catch (Throwable e) {
      logger.error("Error with problemsQuery", e);

    }
/*    try {
      chronus2.closeCommand();
    } catch (Exception e) {
      logger.error("Error closing Command: " +e.getMessage(), e);

    } */
    /* Sometimes we may have prescriptions that have the same rxno on the same
       day. They are distinguished by their fill type N(ew), R(ifill), P(artial).
       We want to use the records in the order of N, R, P.

       Sort the query by rxno, and
    */
/*    try {
      String med, rxno, fillType, assessedStatus;
      float dailyDose;
      String savedMed = null, savedRxno = null, savedFillType = null, savedAssessedStatus = null;
      float savedDailyDose = 0;
      edu.stanford.smi.eon.ChronusII.TemporalResult medicationsResult = chronus2.processCommand(medicationsQuery);
      boolean firstRecord = true;
      while (medicationsResult.next()) {
        try {
          fillType = medicationsResult.getString("fill_type").trim();
        } catch (Exception e) {
          logger.error("DataSource medicationsResult.getString('fill_type') "+
            " failed");
          fillType = "";
        }
        try {
          rxno = medicationsResult.getString("rxno").trim();
        } catch (Exception e) {
          logger.error("DataSource medicationsResult.getString('rxno') "+
            " failed");
          rxno = "";
        }
        med = medicationsResult.getString("ingredient").trim();
        try {
          dailyDose = Float.parseFloat(medicationsResult.getString("daily_dose"));
        } catch (Exception e) {
          logger.error("DataSource medicationsResult.getString('daily_dose') "+
            medicationsResult.getString("daily_dose")+" is not a float");
          dailyDose = 0;
        }
         try {
          assessedStatus = medicationsResult.getString("sig").trim();
        } catch (Exception e) {
          logger.error("DataSource medicationsResult.getString('sig') "+
            " failed");
          assessedStatus = "";
        }
        if (firstRecord) {
          savedMed = med; savedRxno=rxno; savedFillType = fillType; savedAssessedStatus = assessedStatus;
          savedDailyDose = dailyDose;
          firstRecord = false;
        } else {
        	if (!med.equals(savedMed)) {
	            glDataHandler.cachePrescription(savedMed, savedDailyDose, null, true, savedAssessedStatus);
                savedMed = med; savedRxno=rxno; savedFillType = fillType; savedAssessedStatus = assessedStatus;
                savedDailyDose = dailyDose;
        	} else
	          if (!rxno.equals(savedRxno)) {
	            glDataHandler.cachePrescription(savedMed, savedDailyDose, null, true, savedAssessedStatus);
	            savedMed = med; savedRxno=rxno; savedFillType = fillType; savedAssessedStatus = assessedStatus;
	            savedDailyDose = dailyDose;
	          } else { //same ingredient and same rxno
	            if ((savedFillType.equals("N")) || (savedFillType.equals("R") && fillType.equals("P"))
	                || fillType.equals("") ) {
	              continue;
	            } else {
	              savedMed = med; savedRxno=rxno; savedFillType = fillType; savedAssessedStatus = assessedStatus;
	              savedDailyDose = dailyDose;
	            }
	          }
	        }
      } //while
      glDataHandler.cachePrescription(savedMed, savedDailyDose, null, true, savedAssessedStatus);
      medicationsResult.close();
    } catch (Throwable e) {
      logger.error("Error with medicationsQuery", e);

    }
*//*    try {
      chronus2.closeCommand();
    } catch (Exception e) {
      logger.error("Error closing Command: " +e.getMessage(), e);

    }   */

/*    try {
      edu.stanford.smi.eon.ChronusII.TemporalResult studiesResult = chronus2.processCommand(studiesQuery);
      String lab;
      String stringValue;
      float value;
      Collection validTime;
      if (studiesResult != null) {
        logger.debug("Number of studies: "+ studiesResult.getNumberOfRows());
        while (studiesResult.next()) {
          lab = studiesResult.getString("target_class");
          stringValue = studiesResult.getString("value");
          try {
            value = Float.parseFloat(stringValue);
            validTime = studiesResult.getValidTime();
            //for (Iterator i=validTime.iterator(); i.hasNext();) {
            //  String timestamp = ((Instant)i.next()).datetime;
            //  glDataHandler.updateNumericData(Data_Operation_Type.add, lab, "", timestamp, stringValue);
            //}
            glDataHandler.cacheNumericEvent(lab, value, validTime);
          } catch ( NumberFormatException e){
            validTime = studiesResult.getValidTime();
            glDataHandler.cacheQualitativeData(lab, stringValue.trim(), validTime);
            logger.debug("DataSource study " +stringValue+" is not a float");
          }
        }
      }
      studiesResult.close();
    } catch (Exception e) {
      logger.error("Error with studiesQuery"+e.getMessage(), e);

    }
*//*    try {
      chronus2.closeCommand();
    } catch (Exception e) {
      logger.error("Error closing Command: " +e.getMessage(), e);

    }  */
/*    try {
      edu.stanford.smi.eon.ChronusII.TemporalResult adverseEventsResult = chronus2.processCommand(adverseEventsQuery);
      String drugName;
      String symptom;
      Collection validTime;
      if (adverseEventsResult != null) {

        while (adverseEventsResult.next()) {

          drugName = adverseEventsResult.getString("Name");
          symptom = adverseEventsResult.getString("reaction");
          logger.debug("AdverseEvent drugName:"+
            drugName+" symptom: "+symptom);
          try {
            validTime = adverseEventsResult.getValidTime();

            glDataHandler.cacheAdverseEvent(drugName, symptom, validTime);
          } catch ( Exception e){
            logger.error(e.getMessage(), e);

          }
        }
      }
      adverseEventsResult.close();
    } catch (Exception e) {
      logger.error("Error with adverseEventsQuery"+e.getMessage(), e);

    }
*//*    try {
      chronus2.closeCommand();
    } catch (Exception e) {
      logger.error("Error closing Command: " +e.getMessage(), e);

    }    */
/*    try {
      logger.debug(adverseEventsQuery2);
      edu.stanford.smi.eon.ChronusII.TemporalResult adverseEventsResult = chronus2.processCommand(adverseEventsQuery2);
      String drugName;
      String symptom;
      Collection validTime;
      if (adverseEventsResult != null) {

        while (adverseEventsResult.next()) {

          drugName = adverseEventsResult.getString("ProtegeClass");
          symptom = adverseEventsResult.getString("reaction");
          logger.debug("AdverseEvent2 drugName:"+
            drugName+" symptom: "+symptom);
          try {
            validTime = adverseEventsResult.getValidTime();

            glDataHandler.cacheAdverseEvent(drugName, symptom, validTime);
          } catch ( Exception e){
            logger.error(e.getMessage(), e);

          }
        }
      }
      adverseEventsResult.close();
    } catch (Exception e) {
      logger.error("Error with adverseEventsQuery"+e.getMessage(), e);

    }
*//*    try {
      chronus2.closeCommand();
    } catch (Exception e) {
      logger.error("Error closing Command: " +e.getMessage(), e);

    }*/
/*    try {
      edu.stanford.smi.eon.ChronusII.TemporalResult vitalsResult = chronus2.processCommand(vitalsQuery);
      String measurement;
      float value;
      List validTime;
      if (vitalsResult != null) {
        while (vitalsResult.next()) {
          measurement = vitalsResult.getString("target_class");
          try {
            value = Float.parseFloat(vitalsResult.getString("value"));
          } catch (Exception e) {
            logger.error("DataSource Vitals vitalsResult.getString('value') "+
              vitalsResult.getString("value")+" is not a float" );
            value = 0;
          }
          validTime = vitalsResult.getValidTime();
          glDataHandler.cacheNumericEvent(measurement, value, validTime);
        }
      }
      vitalsResult.close();
    } catch (Exception e) {
      logger.error("Error with vitalsQuery" + e.getMessage(), e);

    }
    try {
      chronus2.closeCommand();
    } catch (Exception e) {
      logger.error("Error closing Command: " +e.getMessage(), e);

    }   
    try {
      edu.stanford.smi.eon.ChronusII.TemporalResult demographicsResult = chronus2.processCommand(DOBSexQuery);
      Collection currentTime = new ArrayList();
      currentTime.add(new edu.stanford.smi.eon.ChronusII.Instant(sessionTime));
      if (demographicsResult != null) {
        while (demographicsResult.next()) {
          int age;
          Calendar rightNow = Calendar.getInstance();
          String s = demographicsResult.getString("DOB");
          String sex = demographicsResult.getString("target_class");
          glDataHandler.cacheDemographics("Sex", sex);
          logger.debug("sex: "+sex);
          if (s != null) {
            java.util.Date dateOfBirth = defaultDateFormat.parse(s);
            logger.debug("dateOfBirth: "+dateOfBirth.toString());
            // Day expects January to be "1"
            Day currentDay = new Day(rightNow.get(Calendar.YEAR),
              rightNow.get(Calendar.MONTH)+1, rightNow.get(Calendar.DATE));
            age = (currentDay.toJulian() - HelperFunctions.Day2Int3(dateOfBirth))/365;

            glDataHandler.cacheNumericEvent("Age", age, currentTime);
          }

        }
        demographicsResult.close();
      }
    } catch (Throwable e) {
      logger.error("Error with DOBSexQuery", e);

    }

    try {
      edu.stanford.smi.eon.ChronusII.TemporalResult demographicsResult = chronus2.processCommand(raceQuery);
      Collection currentTime = new ArrayList();
      currentTime.add(new edu.stanford.smi.eon.ChronusII.Instant(sessionTime));
      if (demographicsResult != null) {
        while (demographicsResult.next()) {
          String race = demographicsResult.getString("target_class");
          glDataHandler.cacheDemographics("Race", race);
          logger.debug("race: "+race);
        }
        demographicsResult.close();
      }
    } catch (Throwable e) {
      logger.error("Error with raceQuery", e);

    }

    if (m_isHandlingOpioidTherapy) {
      loadAdditionalOpioidTherapyData(patient_id, sessionTime);
    }

    completeLoadTime = new java.util.Date();
    logger.debug("Loading completed, took"
      + (completeLoadTime.getTime()-startLoadTime.getTime())+" milliseconds");
*/  }

//  public String getCaseSlot() {
//   return  "patient_id";
//  }

 /* public void loadAdditionalOpioidTherapyData(String patient_id, String sessionTime) {
	System.out.println("loadAdditionalOpioidTherapyData() ENTERED!!!!!!!");
	if (patient_id == null || patient_id.length() <= 0)
	   return;

    String numericValueType = "N";
    String characterValueType = "C";


	StringBuffer findingsQuery=new StringBuffer("select f.domain_term, f.valueType, f.valueChar, f.valueInt, fv.timestamp ");
	findingsQuery.append("from Findings f, Findings_V fv ");
	findingsQuery.append(" where f.SSN ='").append(patient_id).append("'");
	findingsQuery.append(" and fv.ValidID = f.ValidID ");

	logger.debug(" loadAdditionalOTData: findingsQuery " + findingsQuery.toString());
	System.out.println(" loadAdditionalOTData: findingsQuery " + findingsQuery.toString());
    try {
      edu.stanford.smi.eon.ChronusII.TemporalResult findings = chronus2.processCommand(findingsQuery.toString());

      String domainTerm, valueType, valueChar;
      float valueNumeric;
      Collection validTimes;
      String strTimeStamp;
      String tempStrForNumber;
      if (findings != null) {
        while (findings.next()) {

          domainTerm = findings.getString("domain_term");
          if (domainTerm == null || domainTerm.length() <= 0)
             continue;
          domainTerm.trim();

          strTimeStamp = findings.getString("timestamp");

          valueType = findings.getString("valueType");
          if (valueType == null || valueType.length() <= 0)
            continue;
          valueType.trim().toUpperCase();
          if (valueType.equals(numericValueType)) {
            valueChar = null;
            tempStrForNumber = findings.getString("valueInt");
            valueNumeric = Float.parseFloat(tempStrForNumber);
            //System.out.println(" loadAdditionalOT: domain_term " + domainTerm +
            //                   " valueNumeric " + valueNumeric + " time " + strTimeStamp);
            glDataHandler.cacheNumericEvent(domainTerm, valueNumeric, strTimeStamp);
            continue;
	      }

          // Here if we have a character string value type
          valueNumeric = (float) 0.0;
          valueChar = findings.getString("valueChar");
          //System.out.println(" loadAdditionalOT: domain_term " + domainTerm +
          //                   " valueChar " + valueChar + " time " + strTimeStamp);
          glDataHandler.cacheQualitativeData(domainTerm, valueChar, strTimeStamp);
        }
        findings.close();
      }

    } catch (Throwable e) {
      e.printStackTrace();
      logger.error("Error in loadAdditionalOpioidTherapyData ", e);
    }

  }
*/
  /*
  public void loadAdditionalOpioidTherapyData(String patient_id, String sessionTime) {

    String numericValueType = "N";
    String characterValueType = "C";

	String findingsQuery="temporal select domain_term, valueType, valueChar, valueInt from Findings where SSN = '" + patient_id + "'";;
    System.out.println(" loadAdditionalOTData: findingsQuery " + findingsQuery);
    try {
      edu.stanford.smi.eon.ChronusII.TemporalResult findings = chronus2.processCommand(findingsQuery);

      String domainTerm, valueType, valueChar;
      float valueNumeric;
      Collection validTimes;
      String tempStrForNumber;
      if (findings != null) {
        while (findings.next()) {

          domainTerm = findings.getString("domain_term");
          if (domainTerm == null || domainTerm.length() <= 0)
             continue;
          domainTerm.trim();

          valueType = findings.getString("valueType");
          if (valueType == null || valueType.length() <= 0)
            continue;
          valueType.trim().toUpperCase();
          if (valueType.equals(numericValueType)) {
            valueChar = null;
            tempStrForNumber = findings.getString("valueInt");
            valueNumeric = Float.parseFloat(tempStrForNumber);
            System.out.println(" loadAdditionalOT: domain_term " + domainTerm +
                               " valueNumeric " + valueNumeric);
            glDataHandler.cacheNumericEvent(domainTerm, valueNumeric,
                                              findings.getValidTime());
            continue;
	      }

          // Here if we have a character string value type
          valueNumeric = (float) 0.0;
          valueChar = findings.getString("valueChar");
          System.out.println(" loadAdditionalOT: domain_term " + domainTerm +
                             " valueChar " + valueChar);
          glDataHandler.cacheQualitativeData(domainTerm, valueChar,
                                             findings.getValidTime());

        }
        findings.close();
      }

    } catch (Throwable e) {
      e.printStackTrace();
      logger.error("Error in loadAdditionalOpioidTherapyData ", e);
    }

  }
  */


