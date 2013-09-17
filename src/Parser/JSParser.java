package Parser;

import Graph.Graph;
import Rule.RuleList;
import org.mozilla.javascript.*;
import org.mozilla.javascript.ast.*;
import org.mozilla.javascript.tools.shell.Global;

import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

public class JSParser {
	
	private String url = "";
	private String jsCodes = "";
	private AstRoot ast = null;
	private Graph graph = null;
	
	private JSCode jsCode = null;
	public JSParser(JSCode jsCode, int offset4id) {
		this.jsCode = jsCode;
		this.url = jsCode.getUrl();
		this.jsCodes = jsCode.toSource();
		AstRoot ast = this.createAST(url, jsCode.toSource());
		this.ast = ast;
		this.graph = new Graph(offset4id);
	}
	public JSCode getJsCode() {
		return this.jsCode;
	}
	
	public JSParser(String url, String jsCode, int offset4id) {
		this.url = url;
		this.jsCodes = jsCode;
		AstRoot ast = this.createAST(url, jsCode);
		this.ast = ast;
		this.graph = new Graph(offset4id);
	}
	
	public int getOffset4Id() {
		return this.graph.getOffset4Id();
	}
	
	protected RuleList rules = null;
	public void setRules(RuleList rules) {
		this.rules = rules;
		this.graph.setRules(rules);
	}
	
	public AstRoot getAst() {
		return this.ast;
	}
	
	public Graph getGraph() {
		return this.graph;
	}
	
	public void parse() {
		this.parse(this.ast);
	}
	
