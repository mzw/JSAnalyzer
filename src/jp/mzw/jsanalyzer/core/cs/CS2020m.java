package jp.mzw.jsanalyzer.core.cs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jp.mzw.jsanalyzer.config.FileExtension;
import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.core.Project;
import jp.mzw.jsanalyzer.formulator.property.Property;
import jp.mzw.jsanalyzer.modeler.Modeler;
import jp.mzw.jsanalyzer.preventer.Preventer;
import jp.mzw.jsanalyzer.serialize.Serializer;
import jp.mzw.jsanalyzer.serialize.model.FiniteStateMachine;
import jp.mzw.jsanalyzer.util.TextFileUtils;
import jp.mzw.jsanalyzer.verifier.Verifier;
import jp.mzw.jsanalyzer.verifier.modelchecker.NuSMV;
import jp.mzw.jsanalyzer.verifier.specification.Specification;

public class CS2020m extends Project {
	
	public static void main(String[] args) {
		Project project = CS2020m.getProject(CS2020m.Original);
		Analyzer analyzer = new Analyzer(project);
		
//		Modeler modeler = new Modeler(analyzer);
//		jp.mzw.jsanalyzer.modeler.model.fsm.FiniteStateMachine fsm = modeler.extract();
//		Serializer.serialze(analyzer, fsm);
		
//		Verifier verifier = new Verifier(analyzer);
////		verifier.setup();
//		List<Specification> specList = CS2020m.getSpecList(analyzer, verifier.getExtractedFSM());
//		verifier.verifyIADP(specList);
		
		Preventer preventer = new Preventer(analyzer);
//		CS2020m.insertDelay(analyzer, preventer, CS2020m.UEHRegist);
//		CS2020m.insertDelay(analyzer, preventer, CS2020m.SeedRetrieve);
		CS2020m.insertDelay(analyzer, preventer, CS2020m.ValidateLogin);
		
	}
	
	private CS2020m(String projName, String projUrl) {
		super(projName, projUrl,
				CS2020m.setRuleFilenames(),
				"projects/project");
	}

	public static final int
		Original = 0,
		UEHRegist = 1,
		SeedRetrieve = 2,
		ValidateLogin = 3;
	
	public static CS2020m getProject(int ver) {
		switch(ver) {
		case CS2020m.Original:
			return new CS2020m("2020m.Original", "http://localhost/~yuta/research/cs/2020m/0.origin/2020m.html");
		case CS2020m.UEHRegist:
			return new CS2020m("2020m.UEHRegist", "http://localhost/~yuta/research/cs/2020m/0.origin/2020m.html.ver.1.html");
		case CS2020m.SeedRetrieve:
			return new CS2020m("2020m.SeedRetrieve", "http://localhost/~yuta/research/cs/2020m/0.origin/2020m.html.ver.2.html");
		case CS2020m.ValidateLogin:
			return new CS2020m("2020m.SeedRetrieve", "http://localhost/~yuta/research/cs/2020m/0.origin/2020m.html.ver.3.html");
		}
		return null;
	}
	
	public static List<String> setRuleFilenames() {
		List<String> ret = Project.getDefaultRuleFilenames();
		
		ret.add("res/libRules/jquery.xml");
		
		return ret;
	}
	
