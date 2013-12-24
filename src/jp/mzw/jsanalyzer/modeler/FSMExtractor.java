package jp.mzw.jsanalyzer.modeler;

import org.apache.commons.lang3.tuple.Pair;

import jp.mzw.jsanalyzer.config.FilePath;
import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.modeler.model.CallGraph;
import jp.mzw.jsanalyzer.modeler.model.FiniteStateMachine;
import jp.mzw.jsanalyzer.util.TextFileUtils;

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
		
		// Gets extended call graph and enable/disable statements
		FSMExtender extender = new FSMExtender(this.mAnalyzer);
		Pair<CallGraph, EnDisableManager> xcg_ed = extender.createExtendedCallGraph();
		
		TextFileUtils.write(this.mAnalyzer.getProject().getDir() + FilePath.ModelDir, FilePath.ExtendedCallGraph, xcg_ed.getLeft().toDot());
		
		// Abstracts extended call graph
		FSMAbstractor abstractor = new FSMAbstractor(this.mAnalyzer);
		Pair<CallGraph, AbstractionManager> acg_am = abstractor.abst(xcg_ed.getLeft());

		TextFileUtils.write(this.mAnalyzer.getProject().getDir() + FilePath.ModelDir, FilePath.AbstractedCallGraph, acg_am.getLeft().toDot());
		
		// Refines abstracted call graph
		FSMRefiner refiner = new FSMRefiner(this.mAnalyzer);
		FiniteStateMachine fsm = refiner.refine(acg_am.getLeft(), xcg_ed.getRight(), acg_am.getRight());
		
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
