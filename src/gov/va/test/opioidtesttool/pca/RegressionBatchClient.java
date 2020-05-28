package gov.va.test.opioidtesttool.pca;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import edu.stanford.smi.eon.PCAServerModule.Guideline_Service_Record;
import edu.stanford.smi.eon.PCAServerModule.Improper_Data_Exception;
import edu.stanford.smi.eon.PCAServerModule.PCAServer_i;
import edu.stanford.smi.eon.PCAServerModule.PCASession;
import edu.stanford.smi.eon.PCAServerModule.PCA_Initialization_Exception;
import edu.stanford.smi.eon.PCAServerModule.PCA_Session_Exception;
import edu.stanford.smi.eon.PCAServerModule.Patient_Data;
import edu.stanford.smi.eon.clients.ClientUtil;
import edu.stanford.smi.eon.execEngine.IEON;
import edu.stanford.smi.eon.inputoutput.AdvisoryFormater;
import edu.stanford.smi.eon.kbhandler.KBHandler;
import edu.stanford.smi.eon.util.HelperFunctions;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.KnowledgeBase;
import gov.va.athena.advisory.Advisory;
import gov.va.test.opioidtesttool.*;
import gov.va.test.opioidtesttool.degui.MainEntryPanel;


/***
We assume that we have a testResult subdirectory in ATHENA_TestEnvironment that 
has two subdirectories working/ and archive/.

We add  regression_batch.bat/.ini/.xml  files in in the batch/ini/data 
directories respectively.  In the ATHENASharedKB folder we have a 
ATHENA_CVD.pprj/pont/pins KB that includes the 5 cardiovascular/diabetes ATHENA KBs.

The ini file should specify the KB to be used, a shorthand label for the KB, and
the session time that's shared by all cases in an xml input file. 

Output is written to the working directory and an archive subdirectory indexed by
the date when the batch program is written.

An "individual" output html file contains a listing of the data available to the EON execution engine, 
the advisories, based on a collection of encoded guidelines, for a single patient case. 
If "JohnDoe" is the name of the patient case as specified in the XML file, then the
output html page should have the name "inputxmlfile-KBshorthand-JohnDoe.html", where "inputxmlfile" 
is the name of the datafile from which the advisories are generated and KBshorthand is 
the KB shorthand.

In addition to "individual" output html files, the system will generate a concatenated
version named "inputxmlfile.html" that contains all of the output generated from
an input xml file.

For example, on 2012/09/04, whenever you run regression_batch.bat using "xmlinput.xml"
as the input file, the system generates and overwrite 20120904/JohnDoe.html, 
20120904/xmlinput.html and working/JohnDoe-new.html, working/xmlinput-new.html. 
The files in the 20120904/ and working/ folders have exactly the same content.

Batch results can be generated for different combinations of guideline KBs by changing


This way you always have the archive result for the day and know which file (-new) is 
the most recent.


ATHENA_TestEnvironment/
  batch/regression_batch.bat
  ini/regression_batch.ini
  data/regression_batch.xml (data for JohnDoe, JaneDoe, ...)
  testresult/
      working/
          inputxmlfile-CVD-JohnDoe-new.html
          inputxmlfile-CVD-JaneDoe-new.html
          -CVD-regression_batch.html
      archive/
          20120904/
          	inputxmlfile-CVD-JohnDoe.html
          	inputxmlfile-CVD-JaneDoe.html
          	CVD-regression_batch.html
          20120903/
          	inputxmlfile-CVD-JohnDoe.html
          	inputxmlfile-CVD-JaneDoe.html
          	CVD-regression_batch.html

The batch program takes one arguments (initialization file).

 */

public class RegressionBatchClient {

	static Logger logger = Logger.getLogger(RegressionBatchClient.class);

	public static void initialize(String initFile, String kbURLString) {
		// Get Init File from environment
		GlobalVars.initFile = initFile;
		System.out.println("Initializing RegressionBatchClient");
		loadInitFile(kbURLString);
		loadPatientData();
		GlobalVars.standAlone = true;

	}

