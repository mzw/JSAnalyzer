package jp.mzw.jsanalyzer.core.example;

import jp.mzw.jsanalyzer.core.Project;

public class FileDLerError extends Project {

	public FileDLerError() {
		super("FileDLerError",
				"http://mzw.jp/research/ex/fd/motivatingExample_fault.html",
				Project.getDefaultRuleFilenames(),
				"projects/test2");
	}
}
