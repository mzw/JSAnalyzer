package jp.mzw.jsanalyzer.formulator.property;

import jp.mzw.jsanalyzer.formulator.adp.UserAction;
import jp.mzw.jsanalyzer.formulator.pp.Precedence;
import jp.mzw.jsanalyzer.formulator.pp.PropertyPattern.Scope;

public class UserEventHandlerRegistration extends Property {
	
	public UserEventHandlerRegistration() {
		this.mPropertyName = "User event handler registration";
		this.mPropertyNameAbbreviation = "UEHRegist";
		
		this.mRequirement = "Ajax apps should register user event handlers at page load";
		this.mAjaxDesignPattern = new UserAction();
		
		this.mPropertyPatternScope = Scope.Globally;
		this.mPropertyPattern = new Precedence(this.mPropertyPatternScope);
	}
}
