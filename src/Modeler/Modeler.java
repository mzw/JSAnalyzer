package Modeler;

import java.util.LinkedList;
import java.util.List;

import Graph.Graph;
import Graph.Node;

public class Modeler {
	
	protected Graph graph = null;

	protected List<Node> funcNodes = null;
	
	public Modeler(Graph graph) {
		this.graph = graph;
		
		this.funcNodes = this.getFuncNodes();
	}
	
	/// Utilities
	public Graph getGraph() {
		return this.graph;
	}
	
	public Node getRetNode(Node node) {
		org.mozilla.javascript.ast.AstNode astNode = node.getAstNode();
		if(astNode instanceof org.mozilla.javascript.ast.Name) {
			return this.getRetNode(node.getParent());
		} else if(astNode instanceof org.mozilla.javascript.ast.FunctionCall) {
			return this.getRetNode(node.getParent());
		} else if(astNode instanceof org.mozilla.javascript.ast.ExpressionStatement) {
			return this.graph.getNodeByAstNode(astNode);
		}
		
		else {
			System.out.println("Unknown class@Modeler.getRetNode: " + astNode.getClass());
		}
		return null;
	}
	

	public Node getArgNode(Node node, int argNum) {
		org.mozilla.javascript.ast.AstNode astNode = node.getAstNode();
		if(astNode instanceof org.mozilla.javascript.ast.FunctionCall) {
			org.mozilla.javascript.ast.FunctionCall _astNode = (org.mozilla.javascript.ast.FunctionCall)astNode;
			for(int i = 1 ; i <= _astNode.getArguments().size(); i++) {
				if(i == argNum) {
					org.mozilla.javascript.ast.AstNode argAstNode = _astNode.getArguments().get(i-1);
					return this.graph.getNodeByAstNode(argAstNode);
				}
			}
			System.err.println("Invalid arg num@Modeler.getArgNode: " + argNum + ", " + astNode.toSource());
		}
		
		// jQuery?
		else if(astNode instanceof org.mozilla.javascript.ast.PropertyGet) {
			if(astNode.getParent() instanceof org.mozilla.javascript.ast.FunctionCall) {
				org.mozilla.javascript.ast.FunctionCall _astNode = (org.mozilla.javascript.ast.FunctionCall)astNode.getParent();
				for(int i = 1; i <= _astNode.getArguments().size(); i++) {
					org.mozilla.javascript.ast.AstNode arg = _astNode.getArguments().get(i-1);
					if(arg.equals(astNode)) {
						// { object.property }
						return null;
					} else {
						if(i == argNum) {
							return this.graph.getNodeByAstNode(arg);
						}
					}
				}
			} else if(astNode.getParent() instanceof org.mozilla.javascript.ast.InfixExpression) {
				// { 'foo' + object.property + 'bar' }
				return null;
			} else {
				System.out.println("Unknown class for jQuert?@Modeler.getArgNode: " + astNode.getParent().getClass());
			}
		} else if(astNode instanceof org.mozilla.javascript.ast.FunctionNode) {
			return null;
			/*
			org.mozilla.javascript.ast.FunctionNode _astNode = (org.mozilla.javascript.ast.FunctionNode)astNode;
			for(org.mozilla.javascript.ast.AstNode __astNode : _astNode.getParams()) {
				System.out.println("FuncNode param: " + __astNode.toSource());
			}
			for(int i = 1; i <= _astNode.getParams().size(); i++) {
				org.mozilla.javascript.ast.AstNode arg = _astNode.getParams().get(i-1);
				// jQuery.ready( >> function($) << { ...
				if(i == argNum) {
					return this.graph.getNodeByAstNode(arg);
				}
			}
			System.err.println("Invalid arg num due to jQuery?@Modeler.getArgNode: " + argNum + ", " + astNode.toSource());
			*/
		}
		
		
		else {
			System.err.println("Unknown class@Modeler.getArgNode: " + astNode.getClass());
			System.err.println(astNode.toSource());
		}
		return null;
	}
	

