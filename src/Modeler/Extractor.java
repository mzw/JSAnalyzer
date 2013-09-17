package Modeler;

import Analyzer.Analyzer;
import Analyzer.Util;
import Graph.Graph;
import Graph.Node;
import Parser.*;
import org.jsoup.nodes.Document;

import java.util.LinkedList;
import java.util.List;

public class Extractor {
	Analyzer analyzer = null;
	public Extractor(Analyzer analyzer) {
		this.analyzer = analyzer;
		
		this.init4parse();
	}
	
	private Document doc = null;
	private Graph jsGraph = null;
	private List<CSSParser> cssParsers = null;
	private void init4parse() {
		this.jsGraph = null;
		this.cssParsers = new LinkedList<CSSParser>();
	}
	public Document getDoc() {
		if(this.doc == null) {
			System.err.println("Error@Extractor.getDoc: null");
			return null;
		}
		return this.doc;
	}
	public Graph getJsGraph() {
		if(this.jsGraph == null) {
			System.err.println("Error@Extractor.getJsGraph: null");
			return null;
		}
		return this.jsGraph;
	}
	public List<CSSParser> getCssParsers() {
		if(this.cssParsers == null) {
			System.err.println("Error@Extractor.getCssParsers: null");
			return null;
		}
		return this.cssParsers;
	}
	
	/////
	public void parse() {
		/// HTML
		HTMLParser htmlParser = new HTMLParser();
		htmlParser.parse(
				this.analyzer.getProject().getUrl(),
				this.analyzer.getRuleList());

		/// JS
		List<JSCode> jsCodes = htmlParser.getJSCodes();
		List<JSParser> jsParsers = new LinkedList<JSParser>();
		
		Graph _jsGraph = Graph.getDummyGraph(); // for combining
		int offset4id = _jsGraph.getOffset4Id() + 1;
		
		for(JSCode jsCode : jsCodes) { // time consuming --> parallel?
			JSParser jsParser = new JSParser(jsCode.getUrl(), jsCode.toSource(), offset4id);
			jsParsers.add(jsParser);
			
			jsParser.parse();
			jsParser.setRules(this.analyzer.getRuleList());
			
			/// combining
			if(jsCode.getTarget() == null) { // embedded or external
				List<Node> rootChildren = jsParser.getGraph().getRootChildren();
				for(Node child : rootChildren) {
					_jsGraph.addChildren(child, jsParser.getGraph());
					if(child.isExecutableNode()) {
						_jsGraph.addEdgeFromRootNode(child);
					}
				}
			} else { // in-line
				int preRootNodeId = _jsGraph.addGraph(jsParser.getGraph());
				int edgeId = _jsGraph.addEdgeFromRootNode(preRootNodeId);
				_jsGraph.setEvent(edgeId, jsCode.getTarget(), jsCode.getAttr());
			}
			
			offset4id = jsParser.getOffset4Id() + 1; // +1 for an edge
		}
		
		/// CSS
		List<CSSCode> cssCodes = htmlParser.getCSSCodes();
		List<CSSCode> parsedCssCodes = new LinkedList<CSSCode>();
		
		Document _doc = htmlParser.getDocument();
		
		for(CSSCode cssCode : cssCodes) {
			CSSParser cssParser = new CSSParser(cssCode);

			// set target element
			if(cssCode.getTarget() == null) { // embedded or external
				cssCode.setIsInlinde(false);
				List<CSSCode> _cssCodes = cssParser.parse(_doc, this.analyzer.getRuleList());
				parsedCssCodes.addAll(_cssCodes);
				
				// import: depth = 1
				for(CSSCode imported : cssParser.getImportedCssCode()) {
					CSSParser _cssParser = new CSSParser(imported);
					List<CSSCode> __cssCodes = _cssParser.parse(_doc, this.analyzer.getRuleList());
					parsedCssCodes.addAll(__cssCodes);
				}
			} else {
				cssCode.setIsInlinde(true);
			}
		}
		
		cssCodes.addAll(parsedCssCodes);
		List<CSSParser> _cssParsers = new LinkedList<CSSParser>();
		for(CSSCode cssCode : cssCodes) {
			if(cssCode.getTarget() == null) {
				// CSS limitations: pseudo classes, @media, @import?
				//System.err.println("Unset target: " + cssCode.toSource());
			} else {
				CSSParser cssParser = new CSSParser(cssCode);
				_cssParsers.add(cssParser);
			}
		}
		
		///// parse result: doc, jsGraph and cssParsers
		this.doc = _doc;
		this.jsGraph = _jsGraph;
		this.cssParsers = _cssParsers;
	}
	
	/////
	public Graph extract() {

		Extender extender = new Extender(this.jsGraph);
		extender.extend();
		
		Util.write("/Users/yuta/Desktop", "extender.sm", this.jsGraph.toStringDot());
		
		Abstractor abstractor = new Abstractor(this.jsGraph);
		abstractor.abst();
		
		Util.write("/Users/yuta/Desktop", "abst.sm", this.jsGraph.toStringDot());
		
		DOM dom = new DOM(this.doc, this.cssParsers);
		Refiner refiner = new Refiner(this.jsGraph, dom, this.analyzer.getRuleList());
		refiner.refine();
		
		return jsGraph;
	}
	
	public void calcLoC() {

		/// HTML
		HTMLParser htmlParser = new HTMLParser();
		htmlParser.parse(
				this.analyzer.getProject().getUrl(),
				this.analyzer.getRuleList());
		
		String htmlCode = Util.wget(this.analyzer.getProject().getUrl());
		
		System.out.println("HTML LoC: " + Util.calcLoC(htmlCode));
		
		int jsLoC = 0;
		List<JSCode> jsCodes = htmlParser.getJSCodes();
		for(JSCode jsCode : jsCodes) {
			jsLoC += Util.calcLoC(jsCode.toSource());
		}
		System.out.println("JavaScript LoC: " + jsLoC);
		
		int cssLoC = 0;
		List<CSSCode> cssCodes = htmlParser.getCSSCodes();
		for(CSSCode cssCode : cssCodes) {
			cssLoC += Util.calcLoC(cssCode.toSource());
		}
		System.out.println("CSS LoC: " + cssLoC);
	}
}
