package jp.mzw.jsanalyzer.formulator.property;

import java.util.ArrayList;
import java.util.List;

import jp.mzw.jsanalyzer.formulator.adp.UserAction;
import jp.mzw.jsanalyzer.formulator.pp.Absence;
import jp.mzw.jsanalyzer.formulator.pp.PropertyPattern;

public class UEHSingle extends Property {
	
	public UEHSingle() {
		this.mPropertyName = "User event handler singleton";
		this.mPropertyNameAbbreviation = "UEHSingle";
		
		this.mRequirement = "Ajax apps can prevent multiple calls of specific functions";
		this.mAjaxDesignPattern = new UserAction();
		
		this.mPropertyPatternScope = PropertyPattern.Scope.Globally;
		this.mPropertyPattern = new Absence(this.mPropertyPatternScope);
	}
	

	/**
	 * Sets variables for generating CTL formulas
	 * @param P1 Prevent function
	 * @param P2 User event
	 * @param isBranch Represents whether the prevent function at P1 is branch node or not
	 */
	public void setTemplateVariables(String P1, String P2, boolean isBranch) {
		this.mP = P1 + " & ";
		if(isBranch) {
			this.mP += "EX EX " + P2;
		} else {
			this.mP += "EX " + P2;
		}
	}
	
	@Override
	public List<String> getVariablesXML() {
		ArrayList<String> ret = new ArrayList<String>();

		String P1 = "<Variable id=\"$P1\" semantic=\"prevent function\" source=\"func\" />";
		String P2 = "<Variable id=\"$P2\" semantic=\"user events\" source=\"event\" />";
		
		ret.add(P1);
		ret.add(P2);
		
		return ret;
	}

	@Override
	public String getCTLFormula() {
		if(this.mP == null) {
			return "TRUE";
		}
		
		String template = this.mPropertyPattern.getCTLTemplate();
		String formula = template.replace("$P", this.mP);
		
		return formula;
	}

}
