package gov.va.test.opioidtesttool.degui;

import gov.va.test.opioidtesttool.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import com.toedter.calendar.*;


import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.model.Project;

public class DEDialog extends JDialog {

    
    // Handle all events associated with Dialog Box
    private DEEvents DEEventHandler;

    // need parent panel to access dataModel
    // ref to data only stored in parent panel
    // this ref will change, when the Patient changes, 
    public DEPanel parent;

    //***
    
    // dialog mode
    String title = "";
    String mode = "";
    // selected row
    int rowIndex;
    
    // tree panel
    JSplitPane treeFieldsPane = null;
    JScrollPane treeSP = new JScrollPane();
    JTree nameTree = null;
    
    // fields
    JTextField firstField = new JTextField(20);
    JTextField secondField = new JTextField(20);

    // dates
    JDateChooser startDate, endDate; 




    
    public DEDialog(String _title, String _mode, DEPanel _parent, JTree tree) {
	title = _title;
	mode = _mode;
	parent = _parent;
	nameTree = tree;	

	setTitle(title);	
	setSize(700, 400);
	//    setBackground(new Color(255,255,238));
	
	DEEventHandler = new DEEvents(this);
	// some dialogs will have no tree
	if ( nameTree != null ) {
	    nameTree.addTreeSelectionListener(DEEventHandler);
	}

	getContentPane().setLayout(new BorderLayout());
	getContentPane().add(buildTitlePanel(), BorderLayout.NORTH);
	getContentPane().add(buildTreeFieldsPane(), BorderLayout.CENTER);
	getContentPane().add(buildButtonsPanel(), BorderLayout.SOUTH);
    } // constructor MedDialog
    
    public JPanel buildTitlePanel() {
	JPanel titlePanel = new JPanel(new BorderLayout());
	JLabel titleLabel = new JLabel();

	if (mode.equals(DEEvents.NEW)) {
	    titleLabel.setText(DEEvents.NEW);
	} else if (mode.equals(DEEvents.DELETE)) {
	    titleLabel.setText(DEEvents.DELETE);
	} else if (mode.equals(DEEvents.MODIFY)) {
	    titleLabel.setText(DEEvents.MODIFY);
	}
	titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
	titleLabel.setFont(new Font("Times-Roman",
				    Font.BOLD,
				    18));
	titlePanel.add(titleLabel, BorderLayout.CENTER);
	return titlePanel;
    } // buildTitlePanel
    
