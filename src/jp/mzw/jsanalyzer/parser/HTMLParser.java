package jp.mzw.jsanalyzer.parser;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

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
		
		return htmlParser;
	}
	
	/**
	 * Gets CSS codes
	 * @return CSS codes
	 */
	public ArrayList<CSSCode> getCSSCodeList() {
		ArrayList<CSSCode> codeList = new ArrayList<CSSCode>();
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
					codeList.add(new CSSCode(code, elm, false)); 
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
						codeList.add(new CSSCode(code, elm, hrefUrl)); 
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
					codeList.add(new CSSCode(code, elm, true)); 
				}
			}
		}
		return codeList;
	}
	
	/**
	 * 
	 * @param baseUrl is a HTML URL for getting corresponding URLs of external CSS files 
	 * @param ruleManager used for 
	 * @return JavaScript source code fragments
	 */
	public ArrayList<JSCode> getJSCodeList(String baseUrl, RuleManager ruleManager) {
		ArrayList<JSCode> codeList = new ArrayList<JSCode>();

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
							String srcUrl = StringUtils.getUrlByHref(baseUrl, attr_src);
							String filename = StringUtils.getFilename(attr_src);
							Library lib = ruleManager.getLibraryByFilename(filename);
							if (lib != null) {
								// Adds rules corresponding to this JavaScript library
								ruleManager.parseRuleFile(lib.getPath());
							} else {
								String code = TextFileUtils.wget(srcUrl);
								JSCode jsCode = new JSCode(code, elm, srcUrl);
								codeList.add(jsCode);
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
							JSCode jsCode = new JSCode(code, elm, false);
							codeList.add(jsCode);
						}
					}
				} else {
					StringUtils.printError(this, "Unknown type", attr_type);
				}
			}
			// In-line
			for (Attribute attr : elm.attributes()) {
				String key = attr.getKey();
				if(ruleManager.isTrigger(key)) {
					String code = attr.getValue();
					codeList.add(new JSCode(code, elm, true));
				}
			}
		}
		
		return codeList;
	}

}
