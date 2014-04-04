package jp.mzw.jsanalyzer.serialize.model;

import java.util.ArrayList;
import java.util.List;

import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.serialize.model.State;
import jp.mzw.jsanalyzer.serialize.model.State.FuncElement;
import jp.mzw.jsanalyzer.serialize.model.Transition;

public class FiniteStateMachine extends SerializableElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected ArrayList<State> mStateList;
	protected ArrayList<Transition> mTransList;
	
	protected String mRootStateId;
	protected String mExitStateId;
	
	public FiniteStateMachine(String id) {
		super(id);
		
		this.mStateList = new ArrayList<State>();
		this.mTransList = new ArrayList<Transition>();
	}
	
	public void setRootStateId(String id) {
		this.mRootStateId = id;
	}
	public String getRootStateId() {
		return this.mRootStateId;
	}
	public void setExitStateId(String id) {
		this.mExitStateId = id;
	}
	public String getExitStateId() {
		return this.mExitStateId;
	}
	
	public void addState(State state) {
		this.mStateList.add(state);
	}
	public List<State> getStateList() {
		return this.mStateList;
	}
	
	public void addTrans(Transition trans) {
		this.mTransList.add(trans);
	}
	public List<Transition> getTransList() {
		return this.mTransList;
	}
	
	public String getStateId(String funcname) {
		for(State state : this.mStateList) {
			for(FuncElement funcElm : state.getFuncElement()) {
				if(funcElm.getFuncName().equals(funcname)) {
					return state.getId();
				}
			}
		}
		return null;
	}
	public String getEventId(String eventname, int lineno, int position) {
		for(Transition trans : this.mTransList) {
			if(trans.getEvent() != null) {
				Event event = trans.getEvent();
				if(event.getLineNo() == lineno &&
						event.getPosition() == position &&
						event.getEvent().equals(eventname)) {
					return event.getId();
				}
			}
		}
		
		return null;
	}
	
	
	/////

	/**
	 * Gets state ID corresponding to guided funtion name
	 * @param fsm Extracted finite state machine that has all states
	 * @param guide Given function name
	 * @return State ID list
	 */
	public List<String> getFuncIdList(String guide) {
		ArrayList<String> ret = new ArrayList<String>();
		
		for(State state : this.getStateList()) {
			for(FuncElement func : state.getFuncElement()) {

				/// Requiring user's guide information
				if(guide.equals(func.getFuncName()) &&
						!ret.contains(state.getId())) {
					ret.add(state.getId());
				}
				
			}
		}
		
		return ret;
	}
	

	/**
	 * Gets event ID corresponding to guided event name
	 * @param fsm Extracted finite state machine that has all events
	 * @param guide Given event name
	 * @return Event ID list
	 */
	public List<String> getEventIdList(String guide) {
		ArrayList<String> ret = new ArrayList<String>();
		for(Transition trans : this.getTransList()) {
			Event event = trans.getEvent();
			
			/// Requiring user's guide information
			if(event != null &&
					guide.equals(event.getEvent()) &&
					!ret.contains(event.getId())) {
				ret.add(event.getId());
			}
			
		}
		
		return ret;
	}
	
	/**
	 * Gets user event IDs
	 * @param fsm Extracted finite state machine
	 * @param analyzer Provides the rule manager that determines whether an event is user's one or not
	 * @return User event ID list
	 */
	public List<String> getUserEventIdList(Analyzer analyzer) {
		ArrayList<String> ret = new ArrayList<String>();
		for(Transition trans : this.getTransList()) {
			Event event = trans.getEvent();
			
			/// Requiring user's guide information
			if(event != null &&
					analyzer.getRuleManager().isUserInteraction(event.getEvent()) &&
					!ret.contains(event.getId())) {
				ret.add(event.getId());
			}
			
		}
		
		return ret;
	}
	
	/////
	public void show() {
		System.out.println("=== Serialized/Deserialized finite state machine ===");
		System.out.println("[States]");
		for(State state : this.mStateList) {
			System.out.println("State identified by: " + state.getId());
			for(FuncElement funcElm : state.getFuncElement()) {
				System.out.println("\tFuncs: " + funcElm.getFuncName());
			}
		}
		System.out.println("[Transitions]");
		for(Transition trans : this.mTransList) {
			System.out.println("Transition from/to: " + trans.getFromStateId() + "/" + trans.getToStateId());
			if(trans.getEvent() != null) {
				System.out.println("\t" + trans.getEvent().getEvent());
			}
		}
	}
}