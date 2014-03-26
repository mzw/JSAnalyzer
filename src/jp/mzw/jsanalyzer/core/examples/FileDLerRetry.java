package jp.mzw.jsanalyzer.core.examples;

import jp.mzw.jsanalyzer.core.Project;

public class FileDLerRetry extends Project {

	public FileDLerRetry() {
		super("FileDLerRetry",
				"http://mzw.jp/research/ex/fd/motivatingExample.retry.html",
				Project.getDefaultRuleFilenames(),
				"projects/test2");
	}
}
