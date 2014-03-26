package jp.mzw.jsanalyzer.formulator.property;

import jp.mzw.jsanalyzer.formulator.adp.UserAction;
import jp.mzw.jsanalyzer.formulator.pp.PropertyPattern;
import jp.mzw.jsanalyzer.formulator.pp.Response;

public class AsynchronousCommunicationRetry extends Property {

	public AsynchronousCommunicationRetry() {

		this.mPropertyName = "Asynchronous communication retry";
		this.mPropertyNameAbbreviation = "ACRetry";
		
		this.mRequirement = "Ajax apps can handle user events during asynchronous communications";
		this.mAjaxDesignPattern = new UserAction();
		
		this.mPropertyPatternScope = PropertyPattern.Scope.Globally;
		this.mPropertyPattern = new Response(this.mPropertyPatternScope);
	}
	
	/**
	 * 
	 * @param P Failure event
	 * @param S Ajax request function
	 * @return
	 */
	public String genCTLFormula(String P, String S) {
		String template = this.mPropertyPattern.getCTLTemplate();
		
		String formula = template.replace("$P", P).replace("$S", S);
		
		return formula;
	}
}
