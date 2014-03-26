package jp.mzw.jsanalyzer.formulator.property;

import jp.mzw.jsanalyzer.formulator.adp.UserAction;
import jp.mzw.jsanalyzer.formulator.pp.PropertyPattern;
import jp.mzw.jsanalyzer.formulator.pp.Response;

public class AsyncComm extends Property {

	public AsyncComm() {
		this.mPropertyName = "Asynchronous communication";
		this.mPropertyNameAbbreviation = "AsyncComm";
		
		this.mRequirement = "Ajax apps can handle user events during asynchronous communications";
		this.mAjaxDesignPattern = new UserAction();
		
		this.mPropertyPatternScope = PropertyPattern.Scope.Globally;
		this.mPropertyPattern = new Response(this.mPropertyPatternScope);
	}
	
	/**
	 * 
	 * @param P Ajax request function
	 * @param S User events (nEXt)
	 * @return
	 */
	public String genCTLFormula(String P, String S) {
		String template = this.mPropertyPattern.getCTLTemplate();
		
		String formula = template.replace("$P", P).replace("$S", "EX " + S);
		
		return formula;
	}
	
}
