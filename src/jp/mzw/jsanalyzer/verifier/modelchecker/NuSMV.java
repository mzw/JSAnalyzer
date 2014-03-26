package jp.mzw.jsanalyzer.verifier.modelchecker;

import java.util.ArrayList;
import java.util.List;

import jp.mzw.jsanalyzer.config.FileExtension;
import jp.mzw.jsanalyzer.config.FilePath;
import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.core.examples.*;
import jp.mzw.jsanalyzer.formulator.property.*;
import jp.mzw.jsanalyzer.modeler.model.fsm.FiniteStateMachine;
import jp.mzw.jsanalyzer.util.TextFileUtils;

public class NuSMV extends ModelChecker {

	public NuSMV(FiniteStateMachine fsm, Analyzer analyzer) {
		super(fsm, analyzer);
	}
	
	public static void main(String[] args) {
		Analyzer analyzer = new Analyzer(new FileDLerCorrect());
//		Analyzer analyzer = new Analyzer(new FileDLerError());
//		Analyzer analyzer = new Analyzer(new FileDLerRetry());
		
		String objName = analyzer.getProject().getName() + ".fsm" + FileExtension.Serialized;
		Object obj = TextFileUtils.deserialize(analyzer.getProject().getDir(), objName);
		jp.mzw.jsanalyzer.serialize.model.FiniteStateMachine fsm = (jp.mzw.jsanalyzer.serialize.model.FiniteStateMachine)obj;
		
		
		
		ArrayList<String> asyncCommFuncIdList = new ArrayList<String>();
		for(jp.mzw.jsanalyzer.serialize.model.State state : fsm.getStateList()) {
			for(jp.mzw.jsanalyzer.serialize.model.State.FuncElement func : state.getFuncElement()) {
				
				/// asynchronous communication states
				/// To be modified
				if("Ajax.Request".equals(func.getFuncName()) &&
						!asyncCommFuncIdList.contains(state.getId())) {
					asyncCommFuncIdList.add(state.getId());
				}
				
				
			}

			
		}
		
		
		ArrayList<String> userEventIdList = new ArrayList<String>();
		ArrayList<String> failEventIdList = new ArrayList<String>();
		for(jp.mzw.jsanalyzer.serialize.model.Transition trans : fsm.getTransList()) {
			jp.mzw.jsanalyzer.serialize.model.Event event = trans.getEvent();
			
			// user events
			if(event != null &&
					analyzer.getRuleManager().isUserInteraction(event.getEvent()) &&
					!userEventIdList.contains(event.getId())) {
				userEventIdList.add(event.getId());
			}
			
			// async comm failure events
			/// To be modified
			if(event != null &&
					"onFailure".equals(event.getEvent()) &&
					!userEventIdList.contains(event.getId())) {
				failEventIdList.add(event.getId());
			}
		}
		
		
		
		
//		Property p = new AsynchronousCommunication();
		AsynchronousCommunication pAsyncComm = new AsynchronousCommunication();
		System.out.println("AsyncComm: " +  pAsyncComm.genCTLFormula(NuSMV.genOr(asyncCommFuncIdList), NuSMV.genOr(userEventIdList)));

		AsynchronousCommunicationRetry pACRetry = new AsynchronousCommunicationRetry();
		System.out.println("ACRetry: " +  pACRetry.genCTLFormula(NuSMV.genOr(failEventIdList), NuSMV.genOr(asyncCommFuncIdList)));
	}
	
	
	public static String genOr(List<String> elmIdList) {
		if(elmIdList == null || elmIdList.size() == 0) {
			return "TRUE";
		}
		
		String ret = "";
		
		ret += "(";
		for(int i = 0; i< elmIdList.size() - 1; i++) {
			ret += "app.state = " + elmIdList.get(i) + " | ";
		}
		ret += "app.state = " + elmIdList.get(elmIdList.size()-1);
		ret += ")";
		
		return ret;
	}
	
	public static void _main(String[] args) {
		Analyzer analyzer = new Analyzer(new FileDLerCorrect());
//		Analyzer analyzer = new Analyzer(new FileDLerError());
//		Analyzer analyzer = new Analyzer(new FileDLerRetry());
		
		String objName = analyzer.getProject().getName() + ".fsm" + FileExtension.Serialized;
		Object obj = TextFileUtils.deserialize(analyzer.getProject().getDir(), objName);
		jp.mzw.jsanalyzer.serialize.model.FiniteStateMachine fsm = (jp.mzw.jsanalyzer.serialize.model.FiniteStateMachine)obj;
		
		TextFileUtils.write(
				analyzer.getProject().getDir() + FilePath.VerifyResult,
				analyzer.getProject().getName() + FileExtension.SMVModel,
				NuSMV.translate(fsm));
		
//		System.out.println(NuSMV.translate(fsm));
	}

