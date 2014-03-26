package jp.mzw.jsanalyzer.serialize.model;

import java.util.ArrayList;
import java.util.List;

public class State extends SerializableElement {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<FuncElement> mFuncElementList;
	
	public State(String id) {
		super(id);
		this.mFuncElementList = new ArrayList<FuncElement>();
	}
	
	public void addFuncElement(FuncElement func) {
		this.mFuncElementList.add(func);
	}
	public List<FuncElement> getFuncElement() {
		return this.mFuncElementList;
	}
	

	public static class FuncElement extends SerializableElement {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private jp.mzw.jsanalyzer.serialize.model.MyAstNode mAstNode;
		private String mFuncName;
		int pos = -1;
		int lineno = -1;
		
		public FuncElement(String id, jp.mzw.jsanalyzer.serialize.model.MyAstNode astNode, String funcname) {
			super(id);
			
			mAstNode = astNode;
			mFuncName = funcname;
			
			pos = Integer.parseInt(mAstNode.getProperty(jp.mzw.jsanalyzer.serialize.model.MyAstNode.Property.Position).toString());
			lineno = Integer.parseInt(mAstNode.getProperty(jp.mzw.jsanalyzer.serialize.model.MyAstNode.Property.LineNo).toString());
		}
		
		public String getFuncName() {
			return mFuncName;
		}
		public int getPosition() {
			return pos;
		}
		public int getLineNo() {
			return lineno;
		}
		
		public boolean equals(FuncElement funcElement) {
			if(funcElement.getLineNo() == this.getLineNo() &&
					funcElement.getPosition() == this.pos) {
				return true;
			}
				
			return false;
		}
	}
}
