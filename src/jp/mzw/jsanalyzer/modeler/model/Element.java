package jp.mzw.jsanalyzer.modeler.model;

import jp.mzw.jsanalyzer.core.IdGen;

/**
 * Contains common model elements
 * @author Yuta Maezawa
 *
 */
public class Element {
	
	/**
	 * Constructor. A model element should have id string.
	 * @param id Identifies this element
	 */
	public Element(String id) {
		this.mId = id;
	}
	
	/**
	 * Constructor. If no given ID string, this element gets a ID string.
	 */
	public Element() {
		this.mId = IdGen.genId();
	}
	
	/**
	 * A unique string representing this element
	 */
	protected String mId;
	
	/**
	 * Gets this unique ID string
	 * @return A string for identifying this element
	 */
	public String getId() {
		return this.mId;
	}
}
