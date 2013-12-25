package jp.mzw.jsanalyzer.verifier;

import java.util.LinkedList;
import java.util.List;

import jp.mzw.jsanalyzer.modeler.model.fsm.FiniteStateMachine;
import jp.mzw.jsanalyzer.modeler.model.fsm.State;
import jp.mzw.jsanalyzer.modeler.model.fsm.Transition;
import jp.mzw.jsanalyzer.modeler.model.interaction.Event;

/**
 * Translates from extracted FSM to promela models
 * @author Yuta Maezawa
 */
public class Translator {
	
	protected FiniteStateMachine mFSM;
	
	public Translator(FiniteStateMachine fsm) {
		this.mFSM = fsm;
	}

	public String translate() {
//		fsm.abstractNoEventEdges();
		
		String ret = "";
		
		ret += "mtype = {\n";
		for(State state : this.mFSM.getStateList()) {
			ret += "\t" + state.getPromelaLabel() + ",\n";
		}
		ret += "\t__exit__\n";
		ret += "};\n\n";
		
		ret += "mtype = {\n";
		for(Event event : this.mFSM.getEventList()) {
			ret += "\t" + event.getId() + ",\n";
		}
		ret += "\t__empty__\n";
		ret += "};\n\n";

		ret += "mtype App_state, App_event, Int_event;\n"; // represents an application-handling event
		ret += "chan App_ch = [0] of { mtype };\n";
		ret += "bool flg_exit = false;\n";
		ret += "\n";
		
		////// Generates an App model
		ret += "active proctype App() {\n";
		ret += translate(this.mFSM.getInitState(), new LinkedList<State>(), "\t");
		
		ret += "goto___exit__:\n";
		ret += "\tflg_exit = true;\n";
		ret += "};\n\n";

		///// Generates an Interaction model
		ret += "active proctype Interaction() {\n";
		ret += "\tdo ::\n";
		ret += "\t\tif\n";
		ret += "\t\t:: flg_exit -> break;\n";
		ret += "\t\t:: else ->\n";
		ret += "\t\t\tif\n";
		for(Event event : this.mFSM.getEventList()) {
			ret += "\t" + event.getId() + ",\n";
			ret += "\t\t\t:: skip -> App_ch!" + event.getId() + ";\n";
		}
		ret += "\t\t\tfi;\n";
		ret += "\t\tfi;\n";
		ret += "\tod;\n";
		ret += "};\n\n";
		
		return ret;
	}
	
	private String translate(State target, List<State> rearched, String indent) {
		String ret = "";
		
		ret += indent + "d_step {\n";
		//ret += indent + "\tevent = __empty__;\n";
		ret += indent + "\tInt_event = __empty__;\n";
		ret += indent + "\tApp_event = __empty__;\n";
		ret += indent + "\tApp_state = " + target.getId() + ";\n";
		ret += indent + "}\n";
		if(!rearched.contains(target)) {
			ret += "goto_" + target.getId() + ":\n";
			rearched.add(target);
		} else {
			ret += indent + "goto goto_" +  target.getId() + ";\n";
			return ret;
		}
		
		List<Transition> targetTransList = this.mFSM.getTransListFrom(target.getId());
		if(0 < targetTransList.size()) {

			List<Transition> eventTransList = new LinkedList<Transition>();
			List<Transition> noEventTransList = new LinkedList<Transition>();
			for(Transition trans : targetTransList) {
				if(trans.hasEvent()) {
					eventTransList.add(trans);
				} else {
					noEventTransList.add(trans);
				}
			}
			
			//if(0 < eventTranss.size()) {
			if(0 < eventTransList.size() || 0 < noEventTransList.size()) {
				ret += indent + "do\n";
				//ret += indent + ":: App_ch?event -> fired_event = event;\n";
				ret += indent + ":: App_ch?Int_event ->\n";
				ret += indent + "\tif\n";
				for (Transition trans : eventTransList) {
					Event event = trans.getEvent();
					ret += indent + "\t:: Int_event == " + event.getId() + " ->\n";
					ret += indent + "\t\tApp_event = " + event.getId() + ";\n";
					if(this.mFSM.isExitState(trans.getToStateId())) {
						ret += indent + "\t\t goto goto___exit__;\n";
					} else {
						ret += translate(this.mFSM.getState(trans.getToStateId()), rearched, indent + "\t\t");
					}
				}
				//ret += indent + "\t:: else -> d_step { event = __empty__; fired_event = __empty__; }\n";

				// no event transitions
				for(Transition trans : noEventTransList) {
					ret += indent + "\t:: skip ->\n";
					if(this.mFSM.isExitState(trans.getToStateId())) {
						ret += indent + "\t\t goto goto___exit__;\n";
					} else {
						ret += translate(this.mFSM.getState(trans.getToStateId()), rearched, indent + "\t\t");
					}
				}
				
				ret += indent + "\t:: else -> Int_event = __empty__;\n";
				ret += indent + "\tfi;\n";
				ret += indent + "od;\n";
			}

		} else {
			ret += indent + "goto goto___exit__;\n";
		}
		
		return ret;
	}
}
