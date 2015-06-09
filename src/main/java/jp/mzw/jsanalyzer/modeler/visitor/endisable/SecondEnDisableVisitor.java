package jp.mzw.jsanalyzer.modeler.visitor.endisable;

import org.mozilla.javascript.ast.Assignment;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.PropertyGet;

import jp.mzw.jsanalyzer.core.IdGen;
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

public class SecondEnDisableVisitor extends EnDisableDetectionVisitor{
	protected Control control;
	
	@Override
	public boolean detect(Node node, RuleManager rule) {
		String src = node.getAstNode().toSource();
		this.control = rule.isControl(src);
		
		if(this.control != null && this.control instanceof JSControl &&
				node.getAstNode().getParent() instanceof PropertyGet &&
				node.getAstNode().getParent().getParent() instanceof FunctionCall){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public void process(Node node, CallGraph callGraph,
			EnDisableManager edManager) {
		FunctionCall funcCall = (FunctionCall)node.getAstNode().getParent().getParent();
		
		String prop = IdGen.genId();
		String val = IdGen.genId();
		
		int propArgNum = Rule.hasArgNum(((JSControl)this.control).getProp());
		if((propArgNum != -1) && (propArgNum < funcCall.getArguments().size())) {
			AstNode argAstNode = funcCall.getArguments().get(propArgNum);
			
			prop = argAstNode.toSource();
		}
		int valArgNum = Rule.hasArgNum(((JSControl)this.control).getValue());
		if((valArgNum != -1) && (valArgNum < funcCall.getArguments().size())) {
			AstNode argAstNode = funcCall.getArguments().get(valArgNum);
			
			val = argAstNode.toSource();
		}
		
		// Re-match
		// Because "setAttribute", "jQuery.attr", and so on are not always used for enabling/disabling interactions
		// $("id").setAttribute("class", "my_class");
		if(((JSControl)this.control).rematch(prop, val)) {
			EnDisable ed = new EnDisable(
					((PropertyGet)node.getAstNode().getParent()).getTarget(),
					prop,
					val,
					node.getJSCode().getType(),
					this.control);
			
			edManager.add(callGraph.getNode(funcCall).getId(), ed);
			node.setNodeType(Node.Control);
//			TextFileUtils.registSnapchot(callGraph.toDot());
		}
	}
}