	public static void loadInitFile(String kbURLString) {
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

		/*
		 * String outdir = System.getProperty("OutputDir"); if ( outdir == null ) {
		 * System.err.println("NO OUTPUT DIRECTORY SET!@%$!@$!"); return; }
		 * outdir = outdir.replace( '\\', '/'); GlobalVars.outputdir = new
		 * String(outdir); System.out.println("Output directory: "+
		 * GlobalVars.outputdir);
		 */

		//get current date time with Date()
		Date date = new Date();

		Properties p = GlobalVars.settings;
		GlobalVars.sessionTime = p.getProperty("SESSIONTIME", HelperFunctions.internalDateFormatter.format(date));
		GlobalVars.kbLabel = p.getProperty("KBLABEL", "");
		GlobalVars.guidelineName = p.getProperty("GUIDELINEID", "");
		GlobalVars.runtimeDate = HelperFunctions.internalDateFormatter.format(date);
		String cumulateDoseStr = p.getProperty("CUMULATEDOSE");
		if ((cumulateDoseStr == null) || cumulateDoseStr.equals(""))
			GlobalVars.cumulateDose = true;
		else
			GlobalVars.cumulateDose=cumulateDoseStr.equals("TRUE");
		try {
			GlobalVars.XMLDataFile = getPath(p.getProperty("XMLDATAFILE", ""));
			GlobalVars.kbURL = getPath(p.getProperty("KBPATH", kbURLString));
			GlobalVars.outputdir = getPath(p.getProperty("WORKINGDIR", ""));
			GlobalVars.archivedir = getPath(p.getProperty("ARCHIVEDIR"));
		} catch (Exception e) {
			logger.error("Cannot construct path");
			System.exit(-1);
		}


		// Check for setting knowledge base entry points into
		// different data entry panels
		if (p.containsKey("DRUG_ROOT")) {
			String tmp = new String(p.getProperty("DRUG_ROOT"));
			MainEntryPanel.DRUG_ROOT = tmp.split(",");
		}
		if (p.containsKey("DX_ROOT")) {
			String tmp = new String(p.getProperty("DX_ROOT"));
			MainEntryPanel.DX_ROOT = tmp.split(",");
		}
		if (p.containsKey("LABS_ROOT")) {
			String tmp = new String(p.getProperty("LABS_ROOT"));
			MainEntryPanel.LABS_ROOT = tmp.split(",");
		}
		if (p.containsKey("SIGNS_ROOT")) {
			String tmp = new String(p.getProperty("SIGNS_ROOT"));
			MainEntryPanel.SIGNS_ROOT = tmp.split(",");
		}
		if (p.containsKey("DEMOGRAPHICS_ROOT")) {
			String tmp = new String(p.getProperty("DEMOGRAPHICS_ROOT"));
			MainEntryPanel.DEMOGRAPHICS_ROOT = tmp.split(",");
		}
		if (p.containsKey("ADR_ROOT")) {
			String tmp = new String(p.getProperty("ADR_ROOT"));
			MainEntryPanel.ADR_ROOT = tmp.split(",");
		}

		/*
		 * Data structure typing We specify default data structures to send to
		 * the the guideline interpreter by default Here we load any over-rides --
		 * that depend on domain term Start with DATA_TYPE_OVERRIDE_1 , continue
		 * as long as DATA_TYPE_OVERRIDE_N exists.
		 */
		int num = 1;
		GlobalVars.dtor = new DataTypeOverRides();
		String key_nam = "DATA_TYPE_OVERRIDE_" + num;
		while (p.containsKey(key_nam)) {
			String tmp = new String(p.getProperty(key_nam));
			GlobalVars.dtor.add(tmp);
			num++;
			key_nam = new String("DATA_TYPE_OVERRIDE_" + num);
		}
	}

	private static String getPath(String path) throws Exception{
		String eonhome = System.getProperty("EON_HOME");
		if ( eonhome == null ) {
			logger.warn("NO EON_HOME SET!@%$!@$!");
		}
		if (edu.stanford.smi.eon.util.DirectoryHandler.isAbsolutePath(path))    
			return path;
		else if (eonhome != null)
			return edu.stanford.smi.eon.util.DirectoryHandler.combinePath(eonhome, path);
		else {
			logger.error("Cannot construct path to "+path+"; EON_HOME is null");
			throw new Exception("Cannot construct path to "+path+"; EON_HOME is null");			
		}
	}

