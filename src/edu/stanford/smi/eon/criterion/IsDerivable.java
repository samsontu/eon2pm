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
package edu.stanford.smi.eon.criterion;
import edu.stanford.smi.protegex.pal.engine.*;
import edu.stanford.smi.protegex.pal.parser.*;
import edu.stanford.smi.protegex.pal.relations.*;
import edu.stanford.smi.protegex.pal.language.*;
import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.eon.criterion.*;
import edu.stanford.smi.eon.PCAServerModule.*;
import edu.stanford.smi.eon.util.*;
import edu.stanford.smi.eon.guidelineinterpreter.GuidelineInterpreter;

import java.util.*;

import org.apache.log4j.*;
/**
 * Defines a new PAL predicate "is-derivable". The single arity predicate takes
 * a Protege-2000 class (i.e. an instance of Java Cls class) as argument and
 * checks to see if it has a "DiagnosticCriteria" own slot. If yes, the
 * value of the DiagnosticCriteria is evaluated. If that criterion evaluates to
 * true, then the predicate returns "true".
 */

public class IsDerivable extends SingleArityPredicate {

 public IsDerivable(Context context) {
  super(context);
  _signature= new SignatureElement[1];
  _signature[0]=new SignatureElement(_typeManager.CLASS, false);
  }
 
 static  Logger  logger = Logger.getLogger(IsDerivable.class);

 public String getPALName()
 {
  return "is-derivable";
 }

  public int getArity()
 {
  return 1;
 }

 public boolean holds(Value[] arguments) throws TypeCoercionException
 {
  Object argument = null;
  GuidelineInterpreter guidelineManager = GuidelineInterpreter.currentGuidelineInterpreter;

  if (null==arguments)
  {
   return false;
  }
  argument = (Object)((arguments[0]).getActualValue(_typeManager.STRING));
  if (null!=argument)
  {
	  Cls classInstance = _context.kb.getCls((String)argument);
	  Criteria_Evaluation criteriaEvaluation = null;
	  //Checks that there is a definition associated with this class
	  if (classInstance != null) {
		  logger.debug("IsDerivable holds: class="+classInstance.getName());
		  if (guidelineManager == null) {
			  logger.error("IsDerivable holds: guidelineManager is null");
			  return false;
		  }
		  Slot definitionSlot = (Slot)classInstance.getKnowledgeBase().getFrame("DiagnosticCriteria");
		  if (classInstance.hasOwnSlot(definitionSlot)) {
			  Collection<Criterion> definition = classInstance.getOwnSlotValues(definitionSlot);
			  if (definition != null) {
				  logger.debug("IsDerivable holds: class="+classInstance.getName()+" has definition");
				  for (Criterion def : definition) {
					  try {
						  criteriaEvaluation = def.evaluate(guidelineManager, true);
						  if (criteriaEvaluation !=null) {
							  logger.debug("IsDerivable holds: class="+classInstance.getName()+ " "+def.getBrowserText()+
									  " evaluation "+criteriaEvaluation.truth_value);
							  if (criteriaEvaluation.truth_value.equals(Truth_Value._true)) {
								  return true;
							  }
						  }
					  } catch (PCA_Session_Exception e) {
						  logger.debug("IsDerivable holds: class="+classInstance.getName()+ " "+def.getBrowserText()+
								  " evaluation throws an exception. Continuing to evaluate PAL criterion/query");
						  e.printStackTrace();
					  }
				  }
			  }
		  }
	  }
  }
  return false;
 }
}


