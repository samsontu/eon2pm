package gov.va.athena.advisory;

/**
 * Generated by Protege (http://protege.stanford.edu).
 * Source Class: Conclusion
 *
 * @version generated on Thu Jul 05 01:40:52 GMT 2012
 */
public interface Missing_Data extends Advisory_Component {

    // Slot criterion_id

    String getCriterion_id();

    boolean hasCriterion_id();

    void setCriterion_id(String newCriterion_id);
    
    // Slot criterion_evaluation_result (can be true,false,unknown)
    // In addition, for performance measures, 
    //   if criterion type = denominator,  
    //        criterion_evaluation_result = false (for exclusion)
    //   if criterion type = exclusion
    //        criterion_evaluation_result = true (for exclusion)
    String getCriterion_evaluation_result();

    boolean hasCriterion_evaluation_result();

    void setCriterion_evaluation_result(String newCriterion_evaluation_result);            
    
    // Slot data

    String getParameter();

    boolean hasParameter();

    void setParameter(String newData);    

    
}
