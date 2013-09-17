package Graph;

import Analyzer.Util;
import Rule.*;

import java.util.LinkedList;
import java.util.List;

public class Node extends Element {
	
	protected org.mozilla.javascript.Node node = null;
	
	protected boolean isTrigger = false;
	protected boolean isPotential = false;
	protected boolean isControl = false;
	protected boolean isManipulate = false;
	
	protected Trigger trigger = null;
	protected Potential potential = null;
	protected Control control = null;
	protected Manipulate manipulate = null;
	
	protected Node parent = null;
	protected List<Node> children = null;
	
	protected org.mozilla.javascript.Node specialNode = null;
	
	protected boolean isExit = false;
	
	public Node(int id, org.mozilla.javascript.Node node) {
		super(id);
		
		this.node = node;
		
		this.isTrigger = false;
		this.isPotential = false;
		this.isControl = false;
		this.isManipulate = false;
		
		this.trigger = null;
		this.potential = null;
		this.control = null;
		this.manipulate = null;
		
		this.parent = null;
		this.children = new LinkedList<Node>();
		
		this.specialNode = null;
		
		this.isExit = false;
		
		this.init4dummy();
		this.init4dfs();
		this.init4abst();
	}
	
	public boolean isExit() {
		return this.isExit;
	}
	public void setIsExit(boolean isExit) {
		this.isExit = isExit;
	}
	
	/////
	protected RuleList rules = null;
	public void applyRule(RuleList rules) {
		this.rules = rules;
		
		if(this.node == null) {
			return;
		}
		org.mozilla.javascript.ast.AstNode _node = (org.mozilla.javascript.ast.AstNode)node;
		
		this.trigger = this.rules.getTrigger(_node.toSource());
		if(this.trigger != null) {
			this.isTrigger = true;
			this.specialNode = this.node;
			return;
		}
		this.potential = this.rules.getPotential(_node.toSource());
		if(this.potential != null) {
			this.isPotential = true;
			this.specialNode = this.node;
			return;
		}
		this.control = this.rules.getControl(_node.toSource());
		if(this.control != null) {
			this.isControl = true;
			this.specialNode = this.node;
			return;
		}
		this.manipulate = this.rules.getManipulate(_node.toSource());
		if(this.manipulate != null) {
			this.isManipulate = true;
			this.specialNode = this.node;
			return;
		}
	}
	
	public org.mozilla.javascript.Node getSpecialNode() {
		return this.specialNode;
	}
	
	public boolean isTrigger() {
		return this.isTrigger;
	}
	public boolean isPotential() {
		return this.isPotential;
	}
	public Potential getPotential() {
		return this.potential;
	}
	public boolean isControl() {
		return this.isControl;
	}
	public org.mozilla.javascript.ast.AstNode getControlAstNode() {
		return (org.mozilla.javascript.ast.AstNode)this.specialNode;
	}
	public Control getControlRule() {
		return this.control;
	}
	public boolean isManipulate() {
		return this.isManipulate;
	}
	public Manipulate getManipulateRule() {
		return this.manipulate;
	}
	public boolean isSpecial() {
		return this.isTrigger || this.isPotential || this.isControl || this.isManipulate;
	}
	
	public boolean isAbstractedControl() {
		if(this.isControl()) {
			return true;
		}
		for(Node _node : this.getAllAbstractedNodes()) {
			if(_node.isControl()) {
				return true;
			}
		}
		
		return false;
	}
	
	/// for abstraction
	private List<Node> abstractedNodes = null;
	private List<Edge> abstractedEdges = null;
	public void init4abst() {
		this.abstractedNodes = new LinkedList<Node>();
		this.abstractedEdges = new LinkedList<Edge>();
		
	}
	public List<Node> getAbstractedNodes() {
		return this.abstractedNodes;
	}
	public List<Edge> getAbstractedEdges() {
		return this.abstractedEdges;
	}
	public void abstractNode(Node node, Edge edge) {
		if(!this.abstractedNodes.contains(node)) {
			this.abstractedNodes.add(node);
		}
		if(!this.abstractedEdges.contains(edge)) {
			this.abstractedEdges.add(edge);
		}
		for(Node _node : node.getAbstractedNodes()) {
			if(!this.abstractedNodes.contains(_node)) {
				this.abstractedNodes.add(_node);
			}
			for(Edge _edge : _node.getAbstractedEdges()) {
				if(!this.abstractedEdges.contains(_edge)) {
					this.abstractedEdges.add(_edge);
				}
			}
		}
	}
	
	public List<Node> getAllAbstractedNodes() {
		List<Node> ret = new LinkedList<Node>();
		for(Node abstNode : this.getAbstractedNodes()) {
			if(!ret.contains(abstNode)) {
				ret.add(abstNode);
			}
			for(Node _abstNode : abstNode.getAllAbstractedNodes()) {
				if(!ret.contains(_abstNode)) {
					ret.add(_abstNode);
				}
			}
		}
		return ret;
	}
	
