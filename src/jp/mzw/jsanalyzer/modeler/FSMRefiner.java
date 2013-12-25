package jp.mzw.jsanalyzer.modeler;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.modeler.model.graph.CallGraph;
import jp.mzw.jsanalyzer.modeler.model.graph.Edge;
import jp.mzw.jsanalyzer.modeler.model.graph.Node;
import jp.mzw.jsanalyzer.modeler.model.interaction.Callback;
import jp.mzw.jsanalyzer.modeler.model.interaction.Event;
import jp.mzw.jsanalyzer.modeler.model.interaction.Interaction;
import jp.mzw.jsanalyzer.modeler.model.fsm.FiniteStateMachine;
import jp.mzw.jsanalyzer.modeler.model.fsm.State;
import jp.mzw.jsanalyzer.modeler.model.fsm.Transition;
import jp.mzw.jsanalyzer.rule.Potential;
import jp.mzw.jsanalyzer.rule.Rule;
import jp.mzw.jsanalyzer.util.StringUtils;

public class FSMRefiner extends Modeler {
	public FSMRefiner(Analyzer analyzer) {
		super(analyzer);
	}
	
	/**
	 * Refines abstracted call graph using en/disabling statements
	 * @param acg Abstracted call graph
	 * @return Refined call graph
	 */
	public FiniteStateMachine refine(CallGraph acg, EnDisableManager edManager, AbstractionManager abstManager) {
		FiniteStateMachine fsm = new FiniteStateMachine();
		

		
		edManager.show();
		
		this.setInteractions(fsm, acg, abstManager);
		
		return fsm;
	}
	
	private void setInteractions(FiniteStateMachine fsm, CallGraph graph, AbstractionManager abstManager) {
		// Sets interactions at each states where Ajax app registers them
		
		// Creates states
		HashMap<Node, State> hashNodeState = new HashMap<Node, State>(); 
		for(Node node : graph.getAllNodeList()) {
			State state = new State(node);
			hashNodeState.put(node, state);
			state.addOriginNodes(abstManager.getAbstractedNode(node));
			fsm.addState(state);
		}

		for(Edge edge : graph.getEdgeList()) {
			Node fromNode = graph.getNode(edge.getFromNodeId(), true);
			Node toNode = graph.getNode(edge.getToNodeId(), true);
			if(edge.hasEvent()) {
				Event event = edge.getEvent();
				Callback callback = new Callback(toNode);
				Interaction interaction = new Interaction(event, callback);
				
				State state = hashNodeState.get(fromNode);
				state.addInteraction(interaction);
			} else if(edge.hasCond()) {
				State fromState = hashNodeState.get(fromNode);
				State toState = hashNodeState.get(toNode);
				
				Transition trans = new Transition(fromState.getId(), toState.getId());
				trans.setCond(edge);
				
				fsm.addTransition(trans);
			} else {
				StringUtils.printError(this, "Invalid abstracted call graph", edge.getDotLabel());
			}
		}
		
		
		// Breadth first search
		for(Node node : graph.getAllNodeList()) {
			node.prepareSearch();
		}
		for(Edge edge : graph.getEdgeList()) {
			Node fromNode = graph.getNode(edge.getFromNodeId(), true);
			Node toNode = graph.getNode(edge.getToNodeId(), true);
			fromNode.addChild(toNode);
		}
		ArrayDeque<Node> queue = new ArrayDeque<Node>(); 
		queue.offer(CallGraph.getInitNode());
		while(!queue.isEmpty()) {
			Node node = queue.poll();
			
			State state = hashNodeState.get(node);
			List<Interaction> interactionList = state.getInteractionList();
			
			for(Node childNode : node.getChildren()) {
				// Propagates already-registered interactions to states
				State childState = hashNodeState.get(childNode);
				for(Interaction interaction : interactionList) {
					
					// to be refactor
					boolean propagate = true;
					Rule rule = interaction.getEvent().getRule();
					if(rule != null && rule instanceof Potential) {
						Potential potential = (Potential)rule;
						if(!potential.getRepeatable()) {
							propagate = false;
						}
					}
					if(propagate) {
						childState.addInteraction(interaction);
					}
					
					
				}
				
				queue.offer(childNode);
			}
		}
		
		// show
		for(State state : fsm.getStateList()) {
			for(Interaction interaction : state.getInteractionList()) {
				Event event = interaction.getEvent();
				Callback callback = interaction.getCallback();
				
				Node cbNode = callback.getNode();
				State cbState = hashNodeState.get(cbNode);
				
				Transition trans = new Transition(state.getId(), cbState.getId());
				trans.setEvent(event);
				fsm.addTransition(trans);
			}
		}
	}
	
	private void determineEnDisable(FiniteStateMachine fsm, CallGraph graph, EnDisableManager edManager) {
		ArrayList<Node> allNodes = new ArrayList<Node>();
		allNodes.add(CallGraph.getInitNode());
		allNodes.addAll(graph.getNodeList());
		
		for(Node node : allNodes) {
			
			State state = new State(node);
			fsm.addState(state);
			
			// Sets interactions at each node
			for(Edge edge : graph.getEdgeList()) {
				if(edge.getFromNodeId() == node.getId() && edge.hasEvent()) {
					Event event = edge.getEvent();
					Callback callback = new Callback(graph.getNode(edge.getToNodeId()));
					Interaction interaction = new Interaction(event, callback);
					
					state.addInteraction(interaction);
				}
			}

//			// Sets enable/disable statements at each node
//			for(EnDisable ed : edManager.getEnDisableList(node.getId())) {
//				state.addEnDisable(ed);
//			}
			
		}
	}
}
