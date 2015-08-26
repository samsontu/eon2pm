package edu.stanford.smi.eon.execEngine;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import edu.stanford.smi.eon.PCAServerModule.Guideline_Service_Record;
import edu.stanford.smi.eon.PCAServerModule.PCASession;
import edu.stanford.smi.eon.PCAServerModule.PCA_Initialization_Exception;
import edu.stanford.smi.eon.PCAServerModule.PCA_Session_Exception;
import edu.stanford.smi.eon.data.AdverseReaction;
import edu.stanford.smi.eon.data.DataEntry;
import edu.stanford.smi.eon.data.Medication;
import edu.stanford.smi.eon.datahandler.DataHandler;
import edu.stanford.smi.eon.inputoutput.AdvisoryFormater;
import edu.stanford.smi.eon.kbhandler.KBHandler;
import edu.stanford.smi.eon.pca.PCASession_Imp;
import gov.va.athena.advisory.Advisory;
import gov.va.athena.advisory.Guideline_Goal;

public abstract class EON implements IEON {
	KBHandler m_kb;
	DataHandler m_dataHandler;
	String m_initFileFullPath; //This is not in the IEON interface
	IDataSource m_DS;
	PCASession_Imp pcaSession = null;

	static Logger logger = Logger.getLogger(EON.class);

	public EON(KBHandler kb) {
		m_kb = kb;
		setUpPCASession();
	}

	public EON(KBHandler kb, IDataSource aDS) throws Exception {
		m_kb = kb;
		// setUpPCASession initializes the necessary execution engine
		// infrastructure, including the data handler. 
		setUpPCASession();
		setDataSource(aDS);
	}

	public KBHandler getKBHandler() {
		return m_kb;
	}

	public void setDataSource(IDataSource aDS) throws Exception {
		m_DS = aDS;
		if (aDS.getDataImporter() != null)
		{
			pcaSession.setDataManager(aDS.getDataImporter().getDataHandler());
			m_dataHandler = aDS.getDataImporter().getDataHandler();
			if (aDS.getCaseID()!= null) {
				aDS.updateKB(m_kb);
			}
		} else
			aDS.setDataImporter(new DataImporter(m_dataHandler));
	}

	public void setGuideline(String guidelineName) {
		try {
			pcaSession.setGuideline(guidelineName);
		} catch (PCA_Session_Exception e) {
			e.printStackTrace();
		} catch (PCA_Initialization_Exception e) {
			e.printStackTrace();
		}

	}


	public java.lang.String printData(String format) {
		return pcaSession.printData(format);
	}

	public java.lang.String printAdvisory(Advisory advisory, String format) {
		return pcaSession.printAdvisory(advisory, format);
	}

	public Advisory computePerformanceMeasuresAdvisory(String pid,
			String hospitalizationID, String sessionTime, String guidelineName,
			String PMName, String startTime, String stopTime) throws PCA_Session_Exception {
		try {
			m_DS.loadData(pid, hospitalizationID, sessionTime);
			return pcaSession.computePerformanceMeasuresAdvisory(pid, hospitalizationID, sessionTime, guidelineName, PMName, startTime, stopTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public Collection<Advisory> computeAndStoreAdvisories(String pid, String sessionTime, 
			String guidelineName, String storageDirectory, boolean computeSubguidelines
			) throws
			PCA_Session_Exception {
		Guideline_Service_Record[] advisories;
		setUp(pid, guidelineName, sessionTime);
		advisories = pcaSession.computeAndStoreAdvisories(storageDirectory);
		if (computeSubguidelines) {
			// TODO Finish coding method
		}
		
		return this.convertToATHENAAdvisories(advisories, pid,  guidelineName,  sessionTime);


	}

	public Collection<Advisory> loadAdvisories(String pid, String sessionTime, 
			 String storageDirectory
			) throws
			PCA_Session_Exception {
		// TODO Finish coding method
		return null;
	}

	public Collection<Advisory> computeAdvisories(String pid, String sessionTime, String guidelineName
			, boolean computeSubguidelines) throws
			PCA_Session_Exception {
		
		Guideline_Service_Record[] advisories;
		boolean ready = setUp(pid, guidelineName, sessionTime);
		if (ready) {
			advisories = pcaSession.computeAdvisories(pid, sessionTime, 
					guidelineName, computeSubguidelines);
			return convertToATHENAAdvisories(advisories, pid, guidelineName, sessionTime);
		} else
			return null;
	}

	public boolean isEligible(String pid, String sessionTime, String guidelineName
			) throws
			PCA_Session_Exception {
		// TODO Finish coding method
		return false;
	}

	// Returns true if the patient satisfies the goal of the guideline
	public Guideline_Goal evaluateGoal(String pid, String sessionTime, String guidelineName
			) throws
			PCA_Session_Exception {
		// TODO Finish coding method
		Guideline_Goal evaluatedGoal = null;
		return evaluatedGoal;
	}

	public void updateData(
			edu.stanford.smi.eon.PCAServerModule.Patient_Data[] data
			) throws
			edu.stanford.smi.eon.PCAServerModule.Improper_Data_Exception {
		pcaSession.updateData(data);
	}

	public Collection<Advisory> updateAdvisories(String pid, String guidelineName, String sessionTime,
			boolean computeSubguidelines
			) throws
			PCA_Session_Exception {
		Guideline_Service_Record[] advisories = pcaSession.computeAdvisories(pid, sessionTime, 
				guidelineName, computeSubguidelines);
		return convertToATHENAAdvisories(advisories, pid, guidelineName, sessionTime);
	}


	private void setUpPCASession() {
		pcaSession = new PCASession_Imp();
		try {
			pcaSession.initPCASesion(getKBHandler());
			m_dataHandler = pcaSession.getDataManager();
		} catch (PCA_Initialization_Exception e) {
			e.printStackTrace();
		}
	}

	public PCASession_Imp getPCASession() {
		return pcaSession;
	}

	private Collection<Advisory> convertToATHENAAdvisories(Guideline_Service_Record[] gsrs,
			String pid, String guidelineName, String sessionTime ) {
		Collection<Advisory> advisories = 
			AdvisoryFormater.javaTransform(gsrs, pid, null, guidelineName, sessionTime, null, null, pcaSession.kbManager.getKB());
		return advisories;
	}

	public void finishSession() {
		pcaSession.finishSession();
	}

	private boolean setUp(String pid, String guidelineName, String sessionTime) {
		try {
			if (m_DS.getCaseID() != pid) {
				m_DS.loadData(pid, null, sessionTime);
				m_DS.setCaseID(pid);
			} else if (needUpdateKB()) {
				m_DS.updateKB(m_kb);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		if (guidelineName != null) {
			try {
				pcaSession.setGuideline(guidelineName);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} 
		} else {
			if (pcaSession.getGuideline() == null) {
				logger.error("Null guideline name and no prior set up of guideline");
				return false;
			}
		}
		return true;
	}
	
	
	private boolean needUpdateKB() {
		return (m_DS.getDataImporter().getDataHandler().getKBHandler() != m_kb);
	}

	public Collection<DataEntry> getDataEntry (String domainTerm, boolean mostRecent) {
		return m_dataHandler.getDataEntry(domainTerm, mostRecent);
	}
	public Collection<Medication> getMedication (String drugName, boolean mostRecent) {
		return m_dataHandler.getMedication(drugName, mostRecent);
	}
	public Collection<AdverseReaction> getAdverseReaction (String substance, boolean mostRecent) {
		return m_dataHandler.getAdverseReaction(substance, mostRecent);
	}

}
