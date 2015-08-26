package edu.stanford.smi.eon.clients;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import edu.stanford.smi.eon.PCAServerModule.PCA_Initialization_Exception;
import edu.stanford.smi.eon.execEngine.EONConstants;
import edu.stanford.smi.eon.execEngine.IDataSource;
import edu.stanford.smi.eon.execEngine.IDataSourceFactory;
import edu.stanford.smi.eon.execEngine.IEON;
import edu.stanford.smi.eon.execEngine.IEonFactory;
import edu.stanford.smi.eon.siteCustomization.BMIRDataSourceFactory;
import edu.stanford.smi.eon.siteCustomization.BMIREonFactory;
import edu.stanford.smi.eon.util.HelperFunctions;
import gov.va.athena.advisory.Advisory;
import gov.va.athena.advisory.Criterion_Evaluation;
import gov.va.athena.advisory.Missing_Data;

public class TestEONExecEngineAPIForInpatient {

	/**
	 * @param args
	 */
	static Logger logger = LogManager.getLogger(ComputeOutPatientPerformanceMeasure.class.getName());

	public static void main(String[] argv) {
		// guidelineID are set externally (could be part 
		// of args in this example) by the choice of
		// patient and which performance measure to address.


		if (argv.length < 3) {
			logger.error("ComputePerformanceMeasure needs path to initFile and performance start and stop times as arguments");
			return;
		}
		String initFileFullPath = argv[0];
		String PMStartTime = argv[1];
		String PMStopTime = argv[2];

		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.WARN);

		PropertyConfigurator.configure(argv[0]);

		logger.info("Input parameters: "+ initFileFullPath+" "+ PMStartTime +" "+ PMStopTime); 

		Properties settings = new Properties();
		try {
			FileInputStream sf = new FileInputStream(initFileFullPath);
			settings.load(sf);
		} catch (Exception ex) {
			logger.error("Exception during loading initialization file");
			logger.error(ex.toString());
		}
		String dataFilePath = settings.getProperty("DBCONFIG", "");
		String guidelineName  = settings.getProperty("GUIDELINENAME", "");
		String context =settings.getProperty("CONTEXT", "");
		String protegeProjFullPath =  settings.getProperty("PMKB", "");
		String outputdir = settings.getProperty("OUTPUTDIR", "C:/Dropbox/");

		//assume that args[0] is the initFileFullPath, and this contain 
		//information about the context, which could either 
		//be BMIR (for Stanford Center for Biomedical Informatics Research) or VA.
		IEON aEON = null;
		IDataSource aDS = null;
		// read context from initFileFullPath
		if (context.equals("BMIR")) {

			IDataSourceFactory aDSFactory = new BMIRDataSourceFactory();
			try {
				aDS = aDSFactory.createDataSource(dataFilePath);
			} catch (Exception e1) {
				logger.error("Can't create data source ");
				e1.printStackTrace();
				System.exit(0);
			}

			IEonFactory aEonFactory = new BMIREonFactory();
			try {
				aEON = aEonFactory.createEON(protegeProjFullPath);
			} catch (PCA_Initialization_Exception e) {
				logger.error("Can't create EON ");
				e.printStackTrace();
			}     
			// Separate aEON and aDS for the possibility that data will be coming from API. Alternatively, call createEON(protegeProjFullPath, aDS)
			try {
				aEON.setDataSource(aDS);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Cannot set up data source. Quit.");
				System.exit(0);
			}
		}
		else {
			// No context. simple exit here. 
			System.out.println("Configuration not equal to BMIR. Quit.");
			System.exit(0);

		}
		// If we had the VA classes and jars, we could include them here
		// else if (context == VA) {
		//   IDataSourceFactory aDSFactory = new VaDataSourceFactory();
		//   IDataSource aDS = aDSFactory.createDataSource(initFileFullPath);
		//
		//   // obtain ProtegeProjFullPath from initFile
		//   String protegeProjFullPath = ...
		//
		//   IEonFactory aEonFactory = new VaEonFactory();
		//   aEON = aEonFactory.createEON(protegeProjFullPath); 
		//   aEON.setDataSource(aDS);     



		// Here we have aEon available and ready, with its DataSource
		// also determined.

