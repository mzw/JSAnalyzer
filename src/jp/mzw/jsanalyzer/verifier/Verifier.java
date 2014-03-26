package jp.mzw.jsanalyzer.verifier;

import java.util.List;

import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.formulator.IADPFormulator;
import jp.mzw.jsanalyzer.formulator.adp.AjaxDesignPattern;
import jp.mzw.jsanalyzer.modeler.model.fsm.FiniteStateMachine;
import jp.mzw.jsanalyzer.util.TextFileUtils;
import jp.mzw.jsanalyzer.verifier.modelchecker.ModelChecker;
import jp.mzw.jsanalyzer.verifier.modelchecker.Spin;
import jp.mzw.jsanalyzer.verifier.specification.Specification;

public class Verifier {
	
	protected Analyzer mAnalyzer;
	public Verifier(Analyzer analyzer) {
		this.mAnalyzer = analyzer;
	}
	
	protected FiniteStateMachine mFiniteStateMachine;
	protected List<Specification> mSpecList;
	protected ModelChecker mModelChecker;
	
	/**
	 * A elapsed time for verifying the correctness of the extracted finite state machine
	 */
	protected long mVerifyTime;
	
	/**
	 * Verifies the correctness of the extracted finite state machine
	 */
	public void verify(FiniteStateMachine fsm, List<Specification> specList, ModelChecker mc) {
		long start = System.currentTimeMillis();
		/// start
		
		/*
		 * To be implemented
		 */
		
		/// end
		long end = System.currentTimeMillis();
		this.mVerifyTime = end - start;
		
	}
	
	
	/**
	 * Verifies interaction invariants based on Ajax design patterns
	 * @param fsm Extracted finite state machine
	 * @param filename Contains information about implemented Ajax design patterns (IADP Information)
	 */
	public void verifyIADP(FiniteStateMachine fsm, String filename) {
		long start = System.currentTimeMillis();
		/// start

		// Translating an extracted finite machine
		Spin spin = new Spin(fsm, this.mAnalyzer);
		String promela = spin.translate();
		spin.savePromela(promela);

		// Generating verification formulas
		IADPFormulator formulator = new IADPFormulator("projects/test2/FileDLer.xml");
		List<Specification> specList = formulator.generate();
		
		// Running Spin
		
		
		/// end
		long end = System.currentTimeMillis();
		this.mVerifyTime = end - start;
		
	}
	
	
	
	
	
	
	
	
	public void saveVerifyResults(Analyzer analyzer, List<Specification> specs) {
		String html_filename = analyzer.getProject().getName() + ".html";

		String content = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\">\n";
		content += "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n";
		content += "<title>Verification results of JSVerifier</title>\n";
		content += "<link rel=\"stylesheet\" href=\"css/base.css\" type=\"text/css\" />\n";
		content += "<body>\n";
		

		content += "<h1>Target application</h1>\n";
		content += "<a href=\"" + analyzer.getProject().getUrl() + "\">" + analyzer.getProject().getUrl() + "</a>\n";

		content += "<h1>Results</h1>\n";
		content += "<table cellspacing=1>\n";
		
		content += "<tr><th>Id</th><th>Description</th><th>LTL formula</th><th>Result</th></tr>\n";
		
		for(Specification spec : specs) {
			content += spec.toHTML();
		}
		content += "</table>\n";

		content += "</body></html>\n";
		
		TextFileUtils.write(analyzer.getProject().getDir(), html_filename, content);
	}
	

//	public String saveVerifyIADPResults(Analyzer analyzer, List<AjaxDesignPattern> specs) {
	public String saveVerifyIADPResults(Analyzer analyzer, List<Specification> specs) {
		String html_filename = analyzer.getProject().getName() + ".html";

		String content = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\">\n";
		content += "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n";
		content += "<title>Verification results of JSVerifier</title>\n";
		content += "<link rel=\"stylesheet\" href=\"css/base.css\" type=\"text/css\" />\n";
		content += "<body>\n";
		

		content += "<h1>Target application</h1>\n";
		content += "<a href=\"" + analyzer.getProject().getUrl() + "\">" + analyzer.getProject().getUrl() + "</a>\n";

		content += "<h1>Results</h1>\n";
		content += "<table cellspacing=1>\n";
		
		//content += "<tr><th>Id</th><th>Property name</th><th width='50'>Description</th><th>Derived Ajax design pattern</th><th>Mapped property pattern</th><th>Formula template</th><th>Verification formula</th><th>Result</th></tr>\n";
		content += "<tr><th>Id</th><th>Property name</th><th width='250'>Description</th><th>Derived Ajax design pattern</th><th>Mapped property pattern</th><th>Verification formula</th><th>Result</th></tr>\n";
		
		for(Specification spec : specs) {
			content += spec.toHTML();
		}
		content += "</table>\n";

		content += "</body></html>\n";
		
		TextFileUtils.write(analyzer.getProject().getDir(), html_filename, content);
		
		return content;
	}
	
	public String saveVerifyBLPResults(Analyzer analyzer, List<Specification> specs) {
		String content = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\">\n";
		content += "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n";
		content += "<title>Verification results of JSVerifier</title>\n";
		content += "<link rel=\"stylesheet\" href=\"css/base.css\" type=\"text/css\">\n";
		content += "<body>\n";
		

		content += "<h1>Target application</h1>\n";
		content += "<p><a href=\"" + analyzer.getProject().getUrl() + "\">" + analyzer.getProject().getUrl() + "</a></p>\n";

		content += "<h1>Results</h1>\n";
		content += "<table cellspacing=1>\n";
		
		content += "<tr><th>Id</th><th>Property name</th><th>Verification formula</th><th>Result</th></tr>\n";
		
		for(Specification spec : specs) {
			content += spec.toHTML();
		}
		content += "</table>\n";

		content += "</body></html>\n";
		
		return content;
	}
}
