package jp.mzw.jsanalyzer.formulator;

import java.util.ArrayList;
import java.util.List;

import jp.mzw.jsanalyzer.verifier.specification.Specification;

public class IADPFormulator extends Formulator {

	
	/**
	 * Constructor
	 * @param filename Contains information about implemented Ajax design patterns (IADP information)
	 */
	public IADPFormulator(String filename) {
		super(filename);
	}
	
	/**
	 * Reads given a XML file containing IADP information
	 */
	@Override
	protected void read(String filename) {
		
	}
	
	@Override
	public List<Specification> generate() {
		ArrayList<Specification> ret = new ArrayList<Specification>();
		
		
		return ret;
	}
}
