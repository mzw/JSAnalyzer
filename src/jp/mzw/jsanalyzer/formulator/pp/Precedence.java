package jp.mzw.jsanalyzer.formulator.pp;

public class Precedence extends Order {
	public Precedence() {
		super("");
	}
	
	public static String getCTLTemplate() {
		// A[!P W S] = !E[!S U (!!P & !S)] = !E[!S U (P & !S)]
		return "!E[!S U (P & !S)]";
	}
	
	public static String genCTLFormulaGloabally(String P, String S) {
		return "!E[!" + S + " U (" + P + "& !" + S + ")]";
	}
}
