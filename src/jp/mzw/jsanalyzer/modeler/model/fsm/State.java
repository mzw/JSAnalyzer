package jp.mzw.jsanalyzer.modeler.model.fsm;

import java.util.ArrayList;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;

import jp.mzw.jsanalyzer.modeler.EnDisable;
import jp.mzw.jsanalyzer.modeler.model.graph.Node;
import jp.mzw.jsanalyzer.modeler.model.interaction.Interaction;

public class State extends Node {

	/**
	 * Represents this state
	 */
	protected Node mNode;
	/**
	 * Originals
	 */
	protected List<AstNode> mAstNodeList;
	
	protected List<Interaction> mInteractionList;
	protected List<EnDisable> mEnDisableList;
	
	public State(Node node) {
		super(node);
		this.mNode = node;
		
		this.mInteractionList = new ArrayList<Interaction>();
		this.mEnDisableList = new ArrayList<EnDisable>();
	}
	
	public void addInteraction(Interaction interaction) {
		this.mInteractionList.add(interaction);
	}
	public void addEnDisable(EnDisable ed) {
		this.mEnDisableList.add(ed);
	}
	
	public boolean isDisabled(Interaction interaction) {
		
		
		return false;
	}
	
	
	
	
	
	
	
	
	
	
	
	public String getPromelaLabel() {
		return this.mId;
	}
	
	public String getDotLabel() {
		String ret = "";
		
		return ret;
	}
}
