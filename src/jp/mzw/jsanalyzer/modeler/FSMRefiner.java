package jp.mzw.jsanalyzer.modeler;

import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.modeler.model.CallGraph;
import jp.mzw.jsanalyzer.modeler.model.Edge;
import jp.mzw.jsanalyzer.modeler.model.FiniteStateMachine;
import jp.mzw.jsanalyzer.modeler.model.Node;
import jp.mzw.jsanalyzer.modeler.model.State;

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
		
		this.search(fsm, acg);
		
		return fsm;
	}
	
	private void search(FiniteStateMachine fsm, CallGraph graph) {
		for(Node node : graph.getNodeList()) {
			
			State state = new State(node);
			
			for(Edge edge : graph.getEdgeList()) {
				if(edge.getFromNodeId() == node.getId()) {
					
				}
			}
		}
	}
}
