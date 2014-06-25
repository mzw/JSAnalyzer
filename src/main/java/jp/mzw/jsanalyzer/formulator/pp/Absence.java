package jp.mzw.jsanalyzer.formulator.pp;

public class Absence extends Occurrence {
	
	public Absence(int scope) {
		super(scope);
	}
	
//	public static String getCTLGlobally(String P) {
//		if(P == null || "".equals(P)) {
//			return null;
//		}
//		/// AG(!P)
//		return "AG(!(" + P + "))";
//	}

	public String getCTLTemplate() {
		switch(this.mScope) {
		case Scope.Globally:
			return "AG(!$P)"; // AG(!P)
		}
		System.err.println("To be implemented: " + this.mScope + ", " + Absence.class);
		return null;
	}
	
}
