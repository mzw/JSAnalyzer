package jp.mzw.jsanalyzer.config;

/**
 * Provides paths of commands which JSAnalzyer calls 
 * @author Yuta Maezawa
 *
 */
public class Bin {
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
	
	
	/**
	 * A path where NuSMV is installed
	 */
	public static final String NuSMV			= "/opt/local/bin/NuSMV";
	
	/**
	 * For bash file to generate graphs by using Graphviz
	 */
	public static final String SH				= "/bin/sh";
	
	/**
	 * To execute and test Ajax apps by using Selenium WebDriver
	 */
	public static final String Firefox_10_0_2	= "/Users/yuta/Applications/Firefox.10.0.2.app/Contents/MacOS/firefox-bin";
	public static final String Firefox_17_0_11	= "/Users/yuta/Applications/Firefox.17.0.11esr.app/Contents/MacOS/firefox-bin";
	public static final String Firefox_27_0		= "/Users/yuta/Applications/Firefox.27.0.app/Contents/MacOS/firefox-bin";

	public static final String Firefox			= Firefox_10_0_2;
}
