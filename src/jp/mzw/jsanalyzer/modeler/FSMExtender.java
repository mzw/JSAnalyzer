package jp.mzw.jsanalyzer.modeler;

import java.util.HashMap;
import java.util.List;

import org.jsoup.nodes.Element;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;

import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.core.IdGen;
import jp.mzw.jsanalyzer.modeler.model.CallGraph;
import jp.mzw.jsanalyzer.parser.CSSCode;
import jp.mzw.jsanalyzer.parser.CSSParser;
import jp.mzw.jsanalyzer.parser.HTMLParser;
import jp.mzw.jsanalyzer.parser.JSCode;
import jp.mzw.jsanalyzer.parser.JSParser;
import jp.mzw.jsanalyzer.rule.CSSControl;
import jp.mzw.jsanalyzer.rule.RuleManager;

public class FSMExtender extends Modeler {
	public FSMExtender(Analyzer analyzer) {
		super(analyzer);
	}
	
	public CallGraph createExtendedCallGraph() {
		System.out.println("Extends call graph");
		
		HTMLParser htmlParser = this.parseHTMLCode();
		this.analyzeControlPropertyByCSSCode(htmlParser);
		
		return null;
	}
	

	/**
	 * Gets HTML parser 
	 * @return HTML parser
	 */
	protected HTMLParser parseHTMLCode() {
		String baseUrl = this.mAnalyzer.getProject().getUrl();
		String dir = this.mAnalyzer.getProject().getDir();
		
		HTMLParser htmlParser = HTMLParser.createHTMLParser(baseUrl, dir);
		
		return htmlParser;
	}
	
	/**
	 * 
	 * @param htmlParser
	 */
	protected void analyzeControlPropertyByCSSCode(HTMLParser htmlParser) {
		
		HashMap<Element, HashMap<String, Boolean>> hash_HTMLElement_ControlProperties =
				new HashMap<Element, HashMap<String, Boolean>>();
		
		// First analyzes embedded and external CSS codes because lower priority
		System.out.println("===== Embedded and external CSS code =====");
		for(CSSCode cssCode : htmlParser.getCSSCodeList()) {
			if(!cssCode.isInline()) { // Represents embedded and external CSS codes
				for(CSSStyleRule cssStyleRule : cssCode.getCSSStyleRuleList()) {
					String selector = cssStyleRule.getSelectorText();
					
					 CSSStyleDeclaration style = cssStyleRule.getStyle();
					 for(int i = 0; i < style.getLength(); i++) { // Search all CSS properties in this CSS code
						 String prop = style.item(i);
						 String val = style.getPropertyValue(prop);
						 
						 // Determines whether this CSS property is control one or not
						 CSSControl cssControl = this.mAnalyzer.getRuleManager().isControlCSSProperty(prop);
						 if(cssControl != null) { // Matches CSS control property

							 System.out.println("Control: " + prop + ", " + val + " :: " + cssControl.disabled(val));
							 for(Element elm : htmlParser.getDoc().select(selector)) {
								 
							 }
							 
							 
						 } else { // Not control CSS property
							 // NOP
						 }
						 
					 }
					
				}
			}
		}
		
		// Then updates using in-line CSS codes because highest priority
		System.out.println("===== Inline CSS code=====");
		for(CSSCode cssCode : htmlParser.getCSSCodeList()) {
			if(cssCode.isInline()) {
				
				// To be debugged
				// Cannot parse in-line CSS code because it contains only CSS declarations
				CSSCode tmp_CSSCode = new CSSCode(IdGen.genId() + "{" + cssCode.getCode() + "}", cssCode.getHTMLElement(), true);
				CSSParser tmp_CSSParser = new CSSParser(tmp_CSSCode);
				tmp_CSSParser.parse();
				
//				for(CSSStyleRule cssStyleRule : cssCode.getCSSStyleRuleList()) {
				for(CSSStyleRule cssStyleRule : tmp_CSSCode.getCSSStyleRuleList()) {
					 CSSStyleDeclaration style = cssStyleRule.getStyle();
					 for(int i = 0; i < style.getLength(); i++) { // Search all CSS properties in this CSS code
						 String prop = style.item(i);
						 String val = style.getPropertyValue(prop);
						 
						 CSSControl cssControl = this.mAnalyzer.getRuleManager().isControlCSSProperty(prop);
						 if(cssControl != null) { // Matches CSS control property
							 
							 System.out.println("\tControl: " + prop + ", " + val);
							 System.out.println("\t\t" + cssCode.getHTMLElement().toString());
						 }
						 
					 }
					
				}
			}
		}
	}
}
