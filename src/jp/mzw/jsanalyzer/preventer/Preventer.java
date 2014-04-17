package jp.mzw.jsanalyzer.preventer;

import java.util.Date;

import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.preventer.insert_delay_ajax.JavaScriptLocation;
import jp.mzw.jsanalyzer.preventer.insert_delay_ajax.MutatedCodeGenerator;
import jp.mzw.jsanalyzer.util.TextFileUtils;

public class Preventer {
	
	

	public static void main(String[] args) {
		System.out.println("==============================");
		System.out.println("Running JSAnalyzer (Revealer)");
		System.out.println("This implementation is also called as <JSPreventer>");
		System.out.println("Visit: http://jsanalyzer.mzw.jp/about/jspreventer/");
		System.out.println((new Date()).toString());
		System.out.println("==============================");
		
		
		

		System.out.println("==============================");
		System.out.println("See you again!");
		System.out.println("==============================");
	}
	
	protected Analyzer mAnalyzer;

	/**
	 * Delayed time in millisecond
	 * Default value: 1000 msec
	 * b/c of "Time Scales in User Experience", http://www.nngroup.com/articles/powers-of-10-time-scales-in-ux/
	 */
	protected static final int mDelayTime = 1000;
	
	/**
	 * Server-side script for emulating network latency
	 * This program sends empty response after receiving request from Ajax app and elapsed given time
	 * !! Should deploy this program in your environment
	 */
	protected static final String RESPONSE_AFTER_DELAY = "http://localhost/~yuta/research/cs/response_after_delay.js.php?millisecond=";
	
	/**
	 * Constructor
	 */
	public Preventer(Analyzer analyzer) {
		this.mAnalyzer = analyzer;
	}
	

	public String getMutatedHTMLCode(String filename, int offset) {
		String html = TextFileUtils.cat(filename);
		
		String insersion = "<!-- start of delay insersion -->\n";
		insersion += "<script type=\"text/javascript\" src=\"" + Preventer.RESPONSE_AFTER_DELAY + this.mDelayTime + "\"></script>\n";
		insersion += "<!-- end of delay insersion -->\n";
		
		StringBuilder sb = new StringBuilder();
		sb.append(html);
		sb.insert(offset, insersion);
		
		return new String(sb); 
	}
	
	public String getMutatedJSCode(String filename, int pos) {
		JavaScriptLocation jsloc = JavaScriptLocation.jsFile(filename);
		
		MutatedCodeGenerator gen = new MutatedCodeGenerator();
		String code = gen.insertDelayedRequestObject(jsloc, pos, this.mDelayTime);
		
		return code;
	}
	
}
