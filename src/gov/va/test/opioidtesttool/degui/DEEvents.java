package gov.va.test.opioidtesttool.degui;

import gov.va.test.opioidtesttool.*;

import java.awt.*;
import java.util.*;
import java.text.*;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.tree.*;
import javax.swing.border.*;
import javax.swing.event.*;

import java.text.SimpleDateFormat;

import edu.stanford.smi.protege.model.*;
import sun.awt.*;
import sun.awt.windows.*;


/*
 * This class handles all events generated in the
 * Meds Dialog
 */

public class DEEvents implements ActionListener, TreeSelectionListener {
    // label for button to add new drug
    public static final String NEW = "New";
    public static final String DELETE = "Delete";
    public static final String MODIFY = "Modify";
    public static final String ADD = "Add";
    public static final String SELECT = "Select";
    public static final String CANCEL = "Cancel";

    private DEPanel pan = null;
    private DEDialog diag = null;

    // see DEPanel.java for description (useTreeToSetSecondField())
    boolean treeSecondField = false;

    // Handle events from DEPanel
    public DEEvents(DEPanel _panel) {
	pan = _panel;
    }

    // Handle events from DEDialog
    public DEEvents(DEDialog _dialog) {
	diag = _dialog;
    }

     public void actionPerformed(ActionEvent ae) {
	String eventName = ae.getActionCommand();

	/*
	 * Events from DEPanel
	 */
	if ( pan != null ) {
	    if ( eventName.equals(NEW) ) {
		/* Called from DEPanel New Drug, Button */
		long t = System.currentTimeMillis();
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = dateFormat.parse(GlobalVars.sessionTime);
			t = date.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		long mon = (long) (30*24) *   (long) (60*60) * (long) 1000;
		mon = (long) mon + t;
		pan.addDialog.setFields(-1, " ", " ", new Date(t), new Date(mon));
		pan.addDialog.show();
		System.out.println("New Panel!");
	    } else if  ( eventName.equals(DELETE) ) {
		// Delete currently selected drug.
		System.out.println("Delete Panel!");
		int rowIndex = pan.dataTable.getSelectedRow();
		if (rowIndex != -1) { // i.e. a drug is selected
		    display_dialog(pan.dataModel,rowIndex, pan.deleteDialog);
		}
	    } else if  ( eventName.equals(MODIFY) ) {
		// modify currently selected drug
		System.out.println("Modify Panel!");
		int rowIndex = pan.dataTable.getSelectedRow();
		if (rowIndex != -1) { // i.e. a drug is selected
		    display_dialog(pan.dataModel,rowIndex, pan.modifyDialog);
		}
	    }
	} // if mp


	/*
	 * Events from DEDialog
	 */
	if ( diag != null ) {
	    if ( eventName.equals(ADD) ) {
		System.out.println("Adding a new item to table!\n");

		diag.parent.dataModel.teAddRow1(
				      diag.firstField.getText(),
				      diag.secondField.getText(),
				      diag.startDate.getDate(),
				      diag.endDate.getDate()
				      );

		// now that we added the drug, hide window
		diag.hide();
	    } else if  ( eventName.equals(SELECT) ) {
		/* Clicked select Drug button, create tree
		 * to view all drugs */
		diag.treeSP.setViewportView(diag.nameTree);
		diag.treeFieldsPane.updateUI();
		diag.treeFieldsPane.setDividerLocation(200);

	    } else if  ( eventName.equals(MODIFY) ) {
		System.out.println("Going to modify an item");

		diag.parent.dataModel.teModifyRow(
					   diag.rowIndex,
					   diag.firstField.getText(),
					   diag.secondField.getText(),
					   diag.startDate.getDate(),
					   diag.endDate.getDate()
					   );

		diag.hide();
	    } else if ( eventName.equals(DELETE) ) {
		System.out.println("Going to delete");
		diag.parent.dataModel.removeRow(diag.rowIndex);
		diag.hide();
	    }  else if ( eventName.equals(CANCEL) ) {
		diag.hide();
	    }
	} // if md

    }    // actionPerformed

    /*
     * Handles when user selects new item
     * from tree
     */
    public void valueChanged(TreeSelectionEvent e) {
	TreePath path = e.getPath();
	DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
	TreeNodeData info = (TreeNodeData) node.getUserObject();
	// "info" is the TreeNodeData that holds a frame, n.
	// use treeSecondField when selecting from medical conditions
	// Nadeem assumes that the leafnode is always an ICD code
	// Now we use the second field to hold browser text
	//System.err.println("treeSecondField = " + treeSecondField);
	if ( treeSecondField ) {
		String secondFieldText = info.toString();
		//DefaultMutableTreeNode parent =  (DefaultMutableTreeNode) node.getParent();
		//String textInfo = (String) parent.getUserObject();
		if (info.getName().equals(secondFieldText)) {
			if (isICD(info.getName())) {
				DefaultMutableTreeNode parent =  (DefaultMutableTreeNode) node.getParent();
				secondFieldText = ((TreeNodeData) parent.getUserObject()).toString();
			} else secondFieldText = null;
		} 
		diag.firstField.setText(info.getName());
		diag.secondField.setText(secondFieldText);
	} else {
		diag.firstField.setText(info.getName());
	}
	/*
	       System.out.println("Updating text field! " + info);
	       System.out.println(diag.toString());
	 */
    }
    
    private boolean isICD(String name) {
    	Pattern p = Pattern.compile("\\d+.?\\d*");
    	Matcher m = p.matcher(name);
    	return m.matches();
    
    	// return false;
    }

    public void useTreeToSetSecondField() {
	treeSecondField = true;
    }

    /*
     * Little routine to help display delete and modify dialog
     * when they are clincked on from DEPanel
     */
    private void display_dialog(TETableModel mm, int rowIndex, DEDialog mdg) {
	String firstField = mm.getFirstField(rowIndex);
	// data values will be null if not used, setFields can handle that
	String secondField = mm.getSecondField(rowIndex);
	Date start = mm.getStart(rowIndex);
	Date end = mm.getEnd(rowIndex);
	System.out.println(start);
	mdg.setFields(rowIndex, firstField, secondField, start, end);
	mdg.show();
    }
}
