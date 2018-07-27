package edu.stanford.smi.eon.util;

import java.util.*;
import java.text.*;

import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.widget.*;
import edu.stanford.smi.protege.resource.*;

import edu.stanford.smi.eon.PCAServerModule.*;
import gov.va.test.opioidtesttool.XMLInterface;
import edu.stanford.smi.eon.kbhandler.KBHandler;
import edu.stanford.smi.eon.clients.PCATest;

// Stuff for DOCUMENT
import org.apache.log4j.Logger;
import org.w3c.dom.*;

/*
 * Store all patient data
 * in internal format, for editing
 * modifying, etc.
 */

public class PatientDataStore {
	static Logger logger = Logger.getLogger(PatientDataStore.class);
    private String name;

    public TETableModel medsData = null;
    public TETableModel dxData = null;
    public TETableModel clinicalSignsData = null;
    public TETableModel adrData = null;
    public TETableModel labsData = null;
    public TETableModel demoGraphicsData = null;
    public TETableModel cptData = null;

    // this just stores all TETableModels, makes it easier
    // to loop through all TETableModels
    private final static String KEYS[] = { "meds", "dx", "clinicalSigns", "adr", "labs", "demographics", "cpt" };
    private Hashtable ht  = null;

    public PatientDataStore(String _name) {
	createTables();
	name = new String(_name);
    }

    // Load Patient Data From XML
    public PatientDataStore(Element e) {
	NodeList nl;
	int x;
	TETableModel tmp;
	createTables();

	name = new String(e.getAttribute("name"));

	// load all patient data tables from XML
	for (x=0; x< KEYS.length; x++) {
	    tmp = (TETableModel) ht.get(KEYS[x]);
	    nl = e.getElementsByTagName(KEYS[x]);
	    if ( nl.getLength() > 0 )
		tmp.loadXMLData((Element) nl.item(0));
	}
    }

    // create initial data structures
    void createTables() {
	ht = new Hashtable();

	String[] medsFieldLabels = {"Drug", "Daily Dose"};
	// previous version as follows:
	//medsData = new TETableModel(medsFieldLabels, true, 2, TETableModel.PCA_PRESCRIPTION, null, true);
	
	// current version as follows:
	medsData = new TETableModel(medsFieldLabels, true, 1, TETableModel.PCA_PRESCRIPTION, null, true);
	ht.put(KEYS[0], medsData);

	String[] dxFieldLabels = {"Dx", "Name"};
	dxData = new TETableModel(dxFieldLabels, false, 1, TETableModel.PCA_NOTE, Entry_Type.problem, false);
	ht.put(KEYS[1], dxData);

	String[] clinicalSignsFieldLabels = {"Sign", "Value"};
	clinicalSignsData = new TETableModel(clinicalSignsFieldLabels, false, 1, TETableModel.PCA_NOTE_OR_NUMERIC, null, false);
	ht.put(KEYS[2], clinicalSignsData);

	String[] adrFieldLabels = {"ADR" };
	adrData = new TETableModel(adrFieldLabels, false, 1, TETableModel.PCA_NOTE, Entry_Type.adverse_event, false);
	ht.put(KEYS[3], adrData);

	String[] labsFieldLabels = {"Lab", "Value"};
	// qualitative labs are sent over as note entry with problem type field
        labsData = new TETableModel(labsFieldLabels, false, 1, TETableModel.PCA_NOTE_OR_NUMERIC, Entry_Type.problem, true);
	ht.put(KEYS[4], labsData);

	String[] demoGraphicsFieldLabels = { "Field Name", "Value" };
        demoGraphicsData = new TETableModel(demoGraphicsFieldLabels, false, 0, TETableModel.PCA_NOTE_OR_NUMERIC, Entry_Type.problem, true);
	ht.put(KEYS[5], demoGraphicsData);

	String[] cptFieldLabels = { "Name", "CPT Code" };
        cptData = new TETableModel(cptFieldLabels, true, 0, TETableModel.PCA_NOTE, null, false);
	ht.put(KEYS[6], cptData);
    }


    /*
     * The following method, takes all the data structures
     * returns back a list of Patient_Data that is suitable to pass to the guideline Interpreter
     */
    public Collection create_patient_data_array() {

		Collection patientDataList = new ArrayList();
		int x;
	
		// Convert every TETableModel to PCA Data format
		// add to patient data list
		for (x=0; x< KEYS.length; x++) {
		    patientDataList.addAll( ((TETableModel) ht.get(KEYS[x])).convertToGuidelineData() );
		}
	
		logger.info("PatientDataStore.create_patient_data_array: Size of data list: " + patientDataList.size());
		return patientDataList;
    }

    public Node createXMLNode(Document doc) {
	int level = 1;
	Node n;
	int x;
	TETableModel tmp;

	Element patient = doc.createElement("patient");
	patient.setAttribute("name", getName());
	patient.appendChild(XMLInterface.nl(doc));

	for (x=0; x < KEYS.length; x++) {
	    tmp = (TETableModel) ht.get(KEYS[x]);
	    XMLInterface.addNode(doc, patient,
				 tmp.createXMLNode(doc, KEYS[x], level+1),
				 level);
	}
	return patient;
    }

    // getters
    public String getName() {
	return name;
    }

