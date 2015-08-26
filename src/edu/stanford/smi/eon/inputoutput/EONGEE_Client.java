package edu.stanford.smi.eon.inputoutput;

import edu.stanford.smi.eon.Dharma.Management_Guideline;
import edu.stanford.smi.eon.PCAServerModule.*;
import edu.stanford.smi.eon.clients.ClientUtil;
import edu.stanford.smi.eon.guidelineinterpreter.GuidelineInterpreter;
import edu.stanford.smi.eon.kbhandler.KBHandler;
import edu.stanford.smi.eon.kbhandler.WhereComparisonFilter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;


/*
  How will it work from some other program running under Protege?
  Run dummy patient

    PCAClient testObj = new PCAClient();

    testObj.getInitParamsFromFile(  );

    // assume here that kbHandler, guidelineName are independently set up
    testObj.setUpGuidelineInterpreter( kbHandler, guidelineName, );

    // assume here that ptDataArray and dir are set up outside
    outputFile = testObject.computeAndSaveAdvisory( ptDataArray, outputdir);

 */

public class EONGEE_Client extends Object {
	private boolean invokedStandalone = true;
	private String kbURL;
	private String database;

	private String user ;
	private String password;
	private String guidelineName;
	private String serverLogFile;
	private KBHandler m_kbHandler;
	private PCASession m_pca;
	private Vector ptList;
	private Guideline_Service_Record[] dssOutput;

	private static Compliance_Level complianceLevel = Compliance_Level.strict;

	static Logger logger = Logger.getLogger(EONGEE_Client.class);

	public EONGEE_Client() {

	}


/*	public void setUpKBHandler() {
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
*/
/*	public void setUpGuidelineInterpreter(KBHandler kbManager, String guidelineName, String databaseName) {

		m_pca = null;

		if (kbManager == null || guidelineName == null || databaseName == null) {
			System.out.println(" setUpGuidelineInterpreter: input argument(s) null ");
			System.exit(0);
		}

		try {
			PCAServer_i PCAImp = new PCAServer_i();
			PCAImp.kbManager = kbManager;
			PCAImp.setServerLog(serverLogFile);
			PCAImp.kbURLString = kbURL;
			m_pca = PCAImp.open_pca_session(database, user, password, null);
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
*/
	public void setUpGuidelineInterpreter(KBHandler kbManager, String guidelineName) {

		m_pca = null;

		if (kbManager == null || guidelineName == null) {
			System.out.println(" setUpGuidelineInterpreter: input argument(s) null ");
			System.exit(0);
		}

		try {
			PCAServer_i PCAImp = new PCAServer_i();

			PCAImp.kbManager = kbManager;

			// System.out.println(serverLogFile);
			// PCAImp.setServerLog(serverLogFile);
			//PCAImp.kbURLString = kbURL;

			m_pca = PCAImp.open_pca_session("", "", "", "");

			System.out.println(" complete open_pca_session ");

			m_pca.setGuideline(guidelineName);

			this.guidelineName = guidelineName;			

			System.out.println(" completed setting guideline: " + guidelineName );

			m_pca.compliance(Compliance_Level.strict);

		} catch(Exception se) {
			System.out.println("Exception raised during creation of PCAServer_i " +
					se.toString());
			System.exit(0);
		}

	}

	public boolean setUpGuidelineInterpreterAndComputeGoalSatisfied(KBHandler kbManager, Patient_Data[] data, String guidelineName, String ptID) {

		if (kbManager == null || guidelineName == null) {
			System.out.println(" setUpGuidelineInterpreter: input argument(s) null ");
			System.exit(0);
		}

		Collection guidelines  =  (kbManager.findInstances("Management_Guideline",
				new WhereComparisonFilter("label", "eq", guidelineName), null));

		if (guidelines == null || guidelines.isEmpty()) {
			System.out.println("guideline not found:"+ guidelineName);
			return false;
		}
		else	{
			Collection guidelineGoals = getGuideline(kbManager, guidelineName).getgoalValue();
			System.out.println("guidelineName-------------------:" + guidelineName);

			if ((guidelineGoals == null) || (guidelineGoals.isEmpty()))	{
				System.out.println("There is no goal in this Guideline");
				return false;
			}
			else	{
				m_pca = null;
				try {
					PCAServer_i PCAImp = new PCAServer_i();

					PCAImp.kbManager = kbManager;

					m_pca = PCAImp.open_pca_session("", "", "", "");

					System.out.println(" complete open_pca_session ");

					m_pca.setGuideline(guidelineName);
					this.guidelineName = guidelineName;
					m_kbHandler = kbManager;

					/*///////////////////////////////
					Collection subguidelines = subGuidelines(kbManager, guidelineName);
					if (subguidelines.size() != 0) {
		//				System.out.println("Not Null- ==============" + subguidelines.size());
						for (Iterator i=subguidelines.iterator(); i.hasNext(); ) {
							Management_Guideline sub=(Management_Guideline)i.next();
							try {
								logger.info("executing subguideline: "+getGuidelineId(sub));
								m_pca.setGuideline(getGuidelineId(sub));
								this.guidelineName = getGuidelineId(sub);
								System.out.println("+++++++++++++++++>>>" + getGuidelineId(sub));
							} catch (Exception e) {
								logger.error("Exception in computing advisory for "+ getGuidelineId(sub));
								logger.error(e.getMessage());
								e.printStackTrace();
							}
						}
					}
					else	{
						m_pca.setGuideline(guidelineName);
						this.guidelineName = guidelineName;
					}
					 */
					System.out.println(" completed setting guideline: " + guidelineName );

					m_pca.compliance(Compliance_Level.strict);

				} catch(Exception se) {
					System.out.println("Exception raised during creation of PCAServer_i " +
							se.toString());
					System.exit(0);
				}
				return computeGoalSatisfied(kbManager, data, guidelineName, ptID);
			}
		}
	}

