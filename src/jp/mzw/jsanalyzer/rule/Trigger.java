package jp.mzw.jsanalyzer.rule;

public class Trigger extends InteractionRule {
	

	/**
	 * Constructor
	 * @param event An event name of an interaction
	 * @param interact A kind of an the interaction
	 */
	public Trigger(String event, String interact, boolean repeatable) {
		super(event, interact);
		this.mRepeatable = repeatable;
	}
}
