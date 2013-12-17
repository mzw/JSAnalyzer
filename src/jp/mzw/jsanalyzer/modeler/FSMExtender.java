package jp.mzw.jsanalyzer.modeler;

import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.nodes.Element;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;

import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.core.IdGen;
import jp.mzw.jsanalyzer.modeler.model.CallGraph;
import jp.mzw.jsanalyzer.parser.CSSCode;
import jp.mzw.jsanalyzer.parser.CSSParser;
import jp.mzw.jsanalyzer.parser.Code;
import jp.mzw.jsanalyzer.parser.HTMLParser;
import jp.mzw.jsanalyzer.parser.JSCode;
import jp.mzw.jsanalyzer.parser.JSParser;
import jp.mzw.jsanalyzer.rule.CSSControl;
import jp.mzw.jsanalyzer.rule.RuleManager;
import jp.mzw.jsanalyzer.util.TextFileUtils;

public class FSMExtender extends Modeler {
	public FSMExtender(Analyzer analyzer) {
		super(analyzer);
	}
	
	/**
	 * Gets an extended call graph focusing on interactions.
	 * Simultaneously, gets enable/disable statements.
	 * @return An extended call graph and enable/disable statements
	 */
	public Pair<CallGraph, EnDisableManager> createExtendedCallGraph() {
		System.out.println("Extends call graph");
		
		HTMLParser htmlParser = this.parseHTMLCode();
		
		EnDisableManager edManager = new EnDisableManager();
		this.analyzeControlPropertyByCSSCode(htmlParser, edManager);
		CallGraph callGraph = this.analyzeInteractionRelationships(htmlParser, this.mAnalyzer.getRuleManager(), edManager);
		
		TextFileUtils.write("/Users/yuta/Desktop/test", "a.dot", callGraph.toDot());
		
		edManager.show();
		
		return Pair.of(callGraph, edManager);
	}
	
	
	protected CallGraph analyzeInteractionRelationships(HTMLParser htmlParser, RuleManager ruleManager, EnDisableManager edManager) {
		CallGraph cg = new CallGraph();
		for(JSCode jsCode : htmlParser.getJSCodeList(this.mAnalyzer.getRuleManager())) {
			cg.add(jsCode);
		}
		cg.extend(ruleManager);
		
		return cg;
	}
	
	
	

	/**
	 * To be re-factored
	 * @param htmlParser
	 * @param callGraph
	 * @param edManager
	 */
	protected void analyzeControlPropertyByCSSCode(HTMLParser htmlParser, EnDisableManager edManager) {
		
		// First analyzes embedded and external CSS codes because lower priority
		for(CSSCode cssCode : htmlParser.getCSSCodeList()) {
			if(!cssCode.isInline()) { // Represents embedded and external CSS codes
				for(CSSStyleRule cssStyleRule : cssCode.getCSSStyleRuleList()) {
					String selector = cssStyleRule.getSelectorText();
					
					// To be debugged
					if(CSSParser.containsCSSPseudoClass(selector)) {
						continue;
					}
					
					CSSStyleDeclaration style = cssStyleRule.getStyle();
					for(int i = 0; i < style.getLength(); i++) { // Search all CSS properties in this CSS code
						String prop = style.item(i);
						String val = style.getPropertyValue(prop);
						 
						// Determines whether this CSS property is control one or not
						CSSControl cssControl = this.mAnalyzer.getRuleManager().isControlCSSProperty(prop);
						if(cssControl != null) { // Matches CSS control property

							for(Element elm : htmlParser.getDoc().select(selector)) {
								// Add
								EnDisable ed = new EnDisable(elm, prop, val, cssCode.getType(), cssControl);
								edManager.add(CallGraph.getInitNode(), ed);
							}
						} else { // Not control CSS property
							 // NOP
						}
						 
					}
					
				}
			}
		}
		
		// Then updates using in-line CSS codes because highest priority
		for(CSSCode cssCode : htmlParser.getCSSCodeList()) {
			if(cssCode.isInline()) {
				
				// To be debugged
				// Cannot parse in-line CSS code because it contains only CSS declarations
				CSSCode tmp_CSSCode = new CSSCode(IdGen.genId() + "{" + cssCode.getCode() + "}", cssCode.getHTMLElement(), this.mAnalyzer.getProject().getUrl(), Code.Inline);
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

							 // Add
							 EnDisable ed = new EnDisable(cssCode.getHTMLElement(), prop, val, cssCode.getType(), cssControl);
							 edManager.add(CallGraph.getInitNode(), ed);
						 }
					 }
					
				}
			}
		}
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
}