	// the following method, computeAndSaveAdvisory does not work for Glinda
	public String computeAndSaveAdvisory(String ptID, String caseData, KBHandler kbmanager, String currentTime) {
		if (m_pca == null) {
			System.out.println(" computeAndSaveAdvisory error: pca not set up ");
			System.exit(0);
		}

		String recommendation = "";
		/* before String caseData; */
		Guideline_Service_Record[] dssOutput;
		try {
			m_pca.resetAdvisories();
			m_pca.setCase(ptID, currentTime);
			dssOutput = m_pca.computeAdvisories();
			System.out.println("-----------" + dssOutput.length);
			System.out.println(" computeAdvisories completed for patient " + ptID);

			/* before  caseData = m_pca.printData(); */
			recommendation = saveComputedRecommendations(ptID, caseData, dssOutput, kbmanager, currentTime);
			m_pca.finishSession();
		}
		catch (Exception e) {
			System.out.println("computeAndSaveAdvisory error for patient: " + ptID);
			e.printStackTrace();
		}
		return recommendation;
	}

	// the following method, computeAndSaveAdvisory WORKS for Glinda

	public String computeAndSaveAdvisory(String ptID, String currentTime, Patient_Data[] data, KBHandler kbmanager) {
		System.out.println("computeAndSaveAdvisory----**--------");
		if (m_pca == null) {
			System.out.println(" computeAndSaveAdvisory error: pca not set up ");
			System.exit(0);
		}

		// String outputFile = "";
		// change as below
		String HTMLAdvisory = null;
		String XMLAdvisory = null;
		String caseData;
//		String ptIDGenerated = null;
		try {

			m_pca.resetAdvisories();
			m_pca.setCase(ptID, currentTime);
			m_pca.updateData(data);
			dssOutput = m_pca.updateAdvisories();
			System.out.println(" updateAdvisories completed for dummy patient " + ptID);


			//////////////////////////////////////////////////////////////////
			caseData = m_pca.printData();		

			// outputFile = saveComputedRecommendations(ptIDGenerated, caseData, dssOutput, kbmanager);
			HTMLAdvisory = saveComputedRecommendations(ptID, caseData, dssOutput, kbmanager, currentTime);
			XMLAdvisory = generateXMLAdvisory(ptID, dssOutput, kbmanager, currentTime);
			m_pca.finishSession();
		}
		catch (Exception e) {
			System.out.println("computeAndSaveAdvisory error for  patient: " + ptID);
			e.printStackTrace();
		}
		return HTMLAdvisory+XMLAdvisory;
		
		// return outputFile;
		
	}

	public boolean computeGoalSatisfied(KBHandler kbManager, Patient_Data[] data, String glName, String ptID) {
		System.out.println("computeGoalSatisfied----**--------");
		if (m_pca == null) {
			System.out.println(" computeGoalSatisfied error: pca not set up ");
			System.exit(0);
		}

		String ptIDGenerated = null;
		boolean goalSatisfied = false;
		try {
			ptIDGenerated = m_pca.setDummyCase();
			m_pca.updateData(data);

			goalSatisfied = m_pca.computeEligibilityAndGoal(); // .updateAdvisories();
			System.out.println("goalSatisfied?: " + "[" + goalSatisfied + "]");
		}
		catch (Exception e) {
			System.out.println("computeAndSaveAdvisory error for dummy patient: " + ptIDGenerated);
			e.printStackTrace();
		}		
		//System.out.println("subGuideline=============>" + subGuidelines(kbManager, glName));
		////////////////////////////////////////////////////
		// Do not compute subguideline goals. Not clear what should be.
/*		Collection subguidelines = subGuidelines(kbManager, glName);
		if (subguidelines != null ) {
			for (Iterator i=subguidelines.iterator(); i.hasNext(); ) {
				Management_Guideline sub=(Management_Guideline)i.next();
				try {
					logger.info("executing subguideline: "+getGuidelineId(sub));
					m_pca.setGuideline(getGuidelineId(sub)); 

				} catch (Exception e) {
					logger.error("Exception in computing advisory for "+ getGuidelineId(sub));
					logger.error(e.getMessage());
					e.printStackTrace();
				}
			}
		}
*/		//System.out.println("getGuidelineId(sub)" + getGuidelineId(sub));
		////////////////////////////////////////////////////
		return goalSatisfied;
	}

