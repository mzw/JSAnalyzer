package jp.mzw.jsanalyzer.parser;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import jp.mzw.jsanalyzer.core.IdGen;
import jp.mzw.jsanalyzer.rule.Library;
import jp.mzw.jsanalyzer.rule.RuleManager;
import jp.mzw.jsanalyzer.util.StringUtils;
import jp.mzw.jsanalyzer.util.TextFileUtils;
import jp.mzw.jsanalyzer.xml.HTMLAttr;
import jp.mzw.jsanalyzer.xml.HTMLTag;

/**
 * Parses HTML code
 * @author Yuta Maezawa
 *
 */
public class HTMLParser extends Parser {

	/**
	 * Constructor
	 * @param code is HTML source code
	 */
	private HTMLParser(HTMLCode code) {
		super(code);
	}
	
	/**
	 * A base URL
	 */
	protected String mBaseUrl;
	
	/**
	 * Gets a base URL
	 * @return A base URL
	 */
	public String getBaseUrl() {
		return this.mBaseUrl;
	}
	
	/**
	 * A base directory
	 */
	protected String mBaseDir;
	
	/**
	 * Gets a base directory
	 * @return A base directory
	 */
	public String getBaseDir() {
		return this.mBaseDir;
	}
	
	/**
	 * Document representing DOM
	 */
	protected Document mDoc;
	
	/**
	 * Gets document
	 * @return Document instance
	 */
	public Document getDoc() {
		return this.mDoc;
	}
	