	public Node getFuncCallNode(Node node) {
		org.mozilla.javascript.ast.AstNode astNode = node.getAstNode();
		if(astNode instanceof org.mozilla.javascript.ast.Name) {
			return this.getFuncCallNode(node.getParent());
		} else if(astNode instanceof org.mozilla.javascript.ast.FunctionCall) {
			return this.graph.getNodeByAstNode(astNode);
		}
		
		// 
		else if(astNode instanceof org.mozilla.javascript.ast.PropertyGet) {
			// Ajax.Requset
			return this.graph.getNodeByAstNode(astNode);
		}
		
		// to be debugged : related to Graph.extend.isPotential.funcCallNode
		else if(astNode instanceof org.mozilla.javascript.ast.ObjectProperty) {
			//return this.getFuncCallNode(node.getParent());
			return null;
		}
		
		else {
			System.err.println("To be debugged@Modeler.getFuncCallNode: " + astNode.getClass());
		}
		return null;
	}
	
	/// for analyzing triggers
	public Node getTriggerSetNode(Node node) {
		org.mozilla.javascript.ast.AstNode astNode = node.getAstNode();
		if(astNode instanceof org.mozilla.javascript.ast.Name) {
			return this.getTriggerSetNode(node.getParent());
		}
		// object.property = callback
		else if(astNode instanceof org.mozilla.javascript.ast.PropertyGet) {
			return this.getTriggerSetNode(node.getParent());
		} else if(astNode instanceof org.mozilla.javascript.ast.Assignment) {
			return this.graph.getNodeByAstNode(astNode);
		}
		// new object({property : callback});
		else if(astNode instanceof org.mozilla.javascript.ast.ObjectProperty) {
			// property : callback
			return this.graph.getNodeByAstNode(astNode);
		}
		
		else {
			System.err.println("Unknown class@Modeler.getFromNode: " + astNode.getClass());
		}
		return null;
	}
	

	public Node getCallbackNode(Node node) {
		org.mozilla.javascript.ast.AstNode astNode = node.getAstNode();
		if(astNode instanceof org.mozilla.javascript.ast.Name) {
			return this.getCallbackNode(node.getParent());
		} else if(astNode instanceof org.mozilla.javascript.ast.PropertyGet) {
			return this.getCallbackNode(node.getParent());
		} else if(astNode instanceof org.mozilla.javascript.ast.Assignment) {
			org.mozilla.javascript.ast.Assignment _astNode = (org.mozilla.javascript.ast.Assignment)astNode;
			return this.graph.getNodeByAstNode(_astNode.getRight());
		}
		// new object(property: callback)
		else if(astNode instanceof org.mozilla.javascript.ast.ObjectProperty) {
			org.mozilla.javascript.ast.ObjectProperty _astNode = (org.mozilla.javascript.ast.ObjectProperty)astNode;
			Node _node = this.graph.getNodeByAstNode(_astNode.getRight());
			return _node;
		}
		
		else {
			System.err.println("Unknown path@Modeler.getCallbackNode: " + astNode.getClass());
		}
		return null;
	}
	
	public Node getFuncNode(Node node) {
		org.mozilla.javascript.ast.AstNode astNode = node.getAstNode();
		if(astNode instanceof org.mozilla.javascript.ast.Name) {
			return this.getFuncNodeByName(node.toSource());
		} else if(astNode instanceof org.mozilla.javascript.ast.FunctionNode) {
			return node;
		}
		return null;
	}
	