	private void parse(Node node) {
		if(node == null) {
			return;
		}
		
		/// 6th level
		/// ForInLoop
		else if(node instanceof ArrayComprehensionLoop) {
			System.err.println("Not yet@JSParser.parse: ArrayComprehensionLoop@6th");
		} else if(node instanceof GeneratorExpressionLoop) {
			System.err.println("Not yet@JSParser.parse: GeneratorExpressionLoop@6th");
		} 
		
		/// 5th level
		/// Loop
		else if(node instanceof DoLoop) {
			System.err.println("Not yet@JSParser.parse: DoLoop@5th");
		} else if(node instanceof ForInLoop) {
			///// to be considered
			ForInLoop _node = (ForInLoop)node;

			this.graph.addEdge(node, _node.getIterator());
			this.graph.addEdge(node, _node.getIteratedObject());
			
			this.parse(_node.getBody());

			//System.err.println("Not yet@JSParser.parse: ForIn@5th");
		} else if(node instanceof ForLoop) {
			ForLoop _node = (ForLoop)node;
			AstNode parent = _node.getParent();
			this.graph.addEdge(parent, _node);

			this.parse(_node.getInitializer());
			this.parse(_node.getCondition());
			this.parse(_node.getIncrement());
			this.parse(_node.getBody());
			
			//System.err.println("Not yet@JSParser.parse: ForLoop@5th");
		} else if(node instanceof WhileLoop) {
			System.err.println("Not yet@JSParser.parse: WhileLoop@5th");
		}
		/// ScriptNode
		else if(node instanceof AstRoot) {
			AstRoot _node = (AstRoot)node;
			this.graph.addRootNode(_node);
		} else if(node instanceof FunctionNode) {
			FunctionNode _node = (FunctionNode)node;
			AstNode parent = _node.getParent();
			this.graph.addEdge(parent, _node);
			
			parse(_node.getFunctionName());
			
			for(AstNode __node : _node.getParams()) {
				parse(__node);
			}
			parse(_node.getBody());
		}
		
		// 4th level
		/// Scope
		else if(node instanceof ArrayComprehension) {
			System.err.println("Not yet@JSParser.parse: ArrayComprehension@4th");
		} else if(node instanceof GeneratorExpression) {
			System.err.println("Not yet@JSParser.parse: GeneratorExpression@4th");
		} else if(node instanceof LetNode) {
			System.err.println("Not yet@JSParser.parse: LetNode@4th");
		} else if(node instanceof Loop) {
			System.err.println("Not yet@JSParser.parse: Loop@4th");
		} else if(node instanceof ScriptNode) {
			System.err.println("Not yet@JSParser.parse: ScriptNode@4th");
		} 
		
		/// 3rd level
		/// FunctionCall
		else if(node instanceof NewExpression) {
			NewExpression _node = (NewExpression)node;
			AstNode parent = _node.getParent();
			this.graph.addEdge(parent, _node);
		
			parse(_node.getInitializer());
			
			for(AstNode __node : _node.getArguments()) {
				parse(__node);
			}
			
			parse(_node.getTarget());
		}
		/// Jump
		else if(node instanceof BreakStatement) {
			///// to be considered
			BreakStatement _node = (BreakStatement)node;
			//System.out.println("break statement: " + _node.toSource());
			
			//System.err.println("Not yet@JSParser.parse: BreakStatement@3rd");
		} else if(node instanceof ContinueStatement) {
			System.err.println("Not yet@JSParser.parse: ContinueStatement@3rd");
		} else if(node instanceof Label) {
			System.err.println("Not yet@JSParser.parse: Label@3rd");
		} else if(node instanceof Scope) {
			Scope _node = (Scope)node;
			AstNode parent = _node.getParent();
			this.graph.addEdge(parent, _node);
		} else if(node instanceof SwitchStatement) {
			///// to be considered
			SwitchStatement _node = (SwitchStatement)node;
			
			//AstNode val = _node.getExpression();
			for(SwitchCase sc : _node.getCases()) {
				parse(sc);
				
				//AstNode param = sc.getExpression();
				//String cond = val.toSource() + "==" + param.toSource();
				
				int edgeId = this.graph.getEdgeId(_node, sc);
				this.graph.setCondition(edgeId, sc);
			}
			
			//System.err.println("Not yet@JSParser.parse: SwitchStatement@3rd");
		}
		/// InfixExpression
		else if(node instanceof Assignment) {
			Assignment _node = (Assignment)node;
			AstNode parent = _node.getParent();
			this.graph.addEdge(parent, _node);
			
			parse(_node.getLeft());
			parse(_node.getRight());
		} else if(node instanceof ObjectProperty) {
			ObjectProperty _node = (ObjectProperty)node;
			AstNode parent = _node.getParent();
			this.graph.addEdge(parent, _node);
			
			parse(_node.getLeft());
			parse(_node.getRight());
		} else if(node instanceof PropertyGet) {
			PropertyGet _node = (PropertyGet)node;
			AstNode parent = _node.getParent();
			this.graph.addEdge(parent, _node);
			
			parse(_node.getTarget());
			parse(_node.getProperty());
		} else if(node instanceof XmlDotQuery) {
			System.err.println("Not yet@JSParser.parse: XmlDotQuery@3rd");
		} else if(node instanceof XmlMemberGet) {
			System.err.println("Not yet@JSParser.parse: XmlMemberGet@3rd");
		}
		/// XmlFragment
		else if(node instanceof XmlExpression) {
			System.err.println("Not yet@JSParser.parse: XmlExpression@3rd");
		} else if(node instanceof XmlString) {
			System.err.println("Not yet@JSParser.parse: XmlString@3rd");
		}
		/// XmlRef
		else if(node instanceof XmlElemRef) {
			System.err.println("Not yet@JSParser.parse: XmlElemRef@3rd");
		} else if(node instanceof XmlPropRef) {
			System.err.println("Not yet@JSParser.parse: XmlPropRef@3rd");
		}
		
		/// 2nd level
		/// All from AstNode
		else if(node instanceof ArrayLiteral) {
			ArrayLiteral _node = (ArrayLiteral)node;
			AstNode parent = _node.getParent();
			this.graph.addEdge(parent, _node);
			for(AstNode __node : _node.getElements()) {
				parse(__node);
			}
		} else if(node instanceof Block) {
			Block _node = (Block)node;
			AstNode parent = _node.getParent();
			this.graph.addEdge(parent, _node);
		} else if(node instanceof CatchClause) {
			System.err.println("Not yet@JSParser.parse: CatchClause@2nd");
		} else if(node instanceof Comment) {
			System.err.println("Not yet@JSParser.parse: Comment@2nd");
		} else if(node instanceof ConditionalExpression) {
			ConditionalExpression _node = (ConditionalExpression)node;
			AstNode parent = _node.getParent();
			
			this.graph.addEdge(parent, _node);

			parse(_node.getTestExpression());
			parse(_node.getTrueExpression());
			int edgeId = this.graph.getEdgeId(_node, _node.getTrueExpression());
			this.graph.setCondition(edgeId, _node.getTestExpression());
			
			parse(_node.getFalseExpression());
		} else if(node instanceof ElementGet) {
			ElementGet _node = (ElementGet)node;
			AstNode parent = _node.getParent();
			this.graph.addEdge(parent, _node);

			parse(_node.getTarget());
			parse(_node.getElement());
		} else if(node instanceof EmptyExpression) {
			///// Not necessary
			//System.err.println("Not yet@JSParser.parse: EmptyExpression@2nd");
		} else if(node instanceof EmptyStatement) {
			///// Not necessary
			//EmptyStatement _node = (EmptyStatement)node;
			//AstNode parent = _node.getParent();
			//this.graph.addEdge(parent, _node);
		} else if(node instanceof ErrorNode) {
			System.err.println("Not yet@JSParser.parse: ErrorNode@2nd");
		} else if(node instanceof ExpressionStatement) {
			ExpressionStatement _node = (ExpressionStatement)node;
			AstNode parent = _node.getParent();
			this.graph.addEdge(parent, _node);
			
			parse(_node.getExpression());
		} else if(node instanceof FunctionCall) {
			FunctionCall _node = (FunctionCall)node;
			AstNode parent = _node.getParent();
			this.graph.addEdge(parent, _node);
			for(AstNode __node : _node.getArguments()) {
				parse(__node);
			}
			parse(_node.getTarget());
		} else if(node instanceof IfStatement) {
			IfStatement _node = (IfStatement)node;
			AstNode parent = _node.getParent();
			
			this.graph.addEdge(parent, _node);
			
			parse(_node.getCondition());
			
			parse(_node.getThenPart());
			
			int edgeId = this.graph.getEdgeId(_node, _node.getThenPart());
			this.graph.setCondition(edgeId, _node.getCondition());
			
			parse(_node.getElsePart());
			
			if(!this.graph.hasCondition(_node, _node.getElsePart())) {
				int _edgeId = this.graph.getEdgeId(_node, _node.getElsePart());
				this.graph.setElseCondition(_edgeId, _node);
			}
			
		} else if(node instanceof InfixExpression) {
			InfixExpression _node = (InfixExpression)node;
			AstNode parent = _node.getParent();
			this.graph.addEdge(parent, _node);
			
			parse(_node.getLeft());
			parse(_node.getRight());
		} else if(node instanceof Jump) {
			System.err.println("Not yet@JSParser.parse: Jump@2nd");
		} else if(node instanceof KeywordLiteral) {
			KeywordLiteral _node = (KeywordLiteral)node;
			AstNode parent = _node.getParent();
			this.graph.addEdge(parent, _node);
		} else if(node instanceof LabeledStatement) {
			System.err.println("Not yet@JSParser.parse: LabeledStatement@2nd");
		} else if(node instanceof Name) {
			Name _node = (Name)node;
			AstNode parent = _node.getParent();
			this.graph.addEdge(parent, _node);
		} else if(node instanceof NumberLiteral) {
			NumberLiteral _node = (NumberLiteral)node;
			AstNode parent = _node.getParent();
			this.graph.addEdge(parent, _node);
		} else if(node instanceof ObjectLiteral) {
			ObjectLiteral _node = (ObjectLiteral)node;
			AstNode parent = _node.getParent();
			this.graph.addEdge(parent, _node);
			for(AstNode __node : _node.getElements()) {
				parse(__node);
			}
		} else if(node instanceof ParenthesizedExpression) {
			ParenthesizedExpression _node = (ParenthesizedExpression)node;
			AstNode parent = _node.getParent();
			this.graph.addEdge(parent, _node);
			
			parse(_node.getExpression());
		} else if(node instanceof RegExpLiteral) {
			RegExpLiteral _node = (RegExpLiteral)node;
			AstNode parent = _node.getParent();
			this.graph.addEdge(parent, _node);
		} else if(node instanceof ReturnStatement) {
			ReturnStatement _node = (ReturnStatement)node;
			AstNode parent = _node.getParent();
			this.graph.addEdge(parent, _node);
			
			parse(_node.getReturnValue());
		} else if(node instanceof StringLiteral) {
			StringLiteral _node = (StringLiteral)node;
			AstNode parent = _node.getParent();
			this.graph.addEdge(parent, _node);
		} else if(node instanceof SwitchCase) {
			///// to be considered
			SwitchCase _node = (SwitchCase)node;
			AstNode parent = _node.getParent();
			this.graph.addEdge(parent, _node);

			if(_node.getStatements() != null) {
				for(AstNode s : _node.getStatements()) {
					parse(s);
				}
			}
			
			//System.err.println("Not yet@JSParser.parse: SwitchCase@2nd");
		} else if(node instanceof ThrowStatement) {
			ThrowStatement _node = (ThrowStatement)node;
			AstNode parent = _node.getParent();
			this.graph.addEdge(parent, _node);
			
			parse(_node.getExpression());
		} else if(node instanceof TryStatement) {
			TryStatement _node = (TryStatement)node;
			AstNode parent = _node.getParent();
			this.graph.addEdge(parent, _node);
			
			this.parse(_node.getTryBlock());
			List<CatchClause> ccs = _node.getCatchClauses();
			for(CatchClause cc : ccs) {
				this.parse(cc.getCatchCondition());
				this.parse(cc.getBody());
			}
			
			//System.err.println("Not yet@JSParser.parse: TryStatement@2nd");
		} else if(node instanceof UnaryExpression) {
			UnaryExpression _node = (UnaryExpression)node;
			AstNode parent = _node.getParent();
			this.graph.addEdge(parent, _node);
			
			parse(_node.getOperand());
		} else if(node instanceof VariableDeclaration) {
			VariableDeclaration _node = (VariableDeclaration)node;
			AstNode parent = _node.getParent();
			this.graph.addEdge(parent, _node);
			
			for(VariableInitializer __node  : _node.getVariables()) {
				parse(__node);
			}
		} else if(node instanceof VariableInitializer) {
			VariableInitializer _node = (VariableInitializer)node;
			AstNode parent = _node.getParent();
			this.graph.addEdge(parent, _node);

			parse(_node.getTarget());
			parse(_node.getInitializer());
		} else if(node instanceof WithStatement) {
			System.err.println("Not yet@JSParser.parse: WithStatement@2nd");
		} else if(node instanceof XmlFragment) {
			System.err.println("Not yet@JSParser.parse: XmlFragment@2nd");
		} else if(node instanceof XmlLiteral) {
			System.err.println("Not yet@JSParser.parse: XmlLiteral@2nd");
		} else if(node instanceof XmlRef) {
			System.err.println("Not yet@JSParser.parse: XmlRef@2nd");
		} else if(node instanceof Yield) {
			System.err.println("Not yet@JSParser.parse: Yield@2nd");
		}
		
		/// 1st level
		else if(node instanceof AstNode) {
			System.err.println("Not yet@JSParser.parse: AstNode@1st");
		}
		
		/// Other Objects in package of "org.mozilla.javascript.ast"
		/// AstNode.DebugPrintVisitor
		/// AstNode.PositionComparator
		/// ErrorCollector
		/// Symbol
		/// ParseProblem
		
		else {
			System.err.println("Unknown node type: " + node.getClass() + ", " + node.toString());
			AstNode parent = ((AstNode)node).getParent();
			if(parent != null) {
				System.err.println("\tparent: " + parent.getClass());
			}
		}

		///// children
		for(Node child = node.getFirstChild(); child != null; child = child.getNext()) {
			parse(child);
		}
	}

