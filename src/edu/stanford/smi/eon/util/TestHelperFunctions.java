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
package edu.stanford.smi.eon.util;
import junit.framework.*;

public class TestHelperFunctions extends TestCase {

  public TestHelperFunctions(String name) {
    super(name);

  }
  public void testStringReplacement() {
    String testString1 = "this is a test of test";
    String expected1 = "this is a success of success";
    String testString2 = "foobar2foo";
    String expected2 = "fxxbar2fxx";

    assertTrue(expected1.equals(HelperFunctions.replaceSubstring(
      testString1, "test", "success")));
    assertTrue(expected2.equals(HelperFunctions.replaceSubstring(
      testString2, "o", "x")));
  }

  public static Test suite() {
    TestSuite suite= new TestSuite();
    suite.addTest(new TestHelperFunctions("testStringReplacement"));
    return suite;
  }
}