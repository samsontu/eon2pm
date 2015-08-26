package gov.va.test.opioidtesttool;

import gov.va.test.opioidtesttool.degui.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.io.*;

import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.widget.*;
import edu.stanford.smi.protege.resource.*;

import edu.stanford.smi.eon.PCAServerModule.*;
import edu.stanford.smi.eon.kbhandler.KBHandler;

/*
 * GUI modeled after AthenaDemo testing environment
 * Main widget is a SplitPane
 * SplitPane has two sub-panes -- one for input and one for ouptut
 * input pane = tabbed pane
 * output pane = HTML rendering pane
 */

public class TestingPanel extends JPanel {
    // main gui widgets
    private JSplitPane split = null;
    private JTabbedPane tabs = null;
    private JTabbedPane resultTabs = null;
    private JPanel entryPanel = null;
    private MainEntryPanel patientDataEntryPanel = null;
//    private JPanel resultsBrief = null;
    private JPanel resultsDebug = null;
//    private JScrollPane htmlPaneBrief = null;
    private JScrollPane htmlPaneDebug = null;

    // this widgets will be read / accessed from Event Handler
    protected JTextField patientIDField = null;
    protected JComboBox patientDataEntryMode = null;
    protected JComboBox patientSelect = null;
    protected GridBagConstraints selectSubConstraints = null;
    protected JPanel dbPanel = null;
    protected JPanel fdPanel = null;  // fake data panel

    private JCheckBox m_saveToDBCbx = null;  // checkBox on test panel which enables
                                             // saving of patient data to the database

    // widgets modified by Event Handler
    protected JPanel selectPanel = null;

    // event handler
    private TestingPanelEvents  tpe = null;

    public JCheckBox getSaveToDBCbx() { return m_saveToDBCbx; };

