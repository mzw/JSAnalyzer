package jp.mzw.jsanalyzer.formulator.pp;

public class Response extends Order {
	
	
	public Response(int scope) {
		super(scope);
	}
	
	public String getCTLTemplate() {
		switch(this.mScope) {
		case Scope.Globally:
			return "AG($P -> AF($S))";
//		case Scope.Before:
//			return "A[((P -> A[!R U (S & !R)]) | AG(!R)) W R]";
//		case Scope.After:
//			return "A[!Q W (Q & AG(P -> AF(S))] ";
//		case Scope.Between:
//			return "AG(Q & !R -> A[((P -> A[!R U (S & !R)]) | AG(!R)) W R])";
//		case Scope.AfterUntil:
//			return "AG(Q & !R -> A[(P -> A[!R U (S & !R)]) W R])";
		}
		
		System.err.println("To be implemented: " + this.mScope + ", " + Response.class);
		return null;
	}
}
