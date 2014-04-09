package jp.mzw.jsanalyzer.formulator.property;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;

import jp.mzw.jsanalyzer.core.IdGen;
import jp.mzw.jsanalyzer.formulator.adp.AjaxDesignPattern;
import jp.mzw.jsanalyzer.formulator.adp.AjaxDesignPattern.Category;
import jp.mzw.jsanalyzer.formulator.pp.Existence;
import jp.mzw.jsanalyzer.formulator.pp.PropertyPattern;
import jp.mzw.jsanalyzer.serialize.model.FiniteStateMachine;
import jp.mzw.jsanalyzer.util.StringUtils;

public class Property implements Cloneable {

	protected String mPropertyName;
	protected String mPropertyNameAbbreviation;
	
	protected String mRequirement;
	protected AjaxDesignPattern mAjaxDesignPattern;
	
	protected PropertyPattern mPropertyPattern;
	protected int mPropertyPatternScope;

	private static int property_id_source = 0;
	private static final String property_id_prefix = "JSAnalyzerPropID_";
	
	private int mPropertyIdNum;
	protected String mPropertyId;
	
	protected Element mIADPInfo;
	
	/**
	 * Constructor for fundamental properties
	 */
	protected Property() {
		this.mPropertyIdNum = ++Property.property_id_source;
		this.mPropertyId = Property.property_id_prefix + Integer.toString(mPropertyIdNum);
	}
	
	public String getId() {
		return this.mPropertyId;
	}
	public int getIdNum() {
		return this.mPropertyIdNum;
	}
	public boolean equals(Property prop) {
		if(this.mPropertyId.equals(prop.getId())) {
			return true;
		}
		return false;
	}
	
	public String getNameAbbr() {
		return this.mPropertyNameAbbreviation;
	}
	
	public String getRequirement() {
		return this.mRequirement;
	}
	
	public AjaxDesignPattern getAjaxDesignPattern() {
		return this.mAjaxDesignPattern;
	}
	public PropertyPattern getPropertyPattern() {
		return this.mPropertyPattern;
	}
	
	public void setIADPInfo(Element iadp) {
		this.mIADPInfo = iadp;
	}
	public Element getIADPInfo() {
		return this.mIADPInfo;
	}
	
	@Override
	public Property clone() {
		try {
			return (Property) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
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

		this.mPropertyIdNum = ++Property.property_id_source;
		this.mPropertyId = Property.property_id_prefix + Integer.toString(mPropertyIdNum);
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
	
	/**
	 * To be implemented
	 * @param P1
	 * @param P2
	 * @param fsm
	 * @deprecated
	 */
	public String getTemplateVariableP(String P1, String P2, FiniteStateMachine fsm) {
		return P1 + " | " + P2;
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

	/**
	 * Fundamemtal properties
	 */
	public static Property[] mFundamentalPropertyList = {
		new AsyncComm(),
		new ACRetry(),
		new SRWait(),
		new UEHRegist(),
		new UEHSingle(),
	};
	
	/**
	 * Constains original properties
	 */
	public static List<Property> mOriginalPropertyList = new ArrayList<Property>();

	/**
	 * To add your original properties in the following three steps:
	 * 1: Instantiates Property class
	 * 2: Sets variables in XML
	 * 3: Adds property list
	 * Note that fundamental properties have been already registered
	 */
	public static void setOriginalPropertyList() {
		/// 1. Instantiate
		Property pUESubmit = new Property(
				"User event and submit",
				"UESubmit",
				"Ajax apps should require explicit user operations before form data is submitted",
				new AjaxDesignPattern(AjaxDesignPattern.Category.Programming, "Explicit Submission"),
				new Existence(PropertyPattern.Scope.Before),
				PropertyPattern.Scope.Before);
		/// 2. Set variables in XML
		ArrayList<String> varsXML = new ArrayList<String>();
		String P = "<Variable id=\"$P\" semantic=\"User event\" source=\"event\" />";
		String S = "<Variable id=\"$R\" semantic=\"Submit function\" source=\"func\" />";
		varsXML.add(P);
		varsXML.add(S);
		pUESubmit.setVeriablesXML(varsXML);
		/// 3: Adds property list
		Property.mOriginalPropertyList.add(pUESubmit);

		/**
		 * Here you can add your original properties
		 */
		
	}
	
	public static Property getProperty(String id) {
		for(Property prop : Property.mFundamentalPropertyList) {
			if(prop.getId().equals(id)) {
				return prop;
			}
		}
		for(Property prop : Property.mOriginalPropertyList) {
			if(prop.getId().equals(id)) {
				return prop;
			}
		}
		return null;
	}
	
	/**
	 * Gets property list in XML
	 * @return Property list in XML
	 */
	public static String getPropertyListXML() {
		String ret = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
		
		ret += "<Properties>\n";
		/// Fundamentals
		for(Property prop : Property.mFundamentalPropertyList) {
			ret += prop.toXML();
		}
		/// Originals
		for(Property prop : Property.mOriginalPropertyList) {
			ret += prop.toXML();
		}
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
	
	public String toXML() {
		String ret = "";
		ret += "<Property id=\"" + this.mPropertyId + "\">\n";
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
	
}
