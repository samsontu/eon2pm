package edu.stanford.smi.eon.siteCustomization;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;

import edu.stanford.smi.eon.execEngine.DataSource;
import edu.stanford.smi.eon.execEngine.IDataSource;
import edu.stanford.smi.eon.util.HelperFunctions;

public abstract class BMIRDataSource extends DataSource implements IDataSource {
	static Logger logger = Logger.getLogger(BMIRDataSource.class);
	private SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd hh:mm:ss zzz yyyy");
	protected void loadMed(Connection con, String patientID, String episodeID, String sessionTime, String queryString) throws SQLException {
		Statement stmt = null;
		logger.info("med query: "+ queryString);
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(queryString);
	        while (rs.next()) {
				String drugName = rs.getString("generic_name");
				String dailyDose = rs.getString("daily_dose");	
				///////////////////////////////////////////////////
				// if the value of daily dose is NULL, 
				// the current version is setting the value as "0"
				if (dailyDose == null)
					dailyDose = "0";
				else if (dailyDose.equals("null"))
					dailyDose = "0";
				String date = rs.getString("date");
				aDataImporter.cachePrescription(drugName, Float.parseFloat(dailyDose), date, null, false, null);
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) { stmt.close(); }
		}
	}

	protected void loadDX(Connection con, String patientID, String episodeID, String sessionTime, String queryString) throws SQLException {
		Statement stmt = null;
		logger.info("DX query: "+queryString);
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(queryString);
	        while (rs.next()) {
				String problem = rs.getString("athena_dx_name");
				String date = rs.getDate("last_date").toString();
				aDataImporter.cacheNoteEntry( problem,  date);
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) { stmt.close(); }
		}
	}

	protected void loadLab(Connection con, String patientID, String episodeID, String sessionTime, String queryString) throws SQLException {
		Statement stmt = null;
		logger.info("lab query: "+queryString);
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(queryString);
	        while (rs.next()) {
				String labName = rs.getString("athena_lab_name");
				String labValue = rs.getString("lab_text");
				// eliminate the special character, '<' to generate XML file
				if (labValue.charAt(0) == '<')
					labValue = labValue.substring(1);
				String labDate = rs.getDate("last_date").toString();
				try {
					aDataImporter.cacheNumericEntry(labName, Float.parseFloat(labValue), labDate);
				} catch (Exception e){
					logger.error(labName + " value "+labValue+" cannot be converted to a float");
				}
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) { stmt.close(); }
		}
	}
	
	protected void loadAge(Connection con, String patientID, String episodeID,
			String sessionTime, String query) {
		// TODO Auto-generated method stub
	}


	private String setDateFormat(String theDate) {
		logger.info("Input Date: "+theDate);
		String resultDate = null;
		try {
			resultDate = HelperFunctions.internalDateFormatter.format(inputFormat.parse(theDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		logger.info("Date: "+resultDate);
		return resultDate; 
	}
	

}
