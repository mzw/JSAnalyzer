package jp.mzw.jsanalyzer.modeler.model.fsm;

import java.util.ArrayList;
import java.util.List;


import jp.mzw.jsanalyzer.modeler.EnDisable;
import jp.mzw.jsanalyzer.modeler.model.graph.Node;
import jp.mzw.jsanalyzer.modeler.model.interaction.Interaction;

public class State extends Node {

	/**
	 * Represents this state
	 */
	protected Node mNode;
	/**
	 * All nodes containing this node and origin nodes
	 */
	protected List<Node> mNodeList;
	/**
	 * Interactions which target app has at this state
	 */
	protected List<Interaction> mInteractionList;
	
	protected List<EnDisable> mEnDisableList;
	
	public State(Node node) {
		super(node);
		this.mNode = node;
		
		this.mNodeList = new ArrayList<Node>();
		this.mNodeList.add(node);
		
		this.mInteractionList = new ArrayList<Interaction>();
		this.mEnDisableList = new ArrayList<EnDisable>();
	}
	
	/**
	 * Adds origin nodes
	 * @param originNodeList Nodes which represents this state
	 */
	public void addOriginNodes(List<Node> originNodeList) {
		if(originNodeList == null) {
			return;
		}
		this.mNodeList.addAll(originNodeList);
	}
	
	/**
	 * Adds interactions which target app has at this state
	 * @param interaction
	 */
	public void addInteraction(Interaction interaction) {
		this.mInteractionList.add(interaction);
	}
	
	
	public List<Interaction> getInteractionList() {
		return this.mInteractionList;
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
