package edu.stanford.smi.eon.siteCustomization;

import org.apache.log4j.Logger;

import edu.stanford.smi.eon.PCAServerModule.PCA_Initialization_Exception;
import edu.stanford.smi.eon.datahandler.DataHandler;
import edu.stanford.smi.eon.execEngine.IDataSource;
import edu.stanford.smi.eon.execEngine.IEON;
import edu.stanford.smi.eon.execEngine.IEonFactory;
import edu.stanford.smi.eon.kbhandler.KBHandler;

public class BMIREonFactory implements IEonFactory {
	static Logger logger = Logger.getLogger(BMIREonFactory.class);

	public IEON createEON(String ProtegeProjFullPath) throws PCA_Initialization_Exception {
		KBHandler kbHandler;
		kbHandler = new KBHandler(ProtegeProjFullPath);
		BMIREon eon  = new BMIREon(kbHandler);
		return eon;
	}

	public IEON createEON(String ProtegeProjFullPath, IDataSource aDS) throws PCA_Initialization_Exception {
		KBHandler kbHandler;
		kbHandler = new KBHandler(ProtegeProjFullPath);
		BMIREon eon  = new BMIREon(kbHandler);
		try {
			eon.setDataSource(aDS);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new PCA_Initialization_Exception();
		}
		return eon;
	}

	public IEON createEON(KBHandler kbHandler) throws PCA_Initialization_Exception {
		if (kbHandler != null) 
			return  new BMIREon(kbHandler);
		else {
			logger.error("Null KB Handler in creating EON");
			throw new PCA_Initialization_Exception();
		}
	}

}
