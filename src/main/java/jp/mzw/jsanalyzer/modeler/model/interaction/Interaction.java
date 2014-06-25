package jp.mzw.jsanalyzer.modeler.model.interaction;

import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.modeler.EnDisableManager;
import jp.mzw.jsanalyzer.modeler.TargetSolver;
import jp.mzw.jsanalyzer.util.StringUtils;

import org.jsoup.nodes.Element;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.Name;


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
	public void setTargetId(Analyzer analyzer, EnDisableManager edManager) {
		/// From HTML code
		if(this.mEvent.getTargetObj() instanceof Element) {
			Element target = (Element)this.mEvent.getTargetObj();
			this.mTargetId = target.attr("id");
		}
		/// From JS code
		else if(this.mEvent.getEventObj() instanceof AstNode) {
//			AstNode astNode = (AstNode)this.mEvent.getEventObj();
			
			if(this.mEvent.getTargetObj() != null) {
				AstNode target = (AstNode)this.mEvent.getTargetObj();
				if(target instanceof FunctionCall) {
					this.mTargetId = TargetSolver.getElementIdBy((FunctionCall)target, analyzer);
				} else if(target instanceof Name) {
//					this.mTargetId = TargetSolver.getElementId((Name)target);
					this.mTargetId = edManager.findElementId(TargetSolver.getParentScope(target), target.toSource());
				}
				else {
					StringUtils.printError(this, "Unknown JS element class@Interaction.setTargetId", target.getClass() + ", src= " + target.toSource());
				}
			}
			/// Target has not determined yet
			else {
				/// To be debugged
				/// User Click?
//				StringUtils.printError(this, "Built-in or Library function?", this.getEvent().getEvent());
			}
		}
		else {
			StringUtils.printError(this, "#setTargetId, Cannot parse element ID", this.getId() + ", event obj: " + this.mEvent.getEventObj() + ", " + this.mEvent.getEventObj().getClass().getName());
		}
		
	}
	public String getTargetId() {
		return this.mTargetId;
	}
 	
}
