package jp.mzw.jsanalyzer.formulator.property;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;

import jp.mzw.jsanalyzer.formulator.adp.*;
import jp.mzw.jsanalyzer.formulator.pp.*;
import jp.mzw.jsanalyzer.serialize.model.FiniteStateMachine;
import jp.mzw.jsanalyzer.serialize.model.State;
import jp.mzw.jsanalyzer.serialize.model.Transition;
import jp.mzw.jsanalyzer.util.StringUtils;
import jp.mzw.jsanalyzer.verifier.modelchecker.NuSMV;

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
		init();
	}
	
	private void init() {
		this.mPropertyIdNum = ++Property.property_id_source;
		this.mPropertyId = Property.property_id_prefix + Integer.toString(mPropertyIdNum);
		
		this.mVarsXML = new ArrayList<String>();
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
			PropertyPattern pp) {
		this.mPropertyName = propName;
		this.mPropertyNameAbbreviation = propNameAbbr;
		this.mRequirement = requirement;
		this.mAjaxDesignPattern = adp;
		this.mPropertyPattern = pp;
		this.mPropertyPatternScope = pp.getScope();

		init();
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
				new Existence(PropertyPattern.Scope.Before)
				);
		/// 2. Set variables in XML
		pUESubmit.addVeriables("$P", "UserEvents", "event");
		pUESubmit.addVeriables("$R", "Submit function", "func");
		/// 3: Adds property list
		Property.mOriginalPropertyList.add(pUESubmit);

		/**
		 * Here you can add your original properties
		 */
		/// Form data validation
		Property pFSValid = new Property (
				"Form data validation",
				"FDValid",
				"Ajax apps should validate form data before submission",
				new AjaxDesignPattern(AjaxDesignPattern.Category.FunctionAndUsability, "Live Form"),
				new Precedence(PropertyPattern.Scope.Globally)
				);
		pFSValid.addVeriables("$P", "UserEvents", "event");
		pFSValid.addVeriables("$S", "Submit function", "func");
		Property.mOriginalPropertyList.add(pFSValid);
		/// Seed retrieval
		Property pSeedRetreival = new Property (
				"Seed retrieval",
				"SeedRetrieval",
				"Ajax apps should retrieve seed data before login attempt",
				new AjaxDesignPattern(AjaxDesignPattern.Category.FunctionAndUsability, "Direct Login"),
				new Precedence(PropertyPattern.Scope.Globally)
				);
		pSeedRetreival.addVeriables("$P", "Login function", "func");
		pSeedRetreival.addVeriables("$S", "Retrieve function", "func");
		Property.mOriginalPropertyList.add(pSeedRetreival);
		/// Login form disable
		Property pLFDisable = new Property (
				"Login form disable",
				"LFDisable",
				"Ajax apps should disable login form after successful login",
				new AjaxDesignPattern(AjaxDesignPattern.Category.FunctionAndUsability, "Direct Login"),
				new Absence(PropertyPattern.Scope.Globally)
				);
		pLFDisable.addVeriables("$P1", "Successful login callback function", "func");
		pLFDisable.addVeriables("$P2", "Login attempt event", "event");
		Property.mOriginalPropertyList.add(pLFDisable);
		
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

	public static Property getPropertyByNameAbbr(String abbr) {
		for(Property prop : Property.mFundamentalPropertyList) {
			if(prop.getNameAbbr().equals(abbr)) {
				return prop;
			}
		}
		for(Property prop : Property.mOriginalPropertyList) {
			if(prop.getNameAbbr().equals(abbr)) {
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
	public void addVeriables(String var, String semantic, String src) {
		String xml = "<Variable id=\"" + var + "\" semantic=\"" + semantic + "\" source=\"" + src + "\" />";
		if(this.mVarsXML == null) {
			this.mVarsXML = new ArrayList<String>();
		}
		this.mVarsXML.add(xml);
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
		for(String var : this.mVarsXML) {
			ret += "\t\t" + var + "\n";
		}
		ret += "\t" + "</Variables>\n";
		ret += "</Property>\n";
		return ret;
	}


	/**
	 * 
	 * @param P1
	 * @param P2
	 * @param fsm
	 * @deprecated
	 */
	public static String genTemplateVariableP(String P1, String P2, FiniteStateMachine fsm) {
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
		
		String p = P1 + " & ";
		if(isBranch) {
			p += "EX EX " + P2;
		} else {
			p += "EX " + P2;
		}
		return "(" + p + ")";
	}
	
}