package Parser;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import Rule.RuleList;

import Analyzer.Util;

public class HTMLParser {
	private String url = null;
	private Document doc = null;
	
	private List<JSCode> jsCodes = null;
	private List<CSSCode> cssCodes = null;
	
	
	
	public HTMLParser() {
		this.jsCodes = new LinkedList<JSCode>();
		this.cssCodes = new LinkedList<CSSCode>();
	}
	public void parse(String url, RuleList rules) {
		this.url = url;
		String src = Util.wget(this.url);
		
		this.doc = Jsoup.parse(src);
		Elements elms = this.doc.getAllElements();
		
		for(Element elm : elms) {
            //System.out.println(elm.tagName());
            
            Attributes attrs = elm.attributes();
            
            // for embedded CSS
            if("style".compareToIgnoreCase(elm.tagName()) == 0) {
                    //String attr_type = attrs.get("type"); // type="text/css"
                    for(Node childNode : elm.childNodes()) {
                            String cssCode = Util.removeHtmlComment(childNode.toString());
                            this.cssCodes.add(new CSSCode(this.url, cssCode));
                    }
            }
            // for external CSS
            if("link".compareToIgnoreCase(elm.tagName()) == 0) {
                String attr_type = attrs.get("type");
                if("text/css".equals(attr_type)) {
                        String cssUrl = Util.getUrlByHref(url, attrs.get("href"));
                        String cssCode = Util.wget(cssUrl);
                        this.cssCodes.add(new CSSCode(cssUrl, cssCode));
                } else if("".equals(attr_type) ||
                                "application/rss+xml".equals(attr_type) ||
                                "application/rsd+xml".equals(attr_type) ||
                                "application/wlwmanifest+xml".equals(attr_type) ||
                                false ) {
                        // NOP
                	} else {
                		System.err.println("Unknown@HTMLParser: Link tag but type attr is " + attr_type);
		            }
		    }
		    
		    // for JS
		    if("script".compareToIgnoreCase(elm.tagName()) == 0) {
		            String attr_type = attrs.get("type");
		            if("text/javascript".equals(attr_type) ||
		                            "".equals(attr_type) ||
		                            false) {
		                    String attr_src = attrs.get("src");
		                    // external JS code
		                    if(!"".equals(attr_src)) {
		
		                    	String filename = Util.getFilename(attr_src);
		                    	boolean isRuledJsLib = rules.isRuledJsLib(filename);
		                    	if(!isRuledJsLib) {
			                        String jsUrl = Util.getUrlByHref(url, attr_src);
			                        String code = Util.wget(jsUrl);
			                        JSCode jsCode = new JSCode(jsUrl, code);
			                        this.jsCodes.add(jsCode);
		                    	}
		                    	
		                }
		                // for embedded JS code
		                else {
		                        System.out.println(attr_src);
		                        for(Node childNode : elm.childNodes()) {
		                                String code = Util.removeHtmlComment(childNode.toString());
		                                JSCode jsCode = new JSCode(this.url, code);
				                        this.jsCodes.add(jsCode);
		                        }
		                }
		        } else {
		        	System.err.println("Unknown@Main: Script tag but type attr is " + attr_type);
		        }
		    }
		    

		    // for in-line
		    for(Iterator<Attribute> it = attrs.iterator(); it.hasNext();) {
		    	Attribute attr = it.next();
		    	
			    // for in-line JS
		    	if(rules.isTrigger(attr.getKey())) {
		    		JSCode jsCode = new JSCode(this.url + " >>> Xpath(ToDo)", attr.getValue());
		    		jsCode.setTarget(elm, attr);
		    		this.jsCodes.add(jsCode);
		    	}

			    // for in-line CSS
		    	if("style".equals(attr.getKey())) {
		            String cssUrl = this.url + " >>> XPath(ToDo)";
                    CSSCode cssCode = new CSSCode(cssUrl, attr.getValue());
		    		cssCode.setTarget(elm, attr);
		    		this.cssCodes.add(cssCode);
		    	}
		    }
		}
	}
	
	public List<JSCode> getJSCodes() {
		return this.jsCodes;
	}
	public List<CSSCode> getCSSCodes() {
		return this.cssCodes;
	}
	
	public Document getDocument() {
		return this.doc;
	}
}
