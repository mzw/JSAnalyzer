package jp.mzw.jsanalyzer.modeler.model.fsm;

import java.util.ArrayList;
import java.util.List;


import jp.mzw.jsanalyzer.modeler.EnDisableManager.EnDisable;
import jp.mzw.jsanalyzer.modeler.model.graph.Node;
import jp.mzw.jsanalyzer.modeler.model.interaction.Interaction;

public class State extends Node {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	 * Gets nodes which construct this state
	 * @return An origin node list
	 */
	public List<Node> getOriginNodeList() {
		return this.mNodeList;
	}
	
	/**
	 * Adds interactions which target app has at this state
	 * @param interaction
	 */
	public void addInteraction(Interaction interaction) {
		this.mInteractionList.add(interaction);
	}
	
	public void removeInteraction(String id) {
		Interaction remove = null;
		for(Interaction interaction : this.mInteractionList) {
			if(interaction.getId().equals(id)) {
				remove = interaction;
				break;
			}
		}
		if(remove != null) {
			this.mInteractionList.remove(remove);
		}
	}
	
	public void removeAllInteractions() {
		this.mInteractionList = new ArrayList<Interaction>();
	}
	
	/**
	 * @deprecated
	 */
	protected Interaction mMaskInteraction = null;
	/**
	 * @deprecated
	 */
	public void setMaskInteraction(Interaction interaction) {
		this.mMaskInteraction = interaction;
	}
	/**
	 * @deprecated
	 */
	public boolean isMasked() {
		if(this.mMaskInteraction != null) {
			return true;
		}
		return false;
	}
	/**
	 * @deprecated
	 */
	public Interaction getMaskInteraction() {
		return this.mMaskInteraction;
	}
	
	/**
	 * Gets interactions at this state
	 * @return An interaction list
	 */
	public List<Interaction> getInteractionList() {
		return this.mInteractionList;
	}
	
	public String getName() {
		String ret = "";
		
		ret += this.mId;
		
		return ret;
	}
	
	/**
	 * Adds enable and disable statements at this state
	 * @param ed Enable/Disable statement
	 */
	public void addEnDisable(EnDisable ed) {
		this.mEnDisableList.add(ed);
	}
	
	public List<EnDisable> getEnDisableList() {
		return this.mEnDisableList;
	}
	
}