package gov.va.test.opioidtesttool;

import java.util.*;
import java.util.Vector;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.text.*;

import edu.stanford.smi.eon.PCAServerModule.*;
import edu.stanford.smi.eon.kbhandler.KBHandler;
import edu.stanford.smi.eon.util.HelperFunctions;

// Stuff for DOCUMENT
import org.w3c.dom.*;

/*
 * Class provides a way to store data entry from gui
 * We assume all entry widgets have 4 data points
 * Two of the points are text and/or numeric
 * The other two points are some form of dates
 *
 * Also has method to transform GUI data entered
 * Into format for Guideline Interpreter
 * and XML file for saving 
 */

public class TETableModel extends DefaultTableModel {
	public boolean fieldTwoNumeric;
	public int fields;
	public int dates;

	// Guideline Data types, (options passed to convertToGuidelineData)
	public final static String PCA_PRESCRIPTION = "Prescription";
	public final static String PCA_NUMERIC = "NUMERIC";
	public final static String PCA_NOTE = "NOTE";
	public final static String PCA_NOTE_OR_NUMERIC = "NOTE_OR_NUMERIC";
	public final static String PCA_MPR = "MPR";
	public final static String PCA_STATUS = "STATUS";
	private int numFields;
	private boolean require2ndField;
	private int numDates;
	private int dateIndex;
	private int totalCols;

	private String pcaDataType;
	private Entry_Type pcaEntryType;

	public TETableModel(String[] fieldLabels, boolean _f2Numeric,
			int _numDates, String _pcaDataType, Entry_Type _pcaEntryType, boolean require2ndField) {
		super();

		int x;
		numFields = fieldLabels.length;
		this.require2ndField = require2ndField;
		numDates = _numDates;
		fieldTwoNumeric = _f2Numeric;
		pcaDataType = new String(_pcaDataType);

		// by default we'll just store an entry Type of problem
		if (_pcaEntryType != null)
			pcaEntryType = _pcaEntryType;
		else
			pcaEntryType = Entry_Type.problem;

		if (numFields < 1 || numFields > 2)
			throw new RuntimeException(
					"Error creating TETableModel, we need at least 1 field!");

		for (x = 0; x < numFields; x++) {
			addColumn(fieldLabels[x]);
		}

		// First date is at index 1, if only 1 field
		dateIndex = numFields;
		if (numDates >= 1)
			addColumn("Start");
		if (numDates >= 2)
			addColumn("End");

		totalCols = numFields + numDates;
	}

	public void teAddRow(String obj1, String obj2, Date start, Date end) {
		int i = 0;
		//System.out.println("teAddRow: " + obj1 + ", "+obj2 );
		Object[] rowData = new Object[totalCols];
		rowData[i] = obj1;

		if (numFields >= 2) {
			rowData[++i] = obj2;
		}
		//System.out.println("rowData[1] = "+rowData[1]);

		// add dates as needed
		if (numDates >= 1)
			rowData[++i] = start;
		if (numDates >= 2)
			rowData[++i] = end;

		addRow(rowData);
	}

	public void teAddRow1(String obj1, String obj2, Date start, Date end) {
		int i = 0;
		//System.out.println("teAddRow: " + obj1 + ", "+obj2 );
		Object[] rowData = new Object[totalCols];
		rowData[i] = obj1;

		if (numFields >= 2) {
			rowData[++i] = obj2;
		}
		//System.out.println("rowData[1] = "+rowData[1]);

		// add dates as needed
		if (numDates >= 1)
			rowData[++i] = start;
		if (numDates >= 2)
			rowData[++i] = end;

		addRow(rowData);
	}

	/*
	 * Function to facilitate modification of data item via GUI
	 */
	public void teModifyRow(int rowIndex, String obj1, String obj2, Date start,
			Date end) {
		if (numFields >= 1) {
			setValueAt(obj1, rowIndex, 0);
		}
		if (numFields >= 2) {
			setValueAt(obj2, rowIndex, 1);
		}
		if (numDates >= 1) {
			setValueAt(start, rowIndex, dateIndex);
		}
		if (numDates >= 2) {
			setValueAt(end, rowIndex, dateIndex + 1);
		}
	}

