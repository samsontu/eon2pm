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
package edu.stanford.smi.eon.siteCustomization;
import edu.stanford.smi.eon.PCAServerModule.*;
import edu.stanford.smi.eon.datahandler.Note_Entry;

import org.apache.log4j.*;


import java.util.*;

public class CompareEvals implements Comparator {

  public CompareEvals() {
  }
  static Logger logger = Logger.getLogger(CompareEvals.class);

  public int compare(Object o1, Object o2) {
    if ((o1 instanceof Choice_Evaluation) &&
        (o2 instanceof Choice_Evaluation)) {
      int score1 = score((Choice_Evaluation)o1);
      int score2 = score((Choice_Evaluation) o2);
      if (score1 > score2) return -1;
      else if (score1 == score2) return 0;
           else return 1;
    } else return 0;
  }

  public boolean equals( Object obj) {
    if (obj instanceof  edu.stanford.smi.eon.siteCustomization.CompareEvals)
      return true;
    else return false;
  }

  private int score(Choice_Evaluation obj) {
    int score = 0;
    if (obj != null) {
      switch (obj.discriminator().value()) {
        case Evaluation_Type._add:
          Add_Evaluation evaluation = obj.add_eval();
          if (evaluation.prior_use.value() == Truth_Value._true.value()) {
            score = -100;
          }
          score = score + (-1 * evaluation.harmful_interactions.length) +
                  (10 * evaluation.compelling_indications.length) +
                  (-100 * evaluation.contraindications.length) +
                  (+1 * evaluation.relative_indications.length) +
                  (-1 * evaluation.relative_contraindications.length) +
                  (-1 * evaluation.side_effects.length);
          logger.debug("CompareEvals.score: "+ evaluation.activity_to_start +
            " score = "+score);
          return score;
        default:
          break;
      }
    }
    return score;
  }
}