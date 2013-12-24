package jp.mzw.jsanalyzer.modeler;

import java.util.ArrayList;

import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.modeler.model.graph.CallGraph;
import jp.mzw.jsanalyzer.modeler.model.graph.Edge;
import jp.mzw.jsanalyzer.modeler.model.graph.Node;
import jp.mzw.jsanalyzer.modeler.model.interaction.Callback;
import jp.mzw.jsanalyzer.modeler.model.interaction.Event;
import jp.mzw.jsanalyzer.modeler.model.interaction.Interaction;
import jp.mzw.jsanalyzer.modeler.model.fsm.FiniteStateMachine;
import jp.mzw.jsanalyzer.modeler.model.fsm.State;

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
		

		ArrayList<Node> allNodes = new ArrayList<Node>();
		allNodes.add(CallGraph.getInitNode());
		allNodes.addAll(acg.getNodeList());
		
		for(Node node : allNodes) {
			State state = new State(node);
			fsm.addState(state);

			// Sets initial interactions at each node
			for(Edge edge : acg.getEdgeList()) {
				if(edge.getFromNodeId() == node.getId() && edge.hasEvent()) {
					Event event = edge.getEvent();
					Callback callback = new Callback(acg.getNode(edge.getToNodeId()));
					Interaction interaction = new Interaction(event, callback);
					
					state.addInteraction(interaction);
				}
			}
			// Sets enable/disable statements at each node
			for(EnDisable ed : edManager.getEnDisableList(node)) {
				state.addEnDisable(ed);
			}
		}
		
		return fsm;
	}
	
	private void search(FiniteStateMachine fsm, CallGraph graph, EnDisableManager edManager) {
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
			// Sets enable/disable statements at each node
			
		}
	}
}
