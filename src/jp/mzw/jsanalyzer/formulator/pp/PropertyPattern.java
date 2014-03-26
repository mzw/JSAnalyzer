package jp.mzw.jsanalyzer.formulator.pp;


public class PropertyPattern {
	
	protected int mScope;
	
	public PropertyPattern(int scope) {
		this.mScope = scope;
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
	}
}
