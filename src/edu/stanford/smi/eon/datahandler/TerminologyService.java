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
package edu.stanford.smi.eon.datahandler;
import edu.stanford.smi.protege.model.*;
import java.util.*;
import edu.stanford.smi.eon.kbhandler.KBHandler;
import org.apache.log4j.*;


public class TerminologyService {
  private boolean getICD9=false;
  private HashMap ICD9Code = null;
  private KnowledgeBase kb = null;

  public TerminologyService(KBHandler kbHandler) {
    this.kb = kbHandler.getKB();
  }
  
  static Logger logger=Logger.getLogger(TerminologyService.class);

  public String getTermDescription(String termCode, String termSystem) {
    return null;
  }

  public String translateTerm(String sourceCode, String sourceSystem, String targetSystem)
    throws Exception {
    logger.debug("TerminologyService.translateTerm input: "+sourceCode);
    String targetCode = "";
    Slot fromSlot = null;
    Slot toSlot = null;
    if (kb == null) throw new Exception("TerminologyService.translateTerm: No mapping knowledge base loaded!");
    if (sourceSystem.equals("db")){
      fromSlot = kb.getSlot("Source_String_in_DB");
      toSlot=kb.getSlot("Target_Class_in_KB");
    } else {
      fromSlot = kb.getSlot("Target_Class_in_KB");
      toSlot=kb.getSlot("Source_String_in_DB");
    }
    if (fromSlot == null) throw new Exception ("TerminologyService.translateTerm: No valid from slot");
    if (toSlot == null) throw new Exception("TerminologyService.translateTerm: No valid to slot");

    Collection matchingTerms = kb.getMatchingFrames(fromSlot, null, false, sourceCode, 1);
    if ((matchingTerms != null) && !matchingTerms.isEmpty()) {
      for (Iterator i=matchingTerms.iterator(); i.hasNext();) {
        Instance mapping = (Instance) i.next();
        Object toObj = mapping.getOwnSlotValue(toSlot);
        if (toObj != null) {
          if (sourceSystem.equals("db")) targetCode = ((Cls)toObj).getName();
          else targetCode = (String)toObj;
        } else throw new Exception("TerminologyService.translateTerm: no target term found for "+sourceCode);
      }
    } else throw new Exception("TerminologyService.translateTerm: no mapping defined for "+sourceCode);
    logger.debug("TerminologyService.translateTerm: in "+sourceCode+
    ", out "+targetCode);
    return targetCode;
  }
} 