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
import java.util.*;
import edu.stanford.smi.eon.util.*;

public class IsClassName extends SingleArityPredicate {

 public IsClassName(Context context) {
  super(context);
  _signature= new SignatureElement[1];
  _signature[0]=new SignatureElement(_typeManager.STRING, false);
 }

 public String getPALName()
 {
  return "isa-classname";
 }

  public int getArity()
 {
  return 1;
 }

 public boolean holds(Value[] arguments) throws TypeCoercionException
 {
  Object argument = null;
  if (null==arguments)
  {
   return false;
  }
  argument = (Object)((arguments[0]).getActualValue(_typeManager.STRING));
  if (null!=argument)
  {
    Cls classInstance = _context.kb.getCls((String)argument);
    return (classInstance != null);
  }
  return false;
 }

}