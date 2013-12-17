package jp.mzw.jsanalyzer.modeler.model;

import org.mozilla.javascript.ast.AstRoot;

/**
 * Represents an extracted finite state machine based on a call graph.
 * @author Yuta Maezawa
 *
 */
public class FiniteStateMachine extends CallGraph {
	
	protected static final State entry = new State();
}
