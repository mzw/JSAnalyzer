package jp.mzw.jsanalyzer.modeler.visitor.interaction;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.NewExpression;
import org.mozilla.javascript.ast.ObjectLiteral;
import org.mozilla.javascript.ast.ObjectProperty;

import jp.mzw.jsanalyzer.modeler.model.graph.CallGraph;
import jp.mzw.jsanalyzer.modeler.model.graph.Edge;
import jp.mzw.jsanalyzer.modeler.model.graph.Node;
import jp.mzw.jsanalyzer.modeler.visitor.InteractionDetectionVisitor;
import jp.mzw.jsanalyzer.rule.Function;
import jp.mzw.jsanalyzer.rule.Rule;
import jp.mzw.jsanalyzer.rule.RuleManager;
import jp.mzw.jsanalyzer.rule.Trigger;
import jp.mzw.jsanalyzer.util.StringUtils;

public class FirstInteractionVisitor extends InteractionDetectionVisitor{
	protected Function function;

	@Override
	public boolean detect(Node node, RuleManager rule) {
		String src = node.getAstNode().toSource();
		this.function = rule.isFunction(src);
		
		if (this.function !=null && node.getAstNode().getParent() instanceof FunctionCall){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public void process(Node node, CallGraph callGraph) {
			FunctionCall funcCall = (FunctionCall)node.getAstNode().getParent();
			
			// hard coding
			AstNode eventAstNode = null;
			int eventArgNum = Rule.hasArgNum(this.function.getEvent());
			if(eventArgNum != -1) {
				eventAstNode = funcCall.getArguments().get(eventArgNum);
			} else {
				eventAstNode = node.getAstNode();
			}
			/////
			AstNode cbAstNode = null;
			int cbArgNum = Rule.hasArgNum(this.function.getCallback());
			if(cbArgNum != -1) {
				AstNode argAstNode = funcCall.getArguments().get(cbArgNum);
				cbAstNode = callGraph.getFunctionNode(argAstNode);
			} else {
				cbAstNode = node.getAstNode();
			}
			
			Node fromNode = callGraph.getNode((FunctionCall)node.getAstNode().getParent());
			Node toNode = callGraph.getNode(cbAstNode);
			
			if(fromNode == null || toNode == null) {
				//following "this" is fragile
				StringUtils.printError(this, "Make sure interaction or not", "[Rule: " + this.function.getKeyword() + "] " + node.getAstNode().toSource());
				return;
			}
			
			Edge edge = new Edge(fromNode.getId(), toNode.getId());
			edge.setEvent(eventAstNode, this.function);
			node.setNodeType(Node.Potential);
			callGraph.addEdge(edge);
//			TextFileUtils.registSnapchot(callGraph.toDot());
	}
	
}