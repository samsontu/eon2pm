package edu.stanford.smi.eon.execEngine;

import java.util.Collection;

import edu.stanford.smi.eon.data.AdverseReaction;
import edu.stanford.smi.eon.data.DataEntry;
import edu.stanford.smi.eon.data.Medication;
import edu.stanford.smi.eon.datahandler.DataException;
import edu.stanford.smi.eon.datahandler.DataHandler;
import edu.stanford.smi.eon.kbhandler.KBHandler;




public interface IDataImporter {

	void cachePrescription(String med, float dailyDose,
			String startTime, String stopTime, boolean cumulative, String assessedStatus);
	
	public void cachePrescription (String med, float dailyDose,
			String startTime, String stopTime, boolean cumulative, String assessedStatus, float MPR, String PRT);

	public void changeCase(String caseID, String sessionTime) ;

	void cacheNoteEntry(String domainTerm, String date);
	
	void cacheNoteEntry(String domainTerm, String value, String date);
	
	public void cacheDemographics(String name, String value);

	public void cacheAdverseEvent(String substance, String symptom, String date);
	
	public void cacheNumericEntry(String parameter, float value, String date)
			throws DataException;
	
	public void setEpisodePeriod(String name, String startTime, String stopTime)
		throws DataException;
	
	public DataHandler getDataHandler();
	
	public void updateKB(KBHandler kb) throws Exception;
	
	public void cacheKBData() throws Exception;
	


}
