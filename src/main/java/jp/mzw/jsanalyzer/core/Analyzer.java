package jp.mzw.jsanalyzer.core;

import java.util.Date;

import jp.mzw.jsanalyzer.core.examples.*;
import jp.mzw.jsanalyzer.modeler.FSMExtractor;
import jp.mzw.jsanalyzer.modeler.Modeler;
import jp.mzw.jsanalyzer.modeler.model.fsm.FiniteStateMachine;
import jp.mzw.jsanalyzer.rule.RuleManager;
import jp.mzw.jsanalyzer.serialize.Serializer;
import jp.mzw.jsanalyzer.util.StringUtils;
import jp.mzw.jsanalyzer.verifier.Verifier;

/**
 * Contains main method
 * @author Yuta Maezawa
 *
 */
public class Analyzer {
	
	/**
	 * APIs for project information
	 */
	private Project mProject;
	
	/**
	 * APIs for rule information
	 */
	private RuleManager mRuleManager;
	
	/**
	 * Constructor
	 * @param project An example project.
	 */
	public Analyzer(Project project) {
		this.mProject = project;
		this.mRuleManager = createRuleManager(this.mProject);
	}
	
	/**
	 * Constructor
	 * @param filename A project filename
	 */
	public Analyzer(String filename) {
		this.mProject = new Project(filename);
		this.mRuleManager = createRuleManager(this.mProject);
	}
	
	/**
	 * Creates a RuleManager instance
	 * @param project An instance of the Project containing project information
	 * @return An instance of RuleManager which has filenames described in the project
	 */
	private RuleManager createRuleManager(Project project) {
		if(project == null) {
			StringUtils.printError(this, "Project is null");
			return null;
		}
		return new RuleManager(project.getRuleFilenames());
	}
	
	/**
	 * Gets a project instance
	 * @return This project instance
	 */
	public Project getProject() {
		return this.mProject;
	}
	
	/**
	 * Gets a rule manager instance
	 * @return This rule manager instance
	 */
	public RuleManager getRuleManager() {
		return this.mRuleManager;
	}
	
	/**
	 * An example main method. Set the argument of the Analyzer instance
	 * @param args Gives a filename of your project in JSAnalyzer at first argument (to be determined)
	 */
	public static void main(String[] args) {
		System.out.println("==============================");
		System.out.println("Running JSAnalyzer");
		System.out.println((new Date()).toString());
		System.out.println("==============================");

		System.out.println("Preparing your project...");
//		Analyzer analyzer = new Analyzer("projects/test2/project.xml");
		Analyzer analyzer = new Analyzer(new QAsiteError());
//		Analyzer analyzer = new Analyzer(new QAsiteCorrect());
//		Analyzer analyzer = new Analyzer(new FileDLerError());
//		Analyzer analyzer = new Analyzer(new FileDLerCorrect());
//		Analyzer analyzer = new Analyzer(new FileDLerRetry());
//		Analyzer analyzer = new Analyzer(new SWSError());
//		Analyzer analyzer = new Analyzer(new LoginDemo());
//		Analyzer analyzer = new Analyzer(new LWA());
		
		/// JSModeler
//		Modeler modeler = new Modeler(analyzer);
//		FiniteStateMachine fsm = modeler.extract();
//		Serializer.serialze(analyzer, fsm);
		
		/// JSVerifier
//		Verifier verifier = new Verifier(analyzer);
//		verifier.setup();
//		verifier.verifyIADP();
		
//		for(Limitation l : LimitationManager.getLimitations()) {
//			System.out.println(l.toString());
//		}

		System.out.println("==============================");
		System.out.println("See you again!");
		System.out.println("==============================");
	}

}