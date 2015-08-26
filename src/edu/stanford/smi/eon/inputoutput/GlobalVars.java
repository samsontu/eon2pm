package edu.stanford.smi.eon.inputoutput;

import java.util.*;

import edu.stanford.smi.protege.model.*;
import gov.va.test.opioidtesttool.PatientDataStore;

public class GlobalVars {
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

    // Variables that are important for running environment standalone
    public static String kbURL = null;
    public static boolean standAlone = false;
    public static long kbLastModified = 0;
    public static int JAVAFormat = 0;
    public static int XMLFormat = 1;

    // All patient data stored here internally
    // collection of PatientDataStore
    public static Collection pds = null;
    // patient currently being editted from pds
    public static PatientDataStore currentPatient = null;    

   
}