		//		List<String> patients = new ArrayList<String>();
		//		patients.add("1316243");


		List<String> sessionTimes = new ArrayList<String>();
		sessionTimes.add("1979-06-05");
		//sessionTimes.add("1979-04-16");
		//sessionTimes.add("1979-07-13");
		List<String> hospitalizationIDs = new ArrayList<String>();
		hospitalizationIDs.add("12345");
		//hospitalizationIDs.add("12346");
		//hospitalizationIDs.add("12347");

		PrintWriter out;
		try {
			String fileName = outputdir+"inPatientResult.html";
			File outputFile = new File(fileName);;
			if (outputFile.exists()) {
				outputFile.delete();
			}
			out = new PrintWriter(fileName);
			printHeader(out, EONConstants.HTML);
			for (String sessionTime : sessionTimes) {
				for (String hospitalizationID : hospitalizationIDs) {
					out.println("<h1>Session Time/Hospitalization ID: "+sessionTime+"("+ (int) HelperFunctions.Day2Int2(sessionTime)+")/"+hospitalizationID+"</h1>");
					Advisory pmOutput = aEON.computePerformanceMeasuresAdvisory("1316243", hospitalizationID, sessionTime, 
							guidelineName, "NQF 081 Inpatient ACE", PMStartTime, PMStopTime);
					out.print(aEON.printData(IEON.HTML));
					if (pmOutput != null) {
						out.print(aEON.printAdvisory(pmOutput, IEON.HTML));
					}
					else {
						logger.error("No output!");
						out.println("<p>No output</p>");
					}
					aEON.finishSession();
				}
			} 
			printFooter(out, EONConstants.HTML);
			out.flush();
			out.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
	}
	
//	for (String sessionTime : sessionTimes) {
//		for (String hospitalizationID : hospitalizationIDs) {
//			try {
//				Advisory pmOutput = aEON.computePerformanceMeasuresAdvisory("1316243", hospitalizationID, sessionTime, 
//						guidelineName, null, PMStartTime, PMStopTime);
//				if (pmOutput != null)
//					displayPMOutput(pmOutput);
//				else
//					logger.error("No output!");
//				aEON.finishSession();
//			} catch (Exception e1) {
//				e1.printStackTrace();
//			} 
//		}
//	}

	

	private static void printFooter(PrintWriter writer, String html) {
		writer.println("<html><head></head><body>");
		
	}


	private static void printHeader(PrintWriter writer, String html) {
		writer.println("</body></html>");
		
	}


	


	private static void displayPMOutput(Advisory pmOutput) {
		System.out.println("\nCase ID: "+pmOutput.getCase_id());
		System.out.println("Advisory time: "+ pmOutput.getAdvisory_time());
		System.out.println("Start time: "+ pmOutput.getStart_time());
		System.out.println("Stop time: "+ pmOutput.getStop_time());
		System.out.println("GuidelineID: "+ pmOutput.getGuideline_id());
		System.out.println("_____________________");
		if (pmOutput.getEvaluated_goal() != null) {
			for (gov.va.athena.advisory.Guideline_Goal pm : pmOutput.getEvaluated_goal()) {
				System.out.println("Performance measure:   "+pm.getKb_goal_id());
				if (pm.getCriterion_Evaluation() != null){
					for (Criterion_Evaluation eval : pm.getCriterion_Evaluation()) {
						System.out.println("Criterion type: "+eval.getCriterion_type());
						System.out.println("Criterion name: "+eval.getCriterion_id());
						System.out.println("Evaluation result:   "+eval.getCriterion_evaluation_result());
						System.out.println("Data:                "+eval.getData());
						Collection<Missing_Data> missingData = eval.getMissing_Data();
						if ((missingData != null) && !missingData.isEmpty()) {
							for (Missing_Data md: missingData) {
								System.out.println("	Missing data criterion:  "+md.getCriterion_id());
								System.out.println("	Missing data parameter:  "+md.getParameter());
								System.out.println("	Missing data result:  "+md.getCriterion_evaluation_result());
							}
							
						}
						System.out.println("------------------------");
					}
				}
				System.out.println("Satisfy numerator criteria:    "+pm.getAchieved());
				System.out.println("*******************************");
				System.out.println("*******************************");
			}
		}

	}
}
