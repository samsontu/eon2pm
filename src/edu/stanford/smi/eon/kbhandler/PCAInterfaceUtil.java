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
import edu.stanford.smi.eon.PCAServerModule.*;
import edu.stanford.smi.protege.model.*;
import java.util.*;

public class PCAInterfaceUtil {

  public PCAInterfaceUtil() {
  }

  public static Preference mapPreference (String preference) {
    if (preference == null) {
      return Preference.neutral;
    } else {
      if (preference.equals("prefered")) {
        return Preference.preferred;
      } else {
        if (preference.equals("greyed_out")) {
          return Preference.ruled_out;
        } else return Preference.neutral;
      }
    }
  }

  public static SelectionAlternatives mapSelectionAlternatives(String alternative)
     throws PCA_Session_Exception {
    if (alternative == null)
     throw new PCA_Session_Exception(alternative + " is not a legal term");
    else {
      if (alternative.equals("all_of")){
        return SelectionAlternatives.all_of;
      } else if (alternative.equals("some_of")) {
        return SelectionAlternatives.some_of;
      } else if (alternative.equals("one_of")) {
        return SelectionAlternatives.one_of;
      } else throw new PCA_Session_Exception(alternative + " is not a legal term");
    }
  }

  public static Logical_Operator mapLogicalOperator (String op)
    throws PCA_Session_Exception {
    if (op == null) return Logical_Operator.ATOMIC;
    else if (op.equals("AND")) return Logical_Operator.AND;
      else if (op.equals("OR")) return Logical_Operator.OR;
          else if (op.equals("NOT")) return Logical_Operator.NOT;
    else throw new PCA_Session_Exception("Illegal operator " + op);
  }

  public static boolean mapTruthValue (Truth_Value tvalue)
    throws PCA_Session_Exception {
    if (tvalue != null) {
      if (tvalue.equals(Truth_Value._true)) return true;
      else return false;
    } else throw new PCA_Session_Exception("Null Truth_Value");
  }

  /** cls@Strings takes a collection of Cls and returns a collection
      of class names as strings
  */
  public static Collection cls2Strings(Collection clsCollection) {
    Collection stringList = new ArrayList();
    for (Iterator i=clsCollection.iterator(); i.hasNext();) {
      stringList.add(((Cls) i.next()).getName());
    }
    return stringList;
  }
}
