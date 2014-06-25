package jp.mzw.jsanalyzer.formulator;

import java.util.List;

import jp.mzw.jsanalyzer.verifier.specification.Specification;

public class Formulator {

	/**
	 * Contains information for generating verification formulas
	 */
	protected String mFilename;
	
	/**
	 * Constructor
	 * @param filename Contains information for generating verification formulas
	 */
	public Formulator(String filename) {
		this.mFilename = filename;
		this.read(this.mFilename);
	}
	
	/**
	 * Should override
	 * @param filename
	 */
	protected void read(String filename) {
		
	}
	
	/**
	 * Should override
	 * @return
	 */
	public List<Specification> generate() {
		return null;
	}
}