	public static void insertDelay(Analyzer analyzer, Preventer preventer, int ver) {
		
		long start = System.currentTimeMillis();
		
		/// Preliminary
		String base_dir = "/Users/yuta/Sites/research/cs/2020m/0.origin/";
		/// files
		String html_filename = "2020m.html";
		String js_filename = "2020m_files/login_controller.js";
		
		String postfix = ".ver."; // should add extension at tail
		String output_filename = null; // should set "filename.inserted.property.ext"
		
		int html_offset = -1;
		String html_code = null;
		
		String snippet = null;
		String inserted_js_content = null;
		
		switch(ver) {
		case CS2020m.Original:
			break;
		case CS2020m.UEHRegist:
			
			html_offset = preventer.getHTMLScriptOffset(base_dir, html_filename, js_filename);
			html_code = preventer.getMutatedHTMLCode(base_dir, html_filename, html_offset);
			/// Writes
			output_filename = html_filename + postfix + CS2020m.UEHRegist + FileExtension.HTML;
			TextFileUtils.write(base_dir, output_filename, html_code);
			
			break;
		case CS2020m.SeedRetrieve:
			
			snippet = "$.post(\"include/login.php\", { task: 'getseed' }";
			inserted_js_content = preventer.getMutatedJSCode(base_dir, js_filename, snippet);
			/// Writes
			output_filename = js_filename + postfix + CS2020m.SeedRetrieve + FileExtension.JS;
			TextFileUtils.write(base_dir, output_filename, inserted_js_content);
			
			// for HTML code
			/// from js_filename to output_filename
			html_code = preventer.changeJSRefInHTML(base_dir, html_filename, js_filename, output_filename);
			output_filename = html_filename + postfix + CS2020m.SeedRetrieve + FileExtension.HTML;
			TextFileUtils.write(base_dir, output_filename, html_code);
			
			break;
		case CS2020m.ValidateLogin:
			
			snippet = "$.post(action, { task: $('#task').val()";
			inserted_js_content = preventer.getMutatedJSCode(base_dir, js_filename, snippet);
			/// Writes
			output_filename = js_filename + postfix + CS2020m.ValidateLogin + FileExtension.JS;
			TextFileUtils.write(base_dir, output_filename, inserted_js_content);
			
			// for HTML code
			/// from js_filename to output_filename
			html_code = preventer.changeJSRefInHTML(base_dir, html_filename, js_filename, output_filename);
			output_filename = html_filename + postfix + CS2020m.ValidateLogin + FileExtension.HTML;
			TextFileUtils.write(base_dir, output_filename, html_code);
			
			break;
		}

		long end = System.currentTimeMillis();
		System.out.println("Insertion time: " + (end - start));
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
		pAsyncComm_post.setTemplateVariables(NuSMV.genOr(fsm.getFuncIdList("jQuery.post")), NuSMV.genOr(fsm.getUserEventIdList(analyzer)), null, null);
		ret.add(new Specification(pAsyncComm_post));
		
		/// ACRetry
		// Could not find any failure events
		
		/// SRWait
		// Same as SeedRetrieve
		
		/// UEHRegist
		Property pUEHRegist_ready = Property.getPropertyByNameAbbr("UEHRegist").clone();
		pUEHRegist_ready.setTemplateVariables(NuSMV.genOr(fsm.getUserEventIdList(analyzer)), NuSMV.genOr(fsm.getEventIdList("ready")), null, null);
		ret.add(new Specification(pUEHRegist_ready));
		
		/// UEHSingle
		// Same as LFDisable
		

		/////
		/// Additional properties
		/////
		
		/// Properties from Direct Login Pattern
		/// b/c keyword "login_controller.js" at NerdyData
		///
		
		/// SeedRetrieval
		Property pSeedRetrieval = Property.getPropertyByNameAbbr("SeedRetrieval").clone();
		pSeedRetrieval.setTemplateVariables(NuSMV.genOr(fsm.getFuncIdList("validateLogin")), NuSMV.genOr(fsm.getFuncIdList("getSeed")), null, null);
		ret.add(new Specification(pSeedRetrieval));
		
		/// LFDisable
		String loginEventId = fsm.getEventId("submit", 21, 15);
		String succLoginStateId = fsm.getFuncId("window.location.replace", 34, 0);
		String P = Property.genTemplateVariableP(NuSMV.genExpr(succLoginStateId), NuSMV.genExpr(loginEventId), fsm);
		
		Property pLFDisable = Property.getPropertyByNameAbbr("LFDisable").clone();
		pLFDisable.setTemplateVariables(P, null, null, null);
		ret.add(new Specification(pLFDisable));
		
		return ret;
	}
}
