package jp.mzw.jsanalyzer.serialize;

import java.util.HashMap;

import jp.mzw.jsanalyzer.config.FileExtension;
import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.core.IdGen;
import jp.mzw.jsanalyzer.modeler.model.fsm.FiniteStateMachine;
import jp.mzw.jsanalyzer.modeler.model.fsm.State;
import jp.mzw.jsanalyzer.modeler.model.fsm.Transition;
import jp.mzw.jsanalyzer.modeler.model.graph.Node;
import jp.mzw.jsanalyzer.modeler.model.interaction.Event;
import jp.mzw.jsanalyzer.util.TextFileUtils;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.FunctionNode;

public class Serializer {

	/**
	 * Serializes an extracted finite state machine
	 * @param fsm Extracted finite state machine
	 * @deprecated
	 */
	public static void serialze(Analyzer analyzer, FiniteStateMachine fsm) {
		
		jp.mzw.jsanalyzer.serialize.model.FiniteStateMachine sFsm = new jp.mzw.jsanalyzer.serialize.model.FiniteStateMachine(fsm.getId());
		sFsm.setRootStateId(fsm.getInitState().getId());
		sFsm.setExitStateId(fsm.getExitState().getId());
		
		/// States
		for(State state : fsm.getStateList()) {
			jp.mzw.jsanalyzer.serialize.model.State sState = new jp.mzw.jsanalyzer.serialize.model.State(state.getId());
			
			/// Abstracted
			/// To be debugged: only function call or node
			for(Node node : state.getOriginNodeList()) {
				
				jp.mzw.jsanalyzer.serialize.model.State.FuncElement funcElement = null;
				if(node.getAstNode() instanceof FunctionNode) {
					funcElement = new jp.mzw.jsanalyzer.serialize.model.State.FuncElement(
							node.getId(),
							Serializer.genMyAstNode(node.getAstNode()),
							((FunctionNode)node.getAstNode()).getName()
							);	
				} else if(node.getAstNode() instanceof FunctionCall) {
					funcElement = new jp.mzw.jsanalyzer.serialize.model.State.FuncElement(
							node.getId(),
							Serializer.genMyAstNode(node.getAstNode()),
							((FunctionCall)node.getAstNode()).getTarget().toSource()
							);
				}
				
				if(funcElement != null) {
					sState.addFuncElement(funcElement);
				}
			}
			
			sFsm.addState(sState);
		}
		
		/// Transitions
		for(Transition trans : fsm.getTransList()) {
			jp.mzw.jsanalyzer.serialize.model.Transition sTrans = new jp.mzw.jsanalyzer.serialize.model.Transition(
					trans.getId(), trans.getFromStateId(), trans.getToStateId());
			if(trans.hasEvent()) {
				Event event = trans.getEvent();
				
				jp.mzw.jsanalyzer.serialize.model.Event sEvent = null;
				if (event.getEventObj() instanceof org.jsoup.nodes.Element) {
					org.jsoup.nodes.Element elm = (org.jsoup.nodes.Element)event.getEventObj();
					sEvent = new jp.mzw.jsanalyzer.serialize.model.Event(event.getId(), event.getEvent(), elm.attr("id"));
				} else if (event.getEventObj() instanceof AstNode) {
					AstNode astNode = (AstNode)event.getEventObj();
					sEvent = new jp.mzw.jsanalyzer.serialize.model.Event(event.getId(), event.getEvent(), astNode.getLineno(), astNode.getPosition());
				}
				
				sTrans.setEvent(sEvent);
			}
			
			if(trans.hasCond()) {
				AstNode cond = trans.getCond();
				jp.mzw.jsanalyzer.serialize.model.MyAstNode sCond = Serializer.genMyAstNode(cond);
				sTrans.setCond(sCond);
			}
			
			sFsm.addTrans(sTrans);
		}
		

		
		String objName = analyzer.getProject().getName() + ".fsm" + FileExtension.Serialized;
		TextFileUtils.serialize(analyzer.getProject().getDir(), objName, sFsm);
		
		
	}
	
	public static jp.mzw.jsanalyzer.serialize.model.MyAstNode genMyAstNode(AstNode astNode) {
		jp.mzw.jsanalyzer.serialize.model.MyAstNode ret = new jp.mzw.jsanalyzer.serialize.model.MyAstNode(IdGen.genId());
		
		HashMap<String, Object> propList = new HashMap<String, Object>();

		propList.put(jp.mzw.jsanalyzer.serialize.model.MyAstNode.Property.Type, astNode.getType());
		propList.put(jp.mzw.jsanalyzer.serialize.model.MyAstNode.Property.LineNo, astNode.getLineno());
		propList.put(jp.mzw.jsanalyzer.serialize.model.MyAstNode.Property.Position, astNode.getPosition());
		propList.put(jp.mzw.jsanalyzer.serialize.model.MyAstNode.Property.Source, astNode.toSource());
		
		ret.setPropertyList(propList);
		
		return ret;
		
	}

}
