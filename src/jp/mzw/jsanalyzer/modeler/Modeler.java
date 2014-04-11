package jp.mzw.jsanalyzer.modeler;

import java.util.Date;

import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.core.Project;
import jp.mzw.jsanalyzer.core.examples.*;
import jp.mzw.jsanalyzer.modeler.model.fsm.FiniteStateMachine;
import jp.mzw.jsanalyzer.parser.Code;
import jp.mzw.jsanalyzer.serialize.Serializer;

public class Modeler {

	/**
	 * Gets all information for modeling via given Analyzer instance
	 */
	protected Analyzer mAnalyzer;
	
	public Modeler(Analyzer analyzer) {
		this.mAnalyzer = analyzer;
	}
	

	public static void main(String[] args) {
		System.out.println("==============================");
		System.out.println("Running JSAnalyzer (Extractor)");
		System.out.println("This implementation is also called as <JSModeler>");
		System.out.println("Visit: http://jsanalyzer.mzw.jp/about/jsmodeler/");
		System.out.println((new Date()).toString());
		System.out.println("==============================");

		System.out.println("Preparing your project...");
//		Analyzer analyzer = new Analyzer("projects/project/project.xml");
//		Analyzer analyzer = new Analyzer(new FileDLerError());
//		Analyzer analyzer = new Analyzer(new FileDLerCorrect());
//		Analyzer analyzer = new Analyzer(new FileDLerRetry());
//		Analyzer analyzer = new Analyzer(new QAsiteError());
//		Analyzer analyzer = new Analyzer(new QAsiteCorrect());
//		Analyzer analyzer = new Analyzer(new SWSError());
//		Analyzer analyzer = new Analyzer(new LoginDemo());
//		Analyzer analyzer = new Analyzer(new LWA());

		Project project = CS2020m.getProject(CS2020m.INIT);
		Analyzer analyzer = new Analyzer(project);
		
		Modeler modeler = new Modeler(analyzer);
		FiniteStateMachine fsm = modeler.extract();
		Serializer.serialze(analyzer, fsm);
		
		System.out.println("==============================");
		System.out.println("See you again!");
		System.out.println("==============================");
	}
	
	/**
	 * Starts our analysis method with given project information
	 */
	public FiniteStateMachine extract() {
		System.out.println("Starts to analyze!");
		
		System.out.println("Extracting finite state machine...");
		FSMExtractor extractor = new FSMExtractor(this.mAnalyzer);
		FiniteStateMachine fsm = extractor.extracts();
		
		System.out.println("--> [Success] Extraction time: " + extractor.getExtractTime());
		System.out.println("HTML LoC: " + extractor.getHTMLCode().getLoC());
		int jsloc = 0;
		for(Code jscode : extractor.getJSCodeList()) {
			jsloc += jscode.getLoC();
		}
		System.out.println("JavaScript LoC: " + jsloc);
		int cssloc = 0;
		for(Code csscode : extractor.getCSSCodeList()) {
			cssloc += csscode.getLoC();
		}
		System.out.println("CSS LoC: " + cssloc);
		
		return fsm;
	}
}
