package gov.va.athena.advisory.impl;

import java.util.*;

import gov.va.athena.advisory.*;

/**
 * Generated by Protege (http://protege.stanford.edu).
 * Source Class: Drug_Recommendation
 *
 * @version generated on Wed Jun 06 18:58:08 GMT 2012
 */
public class DefaultDrug_Recommendation extends DefaultAdvisory_Component implements Drug_Recommendation {

    /* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((drug_action_type == null) ? 0 : drug_action_type.hashCode());
		result = prime * result
				+ ((preference == null) ? 0 : preference.hashCode());
		result = prime * result
				+ ((specific_drug == null) ? 0 : specific_drug.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof DefaultDrug_Recommendation)) {
			return false;
		}
		DefaultDrug_Recommendation other = (DefaultDrug_Recommendation) obj;
		if (drug_action_type == null) {
			if (other.drug_action_type != null) {
				return false;
			}
		} else if (!drug_action_type.equals(other.drug_action_type)) {
			return false;
		}
		if (preference == null) {
			if (other.preference != null) {
				return false;
			}
		} else if (!preference.equals(other.preference)) {
			return false;
		}
		if (specific_drug == null) {
			if (other.specific_drug != null) {
				return false;
			}
		} else if (!specific_drug.equals(other.specific_drug)) {
			return false;
		}
		return true;
	}

	protected String drug_action_type = null;
    protected String preference = null;
    protected Collection<String> specific_drug = new ArrayList();
    protected Collection<Action> collateral_action = new ArrayList();
	protected int fine_grain_priority = 0;

    public DefaultDrug_Recommendation() {
    }

    // Slot case_id
    // Slot drug_action_type
    public String getDrug_action_type() {
        return drug_action_type;
    }

    public boolean hasDrug_action_type() {
        return drug_action_type != null;
    }

    public void setDrug_action_type(String newDrug_action_type) {
        drug_action_type = newDrug_action_type;
    }

    // Slot fine_grain_priority
    public int getFine_grain_priority() {
		return fine_grain_priority ;
    }

    public boolean hasFine_grain_priority() {
        return (fine_grain_priority >= 0);
    }

    public void setFine_grain_priority(int newFine_grain_priority) {
        fine_grain_priority = newFine_grain_priority;
    }


    // Slot co
    public Collection<Action> getCollateral_action() {
        if (collateral_action.isEmpty()) {
            return null;
        } else {
            return collateral_action;
        }
    }

    public boolean hasCollateral_action() {
        return !collateral_action.isEmpty();
    }

    public void addCollateral_action(Action newCollateral_action) {
        collateral_action.add(newCollateral_action);
    }

    public void removeCollateral_action(Action oldAction) {
        collateral_action.remove(oldAction);
    }

    public void setCollateral_action(Collection<Action> newAction) {
        collateral_action.addAll(newAction);
    }

    // Slot preference
    public String getPreference() {
        return (String) preference;
    }

    public boolean hasPreference() {
        return preference != null;
    }

    public void setPreference(String newPreference) {
        preference = newPreference;
    }

    // Slot specific_drug
    public Collection<String> getSpecific_drug() {
        if (specific_drug.isEmpty()) {
            return null;
        } else {
            return specific_drug;
        }
    }

    public boolean hasSpecific_drug() {
        return !specific_drug.isEmpty();
    }

    public void addSpecific_drug(String newSpecific_drug) {
        specific_drug.add(newSpecific_drug);
    }

    public void removeSpecific_drug(String oldSpecific_drug) {
        specific_drug.remove(oldSpecific_drug);
    }

    public void setSpecific_drug(Collection<String> newSpecific_drug) {
        specific_drug.addAll(newSpecific_drug);
    }
    
   public boolean equals(Drug_Recommendation aDrugRec) {
  
      // No specific drug name for either drug rec, or both, means these drug recommendations
      // can't be equal
      if (specific_drug == null || aDrugRec.getSpecific_drug() == null)
         return false;    
      
      if (specific_drug.equals(aDrugRec.getSpecific_drug()) &&
          drug_action_type.equals(aDrugRec.getDrug_action_type()) && 
          preference.equals(aDrugRec.getPreference()) )
         return true;
      else
         return false;
    
   }
}
