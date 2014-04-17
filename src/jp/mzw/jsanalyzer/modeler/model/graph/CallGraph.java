package jp.mzw.jsanalyzer.modeler.model.graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import jp.mzw.jsanalyzer.modeler.model.Element;
import jp.mzw.jsanalyzer.parser.JSCode;
import jp.mzw.jsanalyzer.parser.JSParser;
import jp.mzw.jsanalyzer.util.StringUtils;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.IfStatement;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NodeVisitor;

/**
 * Represents call graph
 * @author Yuta Maezawa
 *
 */
public class CallGraph extends Element {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * A dummy AST root node for this initial node
	 */
	protected static AstRoot mDummyAstRoot = new AstRoot();
	/**
	 * An initial node using the dummy AST root node
	 */
	protected static Node mInitNode = new Node(CallGraph.mDummyAstRoot, null);
	
	/**
	 * Contains all nodes
	 */
	protected List<Node> mNodeList;
	/**
	 * Contains all edges
	 */
	protected List<Edge> mEdgeList;
	
	/**
	 * Constructor
	 */
	public CallGraph(List<JSCode> jsCodeList) {
		this.mNodeList = new ArrayList<Node>();
		this.mEdgeList = new ArrayList<Edge>();
		
		for(JSCode jsCode : jsCodeList) {
			JSParser jsParser = new JSParser(jsCode);
			this.addNodes(jsParser.getAstRoot(), jsCode);
		}
		this.addCondition();
		this.addCallerCallee();
	}
	
	/**
	 * Constructor for finite state machine
	 */
	protected CallGraph() {
		this.mNodeList = new ArrayList<Node>();
		this.mEdgeList = new ArrayList<Edge>();
	}

	/**
	 * Constructor
	 * @param nodeList A list of nodes
	 * @param edgeList A list of edges
	 */
	public CallGraph(List<Node> nodeList, List<Edge> edgeList) {
		this.mNodeList = nodeList;
		this.mEdgeList = edgeList;
	}
	
	///// Constructing call graph
	/**
	 * Adds JavaScript AST nodes
	 * @param astRoot A root of JavaScript AST
	 * @param event If in-line JS code, it has an event which occurs when this JS code is called
	 */
	private void addNodes(AstRoot astRoot, final JSCode jsCode) {
		final ArrayList<Node> newNodes = new ArrayList<Node>();
		
		if(astRoot == null) {
			System.out.println(jsCode);
		}
		
		// Adds nodes
		astRoot.visitAll(new NodeVisitor() {
			@Override
			public boolean visit(AstNode astNode) {
				Node node = new Node(astNode, jsCode);
				newNodes.add(node);
				mNodeList.add(node);
//				TextFileUtils.registSnapchot(toDot());
				return true;
			}
		});

		// Adds edges
		for(Node node : newNodes) {
			if(node.getAstNode() instanceof AstRoot) {
				Edge edge = new Edge(CallGraph.mInitNode.getId(), node.getId());
				edge.setEvent(jsCode.getHTMLElement(), jsCode.getEventAttr(), jsCode.getTriggerRule());
				this.mEdgeList.add(edge);
//				TextFileUtils.registSnapchot(toDot());
			} else if(node.getAstNode() instanceof FunctionNode) {
				// Will add at this.addCallerCallee()
			} else {
				AstNode parentAstNode = node.getAstNode().getParent();
				Node parent = this.getNode(parentAstNode);
				Edge edge = new Edge(parent.getId(), node.getId());
				this.mEdgeList.add(edge);
//				TextFileUtils.registSnapchot(toDot());
			}
		}
	}
	
	/**
	 * Adds edges representing caller-callee relationships
	 */
	private void addCallerCallee() {
		// Collects function nodes
		ArrayList<FunctionNode> funcNodeList = new ArrayList<FunctionNode>();
		for(Node node : this.mNodeList) {
			if(node.getAstNode() instanceof FunctionNode) {
				funcNodeList.add((FunctionNode)node.getAstNode());
			}
		}
		// Relates function calls to corresponding function nodes
		for(Node node : this.mNodeList) {
			if(node.getAstNode() instanceof FunctionCall) {
				FunctionCall funcCall = (FunctionCall)node.getAstNode();
				
				boolean found = false;
				for(FunctionNode funcNode : funcNodeList) {
					if(funcNode.getName().equals(funcCall.getTarget().toSource())) {
						Edge edge = new Edge(this.getNode(funcCall).getId(), this.getNode(funcNode).getId());
						this.mEdgeList.add(edge);
//						TextFileUtils.registSnapchot(toDot());
						found = true;
					}
				}
				if(!found && !JSParser.isExtern(funcCall.getTarget().toSource())) {
					StringUtils.printError(this, "Cannot find corresponding function node", funcCall.getTarget().toSource());
				}
			}
		}
	}
	
	/**
	 * Adds condition statements at each edge (to be debugged)
	 */
	private void addCondition() {
		for(Node node : this.mNodeList) {
			if(node.getAstNode() instanceof IfStatement) {
				IfStatement ifStatement = (IfStatement)node.getAstNode();
				
				AstNode cond = ifStatement.getCondition();
				Node toNode = this.getNode(ifStatement.getThenPart());
				Edge edge = this.getEdge(node.getId(), toNode.getId());
				if(edge != null) {
					edge.setCond(cond, false);
//					TextFileUtils.registSnapchot(toDot());
				}
				
				AstNode elsePart = ifStatement.getElsePart();
				if(elsePart != null) {
					Node elseNode = this.getNode(elsePart);
					Edge elseEdge = this.getEdge(node.getId(), elseNode.getId());
					elseEdge.setCond(cond, true);
//					TextFileUtils.registSnapchot(toDot());
				}
				
			}
		}
	}
	
