package jp.mzw.jsanalyzer.modeler.model.fsm;

import org.mozilla.javascript.ast.AstRoot;

import jp.mzw.jsanalyzer.modeler.model.graph.Node;

public class Exit extends State {

	public Exit() {
		super(new Node(new AstRoot(), null));
	}
	
	@Override
	public String getDotLabel() {
		String ret = "[label=\"\",shape=doublecircle,style=filled,color=black,fixedsize=true,width=0.3]";
		return ret;
	}
}
