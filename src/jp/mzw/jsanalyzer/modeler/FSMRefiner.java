package jp.mzw.jsanalyzer.modeler;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.nodes.Element;

import jp.mzw.jsanalyzer.core.Analyzer;
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
import jp.mzw.jsanalyzer.util.StringUtils;

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
		
		this.determineEnDisable(fsm, edManager);
		
		this.addExitTransitions(fsm);
		
//		fsm.setAbstractionManager(abstManager);
//		fsm.setEnDisableManager(edManager);
		
		return fsm;
	}
	
	/**
	 * Adds transitions to the exit state (exit transitions) from states that do not handle any interactions
	 * @param fsm Extracted finite state machine that has not yet been added the exit transitions
	 */
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
				StringUtils.printError(this, "Invalid abstracted call graph", edge.getDotLabel() + ", from-to: " + fromNode.getId() + "-" + toNode.getId());
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
		
	}
	
	
	/**
	 * Determines whether interactions at each state is enabled or disabled.
	 * And remove corresponding transitions if disabled.
	 * @param fsm Finite state machine as output
	 * @param graph Abstracted call graph
	 * @param edManager Provides enable and disable statement
	 */
	private void determineEnDisable(FiniteStateMachine fsm, EnDisableManager edManager) {
		this.setTargetId(fsm, edManager);
		/// Recursively traverses until no refineable element in FSM
		this.traverse(fsm, edManager);
	}
	
	private void setTargetId(FiniteStateMachine fsm, EnDisableManager edManager) {
		this.setTargetId(fsm, edManager, false);
	}
	
	private void setTargetId(FiniteStateMachine fsm, EnDisableManager edManager, boolean print) {
		for(State state : fsm.getStateList()) {
			
			if(print) System.out.println("StateId = " + state.getId());
				
			if(print) System.out.println("\tEnable/Disable statements");
			for(EnDisable ed : state.getEnDisableList()) {
				ed.setTargetId(this.mAnalyzer, edManager);
				if(print) System.out.println("\t\t#" + ed.getTargetId() + " is disabled?: " + ed.isDisabled());
			}

			if(print) System.out.println("\tInteractions");
			for(Interaction interaction: state.getInteractionList()) {
				interaction.setTargetId(this.mAnalyzer, edManager);
				if(print) System.out.println("\t\t#" + interaction.getTargetId() + " whose event is: " + interaction.getEvent().getEvent());
			}
		}
	}
	
	private void traverse(FiniteStateMachine fsm, EnDisableManager edManager) {
		for(State state : fsm.getStateList()) {
			state.prepareSearch();
		}
		this.findDisabledInteraction(fsm.getInitState().getId(), fsm, edManager, new ArrayList<EnDisable>());
	}
	
	private void findDisabledInteraction(String curStateId, FiniteStateMachine fsm, EnDisableManager edManager, List<EnDisable> context) {
		this.findDisabledInteraction(curStateId, fsm, edManager, context, true);
	}
	
	private void findDisabledInteraction(String curStateId, FiniteStateMachine fsm, EnDisableManager edManager, List<EnDisable> context, boolean print) {
		State curState = fsm.getState(curStateId);

		/// Prevents infinite loop
		if(curState.visited()) {
			return;
		} else {
			curState.visit();
		}
		
		/// Inherits previous context
		List<EnDisable> curContext = new ArrayList<EnDisable>();
		curContext.addAll(context);
		curContext.addAll(curState.getEnDisableList());
		
		/// Determines whether interactions at current state are disabled or not
		for(Interaction interaction : curState.getInteractionList()) {
			boolean isDisabled = false;
			for(EnDisable ed : curContext) {
				if(interaction.getTargetId() != null && interaction.getTargetId().equals(ed.getTargetId())) {
					isDisabled = ed.isDisabled();
				}
			}
			/// This interaction is disabled at this state
			if(isDisabled) {
				/// Removes this intaraction
				curState.removeInteraction(interaction.getId());
				fsm.removeTransition(curState, interaction);
				
				if(print) {
					System.out.println("@State.id = " + curStateId);
					System.out.println("\tDisabled interaction: " + interaction.getEvent().getEvent());
				}
				
				/// Recursively traverses
				this.traverse(fsm, edManager);
				return;
			}
		}
		
		/// Goes to next states if no disabled interactions at currect state
		for(Transition trans : fsm.getTransListFrom(curState.getId())) {
			this.findDisabledInteraction(trans.getToStateId(), fsm, edManager, curContext, print);
		}
	}
	
	
	/**
	 * Gets children HTML elements of given one
	 * @param element Given HTML element
	 * @returnGiven HTML element and its children
	 * @deprecated
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
