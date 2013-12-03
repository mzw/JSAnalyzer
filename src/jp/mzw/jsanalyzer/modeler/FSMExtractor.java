package jp.mzw.jsanalyzer.modeler;

import java.util.ArrayList;

import jp.mzw.jsanalyzer.config.FilePath;
import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.modeler.model.FiniteStateMachine;
import jp.mzw.jsanalyzer.parser.CSSCode;
import jp.mzw.jsanalyzer.parser.CSSParser;
import jp.mzw.jsanalyzer.parser.HTMLCode;
import jp.mzw.jsanalyzer.parser.HTMLParser;
import jp.mzw.jsanalyzer.parser.JSCode;
import jp.mzw.jsanalyzer.parser.JSParser;
import jp.mzw.jsanalyzer.util.TextFileUtils;

/**
 * Extracts finite state machines
 * @author Yuta Maezawa
 *
 */
public class FSMExtractor extends Modeler {
	
	
	/**
	 * Constructor
	 * @param analyzer gives all information of this project
	 */
	public FSMExtractor(Analyzer analyzer) {
		super(analyzer);
	}
	
	/**
	 * A elapsed time for extracting finite state machine
	 */
	protected long mExtractTime;
	
	/**
	 * Extracts a finite state machine
	 * @return An extracted finite state machine
	 */
	public FiniteStateMachine extracts() {
		long start = System.currentTimeMillis();
		/// start
		
		parse();
		
		FSMExtender extender = new FSMExtender(this.mAnalyzer);
		FSMAbstractor abstractor = new FSMAbstractor(this.mAnalyzer);
		FSMRefiner refiner = new FSMRefiner(this.mAnalyzer);
		FiniteStateMachine fsm = refiner.refine();
		
		/// end
		long end = System.currentTimeMillis();
		this.mExtractTime = end - start;
		
		return fsm;
	}
	
	/**
	 * 
	 */
	protected void parse() {
		String baseUrl = this.mAnalyzer.getProject().getUrl();
		String dir = this.mAnalyzer.getProject().getDir();
		
		HTMLParser htmlParser = HTMLParser.createHTMLParser(baseUrl, dir);
		
		ArrayList<CSSCode> cssCodeList = htmlParser.getCSSCodeList();
		for(CSSCode cssCode : cssCodeList) {
			CSSParser cssParser = new CSSParser(cssCode);
			cssParser.parse(); // set CSSStyleRules to CSSCode
			cssCode.manipulate(htmlParser.getDoc());
		}
		// Manipulates DOM using CSS code in HTML code
		// CSS code in JS code should be dynamically manipulated
		for(CSSCode cssCode : cssCodeList) {
			if(!cssCode.isInline()) { // first should manipulate using embedded or external codes
				
			}
		}
		for(CSSCode cssCode : cssCodeList) {
			if(cssCode.isInline()) { // Then update CSS rules using in-line codes
				
			}
			
		}
		
		
		
		ArrayList<JSCode> jsCodeList = htmlParser.getJSCodeList(this.mAnalyzer.getProject().getUrl(), this.mAnalyzer.getRuleManager());
		for(JSCode jsCode : jsCodeList) {
			JSParser jsParser = new JSParser(jsCode);
			//System.out.println(jsCode.getFilePath());
			
			/// created elements must be manipulated using embedded and external CSS code
		}
	}
	
	/**
	 * Gets an extract time in millisecond
	 * @return An elapsed time for extracting finite state machine
	 */
	public long getExtractTime() {
		return this.mExtractTime;
	}
}
