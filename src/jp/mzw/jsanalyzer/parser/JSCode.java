package jp.mzw.jsanalyzer.parser;

import org.jsoup.nodes.Element;

public class JSCode extends Code {

	public JSCode(String code, Element html, boolean isInline) {
		super(code, html, isInline);
	}
	
	public JSCode(String code, Element html, String url) {
		super(code, html, url);
	}
}
