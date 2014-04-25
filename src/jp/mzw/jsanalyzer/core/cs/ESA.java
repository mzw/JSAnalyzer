package jp.mzw.jsanalyzer.core.cs;

import java.util.ArrayList;
import java.util.List;

import jp.mzw.jsanalyzer.config.FileExtension;
import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.core.Project;
import jp.mzw.jsanalyzer.formulator.adp.AjaxDesignPattern;
import jp.mzw.jsanalyzer.formulator.pp.Existence;
import jp.mzw.jsanalyzer.formulator.pp.PropertyPattern;
import jp.mzw.jsanalyzer.formulator.property.ACRetry;
import jp.mzw.jsanalyzer.formulator.property.AsyncComm;
import jp.mzw.jsanalyzer.formulator.property.Property;
import jp.mzw.jsanalyzer.formulator.property.SRWait;
import jp.mzw.jsanalyzer.formulator.property.UEHRegist;
import jp.mzw.jsanalyzer.modeler.Modeler;
import jp.mzw.jsanalyzer.preventer.Preventer;
import jp.mzw.jsanalyzer.serialize.Serializer;
import jp.mzw.jsanalyzer.serialize.model.Event;
import jp.mzw.jsanalyzer.serialize.model.FiniteStateMachine;
import jp.mzw.jsanalyzer.serialize.model.State;
import jp.mzw.jsanalyzer.serialize.model.Transition;
import jp.mzw.jsanalyzer.util.TextFileUtils;
import jp.mzw.jsanalyzer.verifier.Verifier;
import jp.mzw.jsanalyzer.verifier.modelchecker.NuSMV;
import jp.mzw.jsanalyzer.verifier.specification.Specification;

public class ESA extends Project {
	
	public static void main(String[] args) {
		Project project = ESA.getProject(ESA.Original);
		Analyzer analyzer = new Analyzer(project);

//		Modeler modeler = new Modeler(analyzer);
//		jp.mzw.jsanalyzer.modeler.model.fsm.FiniteStateMachine fsm = modeler.extract();
//		Serializer.serialze(analyzer, fsm);
		
//		Verifier verifier = new Verifier(analyzer);
////		verifier.setup();
//		List<Specification> specList = ESA.getSpecList(analyzer, verifier.getExtractedFSM());
//		verifier.verifyIADP(specList);

		Preventer preventer = new Preventer(analyzer);
//		ESA.insertDelay(analyzer, preventer, ESA.UEHRegist);
		ESA.insertDelay(analyzer, preventer, ESA.FDValid);
	}
	
	private ESA(String projName, String projUrl) {
		super(projName, projUrl,
				ESA.setRuleFilenames(),
				"projects/project");
	}

	public static final int
		Original = 0,
		UEHRegist = 1,
		FDValid = 2;
	
	public static ESA getProject(int ver) {
		switch(ver) {
		case ESA.Original:
			return new ESA("ESA.Original", "http://localhost/~yuta/research/cs/esa/0.1.origin/esa.html");
		case ESA.UEHRegist:
			return new ESA("ESA.UEHRegist", "http://localhost/~yuta/research/cs/esa/0.1.origin/esa.html.ver.1.html");
		case ESA.FDValid:
			return new ESA("ESA.FDValid", "http://localhost/~yuta/research/cs/esa/0.1.origin/esa.html.ver.2.html");
		}
		return null;
	}
	

	public static void insertDelay(Analyzer analyzer, Preventer preventer, int ver) {
		
		long start = System.currentTimeMillis();
		
		/// Preliminary
		String base_dir = "/Users/yuta/Sites/research/cs/esa/0.1.origin/";
		/// files
		String html_filename = "esa.html";
		String js_filename = "esa_files/core129.js";
		
		String postfix = ".ver."; // should add extension at tail
		String output_filename = null; // should set "filename.inserted.property.ext"
		
		int html_offset = -1;
		String html_code = null;
		
		
		switch(ver) {
		case ESA.Original:
			break;
		case ESA.UEHRegist:
			
			html_offset = preventer.getHTMLScriptOffset(base_dir, html_filename, "esa_files/addthis_widget.js");
			html_code = preventer.getMutatedHTMLCode(base_dir, html_filename, html_offset);
			/// Writes
			output_filename = html_filename + postfix + ESA.UEHRegist + FileExtension.HTML;
			TextFileUtils.write(base_dir, output_filename, html_code);
			
			break;
		case ESA.FDValid:
			
			html_offset = preventer.getHTMLScriptOffset(base_dir, html_filename, "esa_files/c_main.js");
			html_code = preventer.getMutatedHTMLCode(base_dir, html_filename, html_offset);
			/// Writes
			output_filename = html_filename + postfix + ESA.FDValid + FileExtension.HTML;
			TextFileUtils.write(base_dir, output_filename, html_code);
			break;
		}

		long end = System.currentTimeMillis();
		System.out.println("Insertion time: " + (end - start));
	}
	
	
	public static List<String> setRuleFilenames() {
		List<String> ret = Project.getDefaultRuleFilenames();
		
		ret.add("res/libRules/jquery.xml");
		
		return ret;
	}
	
	
	public static List<Specification> getSpecList(Analyzer analyzer, FiniteStateMachine fsm) {
		ArrayList<Specification> ret = new ArrayList<Specification>();

		/////
		// Fundamental properties
		/////
		
		/// AyncComm
		// Implemented but not called from anywhere, i.e., function va(id) {...}
//		Property pAsyncComm_get = Property.getPropertyByNameAbbr("AsyncComm").clone();
//		pAsyncComm_get.setTemplateVariables(NuSMV.genOr(fsm.getFuncIdList("jQuery.get")), NuSMV.genOr(fsm.getUserEventIdList(analyzer)), null, null);
//		ret.add(new Specification(pAsyncComm_get));

		Property pAsyncComm_post = Property.getPropertyByNameAbbr("AsyncComm").clone();
		pAsyncComm_post.setTemplateVariables(NuSMV.genOr(fsm.getFuncIdList("jQuery.get")), NuSMV.genOr(fsm.getUserEventIdList(analyzer)), null, null);
		ret.add(new Specification(pAsyncComm_post));
		
		/// ACRetry
		// Could not find any failure events
		
		/// SRWait
		// response: used only in callback
		
		/// UEHRegist
		Property pUEHRegist_onload = Property.getPropertyByNameAbbr("UEHRegist").clone();
		pUEHRegist_onload.setTemplateVariables(NuSMV.genOr(fsm.getUserEventIdList(analyzer)), NuSMV.genOr(fsm.getEventIdList("ready")), null, null);
		ret.add(new Specification(pUEHRegist_onload));
		
		/// UEHSingle
		// Could not determine
		
		/////
		/// Additional properties
		/////
		
		/// Properties from Live Form Pattern
		/// b/c keyword "onsubmit=validate" at NerdyData
		
		/// FDValid
		String onsubmit = fsm.getEventId("onsubmit", 0, 0);
		String validate = fsm.getFuncId("validate", 13, 727);
		
		Property pFDValid = Property.getPropertyByNameAbbr("FDValid").clone();
		pFDValid.setTemplateVariables(NuSMV.genExpr(onsubmit), NuSMV.genExpr(validate), null, null);
		ret.add(new Specification(pFDValid));

		
		
		
		return ret;
	}
}
