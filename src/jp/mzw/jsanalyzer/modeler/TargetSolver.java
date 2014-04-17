package jp.mzw.jsanalyzer.modeler;

import java.util.HashMap;

import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.core.LimitationManager;
import jp.mzw.jsanalyzer.core.LimitationManager.Limitation;
import jp.mzw.jsanalyzer.modeler.model.graph.CallGraph;
import jp.mzw.jsanalyzer.modeler.model.graph.Node;
import jp.mzw.jsanalyzer.util.StringUtils;

import org.apache.commons.lang3.tuple.Pair;
import org.mozilla.javascript.ast.Assignment;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.InfixExpression;
import org.mozilla.javascript.ast.KeywordLiteral;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.PropertyGet;
import org.mozilla.javascript.ast.Scope;
import org.mozilla.javascript.ast.StringLiteral;
import org.mozilla.javascript.ast.VariableInitializer;


/**
 * Finds HTML elements identified by "id" attributes.
 * These HTML elements can be event targets of interactions with Ajax applications.
 * @author Yuta Maezawa
 */
public class TargetSolver {
	
	/**
	 * Has JavaScript assignments to HTML element ID attributes.
	 * This key is a pair of the assignment scope and the variable name.
	 * And this value is the ID value.
	 */
	private HashMap<Pair<Scope, String>, String> mVarSrc;
	
	/**
	 * Constructor
	 */
	public TargetSolver() {
		this.mVarSrc = new HashMap<Pair<Scope, String>, String>();
	}
	
	/**
	 * Finds the ID value that is assigned by using given variable name in given scope
	 * @param scope A scope of the variable
	 * @param varName A variable name
	 * @return HTML element ID attribut value, or null when cannot find
	 */
	public String findElementId(Scope scope, String varName) {
		
		for(Pair<Scope, String> var : this.mVarSrc.keySet()) {
			Scope _scope = var.getKey();
			String _varName = var.getValue();
			
			if(TargetSolver.isChildScope(_scope, scope) && _varName.equals(varName)) {
				return this.mVarSrc.get(var);
			}
		}
		
		return null;
	}
	
	/**
	 * Should preliminarily obtains JavaScirpt statements that assing values to HTML element ID attributes
	 * @param callGraph Has JavaScript code fragments
	 */
	public void findTargetCandidates(CallGraph callGraph, Analyzer analyzer) {
		for(Node node : callGraph.getNodeList()) {
			AstNode astNode = node.getAstNode();
			
			/// (var foo = ) document.getElementBy("ID");
			if(astNode instanceof FunctionCall) {
				FunctionCall funcCallAstNode = (FunctionCall) astNode;
				String idString = TargetSolver.getElementIdBy(funcCallAstNode, analyzer);
				
				/// Cannot parse id value, then does not regist this candidate
				if(idString == null) {
					continue;
				}
				
				/// Data flow
				/// var foo = TARGET
				if(astNode.getParent() instanceof VariableInitializer &&
						((VariableInitializer)astNode.getParent()).getInitializer() == astNode) { // TARGET
					AstNode varAstNode = ((VariableInitializer)astNode.getParent()).getTarget(); // foo
					String varName = varAstNode.toSource();
					Scope varScope = TargetSolver.getParentScope(varAstNode);
					Pair<Scope, String> var = Pair.of(varScope, varName); // A variable can be identified by its name and scope
					this.mVarSrc.put(var, idString);
				} else if(astNode.getParent() instanceof PropertyGet) {
					// NOP
				}
				/// foo.setAttribute("id", "value");
				else if(funcCallAstNode.getTarget() instanceof PropertyGet) {
					PropertyGet propGetAstNode = (PropertyGet)(funcCallAstNode.getTarget());
					String varName = propGetAstNode.getTarget().toSource();
					Scope varScope = TargetSolver.getParentScope(propGetAstNode);
					Pair<Scope, String> var = Pair.of(varScope, varName); // A variable can be identified by its name and scope
					this.mVarSrc.put(var, idString);
				} else {
//					System.out.println(astNode.getParent().getClass());
//					System.out.println("\t" + astNode.getParent().toSource());
				}
			}
			/// element.id = "ID";
			else if(astNode instanceof PropertyGet &&
					"id".equals(((PropertyGet)astNode).getProperty().toSource()) &&
					astNode.getParent() instanceof Assignment &&
					((Assignment)astNode.getParent()).getLeft() == astNode) { // to prevent, e.g., var foo = element.id;
				PropertyGet propAstNode = (PropertyGet)astNode;
				
				AstNode idAstNode = ((Assignment)astNode.getParent()).getRight();
				String idString = "";
				if(idAstNode instanceof StringLiteral) { // "ID"
					idString = StringUtils.removeQuote(idAstNode.toSource());
				}
				else {
					System.err.println("idAstNode is NOT StringLiteral@TargetSolver#findTargetCandidates: " + idAstNode.getClass() + ", " + astNode.getParent().toSource());
				}
				
				if(propAstNode.getTarget() instanceof Name) {
					String varName = propAstNode.getTarget().toSource();
					Scope varScope = TargetSolver.getParentScope(propAstNode);
					Pair<Scope, String> var = Pair.of(varScope, varName); // A variable can be identified by its name and scope
					this.mVarSrc.put(var, idString);
				} else {
					System.out.println("Unknown class of PropertyGet#getTarget@TargetSolver#findTargetCandidates: "
							+ propAstNode.getTarget().getClass() + ", "
							+ propAstNode.getTarget().toSource());
				}
			}
		}
	}
	
