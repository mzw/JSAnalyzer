package jp.mzw.jsanalyzer.modeler.model.fsm;

import org.mozilla.javascript.ast.AstRoot;

import jp.mzw.jsanalyzer.modeler.model.graph.Node;

public class Entry extends State {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Entry() {
		super(new Node(new AstRoot(), null));
	}
	
	@Override
	public String getDotLabel() {
		String ret = "[label=\"\",shape=circle,style=filled,color=black,fixedsize=true,width=0.3]";
		return ret;
	}
}
