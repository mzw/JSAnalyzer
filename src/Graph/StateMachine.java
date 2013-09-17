package Graph;

import java.awt.Point;
import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import Analyzer.Analyzer;
import Analyzer.Config;
import Analyzer.Util;

public class StateMachine extends Graph {

	private Graph graph = null;
	public static StateMachine construct(Analyzer analyzer, Graph graph) {
		StateMachine ret = new StateMachine();
		ret.setGraph(graph);
		ret.setName(analyzer.getProject().getName());

		int entryNodeId = graph.getNextId();
		ret.addEntry(entryNodeId);
		int exitNodeId = graph.getNextId();
		ret.addExit(exitNodeId);
		ret.addTransition(graph.getNextId(), entryNodeId, graph.getRootNodeId(), "", "", "");
		
		for(Node node : graph.getNodes()) {
			ret.addState(node.getId(), node.getName());
			
			if(node.isExit()) {
				int exitTransId = graph.getNextId();
				ret.addTransition(exitTransId, node.getId(), exitNodeId, "", "", "");
			}
		}
		
		
		for(Edge edge : graph.getEdges()) {
			ret.addTransition(edge.getId(), edge.getFrom(), edge.getTo(),
					edge.getEventLabel(), edge.getCondLabel(), "");
		}
		
		return ret;
	}
	public void setGraph(Graph graph) {
		this.graph = graph;
	}
	public Graph getGraph() {
		return this.graph;
	}
	
	/////
	private String name = null;
	private int nextId = 3; // 1 = entry, 2 = exit
	
	private List<Entry> entries = null;
	private List<Exit> exits = null;
	private List<State> states = null;
	private List<Transition> transs = null;
	
	public StateMachine() {
		super(3);
		this.name = "UnnamedStateMachine";
		this.states = new LinkedList<State>();
		this.transs = new LinkedList<Transition>();
		this.entries = new LinkedList<Entry>();
		this.exits = new LinkedList<Exit>();	
	}
	
