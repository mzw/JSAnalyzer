package jp.mzw.jsanalyzer.parser;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;

public class JSCode extends Code {
	protected Attribute mEventAttr;
	
	public JSCode(String code, Element html, String url, int type) {
		super(code, html, url, type);
		
		this.mEventAttr = null;
	}
	
	public void setEventAttr(Attribute event) {
		this.mEventAttr = event;
	}
	public Attribute getEventAttr() {
		return this.mEventAttr;
	}
}
