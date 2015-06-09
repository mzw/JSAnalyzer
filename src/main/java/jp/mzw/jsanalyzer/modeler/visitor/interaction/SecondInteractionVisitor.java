package jp.mzw.jsanalyzer.modeler.visitor.interaction;

import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.NewExpression;
import org.mozilla.javascript.ast.ObjectLiteral;
import org.mozilla.javascript.ast.ObjectProperty;
import org.mozilla.javascript.ast.PropertyGet;

import jp.mzw.jsanalyzer.core.LimitationManager;
import jp.mzw.jsanalyzer.core.LimitationManager.Limitation;
import jp.mzw.jsanalyzer.modeler.model.graph.CallGraph;
import jp.mzw.jsanalyzer.modeler.model.graph.Edge;
import jp.mzw.jsanalyzer.modeler.model.graph.Node;
import jp.mzw.jsanalyzer.modeler.visitor.InteractionDetectionVisitor;
import jp.mzw.jsanalyzer.rule.Function;
import jp.mzw.jsanalyzer.rule.Rule;
import jp.mzw.jsanalyzer.rule.RuleManager;
import jp.mzw.jsanalyzer.rule.Trigger;
import jp.mzw.jsanalyzer.util.StringUtils;

public class SecondInteractionVisitor extends InteractionDetectionVisitor{
	protected Function function;

	@Override
	public boolean detect(Node node, RuleManager rule) {
		String src = node.getAstNode().toSource();
		this.function = rule.isFunction(src);
		
		if (this.function !=null && node.getAstNode().getParent() instanceof PropertyGet &&
				node.getAstNode().getParent().getParent() instanceof FunctionCall){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public void process(Node node, CallGraph callGraph) {
		PropertyGet propAstNode = (PropertyGet)node.getAstNode().getParent();
		AstNode targetAstNode = propAstNode.getTarget();
		
		int cbArgNum = Rule.hasArgNum(this.function.getCallback());
		if(cbArgNum != -1) {
			FunctionCall funcCall = (FunctionCall)node.getAstNode().getParent().getParent();
			List<AstNode> argListAstNode = funcCall.getArguments();
//			if(argListAstNode.size() == 0 || argListAstNode.get(cbArgNum) == null) {
			if(argListAstNode.size() == 0 || argListAstNode.size() < cbArgNum + 1) {
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

				if(!"".equals(this.function.getEvent())) { // Due to just callback functions, e.g. jQuery.each

					int evArgNum = Rule.hasArgNum(this.function.getEvent());
					/// Ex. $("#ID").bind("event", callback);
					if(evArgNum != -1 && argListAstNode.size() != 0 && argListAstNode.get(evArgNum) != null) {
						AstNode evAstNode = argListAstNode.get(evArgNum);
						edge.setEvent(targetAstNode, evAstNode, this.function);
						node.setNodeType(Node.Potential);
					}
					/// Ex. $("#ID").event(callback);
					else {
						edge.setEvent(targetAstNode, node.getAstNode(), this.function);
						node.setNodeType(Node.Potential);
					}
					
				}
				callGraph.addEdge(edge);
//				TextFileUtils.registSnapchot(callGraph.toDot());
			}
			
			// e.g. jQuery.error(callback) is an potential function
			// but data objects can have the same name property
			else {
				//following "this" is fragile
				StringUtils.printError(this, "Make sure this is not potential function call", argAstNode.toSource());
			}
			
		} else {
			//following "this" is fragile
			StringUtils.printError(this, "Invlid potential rule?", this.function.getEvent() + ", " + node.getAstNode().toSource());
		}
	}
	
}