	/*
	 * Converts the data structure into format for Guideline interpreter returns
	 * resulting Patient_Data array
	 */
	public Collection convertToGuidelineData() {
		int x, rows;
		Patient_Data ptData;
		Collection patientDataList = new ArrayList();

		rows = getRowCount();
		for (x = 0; x < rows; x++) {
			ptData = new Patient_Data();
			String f1 = getFirstField(x);
			String f2 = getSecondField(x);
			String d1 = "", d2 = "";
			if (numDates >= 1)
				d1 = HelperFunctions.internalDateFormatter.format(getStart(x));
			if (numDates >= 2)
				if (getEnd(x) != null)
					d2 = HelperFunctions.internalDateFormatter.format(getEnd(x));

			/*
			 * see if there is a data type over ride for this for this domain
			 * term (domain terms are stored in field f1) returns null if no
			 * over ride
			 */
			String cur_pcaDataType = null;
			if (GlobalVars.dtor != null ){
				cur_pcaDataType = GlobalVars.dtor.get(f1);
				if (cur_pcaDataType == null)
					cur_pcaDataType = pcaDataType;
				else
					System.err.println("Field: " + f1
						+ "  ,found data-type over ride: '" + cur_pcaDataType
						+ "'\n");
			} else 
				cur_pcaDataType = pcaDataType;
			
			if (cur_pcaDataType.equals(PCA_PRESCRIPTION)) {
				ptData.prescription(PCAInterface.newPrescription(f1, new Float(
						f2), d1, d2));
				System.out.println(f1+": "+f2+" "+d1+"-"+d2);
			} else if (cur_pcaDataType.equals(PCA_NOTE)) {
				ptData.note_data(PCAInterface.newNote(f1, f2, pcaEntryType, d1,
						d2));
			} else if (cur_pcaDataType.equals(PCA_NUMERIC)) {
				ptData.numeric(PCAInterface.newNumeric(f1, f2, d1));
			} else if (cur_pcaDataType.equals(PCA_NOTE_OR_NUMERIC)) {
				// if f2 is numeric, create a new numeric entry
				// otherwise create a new note entry
				try {
					//System.out.println("f1 = "+f1 + " f2 = "+ f2);
					if (f2.equals(""))
						ptData.note_data(PCAInterface.newNote(f1, f2,
								pcaEntryType, d1, d2));
					else {
						// if not a number, will throw an exception
						Float f = Float.valueOf(f2);
						//System.out.println("F1 = " + f1 + " Numeric value = "
						//		+ f);
						// set default date, if not provided
						if (d1.equals(""))
							d1 = new String("1970-01-01");
						ptData.numeric(PCAInterface.newNumeric(f1, f2, d1));
					}
				} catch (NumberFormatException e) {
					ptData.note_data(PCAInterface.newNote(f1, f2, pcaEntryType,
							d1, d2));
				}
			} else if (cur_pcaDataType.equals(PCA_MPR)){
				//data has the form f1=drugname, f2= numeric mpr, d1=present release date
				ptData.prescription(PCAInterface.mprPrescription(f1, Integer.parseInt(f2.trim()), d1));
			} else if (cur_pcaDataType.equals(PCA_STATUS)){
				//data has the form f1=drugname, f2= status, d1= a date (ignored)
				ptData.prescription(PCAInterface.statusPrescription(f1, f2, d1));
			} else {	
				throw new RuntimeException("Unknown data type requested");
			}

			// add to list of patient data
			patientDataList.add(ptData);
		}
		return patientDataList;
	}

	/*
	 * Convert Table data structure into an XML Node Facilitates, saving data to
	 * XML file see loadXMLData() for Node structure
	 */
	public Node createXMLNode(Document doc, String name, int iLevel) {
		int x;
		Element tableRoot = doc.createElement(name);
		Element item, f;

		tableRoot.appendChild(XMLInterface.nl(doc));
		System.out.println("Starting XML Node");

		for (x = 0; x < getRowCount(); x++) {
			item = doc.createElement("item");
			item.appendChild(XMLInterface.nl(doc));

			tableRoot.appendChild(XMLInterface.indent(doc, iLevel));
			tableRoot.appendChild(item);

			// first field
			f = doc.createElement("field1");
			f.setAttribute("label", getColumnName(0));
			f.appendChild(doc.createTextNode(getFirstField(x)));
			XMLInterface.addNode(doc, item, f, iLevel + 1);

			// second field
			if (numFields >= 2) {
				f = doc.createElement("field2");
				f.setAttribute("label", getColumnName(1));
				f.appendChild(doc.createTextNode(getSecondField(x)));
				XMLInterface.addNode(doc, item, f, iLevel + 1);
			}

			if (numDates >= 1) {
				f = doc.createElement("start");
				f.appendChild(doc.createTextNode(getStart(x).toString()));
				XMLInterface.addNode(doc, item, f, iLevel + 1);
			}

			if (numDates >= 2) {
				f = doc.createElement("end");
				String endString = (getEnd(x) != null) ? getEnd(x).toString() : "";
				f.appendChild(doc.createTextNode(endString));
				XMLInterface.addNode(doc, item, f, iLevel + 1);
			}

			item.appendChild(XMLInterface.indent(doc, iLevel));
			tableRoot.appendChild(XMLInterface.nl(doc));
		}
		System.out.println(tableRoot);
		tableRoot.appendChild(XMLInterface.indent(doc, iLevel - 1));
		return tableRoot;
	}

