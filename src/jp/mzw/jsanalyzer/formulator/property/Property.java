package jp.mzw.jsanalyzer.formulator.property;

import jp.mzw.jsanalyzer.formulator.adp.AjaxDesignPattern;
import jp.mzw.jsanalyzer.formulator.pp.PropertyPattern;

public abstract class Property {

	protected String mPropertyName;
	protected String mPropertyNameAbbreviation;
	
	protected String mRequirement;
	protected AjaxDesignPattern mAjaxDesignPattern;
	
	protected PropertyPattern mPropertyPattern;
	protected int mPropertyPatternScope;
	
	/**
	 * To be implemented
	 * @param filename Contains properties to be verified
	 * @deprecated
	 */
	public static void read(String filename) {
		// NOP
	}
	
}
