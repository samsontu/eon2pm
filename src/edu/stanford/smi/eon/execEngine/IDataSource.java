package edu.stanford.smi.eon.execEngine;

import edu.stanford.smi.eon.kbhandler.KBHandler;
import edu.stanford.smi.eon.pca.PCASession_Imp;


public interface IDataSource {
	  public void loadData(String patientID, String episodeID, String sessionTime)
			  throws Exception ;  // Question:use of sessionTime?
	  
	  public void setPCA(PCASession_Imp pca);
	  public PCASession_Imp getPCA();

	  public IDataImporter getDataImporter();
	  public void setDataImporter(IDataImporter aDataImporter);
	  

	  // returns a case id if the data source is already loaded with a patient case
	  public String getCaseID();
	  public void setCaseID(String caseID);
	  
	  // Set up KB so that it is consistent with the loaded data
	  public void updateKB(KBHandler kb) throws Exception;
	  
	  

}
