package gov.va.test.opioidtesttool.pca;

import edu.stanford.smi.eon.Dharma.Management_Guideline;
import edu.stanford.smi.eon.PCAServerModule.*;
import edu.stanford.smi.eon.inputoutput.AdvisoryFormater;
import edu.stanford.smi.eon.inputoutput.ClientUtilXML;
import edu.stanford.smi.eon.kbhandler.KBHandler;
import edu.stanford.smi.eon.kbhandler.WhereComparisonFilter;
import edu.stanford.smi.eon.clients.*;
import edu.stanford.smi.eon.execEngine.IEON;
import gov.va.athena.advisory.Advisory;
import gov.va.test.opioidtesttool.GlobalVars;


import java.util.*;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.*;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;


/*
  How will it work from some other program running under Protege?
  Run dummy patient

    PCAClient testObj = new PCAClient();

    testObj.getInitParamsFromFile( initFile );

    // assume here that kbHandler, guidelineName are independently set up
    testObj.setUpGuidelineInterpreter( kbHandler, guidelineName, initFile);

    // assume here that ptDataArray and outputdir are set up outside
    outputFile = testObject.computeAndSaveAdvisory( ptDataArray, outputdir);

 */

public class Client extends Object {
	private boolean invokedStandalone = true;
	private String kbURL;
	private String database;
	private String outputdir;
	private String user ;
	private String password;
	private String guidelineName=null;
	private String serverLogFile;
	private KBHandler m_kbHandler;
	private PCASession m_pca;
	private Vector ptList;
	private Guideline_Service_Record[] dssOutput;

	private static Compliance_Level complianceLevel = Compliance_Level.strict;

	static Logger logger = Logger.getLogger(Client.class);

	public Client() {

	}

	public void getInitParamsFromFile(String initFile) {

		System.out.println("getInitParamsFromFile initfile: " + initFile);

		Properties settings = new Properties();
		try {
			FileInputStream sf = new FileInputStream(initFile);
			settings.load(sf);
		} catch (Exception ex) {
			System.out.println("Exception during loading initialization file");
			System.out.println(ex.toString());
			System.exit(0);
		}

		kbURL = settings.getProperty("SERVERKB", "");
		guidelineName = settings.getProperty("GUIDELINEID", "");
		database = settings.getProperty("DATABASE", "");
		outputdir = settings.getProperty("PCAOUTPUTDIR", "");
		serverLogFile = settings.getProperty("SERVER_LOGFILE");
		String cumulateDoseStr = settings.getProperty("CUMULATEDOSE");
		if ((cumulateDoseStr == null) || cumulateDoseStr.equals(""))
			GlobalVars.cumulateDose = true;
		else
			GlobalVars.cumulateDose=cumulateDoseStr.equals("TRUE");
		
		user = settings.getProperty("USER");;
		password = settings.getProperty("PASSWORD");

		if (guidelineName == null)
			System.out.println("No guideline name!!!");
		System.out.println("PCAClient: serverkb " +  kbURL);
		System.out.println("PCAClient: guidelineName " + guidelineName);
	}

	public void setUpKBHandler() {
		if (kbURL == null) {
			System.out.println("Error: Knowledge Base Location Unspecified ");
			System.exit(0);
		}

		try {
			m_kbHandler = new KBHandler(kbURL);
			System.out.println(" Knowledge Base Handler Successfully set up ");
		}
		catch (Exception e) {
			System.out.println("setUpKBHandler error with kbURL: " + kbURL);
			e.printStackTrace();
			System.exit(0);
		}
	}

	public void setUpGuidelineInterpreter(KBHandler kbManager, String guidelineName, String databaseName, String initFile) {

		m_pca = null;

		if (kbManager == null || guidelineName == null || databaseName == null ||
				initFile == null) {
			System.out.println(" setUpGuidelineInterpreter: input argument(s) null ");
			System.exit(0);
		}

		try {
			PCAServer_i PCAImp = new PCAServer_i();
			PCAImp.kbManager = kbManager;
			PCAImp.setServerLog(serverLogFile);
			PCAImp.kbURLString = kbURL;
			m_pca = PCAImp.open_pca_session(database, user, password, initFile );
			System.out.println(" complete open_pca_session ");
			m_pca.setGuideline(guidelineName);
			m_pca.setCumulativeFlag(GlobalVars.cumulateDose);
			System.out.println(" completed setting guideline: " + guidelineName );

			m_pca.compliance(Compliance_Level.strict);

		} catch(Exception se) {
			System.out.println("Exception raised during creation of PCAServer_i " +
					se.toString());
			System.exit(0);
		}

	}

