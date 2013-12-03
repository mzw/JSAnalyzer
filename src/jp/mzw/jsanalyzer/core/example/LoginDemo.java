package jp.mzw.jsanalyzer.core.example;

import jp.mzw.jsanalyzer.core.Project;

public class LoginDemo extends Project {
	public LoginDemo() {
		super("LoginDemo",
				"http://mzw.jp/research/ex/LoginDemo/index.php",
				Project.getDefaultRuleFilenames(),
				"projects/test2");
	}
}
