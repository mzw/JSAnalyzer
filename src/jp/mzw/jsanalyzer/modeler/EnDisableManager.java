package jp.mzw.jsanalyzer.modeler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.nodes.Element;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.Scope;
import org.mozilla.javascript.ast.StringLiteral;

import jp.mzw.jsanalyzer.modeler.model.graph.CallGraph;
import jp.mzw.jsanalyzer.rule.Control;
import jp.mzw.jsanalyzer.rule.JSControl;
import jp.mzw.jsanalyzer.util.StringUtils;

/**
 * Manages enable and disable statements
 * @author Yuta Maezawa
 *
 */
public class EnDisableManager {
	
	/**
	 * Contains information of enable/disable statements.
	 *  A key is a call graph node which has the statements.
	 *  A value is information of the statements. 
	 */
	HashMap<String, List<EnDisable>> mEnDisableInfo;
	
	TargetSolver mTargetSolver;
	
	/**
	 * Constructor
	 */
	public EnDisableManager() {
		this.mEnDisableInfo = new HashMap<String, List<EnDisable>>();
		this.mTargetSolver = new TargetSolver();
	}
	
	public TargetSolver getTargetSolver() {
		return this.mTargetSolver;
	}
	public void findTargetCandidates(CallGraph callGraph) {
		this.mTargetSolver.findTargetCandidates(callGraph);
	}
	public String findElementId(Scope scope, String varName) {
		return this.mTargetSolver.findElementId(scope, varName);
	}
	
	/**
	 * Adds an enable/disable statement
	 * @param node Represents where the statement is
	 * @param ed Contains information of the statement
	 */
	public void add(String nodeId, EnDisable ed) {
		List<EnDisable> edList = this.mEnDisableInfo.get(nodeId);
		if(edList == null) {
			edList = new ArrayList<EnDisable>();
		}
		
		edList.add(ed);
		this.mEnDisableInfo.put(nodeId, edList);
	}
	
	/**
	 * Gets enable/disable statements at given node identified by its ID
	 * @param node Given node ID
	 * @return Enable/disable statements
	 */
	public List<EnDisable> getEnDisableList(String nodeId) {
		List<EnDisable> ret = this.mEnDisableInfo.get(nodeId);
		if(this.mEnDisableInfo.get(nodeId) == null) {
			ret = new ArrayList<EnDisable>();
		}
		return ret;
	}
	
	
	public void show() {
		for(String nodeId : this.mEnDisableInfo.keySet()) {
			List<EnDisable> edList = this.mEnDisableInfo.get(nodeId);
			System.out.println("At node identified by " + nodeId);
			for(EnDisable ed : edList) {
				System.out.println("\t" + ed.toString());
			}
		}
	}
	
	public static class EnDisable {

		protected Element mHTMLElement;
		protected String mProp;
		protected String mVal;
		protected int mType;
		protected Control mControlRule;
		
		protected String mTargetId;
		
		public EnDisable(Element html, String prop, String val, int type, Control rule) {
			this.mHTMLElement = html;
			this.mProp = prop;
			this.mVal = val;
			this.mType = type;
			this.mControlRule = rule;
			
			this.mTargetId = null;
		}

		protected AstNode mJSTargetNode;

		public EnDisable(AstNode js, String prop, String val, int type, Control rule) {
			this.mJSTargetNode = js;
			this.mProp = prop;
			this.mVal = val;
			this.mType = type;
			this.mControlRule = rule;
		}
		
		public AstNode getJSTargetNode() {
			return this.mJSTargetNode;
		}
		
		public Control getRule() {
			return this.mControlRule;
		}
		
		public Element getTarget() {
			return this.mHTMLElement;
		}
		
		public boolean isDisabled() {
			return this.mControlRule.disabled(this.mProp, this.mVal);
		}
		
		/**
		 * Hard coding
		 */
		public void setTargetId(EnDisableManager edManager) {
			if(this.mHTMLElement != null) {
				this.mTargetId = this.mHTMLElement.attr("id");
			} else if(this.mJSTargetNode != null) {
				if(this.mJSTargetNode instanceof FunctionCall) {
					// document.getElementById("ID").disable = true;
					this.mTargetId = TargetSolver.getElementIdBy((FunctionCall)this.mJSTargetNode);
				}
				else if(this.mJSTargetNode instanceof Name) {
//					this.mTargetId = TargetSolver.getElementId((Name)this.mJSTargetNode);
					this.mTargetId = edManager.findElementId(TargetSolver.getParentScope(this.mJSTargetNode), this.mJSTargetNode.toSource());
				}
				else {
					System.out.println("Unknown JS element class: " + this.mJSTargetNode.getClass() + ", src= " + this.mJSTargetNode.toSource());
				}
			} else {
				System.err.println("This en/disable does not have any targets: " + this.toString());
			}
		}
		public String getTargetId() {
			return this.mTargetId;
		}
		
		
		
		
		
		
		
		/**
		 * To output this contents 
		 */
		public String toString() {
			String ret = "";
			
			ret += "To an element of ";
			
			if(this.mHTMLElement == null) {
				ret += "To-Be-Defined, " + this.mProp + " is " + this.mVal + ", therefore disabled is true/false to be defined by " + this.mVal;
			} else {
				ret += this.mHTMLElement.id() + ", " + this.mProp + " is " + this.mVal + ", therefore disabled is " + this.mControlRule.disabled(this.mProp, this.mVal);
			}
			
			
			return  ret;
		}
	}
}
