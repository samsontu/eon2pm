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
/**
 * The DataHandler class should have all of the methods for answering
 * patient-specific queries
 *
 */
package edu.stanford.smi.eon.datahandler;

import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.eon.PCAServerModule.*;
import edu.stanford.smi.eon.util.*;
import edu.stanford.smi.eon.Dharma.*;
import edu.stanford.smi.eon.siteCustomization.LegacyDataSource;
import edu.stanford.smi.eon.time.*;
import edu.stanford.smi.eon.kbhandler.*;
import edu.stanford.smi.eon.data.AdverseReaction;
import edu.stanford.smi.eon.data.DataEntry;
import edu.stanford.smi.eon.guidelineinterpreter.*;
import gov.va.test.opioidtesttool.GlobalVars;

import java.text.ParseException;
import java.util.*;
import java.io.*;

import org.apache.log4j.*;
public class DataHandler {
	private String patient_id;
	private String sessionTime;

	private KBHandler kbmanager;

	private LegacyDataSource dataSource = null;

	private TerminologyService termService = null;

	// cached information
	private Map currentProblems;

	private Map adverseEvents;

	private Map currentMeds;

	private Map numericEntries = null;

	private Map demographics;

	private Map currentMedObjects;
	private Map adverseEventObjects;
	private Map qualitativeDataObjects;

	private Map RuntimeProtegeInstances = null;

	private WhereComparisonFilter caseSelector = null;

	static Logger logger = Logger.getLogger(DataHandler.class);

	public DataHandler() {
	}

	public DataHandler(String dbname, KBHandler kbmanager, String user,
			String password, String initFile) // , boolean fromFile)
					throws PCA_Initialization_Exception {
		// should throw exception for calling method to catch
		// New possibility: getting data through API, not from data source
		this.kbmanager = kbmanager;
		this.termService = new TerminologyService(kbmanager);
		RuntimeProtegeInstances = new HashMap();
		if (dbname != null) {
			try {

				this.dataSource = new LegacyDataSource(dbname, user, password, initFile,

						this);

			} catch (Exception e) {

				logger.warn("No data source");

			}
		} else
			this.dataSource = null;
	}

	public void setKBManager(KBHandler kbmanager) {
		this.kbmanager = kbmanager;
	}

	public String getCaseID() {
		return patient_id;
	}

	public LegacyDataSource getDataSource() {
		return dataSource;
	}

	public String getSessionTime() {
		return sessionTime;
	}
	public WhereComparisonFilter getCaseSelector() {
		if (caseSelector != null)
			return caseSelector;
		else {
			if (dataSource != null) {
				caseSelector = new WhereComparisonFilter(Constants.caseID, "eq", patient_id);
				return caseSelector;
			} else {
				logger
				.error("DataHandler.getCaseSelector: ERROR!!!! Null dataSource");
				return null;
			}
		}
	}

	public void setCaseSelector(String patient_id) {
		this.caseSelector = new WhereComparisonFilter(Constants.caseID,
				"eq", patient_id);
	}

	public WhereFilter caseSpecificWhere(String aggregation_operator,
			AbstractWhereFilter filter) {
		Collection whereClauses = new ArrayList();
		AbstractWhereFilter caseSelector = getCaseSelector();
		if (caseSelector != null) {
			whereClauses.add(caseSelector);
		} else {
			logger
			.error("DataHandler.caseSpecificWhere: ERROR!!!! Null case selector");
		}
		whereClauses.add(filter);
		return new WhereFilter(aggregation_operator, whereClauses);

	}

	public void loadData() throws PCA_Initialization_Exception {
		if (dataSource != null) {
			this.caseSelector = new WhereComparisonFilter(Constants.caseID, "eq", patient_id);
			dataSource.loadData(patient_id, sessionTime);
		} else
			logger.info("No data source. Need to pass data through API!");
	}

	public void changeCase(String case_id, String sessionTime) {
		currentProblems = new HashMap();
		currentMeds = new HashMap();
		numericEntries = new HashMap();
		demographics = new HashMap();
		adverseEvents = new HashMap();
		this.patient_id = case_id;
		this.sessionTime = sessionTime;
		for (Instance med : kbmanager.getKB().getCls("Medication").getInstances()) {
			logger.debug("Before clearRuntimeInstance med: "+med.getOwnSlotValue(kbmanager.getKB().getSlot("drug_name"))+ 
					" dose: "+med.getOwnSlotValue(kbmanager.getKB().getSlot("daily_dose")));
		}
		clearRuntimeInstances();
		for (Instance med : kbmanager.getKB().getCls("Medication").getInstances()) {
			logger.debug("After clearRuntimeInstance med: "+med.getOwnSlotValue(kbmanager.getKB().getSlot("drug_name"))+ 
					" dose: "+med.getOwnSlotValue(kbmanager.getKB().getSlot("daily_dose")));
		}
		System.out.println("****** Frame count in datamanager.changeCase; after clearRuntimeInstances: *****" + GlobalVars.kb.getFrameCount());
		setCaseSelector(case_id);
		cacheQualitativeData(DharmaPaddaConstants.SessionTime, sessionTime, sessionTime);
		/*Note_Entry sessionNote = (Note_Entry)createInstance(DharmaPaddaConstants.Qualitative_Observation);
		if (sessionNote != null) {
			sessionNote.setDomain_term(DharmaPaddaConstants.SessionTime);
			sessionNote.setValue(sessionTime);
		} else {
			logger.error("No Session_Time in KB");
		}
		 */
		currentMedObjects = new HashMap();
		adverseEventObjects = new HashMap();
		qualitativeDataObjects = new HashMap();


		// Collection instances = kbmanager.getKB().getFrames();
		//
		/*
		 * Collection dataClasses = new ArrayList();
		 * dataClasses.add(kbmanager.getCls("Medication"));
		 * dataClasses.add(kbmanager.getCls("Numeric_Entry"));
		 * dataClasses.add(kbmanager.getCls("Note_Entry"));
		 * dataClasses.add(kbmanager.getCls("Adverse_Reaction"));
		 * dataClasses.add(kbmanager.getCls("Absolute_Time_Point")); for
		 * (Iterator classes = dataClasses.iterator(); classes.hasNext();) { Cls
		 * cls = (Cls) classes.next(); for (Iterator inst =
		 * cls.getInstances().iterator(); inst.hasNext();) {
		 * kbmanager.deleteInstance((Instance)inst.next()); } }
		 */
	}

	/***************************************************************************
	 * caching methods
	 */
	/*	public void cacheNumericEvent(String label, float value,
			Collection validTimes) throws DataException {
		label = label.trim();
		Object dataElement = numericEntries.get(label);
		SortedMap dataValues;

		logger.debug("label: " + label + " " + "value: " + value);
		if ((validTimes == null) || (validTimes.isEmpty()) || label.equals(""))
			throw new DataException(validTimes.toString() + " or " + label
					+ " is not valid");

		if (dataElement != null) {
			// has previously stored values
			dataValues = (SortedMap) dataElement;
		} else {
			dataValues = new TreeMap();
			numericEntries.put(label, dataValues);
		}

		for (Iterator i = validTimes.iterator(); i.hasNext();) {
			String timestamp = ((edu.stanford.smi.eon.ChronusII.Instant) i
					.next()).datetime;
			long numericTime = HelperFunctions.DateTime2Int(timestamp);
			dataValues.put(new Long(numericTime), new Float(value));
			makeNumericEvent(label, value, numericTime);
		}

	}
	 */
	public void cacheNumericEntry(String label, float value, String timestamp)
			throws DataException {
		label = label.trim();
		Object dataElement = numericEntries.get(label);
		SortedMap dataValues;

		logger.debug("label: " + label + " " + "value: " + value);
		if ((timestamp == null) || label.equals(""))
			throw new DataException(timestamp + " or " + label
					+ " is not valid");

		if (dataElement != null) {
			// has previously stored values
			dataValues = (SortedMap) dataElement;
		} else {
			dataValues = new TreeMap();
			numericEntries.put(label, dataValues);
		}
		long numericTime = HelperFunctions.DateTime2Int(timestamp);
		dataValues.put(new Long(numericTime), new Float(value));
		makeNumericEvent(label, value, numericTime);
	}

