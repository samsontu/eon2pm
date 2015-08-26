/**
 * 
 */
package edu.stanford.smi.eon.execEngine;

import edu.stanford.smi.eon.PCAServerModule.PCA_Initialization_Exception;
import edu.stanford.smi.eon.kbhandler.KBHandler;

/**
 * @author swt
 *
 */
public interface IEonFactory {
    public IEON createEON(String ProtegeProjFullPath) throws PCA_Initialization_Exception;
	public IEON createEON(String ProtegeProjFullPath, IDataSource aDS) throws PCA_Initialization_Exception; 
	public IEON createEON(KBHandler kbHandler) throws PCA_Initialization_Exception; 
		// Equivalent to a = createEON(ProtegeProjFullPath) followed by a.setDataSource(aDS)

}
