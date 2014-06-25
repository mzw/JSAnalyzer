package jp.mzw.jsanalyzer.rule;

import jp.mzw.jsanalyzer.xml.XMLAttr;

public class InteractionRule extends Rule {

	/**
	 * Represents a kind of the interaction
	 */
	protected String mInteract;

	/**
	 * 
	 */
	protected boolean mRepeatable;
	
	/**
	 * Constructor
	 * @param keyword for distinguishing an interaction
	 * @param interact represents a type of the interaction
	 */
	public InteractionRule(String keyword, String interact) {
		super(keyword);
		this.mInteract = interact;
	}
	
	/**
	 * @return A string representing contents of this rule
	 */
	@Override
	public String toString() {
		return Rule.class.toString() + ": keyword = " + this.mKeyword + ", interact = " + this.mInteract;
	}
	
	public boolean isUserInteract() {
		if(XMLAttr.RuleInteract_User.equals(this.mInteract)) {
			return true;
		}
		return false;
	}

	public boolean isServerInteract() {
		if(XMLAttr.RuleInteract_Server.equals(this.mInteract)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @return
	 */
	public boolean getRepeatable() {
		return this.mRepeatable;
	}
}