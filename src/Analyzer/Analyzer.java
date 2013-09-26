package Analyzer;

import Graph.Graph;
import Graph.StateMachine;
import Modeler.Extractor;
import Rule.RuleList;
import Verifier.AjaxDesignProperty;
import Verifier.Specification;
import Verifier.Spin;

import java.util.LinkedList;
import java.util.List;

public class Analyzer {
	private static String filename = "projects/test/project.xml";
	
	
	///// verify
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
	
	
	///// analyze
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
	
	
	
	private Project project = null;
	private RuleList ruleList = null;
	public Analyzer(String project_filename) {
		this.project = new Project(project_filename);
		
		this.ruleList = new RuleList();
		ruleList.setRules(this.project.getRuleFilenames());
	}
	public Project getProject() {
		return this.project;
	}
	public RuleList getRuleList() {
		return this.ruleList;
	}

	public static void main(String[] args) {
		
		Analyzer analyzer = new Analyzer(filename);
		Graph graph = analyzer.analyze();
		StateMachine _sm = StateMachine.construct(analyzer, graph);
		
		Util.write("/Users/yuta/Desktop", "sm.dot", _sm.toString_dot());
		//analyzer.verify(graph);
		
		//analyzer.verifyADP("projects/test/IADPInfo_QAsite.xml", analyzer, graph);
		//analyzer.verify4ouworkshop("/Users/yuta/Desktop/result/", graph);
		
		/*
		StateMachine sm = analyzer.construct(graph);
		sm.setStateLayout(analyzer);
		Uppaal uppaal = new Uppaal(sm);
		String uppaal_str = uppaal.translate();
		System.out.println(uppaal_str);
		*/
		
		System.out.println("all done");
	}
	
}
