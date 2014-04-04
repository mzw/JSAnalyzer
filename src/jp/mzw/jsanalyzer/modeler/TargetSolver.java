package jp.mzw.jsanalyzer.modeler;

import java.util.HashMap;

import jp.mzw.jsanalyzer.modeler.model.graph.CallGraph;
import jp.mzw.jsanalyzer.modeler.model.graph.Node;
import jp.mzw.jsanalyzer.util.StringUtils;

import org.apache.commons.lang3.tuple.Pair;
import org.mozilla.javascript.ast.Assignment;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.FunctionCall;
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
	
	private HashMap<Pair<Scope, String>, String> mVarSrc;
	
	public TargetSolver() {
		this.mVarSrc = new HashMap<Pair<Scope, String>, String>();
	}
	
	public String findElementId(Scope scope, String varName) {
		
		for(Pair<Scope, String> var : this.mVarSrc.keySet()) {
			Scope _scope = var.getKey();
			String _varName = var.getValue();
			if(_scope == scope && _varName.equals(varName)) {
				return this.mVarSrc.get(var);
			}
		}
		
		return null;
	}
	
	public void findTargetCandidates(CallGraph callGraph) {
		for(Node node : callGraph.getNodeList()) {
			AstNode astNode = node.getAstNode();
			
			/// (var foo = ) document.getElementBy("ID");
			if(astNode instanceof FunctionCall) {
				String idString = TargetSolver.getElementIdBy((FunctionCall) astNode);
				
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
					System.err.println("idAstNode is NOT StringLiteral@TargetSolver#findTargetCandidates: " + idAstNode.getClass());
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
	public static String getElementIdBy(FunctionCall funcCallAstNode) {
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
			boolean isJQuery = false; // when Prototype, false
			AstNode idAstNode = funcCallAstNode.getArguments().get(0);
			String idString = "";
			if(idAstNode instanceof StringLiteral) { // "ID"
				if(isJQuery) {
					idString = StringUtils.removeQuote(idAstNode.toSource()).substring(1);
				} else { // Prototype
					idString = StringUtils.removeQuote(idAstNode.toSource());
				}
//				System.out.println("TargetSolver#getElementIdBy: " + idString);
				return idString;
			}
			else {
				System.err.println("idAstNode is NOT StringLiteral@TargetSolver#getElementId: " +
						idAstNode.getClass() + ", src= " + idAstNode.toSource() + "; func is: " + funcName);
			}
			System.out.println("Now: " + funcCallAstNode.getTarget().toSource());
		}
		else {
//			System.out.println("Built-in or Library function?: " + funcCallAstNode.getTarget().toSource());
//			return funcCallAstNode.getTarget().toSource();
		}
		return null;
	}
	
	public static Scope getParentScope(AstNode astNode) {
		AstNode parent = astNode.getParent();
		if(parent instanceof Scope) {
			return (Scope)parent;
		} else {
			return getParentScope(parent);
		}
	}
	
}
