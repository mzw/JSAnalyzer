package jp.mzw.jsanalyzer.rule;

public class InteractionRule extends Rule {

	/**
	 * Represents a kind of the interaction
	 */
	protected String mInteract;
	
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
}
