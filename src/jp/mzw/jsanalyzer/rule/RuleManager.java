package jp.mzw.jsanalyzer.rule;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import jp.mzw.jsanalyzer.xml.XMLAttr;
import jp.mzw.jsanalyzer.xml.XMLTag;
import jp.mzw.jsanalyzer.util.StringUtils;
import jp.mzw.jsanalyzer.util.TextFileUtils;

public class RuleManager {
	
	protected ArrayList<Rule> mRuleList;
	protected ArrayList<Trigger> mTriggerRuleList;
	protected ArrayList<Potential> mPotentialRuleList;
	protected ArrayList<Control> mControlRuleList;
	protected ArrayList<Manipulate> mManipulateRuleList;
	protected ArrayList<Library> mLibraryRuleList;
	
	
	/*
	 * Constructor
	 * @param filenames filenames of rule files
	 */
	public RuleManager(ArrayList<String> filenames) {
		this.mRuleList = new ArrayList<Rule>();
		this.mTriggerRuleList = new ArrayList<Trigger>();
		this.mPotentialRuleList = new ArrayList<Potential>();
		this.mControlRuleList = new ArrayList<Control>();
		this.mManipulateRuleList = new ArrayList<Manipulate>();
		this.mLibraryRuleList = new ArrayList<Library>();
		for(String filename : filenames) {
			this.parseRuleFile(filename);
		}
	}
	
	/*
	 * Parses a XML file containing rule information
	 * @param filename A rule filename
	 */
	public void parseRuleFile(String filename) {
		String content = TextFileUtils.cat(filename);
		
		Document doc = Jsoup.parse(content, filename, Parser.xmlParser());
		if(doc == null) {
			StringUtils.printError(this, "fail to read a rule file");
			System.exit(-1);
		}
		
		for(Element elm : doc.getElementsByTag(XMLTag.RuleTrigger)) {
			String event = elm.attr(XMLAttr.RuleEvent);
			String interact = elm.attr(XMLAttr.RuleInteract);
			
			Trigger trigger = new Trigger(event, interact);
			this.mRuleList.add(trigger);
			this.mTriggerRuleList.add(trigger);
		}

		for(Element elm : doc.getElementsByTag(XMLTag.RulePotential)) {
			String func = elm.attr(XMLAttr.RuleFunc);
			String interact = elm.attr(XMLAttr.RuleInteract);
			String event = elm.attr(XMLAttr.RuleEvent);
			String callback = elm.attr(XMLAttr.RuleCallback);
			
			Potential potential = new Potential(func, interact, event, callback);
			this.mRuleList.add(potential);
			this.mPotentialRuleList.add(potential);
		}

		for(Element elm : doc.getElementsByTag(XMLTag.RuleControl)) {
			Control control = null;
			
			String lang = elm.attr(XMLAttr.RuleLang);
			if(XMLAttr.RuleLang_HTML.equals(lang)) {
				String attr = elm.attr(XMLAttr.RuleAttr);
				String value = elm.attr(XMLAttr.RuleDisabled);
				control = new HTMLControl(attr, value);
			} else if(XMLAttr.RuleLang_CSS.equals(lang)) {
				String prop = elm.attr(XMLAttr.RuleProp);
				String value = elm.attr(XMLAttr.RuleDisabled);
				control = new CSSControl(prop, value);
			} else if(XMLAttr.RuleLang_JS.equals(lang)) {
				// To be debugged
				String func = elm.attr(XMLAttr.RuleFunc);
				String value = elm.attr(XMLAttr.RuleDisabled);
				control = new JSControl(func, value);
			} else {
				StringUtils.printError(this, "Unknown lang at control rule", lang);
			}
			
			this.mRuleList.add(control);
			this.mControlRuleList.add(control);
		}
		
		for(Element elm : doc.getElementsByTag(XMLTag.RuleManipulate)) {
			String func = elm.attr(XMLAttr.RuleFunc);
			String semantic = elm.attr(XMLAttr.RuleSemantic);
			String by = elm.attr(XMLAttr.RuleBy);
			String value = elm.attr(XMLAttr.RuleValue);
			String target = elm.attr(XMLAttr.RuleTarget);
			
			Manipulate manipulate = new Manipulate(func, semantic, by, value, target);
			this.mRuleList.add(manipulate);
			this.mManipulateRuleList.add(manipulate);
		}
		
		for(Element elm : doc.getElementsByTag(XMLTag.RuleLibrary)) {
			String name = elm.attr(XMLAttr.RuleName);
			String method = elm.attr(XMLAttr.RuleMethod);
			String path = elm.attr(XMLAttr.RulePath);

			Library library = new Library(name, method, path);
			this.mRuleList.add(library);
			this.mLibraryRuleList.add(library);
		}
	}
	
	/**
	 * Determines whether given keyword is a trigger
	 * @param keyword A trigger candidate
	 * @return True or false represents that given keyword is a trigger or not
	 */
	public Trigger isTrigger(String keyword) {
		for(Trigger trigger : this.mTriggerRuleList) {
			if(trigger.match(keyword)) {
				return trigger;
			}
		}
		return null;
	}
	
	/**
	 * Determines whether given property is control CSS property or not
	 * @param prop Given CSS property
	 * @return If given CSS property is control one, returns corresponding control rule.
	 * Otherwise, returns null.
	 */
	public CSSControl isControlCSSProperty(String prop) {
		for(Control control : this.mControlRuleList) {
			if(control instanceof CSSControl && control.match(prop)) {
				return (CSSControl)control;
			}
		}
		return null;
	}
	
	public HTMLControl isHTMLControl(String attr) {
		for(Control control : this.mControlRuleList) {
			if(control instanceof HTMLControl && control.match(attr)) {
				return (HTMLControl)control;
			}
		}
		return null;
	}
	
	public Control isControl(String keyword) {
		for(Control control : this.mControlRuleList) {
			if(control.match(keyword)) {
				return control;
			}
		}
		return null;
	}
	
	public Potential isPotential(String keyword) {
		for(Potential potential : this.mPotentialRuleList) {
			if(potential.match(keyword)) {
				return potential;
			}
		}
		return null;
	}
	public Manipulate isManipulate(String keyword) {
		for(Manipulate manipulate : this.mManipulateRuleList) {
			if(manipulate.match(keyword)) {
				return manipulate;
			}
		}
		return null;
	}
	
	/**
	 * Gets Library instance corresponding to given filename
	 * @param filename A filename of a JavaScript library
	 * @return Library instance. If given filename is not ruled JavaScript library, returns null.
	 */
	public Library getLibraryByFilename(String filename) {
		for(Library lib : this.mLibraryRuleList) {
			if(lib.match(filename)) {
				return lib;
			}
		}
		
		return null;
	}
	
}
