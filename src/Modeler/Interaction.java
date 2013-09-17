package Modeler;

import Graph.Edge;
import Graph.Node;
import Parser.CSSParser;
import Parser.DOM;
import Rule.RuleList;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mozilla.javascript.ast.AstNode;

import java.util.List;

public class Interaction {

	///// constructor
	private Edge orgEdge = null;
	
	private Element elm = null;
	private Attribute attr = null;
	public Interaction(Edge originalEdge, Element elm, Attribute attr) {
		this.orgEdge = originalEdge;
		this.elm = elm;
		this.attr = attr;
	}

	private AstNode eventAstNode = null;
	private AstNode targetAstNode = null;
	private boolean isEnable = true;
	public Interaction(Edge originalEdge, AstNode eventAstNode, AstNode targetAstNode) {
		this.orgEdge = originalEdge;
		this.eventAstNode = eventAstNode;
		this.targetAstNode = targetAstNode;
		
		this.isEnable = true; 
	}
	
	/////
	public boolean fromHTML() {
		if(this.attr != null) {
			return true;
		}
		return false;
	}
	public boolean fromJS() {
		if(this.eventAstNode != null) {
			return true;
		}
		return false;
	}
	
	public String getTargetLabel() {
		if(this.fromHTML()) {
			return this.elm.tagName();
		} else if(this.fromJS()) {
			return this.targetAstNode.toSource();
		}
		return "";
	}
	
	/////
	public boolean isPotential(RuleList rules) {
		/*
		for(Potential potential : rules.getPotentials()) {
			if((potential.getFunc().equals(this.getTargetLabel())) ||
					((this.getTargetLabel()).contains(potential.getFunc()))) {
				return true;
			}
		}
		*/
		
		// hard cording
		String[] ps = {"window", "alert", "Ajax", "setTimeout", "jQuery"};
		for(String p : ps) {
			if((p.equals(this.getTargetLabel())) ||
					((this.getTargetLabel()).contains(p))) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean isDisabled4AstNode(Node node, DOM dom, Modeler modeler, RuleList rules) {
		Node tarNode = modeler.getGraph().getNodeByAstNode(this.targetAstNode);
		Elements tarElms = dom.findElementsByNode(tarNode);
		
		if(this.isPotential(rules)) {
			modeler.getGraph().getNodeById(this.orgEdge.getFrom());
			
			
			if(this.orgEdge.getFrom() == node.getId()) {
				return false; // enable
			} else {
				return true; // disable
			}
		}
		
		// hard coding
		for(Edge edge : modeler.getGraph().getEdgesFrom(node)) {
			if(edge.hasEvent() && "User Click".equals(edge.getEventLabel())) {
				return true;
			}
		}

		// hard coding
		if(this.targetAstNode.toSource().equals("document.getElementById(\"submit\")")) {
			tarElms = new Elements();
			tarElms.add(dom.getDoc().getElementById("submit"));
		}
		

		boolean isDisabled = true; // If all elements are disabled, return true;
		for(Element elm : tarElms) {

			/*
			List<CSSCode> cssCodes = dom.getCssCodeByElement(elm);
			for(CSSCode cssCode : cssCodes) {
				elm.attr("style", cssCode.toSource());
			}
			*/
			
			List<CSSParser> cssParsers = dom.getCssParsersByElement(elm);
			for(CSSParser cssParser : cssParsers) {
				if(cssParser.isControl() && cssParser.isDisabled()) {
					isDisabled = true;
				}
			}
			
			
			String dis = elm.attr("disabled");
			if(!"true".equals(dis)) {
				isDisabled = false;
			}
			///
		}
		
		
		return isDisabled;
	}
	
	private boolean isDisabled4ElmAttr(Node node, DOM dom, Modeler modeler, RuleList rules) {
		
		Element tarElm = dom.getCurElement(this.elm);
		//Element tarElm = dom.getOriginalElement(this.elm);
		
		// hard coding
		if(tarElm == null) {
			System.err.println("cannot find target element@Interaction.isDisabled4ElmAttr:" + this.elm.toString());
			String id = this.elm.attr("id");
			tarElm = dom.getDoc().getElementById(id);
		}
		
		boolean isDisabled = false;
		/*
		List<CSSCode> cssCodes = dom.getCssCodeByElement(elm);
		for(CSSCode cssCode : cssCodes) {
			System.out.println("\t\tCSSCode, to be implemented@Interaction4ElmAttr: " + cssCode.toSource());
		}
		*/
		List<CSSParser> cssParsers = dom.getCssParsersByElement(elm);
		for(CSSParser cssParser : cssParsers) {
			if(cssParser.isControl() && cssParser.isDisabled()) {
				isDisabled = true;
			}
		}
		
		String dis = tarElm.attr("disabled");
		if(!"true".equals(dis)) {
			return false;
		}
		
		
		return isDisabled;
	}
	
	public boolean isDisabled(Node node, DOM dom, Modeler modeler, RuleList rules) {
		
		
		if(this.attr != null) {
			return isDisabled4ElmAttr(node, dom, modeler, rules);
		} else if(this.eventAstNode != null) {
			return this.isDisabled4AstNode(node, dom, modeler, rules);
		} else {
			System.err.println("Event objects are null@Interaction.isDisabled: " + this.toString());
		}
		
		return false;
	}
	
	/// utilities
	public boolean equals(Interaction interaction) {
		if(this.attr != null) {
			return this.attr.equals(interaction.getAttr());
		}
		if(this.eventAstNode != null) {
			return this.eventAstNode.equals(interaction.getEventAstNode());
		}
		return false;
	}
	public boolean hasEventAttr() {
		if(this.attr != null) {
			return true;
		}
		return false;
	}
	public Element getElm() {
		return this.elm;
	}
	public Attribute getAttr() {
		return this.attr;
	}
	public boolean hasEventAstNode() {
		if(this.eventAstNode != null) {
			return true;
		}
		return false;
	}
	public AstNode getEventAstNode() {
		return this.eventAstNode;
	}
	public AstNode getTargetAstNode() {
		return this.targetAstNode;
	}
	public Object getEventObject() {
		if(this.hasEventAttr()) {
			return this.attr;
		}
		if(this.hasEventAstNode()) {
			return this.eventAstNode;
		}
		System.err.println("No event object@Interaction.getEventObject: ");
		return null;
	}
	
	public String getEventLabel() {
		if(this.attr != null) {
			return this.attr.getKey();
		}
		if(this.eventAstNode != null) {
			return this.eventAstNode.toSource();
		}
		return "";
	}
	
	public Edge getOriginalEdge() {
		return this.orgEdge;
	}
	public boolean getIsEnable() {
		return this.isEnable;
	}
	
	/// toStrings
	public String toString() {
		if(this.attr != null) {
			return "[event=" + this.attr.getKey() + ", target(element)=" + this.elm.toString() + "]";
		} else if(this.eventAstNode != null) {
			return "[event=" + this.eventAstNode.toSource() + ", target(AstNode)=" + this.targetAstNode.toSource() + "]";
		}
		
		return "Invalid interaction";
	}
	
}
