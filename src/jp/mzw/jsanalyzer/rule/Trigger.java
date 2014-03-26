package jp.mzw.jsanalyzer.rule;

import jp.mzw.jsanalyzer.xml.XMLAttr;

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
	
	/**
	 * Determines whether this trigger represents a user interaction or not
	 * @return Is a user interaction
	 */
	public boolean isUserInteraction() {
		if(XMLAttr.RuleInteract_User.equals(this.mInteract)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Determines whether this trigger represents a server interaction or not
	 * @return Is a server interaction
	 */
	public boolean isServerInteraction() {
		if(XMLAttr.RuleInteract_Server.equals(this.mInteract)) {
			return true;
		}
		return false;
	}
	
}