	private static String makeFileNameStem(String caseID) {
		String inputFileName = new File(GlobalVars.XMLDataFile).getName();
		return inputFileName.substring(0, inputFileName.lastIndexOf('.'))+"-"+ 
		GlobalVars.kbLabel+"-"+caseID.replaceAll("\\W", "");
	}

	private static String makeConcatFileNameStem() {
		String inputFileName = new File(GlobalVars.XMLDataFile).getName();
		return inputFileName.substring(0, inputFileName.lastIndexOf('.'))+"-"+ 
		GlobalVars.kbLabel;
	}

	public static void loadPatientData() {
		GlobalVars.pds = new ArrayList();

		// load data from xml
		XMLInterface.read();

		// no data in xml file
		if (GlobalVars.pds.isEmpty()) {
			logger.error("NO XML DATA FILE -- loading default patient");
			GlobalVars.pds.add(new PatientDataStore("JohnDoe"));
		}

		// Set current patient to first PatientData from DataStore
		Iterator pdsIt = GlobalVars.pds.iterator();
		PatientDataStore pds = (PatientDataStore) pdsIt.next();
		GlobalVars.currentPatient = pds;
	}


	private static void writeToFile(String outputdir, String fileNameStem,
			String htmlOutput, String caseID) {
		File outputFile = new File(outputdir+GlobalVars.DirDelimiter+fileNameStem + ".html");
		File oldFile = new File(outputdir + GlobalVars.DirDelimiter+fileNameStem + "~.html");
		// Check to see if file already exists
		if (outputFile.exists()) {
			if (oldFile.exists()) {
				oldFile.delete();
			}
			if (!outputFile.renameTo(oldFile)) {
				logger.error(oldFile
						+ " problem saving old files. Will be overwritten.");
				outputFile.delete();
			}
		}
		PrintWriter itsWriter;
		try {
			itsWriter = new PrintWriter(new FileWriter(outputFile), true);
			ClientUtil.printHTMLHeader(itsWriter);
			itsWriter.print(htmlOutput);
			ClientUtil.printFooter(itsWriter);
			itsWriter.close();
		} catch (IOException e) {
			logger.error("Problem writing html output to "+ outputdir + 
					GlobalVars.DirDelimiter+fileNameStem);
			e.printStackTrace();
		}

	}

	private static void createDirectoryIfNeeded(String directoryName)
	{
		File theDir = new File(directoryName);

		// if the directory does not exist, create it
		if (!theDir.exists())
		{
			logger.info("creating directory: " + directoryName);
			theDir.mkdir();
		}
	}

