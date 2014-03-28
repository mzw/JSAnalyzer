package jp.mzw.jsanalyzer.rule;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import jp.mzw.jsanalyzer.xml.XMLAttr;
import jp.mzw.jsanalyzer.xml.XMLTag;
import jp.mzw.jsanalyzer.modeler.model.interaction.Event;
import jp.mzw.jsanalyzer.util.StringUtils;
import jp.mzw.jsanalyzer.util.TextFileUtils;

public class RuleManager {
	
	protected ArrayList<Rule> mRuleList;
	protected ArrayList<Trigger> mTriggerRuleList;
	protected ArrayList<Function> mFunctionRuleList;
	protected ArrayList<Control> mControlRuleList;
	protected ArrayList<Library> mLibraryRuleList;
	
	
	/*
	 * Constructor
	 * @param filenames filenames of rule files
	 */
	public RuleManager(List<String> filenames) {
		this.mRuleList = new ArrayList<Rule>();
		this.mTriggerRuleList = new ArrayList<Trigger>();
		this.mFunctionRuleList = new ArrayList<Function>();
		this.mControlRuleList = new ArrayList<Control>();
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
			
			boolean repeatable = true;
			String _repeatalbe = elm.attr(XMLAttr.RuleRepeatable);
			if(_repeatalbe != null && _repeatalbe.equals(XMLAttr.RuleRepeatable_False)) {
				repeatable = false;
			}
			
			Trigger trigger = new Trigger(event, interact, repeatable);
			this.mRuleList.add(trigger);
			this.mTriggerRuleList.add(trigger);
		}

		for(Element elm : doc.getElementsByTag(XMLTag.RuleFunction)) {
			String func = elm.attr(XMLAttr.RuleFunc);
			String interact = elm.attr(XMLAttr.RuleInteract);
			String target = elm.attr(XMLAttr.RuleTarget);
			String event = elm.attr(XMLAttr.RuleEvent);
			String event_modifier = elm.attr(XMLAttr.RuleEventModifier);
			String callback = elm.attr(XMLAttr.RuleCallback);

			boolean repeatable = true;
			String _repeatalbe = elm.attr(XMLAttr.RuleRepeatable);
			if(_repeatalbe != null && _repeatalbe.equals(XMLAttr.RuleRepeatable_False)) {
				repeatable = false;
			}
			
			Function function = new Function(func, interact, target, event, event_modifier, callback, repeatable);
			this.mRuleList.add(function);
			this.mFunctionRuleList.add(function);
		}

		for(Element elm : doc.getElementsByTag(XMLTag.RuleControl)) {
			Control control = null;

			String attr = elm.attr(XMLAttr.RuleAttr);
			String func = elm.attr(XMLAttr.RuleFunc);
			String prop = elm.attr(XMLAttr.RuleProp);
			
			if(attr != null) {
				String value = elm.attr(XMLAttr.RuleDisabled);
				control = new HTMLControl(attr, value);
			} else if(func != null) {
				String value = elm.attr(XMLAttr.RuleValue);
				String cond = elm.attr(XMLAttr.RuleCond);
				String disabled = elm.attr(XMLAttr.RuleDisabled);

				control = new JSControl(func, "PropTarget", prop, value, cond, "set", disabled);
			} else if (prop != null) {
				String value = elm.attr(XMLAttr.RuleDisabled);
				control = new CSSControl(prop, value);
			} else {
				StringUtils.printError(this, "Unknown lang at control rule", "attr, func, prop = " + attr + ", " + func + ", " + prop);
			}
			
			this.mRuleList.add(control);
			this.mControlRuleList.add(control);
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
	
	public boolean isUserInteraction(String event) {
		for(Trigger trigger : this.mTriggerRuleList) {
			if(trigger.match(event)) {
				return trigger.isUserInteract();
			}
		}
		return false;
	}
	public boolean isServerInteraction(String event) {
		for(Trigger trigger : this.mTriggerRuleList) {
			if(trigger.match(event)) {
				return trigger.isServerInteract();
			}
		}
		return false;
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
	
	public Function isFunction(String keyword) {
		for(Function potential : this.mFunctionRuleList) {
			if(potential.match(keyword)) {
				return potential;
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
