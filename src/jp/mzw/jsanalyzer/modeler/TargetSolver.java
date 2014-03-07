package jp.mzw.jsanalyzer.modeler;

import jp.mzw.jsanalyzer.util.StringUtils;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.Scope;
import org.mozilla.javascript.ast.StringLiteral;


/**
 * To be debugged
 * @author Yuta Maezawa
 * @deprecated
 */
public class TargetSolver {


	public static String getElementId(FunctionCall funcCallAstNode) {
		if("document.getElementById".equals(funcCallAstNode.getTarget().toSource())) {
			AstNode argAstNode = funcCallAstNode.getArguments().get(0);
			if(argAstNode instanceof StringLiteral) {
				String id = StringUtils.removeQuote(argAstNode.toSource());
				return id;
			}
			else {
				System.out.println("Unknown argument: " + argAstNode.getClass() + ", src= " + argAstNode.toSource() + "; func is: " + funcCallAstNode.getTarget().toSource());
			}
		}
		
		else {
			System.out.println("Unknown function: " + funcCallAstNode.getTarget().toSource());
		}
		return null;
	}
	
	
	public static String getElementId(Name nameAstNode) { // Needs data flow analysis
		return nameAstNode.toSource();
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
