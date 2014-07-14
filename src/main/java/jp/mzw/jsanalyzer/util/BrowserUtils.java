package jp.mzw.jsanalyzer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.mzw.jsanalyzer.util.VersionUtils;

public class BrowserUtils {
	protected static final Logger LOG = LoggerFactory.getLogger(BrowserUtils.class);

	public enum Type {
		FIREFOX
	}

	public static final String SYSTEM_PROPERTY_FIREFOX 	= "webdriver.firefox.bin";
	
	private static final String Firefox_10_0_2	= "/Users/yuta/Applications/Firefox.10.0.2.app/Contents/MacOS/firefox-bin";
	private static final String Firefox_17_0_11	= "/Users/yuta/Applications/Firefox.17.0.11esr.app/Contents/MacOS/firefox-bin";
	private static final String Firefox_27_0	= "/Users/yuta/Applications/Firefox.27.0.app/Contents/MacOS/firefox-bin";
	
	public static String getFirefoxBin(VersionUtils ver) {
		if(ver.equals(10, 0, 2)) {
			return Firefox_10_0_2;
		} else if(ver.equals(17, 0, 11)) {
			return Firefox_17_0_11;
		} else if(ver.equals(27, 0, 0)) {
			return Firefox_27_0;
		}
		return Firefox_10_0_2; // default
	}
	
	public static void setBrowser(BrowserUtils.Type type, VersionUtils ver) {
		if(BrowserUtils.Type.FIREFOX.equals(type)) {
			System.setProperty(BrowserUtils.SYSTEM_PROPERTY_FIREFOX, BrowserUtils.getFirefoxBin(ver));
		} else {
			LOG.info("Unknown browser type", type);
		}
	}
}
