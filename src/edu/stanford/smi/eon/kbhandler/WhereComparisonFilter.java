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


package edu.stanford.smi.eon.kbhandler;
import java.util.*;
import edu.stanford.smi.eon.util.*;
import edu.stanford.smi.eon.datahandler.Constants;
import edu.stanford.smi.eon.guidelineinterpreter.*;
import edu.stanford.smi.eon.time.*;
import edu.stanford.smi.eon.criterion.*;
import edu.stanford.smi.protege.model.*;
import org.apache.log4j.*;

public class WhereComparisonFilter implements AbstractWhereFilter {
	public String attribute, comparison_operator;
	public Object value;

	static Logger logger =  Logger.getLogger(WhereComparisonFilter.class);
	public WhereComparisonFilter(String attribute, String comparison_operator,
			Object value) {
		this.attribute = attribute;
		this.comparison_operator = comparison_operator;
		this.value = value;
	}

	// A comparison filter has the form of attribute operator value_expression
	// Check to see if value of *attribute* of argument instance is (eq/neq/member_of/equal_or_subclass_of)
	// *value*

	public boolean evaluate (Instance instance) throws KnowledgeBaseException {
		return evaluate(instance, null);
	}
	public boolean evaluate(Instance instance, GuidelineInterpreter guidelineManager) throws KnowledgeBaseException {
		KnowledgeBase kb = instance.getKnowledgeBase();
		if (value == null) {
			logger.warn("WhereComparisonFilter.evaluate attribute: "+attribute+
				" opeator: "+ comparison_operator + " has null value");
			return false;
//			throw new KnowledgeBaseException("WhereComparisonFilter.evaluate attribute: "+attribute+
//					" opeator: "+ comparison_operator + " has null value");
		}
		// NoAttribute means comparing the instance (eq, neq or  a member_of, subclass) to the RHS
		if ((attribute.equals(DharmaPaddaConstants.NoAttribute)) && 
				!((this.comparison_operator.equals(DharmaPaddaConstants.eq) || 
						this.comparison_operator.equals(DharmaPaddaConstants.neq) || 
						this.comparison_operator.equals(DharmaPaddaConstants.member_of)) || 
						this.comparison_operator.equals(DharmaPaddaConstants.subclass_of) ||
						this.comparison_operator.equals(DharmaPaddaConstants.superclass_of))){
			logger.error("No attribute and comparison operator "+comparison_operator+ " are not compatible");
			return false;
		}
		Slot compareSlot = (attribute.equals(DharmaPaddaConstants.NoAttribute)) ? null : kb.getSlot(attribute);
		boolean returnValue = false;
		Cls cls = instance.getDirectType();
		logger.debug("WhereComparisonFilter.evaluate attribute: "+attribute+
				" opeator: "+ comparison_operator + " value: "+ (value == null ? "null" : value.toString()) 
				);
		boolean cardinalityMultiple = (attribute.equals(DharmaPaddaConstants.NoAttribute)) ? false : cls.getTemplateSlotAllowsMultipleValues(compareSlot);
		if (comparison_operator.equals(DharmaPaddaConstants.hasValue)) {
			returnValue = (instance.getOwnSlotValue(compareSlot) != null);
			logger.debug("WhereComparisonFilter.evaluate value slot hasValue "+returnValue);
			return returnValue;
		} else {
			if (value instanceof Expression) {
				try {
					value = ((Expression)value).evaluate_expression(guidelineManager);
				} catch (Exception e) {
					logger.error("Exception evaluating "+value.toString());
					e.printStackTrace();
					return false;
				}
			}
			if (cardinalityMultiple) {
				// only eq & neq are possible comparison operators when LHS is a collection
				// RHS must be a set
				if (!(value instanceof Set_Expression)) returnValue = false;
				else {
					Collection slotValue = ModelUtilities.getOwnSlotValues(instance , attribute);
					returnValue = collectionComparison(slotValue, comparison_operator, ((Set_Expression)value).getset_elementsValue());
				}
			} else {//cardinality single
				Object slotValue = (compareSlot != null) ? ModelUtilities.getOwnSlotValue(instance , attribute) : instance;
				if (slotValue == null) {
					if (value != null) {
						logger.debug("WhereComparisonFilter Instance "+instance.getBrowserText()+"/"+ instance.getName()+
								" gives null value for "+ attribute+ " value = "+value.toString());
					} else {
						logger.warn("WhereComparisonFilter Instance "+instance.getBrowserText()+"/"+ instance.getName()+
								" gives null value for "+ attribute+ " and null comparison value. Returning false");
						return false;
					}
					//throw new KnowledgeBaseException("");
					if (comparison_operator.equals("neq"))
						returnValue = (slotValue != value);
					else if (comparison_operator.equals("eq")) 
						returnValue = (slotValue == value);
				} else {
					logger.debug("WhereCoomparisonFilter.evaluate "+instance.getName()+ ": "+ attribute +"["+slotValue+"]"+
							" "+this.comparison_operator+ " "+ this.value);

					if (slotValue instanceof Expression) {
						try {
							slotValue = ((Expression)slotValue).evaluate_expression(guidelineManager);
						} catch (Exception e) {
							logger.error("Exception evaluating "+slotValue.toString());
							e.printStackTrace();
							return false;
						}
					}
					//where  comparison_operator can be eq, neq subclass_of or member_of
					//	superclass_of <=, <, >, >= or =
					returnValue = compare(slotValue, comparison_operator, value, kb);
				}
			}
		}
		logger.debug("WhereComparisonFilter.evaluate returns " +returnValue);
		return returnValue;
	}

