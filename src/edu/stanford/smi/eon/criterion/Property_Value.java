package edu.stanford.smi.eon.criterion;

import org.apache.log4j.Logger;

import edu.stanford.smi.protege.model.*;

public class Property_Value extends DefaultSimpleInstance {
	public Property_Value(KnowledgeBase kb, FrameID id ) {
		super(kb, id);
	}
	static  Logger  logger = Logger.getLogger(Property_Value.class);

	public void setstring_nameValue(String expression) {
		ModelUtilities.setOwnSlotValue(this, "string_name", expression);	}
	public String getstring_nameValue(){
		return  (String) ModelUtilities.getOwnSlotValue(this, "string_name");
	}
	public void setslotValue(Slot slotName) {
		ModelUtilities.setOwnSlotValue(this, "slot", slotName);	}
	public Slot getslotValue() {
		return ((Slot) ModelUtilities.getOwnSlotValue(this, "slot"));
	}

}
