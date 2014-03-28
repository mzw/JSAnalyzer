package jp.mzw.jsanalyzer.formulator.adp;

public class AjaxDesignPattern {

	protected String mName;
	protected int mCategory;
	
	protected AjaxDesignPattern() {
		// NOP
	}
	
	public AjaxDesignPattern(int category, String name) {
		this.mCategory = category;
		this.mName = name;
	}
	
	public String getName() {
		return this.mName;
	}
	public int getCategoryId() {
		return this.mCategory;
	}

	public static class Category {
		public static final int
			Fundamental = 1,
			Programming = 2,
			FunctionAndUsability = 3,
			Development = 4;

		public static String getCategoryName(int category) {
			switch(category) {
			case Fundamental:
				return "Fundamental";
			case Programming:
				return "Programming";
			case FunctionAndUsability:
				return "FunctionAndUsability";
			case Development:
				return "Development";
			}
			
			return null;
		}
		public static int getCategoryId(String name) {
			if(name == null) {
				return -1;
			}
			
			if("Fundamental".equals(name)) {
				return Fundamental;
			} else if("Programming".equals(name)) {
				return Programming;
			} else if("FunctionAndUsability".equals(name)) {
				return FunctionAndUsability;
			} else if("Development".equals(name)) {
				return Development;
			}
			
			return -1;
		}
	}
}
