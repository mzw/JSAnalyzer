package jp.mzw.jsanalyzer.test;

import java.util.ArrayList;
import java.util.List;

import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.core.Project;
import jp.mzw.jsanalyzer.formulator.adp.*;
import jp.mzw.jsanalyzer.formulator.pp.*;
import jp.mzw.jsanalyzer.formulator.property.*;
import jp.mzw.jsanalyzer.modeler.Modeler;
import jp.mzw.jsanalyzer.serialize.Serializer;
import jp.mzw.jsanalyzer.serialize.model.FiniteStateMachine;
import jp.mzw.jsanalyzer.verifier.Verifier;
import jp.mzw.jsanalyzer.verifier.modelchecker.NuSMV;
import jp.mzw.jsanalyzer.verifier.specification.Specification;

public class TestProject extends Project {

//	public static final String url = "http://b-med.ru/index.php";
//	public static final String url = "http://localhost/~yuta/research/test/irctc/irctc.html"; //"https://irctc.co.in/";
//	public static final String url = "http://localhost/~yuta/research/test/pastbin/pastebin.html";
//	public static final String url = "http://localhost/~yuta/research/test/aacom/aa.com.html";
//	public static final String url = "http://www.opencart.com/index.php";
//	public static final String url = "http://localhost/~yuta/research/test/pelusita/pelusita.html";
//	public static final String url = "http://localhost/~yuta/research/test/ffd8/ffd8.org.html";
//	public static final String url = "http://localhost/~yuta/research/test/stonecontact/stone.contact.html";
//	public static final String url = "http://localhost/~yuta/research/test/stadtmelder/stadtmelder.html";
//	public static final String url = "http://localhost/~yuta/research/test/sportsauthority/sportsauthority.html";
	public static final String url 
//	= "http://localhost/~yuta/research/test/correiois/correios.html";
//	= "http://localhost/~yuta/research/test/amware/amware.html";
//	= "http://localhost/~yuta/research/test/vmi/vmi.html";
	= "http://localhost/~yuta/research/test/esa/esa.html";

	public static void main(String[] args) {
		Project project = new TestProject();
		Analyzer analyzer = new Analyzer(project);
		
		Modeler modeler = new Modeler(analyzer);
		jp.mzw.jsanalyzer.modeler.model.fsm.FiniteStateMachine fsm = modeler.extract();
		Serializer.serialze(analyzer, fsm);
		
		
//		Verifier verifier = new Verifier(analyzer);
//		verifier.setup();
		
//		List<Specification> specList = TestProject.getSpecList(analyzer, verifier.getExtractedFSM());
//		verifier.verifyIADP(specList);
		
	}
	
	public TestProject() {
		super("TestProject", url, TestProject.getDefaultRuleFilenames(), "projects/test");
	}
	
	
	public static List<String> setRuleFilenames() {
		List<String> ret = Project.getDefaultRuleFilenames();
		
//		ret.add("res/libRules/jquery.xml");
		
		return ret;
	}
	

	public static List<Specification> getSpecList(Analyzer analyzer, FiniteStateMachine fsm) {
		ArrayList<Specification> ret = new ArrayList<Specification>();
		

		Property pAsyncComm = Property.getPropertyByNameAbbr("AsyncComm").clone();
		pAsyncComm.setTemplateVariables(NuSMV.genOr(fsm.getFuncIdList("jQuery.post")), NuSMV.genOr(fsm.getUserEventIdList(analyzer)), null, null);
		ret.add(new Specification(pAsyncComm));
		
		return ret;
	}
	
}
