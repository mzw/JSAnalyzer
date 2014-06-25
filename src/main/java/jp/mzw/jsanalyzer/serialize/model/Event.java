package jp.mzw.jsanalyzer.serialize.model;


public class Event extends SerializableElement {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String mEvent;
	protected String mElementId;
	protected int mLineNo;
	protected int mPosition;
	
	public Event(String id, String event, String elementId) {
		super(id);
		mEvent = event;
		mElementId = elementId;
	}
	public Event(String id, String event, int lineno, int position) {
		super(id);
		mEvent = event;
		mLineNo = lineno;
		mPosition = position;
	}
	public String getEvent() {
		return mEvent;
	}
	public String getElementId() {
		return mElementId;
	}
	public int getLineNo() {
		return mLineNo;
	}
	public int getPosition() {
		return mPosition;
	}
}
