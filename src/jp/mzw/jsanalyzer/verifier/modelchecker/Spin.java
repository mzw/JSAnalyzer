package jp.mzw.jsanalyzer.verifier.modelchecker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.FunctionNode;

import jp.mzw.jsanalyzer.config.Command;
import jp.mzw.jsanalyzer.config.FileExtension;
import jp.mzw.jsanalyzer.config.FilePath;
import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.modeler.model.Element;
import jp.mzw.jsanalyzer.modeler.model.fsm.FiniteStateMachine;
import jp.mzw.jsanalyzer.modeler.model.fsm.State;
import jp.mzw.jsanalyzer.modeler.model.fsm.Transition;
import jp.mzw.jsanalyzer.modeler.model.graph.Node;
import jp.mzw.jsanalyzer.modeler.model.interaction.Event;
import jp.mzw.jsanalyzer.util.CommandLineUtils;
import jp.mzw.jsanalyzer.util.StringUtils;
import jp.mzw.jsanalyzer.util.TextFileUtils;
import jp.mzw.jsanalyzer.verifier.specification.Specification;

public class Spin extends ModelChecker {
	
	protected String mBaseDir;
	protected String mBaseName;
	
	public Spin(FiniteStateMachine fsm, Analyzer analyzer) {
		super(fsm, analyzer);
		
		this.mBaseDir = analyzer.getProject().getDir() + FilePath.VerifyResult;
		this.mBaseName = analyzer.getProject().getName();
		
	}
	
