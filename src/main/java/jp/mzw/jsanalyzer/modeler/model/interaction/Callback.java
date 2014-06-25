package jp.mzw.jsanalyzer.modeler.model.interaction;

import jp.mzw.jsanalyzer.modeler.model.fsm.State;


public class Callback {
	
	protected State mCallbackState;
	
	public Callback(State callback) {
		this.mCallbackState = callback;
	}
	
	public State getState() {
		return this.mCallbackState;
	}

//	public boolean isSame(Callback callback) {
//		return false;
//	}
}
