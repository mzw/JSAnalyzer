package jp.mzw.jsanalyzer.formulator.pp;

import java.util.List;

public class PropertyPattern {
	
	public static final String BoundedExsistence_Between = "[]((Q & <>R) -> ((!P & !R) U (R | ((P & !R) U (R | ((!P & !R) U (R | ((P & !R) U (R | (!P U R))))))))))";;
	
	public static final String instantiate(String pattern, String p, String q, String r) {
		String ret = "";
		
		
		return ret;
	}
	
	
	protected String mTemplate;
	protected List<String> mArgs;

	public PropertyPattern(String template) {
		this.mTemplate = template;
	}
	
	public void setArgs(List<String> args) {
		this.mArgs = args;
	}
	
	public String generate() {
		String ret = "";
		
		
		return ret;
	}
}
