package jp.mzw.jsanalyzer.modeler.model.fsm;

import jp.mzw.jsanalyzer.modeler.model.graph.Edge;
import jp.mzw.jsanalyzer.modeler.model.interaction.Event;

public class Transition extends Edge {
	
	protected Edge mEdge;
	
	protected String mFromStateId;
	protected String mToStateId;
	
	
	public Transition(String fromStateId, String toStateId) {
		super(fromStateId, toStateId);
		
		this.mFromStateId = fromStateId;
		this.mToStateId = toStateId;
	}
	
	public void setEdge(Edge edge) {
		this.mEdge = edge;
	}
	
	public void setCond(Edge edge) {
		this.mCondObj = edge.getCondObj();
		this.mCondNegate = edge.getCondNegate();
	}
	
	public String getFromStateId() {
		return this.mFromNodeId;
	}
	public String getToStateId() {
		return this.mToStateId;
	}
	
	public void setEvent(Event event) {
		this.mEvent = event;
	}

}
