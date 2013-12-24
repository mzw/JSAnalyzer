package jp.mzw.jsanalyzer.modeler.model.fsm;

import java.util.ArrayList;
import java.util.List;

import jp.mzw.jsanalyzer.modeler.model.graph.CallGraph;
import jp.mzw.jsanalyzer.modeler.model.interaction.Event;


/**
 * Represents an extracted finite state machine based on a call graph.
 * @author Yuta Maezawa
 *
 */
public class FiniteStateMachine extends CallGraph {

//	protected static final State entry = new State();
//	protected static final State exit = new State();
	
	protected Entry mEntry;
	protected Exit mExit;
	protected State mInitState;

	protected ArrayList<State> mStateList;
	protected ArrayList<Transition> mTransList;
	
	public FiniteStateMachine() {
		super();
		
		this.mStateList = new ArrayList<State>();
		this.mTransList = new ArrayList<Transition>();
		
		this.mEntry = new Entry();
		this.mExit = new Exit();
		
		this.mInitState = new State(CallGraph.getInitNode());
	}
	
	
	public void show() {
		for(State state : this.mStateList) {
			System.out.println(state.getDotLabel());
		}
	}

	///// Booleans
	public boolean isExitState(String stateId) {
		if(this.mExit.getId().equals(stateId)) {
			return true;
		}
		return false;
	}
	///// Setters
	public void addState(State state) {
		this.mStateList.add(state);
	}
	
	public void addExit(State fromState) {
		Transition trans = new Transition(fromState.getId(), mExit.getId());
		this.mTransList.add(trans);
	}
	///// Getters
	/**
	 * Gets this initial state;
	 * @return
	 */
	public State getInitState() {
		return this.mInitState;
	}
	/**
	 * Gets a state whose id value is given
	 * @param id Given id value
	 * @return Returns null if no corresponding state
	 */
	public State getState(String id) {
		for(State state : this.mStateList) {
			if(state.getId().equals(id)) {
				return state;
			}
		}
		return null;
	}
	/**
	 * Gets all events on this transitions
	 * @return All events
	 */
	public List<Event> getEventList() {
		List<Event> ret = new ArrayList<Event>();
		for(Transition trans : this.mTransList) {
			if(trans.hasEvent()) {
				ret.add(trans.getEvent());
			}
		}
		return ret;
	}
	/**
	 * Gets transitions which come from given state
	 * @param fromStateId Given state's ID
	 * @return Transitions which come from given state
	 */
	public List<Transition> getTransListFrom(String fromStateId) {
		List<Transition> ret = new ArrayList<Transition>();
		for(Transition trans : this.mTransList) {
			if(trans.getFromStateId().equals(fromStateId)) {
				ret.add(trans);
			}
		}
		return ret;
	}
	
	/**
	 * Gets all states
	 * @return All states
	 */
	public List<State> getStateList() {
		return this.mStateList;
	}
	
	///// toStrings
	/**
	 * Gets a string representing this finite state machine contents in DOT format
	 * @return Dot-formatted string
	 */
	@Override
	public String toDot() {
		String ret = "digraph FSM {\n";

//		ret += "size=\"15.0,10.0\";\n";
//		ret += "ratio=\"0.75\";\n";

		ret += this.mEntry.getId() + this.mEntry.getDotLabel() + ";\n";
		ret += this.mExit.getId() + this.mExit.getDotLabel() + ";\n";
		
		for(State state : this.mStateList) {
			ret += state.getId() + state.getDotLabel() + ";\n";
		}
		
		for(Transition trans : this.mTransList) {
			ret += trans.getFromStateId() + " -> " + trans.getToStateId() + trans.getDotLabel() + ";\n";
		}
		
		ret += "}\n";
		
		return ret;
	}
}
