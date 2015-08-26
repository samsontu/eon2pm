package edu.stanford.smi.eon.execEngine;

import org.apache.log4j.Logger;
import edu.stanford.smi.eon.execEngine.IDataSource;
import edu.stanford.smi.eon.execEngine.IDataSourceFactory;

public class XMLDataSourceFactory implements IDataSourceFactory {

	static Logger logger = Logger.getLogger(XMLDataSourceFactory.class);

	public IDataSource createDataSource(String XMLFileFullPath) throws Exception {
		
		XMLDataSource ds = null;
		try {
			ds = new XMLDataSource(XMLFileFullPath);
			
		} catch (Exception ex) {
			logger.error("Exception during loading XML data file");
			logger.error(ex.toString());
			throw ex;
		}
		return ds;
	}
	

}
