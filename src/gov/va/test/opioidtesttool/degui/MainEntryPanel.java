package gov.va.test.opioidtesttool.degui;

import gov.va.test.opioidtesttool.*;
import java.awt.*;
import java.util.*;
import java.util.Vector;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import javax.swing.tree.*;
import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.model.Project;

/*
 * This Panel is the tab for data entry
 * Stores data in PatientDataStore objects
 */

public class MainEntryPanel extends JPanel {
    private DEPanel medsPanel = null;
    private DEPanel dxPanel = null;
    private DEPanel labsPanel = null;
    private DEPanel adrPanel = null;
    private DEPanel clinicalSignsPanel = null;
    private DEPanel demoGraphicsPanel = null;
    private DEPanel cptPanel = null;
    private DEPanel mprPanel = null;

    // roots for tree in knowledge base
    public static String[] DRUG_ROOT = {"Opioids" , "CYP2D6 Inhibiting Drugs", "Opioid Side Effect Modifying Drugs", "NSAID"};
    public static String[] DX_ROOT = {"Medical_Conditions_Class"};
    public static String[] LABS_ROOT = {"Laboratory_Tests"};
    public static String[] SIGNS_ROOT = {"Patient_Observations"};
    public static String[] DEMOGRAPHICS_ROOT = {"Patient_Demographics"};
    public static String[] ADR_ROOT = DRUG_ROOT;
    public static String[] PROCEDURE_ROOT = {"Education", "nutritional supplements", "Therapeutic_Procedure"};
    
    public MainEntryPanel() {	
	PatientDataStore pds = GlobalVars.currentPatient;

	/*
	 * Layout: Row 1: Demographic Panel 
	 *         Row 2: Clinical Signs & Labs Panel
	 *         Row 3: Drugs & MPR Panels
	 *         Row 4: Diagnosis Panel & Adverse reaaction Panel
	 */
	setLayout(new GridLayout(4,1,5,5));

	// Row 1: dmCPTPanel contains both Demographics and CPT code panel	
	JPanel dmCPTPanel = new JPanel();
	dmCPTPanel.setPreferredSize(new Dimension(700,200));
	dmCPTPanel.setLayout(new GridLayout(1,2,5,5));
	demoGraphicsPanel =  new DEPanel("Demographics",pds.demoGraphicsData, "Patient_Demographics", DEMOGRAPHICS_ROOT);
	dmCPTPanel.add(demoGraphicsPanel);
	cptPanel = new DEPanel("Procedures",pds.cptData, "Medical_Task", PROCEDURE_ROOT);
	dmCPTPanel.add(cptPanel);
	add(dmCPTPanel);
	

	// Row 2: clPanel contains both Clinical Signs and labs sub panels
	JPanel clPanel = new JPanel();
	clPanel.setPreferredSize(new Dimension(700,200));
	clPanel.setLayout(new GridLayout(1,2,5,5));
	clinicalSignsPanel =  new DEPanel("Clinical Signs",pds.clinicalSignsData,"Patient_Observations", SIGNS_ROOT);
	clPanel.add(clinicalSignsPanel);
	labsPanel = new DEPanel("Labs",pds.labsData,"Laboratory_Tests", LABS_ROOT);
	clPanel.add(labsPanel);
	add(clPanel);

	// Row 3: dmPanel contains drugs and mprs 
	JPanel dmPanel = new JPanel();
	dmPanel.setPreferredSize(new Dimension(700,200));
	dmPanel.setLayout(new GridLayout(1,2,5,5));
	medsPanel = new DEPanel("Drugs",pds.medsData,"Medications_Class", DRUG_ROOT);
	dmPanel.add(medsPanel);
	mprPanel = new DEPanel("Medication Possesion Ratio",pds.mprData,"Medications_Class", DRUG_ROOT);
	dmPanel.add(mprPanel);
	add(dmPanel);
       
	// row 4:daPanel contains Dxs and adrs
	JPanel daPanel = new JPanel();
	daPanel.setPreferredSize(new Dimension(700,200));
	daPanel.setLayout(new GridLayout(1,2,5,5));
	dxPanel = new DEPanel("Dx", pds.dxData, "Medical_Conditions_Class", DX_ROOT);
	dxPanel.useTreeToSetSecondField();
	daPanel.add(dxPanel);
	adrPanel = new DEPanel("Adverse Reactions",pds.adrData,"Medications_Class", DRUG_ROOT);
	daPanel.add(adrPanel);
	add(daPanel);
	

	System.err.println("Clinical Signs panel incomplete");
	System.err.println("ADR panel incomplete");
	System.err.println("LABS panel incomplete");

	setPreferredSize(new Dimension(700,600));
    }   

    public void setCurrentPatient(PatientDataStore p) {
	medsPanel.setCurrentDataModel(p.medsData);
	dxPanel.setCurrentDataModel(p.dxData);
	labsPanel.setCurrentDataModel(p.labsData);
	adrPanel.setCurrentDataModel(p.adrData);
	clinicalSignsPanel.setCurrentDataModel(p.clinicalSignsData);
	demoGraphicsPanel.setCurrentDataModel(p.demoGraphicsData);
	cptPanel.setCurrentDataModel(p.cptData);	
	mprPanel.setCurrentDataModel(p.mprData);
    }
}
