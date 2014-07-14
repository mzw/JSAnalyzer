package jp.mzw.jsanalyzer.revmutator.runner;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crawljax.core.configuration.CrawlRules.CrawlRulesBuilder;
import com.crawljax.core.configuration.InputSpecification;

public abstract class Runner {
	protected static final Logger LOG = LoggerFactory.getLogger(Runner.class);
	
	public abstract void run();
	
	/**
	 * 
	 * @param crawlRulesBuilder
	 */
	protected abstract void setClickCrawlRules(CrawlRulesBuilder crawlRulesBuilder);
	
	/**
	 * Take your greatest care with multiple entries with same key
	 * @return Input specifications
	 */
	protected abstract InputSpecification getInputSpec();

	private static final String OUTPUT_DIR = "runner_output";
	public static String getOutputDir(String projDir) {
		File _file = new File(projDir, OUTPUT_DIR);
		return _file.getPath();
	}
	
	/// Crawljax default settings
	protected static final boolean INSERT_RANDOM_DATA_IN_INPUT_FORMS = false;
	protected static final boolean CLICK_ONCE = true;
	protected static final boolean CLICK_ELEMENTS_IN_RANDOM_ORDER = true;
	protected static final int MAX_STATES = 250;
	protected static final int MAX_DEPTH = 5;
	protected static final long WAIT_TIME_AFTER_EVENT = 200;
	protected static final long WAIT_TIME_AFTER_RELOAD = 20;
	/// Proxy
	protected static final String PROXY_HOST = "127.0.0.1";
	protected static final int PROXY_PORT = 8084;
	
}
