package jp.mzw.jsanalyzer.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import jp.mzw.jsanalyzer.config.FileExtension;
import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.core.examples.*;
import jp.mzw.jsanalyzer.formulator.IADPFormulator;
import jp.mzw.jsanalyzer.formulator.property.Property;
import jp.mzw.jsanalyzer.util.CommandLineUtils;
import jp.mzw.jsanalyzer.util.StringUtils;
import jp.mzw.jsanalyzer.util.TextFileUtils;
import jp.mzw.jsanalyzer.verifier.modelchecker.NuSMV;
import jp.mzw.jsanalyzer.verifier.modelchecker.Spin;
import jp.mzw.jsanalyzer.verifier.specification.Specification;

public class Test {

	public static void main(String[] args) {
//		Analyzer analyzer = new Analyzer(new FileDLerCorrect());
		Analyzer analyzer = new Analyzer(new FileDLerError());
//		Analyzer analyzer = new Analyzer(new FileDLerRetry());
		
		String objName = analyzer.getProject().getName() + ".fsm" + FileExtension.Serialized;
		Object obj = TextFileUtils.deserialize(analyzer.getProject().getDir(), objName);
		jp.mzw.jsanalyzer.serialize.model.FiniteStateMachine fsm = (jp.mzw.jsanalyzer.serialize.model.FiniteStateMachine)obj;
		
		NuSMV nusmv = new NuSMV(fsm, analyzer);
		for(Specification spec : IADPFormulator.getSpecList4FileDLer(analyzer)) {
			nusmv.verify(spec);
		}
	}
	
	
	/////
	
	public static void ___main(String[] args) {
		Analyzer analyzer = new Analyzer(new FileDLerCorrect());
//		Analyzer analyzer = new Analyzer(new FileDLerError());
//		Analyzer analyzer = new Analyzer(new FileDLerRetry());
		
		String objName = analyzer.getProject().getName() + ".fsm" + FileExtension.Serialized;
		Object obj = TextFileUtils.deserialize(analyzer.getProject().getDir(), objName);
		jp.mzw.jsanalyzer.serialize.model.FiniteStateMachine fsm = (jp.mzw.jsanalyzer.serialize.model.FiniteStateMachine)obj;
		
		
		String fsmdata = "";
		
		/// Sets project data
		fsmdata += "<Application name=\"" + analyzer.getProject().getName() + "\" url=\"" + analyzer.getProject().getUrl() + "\">";
		fsmdata += "\n";
		
		fsmdata += "<FSMData>\n";
		
		/// States
		for(jp.mzw.jsanalyzer.serialize.model.State state : fsm.getStateList()) {
			fsmdata += "\t";
			fsmdata += "<State ";
			fsmdata += "id=\"" + state.getId() +"\"";
			fsmdata += ">\n";
			
			for(jp.mzw.jsanalyzer.serialize.model.State.FuncElement func : state.getFuncElement()) {
				fsmdata += "\t";
				fsmdata += "\t";

				fsmdata += "<Abstracted ";
				if("".equals(func.getFuncName())) {
					fsmdata += "func=\"" + "Nameless" + "\" ";
				} else {
					fsmdata += "func=\"" + StringUtils.esc_xml(func.getFuncName()) + "\" ";
				}
				fsmdata += "lineno=\"" + func.getLineNo() + "\" ";
				fsmdata += "pos=\"" + func.getPosition() + "\" ";
				fsmdata += "/>";
				
				fsmdata += "\n";
			}

			fsmdata += "\t";
			fsmdata += "</State>\n";
		}
		
		/// Events
		for(jp.mzw.jsanalyzer.serialize.model.Transition trans : fsm.getTransList()) {
			if(trans.getEvent() != null) {
				fsmdata += "\t";
				fsmdata += "<Event id=\"" + trans.getEvent().getId() + "\" name=\"" + trans.getEvent().getEvent() + "\" lineno=\"" + trans.getEvent().getLineNo() + "\" pos=\"" + trans.getEvent().getPosition() + "\" />\n";
			}
		}
		

		fsmdata += "</FSMData>\n";
		
		
		fsmdata += "</Application>";
		
//		System.out.println(fsmdata);
		
		
		String propListXML = jp.mzw.jsanalyzer.formulator.property.Property.getPropertyListXML();
		System.out.println(propListXML);
	}
	
	
	/////////////////////////////////////////
	public static void _main(String[] args) {
		System.out.println("==============================");
		System.out.println("[Test] Running JSAnalyzer");
		System.out.println((new Date()).toString());
		System.out.println("==============================");
		
		/// user@host$ randltl -n 300000 P Q R --ltl-priorities 'true=0,false=0,xor=0,implies=1,equiv=0,W=0,M=0,X=0,U=1,R=0,W=0,M=0' > test.ltls
		
		int index = 1160;
		int sep = 100000;
		for(int seed = index; seed < index + sep; seed++) {
			System.out.println("[#" + seed + "]");
			String raw = genRandLtlTemplate("/Users/yuta/Desktop", seed, 1);
			String template = raw.replace("\"P\"", "P").replace("\"Q\"", "Q").replace("\"R\"", "R").replace("|", "||").replace("&", "&&").replace("F", "<>").replace("G", "[]");

			runBoth(template);
		}
		
		//(<>Q || (<>R && [](Q -> P))) U (P && Q)
		//Err: --__empty__-->(root)--onload-->--__empty__-->(countdown)--__empty__-->(Ajax.Request)--keyup-->--__empty__-->(inputFormText)
		
		System.out.println("==============================");
		System.out.println("See you again!");
		System.out.println("==============================");
	}
	
