package jp.mzw.jsanalyzer.parser;

import jp.mzw.jsanalyzer.rule.Trigger;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;

public class JSCode extends Code {
	protected Attribute mEventAttr;
	protected Trigger mTriggerRule;
	
	public JSCode(String code, Element html, String url, int type) {
		super(code, html, url, type);
		
		this.mEventAttr = null;
	}
	
	public void setEventAttr(Attribute event, Trigger rule) {
		this.mEventAttr = event;
		this.mTriggerRule = rule;
	}
	public Attribute getEventAttr() {
		return this.mEventAttr;
	}
	public Trigger getTriggerRule() {
		return this.mTriggerRule;
	}
}
