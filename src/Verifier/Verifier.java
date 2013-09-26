package Verifier;

import Analyzer.Analyzer;
import Analyzer.Util;
import Graph.StateMachine;

import java.util.List;

public class Verifier {
	
	protected StateMachine sm = null;
	public Verifier(StateMachine sm) {
		this.sm = sm;
	}

	public String translate() { return "Verifier.translate: " + this.sm.getName(); };
	

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
			content += spec.getHTML();
		}
		content += "</table>\n";

		content += "</body></html>\n";
		
		Util.write(analyzer.getProject().getDir(), html_filename, content);
	}
	

	public String saveVerifyIADPResults(Analyzer analyzer, List<AjaxDesignProperty> specs) {
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
			content += spec.getHTML();
		}
		content += "</table>\n";

		content += "</body></html>\n";
		
		Util.write(analyzer.getProject().getDir(), html_filename, content);
		
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
			content += spec.getHTML();
		}
		content += "</table>\n";

		content += "</body></html>\n";
		
		return content;
	}
}