	private static String generateOutput(String patientId, String caseData, 
			Guideline_Service_Record[] dssOutput, KnowledgeBase kb, String additionalOutput) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintWriter itsWriter = null;
		itsWriter = new PrintWriter(outputStream, true);
		ClientUtil.printCase(itsWriter, patientId);
		itsWriter.print(caseData);
		if (dssOutput != null) {
			for (int j = 0; j < dssOutput.length; j++) {
				itsWriter.println("<h2 align=\"center\">"+ dssOutput[j].guidelineName+"</h2>");
				ClientUtil.showResultWithKB(dssOutput[j], itsWriter, kb);
			}
			if (additionalOutput != null) 
				itsWriter.print(additionalOutput);
			itsWriter.close();
		}
		return outputStream.toString();
	}

	private static void showResultWithTitle(Guideline_Service_Record[] dssOutput,
			PrintWriter itsWriter, KnowledgeBase kb) {
		itsWriter.println("<h1 align=\"center\">"+GlobalVars.guidelineName+"</h1>");
	}


	public static void main(String[] args) {

		String caseData = null;
		Guideline_Service_Record[] dssOutput = null;
		String kbURLString = null;
		PCAServer_i PCAImp =  new PCAServer_i();;
		if (args.length < 1) {
			System.out
			.println("BatchClient needs at an initialization file as argument");
			return;
		}

		// Set everything up

		GlobalVars.initFile = new String(args[0]);
		if (args.length == 2)
			kbURLString = new String(args[1]);
		initialize(GlobalVars.initFile, kbURLString);
		Collection error_messages = new Vector();

		logger.info("Loading project " + GlobalVars.kbURL);
		try {
			PCAImp.kbManager = new KBHandler(GlobalVars.kbURL);
		} catch (PCA_Initialization_Exception e2) {
			e2.printStackTrace();
		}
		//GlobalVars.project = new Project(GlobalVars.kbURL, error_messages);
		GlobalVars.kb = PCAImp.kbManager.getKB();
		PCAImp.kbURLString = GlobalVars.kbURL;
		String concatenatedResult = null;
		PCASession pcaSession = null;
		try {
			pcaSession = PCAImp.open_pca_session();
			pcaSession.setGuideline(GlobalVars.guidelineName );
			pcaSession.setCumulativeFlag(GlobalVars.cumulateDose);
		} catch (Exception e) {
			System.out.println("The execution engine has not been set up property");
			e.printStackTrace();
			System.exit(0);
		}
		for (PatientDataStore p: (GlobalVars.pds)) {
			String patientId = p.getName();
			String advioryHTML = null;
			try {
				Collection<Patient_Data> ptDataArray = p.create_patient_data_array();
				Patient_Data[] data = (Patient_Data[]) ptDataArray.toArray(new Patient_Data[0]);
				System.out.println("***************Patient : "+patientId+"*******************");
				for (Instance med : GlobalVars.kb.getCls("Medication").getInstances()) {
					logger.debug("Before reset advisory and set case med: "+med.getOwnSlotValue(GlobalVars.kb.getSlot("drug_name"))+ 
							" dose: "+med.getOwnSlotValue(GlobalVars.kb.getSlot("daily_dose")));
				}
				pcaSession.resetAdvisories();
				//System.out.println("****** Frame count: *****" + GlobalVars.kb.getFrameCount());
				pcaSession.setCase(patientId, GlobalVars.sessionTime);
				for (Instance med : GlobalVars.kb.getCls("Medication").getInstances()) {
					logger.debug("After reset advisory and set case med: "+med.getOwnSlotValue(GlobalVars.kb.getSlot("drug_name"))+ 
							" dose: "+med.getOwnSlotValueCount(GlobalVars.kb.getSlot("daily_dose")));
				}
				pcaSession.updateData(data);
				for (Instance med : GlobalVars.kb.getCls("Medication").getInstances()) {
					logger.debug("After update data med: "+med.getOwnSlotValue(GlobalVars.kb.getSlot("drug_name"))+ 
							" dose: "+med.getOwnSlotValue(GlobalVars.kb.getSlot("daily_dose")));
				}
				dssOutput = pcaSession.computeAdvisories(patientId, 
						GlobalVars.sessionTime, GlobalVars.guidelineName, true);
				//Adding translation to ATHENA Advisory format for testing purpose
				Collection<Advisory> advisories = AdvisoryFormater.javaTransform(dssOutput, patientId, null, GlobalVars.guidelineName, GlobalVars.sessionTime, null, null, GlobalVars.kb);
				for (Advisory advisory: advisories)
					advioryHTML = advioryHTML+pcaSession.printAdvisory(advisory, IEON.HTML)+"<br><br>";
				caseData = pcaSession.printData();
				String htmlOutput = generateOutput(patientId, caseData, dssOutput, GlobalVars.kb, advioryHTML);	
				concatenatedResult = concatenatedResult + htmlOutput ;
				String fileName = makeFileNameStem(patientId);
				writeToFile(GlobalVars.outputdir, fileName+"-new", htmlOutput, patientId);
				String archiveDirName = GlobalVars.archivedir+GlobalVars.DirDelimiter+ 
						GlobalVars.runtimeDate;
				createDirectoryIfNeeded(archiveDirName);
				writeToFile(archiveDirName, fileName, htmlOutput, patientId);
			} catch (PCA_Initialization_Exception e1) {
				logger.error("PCA_Initialization_Exception for case "+patientId+". Output not generated for the case.");
				e1.printStackTrace();
			} catch (PCA_Session_Exception e) {
				logger.error("PCA_Session_Exception  for case "+patientId+". Output not generated for the case.");
				e.printStackTrace();
			} catch (Improper_Data_Exception e) {
				logger.error("Improper_Data_Exception  for case "+patientId+". Output not generated for the case.");
				e.printStackTrace();
			}
		} // while
		writeToFile(GlobalVars.outputdir, makeConcatFileNameStem(), concatenatedResult, null);
		writeToFile(GlobalVars.archivedir+GlobalVars.DirDelimiter+GlobalVars.runtimeDate, 
				makeConcatFileNameStem(), concatenatedResult, null);
	}

}
