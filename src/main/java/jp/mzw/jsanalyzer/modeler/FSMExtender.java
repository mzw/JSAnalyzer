package jp.mzw.jsanalyzer.modeler;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.nodes.Element;
import org.mozilla.javascript.ast.Assignment;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.NewExpression;
import org.mozilla.javascript.ast.ObjectLiteral;
import org.mozilla.javascript.ast.ObjectProperty;
import org.mozilla.javascript.ast.PropertyGet;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;

import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.core.IdGen;
import jp.mzw.jsanalyzer.core.LimitationManager;
import jp.mzw.jsanalyzer.core.LimitationManager.Limitation;
import jp.mzw.jsanalyzer.core.Project;
import jp.mzw.jsanalyzer.modeler.model.graph.CallGraph;
import jp.mzw.jsanalyzer.modeler.model.graph.Edge;
import jp.mzw.jsanalyzer.modeler.model.graph.Node;
import jp.mzw.jsanalyzer.modeler.visitor.CSSDetectionVisitor;
import jp.mzw.jsanalyzer.modeler.visitor.EnDisableDetectionVisitor;
import jp.mzw.jsanalyzer.modeler.visitor.InteractionDetectionVisitor;
import jp.mzw.jsanalyzer.parser.CSSCode;
import jp.mzw.jsanalyzer.parser.CSSParser;
import jp.mzw.jsanalyzer.parser.Code;
import jp.mzw.jsanalyzer.parser.HTMLParser;
import jp.mzw.jsanalyzer.rule.CSSControl;
import jp.mzw.jsanalyzer.rule.Control;
import jp.mzw.jsanalyzer.rule.Function;
import jp.mzw.jsanalyzer.rule.JSControl;
import jp.mzw.jsanalyzer.rule.Rule;
import jp.mzw.jsanalyzer.rule.RuleManager;
import jp.mzw.jsanalyzer.rule.Trigger;
import jp.mzw.jsanalyzer.util.StringUtils;
import jp.mzw.jsanalyzer.xml.XMLAttr;
import jp.mzw.jsanalyzer.modeler.EnDisableManager.EnDisable;

public class FSMExtender extends Modeler {
	
	/**
	 * Constructor
	 * @param analyzer Provides project information
	 */
	public FSMExtender(Analyzer analyzer) {
		super(analyzer);
	}
	
	/**
	 * 
	 */
	protected HTMLParser mHTMLParser;
	
	/**
	 * This should be called after {@link #createExtendedCallGraph()}
	 * @return
	 */
	public HTMLParser getHTMLParser() {
		return this.mHTMLParser;
	}
	
	/**
	 * Gets an extended call graph focusing on interactions.
	 * Simultaneously, gets enable/disable statements.
	 * @return An extended call graph and enable/disable statements
	 */
	public Pair<CallGraph, EnDisableManager> createExtendedCallGraph() {
		System.out.println("Extends call graph...");

		this.mHTMLParser = this.parseHTMLCode();
		
		EnDisableManager edManager = new EnDisableManager();
		this.mHTMLParser.setControledHTMLElementList(this.mAnalyzer.getRuleManager(), edManager);
		this.setCSSControlProperty(this.mHTMLParser, edManager);
		
		RuleManager ruleManager = this.mAnalyzer.getRuleManager();
		CallGraph callGraph = new CallGraph(this.mHTMLParser.getJSCodeList(ruleManager));
		
		this.extend(callGraph, ruleManager, edManager);
		
		return Pair.of(callGraph, edManager);
	}
	
	
	/**
	 * Extends a call graph focusing on interactions with Ajax applications (To be debugged)
	 * @param ruleManager Contains distinguishing rules
	 */
	private void extend(CallGraph callGraph, RuleManager ruleManager, EnDisableManager edManager) {
		List<InteractionDetectionVisitor> interactionList = this.mAnalyzer.getProject().getInteractionList();
		List<EnDisableDetectionVisitor> enDisableList = this.mAnalyzer.getProject().getEnDisableList();
			
		for(Node node : callGraph.getNodeList()) {
			for(InteractionDetectionVisitor v : interactionList){
				if (v.detect(node, ruleManager))
					v.process(node, callGraph);
			}

			for(EnDisableDetectionVisitor v : enDisableList){
				if(v.detect(node, ruleManager))
					v.process(node, callGraph, edManager);
			}
			
		}		
		/// Additionally, finds target candidates
		edManager.findTargetCandidates(callGraph, this.mAnalyzer);
	}
	
	/**
	 * Sets CSS properties which enable/disable interactions (to be re-factored)
	 * @param htmlParser Provides CSS codes
	 * @param edManager Manages enable/disable statements
	 */
	protected void setCSSControlProperty(HTMLParser htmlParser, EnDisableManager edManager) {
		List<CSSDetectionVisitor> csslist = this.mAnalyzer.getProject().getCSSList();
		for(CSSCode cssCode : htmlParser.getCSSCodeList()) {
			for(CSSDetectionVisitor v : csslist){
				if(v.detect(cssCode))
					v.process(this.mAnalyzer, htmlParser, edManager);
			}
		}
		for(CSSCode cssCode : htmlParser.getCSSCodeList()) {
			for(CSSDetectionVisitor v : csslist){
				if(v.detect(cssCode))
					v.process(this.mAnalyzer, htmlParser, edManager);
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