    public JSplitPane buildTreeFieldsPane() {
	JPanel fieldsPanel = buildFieldsPanel();
	treeFieldsPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
					treeSP, fieldsPanel);
	treeFieldsPane.setOneTouchExpandable(true);
	treeFieldsPane.setContinuousLayout(true);
	treeFieldsPane.setDividerLocation(200);
	return treeFieldsPane;
    } // buildTreeFieldsPane
    
    public JPanel buildFieldsPanel() {
	// field 1
	JPanel fieldsPanel = new JPanel();	
	JLabel firstLabel = new JLabel(parent.dataModel.getColumnName(0));
	firstLabel.setForeground(new Color(99,0,66));
	firstField.setForeground(new Color(0,0,128));
	firstField.setFont( new Font("Times-Roman", Font.BOLD+Font.ITALIC, 12));

	// field 2
	JLabel secondLabel = new JLabel(parent.dataModel.getColumnName(1));
	secondLabel.setForeground(new Color(99,0,66));
	secondField.setForeground(new Color(0,0,128));
	secondField.setFont( new Font("Times-Roman", Font.BOLD+Font.ITALIC, 12));	

	// startDate
	JLabel startDateLabel = new JLabel("Start");
	startDateLabel.setForeground(new Color(99,0,66));
	startDate = new JDateChooser(GlobalVars.sessionTime, false);

	// endDate
	JLabel endDateLabel = new JLabel("End");
	endDateLabel.setForeground(new Color(99,0,66));
	endDate = new JDateChooser(GlobalVars.sessionTime, false);



	JButton selectButton = new JButton(DEEvents.SELECT);
	selectButton.setBackground(new Color(0,48,16));
	selectButton.setForeground(new Color(255,255,255));
	selectButton.addActionListener(DEEventHandler);	
			
	if (mode.equals(DEEvents.DELETE) || nameTree == null) {
	    // either delete dialog, or no name tree for this dialog
	    selectButton.setEnabled(false);
	}
	
	GridBagLayout gbl = new GridBagLayout();
	GridBagConstraints gbc = new GridBagConstraints();
	
	fieldsPanel.setLayout(gbl);

	int ypos = 1;

	gbc.anchor = GridBagConstraints.WEST;
	gbc.gridwidth = 1;
	gbc.gridy = ypos;
	gbc.insets = new Insets(10, 0, 10, 0);
	fieldsPanel.add(firstLabel, gbc);
	fieldsPanel.add(firstField, gbc);
	gbc.gridwidth = GridBagConstraints.REMAINDER;
	gbc.fill = GridBagConstraints.NONE;
	fieldsPanel.add(selectButton, gbc);

	if ( parent.dataModel.getNumFields() >= 2 ) {
	    gbc.gridwidth = 1;
	    gbc.gridy = ++ypos;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    fieldsPanel.add(secondLabel, gbc);
	    fieldsPanel.add(secondField, gbc);
	}
	
	if ( parent.dataModel.getNumDates() >= 1 ) {
	    gbc.gridwidth = 1;
	    gbc.gridy = ++ypos;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    fieldsPanel.add(startDateLabel, gbc);
	    fieldsPanel.add(startDate, gbc);
	}

	if ( parent.dataModel.getNumDates() >= 2 ) {
	    gbc.gridwidth = 1;
	    gbc.gridy = ++ypos;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    fieldsPanel.add(endDateLabel, gbc);
	    fieldsPanel.add(endDate, gbc);
	}

	gbc.gridwidth = GridBagConstraints.REMAINDER;
	gbc.fill = GridBagConstraints.NONE;
	
	return fieldsPanel;
    } // buildFieldsPanel
    
    public JPanel buildButtonsPanel() {
	JPanel buttonsPanel = new JPanel();
	JButton admButton = new JButton();
	admButton.setBackground(new Color(0,48,16));
	admButton.setForeground(new Color(255,255,255));
	if (mode.equals(DEEvents.NEW)) {
	    admButton.setText(DEEvents.ADD);
	    admButton.addActionListener(DEEventHandler);	    
	} else if (mode.equals(DEEvents.DELETE)) {
	    admButton.setText(DEEvents.DELETE);
	    admButton.addActionListener(DEEventHandler);
	} else if (mode.equals(DEEvents.MODIFY)) {
	    admButton.setText(DEEvents.MODIFY);
	    admButton.addActionListener(DEEventHandler);
	} // if mode

	buttonsPanel.add(admButton);
	JButton cancelButton = new JButton("Cancel");
	cancelButton.setBackground(new Color(0,48,16));
	cancelButton.setForeground(new Color(255,255,255));
	cancelButton.addActionListener(DEEventHandler);
	buttonsPanel.add(cancelButton);

	return buttonsPanel;
  }

    /*
     * This method, allows us to keep track of current meds model reference
     * plus the row in the table we are currently working on
     * called from button push events
     * as can see, can deal with null values being passed in
     */
    public void setFields(int _rowIndex, String _first, String _second, Date _start, Date _end) {
	rowIndex = _rowIndex;
	firstField.setText(_first);
	if ( _second != null ) 
	    secondField.setText(_second);
	if ( _start != null )
	    startDate.setDate(_start);
	if ( _end != null ) 
	    endDate.setDate(_end);
    } // setFields

    // Hide dialog after we don't wnat to show it any more
    // clear nameTree selection if it exists
    public void hideAndClear() {
	if ( nameTree != null ) 
	    nameTree.clearSelection();
	setVisible(false);
    }

    void useTreeToSetSecondField() {
	DEEventHandler.useTreeToSetSecondField();
    }


    public String toString() {
	return mode;
    }
} // class MDialog

