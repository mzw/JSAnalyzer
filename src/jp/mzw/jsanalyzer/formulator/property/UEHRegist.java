package jp.mzw.jsanalyzer.formulator.property;

import jp.mzw.jsanalyzer.formulator.adp.UserAction;
import jp.mzw.jsanalyzer.formulator.pp.Precedence;
import jp.mzw.jsanalyzer.formulator.pp.PropertyPattern;

public class UEHRegist extends Property {
	
	public UEHRegist() {
		this.mPropertyName = "User event handler registration";
		this.mPropertyNameAbbreviation = "UEHRegist";
		
		this.mRequirement = "Ajax apps should register user event handlers at page load";
		this.mAjaxDesignPattern = new UserAction();
		
		this.mPropertyPatternScope = PropertyPattern.Scope.Globally;
		this.mPropertyPattern = new Precedence(this.mPropertyPatternScope);

		this.addVeriables("$P", "UserEvents", "event");
		this.addVeriables("$S", "Page load event", "event");
	}
	

	/**
	 * Sets variables for generating CTL formulas
	 * @param P User events
	 * @param S Page load event
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
		String formula = template.replace("$P", this.mP).replace("$S", this.mS);
		
		return formula;
	}

}
