package jp.mzw.jsanalyzer.modeler.model.graph;

import jp.mzw.jsanalyzer.modeler.model.Element;
import jp.mzw.jsanalyzer.modeler.model.interaction.Event;
import jp.mzw.jsanalyzer.rule.Function;
import jp.mzw.jsanalyzer.rule.Rule;
import jp.mzw.jsanalyzer.rule.Trigger;
import jp.mzw.jsanalyzer.util.StringUtils;

import org.jsoup.nodes.Attribute;
import org.mozilla.javascript.ast.AstNode;

/**
 * Represents a directed edge
 * @author Yuta Maezawa
 *
 */
public class Edge extends Element {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Event mEvent;
	
	protected Object mCondObj;
	protected boolean mCondNegate;
	
	/**
	 * Constructor
	 */
	public Edge(String fromNodeId, String toNodeId) {
		this.mFromNodeId = fromNodeId;
		this.mToNodeId = toNodeId;
		
		this.mEvent = null;
		
		this.mCondObj = null;
		this.mCondNegate = false;
	}
	
	/**
	 * Clones this edge
	 */
	public Edge clone() {
		Edge edge = new Edge(this.mId, this.mFromNodeId, this.mToNodeId, this.mEvent, this.mCondObj, this.mCondNegate);
		return edge;
	}
	/**
	 * Constructor for cloning
	 * @param id
	 * @param fromNodeId
	 * @param toNodeId
	 * @param event
	 * @param condObj
	 * @param negate
	 */
	private Edge(String id, String fromNodeId, String toNodeId, Event event, Object condObj, boolean negate) {
		this.mId = id;
		this.mFromNodeId = fromNodeId;
		this.mToNodeId = toNodeId;
		this.mEvent = event;
		this.mCondObj = condObj;
		this.mCondNegate = negate;
	}

	/**
	 * Represents a node ID which this edge comes from
	 */
	protected String mFromNodeId;
	/**
	 * Represents a node ID which this edge goes to
	 */
	protected String mToNodeId;
	/**
	 * Gets this from node ID
	 * @return This from node ID
	 */
	public String getFromNodeId() {
		return this.mFromNodeId;
	}
	/**
	 * Gets this to node ID
	 * @return This to node ID
	 */
	public String getToNodeId() {
		return this.mToNodeId;
	}
	
	public void setEvent(org.jsoup.nodes.Element target, Attribute event, Trigger rule) {
		this.mEvent = new Event(target, event, rule);
	}
	public void setEvent(AstNode target, AstNode event, Rule rule) {
		this.mEvent = new Event(target, event, rule); // null means solving after
	}
	public void setEvent(AstNode event, Function rule) {
		this.mEvent = new Event(null, event, rule); // null means solving after
		this.mEvent.setEventModifier(rule.getEventModifierType());
	}
	
	public Event getEvent() {
		return this.mEvent;
	}
	
	
	public void setCond(AstNode cond, boolean negate) {
		this.mCondObj = cond;
		this.mCondNegate = negate;
	}
	
	public Object getCondObj() {
		return this.mCondObj;
	}
	public boolean getCondNegate() {
		return this.mCondNegate;
	}
	
	public AstNode getCond() {
		if(this.mCondObj instanceof AstNode) {
			return (AstNode)this.mCondObj;
		}
		return null;
	}
	
	public boolean hasEvent() {
		if(this.mEvent == null) {
			return false;
		}
		return this.mEvent.hasEvent();
	}
	public boolean hasCond() {
		if(this.mCondObj != null) {
			return true;
		}
		return false;
	}
	
	public void setFromNodeId(String nodeId) {
		this.mFromNodeId = nodeId;
	}
	public void setToNodeId(String nodeId) {
		this.mToNodeId = nodeId;
	}
	
	public String getDotLabel() {
		String ret = "";
		
		ret += "[label=\"";
		// event
		if(this.mEvent != null) {
			ret += this.mEvent.getEvent();
		}
		// guard
		if(this.getCond() != null) {
			ret += "[";
			if(this.mCondNegate) {
				ret += "NOT ";
			}
			ret += StringUtils.esc_dot(this.getCond().toSource()) + "]";
		}
		ret += "\"]";
		
		return ret;
	}
}
