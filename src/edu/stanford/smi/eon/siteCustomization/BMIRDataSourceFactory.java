package edu.stanford.smi.eon.siteCustomization;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import edu.stanford.smi.eon.execEngine.IDataSource;
import edu.stanford.smi.eon.execEngine.IDataSourceFactory;

public class BMIRDataSourceFactory implements IDataSourceFactory {

	static Logger logger = Logger.getLogger(BMIRDataSourceFactory.class);

	public IDataSource createDataSource(String initFileFullPath) throws Exception {
		// TODO Auto-generated method stub
		// Read initFileFullPath and set up jdbc connection con

		Properties settings = new Properties();
		try {
			FileInputStream sf = new FileInputStream(initFileFullPath);
			settings.load(sf);
		} catch (Exception ex) {
			logger.error("Exception during loading initialization file");
			logger.error(ex.toString());
			throw ex;
		}
		String host = settings.getProperty("DBHOST", "");
		String dbUser = settings.getProperty("USER");
		String dbPassword = settings.getProperty("PWORD", "");
		String sessionType = settings.getProperty("SESSIONTYPE");
		Class.forName("com.mysql.jdbc.Driver");
		System.out.println("--------Connecting to the Glinda DB Server"+host+" "+dbUser+" "+ dbPassword);	
		logger.info("BMIRDataSource args: "+host+" "+dbUser+" "+ dbPassword);
		Connection con = DriverManager.getConnection (host, dbUser, dbPassword);
		if (sessionType.equals("Outpatient")) { 
			return new BMIROutPatientDataSource(con, initFileFullPath) ;
		} else if (sessionType.equals("Inpatient")) {
			return new BMIRInPatientDataSource(con, initFileFullPath);
		}
		else 
			logger.error("No valid session type (must be inpatient or outpatient");
		return null;
	}

}
