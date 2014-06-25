package jp.mzw.jsanalyzer.parser;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import jp.mzw.jsanalyzer.core.LimitationManager;
import jp.mzw.jsanalyzer.core.LimitationManager.Limitation;
import jp.mzw.jsanalyzer.util.StringUtils;
import jp.mzw.jsanalyzer.util.TextFileUtils;

import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.ErrorHandler;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSCharsetRule;
import org.w3c.dom.css.CSSFontFaceRule;
import org.w3c.dom.css.CSSImportRule;
import org.w3c.dom.css.CSSMediaRule;
import org.w3c.dom.css.CSSPageRule;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.css.CSSUnknownRule;
import org.w3c.dom.stylesheets.MediaList;

import com.steadystate.css.parser.CSSOMParser;

/**
 * Parses CSS source code
 * @author Yuta Maezawa
 *
 */
public class CSSParser extends Parser {
	/**
	 * Constructor
	 * @param code CSS code
	 */
	public CSSParser(CSSCode code) {
		super(code);
		this.mStyleSheet = createStyleSheet();
	}
	
	/**
	 * Style sheet in CSS code
	 */
	protected CSSStyleSheet mStyleSheet;
	
	/**
	 * Creates a style sheet by parsing CSS code
	 */
	protected CSSStyleSheet createStyleSheet() {
		StringReader reader = new StringReader(this.mCode.getCode());
		InputSource source = new InputSource(reader);
		CSSOMParser parser = new CSSOMParser();
		parser.setErrorHandler(new QuietCssErrorHandler());
		// Should not occur any IOExceptions because this code has already retrieved
		try {
			return parser.parseStyleSheet(source, null, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Gets this CSS style sheet
	 * @return This CSS style sheet
	 */
	public CSSStyleSheet getCSSStyleSheet() {
		return this.mStyleSheet;
	}
	
	/**
	 * Parses this CSS code
	 */
	public void parse() {
		CSSRuleList cssRuleList = this.mStyleSheet.getCssRules();
		this.parse(cssRuleList);
	}
	
	/**
	 * Parses CSS rules
	 * @param cssRuleList A list containing CSS rules 
	 */
	private void parse(CSSRuleList cssRuleList) {
		for(int i = 0; i < cssRuleList.getLength(); i++) {
			CSSRule cssRule = cssRuleList.item(i);
			
			if(cssRule instanceof CSSCharsetRule) {
				// e.g., @charset "Shift_JIS";
				// NOP
			} else if(cssRule instanceof CSSFontFaceRule) {
				// e.g., @font-face { font-family: myfont; src: url(http://example.com/myfont.woff) format("woff"); }
				// NOP
			} else if(cssRule instanceof CSSImportRule) {
				CSSImportRule cssImportRule = (CSSImportRule)cssRule;
				String href = cssImportRule.getHref();
				// To be implemented
				StringUtils.printError(this, "Should implement for CSS import rule", href);
			} else if(cssRule instanceof CSSMediaRule) {
				this.parseMediaRules((CSSMediaRule)cssRule);
			} else if(cssRule instanceof CSSPageRule) {
				CSSPageRule cssPageRule = (CSSPageRule)cssRule;
				// To be implemented
				StringUtils.printError(this, "Should implement for CSS page rule", cssPageRule.toString());
			} else if(cssRule instanceof CSSStyleRule) {
				CSSStyleRule cssStyleRule = (CSSStyleRule)cssRule;
				((CSSCode)this.mCode).addCSSStyleRule(cssStyleRule);
			} else if(cssRule instanceof CSSUnknownRule) {
//				CSSUnknownRule cssUnknownRule = (CSSUnknownRule)cssRule;
				CSSParser.skipCSSRule(cssRule);
			} else {
				StringUtils.printError(this, "Unknown CSS rule class", cssRule.getClass().toString());
			}
			
		}
	}

	/**
	 * CSS pseudo classes
	 */
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

		":valid",
		":invalid",
		":after",
		":before"
	};
	
	/**
	 * Parses CSS rules which has media at-rule (to be modified)
	 * @param cssMediaRule A CSS media rule
	 * @deprecated
	 */
	public void parseMediaRules(CSSMediaRule cssMediaRule) {
		
//		| To be implemented | Media types |
//		| --- | --- |
//		| yes | all and screen |
//		| no  | tv, handheld, tty, print, projection, braille, embossed, and speech |
		
		MediaList mediaList = cssMediaRule.getMedia();
		for(int j = 0; j < mediaList.getLength(); j++) {
			String media = mediaList.item(j);
			if("all".equals(media) || "screen".equals(media)) {
				this.parse(cssMediaRule.getCssRules());
				break;
			}
		}
		
	}
	
	/**
	 * Determins whether given selector contains CSS pseudo classes.
	 * Because the classes correspond to one of the static analysis limitations.
	 * @param selector Given selector string
	 * @return True or false represents given selector contains the classes or not
	 * @deprecated
	 */
	public static boolean containsCSSPseudoClass(String selector) {
		for(String pc : CSSParser.PseudoClasses) {
			if(selector.contains(pc)) {
				Limitation limitation = new Limitation(selector, Limitation.CSS_Pseudo_Class, "@CSSParser#containsCSSPseudoClass");
				LimitationManager.addLimitation(limitation);
//				StringUtils.printError(CSSParser.class, "[Limitation] Contains CSS pseudo classes", selector);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Determines whether given CSS unknown rule can be skipped 
	 * @param cssRule
	 * @deprecated
	 */
	public static void skipCSSRule(CSSRule cssRule) {
		
		// hard coding
		ArrayList<String> skips = new ArrayList<String>();
		skips.add("@font-face");
		skips.add("@-webkit-keyframes");
		skips.add("@-moz-keyframes");
		skips.add("@-ms-keyframes");
		skips.add("@-o-keyframes");
		skips.add("@keyframes");
		
		for(String skip : skips) {
			if(cssRule.toString().contains(skip)) {
//				StringUtils.printError(CSSParser.class, "Unknown CSS rule", cssRule.toString());
				return;
			}
		}
		
		StringUtils.printError(CSSParser.class, "Really unknown CSS rule", cssRule.toString());
	}
	
	/**
	 * CSS Utility Class
	 * Do not handle any errors
	 */
	private static class QuietCssErrorHandler implements ErrorHandler {
		@Override public void error(CSSParseException arg0) throws CSSException {}
		@Override public void fatalError(CSSParseException arg0) throws CSSException {}
		@Override public void warning(CSSParseException arg0) throws CSSException {}
	}
}