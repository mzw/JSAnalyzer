package jp.mzw.jsanalyzer.formulator.property;

import jp.mzw.jsanalyzer.formulator.adp.OnDemandJavaScript;
import jp.mzw.jsanalyzer.formulator.pp.PropertyPattern;
import jp.mzw.jsanalyzer.formulator.pp.Precedence;;

public class SRWait extends Property {
	
	public SRWait() {
		this.mPropertyName = "Wait for server response";
		this.mPropertyNameAbbreviation = "SRWait";
		
		this.mRequirement = "Ajax apps should reveive a server response before calling reposen-dependent functions";
		this.mAjaxDesignPattern = new OnDemandJavaScript();
		
		this.mPropertyPatternScope = PropertyPattern.Scope.Globally;
		this.mPropertyPattern = new Precedence(this.mPropertyPatternScope);

		this.addVeriables("$P", "Wait function", "func");
		this.addVeriables("$S", "Server response event", "event");
	}

	/**
	 * 
	 * @param P Function to wait for
	 * @param S Server response
	 * @return
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
