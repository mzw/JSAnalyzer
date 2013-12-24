package jp.mzw.jsanalyzer.modeler.model;

import java.util.ArrayList;


/**
 * Represents an extracted finite state machine based on a call graph.
 * @author Yuta Maezawa
 *
 */
public class FiniteStateMachine extends CallGraph {

//	protected static final State entry = new State();
//	protected static final State exit = new State();

	protected ArrayList<State> mStateList;
	protected ArrayList<Transition> mTransList;
	
	public FiniteStateMachine() {
		super();
		
		this.mStateList = new ArrayList<State>();
		this.mTransList = new ArrayList<Transition>();
	}
	
	public void addState(State state) {
		this.mStateList.add(state);
	}
	
//	public void addExit(State fromState) {
//		Transition trans = new Transition(fromState.getId(), exit.getId());
//		this.transList.add(trans);
//	}
}
