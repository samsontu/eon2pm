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

//Title:        PCAServer
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Samson Tu
//Company:      Stanford Medical Informatics
//Description:  Protocol Compliance Manager Server for the EON project


package edu.stanford.smi.eon.servers;
import edu.stanford.smi.eon.PCAServerModule.*;
import edu.stanford.smi.eon.clients.PCATest;
import edu.stanford.smi.eon.kbhandler.KBHandler;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

import org.apache.log4j.*;



public class PCAServer extends Frame{
  private boolean invokedStandalone = true;
  static Logger logger = Logger.getLogger(PCAServer.class.getName());

  public PCAServer() {
  }

  public PCAServer(String objectName)
  {
    super(objectName);
    setResizable(true);
    setSize(new Dimension(200, 50));
    add(new TextField("Object name: " + objectName));
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        setVisible(false);         // hide the Frame
        dispose();
        System.exit(0);
      }
    });
    show();
  }

  public static void main(String[] argv) {
    if (argv.length < 2) {
        logger.error("PCA needs object name and initialization file");
	      return;
    }
    String objectName = argv[0];
    String initfile = argv[1];
 //   Properties orbProperties = new Properties();;
    logger.info("PCA:initfile: " + initfile);


    PCAServer pCAServer = new PCAServer(objectName);
    pCAServer.invokedStandalone = true;
    PCAServer_i PCAImp = null;
    Properties settings = new Properties();
    String kbURL=null;
    try {
            FileInputStream sf = new FileInputStream(initfile);
            settings.load(sf);
        }
    catch (Exception ex) {
        logger.error("Exception during loading initialization file");
        logger.error(ex.toString());
    }
    PropertyConfigurator.configure(initfile);
    kbURL = settings.getProperty("SERVERKB", "");
    String serverLogFile = settings.getProperty("SERVER_LOGFILE");

    try {
      PCAImp = new PCAServer_i(objectName);
      if ((serverLogFile != null) && (!serverLogFile.equals("")) &&
        (!serverLogFile.equals("no"))){
        PCAImp.setServerLog(serverLogFile);
      }
      PCAImp.setInitFile(initfile);
      PCAImp.kbManager = new KBHandler(kbURL);
      PCAImp.kbURLString = kbURL;
    } catch(Exception se) {
      logger.error("Exception raised during creation of PCAServer_i " +
                     se.toString());
      System.exit(1);
    }

/*  Registere with daemon
    try  {
      boa.obj_is_ready(PCAImp);
      logger.info("PCAServer ready");
      boa.impl_is_ready();
    }
    catch(SystemException se) {
      logger.error("Exception raised  impl_is_ready : " +
                     se.toString());
      System.exit(1);
    }

 */
  }


  public synchronized void show() {
    setLocation(650, 50);
    super.show();
  }

 /* public void processEvent(AWTEvent event) {

    if (event.id == Event.WINDOW_DESTROY) {
      setVisible(false);         // hide the Frame
      dispose();      // tell windowing system to free resources
      System.exit(0); // exit

    }
    super.processEvent(event);
  } */

}
