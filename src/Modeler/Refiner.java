package Modeler;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import Parser.DOM;
import Rule.RuleList;
import Graph.Edge;
import Graph.Graph;
import Graph.Node;

public class Refiner extends Modeler {
	
	private DOM dom = null;
	private RuleList rules = null;
	
	List<Node> visit = null;
	private HashMap<Node, List<Interaction>> hash_Node_Interactions = null;
	List<Pair> addIntrs = null;
	List<Pair> rmIntrs = null;

	public Refiner(Graph graph, DOM dom, RuleList rules) {
		super(graph);
		this.dom = dom;
		this.rules = rules;
		
		this.visit = new LinkedList<Node>();
		this.hash_Node_Interactions = new HashMap<Node, List<Interaction>>();
	}
	
	public void refine() {

		this.hash_Node_Interactions = new HashMap<Node, List<Interaction>>();
		this.setInteractions(this.graph.getRootNode(), new LinkedList<Interaction>());

		this.visit = new LinkedList<Node>();
		this.addIntrs = new LinkedList<Pair>();
		this.rmIntrs = new LinkedList<Pair>();
		this.refine(this.graph.getRootNode(), this.dom);
		this.addrmIntr();
		this.rmUnreachable();
		this.addExit();
	}
	
	private void addExit() {
		List<Node> exitNodes = new LinkedList<Node>();
		
		for(Node node : this.graph.getNodes()) {

			List<Edge> edges = this.graph.getEdgesFrom(node);
			if(edges.size() == 0) {
				exitNodes.add(node);
			} else {
				
				boolean hasEvent = false;
				boolean allPotential = true;
				for(Edge edge : edges) {
					if(edge.hasEvent()) {
						hasEvent = true;
						Interaction intr = edge.getInteraction();
						if(!intr.isPotential(this.rules) || edge.getFrom() != edge.getTo()) {
							allPotential = false;
						}
					}
				}
				if(hasEvent && allPotential) {
					exitNodes.add(node);
				}
			}
			
		}
		
		for(Node node : exitNodes) {
			this.graph.setExitNode(node.getId(), true);
		}
	}
	
	private void rmUnreachable() {

		List<Node> rmNodes = new LinkedList<Node>();
		List<Edge> rmEdges = new LinkedList<Edge>();
		
		for(Node node : this.graph.getNodes()) {
			if(node.getId() != this.graph.getRootNodeId() &&
					this.graph.getEdgesTo(node).size() == 0) {
				List<Edge> edges = this.graph.getEdgesFrom(node);
				for(Edge edge : edges) {
					rmEdges.add(edge);
				}
				rmNodes.add(node);
			}
		}
		
		if(0 < rmNodes.size()) {
			for(Node node : rmNodes) {
				this.graph.removeNode(node);
			}
			for(Edge edge : rmEdges) {
				this.graph.removeEdge(edge);
			}
			this.rmUnreachable();
		}
	}
	
	private void addrmIntr() {
		for(Pair add : this.addIntrs) {
			Node fromNode = add.getNode();
			Interaction intr = add.getInteraction();
			
			Node toNode = this.graph.getNodeById(intr.getOriginalEdge().getTo());
			
			int edgeId = this.graph.addEdge(fromNode.getId(), toNode.getId());
			if(intr.fromHTML()) {
				this.graph.setEvent(edgeId, intr.getElm(), intr.getAttr());
			} else if(intr.fromJS()) {
				this.graph.setEvent(edgeId, intr.getTargetAstNode(), intr.getEventAstNode());
			} else {
				System.err.println("Unknown from interaction@Refiner.addemIntr: ");
			}
		}
		

		for(Pair rm : this.rmIntrs) {
			Node fromNode = rm.getNode();
			Interaction intr = rm.getInteraction();
			
			List<Edge> edges = this.graph.getEdgesFrom(fromNode);
			for(Edge edge : edges) {
				if(edge.getEventAstNode() == intr.getEventAstNode()) {
					this.graph.removeEdge(edge);
					break;
				}
			}
			
		}
		
	}
	
