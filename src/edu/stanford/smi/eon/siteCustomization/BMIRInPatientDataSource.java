package edu.stanford.smi.eon.siteCustomization;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.log4j.Logger;

import edu.stanford.smi.eon.execEngine.IDataSource;

public class BMIRInPatientDataSource extends BMIRDataSource implements IDataSource {
	private Connection con = null;
	static Logger logger = Logger.getLogger(BMIRInPatientDataSource.class);
	private String MedQuery;
	private String DXQuery;
	private String VitalsQuery;
	private String ADRQuery;
	private String LabQuery;
	private String DemographicsQuery;
	private String AgeQuery;
	private String HospitalizationQuery;

	BMIRInPatientDataSource(Connection con, String DBINIfile) {
		this.con = con;
		Properties settings = new Properties();
		try {
			FileInputStream sf = new FileInputStream(DBINIfile);
			settings.load(sf);
		} catch (Exception ex) {
			logger.error("Exception during loading initialization file");
			logger.error(ex.toString());
		}
		MedQuery = settings.getProperty("QUERY_FOR_MEDS", "");
		DXQuery  = settings.getProperty("QUERY_FOR_DX", "");
		VitalsQuery=settings.getProperty("QUERY_FOR_VITALS", "");
		ADRQuery=settings.getProperty("QUERY_FOR_ADR", "");
		LabQuery=settings.getProperty("QUERY_FOR_LABS", "");
		DemographicsQuery=settings.getProperty("QUERY_FOR_DEMOGRAPHICS", "");
		AgeQuery=settings.getProperty("QUERY_FOR_AGE", "");
		HospitalizationQuery = settings.getProperty("QUERY_FOR_HOSPITALIZATION", "");
		// LVEF, 
		// Death
		// CPT code
		//
		//Disposition: AMA, disChargeToHostal, dischargeToHospice
		//Length of stay: 120 days
		//In skilled nursing facility <3m before admission
		//Admission date
		//Discharge date

	}

	private void loadHospitalization(Connection con, String patientID, String episodeID, String sessionTime, String queryString) throws SQLException {
		Statement stmt = null;
		logger.info("hospitalization query: "+queryString);
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(queryString);
			while (rs.next()) {
				String admissionDate = rs.getString("admission_date");
				String dischargeDate = rs.getString("discharge_date");
				rs.getString("admission_diagnosis");
				String lengthOfStay =rs.getString("length_of_stay");
				String disposition =rs.getString("disposition");
				try {
					aDataImporter.cacheNumericEntry("Hospitalization_LengthofStay", Integer.parseInt(lengthOfStay), dischargeDate);
					aDataImporter.cacheNoteEntry("Admission_Institution", admissionDate, dischargeDate);
					aDataImporter.cacheNoteEntry("Disposition", disposition, dischargeDate);
					//aDataImporter.cacheAdverseEvent("lisinopril", "angioedema", "1979-06-01");
					aDataImporter.cacheNumericEntry("Left Ventricular Ejection fraction", 25, "1979-06-01");
					//System.out.println("Disposition: "+disposition);
					//aDataImporter.cacheNoteEntry("Principal_Diagnosis", admissionDiagnosis, admissionDate);
					aDataImporter.setEpisodePeriod("FocusAdmissionPeriod", admissionDate, dischargeDate);
					aDataImporter.cacheNumericEntry("eGFR", 65, "1979-6-01");
					aDataImporter.cacheAdverseEvent("lisinopril", "hyperkalemia", "1979-4-02");
					aDataImporter.cachePrescription("lisinopril", 0, "1979-4-20", "1979-06-01", false, null);
					aDataImporter.cacheNumericEntry("Creatinine", 0.7f, "1979-4-12");
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) { stmt.close(); }
		}
	}

	public void loadData(String patientID, String episodeID, String sessionTime) throws Exception {
		aDataImporter.changeCase(patientID, sessionTime);
		if (aDataImporter != null) {	
			String query = MedQuery.replaceAll("xxx", patientID).replaceAll("xxSessionTimexx", sessionTime);
			loadMed(con, patientID,  episodeID,  sessionTime, query);
			query = DXQuery.replaceAll("xxx", patientID).replaceAll("xxSessionTimexx", sessionTime);
			loadDX(con, patientID,  episodeID,  sessionTime, query);
			query = LabQuery.replaceAll("xxx", patientID).replaceAll("xxSessionTimexx", sessionTime);
			loadLab(con, patientID,  episodeID,  sessionTime, query);
			query=HospitalizationQuery.replaceAll("xxx", patientID).replaceAll("xxHIDxx", episodeID);
			loadHospitalization(con, patientID, episodeID, sessionTime, query);
		} else
			throw new Exception("data importer not initialized in data source!");
	}

}
