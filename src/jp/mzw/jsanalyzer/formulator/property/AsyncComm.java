package jp.mzw.jsanalyzer.formulator.property;

import java.util.ArrayList;
import java.util.List;

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
	public List<String> getVariablesXML() {
		ArrayList<String> ret = new ArrayList<String>();

		String P = "<Variable id=\"$P\" semantic=\"async comm call function\" source=\"func\" />";
		String S = "<Variable id=\"$S\" semantic=\"user events\" source=\"event\" />";
		
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
		String formula = template.replace("$P", this.mP).replace("$S", "EX " + this.mS);
		
		return formula;
	}
	
}
