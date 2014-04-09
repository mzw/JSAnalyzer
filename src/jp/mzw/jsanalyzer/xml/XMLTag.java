package jp.mzw.jsanalyzer.xml;

public class XMLTag {
	/**
	 * Project
	 */
	public static final String Project 				= "Project";
	public static final String ProjectName 			= "Name";
	public static final String ProjectUrl 			= "URL";
	public static final String ProjectRules 		= "Rules";
	public static final String ProjectRule	 		= "Rule";
	
	/**
	 * Rule
	 */
	public static final String RuleTrigger 			= "Trigger";
	public static final String RuleFunction			= "Function";
	public static final String RuleControl	 		= "Control";
	public static final String RuleLibrary 			= "Library";
	
	
	/**
	 * Finite State Machine
	 */
	public static final String FSM	 				= "FiniteStateMachine";
	public static final String FSMStates 			= "States";
	public static final String FSMState	 			= "State";
	public static final String FSMTranss 			= "Transitions";
	public static final String FSMTrans	 			= "Transition";
	public static final String FSMEntry				= "Entry";
	public static final String FSMExit 				= "Exit";
	
	/**
	 * IADP Repository
	 */
	public static final String IADPProperty				= "Property";
	public static final String IADPVeriable				= "Variable";
	public static final String IADPAttrVarId			= "id";
	public static final String IADPAttrVarId_P			= "$P";
	public static final String IADPAttrVarId_P1			= "$P1";
	public static final String IADPAttrVarId_P2			= "$P2";
	public static final String IADPAttrVarId_S			= "$S";
	public static final String IADPAttrVarId_Q			= "$Q";
	public static final String IADPAttrVarId_R			= "$R";
	public static final String IADPAttrSrc				= "source";
	public static final String IADPAttrSrc_Func			= "func";
	public static final String IADPAttrSrc_Event		= "event";
	public static final String IADPAttrVar_UserEvents	= "UserEvents";
}