    public String toString() {
	int x;
	StringBuffer sb = new StringBuffer();
	sb.append("Patient: " + getName() + "\n");
	for (x=0; x< KEYS.length; x++) {
	    logger.error("Checking " + KEYS[x]);
	    sb.append("\n" + KEYS[x]);
	    sb.append("--------------------\n\n");
	    sb.append( ((TETableModel) ht.get(KEYS[x])).toString() );
	}
	return sb.toString();
    }

    /*
     * Method to dump this PatientDataStore to the
     * console
     */
    public void dump() {
      System.out.println("\n Start Data Patient Name: " + name);

      dumpTableData(KEYS[0], medsData);
      dumpTableData(KEYS[1], dxData);
      dumpTableData(KEYS[2], clinicalSignsData);
      dumpTableData(KEYS[3], adrData);
      dumpTableData(KEYS[4], labsData);
      dumpTableData(KEYS[5], demoGraphicsData);
      //dumpTableData(KEYS[6], cptData);

      System.out.println(" End Data Patient Name: " + name);
   }


    /*
     * Method to dump the data from a single table to the
     * console
     */
    public void dumpTableData(String tblKey, TETableModel tbModel) {
      if (tblKey == null || tbModel == null ||
          tbModel.getColumnCount() <= 0 ||
          tbModel.getRowCount() <= 0) {
	    System.out.println(" null table: " + tblKey);
        return;
      }
      System.out.println("\t Table name: " + tblKey);

	  int rows = tbModel.getRowCount();
	  int numDates = tbModel.getNumDates();

	  for (int x=0; x<rows; x++) {
	    String f1 = tbModel.getFirstField(x);
	    String f2 = tbModel.getSecondField(x);
	    String d1 = "", d2 = "";
	    if ( numDates == 0 ) {
		  System.out.println(" \t\t domain: " + f1 + " value: " + f2);
	    }
	    if ( numDates == 1 ) {
		  d1 = HelperFunctions.internalDateFormatter.format(tbModel.getStart(x));
		  System.out.println(" \t\t domain: " + f1 + " value: " + f2 +
		                     " start: " + d1);
	    }
	    if ( numDates == 2 ) {
		  d1 = HelperFunctions.internalDateFormatter.format(tbModel.getStart(x));
		  d2 = HelperFunctions.internalDateFormatter.format(tbModel.getEnd(x));
		  System.out.println(" \t\t domain: " + f1 + " value: " + f2 +
		                     " start: " + d1 + " end: " + d2);
	    }
      }
      System.out.println("\t End table name: " + tblKey);
   }

   public void dumpNumericData(Numeric_Data nd) {
     if (nd == null)
       return;

     System.out.println(" \t\t Numeric data: domain " + nd.domain_term +
                        " unit " + nd.unit + " valid_time " + nd.valid_time +
                        " value " + nd.value);
     return;

   }

   public void dumpPrescriptionData(Prescription_Data dd) {
     if (dd == null)
       return;

     System.out.println(" \t\t Prescription data: drug_name " + dd.drug_name +
                        " daily_dose " + dd.daily_dose + " daily_dose_unit " + dd.daily_dose_unit +
                        " start_time " + dd.start_time + " sig " + dd.sig);
     return;

   }

   public void dumpNoteEntryData(Note_Entry_Data dd) {
     if (dd == null)
       return;

     String entryTypeDesc = null;
     if (dd.entry_type.value() == Entry_Type._adverse_event)
       entryTypeDesc = "ADVERSE_EVENT";
     else if (dd.entry_type.value() == Entry_Type._problem)
       entryTypeDesc = "PROBLEM";
     else
       entryTypeDesc = "UNKNOWN_TYPE";

     System.out.println(" \t\t Note_Entry data: entry_type(desc) " + entryTypeDesc +
                        " domain_term " + dd.domain_term + " value " + dd.value);
     return;

   }

}


/*
    ---
    Saved: 20060721
    ---
    public void dumpTableData(String tblKey, TETableModel tbModel) {
      if (tblKey == null || tbModel == null ||
          tbModel.getColumnCount() <= 0 ||
          tbModel.getRowCount() <= 0) {
	    System.out.println(" null table: " + tblKey);
        return;
      }
      System.out.println("\t Table name: " + tblKey);

      ArrayList ptdList = (ArrayList) tbModel.convertToGuidelineData();
      if (ptdList == null || ptdList.size() <= 0) {
	    System.out.println(" No data in table: " + tblKey);
        return;
      }

      Patient_Data ptd;
      Patient_Data_Type ptdType;
      for (int j=0; j<ptdList.size(); j++) {
        ptd = (Patient_Data) ptdList.get(j);
        ptdType = ptd.discriminator();
        if (ptdType.value() == Patient_Data_Type._numeric_data) {
	      dumpNumericData(ptd.numeric());
	    }
        else if (ptdType.value() == Patient_Data_Type._prescription_data) {
          dumpPrescriptionData(ptd.prescription());
	    }
	    else if (ptdType.value() == Patient_Data_Type._note_entry_data) {
		  dumpNoteEntryData(ptd.note_data());
	    }
	    else
	      System.out.println(" Patient Data Type is illegal, unknown." );
      }
      System.out.println("\t End table name: " + tblKey);
   }

*/