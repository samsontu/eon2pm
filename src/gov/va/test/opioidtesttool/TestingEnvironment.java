package gov.va.test.opioidtesttool;

import gov.va.test.opioidtesttool.degui.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import java.io.*;

import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.widget.*;
import edu.stanford.smi.protege.resource.*;
import edu.stanford.smi.eon.PCAServerModule.*;
import edu.stanford.smi.eon.criterion.Date_Expression;
import edu.stanford.smi.eon.kbhandler.KBHandler;

import org.apache.log4j.*;

/*
 * Contains main()
 * as well as stuff to create Protege tab
 */
public class TestingEnvironment extends AbstractTabWidget {
	private TestingPanel tp = null;

	static  Logger  logger = Logger.getLogger(TestingEnvironment.class);
	// startup code
	public void initialize() {
		// Get Init File from environment
		String str = System.getProperty("TestEnvironmentINI");
		if ( str == null ) {
			System.err.println("NO TESTING ENVIRONMENT INI FILE SET!@%$!@$!");
			return;
		}
		GlobalVars.initFile = new String(str); 
		System.out.println("Initializing TestingEnvironment");
		loadInitFile();
		loadPatientData();
		GlobalVars.standAlone = false;

		// initialize the tab label
		setLabel("Guideline Test Environment");
		setIcon(Icons.getInstanceIcon());

		//setup globval vars thingie
		GlobalVars.kb = getKnowledgeBase();

		// add the components to the tab widget
		tp = new TestingPanel();
		// mainTestPanel = new JScrollPane(tp);
		setLayout(new FlowLayout());
		add(tp);
		revalidate();
	}

	/*
	 * Protege calls revalidate whenever we switch to
	 * the particular tab
	 * This allows us to get the current screen size,
	 * and resize other panels appropriately
	 */
	public void revalidate() {
		int subtract = 50;
		int w = getWidth() - subtract;
		int h = getHeight() - subtract;
		// System.out.println("Called revalidate: w = " + w +  "h = " + h);
		if ( tp != null )
			tp.changeSize(w,h);
		super.revalidate();
	}


	public static boolean isSuitable(Project project, Collection errors) {
		boolean isSuitable = true;
		return isSuitable;
	}

	public static void loadInitFile() {
		System.out.println("Initfile: " + GlobalVars.initFile);
		// Load init file
		GlobalVars.settings = new Properties();
		PropertyConfigurator.configure(GlobalVars.initFile);

		try {
			FileInputStream sf = new FileInputStream(GlobalVars.initFile);
			GlobalVars.settings.load(sf);
		} catch (Exception ex) {
			System.out.println("Exception during loading initialization file");
			System.out.println(ex.toString());
			System.exit(2);
		}

		String eonhome = System.getProperty("EON_HOME");
		if ( eonhome == null ) {
			logger.error("NO EON_HOME SET!@%$!@$!");
		}

		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
				"yyyy-MM-dd");
		String currentTime = formatter.format(new java.util.Date());

		Properties p = GlobalVars.settings;
		GlobalVars.kbURL = p.getProperty("SERVERKB", "");
		if (edu.stanford.smi.eon.util.DirectoryHandler.isAbsolutePath(p.getProperty("PCAOUTPUTDIR", "")))    
			GlobalVars.outputdir = p.getProperty("PCAOUTPUTDIR", "");
		else if (eonhome != null)
			GlobalVars.outputdir = edu.stanford.smi.eon.util.DirectoryHandler.combinePath(eonhome, p.getProperty("PCAOUTPUTDIR", ""));
		else {
			logger.error("Cannot construct path to output directory: EON_HOME is null");
			return;			
		}
		GlobalVars.guidelineName = p.getProperty("GUIDELINEID", "");
		GlobalVars.database = p.getProperty("DATABASE", "");
		System.out.println("Data path: "+p.getProperty("XMLDATAFILE", ""));
		if (edu.stanford.smi.eon.util.DirectoryHandler.isAbsolutePath(p.getProperty("XMLDATAFILE", "")))    
			GlobalVars.XMLDataFile = p.getProperty("XMLDATAFILE", "");
		else if (eonhome != null)
			GlobalVars.XMLDataFile = edu.stanford.smi.eon.util.DirectoryHandler.combinePath(eonhome, p.getProperty("XMLDATAFILE", ""));
		else {
			logger.error("Cannot construct path to data file: EON_HOME is null");
			return;
		}
		System.out.println("XMLDataFile: "+GlobalVars.XMLDataFile);
		GlobalVars.sessionTime=p.getProperty("SESSIONTIME", currentTime);

