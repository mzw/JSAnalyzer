package jp.mzw.jsanalyzer.serialize.model;

import java.util.HashMap;

public class MyAstNode extends SerializableElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected HashMap<String, Object> mPropertyList;

	public MyAstNode(String id) {
		super(id);
		
		this.mPropertyList = new HashMap<String, Object>();
	}
	
	public void setPropertyList(HashMap<String, Object> info) {
		this.mPropertyList = info;
	}
	public HashMap<String, Object> getPropertyList() {
		return this.mPropertyList;
	}
	
	public Object getProperty(String propName) {
		return this.mPropertyList.get(propName);
	}
	
	public static class Property {
		public static final String Type = "type";
		public static final String LineNo = "lineno";
		public static final String Position = "position";
		public static final String Source = "src";
		
	}
}
