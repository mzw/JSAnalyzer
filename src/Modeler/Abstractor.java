package Modeler;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import Graph.Edge;
import Graph.Graph;
import Graph.Node;

public class Abstractor extends Modeler {
	
	public Abstractor(Graph graph) {
		super(graph);
	}
	
	public void abst() {
		this.init4dfs();
		this.abst(this.graph.getRootNode(), this.graph);
	}
	
	private void abst(Node start, Graph graph) {

		Stack<Node> stack = new Stack<Node>();
		stack.push(start);
		while(!stack.isEmpty()) {
			Node node = stack.peek();

			Node child = node.findUnvisitedChild();
			if(child == null) {

				List<Edge> fromEdges = graph.getEdgesFrom(node);
				List<Edge> toEdges = graph.getEdgesTo(node);

				if(fromEdges.size() == 1 && toEdges.size() == 0) {
					// root node
				} else if(fromEdges.size() == 0 && toEdges.size() == 1) {
					// ---(one edge)---> (this node) (no edge)
					Edge toEdge = toEdges.get(0);
					
					if(!toEdge.hasEvent() && !toEdge.hasCond()) {
						//if(!toEdge.hasCond()) || (toEdge.hasCond() && !node.isControl())) {
						Node fromNode = graph.getNodeById(toEdge.getFrom());
						fromNode.abstractNode(node, toEdge);
						
						graph.removeNode(node);
						graph.removeEdge(toEdge);
						//}
					}
				} else if(fromEdges.size() == 1 && toEdges.size() == 1) {
					// (this node) ---(one edge)--->
					Edge fromEdge = fromEdges.get(0);
					Edge toEdge = toEdges.get(0);
					
					if(!toEdge.hasEvent() && !toEdge.hasCond()) {
						//if(!toEdge.hasCond()) || (toEdge.hasCond() && !node.isControl())) {
						Node fromNode = graph.getNodeById(toEdge.getFrom());
						fromNode.abstractNode(node, toEdge);
						
						graph.removeNode(node);
						graph.removeEdge(toEdge);
						
						fromEdge.setFrom(fromNode.getId());
						//}
					}
					
				}
				
				node = stack.pop();
			} else {
				child.setVisit4dfs(true);
				stack.push(child);
			}
		}
		
		/// abstract improper node
		List<Node> improperNodes = new LinkedList<Node>();
		List<Edge> improperEdges = new LinkedList<Edge>();
		for(Node node : graph.getNodes()) {
			List<Edge> fromEdges = graph.getEdgesFrom(node);
			if(!node.isAbstractedControl() && fromEdges.size() == 0) {
				List<Edge> toEdges = graph.getEdgesTo(node);
				boolean isImproper = true;
				for(Edge toEdge : toEdges) {
					if(toEdge.hasEvent()) {
						isImproper = false;
					}
				}
				if(isImproper) {
					for(Edge toEdge : toEdges) {
						Node fromNode = graph.getNodeById(toEdge.getFrom());
						fromNode.abstractNode(node, toEdge);
						improperEdges.add(toEdge);
					}
					improperNodes.add(node);
				}
			}
		}
		// abstract improper edge
		for(Edge edge : graph.getEdges()) {
			List<Node> fromNodes = graph.getFromNodes(edge);
			List<Node> toNodes = graph.getToNodes(edge);
			
			if(fromNodes.size() == 1 && toNodes.size() == 1 &&
					!edge.hasEvent() && !edge.hasCond()) {
				Node fromNode = fromNodes.get(0);
				Node toNode = toNodes.get(0);
				
				if(fromNode.isAbstractedControl() && toNode.isAbstractedControl()) {
					continue;
				}
				
				fromNode.abstractNode(toNode, edge);
				improperNodes.add(toNode);
				improperEdges.add(edge);
				for(Edge _edge : graph.getEdgesFrom(toNode)) {
					_edge.setFrom(fromNode.getId());
					if(_edge.getTo() == toNode.getId()) {
						_edge.setTo(fromNode.getId());
					}
				}
				
			}
		}
		// remove
		for(Node node : improperNodes) {
			graph.removeNode(node);
		}
		for(Edge edge : improperEdges) {
			graph.removeEdge(edge);
		}
		
		
		// re-abstract due to improper nodes
		if(0 < improperNodes.size()) {
			this.abst(graph.getRootNode(), graph);
		}
	}
}
