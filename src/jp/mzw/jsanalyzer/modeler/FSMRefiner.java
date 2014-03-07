package jp.mzw.jsanalyzer.modeler;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.InfixExpression;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.PropertyGet;
import org.mozilla.javascript.ast.Scope;
import org.mozilla.javascript.ast.StringLiteral;

import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.core.LimitationManager;
import jp.mzw.jsanalyzer.core.LimitationManager.Limitation;
import jp.mzw.jsanalyzer.modeler.EnDisableManager.EnDisable;
import jp.mzw.jsanalyzer.modeler.model.graph.CallGraph;
import jp.mzw.jsanalyzer.modeler.model.graph.Edge;
import jp.mzw.jsanalyzer.modeler.model.graph.Node;
import jp.mzw.jsanalyzer.modeler.model.interaction.Callback;
import jp.mzw.jsanalyzer.modeler.model.interaction.Event;
import jp.mzw.jsanalyzer.modeler.model.interaction.Interaction;
import jp.mzw.jsanalyzer.modeler.model.fsm.FiniteStateMachine;
import jp.mzw.jsanalyzer.modeler.model.fsm.State;
import jp.mzw.jsanalyzer.modeler.model.fsm.Transition;
import jp.mzw.jsanalyzer.parser.HTMLParser;
import jp.mzw.jsanalyzer.rule.Function;
import jp.mzw.jsanalyzer.rule.JSControl;
import jp.mzw.jsanalyzer.rule.Trigger;
import jp.mzw.jsanalyzer.util.StringUtils;
import jp.mzw.jsanalyzer.xml.XMLAttr;

public class FSMRefiner extends Modeler {
	
	/**
	 * Constructor
	 * @param analyzer Provides project information
	 */
	public FSMRefiner(Analyzer analyzer) {
		super(analyzer);
	}
	
	/**
	 * Refines abstracted call graph using en/disabling statements
	 * @param acg Abstracted call graph
	 * @return Refined call graph
	 */
	public FiniteStateMachine refine(HTMLParser html, CallGraph acg, EnDisableManager edManager, AbstractionManager abstManager) {
		System.out.println("Refines call graph...");
		
		FiniteStateMachine fsm = new FiniteStateMachine();
		
		HashMap<Node, State> hashNodeState = this.setInteractions(fsm, acg, abstManager);
		this.setEnDisables(fsm, acg, hashNodeState, edManager);
		
		this.determineEnDisable(fsm, html, acg, edManager);
		
		this.addExitTransitions(fsm);
		
//		fsm.setAbstractionManager(abstManager);
//		fsm.setEnDisableManager(edManager);
		
		return fsm;
	}
	
	private void addExitTransitions(FiniteStateMachine fsm) {
		for(State state : fsm.getStateList()) {
			boolean exit = true;
			for(Transition trans : fsm.getTransList()) {
				if(trans.getFromStateId().equals(state.getId())) {
					exit = false;
					break;
				}
			}
			if(exit) {
				Transition trans = new Transition(state.getId(), fsm.getExitState().getId());
				fsm.addTransition(trans);
			}
		}
	}
	
