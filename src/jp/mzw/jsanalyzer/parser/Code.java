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
	 * 
	 */
	protected boolean isExternal;
	protected boolean isEmbedded;
	protected boolean isInline;
	
	protected String mURL;
	
	/**
	 * Constructor
	 * @param code is source code fragment
	 */
	public Code(String code, Element html, boolean isInline) {
		this.mCode = code;
		this.mHTMLElement = html;
		
		this.isExternal = false;
		if(isInline) {
			this.isEmbedded = false;
			this.isInline = true;
		} else {
			this.isEmbedded = true;
			this.isInline = false;
		}
		
		this.mURL = null;
	}
	public Code(String code, Element html, String url) {
		this.mCode = code;
		this.mHTMLElement = html;
		
		this.isExternal = true;
		this.isEmbedded = false;
		this.isInline = false;
		
		this.mURL = url;
	}
	
	/**
	 * Determines whether this code is at in-line or not
	 * @return True or false represents in-line code or not
	 */
	public boolean isInline() {
		return this.isInline;
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
