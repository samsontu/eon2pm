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
/*
 * Cay S. Horstmann & Gary Cornell, Core Java
 * Published By Sun Microsystems Press/Prentice-Hall
 * Copyright (C) 1997 Sun Microsystems Inc.
 * All Rights Reserved.
 *
 * Permission to use, copy, modify, and distribute this
 * software and its documentation for NON-COMMERCIAL purposes
 * and without fee is hereby granted provided that this
 * copyright notice appears in all copies.
 *
 * THE AUTHORS AND PUBLISHER MAKE NO REPRESENTATIONS OR
 * WARRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE, EITHER
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. THE AUTHORS
 * AND PUBLISHER SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED
 * BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 */

/**
 * Store dates and perform date arithmetic
 * (another Date class, but more convenient that
 * java.util.Date or java.util.Calendar)
 * @version 1.02 13 Jun 1996
 * @author Cay Horstmann
 */

package edu.stanford.smi.eon.util;

import java.util.*;
import org.apache.log4j.*;

public class Day implements Cloneable
{  /**
    *  Constructs today's date
    */
   static Logger logger = Logger.getLogger(Day.class);
   public Day()
   {  GregorianCalendar todaysDate
         = new GregorianCalendar();
      year = todaysDate.get(Calendar.YEAR);
      month = todaysDate.get(Calendar.MONTH) + 1;
      day = todaysDate.get(Calendar.DAY_OF_MONTH);
   }

   /**
    * Constructs a specific date
    * @param yyyy year (full year, e.g., 1996,
    * <i>not</i> starting from 1900)
    * @param m month
    * @param d day
    * @exception IllegalArgumentException if yyyy m d not a
    * valid date
    */

   public Day(int yyyy, int m, int d)
   {  year = yyyy;
      month = m;
      day = d;
      if (!isValid()) {
        logger.error("In Day constructor. Illegal arguments: "+yyyy+" "+
          m+" "+d);
        throw new IllegalArgumentException();
      }
   }

   /**
    * Advances this day by n days. For example.
    * d.advance(30) adds thirdy days to d
    * @param n the number of days by which to change this
    * day (can be < 0)
    */

   public void advance(int n)
   {  fromJulian(toJulian() + n);
   }

   public void setDay(int inputday)
   {
    day = inputday;
   }


   public void setMonth(int m)
   {
    month = m;
   }


   public void setYear(int yyyy)
   {
    year = yyyy;
   }


   public int getDay()
   /**
    * Gets the day of the month
    * @return the day of the month (1...31)
    */

   {  return day;
   }

   public int getMonth()
   /**
    * Gets the month
    * @return the month (1...12)
    */

   { return month;
   }

   public int getYear()
   /**
    * Gets the year
    * @return the year (counting from 0, <i>not</i> from 1900)
    */

   { return year;
   }

   /**
    * Gets the weekday
    * @return the weekday (0 = Sunday, 1 = Monday, ...,
    * 6 = Saturday)
    */

   public int weekday() { return (toJulian() + 1)% 7; }

   /**
    * The number of days between this and day parameter
    * @param b any date
    * @return the number of days between this and day parameter
    * and b (> 0 if this day comes after b)
    */

   public int daysBetween(Day b)
   {  return toJulian() - b.toJulian();
   }

   /**
    * A string representation of the day
    * @return a string representation of the day
    */

   public String toString()
   {  //return "Day[" + year + "," + month + "," + day + "]";
      return year+"-"+month+"-"+day;
   }
   
   public String toUSDate()
   {
	   return month+"/"+day+"/"+year;
   }

   /**
    * Makes a bitwise copy of a Day object
    * @return a bitwise copy of a Day object
    */

   public Object clone()
   {  try
      {  return super.clone();
      } catch (CloneNotSupportedException e)
      {  // this shouldn't happen, since we are Cloneable
         return null;
      }
   }

   /**
    * Computes the number of days between two dates
    * @return true iff this is a valid date
    */

   private boolean isValid()
   {  Day t = new Day();
      t.fromJulian(this.toJulian());
      return t.day == day && t.month == month
         && t.year == year;
   }

   public int toJulian()
   /**
    * @return The Julian day number that begins at noon of
    * this day
    * Positive year signifies A.D., negative year B.C.
    * Remember that the year after 1 B.C. was 1 A.D.
    *
    * A convenient reference point is that May 23, 1968 noon
    * is Julian day 2440000.
    *
    * Julian day 0 is a Monday.
    *
    * This algorithm is from Press et al., Numerical Recipes
    * in C, 2nd ed., Cambridge University Press 1992
    */
   {  int jy = year;
      if (year < 0) jy++;
      int jm = month;
      if (month > 2) jm++;
      else
      {  jy--;
         jm += 13;
      }
      int jul = (int) (java.lang.Math.floor(365.25 * jy)
      + java.lang.Math.floor(30.6001*jm) + day + 1720995.0);

      int IGREG = 15 + 31*(10+12*1582);
         // Gregorian Calendar adopted Oct. 15, 1582

      if (day + 31 * (month + 12 * year) >= IGREG)
         // change over to Gregorian calendar
      {  int ja = (int)(0.01 * jy);
         jul += 2 - ja + (int)(0.25 * ja);
      }
      return jul;
   }

   public void fromJulian(int j)
   /**
    * Converts a Julian day to a calendar date
    * @param j  the Julian date
    * This algorithm is from Press et al., Numerical Recipes
    * in C, 2nd ed., Cambridge University Press 1992
    */
   {  int ja = j;

      int JGREG = 2299161;
         /* the Julian date of the adoption of the Gregorian
            calendar
         */

      if (j >= JGREG)
      /* cross-over to Gregorian Calendar produces this
         correction
      */
      {  int jalpha = (int)(((float)(j - 1867216) - 0.25)
             / 36524.25);
         ja += 1 + jalpha - (int)(0.25 * jalpha);
      }
      int jb = ja + 1524;
      int jc = (int)(6680.0 + ((float)(jb-2439870) - 122.1)
          /365.25);
      int jd = (int)(365 * jc + (0.25 * jc));
      int je = (int)((jb - jd)/30.6001);
      day = jb - jd - (int)(30.6001 * je);
      month = je - 1;
      if (month > 12) month -= 12;
      year = jc - 4715;
      if (month > 2) --year;
      if (year <= 0) --year;
   }

   private int day;
   private int month;
   private int year;
}
