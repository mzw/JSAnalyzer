package jp.mzw.jsanalyzer.formulator.property;

import java.util.ArrayList;
import java.util.List;

import jp.mzw.jsanalyzer.formulator.adp.XMLHttpRequestCall;
import jp.mzw.jsanalyzer.formulator.pp.PropertyPattern;
import jp.mzw.jsanalyzer.formulator.pp.Response;

public class ACRetry extends Property {

	public ACRetry() {

		this.mPropertyName = "Asynchronous communication retry";
		this.mPropertyNameAbbreviation = "ACRetry";
		
		this.mRequirement = "Ajax apps can retry an asychronous communication when it fails";
		this.mAjaxDesignPattern = new XMLHttpRequestCall();
		
		this.mPropertyPatternScope = PropertyPattern.Scope.Globally;
		this.mPropertyPattern = new Response(this.mPropertyPatternScope);
	}
	

	/**
	 * Sets variables for generating CTL formulas
	 * @param P Failure event
	 * @param S Ajax request function
	 */
	public void setTemplateVariables(String P, String S) {
		this.mP = P;
		this.mS = S;
	}
	
	@Override
	public List<String> getVariablesXML() {
		ArrayList<String> ret = new ArrayList<String>();

		String P = "<Variable id=\"$P\" semantic=\"async comm failure event\" source=\"event\" />";
		String S = "<Variable id=\"$S\" semantic=\"async comm call function\" source=\"func\" />";
		
		ret.add(P);
		ret.add(S);
		
		return ret;
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
