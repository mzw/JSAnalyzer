package jp.mzw.jsanalyzer.core.examples;

import jp.mzw.jsanalyzer.core.Project;

public class SWSError extends Project {

	public SWSError() {
		super("SWSError",
				"http://mzw.jp/research/ex/sws/fault_quick.html",
				Project.getDefaultRuleFilenames(),
				"projects/test2");
	}
}
