package jp.mzw.jsanalyzer.modeler.visitor.interaction;

import org.mozilla.javascript.ast.Assignment;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.PropertyGet;

import jp.mzw.jsanalyzer.modeler.model.graph.CallGraph;
import jp.mzw.jsanalyzer.modeler.model.graph.Edge;
import jp.mzw.jsanalyzer.modeler.model.graph.Node;
import jp.mzw.jsanalyzer.modeler.visitor.InteractionDetectionVisitor;
import jp.mzw.jsanalyzer.rule.Rule;
import jp.mzw.jsanalyzer.rule.RuleManager;
import jp.mzw.jsanalyzer.rule.Trigger;

public class BuiltinInteractionVisitor extends InteractionDetectionVisitor{
	protected Trigger trigger;

	@Override
	public boolean detect(Node node, RuleManager rule) {
		String src = node.getAstNode().toSource();
		this.trigger = rule.isTrigger(src);
		
		if (this.trigger !=null && node.getAstNode().getParent() instanceof PropertyGet &&
				node.getAstNode().getParent().getParent() instanceof Assignment &&
				((Assignment)node.getAstNode().getParent().getParent()).getLeft() == node.getAstNode().getParent()){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public void process(Node node, CallGraph callGraph) {
		AstNode cbAstNode = ((Assignment)node.getAstNode().getParent().getParent()).getRight();
		Node fromNode = callGraph.getNode((Assignment)node.getAstNode().getParent().getParent());
		Node toNode = callGraph.getNode(callGraph.getFunctionNode(cbAstNode));
		
		Edge edge = new Edge(fromNode.getId(), toNode.getId());
		
		PropertyGet parent = (PropertyGet)node.getAstNode().getParent();
		edge.setEvent(parent.getTarget(), node.getAstNode(), this.trigger);
		
		node.setNodeType(Node.Trigger);
		callGraph.addEdge(edge);
//		TextFileUtils.registSnapchot(callGraph.toDot());
	}
	
}