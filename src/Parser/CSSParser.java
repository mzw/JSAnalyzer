package Parser;

import Analyzer.Util;
import Rule.Control;
import Rule.RuleList;
import com.steadystate.css.parser.CSSOMParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.ErrorHandler;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.*;
import org.w3c.dom.stylesheets.MediaList;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class CSSParser {
	
	private CSSCode cssCode = null;
	private CSSStyleSheet stylesheet = null;
	
	private List<CSSCode> imported = null;
	
	private RuleList myRules = null;
	
	private boolean isControl = false;
	private boolean disabled = false;
	
	public CSSParser(CSSCode cssCode) {
		this.cssCode = cssCode;
		this.stylesheet = CSSParser.createCSSSS(this.cssCode.toSource());

		this.imported = new LinkedList<CSSCode>();
		
		this.isControl = false;
		this.disabled = false;
	}
	
	public CSSCode getCssCode() {
		return this.cssCode;
	}
	
	public boolean isControl() {
		return this.isControl;
	}
	public boolean isDisabled() {
		return this.disabled;
	}
	
	public List<CSSCode> parse(Document doc, RuleList myRules) { // for embedded and external
		this.myRules = myRules;
		
		List<CSSCode> ret = new LinkedList<CSSCode>();
		
		CSSRuleList rules = this.stylesheet.getCssRules();
		for(int item = 0; item < rules.getLength(); item++) {
			CSSRule rule = rules.item(item);
			
			if(rule instanceof CSSCharsetRule) {
				// NOP
				// @charset "Shift_JIS";
				//CSSCharsetRule _rule = (CSSCharsetRule)rule;
				//System.err.println("CSSCharsetRule: " + _rule.getEncoding());
			} else if(rule instanceof CSSFontFaceRule) {
				// NOP
				//@font-face {
				//	font-family: myfont;
				//	src: url(http://www.yyy.zzz/myfont.woff) format("woff");
				//}
				//CSSFontFaceRule _rule = (CSSFontFaceRule)rule;
				//System.err.println("CSSFontFaceRule");
			} else if(rule instanceof CSSImportRule) {
				CSSImportRule _rule = (CSSImportRule)rule;
				String href = _rule.getHref();

				System.err.println("CSSImportRule: " + _rule.getHref());
				String _src = Util.wget(href);
				CSSCode _cssCode = new CSSCode(href, _src);
				this.imported.add(_cssCode);
				
				// avoid to recursively get imported css codes
				//ret.add(_cssCode);
			} else if(rule instanceof CSSMediaRule) {
				/// current limitation
				CSSMediaRule _rule = (CSSMediaRule)rule;
				CSSRuleList _rules = _rule.getCssRules();
				
				// to be debugged
				// all: all -> y
				// screen: computer -> y
				// tv: television -> n, target is "Web/Ajax applications"
				// handheld: portable device, not contain smartphone -> n
				// tty: -> n
				// print -> n
				// projection -> n as well as tv
				// braille: point system -> n
				// embossed: -> n as well as braille
				// speech -> n, not recomend in CSS2
				
				boolean isTarget = false;
				MediaList mediaList = _rule.getMedia();
				for(int _item = 0; _item < mediaList.getLength(); _item++) {
					String media = mediaList.item(_item);
					if("all".equals(media) || "screen".equals(media)) {
						isTarget = true;
					}
				}
				
				for(int _item = 0; _item < _rules.getLength(); _item++) {
					CSSRule __rule = _rules.item(_item);
					if(__rule instanceof CSSStyleRule) {
						
						CSSStyleRule ___rule = (CSSStyleRule)__rule;
						
						/// previous
						//CSSParser.causedByLimitation(___rule.getStyle(), myRules, "@media");
						
						if(isTarget) {
							String selector = ___rule.getSelectorText();
							String cssText = ___rule.getStyle().getCssText() + ";";
							
							if(CSSParser.containPseudoClass(selector)) {
								CSSParser.causedByLimitation(___rule.getStyle(), myRules, "pseudo class");
								continue;
							}
							
							Elements elms = doc.select(selector);
							for(Element elm : elms) {
								CSSCode cssCode = new CSSCode(this.cssCode.getUrl(), cssText);
								cssCode.setTarget(elm, null);
								ret.add(cssCode);
							}
						}
						
					} else {
						System.err.println("CSSMediaRule@media: " + __rule.getClass());
					}
				}
			} else if(rule instanceof CSSPageRule) {
				// NOP
				// When printing
				//CSSPageRule _rule = (CSSPageRule)rule;
				//CSSParser.causedByLimitation(_rule.getStyle(), myRules, "@page");
				//System.err.println("CSSPageRule: " + _rule.getClass());
			} else if(rule instanceof CSSStyleRule) {
				CSSStyleRule _rule = (CSSStyleRule)rule;
				
				String selector = _rule.getSelectorText();
				String cssText = _rule.getStyle().getCssText() + ";";
				
				if(CSSParser.containPseudoClass(selector)) {
					CSSParser.causedByLimitation(_rule.getStyle(), myRules, "pseudo class");
					continue;
				}
				
				Elements elms = doc.select(selector);
				for(Element elm : elms) {
					CSSCode cssCode = new CSSCode(this.cssCode.getUrl(), cssText);
					cssCode.setTarget(elm, null);
					ret.add(cssCode);
					
					CSSStyleDeclaration styles = _rule.getStyle();
					for(int _item = 0; _item < styles.getLength(); _item++) {
						String prop = styles.item(_item);
						String val = styles.getPropertyCSSValue(prop).getCssText();
						if(myRules.getControl(prop) != null) {
							this.isControl = true;
							
							Control control = myRules.getControl(prop);
							if(val.equals(control.getAttr())) {
								this.disabled = control.disabled(val);
							}
							
						}
					}
					
				}
			} else if(rule instanceof CSSUnknownRule) {
				CSSUnknownRule _rule = (CSSUnknownRule)rule;
				
				// hard coding
				List<String> unknowns = new LinkedList<String>();
				unknowns.add("@font-face");
				unknowns.add("@-webkit-keyframes");
				unknowns.add("@-moz-keyframes");
				unknowns.add("@-ms-keyframes");
				unknowns.add("@-o-keyframes");
				unknowns.add("@keyframes");
				
				boolean isUnknown = false;
				for(String unknown : unknowns) {
					if(_rule.toString().contains(unknown)) {
						// NOP
						// Why cannot catch CSSFontFaceRule???
						isUnknown = true;
						break;
					}
				}
				if(!isUnknown) {
					System.err.println("CSSUnknownRule: " + _rule.toString());
				}

				/*
				if(_rule.toString().contains("@font-face")) {
					// NOP
					// Why cannot catch CSSFontFaceRule??? 
				} else {
					System.err.println("CSSUnknownRule: " + _rule.toString());
				}
				*/
				
			}
		}
		return ret;
	}
	
	public List<CSSCode> getImportedCssCode() {
		return this.imported;
	}
	
	public CSSStyleSheet getStyleSheet() {
		return this.stylesheet;
	}
	public CSSRuleList getCssRules() {
		return this.stylesheet.getCssRules();
	}
	
	public String getUrl() {
		return this.cssCode.getUrl();
	}
	public String getSrc() {
		return this.cssCode.toSource();
	}
	
	private static CSSStyleSheet createCSSSS(String src) {
		CSSStyleSheet ret = null;
		StringReader reader = new StringReader(src);
		InputSource source = new InputSource(reader);
		CSSOMParser parser = new CSSOMParser();
		parser.setErrorHandler(new QuietCssErrorHandler());
		try {
			ret = parser.parseStyleSheet(source, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	private static class QuietCssErrorHandler implements ErrorHandler {
		@Override public void error(CSSParseException arg0) throws CSSException {}
		@Override public void fatalError(CSSParseException arg0) throws CSSException {}
		@Override public void warning(CSSParseException arg0) throws CSSException {}
	}
	
	/// Utilities
	public static List<String> parseSelector(String selector) {
		List<String> xpaths = new LinkedList<String>();
		
		// for grouping
		StringTokenizer tokenizer = new StringTokenizer(selector, ",");
		while(tokenizer.hasMoreTokens()) {
			String xpath = ".";
			String token = tokenizer.nextToken();
			// for parent-child
			StringTokenizer _tokenizer = new StringTokenizer(token, " ");
			while(_tokenizer.hasMoreTokens()) {
				String _token = _tokenizer.nextToken();
				
				int index_id = _token.indexOf("#");
				int index_class = _token.indexOf(".");
				if(index_id < 0 && index_class < 0) {
					String tagName = _token;
					xpath += "/descendant::" + tagName.toUpperCase();
				} else if(0 < index_id) {
					String tagName = _token.substring(0, index_id);
					String idName = _token.substring(index_id + 1);
					xpath += "/descendant::" + tagName.toUpperCase() + "[@id='" + idName + "']";
				} else if(0 < index_class) {
					String tagName = _token.substring(0, index_class);
					String className = _token.substring(index_class + 1);
					xpath += "/descendant::" + tagName.toUpperCase() + "[@class='" + className + "']";
				}
			}
			xpaths.add(xpath);
		}
		return xpaths;
	}
	public static boolean containPseudoClass(String selector) {
		// for grouping
		StringTokenizer tokenizer = new StringTokenizer(selector, ",");
		while(tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			// for parent-child
			StringTokenizer _tokenizer = new StringTokenizer(token, " ");
			while(_tokenizer.hasMoreTokens()) {
				String _token = _tokenizer.nextToken();
				
				// hard coding
				for(int i = 0; i < CSSParser.PseudoClasses.length; i++) {
					String pc = CSSParser.PseudoClasses[i];
					if(_token.contains(pc)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	// Limitation: avoid pseudo classes in CSS
	public static final String[] PseudoClasses = { // all cover
			/// css1
			":link",
			":visited",
			":active",
			/// css2
			":hover",
			":focus",
			":first-child",
			/// css3
			":last-child",
			":target",
			":enabled",
			":disabled",
			":checked",
			":indeterminate",
			":root",
			":nth-child", // nth-child(n)
			":nth-last-child", // (n)
			":nth-of-type", // (n)
			":nth-last-of-type", // (n)
			":first-of-type",
			":last-of-type",
			":only-child",
			":only-of-type",
			":empty",
			":contains", // contains()
			":not", // not()

			// nth-child
			":odd",
			":even",
	};
	public static void causedByLimitation(CSSStyleDeclaration styles, RuleList myRules, String limitType) {
		for(int _item = 0; _item < styles.getLength(); _item++) {
			String prop = styles.item(_item);
			String val = styles.getPropertyCSSValue(prop).getCssText();
			if(myRules.getControl(prop) != null) {
				/// for measuring time
				//System.err.println("Limitation@CSSParser.parse: " + limitType);
				//System.err.println("\tAffect: " + "prop=" + prop + ", val=" + val);
			}
		}
	}
}