	// Ad-hoc workarounds for incomplete analysis by code replacement. This can break original
	// semantic and in the worst case, replaced jsCode become syntactically invalid.
	// TODO: In the future, this method should be removed.
	private String preprocessJsCodeForParsing(String jsCode) {
		// Because we cannot resolve the event target when it's not stored in variable,
		// replace something like "document.getElementById('hoge')."
		// to "var autogenerated_hoge = document.getElementById('hoge'); autogenerated_hoge."
		jsCode = jsCode.replaceAll("document\\.getElementById\\('([a-zA-Z0-9_]+)'\\)\\.",
				"var  autogenerated_$1 = document.getElementById('$1'); autogenerated_$1.");
		return jsCode;
	}

	private AstRoot createAST(String url, String jsCode) {
		AstRoot ast = null;

		jsCode = preprocessJsCodeForParsing(jsCode);

				Global global = new Global();
		Context cx = ContextFactory.getGlobal().enterContext();
		global.init(cx);
		try {
			Scriptable scope = cx.initStandardObjects(global);
			cx.setOptimizationLevel(-1); // bypass 64kb size limit
			cx.evaluateReader(scope, new FileReader("res/js/env.rhino.1.2.js"), "env.rhino.js", 1, null);
			
			CompilerEnvirons ce = new CompilerEnvirons();
			ce.initFromContext(cx);
			Parser parser = new Parser(ce);

			// hard coding
			jsCode = jsCode.replace("jPreLoader script", "");
			jsCode = jsCode.replace("End of", "");
			
			ast = parser.parse(jsCode, url, 1);
			
			if(ast == null) {
				System.err.println("fail to parse");
				System.exit(-1);
			} else {
				this.jsCodes += jsCode;
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			Context.exit();
		}
		return ast;
	}
	
	public int jsloc() {
		int ret = 0;
		for(; ret < this.jsCodes.length(); ret++ );
		return ret;
	}
	
	public List<String> getComments(String jsCode) {
		List<String> ret = new LinkedList<String>();
		for(int i = 0; i < jsCode.length()-1; i++) {
			if(jsCode.charAt(i) == '/' && jsCode.charAt(i+1) == '/') {
				String substr = jsCode.substring(i, jsCode.length());
				int index = substr.indexOf("\n");
				String comment = substr.substring(2, index);
				ret.add(comment);
			} else if(jsCode.charAt(i) == '/' && jsCode.charAt(i+1) == '*') {
				String substr = jsCode.substring(i, jsCode.length());
				int index = substr.indexOf("*/");
				String comment = substr.substring(2, index);
				ret.add(comment);
			} 
		}
		return ret;
	}
}
