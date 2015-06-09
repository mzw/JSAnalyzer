package jp.mzw.jsanalyzer.modeler.visitor.css;

import org.jsoup.nodes.Element;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;

import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.modeler.EnDisableManager;
import jp.mzw.jsanalyzer.modeler.EnDisableManager.EnDisable;
import jp.mzw.jsanalyzer.modeler.model.graph.CallGraph;
import jp.mzw.jsanalyzer.modeler.visitor.CSSDetectionVisitor;
import jp.mzw.jsanalyzer.parser.CSSCode;
import jp.mzw.jsanalyzer.parser.CSSParser;
import jp.mzw.jsanalyzer.parser.HTMLParser;
import jp.mzw.jsanalyzer.rule.CSSControl;

public class NotInlineCSSVisitor extends CSSDetectionVisitor{
	protected CSSCode cssCode;
	@Override
	public boolean detect(CSSCode cssCode) {
		this.cssCode = cssCode;
		
		if (!this.cssCode.isInline()){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public void process(Analyzer analyzer, HTMLParser htmlParser, EnDisableManager edManager) {
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
				CSSControl cssControl = analyzer.getRuleManager().isControlCSSProperty(prop);
				if(cssControl != null) { // Matches CSS control property

					for(Element elm : htmlParser.getDoc().select(selector)) {
						// Add
						EnDisable ed = new EnDisable(elm, prop, val, cssCode.getType(), cssControl);
						edManager.add(CallGraph.getInitNode().getId(), ed);
					}
				} else { // Not control CSS property
					 // NOP
				}
				 
			}
			
		}
	}
}