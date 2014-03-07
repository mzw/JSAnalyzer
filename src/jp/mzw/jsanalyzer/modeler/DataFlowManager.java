package jp.mzw.jsanalyzer.modeler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.Scope;
import org.mozilla.javascript.ast.VariableDeclaration;
import org.mozilla.javascript.ast.VariableInitializer;

import jp.mzw.jsanalyzer.modeler.model.graph.CallGraph;
import jp.mzw.jsanalyzer.modeler.model.graph.Node;

public class DataFlowManager {
	
	HashMap<Scope, MyData> mMyDataMap;
	public DataFlowManager() {
		this.mMyDataMap = new HashMap<Scope, MyData>();
	}
	
	public void analyze(CallGraph graph) {
		// initialize
		CallGraph.getInitNode().prepareSearch();
		for(Node node : graph.getNodeList()) {
			node.prepareSearch();
			
			AstNode parentAstNode = node.getAstNode().getParent();
			if(node.getAstNode() instanceof AstRoot) {
				CallGraph.getInitNode().addChild(node);
			}
			if(parentAstNode != null) {
				Node parent = graph.getNode(parentAstNode);
				parent.addChild(node);
			}
		}
		
		this.analyzeDataFlow(graph);
	}
	
	private void analyzeDataFlow(CallGraph graph) {
		Stack<Node> stack = new Stack<Node>();
		stack.push(CallGraph.getInitNode());
		while(!stack.isEmpty()) {
			Node node = stack.peek();
			Node child = node.findUnvisitedChild();
			if(child == null) {
				node = stack.pop();

				if(node.getAstNode() instanceof VariableDeclaration) {
					VariableDeclaration vardec = (VariableDeclaration)node.getAstNode();
					
					Scope key = getParentScope(node.getAstNode());
					MyData myData = this.mMyDataMap.get(key);
					if(myData == null) {
						myData = new MyData();
					}
					
					myData.addVarDec(vardec);
					
					for(VariableInitializer varinit : vardec.getVariables()) {
						System.out.println(varinit.getTarget().toSource() + ", " + varinit.getInitializer().toSource());
					}
					System.out.println(node.getAstNode().toSource());
					Scope curScope = getParentScope(node.getAstNode());
					System.out.println(curScope.toSource());
				}
			} else {
				child.visit();
				stack.push(child);
			}
		}
	}
	
	/**
	 * Gets parent scope object
	 * @param astNode 
	 * @return Scope object
	 */
	public static Scope getParentScope(AstNode astNode) {
		AstNode parent = astNode.getParent();
		if(parent instanceof Scope) {
			return (Scope)parent;
		} else {
			return getParentScope(parent);
		}
	}
	
	
	private class MyData {
		ArrayList<VariableDeclaration> mVarDecList;
		private MyData() {
			this.mVarDecList = new ArrayList<VariableDeclaration>();
		}
		
		public void addVarDec(VariableDeclaration vardec) {
			
			
			for(VariableInitializer varinit : vardec.getVariables()) {
				System.out.println(varinit.getTarget().toSource() + ", " + varinit.getInitializer().toSource());
			}
			
		}
	}
}
