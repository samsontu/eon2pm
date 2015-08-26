package edu.stanford.smi.eon.siteCustomization;

import edu.stanford.smi.eon.execEngine.EON;
import edu.stanford.smi.eon.execEngine.IDataSource;
import edu.stanford.smi.eon.execEngine.IEON;
import edu.stanford.smi.eon.kbhandler.KBHandler;


public class BMIREon extends EON {
	

	public BMIREon(KBHandler kb, IDataSource aDS) throws Exception {
		super(kb, aDS);
		
	}
	
	public BMIREon(KBHandler kb) {
		super(kb);
		
	}

}
