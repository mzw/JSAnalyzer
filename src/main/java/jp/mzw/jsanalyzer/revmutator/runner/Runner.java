package jp.mzw.jsanalyzer.revmutator.runner;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crawljax.core.configuration.InputSpecification;

import jp.mzw.jsanalyzer.config.Bin;

public abstract class Runner {
	protected static final Logger LOGGER = LoggerFactory.getLogger(Runner.class.getName());
	
	public abstract void run();
	protected abstract InputSpecification getInputSpec();

	private static final String OUTPUT_DIR = "runner_output";
	public static String getOutputDir(String projDir) {
		File _file = new File(projDir, OUTPUT_DIR);
		return _file.getPath();
	}
	
	public enum BrowserType {
		FIREFOX
	}
	private static final String SYSTEM_PROPERTY_FIREFOX = "webdriver.firefox.bin";
	protected static void setBrowser(BrowserType type) {
		if(BrowserType.FIREFOX.equals(type)) {
			System.setProperty(SYSTEM_PROPERTY_FIREFOX, Bin.Firefox);
		} else {
			LOGGER.debug("Unknown browser type", type);
		}
	}
	
	/// Crawljax default settings
	protected static final boolean INSERT_RANDOM_DATA_IN_INPUT_FORMS = false;
	protected static final boolean CLICK_ONCE = true;
	protected static final boolean CLICK_ELEMENTS_IN_RANDOM_ORDER = true;
	protected static final int MAX_STATES = 20;
	protected static final int MAX_DEPTH = 2;
	protected static final long WAIT_TIME_AFTER_EVENT = 50;
	protected static final long WAIT_TIME_AFTER_RELOAD = 20;
	/// Proxy
	protected static final String PROXY_HOST = "127.0.0.1";
	protected static final int PROXY_PORT = 8008;
	
}
