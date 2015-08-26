package edu.stanford.smi.eon.execEngine;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;

import org.apache.log4j.Logger;

import edu.stanford.smi.eon.PCAServerModule.Improper_Data_Exception;
import edu.stanford.smi.eon.PCAServerModule.PCA_Initialization_Exception;
import edu.stanford.smi.eon.PCAServerModule.Patient_Data;
import edu.stanford.smi.eon.pca.PCASession_Imp;

public  class XMLDataSource extends DataSource implements IDataSource {

	static Logger logger = Logger.getLogger(XMLDataSource.class);
	String xmlData = null;
	
	XMLDataSource(String XMLFileFullPath) {
		try {
			xmlData = readFileAsString(XMLFileFullPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	  	  
	@Override
	public void loadData(String patientID, String episodeID, String sessionTime)
			throws Exception {
		if ((xmlData != null) && (pca != null)) {
			loadData(patientID, episodeID, sessionTime, xmlData, pca);
		} else {
			throw (new Exception("No xmlData or PCA"));
		}
	}

	public void loadData(String patientID, String episodeID,
			String sessionTime, String XMLData, PCASession_Imp pca) {
		aDataImporter.changeCase(patientID, sessionTime);
		Collection<Patient_Data> ptDataArray = pca.readPatientData(XMLData, patientID).create_patient_data_array();
		if (ptDataArray != null) {
			Patient_Data[] data = (Patient_Data[]) ptDataArray.toArray(new Patient_Data[0]);
				pca.resetAdvisories();
				try {
					pca.setCase(patientID, sessionTime);
				} catch (PCA_Initialization_Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					pca.updateData(data);
				} catch (Improper_Data_Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		} else {
			logger.error("No XML patient data!");
		}
	}

		
	private  String readFileAsString(String filePath) throws java.io.IOException{
	    byte[] buffer = new byte[(int) new File(filePath).length()];
	    BufferedInputStream f = null;
	    try {
	        f = new BufferedInputStream(new FileInputStream(filePath));
	        f.read(buffer);
	    } finally {
	        if (f != null) try { f.close(); } catch (IOException ignored) { }
	    }
	    return new String(buffer);
	}

}
