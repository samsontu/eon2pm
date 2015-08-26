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
package edu.stanford.smi.eon.PCAServerModule;
/**
<p>
<ul>
<li> <b>Java Class</b> edu.stanford.smi.eon.PCAServerModule.PCAServer
<li> <b>Source File</b> edu/stanford/smi/eon/PCAServerModule/PCAServer.java
<li> <b>IDL Source File</b> PCAServer.idl
<li> <b>IDL Absolute Name</b> ::PCAServerModule::PCAServer
<li> <b>Repository Identifier</b> IDL:PCAServerModule/PCAServer:1.0
</ul>
<b>IDL definition:</b>
<pre>
    #pragma prefix "PCAServerModule"
    interface PCAServer {
      readonly attribute string kbURL;
      ::PCAServerModule::PCASession open_pca_session(
        in string database,
        in string user,
        in string password,
        in string initFile
      )
      raises(
        ::PCAServerModule::PCA_Initialization_Exception
      );
    };
</pre>
</p>
*/
public interface PCAServer { //extends com.inprise.vbroker.CORBA.Object {
  /**
  <p>
  Reader for attribute: <b>::PCAServerModule::PCAServer::kbURL</b>.
  <pre>
    #pragma prefix "PCAServerModule/PCAServer"
    readonly attribute string kbURL;
  </pre>
  </p>
  */
  public java.lang.String kbURL();
  /**
  <p>
  Operation: <b>::PCAServerModule::PCAServer::open_pca_session</b>.
  <pre>
    #pragma prefix "PCAServerModule/PCAServer"
    ::PCAServerModule::PCASession open_pca_session(
      in string database,
      in string user,
      in string password,
      in string initFile
    )
    raises(
      ::PCAServerModule::PCA_Initialization_Exception
    );
  </pre>
  </p>
  */
  public edu.stanford.smi.eon.PCAServerModule.PCASession open_pca_session(
    java.lang.String database,
    java.lang.String user,
    java.lang.String password,
    java.lang.String initFile
  ) throws
    edu.stanford.smi.eon.PCAServerModule.PCA_Initialization_Exception;
}
