package jp.mzw.jsanalyzer.core;

import java.util.Date;

import jp.mzw.jsanalyzer.core.examples.*;
import jp.mzw.jsanalyzer.verifier.modelchecker.Spin;
import jp.mzw.jsanalyzer.verifier.specification.Specification;

public class Test {
	public static void main(String[] args) {
		System.out.println("==============================");
		System.out.println("[Test] Running JSAnalyzer");
		System.out.println((new Date()).toString());
		System.out.println("==============================");
		
		
		String[] templates = {
				"[]((Q && !R && <>R) -> (!P U R))", // Absence (P) Between (Q) and (R)
				"[]((Q && !R) -> (!P U (R || []!P)))", // Absence (P) After (Q) until (R)
				"<>(Q && P)", // Existence (Q and P)
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

		if(!correct && error) {
			System.err.println("Result: " + correct + ", " + error);
		} else {
			System.out.println("Result: " + correct + ", " + error);
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
		P = "(" + keyup + " || " + click1 + " || " + click2 + ")";
//		P = "(" + keyup + ")";
		Q = "(" + ajax + ")";
		R = "(" + success + " || " + failure + ")";
		///
		formula = template.replaceAll("P", P).replaceAll("Q", Q).replaceAll("R", R);
		spec = new Specification("Description", formula);
		spin = new Spin(null, analyzer);
		spin.verify(spec);
		
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
		P = "(" + keyup + " || " + click1 + " || " + click2 + ")";
//		P = "(" + keyup + ")";
		Q = "(" + ajax + ")";
		R = "(" + success + " || " + failure + ")";
		///
		formula = genFormula(template, P, Q, R);
		spec = new Specification("Description", formula);
		spin = new Spin(null, analyzer);
		spin.verify(spec);
		
		return spec.isFault();
	}
	
	public static String genFormula(String template, String p, String q, String r) {
		String ret = template;
		ret = ret.replaceAll("P", p).replaceAll("Q", q).replaceAll("R", r);
		return ret;
	}
}
