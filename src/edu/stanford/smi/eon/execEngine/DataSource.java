package edu.stanford.smi.eon.execEngine;

import org.apache.log4j.Logger;

import edu.stanford.smi.eon.kbhandler.KBHandler;
import edu.stanford.smi.eon.pca.PCASession_Imp;

public abstract class DataSource implements IDataSource {
	protected IDataImporter aDataImporter = null;
	protected String caseID = null;
	protected PCASession_Imp pca = null;

	static Logger logger = Logger.getLogger(DataSource.class);

	public void setDataImporter(IDataImporter aDataImporter) {
		this.aDataImporter = aDataImporter;

	}
		
	public IDataImporter getDataImporter() {
		return this.aDataImporter;
	}
	
	  
	public String getCaseID() {
		return caseID;
	}
	
	public void setCaseID(String caseID) {
		this.caseID = caseID;
	}
	  
	public void updateKB(KBHandler kb) throws Exception {
		aDataImporter.updateKB(kb);
	}
	  	  
	public void setPCA(PCASession_Imp pca) {
		this.pca = pca;
	}
	
	public PCASession_Imp getPCA() {
		return pca;
	}


		

}
