package jp.mzw.jsanalyzer.rule;

import jp.mzw.jsanalyzer.xml.XMLAttr;

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
	 * Gets keyword to be distinguished
	 * @return
	 */
	public String getKeyword() {
		return this.mKeyword;
	}

	/**
	 * @return A string representing contents of this rule
	 */
	public String toString() {
		return Rule.class.toString() + ": keyword = " + this.mKeyword;
	}
	
	/**
	 * Determines whether given rule string has arg_*
	 * @param rule Given rule string
	 * @return The number of the argument. If not contains, return -1.
	 * @deprecated
	 */
	public static int hasArgNum(String rule) {
		String arg_str = XMLAttr.RuleArgNum;
		int arg_num = 0;
		int arg_num_max = 10;
		for(; arg_num < arg_num_max; arg_num++) {
			String tmp_arg = arg_str + arg_num;
			if(tmp_arg.equals(rule)) {
				break;
			}
		}
		if(arg_num < arg_num_max) {
			return arg_num;
		}
		
		return -1;
	}
}
