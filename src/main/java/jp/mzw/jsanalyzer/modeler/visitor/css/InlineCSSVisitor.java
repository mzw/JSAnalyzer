package jp.mzw.jsanalyzer.modeler.visitor.css;

import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;

import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.core.IdGen;
import jp.mzw.jsanalyzer.modeler.EnDisableManager;
import jp.mzw.jsanalyzer.modeler.EnDisableManager.EnDisable;
import jp.mzw.jsanalyzer.modeler.model.graph.CallGraph;
import jp.mzw.jsanalyzer.modeler.visitor.CSSDetectionVisitor;
import jp.mzw.jsanalyzer.parser.CSSCode;
import jp.mzw.jsanalyzer.parser.CSSParser;
import jp.mzw.jsanalyzer.parser.Code;
import jp.mzw.jsanalyzer.parser.HTMLParser;
import jp.mzw.jsanalyzer.rule.CSSControl;

public class InlineCSSVisitor extends CSSDetectionVisitor{
	protected CSSCode cssCode;
	@Override
	public boolean detect(CSSCode cssCode) {
		this.cssCode = cssCode;
		
		if (this.cssCode.isInline()){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public void process(Analyzer analyzer, HTMLParser htmlParser, EnDisableManager edManager) {
		// To be debugged
		// Cannot parse in-line CSS code because it contains only CSS declarations
		CSSCode tmp_CSSCode = new CSSCode(IdGen.genId() + "{" + this.cssCode.getCode() + "}", cssCode.getHTMLElement(), analyzer.getProject().getUrl(), Code.Inline);
		CSSParser tmp_CSSParser = new CSSParser(tmp_CSSCode);
		tmp_CSSParser.parse();
		
//		for(CSSStyleRule cssStyleRule : cssCode.getCSSStyleRuleList()) {
		for(CSSStyleRule cssStyleRule : tmp_CSSCode.getCSSStyleRuleList()) {
			 CSSStyleDeclaration style = cssStyleRule.getStyle();
			 for(int i = 0; i < style.getLength(); i++) { // Search all CSS properties in this CSS code
				 String prop = style.item(i);
				 String val = style.getPropertyValue(prop);
				 
				 CSSControl cssControl = analyzer.getRuleManager().isControlCSSProperty(prop);
				 if(cssControl != null) { // Matches CSS control property

					 // Add
					 EnDisable ed = new EnDisable(cssCode.getHTMLElement(), prop, val, cssCode.getType(), cssControl);
					 edManager.add(CallGraph.getInitNode().getId(), ed);
				 }
			 }
			
		}
	}
}