	public Node getObjNode(Node node) {
		org.mozilla.javascript.ast.AstNode astNode = node.getAstNode();
		if(astNode instanceof org.mozilla.javascript.ast.Name) {
			return this.getObjNode(node.getParent());
		}
		// object.property
		else if(astNode instanceof org.mozilla.javascript.ast.PropertyGet) {
			org.mozilla.javascript.ast.PropertyGet _astNode = (org.mozilla.javascript.ast.PropertyGet)astNode;
			return this.graph.getNodeByAstNode(_astNode.getTarget());
		}
		// new object({property: callback})
		else if(astNode instanceof org.mozilla.javascript.ast.ObjectProperty) {
			return this.getObjNode(node.getParent());
		} else if(astNode instanceof org.mozilla.javascript.ast.ObjectLiteral) {
			return this.getObjNode(node.getParent());
		} else if(astNode instanceof org.mozilla.javascript.ast.NewExpression) {
			org.mozilla.javascript.ast.NewExpression _astNode = (org.mozilla.javascript.ast.NewExpression)astNode;
			Node _node = this.graph.getNodeByAstNode(_astNode.getTarget());
			return this.getObjNode(_node);
		}
		
		// to be debugged
		else if(astNode instanceof org.mozilla.javascript.ast.FunctionCall) {
			return null;
		}
		
		else {
			System.err.println("Unknown path@Modeler.getObjNode: " + astNode.getClass());
		}
		return null;
	}
	
	

	public Node getFuncNodeByName(String funcname) {
		if(this.funcNodes == null) {
			System.err.println("Execute getFuncNodes@Modeler.getFuncNodeByName: " + funcname);
			return null;
		}
		if(funcname == null || "".equals(funcname)) {
			System.err.println("Given function name is invalid@Modeler.getFuncNodeByName");
			return null;
		}
		for(Node node : this.funcNodes) {
			org.mozilla.javascript.ast.AstNode astNode = node.getAstNode();
			if(astNode instanceof org.mozilla.javascript.ast.FunctionNode) {
				org.mozilla.javascript.ast.FunctionNode _astNode = (org.mozilla.javascript.ast.FunctionNode)astNode;
				org.mozilla.javascript.ast.Name funcNameAstNode = _astNode.getFunctionName();
				if(funcNameAstNode != null) {
					if(funcname.equals(funcNameAstNode.toSource())) {
						return node;
					}
				} else {
					// NOP
					// Nameless function
				}
			} else {
				System.err.println("Why not FunctionNode?@Modeler.getFuncNodeByName: " + astNode.getClass());
				return null;
			}
		}
		return null;
	}

	public Node getFuncCallNameNode(Node node) {
		org.mozilla.javascript.ast.AstNode astNode = node.getAstNode();
		if(astNode instanceof org.mozilla.javascript.ast.FunctionCall) {
			org.mozilla.javascript.ast.FunctionCall _astNode = (org.mozilla.javascript.ast.FunctionCall)astNode;
			Node _node = this.graph.getNodeByAstNode(_astNode.getTarget());
			return this.getFuncCallNameNode(_node);
		} else if(astNode instanceof org.mozilla.javascript.ast.Name) {
			Node _node = this.graph.getNodeByAstNode(astNode);
			return _node;
		} else if(astNode instanceof org.mozilla.javascript.ast.PropertyGet) {
			org.mozilla.javascript.ast.PropertyGet _astNode = (org.mozilla.javascript.ast.PropertyGet)astNode;
			Node _node = this.graph.getNodeByAstNode(_astNode.getProperty());
			return this.getFuncCallNameNode(_node);
		}
		
		else if(astNode instanceof org.mozilla.javascript.ast.ParenthesizedExpression) {
			org.mozilla.javascript.ast.ParenthesizedExpression _astNode = (org.mozilla.javascript.ast.ParenthesizedExpression)astNode;
			Node _node = this.graph.getNodeByAstNode(_astNode.getExpression());
			return this.getFuncCallNameNode(_node);
		} else if(astNode instanceof org.mozilla.javascript.ast.FunctionNode) {
			return this.graph.getNodeByAstNode(astNode);
		}
		
		else {
			System.err.println("Unknown class@Modeler.getFuncCallNameNode: " + astNode.getClass());
		}
		return null;
	}

	public List<Node> getFuncNodes() {
		List<Node> ret = new LinkedList<Node>();
		for(Node node : this.graph.getNodes()) {
			if(node.getAstNode() instanceof org.mozilla.javascript.ast.FunctionNode) {
				if(!ret.contains(node)) {
					ret.add(node);
				}
			}
		}
		return ret;
	}
	

	public void init4dfs() {
		for(Node node : this.graph.getNodes()) {
			node.init4dfs();
		}
	}
	
}
