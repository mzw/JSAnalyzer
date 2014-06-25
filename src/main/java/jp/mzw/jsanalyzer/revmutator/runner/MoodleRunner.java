package jp.mzw.jsanalyzer.revmutator.runner;

import java.io.File;
import java.util.concurrent.TimeUnit;

import jp.mzw.jsanalyzer.core.Project;
import jp.mzw.jsanalyzer.core.cs.Moodle;
import jp.mzw.jsanalyzer.revmutator.MutatorPlugin;
import jp.mzw.jsanalyzer.revmutator.MutatorProxyPlugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crawljax.core.CrawljaxRunner;
import com.crawljax.core.configuration.CrawlRules.CrawlRulesBuilder;
import com.crawljax.core.configuration.CrawljaxConfiguration;
import com.crawljax.core.configuration.InputSpecification;
import com.crawljax.core.configuration.ProxyConfiguration;
import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;
import com.crawljax.plugins.proxy.WebScarabProxyPlugin;

public class MoodleRunner extends Runner {
	protected static final Logger LOGGER = LoggerFactory.getLogger(MoodleRunner.class);
	
	private static void log(String content) {
		LOGGER.info(content);
	}
	
	public static void main(String[] args) {
		log("kore ha test desu");
		System.out.println("kore ha test desu @stdout");
		
		Runner runner = new MoodleRunner();
		runner.run();
	}
	
	@Override
	public void run() {
		LOGGER.info("Moodle runner start");
		
		Project project = Moodle.getProject(Moodle.Original_2_3_1);
		String url = project.getUrl();
		String outputdir = getOutputDir(project.getDir());

		CrawljaxConfigurationBuilder builder = CrawljaxConfiguration.builderFor(url);
		setBrowser(BrowserType.FIREFOX);
		///
		builder.crawlRules().insertRandomDataInInputForms(INSERT_RANDOM_DATA_IN_INPUT_FORMS);
		builder.crawlRules().clickElementsInRandomOrder(CLICK_ELEMENTS_IN_RANDOM_ORDER);
		builder.crawlRules().clickOnce(CLICK_ONCE);
		builder.crawlRules().waitAfterReloadUrl(WAIT_TIME_AFTER_RELOAD, TimeUnit.MILLISECONDS);
		builder.crawlRules().waitAfterEvent(WAIT_TIME_AFTER_EVENT, TimeUnit.MILLISECONDS);
		///
		builder.setMaximumStates(MAX_STATES);
		builder.setMaximumDepth(MAX_DEPTH);
		builder.setOutputDirectory(new File(outputdir));

		/// Proxy
		ProxyConfiguration proxy = ProxyConfiguration.manualProxyOn(PROXY_HOST, PROXY_PORT);
		builder.setProxyConfig(proxy);

		WebScarabProxyPlugin web = new WebScarabProxyPlugin();
		MutatorProxyPlugin intercepter = new MutatorProxyPlugin();
		intercepter.setDefaultExcludeContentList();
		web.addPlugin(intercepter);
		builder.addPlugin(web);
		
		/// My crawling plugin
		MutatorPlugin mutator = new MutatorPlugin();
		mutator.setOutputFolder(outputdir);
		builder.addPlugin(mutator);
		
		/// Gives input specs
		setClickCrawlRules(builder.crawlRules());
		builder.crawlRules().setInputSpec(getInputSpec());
		
		CrawljaxRunner crawljax = new CrawljaxRunner(builder.build());
		crawljax.call();
		
	}
	
	@Override
	protected void setClickCrawlRules(CrawlRulesBuilder builder) {
		builder.click("a").withText("Login");
	}
	
	@Override
	protected InputSpecification getInputSpec() {
		InputSpecification input = new InputSpecification();

		/// Take your greatest care: Multiple entries with same key
		
		String username = "admin";
		String password = "admin-Adm1n";

		input.field("username").setValue(username);
		input.field("password").setValue(password);
		
		return input;
	}
}
