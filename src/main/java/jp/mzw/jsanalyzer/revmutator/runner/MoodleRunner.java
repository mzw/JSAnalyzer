package jp.mzw.jsanalyzer.revmutator.runner;

import java.io.File;
import java.util.concurrent.TimeUnit;

import jp.mzw.jsanalyzer.core.Project;
import jp.mzw.jsanalyzer.core.cs.Moodle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crawljax.core.CrawljaxRunner;
import com.crawljax.core.configuration.CrawljaxConfiguration;
import com.crawljax.core.configuration.InputSpecification;
import com.crawljax.core.configuration.ProxyConfiguration;
import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;
import com.crawljax.plugins.proxy.WebScarabProxyPlugin;

public class MoodleRunner extends Runner {
	protected static final Logger LOGGER = LoggerFactory.getLogger(MoodleRunner.class.getName());
	
	public static void main(String[] args) {
		Runner runner = new MoodleRunner();
		runner.run();
	}
	
	@Override
	public void run() {
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
		ProxyConfiguration prox = ProxyConfiguration.manualProxyOn(PROXY_HOST, PROXY_PORT);
		builder.setProxyConfig(prox);

		WebScarabProxyPlugin web = new WebScarabProxyPlugin();
		/// To be implemented
//		AstFunctionCallInstrumenter astfuncCallInst = new AstFunctionCallInstrumenter();
//		JSCyclCompxCalc cyclo = new JSCyclCompxCalc(OUTPUT_DIR);
//		JSModifyProxyPlugin proxyPlugin = new JSModifyProxyPlugin(astfuncCallInst, cyclo);
//		proxyPlugin.excludeDefaults();
//		web.addPlugin(proxyPlugin);
//		JSFuncExecutionTracer tracer = new JSFuncExecutionTracer("funcinstrumentation");
//		tracer.setJsFilesFolder(JS_FILES_FOLDER);
//		builder.addPlugin(tracer);
		///
		builder.addPlugin(web);
		
		/// Gives input specs
		builder.crawlRules().setInputSpec(getInputSpec());
		
		CrawljaxRunner crawljax = new CrawljaxRunner(builder.build());
		crawljax.call();
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