	public void setUpGuidelineInterpreter(KBHandler kbManager, String guidelineName, String initFile) {

		m_pca = null;

		if (kbManager == null || guidelineName == null || initFile == null) {
			System.out.println(" setUpGuidelineInterpreter: input argument(s) null ");
			System.exit(0);
		}

		try {
			PCAServer_i PCAImp = new PCAServer_i();
			PCAImp.kbManager = kbManager;
//			PCAImp.setServerLog(serverLogFile);
			PCAImp.kbURLString = kbURL;
			m_pca = PCAImp.open_pca_session("", "", "", initFile );
			System.out.println(" complete open_pca_session ");
			m_pca.setGuideline(guidelineName);
			System.out.println(" completed setting guideline: " + guidelineName );

			m_pca.compliance(Compliance_Level.strict);

		} catch(Exception se) {
			System.out.println("Exception raised during creation of PCAServer_i " +
					se.toString());
			System.exit(0);
		}

	}

	public String computeAndSaveAdvisory(String ptID, String outputdir, KBHandler kbmanager) {

		if (m_pca == null) {
			System.out.println(" computeAndSaveAdvisory error: pca not set up ");
			System.exit(0);
		}
		if (outputdir == null) {
			System.out.println(" computeAndSaveAdvisory error: output dir not specified ");
			System.exit(0);
		}

		String outputFile = "";
		String caseData;
		Guideline_Service_Record[] dssOutput;
		try {
			m_pca.resetAdvisories();
			m_pca.setCase(ptID, GlobalVars.sessionTime);
			dssOutput = m_pca.computeAdvisories();
			System.out.println(" computeAdvisories completed for patient " + ptID);

			caseData = m_pca.printData();
			outputFile = saveComputedRecommendations(ptID, caseData, dssOutput, outputdir, kbmanager, null);
		}
		catch (Exception e) {
			System.out.println("computeAndSaveAdvisory error for patient: " + ptID);
			e.printStackTrace();
		}
		return outputFile;
	}

	public String computeAndSaveAdvisory(Patient_Data[] data, String outputdir, KBHandler kbmanager, String patient_id) {

		if (m_pca == null) {
			System.out.println(" computeAndSaveAdvisory error: pca not set up ");
			System.exit(0);
		}
		if (outputdir == null) {
			System.out.println(" computeAndSaveAdvisory error: output dir not specified ");
			System.exit(0);
		}

		String outputFile = "";
		String caseData;
		String advisoryHTML = "";
		try {
			m_pca.setCase(patient_id, GlobalVars.sessionTime);
			m_pca.setCumulativeFlag(GlobalVars.cumulateDose);
			m_pca.updateData(data);
			//dssOutput = m_pca.updateAdvisories();
			// dssOutput = m_pca.computeAndStoreAdvisories(outputdir);
			dssOutput = m_pca.computeAdvisories();
			System.out.println(" updateAdvisories completed for patient " + patient_id);			
			Collection<Advisory> advisories = AdvisoryFormater.javaTransform(dssOutput, patient_id, null, guidelineName, GlobalVars.sessionTime, null, null, GlobalVars.kb);
			caseData = m_pca.printData();
			for (Advisory advisory: advisories)
				advisoryHTML = advisoryHTML +m_pca.printAdvisory(advisory, IEON.HTML)+ "<br><br>";
			outputFile = saveComputedRecommendations(patient_id, caseData, dssOutput, outputdir, kbmanager, advisoryHTML);
		}
		catch (Exception e) {
			System.out.println("computeAndSaveAdvisory error for dummy patient: " + patient_id);
			e.printStackTrace();
		}
		return outputFile;
	}

	public Guideline_Service_Record[] getDSSOutput() {
		return dssOutput;
	}

