package jp.mzw.jsanalyzer.modeler;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.commons.lang3.tuple.Pair;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;

import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.modeler.model.graph.CallGraph;
import jp.mzw.jsanalyzer.modeler.model.graph.Edge;
import jp.mzw.jsanalyzer.modeler.model.graph.Node;
import jp.mzw.jsanalyzer.util.TextFileUtils;

public class FSMAbstractor extends Modeler {
	
	/**
	 * Constructor
	 * @param analyzer Provides project information
	 */
	public FSMAbstractor(Analyzer analyzer) {
		super(analyzer);
	}
	
	
	/**
	 * Abstracts extended call graph focusing on interactions with Ajax apps
	 * @param xcg Extended call graph
	 * @return Abstracted call graph
	 */
	public Pair<CallGraph, AbstractionManager> abst(CallGraph xcg) {
		System.out.println("Abstracts call graph...");
		
		AbstractionManager abstManager = new AbstractionManager();
		
		CallGraph acg = xcg.clone();
		
		CallGraph.getInitNode().prepareSearch();
		for(Node node : acg.getNodeList()) {
			node.prepareSearch();
			
			AstNode parentAstNode = node.getAstNode().getParent();
			if(node.getAstNode() instanceof AstRoot) {
				CallGraph.getInitNode().addChild(node);
			}
			if(parentAstNode != null) {
				Node parent = acg.getNode(parentAstNode);
				parent.addChild(node);
			}
		}

		this.search(acg, abstManager);
		
		/// Heuristic processes (to be refactorred)
		this.removeIsolateNodes(acg, abstManager);
		this.removeNotCalledFunctions(acg);
		this.removeImproperRelationships(acg, abstManager);
		
		return Pair.of(acg, abstManager);
	}
	

