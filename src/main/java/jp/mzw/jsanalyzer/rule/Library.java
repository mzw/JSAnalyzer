package jp.mzw.jsanalyzer.rule;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.mzw.jsanalyzer.xml.XMLAttr;

public class Library extends Rule {
	
	/**
	 * Represents a method for matching between given string and keyword
	 */
	protected String mMethod;
	
	/**
	 * Gives a path to a corresponding rule file
	 */
	protected String mPath;

	/**
	 * Constructor
	 * @param filename is a keyword for detecting JavaScript libraries which have been ruled
	 */
	public Library(String filename, String method, String path) {
		super(filename);
		this.mMethod = method;
		this.mPath = path;
	}
	
	/**
	 * Determines whether given filename matches library rules
	 * @param filename is a JavaScript library
	 */
	@Override
	public boolean match(String filename) {
		if(filename == null || "".equals(filename)) {
			return false;
		}
		
		if(filename.equals(this.mKeyword)) {
			return true;
		}
		
		// a little bit high-level matching
		if(XMLAttr.RuleMethod_StartsWith.equals(this.mMethod) &&
				filename.startsWith(mKeyword)) {
			return true;
		} else if(XMLAttr.RuleMethod_Containts.equals(this.mMethod) &&
				filename.contains(this.mKeyword)) {
			return true;
		} else if(XMLAttr.RuleMethod_Regex.equals(this.mMethod)) {
			Pattern pattern = Pattern.compile(this.mKeyword);
			Matcher matcher = pattern.matcher(filename);
			if(matcher.find()) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Gets a path of this rule file
	 * @return A path of this rule file
	 */
	public String getPath() {
		return this.mPath;
	}
}
