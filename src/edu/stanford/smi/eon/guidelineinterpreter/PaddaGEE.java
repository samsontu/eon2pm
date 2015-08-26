/*
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License");  you may not use this file except in 
 * compliance with the License.  You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 *
 * The Original Code is EON Guideline Execution Engine.
 *
 * The Initial Developer of the Original Code is Stanford University. Portions
 * created by Stanford University are Copyright (C) 2009.  All Rights Reserved.
 *
 * The EON Guideline Execution Engine was developed by Center for Biomedical
 * Informatics Research (http://www.bmir.stanford.edu) at the Stanford University
 * with support from the National Library of Medicine.  Current
 * information about the EON project can be obtained at 
 * http://www.smi.stanford.edu/projects/eon/
 *
 */
package edu.stanford.smi.eon.guidelineinterpreter;

import java.util.*;
import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.eon.PCAServerModule.*;
import edu.stanford.smi.eon.kbhandler.*;
import edu.stanford.smi.eon.datahandler.*;
import edu.stanford.smi.eon.Dharma.* ;
import java.io.*;


public interface PaddaGEE  {


	/**
	 * Set the guideline associated with the guideline execution engine
	 * Note that nothing else is reset 
	 * @param guideline
	 */
	public void setGuideline(Management_Guideline guideline);
	public String getCurrentGuidelineID();

	// KBHandler is created as part of initialization of GEE
	// putDBmanager(...) is used in resetting cases
	public void setDBmanager(DataHandler dbmanager) ;
	public DataHandler getDBmanager();
	public KBHandler getKBmanager() ;

	public CriteriaEvalManager getEvalManager();

	public Set getExpandedChoices();
	public boolean hasUnexpandedPreferedChoice(Guideline_Service_Record advisories);

	public void resetAdvisories () ;
	public Guideline_Service_Record returnAdvisory() ;
	public boolean containsAdvisories() ;

	public ActivityEvaluation getEvaluation(Object key);
	public void putEvaluation(Object key, Object value) ;

	public void setAssumption(Criteria_Evaluation assume, String assumptionString) ;

	/**
	 * From a collection of conditional goals, find the goal that's applicable and 
	 * compute whether the goal is satisfied. Cache the result in the 'goals' variable
	 * @param possibleGoals
	 * @param guideline
	 * @return Guideline_Goal
	 * @throws PCA_Session_Exception
	 */
	public Guideline_Goal computeConditionalGoals(Collection<Instance> possibleGoals,
			Guideline_Entity guideline) throws PCA_Session_Exception ;
	/**
	 * Check to see if the goal associated with guideline has been evaluated
	 * @param guideline
	 * @return Criteria_Evaluation (evaluated goal)
	 * 
	 */
	public Criteria_Evaluation checkGoal(Guideline_Entity guideline) ;
	public void retractGoals ();

	public Guideline_Scenario_Choices initializeScenarios()
			throws PCA_Session_Exception ;
	/** Given a selected scenario, recompute advisories */
	public Guideline_Service_Record chosenScenario(Selected_Scenario selectedScenario,
			Compliance_Level compliance)
					throws PCA_Session_Exception ;


	public Guideline_Service_Record chosenActions(Collection selectedActions) ;


	public Collection addEvaluatedChoice (Guideline_Activity_Evaluations evaluation);
	public Collection getEvaluatedChoices () ;
	public Collection addScenarioChoice (Guideline_Scenario_Choices choices) ;
	public Collection getScenarioChoices () ;
	public Collection addActionChoice (Guideline_Action_Choices choices) ;
	public Collection addActionChoices (Collection choices) ;
	public Collection getActionChoices () ;
	public Collection addConclusions (Collection conclusions) ;
	public Collection addConclusion (Conclusion conclusion) ;
	public Collection getConclusions ();
	public void addGoal(Guideline_Goal goal) ;

	public Collection matchData(String dataClass, Collection concepts, Collection data) ;
	public Collection matchAdverseEvents(Cls drugClass) ;



	/**
    For a concept name and "data", a set of concept names, return a "Matched_Data"
    structure consisting of those concept names in "data" that are names of
    subclasses of the given concept.
	 */
	public Matched_Data containSubclassOf(String data_category, String className,
			Collection data) ;
	public void serializeAdvisories(ObjectOutputStream out) ;

	public void restoreAdvisories(ObjectInputStream in) throws java.io.IOException,
	java.lang.ClassNotFoundException, PCA_Session_Exception ;

	public Conclusion checkGuidelineApplicability (Management_Guideline guideline,
			String patient_id, String session_time) ;

	public void determinePatientCharacteristics(Collection caseClassification,
			String patientId, String sessionTime) throws PCA_Session_Exception ;

	public Guideline_Service_Record computePerformanceMeasures(String pid,
			String hospitalizationID, String sessionTime, String PMName,
			String startTime, String stopTime);

}
