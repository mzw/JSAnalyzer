package Verifier;

import Analyzer.Analyzer;
import Analyzer.Util;
import Graph.StateMachine;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.LinkedList;
import java.util.List;

public class AjaxDesignProperty extends Specification {
	
	protected String name = null;
	protected String dp = null; // derived Ajax design pattern name
	protected String pp = null; // mapped property pattern
	protected String template = null; // template formula of property pattern
	
	public AjaxDesignProperty(String name, String description, String dp, String pp, String template, String formula) {
		super(description, formula);
		this.name = name;
		this.dp = dp;
		this.pp = pp;
		this.template = template;
	}

	public static List<AjaxDesignProperty> parse(String filename, Analyzer analyzer, StateMachine sm) {
		List<AjaxDesignProperty> ret = new LinkedList<AjaxDesignProperty>();
		
		/// for any user events
		List<String> ues = new LinkedList<String>();
		for(String event : sm.getEvents()) {
			if(analyzer.getRuleList().isUserEvent(event)) {
				ues.add(event);
			}
		}

		String str = Util.cat(filename);
		Document doc = Jsoup.parse(str, filename, Parser.xmlParser());
		
		for(Element elm : doc.getElementsByTag("Application")) {
			if(elm.hasAttr("url")) {
				String candUrl = elm.attr("url");
				if(candUrl.equals(analyzer.getProject().getUrl())) {
					for(Element prop : elm.getElementsByTag("Property")) {
						
						int propId = Integer.parseInt(prop.attr("id"));
						String name = prop.getElementsByTag("Name").get(0).text();
						String description = prop.getElementsByTag("Description").get(0).text();
						String dp = prop.getElementsByTag("AjaxDesignPattern").get(0).attr("name");
						String pp = prop.getElementsByTag("PropertyPattern").get(0).attr("name");
						
						String template = prop.getElementsByTag("PropertyPattern").get(0).text();
						
						Elements argElms = prop.getElementsByTag("Argument");
						List<String> formulas = new LinkedList<String>();
						formulas.add(template);
						for(int i = 0; i < argElms.size(); i++) {
							Element argElm = argElms.get(i);
							int argOrder = Integer.parseInt(argElm.attr("order"));
							String argValue = argElm.text();

							List<String> _formulas = new LinkedList<String>();
							if(argValue.equals("EachUserEvent")) {
								for(String formula : formulas) {
									for(String ue : ues) {
										String _formula = formula.replaceAll("arg_" + argOrder, ue);
										_formulas.add(_formula);
									}
								}
							} else {
								for(String formula : formulas) {
									String _formula = formula.replaceAll("arg_" + argOrder, argValue);
									_formulas.add(_formula);
								}
							}
							formulas = _formulas;
						}
						
						// generated formulas
						for(String formula : formulas) {
							AjaxDesignProperty spec = new AjaxDesignProperty(name, description, dp, pp, template, formula);
							ret.add(spec);
						}
						
					}
				}
			}
		}
		
		return ret;
	}
	
	public String getHTML() {
		String ret = "";

		ret += "\t<tr><td>" + this.id + "</td>\n";

		ret += "\t<td>" + Util.esc_xml(this.name) + "</td>\n";
		ret += "\t<td>" + Util.esc_xml(this.description) + "</td>\n";
		ret += "\t<td>" + Util.esc_xml(this.dp) + "</td>\n";
		ret += "\t<td>" + Util.esc_xml(this.pp) + "</td>\n";
		//ret += "\t<td>" + Util.esc_xml(this.template) + "</td>\n";
		ret += "\t<td>" + Util.esc_xml(this.formula) + "</td>\n";
		ret += "\t<td " + (this.isEvaluated ? (this.isFault ? "style=\"background-color:#FA8072;\" onclick=\"alert('" + Util.esc_xml(this.anti_example) + "')\">fault" : "style=\"background-color:#7FFFD4;\">correct") : "") + "</td>\n";
		
		ret += "\t</tr>\n";
		
		return ret;
	}
}
