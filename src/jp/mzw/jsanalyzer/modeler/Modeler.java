package jp.mzw.jsanalyzer.modeler;

import jp.mzw.jsanalyzer.core.Analyzer;

public class Modeler {

	/**
	 * Gets all information for modeling via given Analyzer instance
	 */
	protected Analyzer mAnalyzer;
	
	public Modeler(Analyzer analyzer) {
		this.mAnalyzer = analyzer;
	}
}
