package Analyzer;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

public class Project {
	private Document xmlDoc = null;
	public Project(String filename) {
		String xmlStr = Util.cat(filename);
		this.xmlDoc = Jsoup.parse(xmlStr, filename, Parser.xmlParser());
		
		this.load();
		this.setDir(filename);
	}
	
	private String dir = null;
	private void setDir(String filename) {
		File file = new File(filename);
		File parent = new File(file.getParent());
		this.dir = parent.getAbsolutePath();
	}
	public String getDir() {
		return this.dir;
	}
	public String getName() {
		return this.projName;
	}
	
	private String projName = null;
	public void load() {
		if(this.xmlDoc == null) {
			System.err.println("Project file is not loaded...");
		}
		
		Element projElm = this.xmlDoc.select("Project").get(0);
		this.projName = projElm.attr("name");
		
		Elements settings = projElm.getElementsByTag("setting");
		for(Element setting : settings) {
			String setting_type = setting.attr("type");
			String setting_value = setting.attr("value");
			if(setting_type.equalsIgnoreCase("URL")) {
				this.setUrl(setting_value);
			} else if(setting_type.equalsIgnoreCase("Rule")) {
				this.setRuleFilename(setting_value);
			} else {
				System.err.println("Unknown setting@Project.load: " + setting_type + ", " + setting_value);
			}
		}
	}
	
	private String url = null;
	private void setUrl(String url) {
		this.url = url;
	}
	public String getUrl() {
		return this.url;
	}
	
	private List<String> ruleFilenames = null;
	private void setRuleFilename(String filename) {
		if(this.ruleFilenames == null) {
			this.ruleFilenames = new LinkedList<String>();
		}
		this.ruleFilenames.add(filename);
	}
	public List<String> getRuleFilenames() {
		return this.ruleFilenames;
	}
	
}
