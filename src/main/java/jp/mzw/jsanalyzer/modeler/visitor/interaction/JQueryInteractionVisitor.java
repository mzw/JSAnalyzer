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
import jp.mzw.jsanalyzer.rule.Rule;
import jp.mzw.jsanalyzer.rule.RuleManager;
import jp.mzw.jsanalyzer.rule.Trigger;

public class JQueryInteractionVisitor extends InteractionDetectionVisitor{
	protected Trigger trigger;

	@Override
	public boolean detect(Node node, RuleManager rule) {
		String src = node.getAstNode().toSource();
		this.trigger = rule.isTrigger(src);
		
		if (this.trigger !=null && node.getAstNode().getParent() instanceof ObjectProperty &&
				((ObjectProperty)node.getAstNode().getParent()).getLeft() == node.getAstNode() &&
				node.getAstNode().getParent().getParent() instanceof ObjectLiteral &&
				node.getAstNode().getParent().getParent().getParent() instanceof FunctionCall){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public void process(Node node, CallGraph callGraph) {
		AstNode cbAstNode = ((ObjectProperty)node.getAstNode().getParent()).getRight();
		Node fromNode = callGraph.getNode((ObjectProperty)node.getAstNode().getParent());
		Node toNode = callGraph.getNode(callGraph.getFunctionNode(cbAstNode));

		Edge edge = new Edge(fromNode.getId(), toNode.getId());
		
		FunctionCall target = (FunctionCall)node.getAstNode().getParent().getParent().getParent();
		edge.setEvent(target, node.getAstNode(), this.trigger);
		
		node.setNodeType(Node.Trigger);
		callGraph.addEdge(edge);
//		TextFileUtils.registSnapchot(callGraph.toDot());
	}
	
}