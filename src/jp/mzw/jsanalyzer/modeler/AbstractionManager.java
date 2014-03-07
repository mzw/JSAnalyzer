package jp.mzw.jsanalyzer.modeler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import jp.mzw.jsanalyzer.modeler.model.graph.Edge;
import jp.mzw.jsanalyzer.modeler.model.graph.Node;

public class AbstractionManager {

	HashMap<Node, List<Node>> mAbstractedNodeHash;
	HashMap<Node, List<Edge>> mAbstractedEdgeHash;
	
	public AbstractionManager() {
		this.mAbstractedNodeHash = new HashMap<Node, List<Node>>();
		this.mAbstractedEdgeHash = new HashMap<Node, List<Edge>>();
	}
	
	/**
	 * 
	 * @param abstNodeId
	 * @return
	 * @deprecated
	 */
	public Pair<Node, Node> getConcreteAbstractedNodePair(String abstNodeId) {
		for(Node _node : this.mAbstractedNodeHash.keySet()) {
			if(_node == null) {
				continue;
			}
			for(Node _abstNode : this.mAbstractedNodeHash.get(_node)) {
				if(_abstNode != null && _abstNode.getId().equals(abstNodeId)) {
					return Pair.of(_node, _abstNode);
				}
			}
		}
		return null;
	}
	
	public Node getAbstractedNode(String abstNodeId) {
		for(Node _node : this.mAbstractedNodeHash.keySet()) {
			if(_node == null) {
				continue;
			}
			for(Node _abstNode : this.mAbstractedNodeHash.get(_node)) {
				if(_abstNode != null && _abstNode.getId().equals(abstNodeId)) {
					return _abstNode;
				}
			}
		}
		return null;
	}
	
	public List<Node> getAbstractedNode(Node node) {
		ArrayList<Node> ret = new ArrayList<Node>();
		for(Node _node : this.mAbstractedNodeHash.keySet()) {
			if(_node != null && _node.getId().equals(node.getId())) {
				ret.addAll(this.mAbstractedNodeHash.get(_node));
			}
		}
		return ret;
	}
	
	public void add(Node origin, Node abstNode, Edge abstEdge) {
		// Before: (origin)---abstEdge-->(abstNode)
		// After: (origin)
		
		/// for nodes
		List<Node> abstNodeList = this.mAbstractedNodeHash.get(origin);
		if(abstNodeList == null) {
			abstNodeList = new ArrayList<Node>();
		}
		abstNodeList.add(abstNode);
		
		List<Node> inherit_abstNodeList = this.mAbstractedNodeHash.get(abstNode);
		if(inherit_abstNodeList != null) {
			abstNodeList.addAll(inherit_abstNodeList);
		}
		
		this.mAbstractedNodeHash.put(origin, abstNodeList);
		
		/// for edges
		List<Edge> abstEdgeList = this.mAbstractedEdgeHash.get(origin);
		if(abstEdgeList == null) {
			abstEdgeList = new ArrayList<Edge>();
		}
		abstEdgeList.add(abstEdge);
		
		List<Edge> inherit_abstEdgeList = this.mAbstractedEdgeHash.get(abstNode);
		if(inherit_abstEdgeList != null) {
			abstEdgeList.addAll(inherit_abstEdgeList);
		}
		
		this.mAbstractedEdgeHash.put(origin, abstEdgeList);
	}
	
	/**
	 * 
	 * @param origin
	 * @param abstNode
	 * @param abstEdge
	 * @deprecated
	 */
	public void remove(Node origin, Node abstNode) {
		// Before: (origin)
		// After: (origin)---abstEdge-->(abstNode)

		List<Node> originNodeList = this.mAbstractedNodeHash.get(origin);
		if(originNodeList.contains(abstNode)) {
			originNodeList.remove(abstNode);
		}
		
		// Inherit abstracted nodes and edges
		// to be implemented if necessary
	}
	
	public boolean isControl(Node node) {
		if(node.getNodeType() == Node.Control) {
			return true;
		}
		
		List<Node> abstNodeList = this.mAbstractedNodeHash.get(node);
		if(abstNodeList == null) {
			return false;
		}
		for(Node abstNode : abstNodeList) {
			if(abstNode.getNodeType() == Node.Control) {
				return true;
			}
		}
		return false;
	}
	
}
