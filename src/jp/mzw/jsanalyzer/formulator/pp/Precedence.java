package jp.mzw.jsanalyzer.formulator.pp;

public class Precedence extends Order {
	
	
	public Precedence(int scope) {
		super(scope);
	}
	
	public String getCTLTemplate() {
		switch(this.mScope) {
		case Scope.Globally:
			return "!E[!$S U ($P & !$S)]"; // A[!P W S] = !E[!S U (!!P & !S)] = !E[!S U (P & !S)]
		}
		System.err.println("To be implemented: " + this.mScope + ", " + Precedence.class);
		return null;
	}
}