	public String saveComputedRecommendations(String ptID, String caseData, Guideline_Service_Record[] dssOutput, String outputdir,
			KBHandler kbmanager, String additionalOutput) {

		String filePrefix = outputdir + "/" + ptID.replace(' ', '_');

		File debugOutput= new File(filePrefix + ".html");
		File xmlOutput = new File(filePrefix+".xml");
//		File briefOutput= new File(filePrefix + "-BRIEF.html");

		try {
			/* First write debug page*/
			PrintWriter debugWriter = new PrintWriter (new FileWriter(debugOutput), true);
			PrintWriter xmlWriter = new PrintWriter (new FileWriter(xmlOutput), true);
//			PrintWriter briefWriter = new PrintWriter( new FileWriter(briefOutput), true);
			ClientUtil.printHeader(debugWriter, ptID.replace('_', ' ')); //m_pca.patient_id());
			debugWriter.print(caseData);

			if (dssOutput != null) {
				for (int j=0; j< dssOutput.length; j++) {
					debugWriter.println("<h1 align=\"center\">"+guidelineName+"</h1>");
					ClientUtil.showResultWithKB(dssOutput[j], debugWriter, kbmanager.getKB());
					//BriefOutput.showResult(dssOutput[j], briefWriter, Compliance_Level.strict);
				}
				//Generate XML output
				xmlWriter.print(ClientUtilXML.generateXMLAdvisory( ptID, dssOutput, kbmanager, guidelineName ));
				logger.debug("is eligible? guidelineName:"+ guidelineName + " "+isEligible(dssOutput, guidelineName));
				if (isEligible(dssOutput, guidelineName)){
					Collection subguidelines = subGuidelines(kbmanager, guidelineName);
					if (subguidelines != null ) {
						for (Iterator i=subguidelines.iterator(); i.hasNext(); ) {
							Management_Guideline sub=(Management_Guideline)i.next();
							try {
								logger.info("executing subguideline: "+getGuidelineId(sub));
								m_pca.setGuideline(getGuidelineId(sub)); 
								dssOutput = m_pca.updateAdvisories();
								if (dssOutput != null) {
									debugWriter.println("<h2>"+getGuidelineId(sub)+"</h2>");
									for (int j=0; j< dssOutput.length; j++) {
										ClientUtil.showResultWithKB(dssOutput[j], debugWriter, kbmanager.getKB());
									}
									xmlWriter.print(ClientUtilXML.generateXMLAdvisory( ptID, dssOutput, kbmanager, guidelineName ));
								}
							} catch (Exception e) {
								logger.error("Exception in computing advisory for "+ getGuidelineId(sub));
								logger.error(e.getMessage());
								e.printStackTrace();
							}
						}
					}
				}

			}
			if (additionalOutput != null)
				debugWriter.print(additionalOutput);
//			briefWriter.close();
			debugWriter.close();
			xmlWriter.close();

			/* Now write brief output page*/

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				m_pca.setGuideline(guidelineName); 
			} catch (Exception e) {
				logger.error("Client.saveComputedRecommendations: Problem resetting guideline to "+guidelineName);
			}
		}

		return filePrefix;      
	}

	static boolean isEligible( Guideline_Service_Record[] dssOutput, String guidelineName) {
		boolean isEligible = false;
		for (int j=0; j < dssOutput.length; j=j+1){
			if (dssOutput[j].subject_classification != null) {
				for (int i=0; i < dssOutput[j].subject_classification.length; i++) {
					if (dssOutput[j].subject_classification[i] != null) {
						Conclusion classification = (Conclusion)dssOutput[j].subject_classification[i];
						logger.debug("parameter: " +classification.parameter);
						if (classification.parameter.equals(guidelineName)){
							logger.debug("Client.isEligible "+classification.value);
							isEligible = classification.value.equals(Truth_Value._true.toString());
							return isEligible;
						}
					}
				}
			}
		}
		return isEligible;
	}


	static String getGuidelineId( Management_Guideline guideline) {
		logger.debug("gov.va.test.opioidtesttool.pca.Client.getGuidelienId: "+guideline.getBrowserText()+".");
		return guideline.getBrowserText();
	}

	static Collection subGuidelines( KBHandler kbManager, String guidelineId) {
		Management_Guideline topGL = getGuideline(kbManager, guidelineId);
		logger.debug("topguideline: " + topGL);
		logger.debug(kbManager.getKB().getSlot("subguidelines").toString());
		return topGL.getOwnSlotValues(kbManager.getKB().getSlot("subguidelines"));
	}

	static Management_Guideline getGuideline(KBHandler kbManager, String guidelineId) {
		Management_Guideline gl = null;
		Collection guidelines  =  (kbManager.findInstances("Management_Guideline",
				new WhereComparisonFilter("label", "eq", guidelineId), null));
		if (guidelines == null || guidelines.isEmpty()) {
			logger.error("guideline not found"+ guidelineId);
		}
		if (guidelines.size() > 1) {
			logger.error("guideline ambiguous" + guidelineId);
		} else {
			return (Management_Guideline) (guidelines.toArray())[0];
		}
		return null;
	}
	

}