	/**
	 * Finds and abstracts given call graph by width-first-search.
	 * And, stores abstraction information.
	 * @param graph Given call graph
	 * @param abstManager Where stores abstraction information
	 */
	private void search(CallGraph graph, AbstractionManager abstManager) { // Width first search
		
		Stack<Node> stack = new Stack<Node>();
		stack.push(CallGraph.getInitNode());
		while(!stack.isEmpty()) {
			Node node = stack.peek();
			Node child = node.findUnvisitedChild();
			if(child == null) {
				node = stack.pop();

				// Abstraction
				List<Edge> fromEdges = graph.getEdgesFrom(node);
				List<Edge> toEdges = graph.getEdgesTo(node);
				
				if(fromEdges.size() == 0 && toEdges.size() == 1) {
					// ---(one edge)---> (this node) (no edge)
					Edge toEdge = toEdges.get(0);
					
					if(!toEdge.hasEvent() && !toEdge.hasCond()) {
						Node fromNode = graph.getNode(toEdge.getFromNodeId());
//						fromNode.abstractNode(node, toEdge);
						abstManager.add(fromNode, node, toEdge);
						
						graph.removeNode(node);
						graph.removeEdge(toEdge);
						
//						TextFileUtils.registSnapchot(graph.toDot());
					}
				} else if(fromEdges.size() == 1 && toEdges.size() == 1) {
					// (this node) ---(one edge)--->
					Edge fromEdge = fromEdges.get(0);
					Edge toEdge = toEdges.get(0);
					
					if(!toEdge.hasEvent() && !toEdge.hasCond()
							&& graph.getNode(toEdge.getFromNodeId()) != null) { // Considering isolate nodes
						Node fromNode = graph.getNode(toEdge.getFromNodeId());
						
//						fromNode.abstractNode(node, toEdge);
						abstManager.add(fromNode, node, toEdge);
						
						graph.removeNode(node);
						graph.removeEdge(toEdge);

//						TextFileUtils.registSnapchot(graph.toDot());
						
						fromEdge.setFromNodeId(fromNode.getId());
					}
					
				}
			} else {
				child.visit();
				stack.push(child);
			}
		}

		/// abstract improper node
		List<Node> improperNodes = new ArrayList<Node>();
		List<Edge> improperEdges = new ArrayList<Edge>();
		for(Node node : graph.getNodeList()) {
			List<Edge> fromEdges = graph.getEdgesFrom(node);
//			if(!node.isAbstractedControl() && fromEdges.size() == 0) {
			if(!abstManager.isControl(node) && fromEdges.size() == 0) {
				List<Edge> toEdges = graph.getEdgesTo(node);
				boolean isImproper = true;
				for(Edge toEdge : toEdges) {
					if(toEdge.hasEvent()) {
						isImproper = false;
					}
				}
				if(isImproper) {
					for(Edge toEdge : toEdges) {
						Node fromNode = graph.getNode(toEdge.getFromNodeId());
//						fromNode.abstractNode(node, toEdge);
						abstManager.add(fromNode, node, toEdge);
						improperEdges.add(toEdge);
					}
					improperNodes.add(node);
				}
			}
		}
		// abstract improper edge
		for(Edge edge : graph.getEdgeList()) {
			List<Node> fromNodes = graph.getFromNodes(edge);
			List<Node> toNodes = graph.getToNodes(edge);
			
			if(fromNodes.size() == 1 && toNodes.size() == 1 &&
					!edge.hasEvent() && !edge.hasCond()) {
				Node fromNode = fromNodes.get(0);
				Node toNode = toNodes.get(0);
				
//				if(fromNode.isAbstractedControl() && toNode.isAbstractedControl()) {
				if(abstManager.isControl(fromNode) && abstManager.isControl(toNode)) {
					continue;
				}
				
//				fromNode.abstractNode(toNode, edge);
				abstManager.add(fromNode, toNode, edge);
				
				improperNodes.add(toNode);
				improperEdges.add(edge);
				for(Edge _edge : graph.getEdgesFrom(toNode)) {
					_edge.setFromNodeId(fromNode.getId());
					if(_edge.getToNodeId() == toNode.getId()) {
						_edge.setToNodeId(fromNode.getId());
					}
				}
				
			}
		}
		// remove
		for(Node node : improperNodes) {
			graph.removeNode(node);

//			TextFileUtils.registSnapchot(graph.toDot());
		}
		for(Edge edge : improperEdges) {
			graph.removeEdge(edge);

//			TextFileUtils.registSnapchot(graph.toDot());
		}
		
		// Over abstraction
		boolean overAbst = false;
		for(Edge edge : graph.getEdgeList()) {
			Node fromNode = graph.getNode(edge.getFromNodeId());
			Node toNode = graph.getNode(edge.getToNodeId());
			
			if(toNode == null) {
				// Over abstracted node
				// fromNode -(edge)-> "No toNode"
				toNode = abstManager.getAbstractedNode(edge.getToNodeId());
				graph.addNode(toNode);
				
				abstManager.remove(fromNode, toNode);
				
				overAbst = true;
			}
		}
		
		
		// re-abstract due to improper nodes
		if(0 < improperNodes.size() || overAbst) {
			this.search(graph, abstManager);
		}
	}

