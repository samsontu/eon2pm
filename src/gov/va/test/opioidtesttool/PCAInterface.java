package gov.va.test.opioidtesttool;

import gov.va.test.opioidtesttool.degui.*;
// import gov.va.test.PCAClient;
import gov.va.test.opioidtesttool.pca.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.io.*;
import java.text.*;
import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.widget.*;
import edu.stanford.smi.protege.resource.*;

import edu.stanford.smi.eon.PCAServerModule.*;
import edu.stanford.smi.eon.kbhandler.KBHandler;


/*
 * This object is responsible for dealing with PCA Server
 * (i.e. Guideline Exection Manager)
 * 
 * It handles creating a new guideline session
 * sending data to the guideline server
 * reading data back out.
 */
public class PCAInterface {
    public static KBHandler kbManager = null;
    public static Client pcaSession  = null;

    public static boolean isInitialized = false;

    public PCAInterface(){
    }

    /*
     * This method should only be called once
     * It setups the PCATest object
     * we first setup the knowledge base
     * then, setup the guideline interpreter
     */
    public static void initialize() {
	pcaSession = new Client();
	pcaSession.getInitParamsFromFile(GlobalVars.initFile);	

	try {
	    if ( GlobalVars.standAlone ) {
		// Create a new knowledge base handler from the URL
		kbManager = new KBHandler(GlobalVars.kbURL);
		System.out.println("Knowledge Base Handler Successfully set up ");
	    } else {
		// try creating it from existing knowledge base in protege
		kbManager = new KBHandler(GlobalVars.kb);
	    }
	}
	catch (Exception e) {
	    System.out.println("error setting up knowledge bse");
	    e.printStackTrace();
	    System.exit(0);
	}

	System.out.println("Using guideline " + GlobalVars.guidelineName);
	pcaSession.setUpGuidelineInterpreter(kbManager, GlobalVars.guidelineName, GlobalVars.database);	
	isInitialized = true;

//	File f = new File(GlobalVars.kbURL);
//	if ( ! f.exists() ) 
//	    throw new RuntimeException("Can't find knowledge base: " + GlobalVars.kbURL );
//	GlobalVars.kbLastModified = f.lastModified();     
    }
  
    /*
     * This method just computes recommendation 
     * Using global pcaSession (this object must be initialized before
     * using this method
     */
    public static String computeRecommendations(PatientDataStore pds) {
	Collection ptDataArray = pds.create_patient_data_array();  
	Patient_Data[] data = (Patient_Data[]) ptDataArray.toArray(new Patient_Data[0]);

	// crap, made up data
	// Patient_Data[] data = pcaSession.setUpPatientData();

	System.out.println("Right before compute and Save, currentPatient: "+ GlobalVars.currentPatient.getName().replace(' ', '_'));
	java.util.Date startTime = new java.util.Date();
	System.out.println("start compute advisory "+(startTime.getTime()));
	String outputFile = pcaSession.computeAndSaveAdvisory(data, GlobalVars.outputdir,
			 kbManager, GlobalVars.currentPatient.getName().replace(' ', '_'));
	java.util.Date finishedTime = new java.util.Date();
	System.out.println("finished compute advisory "+finishedTime.getTime() +" ***Took "+
			(finishedTime.getTime()-startTime.getTime())+" milliseconds");

	// if we get no file back means an error occurred
	if ( outputFile.trim().equals("") ) {
	    throw new RuntimeException("Error creating Recommendation");
	}

	return outputFile;	
    }

    /*
     * Create a Prescription data structure
     */
    public static Prescription_Data newPrescription(String name, Float dose) {
	Prescription_Data prescData = new Prescription_Data();
	// prescData.operation = Data_Operation_Type.modify;
	prescData.operation = Data_Operation_Type.add;
	prescData.drug_name = name;
	prescData.daily_dose = dose.floatValue();
	prescData.daily_dose_unit = "";
	prescData.start_time = "";
	prescData.sig = "";
	return prescData;
    }
    
    public static Prescription_Data newPrescription(String name, Float dose, String start, String end) {
	Prescription_Data prescData = new Prescription_Data();
	// prescData.operation = Data_Operation_Type.modify;
	prescData.operation = Data_Operation_Type.add;
	prescData.drug_name = name;
	prescData.daily_dose = dose.floatValue();
	prescData.daily_dose_unit = "";
	if (start != null) prescData.start_time = start;
	else prescData.start_time = "";
	if (end != null) prescData.stop_time = end;
	else prescData.stop_time = "";
	prescData.sig = "";
	return prescData;
    }

    public static Prescription_Data mprPrescription(String name, Integer mpr, String releaseDate) {
    	Prescription_Data prescData = new Prescription_Data();
    	prescData.operation = Data_Operation_Type.modify;
    	prescData.drug_name = name;
    	prescData.daily_dose = (float)0.0;
    	prescData.daily_dose_unit = "";
    	prescData.medication_possession_ratio = mpr.intValue();
    	prescData.present_release_time = releaseDate;
    	prescData.start_time = "";
    	prescData.sig = "";
    	return prescData;
    }
    
    public static Prescription_Data statusPrescription(String name, String status, String statusDate) {
    	Prescription_Data prescData = new Prescription_Data();
    	prescData.operation = Data_Operation_Type.modify;
    	prescData.drug_name = name;
    	prescData.daily_dose = (float)0.0;
    	prescData.daily_dose_unit = "";
    	prescData.medication_possession_ratio = 0;
    	//prescData.present_release_time = releaseDate;
    	prescData.start_time = "";
    	prescData.sig = status;
    	return prescData;
    }

    public static Note_Entry_Data newNote(String term, String value, Entry_Type e,
    		String first_time, String second_time) {
	Note_Entry_Data neData = new Note_Entry_Data();
	// setup note data structure
	neData.operation = Data_Operation_Type.add;
	neData.domain_term = term;
	neData.entry_type = e;
	neData.value = value;
	if (second_time.equals("")) {
	neData.valid_time = first_time;
	neData.start_time = "";
	} else {
		neData.start_time = first_time;
		neData.valid_time = second_time;
	}
	return neData;

    }

    public static Numeric_Data newNumeric(String field, String val, String date) {
	Numeric_Data nData = new Numeric_Data();
	// setup numeric data structure
	nData.operation = Data_Operation_Type.add;
	nData.domain_term = field;
	nData.value = val;
	nData.unit = "";
	nData.valid_time = date;
	return nData;
    }

    // this method recalls initilization 
    // then runs computeRecommendations
    public static String test_pca(PatientDataStore pds) {			
	String str;
	initialize();
	str = computeRecommendations(pds);
	return str;
    }
}