	public void setStateLayout(Analyzer analyzer) {

		String dot_filename = analyzer.getProject().getName() + Config.EXT_Dot;
		String dot_content = this.toString_dot();
		Util.write(analyzer.getProject().getDir(), dot_filename, dot_content);
		try {
			HashMap<String, Point> layout = Util.getStateLayout(analyzer.getProject().getDir(), dot_filename);
			
			// states
			for(State state : this.states) {
				String str_id = (new Integer(state.getId())).toString();
				Point pos = layout.get(str_id);
				state.setPos(pos.getX(), pos.getY());
			}

			// entries and exits
			for(State state : this.entries) {
				String str_id = (new Integer(state.getId())).toString();
				Point pos = layout.get(str_id);
				state.setPos(pos.getX(), pos.getY());
			}
			for(State state : this.exits) {
				String str_id = (new Integer(state.getId())).toString();
				Point pos = layout.get(str_id);
				state.setPos(pos.getX(), pos.getY());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getFilename() {
		return this.name + Config.EXT_StateMachine;
	}
	public void setNextId(int nextId) {
		this.nextId = nextId;
	}
	public int getNextId() {
		return nextId++;
	}
	
	public void addEntry(int id) {
		Entry entry = new Entry(id);
		this.entries.add(entry);
	}
	public void addExit(int id) {
		Exit exit = new Exit(id);
		this.exits.add(exit);
	}
	public void addState(int id, String name) {
		State state = new State(id, name);
		this.states.add(state);
	}
	public void addTransition(int id, int from, int to, String event, String guard, String action) {
		Transition trans = new Transition(id, from, to, event, guard, action);
		this.transs.add(trans);
	}
	
	///// utilities
	public static StateMachine read(String filename) {
		StateMachine sm = new StateMachine();;

		try {
			javax.xml.parsers.DocumentBuilderFactory dbfactory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
			dbfactory.setCoalescing(true);
			javax.xml.parsers.DocumentBuilder builder = dbfactory.newDocumentBuilder();
			org.w3c.dom.Document doc = builder.parse(new File(filename));

			// Identify a model type
			org.w3c.dom.Element root = doc.getDocumentElement();
			if((Config.StateMachineTag).compareToIgnoreCase(root.getNodeName()) != 0) {
				System.err.println("Unknown input file: " + root.getNodeName());
				return null;
			}
			
			// initialized params
			org.w3c.dom.NodeList nl = null;

			String sm_name = root.getAttribute("name");
			int nextId = Integer.parseInt(root.getAttribute("nextId"));
			sm.setName(sm_name);
			sm.setNextId(nextId);

			// entry
			nl = root.getElementsByTagName(Config.entryTag);
			for(int i = 0; i < nl.getLength(); i++) {
				org.w3c.dom.Element elm = (org.w3c.dom.Element)nl.item(i);
				String id = elm.getAttribute("id");
				sm.addEntry(Integer.parseInt(id));
			}
			// exit
			nl = root.getElementsByTagName(Config.exitTag);
			for(int i = 0; i < nl.getLength(); i++) {
				org.w3c.dom.Element elm = (org.w3c.dom.Element)nl.item(i);
				String id = elm.getAttribute("id");
				sm.addExit(Integer.parseInt(id));
			}
			
			
			
			// state
			nl = root.getElementsByTagName(Config.stateTag);
			for(int i = 0; i < nl.getLength(); i++) {
				org.w3c.dom.Element elm = (org.w3c.dom.Element)nl.item(i);

				int id = Integer.parseInt(elm.getAttribute("id"));
				String name = elm.getAttribute("name");
				
				sm.addState(id, name);
			}
			// trans
			nl = root.getElementsByTagName(Config.transTag);
			for(int i = 0; i < nl.getLength(); i++) {
				org.w3c.dom.Element elm = (org.w3c.dom.Element)nl.item(i);

				int id = Integer.parseInt(elm.getAttribute("id"));
				int from = Integer.parseInt(elm.getAttribute("from"));
				int to = Integer.parseInt(elm.getAttribute("to"));
				
				String event = elm.getAttribute("event");
				String guard = elm.getAttribute("guard");
				String action = elm.getAttribute("action");
				
				sm.addTransition(id, from, to, event, guard, action);
			}


		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return sm;
	}
	
	///// toStrings
	public String toString_dot() {
		String ret = "digraph G {\n";
		
		//ret += "splines=false\n"; // for straight edges

		for(Entry entry : this.entries) {
			ret += entry.toString_dot() + "\n";
		}
		for(Exit exit : this.exits) {
			ret += exit.toString_dot() + "\n";
		}
		
		ret += "\n";
		
		for(State state : this.states) {
			ret += state.toString_dot() + "\n";
		}
		
		ret += "\n";
		
		for(Transition trans : this.transs) {
			ret += trans.toString_dot() + "\n";
		}

		ret += "\n}\n";
		
		return ret;
	}
	
	public String toString_xml() {
		String ret = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
		
		ret += "<" + Config.StateMachineTag + " name=\"" + Util.esc_xml(this.name) + "\" nextId=\"" + this.nextId + "\" >\n";
		
		for(Entry entry : this.entries) {
			ret += "\t" + entry.toString_xml() + "\n";
		}
		for(Exit exit : this.exits) {
			ret += "\t" + exit.toString_xml() + "\n";
		}

		ret += "\t<" + Config.stateTag + "s>\n";
		for(State state : this.states) {
			ret += "\t\t" + state.toString_xml() + "\n";
		}
		ret += "\t</" + Config.stateTag + "s>\n";
		

		ret += "\t<" + Config.transTag + "s>\n";
		for(Transition trans : this.transs) {
			ret += "\t\t" + trans.toString_xml() + "\n";
		}
		ret += "\t</" + Config.transTag + "s>\n";
		
		ret += "</" + Config.StateMachineTag + ">\n";
		
		return ret;
	}
	
	public static String toString(org.w3c.dom.Element elm) {
		String ret = "";
		
		ret += "<" + elm.getTagName() + " ";
		org.w3c.dom.NamedNodeMap attrs = elm.getAttributes();
		
		for(int i = 0; i < attrs.getLength(); i++) {
			org.w3c.dom.Node attr = attrs.item(i);
			ret += attr.getNodeName() + "=" + attr.getNodeValue() + " ";
		}
		 
		ret += "/>";
		
		return ret;
	}
	
	///
	public String getName() {
		return this.name;
	}
	public int getNumOfStates() {
		return this.states.size();
	}
	public int getNumOfTranss() {
		return this.transs.size();
	}
	public State getState(int id) {
		return this.states.get(id);
	}
	public List<State> getStates() {
		return this.states;
	}
	public Transition getTransition(int id) {
		return this.transs.get(id);
	}

	public List<String> getEvents() {
		List<String> ret = new LinkedList<String>();
		
		for(int i = 0; i < this.transs.size(); i++) {
			Transition t = this.transs.get(i);
			if(!"".equals(t.getEvent()) && !ret.contains(t.getEvent())) {
				ret.add(t.getEvent());
			}
		}
		return ret;
	}

	public State getStateById(int id) {
		for(int i = 0; i < this.states.size(); i++) {
			State s = this.states.get(i);
			if(s.getId() == id) {
				return s;
			}
		}
		return null;
	}
	public boolean isExitStateById(int id) {
		for(int i = 0; i < this.exits.size(); i++) {
			if(this.exits.get(i).getId() == id) {
				return true;
			}
		}
		return false;
	}
	
	public State getInitState() {
		if(this.entries.size() != 1) {
			System.err.println("Currently, only deal with 1 entry state machines");
			System.exit(-1);
		} else {
			State entry = this.entries.get(0);
			List<Transition> transFromEntry = new LinkedList<Transition>();
			for(int i = 0; i < this.transs.size(); i++) {
				Transition t = this.transs.get(i);
				if(t.getFrom() == entry.getId()) {
					transFromEntry.add(t);
				}
			}
			if(transFromEntry.size() != 1) {
				System.err.println("Currently, only deal with 1 initial state");
				System.exit(-1);
			} else {
				return this.getStateById(transFromEntry.get(0).getTo());
			}
		}
		System.err.println("Bug @ StateMachine.StateMachine.getInitState");
		System.exit(-1);
		return null;
	}
	
	public List<Transition> getTranssFromStateId(int id) {
		List<Transition> ret = new LinkedList<Transition>();
		for (int i = 0; i < this.transs.size(); i++) {
			Transition t = this.transs.get(i);
			if(t.getFrom() == id) {
				ret.add(t);
			}
		}
		return ret;
	}
	
	public boolean isExitById(int id) {
		for(int i = 0; i < this.exits.size(); i++) {
			if(this.exits.get(i).getId() == id) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isEntryById(int id) {
		for(int i = 0; i < this.entries.size(); i++) {
			if(this.entries.get(i).getId() == id) {
				return true;
			}
		}
		return false;
	}

	///// for Spin
	private Hashtable<State, List<State>> abstStateStates = new Hashtable<State, List<State>>();;
	private Hashtable<State, List<Transition>> abstStateTranss = new Hashtable<State, List<Transition>>();
	public void abstractNoEventEdges() {
		List<State> rmStates = new LinkedList<State>();
		List<Transition> rmTranss = new LinkedList<Transition>();
		for(int i = 0; i < this.transs.size(); i++) {
			Transition t = this.transs.get(i);
			if(!this.isEntryById(t.getFrom()) && (t.getEvent() == null || "".equals(t.getEvent()))) {
				State fromState = this.getStateById(t.getFrom());
				State toState = this.getStateById(t.getTo());
				
				
				// replace
				for(int j = 0; j < this.transs.size(); j++) {
					Transition rep = this.transs.get(j);
					if(rep == t) {
						continue;
					}
					if(toState == null) {
						continue;
					}
					if(rep.getFrom() == toState.getId()) {
						rep.setFrom(fromState.getId());
					}
					if(rep.getTo() == toState.getId()) {
						rep.setTo(fromState.getId());
					}
				}
				
				// remove
				rmStates.add(toState);
				rmTranss.add(t);
				
				// store abstraction information
				List<State> abstStates = this.abstStateStates.get(fromState);
				if(abstStates == null) {
					abstStates = new LinkedList<State>();
				}
				abstStates.add(toState);
				this.abstStateStates.put(fromState, abstStates);
				List<Transition> abstTranss = this.abstStateTranss.get(fromState);
				if(abstTranss == null) {
					abstTranss = new LinkedList<Transition>();
				}
				abstTranss.add(t);
				this.abstStateTranss.put(fromState, abstTranss);
				
				//System.out.println(toState.getName() + " is abstracted to " + fromState.getName());
			}
		}
		
		for(State s : rmStates) {
			this.states.remove(s);
		}
		for(Transition t : rmTranss) {
			this.transs.remove(t);
		}
	}

	public boolean hasStateForSpin(String state) {
		if(state == null || "".equals(state)) {
			return false;
		}
		List<State> states = this.getStates();
		for(State s : states) {
			if(state.equals(Util.esc_spin(s.getName()))) {
				return true;
			}
		}
		return false;
	}

	public boolean hasEventForSpin(String event) {
		if(event == null || "".equals(event)) {
			return false;
		}
		List<String> events = this.getEvents();
		for (String e : events) {
			if(event.equals(Util.esc_spin(e))) {
				return true;
			}
		}
		return false;
	}
	
	public List<State> getErrorProneStatesForSpin(String state) {
		if(state == null || "".equals(state)) {
			return null;
		}
		
		State corrState = this.getStateByNameForSpin(state);
		List<State> abstractedStates = new LinkedList<State>();
		abstractedStates.add(corrState);
		getAbstractedStates(corrState, abstractedStates);
		return abstractedStates;
	}
	public List<Transition> getErrorProneTransitionsForSpin(List<State> abstractedStates) {
		if(abstractedStates == null) {
			System.err.println("Bug @getErrorProneTransitionsForSpin");
			System.exit(-1);
		}
		
		List<Transition> abstractedTranss = new LinkedList<Transition>();
		for(State ass : abstractedStates) {
			List<Transition> ats = this.abstStateTranss.get(ass);
			if(ats != null) {
				abstractedTranss.addAll(ats);
			}
		}
		return abstractedTranss;
	}
	public Transition getErrorProneTransitionForSpin(String event, List<State> cand_pre_states) {
		if(event == null || "".equals(event)) {
			return null;
		}
		
		List<Transition> cand_transs = new LinkedList<Transition>();
		for(Transition t : this.getAllTranss()) {
			if(event.equals(Util.esc_spin(t.getEvent()))) {
				cand_transs.add(t);
			}
		}
		
		Transition ret = null;
		for(Transition t : cand_transs) {
			for(State s : cand_pre_states) {
				if(t.getFrom() == s.getId()) {
					ret = t;
				}
			}
		}
		
		if(ret == null) {
			System.err.println("bug @StateMachine.getErrorProneTransitionForSpin: " + event);
			//System.exit(-1);
		}
		
		return ret;
	}

	public List<State> getAllStates() {
		List<State> allStates = new LinkedList<State>();
		for(State s : this.states) {
			allStates.add(s);
		}
		for(List<State> ass : this.abstStateStates.values()) {
			allStates.addAll(ass);
		}
		return allStates;
	}
	
	public List<Transition> getAllTranss() {
		List<Transition> allTranss = new LinkedList<Transition>();
		for(Transition t : this.transs) {
			allTranss.add(t);
		}
		for(List<Transition> ats : this.abstStateTranss.values()) {
			allTranss.addAll(ats);
		}
		return allTranss;
	}
	
	private void getAbstractedStates(State state, List<State> abstractedStates) {
		List<State> abstStates = this.abstStateStates.get(state);
		if(abstStates != null) {
			for(State as : abstStates) {
				abstractedStates.add(as);
				getAbstractedStates(as, abstractedStates);
			}
		}
	}
	
	private State getStateByNameForSpin(String name) {
		if(name == null || "".equals(name)) {
			return null;
		}
		for(State s : this.getStates()) {
			if(name.equals(Util.esc_spin(s.getName()))) {
				return s;
			}
		}
		return null;
	}
	
	public void createStateMachineFromStatesAndTransitions(List<State> states, List<Transition> transs) {
		this.setStates(states);
		this.setTransitions(transs);
	}
	

	private void setStates(List<State> states) {
		this.states = states;
	}
	private void setTransitions(List<Transition> transs) {
		this.transs = transs;
	}

	public void save(String dir, String filename) {
		String content = "";
		
		content += "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
		content += "<StateMachine name=\"" + this.name + "\" nextId=\"" + 0 +"\">\n"; // modify nextId

		int initId = -1;
		int entryId = 1;
		boolean isTrans_entry_init = false;
		
		content += "\t<Entry id=\"" + entryId + "\" xpos=\"50\" ypos=\"50\" />\n";
		
		
		content += "\t<States>\n";
		for(State s : this.states) {
			content += "\t\t" + s.toString_xml() + "\n";
			
			if("__init__".equals(s.getName())) {
				initId = s.getId();
			}
		}
		content += "\t</States>\n";
		
		content += "\t<Transitions>\n";
		for(Transition t : this.transs) {
			content += "\t\t" + t.toString_xml() + "\n";
			
			if(t.getFrom() == entryId && t.getTo() == initId) {
				isTrans_entry_init = true;
			}
		}
		if(!isTrans_entry_init) {
			content +=
					"\t\t<Transition id=\"10000\"" +
					" from=\"" + entryId + "\" to=\"" + initId + "\" event=\"\" guard=\"\" action=\"\" />\n";
		}
		content += "\t</Transitions>\n";

		content += "</StateMachine>\n";
		
		Util.write(dir, filename, content);
	}
}