	boolean compare (Object slotValue, String comparison_operator, Object value, KnowledgeBase kb){
		boolean returnValue = false;
		if ((comparison_operator.equals("eq")) || (comparison_operator.equals("neq"))) {
			returnValue = eqneqComparison(slotValue, comparison_operator, value);
		} else if ((comparison_operator.equals("subclass_of")) || (comparison_operator.equals("superclass_of"))) {
			returnValue = subSuperComparison(slotValue, comparison_operator, value, kb);
		} else if (comparison_operator.equals(DharmaPaddaConstants.member_of)){
			returnValue = memberOfComparison(slotValue, comparison_operator, value);
		} else if (comparison_operator.equals(DharmaPaddaConstants.intersect)) {
			//slotValue must be a Cls. Take intersection of subclasses of Cls and value
			try {  
				Collection<Cls> subs = new ArrayList<Cls>();
				subs.add((Cls)slotValue);
				subs.addAll(((Cls)slotValue).getSubclasses());
				if (value instanceof Set_Expression) 
					return collectionComparison(subs, comparison_operator, ((Set_Expression)value).getset_elementsValue());
				else
					return collectionComparison(subs, comparison_operator, (Collection)value);
			} catch (Exception e) {
				logger.error(slotValue.toString()+" or "+value.toString()+" does not resolve to a collection");
				return false;
			}
		} else {
			returnValue = quantitativeComparison(slotValue, comparison_operator, value, kb);
		}
		return returnValue;
	}

	boolean quantitativeComparison (Object slotValue, String comparison_operator, Object value, KnowledgeBase kb){
		if (value instanceof Abstract_Time_Point) {
			return temporalComparison( slotValue,  comparison_operator,  value,  kb);
		}	else return numericComparison(slotValue,  comparison_operator,  value,  kb);
	}

	boolean numericComparison(Object slotValue, String comparison_operator, Object value, KnowledgeBase kb) {
		float floatSlotValue;
		float floatValue;
		if (value instanceof Numeric_Constant) {
			floatValue = ((Numeric_Constant)value).getvalueValue();
		} else if (value instanceof java.lang.Float) 
			floatValue = ((java.lang.Float)value).floatValue();
		else if (value instanceof java.lang.Integer)
			floatValue = ((java.lang.Integer)value).intValue();
		else {
			logger.error("WhereComparisonFilter.numericComparison  value "+
					value.toString()+" is not numeric"); 
			return false;
		}
		if (slotValue instanceof java.lang.Float) {
			floatSlotValue = ((java.lang.Float)slotValue).floatValue();
			return numericComparison0(floatSlotValue, comparison_operator, floatValue);
		} else if (slotValue instanceof java.lang.Integer) {
			floatSlotValue = ((java.lang.Integer)slotValue).intValue();
			return numericComparison0(floatSlotValue, comparison_operator, floatValue);
		} else {
			logger.error("WhereComparisonFilter.numericComparison slot value "+
					slotValue.toString()+" is not numeric");
		}
		return false;
	}

