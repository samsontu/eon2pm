package edu.stanford.smi.eon.execEngine;


import org.apache.log4j.Logger;



import edu.stanford.smi.eon.PCAServerModule.PCA_Session_Exception;
import edu.stanford.smi.eon.datahandler.DataException;
import edu.stanford.smi.eon.datahandler.DataHandler;
import edu.stanford.smi.eon.kbhandler.KBHandler;

public class DataImporter implements IDataImporter {
	DataHandler dataManager = null;
	
	DataImporter(DataHandler dataHandler) {
		this.dataManager = dataHandler;
	}
	
	static Logger logger = Logger.getLogger(DataImporter.class);
	
	public void cachePrescription(String med, float dailyDose,
			String startTime, String stopTime, boolean cumulative, String assessedStatus) {
		dataManager.cachePrescription(med, dailyDose, startTime, stopTime, cumulative, assessedStatus);
		
	}
	
	public void cachePrescription (String med, float dailyDose,
			String startTime, String stopTime, boolean cumulative, String assessedStatus, int MPR, String PRT) {
		dataManager.cachePrescription(med, dailyDose, startTime, stopTime, cumulative, assessedStatus, MPR, PRT);
	}

	public void changeCase(String caseID, String sessionTime) {
		dataManager.changeCase(caseID, sessionTime);
	}

	public void cacheNoteEntry(String problem, String date) {
		dataManager.cacheNoteEntry(problem, date);
		
	}

	public void cacheNoteEntry(String problem, String value, String date) {
		dataManager.cacheQualitativeData(problem, value, date);
		
	}

	public void cacheDemographics(String name, String value) {
		dataManager.cacheDemographics(name, value);
		
	}

	public void cacheAdverseEvent(String substance, String symptom, String date) {
		dataManager.cacheOneAdverseEvent(substance, symptom, date);
		
	}

	public void cacheNumericEntry(String label, float value, String date)
			throws DataException {
		dataManager.cacheNumericEntry(label, value, date);
		
	}

	public void setEpisodePeriod(String name, String startTime, String stopTime) 
			throws DataException {
		dataManager.setEpisodePeriod( name,  startTime,  stopTime);
		
	}

	public void updateKB(KBHandler kb) throws Exception {
		dataManager.updateKB(kb);
		
	}
	
	public DataHandler getDataHandler() {
		return this.dataManager;
	}

	public void cacheKBData() throws Exception {
		this.dataManager.cacheKBData();
		
	}
}
