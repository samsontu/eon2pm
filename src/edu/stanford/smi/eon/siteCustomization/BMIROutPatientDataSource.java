package edu.stanford.smi.eon.siteCustomization;

import java.io.FileInputStream;
import java.sql.Connection;
import java.util.Properties;

import org.apache.log4j.Logger;

import edu.stanford.smi.eon.execEngine.IDataSource;

public class BMIROutPatientDataSource extends BMIRDataSource implements IDataSource {
	private Connection con = null;
	static Logger logger = Logger.getLogger(BMIROutPatientDataSource.class);
	private String MedQuery;
	private String DXQuery;
	private String VitalsQuery;
	private String ADRQuery;
	private String LabQuery;
	private String DemographicsQuery;
	private String AgeQuery;
	
	BMIROutPatientDataSource(Connection con, String DBINIfile) {
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
		// LVEF, Death
		// CPT table
		// 
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
			loadAge(con, patientID, episodeID, sessionTime, AgeQuery.replaceAll("xxx", patientID).replaceAll("xxStartMeasurementPeriodxx", "xx"));
			aDataImporter.cacheNumericEntry("Treatment_Diastolic_BP", 95, "1979-12-01 05:23");
			aDataImporter.cacheNumericEntry("Treatment_Systolic_BP", 150, "1979-11-01");
			aDataImporter.cacheNumericEntry("Potassium", 8.0f, "1979-12-01");
			aDataImporter.cacheNoteEntry("Diabetes_Mellitus", "1977-12-01");
			aDataImporter.cacheKBData();
			aDataImporter.cacheAdverseEvent("atenolol", "Rash", "1978-01-03");
			aDataImporter.cacheAdverseEvent("hydrochlorothiazide", "Gout", "1978-01-03");
			aDataImporter.cacheAdverseEvent("lisinopril", "Cough", "1978-01-03");
			aDataImporter.cacheAdverseEvent("felodipine", "Nausea", "1978-01-03");
		} else
			throw new Exception("data importer not initialized in data source!");
	}


}