	///// Getters and setters	
	public List<Node> getNodeList() {
		return this.mNodeList;
	}
	public List<Node> getAllNodeList() {
		List<Node> ret = new ArrayList<Node>();
		ret.add(mInitNode);
		ret.addAll(this.mNodeList);
		return ret;
	}
	public List<Edge> getEdgeList() {
		return this.mEdgeList;
	}
	public void addEdge(Edge edge) {
		this.mEdgeList.add(edge);
	}
	public void addNode(Node node) {
		this.mNodeList.add(node);
	}

	public List<Edge> getEdgesFrom(Node node) {
		List<Edge> ret = new LinkedList<Edge>();
		if(node == null) {
			return ret;
		}
		for(Edge edge : this.mEdgeList) {
			if(edge.getFromNodeId() == node.getId()) {
				ret.add(edge);
			}
		}
		return ret;
	}
	public List<Edge> getEdgesTo(Node node) {
		List<Edge> ret = new LinkedList<Edge>();
		if(node == null) {
			return ret;
		}
		for(Edge edge : this.mEdgeList) {
			if(edge.getToNodeId() == node.getId()) {
				ret.add(edge);
			}
		}
		return ret;
	}
	public List<Node> getFromNodes(Edge edge) {
		List<Node> ret = new LinkedList<Node>();
		if(edge == null) {
			return ret;
		}
		for(Node node : this.mNodeList) {
			if(edge.getFromNodeId() == node.getId()) {
				ret.add(node);
			}
		}
		return ret;
	}
	public List<Node> getToNodes(Edge edge) {
		List<Node> ret = new LinkedList<Node>();
		if(edge == null) {
			return ret;
		}
		for(Node node : this.mNodeList) {
			if(edge.getToNodeId() == node.getId()) {
				ret.add(node);
			}
		}
		return ret;
	}
	
	/**
	 * Gets an initial node, which is statically created with a dummy AST root
	 * @return An initial node of this call graph
	 */
	public static Node getInitNode() {
		return CallGraph.mInitNode;
	}
	
	/**
	 * Gets a node corresponding to given AST node
	 * @param astNode Given AST node
	 * @return A corresponding node
	 */
	public Node getNode(AstNode astNode) {
		for(Node node : this.mNodeList) {
			if(astNode == node.getAstNode()) {
				return node;
			}
		}
		return null;
	}

	public Node getNode(String id, boolean containsInitNode) {
		if(containsInitNode && mInitNode.getId().equals(id)) {
			return mInitNode;
		}
		return this.getNode(id);
	}
	
	public Node getNode(String id) {
		for(Node node : this.mNodeList) {
			if(node.getId().equals(id)) {
				return node;
			}
		}
		return null;
	}
	

	public void removeNode(Node node) {
		this.mNodeList.remove(node);
	}
	public void removeEdge(Edge edge) {
		this.mEdgeList.remove(edge);
	}
	
	/**
	 * Gets an edge using from- and to-node IDs
	 * @param fromNodeId From-node ID
	 * @param toNodeId To-node ID
	 * @return An edge which comes from and goes to given nodes
	 */
	public Edge getEdge(String fromNodeId, String toNodeId) {
		for(Edge edge : this.mEdgeList) {
			if(edge.getFromNodeId().equals(fromNodeId) && edge.getToNodeId().equals(toNodeId)) {
				return edge;
			}
		}
		return null;
	}
	

	/**
	 * Gets corresponding function node with given AST node (to be debugged)
	 * @param cbNode Given AST node
	 * @return Corresponding function node
	 */
	public FunctionNode getFunctionNode(AstNode cbNode) {
		if(cbNode instanceof FunctionNode) {
			return (FunctionNode)cbNode;
		} else if(cbNode instanceof Name) {
			String funcName = cbNode.toSource();
			for(Node node : this.mNodeList) {
				if(node.getAstNode() instanceof FunctionNode) {
					String _funcName = ((FunctionNode)node.getAstNode()).getName();
					if(funcName.equals(_funcName)) {
						return (FunctionNode)node.getAstNode();
					}
				}
			}
		} else {
			StringUtils.printError(this, "Cannot find callback function node", "[" + cbNode.getClass().getName() + "] " + cbNode.toSource());
		}
		
		return null;
	}
	
	/**
	 * Clone this call graph
	 * @return clone
	 */
	public CallGraph clone() {
		ArrayList<Node> cloneNodeList = new ArrayList<Node>();
		for(Node node : this.mNodeList) {
			cloneNodeList.add(node.clone());
		}
		
		ArrayList<Edge> cloneEdgeList = new ArrayList<Edge>();
		for(Edge edge : this.mEdgeList) {
			cloneEdgeList.add(edge.clone());
		}
		return new CallGraph(cloneNodeList, cloneEdgeList);
	}
	
	
	///// Output
	/**
	 * Returns a string representing this call graph contents in Graphviz/Dot format
	 * @return A dot string
	 */
	public String toDot() {
		String ret = "digraph CallGraph {\n";

		ret += "size=\"15.0,10.0\";\n";
		ret += "ratio=\"0.75\";\n";

		ret += CallGraph.mInitNode.getId() + ";\n";
		for(Node node : this.mNodeList) {
			ret += node.getId() + node.getDotLabel() + ";\n";
		}
		
		for(Edge edge : this.mEdgeList) {
			ret += edge.getFromNodeId() + " -> " + edge.getToNodeId() + edge.getDotLabel() + ";\n";
		}
		
		ret += "}\n";
		return ret;
	}
}
