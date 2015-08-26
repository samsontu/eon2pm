package gov.va.test.opioidtesttool.degui;
import edu.stanford.smi.protege.model.*;

public class TreeNodeData {
	private Frame frame;
	
	public TreeNodeData(Frame frame) {
		this.frame = frame;
	}
	
	public String toString() {
		return frame.getBrowserText();
	}
	
	public String getName() {
		return frame.getName();
	}

}