	public static String genRandLtlTemplate(String dir, int seed, int num) {
		
		String[] cmd = {
				"/usr/local/bin/randltl",
				"--seed=" + Integer.toString(seed),
				"-n", Integer.toString(num),
				"--ltl-priorities", "true=0,false=0,xor=0,implies=1,equiv=0,W=0,M=0,X=0,U=1,R=0,W=0,M=0",
				"P", "Q", "R",
				};

		Process proc = null;
		int proc_result = 0;
		try {
			try {
				proc = Runtime.getRuntime().exec(cmd, null, new File(dir));
				proc_result = proc.waitFor();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} finally {
			if(proc != null) {
				String stdOut = CommandLineUtils.readStdOut(proc);
				String stdErr = CommandLineUtils.readStdErr(proc);
				
				try {
					proc.getErrorStream().close();
					proc.getInputStream().close();
					proc.getOutputStream().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				proc.destroy();
				
//				return proc_result;
				return stdOut;
			}
		}
//		return proc_result;
		return null;
	}

	public static void __main(String[] args) {
		System.out.println("==============================");
		System.out.println("[Test] Running JSAnalyzer");
		System.out.println((new Date()).toString());
		System.out.println("==============================");
		
		// P: user events
		// Q: Ajax
		// R: success/failure
		String[] templates = {
//				"[]((Q && !R && <>R) -> (!P U R))", // Absence (P) Between (Q) and (R)
//				"[]((Q && !R) -> (!P U (R || []!P)))", // Absence (P) After (Q) until (R)
////				"<>(Q && P)", // Existence (Q and P)
//				"[]( (Q && X(!P U R) ) )",
////				"[](!((Q && X(!P U R))",
//				"(!R U P)", 
//				"[](Q -> X(!P U R))", 
//				"[](Q -> [](!P))",
//				"[](Q && !R -> (!R U (P && !R)))",
//				"[](Q && !R -> (P U (R || []P)))",
//				"[](Q -> (!P U <>R))",
//				"<>Q && []!R && <>P",
//				"<>(Q && !R && P)",
//				"[]!(Q && !R && P)",
//				"[]!(<>Q && []!R && <>P)",
//				"[]((Q && !R) -> (!P U R))",
//				"<>Q -> P",
				
//				"[](failure -> <>!ajax)", // (Q) Response to (P)
//				"![](failure -> <>ajax)",
//				"(failure -> <>ajax)",
//				"!((Q U P) && <>!Q) U <>(P && !<>P)",
				
				
//				
//				"[](<>(R -> P) || (!<>P && <>(P || !P)))",
//				"[]((P U R) U (<>(!P -> (!Q && R)) -> Q))",
//				"[]<>(((!P -> []R) U !(Q -> P)) || <>R)",
//				"[](([]R || <>(!P || (R U Q))) U (R U P))",
				"<>[](!R || (([]R U (R -> P)) U (P || R)))",
				"[](!P || []((!P && R) U [](<>Q U P)))",
				"[](P || R || (Q && R) || <>([]P -> R))",
				"[]<>([]Q || ((R U (!P || R)) -> P))",
				"[](<>P || []((Q U R) U [](R && (P -> Q))))",
				"!<>(!Q && []((P && R) U R) && <>R)",
		};
		 
		
		for(String template : templates) {
			runBoth(template);
		}
		
		
		System.out.println("==============================");
		System.out.println("See you again!");
		System.out.println("==============================");
	}
	
	public static void runBoth(String template) {
		System.out.println(template);
		boolean correct = runCorrect(template);
		boolean error = runError(template);
//		boolean retry = runRetry(template);

		if(correct && !error) {
			System.err.println("Result: " + correct + ", " + error);// + ", " + retry);
			TextFileUtils.write("/Users/yuta/Desktop/shtest", "template." + (new Date()).toString() + ".txt", template);
		} else {
			System.out.println("Result: " + correct + ", " + error);// + ", " + retry);
		}
		
	}
	
	public static boolean runError(String template) {
		Analyzer analyzer;
		String ajax, keyup, click1, click2, success, failure;
		String P, Q, R, formula;
		Specification spec;
		Spin spin;

		// error
//		System.out.println("=====Error======");
		analyzer = new Analyzer(new FileDLerError());
		///
		ajax = "(App_state == JSAnalyzer_0000000126)";
		keyup = "(App_event == JSAnalyzer_0000000938)";
		click1 = "(App_event == JSAnalyzer_0000000940)";
		click2 = "(App_event == JSAnalyzer_0000000944)";
		success = "(App_event == JSAnalyzer_0000000932)";
		failure = "(App_event == JSAnalyzer_0000000934)";
		///
//		P = "(" + keyup + " || " + click1 + " || " + click2 + ")";
		P = "(" + keyup + ")";
//		P = "(" + click1 + ")";
//		P = "(" + click2 + ")";
		Q = "(" + ajax + ")";
		R = "(" + success + " || " + failure + ")";
		///
		formula = template.replaceAll("P", P).replaceAll("Q", Q).replaceAll("R", R).replaceAll("failure", failure).replaceAll("ajax", ajax);
		spec = new Specification("Description", formula);
		spin = new Spin(null, analyzer);
		spin.verify(spec);
		
		if(spec.isFault()) {
			String counter = spec.getAntiExample();
			counter = counter.replaceAll("JSAnalyzer_0000000010", "root");
			counter = counter.replaceAll("JSAnalyzer_0000000928", "onload");
			counter = counter.replaceAll("JSAnalyzer_0000000119", "countdown");
			counter = counter.replaceAll("JSAnalyzer_0000000130", "setTimeout");
			counter = counter.replaceAll("JSAnalyzer_0000000126", "Ajax.Request");
			counter = counter.replaceAll("JSAnalyzer_0000000322", "doSubmit");
			counter = counter.replaceAll("JSAnalyzer_0000000279", "inputFormText");
			counter = counter.replaceAll("JSAnalyzer_0000000938", "keyup");
			counter = counter.replaceAll("JSAnalyzer_0000000940", "click1");
			counter = counter.replaceAll("JSAnalyzer_0000000944", "click2");
			counter = counter.replaceAll("JSAnalyzer_0000000932", "success");
			counter = counter.replaceAll("JSAnalyzer_0000000934", "failure");
			System.out.println("Err: " + counter);
		}

		String nv = "/Users/yuta/Dropbox/workspace/JSAnalyzer/projects/test2/verify/FileDLerError.1.ltl";
		String ltl = TextFileUtils.cat(nv); // never claim
		ltl = ltl.replace(P, "UserEvs");
		ltl = ltl.replace(Q, "Ajax");
		ltl = ltl.replace(R, "Response");
		TextFileUtils.write(nv + ".mod", ltl);
		
		return spec.isFault();
	}
	
	public static boolean runCorrect(String template) {
		Analyzer analyzer;
		String ajax, keyup, click1, click2, success, failure;
		String P, Q, R, formula;
		Specification spec;
		Spin spin;

		// correct
//		System.out.println("=====Correct======");
		analyzer = new Analyzer(new FileDLerCorrect());
		///
		ajax = "(App_state == JSAnalyzer_0000000126)";
		keyup = "(App_event == JSAnalyzer_0000001016)";
		click1 = "(App_event == JSAnalyzer_0000001018)";
		click2 = "(App_event == JSAnalyzer_0000001022)";
		success = "(App_event == JSAnalyzer_0000001010)";
		failure = "(App_event == JSAnalyzer_0000001012)";
		///
//		P = "(" + keyup + " || " + click1 + " || " + click2 + ")";
		P = "(" + keyup + ")";
//		P = "(" + click1 + ")";
//		P = "(" + click2 + ")";
		Q = "(" + ajax + ")";
		R = "(" + success + " || " + failure + ")";
		///
		formula = template.replaceAll("P", P).replaceAll("Q", Q).replaceAll("R", R).replaceAll("failure", failure).replaceAll("ajax", ajax);
		spec = new Specification("Description", formula);
		spin = new Spin(null, analyzer);
		spin.verify(spec);

		if(spec.isFault()) {
			String counter = spec.getAntiExample();
			counter = counter.replaceAll("JSAnalyzer_0000000010", "root");
			counter = counter.replaceAll("JSAnalyzer_0000001006", "onload");
			counter = counter.replaceAll("JSAnalyzer_0000000119", "countdown");
			counter = counter.replaceAll("JSAnalyzer_0000000130", "setTimeout");
			counter = counter.replaceAll("JSAnalyzer_0000000126", "Ajax.Request");
			counter = counter.replaceAll("JSAnalyzer_0000000327", "doSubmit");
			counter = counter.replaceAll("JSAnalyzer_0000000284", "inputFormText");
			counter = counter.replaceAll("JSAnalyzer_0000001016", "keyup");
			counter = counter.replaceAll("JSAnalyzer_0000001018", "click1");
			counter = counter.replaceAll("JSAnalyzer_0000001022", "click2");
			counter = counter.replaceAll("JSAnalyzer_0000001010", "success");
			counter = counter.replaceAll("JSAnalyzer_0000001012", "failure");
			System.out.println("Crr: " + counter);
		}
		
		String nv = "/Users/yuta/Dropbox/workspace/JSAnalyzer/projects/test2/verify/FileDLerCorrect.0.ltl";
		String ltl = TextFileUtils.cat(nv); // never claim
		ltl = ltl.replace(P, "UserEvs");
		ltl = ltl.replace(Q, "Ajax");
		ltl = ltl.replace(R, "Response");
		TextFileUtils.write(nv + ".mod", ltl);
		
		return spec.isFault();
	}
	
	
	public static void forFileDLer(Analyzer analyzer) {
		//// Deserialize fsm
		String objName = analyzer.getProject().getName() + ".fsm" + FileExtension.Serialized;
		Object obj = TextFileUtils.deserialize(analyzer.getProject().getDir(), objName);
		jp.mzw.jsanalyzer.serialize.model.FiniteStateMachine fsm = (jp.mzw.jsanalyzer.serialize.model.FiniteStateMachine)obj;
		

		ArrayList<String> stateNameList = new ArrayList<String>();
		stateNameList.add("countDown");
		stateNameList.add("getPwd");
		stateNameList.add("setTimeout");
		stateNameList.add("setForm");
		stateNameList.add("alert");
		stateNameList.add("inputFormText");
		stateNameList.add("doSubmit");
		stateNameList.add("doDownload");
		
		String[] stateNames = {
			"countDown",
			"getPwd",
			"setTimeout",
			"setForm",
			"alert",
			"inputFormText",
			"doSubmit",
			"doDownload",
		};

		HashMap<String, String> state_id_name = new HashMap<String, String>();
		HashMap<String, String> state_name_id = new HashMap<String, String>();
		for(String stateName : stateNames) {
			String stateId = fsm.getStateId(stateName);
			state_id_name.put(stateId, stateName);
			state_name_id.put(stateName, stateId);
		}
		
		
		String onload = fsm.getEventId("onload", 3, 7);
		String onSuccess = fsm.getEventId("onSuccess", 25, 0);
		String onFailure = fsm.getEventId("onFailure", 30, 0);
		String after = fsm.getEventId("after(1000 msec)", 15, 22);
		String onkeyup = fsm.getEventId("onkeyup", 42, 6);
		String UserClick1 = fsm.getEventId("User Click", 31, 0);
		String onclick1 = fsm.getEventId("onclick", 48, 8);
		String onclick2 = fsm.getEventId("onclick", 91, 7);
		String UserClick2 = fsm.getEventId("User Click", 72, 0);
		
		

//		String P = "(App_event == " + onkeyup + " || App_event == " + onclick1 + " || App_event == " + onclick2 + ")";
////		P = "(" + keyup + ")";
////		P = "(" + click1 + ")";
////		P = "(" + click2 + ")";
//		String Q = "(" + ajax + ")";
//		R = "(" + success + " || " + failure + ")";
	}
	
	public static boolean runRetry(String template) {
		Analyzer analyzer;
		String ajax, keyup, click1, click2, success, failure;
		String P, Q, R, formula;
		Specification spec;
		Spin spin;

		//// Read project file
		analyzer = new Analyzer(new FileDLerRetry());
//		//// Deserialize fsm
//		String objName = analyzer.getProject().getName() + ".fsm" + FileExtension.Serialized;
//		Object obj = TextFileUtils.deserialize(analyzer.getProject().getDir(), objName);
//		//
//		jp.mzw.jsanalyzer.serialize.model.FiniteStateMachine fsm = (jp.mzw.jsanalyzer.serialize.model.FiniteStateMachine)obj;
//		
//		String _ajax = fsm.getStateId("Ajax.Request");
//		System.out.println("st: " + _ajax);
//		
//		String _success = fsm.getEventId("onFailure", 30);
//		System.out.println("ev: " + _success);
		
		///
		ajax = "(App_state == JSAnalyzer_0000000126)";
		keyup = "(App_event == JSAnalyzer_0000001003)";
		click1 = "(App_event == JSAnalyzer_0000001005)";
		click2 = "(App_event == JSAnalyzer_0000001009)";
		success = "(App_event == JSAnalyzer_0000000999)";
		failure = "(App_event == JSAnalyzer_0000001001)";
		///
		P = "(" + keyup + " || " + click1 + " || " + click2 + ")";
//		P = "(" + keyup + ")";
//		P = "(" + click1 + ")";
//		P = "(" + click2 + ")";
		Q = "(" + ajax + ")";
		R = "(" + success + " || " + failure + ")";
		///
		formula = template.replaceAll("P", P).replaceAll("Q", Q).replaceAll("R", R).replaceAll("failure", failure).replaceAll("ajax", ajax);
		spec = new Specification("Description", formula);
		spin = new Spin(null, analyzer);
		spin.verify(spec);

		if(spec.isFault()) {
			String counter = spec.getAntiExample();
			counter = counter.replaceAll("JSAnalyzer_0000000010", "root");
			counter = counter.replaceAll("JSAnalyzer_0000000119", "countdown");
			counter = counter.replaceAll("JSAnalyzer_0000000130", "setTimeout");
			counter = counter.replaceAll("JSAnalyzer_0000000126", "Ajax.Request");
			counter = counter.replaceAll("JSAnalyzer_0000000130", "setTimout");
			counter = counter.replaceAll("JSAnalyzer_0000000165", "setForm");
			counter = counter.replaceAll("JSAnalyzer_0000000278", "inputFormText");
			counter = counter.replaceAll("JSAnalyzer_0000000305", "scope1");
			counter = counter.replaceAll("JSAnalyzer_0000000312", "scope2");
			counter = counter.replaceAll("JSAnalyzer_0000000321", "doSubmit");
			counter = counter.replaceAll("JSAnalyzer_0000000346", "scope3");
			counter = counter.replaceAll("JSAnalyzer_0000000353", "scope4");
			counter = counter.replaceAll("JSAnalyzer_0000000464", "doDownload");
			counter = counter.replaceAll("JSAnalyzer_0000000356", "alert");
			
			counter = counter.replaceAll("JSAnalyzer_0000000995", "onload");
			counter = counter.replaceAll("JSAnalyzer_0000000999", "success");
			counter = counter.replaceAll("JSAnalyzer_0000001001", "failure");
			counter = counter.replaceAll("JSAnalyzer_0000000997", "after");
			counter = counter.replaceAll("JSAnalyzer_0000001003", "keyup");
			counter = counter.replaceAll("JSAnalyzer_0000001005", "click1");
			counter = counter.replaceAll("JSAnalyzer_0000001009", "click2");
			counter = counter.replaceAll("JSAnalyzer_0000001007", "UserClick");
			
			System.out.println("Ret: " + counter);
		}
		
		String nv = "/Users/yuta/Dropbox/workspace/JSAnalyzer/projects/test2/verify/FileDLerRetry.2.ltl";
		String ltl = TextFileUtils.cat(nv); // never claim
		ltl = ltl.replace(P, "UserEvs");
		ltl = ltl.replace(Q, "Ajax");
		ltl = ltl.replace(R, "Response");
		ltl = ltl.replace(failure, "Failure");
		TextFileUtils.write(nv + ".mod", ltl);
		
		return spec.isFault();
	}
	
	public static String genFormula(String template, String p, String q, String r) {
		String ret = template;
		ret = ret.replaceAll("P", p).replaceAll("Q", q).replaceAll("R", r);
		return ret;
	}
}
