package jp.mzw.jsanalyzer.modeler;

import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.modeler.model.FiniteStateMachine;

public class FSMRefiner extends Modeler {
	public FSMRefiner(Analyzer analyzer) {
		super(analyzer);
	}
	
	/**
	 * To be implemented
	 * @return A refined FSM
	 */
	public FiniteStateMachine refine() {
		return null;
	}
}
