package jp.mzw.jsanalyzer.modeler.model.interaction;

import jp.mzw.jsanalyzer.modeler.TargetSolver;

import org.jsoup.nodes.Element;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.PropertyGet;


public class Interaction extends jp.mzw.jsanalyzer.modeler.model.Element {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Event mEvent;
	protected Callback mCallback;
	
	
	public Interaction(Event event, Callback callback) {
		this.mEvent = event;
		this.mCallback = callback;
	}
	
	public Event getEvent() {
		return this.mEvent;
	}
	public Callback getCallback() {
		return this.mCallback;
	}
	
	
	String mTargetId;
	public void setTargetId() {
		if(this.mEvent.getEventObj() instanceof Element) {
			Element target = (Element)this.mEvent.getTargetObj();
			this.mTargetId = target.attr("id");
		} else if(this.mEvent.getEventObj() instanceof AstNode
				&& this.mEvent.getTargetObj() != null) {
			AstNode target = (AstNode)this.mEvent.getTargetObj();
			if(target instanceof FunctionCall) {
				this.mTargetId = TargetSolver.getElementId((FunctionCall)target);
			} else if(target instanceof Name) {
				this.mTargetId = TargetSolver.getElementId((Name)target);
			}
			else {
				System.out.println("Unknown JS element class: " + target.getClass() + ", src= " + target.toSource());
			}
		}
		else {
			System.err.println("Unknown branch@Interaction.setTargetId: " + this.getId());
		}
		
	}
	public String getTargetId() {
		return this.mTargetId;
	}
 	
//	public boolean isSame(Interaction interaction) {
//		if(this.mEvent.isSame(interaction.getEvent()) &&
//				this.mCallback.isSame(interaction.getCallback())) {
//			return true;
//		}
//		
//		return false;
//	}
}
