package jp.mzw.jsanalyzer.rule;

public class Potential extends InteractionRule {
	
	/**
	 * An event of an interaction
	 */
	protected String mEvent;
	
	/**
	 * A callback function of an interaction
	 */
	protected String mCallback;
	
	/**
	 * Constructor
	 * @param func A name of function which handles an interaction as a keyword
	 * @param interact A type of the interaction
	 * @param event An event of the interaction
	 * @param callback A callback function of the interaction
	 */
	public Potential(String func, String interact, String event, String callback) {
		super(func, interact);
		this.mEvent = event;
		this.mCallback = callback;
	}
	
	/**
	 * Gets an event of this interaction
	 * @return A event type
	 */
	public String getEvent() {
		return this.mEvent;
	}
	
	/**
	 * Gets a callback function of the interaction
	 * @return A callback function name
	 */
	public String getCallback() {
		return this.mCallback;
	}
	
}
