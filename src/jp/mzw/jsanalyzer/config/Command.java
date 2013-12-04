package jp.mzw.jsanalyzer.config;

/**
 * Provides paths of commands which JSAnalzyer calls 
 * @author Yuta Maezawa
 *
 */
public class Command {
	/**
	 * A path where Spin model checker is installed
	 */
	public static final String Spin				= "/opt/local/bin/spin";
	
	/**
	 * A path where C compiler is installed
	 */
	public static final String Gcc				= "/usr/bin/gcc";
	
	/**
	 * A path where Graphviz is installed
	 */
	public static final String Dot				= "/usr/local/bin/dot";
}
