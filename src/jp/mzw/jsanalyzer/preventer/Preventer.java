package jp.mzw.jsanalyzer.preventer;

import java.util.Date;

import jp.mzw.jsanalyzer.preventer.insert_delay_ajax.JavaScriptLocation;
import jp.mzw.jsanalyzer.preventer.insert_delay_ajax.MutatedCodeGenerator;

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
	

	/**
	 * Delayed time in millisec
	 */
	protected int mDelayTime;
	
	/**
	 * Constructor
	 */
	public Preventer() {
		this.mDelayTime = 3000; // 3 sec
	}
	
	
	public String getMutatedJSCode(String filename, int pos) {
		JavaScriptLocation jsloc = JavaScriptLocation.jsFile(filename);
		
		MutatedCodeGenerator gen = new MutatedCodeGenerator();
		String code = gen.insertDelayedRequestObject(jsloc, pos, this.mDelayTime);
		
		return code;
	}
}