	/**
	 * Translate a SMV model from a serialized finite state machine
	 * @param fsm Serialized finite state machine
	 * @return SMV model
	 */
	public static String translate(jp.mzw.jsanalyzer.serialize.model.FiniteStateMachine fsm) {
		String smv = "";

		/// Prevents duplicate
		ArrayList<jp.mzw.jsanalyzer.serialize.model.Event> _eventList = new ArrayList<jp.mzw.jsanalyzer.serialize.model.Event>();
		for(jp.mzw.jsanalyzer.serialize.model.Transition trans : fsm.getTransList()) {
			if(trans.getEvent() == null) {
				continue;
			}
			_eventList.add(trans.getEvent());
		}
		ArrayList<jp.mzw.jsanalyzer.serialize.model.Event> eventList = new ArrayList<jp.mzw.jsanalyzer.serialize.model.Event>();
		for(jp.mzw.jsanalyzer.serialize.model.Event _event : _eventList) {
			boolean exit = false;
			for(jp.mzw.jsanalyzer.serialize.model.Event event : eventList) {
				if(event.getId().equals(_event.getId())) {
					exit = true;
					break;
				}
			}
			if(!exit) {
				eventList.add(_event);
			}
		}
		// eventList: a list of events without any duplications
		
		
		
		smv += "MODULE App" + "\n";
		smv += "\n";
		smv += "VAR" + "\n";
		
		/// App.state: Represents application states/events
		smv += "\t" + "state : {";
		for(jp.mzw.jsanalyzer.serialize.model.State state : fsm.getStateList()) {
			smv += state.getId() + ", ";
		}
		smv += fsm.getExitStateId() + "," + "\n";
		smv += "\t\t";
		for(jp.mzw.jsanalyzer.serialize.model.Event event : eventList) {
			smv += event.getId() + ", ";
		}
		smv += "__empty__};" + "\n";
		smv += "\n";
		
		/// State transitions
		smv += "ASSIGN\n";
		smv += "\t" + "init(state) := " + fsm.getRootStateId() + ";" + "\n";
		// State changes
		smv += "\t" + "next(state) := case" + "\n";
		for(jp.mzw.jsanalyzer.serialize.model.State state : fsm.getStateList()) {
			ArrayList<jp.mzw.jsanalyzer.serialize.model.Transition> fromTransList = new ArrayList<jp.mzw.jsanalyzer.serialize.model.Transition>();
			for(jp.mzw.jsanalyzer.serialize.model.Transition trans : fsm.getTransList()) {
				if(trans.getFromStateId().equals(state.getId())) {
					fromTransList.add(trans);
				}
			}
			// branch node
			boolean isBranch = true;
			for(jp.mzw.jsanalyzer.serialize.model.Transition fromTrans : fromTransList) {
				if(fromTrans.getEvent() != null) {
					isBranch = false;
					break;
				}
			}
			
			
			if(isBranch) {
				smv += "\t\t" + "state = " + state.getId() + " : {";
				for(jp.mzw.jsanalyzer.serialize.model.Transition fromTrans : fromTransList) {
					smv += fromTrans.getToStateId() + ", "; // App changes its state
				}
				smv += state.getId() + "};" + "\n";
			} else {
				smv += "\t\t" + "state = " + state.getId() + " : {";
				for(jp.mzw.jsanalyzer.serialize.model.Transition fromTrans : fromTransList) {
					smv += fromTrans.getEvent().getId() + ", "; // App handles event
				}
				smv += state.getId() + "};\n"; // Or stay
				for(jp.mzw.jsanalyzer.serialize.model.Transition fromTrans : fromTransList) {
					smv += "\t\t" + "state = " + fromTrans.getEvent().getId() + " : " + fromTrans.getToStateId() + ";\n"; // When handling, app changes its state
				}
			}
			
			
		}
		smv += "\t\t" + "TRUE : state;" + "\n";
		smv += "\t\t" + "esac;" + "\n";
		
		/// Main module
		smv += "-- Main module\n";
		smv += "MODULE main" + "\n";
		smv += "VAR" + "\n";
		smv += "app : App;" + "\n";
		smv += "\n";
		smv += "-- Specifications are below\n";
		
		return smv;
	}
}
