package jp.mzw.jsanalyzer.formulator.pp;

import java.util.ArrayList;
import java.util.List;

public class PropertyPattern {
	
	protected int mScope;
	
	public PropertyPattern(int scope) {
		this.mScope = scope;
	}
	
	public int getScope() {
		return this.mScope;
	}
	
	public String getCTLTemplate() {
		return null;
	}
	
	public static class Scope {
		public static final int
			Globally = 0,
			Before = 1,
			After = 2,
			Between = 3,
			AfterUntil = 4;
		
		public static String getScopeName(int scope) {
			switch(scope) {
			case Globally:
				return "Globally";
			case Before:
				return "Before";
			case After:
				return "After";
			case Between:
				return "Between";
			case AfterUntil:
				return "AfterUntil";
			}
			
			return null;
		}
		public static int getScopeId(String name) {
			if(name == null) {
				return -1;
			}
			
			if("Globally".equals(name)) {
				return Globally;
			} else if("Before".equals(name)) {
				return Before;
			} else if("After".equals(name)) {
				return After;
			} else if("Between".equals(name)) {
				return Between;
			} else if("AfterUntil".equals(name)) {
				return AfterUntil;
			}
			
			return -1;
		}
	}
}