	/**
	 * Finalizes abstraction search (to be debugged)
	 * @param graph An extended call graph to be abstracted
	 */
	private void removeIsolateNodes(CallGraph graph, AbstractionManager abstManager) {
		List<Node> removeNodes = new ArrayList<Node>();
		List<Edge> removeEdges = new ArrayList<Edge>();
		
		// (Root node) --(no event/cond)-->
		// to be debugged
		for(Node node : graph.getNodeList()) {
			Edge edge = graph.getEdge(CallGraph.getInitNode().getId(), node.getId());
			if(edge != null && !edge.hasEvent() && !edge.hasCond()) {
				
				abstManager.add(CallGraph.getInitNode(), node, edge);
				removeNodes.add(node);
				removeEdges.add(edge);
				
				for(Edge e : graph.getEdgeList()) {
					if(e.getFromNodeId() == node.getId()) {
						e.setFromNodeId(CallGraph.getInitNode().getId());
					}
				}
				
//				TextFileUtils.registSnapchot(graph.toDot());
			}
		}
		
		// isolate nodes
		for(Node node : graph.getNodeList()) {
			boolean isolate = true;
			for(Edge edge : graph.getEdgeList()) {
				if(edge.getFromNodeId() == node.getId() ||
						edge.getToNodeId() == node.getId()) {
					isolate = false;
					break;
				}
			}
			if(isolate) {
				removeNodes.add(node);
			}
		}
		// remove
		for(Node node : removeNodes) {
			graph.removeNode(node);
//			TextFileUtils.registSnapchot(graph.toDot());
		}
		for(Edge edge : removeEdges) {
			graph.removeEdge(edge);
//			TextFileUtils.registSnapchot(graph.toDot());
		}
		
	}
	
	private void removeImproperRelationships(CallGraph graph, AbstractionManager abstManager) {
		List<Node> improperNodes = new ArrayList<Node>();
		List<Edge> improperEdges = new ArrayList<Edge>();

		// abstract improper edge
		for(Node node : graph.getNodeList()) {
			if(node.getId().equals(CallGraph.getInitNode().getId())) {
				continue;
			}
			List<Edge> fromEdgeList = graph.getEdgesFrom(node);
			List<Edge> toEdgeList = graph.getEdgesTo(node);
			
			boolean no_event_cond = true;
			for(Edge toEdge : toEdgeList) {
				if(toEdge.hasEvent() || toEdge.hasCond()) {
					no_event_cond = false;
					break;
				}
			}
			
			/// --(multi and no event/cond)--> (node) --(no edge)-->
			if(fromEdgeList.size() == 0 && 1 < toEdgeList.size() && no_event_cond) {
				// Adds removal targets
				improperNodes.add(node);
				improperEdges.addAll(toEdgeList);
				
				// Sets abstraction information
				for(Edge toEdge : toEdgeList) {
					abstManager.add(graph.getNode(toEdge.getFromNodeId()), node, toEdge);
				}
			}
		}
		
		// remove
		for(Node node : improperNodes) {
			graph.removeNode(node);

//			TextFileUtils.registSnapchot(graph.toDot());
		}
		for(Edge edge : improperEdges) {
			graph.removeEdge(edge);

//			TextFileUtils.registSnapchot(graph.toDot());
		}
		if(improperNodes.size() != 0 && improperEdges.size() != 0) {
			removeImproperRelationships(graph, abstManager);
		}
	}
	
	private void removeNotCalledFunctions(CallGraph graph) {
		
		ArrayList<Node> removeNodeList = new ArrayList<Node>();
		ArrayList<Edge> removeEdgeList = new ArrayList<Edge>();
		for(Node node : graph.getNodeList()) {
			if(node.getId().equals(CallGraph.getInitNode().getId())) {
				continue;
			}
			ArrayList<Edge> fromEdgeList = new ArrayList<Edge>();
			ArrayList<Edge> toEdgeList = new ArrayList<Edge>();
			for(Edge edge : graph.getEdgeList()) {
				if(edge.getFromNodeId().equals(node.getId())) {
					fromEdgeList.add(edge);
				}
				if(edge.getToNodeId().equals(node.getId())) {
					toEdgeList.add(edge);
				}
			}

			/// --(NO edge)--> (Not root node) --(regardless)-->
			/// Remove the (Not root node) and the (regardless) edges
			/// Recursively remove
			if(toEdgeList.size() == 0) {
				removeNodeList.add(node);
				removeEdgeList.addAll(fromEdgeList);
			}
		}
		if(removeNodeList.size() != 0) {
			for(Node node : removeNodeList) {
				graph.removeNode(node);
			}
			for(Edge edge : removeEdgeList) {
				graph.removeEdge(edge);
			}
			removeNotCalledFunctions(graph);
		}
	}
}
