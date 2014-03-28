package jp.mzw.jsanalyzer.modeler.model.interaction;

import jp.mzw.jsanalyzer.modeler.model.Element;
import jp.mzw.jsanalyzer.rule.InteractionRule;
import jp.mzw.jsanalyzer.rule.Rule;
import jp.mzw.jsanalyzer.xml.XMLAttr;

import org.jsoup.nodes.Attribute;
import org.mozilla.javascript.ast.AstNode;

public class Event extends Element {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Object mTargetObj;
	protected Object mEventObj;
	protected Rule mRule;
	protected int mEventModifierType;

	protected static Object mLibObj = new Object();
	protected static Object mLimitObj = new Object();
	public static boolean isLibObj(Object obj) {
		if(mLibObj == obj) {
			return true;
		}
		return false;
	}
	
	/**
	 * Constructor
	 * @param origin
	 */
	public Event(Object target, Object event, Rule rule) {
		this.mTargetObj = target;
		this.mEventObj = event;
		this.mRule = rule;
		this.mEventModifierType = Event.EventModifier.None;
	}
	
	public Object getEventObj() {
		return this.mEventObj;
	}
	
	public Object getTargetObj() {
		return this.mTargetObj;
	}
	
	/**
	 * Determines whether this really has an event
	 * @return
	 */
	public boolean hasEvent() {
		if(this.mEventObj != null) {
			return true;
		}
		return false;
	}
	
	/**
	 * Gets rule which detects this event
	 * @return
	 */
	public Rule getRule() {
		return this.mRule;
	}
	
	/**
	 * Determines whether this is a repeatable event
	 * @return
	 */
	public boolean isRepeatable() {
		if(this.mRule != null && this.mRule instanceof InteractionRule) {
			InteractionRule _rule = (InteractionRule)this.mRule;
			return _rule.getRepeatable();
		}
		
		return true;
	}
	
	/**
	 * Sets an event modifier
	 * @param modifier_type See the Event Modifier class
	 */
	public void setEventModifier(int modifier_type) {
		this.mEventModifierType = modifier_type;
	}
	
	/**
	 * Gets a string representing this event
	 * @return An event string
	 */
	private String getEventStr() {
		if(this.mEventObj == null) {
			return "";
		}
		
		if(this.mEventObj instanceof Attribute) {
			return ((Attribute)this.mEventObj).getKey();
		} else if(this.mEventObj instanceof AstNode) {
			return ((AstNode)this.mEventObj).toSource();
		}
		
		return "";
	}
	
	/**
	 * Gets a string representing this event
	 * @return
	 */
	public String getEvent() {
		String ret = "";

		switch(this.mEventModifierType) {
		case Event.EventModifier.None:
			ret += this.getEventStr();
			break;
		case Event.EventModifier.AfterMilliSec:
			ret += "after(" + this.getEventStr() + " msec)";
			break;
		case Event.EventModifier.UserClick:
			ret += "User Click";
		}
		
		return ret;
	}
	
	/**
	 * Provides event modifier types
	 * @author Yuta Maezawa
	 *
	 */
	public static class EventModifier {
		/**
		 * Event modifier types
		 */
		public static final int
			None = 0,
			AfterMilliSec = 1,
			UserClick = 2;
		
		public static int getType(String event_modifier) {
			if((XMLAttr.RuleEventModifier_AfterMilliSec.equals(event_modifier))) {
				return EventModifier.AfterMilliSec;
			} else if(XMLAttr.RuleEventModifier_UserClick.equals(event_modifier)) {
				return EventModifier.UserClick;
			}
			return EventModifier.None;
		}
	}
}
