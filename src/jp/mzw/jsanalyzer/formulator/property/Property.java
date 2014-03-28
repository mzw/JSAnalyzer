package jp.mzw.jsanalyzer.formulator.property;

import java.util.ArrayList;
import java.util.List;

import jp.mzw.jsanalyzer.formulator.adp.AjaxDesignPattern;
import jp.mzw.jsanalyzer.formulator.adp.AjaxDesignPattern.Category;
import jp.mzw.jsanalyzer.formulator.pp.PropertyPattern;
import jp.mzw.jsanalyzer.util.StringUtils;

public class Property {

	protected String mPropertyName;
	protected String mPropertyNameAbbreviation;
	
	protected String mRequirement;
	protected AjaxDesignPattern mAjaxDesignPattern;
	
	protected PropertyPattern mPropertyPattern;
	protected int mPropertyPatternScope;
	
	/**
	 * Constructor for fundamental properties
	 */
	protected Property() {
		// NOP
	}
	
	public String getNameAbbr() {
		return this.mPropertyNameAbbreviation;
	}
	
	/**
	 * Constructor for original properties
	 * @param propName
	 * @param propNameAbbr
	 * @param requirement
	 * @param adp
	 * @param pp
	 * @param ppScope
	 */
	public Property(String propName, String propNameAbbr,
			String requirement, AjaxDesignPattern adp,
			PropertyPattern pp, int ppScope) {
		this.mPropertyName = propName;
		this.mPropertyNameAbbreviation = propNameAbbr;
		this.mRequirement = requirement;
		this.mAjaxDesignPattern = adp;
		this.mPropertyPattern = pp;
		this.mPropertyPatternScope = ppScope;
		
	}

	protected String mP;
	protected String mS;
	protected String mQ;
	protected String mR;
	
	public void setTemplateVariables(String P, String S, String Q, String R) {
		this.mP = P;
		this.mS = S;
		this.mQ = Q;
		this.mR = R;
	}
	
	public String getCTLFormula() {
		String template = this.mPropertyPattern.getCTLTemplate();
		
		String formula = template;
		if(this.mP != null) {
			formula = formula.replace("$P", this.mP);
		}
		if(this.mS != null) {
			formula = formula.replace("$S", this.mS);
		}
		if(this.mQ != null) {
			formula = formula.replace("$Q", this.mQ);
		}
		if(this.mR != null) {
			formula = formula.replace("$R", this.mR);
		}
		
		return formula;
	}
	
	public static String getPropertyListXML() {
		String ret = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
		
		int id = 0;
		ret += "<Properties>\n";
		ret += (new AsyncComm()).toXML(id++);
		ret += (new ACRetry()).toXML(id++);
		ret += (new SRWait()).toXML(id++);
		ret += (new UEHRegist()).toXML(id++);
		ret += (new UEHSingle()).toXML(id++);
//		ret += (new AsyncComm()).toXML(id++);
//		ret += (new AsyncComm()).toXML(id++);
		ret += "</Properties>\n";
		
		return ret;
	}
	

	private List<String> mVarsXML;
	public List<String> getVariablesXML() {
		return mVarsXML;
	}
	public void setVeriablesXML(List<String> varsXML) {
		this.mVarsXML = varsXML;
	}
	
	public String toXML(int id) {
		String ret = "";
		ret += "<Property id=\"" + id + "\">\n";
		ret += "\t" + "<Name>" + this.mPropertyName + "</Name>\n";
		ret += "\t" + "<NameAbbr>" + this.mPropertyNameAbbreviation + "</NameAbbr>\n";
		ret += "\t" + "<Requirement>" + this.mRequirement + "</Requirement>\n";
		ret += "\t" + "<AjaxDesignPattern>" + this.mAjaxDesignPattern.getName() + "</AjaxDesignPattern>\n";
		ret += "\t" + "<AjaxDesignPatternCategory>" + AjaxDesignPattern.Category.getCategoryName(this.mAjaxDesignPattern.getCategoryId()) + "</AjaxDesignPatternCategory>\n";
		ret += "\t" + "<PropertyPattern>" + this.mPropertyPattern.getClass().getSimpleName() + "</PropertyPattern>\n";
		ret += "\t" + "<PropertyPatternScope>" + PropertyPattern.Scope.getScopeName(this.mPropertyPatternScope) + "</PropertyPatternScope>\n";
		ret += "\t" + "<CTLTemplate>" + StringUtils.esc_xml(this.mPropertyPattern.getCTLTemplate()) + "</CTLTemplate>\n";
		ret += "\t" + "<Variables>\n";
		for(String var : this.getVariablesXML()) {
			ret += "\t\t" + var + "\n";
		}
		ret += "\t" + "</Variables>\n";
		ret += "</Property>\n";
		return ret;
	}
	
	/**
	 * To be implemented
	 * @param filename Contains properties to be verified
	 * @deprecated
	 */
	public static void read(String filename) {
		// NOP
	}
	
}
