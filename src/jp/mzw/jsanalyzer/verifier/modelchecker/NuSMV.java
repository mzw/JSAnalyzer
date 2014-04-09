package jp.mzw.jsanalyzer.verifier.modelchecker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.mzw.jsanalyzer.config.Command;
import jp.mzw.jsanalyzer.config.FileExtension;
import jp.mzw.jsanalyzer.config.FilePath;
import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.core.IdGen;
import jp.mzw.jsanalyzer.serialize.model.Event;
import jp.mzw.jsanalyzer.serialize.model.FiniteStateMachine;
import jp.mzw.jsanalyzer.serialize.model.State;
import jp.mzw.jsanalyzer.serialize.model.Transition;
import jp.mzw.jsanalyzer.util.CommandLineUtils;
import jp.mzw.jsanalyzer.util.TextFileUtils;
import jp.mzw.jsanalyzer.verifier.specification.Specification;

public class NuSMV extends ModelChecker {
	
	/**
	 * Extracted (and serialized) finite state machine
	 * These (non-serialized and serialized) models should be uniformed.
	 */
	protected FiniteStateMachine mFSM;
	
	/**
	 * Translated SMV model in String
	 */
	protected String mSmvModel;
	
	/**
	 * Constructor
	 * @param fsm Extracted (and serialized) finite state machine
	 * @param analyzer Containts this project information
	 */
	public NuSMV(FiniteStateMachine fsm, Analyzer analyzer) {
		super(null, analyzer); // Should accept non-serialized FSM in future
		this.mFSM = fsm;
		
		this.mSmvModel = this.translate();
		
		/// Saves SMV model
		TextFileUtils.write(
				this.mAnalyzer.getProject().getDir() + FilePath.VerifyResult,
				this.mAnalyzer.getProject().getName() + FileExtension.SMVModel,
				this.mSmvModel);
	}
	
	/**
	 * Generates a DOT source where a given state is highlighted
	 * @param stateId Represents a state to be highlighted
	 * @return The DOT source
	 */
	private String visualizeCounterexample(String stateId) {
		String ret = "";
		ret += "digraph FSM {\n";
		for(State state : this.mFSM.getStateList()) {
			ret += state.getId() + " [";
			if(stateId.equals(state.getId())) {
				ret += "style=filled,fillcolor=red";
			}
			ret += "];\n";
		}
		
		for(Transition trans : this.mFSM.getTransList()) {
			ret += trans.getFromStateId() + " -> " + trans.getToStateId();
			ret += " [";
			if(trans.getEvent() != null) {
				ret += "label=\"" + trans.getEvent().getEvent() + "\"";
			}
			ret += "]\n";
		}
		
		ret += "}\n";
		
		return ret;
	}

