package jp.mzw.jsanalyzer.formulator.property;

import jp.mzw.jsanalyzer.formulator.adp.XMLHttpRequestCall;
import jp.mzw.jsanalyzer.formulator.pp.PropertyPattern;
import jp.mzw.jsanalyzer.formulator.pp.Response;

public class AsyncComm extends Property {

	public AsyncComm() {
		this.mPropertyName = "Asynchronous communication";
		this.mPropertyNameAbbreviation = "AsyncComm";
		
		this.mRequirement = "Ajax apps can handle user events during asynchronous communications";
		this.mAjaxDesignPattern = new XMLHttpRequestCall();
		
		this.mPropertyPatternScope = PropertyPattern.Scope.Globally;
		this.mPropertyPattern = new Response(this.mPropertyPatternScope);

		this.addVeriables("$P", "Async comm call function", "func");
		this.addVeriables("$S", "UserEvents", "event");
	}
	
	
	/**
	 * Sets variables for generating CTL formulas
	 * @param P Ajax request function
	 * @param S User events (nEXt)
	 */
	public void setTemplateVariables(String P, String S) {
		this.mP = P;
		this.mS = S;
	}
	
	@Override
	public String getCTLFormula() {
		if(this.mP == null || this.mS == null) {
			return "TRUE";
		}
		
		String template = this.mPropertyPattern.getCTLTemplate();
		String formula = template.replace("$P", this.mP).replace("$S", "EX " + this.mS);
		
		return formula;
	}
	
}