    public TestingPanel() {
	tabs = new JTabbedPane();
	tpe = new TestingPanelEvents(this);

	/*
	 * Input Panel (Tabbed)
	 * First tab contains form to do patient data entry
	 * Second tab allows to select patient, database
	 * and run PCA interpreter, etc.
	 */
	createSelectPanel();
	tabs.add("Select Patient", selectPanel);
	createEntryPanel();
	tabs.add("Patient Data", entryPanel);

	/*
	 * Output panel
	 */
	resultTabs = new JTabbedPane();
	//resultsBrief = new JPanel(new BorderLayout());
	//resultTabs.add("Brief Output", resultsBrief);
	resultsDebug = new JPanel(new BorderLayout());
	resultTabs.add("Full Output", resultsDebug);
	resultTabs.setPreferredSize(new Dimension(200,600));

	/* Main split widget */
	split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                           tabs, resultTabs);
	split.setOneTouchExpandable(true);
	split.setDividerLocation(550);
	add(split);

    }

    /*
     *  Entry Panel is one of the main tabs
     *  for data entry of patient data
     *
     * Contains deguy.MainnEntryPanel
     * and a button to allow quick updates of recommendations
     */
    public void createEntryPanel() {
	patientDataEntryPanel = new MainEntryPanel();
	entryPanel = new JPanel(new GridBagLayout());
	GridBagConstraints gbc = new GridBagConstraints();
	int ypos = 0;

	gbc.gridy = ypos;
	gbc.fill = GridBagConstraints.BOTH;
	gbc.gridwidth = GridBagConstraints.REMAINDER;
	gbc.weightx = 0.95;
	gbc.weighty = 0.95;
	entryPanel.add(patientDataEntryPanel,gbc);

	// Add commpute  button to entry pane
	gbc.gridy = ++ypos;
	gbc.fill = GridBagConstraints.NONE;
	gbc.weightx = 0;
	gbc.weighty = 0;
	JButton doIt = new JButton(TestingPanelEvents.COMPUTE);
	doIt.addActionListener(tpe);
	entryPanel.add(doIt,gbc);
    }

    /*
     * Creates patient selection sub-tab of
     * Input panel
     *
     * First decide on what type of Patient Data Entry (DB or GUI)
     * Then add appropriate subpanel to selectPanel
     */
    public void createSelectPanel() {
	int ypos = 0;
	selectPanel = new JPanel(new GridBagLayout());
	GridBagConstraints gbc = new GridBagConstraints();

	// Field to select mode
	gbc.gridy = ypos;
	gbc.anchor = GridBagConstraints.NORTHWEST;
	gbc.fill = GridBagConstraints.NONE;
	selectPanel.add(new JLabel("Patient Data from: "), gbc);
	patientDataEntryMode = new JComboBox(TestingPanelEvents.MODES);
	patientDataEntryMode.setSelectedIndex(0);
	patientDataEntryMode.addActionListener(tpe);
	gbc.gridy = ypos;
	gbc.gridwidth = GridBagConstraints.REMAINDER;
	selectPanel.add(patientDataEntryMode, gbc);

	createDBPanel();
	createFDPanel();

	// BY Default, display elements for fake data
	selectSubConstraints = new GridBagConstraints();
	selectSubConstraints.gridy = ypos + 1;
	selectSubConstraints.insets = new Insets(10, 0, 10, 0);
	selectPanel.add(fdPanel, selectSubConstraints);

    }

    /*
     * DB Panel is a sub-panel of selectPanel
     * contains elements necessary to compute recommendations
     * from patients stored in db
     */
    public void createDBPanel() {
	int ypos = 0;
	dbPanel = new JPanel(new GridBagLayout());
	GridBagConstraints gbc = new GridBagConstraints();

	// Field to enter Patient ID
	ypos++;
	gbc.gridy = ypos;
	gbc.insets = new Insets(10, 0, 10, 0);
	dbPanel.add(new JLabel("Patient ID:"), gbc);
	gbc.gridy = ypos;
	gbc.gridwidth = GridBagConstraints.RELATIVE;
	patientIDField = new JTextField(10);
	dbPanel.add(patientIDField, gbc);

	// Add commpute  button to panel
	JButton doIt = new JButton(TestingPanelEvents.COMPUTE);
	doIt.addActionListener(tpe);
	ypos++;
	gbc.gridy = ypos;
	dbPanel.add(doIt,gbc);
    }

    /*
     * FD Panel is a sub-panel of selectPanel
     * contains elements necessary to compute recommendations
     * from fake patient data entered via GUI or XML
     */
    public void createFDPanel() {
	int ypos = 0;
	fdPanel = new JPanel(new GridBagLayout());
	GridBagConstraints gbc = new GridBagConstraints();

	// Create Select Button
	Iterator pdsIt = GlobalVars.pds.iterator();
	PatientDataStore pds;
	Collection patientNames = new ArrayList();
	while ( pdsIt.hasNext() ) {
	    pds = (PatientDataStore) pdsIt.next();
	    patientNames.add(pds.getName());
	}
	patientSelect = new JComboBox((String []) patientNames.toArray(new String[0]));
	patientSelect.addActionListener(tpe);
	// put it on panel
	gbc.gridy = ypos;
	gbc.anchor = GridBagConstraints.NORTHWEST;
	gbc.fill = GridBagConstraints.NONE;
	gbc.insets = new Insets(10, 0, 10, 0);
	fdPanel.add(new JLabel("Select Patient:"), gbc);
	fdPanel.add(patientSelect,gbc);


	// Add update button to panel
	JButton updateData = new JButton(TestingPanelEvents.UPDATE);
	updateData.addActionListener(tpe);
	ypos++;
	gbc.gridy = ypos;
	fdPanel.add(updateData,gbc);

	// Add check box to enable saving of patient data to database
	// (No listener needs to be added)
	m_saveToDBCbx = new JCheckBox(TestingPanelEvents.SAVETODATABASE);
    ypos++;
    gbc.gridy = ypos;
    fdPanel.add(m_saveToDBCbx, gbc);

	// New Patient Button
	JButton newPatient = new JButton(TestingPanelEvents.NEWPATIENT);
	newPatient.addActionListener(tpe);
	ypos++;
	gbc.gridy = ypos;
	fdPanel.add(newPatient,gbc);


	// Add commpute  button to panel
	JButton doIt = new JButton(TestingPanelEvents.COMPUTE);
	doIt.addActionListener(tpe);
	ypos++;
	gbc.gridy = ypos;
	fdPanel.add(doIt,gbc);
    }

    /*
     * Adjust testingpanel size, to size of tab in Protege
     * Default sizing is 800x600
     */
    public void changeSize(int w, int h) {
	split.setPreferredSize(new Dimension(w,h));
	int tw = Math.round((float) (w * (float) 0.75));
	entryPanel.setPreferredSize(new Dimension(tw, h));
	split.setDividerLocation(tw);
	tw = Math.round((float) (w * (float) 0.25));
	resultTabs.setPreferredSize(new Dimension(tw, h));
    }

    public void updateResults(String urlString) {
    urlString = urlString.replace(':', '|');
    urlString = urlString.replaceAll(" ", "%20");
	String furl = new String("file:///" + urlString +".html");
	System.out.println("url = " + furl);
	
//	if (htmlPaneBrief != null )
//	    resultsBrief.remove(htmlPaneBrief);
	if (htmlPaneDebug != null )
	    resultsDebug.remove(htmlPaneDebug);

//	JEditorPane htmlOutputBrief = new JEditorPane();
//	htmlOutputBrief.setEditable(false);
	JEditorPane htmlOutputDebug = new JEditorPane();
	htmlOutputDebug.setEditable(false);

	//htmlOutput.setSize(300,300);

	try {
	    //htmlOutputBrief.setPage(url + "-BRIEF.html");
	    htmlOutputDebug.setPage(furl);
	} catch (Exception e) {
	    System.err.println("Couldn't display results html page");
	    e.printStackTrace();
	}

	//htmlPaneBrief = new JScrollPane(htmlOutputBrief);
	htmlPaneDebug = new JScrollPane(htmlOutputDebug);

	//resultsBrief.add(htmlPaneBrief, BorderLayout.CENTER);
	//resultsBrief.revalidate();
	//resultsBrief.repaint();

	resultsDebug.add(htmlPaneDebug, BorderLayout.CENTER);
	resultsDebug.revalidate();
	resultsDebug.repaint();
    }


    void setCurrentPatient(PatientDataStore p) {
	patientDataEntryPanel.setCurrentPatient(p);
    }
}
