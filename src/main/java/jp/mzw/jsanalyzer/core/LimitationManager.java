package jp.mzw.jsanalyzer.core;

import java.util.ArrayList;
import java.util.List;

public class LimitationManager {
	
	/**
	 * Contains limitations
	 */
	private static ArrayList<Limitation> limitationList = new ArrayList<Limitation>();
	
	/**
	 * Adds an limitation
	 * @param limitation
	 */
	public static void addLimitation(Limitation limitation) {
		limitationList.add(limitation);
	}
	
	/**
	 * Gets limitations
	 * @return
	 */
	public static List<Limitation> getLimitations() {
		return limitationList;
	}
	
	
	public static class Limitation {
		
		protected String mValue;
		protected String mMessage;
		protected int mType;
		
		/**
		 * Constructor
		 * @param value A value causing this limitation
		 * @param limitaion A message representing this limitation
		 * @param type This limitation type
		 */
		public Limitation(String value, int type, String message) {
			this.mValue = value;
			this.mMessage = message;
			this.mType = type;
		}
		
		public String toString() {
			String ret = "";
			
			ret += "[" + getType(this.mType) + "] ";
			ret += this.mMessage + ": ";
			ret += this.mValue;
			
			return ret;
		}
		

		/**
		 * Limitation types
		 */
		public static final int
			CSS_Pseudo_Class = 1,
			JS_Infix_Node = 2,
			JS_CANNOT_DETERMINE_INTERACTION_TARGET = 3,
			JS_No_Callback = 4,
			JS_Dataflow = 5;
		
		public static String getType(int type) {
			switch(type) {
			case CSS_Pseudo_Class:
				return "CSS pseudo class";
			case JS_Infix_Node:
				return "JavaScript infix node";
			case JS_CANNOT_DETERMINE_INTERACTION_TARGET:
				return "Cannot determine interaction target";
			case JS_No_Callback:
				return "Cannot find callback node in spite of existing event node";
			case JS_Dataflow:
				return "Cannot analyze dataflow currently";
			default:
				return "No match limitation type";
			}
		}
	}
}