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

public class EqualOrSubclassOf extends SingleArityPredicate {

  public EqualOrSubclassOf(Context context)  {
    super(context);
    _signature= new SignatureElement[2];
    _signature[0]=new SignatureElement(_typeManager.CLASS, false);
    _signature[1]=new SignatureElement(_typeManager.CLASS, false);
  }

 public int getArity()
 {
  return 2;
 }

  public String getPALName()
 {
  return "equal-or-subclass-of";
 }

  public boolean holds(Value[] arguments) throws TypeCoercionException
 {
  Cls subclass = (Cls)((arguments[0]).getActualValue(_typeManager.CLASS));
  Cls superclass = (Cls)((arguments[1]).getActualValue(_typeManager.CLASS));
  if ((null!=subclass) && (null!=superclass))
  {
   return (subclass.hasSuperclass(superclass) ||
    subclass.getName().equals(superclass.getName()));
  }
  return false;
 }

}