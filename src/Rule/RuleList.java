package Rule;

import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import Analyzer.Util;

public class RuleList {
	private List<String> filenames = null;
	
	private List<Trigger> triggers = null;
	private List<Potential> potentials = null;
	private List<Control> controls = null;
	private List<Manipulate> manipulates = null;	
	private List<Library> libraries = null;
	
	public RuleList() {
		this.filenames = new LinkedList<String>();
		
		this.triggers = new LinkedList<Trigger>();
		this.potentials = new LinkedList<Potential>();
		this.controls = new LinkedList<Control>();
		this.manipulates = new LinkedList<Manipulate>();
		this.libraries = new LinkedList<Library>();
	}
	public void setRules(List<String> filenames) {
		this.filenames = filenames;
		for(String filename : filenames) {
			this.setRule(filename);
		}
	}
	public void setRule(String filename) {
		if(!this.filenames.contains(filename)) {
			this.filenames.add(filename);
		}
		this.load(filename);
	}
	private void load(String filename) {
		String str = Util.cat(filename);
		Document doc = Jsoup.parse(str, filename, Parser.xmlParser());
		Elements elms = doc.getAllElements();

		for(int i = 0; i < elms.size(); i++) {
			Element elm = elms.get(i);
			if("Trigger".equalsIgnoreCase(elm.tagName())) {
				Trigger trigger =
						new Trigger(
								elm.attr("interact"),
								elm.attr("via"),
								elm.attr("event"));
				this.triggers.add(trigger);
			} else if("Potential".equalsIgnoreCase(elm.tagName())) {
				Potential potential =
						new Potential(
								elm.attr("interact"),
								elm.attr("via"),
								elm.attr("func"),
								elm.attr("target"),
								elm.attr("event"),
								elm.attr("callback"));
				boolean unimplemented = Boolean.parseBoolean(elm.attr("unimplemented"));
				potential.setUnimplemented(unimplemented);
				this.potentials.add(potential);
			} else if("Control".equalsIgnoreCase(elm.tagName())) {
				Control control = 
						new Control(
								elm.attr("attr"),
								elm.attr("disabled"));
				this.controls.add(control);
				//System.out.println(elm.toString());
			} else if("Manipulate".equalsIgnoreCase(elm.tagName())) {
				Manipulate manipulate =
						new Manipulate(
								elm.attr("func"),
								elm.attr("semantic"),
								elm.attr("by"),
								elm.attr("value"),
								elm.attr("target"));
				this.manipulates.add(manipulate);
			} else if("Library".equalsIgnoreCase(elm.tagName())) {
				Library lib = new Library(elm.attr("name"), elm.attr("path"));
				if(elm.attr("skip") != null) {
					lib.setIsSkip(Boolean.parseBoolean(elm.attr("skip")));
				}
				this.libraries.add(lib);
			}
		}
	}
	
	public boolean isRuledJsLib(String filename) {
		for(Library lib : this.libraries) {
			boolean isFound = lib.isRuledJsLib(filename);
			if(isFound) {
				this.setRule(lib.getRuleFile());
				return isFound;
			}
		}
		
		return false;
	}
	
	public boolean isTrigger(String keyword) {
		for(Trigger trigger : this.triggers) {
			if(trigger.isTrigger(keyword)) {
				return true;
			}
		}
		return false;
	}
	public boolean isUserEvent(String event) {
		if(event == null || "".equals(event)) {
			return false;
		}
		
		for(Trigger trigger : this.triggers) {
			if(trigger.isTrigger(event) && trigger.isUserEvent()) {
				return true;
			}
		}
		
		return false;
	}
	public Trigger getTrigger(String keyword) {
		for(Trigger trigger : this.triggers) {
			if(trigger.isTrigger(keyword)) {
				return trigger;
			}
		}
		return null;
	}
	public Control getControl(String keyword) {
		for(Control control : this.controls) {
			if(control.isControl(keyword)) {
				return control;
			}
		}
		return null;
	}
	public Potential getPotential(String keyword) {
		for(Potential potential : this.potentials) {
			if(potential.isPotential(keyword)) {
				return potential;
			}
		}
		return null;
	}
	public Manipulate getManipulate(String keyword) {
		for(Manipulate manipulate : this.manipulates) {
			if(manipulate.isManipulate(keyword)) {
				return manipulate;
			}
		}
		return null;
	}
	
	public List<Potential> getPotentials() {
		return this.potentials;
	}
}
