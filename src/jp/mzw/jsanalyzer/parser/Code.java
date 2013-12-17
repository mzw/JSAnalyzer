package jp.mzw.jsanalyzer.parser;

import org.jsoup.nodes.Element;

/**
 * Represents source code
 * @author Yuta Maezawa
 *
 */
public class Code {

	/**
	 * Raw source code
	 */
	protected String mCode;
	
	/**
	 * HTML object
	 */
	protected Element mHTMLElement;

	/**
	 * URL of this code
	 */
	protected String mURL;

	/**
	 * Types of this code. 
	 */
	protected int mType;
	
	/**
	 * Code types
	 */
	public static final int	
		Inline		= 0,
		Embedded 	= 1,
		External 	= 2;
	
	/**
	 * Constructor
	 * @param code Raw source code
	 * @param html Corresponding HTML object
	 * @param url URL where this code is
	 * @param type Code type, i.e., in-line, embedded, or external
	 */
	public Code(String code, Element html, String url, int type) {
		this.mCode = code;
		this.mHTMLElement = html;
		this.mURL = url;
		this.mType = type;
	}
	
	/**
	 * Determines whether this code is at in-line or not
	 * @return True or false represents in-line code or not
	 */
	public boolean isInline() {
		if(this.mType == Code.Inline) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Gets this code type
	 * @return This code type
	 */
	public int getType() {
		return this.mType;
	}
	
	/**
	 * Gets this code
	 * @return this code
	 */
	public String getCode() {
		return this.mCode;
	}
	
	/**
	 * Gets this HTML element
	 * @return this HTML element
	 */
	public Element getHTMLElement() {
		return this.mHTMLElement;
	}
	
	public String getUrl() {
		return this.mURL;
	}
}
