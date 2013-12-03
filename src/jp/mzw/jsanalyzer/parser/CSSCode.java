package jp.mzw.jsanalyzer.parser;

import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;

/**
 * Represents CSS source code
 * @author Yuta Maezawa
 *
 */
public class CSSCode extends Code {

	/**
	 * Contains CSS style rules of this CSS code
	 */
	protected ArrayList<CSSStyleRule> mCSSStyleRuleList;
	
	/**
	 * Constructor
	 * @param code is CSS source code fragment
	 */
	public CSSCode(String code, Element html, boolean isInline) {
		super(code, html, isInline);
		init();
	}
	

	/**
	 * Constructor
	 * @param code is CSS source code fragment
	 * @param url 
	 */
	public CSSCode(String code, Element html, String url) {
		super(code, html, url);
		init();
	}
	
	private void init() {
		this.mCSSStyleRuleList = new ArrayList<CSSStyleRule>();
	}
	
	public void addCSSStyleRule(CSSStyleRule cssStyleRule) {
		this.mCSSStyleRuleList.add(cssStyleRule);
	}
	
	public void manipulate(Document doc) {
		if(this.isInline) {
			System.out.println(this.mHTMLElement.toString());
		}
		for(CSSStyleRule cssStyleRule : this.mCSSStyleRuleList) {
			String selector = cssStyleRule.getSelectorText();
			CSSStyleDeclaration style = cssStyleRule.getStyle();

			System.out.println(selector);
			for(Element elm : doc.select(selector)) {
				
				for(int i = 0; i < style.getLength(); i++) {
					String prop = style.item(i);
					String val = style.getPropertyValue(prop);
					
					
					System.out.println("\tprop, val: " + prop + ", " + val);
					System.out.println("\t\t: " + style.getPropertyCSSValue(prop).getCssText());
				}
				
			}
		}
	}
	
}
