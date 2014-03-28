package jp.mzw.jsanalyzer.modeler.model.graph;

import java.util.ArrayList;
import java.util.List;

import jp.mzw.jsanalyzer.core.IdGen;
import jp.mzw.jsanalyzer.modeler.model.Element;
import jp.mzw.jsanalyzer.parser.JSCode;
import jp.mzw.jsanalyzer.util.StringUtils;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.Name;

public class Node extends Element {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Represents this origin in AST
	 */
	protected AstNode mAstNode;
	
	/**
	 * Contains this original JavaScript code
	 */
	protected JSCode mJSCode;
	
	/**
	 * Represents in-line, embedded, or external code
	 */
	protected int mNodeType;
	
	/**
	 * As well as above
	 */
	public static final int
		Normal = 0,
		Trigger = 1,
		Potential = 2,
		Control = 3;
	
	/**
	 * Constructor
	 * @param astNode This original AST node
	 * @param jsCode Contains original JS code
	 */
	public Node(AstNode astNode, JSCode jsCode) {
		this.mAstNode = astNode;
		this.mNodeType = Node.Normal;
		this.mJSCode = jsCode;
	}
	
	public Node(Node node) {
		this.mId = node.mId;
		this.mAstNode = node.mAstNode;
		this.mJSCode = node.mJSCode;
		this.mNodeType = node.mNodeType;
	}
	
	/**
	 * Gets this JavaScript code
	 * @return This JavaScript code
	 */
	public JSCode getJSCode() {
		return this.mJSCode;
	}

	/**
	 * Gets this AST node
	 * @return This AST node
	 */
	public AstNode getAstNode() {
		return this.mAstNode;
	}
	
	/**
	 * Sets this node type, e.g. trigger, potential, or control
	 * @param type This node type
	 */
	public void setNodeType(int type) {
		this.mNodeType = type;
	}
	
	public int getNodeType() {
		return this.mNodeType;
	}
	
	///// clone
	/**
	 * Clones this node
	 */
	public Node clone() {
		return new Node(this.mId, this.mAstNode, this.mJSCode, this.mNodeType);
	}
	/**
	 * Constructor for cloning
	 * @param id Given node ID
	 * @param astNode AST node originating given node
	 * @param jsCode Contains original JavaScript code and its information
	 * @param nodeType Normal, Trigger, Potential, or Control
	 */
	private Node(String id, AstNode astNode, JSCode jsCode, int nodeType) {
		this.mId = id;
		this.mAstNode = astNode;
		this.mJSCode = jsCode;
		this.mNodeType = nodeType;
	}
	
	///// For width-first-search
	/**
	 * Determines whether visited in width-first-search
	 */
	protected boolean visited;
	/**
	 * Contains children nodes for width-first-search
	 */
	protected ArrayList<Node> children;
	/**
	 * Initializes for width-first-search
	 */
	public void prepareSearch() {
		this.visited = false;
		this.children = new ArrayList<Node>();
	}
	/**
	 * Adds a child node for width-first-search
	 * @param child Child node of this node
	 */
	public void addChild(Node child) {
		if(!this.children.contains(child)) {
			this.children.add(child);
		}
	}
	/**
	 * Returns whether this node has been visited or not
	 * @return True or false represents that this node has already been visited or not
	 */
	public boolean visited() {
		return this.visited;
	}
	/**
	 * Visits this node
	 */
	public void visit() {
		this.visited = true;
	}
	/**
	 * Returns this children nodes which have not been visited yet
	 * @return Unvisited children nodes. If no such nodes, return null.
	 */
	public Node findUnvisitedChild() {
		for(Node child : this.children) {
			if(!child.visited()) {
				return child;
			}
		}
		return null;
	}
	public List<Node> getChildren() {
		return this.children;
	}
	
	///// To output contents
	/**
	 * To string in dot format
	 * @return Dot-formatted string
	 */
	public String getDotLabel() {
		String ret = "[label=\"" + StringUtils.esc_dot(this.getNodeLabel()) + ":" + IdGen.getIdNum(this.getId()) + "\"";
		
		switch(this.mNodeType) {
		case Node.Trigger:
			ret += ", style=\"filled\", fillcolor=\"yellow\"";
			break;
		case Node.Potential:
			ret += ", style=\"filled\", fillcolor=\"blue\"";
			break;
		case Node.Control:
			ret += ", style=\"filled\", fillcolor=\"red\"";
			break;
		case Node.Normal:
		default:
			break;
		}
		
		ret += "]";
		return ret;
	}
	
	private String getNodeLabel() {
		String ret = "";
		
		if(this.mAstNode instanceof FunctionNode) {
			FunctionNode fn = (FunctionNode)this.mAstNode;
			ret += this.mAstNode.shortName() + ": " + fn.getName();
		} else if(this.mAstNode instanceof Name) {
			ret += ((Name)this.mAstNode).getIdentifier();
		} else {
			ret += this.mAstNode.shortName();
		}
		
		return ret;
	}
}
