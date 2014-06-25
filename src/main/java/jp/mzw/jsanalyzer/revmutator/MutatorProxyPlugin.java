package jp.mzw.jsanalyzer.revmutator;

import java.io.IOException;
import java.util.ArrayList;

import org.owasp.webscarab.httpclient.HTTPClient;
import org.owasp.webscarab.model.Request;
import org.owasp.webscarab.model.Response;
import org.owasp.webscarab.plugin.proxy.ProxyPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MutatorProxyPlugin extends ProxyPlugin {
	private static final Logger LOGGER = LoggerFactory.getLogger(MutatorProxyPlugin.class);
	public static final String logbackFilePath = "/logback.xml";

	/**
	 * Interprets server communication data
	 * @param req Contains raw request data
	 * @param res Contains raw response data
	 * @return
	 */
	private Response interpret(Request req, Response res) {
		LOGGER.info("Interpret: " + req.getURL().toString());
		
		return res;
	}

	protected ArrayList<String> mExcludeCotentList;
	public MutatorProxyPlugin() {
		mExcludeCotentList = new ArrayList<String>();
	}

	/**
	 * Adds content to be excluded
	 */
	public void addExcludeContent(String content) {
		if(!mExcludeCotentList.contains(content)) {
			mExcludeCotentList.add(content);
		}
	}
	/**
	 * Default exclude contents
	 * @see <a href="https://github.com/saltlab/mutandis/blob/master/src/main/java/mutandis/astModifier/JSModifyProxyPlugin.java">Mutandis</a>
	 */
	public void setDefaultExcludeContentList() {
		addExcludeContent(".*jquery[-0-9.]*.js?.*");
		addExcludeContent(".*jquery.*.js?.*");
	//	addExcludeContent(".*same-game.*.htm?.*");
		addExcludeContent(".*prototype.*js?.*");
		addExcludeContent(".*scriptaculous.*.js?.*");
		addExcludeContent(".*mootools.js?.*");
		addExcludeContent(".*dojo.xd.js?.*");
		addExcludeContent(".*yuiloader.js?.*");
		addExcludeContent(".*google.*");
		addExcludeContent(".*min.*.js?.*");
		addExcludeContent(".*pack.*.js?.*");
		addExcludeContent(".*compressed.*.js?.*");
		addExcludeContent(".*rpc.*.js?.*");
		addExcludeContent(".*o9dKSTNLPEg.*.js?.*");
		addExcludeContent(".*gdn6pnx.*.js?.*");
		addExcludeContent(".*show_ads.*.js?.*");
	//	addExcludeContent(".*ga.*.js?.*");
		//The following 10 excluded files are just for Tudu
		addExcludeContent(".*builder.js");
		addExcludeContent(".*controls.js");
		addExcludeContent(".*dragdrop.js");
		addExcludeContent(".*effects.js");
		addExcludeContent(".*prototype.js");
		addExcludeContent(".*scriptaculous.js");
		addExcludeContent(".*slider.js");
		addExcludeContent(".*unittest.js");
	//	addExcludeContent(".*engine.js");
		addExcludeContent(".*util.js");
		addExcludeContent(".*cycle.js");
		///////
		addExcludeContent(".*qunit.js");
		addExcludeContent(".*filesystem.js");
		addExcludeContent(".*functional.js");
		addExcludeContent(".*test.core.js");
		addExcludeContent(".*inject.js");
	}

	@Override
	public HTTPClient getProxyPlugin(HTTPClient in) {
		return new WrapHTTPClient(in);
	}
	
	/**
	 * Interprets responses on proxy server
	 * @author Yuta Maezawa
	 *
	 */
	private class WrapHTTPClient implements HTTPClient {
		private HTTPClient client;
		public WrapHTTPClient(HTTPClient in) {
			client = in;
		}

		@Override
		public Response fetchResponse(Request request) throws IOException {
			return interpret(request, client.fetchResponse(request));
		}
	}

	@Override
	public String getPluginName() {
		return "MutatorPlugin";
	}
	
}