	/// for searching
	private boolean visit4dfs = false;
	private int order4dfs = -1;
	public void init4dfs() {
		this.visit4dfs = false;
		this.order4dfs = -1;
	}
	public void setVisit4dfs(boolean visit) {
		this.visit4dfs = visit;
	}
	public boolean isVisit4dfs() {
		return this.visit4dfs;
	}
	public void addChild(Node child) {
		if(!this.children.contains(child)) {
			this.children.add(child);
		}
	}
	public List<Node> getChildren() {
		return this.children;
	}
	public void setParent(Node parent, boolean force) {
		if(force) {
			this.parent = parent;
		} else {
			this.setParent(parent);
		}
	}
	public void setParent(Node parent) {
		if(this.parent != null && parent != null && this.parent != parent) {
			System.err.println("Error@Node.setParent: " + this.parent.getNodeLabel() + ", " + this.getNodeLabel());
		}
		this.parent = parent;
	}
	public Node getParent() {
		return this.parent;
	}
	public Node findUnvisitedChild() {
		for(Node child : this.children) {
			if(!child.isVisit4dfs()) {
				return child;
			}
		}
		return null;
	}
	public void setOrder4dfs(int order) {
		this.order4dfs = order;
	}
	
	//// Dummy
	protected boolean isDummy = false;
	public void init4dummy() {
		this.isDummy = false;
	}
	public void setIdDummy(boolean isDummy) {
		this.isDummy = isDummy;
	}
	public boolean getIsDummy() {
		return this.isDummy;
	}
	
	// hard coding
	public boolean isExecutableNode() {
		if(this.node instanceof org.mozilla.javascript.ast.ExpressionStatement) {
			return true;
		}
		return false;
	}
	
	// toStrings
	public String getNodeType() {
		if(this.isDummy) {
			return "DummyNode";
		} else if(this.node == null) {
			/// debug later
			System.err.println("Unset Node@Node.getNodeType: " + this.getId());
			return "UnsetNode";
		}
		return org.mozilla.javascript.Token.typeToName(this.node.getType());
	}
	
	public String getNodeLabel() {
		String ret = "";
		
		ret += this.getNodeType();
		
		if(this.order4dfs != -1) {
			ret += ": " + this.order4dfs;
		}
		
		return ret;
	}
	
	///
	public String getName() {
		if(this.isDummy) {
			return "init";
		}
		if(this.getAstNode() instanceof org.mozilla.javascript.ast.FunctionNode) {
			org.mozilla.javascript.ast.FunctionNode _astNode = (org.mozilla.javascript.ast.FunctionNode)this.getAstNode();
			if(_astNode.getFunctionName() != null) {
				return _astNode.getFunctionName().toSource();
			} else {
				return "Nameless_" + this.getId();
			}
		} else if(this.getAstNode() instanceof org.mozilla.javascript.ast.FunctionCall) {
			org.mozilla.javascript.ast.FunctionCall _astNode = (org.mozilla.javascript.ast.FunctionCall)this.getAstNode();

			// hard coding
			String[] potentials = { "alert" };
			for(String potential : potentials) {
				if(potential.equals(_astNode.getTarget().toSource())) {
					return _astNode.getTarget().toSource() + "_" + this.getId();
				}
			}
		}
		
		List<Node> abstNodes = this.getAbstractedNodes();
		for(Node abstNode : abstNodes) {
			if(abstNode.getAstNode() instanceof org.mozilla.javascript.ast.FunctionNode) {
				org.mozilla.javascript.ast.FunctionNode _astNode = (org.mozilla.javascript.ast.FunctionNode)abstNode.getAstNode();
				if(_astNode.getFunctionName() != null) {
					return _astNode.getFunctionName().toSource();
				} else {
					return "Nameless_" + abstNode.getId();
				}
			} else if(abstNode.getAstNode() instanceof org.mozilla.javascript.ast.FunctionCall) {
				org.mozilla.javascript.ast.FunctionCall _astNode = (org.mozilla.javascript.ast.FunctionCall)abstNode.getAstNode();
				
				// hard coding
				String[] potentials = { "alert" };
				for(String potential : potentials) {
					if(potential.equals(_astNode.getTarget().toSource())) {
						return _astNode.getTarget().toSource() + "_" + this.getId();
					}
				}
			}
		}
		return this.getNodeLabel() + "_" + this.id;
	}
	
	///
	public String toStringDot() {
		String ret = "";
		
		ret += this.getId();
		ret += " [label=\"" + Util.esc_dot(this.getNodeLabel()) + "\"";
		
		if(this.isTrigger) {
			ret += ", color=lightblue, style=filled";
		} else if(this.isPotential) {
			ret += ", color=green, style=filled";
		} else if(this.isControl) {
			ret += ", color=yellow, style=filled";
		} else if(this.isManipulate) {
			ret += ", color=pink, style=filled";
		} else if(this.isExit) {
			ret += ", color=red, style=filled";
		}
		
		ret += "];\n";
		
		return ret;
	}
	
	/// utilities
	public String toSource() {
		return ((org.mozilla.javascript.ast.AstNode)this.node).toSource();
	}
	public Class<?> myGetClass() {
		return ((org.mozilla.javascript.ast.AstNode)this.node).getClass();
	}
	public org.mozilla.javascript.ast.AstNode getAstNode() {
		return (org.mozilla.javascript.ast.AstNode)this.node;
	}
}
