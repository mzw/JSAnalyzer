package Parser;

import Analyzer.Util;
import Graph.Node;
import Modeler.Modeler;
import Rule.Control;
import Rule.Manipulate;
import Rule.Rule;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mozilla.javascript.ast.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DOM {

	private Document doc = null;
	private List<CSSParser> cssParsers = null;

	private HashMap<String, Elements> hash_VarName_Elements = null;
	private HashMap<Node, Elements> hash_Node_Elements = null;
	private HashMap<Element, Element> hash_Element_orgElement = null;
	
	///// constructor
	public DOM(Document doc, List<CSSParser> cssParsers) {
		this.doc = doc;
		
		this.cssParsers = cssParsers;
		
		this.hash_VarName_Elements = new HashMap<String, Elements>();
		this.hash_Node_Elements = new HashMap<Node, Elements>();
		
		this.hash_Element_orgElement = new HashMap<Element, Element>();
		for(Element elm : this.doc.getAllElements()) {
			this.hash_Element_orgElement.put(elm, elm);
		}
		
		this.manipulateStyle();
	}
	
	private void manipulateStyle() {

		for(CSSParser cp : this.cssParsers) {
			CSSCode cc = cp.getCssCode();
			if(cc.getTarget() != null) {
				this.doc.attr("style", cc.toSource());
			} else {
				System.err.println("CSS code target is null@DOM.manipulateStyle: " + cc.getUrl());
			}
		}
		
	}
	
	///// for cloning
	private DOM(
			Document doc,
			List<CSSParser> cssParsers,
			HashMap<String, Elements> hash_VarName_Elements,
			HashMap<Node, Elements> hash_Node_Elements,
			HashMap<Element, Element> hash_Element_orgElement) {
		
		this.cssParsers = cssParsers;
		
		this.setHashInfo(hash_VarName_Elements, hash_Node_Elements, hash_Element_orgElement);
		
		this.doc = doc.clone();
		if(doc.getAllElements().size() != this.doc.getAllElements().size()) {
			System.err.println("Invalid DOM cloning?@DOM.DOM: " + doc.getAllElements().size() + ", " + this.doc.getAllElements().size());
		} else {
			// save cloning information
			for(int i = 0; i < doc.getAllElements().size(); i++) {
				Element clnElm = this.doc.getAllElements().get(i);
				Element orgElm = doc.getAllElements().get(i);
				
				Element org = this.hash_Element_orgElement.get(orgElm);
				if(org == null) {
					this.hash_Element_orgElement.put(clnElm, orgElm);
				} else {
					this.hash_Element_orgElement.put(clnElm, org);
				}
			}
		}
	}
	public DOM clone() {
		DOM ret = new DOM(this.doc,
				this.cssParsers,
				this.hash_VarName_Elements,
				this.hash_Node_Elements,
				this.hash_Element_orgElement);
		return ret;
	}
	private void setHashInfo(
			HashMap<String, Elements> hash_VarName_Elements,
			HashMap<Node, Elements> hash_Node_Elements,
			HashMap<Element, Element> hash_Element_orgElement
			) {

		this.hash_Element_orgElement = new HashMap<Element, Element>();
		this.hash_VarName_Elements = new HashMap<String, Elements>();
		this.hash_Node_Elements = new HashMap<Node, Elements>();
		
		// shared information
		for(Iterator<Element> it = hash_Element_orgElement.keySet().iterator(); it.hasNext();) {
			Element key = it.next();
			Element value = hash_Element_orgElement.get(key);
			
			this.hash_Element_orgElement.put(key, value);
		}
		
		// clone because manipulates
		for(Iterator<String> it = hash_VarName_Elements.keySet().iterator(); it.hasNext();) {
			String key = it.next();
			Elements elms = hash_VarName_Elements.get(key);
			
			Elements _elms = new Elements();
			for(Element elm : elms) {
				Element _elm = elm.clone();
				
				_elms.add(_elm);
				
				Element org = this.hash_Element_orgElement.get(elm);
				if(org == null) {
					this.hash_Element_orgElement.put(_elm, elm);
				} else {
					this.hash_Element_orgElement.put(_elm, org);
				}
			}
			
			this.hash_VarName_Elements.put(key, _elms);
		}
		
		for(Iterator<Node> it = hash_Node_Elements.keySet().iterator(); it.hasNext();) {
			Node key = it.next();
			Elements elms = hash_Node_Elements.get(key);
			
			Elements _elms = new Elements();
			for(Element elm : elms) {
				Element _elm = elm.clone();
				
				_elms.add(_elm);
				
				Element org = this.hash_Element_orgElement.get(elm);
				if(org == null) {
					this.hash_Element_orgElement.put(_elm, elm);
				} else {
					this.hash_Element_orgElement.put(_elm, org);
				}
			}
			this.hash_Node_Elements.put(key, _elms);
		}
	}
	
	///// enabling/disabling interactions
	public List<CSSCode> getCssCodeByElement(Element elm) {
		List<CSSCode> ret = new LinkedList<CSSCode>();
		if(elm == null) {
			return ret;
		}
		Element orgElm = this.getOriginalElement(elm);
		if(orgElm == null) {
			return ret;
		}
		
		for(CSSParser cssParser : this.cssParsers) {
			CSSCode cssCode = cssParser.getCssCode();
			if(cssCode.getTarget() != null) {
				Element _elm = cssCode.getTarget();
				if(orgElm.equals(_elm)) {
					ret.add(cssCode);
				}
			}
		}
		return ret;
	}

	public List<CSSParser> getCssParsersByElement(Element elm) {
		List<CSSParser> ret = new LinkedList<CSSParser>();
		if(elm == null) {
			return ret;
		}
		
		for(CSSParser cssParser : this.cssParsers) {
			CSSCode cssCode = cssParser.getCssCode();
			if(cssCode.getTarget() != null) {
				Element _elm = cssCode.getTarget();
				if(elm.equals(_elm)) {
					ret.add(cssParser);
				}
			}
		}
		return ret;
	}
	
	/////
	public Element getOriginalElement(Element elm) {
		return this.hash_Element_orgElement.get(elm);
	}
	public Element getCurElement(Element orgElm) {
		for(Iterator<Element> it = this.hash_Element_orgElement.keySet().iterator(); it.hasNext();) {
			Element elm = it.next();
			Element _orgElm = this.hash_Element_orgElement.get(elm);
			if(_orgElm != null && _orgElm.equals(orgElm)) {
				return elm;
			}
		}
		
		/// hard coding
		String id = orgElm.attr("id");
		Element elm = this.doc.getElementById(id);
		if(elm != null) {
			return elm;
		}
		
		return null;
	}
	
	////////// controls and manipulations
	public Elements findElementsByAttr(String attr, String val) {
		Elements ret = new Elements();

		for(Iterator<String> _it = this.hash_VarName_Elements.keySet().iterator(); _it.hasNext();) {
			Elements _elms = this.hash_VarName_Elements.get(_it.next());
			if(_elms == null) {
				continue;
			}
			for(Element _elm : _elms) {
				if(_elm.attr(attr).equals(val)) {
					ret.add(_elm);
				}
			}
		}

		for(Iterator<Node> _it = this.hash_Node_Elements.keySet().iterator(); _it.hasNext();) {
			Elements _elms = this.hash_VarName_Elements.get(_it.next());
			if(_elms == null) {
				continue;
			}
			for(Element _elm : _elms) {
				if(_elm.attr(attr).equals(val)) {
					ret.add(_elm);
				}
			}
		}
		
		return ret;
	}
	
	public Elements findElementsByNode(Node node) {
		// hard coding for Ultimate preloader
		if(node == null) {
			return null;
		}
		
		Elements ret = this.hash_VarName_Elements.get(node.toSource());
		if(ret == null) {
			ret = this.hash_Node_Elements.get(node);
		}
		if(ret == null) {
			ret = new Elements();
		}
		return ret;
	}

	/// get node
	private Node getVarNode(Node node, Modeler modeler) {
		AstNode astNode = node.getAstNode();
		
		if(astNode == null) {
			return null;
		}
		
		if(astNode instanceof VariableInitializer) {
			VariableInitializer _astNode = (VariableInitializer)astNode;
			return modeler.getGraph().getNodeByAstNode(_astNode.getTarget());
		}
		
		else if(astNode instanceof PropertyGet) {
			// document.getElementById("ftext").value
			return this.getVarNode(node.getParent(), modeler);
		}

		else if(astNode instanceof FunctionCall) {
			return this.getVarNode(node.getParent(), modeler);
		} else if(astNode instanceof ExpressionStatement) {
			return this.getVarNode(node.getParent(), modeler);
		} else if(astNode instanceof Block) {
			// { $("#id"); }
			return null;
		}
		
		else if(astNode instanceof Assignment) {
			Assignment _astNode = (Assignment)astNode;
			return modeler.getGraph().getNodeByAstNode(_astNode.getLeft());
		}
		
		
		else {
			System.err.println("Unknown class@DOM.getVarNode: " + astNode.getClass());
			System.err.println("\t" + node.toSource());
		}
		return null;
	}
	
	private Node getPropTargetNode(Node node, Modeler modeler) {
		AstNode astNode = node.getAstNode();
		if(astNode instanceof PropertyGet) {
			PropertyGet _astNode = (PropertyGet)astNode;
			return modeler.getGraph().getNodeByAstNode(_astNode.getTarget());
		} else if(astNode instanceof Name) {
			Node _node = modeler.getGraph().getNodeByAstNode(astNode);
			return this.getPropTargetNode(_node.getParent(), modeler);
		}
		
		else {
			/// hard coding for ultimate preload
			/// $(id).appendTo(parent).css({>> rules << is astNode here});
			
			System.out.println(astNode.getParent().toSource());
			System.out.println(astNode.getParent().getParent().toSource());

			System.out.println(astNode.getParent().getParent().getClass().toString());
			System.out.println(((FunctionCall)astNode.getParent().getParent()).getTarget().toSource());
			
			System.err.println("Unknown class@DOM.getPropTargetNode: " + astNode.getClass());
			System.err.println("\t" + astNode.toSource());
		}
		return null;
	}
	
	private Node getRetNode(Node node, Modeler modeler) {
		AstNode astNode = node.getAstNode();
		if(astNode instanceof Assignment) {
			return modeler.getGraph().getNodeByAstNode(((Assignment)astNode).getLeft());
		} else if(astNode instanceof PropertyGet) {
			return this.getRetNode(node.getParent(), modeler);
		} else if(astNode instanceof FunctionCall) { // execute
			return modeler.getGraph().getNodeByAstNode(astNode);
		} else if(astNode instanceof Name) {
			return this.getRetNode(node.getParent(), modeler);
		}
		
		// jQuery?
		else if(astNode instanceof FunctionNode) {
			return null;
		}
		
		else {
			System.err.println("Unknown class@DOM.getRetNode: " + astNode.getClass());
		}
		return null;
	}
	
	public Node getNodeByRule(Node node, String rule, Modeler modeler) {
		Node ret = null;
		if(Rule.containArg(rule)) {
			int argNum = Rule.getArgNum(rule);
			ret = modeler.getArgNode(node.getParent(), argNum);
		} else if("ret".equals(rule)) {
			ret = this.getRetNode(node.getParent(), modeler);
		} else if("propTarget".equals(rule)) {
			ret = this.getPropTargetNode(node.getParent(), modeler);
		}
		
		else {
			System.err.println("Unknown rule type@DOM.getNodeByRule: " + rule + ", " + node.toSource());
		}
		
		return ret;
	}
	
	/// manipulate
	private void manipulateSet(Node node, Modeler modeler, Manipulate rule) {

		Node byNode = this.getNodeByRule(node, rule.getBy(), modeler);
		Node valNode = this.getNodeByRule(node, rule.getValue(), modeler);
		Node tarNode = this.getNodeByRule(node, rule.getTarget(), modeler);
		
		Elements tarElms = this.findElementsByNode(tarNode);
		
		for(Element tarElm : tarElms) {
			tarElm.attr(Util.removeQuote(byNode.toSource()), Util.removeQuote(valNode.toSource()));
		}
	}
	
	private void manipulateInsert(Node node, Modeler modeler, Manipulate rule) {

		Node valNode = this.getNodeByRule(node, rule.getValue(), modeler);
		Node tarNode = this.getNodeByRule(node, rule.getTarget(), modeler);
		
		Elements valElms = this.findElementsByNode(valNode);
		Elements tarElms = this.findElementsByNode(tarNode);
		
		if("none".equals(rule.getBy())) {
			for(int i = 0; i < tarElms.size(); i++) {
				Element tarElm = valElms.get(i);
				for(int j = 0; j < valElms.size(); j++) {
					Element valElm = valElms.get(j);
					
					tarElm.append(valElm.html());
				}
			}
		}
		
		else {
			System.err.println("Unknown rule by@DOM.manipulate.insert: " + rule.getBy());
		}
	}
	
	private void manipulateCreate(Node node, Modeler modeler, Manipulate rule) {
		Node valNode = this.getNodeByRule(node, rule.getValue(), modeler);
		Elements elms = new Elements();

		if("name".equals(rule.getBy())) {
			
			/// hard coding for storys.jp
			if("s".equals(valNode.toSource())) {
				return;
			}
			
			Element elm = this.doc.createElement(Util.removeQuote(valNode.toSource()));
			elms.add(elm);
		} else if("none".equals(rule.getBy())) {
			// NOP
			Element elm = this.doc.createElement("text");
			elms.add(elm);
		} else {
			System.err.println("Unknown rule by@DOM.manipulate.create.by: " + rule.getBy());
		}
		
		// document.getElementById("progress")
		Node tarNode = this.getNodeByRule(node, rule.getTarget(), modeler);
		if(tarNode != null) {
			this.hash_Node_Elements.put(tarNode, elms);
		}
		
		// var progressField = document.getElementById("progress");
		Node varNode = this.getVarNode(tarNode.getParent(), modeler);
		if(varNode != null) {
			this.hash_VarName_Elements.put(varNode.toSource(), elms);
			
			Node scopeNode = this.getScopeNode(varNode, modeler);
			List<Node> corrNodes = this.getCorrNameNodes(scopeNode, varNode.toSource(), modeler);
			for(Node corrNode : corrNodes) {
				this.hash_Node_Elements.put(corrNode, elms);
			}
			
		}
	}
	
	private Node getScopeNode(Node varNode, Modeler modeler) {
		AstNode astNode = varNode.getAstNode();
		if(astNode instanceof Scope) {
			return modeler.getGraph().getNodeByAstNode(astNode);
		} else if(astNode instanceof AstRoot) {
			System.err.println("Reach AstRoot without Scope@DOM.getScopeNode: ");
			return null;
		} else {
			return this.getScopeNode(varNode.getParent(), modeler);
		}
	}
	private List<Node> getCorrNameNodes(Node node, String name, Modeler modeler) {
		List<Node> ret = new LinkedList<Node>();
		AstNode astNode = node.getAstNode();
		if(astNode instanceof Name) {
			if(name.equals(astNode.toSource())) {
				Node corrNode = modeler.getGraph().getNodeByAstNode(astNode);
				ret.add(corrNode);
			}
		}
		for(Node child : node.getChildren()) {
			List<Node> corrNodes = this.getCorrNameNodes(child, name, modeler);
			ret.addAll(corrNodes);
		}
		return ret;
	}
	
	private void manipulateGet(Node node, Modeler modeler, Manipulate rule) {		
		Node valNode = this.getNodeByRule(node, rule.getValue(), modeler);
		
		String val = "";
		
		if(valNode == null) {
			/// for measuring time
			//System.err.println("Cannot solve value node@DOM.manipulateGet:" + node.toSource()
			//		+ " at (lineno, absolute pos)=(" + node.getAstNode().getLineno() + ", " + node.getAstNode().getAbsolutePosition() + ") "
			//		+ "in File: TBD");
			return;
		} else if(valNode.getAstNode() instanceof InfixExpression) { // '<span id="' + statusElement + '" class="confirm">' + data.message + '</span>'
			
			val = valNode.toSource();

			/// for measuring time
			///System.err.println("Data flow element? limitation@DOM.manipulateGet: " + valNode.getAstNode().getClass() + ", " + valNode.toSource());
			return;
			
		} else if(valNode.getAstNode() instanceof PropertyGet) {

			/// for measuring time
			///System.err.println("Data flow element? limitation@DOM.manipulateGet: " + valNode.getAstNode().getClass() + ", " + valNode.toSource());
			return;
			
		}
		/// hard coding
		else if("vId".equals(valNode.toSource())) {
			return;
		} else if("id".equals(valNode.toSource())) {
			return;
		}
		
		else {
			val = valNode.toSource();
		}
		
		Elements elms = new Elements();
		
		if("id".equals(rule.getBy())) {
			Element elm = this.doc.getElementById(Util.removeQuote(val));
			// if not in document, try to find from hashs
			if(elm == null) {
				elms = this.findElementsByAttr("id", Util.removeQuote(val));
			} else {
				elms.add(elm);
			}
		} else if("class".equals(rule.getBy())) {
			elms = this.doc.getElementsByClass(Util.removeQuote(val));
		} else if("name".equals(rule.getBy())) {
			elms = this.doc.getElementsByTag(Util.removeQuote(val));
		} else if("css_query".equals(rule.getBy())) {
			String valNodeStr = Util.removeQuote(val);
			if(!"".equals(valNodeStr)) { // function($) {...
				
				/// hard coding for jQuery
				if(valNodeStr.charAt(0) == '<') { // new element creation: $("<div><p>Hello</p></div>").appendTo("body");
					Element elm = this.doc.html(valNodeStr);
					elms.add(elm);
				} else {
					elms = this.doc.select(valNodeStr);
				}
				
			}
		}
		
		else {
			System.err.println("Unknown rule by @DOM.manipulate.get.by: " + rule.getBy());
		}
		
		
		Node tarNode = this.getNodeByRule(node, rule.getTarget(), modeler);
		
		// document.getElementById("progress")
		if(tarNode != null) {
			this.hash_Node_Elements.put(tarNode, elms);
		}
			
		// var progressField = document.getElementById("progress");
		Node varNode = this.getVarNode(tarNode.getParent(), modeler);
		if(varNode != null) {
			this.hash_VarName_Elements.put(varNode.toSource(), elms);
			
			// >> progressField << in current scope
			Node scopeNode = this.getScopeNode(varNode, modeler);
			List<Node> corrNodes = this.getCorrNameNodes(scopeNode, varNode.toSource(), modeler);
			for(Node corrNode : corrNodes) {
				this.hash_Node_Elements.put(corrNode, elms);
			}
		}
	}
	
	private void manipulate(Node node, Modeler modeler, Manipulate rule) {
		// semantic -> by, value, target
		if("get".equals(rule.getSemantic())) {
			this.manipulateGet(node, modeler, rule);
		} else if("create".equals(rule.getSemantic())) {
			this.manipulateCreate(node, modeler, rule);
		} else if("insert".equals(rule.getSemantic())) {
			this.manipulateInsert(node, modeler, rule);
		} else if("set".equals(rule.getSemantic())) {
			this.manipulateSet(node, modeler, rule);
		}
		else {
			System.err.println("Unknown rule semantic@DOM.manipulate.semantic: " + rule.getSemantic());
		}
	}
	
	/// control manipulation
	private void manipulateControl(Node node, Modeler modeler, Control rule) {

		Node tarNode = this.getNodeByRule(node, "propTarget", modeler);
		Elements tarElms = this.findElementsByNode(tarNode);
		
		Node valNode = this.getValueNode(tarNode, modeler);
		String value = valNode.toSource();
		
		for(Element tarElm : tarElms) {
			tarElm.attr(rule.getAttr(), value);
		}
	}
	public Node getValueNode(Node node, Modeler modeler) {
		AstNode astNode = node.getAstNode();
		if(astNode instanceof Assignment) {
			Assignment _astNode = (Assignment)astNode;
			return modeler.getGraph().getNodeByAstNode(_astNode.getRight());
		}
		// var obj = document.getElementById("fsubmit");
		// obj.setAttribute("disabled", true);
		else if(astNode instanceof PropertyGet) {
			return this.getValueNode(node.getParent(), modeler);
		} else if(astNode instanceof Name) {
			return this.getValueNode(node.getParent(), modeler);
		}
		
		// document.getElementById("fsubmit").disabled = true;
		 else if(astNode instanceof FunctionCall) {
			return this.getValueNode(node.getParent(), modeler);
		}

		else {
			System.err.println("Unknown class@DOM.getValueNode: " + astNode.getClass());
			System.err.println("\t" + astNode.toSource());
		}
		return null;
		
	}
	
	/// main
	public void manipulate(Node node, Modeler modeler) {
		List<Node> _nodes = new LinkedList<Node>();
		_nodes.add(node);
		_nodes.addAll(node.getAbstractedNodes());
		
		for(Node _node : _nodes) {
			if(_node.isControl()) {
				this.manipulateControl(_node, modeler, _node.getControlRule());
			}
			if(_node.isManipulate()) {
				this.manipulate(_node, modeler, _node.getManipulateRule());
			}
			
			// hard coding
			if(_node.getAstNode() instanceof Assignment) {
				Assignment _astNode = (Assignment)_node.getAstNode();
				if(_astNode.getLeft() instanceof PropertyGet) { // target.property = value;
					PropertyGet __astNode = (PropertyGet)_astNode.getLeft();
					Node propNode = modeler.getGraph().getNodeByAstNode(__astNode.getProperty());
					if(!propNode.isControl() && !propNode.isManipulate() // do not evaluate above
							&& !propNode.isTrigger()) { // already evaluated

						Node tarNode = this.getPropTargetNode(propNode, modeler);
						Node valNode = this.getValueNode(propNode, modeler);
						
						Elements tarElms = this.findElementsByNode(tarNode);
						for(Element elm : tarElms) {
							elm.attr(propNode.toSource(), Util.removeQuote(valNode.toSource()));
						}
					}
					
				}
			}
		}
	}
	
	///// utilities
	public List<CSSParser> getCssParsers() {
		return this.cssParsers;
	}
	
	public Document getDoc() {
		return this.doc;
	}
	
	public void showHashInfo() {
		for(Iterator<String> it = this.hash_VarName_Elements.keySet().iterator(); it.hasNext();) {
			String var = it.next();
			Elements elms = this.hash_VarName_Elements.get(var);
			for(Element elm : elms) {
				System.out.println("Var: " + var + ", " + elm);
			}
		}
		for(Iterator<Node> it = this.hash_Node_Elements.keySet().iterator(); it.hasNext();) {
			Node node = it.next();
			Elements elms = this.hash_Node_Elements.get(node);
			for(Element elm : elms) {
				System.out.println("Node: " + node.toSource() + ", " + elm);
			}
		}
	}

}
