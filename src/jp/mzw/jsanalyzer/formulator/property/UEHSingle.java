package jp.mzw.jsanalyzer.formulator.property;

import java.util.ArrayList;
import java.util.List;

import jp.mzw.jsanalyzer.core.IdGen;
import jp.mzw.jsanalyzer.formulator.adp.UserAction;
import jp.mzw.jsanalyzer.formulator.pp.Absence;
import jp.mzw.jsanalyzer.formulator.pp.PropertyPattern;
import jp.mzw.jsanalyzer.serialize.model.FiniteStateMachine;
import jp.mzw.jsanalyzer.serialize.model.State;
import jp.mzw.jsanalyzer.serialize.model.Transition;
import jp.mzw.jsanalyzer.verifier.modelchecker.NuSMV;

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
		String p = P1 + " & ";
		if(isBranch) {
			p += "EX EX " + P2;
		} else {
			p += "EX " + P2;
		}
		this.mP = "(" + p + ")";
	}
	
	
	@Override
	/**
	 * 
	 * @param P1
	 * @param P2
	 * @param fsm
	 * @deprecated
	 */
	public String getTemplateVariableP(String P1, String P2, FiniteStateMachine fsm) {
		/// Determines whether the state representing P1 is a branch state or not
		boolean isBranch = true;
		
		String stateIdByP1 = NuSMV.rev_genOr(P1);
		for(State state : fsm.getStateList()) {
			if(state.getId().equals(stateIdByP1)) {
				
				for(Transition trans : fsm.getTransList()) {
					if(trans.getFromStateId().equals(state.getId())) {
						/// If the state has event transitions, NOT branch state
						if(trans.getEvent() != null) {
							isBranch = false;
						}
					}
				}
				
				break;
			}
		}
		
		this.setTemplateVariables(P1, P2, isBranch);

		return this.mP;
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
