package gov.va.test.opioidtesttool;

import java.util.*;

import edu.stanford.smi.protege.model.*;

public class GlobalVars {
    public static final String DirDelimiter = "/";
	public static final String discontinue = "discontinue";
	public static Project project;
    public static KnowledgeBase kb = null;

    // init file parameters
    public static String initFile = null;
    public static Properties settings = null;
    public static String outputdir = null;
    public static String guidelineName = null;
    public static String database = null;
    public static String XMLDataFile = null;
    public static boolean cumulateDose = true;

    // Variables that are important for running environment standalone
    public static String kbURL = null;
    public static boolean standAlone = false;
    public static long kbLastModified = 0;

    // All patient data stored here internally
    // collection of PatientDataStore
    public static Collection<PatientDataStore> pds = null;
    // patient currently being editted from pds
    public static PatientDataStore currentPatient = null;    

    public static DataTypeOverRides dtor = null;
	public static String sessionTime = null;
	public static String kbLabel = null;
	public static String archivedir;
	public static String runtimeDate;
}
