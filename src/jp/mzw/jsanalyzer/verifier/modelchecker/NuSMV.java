package jp.mzw.jsanalyzer.verifier.modelchecker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.mzw.jsanalyzer.config.Command;
import jp.mzw.jsanalyzer.config.FileExtension;
import jp.mzw.jsanalyzer.config.FilePath;
import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.core.examples.*;
import jp.mzw.jsanalyzer.formulator.adp.*;
import jp.mzw.jsanalyzer.formulator.pp.*;
import jp.mzw.jsanalyzer.formulator.property.*;
import jp.mzw.jsanalyzer.serialize.model.Event;
import jp.mzw.jsanalyzer.serialize.model.FiniteStateMachine;
import jp.mzw.jsanalyzer.serialize.model.State;
import jp.mzw.jsanalyzer.serialize.model.State.FuncElement;
import jp.mzw.jsanalyzer.serialize.model.Transition;
import jp.mzw.jsanalyzer.util.CommandLineUtils;
import jp.mzw.jsanalyzer.util.TextFileUtils;
import jp.mzw.jsanalyzer.verifier.specification.Specification;

public class NuSMV extends ModelChecker {
	
	public static void main(String[] args) {
		Analyzer analyzer = new Analyzer(new FileDLerCorrect());
//		Analyzer analyzer = new Analyzer(new FileDLerError());
//		Analyzer analyzer = new Analyzer(new FileDLerRetry());
		
		String objName = analyzer.getProject().getName() + ".fsm" + FileExtension.Serialized;
		Object obj = TextFileUtils.deserialize(analyzer.getProject().getDir(), objName);
		jp.mzw.jsanalyzer.serialize.model.FiniteStateMachine fsm = (jp.mzw.jsanalyzer.serialize.model.FiniteStateMachine)obj;
		

		// For FileDLer only
		List<Property> propList = NuSMV.getFileDLerPropList(analyzer);
		
		NuSMV nusmv = new NuSMV(fsm, analyzer);
		String smvmodel = nusmv.translate();
		nusmv.setSmvModel(smvmodel);
		
		TextFileUtils.write(
				analyzer.getProject().getDir() + FilePath.VerifyResult,
				analyzer.getProject().getName() + FileExtension.SMVModel,
				smvmodel);
		
		/// Verifies
		for(Property prop : propList) {
			try {
				Specification spec = new Specification(prop);			
				nusmv.runNuSMV(spec);
				nusmv.parseSmvResult(spec);

				if(!spec.getSmvResult()) {
					System.out.print("Counterexample: " );
					int step = 0;
					String sh = "#!/bin/sh\n";
					for(String elmId : spec.getCounterexample()) {
						System.out.print(elmId + " -> ");
						String dot = nusmv.visualizeCounterexample(elmId);
						String filename = spec.getId() + ".counteremaple." + (step++) + ".dot";
						TextFileUtils.write("/Users/yuta/Desktop/test2", filename, dot);
						
						sh += "dot -Tpng " + filename + " -o " + filename + ".png\n";
					}
					System.out.println("");
					
					TextFileUtils.write("/Users/yuta/Desktop/test2", spec.getId() + ".dot.sh", sh);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	
	public String visualizeCounterexample(String elementId) {
		String ret = "";
		ret += "digraph FSM {\n";
		for(State state : this.mFSM.getStateList()) {
			ret += state.getId() + " [";
			if(elementId.equals("  app.state = " + state.getId())) {
				ret += "style=filled,fillcolor=red";
			}
			ret += "];\n";
		}
		
		for(Transition trans : this.mFSM.getTransList()) {
			ret += trans.getFromStateId() + " -> " + trans.getToStateId();
			ret += " [";
			if(trans.getEvent() != null) {
				ret += "label=\"" + trans.getEvent().getEvent() + "\"";
				if(elementId.equals("  app.state = " + trans.getEvent().getId())) {
					ret += ",style=bold,color=red";
				}
			}
			ret += "]\n";
		}
		
		ret += "}\n";
		
		return ret;
	}
	
	protected FiniteStateMachine mFSM;
	protected String mSmvModel;
	public NuSMV(FiniteStateMachine fsm, Analyzer analyzer) {
		super(null, analyzer);
		this.mFSM = fsm;
	}
	
	public void setSmvModel(String smv) {
		this.mSmvModel = smv;
	}
	public String getSmvModel() {
		return this.mSmvModel;
	}
	
	public void verify(Specification spec) {
		if(this.mSmvModel == null || "".equals(this.mSmvModel)) {
			return;
		}
		
	}

	public String getSmvFilename(Specification spec) {
		return this.mAnalyzer.getProject().getName() + "." + spec.getId() + FileExtension.SMVModel;
	}
	public String getSmvResultFilename(Specification spec) {
		return this.mAnalyzer.getProject().getName() + "." + spec.getId() + FileExtension.SMVResult;
	}
	/**
	 * 
	 * @param spec
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void runNuSMV(Specification spec) throws IOException, InterruptedException {
		Thread timeout = new CommandLineUtils.TimeoutThread(Thread.currentThread());
		timeout.start();
		
		// Writes SMV file containing this specification
		String content = this.mSmvModel;
		content += "\n";
		content += "SPEC " + spec.getCtlFormula();
		
		String dir = this.mAnalyzer.getProject().getDir() + FilePath.VerifyResult;
		
		TextFileUtils.write(
				dir,
				this.getSmvFilename(spec),
				content);
		
		Process proc = null;
		String[] cmd = { Command.NuSMV, this.getSmvFilename(spec) };
		try {
			proc = Runtime.getRuntime().exec(cmd, null, new File(dir));
			int proc_result = proc.waitFor();
			spec.setGenNCResult(proc_result);
			timeout.interrupt();
		} finally {
			if(proc != null) {
				TextFileUtils.write(dir, this.getSmvResultFilename(spec), CommandLineUtils.readStdOut(proc));
//				System.out.print(CommandLineUtils.readStdOut(proc));
				System.err.print(CommandLineUtils.readStdErr(proc));
				
				proc.getErrorStream().close();
				proc.getInputStream().close();
				proc.getOutputStream().close();
				proc.destroy();
			}
		}
	}
	
	public void parseSmvResult(Specification spec) {
		String dir = this.mAnalyzer.getProject().getDir() + FilePath.VerifyResult;
		String output = TextFileUtils.cat(dir, this.getSmvResultFilename(spec));
		
		/// Find: "-- specification ... is true/false"
		boolean result = true;
		for(String tmp : output.split("\n")) {
			if(tmp.contains("-- specification ")) {
				String[] _tmp = tmp.split(" ");
				String _result = _tmp[_tmp.length-1];
				result = Boolean.parseBoolean(_result);
				break;
			}
		}
		spec.setSmvResult(result);

		/// Find: "-> State: X.X <-"
		int step = 0;
		ArrayList<String> counterexample = new ArrayList<String>();
		for(String tmp : output.split("-> State: ")) {
			if(step++ == 0) {
				/// Output header
				continue;
			}
			for(String _tmp : tmp.split("\n")) {
				if(_tmp.contains("app.state = ")) {
					counterexample.add(_tmp);
				}
			}
		}
		spec.setCounterexmaple(counterexample);
	}
	
	@Override
	public String translate() {
		String ret = "";

		ArrayList<String> userEventIdList = new ArrayList<String>();
		ArrayList<String> serverEventIdList = new ArrayList<String>();
		ArrayList<String> selfEventIdList = new ArrayList<String>();
		for(Transition trans : this.mFSM.getTransList()) {
			if(trans.getEvent() != null) {
				Event event = trans.getEvent();
				
				if(this.mAnalyzer.getRuleManager().isUserInteraction(event.getEvent()) &&
						!userEventIdList.contains(event.getId())) {
					userEventIdList.add(event.getId());
//					System.out.println("UserEv: " + event.getEvent());
				} else if(this.mAnalyzer.getRuleManager().isServerInteraction(event.getEvent()) &&
						!serverEventIdList.contains(event.getId())) {
					serverEventIdList.add(event.getId());
//					System.out.println("ServerEv: " + event.getEvent());
				} else if(!this.mAnalyzer.getRuleManager().isUserInteraction(event.getEvent()) &&
						!this.mAnalyzer.getRuleManager().isServerInteraction(event.getEvent()) &&
						!selfEventIdList.contains(event.getId())) {
					selfEventIdList.add(event.getId());
//					System.out.println("SelfEv: " + event.getEvent());
				}
			}
		}
		
		/// Generates User module
		ret += "MODULE User\n";
		ret += "VAR\n";
		ret += "\tevent : {";
		for(String event : userEventIdList) {
			ret += event + ", ";
		}
		ret += "__empty__};\n";
		ret += "ASSIGN\n";
		ret += "\tinit(event) := __empty__;\n";
		ret += "\tnext(event) := {";
		for(String event : userEventIdList) {
			ret += event + ", ";
		}
		ret += "__empty__};\n";
		
		ret += "\n";
		
		/// Generates Server module
		ret += "MODULE Server\n";
		ret += "VAR\n";
		ret += "\tevent : {";
		for(String event : serverEventIdList) {
			ret += event + ", ";
		}
		ret += "__empty__};\n";
		ret += "ASSIGN\n";
		ret += "\tinit(event) := __empty__;\n";
		ret += "\tnext(event) := {";
		for(String event : serverEventIdList) {
			ret += event + ", ";
		}
		ret += "__empty__};\n";

		ret += "\n";
		
		/// Generates App module
		ret += "MODULE App(user, server)\n";
		ret += "VAR\n";
		ret += "\tstate : {";
		///// States
		ret += "\n";
		ret += "\t\t-- States\n";
		ret += "\t\t";
		for(State state : this.mFSM.getStateList()) {
			ret += state.getId() + ", ";
		}
		ret += "\n";
		ret += "\t\t" + this.mFSM.getExitStateId() + ", -- Exit state ID\n";
		///// Virtual states representing events
		ret += "\t\t-- Virtual user events\n";
		ret += "\t\t";
		for(String event : userEventIdList) {
			ret += event + ", ";
		}
		ret += "\n";
		ret += "\t\t-- Virtual server events\n";
		ret += "\t\t";
		for(String event : serverEventIdList) {
			ret += event + ", ";
		}
		ret += "\n";
		ret += "\t\t-- Virtual self events\n";
		ret += "\t\t";
		for(String event : selfEventIdList) {
			ret += event + ", ";
		}
		ret += "\n";
		ret += "\t\t__empty__};\n";
		/// State transitions
		ret += "ASSIGN\n";
		ret += "\tinit(state) := " + this.mFSM.getRootStateId() + ";\n";
		ret += "\tnext(state) := case\n";
		
		for(State state : this.mFSM.getStateList()) {
			ArrayList<Transition> fromTransList = new ArrayList<Transition>();
			ArrayList<String> fromUserEventIdList = new ArrayList<String>();
			ArrayList<String> fromServerEventIdList = new ArrayList<String>();
			ArrayList<String> fromSelfEventIdList = new ArrayList<String>();
			boolean isBranch = false;
			for(Transition trans : this.mFSM.getTransList()) {
				if(trans.getFromStateId().equals(state.getId())) {
					fromTransList.add(trans);
					
					if(trans.getEvent() == null) {
						isBranch = true;
					} else {
						Event event = trans.getEvent();
						if(this.mAnalyzer.getRuleManager().isUserInteraction(event.getEvent()) &&
								!fromUserEventIdList.contains(event.getId())) {
							fromUserEventIdList.add(event.getId());
						} else if(this.mAnalyzer.getRuleManager().isServerInteraction(event.getEvent()) &&
								!fromServerEventIdList.contains(event.getId())) {
							fromServerEventIdList.add(event.getId());
						} else if(!this.mAnalyzer.getRuleManager().isUserInteraction(event.getEvent()) &&
								!this.mAnalyzer.getRuleManager().isServerInteraction(event.getEvent()) &&
								!fromSelfEventIdList.contains(event.getId())) {
							fromSelfEventIdList.add(event.getId());
						}
					}
					
				}
			}
			
			if(isBranch) {
				ret += "\t\tstate = " + state.getId() + " : {";
				for(int i = 0; i < fromTransList.size()-1; i++) {
					Transition fromTrans = fromTransList.get(i);
					ret += fromTrans.getToStateId() + ", ";
				}
				Transition fromTrans = fromTransList.get(fromTransList.size()-1);
				ret += fromTrans.getToStateId() + "};\n";
			} else {

				for(String eventId : fromUserEventIdList) {
					ret += "\t\tstate = " + state.getId() + " & ";
					ret += "user.event = " + eventId;
					ret += " : ";
					ret += eventId + ";\n";
				}
				
				for(String eventId : fromServerEventIdList) {
					ret += "\t\tstate = " + state.getId() + " & ";
					ret += "server.event = " + eventId;
					ret += " : ";
					ret += eventId + ";\n";
				}
				
				for(String eventId : fromSelfEventIdList) {
					ret += "\t\tstate = " + state.getId();
					ret += " : ";
					ret += "{" + state.getId() + ", " + eventId + "};\n";
				}
			}
		}
		ret += "\t\t-- Event handling\n";
		ArrayList<String> preventDuplicate = new ArrayList<String>();
		for(Transition trans : this.mFSM.getTransList()) {
			if(trans.getEvent() != null &&
					!preventDuplicate.contains(trans.getEvent().getId())) {
				ret += "\t\tstate = " + trans.getEvent().getId() + " : " + trans.getToStateId() + ";\n";
				preventDuplicate.add(trans.getEvent().getId());
			}
		}

		ret += "\t\tTRUE : state;\n";
		ret += "\t\tesac;\n";
		
		ret += "\n";
		
		/// Generates Main module
		ret += "MODULE main\n";
		ret += "VAR\n";
		ret += "\tuser : User;\n";
		ret += "\tserver : Server;\n";
		ret += "\tapp : App(user, server);\n";

		ret += "\n";
		ret += "----- Specifications are below -----\n";
		
		return ret;
	}

	public static List<Property> getFileDLerPropList(Analyzer analyzer) {
		ArrayList<Property> ret = new ArrayList<Property>();
		
		String objName = analyzer.getProject().getName() + ".fsm" + FileExtension.Serialized;
		Object obj = TextFileUtils.deserialize(analyzer.getProject().getDir(), objName);
		jp.mzw.jsanalyzer.serialize.model.FiniteStateMachine fsm = (jp.mzw.jsanalyzer.serialize.model.FiniteStateMachine)obj;
		
		AsyncComm pAsyncComm = new AsyncComm();
		pAsyncComm.setTemplateVariables(NuSMV.genOr(getFuncIdList(fsm, "Ajax.Request")), NuSMV.genOr(getUserEventIdList(fsm, analyzer)));
		ret.add(pAsyncComm);

		ACRetry pACRetry = new ACRetry();
		pACRetry.setTemplateVariables(NuSMV.genOr(getEventIdList(fsm, "onFailure")), NuSMV.genOr(getFuncIdList(fsm, "Ajax.Request")));
		ret.add(pACRetry);
		
		SRWait pSRWait = new SRWait();
		pSRWait.setTemplateVariables(NuSMV.genOr(getFuncIdList(fsm, "inputFormText")), NuSMV.genOr(getEventIdList(fsm, "onSuccess")));
		ret.add(pSRWait);
		
		UEHRegist pUEHRegist = new UEHRegist();
		pUEHRegist.setTemplateVariables(NuSMV.genOr(getUserEventIdList(fsm, analyzer)), NuSMV.genOr(getEventIdList(fsm, "onload")));
		ret.add(pUEHRegist);
		
//		UEHSingle pUEHSingle = new UEHSingle();
//		pUEHSingle.setTemplateVariables(NuSMV.genOr(getFuncIdList(fsm, "addCart")), NuSMV.genOr(getEventIdList(fsm, "onclick")), true);
//		ret.add(pUEHSingle);
		
		Property pUESubmit = new Property(
				"User event and submit",
				"UESubmit",
				"Ajax apps should require explicit user operations before form data is submitted",
				new AjaxDesignPattern(AjaxDesignPattern.Category.Programming, "Explicit Submittion"),
				new Existence(PropertyPattern.Scope.Before),
				PropertyPattern.Scope.Before);
		pUESubmit.setTemplateVariables(NuSMV.genOr(getUserEventIdList(fsm, analyzer)), null, null, NuSMV.genOr(getFuncIdList(fsm, "doSubmit")));
		ret.add(pUESubmit);
		
		return ret;
	}
	
	
	/**
	 * Gets state ID corresponding to guided funtion name
	 * @param fsm Extracted finite state machine that has all states
	 * @param guide Given function name
	 * @return State ID list
	 */
	public static List<String> getFuncIdList(FiniteStateMachine fsm, String guide) {
		ArrayList<String> ret = new ArrayList<String>();
		
		for(State state : fsm.getStateList()) {
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
	public static List<String> getEventIdList(FiniteStateMachine fsm, String guide) {
		ArrayList<String> ret = new ArrayList<String>();
		for(Transition trans : fsm.getTransList()) {
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
	public static List<String> getUserEventIdList(FiniteStateMachine fsm, Analyzer analyzer) {
		ArrayList<String> ret = new ArrayList<String>();
		for(Transition trans : fsm.getTransList()) {
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
	
	/**
	 * Generates an OR expression by using given element IDs
	 * @param elmIdList State/Event ID list
	 * @return Its OR expression
	 */
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
	
}
