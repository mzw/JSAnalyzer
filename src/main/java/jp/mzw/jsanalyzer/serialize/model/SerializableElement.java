package jp.mzw.jsanalyzer.serialize.model;

import java.io.Serializable;

public class SerializableElement implements Serializable {
	
	/**
	 * For serialization
	 */
	private static final long serialVersionUID = 1L;
	
	protected String mId;
	public SerializableElement(String id) {
		this.mId = id;
	}
	
	public String getId() {
		return this.mId;
	}
}
