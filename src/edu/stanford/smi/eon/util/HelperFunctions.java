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
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.log4j.Logger;

import edu.stanford.smi.eon.PCAServerModule.*;
import edu.stanford.smi.eon.criterion.Criterion;
import edu.stanford.smi.eon.kbhandler.PCAInterfaceUtil;
import edu.stanford.smi.eon.time.Absolute_Time_Point;



public class HelperFunctions {
	private static Day dayObj = new Day();
	private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat sourceFormat = new SimpleDateFormat("EEE MMM dd kk:mm:ss zzz yyyy");
	static Logger logger = Logger.getLogger(HelperFunctions.class);

	public static String replaceSubstring(String original, String oldSub, String newSub) {
		int oldSubLength = oldSub.length();
		int originalSubLength = original.length();
		int index = -1;
		if (oldSubLength > 0) {
			while ((index = original.lastIndexOf(oldSub))>= 0 ) {
				original = original.substring(0, index) + '"'+newSub+
						'"'+
						original.substring(index+oldSubLength);
			}
		}
		return original;
	}

	public static String replaceSubstringWOQuotes(String original, String oldSub, String newSub) {
		int oldSubLength = oldSub.length();
		int originalSubLength = original.length();
		int index = -1;
		if (oldSubLength > 0) {
			while ((index = original.lastIndexOf(oldSub))>= 0 ) {
				original = original.substring(0, index) +newSub+
						original.substring(index+oldSubLength);
			}
		}
		return original;
	}
	
