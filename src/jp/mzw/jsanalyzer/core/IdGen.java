package jp.mzw.jsanalyzer.core;

import jp.mzw.jsanalyzer.util.StringUtils;

/**
 * Generates serial IDs in JSAnalyzer
 * @author Yuta Maezawa
 *
 */
public class IdGen {
	/**
	 * A suffix of an incremental integer attached to id strings
	 */
	private static int ID_SOURCE = 1;
	
	/**
	 * A prefix string attached to id strings
	 */
	private static final String ID_PREFIX = "JSAnalyzer_";
	
	/**
	 * Generates unique id strings
	 * @return A id string
	 */
	public static String genId() {
		return String.format(ID_PREFIX + "%1$010d", ID_SOURCE++);
	}
	
	/**
	 * Parses the serial integer from assigned id string
	 * @param str_id An assigned id string
	 * @return The serial integer
	 */
	public static int getIdNum(String str_id) {
		if(str_id.indexOf(ID_PREFIX) == 0) {
			String _str_id = str_id.substring(ID_PREFIX.length());
			return Integer.parseInt(_str_id);
		}
		StringUtils.printError(IdGen.class, "Not id string", str_id);
		return -1;
	}
}
