package jp.mzw.jsanalyzer.serialize.model;

public class Transition extends SerializableElement {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected jp.mzw.jsanalyzer.serialize.model.Event mEvent;
	protected jp.mzw.jsanalyzer.serialize.model.MyAstNode mCond;
	
	protected String mFromStateId;
	protected String mToStateId;
	
	public Transition(String id, String fromStateId, String toStateId) {
		super(id);

		this.mFromStateId = fromStateId;
		this.mToStateId = toStateId;
	}
	
	public String getFromStateId() {
		return this.mFromStateId;
	}
	public String getToStateId() {
		return this.mToStateId;
	}
	
	public void setEvent(jp.mzw.jsanalyzer.serialize.model.Event event) {
		this.mEvent = event;
	}
	public jp.mzw.jsanalyzer.serialize.model.Event getEvent() {
		return this.mEvent;
	}
	public void setCond(jp.mzw.jsanalyzer.serialize.model.MyAstNode cond) {
		this.mCond = cond;
	}
	
	public jp.mzw.jsanalyzer.serialize.model.MyAstNode getCond() {
		return this.mCond;
	}
}
