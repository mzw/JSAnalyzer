package jp.mzw.jsanalyzer.rule;

import jp.mzw.jsanalyzer.util.StringUtils;

public class Control extends Rule {
	
	/**
	 * A value representing a disabled element
	 */
	protected String mDisableValue;
	
	/**
	 * Constructor
	 * @param key An attribute of HTML or a property of CSS
	 * @param value A value representing a disabled element
	 */
	public Control(String key, String value) {
		super(key);
		this.mDisableValue = value;
	}
	
	/**
	 * Determines whether this statement makes this element enabled or disabled
	 * @param value Assigned to this disable attribute/property at this statement
	 * @return  True or false represent disabled or enabled
	 */
	public boolean disabled(String prop, String value) {
		if(value == null || "".equals(value)) {
			StringUtils.printError(this, "Invalid value", value);
			return false;
		}
		if(value.equalsIgnoreCase(this.mDisableValue)) {
			return true;
		}
		return false;
	}
	

	/**
	 * @return A string representing contents of this rule
	 */
	@Override
	public String toString() {
		return Rule.class.toString() + ": attr/prop = " + this.mKeyword + ", disable value = " + this.mDisableValue;
	}
}
