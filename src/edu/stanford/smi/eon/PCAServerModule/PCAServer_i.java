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

/**
<p>
<ul>
<li> <b>Java Class</b> edu.stanford.smi.eon.PCAServerModule.PCAServer_i
<li> <b>Source File</b> edu/stanford/smi/eon/PCAServerModule/PCAServer_i.java
<li> <b>IDL Source File</b> PCAServer.idl
<li> <b>IDL Absolute Name</b> ::PCAServerModule::PCAServer
<li> <b>Repository Identifier</b> IDL:PCAServerModule/PCAServer:1.0
</ul>
<b>IDL definition:</b>
<pre>
    #pragma prefix "PCAServerModule"
    interface PCAServer {
      ::PCAServerModule::PCASession open_pca_session(
        in string kbURL,
        in string database,
        in string patient_id
      );
    };
</pre>
</p>
 */
package edu.stanford.smi.eon.PCAServerModule;
import edu.stanford.smi.eon.util.*;
import edu.stanford.smi.eon.clients.PCATest;
import edu.stanford.smi.eon.kbhandler.KBHandler;
import org.apache.log4j.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

import org.omg.CORBA.*;
import org.omg.CORBA.SystemException;

public class PCAServer_i { 
	public KBHandler kbManager;
	public String kbURLString;
	private String initFile = null;
	private String serverLogFile = null;
	private PrintStream ps=null;
	private java.text.SimpleDateFormat df
	= new java.text.SimpleDateFormat ("yyyyMMdd.HHmmss");
	static Logger logger = Logger.getLogger(PCAServer_i.class);

	/** Construct a persistently named object. */
	public PCAServer_i(java.lang.String name)
	throws PCA_Initialization_Exception {
		super();
	}
	/** Construct a transient object. */
	public PCAServer_i() {
		super();
	}

	public void setServerLog(String logFile) {
		this.serverLogFile = logFile;
		try {
			this.ps = new PrintStream(new FileOutputStream(logFile, true));
			System.setOut(ps);
			System.setErr(ps);
		} catch (IOException e) {
			logger.warn("Unable to open logFile; Maybe none is specified"+logFile);
			//e.printStackTrace();
		}
	}


	public void setInitFile(String initFile) {
		this.initFile = initFile;
	}

	/**
  <p>
  Reader for attribute: <b>::PCAServerModule::PCAServer::kbURL</b>.
  <pre>
    #pragma prefix "PCAServerModule/PCASession"
    attribute string kbURL;
  </pre>
  </p>
	 */
	public java.lang.String kbURL(){
		return kbURLString;
	}



	/**
  <p>
  Operation: <b>::PCAServerModule::PCAServer::open_pca_session</b>.
  <pre>
    #pragma prefix "PCAServerModule/PCAServer"
    ::PCAServerModule::PCASession open_pca_session(
      in string kbURL,
      in string database,
      in string guideline_name
      in string patient_id  ,
      in string session_time
    );
  </pre>
  </p>
	 */

	public edu.stanford.smi.eon.PCAServerModule.PCASession open_pca_session(
	) throws
	edu.stanford.smi.eon.PCAServerModule.PCA_Initialization_Exception
	{
		PCASession_i session = new PCASession_i();
		session.initPCASesion(kbManager);
		return (PCASession) session;
	}

	public edu.stanford.smi.eon.PCAServerModule.PCASession open_pca_session(
//			java.lang.String kbURL,
			java.lang.String database,
			java.lang.String user,
			java.lang.String password,
			java.lang.String initFile
	) throws
	edu.stanford.smi.eon.PCAServerModule.PCA_Initialization_Exception
	{
		// IMPLEMENT: Operation
		if (this.initFile != null) initFile = this.initFile;
		String sessionKey = df.format(new Date());
		try {
			PCASession_i session = new PCASession_i(database, user, password, initFile);
			logger.info("open_pca_session serverLogFile: "+serverLogFile);
			session.setSessionId(user + sessionKey);
			session.initPCASesion(kbManager);
			return (PCASession) session;
		} catch (Throwable e) {
			logger.error(sessionKey + " PCAServer_i.open_pca_session Exception "
					+e.getMessage(), e);
			throw new PCA_Initialization_Exception(sessionKey +": " +e.getMessage());
		}
	}
}