	public void updateNumericData(Data_Operation_Type operation,
			String domainTerm, String unit, String validTime, String value)
					throws Improper_Data_Exception {
		domainTerm = domainTerm.trim();
		//		try {
		//
		//			domainTerm = termService.translateTerm(domainTerm, "db", "kb");
		//
		//		} catch (Exception e) {
		//
		//			logger.info("updateNumericData: " + e.getMessage());
		//
		//			// e.printStackTrace();
		//
		//		}

		switch (operation.value()) {
		case Data_Operation_Type._modify:
		case Data_Operation_Type._add:
			cacheNumericEntry(domainTerm, Float.parseFloat(value), validTime);
			break;
		case Data_Operation_Type._delete:
			Object dataElement = numericEntries.get(domainTerm);
			SortedMap dataValues;
			Object previousValue;
			if (dataElement != null) {
				dataValues = (SortedMap) dataElement;
				previousValue = dataValues.remove(new Long(HelperFunctions
						.DateTime2Int(validTime)));
				if (previousValue == null)
					throw new Improper_Data_Exception("No existing value for "
							+ domainTerm + " at " + validTime);
			} else
				throw new Improper_Data_Exception("No existing value for "
						+ domainTerm);
			break;
		default:
			;
		}

	}

	public void cachePrescription (String med, float dailyDose,
			String startTime, String stopTime, boolean cumulative, String assessedStatus, int MPR, String PRT) {
		putPrescription(med, dailyDose, startTime, stopTime, cumulative, assessedStatus, MPR, PRT);

	}

/*	When the client program sends prescriptions to the execution engine, it passes in 
 * as arguments a drug name, a “dailyDose”, a “startTime”, a “stopTime”, a “cumulative” 
 * boolean flag and an “assessedStatus” (ignoring MPR for the moment). 

	If there is an active duplicate med 
	(i.e., same drug name and (assessedStatus = ‘active’ or stopTime is after session time))
	     Add “DuplicateDrug” note entry with domain_term = drug_name that’s duplicated
	     If the “cumulative” flag is true add the new dose to the existing dose
	     If the the “cumulative” flag is false
	               If either (existing or new) prescription has no start time, ignore it 
	               else use the prescription that’s “more current” (i.e., has later start time)
	
	If the new med is not “active”, a new Protege med instance is created but 
	it’s not added to the index of “active” meds. There is no checking of whether 
	there is already an Protege med instance whose time overlaps with the new one.

	Note that when the policy is to add doses of duplicate meds, the start time 
	of the med is not updated and it defaults to the first one that enters the 
	system. In principle, what the system should have done is to create a historical 
	med with time interval [earlier start time of prescriptions, later start time of 
	prescription] as the start and end bounds and a “active” med whose start time is 
	the later of the start times. 
*/
	private void putPrescription(String med, float dailyDose,
			String startTime, String stopTime, boolean cumulative, String status, int MPR, String PRT) {
		boolean addProtegeInstance = false;
		boolean indexNewPrescription = false;

		Definite_Time_Point PRTime = null;
		if ((PRT != null) && !PRT.equals("")) {
			PRTime = (Definite_Time_Point)createInstance("Definite_Time_Point");
			PRTime.setDateValue(PRT);
		}
		Medication existingMed = (Medication)(currentMeds.get(med));
		// Case: no previous active med
		if (existingMed == null) {
			addProtegeInstance = true;
			if ((status.equals(Constants.active)) || timeAfterSessionTime(stopTime)) {
				status = Constants.active;
				indexNewPrescription = true;
			} 
		} else {
			// Case: previous active med
			if ((status.equals(Constants.active)) || timeAfterSessionTime(stopTime)) {
				if (dailyDose == 0.0) {
					if (MPR != 0.0) existingMed.setMedicationPossessionRatio(MPR);
					if (PRTime != null) existingMed.setPRT(PRTime);	
					return;
				} else {
					// Generate note entry if there is  no previous duplicate_drug note entry
					Note_Entry note = null;
					Collection<WhereComparisonFilter> comparisons = new ArrayList<WhereComparisonFilter>();
					comparisons.add(new WhereComparisonFilter("domain_term", DharmaPaddaConstants.eq, Constants.DuplicateDrug));
					comparisons.add(new WhereComparisonFilter("value", DharmaPaddaConstants.eq, med));
					WhereFilter where = new WhereFilter(DharmaPaddaConstants.AND, comparisons);
					Collection existingDupNoteEntry = kbmanager.findInstances("Note_Entry", where);
					if (existingDupNoteEntry.isEmpty()) {
						note = (Note_Entry) createInstance("Note_Entry");
						note.setSlotsValues(Constants.DuplicateDrug, patient_id, null, med);	
					}
					status = Constants.active;
					if (cumulative) { // Case: cumulative flag set, add doses
						existingMed.setDaily_dose( existingMed.getDaily_dose() + dailyDose);
						if (MPR != 0) existingMed.setMedicationPossessionRatio(MPR);
						if (PRTime != null) existingMed.setPRT(PRTime);
						return;
					} else  {
						//Find the prescription with earlier start time
						if ((startTime == null) || (startTime.equals(""))) {
							logger.warn("Null or empty start time in second duplicate prescription for "+med);
							indexNewPrescription = false;
							addProtegeInstance = false;
						} else {
							Definite_Time_Interval firstValidInterval = ((Definite_Time_Interval)existingMed.getValid_time());
							if (firstValidInterval != null) {
								Definite_Time_Point currentStart = (Definite_Time_Point)firstValidInterval.getstart_timeValue();
								Definite_Time_Point newStart = (Definite_Time_Point)createInstance("Definite_Time_Point");
								newStart.setDateValue(startTime);
								if (newStart.before(currentStart))
									addProtegeInstance = false;
								else
									addProtegeInstance = true;
							} else { //use new prescription
								addProtegeInstance = true;
								indexNewPrescription = true;
							}
						}
					}
				}
			} else { // Case: the prescription being added is historical prescription
				addProtegeInstance = true;
				indexNewPrescription = false; 
				status = Constants.inactive;
			}
		}
		if (addProtegeInstance) {
			Definite_Time_Interval interval = (Definite_Time_Interval)createInstance("Definite_Time_Interval");		
			try {
				if (startTime == null) logger.warn("The medication "+ med +" has no start time");
				interval.setSlotsValues("["+startTime+","+stopTime+"]", startTime, stopTime, this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if ((status.equals(Constants.active)) || timeAfterSessionTime(stopTime)) {
				if (existingMed != null)
					existingMed.setSlotsValues(dailyDose, "", med, 0, "", null,
							patient_id, "", "", status, interval, MPR, PRTime);
				else {
					Medication medInstance = (Medication) createInstance("Medication");
					medInstance.setSlotsValues(dailyDose, "", med, 0, "", null,
							patient_id, "", "", status, interval, MPR, PRTime);
					if (indexNewPrescription) 
						currentMeds.put(med, medInstance);
				}
			} else {
				Medication medInstance = (Medication) createInstance("Medication");
				medInstance.setSlotsValues(dailyDose, "", med, 0, "", null,
						patient_id, "", "", status, interval, MPR, PRTime);				
			}
		}
	}




	private boolean timeAfterSessionTime(String time) {
		try {
			return ((time == null) || (time.equals("")) || ((int) HelperFunctions.Day2Int2(sessionTime)) < ((int) HelperFunctions.Day2Int2(time)));
		} catch (ParseException e) {
			logger.error("Invalid time argument in cache prescription: "+ time);
			return false;
		}
	}


	public void cachePrescription(String med, float dailyDose,
			String startTime, String stopTime, boolean cumulative, String assessedStatus) {
		putPrescription(med, dailyDose, startTime, stopTime, cumulative, assessedStatus, (int)0, null);
	}


	public void updatePrescription(Data_Operation_Type operation,
			String drugName, float dailyDose, String dailyDoseUnit, 
			String startTime, String stopTime, boolean cumulative, String sig, int MPR, String PRT) {
		switch (operation.value()) {
		case Data_Operation_Type._modify:
			cachePrescription(drugName, dailyDose, startTime, stopTime, cumulative, sig, MPR, PRT);
		case Data_Operation_Type._add:
			cachePrescription(drugName, dailyDose, startTime, stopTime, cumulative, sig, MPR, PRT);
			break;
		case Data_Operation_Type._delete:
			Object hasMed = deleteMed(drugName);
			if (hasMed == null)
				logger
				.warn("UpdatePrescripton: Patient not taking "
						+ drugName);
			break;
		default:
			;
		}
	}

	//	public void updatePrescription(Data_Operation_Type operation,
	//			String drugName, float dailyDose, String dailyDoseUnit,
	//			String startTime, String sig) {
	//		switch (operation.value()) {
	//		case Data_Operation_Type._modify:
	//		case Data_Operation_Type._add:
	//			cachePrescription(drugName, dailyDose, startTime, null, false, sig);
	//			break;
	//		case Data_Operation_Type._delete:
	//			Object hasMed = deleteMed(drugName);
	//			if (hasMed == null)
	//				logger
	//				.warn("UpdatePrescripton: Patient not taking "
	//						+ drugName);
	//			break;
	//		default:
	//			;
	//		}
	//	}

	public void updatePrescription(Data_Operation_Type operation,
			String drugName, float dailyDose, String dailyDoseUnit,
			String startTime, String stopTime, String sig) {
		switch (operation.value()) {
		case Data_Operation_Type._modify:
		case Data_Operation_Type._add:
			cachePrescription(drugName, dailyDose, startTime, stopTime, false, sig);
			break;
		case Data_Operation_Type._delete:
			Object hasMed = deleteMed(drugName);
			if (hasMed == null)
				logger
				.warn("UpdatePrescripton: Patient not taking "
						+ drugName);
			break;
		default:
			;
		}
	}
	// When qualitative data are stored both as currentProblem and noteEntry
	// instances, upon de-serializing
	// from precomputed advisory, two instances of NoteEntries are generated:
	// one
	// from the problems and
	// the other from qualitative entry.
	/*	public void cacheQualitativeData(String domainTerm, String value,
			Collection validPeriods) {
		Note_Entry noteEntry = null;
		Definite_Time_Point timePoint = null;
		String timestamp = "";
		if (validPeriods != null) {
			for (Iterator i = validPeriods.iterator(); i.hasNext();) {
				try {
					timestamp = ((edu.stanford.smi.eon.ChronusII.Instant) i
							.next()).datetime;
					timePoint = (Definite_Time_Point) createInstance("Definite_Time_Point");
					timePoint.setSlotsValues(timestamp);
				} catch (Exception e) {
					logger
					.error("cacheNoteEntry: exception getting note entry time");
				}
				noteEntry = (Note_Entry) createInstance("Note_Entry");
				noteEntry.setSlotsValues(domainTerm, patient_id, timePoint,
						value);
				currentProblems.put(domainTerm, timestamp);
			}
		} else {
			currentProblems.put(domainTerm, null);
			noteEntry = (Note_Entry) createInstance("Note_Entry");
			noteEntry.setSlotsValues(domainTerm, patient_id, timePoint, value);
		}
	}
	 */
	public void cacheQualitativeData(String domainTerm, String value,
			String date) {
		currentProblems.put(domainTerm.trim(), date);
		//		logger.debug("domainterm: "+domainTerm+" value: "+value);
		Definite_Time_Point timePoint = null;
		if ((date != null) && !date.equals("")) {
			timePoint = (Definite_Time_Point) createInstance("Definite_Time_Point");
			timePoint.setDateValue(date);
		}
		if (value == null)
			value = "";
		Note_Entry noteEntry = (Note_Entry) createInstance("Note_Entry");
		noteEntry.setSlotsValues(domainTerm.trim(), patient_id, timePoint, value.trim());
	}

	public void cacheNoteEntry(String problem, String date) {
		Definite_Time_Point timePoint = null;
		// date has the form: yyyy-mm-dd
		if ((date != null) && !(date.equals(""))) {
			timePoint = (Definite_Time_Point) createInstance("Definite_Time_Point");
			timePoint.setDateValue(date);
		}
		String noWhiteSpaceProblem = problem.trim();
		currentProblems.put(noWhiteSpaceProblem, date);
		Note_Entry noteEntry = (Note_Entry) createInstance("Note_Entry");
		noteEntry.setSlotsValues(noWhiteSpaceProblem, patient_id, timePoint, "");

	}

	/*	public void cacheNoteEntry(String problem, String lastDate,
			Collection validPeriods) {
		Definite_Time_Point timePoint = null;
		Note_Entry noteEntry = (Note_Entry) createInstance("Note_Entry");
		edu.stanford.smi.eon.ChronusII.Period previousTime = null;

		if ((lastDate == null) || (lastDate.equals(""))) {
			if (validPeriods != null) {
				for (Iterator i = validPeriods.iterator(); i.hasNext();) {
					try {
						edu.stanford.smi.eon.ChronusII.Period time = (edu.stanford.smi.eon.ChronusII.Period) i
						.next();
						if ((previousTime == null)
								|| time
								.after(
										previousTime,
										edu.stanford.smi.eon.ChronusII.Granularity.DAYS))
							previousTime = time;
					} catch (Exception e) {
						logger
						.error("cacheNoteEntry: exception getting note entry time");
					}
				}
				try {
					lastDate = previousTime
					.getStart(edu.stanford.smi.eon.ChronusII.Granularity.DAYS);
					// logger.debug("cacheNoteEntry: period start time: "
					// + lastDate);
				} catch (Exception e) {
					logger
					.error("cacheNoteEntry: exception getting period start time");
				}
			}
		}
		if ((lastDate != null) && (!lastDate.equals(""))) {
			// logger.debug("DataHandler.cacheNoteEntry: problem:
			// "+problem+" period start time: '" + lastDate+"'");
			timePoint = (Definite_Time_Point) createInstance("Definite_Time_Point");
			timePoint.setDateValue(lastDate);
			noteEntry.setSlotsValues(problem, patient_id, timePoint, null);
			// logger.debug("DataHandler.cacheNoteEntry: problem:
			// completed "+problem+" period start time: " + lastDate);
		} else
			noteEntry.setSlotsValues(problem, patient_id, null, null);
		currentProblems.put(problem, lastDate);
	}
	 */
	public void cacheDemographics(String name, String value) {
		demographics.put(name, value);

	}

	public void cacheAdverseEvent(String substance, String symptom,
			Collection timestamp) {
		logger.debug("in data handler: cacheAdverseEvent substance="
				+ substance + " symptom=" + symptom);

		Adverse_Reaction reaction = (Adverse_Reaction) createInstance("Adverse_Reaction");
		reaction.setSlotsValues(symptom, patient_id, substance, null);
		adverseEvents.put(substance, reaction);
	}
	public void cacheOneAdverseEvent(String substance, String symptom,
			String timestamp) {
		logger.debug("in data handler: cacheAdverseEvent substance="
				+ substance + " symptom=" + symptom);
		Adverse_Reaction reaction = (Adverse_Reaction) createInstance("Adverse_Reaction");
		reaction.setSlotsValues(symptom, patient_id, substance, null);
		adverseEvents.put(substance, reaction);
	}


	public Object deleteNoteEntry(String problem) {
		WhereFilter myProblem = caseSpecificWhere(DharmaPaddaConstants.AND,
				new WhereComparisonFilter("domain_term",
						DharmaPaddaConstants.subclass_of, problem));
		Collection problems = kbmanager.findInstances("Note_Entry", myProblem, null);
		for (Iterator i = problems.iterator(); i.hasNext();) {
			deleteInstance((Instance) i.next());
		}
		Cls problemConcept = kbmanager.getCls(problem);
		if (problemConcept != null) {
			for (Iterator i = problemConcept.getSubclasses().iterator(); i
					.hasNext();) {
				currentProblems.remove(((Cls) i.next()).getName());
			}
		}
		return currentProblems.remove(problem);

	}

	public Object deleteMed(String med) {
		Object existingMed = currentMeds.get(med);
		if (existingMed != null) {
			currentMeds.remove(med);
			deleteInstance((Instance) existingMed);
		}
		return existingMed;
	}

	public Object deleteAdverseEvent(String adverseEvent) {
		Collection whereClauses = new ArrayList();
		whereClauses.add(getCaseSelector());
		WhereFilter myProblem = this.caseSpecificWhere(
				DharmaPaddaConstants.AND, new WhereComparisonFilter(
						"domain_term", "eq", adverseEvent));
		Collection adverseEventsToRemove = kbmanager.findInstances(
				"Adverse_Reaction", myProblem, null);
		for (Iterator i = adverseEventsToRemove.iterator(); i.hasNext();) {
			deleteInstance((Instance) i.next());
		}
		return adverseEvents.remove(adverseEvent);

	}

	public void updateNoteEntry(Data_Operation_Type operation,
			Entry_Type entryType, String domainTerm, String value, String valid_time)
					throws Improper_Data_Exception {
		int entryTypeValue = 0;
		logger.debug("updateNoteEntry() operation: "+operation.toString()+"; entryType: "+entryType+"; domain term: "+domainTerm+"; value: "+value+"; valid_time: "+valid_time);
		if (entryType == null) {
			logger.warn("updateNoteEntry: No entryType for " + domainTerm
					+ " Assume to be a problem/finding");
			entryTypeValue = Entry_Type._problem;
		} else
			entryTypeValue = entryType.value();
		if (entryTypeValue == Entry_Type._problem) {
			switch (operation.value()) {
			case Data_Operation_Type._modify:
			case Data_Operation_Type._add:
				if ((domainTerm != null) && (!domainTerm.equals(""))) {
					if ((value != null) && (!value.equals(""))) {
						cacheQualitativeData(domainTerm.trim(), value,
								valid_time);
						logger.debug("updateNoteEntry: domain term: "
								+ domainTerm + " value: " + value);
					} else {
						cacheNoteEntry(domainTerm.trim(), valid_time);
						logger.debug("updateNoteEntry: domain term: "
								+ domainTerm + " value: " + value);
					}
				} else
					logger
					.error("updateNoteEntry: No domain term in add operation");
				break;
			case Data_Operation_Type._delete:
				deleteNoteEntry(domainTerm.trim());
				// if (previousValue == null) throw new
				// Improper_Data_Exception("No previous value of"+
				// domainTerm);
				break;
			default:
				;
			}
		} else if (entryTypeValue == Entry_Type._adverse_event) {
			switch (operation.value()) {
			case Data_Operation_Type._modify:
			case Data_Operation_Type._add:
				cacheAdverseEvent(domainTerm.trim(), value, null);
				break;
			case Data_Operation_Type._delete:
				Object previousValue = deleteAdverseEvent(domainTerm.trim());
				if (previousValue == null)
					throw new Improper_Data_Exception("No previous value of"
							+ domainTerm);
				break;
			default:
				;
			}

		} else {
			logger.warn("Change " + domainTerm + " not yet handled");
		}
	}

	public void serializeData(ObjectOutputStream out)
			throws java.io.IOException {
		Set medSet = currentMeds.entrySet();
		Set adverseEventsSet = adverseEvents.entrySet();
		Map currentMedsToSerialize = new HashMap();
		Map adverseEventsToSerialize = new HashMap();
		Map qualitativeDataToSerialize = new HashMap();

		// logger.debug("DataHandler.serializeData 1",
		// 0);
		for (Iterator i = medSet.iterator(); i.hasNext();) {
			Map.Entry medEntry = (Map.Entry) i.next();
			ProtegeInstanceStub stub = ((Medication) medEntry.getValue())
					.mkInstanceStub();
			currentMedsToSerialize.put(medEntry.getKey(), stub);
			// medEntry.setValue
		}
		for (Iterator j = adverseEventsSet.iterator(); j.hasNext();) {
			Map.Entry adEntry = (Map.Entry) j.next();
			logger.debug("DataHandler.serializeData adverse event: "
					+ adEntry.getKey() + " "
					+ ((Adverse_Reaction) adEntry.getValue()).getSubstance());
			adverseEventsToSerialize.put(adEntry.getKey(),
					((Adverse_Reaction) adEntry.getValue()).mkInstanceStub());
		}
		Collection qualitativeData = kbmanager.findInstances("Note_Entry",
				new WhereComparisonFilter("value",
						DharmaPaddaConstants.hasValue, null), null);
		for (Iterator i = qualitativeData.iterator(); i.hasNext();) {
			Note_Entry qualitativeDatum = (Note_Entry) i.next();
			logger.debug("DataHandler.serializeData debug"
					+ qualitativeDatum.toString());
			ProtegeInstanceStub stub = qualitativeDatum.makeInstanceStub();
			qualitativeDataToSerialize.put(qualitativeDatum.getDomain_term(),
					stub);
		}
		out.writeObject(patient_id);
		out.writeObject(sessionTime);
		out.writeObject(currentMedsToSerialize);
		out.writeObject(currentProblems);
		out.writeObject(demographics);
		out.writeObject(numericEntries);
		out.writeObject(adverseEventsToSerialize);
		/*		if (qualitativeData == null) {
			logger.error("qualitativeData is null");
			qualitativeData = new ArrayList();
		}*/
		out.writeObject(qualitativeDataToSerialize);
		/*
		 * for (Iterator i=medSet.iterator(); i.hasNext();) { Map.Entry medEntry =
		 * (Map.Entry)i.next();
		 * medEntry.setValue(Medication.mkInstanceFromStub(kbmanager, this,
		 * medEntry.getValue())); } for (Iterator j=adverseEventsSet.iterator();
		 * j.hasNext();) { Map.Entry adEntry = (Map.Entry)j.next();
		 * adEntry.setValue(Adverse_Reaction.mkInstanceFromStub(kbmanager,this,
		 * adEntry.getValue())); }
		 */
		// logger.debug("DataHandler.serializeData 10",
		// debug);
	}

	public void restoreData(ObjectInputStream in) throws PCA_Session_Exception {
		Map qualitativeData = null;
		Map restoredNumericData = null;
		try {
			patient_id = (String) in.readObject();
			in.readObject(); // read but do not use sessionTime
			currentMeds = (Map) in.readObject();
			currentProblems = (Map) in.readObject();
			demographics = (Map) in.readObject();
			restoredNumericData = (Map) in.readObject();
			// numericEntries = (Map)in.readObject();
			adverseEvents = (Map) in.readObject();
			qualitativeData = (Map) in.readObject();
		} catch (Exception e) {
			logger.error("Can't restore data cache");
			throw new PCA_Session_Exception("Can't restore data cache");
		}

		Set medSet = currentMeds.entrySet();
		Set adverseEventsSet = adverseEvents.entrySet();
		Set problemsEventSet = currentProblems.entrySet();
		Set qualitativeDataSet = qualitativeData.entrySet();

		for (Iterator i = medSet.iterator(); i.hasNext();) {
			Map.Entry medEntry = (Map.Entry) i.next();
			// logger.debug("DataHandler.restoreData
			// 1", 0);
			medEntry.setValue(Medication.mkInstanceFromStub(kbmanager, this,
					medEntry.getValue()));
		}
		// logger.debug("DataHandler.restoreData 2",
		// 0);
		for (Iterator j = adverseEventsSet.iterator(); j.hasNext();) {
			Map.Entry adEntry = (Map.Entry) j.next();
			Object adEntryValue = Adverse_Reaction.mkInstanceFromStub(
					kbmanager, this, adEntry.getValue());
			if (adEntryValue != null)
				adEntry.setValue(adEntryValue);

		}
		for (Iterator i = qualitativeDataSet.iterator(); i.hasNext();) {
			Map.Entry qualEntry = (Map.Entry) i.next();
			logger.debug("DataHandler.restoreData 2 " + qualEntry.toString());
			Note_Entry.mkQualDataInstanceFromStub(kbmanager, this, qualEntry
					.getValue());
		}
		for (Iterator k = problemsEventSet.iterator(); k.hasNext();) {
			Map.Entry problemEntry = (Map.Entry) k.next();
			logger
			.debug("DataHandler.restoreData 1 "
					+ problemEntry.toString());
			Note_Entry.mkInstanceFromStub(kbmanager, this, problemEntry,
					patient_id);
		}

		// Convert Integer-valued keys in numericEntries to Long-values
		boolean usingLongKey = false;
		for (Iterator parameterList = restoredNumericData.keySet()
				.iterator(); parameterList.hasNext();) {
			Object parameter = parameterList.next();
			logger.debug("DataHandler restoreData: key = "
					+ parameter.toString());
			SortedMap parameterValue = (SortedMap) restoredNumericData
					.get(parameter);
			Long timeStampKey;
			java.lang.Object keyObj;
			SortedMap newParameterValue = new TreeMap();
			float value = (float) 0.0;
			for (Iterator i = parameterValue.keySet().iterator(); i.hasNext();) {
				keyObj = i.next();
				if (keyObj instanceof Integer) {
					timeStampKey = new Long(((Integer) keyObj).longValue());
					value = ((Float) parameterValue.get((Integer) keyObj))
							.floatValue();
					newParameterValue.put(timeStampKey, parameterValue
							.get((Integer) keyObj));
				} else {
					usingLongKey = true;
					timeStampKey = (Long) keyObj;
					value = ((Float) parameterValue.get((Long) keyObj))
							.floatValue();

					//					break testKey;
				}
				makeNumericEvent(parameter.toString(), value, timeStampKey
						.longValue());
			}
			if (!usingLongKey)
				numericEntries.put(parameter, newParameterValue);
		}
		if (usingLongKey)
			numericEntries = restoredNumericData;

	}

	public void cacheKBData() throws PCA_Session_Exception {
		// TODO 
		Set medSet = currentMeds.entrySet();
		Set adverseEventsSet = adverseEvents.entrySet();
		currentMedObjects = new HashMap();
		adverseEventObjects = new HashMap();
		qualitativeDataObjects = new HashMap();

		// logger.debug("DataHandler.serializeData 1",
		// 0);
		for (Iterator i = medSet.iterator(); i.hasNext();) {
			Map.Entry medEntry = (Map.Entry) i.next();
			ProtegeInstanceStub stub = ((Medication) medEntry.getValue())
					.mkInstanceStub();
			currentMedObjects.put(medEntry.getKey(), stub);
			// medEntry.setValue
		}
		for (Iterator j = adverseEventsSet.iterator(); j.hasNext();) {
			Map.Entry adEntry = (Map.Entry) j.next();
			logger.debug("DataHandler.serializeData adverse event: "
					+ adEntry.getKey() + " "
					+ ((Adverse_Reaction) adEntry.getValue()).getSubstance());
			adverseEventObjects.put(adEntry.getKey(),
					((Adverse_Reaction) adEntry.getValue()).mkInstanceStub());
		}
		Collection qualitativeData = kbmanager.findInstances("Note_Entry",
				new WhereComparisonFilter("value",
						DharmaPaddaConstants.hasValue, null), null);
		for (Iterator i = qualitativeData.iterator(); i.hasNext();) {
			Note_Entry qualitativeDatum = (Note_Entry) i.next();
			logger.debug("DataHandler.serializeData debug"
					+ qualitativeDatum.toString());
			ProtegeInstanceStub stub = qualitativeDatum.makeInstanceStub();
			qualitativeDataObjects.put(qualitativeDatum.getDomain_term(),
					stub);
		}
	}

	public  void updateKB(KBHandler kbmanager) throws Exception {
		this.kbmanager = kbmanager;
		Set medSet = currentMedObjects.entrySet();
		Set adverseEventsSet = adverseEventObjects.entrySet();
		Set problemsEventSet = currentProblems.entrySet();
		Set qualitativeDataSet = qualitativeDataObjects.entrySet();

		for (Iterator i = medSet.iterator(); i.hasNext();) {
			Map.Entry medEntry = (Map.Entry) i.next();
			// logger.debug("DataHandler.restoreData
			// 1", 0);
			medEntry.setValue(Medication.mkInstanceFromStub(kbmanager, this,
					medEntry.getValue()));
		}
		// logger.debug("DataHandler.restoreData 2",
		// 0);
		for (Iterator j = adverseEventsSet.iterator(); j.hasNext();) {
			Map.Entry adEntry = (Map.Entry) j.next();
			Object adEntryValue = Adverse_Reaction.mkInstanceFromStub(
					kbmanager, this, adEntry.getValue());
			if (adEntryValue != null)
				adEntry.setValue(adEntryValue);

		}
		for (Iterator i = qualitativeDataSet.iterator(); i.hasNext();) {
			Map.Entry qualEntry = (Map.Entry) i.next();
			logger.debug("DataHandler.restoreData 2 " + qualEntry.toString());
			Note_Entry.mkQualDataInstanceFromStub(kbmanager, this, qualEntry
					.getValue());
		}
		for (Iterator k = problemsEventSet.iterator(); k.hasNext();) {
			Map.Entry problemEntry = (Map.Entry) k.next();
			logger
			.debug("DataHandler.restoreData 1 "
					+ problemEntry.toString());
			Note_Entry.mkInstanceFromStub(kbmanager, this, problemEntry,
					patient_id);
		}

	}


	public void fetchDataFromFile(String storageDirectory, String fileExtension)
			throws PCA_Session_Exception {
		File StorageLocation = new File(storageDirectory, patient_id
				+ fileExtension);
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
					StorageLocation));
			restoreData(in);
			in.close();
		} catch (Exception e) {
			throw new PCA_Session_Exception(e.getMessage());
		}
	}

	public void storeDataToFile(String storageDirectory, String fileExtension)
			throws PCA_Session_Exception {
		File StorageLocation = new File(storageDirectory, patient_id
				+ fileExtension);
		try {
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(StorageLocation));
			serializeData(out);
			out.close();
		} catch (Exception e) {
			throw new PCA_Session_Exception(e.getMessage());
		}
	}

	public void renameCase(String caseID) {
		patient_id = caseID;
		Set medSet = currentMeds.entrySet();
		Set adverseEventsSet = adverseEvents.entrySet();
		// logger.debug("DataHandler.serializeData 1",
		// 0);
		for (Iterator i = medSet.iterator(); i.hasNext();) {
			Map.Entry medEntry = (Map.Entry) i.next();
			((Medication) medEntry.getValue()).setPatient_id(caseID);
		}
		for (Iterator j = adverseEventsSet.iterator(); j.hasNext();) {
			Map.Entry adEntry = (Map.Entry) j.next();
			((Adverse_Reaction) adEntry.getValue()).setPatient_id(caseID);

		}
		Collection qualitativeData = kbmanager.findInstances("Note_Entry",
				new WhereComparisonFilter("value",
						DharmaPaddaConstants.hasValue, null), null);
		for (Iterator i = qualitativeData.iterator(); i.hasNext();) {
			Note_Entry qualitativeDatum = (Note_Entry) i.next();
			logger.debug("DataHandler.serializeData 0"
					+ qualitativeDatum.toString());
			qualitativeDatum.setPatient_id(caseID);
		}

	}

	/***************************************************************************
	 * External Access Methods
	 */
	public Collection<String> currentProblems() {
		if (currentProblems != null)
			return currentProblems.keySet();
		else {
			logger.error("Null currentProblems! The datahandler has not been initialized property!");
			currentProblems = new HashMap(); 
			return new ArrayList();
		}
	}

	protected boolean isCurrentProblem(String problem, String date) {
		Collection currentProblems = currentProblems();
		if (currentProblems.contains(problem))
			return true;
		else
			return false;
	}

	public Collection currentProblems(Relative_Time_Interval timeSpan) {
		// return problems within a time span
		Definite_Time_Interval timeInterval = timeSpan.resolveTime(sessionTime,
				this);
		return currentProblems( timeInterval);
	}

	public String problemFirstLastDates(String problem) {
		return (String) currentProblems.get(problem);
	}

	public Collection currentActivities(String type, Cls mood) {
		logger.debug("dataHandler currentActivities: type: " + type + "mood: "
				+ ((mood != null) ? mood.getName() : "no mood"));
		if (type.equals("Medication"))
			if (mood == null || mood.getName().equals("Authorized")
			|| mood.getName().equals("Actual"))
				return currentMeds.keySet();
			else {
				WhereFilter filter = caseSpecificWhere(
						DharmaPaddaConstants.AND, new WhereComparisonFilter(
								"mood", "eq", mood.getName()));
				Collection instances = kbmanager.findInstances("Medication",
						filter, null);
				ArrayList drugNames = new ArrayList();
				for (Iterator i = instances.iterator(); i.hasNext();) {
					String drugName = ((Medication) i.next()).getDrug_name();
					logger.debug("dataHandler currentActivities: " + drugName
							+ " " + mood.getName());
					drugNames.add(drugName);
				}
				return drugNames;
			}
		else
			return null;
	}

	public String ICD9Description(String icd9code) {
		return termService.getTermDescription(icd9code, "ICD9");
	}

	public String getDemographics(String label) {
		String value = (String) demographics.get(label);
		if (value == null) {
			try {

				DataElement result = doQualitativeQuery(kbmanager, label,
						null, null);	
				if (result != null)
					value = result.value;
			} catch (Exception e) {
				logger.error("getDemographics exception "
						+ label);
			}
		}
		return value;
	}

	public int doTemporalQuery(String query, Cls mood)
			throws PCA_Session_Exception {
		return dataSource.PresenceQuery(query);
	}

	public DataElement doNumericQuery(
			edu.stanford.smi.eon.guidelineinterpreter.PaddaGEE guidelineMananger,
			String domain_term, String aggregation_operatorValue,
			edu.stanford.smi.protege.model.Instance period, String entryType,
			Cls mood) throws PCA_Session_Exception {
		if (aggregation_operatorValue == null)
			aggregation_operatorValue = "most_recent";
		DataElement returnValue = new DataElement(domain_term, "", "", "",
				aggregation_operatorValue);
		SortedMap dataValues = null;
		int qualitativeEntryNum = 0;
		Matched_Data match = null;
		logger.debug("in doNumericQuery - domain_term: " + domain_term
				+ " entryType: " + entryType);
		if (entryType != null
				&& (entryType.equals("Medication") || entryType
						.equals("Note_Entry"))) {
			if (entryType.equals("Note_Entry")) {
				// find list of problems that are subclasses of domain_term
				logger.debug("in doNumericQuery - currentProblemss: "
						+ currentProblems());
				match = guidelineMananger.containSubclassOf(entryType,
						domain_term, currentProblems());
				if (match != null)
					qualitativeEntryNum = match.data.length;
				else
					qualitativeEntryNum = 0;
			} else {
				logger.debug("in doNumericQuery - activity: "
						+ currentActivities(entryType, mood));
				match = guidelineMananger.containSubclassOf(entryType,
						domain_term, currentActivities("Medication", mood));
				if (match != null)
					qualitativeEntryNum = match.data.length;
				else
					qualitativeEntryNum = 0;
			}
			if (aggregation_operatorValue.equals("count")) {
				logger
				.debug("in doNumericQuery count = "
						+ qualitativeEntryNum);
				returnValue.value = Integer.toString(qualitativeEntryNum);
				return returnValue;
			} else
				logger.error("Incorrect aggregation or entry type: "
						+ aggregation_operatorValue + " for entryType: "
						+ entryType+ " with domain term "+domain_term);
			throw new PCA_Session_Exception();
		} else {
			dataValues = getDataWithinPeriod(domain_term, period);
			return aggregateData(dataValues, aggregation_operatorValue,
					returnValue);
		}
	}

	private DataElement aggregateData(SortedMap dataValues,
			String aggregationOperator, DataElement returnValue)
					throws PCA_Session_Exception {
		float aggregateValue = 0;
		Object currentTime = null;
		if (aggregationOperator.equals("count")) {
			if (dataValues == null)
				returnValue.value = "0";
			else
				returnValue.value = Integer.toString(dataValues.size());
			return returnValue;
		}
		if ((dataValues == null) || dataValues.isEmpty()) {
			return null;
		} else {
			if (aggregationOperator.equals("most_recent")) {
				logger.debug("DataHandler.aggregateData lastKey: "
						+ dataValues.lastKey());
				returnValue.value = ((Float) dataValues.get(dataValues
						.lastKey())).toString();
				returnValue.valid_time = HelperFunctions
						.Int2DateTime(((Long) dataValues.lastKey()).longValue());
				return returnValue;
			} else if (aggregationOperator.equals("average")) {
				for (Iterator i = dataValues.values().iterator(); i.hasNext();) {
					aggregateValue = aggregateValue
							+ ((Float) i.next()).floatValue();
				}
				if (dataValues.size() > 0) {
					returnValue.value = Float.toString(aggregateValue
							/ dataValues.size());
					return returnValue;
				} else
					return null;

			} else if (aggregationOperator.equals("maximum")) {
				aggregateValue = Float.MIN_VALUE;
				float currentValue = 0;
				for (Iterator i = dataValues.entrySet().iterator(); i.hasNext();) {
					Map.Entry entry = (Map.Entry) i.next();
					currentValue = ((Float) entry.getValue()).floatValue();
					if (currentValue > aggregateValue)
						aggregateValue = currentValue;
					currentTime = entry.getKey();
				}
				if (dataValues.size() > 0) {
					returnValue.value = Float.toString(aggregateValue);
					returnValue.valid_time = HelperFunctions
							.Int2DateTime(((Long) currentTime).longValue());
					return returnValue;
				} else
					return null;
			} else if (aggregationOperator.equals("minimum")) {
				aggregateValue = Float.MAX_VALUE;
				float currentValue = 0;
				for (Iterator i = dataValues.entrySet().iterator(); i.hasNext();) {
					Map.Entry entry = (Map.Entry) i.next();
					currentValue = ((Float) entry.getValue()).floatValue();
					if (currentValue < aggregateValue) {
						aggregateValue = currentValue;
						currentTime = entry.getKey();
					}
				}
				if (dataValues.size() > 0) {
					returnValue.value = Float.toString(aggregateValue);
					returnValue.valid_time = HelperFunctions
							.Int2DateTime(((Long) currentTime).longValue());
					return returnValue;
				} else
					return null;
			} else {
				throw new PCA_Session_Exception(
						"DataHandler Incorrect aggregation: "
								+ aggregationOperator);
			}

		}

	}

	private SortedMap getDataWithinPeriod(String domain_term,
			edu.stanford.smi.protege.model.Instance period) {
		Relative_Time_Interval_Definite relativeTime = null;
		// Relative_Time_Point relativeTimePoint = null;
		SortedMap domainData = (SortedMap) numericEntries.get(domain_term);
		if (domainData == null) {
			logger.debug("No data value for " + domain_term);
			return null;
		}
		for (Iterator i = domainData.keySet().iterator(); i.hasNext();) {
			Object key = i.next();
			Object item = domainData.get(key);
			logger.debug("getDataWithinPeriod: " + domain_term + ": "
					+ item.toString() + " " + key.toString());
		}
		if (period != null) {
			Definite_Time_Interval interval = null;
			Long low = null;
			Long high = null;
			try {
				if (period instanceof Relative_Time_Interval_Definite ) {
					relativeTime = (Relative_Time_Interval_Definite) period;
					interval = relativeTime.resolveTime(
							sessionTime, this);
				} else if (period instanceof Definite_Time_Interval) {
					interval = (Definite_Time_Interval)period;
				} else {
					logger.error(period.getBrowserText() + " is not a relative or definite time interval");
					throw new Exception();
				}
				low = new Long(interval.getLow().longValue() * 60 * 24 - 1);
				high = new Long(interval.getHigh().longValue() * 60 * 24 + (60 * 24 -1)); //set to date + 23:59
				logger
				.debug("getDataWithinPeriod high " + high + " low "
						+ low);
			} catch (Exception e) {
				logger.error("Problem resolving time interval "+period.getBrowserText());
				return domainData;
			}

			if (domainData != null)
				return domainData.subMap(low, high);
			else
				return null;
		} else
			return domainData;
	}

	public DataElement doQualitativeQuery(
			edu.stanford.smi.eon.kbhandler.KBHandler kbManager,
			String domain_term, String aggregation_operatorValue,
			Instance period) throws PCA_Session_Exception {
		Definite_Time_Interval timeInterval = null;
		if (period != null) {
			if (period instanceof Relative_Time_Interval) {
				timeInterval = ((Relative_Time_Interval) period).resolveTime(
						sessionTime, this);
			} else if (period instanceof Definite_Time_Interval) {
				timeInterval = (Definite_Time_Interval)period;
			} else {
				logger.error(period.getBrowserText() + " is ot a relative or definite time interval");
			}
		} 
		DataElement returnValue = new DataElement(domain_term, "", null,
				"", aggregation_operatorValue);
		logger.debug("in doQualitativeQuery - domain_term: " + domain_term);

		Object dataResult = demographics.get(domain_term);

		if (dataResult != null) {

			returnValue.value = (String) dataResult;

			return returnValue;
		} else {

			WhereFilter where = caseSpecificWhere(DharmaPaddaConstants.AND,
					new WhereComparisonFilter("domain_term",
							DharmaPaddaConstants.eq, domain_term));
			Collection results = kbManager.findInstances("Note_Entry",
					where, null);

			if ((results != null) || !results.isEmpty()) {
				// relevant aggregration operator is "most_recent". All
				// others
				// return numeric results
				Definite_Time_Point previousTime = null;
				Definite_Time_Point currentTime = null;

				// Iterate through note entries and return value of the
				// most recent one within the time period
				for (Iterator i = results.iterator(); i.hasNext();) {
					Note_Entry entry = (Note_Entry) i.next();
					if (entry.getValue() == null) {
						logger
						.error("DataHandler.doQualitativeQuery: note entry "
								+ domain_term + " has null value!");
						continue;
					}
					if (entry.getValid_time() != null)
						currentTime = (Definite_Time_Point) entry
						.getValid_time();
					else {
						logger.info("Note_Entry " + domain_term
								+ " has null validTime");
						returnValue.value = entry.getValue();
						break;
					}
					try {
						logger
						.debug("DataHandler.doQualitativeQuery: timeInterval: "+ ((timeInterval != null) ? timeInterval.toString() : "null")+
								" time of note entry: "+currentTime.toString());
						if ((timeInterval == null) || (timeInterval.within(currentTime))) {
							if ((aggregation_operatorValue != null) && (aggregation_operatorValue.equals(DharmaPaddaConstants.EXISTS))) {
								returnValue.value = entry.getValue();
								return returnValue;
							} 
							if (previousTime == null) {
								previousTime = currentTime;
								returnValue.value = entry.getValue();
							} else {
								if ((currentTime != null)
										&& (currentTime.after(previousTime))) {
									previousTime = currentTime;
									returnValue.value = entry.getValue();
								}
							}
						}
					} catch (Exception e) {
						logger.error("doQualitativeQuery exception "
								+ e.getMessage());
						throw new PCA_Session_Exception(e.getMessage());
					}
				}
				if ((returnValue.value == null) || (returnValue.value.equals("")))
					return null;
				else {
					logger
					.debug("in doQualitativeQuery - returnValue.value: "
							+ returnValue.value);

					return returnValue;
				}
			} else {

				return null;
			}

		}
	}

	public Set getNumericEventKeys() {
		return numericEntries.keySet();
	}

	public Map getNumericEntries() {
		return numericEntries;
	}

	public TreeMap getNumericEventsOfLabel(String label) {
		return (TreeMap) numericEntries.get(label);
	}

	public Collection getNumericEvents(String label) {
		WhereFilter myNumericEntries = caseSpecificWhere(
				DharmaPaddaConstants.AND, new WhereComparisonFilter(
						"domain_term", DharmaPaddaConstants.eq, label));
		Collection resultSet = kbmanager.findInstances("Numeric_Entry",
				myNumericEntries, null);
		return resultSet;
	}

	public void makeNumericEvent(String label, float value, long timestamp) {
		/*
		 * Collection resultSet = null; TreeMap results = (TreeMap)
		 * numericEntries.get(label); Long key; Absolute_Time_Point timePoint;
		 * Numeric_Entry entry; logger.debug( "DataHandler getNumericEvents:
		 * registering " + label); if (results != null) { resultSet = new
		 * ArrayList(); // Time of numeric data are indexed at granularity of
		 * minutes for (Iterator i = results.keySet().iterator(); i.hasNext();) {
		 */
		Absolute_Time_Point timePoint = (Definite_Time_Point) createInstance("Definite_Time_Point");
		timePoint.setsystem_timeValue((int) (timestamp / 1440));
		timePoint.setDateValue((int) (timestamp / 1440));
		Numeric_Entry entry = (Numeric_Entry) createInstance("Numeric_Entry");
		logger.debug("in data handler: getNumericEvents label=" + label
				+ " key=" + timestamp + " result=" + value);

		entry.setSlotsValues(label, (float) 0.0, patient_id, "", (float) 0.0,
				timePoint, value);
		logger.debug("DataHandler getNumericEvents: registering " + label);
		// resultSet.add(entry);

	}

	public KBHandler getKBHandler() {
		return kbmanager;
	}

	public Collection getAdverseEvents(Cls substanceClass) {

		KnowledgeBase kb = kbmanager.getKB();
		Collection selectedAdverseEvents = new ArrayList();
		if (substanceClass == null) {
			selectedAdverseEvents = adverseEvents.values();
		} else {
			for (Iterator i = adverseEvents.entrySet().iterator(); i.hasNext();) {
				Map.Entry entry = (Map.Entry) i.next();
				Cls keyClass = kb.getCls((String) entry.getKey());
				if ((keyClass != null)
						&& (keyClass.equals(substanceClass) || keyClass
								.hasSuperclass(substanceClass)))
					selectedAdverseEvents.add(entry.getValue());
			}
		}
		if (selectedAdverseEvents.isEmpty())
			return null;
		else
			return selectedAdverseEvents;
	}

	public Object attributeValue(String type, String key, String attribute) {
		if (type.equals("Medication")) {
			Object medObj = currentMeds.get(key);
			if (medObj != null) {
				Medication med = (Medication) medObj;
				if (attribute.equals("daily_dose")) {
					return new Float(med.getDaily_dose());
				} else if ( attribute.equals("valid_time")) {
					return med.getValid_time();
				} else {
					logger.error("Unknown attribute "+attribute);
					return null;
				}
			} else {
				logger.error("Exception: Medication " + key
						+ " has null Medication object");
				return null;
			}		
		} else
			return null;
	}

	public Object findAttributeValue(GuidelineInterpreter glmanager,
			String className, Object obj, String attribute)
					throws PCA_Session_Exception {

		// className: class of data (e.g., Medication)
		// obj: a key to instances of data class (e.g., name of drug)
		// attribute: the attribute whose value we are trying to find
		//
		// special code to find "dose_level" of "Authorized_Medicaiton"

		KnowledgeBase kb = kbmanager.getKB();
		Object valueObject = null;
		if (className.equals("Medication")) {
			// Cls classInstance = kb.getCls(className);
			// check to see if attribute is an attribute of classInstance
			// if yes, get value from obj

			// start hacks
			if (attribute.equals("daily_dose")) {
				valueObject = attributeValue(className, (String) obj,
						"daily_dose");
				if (valueObject != null) {
					logger.debug("DataHandler findAttributeValue classname= "
							+ className + " Object= " + obj.toString()
							+ " valueObject = " + valueObject.toString());
				} else
					logger
					.debug("DataHandler findAttributeValue Null valueObject classname= "
							+ className + " Object= " + obj.toString());
				return valueObject;
			} else {
				if (attribute.equals("dose_level")) {
					Object dailyDose = attributeValue(className, (String) obj,
							"daily_dose");
					logger.debug("in data handler: findAttributeValue class="
							+ className + " object=" + (String) obj
							+ " dailyDose=" + dailyDose);
					if (dailyDose != null) {
						Cls drug = kb.getCls((String) obj);
						if (drug == null) {
							logger.error("Data handler: drug " + (String) obj
									+ " not in knowledge base");
							return null;
						}
						WhereComparisonFilter where = new WhereComparisonFilter(
								"generic_drug", "eq", drug);
						Collection instances = kbmanager.findInstances(kb
								.getCls("Guideline_Drug"), where, glmanager);
						if ((instances != null) && (!instances.isEmpty())) {
							if (instances.size() > 1) {
								throw new PCA_Session_Exception(
										"Ambiguous Guideline_Drug instance "
												+ " more than one matches "
												+ obj);
							} else {
								Guideline_Drug glDrug = (Guideline_Drug) instances
										.iterator().next();
								logger
								.debug("in data handler: findAttributeValue glDrug="
										+ glDrug.getlabelValue());
								Collection ranges = glDrug
										.getdose_level_rangesValue();
								if (ranges != null) {
									for (Iterator i = ranges.iterator(); i
											.hasNext();) {
										Range_Mapping_Entry mappingEntry = (Range_Mapping_Entry) i
												.next();
										if (mappingEntry
												.withinLimits(((Float) dailyDose)
														.floatValue())) {
											valueObject = mappingEntry
													.getabstract_valueValue()
													.getName();
											break;
										}
									}
								}
							}
						} else
							logger.warn("No Guideline_Drug corresponds to "
									+ drug.getName());
					}
				}
			}
		}// end hacks
		if (valueObject != null)
			logger
			.debug("in comparison filter: findAttributeValue, returning doselevel"
					+ valueObject.toString());
		else
			logger
			.debug("in comparison filter: findAttributeValue, returning null doselevel");
		return valueObject;
	}

	public Instance createInstance(String className) {
		Cls cls = null;
		if ((className != null)
				&& (cls = kbmanager.getKB().getCls(className)) != null) {
			Instance newInstance = kbmanager.getKB().createInstance(null, cls);
			registerInstance(newInstance);
			return newInstance;
		} else
			return null;
	}

	public void deleteInstance(Instance instance) {
		if (instance.isIncluded())
			instance.setIncluded(false);
		if (RuntimeProtegeInstances.get(instance.getName()) != null)
			RuntimeProtegeInstances.remove(instance.getName());
		kbmanager.getKB().deleteInstance(instance);
	}

	public void clearRuntimeInstances() {
		for (Iterator i = RuntimeProtegeInstances.entrySet().iterator(); i
				.hasNext();) {
			Map.Entry entry = (Map.Entry) i.next();
			Instance protegeInstance = (Instance) entry.getValue();
			entry.setValue(null);
			if (protegeInstance.isIncluded())
				protegeInstance.setIncluded(false);
			kbmanager.getKB().deleteInstance(protegeInstance);
		}
		RuntimeProtegeInstances.clear();
	}

	public void deleteRecommendations() {
		Cls medCls = kbmanager.getCls("Medication");
		Slot mood = kbmanager.getKB().getSlot("mood");

		for (Iterator inst = medCls.getInstances().iterator(); inst.hasNext();) {
			String moodString = "";
			Instance instance = (Instance) inst.next();
			Object moodObj = instance.getOwnSlotValue(mood);
			if (moodObj != null)
				moodString = ((Cls) moodObj).getName();
			if ((moodString.startsWith("Recommend"))
					&& (RuntimeProtegeInstances.get(instance.getName()) != null))
				deleteInstance(instance);
		}
	}

	public void registerInstance(Instance instance) {
		instance.setIncluded(true);
		RuntimeProtegeInstances.put(instance.getName(), instance);
	}

	public Collection currentProblems(Definite_Time_Interval timeInterval) {
		Collection problems = new ArrayList();
		Collection allproblems = new ArrayList();
		if (currentProblems != null)
			allproblems = currentProblems.keySet();
		else
			return problems;
		// Collection whereClauses = new ArrayList();
		WhereComparisonFilter where = new WhereComparisonFilter("domain_term",
				"eq", null);
		WhereFilter whereFilter = this.caseSpecificWhere(
				DharmaPaddaConstants.AND, where);

		for (Iterator i = allproblems.iterator(); i.hasNext();) {
			String problemName = (String) i.next();
			where.value = problemName;
			// logger.debug("");
			// logger.debug("DataHandler.currentProblems problem name:
			// "+problemName);
			Collection noteEntries = kbmanager.findInstances("Note_Entry",
					whereFilter, null);
			if (noteEntries != null) {
				for (Iterator j = noteEntries.iterator(); j.hasNext();) {
					Note_Entry entry = (Note_Entry) j.next();
					Instance time;
					if ((time = entry.getValid_time()) != null) {
						try {
							logger.debug("Check: NoteEntry domain_term: "
									+ entry.getDomain_term());
							/*							if (((Definite_Time_Point) timeInterval
									.gettime_pointValue()) == null) {
								logger.error("Definite_Time_Point "+timeInterval.getBrowserText()+
										" has null time_point ");
								break;
							} else {
								logger.debug(" TimeInterval: "
										+ ((Definite_Time_Point) timeInterval
												.gettime_pointValue())
												.getsystem_timeValue()
												+ "/"
												+ timeInterval.gethow_manyValue()
												+ " contains "
												+ ((Definite_Time_Point) time)
												.getsystem_timeValue());
							 */								if (timeInterval.within((Definite_Time_Point) time)) {
								 problems.add(problemName);
								 logger.debug("timeInterval "
										 + timeInterval.getName() + " contains "
										 + time.getName());
							 }
							 //	}
						} catch (Exception e) {
							logger.error("Exception checking time bound", e);
						}
					} else
						problems.add(problemName);
				}
			}
		}

		return problems;
	}

	public void setEpisodePeriod(String name, String startTime, String stopTime) 
			throws DataException {
		KnowledgeBase kb = this.kbmanager.getKB();
		Cls timePointCls = kb.getCls("Definite_Time_Point");
		Instance episode = kb.getInstance(name);
		Definite_Time_Point startTimeInstance = null;
		Definite_Time_Point stopTimeInstance = null;
		if ((episode != null) && (episode.hasDirectType(kb.getCls("Definite_Time_Interval")))) {
			if (timePointCls != null) {
				if ((startTime !=null) && !(startTime.equals(""))){
					startTimeInstance = (Definite_Time_Point) kb.createInstance(null,timePointCls ) ;
					this.registerInstance(startTimeInstance);
					startTimeInstance.setDateValue(startTime);
					((Definite_Time_Interval)episode).setstart_timeValue(startTimeInstance);
				}
				if ((stopTime != null) && !(stopTime.equals(""))){
					stopTimeInstance= (Definite_Time_Point) kb.createInstance(null,timePointCls ) ;
					this.registerInstance(stopTimeInstance);
					stopTimeInstance.setDateValue(stopTime);
					((Definite_Time_Interval)episode).setstop_timeValue(stopTimeInstance);
				}
			} else {
				logger.error("Definite_Time_Point is not a Protege class");
				throw new DataException("Definite_Time_Point is not a Protege class");
			}
		} else {
			logger.error(name +" is not an instance or is not a Definite_Time_Interval");
			throw new DataException(name +" is not an instance or is not a Definite_Time_Interval");		
		}
	}

	public Collection<DataEntry> getDataEntry (String domainTerm, boolean mostRecent) {
		Collection<DataEntry> returnValues = new ArrayList<DataEntry>();
		SortedMap numericEventsMap = getNumericEventsOfLabel(domainTerm);
		Collection<Numeric_Entry> numericEventCollection= (Collection<Numeric_Entry>)getNumericEvents(domainTerm);
		if ((numericEventsMap != null) && !numericEventsMap.isEmpty()) {
			if (mostRecent) {
				Float value = (Float) numericEventsMap.get(numericEventsMap.lastKey());
				/* DataEntry (String domain_term, String patient_id, String valid_time, String value) */
				DataEntry data = new DataEntry(domainTerm, patient_id, 
						HelperFunctions.Int2DateTime(((Long)numericEventsMap.lastKey()).longValue()),
						value.toString());
				returnValues.add(data);
			} else {
				for (Numeric_Entry dataInstance : numericEventCollection) {
					DataEntry data = new DataEntry(domainTerm, patient_id, 
							HelperFunctions.Int2DayString(((Definite_Time_Point)dataInstance.getValid_time()).getsystem_timeValue()),
							Float.toString(dataInstance.getValue()));
					returnValues.add(data);					
				}
			}	
		}
		//Try note entry
		if (mostRecent) {
			//	public DataElement(String domain_term, String valid_time, String value, String unit, String qualifier)
			DataElement noteEntry = null;
			try {
				noteEntry = doQualitativeQuery(this.kbmanager,
						domainTerm, "most_recent", null);
			} catch (PCA_Session_Exception e) {
				e.printStackTrace();
			}
			if (noteEntry != null) {
				DataEntry data = new DataEntry(noteEntry.domain_term, patient_id, 
						noteEntry.valid_time,
						noteEntry.value);
				returnValues.add(data);
			}
		} else {
			WhereFilter myProblem = caseSpecificWhere(DharmaPaddaConstants.AND,
					new WhereComparisonFilter("domain_term",
							DharmaPaddaConstants.subclass_of, domainTerm));
			Collection problems = kbmanager.findInstances("Note_Entry", myProblem, null);
			for (Note_Entry noteEntry : (Collection<Note_Entry>)problems) {
				DataEntry data = new DataEntry(noteEntry.getDomain_term(), patient_id, 
						HelperFunctions.Int2DayString(((Definite_Time_Point)noteEntry.getValid_time()).getsystem_timeValue()),
						noteEntry.getValue());
				returnValues.add(data);
			}			
		}
		if (returnValues.isEmpty()) 
			return null;
		else
			return returnValues;
	}

	public Collection<edu.stanford.smi.eon.data.Medication> getMedication (String drugName, boolean mostRecent) {
		Collection<edu.stanford.smi.eon.data.Medication> returnValues = new ArrayList<edu.stanford.smi.eon.data.Medication>();
		WhereFilter where = caseSpecificWhere(DharmaPaddaConstants.AND,
				new WhereComparisonFilter("drug_name",
						DharmaPaddaConstants.subclass_of, drugName));
		Collection meds = this.kbmanager.findInstances("Medication", where);
		for (Medication med : (Collection<Medication>)meds) {
			if ((med.getMood() == null) || (med.getMood().getName().equals("Authorized"))) {
				edu.stanford.smi.eon.data.Medication medData = new edu.stanford.smi.eon.data.Medication (
						med.getDaily_dose(), // float daily_dose,
						med.getDaily_dose_unit(), //String daily_dose_unit,
						med.getDrug_name(), //String drug_name,
						(med.getMood() != null ? med.getMood().getBrowserText() :  null) , //String mood,
						patient_id, //String patient_id, 
						med.getPrescription_id(), //String prescription_id,
						med.getRoute(),//String route,
						med.getStatus(),//String status,
						(med.getValid_time() != null ? ((Definite_Time_Interval)med.getValid_time()).getlabelValue() : null) //String start_time,
						);
				returnValues.add(medData);
			}
		}
		if (returnValues.isEmpty())
			return null;
		else
			return returnValues;
	}

	public Collection<AdverseReaction> getAdverseReaction ( String substance, boolean mostRecent) {
		Collection<AdverseReaction> returnValues = new ArrayList<AdverseReaction>();
		WhereFilter where = caseSpecificWhere(DharmaPaddaConstants.AND,
				new WhereComparisonFilter("substance",
						DharmaPaddaConstants.subclass_of, substance));
		Collection reactions = this.kbmanager.findInstances("Adverse_Reaction", where);
		for (Adverse_Reaction reaction : (Collection<Adverse_Reaction>)reactions) {
			AdverseReaction reactionData = new AdverseReaction (
					reaction.getDomain_term(),
					patient_id, 
					reaction.getSubstance(), 
					null) ;
			returnValues.add(reactionData);
		}
		if (returnValues.isEmpty())
			return null;
		else
			return returnValues;
	}


	/*
	 * public void registerKBInstance(Instance instance) {
	 * instance.setIncluded(true);
	 * RuntimeProtegeInstances.put(instance.getName(), instance); }
	 */

}