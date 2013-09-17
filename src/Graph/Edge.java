package Graph;

import java.util.List;
import java.util.LinkedList;

import org.jsoup.nodes.Attribute;

import Modeler.Interaction;

public class Edge extends Element {

	protected int from = 0;
	protected int to = 0;

	protected String eventLabel = "";
	protected org.jsoup.nodes.Element eventElm = null;
	protected Attribute eventAttr = null;
	protected org.mozilla.javascript.ast.AstNode objAstNode = null;
	protected org.mozilla.javascript.ast.AstNode eventAstNode = null;
	protected boolean hasEvent = false;
	
	protected List<String> guards = null;
	protected boolean hasGuard = false;
	protected String guardLabel = "";
	
	protected List<String> actions = null;
	
	protected boolean isAbsracted = false;
	
	public Edge(int id, int from, int to) {
		super(id);
		this.from = from;
		this.to = to;
		
		this.eventAttr = null;
		this.eventElm = null;
		this.objAstNode = null;
		this.eventAstNode = null;
		this.hasEvent = false;
		
		this.guards = new LinkedList<String>();
		this.hasGuard = false;
		this.guardLabel = "";
		
		this.actions = new LinkedList<String>();
		
		this.isAbsracted = false;
	}
	public int getFrom() {
		return this.from;
	}
	public void setFrom(int from) {
		this.from = from;
	}
	public int getTo() {
		return this.to;
	}
	public void setTo(int to) {
		this.to = to;
	}
	
	///// event
	public void setEvent(org.jsoup.nodes.Element elm, Attribute attr) {
		this.eventElm = elm;
		this.eventAttr = attr;
		this.hasEvent = true;
		this.eventLabel = attr.getKey();
	}
	public void setEvent(org.mozilla.javascript.ast.AstNode objAstNode, org.mozilla.javascript.ast.AstNode eventAstNode) {
		this.objAstNode = objAstNode;
		this.eventAstNode = eventAstNode;
		this.hasEvent = true;
		this.eventLabel = eventAstNode.toSource();
	}
	public boolean hasEvent() {
		return this.hasEvent;
	}
	public void setEventLabel(String eventLabel) {
		this.eventLabel = eventLabel;
	}
	public String getEventLabel() {
		return this.eventLabel;
	}
	public Interaction getInteraction() {
		if(this.eventElm != null) {
			return new Interaction(this, this.eventElm, this.eventAttr);
		}
		if(this.eventAstNode != null) {
			return new Interaction(this, this.eventAstNode, this.objAstNode);
		}
		System.out.println("Error@Edge.getInteraction: Element == null and AstNode == null");
		return null;
	}
	
	public org.mozilla.javascript.ast.AstNode getEventAstNode() {
		return this.eventAstNode;
	}
	
	///// condition
	protected org.mozilla.javascript.ast.AstNode cond = null;
	public org.mozilla.javascript.ast.AstNode getCondNode() {
		return this.cond;
	}
	public void setCondNode(org.mozilla.javascript.ast.AstNode cond) {
		this.hasGuard = true;
		this.guardLabel = cond.toSource();
		this.cond = cond;
	}
	public void setElseCondNode(org.mozilla.javascript.ast.AstNode astNode) {
		this.hasGuard = true;
		this.guardLabel = this.getElseCondLabel(astNode);
		this.cond = astNode; // to be debugged
	}
	private String getElseCondLabel(org.mozilla.javascript.ast.AstNode astNode) {
		String ret = "";
		if(astNode instanceof org.mozilla.javascript.ast.IfStatement) {
			org.mozilla.javascript.ast.IfStatement _astNode = (org.mozilla.javascript.ast.IfStatement)astNode;
			// collect conditions
			List<org.mozilla.javascript.ast.IfStatement> conds = new LinkedList<org.mozilla.javascript.ast.IfStatement>();
			conds.add(_astNode);
			org.mozilla.javascript.ast.AstNode elsePart = _astNode.getElsePart();
			while(elsePart != null) {
				if(elsePart instanceof org.mozilla.javascript.ast.IfStatement) {
					org.mozilla.javascript.ast.IfStatement _elsePart = (org.mozilla.javascript.ast.IfStatement)elsePart;
					conds.add(_elsePart);
					elsePart = _elsePart.getElsePart();
				} else {
					break;
				}
			}
			// generate else condition label
			ret = "!(";
			int i = 0;
			for(; i < conds.size() - 1; i++) {
				ret += "" + conds.get(i).getCondition().toSource() + " || ";
			}
			ret += conds.get(i).getCondition().toSource();
			ret += ")";
		} else {
			System.err.println("Unknown class@Edge.getElseCondLabel: " + astNode.getClass());
		}
		return ret;
	}
	
	public boolean hasCond() {
		return this.hasGuard;
	}
	public String getCondLabel() {
		return this.guardLabel;
	}
	
	///// action
	public boolean hasAction() {
		return false; // TBD
	}
	public String getActionLabel() {
		return ""; // TBD
	}
	
	/// for abstraction
	public void setIsAbstracted(boolean isAbstracted) {
		this.isAbsracted = isAbstracted;
	}
	public boolean getIsAbstracted() {
		return this.isAbsracted;
	}
	
	/// toString
	public String toStringDot() {
		String ret = "";
		
		if(this.hasEvent()) {
			ret += this.getEventLabel() + " ";
		}
		if(this.hasCond()) {
			ret += "[" + this.getCondLabel() + "] ";
		}
		if(this.hasAction()) {
			ret += "/" + this.getActionLabel();
		}
		
		return ret;
	}
	
	
	
	
	/// prev
	public String getEvent() {
		return this.eventLabel;
	}
	public void setEvent(String event) {
		this.eventLabel = event;
	}
	public void addGuard(String guard) {
		this.guards.add(guard);
	}
	public void addAction(String action) {
		this.actions.add(action);
	}
}
