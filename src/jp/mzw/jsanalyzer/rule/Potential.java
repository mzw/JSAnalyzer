package jp.mzw.jsanalyzer.rule;

import jp.mzw.jsanalyzer.modeler.model.interaction.Event;

public class Potential extends InteractionRule {
	/**
	 * A target element/object of interaction
	 */
	protected String mTarget;
	/**
	 * An event of an interaction
	 */
	protected String mEvent;
	
	/**
	 * Modifies this event
	 */
	protected String mEventModifier;
	
	/**
	 * A callback function of an interaction
	 */
	protected String mCallback;
	
	/**
	 * 
	 */
	protected boolean mRepeatable;
	
	/**
	 * Constructor
	 * @param func A name of function which handles an interaction as a keyword
	 * @param interact A type of the interaction
	 * @param event An event of the interaction
	 * @param callback A callback function of the interaction
	 */
	public Potential(String func, String interact, String target, String event, String event_modifier, String callback, boolean repeatable) {
		super(func, interact);
		this.mTarget = target;
		this.mEvent = event;
		this.mEventModifier = event_modifier;
		this.mCallback = callback;
		this.mRepeatable = repeatable;
	}
	
	/**
	 * Gets a target element/object of this interaction
	 * @return
	 */
	public String getTarget() {
		return this.mTarget;
	}
	
	/**
	 * Gets an event of this interaction
	 * @return A event type
	 */
	public String getEvent() {
		return this.mEvent;
	}
	
	/**
	 * Gets raw string of an event modifier
	 * @return An event modifier
	 * @deprecated
	 */
	public String getEventModifier() {
		return this.mEventModifier;
	}
	
	/**
	 * Gets an event modifier type
	 * @return An event modifier type
	 */
	public int getEventModifierType() {
		return Event.EventModifier.getType(this.mEventModifier);
	}
	
	/**
	 * Gets a callback function of the interaction
	 * @return A callback function name
	 */
	public String getCallback() {
		return this.mCallback;
	}
	

	/**
	 * 
	 * @return
	 */
	public boolean getRepeatable() {
		return this.mRepeatable;
	}
}