	boolean numericComparison0(float floatSlotValue, String comparison_operator, float floatValue) {
		logger.debug("WhereComparisonFilter.numericComparison0: "+ floatSlotValue+ " "+ 
				comparison_operator+" "+floatValue);
		if (comparison_operator.equals(">"))
			return floatSlotValue > floatValue;
			else if (comparison_operator.equals(">="))
				return floatSlotValue >= floatValue;
				else if (comparison_operator.equals("="))
					return floatSlotValue == floatValue; 
				else if (comparison_operator.equals("<"))
					return floatSlotValue < floatValue;
				else if (comparison_operator.equals("<="))
					return floatSlotValue <= floatValue;
				else logger.error("Unknown comparison operator "+comparison_operator);
		return false;
	}

	boolean temporalComparison(Object slotValue, String comparison_operator, Object value, KnowledgeBase kb){
		boolean returnValue = false;
		if (!(slotValue instanceof Absolute_Time_Point)) 
			logger.error("WhereComparisonFilter.temporalComparison slotValue "+slotValue.toString()+" is not a time point");
		else if (!(value instanceof Absolute_Time_Point))
			logger.error("WhereComparisonFilter.temporalComparison value "+value.toString()+" is not a time point");
		else	{
			logger.debug("WhereCoomparisonFilter.evaluate "+  attribute +"["+slotValue+"]"+
					" "+this.comparison_operator+ " "+ value);
			int slotValueTime= ((Absolute_Time_Point)slotValue).getsystem_timeValue();
			int valueTime = ((Absolute_Time_Point)value).getsystem_timeValue();
			returnValue = numericComparison0(slotValueTime, comparison_operator, valueTime);
		}
		return returnValue;
	}

	boolean subSuperComparison (Object slotValue, String comparison_operator, Object value, KnowledgeBase kb){
		boolean returnValue = false;
		Cls slotValueCls = null;
		Cls valueCls = null;
		if (value instanceof Set_Expression) {
			if (((Set_Expression)value).getset_elementsValue().size() == 1) {
				value = ((Set_Expression)value).getset_elementsValue().toArray()[0];
			} else return false;
		}
		if (slotValue instanceof Cls) slotValue = ((Cls)slotValue).getName();
		if (value instanceof Cls) value = ((Cls)value).getName();
		else if ((value instanceof Instance) && ((Instance)value).getDirectType().getName().equals("Qualitative_Constant")) {
			value = ((Qualitative_Constant)value).getvalueValue();
		} 
		if (slotValue instanceof String) slotValueCls = kb.getCls((String)slotValue);
		else slotValueCls = null;
		if (value instanceof String) valueCls = kb.getCls((String)value);
		else valueCls = null;
		if ((slotValueCls != null) && (valueCls != null)) {
			Collection slotValueSubclasses = slotValueCls.getSubclasses();
			Collection valueSubclasses = valueCls.getSubclasses();
			if (comparison_operator.equals(DharmaPaddaConstants.subclass_of)) {
				returnValue = valueSubclasses.contains(slotValueCls) ||
						valueCls.equals(slotValueCls) ;
			} else if (comparison_operator.equals(DharmaPaddaConstants.superclass_of)) {
				returnValue = slotValueSubclasses.contains(valueCls) || 
						valueCls.equals(slotValueCls) ;
			} else logger.error("WhereComparisonFilter.subSuperComparison incorrect comparison operator: "+comparison_operator);
		} else {
			//logger.debug("WhereComparisonFilter "+slotValue+" or "+value +" not a class ");
			returnValue =  false;
		}
		return returnValue;
	}

