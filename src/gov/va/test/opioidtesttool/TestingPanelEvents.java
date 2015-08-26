package gov.va.test.opioidtesttool;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

public class TestingPanelEvents implements ActionListener {
	public static final String COMPUTE = "Compute Recommendations";
	public static final String UPDATE = "Save Patient Records";
	public static final String NEWPATIENT = "Create new patient record";
	public static final String SAVETODATABASE = "Save Patient Data To Database";

	public static final String DB_MODE = "Database";
	public static final String ENTRY_MODE = "GUI Entry";
	public static final String MODES[] = { ENTRY_MODE, DB_MODE };

	private TestingPanel tep;

	public TestingPanelEvents(TestingPanel _tep) {
		tep = _tep;
	}

	public void actionPerformed(ActionEvent ae) {
		String eventName = ae.getActionCommand();
		System.out.println(" eventName: " + eventName);
		// Button Events
		if (eventName.equals(COMPUTE)) {
			String url;
			System.out.println("Computing Recocmmendations for: "
					+ GlobalVars.currentPatient.toString());

			// Initialize PCA Server
			try {
				if (!PCAInterface.isInitialized) {
					PCAInterface.initialize();
				}
				/*File f = new File(GlobalVars.kbURL);
				// re-read KB on disk, if stand alone and its changed
				if (GlobalVars.standAlone
						&& GlobalVars.kbLastModified < f.lastModified()) {
					msg("KB Changed", "KB Changed on disk, reloading...");
					PCAInterface.initialize();
				} */ 
			} catch (Exception e) {
				msg("PCA Server Error", "Couldn't Initialize PCA Server");
				return;
			}

			// Compute guideline recommendations
			try {
				url = PCAInterface
						.computeRecommendations(GlobalVars.currentPatient);
			} catch (Exception e) {
				msg("Error generating recommendations",
						"Some error occured generating recommendations\nsee DOS window for more");
				e.printStackTrace();
				return;
			}

			// update Results display
			tep.updateResults(url);
		} else if (eventName.equals(UPDATE)) {
			XMLInterface.write();

			// For debugging only
			// Iterator pdsIt = GlobalVars.pds.iterator();
			// while (pdsIt.hasNext()) {
			// PatientDataStore pds = (PatientDataStore) pdsIt.next();
			// try {
			// pds.dump();
			// }
			// catch (Exception e) {
			// ; // do not worry about dump exceptions
			// }
			// }
			// For debugging only

			// Save patient data to DB?
			JCheckBox cbx = tep.getSaveToDBCbx();
			boolean isChecked = cbx.isSelected();
			if (isChecked) {
				System.out.println("Save to database is not supported");
//				System.out
//						.println(" Beginning saving patient data to database using initFile: "
//								+ GlobalVars.initFile);
//				PatientDataToDBInterface pdIntf = new PatientDataToDBInterface(
//						GlobalVars.initFile);
//				pdIntf.saveToDB(GlobalVars.pds);
//				System.out
//						.println(" Saving patient data to database is completed. ");
			}

		} else if (eventName.equals(NEWPATIENT)) {
			// Create new Patient Record

			String name = JOptionPane
					.showInputDialog("Please enter new patient's name");
			if (name != null && !name.trim().equals("")) {
				PatientDataStore p = new PatientDataStore(name);
				GlobalVars.pds.add(p);

				// Set current patient to it
				GlobalVars.currentPatient = p;

				// update GUI
				tep.setCurrentPatient(p);
				tep.patientSelect.addItem(name);
				tep.patientSelect.setSelectedItem(name);
				tep.repaint();
			}
		}

		// Combo Box Events
		if (eventName.equals("comboBoxChanged")) {
			JComboBox cb = (JComboBox) ae.getSource();
			String sel = (String) cb.getSelectedItem();
			if (cb == tep.patientDataEntryMode) {
				// Going to alter selectPanel to
				// display appropriate widgets
				tep.selectPanel.remove(tep.dbPanel);
				tep.selectPanel.remove(tep.fdPanel);
				if (sel.equals(ENTRY_MODE)) {
					tep.selectPanel.add(tep.fdPanel, tep.selectSubConstraints);
				} else {
					tep.selectPanel.add(tep.dbPanel, tep.selectSubConstraints);
				}
				tep.selectPanel.revalidate();
				tep.selectPanel.repaint();
			} else if (cb == tep.patientSelect) {
				// TIME TO SWITCH Patient Data
				// based on selection from Combo Box

				PatientDataStore p = null, tmp = null;
				Iterator pdsIt = GlobalVars.pds.iterator();
				while (pdsIt.hasNext()) {
					tmp = (PatientDataStore) pdsIt.next();
					if (sel.equals(tmp.getName())) {
						p = tmp;
						break;
					}
				}

				System.out.println("Selected patient: " + p.getName());
				// Set current patient to it
				GlobalVars.currentPatient = p;

				// update GUI
				tep.setCurrentPatient(p);
				tep.repaint();
			}
		}
	}

	// Simple wrapper routine to display messages nicely
	void msg(String title, String msg) {
		JOptionPane.showMessageDialog(null, title, msg,
				JOptionPane.ERROR_MESSAGE);
		System.err.println(msg);
	}
}