		// Check for setting knowledge base entry points into
		// different data entry panels
		if ( p.containsKey("DRUG_ROOT") ) {
			String tmp = new String(p.getProperty("DRUG_ROOT"));
			MainEntryPanel.DRUG_ROOT = tmp.split(",");
		}
		if ( p.containsKey("DX_ROOT") ) {
			String tmp = new String(p.getProperty("DX_ROOT"));
			MainEntryPanel.DX_ROOT = tmp.split(",");
		}
		if ( p.containsKey("LABS_ROOT") ) {
			String tmp = new String(p.getProperty("LABS_ROOT"));
			MainEntryPanel.LABS_ROOT = tmp.split(",");
		}
		if ( p.containsKey("SIGNS_ROOT") ) {
			String tmp = new String(p.getProperty("SIGNS_ROOT"));
			MainEntryPanel.SIGNS_ROOT = tmp.split(",");
		}
		if ( p.containsKey("DEMOGRAPHICS_ROOT") ) {
			String tmp = new String(p.getProperty("DEMOGRAPHICS_ROOT"));
			MainEntryPanel.DEMOGRAPHICS_ROOT = tmp.split(",");
		}
		if ( p.containsKey("ADR_ROOT") ) {
			String tmp = new String(p.getProperty("ADR_ROOT"));
			MainEntryPanel.ADR_ROOT = tmp.split(",");
		}

		/*
		 * Data structure typing
		 * We specify default data structures to send to the
		 * the guideline interpreter by default
		 * Here we load any over-rides -- that depend on
		 * domain term
		 * Start with DATA_TYPE_OVERRIDE_1 , continue as long as DATA_TYPE_OVERRIDE_N exists.
		 */
		int num = 1;
		GlobalVars.dtor = new DataTypeOverRides();
		String key_nam = "DATA_TYPE_OVERRIDE_" + num;
		while ( p.containsKey(key_nam) ) {
			String tmp = new String(p.getProperty(key_nam));
			GlobalVars.dtor.add(tmp);
			num++;
			key_nam = new String("DATA_TYPE_OVERRIDE_" + num);
		}
	}



	public static void loadPatientData() {
		GlobalVars.pds = new ArrayList();

		// load data from xml
		XMLInterface.read();

		// no data in xml file
		if ( GlobalVars.pds.isEmpty() ) {
			System.err.println("NO XML DATA FILE -- loading default patient");
			GlobalVars.pds.add(new PatientDataStore("John Doe"));
		}

		// Set current patient to first PatientData from DataStore
		Iterator pdsIt = GlobalVars.pds.iterator();
		PatientDataStore pds = (PatientDataStore) pdsIt.next();
		GlobalVars.currentPatient = pds;
	}

	/*
	 * Not really maintained code
	 * We only run in Protege mode now-a-days
	 * ONLY useful for debuggin
	 */
	public static void main(String[] args) {

		if ( args.length < 1 ) {
			System.out.println("Testing Environment needs initialization file as argument");
			return;
		}
		// Set everything up
		GlobalVars.initFile =  new String(args[0]);
		loadInitFile();
		loadPatientData();
		GlobalVars.standAlone = true;
		// we now initialize PCA interface right before computing
		// recommendations, this allows us to see if we updated
		// the knowledge base and reload the new one
		// PCAInterface.initialize();

		Collection error_messages = new Vector();

		System.out.println("Loading project " + GlobalVars.kbURL);
		GlobalVars.project = new Project(GlobalVars.kbURL, error_messages);
		GlobalVars.kb = GlobalVars.project.getKnowledgeBase();

		TestingPanel tp = new TestingPanel();
		JFrame frame = new JFrame();

		frame.setSize(250,250);
		frame.getContentPane().add(tp, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
