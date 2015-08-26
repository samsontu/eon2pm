package edu.stanford.smi.eon.execEngine;

import java.util.Collection;

import edu.stanford.smi.eon.PCAServerModule.PCA_Session_Exception;
import edu.stanford.smi.eon.pca.PCASession_Imp;
import gov.va.athena.advisory.Advisory;
import gov.va.athena.advisory.Guideline_Goal;
import edu.stanford.smi.eon.data.*;

public interface IEON {
	
	public static final String HTML = "html";
	public static final String XML = "xml";
	public static final String HTML_NOTABLE = "html_notable";
	
	//In case DataSource changes for a loaded EON instance
	//Two cases: (1) No patient case is associated with data source
	//           (2) the data source is already loaded with a patient case
	public void setDataSource(IDataSource aDS) throws Exception;      

	public void setGuideline(String guidelineName); //In case it's desirable to repeatedly compute advisories 
	//  using the same guideline

	public String printData(String format);  //Only HTML and HTML_NOTABLE format is implemented right now
	
	public String printAdvisory(Advisory advisory, String format); // only html format is implemented right now

	// computePerformanceMeasuresAdvisory assumes that a data source has been set up
	// Loads patient data using the configured data source.
	public Advisory computePerformanceMeasuresAdvisory(String pid,
			String hospitalizationID, String sessionTime, String guidelineName,
			String PMname, String startTime, String stopTime) 
					throws PCA_Session_Exception ;

	// computeAndStoreAdvisories assumes that a data source has been set up
	// Loads patient data using the configured data source if pid is different from
	// stored pid.
	// A guideline may be configured with subguidelines. The "computeSubguidelines" flag
	// indicates whether advisories should be generated from the subguidelines.
	// The "storage" of advisories uses Java serialization.
	public Collection<Advisory> computeAndStoreAdvisories(String pid, String sessionTime, 
			String guidelineName, String storageDirectory, boolean computeSubguidelines
			) throws
			PCA_Session_Exception;

	// loadAdvisories loads a previously stored advisories (including data)
	// No assumption about connection to data
	public Collection<Advisory> loadAdvisories(String pid, String sessionTime, 
			 String storageDirectory
			) throws
			PCA_Session_Exception;
	
	// computeAdvisories assumes that a data source has been set up
	// Loads patient data using the configured data source if pid is different from
	// stored pid.
	// Call each time when applying KB to the different patient cases
	public Collection<Advisory> computeAdvisories(String pid, String sessionTime, String guidelineName
			, boolean computeSubguidelines) throws
			PCA_Session_Exception;
	
    // Returns true if the patient is eligible for the guideline
	public boolean isEligible(String pid, String sessionTime, String guidelineName
			) throws
			PCA_Session_Exception;

    // Returns evaluated Goal of the guideline
	public Guideline_Goal evaluateGoal(String pid, String sessionTime, String guidelineName
			) throws
			PCA_Session_Exception;

	// Add data elements to the EON session
	public void updateData(
			edu.stanford.smi.eon.PCAServerModule.Patient_Data[] data
			) throws
			edu.stanford.smi.eon.PCAServerModule.Improper_Data_Exception;
	
	// Recompute advisories, based on existing or updated data elements
	public Collection<Advisory> updateAdvisories(String pid, String sessionTime, String guidelineName, 
			boolean computeSubguidelines
			) throws
			PCA_Session_Exception;

	//Delete data elements (e.g., Protege instances of EPR entities)
	public void finishSession();
	
	// Returns PCASession so that we can use earlier API.
	public PCASession_Imp getPCASession();
	
	// Return data available to the execution engine. In case of problem note entries, returns data that are
	// possibly subclasses of domainTerm
	public Collection<DataEntry> getDataEntry (String domainTerm, boolean mostRecent) ;
	
	// mostRecent flag of getMedication and getAdverseReaction are ignored at this time
	// the valid time format of Medication is something like [1979-06-05,null]
	// Drug name may be a class of drug. In that case, return all drugs that are subclasses
	// of the drug class
	public Collection<Medication> getMedication (String drugName, boolean mostRecent) ;
	
	// The valid time field of AdverseReaction is always null now
	public Collection<AdverseReaction> getAdverseReaction (String substance, boolean mostRecent) ;

}