	protected String getPromelaFilename() {
		return this.mBaseName + FileExtension.Promela;
	}
	protected String getNCFilename(Specification spec) {
		return this.mBaseName + "." + spec.getId() + FileExtension.NeverClaim;
	}
	protected String getLTLFilename(Specification spec) {
		return this.mBaseName + "." + spec.getId() + FileExtension.LTLProperty;
	}
	protected String getSpinResultFilename(Specification spec) {
		return this.mBaseName + "." + spec.getId() + FileExtension.SpinResult;
	}
	protected String getRawTrailFilename(Specification spec) {
		return this.getPromelaFilename() + FileExtension.Trail;
	}
	protected String getTrailFilename(Specification spec) {
		return this.mBaseName + "." + spec.getId() + FileExtension.Trail;
	}
	protected String getTrailResultFilename(Specification spec) {
		return this.mBaseName + "." + spec.getId() + FileExtension.TrailResult;
	}
	
	
	///// verify
	public void verify(Specification spec) {
		if((this.mBaseDir == null) || (this.mBaseName == null)) {
			System.err.println("Not yet translate");
		}

		try {
			this.execTranslateNeverClaim(spec);
			this.execSpin(spec);
			this.execGcc(spec);
			this.execPan(spec);

			if(spec.getIsFault()) {
//				System.out.println(spec.getId() + ": " + spec.getFormula());
//				System.err.println("---> fault!");
//				System.err.println("An error-prone execution path:");
				this.revealErrorProneExecutionPath(spec);
			} else {
//				System.out.println(spec.getId() + ": " + spec.getFormula());
//				System.out.println("---> satisfied");
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			System.err.println("Cannot execute a spin: " + spec.getDescription());
			System.exit(-1);
		}
	}

	/////
	private void execTranslateNeverClaim(Specification spec) throws IOException, InterruptedException {
		Process proc = null;
		TextFileUtils.write(this.mBaseDir, this.getLTLFilename(spec), spec.getFormula());
		String[] cmd = { Command.Spin, "-F", this.getLTLFilename(spec) };
		try {
			proc = Runtime.getRuntime().exec(cmd, null, new File(this.mBaseDir));
			int proc_result = proc.waitFor();
			spec.setGenNCResult(proc_result);
		} finally {
			if(proc != null) {
				TextFileUtils.write(this.mBaseDir, this.getNCFilename(spec), CommandLineUtils.readStdOut(proc));
//				System.out.print(Static.readStdOut(proc));
				System.err.print(CommandLineUtils.readStdErr(proc));
				
				proc.getErrorStream().close();
				proc.getInputStream().close();
				proc.getOutputStream().close();
				proc.destroy();
			}
		}
	}
	
	/////
	private void execSpin(Specification spec) throws IOException, InterruptedException {
		Process proc = null;
		String[] cmd = { Command.Spin, "-a", "-N", this.getNCFilename(spec), this.getPromelaFilename() };
		try {
			proc = Runtime.getRuntime().exec(cmd, null, new File(this.mBaseDir));
			int proc_result = proc.waitFor();
			spec.setSpinResult(proc_result);
		} finally {
			if(proc != null) {
				//for(int i = 0; i < cmd.length; i++) System.out.print(cmd[i] + " "); System.out.println("");
				
//				System.out.print(Static.readStdOut(proc));
				System.err.print(CommandLineUtils.readStdErr(proc));
				
				proc.getErrorStream().close();
				proc.getInputStream().close();
				proc.getOutputStream().close();
				proc.destroy();
			}
		}	
	}
	
	/////
	private void execGcc(Specification spec) throws IOException, InterruptedException {
		Process proc = null;
		String[] cmd = { Command.Gcc, "-o", "pan", "pan.c" };
//		String[] cmd = { Command.Gcc, "-DSAFETY", "-DREACH", "-o", "pan", "pan.c" };
		try {
			proc = Runtime.getRuntime().exec(cmd, null, new File(this.mBaseDir));
			int proc_result = proc.waitFor();
			spec.setGccResult(proc_result);
		} finally {
			if(proc != null) {
//				System.out.print(Static.readStdOut(proc));
				System.err.print(CommandLineUtils.readStdErr(proc));
				
				proc.getErrorStream().close();
				proc.getInputStream().close();
				proc.getOutputStream().close();
				proc.destroy();
			}
		}
	}
	
	/////
	private void execPan(Specification spec) throws IOException, InterruptedException {
		Process proc = null;
		String[] cmd = { "./pan", "-a" };
//		String[] cmd = { "./pan", };
		try {
			proc = Runtime.getRuntime().exec(cmd, null, new File(this.mBaseDir));
			int proc_result = proc.waitFor();
			spec.setPanResult(proc_result);
		} finally {
			if(proc != null) {
//				System.out.print(Util.readStdOut(proc));
				System.err.print(CommandLineUtils.readStdErr(proc));
				
				String result = CommandLineUtils.readStdOut(proc);
				spec.setIsFault(this.isFault(result));
				TextFileUtils.write(this.mBaseDir, this.getSpinResultFilename(spec), result);
				
				proc.getErrorStream().close();
				proc.getInputStream().close();
				proc.getOutputStream().close();
				proc.destroy();
			}
		}
	}

	private boolean isFault(String result) {
		ArrayList<String> errors = TextFileUtils.grep(result, ".*errors:.*");
		if(errors.size() != 1) {
			System.err.println("Spin result is defect: \n" + result);
			System.exit(-1);
		}
		String error = errors.get(0);
		String[] split_error = error.split(" ");
		for(int i = 0 ; i < split_error.length - 1; i++) {
			if(split_error[i].equals("errors:")) {
				int errorNum = Integer.parseInt(split_error[i+1]);
				if(0 < errorNum) {
					return true;
				}
			}
		}
		return false;
	}
	
	/////
	private void revealErrorProneExecutionPath(Specification spec) throws IOException, InterruptedException {
		this.saveTrail(spec);
		this.getErrorPronePathFromTrail(spec);
	}
	private void saveTrail(Specification spec) throws IOException, InterruptedException {
		String[] cmd_mv = { "mv", this.getRawTrailFilename(spec),  this.getTrailFilename(spec)};
		CommandLineUtils.exec(this.mBaseDir, cmd_mv);
	}

	private String getErrorPronePathFromTrail(Specification spec) throws IOException, InterruptedException {
		String ret = "";

		Process proc = null;
		String[] cmd = { Command.Spin, "-p", "-g", "-l", "-r", "-s", "-k", this.getTrailFilename(spec), this.getPromelaFilename() };
		try {
			proc = Runtime.getRuntime().exec(cmd, null, new File(this.mBaseDir));
			//proc.waitFor();
		} finally {
//			System.out.print(Util.readStdOut(proc));
//			System.err.print(Util.readStdErr(proc)); // comment out 20130501
			
			String result = CommandLineUtils.readStdOut(proc);
			TextFileUtils.write(this.mBaseDir, this.getTrailResultFilename(spec), result);
			
			// error-prone execution path
			ArrayList<State> epStateList = new ArrayList<State>();
			ArrayList<Transition> epTransList = new ArrayList<Transition>();

			ArrayList<State> pre_ass = null;
			
			
			String anti_example = "";
			ArrayList<Element> antiExmapleList = new ArrayList<Element>();
			
			/// Parses trail result
			String[] lines = result.split(System.getProperty("line.separator"));
			for (String line: lines) {
				if(line.contains("[App_state")) {
					/// Finds [App_state = XXX]
					int beginIndex = line.indexOf("[");
					int endIndex = line.indexOf("]");
					String target = line.substring(beginIndex+1, endIndex);
					/// Determines App_state
					String[] target_split = target.split("=");
					String App_state = target_split[1].replaceAll(" ", "");

					
//					State state = this.mFSM.getState(App_state); // App_state is state id
//					if(state != null) {
//						anti_example += "(" + state.getId() + ")";
//					}
					anti_example += "(" + App_state + ")";
					
//					if(original.hasStateForSpin(App_state)) {
//						//System.out.println("App is in: " + App_state);
//						//System.out.println("(" + App_state + ")");
//						anti_example += "(" + App_state + ")";
//						//ep_states.addAll(abstractedStates);
//						List<State> abstractedStates = this.sm.getErrorProneStatesForSpin(App_state);
//						for(State as : abstractedStates) {
//							if(!epStateList.contains(as)) {
//								epStateList.add(as);
//							}
//						}
//						pre_ass = abstractedStates;
//						//ep_transs.addAll(sm.getErrorProneTransitionsForSpin(abstractedStates));
//						List<Transition> abstractedTranss = this.sm.getErrorProneTransitionsForSpin(abstractedStates);
//						for(Transition at : abstractedTranss) {
//							if(!epTransList.contains(at)) {
//								epTransList.add(at);
//							}
//						}
//					}
				} else if(line.contains("[App_event")) {
					/// Finds [App_event = XXX]
					int beginIndex = line.indexOf("[");
					int endIndex = line.indexOf("]");
					String target = line.substring(beginIndex+1, endIndex);
					/// Determines App_event
					String[] target_split = target.split("=");
					String App_event = target_split[1].replaceAll(" ", "");
					
					anti_example += "--" + App_event + "-->";

//					if(this.sm.hasEventForSpin(App_handling_event) && !"__empty__".equals(App_handling_event)) {
//						//System.out.println("App handles: " + App_handling_event);
//						//System.out.println("--" + App_handling_event + "-->");
//						anti_example += "--" + App_handling_event + "-->";
//						Transition t = original.getErrorProneTransitionForSpin(App_handling_event, pre_ass);
//						if(!epTransList.contains(t)) {
//							epTransList.add(t);
//						}
//					} else {
//						//System.out.println("does not have: " + App_handling_event);
//					}
				}
//				else if(line.contains("\t\tInt_event")) {
//					String[] line_split = line.split("=");
//					String fired_event = line_split[1].replaceAll(" ", "");
//					if(this.sm.hasEventForSpin(fired_event)) {
//						//System.out.println("\tfired(random): " + fired_event);
//					} else {
//						//System.out.println("does not have: " + App_handling_event);
//					}
//				}
			}
			spec.setAntiExample(anti_example);
//			System.out.println(anti_example);
			
			proc.getErrorStream().close();
			proc.getInputStream().close();
			proc.getOutputStream().close();
			proc.destroy();
			
		}
		
		return ret;
	}
	
	
	
	public void savePromela(String promela) {
		TextFileUtils.write(mBaseDir, this.getPromelaFilename(), promela);
	}
	
	
	@Override
	public String translate() {
		if(this.mFSM == null) {
			StringUtils.printError(this, "this.mFSM is null at Spin#translate");
		}
		

		/// Prevents duplicate
		ArrayList<Event> eventList = new ArrayList<Event>();
		for(Event event : this.mFSM.getEventList()) {
			boolean exist = false;
			for(Event _event : eventList) {
				if(event.getId().equals(_event.getId())) {
					exist = true;
					break;
				}
			}
			if(!exist) {
				eventList.add(event);
			}
		}
		
		
//		fsm.abstractNoEventEdges();
		
		String ret = "";
		
		ret += "mtype = {\n";
		for(State state : this.mFSM.getStateList()) {
			ret += "\t" + state.getId() + ",";
			/// Node abstracted functions
			ret += " /* ";
			for(FuncInfo funcInfo : this.getAbstFuncList(state)) {
				ret += funcInfo.getFuncName() + "(" + funcInfo.getLineNo() + ", " + funcInfo.getCharNo() + ")" + ", ";
			}
			ret += " */";
			///
			ret += "\n";
		}
		ret += "\t__exit__\n";
		ret += "};\n\n";
		
		ret += "mtype = {\n";
		for(Event event : eventList) {
			ret += "\t" + event.getId() + ",";
			/// Event
			ret += "/* ";
			ret += this.getEventInfo(event);
			ret += " */";
			///
			ret += "\n";
		}
		ret += "\t__empty__\n";
		ret += "};\n\n";

		ret += "mtype App_state, App_event, Int_event;\n"; /// Variables in verification formulas
		ret += "chan App_ch = [0] of { mtype };\n";
		ret += "bool flg_exit = false;\n";
		ret += "\n";
		
		////// Generates an App model
		ret += "active proctype App() {\n";
		ret += translate(this.mFSM.getInitState(), new ArrayList<State>(), "\t");
		
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
		for(Event event : eventList) {
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

			List<Transition> eventTransList = new ArrayList<Transition>();
			List<Transition> noEventTransList = new ArrayList<Transition>();
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
	
	private List<FuncInfo> getAbstFuncList(State state) {
		ArrayList<FuncInfo> ret = new ArrayList<FuncInfo>();
		
		for(Node node : state.getOriginNodeList()) {
			FuncInfo funcInfo = null;
			if(node.getAstNode() instanceof FunctionNode) {
				funcInfo = new FuncInfo(node.getAstNode(), ((FunctionNode)node.getAstNode()).getName());
			} else if(node.getAstNode() instanceof FunctionCall) {
				funcInfo = new FuncInfo(node.getAstNode(), ((FunctionCall)node.getAstNode()).getTarget().toSource());
			}
			
			if(funcInfo != null) {
				boolean exist = false;
				for(FuncInfo _funcInfo : ret) {
					if(funcInfo.equals(_funcInfo)) {
						exist = true;
						break;
					}
				}
				if(!exist) {
					ret.add(funcInfo);
				}
			}
		}		
		return ret;
	}
	
	private String getEventInfo(Event event) {
		String ret = "";
		
		ret += event.getEvent();
		
		if(event.getEventObj() == null) {
			
		} else if (event.getEventObj() instanceof org.jsoup.nodes.Element) {
			org.jsoup.nodes.Element elm = (org.jsoup.nodes.Element)event.getEventObj();
			ret += "@id=" + elm.attr("id");
		} else if (event.getEventObj() instanceof AstNode) {
			AstNode astNode = (AstNode)event.getEventObj();
			ret += " (@lineno=" + astNode.getLineno() + ", @position=" + astNode.getPosition() + ")";
		} 
		return ret;
	}
	
	private class FuncInfo {
		private AstNode mAstNode;
		private String mFuncName;
		int charno = -1;
		int lineno = -1;
		private FuncInfo(AstNode astNode, String funcname) {
			mAstNode = astNode;
			mFuncName = funcname;
			charno = astNode.getPosition();
			lineno = astNode.getLineno();
		}
		
		public String getFuncName() {
			return mFuncName;
		}
		public int getCharNo() {
			return charno;
		}
		public int getLineNo() {
			return lineno;
		}
		
		public boolean equals(FuncInfo funcInfo) {
			return funcInfo.mAstNode.equals(mAstNode);
		}
	}

}