	public Guideline_Service_Record[] getDSSOutput() {
		return dssOutput;
	}
	
	public String generateXMLAdvisory(String ptID, Guideline_Service_Record[] dssOutput,
			KBHandler kbmanager, String currentTime ) {
		Document xmlDoc = ClientUtilXML.makeXMLOutput( dssOutput, ptID, guidelineName, kbmanager.getKB());											
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		String advisoryXML = null;
		Transformer transformer;
		try {
			transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(xmlDoc);
			Writer outWriter = new StringWriter();  
			StreamResult result = new StreamResult( outWriter ); 
			transformer.transform(source, result);
			advisoryXML = result.getWriter().toString();
		} catch (TransformerConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return advisoryXML;
		
	}

	public String saveComputedRecommendations(String ptID, String caseData, Guideline_Service_Record[] dssOutput,
			KBHandler kbmanager, String currentTime) {
		System.out.println("begin saveComputedRecommendations!");
		String sentence = "";
		// String filePrefix = "/" + ptID;
		String recommendation = "";

		// File debugOutput= new File(filePrefix + "-DEBUG.html");
		// File briefOutput= new File(filePrefix + "-BRIEF.html");
		System.out.println ("dssOutput:--------" + dssOutput.length + " " + dssOutput.toString()); 
		try {
			recommendation = ClientUtil.printHeader(m_pca.patient_id(), currentTime);
			System.out.println("----------dssOutput Length=" + dssOutput.length);
			if (dssOutput != null) {
				for (int j=0; j< dssOutput.length; j++) {
					sentence += "<h1 align=\"center\">"+guidelineName+"</h1>" +"\n";
					StringWriter returnStringConstructor = new StringWriter();
					PrintWriter itsWriter = new PrintWriter(returnStringConstructor, true);										
					edu.stanford.smi.eon.clients.ClientUtil.showResultWithKB(dssOutput[j], itsWriter, kbmanager.getKB());
					sentence += returnStringConstructor.toString();
					//sentence += ClientUtil.showResultWithKB(dssOutput[j],kbmanager.getKB());
					//BriefOutput.showResult(dssOutput[j], briefWriter, Compliance_Level.strict);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		recommendation += sentence;
		caseData = "<patient name=" + ptID + ">" + "\n" + caseData;
		caseData += "</patient>";		
		recommendation += caseData;
		recommendation += "\n";
		recommendation += "</body></html>";
		return recommendation;
	}

	static boolean isEligible( Guideline_Service_Record[] dssOutput, String guidelineName) {
		boolean isEligible = false;
		for (int j=0; j < dssOutput.length; j=j+1){
			if (dssOutput[j].subject_classification != null) {
				for (int i=0; i < dssOutput[j].subject_classification.length; i++) {
					if (dssOutput[j].subject_classification[i] != null) {
						Conclusion classification = dssOutput[j].subject_classification[i];
						logger.debug("parameter: " +classification.parameter);

						System.out.println("parameter: " +classification.parameter);

						if (classification.parameter.equals(guidelineName)){
							logger.debug("Client.isEligible "+classification.value);
							System.out.println("-----Client.isEligible "+classification.value);
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
		logger.info("topguideline: " + topGL);
		logger.info(kbManager.getKB().getSlot("subguidelines").toString());
		if (topGL != null) {
			return topGL.getOwnSlotValues(kbManager.getKB().getSlot("subguidelines"));
		} else {
			logger.error("top-level guideline "+ guidelineId +" is null");
			return null;
		}
	}

	static Management_Guideline getGuideline(KBHandler kbManager, String guidelineId) {
		
		Management_Guideline gl = null;
		Collection guidelines  =  (kbManager.findInstances("Management_Guideline",
				new WhereComparisonFilter("label", "eq", guidelineId), null));
		if (guidelines == null || guidelines.size() == 0) {
			logger.error("guideline not found "+ guidelineId);
		} else if (guidelines.size() > 1) {
			logger.error("guideline ambiguous" + guidelineId);
		} else {
			return (Management_Guideline) (guidelines.toArray())[0];
		}
		return null;
	}

}

