package edu.stanford.smi.eon.clients;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import edu.stanford.smi.eon.PCAServerModule.Compliance_Level;
import edu.stanford.smi.eon.PCAServerModule.Criteria_Evaluation;
import edu.stanford.smi.eon.PCAServerModule.Guideline_Service_Record;
import edu.stanford.smi.eon.PCAServerModule.PCA_Initialization_Exception;
import edu.stanford.smi.eon.PCAServerModule.PCA_Session_Exception;
import edu.stanford.smi.eon.data.Data;
import edu.stanford.smi.eon.data.DataEntry;
import edu.stanford.smi.eon.data.Medication;
import edu.stanford.smi.eon.execEngine.EONConstants;
import edu.stanford.smi.eon.execEngine.IDataSource;
import edu.stanford.smi.eon.execEngine.IDataSourceFactory;
import edu.stanford.smi.eon.execEngine.IEON;
import edu.stanford.smi.eon.execEngine.IEonFactory;
import edu.stanford.smi.eon.siteCustomization.BMIRDataSourceFactory;
import edu.stanford.smi.eon.siteCustomization.BMIREonFactory;
import gov.va.athena.advisory.Advisory;
import gov.va.athena.advisory.Criterion_Evaluation;
import gov.va.athena.advisory.Missing_Data;

public class ATHENA_Advisory {

	/**
	 * @param args
	 */
	static Logger logger = LogManager.getLogger(ComputeOutPatientPerformanceMeasure.class.getName());

	public static void main(String[] argv) {
		// In this example, path to init file, session time, and performance period start/stop times are given as arguments
		// The init file specifies data init file, guideline name, context, and path to performance measure KB.
		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.WARN);

		if (argv.length != 2) {
			logger.error("ATHENA Advisory needs (1) path to init file, (2) session time");
			return;
		}
		String initFileFullPath = argv[0];
		String sessionTime = argv[1];
		logger.info("Input parameters: "+ initFileFullPath+" "+sessionTime); 

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
		IEON aEON1 = null;
		IEON aEON2 = null;
		String guidelineName2 = "ATHENA DM: Glycemic Control";
		IDataSource aDS1 = null;
		IDataSource aDS2 = null;
		// read context from initFileFullPath
		if (context.equals("BMIR")) {

			IDataSourceFactory aDSFactory = new BMIRDataSourceFactory();
			try {
				aDS1 = aDSFactory.createDataSource(dataFilePath);
				aDS2 = aDSFactory.createDataSource(dataFilePath);
			} catch (Exception e1) {
				logger.error("Can't create data source ");
				e1.printStackTrace();
				System.exit(0);
			}

			IEonFactory aEonFactory = new BMIREonFactory();
			try {
				aEON1 = aEonFactory.createEON(protegeProjFullPath);
				aEON2 = aEonFactory.createEON("/Users/tu/workspace/eonpm/kbs/ATHENA_GlycemicControl.pprj");
			} catch (PCA_Initialization_Exception e) {
				logger.error("Can't create EON ");
				e.printStackTrace();
			}     
			// Alternatively, call createEON(protegeProjFullPath, aDS)
			try {
				aEON1.setDataSource(aDS1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				aEON1 = null;
			}
			try {
				aEON2.setDataSource(aDS2);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				aEON2 = null;
			}
		}
		else {
			// No context. simple exit here. 
			System.out.println("Configuration not equal to BMIR. Quit.");
			System.exit(0);

		}



		// Here we have aEon available and ready, with its DataSource
		// also determined.

		List<String> patients = new ArrayList<String>();
		patients.add("1316243");
		patients.add("1000693");
		PrintWriter out;

		try {
			String fileName = outputdir+"outPatientResult.html";
			File outputFile = new File(fileName);;
			if (outputFile.exists()) {
				outputFile.delete();
			}
			out = new PrintWriter(fileName);
			printHeader(out, EONConstants.HTML);
			for (String pid : patients) {
				out.println("<h1>Case ID: "+pid+"</h1>");
				if (aEON1 != null) {
					System.out.println("Computing with case "+pid+" and guideline "+guidelineName);
					Collection<Advisory> advisories = aEON1.computeAdvisories(pid, sessionTime, guidelineName,false);
					//begin: code to replicate old html output
					//aEON1.getPCASession().setGuideline(guidelineName);
					//Guideline_Service_Record[] gsrs = aEON1.getPCASession().computeAdvisories(pid, sessionTime, guidelineName, false);
					
					out.print(aEON1.printData(IEON.HTML));
					if (advisories != null) {
						for (Advisory advisory : advisories) {
							out.print(aEON1.printAdvisory(advisory, IEON.HTML));
							out.print("<h2>Output in old HTML format</h2>");
							out.println(advisory.getDebugHTML());
						}
//						out.print("<h2>Output in old HTML format</h2>");
//						for (Guideline_Service_Record gsr : gsrs) {
//							ClientUtil.showResult(gsr, out, Compliance_Level.strict);
//						}
					} else {
						logger.error("No output for guideline "+ guidelineName);
						out.println("<p>No output for guideline "+ guidelineName+"</p>");
					}
					
				}
				printData(out, aEON1.getDataEntry("Potassium", false));
				printData(out, aEON1.getDataEntry("Potassium", true));
				printData(out, aEON1.getDataEntry("Hypertension", false));
				printData(out, aEON1.getDataEntry("CCS-7", false)); //Diseases of the circulatory system
				printData(out, aEON1.getMedication("lisinopril", true));
				printData(out, aEON1.getMedication("ACE_Inhibitors", true));
				printData(out, aEON1.getAdverseReaction("lisinopril", true));
				printData(out, aEON1.getAdverseReaction("ACE_Inhibitors", true));
				
				if (aEON2 != null) {
					System.out.println("Computing with case "+pid+" and guideline "+guidelineName2);
					Collection<Advisory> advisories = aEON2.computeAdvisories(pid, sessionTime, guidelineName2,false);
//					aEON2.getPCASession().setGuideline(guidelineName2);
//					Guideline_Service_Record[] gsrs = aEON2.getPCASession().computeAdvisories(pid, sessionTime, guidelineName2, false);
					out.print(aEON2.printData(IEON.HTML));
					if (advisories != null) {
						for (Advisory advisory : advisories) {
							out.print(aEON2.printAdvisory(advisory, IEON.HTML));
							out.print("<h2>Output in old HTML format</h2>");
							out.println(advisory.getDebugHTML());
						}
//						out.print("<h2>Output in old HTML format</h2>");
//						for (Guideline_Service_Record gsr : gsrs) {
//							ClientUtil.showResult(gsr, out, Compliance_Level.strict);
//						}
					} else {
						logger.error("No output for guideline "+ guidelineName2);
						out.println("<p>No output for guideline "+ guidelineName2+"</p>");
					}
					
				}
				aEON2.finishSession();
				aEON1.finishSession();
			} 
			printFooter(out, EONConstants.HTML);
			out.flush();
			out.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
	}
	

	private static void printData(PrintWriter out, Collection<? extends Data> data) {
		if (data != null ){
			for (Data datum : data) {
				datum.printData(out);
			}
		}
	}


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
