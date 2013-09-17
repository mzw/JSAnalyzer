package Modeler;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import Graph.Edge;
import Graph.Graph;
import Graph.Node;
import Rule.Potential;
import Rule.Rule;

public class Extender extends Modeler {

	public Extender(Graph graph) {
		super(graph);
	}
	
	public void extend() {

		this.relateCallerCallee();
		
		this.init4dfs();
		this.extend(this.graph.getRootNode(), this.graph);
	}

	private void relateCallerCallee() {
		for(Node node : this.graph.getNodes()) {
			org.mozilla.javascript.ast.AstNode astNode = node.getAstNode();
			if(astNode instanceof org.mozilla.javascript.ast.FunctionCall) {
				
				Node funcCallNameNode = this.getFuncCallNameNode(node);
				Node funcNode = this.getFuncNodeByName(funcCallNameNode.toSource());
				if(funcNode != null) {
					
					this.graph.addEdge(node.getId(), funcNode.getId());
					node.addChild(funcNode);
					
				} else {
					// embedded or library
					//System.err.println("Embedded?@Graph.relateCallerCallee: " + funcCallNameNode.toSource());
				}
			}
		}
	}
	
	private void extend(Node start, Graph graph) {
		List<Node> callbacks = new LinkedList<Node>();
		
		Stack<Node> stack = new Stack<Node>();
		stack.push(start);
		while(!stack.isEmpty()) {
			Node node = stack.peek();
			Node child = node.findUnvisitedChild();
			if(child == null) {
				node = stack.pop();
				
				if(node.isTrigger()) {
					Node triggerSetNode = this.getTriggerSetNode(node);
					Node objNode = this.getObjNode(node);
					Node cbNode = this.getCallbackNode(node);
					
					Node _cbNode = this.getFuncNode(cbNode);
					int edgeId = -1;
					if(cbNode.getId() != _cbNode.getId()) {
						edgeId = graph.addEdge(triggerSetNode.getId(), _cbNode.getId());
						triggerSetNode.addChild(_cbNode);
						callbacks.add(_cbNode);
					} else {
						Edge edge = graph.getEdge(triggerSetNode, cbNode);
						edgeId = edge.getId();
					}
					graph.setEvent(edgeId, objNode.getAstNode(), node.getAstNode());
					
				} else if(node.isPotential()) {

					Potential rule = node.getPotential();
					Node funcCallNode = this.getFuncCallNode(node);
					
					// to be debugged
					if(funcCallNode == null) {
						continue;
					}
					
					Node objNode = this.getObjNode(node);
					if(objNode == null) {
						objNode = funcCallNode;
					}
					
					Node eventNode = null;
					String eventLabel = "";
					if(!Rule.containArg(rule.getEvent())) {
						eventNode = node;
						eventLabel = rule.getEvent();
					} else {
						int eventArgNum = Rule.getArgNum(rule.getEvent());
						eventNode = this.getArgNode(funcCallNode, eventArgNum);
						eventLabel = rule.getEvent().replace("arg_" + eventArgNum, eventNode.toSource());
					}
					
					int edgeId = -1;
					if(!Rule.containArg(rule.getCallback())) {
						if("ret".equals(rule.getCallback())) { // alert
							Node retNode = this.getRetNode(node);

							//edgeId = graph.addEdge(funcCallNode.getId(), retNode.getId());
							edgeId = graph.getEdgeId(retNode.getAstNode(), funcCallNode.getAstNode());
						} else if("prop".equals(rule.getCallback())) {
							// NOP
							// Trigger
							continue;
						} else {
							System.out.println("To be implemented@Extender.extend.potential: " + rule.getCallback());
						}
					} else {
						int cbArgNum = Rule.getArgNum(rule.getCallback());
						Node cbNode = this.getArgNode(funcCallNode, cbArgNum);
						
						if(cbNode != null) {
							Node _cbNode = this.getFuncNode(cbNode);
							if(_cbNode != null) {
								if(cbNode.getId() != _cbNode.getId()) {
									edgeId = graph.addEdge(funcCallNode.getId(), _cbNode.getId());
									callbacks.add(_cbNode);
								} else {
									Edge edge = graph.getEdge(cbNode.getParent(), cbNode);
									edgeId = edge.getId();
								}
							} else {
								// cannot solve callback node due to jQuery? <-- object.event( not function but just variable )
								continue;
							}
						} else {
							// cannot solve callback node due to jQuery? <-- { object.event } and { 'foo' + object.event + 'bar' }
							continue;
						}
					}
					graph.setEvent(edgeId, objNode.getAstNode(), eventNode.getAstNode(), eventLabel);
				}
			} else {
				child.setVisit4dfs(true);
				stack.push(child);
			}
		}
		
		for(Node node : callbacks) {
			this.extend(node, graph);
		}
	}


	/*
	public void propargate(Node start) { // depth first
		this.init4dfs();
		Stack<Node> stack = new Stack<Node>();
		stack.push(start);
		int order = 1;
		while(!stack.isEmpty()) {
			Node node = stack.peek();

			Node child = node.findUnvisitedChild();
			if(child == null) {
				///
				Node target = node;
				target.setOrder4dfs(order++);
				if(target.isSpecial() && target.getParent() != null) {
					boolean propagate = true;
					for(Node neighbor : target.getParent().getChildren()) {
						if(target.getId() != neighbor.getId()) {
							if(neighbor.isSpecial()) {
								propagate = false;
								break;
							}
						}
					}
					if(propagate) {
						target.getParent().propagateSpecial(target);
					}
				}
				///
				
				node = stack.pop();
			} else {
				child.setVisit4dfs(true);
				stack.push(child);
			}
		}
		
	}
	*/
}
