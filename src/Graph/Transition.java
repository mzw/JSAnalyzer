package Graph;

import Analyzer.Config;
import Analyzer.Util;

public class Transition extends Edge {
	
	
	public Transition(int id, int from, int to, String event, String guard, String action) {
		super(id, from, to);
		super.setEvent(event);
		super.addGuard(guard);
		super.addAction(action);
	}

	public String toString_dot() {
		return this.from + " -> " + this.to + " [label=\"" + this.getLabel4dot() + "\"]";
	}

	public String toString_xml() {
		String ret = "";
		
		ret += "<" + Config.transTag + " id=\"" + this.id + "\" " +
				"from=\"" + this.from + "\" to=\"" + this.to + "\" " +
				"event=\"" + Util.esc_xml(this.getEventLabel()) + "\" " +
				"guard=\"" + Util.esc_xml(this.getGuardLabel()) + "\" " +
				"action=\"" + Util.esc_xml(this.getActionLabel()) + "\" " +
				"/>";
		
		return ret;
	}
	
	private String getLabel4dot() {
		String ret = "";
		
		ret += this.getEventLabel();
		ret += (!"".equals(this.getGuardLabel()) ? " [" + this.getGuardLabel() + "]" : "");
		ret += (!"".equals(this.getActionLabel()) ? " /" + this.getActionLabel() : "");
		
		return Util.esc_dot(ret);
	}
	
	private String getLabel() {
		return this.getEventLabel() + " [" + this.getGuardLabel() + "] /" + this.getActionLabel();
	}
	
	public String getEventLabel() {
		return this.eventLabel == null ? "" : this.eventLabel;
	}
	
	public String getGuardLabel() {
		String ret = "";
		
		if(this.guards.size() == 0) {
			return ret;
		}
		
		for(int i = 0; i < this.guards.size()-1; i++) {
			ret += this.guards.get(i) + " && ";
		}
		
		String lastGuard = this.guards.get(this.guards.size()-1);
		ret += (lastGuard == null ? "" : lastGuard);
		
		return ret;
	}
	public String getActionLabel() {
		String ret = "";
		
		if(this.actions.size() == 0) {
			return ret;
		}
		
		for(int i = 0; i < this.actions.size()-1; i++) {
			ret += this.actions.get(i) + "; ";
		}

		String lastAction = this.actions.get(this.actions.size()-1);
		ret += (lastAction == null ? "" : lastAction);
		
		return ret;
	}
}
