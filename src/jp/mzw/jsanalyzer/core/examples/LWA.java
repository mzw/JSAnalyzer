package jp.mzw.jsanalyzer.core.examples;

import jp.mzw.jsanalyzer.core.Project;

public class LWA extends Project {
	public LWA() {
		super("LWA",
				"http://mzw.jp/test/wp/index.php",
//				"http://localhost/~yuta/wp/wp.html",
				Project.getDefaultRuleFilenames(),
				"projects/test2");
	}
}
