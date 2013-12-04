package jp.mzw.jsanalyzer.modeler;

import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.modeler.model.CallGraph;
import jp.mzw.jsanalyzer.modeler.model.FiniteStateMachine;

public class FSMRefiner extends Modeler {
	public FSMRefiner(Analyzer analyzer) {
		super(analyzer);
	}
	
	/**
	 * Refines abstracted call graph using en/disabling statements
	 * @param acg Abstracted call graph
	 * @return Refined call graph
	 */
	public FiniteStateMachine refine(CallGraph acg) {
		return null;
	}
}
