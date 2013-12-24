package jp.mzw.jsanalyzer.modeler.model.fsm;

import jp.mzw.jsanalyzer.modeler.model.graph.Edge;

public class Transition extends Edge {
	
	protected String mFromStateId;
	protected String mToStateId;
	
	
	public Transition(String fromStateId, String toStateId) {
		super(fromStateId, toStateId);
		
		this.mFromStateId = fromStateId;
		this.mToStateId = toStateId;
	}
	
	public String getFromStateId() {
		return this.mFromNodeId;
	}
	public String getToStateId() {
		return this.mToStateId;
	}
	
	public String getDotLabel() {
		String ret = "";
		
		return ret;
	}

}
