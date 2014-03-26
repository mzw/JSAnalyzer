package jp.mzw.jsanalyzer.formulator.property;

import jp.mzw.jsanalyzer.formulator.adp.OnDemandJavaScript;
import jp.mzw.jsanalyzer.formulator.pp.PropertyPattern;
import jp.mzw.jsanalyzer.formulator.pp.Precedence;;

public class SRWait extends Property {
	
	public SRWait() {
		this.mPropertyName = "Asynchronous communication";
		this.mPropertyNameAbbreviation = "AsyncComm";
		
		this.mRequirement = "Ajax apps can handle user events during asynchronous communications";
		this.mAjaxDesignPattern = new OnDemandJavaScript();
		
		this.mPropertyPatternScope = PropertyPattern.Scope.Globally;
		this.mPropertyPattern = new Precedence(this.mPropertyPatternScope);
	}

	/**
	 * 
	 * @param P Function to wait for
	 * @param S Server response
	 * @return
	 */
	public String genCTLFormula(String P, String S) {
		String template = this.mPropertyPattern.getCTLTemplate();
		
		String formula = template.replace("$P", P).replace("$S", "EX " + S);
		
		return formula;
	}
}