	/**
	 * Generates a DOT source where a given event is highlighted
	 * @param eventId Represents a event to be highlighted
	 * @param fromStateId Specifies its from state because 1..many edge(s) has the event identified by the same event ID
	 * @return The DOT source
	 */
	private String visualizeCounterexample(String eventId, String fromStateId) {
		String ret = "";
		ret += "digraph FSM {\n";
		for(State state : this.mFSM.getStateList()) {
			ret += state.getId() + ";\n";
		}
		
		for(Transition trans : this.mFSM.getTransList()) {
			ret += trans.getFromStateId() + " -> " + trans.getToStateId();
			ret += " [";
			if(trans.getEvent() != null) {
				ret += "label=\"" + trans.getEvent().getEvent() + "\"";
				if(eventId.equals(trans.getEvent().getId()) && trans.getFromStateId().equals(fromStateId)) {
					ret += ",style=bold,color=red";
				}
			}
			ret += "]\n";
		}
		
		ret += "}\n";
		
		return ret;
	}
	
	
	/**
	 * Executes NuSMV
	 * @param spec Given specication (based on Ajax design patterns)
	 */
	public void verify(Specification spec) {
		if(this.mSmvModel == null || "".equals(this.mSmvModel)) {
			return;
		}
		
		try {
			this.runNuSMV(spec);
			this.parseSmvResult(spec);

			if(!spec.getSmvResult()) {
				String dir = this.mAnalyzer.getProject().getDir() + FilePath.VerifyResult + FilePath.Counterexample;
//				System.out.print("Counterexample: " );
				int step = 0;
				String sh = "#!" + Command.Bin + "\n";
				
				String fromStateId = this.mFSM.getRootStateId();
				for(String elmId : spec.getCounterexample()) {
//					System.out.print(elmId + " -> ");
					
					boolean isState = false;
					for(State state : this.mFSM.getStateList()) {
						if(state.getId().equals(elmId)) {
							fromStateId = state.getId();
							isState = true;
							break;
						}
					}
					
					String dot = "";
					if(isState) {
						dot = this.visualizeCounterexample(elmId);
					} else {
						dot = this.visualizeCounterexample(elmId, fromStateId);
					}
					
					String filename = this.mAnalyzer.getProject().getName() + ".spec." + spec.getId() + ".step." + (step++) + FileExtension.Dot;
					TextFileUtils.write(dir, filename, dot);
					
					sh += Command.Dot + " -Tpng " + filename + " -o " + filename + ".png\n";
				}
//				System.out.println("");
				
				String dotFilename = this.mAnalyzer.getProject().getName() + ".spec." + spec.getId() + ".sh";
				TextFileUtils.write(dir, dotFilename, sh);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private String getSmvFilename(Specification spec) {
		return this.mAnalyzer.getProject().getName() + "." + spec.getId() + FileExtension.SMVModel;
	}
	private String getSmvResultFilename(Specification spec) {
		return this.mAnalyzer.getProject().getName() + "." + spec.getId() + FileExtension.SMVResult;
	}
	

	/**
	 * Executes NuSMV
	 * @param spec Given specication (based on Ajax design patterns)
	 * @throws IOException Writes SMV model with this specification in local file syste
	 * @throws InterruptedException For timeout
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
	
	/**
	 * Parses outputs of the verification results
	 * @param spec Used for identifying the verification results
	 */
	private void parseSmvResult(Specification spec) {
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
					/// "  app.state = JSAnalyzerID"
					String elmId = _tmp.split(" = ")[1];
					counterexample.add(elmId);
				}
			}
		}
		spec.setCounterexmaple(counterexample);
	}
	
	@Override
	/**
	 * Translates the extracted finite state machine to its SMV model
	 */
	public String translate() {
		String ret = "";
		

		ArrayList<String> eventIdList = new ArrayList<String>();
		for(Transition trans : this.mFSM.getTransList()) {
			if(trans.getEvent() != null) {
				Event event = trans.getEvent();
				if(!eventIdList.contains(event.getId())) {
					eventIdList.add(event.getId());
				}
			}
		}
		
		
		/// Generates App module
		ret += "MODULE App\n";
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
		///// Virtual events
		ret += "\t\t-- Virtual events\n";
		ret += "\t\t";
		for(String event : eventIdList) {
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
			boolean isBranch = false;
			for(Transition trans : this.mFSM.getTransList()) {
				if(trans.getFromStateId().equals(state.getId())) {
					fromTransList.add(trans);
					
					if(trans.getEvent() == null) {
						isBranch = true;
					}
				}
			}
			
			if(isBranch) {
				ret += "\t\tstate = " + state.getId() + " : {";
				int size = fromTransList.size();
				for(int i = 0; i < fromTransList.size()-1; i++) {
					ret += fromTransList.get(i).getToStateId() + ", ";
				}
				ret += fromTransList.get(size-1).getToStateId();
				ret += "};\n";
			} else {
				ret += "\t\tstate = " + state.getId() + " : {";
				for(Transition trans : fromTransList) {
					ret += trans.getEvent().getId() + ", ";
				}
				ret += state.getId() + "};\n";
				for(Transition trans : fromTransList) {
					ret += "\t\tstate = " + trans.getEvent().getId() + " : " + trans.getToStateId() + ";\n";
				}
			}
		}

		ret += "\t\tTRUE : state;\n";
		ret += "\t\tesac;\n";
		
		
		/// Generates Main module
		ret += "MODULE main\n";
		ret += "VAR\n";
		ret += "\tapp : App;\n";		

		ret += "\n";
		ret += "----- Specifications are below -----\n";
		
		return ret;
	}
	
	/**
	 * Impropert implementation
	 * @param tbd To be debugged
	 * @return
	 * @deprecated
	 */
	public String translate(int tbd) {
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
	
	/**
	 * 
	 * @param expr
	 * @return
	 * @deprecated
	 */
	public static String rev_genOr(String expr) {
		String ret = IdGen.ID_PREFIX;
		int index = expr.indexOf(IdGen.ID_PREFIX);
		for(int i = index + IdGen.ID_PREFIX.length(); i < expr.length(); i++) {
			char c = expr.charAt(i);
			if(Character.isDigit(c)) {
				ret += expr.charAt(i);
			} else {
				break;
			}
		}
		return ret;
	}
	
}
