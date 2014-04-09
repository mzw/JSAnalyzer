package jp.mzw.jsanalyzer.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import jp.mzw.jsanalyzer.xml.XMLTag;
import jp.mzw.jsanalyzer.util.StringUtils;
import jp.mzw.jsanalyzer.util.TextFileUtils;
import jp.mzw.jsanalyzer.verifier.specification.Specification;

/**
 * Manages project information
 * @author yuta
 *
 */
public class Project {
	
	/**
	 * A project name for using temporal filenames
	 */
	protected String mProjectName;
	
	/**
	 * A URL of target application
	 */
	protected String mUrl;
	
	/**
	 * A list of rule filenames
	 */
	protected List<String> mRuleFilenames;
	
	/**
	 * A list of specifications based on Ajax design patterns
	 */
	protected List<Specification> mSpecList;
	
	/**
	 * A constructor via a XML file
	 * @param filename A XML file which has project information
	 */
	public Project(String filename) {
		String filetext = TextFileUtils.cat(filename);
		this.setDir(filename);
		
		Document doc = Jsoup.parse(filetext, filename, Parser.xmlParser());
		if(doc == null) {
			StringUtils.printError(this, "fail to read a project file");
			System.exit(-1);
		}
		
		Element proj = doc.getElementsByTag(XMLTag.Project).get(0);
		
		this.mProjectName = proj.getElementsByTag(XMLTag.ProjectName).get(0).text();
		this.mUrl = proj.getElementsByTag(XMLTag.ProjectUrl).get(0).text();
		
		Element rules = proj.getElementsByTag(XMLTag.ProjectRules).get(0);
		this.mRuleFilenames = new ArrayList<String>();
		for(Element rule : rules.getElementsByTag(XMLTag.ProjectRule)) {
			this.mRuleFilenames.add(rule.text());
		}
	}
	
	/**
	 * A constructor for example projects
	 * @param name A project name
	 * @param url A URL of target Ajax app
	 * @param filenames A list of filenames containing rule information
	 * @param dir A directory path where temporary files or stuffs are stored
	 */
	protected Project(String name, String url, List<String> filenames, String dir) {
		this.mProjectName = name;
		this.mUrl = url;
		this.mRuleFilenames = filenames;
		this.mDir = dir;
		
		this.mSpecList = new ArrayList<Specification>();
	}
	
	/**
	 * A method for getting a URL of target Ajax app
	 * @return A URL of target Ajax app
	 */
	public String getUrl() {
		return this.mUrl;
	}
	/**
	 * A method for getting a list of filenames containing rule information
	 * @return A list of filenames containing rule information
	 */
	public List<String> getRuleFilenames() {
		return this.mRuleFilenames;
	}

	/**
	 * A static method for setting default rule files for example projects
	 * @return A default list of filenames containing rule information
	 */
	protected static List<String> getDefaultRuleFilenames() {
		ArrayList<String> ret = new ArrayList<String>();

		ret.add("res/rules/trigger.xml");
		ret.add("res/rules/function.xml");
		ret.add("res/rules/control.xml");
		ret.add("res/rules/ruledJS.xml");
		
		return ret;
	}
	
	/**
	 * A directory path where the project file exists
	 */
	protected String mDir;
	
	/**
	 * Sets a directory path
	 * @param filename A project filename in XML
	 */
	private void setDir(String filename) {
		File file = new File(filename);
		File parent = new File(file.getParent());
		this.mDir = parent.getAbsolutePath();
	}
	
	/**
	 * Gets a directory path where this project file exists.
	 * JSAnalyzer stores some temporaries and outputs there.
	 * @return A directory path where a project file exists
	 */
	public String getDir() {
		return this.mDir;
	}
	
	/**
	 * Gets this project name
	 * @return Project name
	 */
	public String getName() {
		return this.mProjectName;
	}
	
	public String toXML4IADPInfoRep() {
		String ret = "";
		
		ret += "<Project name=\"" + this.mProjectName + "\" url=\"" + this.mUrl + "\" />\n";

		ret += "<IADPInfo>\n";
		ret += "<Properties>\n";
		for(Specification spec : this.mSpecList) {
			ret += "\t<Property id=\"" + spec.getId() + "\">\n";
			ret += "\t<Name>" + "</Name>\n";
			ret += "\t<NameAbbr>" + "</NameAbbr>\n";
			ret += "\t<Requirement>" + "</Requirement>\n";
			ret += "\t<AjaxDesignPattern>" + "</AjaxDesignPattern>\n";
			ret += "\t<AjaxDesignPatternCategory>" + "</AjaxDesignPatternCategory>\n";
			ret += "\t<PropertyPatternScope>" + "</PropertyPatternScope>\n";
			ret += "\t<PropertyPatternScope>" + "</PropertyPatternScope>\n";
			ret += "\t<CTLTemplate>" + "</CTLTemplate>\n";
			ret += "\t<Variables>\n";
//			for(String var : spec.getVariableList()) {
//				ret += "\t\t<Variable id=\"" + "var>";
//			}
			ret += "\t</Variables>\n";
			
			ret += "\t</Property>\n";
		}
		ret += "</Properties>\n";
		ret += "</IADPInfo>\n";
		
		return ret;
	}
}
