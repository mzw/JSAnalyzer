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

	//private static String filename = "projects/wp/project.xml";
	//private static String filename = "projects/DocAccesser/project.xml";
	private static String filename = "projects/test/project.xml";
	
	
	///// verify
	public void verifyADP(String filename, Analyzer analyzer, Graph graph) {
		StateMachine _sm = this.construct(graph);
		Util.write("/Users/yuta/Desktop", "sm.dot", _sm.toString_dot());

		long start = System.currentTimeMillis();
		StateMachine sm = this.construct(graph);
		
		
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
		
		spin.saveVerifyADPResults(this, specs);
		
		
	}
	
	public void verify(Graph graph) {

		StateMachine _sm = this.construct(graph);
		Util.write("/Users/yuta/Desktop", "sm.dot", _sm.toString_dot());
		
		long start = System.currentTimeMillis();
		StateMachine sm = this.construct(graph);


		Util.write(this.getProject().getDir(), this.getProject().getName() + Config.EXT_StateMachine, sm.toString_xml());
		
		Spin spin = new Spin(sm, this);
		
		Util.write(
				this.getProject().getDir(),
				this.getProject().getName() + Config.EXT_Promela,
				spin.translate());
		

		List<Specification> specs = new LinkedList<Specification>();
		/*
		 * for Login with Ajax @WordPress
		*/
		String ready = "ready";
		for(String event : sm.getEvents()) {
			if(!event.equals(ready)) {
				Specification spec = new Specification(
						"Ready pattern", "(App_event != " + ready + ") U (App_event == " + event + ")");
				specs.add(spec);
				spin.verify(spec);
			}
		}

		/*
		 * for sForm

		String onload = "onload";
		for(String event : sm.getEvents()) {
			if(!event.equals(onload) && !event.equals("onSuccess") && this.getRuleList().isTrigger(event)) {
				Specification spec = new Specification(
						"User event registration", "(App_event != " + onload + ") U (App_event == " + event + ")");
				specs.add(spec);
			}
		}
		
		
		specs.add(new Specification(
				"Proc before submit", "(App_state != validateIt) U (App_state == submit)"));
		
		
		specs.add(new Specification(
				"User event before submit", "(App_event != onclick) U (App_state == submit)"));
		

		String req = "validateIt";
		String res = "onSuccess";
		for(String event : sm.getEvents()) {
			if(!event.equals(onload) && !event.equals("onSuccess") && this.getRuleList().isTrigger(event)) {
				Specification spec = new Specification(
						//"User event during async comm", "[](((App_state == " + req + ") && (App_event != " + res + ") && <>(App_event == " + res + ")) -> ((App_event != " + event + ") U (App_event == " + res + ")))");
						"User event during async comm", "[](((App_state == " + req + ") && (App_event != " + res + ")) -> ((App_event != " + event + ") U ((App_event == " + res + ") || [](App_event != " + event + "))))");
				specs.add(spec);
			}
		}
		 */
		
		
		/*
		 * for FileDLer

		String onload = "onload";
		
		for(String event : sm.getEvents()) {
			if(!event.equals(onload) && !event.equals("onSuccess") && !event.equals("onFailure") && this.getRuleList().isTrigger(event)) {
				Specification spec = new Specification(
						"User event registration", "(App_event != " + onload + ") U (App_event == " + event + ")");
				specs.add(spec);
			}
		}

		specs.add(new Specification(
				"Response before activation", "(App_event != onSuccess) U (App_state == inputFormText)"));
		

		specs.add(new Specification(
				"User event before submission", "(App_event != onclick) U (App_state == doSubmit)"));

		for(String event : sm.getEvents()) {
			if(!event.equals(onload) && !event.equals("onSuccess") && !event.equals("onFailure") && this.getRuleList().isTrigger(event)) {
				Specification spec = new Specification(
						"User event handler singleton", "<>((App_state == doDownload) && (App_event == " + event + "))");
				specs.add(spec);
			}
		}
		 */
		
		/*
		 * for Shopping wabsite
		String onload = "onload";
		for(String event : sm.getEvents()) {
			if(!event.equals(onload) && !event.equals("onSuccess") && this.getRuleList().isTrigger(event)) {
				Specification spec = new Specification(
						"User event registration", "(App_event != " + onload + ") U (App_event == " + event + ")");
				specs.add(spec);
			}
		}
		specs.add(new Specification(
				"User event handler singleton (Duplicate order)", "<>((App_state == addCart) && (App_event == onclick))"));
		
		 */
		
		/*
		 * for ItemSelector
		String onload = "onload";
		for(String event : sm.getEvents()) {
			if(!event.equals(onload)) {
				Specification spec = new Specification(
						"Ready pattern", "(App_event != " + onload + ") U (App_event == " + event + ")");
				specs.add(spec);
				spin.verify(spec);
			}
		}
		 */
		
		for(Specification spec : specs) {
			System.out.println("Veryfy: " + spec.getFormula());
			long vstart = System.currentTimeMillis();
			spin.verify(spec);
			long vend = System.currentTimeMillis();
			System.out.println(spec.getId() + ": Verification time is " + (vend - vstart) + " msec");
		}
		
		long end = System.currentTimeMillis();
		System.out.println("Verification time: " + (end - start));
		
		spin.saveVerifyResults(this, specs);
		

	}
	

	private void verify4ouworkshop(String dir, Graph graph) {
		StateMachine sm = this.construct(graph);
		Util.write(dir, "sm.dot", sm.toString_dot());
		Util.write(dir, "sm.xml", sm.toString_xml());

		Spin spin = new Spin(sm, this);

		Util.write(dir, "sm.pml", spin.translate());
		Util.write(
				this.getProject().getDir(),
				this.getProject().getName() + Config.EXT_Promela,
				spin.translate());

		// add specs
		List<Specification> specs = new LinkedList<Specification>();		
		specs.add(new Specification("Read down", "(App_state != readPublicDoc) U (App_state == loginAsAnonymous || App_state == loginAsUser || App_state == loginAsSuperUser)"));
		specs.add(new Specification("Read down", "(App_state == tryLogout) U (App_state == tryLogin)"));
		specs.add(new Specification("Read down", "(App_state == loginAsUser) -><> (App_state == readUserDoc)"));
		specs.add(new Specification("Read down", "<> (App_state == __exit__)"));
		
		/*
		specs.add(new Specification("No read up", "(App_state != loginAsAnonymous) U (App_state == readUserDoc)"));
		specs.add(new Specification("No read up", "(App_state != loginAsAnonymous) U (App_state == readPremierDoc)"));
		specs.add(new Specification("Read down", "(App_state != loginAsUser) U (App_state == readPublicDoc)"));
		specs.add(new Specification("Read down", "(App_state != loginAsUser) U (App_state == readUserDoc)"));
		specs.add(new Specification("No read up", "(App_state != loginAsUser) U (App_state == readPremierDoc)"));
		specs.add(new Specification("Read down", "(App_state != loginAsSuperUser) U (App_state == readPublicDoc)"));
		specs.add(new Specification("Read down", "(App_state != loginAsSuperUser) U (App_state == readUserDoc)"));
		specs.add(new Specification("Read down", "(App_state != loginAsSuperUser) U (App_state == readPremierDoc)"));
		*/
		
		// verify specs
		long start = System.currentTimeMillis();
		for(Specification spec : specs) {
			System.out.println("Veryfy: " + spec.getFormula());
			long vstart = System.currentTimeMillis();
			spin.verify(spec);
			long vend = System.currentTimeMillis();
			System.out.println(spec.getId() + ": Verification time is " + (vend - vstart) + " msec");
		}
		
		long end = System.currentTimeMillis();
		System.out.println("Verification time: " + (end - start));

		Util.write(dir, "results.html", spin.saveVerifyBLPResults(this, specs));
	}
	/////
	
	
	///// analyze
	public Graph analyze() {
		
		long start = System.currentTimeMillis();
		Extractor extractor = new Extractor(this);
		extractor.parse();
		Graph graph = extractor.extract();
		long end = System.currentTimeMillis();
		System.out.println("Extraction time: " + (end - start));
		
		extractor.calcLoC();
		
		
		//Util.write("/Users/yuta/Desktop", "jsGraph.dot", graph.toStringDot());
		
		return graph;
	}
	
	private StateMachine construct(Graph graph) {
		StateMachine ret = StateMachine.construct(this, graph);
		
		//ret.setStateLayout(this);
		//Util.write("/Users/yuta/Sites/JSAnalyzerWeb/files", "StateMachine_test.layouted.sm", ret.toString_xml());
		
		return ret;
	}
	/////
	
	
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

		StateMachine _sm = analyzer.construct(graph);
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
