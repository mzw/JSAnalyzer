package jp.mzw.jsanalyzer.modeler.visitor.endisable;

import org.mozilla.javascript.ast.Assignment;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.PropertyGet;

import jp.mzw.jsanalyzer.modeler.EnDisableManager;
import jp.mzw.jsanalyzer.modeler.EnDisableManager.EnDisable;
import jp.mzw.jsanalyzer.modeler.model.graph.CallGraph;
import jp.mzw.jsanalyzer.modeler.model.graph.Edge;
import jp.mzw.jsanalyzer.modeler.model.graph.Node;
import jp.mzw.jsanalyzer.modeler.visitor.EnDisableDetectionVisitor;
import jp.mzw.jsanalyzer.modeler.visitor.InteractionDetectionVisitor;
import jp.mzw.jsanalyzer.rule.Control;
import jp.mzw.jsanalyzer.rule.Rule;
import jp.mzw.jsanalyzer.rule.RuleManager;
import jp.mzw.jsanalyzer.rule.Trigger;

public class BuiltinEnDisableVisitor extends EnDisableDetectionVisitor{
	protected Control control;
	
	@Override
	public boolean detect(Node node, RuleManager rule) {
		String src = node.getAstNode().toSource();
		this.control = rule.isControl(src);
		
		if(this.control != null && node.getAstNode().getParent() instanceof PropertyGet &&
				node.getAstNode().getParent().getParent() instanceof Assignment &&
				((Assignment)node.getAstNode().getParent().getParent()).getLeft() == node.getAstNode().getParent()){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public void process(Node node, CallGraph callGraph,
			EnDisableManager edManager) {
		// Callback node
		Node assignNode = callGraph.getNode((Assignment)node.getAstNode().getParent().getParent());
		
		AstNode valAstNode = ((Assignment)node.getAstNode().getParent().getParent()).getRight();
		
		EnDisable ed = new EnDisable(
				((PropertyGet)node.getAstNode().getParent()).getTarget(),
				node.getAstNode().toSource(),
				valAstNode.toSource(),
				node.getJSCode().getType(),
				this.control);
		
		edManager.add(assignNode.getId(), ed);
		node.setNodeType(Node.Control);
//		TextFileUtils.registSnapchot(callGraph.toDot());

	}
}