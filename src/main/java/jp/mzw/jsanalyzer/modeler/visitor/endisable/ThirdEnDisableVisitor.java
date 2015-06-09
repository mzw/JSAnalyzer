package jp.mzw.jsanalyzer.modeler.visitor.endisable;

import org.mozilla.javascript.ast.Assignment;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.PropertyGet;

import jp.mzw.jsanalyzer.modeler.EnDisableManager;
import jp.mzw.jsanalyzer.modeler.EnDisableManager.EnDisable;
import jp.mzw.jsanalyzer.modeler.model.graph.CallGraph;
import jp.mzw.jsanalyzer.modeler.model.graph.Edge;
import jp.mzw.jsanalyzer.modeler.model.graph.Node;
import jp.mzw.jsanalyzer.modeler.visitor.EnDisableDetectionVisitor;
import jp.mzw.jsanalyzer.modeler.visitor.InteractionDetectionVisitor;
import jp.mzw.jsanalyzer.rule.Control;
import jp.mzw.jsanalyzer.rule.JSControl;
import jp.mzw.jsanalyzer.rule.Rule;
import jp.mzw.jsanalyzer.rule.RuleManager;
import jp.mzw.jsanalyzer.rule.Trigger;
import jp.mzw.jsanalyzer.util.StringUtils;
import jp.mzw.jsanalyzer.xml.XMLAttr;

public class ThirdEnDisableVisitor extends EnDisableDetectionVisitor{
	protected Control control;
	
	@Override
	public boolean detect(Node node, RuleManager rule) {
		String src = node.getAstNode().toSource();
		this.control = rule.isControl(src);
		
		if(this.control != null && this.control instanceof JSControl &&
				node.getAstNode().getParent() instanceof FunctionCall &&
				((FunctionCall)node.getAstNode().getParent()).getTarget() instanceof PropertyGet){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public void process(Node node, CallGraph callGraph,
			EnDisableManager edManager) {
		FunctionCall funcCall = (FunctionCall)node.getAstNode().getParent();
		PropertyGet propGet = (PropertyGet)funcCall.getTarget();
		
		if(XMLAttr.RuleProp_PropTarget.equals(((JSControl)this.control).getTarget())) {
			EnDisable ed = new EnDisable(
					propGet.getTarget(),
					"",
					"",
					node.getJSCode().getType(),
					this.control);
			
			edManager.add(callGraph.getNode(funcCall).getId(), ed);
			node.setNodeType(Node.Control);
//			TextFileUtils.registSnapchot(callGraph.toDot());
			
		} else {
			StringUtils.printError(this, "Unknown JSControl.getTarget for identifying enable/disable statement", node.getAstNode().toSource());
		}
	}
}