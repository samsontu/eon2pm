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
import org.apache.log4j.*;
import edu.stanford.smi.eon.datahandler.Constants;

public class Debug {

  static  Logger  logger = Logger.getLogger(Debug.class);
  public Debug() {
  }
  static public void trace(String string, int level) {
     if (level == Constants.debug)
      logger.debug(string);
    else if (level == Constants.warn)
      logger.warn(string);
    else if (level == Constants.error)
      logger.error(string);
    else if (level == Constants.fatal)
      logger.fatal(string);
    else logger.debug(string); 
    }
  
  static public void trace(String string, int level, Throwable e) {
    if (level == Constants.debug)
      logger.debug(string, e);
    else if (level == Constants.warn)
      logger.warn(string, e);
    else if (level == Constants.error)
      logger.error(string, e);
    else if (level == Constants.fatal)
      logger.fatal(string, e);
    else logger.debug(string, e);
    }

}


