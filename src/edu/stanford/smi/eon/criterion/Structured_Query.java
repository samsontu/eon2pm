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

//Created on Wed Jun 20 12:58:02 PDT 2001
//Copyright Stanford University 2000

package edu.stanford.smi.eon.criterion;

import java.util.*;

import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.eon.guidelineinterpreter.*;
import edu.stanford.smi.eon.PCAServerModule.*;
import edu.stanford.smi.eon.kbhandler.*;
import edu.stanford.smi.eon.datahandler.*;
import edu.stanford.smi.eon.time.*;

import org.apache.log4j.*;
/** 
 */
public class Structured_Query extends Expression {

	public Structured_Query(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
	static  Logger  logger = Logger.getLogger(Structured_Query.class);

	public void setaggregation_operatorValue(String aggregation_operator) {
		ModelUtilities.setOwnSlotValue(this, "aggregation_operator", aggregation_operator);	}
	public String getaggregation_operatorValue() {
		return ((String) ModelUtilities.getOwnSlotValue(this, "aggregation_operator"));
	}

	public void setattributeValue(Instance attribute) {
		ModelUtilities.setOwnSlotValue(this, "attribute", attribute);	}
	public Instance getattributeValue() {
		return ((Instance) ModelUtilities.getOwnSlotValue(this, "attribute"));
	}
	public void setwhere_restrictionValue(Instance where_restriction) {
		ModelUtilities.setOwnSlotValue(this, "where_restriction", where_restriction);	}
	public Collection getwhere_restrictionValue() {
		return ((Collection) ModelUtilities.getOwnSlotValues(this, "where_restriction"));
	}

	public void settypeValue(Cls type) {
		ModelUtilities.setOwnSlotValue(this, "type", type);	}
	public Cls gettypeValue() {
		return ((Cls) ModelUtilities.getOwnSlotValue(this, "type"));
	}
//	__Code above is automatically generated. Do not change

	private Instance getMostRecent(Collection instances) {
		// Return most recent of instances; otherwise return null
		if (this.gettypeValue().hasSuperclass(this.getKnowledgeBase().getCls("EPR_Entity")))
			return (getMostRecentEPREntity(instances));
		else
			logger.error(this.getBrowserText()+" is a generalized structured query on instances that have no temporal attribute. Returning null");
		return null;
	}
	
	private Instance getMostRecentEPREntity(Collection instances) {
		// Return most recent of instances; otherwise return any instance
		TreeMap sort = new TreeMap();
		if (instances != null && !instances.isEmpty()){
			for (Iterator i=instances.iterator(); i.hasNext();) {
				Instance value = (Instance)i.next();
				if (value instanceof Observation ) {
					Instance tp = ((Observation) value).getValid_time();
					if (tp != null) {
						sort.put(new Integer(((Definite_Time_Point)tp).getsystem_timeValue()),
								value);
					}
				}	            
			}
			if (!sort.isEmpty()) {
				Object o = sort.lastKey();
				return (Instance) sort.get(o);
			} else {
				Instance value = null;
				for (Iterator i=instances.iterator(); i.hasNext();) {
					value= (Instance)i.next();
				}
				return value;
			} 
		}else return null;
	}


protected Object getProtegeAttributeValue( Instance instance, Slot slot) {
		logger.debug("Structured_Query.getProtegeAttributeValue instance=" +instance.toString()+
				" slot=" +slot.getName());
		Object attributeValue=null;
		if (instance != null) {
			if (instance.getDirectType().getTemplateSlotAllowsMultipleValues(slot)) {
				attributeValue = instance.getOwnSlotValues(slot);
			} else {
				attributeValue =  instance.getOwnSlotValue(slot);
			}
		}
		return attributeValue;
	}

	protected Expression getAttributeValue (GuidelineInterpreter glmanager, Instance instance, Slot slot) 
			throws PCA_Session_Exception {
		Expression attributeValue = null;
		Object value = getProtegeAttributeValue(   instance,  slot);
		if (value != null){
			logger.debug("Structured_Query.getAttributeValue value = "+value.toString());
			if (value instanceof Collection && !((Collection)value).isEmpty()) {
				attributeValue = (Set_Expression)glmanager.getDBmanager().createInstance(
						"Set_Expression");
				((Set_Expression)attributeValue).setset_elementsValue((Collection)value);
			} 
			if (value instanceof Expression ) 
				attributeValue = (Expression) value;
			else if (value instanceof java.lang.String) {
				attributeValue = (Qualitative_Constant)glmanager.getDBmanager().createInstance(
						"Qualitative_Constant");
				((Qualitative_Constant)attributeValue).setvalueValue((String)value);
			} else if (value instanceof java.lang.Integer) {
				attributeValue = (Numeric_Constant)glmanager.getDBmanager().createInstance(
						"Numeric_Constant");
				((Numeric_Constant)attributeValue).setvalueValue(Float.parseFloat(value.toString()));
			} else if (value instanceof java.lang.Float) {
				attributeValue = (Numeric_Constant)glmanager.getDBmanager().createInstance(
						"Numeric_Constant");
				((Numeric_Constant)attributeValue).setvalueValue(((Float)value).floatValue());
			} else {
				logger.warn("Querying for "+value.getClass().toString()+
						" results turned into a set");
				attributeValue = (Set_Expression)glmanager.getDBmanager().createInstance(
						"Set_Expression");
				Collection singleton = new ArrayList();
				singleton.add(value);
				((Set_Expression)attributeValue).setset_elementsValue(singleton);
			}
		}
		logger.debug("Structured_Query.getAttributeValue attributeValue = "+attributeValue.toString());
		return attributeValue;
	}



	protected Expression getAttributeValues (GuidelineInterpreter glmanager, Collection instances, Slot slot) 
			throws PCA_Session_Exception {
		Set_Expression attributeValues = null;
		Collection values = new ArrayList();
		if ((instances != null) && !instances.isEmpty()) {
			attributeValues = (Set_Expression)glmanager.getDBmanager().createInstance(
					"Set_Expression");
			for (Iterator i = instances.iterator(); i.hasNext() ;) {
				Instance instance = (Instance)i.next();
				Object value = getProtegeAttributeValue(instance,  slot);
				if (value != null) {
					if (value instanceof Collection) {
						values.addAll((Collection)value);
					} else values.add(value);
				}
			} 
			if (!values.isEmpty()) {
				attributeValues.setset_elementsValue(values);
				return attributeValues;
			} 
		}
		return null;
	}
	
	public Collection <Instance> getFilteredInstances(GuidelineInterpreter glmanager, WhereFilter filter) {
		Collection<Instance> instances = null;
		KBHandler kb = glmanager.getKBmanager();
		if (filter != null) {
			instances = (Collection<Instance>)(kb.findInstances(gettypeValue(), filter, glmanager));
			logger.debug("Structured_Query.evaluate_expression: number of query result instances "+instances.size());
		} else {
			// get all instances of 
			if (gettypeValue() == null) {
				logger.error("Structured query "+this.getBrowserText()+" has null type");
				return null;
			} else instances = kb.getKB().getInstances(gettypeValue());
		}
		if (this.gettypeValue().hasSuperclass(this.getKnowledgeBase().getCls("EPR_Entity"))){
			Collection<Instance> caseInstances = new ArrayList<Instance>();
			for (Instance inst : instances) {
				if (glmanager.getDBmanager().getCaseID().equals(((EPR_Entity)inst).getPatient_id()))
					caseInstances.add(inst);
			}
			instances = caseInstances;
		}
		return instances;
	}
	

	public Expression evaluate_expression(GuidelineInterpreter glmanager)
			throws PCA_Session_Exception {
		java.util.Date startTime = new java.util.Date();
		Collection instances = null;
		WhereFilter filter=null;
		Collection whereRestrictions = getwhere_restrictionValue();
		String aggregator = getaggregation_operatorValue();
		Instance attribute = getattributeValue();
		Expression result =  (Expression)glmanager.evalManager.ask(this);

		if (result == null) {
			logger.debug("Structured_Query.evaluate_expression "+getlabelValue());

			if ((whereRestrictions != null) && !(whereRestrictions.isEmpty())){
				logger.debug(whereRestrictions.toString());
				Collection ANDRestrictions = new ArrayList();
				for (Iterator i = whereRestrictions.iterator(); i.hasNext();) {
					ANDRestrictions.add(((Filter)i.next()).constructExpression(glmanager));
				}
				logger.debug(ANDRestrictions.toString());
				filter = new WhereFilter(DharmaPaddaConstants.AND, ANDRestrictions);
			}
			instances = getFilteredInstances(glmanager, filter);

			if (aggregator != null){
				if (aggregator.equals("most_recent")) {
					Instance  mostRecent = getMostRecent(instances);
					if (mostRecent != null) {
						if (attribute != null) 
							result = getAttributeValue(glmanager, mostRecent, (Slot)attribute);
						else {
							logger.debug("With most_recent aggregator, Query attribute = null");
							result = (Set_Expression)glmanager.getDBmanager().createInstance(
									"Set_Expression");
							Collection singleton = new ArrayList();
							singleton.add(mostRecent);
							((Set_Expression)result).setset_elementsValue(singleton);
						}
					} else logger.debug("With most_recent aggregator, Query result = null");
				} else if (aggregator.equals("count")) {
					result = (Numeric_Constant)glmanager.getDBmanager().createInstance("Numeric_Constant");
					((Numeric_Constant)result).setvalueValue((float) instances.size());
					/*				} else if (aggregator.equals("minimum")){

			} else if (aggregator.equals("average")){
					 */					
				} else {
					logger.error("Unsupported aggregation_operator "+aggregator);
					throw new PCA_Session_Exception("Unsupported aggregation_operator "+aggregator);
				}

			} else {
				if (instances != null && !instances.isEmpty()){
					if (attribute != null) {
						result = getAttributeValues(glmanager, instances, (Slot)attribute) ;             
					} else {
						result = (Set_Expression)glmanager.getDBmanager().createInstance(
								"Set_Expression");
						((Set_Expression)result).setset_elementsValue(instances);
					}
					// aggregation makes sense only if there is a selection attribute
				} 
			} 
			if (result != null) {
				glmanager.evalManager.tell(this, result);
			}
		}
		java.util.Date stopTime = new java.util.Date();
		logger.debug("Structured Query evaluate_expression() "+getlabelValue()+": taking @@@@@@@@@ "+ 
				(stopTime.getTime() - startTime.getTime())+" @@@@@@@@@ milliseconds" );
		return result;
	}
	
	public String getSupport(Collection queryResults) {
		String support = "";
		boolean first = true;
		try {
			if (this.getattributeValue() == null) {
				for (Instance inst : (Collection<Instance>)queryResults) {
					String data = "";
					boolean firstData = true;
					String stringValue = "";
					if (this.getwhere_restrictionValue() != null) {
						for (Filter f : (Collection<Filter>)this.getwhere_restrictionValue()) {
							try {
								Collection values = inst.getOwnSlotValues(((Slot)((Comparison_Filter)f).getattributeValue()));
								if (values.size() <= 1) {
									for (Object o : values) {
										stringValue = o.toString();
									}
								}
								else stringValue = values.toString();
								if (firstData) {
									data = stringValue;
									firstData = false;
								} else
									data = data+","+stringValue;
							} catch (Exception e) {
								continue;
							}
						}
					}
					if (first) 	{				
						support= this.gettypeValue().getBrowserText()+"("+data+")";
						first = false;
					} else
						support = support+",("+data+")";
				}
			}
		} catch (Exception e){
			//Wrong query result
		}
		return support;
	}

}
