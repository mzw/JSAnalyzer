package jp.mzw.jsanalyzer.modeler.model;

public class State extends Node {

	protected Node mNode;
	
	public State(Node node) {
		super(node.getAstNode(), node.getJSCode());
		
		this.mNode = node;
	}
}