	/**
	 * Creates a HTML parser
	 * @param url Target HTML URL
	 * @param dir A directory where raw source codes are stored
	 * @return HTML parser instance
	 */
	public static HTMLParser createHTMLParser(String url, String dir) {
		// Gets raw source code via Internet
		String rawCode = TextFileUtils.wget(url);
		// Creates corresponding DOM using raw source code
		Document doc = Jsoup.parse(rawCode);
		// Creates HTML code
		HTMLCode htmlCode = new HTMLCode(rawCode, doc, url);
		// Create HTML parser
		HTMLParser htmlParser = new HTMLParser(htmlCode);
		// Sets base URL
		htmlParser.mBaseUrl = url;
		htmlParser.mBaseDir = dir;
		htmlParser.mDoc = doc;
		
		// Stores raw HTML code
		try {
			TextFileUtils.storeRawCode(url, dir, rawCode);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		htmlParser.setId();
		
		return htmlParser;
	}
	
	/**
	 * Represents a list of CSS codes in HTML code
	 */
	protected ArrayList<CSSCode> mCSSCodeList;
	
	/**
	 * Gets CSS codes in HTML code
	 * @return CSS codes
	 */
	public ArrayList<CSSCode> getCSSCodeList() {
		if(this.mCSSCodeList == null) {
			this.mCSSCodeList = new ArrayList<CSSCode>();
		} else {
			return this.mCSSCodeList;
		}
		
		for (Element elm : this.mCode.getHTMLElement().getAllElements()) {
			// Embedded
			if (HTMLTag.Style.compareToIgnoreCase(elm.tagName()) == 0) {
				for (Node childNode : elm.childNodes()) {
					String code = childNode.toString();
					/*
					 *  To be considered
					 *  eg, <![CDATA[...]]>
					 */
					//String code = Util.removeHtmlComment(childNode.toString());
					//String code = StringEscapeUtils.escapeHtml4(childNode.toString());
					this.mCSSCodeList.add(new CSSCode(code, elm, this.mBaseUrl, Code.Embedded)); 
				}
			}
			// External
			if (HTMLTag.Link.compareToIgnoreCase(elm.tagName()) == 0) {
				String attr_rel = elm.attr(HTMLAttr.Rel);
				String attr_type = elm.attr(HTMLAttr.Type);
				if (HTMLAttr.Rel_Stylesheet.equalsIgnoreCase(attr_rel) && 
						HTMLAttr.Type_CSS.equalsIgnoreCase(attr_type)) {
					try {
						String href = elm.attr(HTMLAttr.Href);
						String hrefUrl = StringUtils.getUrlByHref(this.mBaseUrl, href);
						String code = TextFileUtils.wget(hrefUrl);
						this.mCSSCodeList.add(new CSSCode(code, elm, hrefUrl, Code.External)); 
						// Stores source code
						try {
							TextFileUtils.storeRawCode(hrefUrl, this.getBaseDir(), code);
						} catch (URISyntaxException e) {
							e.printStackTrace();
						}
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
				}
			}
			// In-line
			for (Attribute attr : elm.attributes()) {
				if (HTMLAttr.Style.equals(attr.getKey())) {
					String code = attr.getValue();
					this.mCSSCodeList.add(new CSSCode(code, elm, this.mBaseUrl, Code.Inline)); 
				}
			}
		}
		
		// Sets CSSStyleRules to CSSCode
		for(CSSCode cssCode : this.mCSSCodeList) {
			CSSParser cssParser = new CSSParser(cssCode);
			cssParser.parse();
		}
		
		return this.mCSSCodeList;
	}
	
	/**
	 * Gets CSS code corresponding to given HTML element
	 * @param html Given HTML element
	 * @return CSS codes
	 */
	public CSSCode getCSSCodeByHTMLElement(Element html) {
		for(CSSCode cssCode : this.mCSSCodeList) {
			if(cssCode.getHTMLElement().equals(html)) {
				return cssCode;
			}
		}
		return null;
	}
	

	/**
	 * Represents a list of JavaScript codes in HTML code
	 */
	protected ArrayList<JSCode> mJSCodeList;
	
	/**
	 * Gets JavaScript codes in HTML code
	 * @param ruleManager Detects event attributes at HTML elements.
	 * Because values of the event attributes are JavaScript codes.
	 * @return JavaScript codes
	 */
	public ArrayList<JSCode> getJSCodeList(RuleManager ruleManager) {
		if(this.mJSCodeList == null) {
			this.mJSCodeList = new ArrayList<JSCode>();
		} else {
			return this.mJSCodeList;
		}

		for (Element elm : this.mCode.getHTMLElement().getAllElements()) {
			if (HTMLTag.Script.compareToIgnoreCase(elm.tagName()) == 0) {
				String attr_type = elm.attr(HTMLAttr.Type);
				if (HTMLAttr.Type_JavaScript.equals(attr_type)
						|| "".equals(attr_type) // to be considered
						|| false) {
					String attr_src = elm.attr(HTMLAttr.Src);
					// External
					if (!"".equals(attr_src)) {
						try {
							String srcUrl = StringUtils.getUrlByHref(this.mBaseUrl, attr_src);
							String filename = StringUtils.getFilename(attr_src);
							Library lib = ruleManager.getLibraryByFilename(filename);
							if (lib != null) {
								// Adds rules corresponding to this JavaScript library
								ruleManager.parseRuleFile(lib.getPath());
							} else {
								String code = TextFileUtils.wget(srcUrl);
								JSCode jsCode = new JSCode(code, elm, srcUrl, Code.External);
								this.mJSCodeList.add(jsCode);
								// Stores source code
								try {
									TextFileUtils.storeRawCode(srcUrl, this.getBaseDir(), code);
								} catch (URISyntaxException e) {
									e.printStackTrace();
								}
							}
						} catch (MalformedURLException e) {
							e.printStackTrace();
						}
					}
					// Embedded
					else {
						for (Node childNode : elm.childNodes()) {
							String code = childNode.toString();
							// to be considered
							//String code = Util.removeHtmlComment(childNode.toString());
							JSCode jsCode = new JSCode(code, elm, this.mBaseUrl, Code.Embedded);
							this.mJSCodeList.add(jsCode);
						}
					}
				} else {
					StringUtils.printError(this, "Unknown type", attr_type);
				}
			}
			// In-line
			for (Attribute attr : elm.attributes()) {
				String key = attr.getKey();
				if(ruleManager.isTrigger(key) != null) {
					String code = attr.getValue();
					JSCode jsCode = new JSCode(code, elm, this.mBaseUrl, Code.Inline);
					jsCode.setEventAttr(attr);
					this.mJSCodeList.add(jsCode);
				}
			}
		}
		
		return this.mJSCodeList;
	}
	

	/**
	 * Sets id values to elements whose id attributes are empty
	 * @deprecated
	 */
	private void setId() {
		for(Element elm : this.mDoc.getAllElements()) {
			if("".equals(elm.id())) {
				elm.attr("id", IdGen.genId());
			}
		}
	}
	
}
