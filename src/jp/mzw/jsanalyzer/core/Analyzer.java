package jp.mzw.jsanalyzer.core;

import java.util.Date;

import org.apache.commons.lang3.StringEscapeUtils;

import jp.mzw.jsanalyzer.util.*;
import jp.mzw.jsanalyzer.core.example.*;
import jp.mzw.jsanalyzer.modeler.FSMExtractor;
import jp.mzw.jsanalyzer.modeler.Modeler;
import jp.mzw.jsanalyzer.rule.RuleManager;

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
	protected Analyzer(Project project) {
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
	 * Starts our analysis method with given project information
	 */
	public void analyze() {
		System.out.println("Starts to analyze");
		
		System.out.println("Extracting finite state machine...");
		FSMExtractor extractor = new FSMExtractor(this);
		extractor.extracts();
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
//		Analyzer analyzer = new Analyzer(new QAsite());
//		Analyzer analyzer = new Analyzer(new FileDLerError());
//		Analyzer analyzer = new Analyzer(new SWSError());
		Analyzer analyzer = new Analyzer(new LoginDemo());
		analyzer.analyze();
		
		/*
		Analyzer analyzer = new Analyzer(example);
		
		Graph graph = analyzer.analyze();
		StateMachine sm = StateMachine.construct(analyzer, graph);
		
		System.out.println(analyzer.getProject().getUrl());
		
		Util.write("/Users/yuta/Desktop", "sm.dot", sm.toString_dot());

		//analyzer.verify(graph);
		//analyzer.verifyADP("projects/test/IADPInfo_QAsite.xml", analyzer, graph);
		
		/*
		sm.setStateLayout(analyzer);
		Uppaal uppaal = new Uppaal(sm);
		Util.write("/Users/yuta/Desktop", "uppaal.xml", uppaal.translate());
		*/

		System.out.println("==============================");
		System.out.println("See you again!");
		System.out.println("==============================");
	}
}