	boolean memberOfComparison(Object slotValue, String comparison_operator, Object value) {
		boolean returnValue = false;
		Collection valueCollection = null;
		if (value instanceof Collection)
			valueCollection = (Collection)value;
		else if (value instanceof Set_Expression)
			valueCollection = ((Set_Expression)value).getset_elementsValue();
		else return false;
		if (valueCollection !=null) 
			if (slotValue != null) {
				for (Object collectionValue : valueCollection) {
					if (collectionValue instanceof Cls) {
						if (subSuperComparison(slotValue, DharmaPaddaConstants.subclass_of,  collectionValue, ((Cls) collectionValue).getKnowledgeBase())) {
							returnValue = true;
							break;
						}
					} else
					if (eqneqComparison(slotValue, DharmaPaddaConstants.eq, collectionValue)) {
						returnValue = true;
						break;
					}
				}
			}
		return returnValue;
	}

	boolean eqneqComparison(Object slotValue, String comparison_operator, Object value) {
		boolean returnValue = false;
		if (slotValue instanceof Cls) slotValue = ((Cls)slotValue).getName();
		if (slotValue instanceof Qualitative_Constant) slotValue = ((Qualitative_Constant)slotValue).getvalueValue();
		if (value instanceof Cls) value = ((Cls)value).getName();
		if (value instanceof Qualitative_Constant) value = ((Qualitative_Constant)value).getvalueValue();
		if (comparison_operator.equals("eq")) {
			if (slotValue instanceof String)
				returnValue = slotValue.equals(value);
			else returnValue = (slotValue == value);
		} else {
			if (comparison_operator.equals("neq"))  {
				if (slotValue instanceof String) {
					returnValue = ((slotValue.equals(value)) ? false : true);
				} else {
					returnValue = ((slotValue == value) ? false : true);
				}
			}
		}
		return returnValue;
	}

	boolean collectionComparison(Collection lhs, String compare, Collection rhs) {
		//The only comparison operators that makes sense are intersect, subset_of, superset_of, eq
		// each set element may be a class, an instance, or a string
		if (lhs.isEmpty()) {
			if (compare.equals("intersect")) return false;
			if (rhs.isEmpty()) return true;
			else //rhs is not empty
				if (compare.equals("subset_of")) return true;
				else return false;
		} else if (rhs.isEmpty()) 
			if (compare.equals("superset_of")) return true;
			else return false;
		else { //lhs & rhs not empty
			if (((lhs.toArray()[0] instanceof String) && rhs.toArray()[0] instanceof String) || 
					((lhs.toArray()[0] instanceof Instance) && rhs.toArray()[0] instanceof Instance)){
				return stringInstanceCollectionComparison(lhs, compare, rhs);
			} else {
				Collection<String> lhsStrings = new ArrayList<String>();
				Collection<String> rhsStrings = new ArrayList<String>();
				if (lhs.toArray()[0] instanceof Cls) {
					for (Cls cls : (Collection<Cls>)lhs) {
						lhsStrings.add(cls.getName());
						for (Cls sub: (Collection<Cls>)cls.getSubclasses())
							lhsStrings.add(sub.getName());
					}
				} else if (lhs.toArray()[0] instanceof String)
					lhsStrings = lhs;
				if (rhs.toArray()[0] instanceof Cls) {
					for (Cls cls : (Collection<Cls>)rhs) {
						rhsStrings.add(cls.getName());
						for (Cls sub: (Collection<Cls>)cls.getSubclasses())
							rhsStrings.add(sub.getName());
					}
				} else if (rhs.toArray()[0] instanceof String)
					rhsStrings = rhs;
				if ((rhsStrings.isEmpty()) || rhsStrings.isEmpty()) return false;
				else return stringInstanceCollectionComparison(lhsStrings, compare, rhsStrings);
			}
		}
	}

	boolean stringInstanceCollectionComparison(Collection lhs, String compare, Collection rhs) {
		if (compare.equals("superset_of")) 
			return lhs.containsAll(rhs);
		else if (compare.equals("subset_of"))
			return rhs.contains(lhs);
		else if (compare.equals("eq"))
			return (rhs.contains(lhs) && lhs.contains(rhs));
		else if (compare.equals("intersect")) {
			Collection modifiableCollection = new ArrayList();
			modifiableCollection.addAll(rhs);
			modifiableCollection.retainAll(lhs);
			return !modifiableCollection.isEmpty();
		} else return false;
	}
}

