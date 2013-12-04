package jp.mzw.jsanalyzer.modeler;

import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.modeler.model.CallGraph;
import jp.mzw.jsanalyzer.modeler.model.FiniteStateMachine;

/**
 * Extracts finite state machines
 * @author Yuta Maezawa
 *
 */
public class FSMExtractor extends Modeler {
	
	
	/**
	 * Constructor
	 * @param analyzer gives all information of this project
	 */
	public FSMExtractor(Analyzer analyzer) {
		super(analyzer);
	}
	
	/**
	 * A elapsed time for extracting finite state machine
	 */
	protected long mExtractTime;
	
	/**
	 * Extracts a finite state machine
	 * @return An extracted finite state machine
	 */
	public FiniteStateMachine extracts() {
		long start = System.currentTimeMillis();
		/// start
		
		// Gets extended call graph
		FSMExtender extender = new FSMExtender(this.mAnalyzer);
		CallGraph xcg = extender.createExtendedCallGraph();
		
		// Abstracts extended call graph
		FSMAbstractor abstractor = new FSMAbstractor(this.mAnalyzer);
		CallGraph acg = abstractor.abst(xcg);
		
		// Refines abstracted call graph
		FSMRefiner refiner = new FSMRefiner(this.mAnalyzer);
		FiniteStateMachine fsm = refiner.refine(acg);
		
		/// end
		long end = System.currentTimeMillis();
		this.mExtractTime = end - start;
		
		// Finally returns finite state machine
		// based on refined call graph
		return fsm;
	}
	
	/**
	 * Gets an extract time in millisecond
	 * @return An elapsed time for extracting finite state machine
	 */
	public long getExtractTime() {
		return this.mExtractTime;
	}
}
