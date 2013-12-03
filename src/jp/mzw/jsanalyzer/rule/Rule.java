package jp.mzw.jsanalyzer.rule;

public class Rule {
	
	/**
	 * A keyword for distinguishing an interaction
	 */
	protected String mKeyword;
	
	/**
	 * Constructor
	 * @param keyword
	 */
	public Rule(String keyword) {
		this.mKeyword = keyword;
	}
	/**
	 * Determines whether given str matches this interaction keyword
	 * @param str Given string
	 * @return True or false represent matched or unmatched
	 */
	public boolean match(String str) {
		if(str == null || "".equals(str)) {
			return false;
		}
		if(str.equalsIgnoreCase(this.mKeyword)) {
			return true;
		}
		
		return false;
	}

	/**
	 * @return A string representing contents of this rule
	 */
	public String toString() {
		return Rule.class.toString() + ": keyword = " + this.mKeyword;
	}
}
