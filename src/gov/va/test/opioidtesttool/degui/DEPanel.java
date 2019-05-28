package gov.va.test.opioidtesttool.degui;
/*
 * Data Entry Panel
 */

import gov.va.test.opioidtesttool.*;
import java.awt.*;
import java.util.*;
import java.util.Vector;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.tree.*;
import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.model.Project;

import sun.awt.*;


public class DEPanel extends JPanel {
    /*
     * Note the format of Object Array
     * rowData[0] = String (can be typed or set from Tree view)
     * rowData[1] = String or numeric value
     * rowData[2] = date
     * rowData[3] = date
     */
    private int cols = 4;   
    private Object[] rowData = new Object[cols];

    public TETableModel dataModel;
    private String title;
    private boolean useTree = false;

    // widgets
    public JTable dataTable = null;
    public JScrollPane dataTableSP = new JScrollPane();
    public DEDialog addDialog;
    public DEDialog modifyDialog;
    public DEDialog deleteDialog;
    static JTree nameTree = null;
    
    // event handler
    private DEEvents DEEventHandler;

    


    /*
     * A data entry panel
     * can be used select and edit the values of
     * 4 data entry point 
     */
    public DEPanel(String _title, TETableModel _mod, 
		   String rootName, String[] subRoots
		   ) {
	// copy values
	title = new String(_title);
	dataModel = _mod;
	dataTable = new JTable(dataModel);

	setLayout(new BorderLayout());
	setBorder(BorderFactory.createLineBorder(new Color(0, 104, 24), 2));

	JPanel topPanel = new JPanel(new BorderLayout());
	JLabel titleLabel = new JLabel(title, JLabel.CENTER);

	titleLabel.setForeground(new Color(99,0,66));
	titleLabel.setFont(new Font("Times-Roman",
				    Font.BOLD + Font.ITALIC,
				    12));
	topPanel.add(titleLabel, BorderLayout.CENTER);
	
	if ( rootName != null )
	    nameTree = buildNameTree(rootName, subRoots, subRoots.length );

	// Create Add, delete, modify dialog boxes
	addDialog = new DEDialog(title, DEEvents.NEW, this, nameTree);
	deleteDialog = new DEDialog(title, DEEvents.DELETE, this, nameTree);
	modifyDialog = new DEDialog(title, DEEvents.MODIFY, this, nameTree);
	DEEventHandler = new DEEvents(this);

	// Buttons and event handlers
	JButton addButton = new JButton(DEEvents.NEW);
	addButton.addActionListener(DEEventHandler);
	JButton deleteButton = new JButton(DEEvents.DELETE);
	deleteButton.addActionListener(DEEventHandler);
	JButton modifyButton = new JButton(DEEvents.MODIFY);
	modifyButton.addActionListener(DEEventHandler);
	
	// place buttons on toolbar
	JToolBar toolbar = new JToolBar();
	toolbar.add(addButton);
	toolbar.add(deleteButton);
	toolbar.add(modifyButton);
	topPanel.add(toolbar, BorderLayout.EAST);
	
	add(topPanel, BorderLayout.NORTH);	
	add(dataTableSP, BorderLayout.CENTER);
	
	dataTableSP.setViewportView(dataTable);	
    }

    /*
     * When we change a Patient Record
     * Need to update tablemodel in interface
     */
    public void setCurrentDataModel(TETableModel tm) {
	dataModel = tm;
	dataTable.setModel(tm);
    }

    private JTree buildNameTree(String rootName, String[] subRoots, int numSubRoots) {	
	
	JTree tree = null; 
	int x;
	Cls rootCls = GlobalVars.kb.getCls(rootName);
	if (rootCls == null) {
		System.out.println("Class not found in kb -" + rootName);
	} else {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(new TreeNodeData(rootCls));
		tree = new JTree(new DefaultTreeModel(root));
		for (x=0; x< subRoots.length ;x++ ) {	
			Cls currentClass = GlobalVars.kb.getCls(subRoots[x]);
			if (currentClass == null) {
				System.out.println("Class not found in kb -" + subRoots[x]);
			} else {
				DefaultMutableTreeNode sub_root = 
					new DefaultMutableTreeNode(new TreeNodeData(currentClass));
	    
				//System.out.println("WE ARE HERE!\n");
				root.add(sub_root);
				buildRestOfNameTree(sub_root, currentClass);
			}
		}
	}
	// System.out.println("Finished building tree?\n");
	tree.setShowsRootHandles(true);
	tree.expandRow(0);
	// Listner for tree selection event is added in DEDialog constructor
	return tree;
    } // buildNameTree

    private void buildRestOfNameTree(DefaultMutableTreeNode parent, Cls currentClass) {
	Iterator classIterator;
	DefaultMutableTreeNode child;
	Collection classes = currentClass.getDirectSubclasses();
	classIterator = classes.iterator();
	while (classIterator.hasNext()) {
	    currentClass = (Cls) classIterator.next();
	    //Change cName to TreeNodeData instead of class name
	    TreeNodeData cName = new TreeNodeData(currentClass);
	    child = new DefaultMutableTreeNode(cName);
	    parent.add(child);
	    buildRestOfNameTree(child, currentClass);
	}
    } // buildRestOfNameTree        

    /* 
     * In the case of DX panels
     * We want to fill in the first field based on the parent
     * of the selected node, and the second field based on
     * the selected node. 
     * (i.e. the parent is the Disease and the leaf is the ICD9 code)
     * This is a hack to propogate that change
     */
    void useTreeToSetSecondField() {
	addDialog.useTreeToSetSecondField();
	modifyDialog.useTreeToSetSecondField();
	deleteDialog.useTreeToSetSecondField();
    }
}