	/*
	 * Load data from DOM Node (in xml format) Format: <data-type> (i.e. meds,
	 * dx, etc.) <item> <field1>value1</field1> <field2>value2</field2>
	 * <start>Start Date</start> <end>End Date </end> </item .... <item>..
	 * </item> ... </data-type>
	 */
	public void loadXMLData(Element e) {
		int x;
		String f1 = null;
		String f2 = null;
		Date start = null;
		Date end = null;
		SimpleDateFormat sdf = new SimpleDateFormat(
				"EEE MMM dd hh:mm:ss zzz yyyy");
		DateFormat df = DateFormat.getDateInstance();

		NodeList nl = e.getElementsByTagName("item");
		for (x = 0; x < nl.getLength(); x++) {
			Element i = (Element) nl.item(x);

			NodeList inl = i.getElementsByTagName("field1");
			Node f = inl.item(0); // this is the tag element
			f = f.getFirstChild(); // get the text element
			f1 = new String(f.getNodeValue());

			if (numFields >= 2) {
				f2 = null;
				inl = i.getElementsByTagName("field2");
				f = inl.item(0);
				if (require2ndField) {	
					f = f.getFirstChild();
					f2 = new String(f.getNodeValue());
				} else {
					if (f != null) {
						f = f.getFirstChild();
						if ((f != null) && (f.getNodeValue() != null))
							f2 = new String(f.getNodeValue());
					}
				}
			}

			try {
				if (numDates >= 1) {
					inl = i.getElementsByTagName("start");
					f = inl.item(0);
					f = f.getFirstChild();
					if (f != null) {
						start = sdf.parse(f.getNodeValue());
					}
				}

				if (numDates >= 2) {
					inl = i.getElementsByTagName("end");
					f = inl.item(0);
					if (f != null) {
						f = f.getFirstChild();
						if (f != null) {
							//System.err.println(f.getNodeValue());
							end = sdf.parse(f.getNodeValue());
						}
						 
					}
				}
			} catch (Exception ex) {
				System.err.println("Error formatting date");
				ex.printStackTrace();
			}

			teAddRow(f1, f2, start, end);
		}
	}

	// getters
	public String getFirstField(int rowIndex) {
		if (numFields >= 1)
			return ((String) getValueAt(rowIndex, 0)).trim();
		else
			return (new String(""));
	}

	public String getSecondField(int rowIndex) {
		if (numFields >= 2) {
			String str = (String) getValueAt(rowIndex, 1);
			if (fieldTwoNumeric) {
				/*
				 * if the field is numeric but no data entered, return 0
				 */
				if ((str == null ) || (str.trim().equals(""))) {
					return new String("0.0");
				} else {
					return str;
				}
			} else {
				if (str == null)
					return (new String(""));
				else 
					return str.trim();
			}
		} else {
			return (new String(""));
		}
	}

	public Date getStart(int rowIndex) {
		if (numDates >= 1)
			return (Date) getValueAt(rowIndex, dateIndex);
		else
			return null;
	}

	public Date getEnd(int rowIndex) {
		if (numDates >= 2)
			return (Date) getValueAt(rowIndex, dateIndex + 1);
		else
			return null;
	}

	public int getNumFields() {
		return numFields;
	}

	public int getNumDates() {
		return numDates;
	}

	public int getTotalCols() {
		return totalCols;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		int x = 0;
		for (x = 0; x < getRowCount(); x++) {
			sb.append("\t" + getColumnName(0) + ": " + getFirstField(x) + "\n");
			;
			if (numFields >= 2) {
				sb.append("\t" + getColumnName(1) + ": " + getSecondField(x)
						+ "\n");
				;
			}
			if (numDates >= 1) {
				sb.append("\t" + "Start: " + getStart(x) + "\n");
				;
			}
			if (numDates >= 2) {
				sb.append("\t" + "End: " + getEnd(x) + "\n");
				;
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}