	public static String DateTime2Date(String datetime) {
		// Change date from "Fri Jun 03 18:29:55 PDT 2011" to "2011-06-03"
		Date date;
		String formattedDate = "";
		if (datetime != null) {
			try {
				date = sourceFormat.parse(datetime);
				formattedDate = formatter.format(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return formattedDate;
	}


	public static String Int2DateTime (long julianMinutes) {
		int minutesInDay = 1440;
		int julianDays = (int) (julianMinutes / minutesInDay);
		dayObj.fromJulian(julianDays);
		julianMinutes = julianMinutes % minutesInDay;
		if (julianMinutes == 0) return dayObj.toString();
		else return dayObj.toString() + " "+julianMinutes / 60 +":"+ julianMinutes % 60+":00";
	}

	public static String Int2DayString (int julianDays) {
		dayObj.fromJulian(julianDays);
		return dayObj.toString();
	}

	public static int Day2Int2(String dateString)  throws ParseException  {
		// datetime has the form: yyyy-mm-dd hh:mm:ss:mmm 

		Date date;
		date = formatter.parse(dateString);

		// get a calendar using the default time zone and locale.  
		Calendar calendar = Calendar.getInstance();  
		calendar.setTime(date);  
		dayObj.setYear(calendar.get(Calendar.YEAR));  
		dayObj.setMonth(calendar.get(Calendar.MONTH)+1);  
		dayObj.setDay(calendar.get(Calendar.DAY_OF_MONTH));  
		return dayObj.toJulian();

		/*    int firstdash = date.indexOf("-", 0);
    int seconddash = date.indexOf("-", firstdash+1);
    int firstblank = date.indexOf(" ",0);
    int limit = date.length();
    if (firstblank > 0) limit = firstblank;
     dayObj.setYear(Integer.parseInt(date.substring(0, firstdash)));
    dayObj.setMonth(Integer.parseInt(date.substring(firstdash+1, seconddash)));
    dayObj.setDay(Integer.parseInt(date.substring(seconddash+1, limit )));
		 */
		//dayObj.setDay(Integer.parseInt(date.substring(8, 10)));
		//dayObj.setYear(Integer.parseInt(date.substring(0, 4)));
		//dayObj.setMonth(Integer.parseInt(date.substring(5, 7)));
		
	}

	
	public static long DateTime2Int(String date)    {
		// datetime has the form: yyyy-mm-dd hh:mm:ss:mmm
		int minutesInADay = 1440;
		date = date.trim();

		int firstdash = date.indexOf("-", 0);
		int seconddash = date.indexOf("-", firstdash+1);
		int firstblank = date.indexOf(" ",0);
		int limit = date.length();
		if (firstblank > 0) limit = firstblank;
		dayObj.setYear(Integer.parseInt(date.substring(0, firstdash)));
		dayObj.setMonth(Integer.parseInt(date.substring(firstdash+1, seconddash)));
		dayObj.setDay(Integer.parseInt(date.substring(seconddash+1, limit )));
		int daysInt = dayObj.toJulian();

		long numberOfMinutes = ((long) daysInt) * minutesInADay;
		int firstcolon = date.indexOf(":", 0);
		int secondcolon = date.indexOf(":", firstcolon+1);
		if (firstcolon > 0) {
			numberOfMinutes = numberOfMinutes + 60* Integer.parseInt(date.substring(firstblank+1, firstcolon));
			if (secondcolon > 0) numberOfMinutes = numberOfMinutes +
					Integer.parseInt(date.substring(firstcolon+1, secondcolon));
		}
		return numberOfMinutes;
	}
	
/*	public static long DateTime2Int(String dateString)    {
		// datetime has the form: yyyy-mm-dd hh:mm:ss:mmm
		SimpleDateFormat timeformatter = null;
		int firstcolon = dateString.indexOf(":", 0);
		int minutesInADay = 1440;
		long numberOfMinutes = 0;

		if (firstcolon > 0 )
			timeformatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.");
		else
			timeformatter = formatter;
		Date date;
		try {
			date = timeformatter.parse(dateString);

			// get a calendar using the default time zone and locale.  
			Calendar calendar = Calendar.getInstance();  
			calendar.setTime(date);  
			dayObj.setYear(calendar.get(Calendar.YEAR));  
			dayObj.setMonth(calendar.get(Calendar.MONTH)+1);  
			dayObj.setDay(calendar.get(Calendar.DAY_OF_MONTH));  
			int daysInt = dayObj.toJulian();
			numberOfMinutes = ((long) daysInt) * minutesInADay + 60 * calendar.get(Calendar.HOUR_OF_DAY) + calendar.get(Calendar.MINUTE);
			
		} catch (ParseException e) {
			logger.warn(dateString +" can't be parsed (not in yyyy-mm-dd format");
			e.printStackTrace();
		}
		return numberOfMinutes;
	}
*/
	public static int Day2Int3(Date date) {
		Calendar DOB = new GregorianCalendar();
		DOB.setTime(date);
		Day day = new Day(DOB.get(Calendar.YEAR),
				DOB.get(Calendar.MONTH)+1, DOB.get(Calendar.DATE));
		return day.toJulian();
	}
	public static int Day2Int(String date)
	//throws IOException
	{
		// date has the form     "Jul 27 1997 12:00AM" or
		//                       "Jul 3 1997 12:00AM"
		String m ;

		dayObj.setDay(Integer.parseInt(date.substring(4,6).trim()));
		if (dayObj.getDay() > 9) {
			dayObj.setYear(Integer.parseInt(date.substring(6,11).trim()));
		}
		else {
			dayObj.setYear(Integer.parseInt(date.substring(6,10).trim()));
		}

		m = date.substring(0,3);

		if (m.equals("Jan")) { dayObj.setMonth(1); }
		else if (m.equals( "Feb") ) { dayObj.setMonth(2); }
		else if (m.equals( "Mar") ) { dayObj.setMonth(3);}
		else if (m.equals( "Apr") ) { dayObj.setMonth(4);}
		else if (m.equals( "May") ) { dayObj.setMonth(5);}
		else if (m.equals( "Jun") ) { dayObj.setMonth(6);}
		else if (m.equals( "Jul") ) { dayObj.setMonth(7);}
		else if (m.equals( "Aug") ) { dayObj.setMonth(8);}
		else if (m.equals( "Sep") ) { dayObj.setMonth(9);}
		else if (m.equals( "Oct") ) { dayObj.setMonth(10);}
		else if (m.equals( "Nov") ) { dayObj.setMonth(11);}
		else if (m.equals( "Dec") ) { dayObj.setMonth(12);}
		//else throw new IOException();
		return dayObj.toJulian();
	}

	/*   public static int DateTime2Min(String date) {

     // date has the form     "Jul 27 1997 12:00AM" or
     //                       "Jul 3 1997 12:00AM"
     int days = Day2Int(date);
     int minutes = 0;
     return minutes;
   }

	 */
	public static Justification dummyJustification() {

		return new Justification(dummyCriteriaEvaluation(), "");
	}

	public static boolean isDummyJustification(Justification just) {
		return isDummyCriteriaEvaluation(just.evaluation);
	}

	public static Scenario_Choice dummyScenarioChoice() {
		Preference preference =  Preference.neutral;
		Justification justification = HelperFunctions.dummyJustification();

		// construct return type
		Scenario_Choice choice = new Scenario_Choice
				(preference, "",justification,
						dummyGuidelineEntity(), "");
		return choice;
	}


	public static Criteria_Evaluation dummyCriteriaEvaluation() {
		return new Criteria_Evaluation(Logical_Operator.AND, Truth_Value.unknown,
				new Criteria_Evaluation[0], dummyGuidelineEntity(), "");
	}

	public static boolean isDummyCriteriaEvaluation(Criteria_Evaluation eval) {
		return ((eval._operator.value() == Logical_Operator._AND) &&
				(eval.children.length == 0));
	}

	public static Guideline_Entity dummyGuidelineEntity () {
		return new Guideline_Entity("", "", "", "");
	}

	public static boolean isDummyGuidelineEntity(Guideline_Entity entity) {
		return ( entity.entity_id.equals(""));
	}
	public static Action_To_Choose dummyActionToChoose() {
		return new Action_To_Choose("", Preference.neutral, dummyJustification(),
				new Action_Spec_Record[0], dummyGuidelineEntity(), "");
	}

	public static boolean isDummyActionToChoose(Action_To_Choose action) {
		return isDummyGuidelineEntity(action.action_step);
	}
	public static boolean GuidelineServiceRecordHasNull(Guideline_Service_Record g) {
		return true;
	}

	public static Delete_Evaluation dummyDeleteEvaluation(String currentDrug){
		return new Delete_Evaluation("",
				currentDrug,
				new Guideline_Entity("","","",""),
				currentDrug,
				new Matched_Data[0],
				new Matched_Data[0],
				new Matched_Data[0],
				new Matched_Data[0],
				new Matched_Data[0],
				new Matched_Data[0],
				new Matched_Data[0],
				Truth_Value._false,
				new Action_Spec_Record[0],
				Preference.neutral, 
				null, 0);
	}
	/*
   public static void main(String[] argv) {

     logger.info("Testing Datetime2Int");

     long test = DateTime2Int("2003-12-12 3:24:34");
     long result = ((long)Day2Int2("2003-12-12"))*1440 + 60*3+24;
     if (test == result) {
       logger.info("2003-12-12 3:24:34 = "+test +" is correct");
     } else {
       logger.info("2003-12-12 3:24:34 = "+test +" is not correct");
     }

     logger.info("Testing Int2DateTime");

     String functionCallResult = Int2DateTime(result);
     String expectedResult = "2003-12-12 3:24:00";
     if (functionCallResult.equals(expectedResult)) {
       logger.info(result +" = "+functionCallResult +" is correct");
     } else {
       logger.info(result +" = "+functionCallResult +" is not correct");
     }

   } */
}