	/**
	 * Sets interactions to each state
	 * @param fsm Finite state machine as output
	 * @param graph Abstracted call graph
	 * @param abstManager Provides abstraction information
	 * @return 
	 */
	private HashMap<Node, State> setInteractions(FiniteStateMachine fsm, CallGraph graph, AbstractionManager abstManager) {
		// Sets interactions at each states where Ajax app registers them
		
		// Creates states
		HashMap<Node, State> hashNodeState = new HashMap<Node, State>(); 
		for(Node node : graph.getAllNodeList()) {
			State state = new State(node);
			hashNodeState.put(node, state);
			state.addOriginNodes(abstManager.getAbstractedNode(node));
			fsm.addState(state);
		}
		
		for(Edge edge : graph.getEdgeList()) {
			Node fromNode = graph.getNode(edge.getFromNodeId(), true);
			Node toNode = graph.getNode(edge.getToNodeId(), true);
			
			State fromState = hashNodeState.get(fromNode);
			State toState = hashNodeState.get(toNode);
			
			if(edge.hasEvent()) {
				Event event = edge.getEvent();
				Callback callback = new Callback(toState);
				Interaction interaction = new Interaction(event, callback);
				
				State state = hashNodeState.get(fromNode);
				state.addInteraction(interaction);
			} else if(edge.hasCond()) {
				Transition trans = new Transition(fromState.getId(), toState.getId());
				trans.setCond(edge);
				
				fsm.addTransition(trans);
			} else {
				StringUtils.printError(this, "Invalid abstracted call graph", edge.getDotLabel());
			}
		}
		
		// Breadth first search
		for(Node node : graph.getAllNodeList()) {
			node.prepareSearch();
		}
		for(Edge edge : graph.getEdgeList()) {
			Node fromNode = graph.getNode(edge.getFromNodeId(), true);
			Node toNode = graph.getNode(edge.getToNodeId(), true);
			
			fromNode.addChild(toNode);
		}
		ArrayDeque<Node> queue = new ArrayDeque<Node>();
		queue.offer(CallGraph.getInitNode());
		while(!queue.isEmpty()) {
			Node node = queue.poll();
			
			State state = hashNodeState.get(node);
			List<Interaction> interactionList = state.getInteractionList();
			
			for(Node childNode : node.getChildren()) {
				if(childNode.visited()) {
					continue;
				}
				
				// Propagates already-registered interactions to states
				State childState = hashNodeState.get(childNode);
				
				// Gets current interaction that comes from "node" and goes to "childNode"
				Interaction curInteraction = null;
 				for(Interaction interaction : interactionList) {
					State cbState = interaction.getCallback().getState();
					if(childState.equals(cbState)) { // node -(interaction)-> childNode
						curInteraction = interaction;
						break;
					}
 				}
				if(isMasked(curInteraction)) {
					state.setMaskInteraction(curInteraction);
				}
				
 				for(Interaction interaction : interactionList) {
					State cbState = interaction.getCallback().getState();
					if(interaction.getEvent().isRepeatable() || !childState.equals(cbState)) {
						
						if(isExclusive(curInteraction, interaction)) {
							continue;
						}
						
						childState.addInteraction(interaction);
					}
				}

				childNode.visit();
				queue.offer(childNode);
			}
		}
		

		// Remove interactions at condition state
		// Because immediately transit
		for(Edge edge : graph.getEdgeList()) {
			if(edge.hasCond()) {
				Node fromNode = graph.getNode(edge.getFromNodeId(), true);
				State fromState = hashNodeState.get(fromNode);
				fromState.removeAllInteractions();
			}
		}
		
		
		// Adds transitions corresponding to interactions
		for(State state : fsm.getStateList()) {
			for(Interaction interaction : state.getInteractionList()) {
				Event event = interaction.getEvent();
				Callback callback = interaction.getCallback();
				
				Transition trans = new Transition(state.getId(), callback.getState().getId());
				trans.setEvent(event);
				fsm.addTransition(trans);
			}
		}
		
		return hashNodeState;
	}
	