	///// enabling/disabling interactions
	private void refine(Node curNode, DOM preDom) {
		
		//System.out.println("Node: " + curNode.getName());
		
		/// visit only one time
		if(this.visit.contains(curNode)) {
			return;
		} else {
			this.visit.add(curNode);
		}
		
		/// manipulate
		DOM curDom = preDom.clone();
		curDom.manipulate(curNode, this);
		
		/// If no edge, go to next nodes
		boolean hasEventEdge = false;
		for(Edge edge : this.graph.getEdgesFrom(curNode)) {
			if(edge.hasEvent()) {
				hasEventEdge = true;
			}
		}
		if(!hasEventEdge) {
			for(Edge edge : this.graph.getEdgesFrom(curNode)) {
				Node nextNode = this.graph.getNodeById(edge.getTo());
				this.refine(nextNode, curDom);
			}
		}
		
		/// evaluate interactions
		for(Interaction intr : this.hash_Node_Interactions.get(curNode)) {
			boolean disabled = intr.isDisabled(curNode, curDom, this, this.rules);

			Node toNode = this.graph.getNodeById(intr.getOriginalEdge().getTo());
			List<Edge> edges= this.graph.getEdges(curNode, toNode);
			
			boolean exist = false;
			for(Edge edge : edges) {
				if(edge.getEventAstNode() == intr.getEventAstNode()) {
					exist = true;
					break;
				}
			}
			
			if((hasEventEdge || this.graph.getEdgesFrom(curNode).size() == 0) && // skip branch nodes
					!exist && !disabled) {
				Pair addIntr = new Pair(curNode, intr);
				this.addIntrs.add(addIntr);
				
			} else if((hasEventEdge || this.graph.getEdgesFrom(curNode).size() == 0) && // skip branch nodes
					exist && disabled) {
				Pair rmIntr = new Pair(curNode, intr);
				this.rmIntrs.add(rmIntr);
			}
			
		}
		
		/// go to next nodes
		for(Edge edge : this.graph.getEdgesFrom(curNode)) {
			Node nextNode = this.graph.getNodeById(edge.getTo());
			
			this.refine(nextNode, curDom);
			
		}
		
	}
	
	///// set interactions on each node
	private void setInteractions(Node curNode, List<Interaction> preIntrs) {
		
		List<Interaction> curIntrs = this.hash_Node_Interactions.get(curNode);
		if(curIntrs == null) {
			curIntrs = new LinkedList<Interaction>();
			this.addInteractions(curIntrs, preIntrs);
			
			for(Edge edge : this.graph.getEdgesFrom(curNode)) {
				if(edge.hasEvent()) {
					this.addInteraction(curIntrs, edge.getInteraction());
				}
			}

			this.hash_Node_Interactions.put(curNode, curIntrs);
			

			for(Edge edge : this.graph.getEdgesFrom(curNode)) {
				Node nextNode = this.graph.getNodeById(edge.getTo());
				this.setInteractions(nextNode, curIntrs);
			}
		} else {

			for(Edge edge : this.graph.getEdgesFrom(curNode)) {
				Node nextNode = this.graph.getNodeById(edge.getTo());
				List<Interaction> nextIntrs = this.hash_Node_Interactions.get(nextNode);
				
				if(nextIntrs == null) {
					this.setInteractions(nextNode, curIntrs);
				} else {

					if(!this.sameInteractions(curNode, nextNode)) {
						this.addInteractions(nextIntrs, curIntrs);
						this.hash_Node_Interactions.put(nextNode, nextIntrs);
						this.setInteractions(nextNode, curIntrs);
					}
					
				}
			}
		}
		
	}
	

	public boolean sameInteractions(Node node, Node nextNode) {
		List<Interaction> nextIntrs = this.hash_Node_Interactions.get(nextNode);
		if(nextIntrs == null) {
			return false;
		}
		
		List<Interaction> intrs = this.hash_Node_Interactions.get(node);
		if(intrs.size() != nextIntrs.size()) {
			return false;
		}
		
		for(Interaction intr : intrs) {
			boolean same = false;
			for(Interaction nextIntr : nextIntrs) {
				if(intr.equals(nextIntr)) {
					same = true;
					break;
				}
			}
			if(!same) {
				return false;
			}
		}
		
		return true;
	}

	public void addInteraction(List<Interaction> interactions, Interaction interaction) {
		boolean isSame = false;
		for(Interaction _interaction : interactions) {
			if(_interaction.equals(interaction)) {
				isSame = true;
				break;
			}
		}
		if(!isSame) {
			interactions.add(interaction);
		}
	}
	
	public void addInteractions(List<Interaction> dst, List<Interaction> src) {
		for(Interaction _src : src) {
			this.addInteraction(dst, _src);
		}
	}
	
	///
	private class Pair {
		private Node node = null;
		private Interaction intr = null;
		private Pair(Node node, Interaction intr) {
			this.node = node;
			this.intr = intr;
		}
		public Node getNode() {
			return this.node;
		}
		public Interaction getInteraction() {
			return this.intr;
		}
	}
}
