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
import jp.mzw.jsanalyzer.modeler.model.graph.CallGraph;
import jp.mzw.jsanalyzer.modeler.model.graph.Edge;
import jp.mzw.jsanalyzer.modeler.model.graph.Node;
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
		for(Node node : callGraph.getNodeList()) {
			String src = node.getAstNode().toSource();
			
			// For interactions
			Trigger trigger = ruleManager.isTrigger(src);
			if(trigger != null) {
				this.addInteraction(callGraph, node, trigger);
			}
			Function function = ruleManager.isFunction(src);
			if(function != null) {
				this.addInteraction(callGraph, node, function);
			}
			
			// for enable/disable statements
			Control control = ruleManager.isControl(src);
			if(control != null) {
				this.addEnDisable(callGraph, node, control, edManager);
			}
			
		}
		
		/// Additionally, finds target candidates
		edManager.findTargetCandidates(callGraph, this.mAnalyzer);
	}
	
	/**
	 * Obtains enable/disable statements (to be debugged)
	 * @param callGraph
	 * @param node
	 * @param rule
	 * @param edManager
	 */
	private void addEnDisable(CallGraph callGraph, Node node, Control rule, EnDisableManager edManager) {
		// object.prop = value
		if(node.getAstNode().getParent() instanceof PropertyGet &&
				node.getAstNode().getParent().getParent() instanceof Assignment &&
				((Assignment)node.getAstNode().getParent().getParent()).getLeft() == node.getAstNode().getParent()) {
			// Callback node
			Node assignNode = callGraph.getNode((Assignment)node.getAstNode().getParent().getParent());
			
			AstNode valAstNode = ((Assignment)node.getAstNode().getParent().getParent()).getRight();
			
			EnDisable ed = new EnDisable(
					((PropertyGet)node.getAstNode().getParent()).getTarget(),
					node.getAstNode().toSource(),
					valAstNode.toSource(),
					node.getJSCode().getType(),
					rule);
			
			edManager.add(assignNode.getId(), ed);
			node.setNodeType(Node.Control);
//			TextFileUtils.registSnapchot(callGraph.toDot());
		}
		// Object.function(prop, value), e.g. $("id").attr("disabled", "disabled");
		// Object.function(prop), e.g. $("id").removeAttr("disabled");
		else if(rule instanceof JSControl &&
				node.getAstNode().getParent() instanceof PropertyGet &&
				node.getAstNode().getParent().getParent() instanceof FunctionCall) {
			FunctionCall funcCall = (FunctionCall)node.getAstNode().getParent().getParent();
			
			String prop = IdGen.genId();
			String val = IdGen.genId();
			
			int propArgNum = Rule.hasArgNum(((JSControl)rule).getProp());
			if((propArgNum != -1) && (propArgNum < funcCall.getArguments().size())) {
				AstNode argAstNode = funcCall.getArguments().get(propArgNum);
				
				prop = argAstNode.toSource();
			}
			int valArgNum = Rule.hasArgNum(((JSControl)rule).getValue());
			if((valArgNum != -1) && (valArgNum < funcCall.getArguments().size())) {
				AstNode argAstNode = funcCall.getArguments().get(valArgNum);
				
				val = argAstNode.toSource();
			}
			
			// Re-match
			// Because "setAttribute", "jQuery.attr", and so on are not always used for enabling/disabling interactions
			// $("id").setAttribute("class", "my_class");
			if(((JSControl)rule).rematch(prop, val)) {
				EnDisable ed = new EnDisable(
						((PropertyGet)node.getAstNode().getParent()).getTarget(),
						prop,
						val,
						node.getJSCode().getType(),
						rule);
				
				edManager.add(callGraph.getNode(funcCall).getId(), ed);
				node.setNodeType(Node.Control);
//				TextFileUtils.registSnapchot(callGraph.toDot());
			}
		}
		// Parent.appendChild(object) or Object.prependTo(parent)
		else if(rule instanceof JSControl &&
				node.getAstNode().getParent() instanceof FunctionCall &&
				((FunctionCall)node.getAstNode().getParent()).getTarget() instanceof PropertyGet) {
			
			FunctionCall funcCall = (FunctionCall)node.getAstNode().getParent();
			PropertyGet propGet = (PropertyGet)funcCall.getTarget();
			
			if(XMLAttr.RuleProp_PropTarget.equals(((JSControl)rule).getTarget())) {
				EnDisable ed = new EnDisable(
						propGet.getTarget(),
						"",
						"",
						node.getJSCode().getType(),
						rule);
				
				edManager.add(callGraph.getNode(funcCall).getId(), ed);
				node.setNodeType(Node.Control);
//				TextFileUtils.registSnapchot(callGraph.toDot());
				
			} else {
				StringUtils.printError(this, "Unknown JSControl.getTarget for identifying enable/disable statement", node.getAstNode().toSource());
			}
		}
		
		else {
			StringUtils.printError(this, "Cannot identify enable/disable statement",
					"[Class: " + node.getAstNode().getClass().toString() + "] " + node.getAstNode().toSource() + "\n" +
					node.getAstNode().getParent().toSource() + " (" + node.getAstNode().getLineno() + "," + node.getAstNode().getPosition() + ")"
					);
		}
	}
	
	/**
	 * Add interaction relationships coming from potential functions (to be debugged)
	 * @param node A node representing a potential function
	 * @param rule A potential rule which detect the potential function
	 */
	private void addInteraction(CallGraph callGraph, Node node, Function rule) {
		// potential_function(event, callback);
		if(node.getAstNode().getParent() instanceof FunctionCall) {
			FunctionCall funcCall = (FunctionCall)node.getAstNode().getParent();
			
			// hard coding
			AstNode eventAstNode = null;
			int eventArgNum = Rule.hasArgNum(rule.getEvent());
			if(eventArgNum != -1) {
				eventAstNode = funcCall.getArguments().get(eventArgNum);
			} else {
				eventAstNode = node.getAstNode();
			}
			/////
			AstNode cbAstNode = null;
			int cbArgNum = Rule.hasArgNum(rule.getCallback());
			if(cbArgNum != -1) {
				AstNode argAstNode = funcCall.getArguments().get(cbArgNum);
				cbAstNode = callGraph.getFunctionNode(argAstNode);
			} else {
				cbAstNode = node.getAstNode();
			}
			
			Node fromNode = callGraph.getNode((FunctionCall)node.getAstNode().getParent());
			Node toNode = callGraph.getNode(cbAstNode);
			Edge edge = new Edge(fromNode.getId(), toNode.getId());
			edge.setEvent(eventAstNode, rule);
			node.setNodeType(Node.Potential);
			callGraph.addEdge(edge);
//			TextFileUtils.registSnapchot(callGraph.toDot());
		}
		// TargetObject.potential_function(..., callback, ...)
		else if(node.getAstNode().getParent() instanceof PropertyGet &&
				node.getAstNode().getParent().getParent() instanceof FunctionCall
				) {
			PropertyGet propAstNode = (PropertyGet)node.getAstNode().getParent();
			AstNode targetAstNode = propAstNode.getTarget();
			
			int cbArgNum = Rule.hasArgNum(rule.getCallback());
			if(cbArgNum != -1) {
				FunctionCall funcCall = (FunctionCall)node.getAstNode().getParent().getParent();
				List<AstNode> argListAstNode = funcCall.getArguments();
				if(argListAstNode.size() == 0 || argListAstNode.get(cbArgNum) == null) {
					// This is not an interaction or an ivalid one, e.g., no callback
					LimitationManager.addLimitation(new Limitation(
							funcCall.toSource(),
							Limitation.JS_No_Callback,
							"[May cause false positives] Developer can dynamically change interaction callbacks.\n"
							+ "Ex) $('#id').event(cb); // Previously manipulates something in cb\n"
							+ "$('#id').event(); // Now do nothing\n"));
					return;
				}
				AstNode argAstNode = argListAstNode.get(cbArgNum);
				AstNode cbAstNode = callGraph.getFunctionNode(argAstNode);
				
				if(cbAstNode != null) {
					Node fromNode = callGraph.getNode(funcCall);
					Node toNode = callGraph.getNode(cbAstNode);
					
					Edge edge = new Edge(fromNode.getId(), toNode.getId());

					if(!"".equals(rule.getEvent())) { // Due to just callback functions, e.g. jQuery.each

						int evArgNum = Rule.hasArgNum(rule.getEvent());
						/// Ex. $("#ID").bind("event", callback);
						if(evArgNum != -1 && argListAstNode.size() != 0 && argListAstNode.get(evArgNum) != null) {
							AstNode evAstNode = argListAstNode.get(evArgNum);
							edge.setEvent(targetAstNode, evAstNode, rule);
							node.setNodeType(Node.Potential);
						}
						/// Ex. $("#ID").event(callback);
						else {
							edge.setEvent(targetAstNode, node.getAstNode(), rule);
							node.setNodeType(Node.Potential);
						}
						
					}
					callGraph.addEdge(edge);
//					TextFileUtils.registSnapchot(callGraph.toDot());
				}
				
				// e.g. jQuery.error(callback) is an potential function
				// but data objects can have the same name property
				else {
					StringUtils.printError(this, "Make sure this is not potential function call", argAstNode.toSource());
				}
				
			} else {
				StringUtils.printError(this, "Invlid potential rule?", rule.getEvent() + ", " + node.getAstNode().toSource());
			}
		}
		
		else {
			StringUtils.printError(this, "Cannot identify interaction", 
					"[Class: " + node.getAstNode().getClass().toString() + "] " + node.getAstNode().toSource() + "\n" +
					node.getAstNode().toSource() + " (" + node.getAstNode().getLineno() + "," + node.getAstNode().getPosition() + ")"
					);
		}
	}
	
	/**
	 * Adds interaction relationships coming from event attributes/properties (to be debugged)
	 * @param node A node representing a trigger
	 * @param rule A trigger rule which detects the trigger node
	 */
	private void addInteraction(CallGraph callGraph, Node node, Trigger rule) {
		
		// target.trigger = callback
		if(node.getAstNode().getParent() instanceof PropertyGet &&
				node.getAstNode().getParent().getParent() instanceof Assignment &&
				((Assignment)node.getAstNode().getParent().getParent()).getLeft() == node.getAstNode().getParent()) {
			
			AstNode cbAstNode = ((Assignment)node.getAstNode().getParent().getParent()).getRight();
			Node fromNode = callGraph.getNode((Assignment)node.getAstNode().getParent().getParent());
			Node toNode = callGraph.getNode(callGraph.getFunctionNode(cbAstNode));
			
			Edge edge = new Edge(fromNode.getId(), toNode.getId());
			
			PropertyGet parent = (PropertyGet)node.getAstNode().getParent();
			edge.setEvent(parent.getTarget(), node.getAstNode(), rule);
			
			node.setNodeType(Node.Trigger);
			callGraph.addEdge(edge);
//			TextFileUtils.registSnapchot(callGraph.toDot());
		}
		// new Target(..., {..., trigger: callback, ...}, ...)
		else if(node.getAstNode().getParent() instanceof ObjectProperty &&
				((ObjectProperty)node.getAstNode().getParent()).getLeft() == node.getAstNode() &&
				node.getAstNode().getParent().getParent() instanceof ObjectLiteral &&
				node.getAstNode().getParent().getParent().getParent() instanceof NewExpression) {
			
			AstNode cbAstNode = ((ObjectProperty)node.getAstNode().getParent()).getRight();
			Node fromNode = callGraph.getNode((ObjectProperty)node.getAstNode().getParent());
			Node toNode = callGraph.getNode(callGraph.getFunctionNode(cbAstNode));

			Edge edge = new Edge(fromNode.getId(), toNode.getId());
			
			NewExpression target = (NewExpression)node.getAstNode().getParent().getParent().getParent();
			edge.setEvent(target, node.getAstNode(), rule);
			
			node.setNodeType(Node.Trigger);
			callGraph.addEdge(edge);
//			TextFileUtils.registSnapchot(callGraph.toDot());
		}
		
		else {
			StringUtils.printError(this, "Cannot identify interaction", 
					"[Class: " + node.getAstNode().getClass().toString() + "] " + node.getAstNode().toSource() + "\n" +
					node.getAstNode().toSource() + " (" + node.getAstNode().getLineno() + "," + node.getAstNode().getPosition() + ")"
					);
		}
		
	}

	/**
	 * Sets CSS properties which enable/disable interactions (to be re-factored)
	 * @param htmlParser Provides CSS codes
	 * @param edManager Manages enable/disable statements
	 */
	protected void setCSSControlProperty(HTMLParser htmlParser, EnDisableManager edManager) {
		
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
								edManager.add(CallGraph.getInitNode().getId(), ed);
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
							 edManager.add(CallGraph.getInitNode().getId(), ed);
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
