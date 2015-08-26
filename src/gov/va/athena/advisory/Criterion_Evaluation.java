package gov.va.athena.advisory;

import java.util.*;

/**
 * Generated by Protege (http://protege.stanford.edu).
 * Source Class: Conclusion
 *
 * @version generated on Thu Jul 05 01:40:52 GMT 2012
 */
public interface Criterion_Evaluation extends Advisory_Component {

    // Slot criterion_id

    String getCriterion_id();

    boolean hasCriterion_id();

    void setCriterion_id(String newCriterion_id);
    

    // Slot kb_goal_id

    String getKb_goal_id();

    boolean hasKb_goal_id();

    void setKb_goal_id(String newKb_goal_id);    
    
    // Slot criterion_type

    String getCriterion_type();

    boolean hasCriterion_type();

    void setCriterion_type(String newCriterion_type);
    

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

    String getData();

    boolean hasData();

    void setData(String newData);    

    // Slot missing data
    public Collection<Missing_Data> getMissing_Data();
    public boolean hasMissing_Data();
    public void addMissing_Data(Missing_Data newMissing_Data);
    public void removeMissing_Data(Missing_Data existingMissing_Data);
    public void setMissing_Data(Collection<Missing_Data> listMissing_Data);
    
}
