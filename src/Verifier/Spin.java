package Verifier;

import Analyzer.Analyzer;
import Analyzer.Config;
import Analyzer.Util;
import Graph.State;
import Graph.StateMachine;
import Graph.Transition;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Spin extends Verifier {
	public Spin(StateMachine sm) {
		super(sm);
	}
	
	private String dir = null;
	private String filename = null;
	private String promela_filename = null;
	private Analyzer analyzer = null;
	public Spin(StateMachine sm, Analyzer analyzer) {
		super(sm);
		this.analyzer = analyzer;
		this.dir = analyzer.getProject().getDir();
		this.filename = analyzer.getProject().getName();
		
		this.promela_filename = this.filename + Config.EXT_Promela;
	}
	
	///// verify
	public void verify(Specification spec) {
		if((this.dir == null) || (this.filename == null)) {
			System.err.println("Not yet translate");
		}

		try {
			this.execTranslateNeverClaim(spec);
			this.execSpin(spec);
			this.execGcc(spec);
			this.execPan(spec);

			if(spec.getIsFault()) {
				//System.out.println(spec.getId() + ": " + spec.getFormula());
				//System.err.println("---> fault!");
				//System.err.println("An error-prone execution path:");
				this.revealErrorProneExecutionPath(spec);
			} else {
				//System.out.println(spec.getId() + ": " + spec.getFormula());
				//System.out.println("---> satisfied");
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
		String ltl_filename = this.promela_filename + "." + spec.getId() + Config.EXT_NeverClaim;
		String spec_filename = this.promela_filename + "." + spec.getId() + Config.EXT_LTLProperty;
		Util.write(this.dir, spec_filename, spec.getFormula());
		String[] cmd = { Config.spin, "-F", spec_filename };
		try {
			proc = Runtime.getRuntime().exec(cmd, null, new File(this.dir));
			int proc_result = proc.waitFor();
			spec.setGenNCResult(proc_result);
		} finally {
			if(proc != null) {
				Util.write(this.dir, ltl_filename, Util.readStdOut(proc));
				//System.out.print(Static.readStdOut(proc));
				System.err.print(Util.readStdErr(proc));
				
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
		String ltl_filename = this.promela_filename + "." + spec.getId() + Config.EXT_NeverClaim;
		String[] cmd = { Config.spin, "-a", "-N", ltl_filename, this.promela_filename };
		try {
			proc = Runtime.getRuntime().exec(cmd, null, new File(this.dir));
			int proc_result = proc.waitFor();
			spec.setSpinResult(proc_result);
		} finally {
			if(proc != null) {
				//for(int i = 0; i < cmd.length; i++) System.out.print(cmd[i] + " "); System.out.println("");
				
				//System.out.print(Static.readStdOut(proc));
				System.err.print(Util.readStdErr(proc));
				
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
		String[] cmd = { Config.gcc, "-o", "pan", "pan.c" };
		try {
			proc = Runtime.getRuntime().exec(cmd, null, new File(this.dir));
			int proc_result = proc.waitFor();
			spec.setGccResult(proc_result);
		} finally {
			if(proc != null) {
				//System.out.print(Static.readStdOut(proc));
				System.err.print(Util.readStdErr(proc));
				
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
		try {
			proc = Runtime.getRuntime().exec(cmd, null, new File(this.dir));
			int proc_result = proc.waitFor();
			spec.setPanResult(proc_result);
		} finally {
			if(proc != null) {
				//System.out.print(Util.readStdOut(proc));
				System.err.print(Util.readStdErr(proc));
				
				String result = Util.readStdOut(proc);
				spec.setIsFault(this.isFault(result));
				Util.write(this.dir, this.promela_filename + "." + spec.getId() + Config.EXT_SpinResult, result);
				
				proc.getErrorStream().close();
				proc.getInputStream().close();
				proc.getOutputStream().close();
				proc.destroy();
			}
		}
	}

	private boolean isFault(String result) {
		List<String> errors = Util.grep(result, ".*errors:.*");
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
		String trail_filename = this.promela_filename + "." + spec.getId() + Config.EXT_Trail;
		String[] cmd_mv = { "mv", this.promela_filename + ".trail",  trail_filename};
		Util.exec(this.dir, cmd_mv);
	}

	private String getErrorPronePathFromTrail(Specification spec) throws IOException, InterruptedException {
		String ret = "";

		Process proc = null;
		String[] cmd = { Config.spin, "-p", "-g", "-l", "-r", "-s", "-k", this.getTrailFilename(spec), this.promela_filename };
		try {
			proc = Runtime.getRuntime().exec(cmd, null, new File(this.dir));
			//proc.waitFor();
		} finally {
			//System.out.print(Util.readStdOut(proc));
			//System.err.print(Util.readStdErr(proc)); // comment out 20130501
			
			String result = Util.readStdOut(proc);
			Util.write(this.dir, this.getTrailFilename(spec) + Config.EXT_TrailResult, result);
			
			// error-prone execution path
			List<State> ep_states = new LinkedList<State>();
			List<Transition> ep_transs = new LinkedList<Transition>();

			List<State> pre_ass = null;
			
			//StateMachine original = this.read(this.dir, this.sm.getFilename());
			StateMachine original = StateMachine.read(this.dir + Util.SYSTEM_DIR + this.filename + Config.EXT_StateMachine);
			
			String anti_example = "";
			// parse trail result
			String[] lines = result.split(System.lineSeparator());
			for (String line: lines) {
				//if(line.contains("\t\tApp_state")) {
				if(line.contains("[App_state")) {
					//String[] line_split = line.split("=");
					//String App_state = line_split[1].replaceAll(" ", "");

					// parse of [App_state = XXX]
					int beginIndex = line.indexOf("[");
					int endIndex = line.indexOf("]");
					String target = line.substring(beginIndex+1, endIndex);

					String[] target_split = target.split("=");
					String App_state = target_split[1].replaceAll(" ", "");

					if(original.hasStateForSpin(App_state)) {
						//System.out.println("App is in: " + App_state);
						//System.out.println("(" + App_state + ")");
						anti_example += "(" + App_state + ")";
						//ep_states.addAll(abstractedStates);
						List<State> abstractedStates = this.sm.getErrorProneStatesForSpin(App_state);
						for(State as : abstractedStates) {
							if(!ep_states.contains(as)) {
								ep_states.add(as);
							}
						}
						pre_ass = abstractedStates;
						//ep_transs.addAll(sm.getErrorProneTransitionsForSpin(abstractedStates));
						List<Transition> abstractedTranss = this.sm.getErrorProneTransitionsForSpin(abstractedStates);
						for(Transition at : abstractedTranss) {
							if(!ep_transs.contains(at)) {
								ep_transs.add(at);
							}
						}
					}
				} else if(line.contains("[App_event")) {
				//} else if(line.contains("\t\tApp_event")) {
					//String[] line_split = line.split("=");
					//String App_handling_event = line_split[1].replaceAll(" ", "");

					// parse of [App_event = XXX]
					int beginIndex = line.indexOf("[");
					int endIndex = line.indexOf("]");
					String target = line.substring(beginIndex+1, endIndex);

					String[] target_split = target.split("=");
					String App_handling_event = target_split[1].replaceAll(" ", "");

					if(this.sm.hasEventForSpin(App_handling_event) && !"__empty__".equals(App_handling_event)) {
						//System.out.println("App handles: " + App_handling_event);
						//System.out.println("--" + App_handling_event + "-->");
						anti_example += "--" + App_handling_event + "-->";
						Transition t = original.getErrorProneTransitionForSpin(App_handling_event, pre_ass);
						if(!ep_transs.contains(t)) {
							ep_transs.add(t);
						}
					} else {
						//System.out.println("does not have: " + App_handling_event);
					}
				} else if(line.contains("\t\tInt_event")) {
					String[] line_split = line.split("=");
					String fired_event = line_split[1].replaceAll(" ", "");
					if(this.sm.hasEventForSpin(fired_event)) {
						//System.out.println("\tfired(random): " + fired_event);
					} else {
						//System.out.println("does not have: " + App_handling_event);
					}
				}
			}
			spec.setAntiExample(anti_example);
			//System.out.println(result);
			//StateMachine ep_sm = new StateMachine();
			//ep_sm.createStateMachineFromStatesAndTransitions(ep_states, ep_transs);
			//ep_sm.save(this.dir, this.getTrailFilename(spec) + Config.EXT_Path);
			
			proc.getErrorStream().close();
			proc.getInputStream().close();
			proc.getOutputStream().close();
			proc.destroy();
			
		}
		
		return ret;
	}
	private String getTrailFilename(Specification spec) {
		return this.promela_filename + "." + spec.getId() + Config.EXT_Trail;
	}
	
	///// translate
	public String translate() {
		return this.translate(this.sm);
	}

	public String translate(StateMachine sm) {
		sm.abstractNoEventEdges();
		
		String ret = "";
		List<String> events = sm.getEvents();
		
		ret += "mtype = {\n";
		for(int i = 0; i < sm.getNumOfStates(); i++) {
			State s = sm.getState(i);
			ret += "\t" + Util.esc_spin(s.getName()) + ",\n";
		}
		ret += "\t__exit__\n";
		ret += "};\n\n";
		
		ret += "mtype = {\n";
		for(int i = 0; i < events.size(); i++) {
		//for(int i = events.size()-1; 0 <= i; i--) {
			String e = events.get(i);
			ret += "\t" + Util.esc_spin(e) + ",\n";
		}
		ret += "\t__empty__\n";
		ret += "};\n\n";

//		ret += "mtype App_state;\n";				// represents an application state
//		ret += "mtype App_event;\n";					// represents an application-handling event
//		ret += "mtype Int_event;\n";					// represents an application-handling event
		ret += "mtype App_state, App_event, Int_event;\n";					// represents an application-handling event
		ret += "chan App_ch = [0] of { mtype };\n";
		ret += "bool flg_exit = false;\n";
		ret += "\n";
		
		/*** create an application process ***/
		ret += "active proctype App() {\n";
		ret += describePromelaOfStateTransition(sm, sm.getInitState(), new LinkedList<State>(), "\t");
		
		ret += "goto___exit__:\n";
		ret += "\tflg_exit = true;\n";
		ret += "};\n\n";

		/*** create an interaction simulator process ***/
		ret += "active proctype Interaction() {\n";
		ret += "\tdo ::\n";
		ret += "\t\tif\n";
		ret += "\t\t:: flg_exit -> break;\n";
		ret += "\t\t:: else ->\n";
		ret += "\t\t\tif\n";
		for(int i = 0; i < events.size(); i++) {
		//for(int i = events.size()-1; 0 <= i; i--) {
			String event = events.get(i);
			ret += "\t\t\t:: skip -> App_ch!" + Util.esc_spin(event) + ";\n";
		}
		//ret += "\t\t\t:: skip -> skip\n";
		ret += "\t\t\tfi;\n";
		ret += "\t\tfi;\n";
		ret += "\tod;\n";
		ret += "};\n\n";
		
		return ret;
	}
	
	private static String describePromelaOfStateTransition(StateMachine sm, State target, List<State> rearched, String indent) {
		String ret = "";
		
		ret += indent + "d_step {\n";
		//ret += indent + "\tevent = __empty__;\n";
		ret += indent + "\tInt_event = __empty__;\n";
		ret += indent + "\tApp_event = __empty__;\n";
		ret += indent + "\tApp_state = " + Util.esc_spin(target.getName()) + ";\n";
		ret += indent + "}\n";
		if(!rearched.contains(target)) {
			ret += "goto_" + Util.esc_spin(target.getName()) + ":\n";
			rearched.add(target);
		} else {
			ret += indent + "goto goto_" +  Util.esc_spin(target.getName()) + ";\n";
			return ret;
		}
		
		List<Transition> targetTranss = sm.getTranssFromStateId(target.getId());
		if(0 < targetTranss.size()) {

			List<Transition> eventTranss = new LinkedList<Transition>();
			List<Transition> noEventTranss = new LinkedList<Transition>();
			for(int i = 0; i < targetTranss.size(); i++) {
				Transition t = targetTranss.get(i);
				if(t.getEvent() != null && !"".equals(t.getEvent())) {
					eventTranss.add(t);
				} else {
					noEventTranss.add(t);
				}
			}
			
			//if(0 < eventTranss.size()) {
			if(0 < eventTranss.size() || 0 < noEventTranss.size()) {
				ret += indent + "do\n";
				//ret += indent + ":: App_ch?event -> fired_event = event;\n";
				ret += indent + ":: App_ch?Int_event ->\n";
				ret += indent + "\tif\n";
				for (int i = 0; i < eventTranss.size(); i++) {
					Transition t = eventTranss.get(i);
					String e = t.getEvent();
					ret += indent + "\t:: Int_event == " + Util.esc_spin(e) + " ->\n";
					ret += indent + "\t\tApp_event = " + Util.esc_spin(e) + ";\n"; // + Util.esc_spin(e) + ";\n";
					if(sm.isExitById(t.getTo())) {
						ret += indent + "\t\t goto goto___exit__;\n";
					} else {
						ret += describePromelaOfStateTransition(sm, sm.getStateById(t.getTo()), rearched, indent + "\t\t");
					}
				}
				//ret += indent + "\t:: else -> d_step { event = __empty__; fired_event = __empty__; }\n";

				// no event transitions
				for(Iterator<Transition> it = noEventTranss.iterator(); it.hasNext();) {
					Transition t = it.next();
					ret += indent + "\t:: skip ->\n";
					if(sm.isExitById(t.getTo())) {
						ret += indent + "\t\t goto goto___exit__;\n";
					} else {
						ret += describePromelaOfStateTransition(sm, sm.getStateById(t.getTo()), rearched, indent + "\t\t");
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
