package Analyzer;

import Graph.Graph;
import Graph.StateMachine;
import Modeler.Extractor;
import Rule.RuleList;
import Verifier.AjaxDesignProperty;
import Verifier.Spin;
import Verifier.Uppaal;

import java.util.List;

public class Analyzer {
	
	/////
	/// Constructor
	/////
	private Project mProject = null;
	private RuleList mRuleList = null;
	public Analyzer(String project_filename) {
		mProject = new Project(project_filename);
		mRuleList = new RuleList();
		mRuleList.setRules(this.mProject.getRuleFilenames());
	}
	public Project getProject() {
		return this.mProject;
	}
	public RuleList getRuleList() {
		return this.mRuleList;
	}

	/////
	/// methods
	/////
	public void verifyIADP(String filename, Analyzer analyzer, Graph graph) {
		
		long start = System.currentTimeMillis();
		
		StateMachine sm = StateMachine.construct(analyzer, graph);
		Util.write(this.getProject().getDir(), this.getProject().getName() + Config.EXT_StateMachine, sm.toString_xml());
		
		Spin spin = new Spin(sm, this);
		Util.write(
				this.getProject().getDir(),
				this.getProject().getName() + Config.EXT_Promela,
				spin.translate());
		
		// generate concrete formulas
		List<AjaxDesignProperty> specs = AjaxDesignProperty.parse(filename, analyzer, sm);
		for(AjaxDesignProperty spec : specs) {
			System.out.println("Veryfy: " + spec.getFormula());
			long vstart = System.currentTimeMillis();
			spin.verify(spec);
			long vend = System.currentTimeMillis();
			System.out.println(spec.getId() + ": Verification time is " + (vend - vstart) + " msec");
		}
		
		long end = System.currentTimeMillis();
		System.out.println("Verification time: " + (end - start));
		
		spin.saveVerifyIADPResults(this, specs);
	}

	public Graph analyze() {
		
		long start = System.currentTimeMillis();
		Extractor extractor = new Extractor(this);
		extractor.parse();
		Graph graph = extractor.extract();
		long end = System.currentTimeMillis();
		System.out.println("Extraction time: " + (end - start));
		
		extractor.calcLoC();
		
		return graph;
	}

	/////
	/// main
	/////
	public static void main(String[] args) {
		
		Analyzer analyzer = new Analyzer(Test.filename);
		
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
		
		System.out.println("all done");
	}	
}