	/**
	 * Gets element ID value
	 * @param funcCallAstNode AstNode representing DOM manipulation function
	 * @return Element ID value
	 * @deprecated
	 */
	public static String getElementIdBy(FunctionCall funcCallAstNode, Analyzer analyzer) {
		String funcName = funcCallAstNode.getTarget().toSource();
		/// document.getElementById("ID").onclick = function() {...}
		if("document.getElementById".equals(funcName)) {
			AstNode idAstNode = funcCallAstNode.getArguments().get(0);
			String idString = "";
			if(idAstNode instanceof StringLiteral) { // "ID"
				idString = StringUtils.removeQuote(idAstNode.toSource());
				return idString;
			}
			else {
				System.err.println("idAstNode is NOT StringLiteral@TargetSolver#getElementId: " +
						idAstNode.getClass() + ", src= " + idAstNode.toSource() + "; func is: " + funcName);
			}
		}
		/// $("CSS Query") in jQuery
		/// Or $("ID") in Prototype
		else if("$".equals(funcName)) {
			AstNode idAstNode = funcCallAstNode.getArguments().get(0);
			String idString = "";
			
			boolean is_prototype_true_jquery_false = true;
			for(String rulefiles : analyzer.getProject().getRuleFilenames()) {
				if(rulefiles.contains("prototype.xml")) {
					is_prototype_true_jquery_false = true;
				} else if(rulefiles.contains("jquery.xml")) {
					is_prototype_true_jquery_false = false;
				}
			}
			
			/// Prototype
			if(idAstNode instanceof StringLiteral) {
				/// $("ID")
				if(is_prototype_true_jquery_false) {
					idString = StringUtils.removeQuote(idAstNode.toSource());
				}
				/// $("#ID"): Note that "#ID" can be css query, such as ".CLASS"
				else {
					idString = StringUtils.removeQuote(idAstNode.toSource()).substring(1);
				}
//				System.out.println("TargetSolver#getElementIdBy: " + idString);
				return idString;
			}
			/// jQuery
			/// Ex. $(this).attr('action'); @2020m
			else if(idAstNode instanceof KeywordLiteral) {
				LimitationManager.addLimitation(new Limitation(
						funcCallAstNode.toSource(),
						Limitation.JS_CANNOT_DETERMINE_INTERACTION_TARGET,
						"'this' instance cannot be determined"));
			}
			/// Infix expression: difficult to determine these in a static manner
			/// Ex. $('#' + ename + 'Btn') @include.js
			else if(idAstNode instanceof InfixExpression) {
				LimitationManager.addLimitation(new Limitation(
						funcCallAstNode.toSource(),
						Limitation.JS_Infix_Node,
						"This is one of the challenges in static analysis methods"));
			}
			/// Ex. $(window).width() @include.js
			/// window or user-defined variables: out of our current ananlysis scope
			else if(idAstNode instanceof Name) {
				LimitationManager.addLimitation(new Limitation(
						funcCallAstNode.toSource(),
						Limitation.JS_Dataflow,
						"Currently ignore to analyze dataflow"));
			}
			else {
				System.err.println("idAstNode is NOT StringLiteral@TargetSolver#getElementId: " +
						idAstNode.getClass() + ", src= " + idAstNode.toSource() + "; func is: " + funcName);
			}
		}
		/// element.setAttribute("id", "value");
		else if(funcCallAstNode.getTarget() instanceof PropertyGet &&
				"setAttribute".equals(((PropertyGet)funcCallAstNode.getTarget()).getProperty().toSource())) {
			AstNode attrAstNode = funcCallAstNode.getArguments().get(0);
			String attr = StringUtils.removeQuote(attrAstNode.toSource());
			if("id".equals(attr)) {
				AstNode valueAstNode = funcCallAstNode.getArguments().get(1);
				String idString = StringUtils.removeQuote(valueAstNode.toSource());
				return idString;
			} else {
//				System.out.println("Not ID assigning function call: " + funcCallAstNode.toSource());
			}
		}
		else {
//			System.out.println("Built-in or Library function?: " + funcCallAstNode.getTarget().toSource() + ", " + funcCallAstNode.getTarget().getClass());
//			return funcCallAstNode.getTarget().toSource();
		}
		return null;
	}
	
	
	/**
	 * Finds scope of given AST node
	 * @param astNode Given AST node
	 * @return Its Scope AST node
	 */
	public static Scope getParentScope(AstNode astNode) {
		if(astNode == null) {
			return null;
		}
		AstNode parent = astNode.getParent();
		if(parent instanceof Scope) {
			return (Scope)parent;
		} else if(parent instanceof AstRoot) {
			return null;
		} else {
			return getParentScope(parent);
		}
	}

	/**
	 * Determines whether given parent scope includes given child scope or not
	 * @param prent Parent-candidate scope AST node
	 * @param child Given scope node
	 * @return True represents the given child is in the given parent, otherwise not
	 */
	public static boolean isChildScope(Scope prent, Scope child) {
		if(child == null) {
			return false;
		}
		if(prent == child) {
			return true;
		}
		return TargetSolver.isChildScope(prent, TargetSolver.getParentScope(child));
	}
	
}