	/**
	 * Sets enable/disable statements to each state
	 * @param fsm
	 * @param graph
	 * @param hashNodeState
	 * @param edManager
	 */
	private void setEnDisables(FiniteStateMachine fsm, CallGraph graph, HashMap<Node, State> hashNodeState, EnDisableManager edManager) {
		
		for(State state : fsm.getStateList()) {
			ArrayList<EnDisable> edList = new ArrayList<EnDisable>();
			for(Node node : state.getOriginNodeList()) {
				List<EnDisable> _edList = edManager.getEnDisableList(node.getId());
				edList.addAll(_edList);
			}
			
			for(EnDisable ed : edList) {
				state.addEnDisable(ed);
			}
		}
		

		/*
		// Breadth first search
		for(Node node : graph.getAllNodeList()) {
			node.prepareSearch();
		}
		for(Edge edge : graph.getEdgeList()) {
			Node fromNode = graph.getNode(edge.getFromNodeId(), true);
			Node toNode = graph.getNode(edge.getToNodeId(), true);
			
			fromNode.addChild(toNode);
		}
		ArrayDeque<Node> queue = new ArrayDeque<Node>();
		queue.offer(CallGraph.getInitNode());
		while(!queue.isEmpty()) {
			Node node = queue.poll();
			
			State state = hashNodeState.get(node);
			List<EnDisable> edList = state.getEnDisableList();
			
			for(Node childNode : node.getChildren()) {
				if(childNode.visited()) {
					continue;
				}
				
				// Propagates enable/disable statements
				State childState = hashNodeState.get(childNode);
				List<EnDisable> childEdList = childState.getEnDisableList();
				
				ArrayList<EnDisable> newEdList = new ArrayList<EnDisable>();
				newEdList.addAll(edList);
				newEdList.addAll(childEdList);
				
				childState.getEnDisableList().clear();
				for(EnDisable ed : newEdList) {
					childState.addEnDisable(ed);
				}

				childNode.visit();
				queue.offer(childNode);
			}
		}
		*/
	}
	
	
	/**
	 * Determines whether interactions at each state is enabled or disabled.
	 * And remove corresponding transitions if disabled.
	 * @param fsm Finite state machine as output
	 * @param graph Abstracted call graph
	 * @param edManager Provides enable and disable statement
	 */
	private void determineEnDisable(FiniteStateMachine fsm, HTMLParser html, CallGraph graph, EnDisableManager edManager) {
		
		ArrayList<Pair<State, Interaction>> disIntrList = new ArrayList<Pair<State, Interaction>>();
		
		for(State state : fsm.getStateList()) {
			System.out.println(state.getId() + ": " + state.getDotLabel());
			for(EnDisable ed : state.getEnDisableList()) {
				System.out.println("\t" + ed.toString());
				ed.setTargetId();
				System.out.println("\t\t" + ed.getTargetId());
			}

			for(Interaction interaction: state.getInteractionList()) {
				System.out.println("\t" + interaction.getEvent().getEvent());

				interaction.setTargetId();
				String targetId = interaction.getTargetId();
				System.out.println("\t\t" + targetId);
				
				
				/*
				Object intr_target = interaction.getEvent().getTargetObj();
				Object intr_ev = interaction.getEvent().getEventObj();
				
				ArrayList<Element> intr_targets = new ArrayList<Element>();
				if(intr_target instanceof Element) {
					Element target = (Element)intr_target;
					System.out.println("\t\t" + target.attr("id"));
					intr_targets.add(target);
				} else if(intr_target == null &&
						intr_ev instanceof AstNode &&
						interaction.getEvent().getRule() instanceof Trigger) {
					AstNode parent = ((AstNode)intr_ev).getParent();
					
					if(parent instanceof PropertyGet) {
						AstNode target = ((PropertyGet)parent).getTarget();
						System.out.println("aa: " + target.toSource());
					}
					System.out.println(parent.getClass());
					
				} else if(intr_target == null &&
						intr_ev instanceof AstNode &&
						interaction.getEvent().getRule() instanceof Function) {
					
					Function rule = (Function)interaction.getEvent().getRule();
					AstNode eventAstNode = (AstNode)intr_ev;
					
					if(XMLAttr.RuleProp_PropTarget.equals(rule.getTarget())) {
						List<Element> solveResult = TargetSolver.solve(eventAstNode, rule, html);
						if(solveResult == null) {
							continue;
						}
						
						intr_targets.addAll(solveResult);
						
					} else {
						System.out.println("\t\tBuilt-in/Library object?: " + interaction.getEvent().getEvent());
						continue;
					}
					
				} else {
					System.out.println("kokokoko");
				}
				
				// Determines whether this interactions is disabled or not
				boolean disabled = false;
				for(Element intr_elm : intr_targets) {
					disabled = this.isDisable(state, intr_elm, html);
				}
				*/
				
				boolean disabled = false;
				for(EnDisable ed : state.getEnDisableList()) {
					String edTargetId = ed.getTargetId();
					if(targetId != null && targetId.equals(edTargetId)) {
						disabled = ed.isDisabled();
					}
				}
				
				if(disabled) {
					Pair<State, Interaction> disIntr = Pair.of(state, interaction);
					disIntrList.add(disIntr);
				}
			}
			
		}
		
		
		for(Pair<State, Interaction> disIntr : disIntrList) {
			System.out.println("Remove: at " + disIntr.getLeft().getId() + ", Int: " + disIntr.getRight().getEvent().getEvent());
			fsm.removeTransition(disIntr.getLeft(), disIntr.getRight());
		}
		
		// to be debugged
		// User click?
		for(State state : fsm.getStateList()) {
			if(state.isMasked()) {
				Interaction maskIntr = state.getMaskInteraction();
				for(Interaction interaction : state.getInteractionList()) {
					if(!maskIntr.getId().equals(interaction.getId())) {
						fsm.removeTransition(state, interaction);
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param state
	 * @param target
	 * @param html
	 * @return
	 */
	private boolean isDisable(State state, Element target, HTMLParser html) {
		System.out.println("\t\tintr id: " + target.id());
		
		boolean disabled =  false;
		for(EnDisable ed : state.getEnDisableList()) {
			ArrayList<Element> edElementList = new ArrayList<Element>();
			
			// Solves targets of this enable/disable statements
			if(ed.getTarget() != null) {
				edElementList.add(ed.getTarget());
			} else {
				AstNode jsTargetNode = ed.getJSTargetNode();
				JSControl rule = (JSControl)ed.getRule();
				
				List<Element> solveResult = TargetSolver.solve(jsTargetNode, rule, html);
				if(solveResult == null) {
					continue;
				}
				
				edElementList.addAll(solveResult);
			}
			
			// Determines
			for(Element edElement : edElementList) {
				System.out.println("\t\t\ted id: " + edElement.id() + ", dis: " + ed.isDisabled() + ", match: " + containsById(edElement, target));
				
				if(containsById(edElement, target)) {
					disabled = ed.isDisabled();
				}
			}
		}
		System.out.println("\t\t" + disabled);
		return disabled;
	}
	
	/**
	 * Determines whether parent HTML element equals/contains target one as its child
	 * @param parent
	 * @param target
	 * @return
	 */
	private static boolean containsById(Element parent, Element target) {
		if(target == null || parent == null || "".equals(target.id())) {
			return false;
		}
		
		List<String> childrenIdList = getChildrenIdList(parent);
		for(String id : childrenIdList) {
			if(target.id().equals(id)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Gets children HTML elements of given one
	 * @param element Given HTML element
	 * @returnGiven HTML element and its children
	 */
	private static List<String> getChildrenIdList(Element element) {
		ArrayList<String> ret = new ArrayList<String>();
		ret.add(element.id());
		
		for(Element child : element.children()) {
			List<String> childrenIdList = getChildrenIdList(child);
			ret.addAll(childrenIdList);
		}
		
		return ret;
	}
	
	
	/**
	 * Solves target element in HTML/object in JS from JS AST node (to be debugged)
	 * @author Yuta Maezawa
	 *
	 */
	public static class TargetSolver {
		
		public static List<Element> solve(AstNode astNode, Function rule, HTMLParser html) {
			ArrayList<Element> ret = new ArrayList<Element>();

			// To be debugged
			// e.g. $(CSSQuery).event
			if(astNode instanceof Name && // event is Name
					astNode.getParent() instanceof PropertyGet && // Object.event is PropertyGet
					((PropertyGet)astNode.getParent()).getTarget() instanceof FunctionCall && // If Object = $(), Object is FunctionCall
					"$".equals(((FunctionCall)((PropertyGet)astNode.getParent()).getTarget()).getTarget().toSource())) { // If function name is $
				FunctionCall funcCall = (FunctionCall)((PropertyGet)astNode.getParent()).getTarget();
				if(funcCall.getArguments().get(0) instanceof StringLiteral) {
					String query = StringUtils.removeQuote(funcCall.getArguments().get(0).toSource());
					
					// Semantic is get (to be debugged)
					Elements elms = html.getDoc().select(query);
					for(Element elm : elms) {
						ret.add(elm);
					}
					
				}
			}
			
			return ret;
		}
		
		public static List<Element> solve(AstNode astNode, JSControl rule, HTMLParser html) {
			ArrayList<Element> ret = new ArrayList<Element>();
			
			// To be debugged
			// e.g. $(CSSQuery) for jQuery
			if(astNode instanceof FunctionCall &&
					"$".equals(((FunctionCall)astNode).getTarget().toSource())) {
				FunctionCall funcCall = (FunctionCall)astNode;
				if(funcCall.getArguments().get(0) instanceof StringLiteral) {
					String query = StringUtils.removeQuote(funcCall.getArguments().get(0).toSource());

//					if(XMLAttr.RuleSemantic_Get.equals(rule.getSemantic())) {
//						
//					} else
					if(XMLAttr.RuleSemantic_Set.equals(rule.getSemantic())) { // Same as Get
						Elements elms = html.getDoc().select(query);
						for(Element elm : elms) {
							ret.add(elm);
						}
					} else if(XMLAttr.RuleSemantic_Append.equals(rule.getSemantic())) {
						Element doc = html.getDoc().append(query);
						
						for(Element elm : doc.select("#" + getId(query))) {
							ret.add(elm);
						}
					} else if(XMLAttr.RuleSemantic_Remove.equals(rule.getSemantic())) {
						Elements elms = html.getDoc().select(query);
						elms.remove();
					}  else {
						System.out.println(rule.getSemantic());
					}
					
				} else if (funcCall.getArguments().get(0) instanceof InfixExpression) {
					Limitation limitation = new Limitation(funcCall.getArguments().get(0).toSource(), Limitation.JS_Infix_Node, "");
					LimitationManager.addLimitation(limitation);
					return null;
				} else {
					System.out.println(funcCall.getArguments().get(0).toSource());
					System.out.println(funcCall.getArguments().get(0).getClass());
				}
			}
			
			return ret;
		}
		

		/**
		 * Hard coding
		 */
		public static String getTargetId(AstNode astNode) {
			if(astNode instanceof FunctionCall) {
				// document.getElementById("ID").disable = true;
				return getElementId((FunctionCall)astNode);
			}
			else if(astNode instanceof Name) {
				return getElementId((Name)astNode);
			}
			return null;
		}
		public static String getElementId(FunctionCall funcCallAstNode) {
			if("document.getElementById".equals(funcCallAstNode.getTarget().toSource())) {
				AstNode argAstNode = funcCallAstNode.getArguments().get(0);
				if(argAstNode instanceof StringLiteral) {
					String id = StringUtils.removeQuote(argAstNode.toSource());
					return id;
				}
				else {
					System.out.println("Unknown argument: " + argAstNode.getClass() + ", src= " + argAstNode.toSource() + "; func is: " + funcCallAstNode.getTarget().toSource());
				}
			}
			
			else {
				System.out.println("Unknown function: " + funcCallAstNode.getTarget().toSource());
			}
			return null;
		}
		public static String getElementId(Name nameAstNode) {
			return nameAstNode.toSource();
		}
		public Scope getParentScope(AstNode astNode) {
			AstNode parent = astNode.getParent();
			if(parent instanceof Scope) {
				return (Scope)parent;
			} else {
				return getParentScope(parent);
			}
		}
		
		/**
		 * Try to get HTML id value from DOM manipulations via raw texts
		 * @param raw_html_code
		 * @return
		 * @deprecated
		 */
		private static String getId(String raw_html_code) {
			int index = raw_html_code.indexOf("id=\"");
			String tmp = raw_html_code.substring(index+4, raw_html_code.length()-1);
			index = tmp.indexOf("\"");
			return tmp.substring(0, index);
		}
	}
	
	
	
	
	
	
	
	
	
	/**
	 * !!TO BE DEBUGGED!!
	 * HARD CODINGS
	 */

	/**
	 * Hard coding. to be debugged.
	 * Updates description capability of distinguishing rule: <Exclusive>...</Exclusive>
	 * @param interaction_1
	 * @param interaction_2
	 * @return
	 * @deprecated
	 */
	public static boolean isExclusive(Interaction interaction_1, Interaction interaction_2) {
		if(interaction_1 == null || interaction_2 == null) {
			return false;
		}
		if(interaction_1.getEvent() == null || interaction_2.getEvent() == null) {
			return false;
		}
		
		// Hard coding
		if(interaction_1.getEvent().getEvent().equals("onSuccess") &&
				interaction_2.getEvent().getEvent().equals("onFailure")) {
			return true;
		}
		else if(interaction_1.getEvent().getEvent().equals("onFailure") &&
				interaction_2.getEvent().getEvent().equals("onSuccess")) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Hard coding. to be debugged.
	 * Updates description capability of distinguishing rule: 
	 * @param interaction
	 * @return
	 * @deprecated
	 */
	public static boolean isMasked(Interaction interaction) {
		if(interaction == null || interaction.getEvent() == null) {
			return false;
		}
		
		if(interaction.getEvent().getEvent().equals("User Click")) {
			return true;
		}
		
		return false;
	}
	
}
