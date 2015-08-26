package gov.va.test.opioidtesttool;

import java.util.*;

import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.model.Project;

import edu.stanford.smi.eon.PCAServerModule.*;
import edu.stanford.smi.eon.kbhandler.KBHandler;

public class DataTypeOverRides {
    Hashtable over_rides = null;

    public DataTypeOverRides() {
	over_rides = new Hashtable();
    }
    
    /* 
     * add a new over ride
     */
    public void add(String or_string) {	
	String param[] = or_string.split(",");
	if (param.length != 2 ) 
	    throw new RuntimeException("Error in configuration ini file, DATA_TYPE_OVERRIDE should be defined with 2 parameters; domain_term, PCA_DATA_TYPE");
	over_rides.put(new String(param[0]), new String(param[1]));
    }
    
    /*
     * Get PCA data type
     * for a particular domain term
     * 
     * This method should technically check 
     * if dt is a sub-class of some other over-ridden
     * term in the KB -- unfortunately we aren't doing that
     */
    public String get(String dt) {
	if ( ! over_rides.containsKey(dt) ) 
	    return null;
	else 
	    return ((String) over_rides.get(dt));
    }
}

