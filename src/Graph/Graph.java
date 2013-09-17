package Graph;

import Analyzer.Config;
import Analyzer.Util;
import Rule.RuleList;
import org.jsoup.nodes.Attribute;
import org.mozilla.javascript.ast.AstRoot;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Graph {
	
	public Graph(int offset) {
		this.init(offset);
	}

	public final org.mozilla.javascript.ast.AstRoot dummyRootAstNode = null;
	public Node dummyRootNode = null;
	public int dummyRootNodeId = 3;
	public static Graph getDummyGraph() {
		Graph ret = new Graph(3);
		ret.setDummyRootNode();
		return ret;
	}
	public static Graph getDummyGraph(int offset) {
		Graph ret = new Graph(offset);
		ret.setDummyRootNode();
		return ret;
	}
	private void setDummyRootNode() {
		this.dummyRootNodeId = this.addRootNode(this.dummyRootAstNode);
		this.dummyRootNode = this.getNodeById(this.dummyRootNodeId);
		this.dummyRootNode.setIdDummy(true);
	}
	public void setExitNode(int nodeId, boolean isExit) {
		Node node = this.getNodeById(nodeId);
		node.setIsExit(isExit);
	}

	protected List<Node> nodes = null;
	protected List<Edge> edges = null;
	protected int nextId = 3;
	protected int rootNodeId = 3;
	
	protected HashMap<org.mozilla.javascript.Node, Integer> hash_AstNode_NodeId = null;
	protected HashMap<Integer, Node> hash_NodeId_Node = null;
	protected HashMap<Integer, Edge> hash_EdgeId_Edge = null;
	
	protected List<Edge> callerCalleeEdges = null;
	
	private void init(int offset) {
		this.nodes = new LinkedList<Node>();
		this.edges = new LinkedList<Edge>();
		this.nextId = offset;
		this.rootNodeId = 3;
		
		this.hash_AstNode_NodeId = new HashMap<org.mozilla.javascript.Node, Integer>();
		this.hash_NodeId_Node = new HashMap<Integer, Node>();
		this.hash_EdgeId_Edge = new HashMap<Integer, Edge>();
		
		this.callerCalleeEdges = new LinkedList<Edge>();
		
	}
	
	///// refactor later
	// should set this information when parsing
	protected RuleList rules = null;
	public void setRules(RuleList rules) {
		this.rules = rules;
		this.applyRule(rules);
		this.relateParentChild();
	}
	private void applyRule(RuleList rules) {
		this.rules = rules;
		for(Node node : this.nodes) {
			node.applyRule(rules);
		}
	}
	
	///// for combining
	public List<Node> getRootChildren() {
		List<Node> ret = new LinkedList<Node>();
		for(Node node : this.getRootNode().getChildren()) {
			ret.add(node);
		}
		return ret;
	}
	public void setEvent(int edgeId, org.jsoup.nodes.Element elm, Attribute attr) {
		Edge edge = this.hash_EdgeId_Edge.get(edgeId);
		edge.setEvent(elm, attr);
	}
	public void setEvent(int edgeId, org.mozilla.javascript.ast.AstNode objAstNode, org.mozilla.javascript.ast.AstNode eventAstNode) {
		Edge edge = this.hash_EdgeId_Edge.get(edgeId);
		edge.setEvent(objAstNode, eventAstNode);
	}
	public void setEvent(int edgeId, org.mozilla.javascript.ast.AstNode objAstNode, org.mozilla.javascript.ast.AstNode eventAstNode, String eventLabel) {
		Edge edge = this.hash_EdgeId_Edge.get(edgeId);
		edge.setEvent(objAstNode, eventAstNode);
		edge.setEventLabel(eventLabel);
	}

	// refactor later
	// should set this information when adding nodes and edges
	public void relateParentChild() {
		for(Edge edge : this.edges) {
			Node fromNode = this.getNodeById(edge.getFrom());
			Node toNode = this.getNodeById(edge.getTo());
			
			fromNode.addChild(toNode);
			toNode.setParent(fromNode);
		}
	}
	
	///// basic manipulations
	public int addNode(org.mozilla.javascript.Node node) {
		Integer id = this.hash_AstNode_NodeId.get(node);
		if(id == null) {
			id = this.getNextId();
			this.hash_AstNode_NodeId.put(node, id);
			
			Node n = new Node(id, node);
			this.nodes.add(n);
			this.hash_NodeId_Node.put(id, n);
		}
		return id;
	}
	
	public void addChildren(Node parent, Graph graph) {
		this.addNode(parent);
		for(Node child : parent.getChildren()) {
			Edge edge = graph.getEdge(parent, child);
			this.addEdge(edge);
			
			this.addChildren(child, graph);
		}
	}
	
	public int addNode(Node node) {
		Integer id = this.hash_AstNode_NodeId.get(node.getAstNode());
		if(id == null) {
			id = node.getId();
			this.hash_AstNode_NodeId.put(node.getAstNode(), id);
			
			this.nodes.add(node);
			this.hash_NodeId_Node.put(id, node);
		}
		if(this.nextId < id) {
			this.nextId = id + 1;
		}
		return id;
	}
	
	public int addEdge(org.mozilla.javascript.Node parent, org.mozilla.javascript.Node node) {
		Integer parentId = this.hash_AstNode_NodeId.get(parent);
		if(parentId == null) {
			parentId = this.addNode(parent);
		}
		Integer nodeId = this.hash_AstNode_NodeId.get(node);
		if(nodeId == null) {
			nodeId = this.addNode(node);
		}
		int edgeId = this.addEdge(parentId, nodeId);
		
		return edgeId;
	}
	public int addEdgeFromRootNode(Node node) {
		node.setParent(this.getRootNode(), true);
		int edgeId = this.addEdge(this.getRootNodeId(), node.getId());
		this.getRootNode().addChild(node);
		return edgeId;
	}
	public int addEdgeFromRootNode(int nodeId) {
		Node node = this.getNodeById(nodeId);
		return this.addEdgeFromRootNode(node);
	}
	public int addGraph(Graph graph) {
		for(Node node : graph.nodes) {
			this.addNode(node);
		}
		for(Edge edge : graph.edges) {
			this.addEdge(edge);
		}
		return graph.getRootNodeId();
	}
	public int getEdgeId(org.mozilla.javascript.Node fromNode, org.mozilla.javascript.Node toNode) {
		Integer fromNodeId = this.hash_AstNode_NodeId.get(fromNode);
		if(fromNodeId == null) {
			fromNodeId = this.addNode(fromNode);
		}
		Integer toNodeId = this.hash_AstNode_NodeId.get(toNode);
		if(toNodeId == null) {
			toNodeId = this.addNode(toNode);
		}
		for(Edge edge : this.edges) {
			if(edge.getFrom() == fromNodeId && edge.getTo() == toNodeId) {
				return edge.getId();
			}
		}
		return -1;
	}
	public void removeNode(Node node) {
		this.nodes.remove(node);
	}
	public void removeEdge(Edge edge) {
		this.edges.remove(edge);
	}
	
	public void setCondition(int edgeId, org.mozilla.javascript.ast.AstNode cond) {
		Edge edge = this.hash_EdgeId_Edge.get(edgeId);
		if(edge == null) {
			System.err.println("Not found edge@Graph.setCondition: " + edgeId + ", " + cond.toSource());
		} else {
			edge.setCondNode(cond);
		}
	}
	public void setElseCondition(int edgeId, org.mozilla.javascript.ast.AstNode astNode) {
		Edge edge = this.hash_EdgeId_Edge.get(edgeId);
		if(edge == null) {
			System.err.println("Not found edge@Graph.setElseCondition: " + edgeId + ", " + astNode.toSource());
		} else {
			edge.setElseCondNode(astNode);
		}
	}
	
	public int addRootNode(AstRoot node) {
		Integer id = this.hash_AstNode_NodeId.get(node);
		if(id != null) {
			if(id == this.rootNodeId) {
				System.err.println("Already set root node");
			} else {
				this.rootNodeId = id;
			}
		} else {
			id = this.addNode(node);
			this.rootNodeId = id;
		}
		return id;
	}

	public int addEdge(int from, int to) {
		int edgeId = this.getNextId();
		Edge edge = new Edge(edgeId, from, to);
		this.edges.add(edge);
		this.hash_EdgeId_Edge.put(edgeId, edge);
		return edgeId;
	}
	public int addEdge(Edge edge) {
		if(!this.edges.contains(edge)) {
			int edgeId = edge.getId();
			this.edges.add(edge);
			this.hash_EdgeId_Edge.put(edgeId, edge);
			return edgeId;
		}
		return edge.getId();
	}
	
	public int getRootNodeId() {
		return this.rootNodeId;
	}
	public Node getRootNode() {
		return this.hash_NodeId_Node.get(this.rootNodeId);
	}
	public List<Node> getNodes() {
		return this.nodes;
	}
	public List<Edge> getEdges() {
		return this.edges;
	}
	public Node getNodeById(int id) {
		return this.hash_NodeId_Node.get(id);
	}
	public Node getNodeByAstNode(org.mozilla.javascript.ast.AstNode astNode) {
		Integer nodeId = this.hash_AstNode_NodeId.get(astNode);
		if(nodeId == null) {
			return null;
		}
		return this.getNodeById(nodeId);
	}
	
	public int getNodeId(org.mozilla.javascript.Node node) {
		Integer id = this.hash_AstNode_NodeId.get(node);
		if(id == null) {
			id = this.addNode(node);
		}
		return id;
	}
	public Edge getEdge(Node fromNode, Node toNode) {
		if(fromNode == null || toNode == null) {
			return null;
		}
		List<Edge> candidates = new LinkedList<Edge>();
		for(Edge edge : this.edges) {
			if(edge.getFrom() == fromNode.getId() &&
					edge.getTo() == toNode.getId()) {
				candidates.add(edge);
			}
		}
		if(candidates.size() == 1) {
			return candidates.get(0);
		}
		System.err.println("Error@Graph.getEdge: " + candidates.size());
		System.err.println("\t" + (fromNode == null ? "from: null" : "from: " + fromNode.getNodeLabel()) + ", "
				+ (toNode == null ? "to: null" : "to: " + toNode.getNodeLabel()));
		return null;
	}
	public List<Edge> getEdges(Node fromNode, Node toNode) {
		if(fromNode == null || toNode == null) {
			return null;
		}
		List<Edge> ret = new LinkedList<Edge>();
		for(Edge edge : this.edges) {
			if(edge.getFrom() == fromNode.getId() &&
					edge.getTo() == toNode.getId()) {
				ret.add(edge);
			}
		}
		return ret;
	}
	
	public boolean hasCondition(org.mozilla.javascript.ast.AstNode fromAstNode, org.mozilla.javascript.ast.AstNode toAstNode) {
		Node fromNode = this.getNodeByAstNode(fromAstNode);
		Node toNode = this.getNodeByAstNode(toAstNode);

		if(fromNode == null || toNode == null) {
			// to be debugged
			//System.err.println("From or to node are null@Graph.hasCondition: " + fromNode + ", " + toNode);
			return true;
		}
		
		Edge edge = this.getEdge(fromNode, toNode);
		if(edge == null) {
			System.err.println("An edge is null@Graph.hasCondition: ");
			return true;
		}
		
		return edge.hasCond();
	}
	
	public List<Edge> getEdgesFrom(Node node) {
		List<Edge> ret = new LinkedList<Edge>();
		if(node == null) {
			return ret;
		}
		for(Edge edge : this.edges) {
			if(edge.getFrom() == node.getId()) {
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
		for(Edge edge : this.edges) {
			if(edge.getTo() == node.getId()) {
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
		for(Node node : this.nodes) {
			if(edge.getFrom() == node.getId()) {
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
		for(Node node : this.nodes) {
			if(edge.getTo() == node.getId()) {
				ret.add(node);
			}
		}
		return ret;
	}
	
	protected int getNextId() {
		return this.nextId++;
	}
	
	public int getOffset4Id() {
		return this.nextId;
	}
	
	
	///// toString
	public String toStringXml() {
		String ret = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";

		ret += "<" + Config.StateMachineTag + " name=\"Call graph\" nextId=\"10000\" >\n";
		ret += "\t<" + Config.entryTag  + " id=\"1\" name=\"Entry\" xpos=\"10\" ypos=\"10\" />\n";
		ret += "\t<" + Config.exitTag  + " id=\"2\" name=\"Exit\" xpos=\"100\" ypos=\"10\" />\n";

		ret += "\t<" + Config.stateTag + "s>\n";
		for(Node node : this.nodes) {
			ret += "\t\t<" + Config.stateTag + " id=\"" + node.getId() + "\" name=\"" + node.getId() + "\" />\n";
		}
		ret += "\t</" + Config.stateTag + "s>\n";
		

		ret += "\t<" + Config.transTag + "s>\n";
		ret += "\t\t<" + Config.transTag + " id=\"" + 1234567 + "\" " +
				"from=\"" + 1 + "\" to=\"" + this.rootNodeId + "\" />\n";
		for(Edge edge : this.edges) {
			ret += "\t\t<" + Config.transTag + " id=\"" + edge.getId() + "\" " +
					"from=\"" + edge.getFrom() + "\" to=\"" + edge.getTo() + "\" />\n";
		}
		ret += "\t</" + Config.transTag + "s>\n";
		
		ret += "</" + Config.StateMachineTag + ">\n";
		
		return ret;
	}
	
	public String toStringDot() {
		String ret = "";
		

		ret += "digraph MyGraph {\n";
		
		///// node start
		for(Node node : this.nodes) {
			ret += "\t" + node.toStringDot();
		}
		
		ret += "\n";
		///// node end
		
		///// edge start
		for(Edge edge : this.edges) {
			ret += "\t" + edge.getFrom() + " -> " + edge.getTo() +
					"  [label=\"" + Util.esc_dot(edge.toStringDot()) + "\", weight=1];\n";
		}
		///// edge end
		
		ret += "}\n";
		
		return ret;
	}
	
	///// utilities
	public Stack<Node> dfs(Node start, Node goal) { // depth first search
		for(Node node : this.nodes) {
			node.init4dfs();
		}
		Stack<Node> stack = new Stack<Node>();
		stack.push(start);
		while(!stack.isEmpty()) {
			Node node = stack.peek();
			if(node.getId() == goal.getId()) {
				return stack;
			} else {
				Node child = node.findUnvisitedChild();
				if(child == null) {
					node = stack.pop();
				} else {
					child.setVisit4dfs(true);
					stack.push(child);
				}
			}
		}
		return stack;
	}
}
