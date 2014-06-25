package jp.mzw.jsanalyzer.verifier.modelchecker;

import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.modeler.model.fsm.FiniteStateMachine;

public class ModelChecker {

	/**
	 * Extracted finite state machine by using JSModeler
	 */
	protected FiniteStateMachine mFSM;
	/**
	 * Provides this project information
	 */
	protected Analyzer mAnalyzer;
	
	/**
	 * Constructor
	 * @param analyzer Contains this project information
	 */
	public ModelChecker(FiniteStateMachine fsm, Analyzer analyzer) {
		this.mFSM = fsm;
		this.mAnalyzer = analyzer;
	}
	
	/**
	 * Should override in concrete model checker classes
	 * @return
	 */
	public String translate() {
		return null;
	}
	
}
