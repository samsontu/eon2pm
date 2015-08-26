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
package edu.stanford.smi.eon.guidelineinterpreter;
import edu.stanford.smi.eon.PCAServerModule.PCA_Session_Exception;

public class DharmaPaddaConstants {

 public final static String TopMedicalConditionClass = "Medical_Conditions_Class";
 public final static String TopMedicationClass = "Medications_Class";
 public final static  int greater_than = 0;
 public final static  int less_than = 1;
 public final static  int kind_of = 2;
 public final static  int equal = 4;
 public final static  int not_equal = 5;
 public final static  int greater_than_or_equal = 6;
 public final static  int less_than_or_equal = 7;
 public final static  int numeric_equal = 8;
 public final static  int numeric_nequal = 9;
 public final static  double float_error = 0.0001;
 public final static  String Authorized = "Authorized";
 public final static  String Actual = "Actual";
 public final static  String Medication = "Medication";
 public final static  String Qualitative_Observation = "Note_Entry";
 public final static  String Quantitative_Observation = "Numeric_Entry";
 public final static  String SessionTime = "Session_Time";

 public final static String DefaultMessageType = "Recommendation";
 public final static  String Recommend_Add = "Recommend_Add";
 public final static  String Recommend_Delete = "Recommend_Delete";
 public final static  String Recommend_Increase = "Recommend_Increase";
 public final static  String Recommend_Decrease = "Recommend_Decrease";
 public final static  String AND = "AND";
 public final static  String OR = "OR";
 public final static  String eq = "eq";
 public final static  String neq = "neq";
 public final static  String hasValue = "hasValue";
 public final static String subclass_of = "subclass_of";
 public final static String superclass_of = "superclass_of";
 public final static String member_of = "member_of";
 public final static String not_subclass_of = "not_subclass_of";
 public static String not_member_of = "not_member_of";
 public final static String EXCLUSION = "exclusion";
 public final static String NUMERATOR = "numerator";
public static final String meaurementPeriodName = "PerformanceMeasurementPeriod";
public static final String NOT = "NOT";
public static final String intersect = "intersect";
public static final String Sex = "Sex";
public static final String Race = "Race";
 public static String INCLUSION = "inclusion";
 public static String SUBPOPSELECTION = "subpopulation_selection";

public static String AnyNotTrue = "any_not_true";
public static String AnyTrue = "any_true";
public static String MISSINGDATA = "no current value";
public static String EXISTS = "exists";
public static String drugUsagesPropertyName = "drug_usages";
public static String NoAttribute = "no attribute";


 

 /*
 public static String convertMoodToInt(String mood) throws PCA_Session_Exception {
  if (mood.equals("Authorized")) return "Authorized";
  else if (mood.equals("Actual")) return "Actual";
  else if (mood.equals("Recommend_Add")) return "Recommend_Add";
  else if (mood.equals("Recommend_Delete")) return "Recommend_Delete";
  else if (mood.equals("Recommend_Incease")) return "Recommend_Incease";
  else throw new PCA_Session_Exception("Not legal mood  value "+mood);
 }
*/
 public static int convertCompareToInt(String compare) throws PCA_Session_Exception {
  if (compare.equals("greater_than")) return 0;
  else if (compare.equals("less_than")) return 1;
  else if (compare.equals("kind_of")) return 2;
  else if (compare.equals("superclass_of")) return 3;
  else if (compare.equals("equal")) return 4;
  else if (compare.equals("not_equal")) return 5;
  else if (compare.equals("greater_than_or_equal")) return 6;
  else if (compare.equals("less_than_or_equal")) return 7;
  else if (compare.equals("=")) return 8;
  else if (compare.equals("~=")) return 9;
  else throw new PCA_Session_Exception("No comparison operator "+compare);
 }
}