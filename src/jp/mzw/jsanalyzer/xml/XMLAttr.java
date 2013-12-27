package jp.mzw.jsanalyzer.xml;

public class XMLAttr {

	/**
	 * Rule notations related to event
	 */
	public static final String
		RuleEvent 						= "event",
		RuleEventModifier 				= "event_modifier",
		RuleEventModifier_AfterMilliSec = "after_msec",
		RuleEventModifier_UserClick		= "user_click";
	
	/**
	 * Rule notations related to interaction
	 */
	public static final String
		RuleInteract 		= "interact",
		RuleInteract_User	= "User",
		RuleInteract_Server	= "Server",
		RuleIntreact_Self	= "Self";
	
	public static final String RuleFunc 			= "func";
	public static final String RuleCallback 		= "callback";
	
	public static final String RuleLang				= "lang";
	public static final String RuleLang_CSS			= "css";
	public static final String RuleLang_HTML		= "html";
	public static final String RuleLang_JS			= "js";
	
	public static final String RuleAttr				= "attr";
	public static final String
		RuleProp 			= "prop",
		RuleProp_PropTarget = "PropTarget",
		RuleArgNum			= "arg_";
	
	public static final String RuleRepeatable		= "repeatable";
	public static final String RuleRepeatable_False	= "false"; // not-false and no description: true
	
	public static final String RuleDisabled			= "disabled";
	public static final String RuleCond				= "cond";
	
	public static final String
		RuleSemantic 		= "semantic",
		RuleSemantic_Get 	= "get",
		RuleSemantic_Set 	= "set",
		RuleSemantic_Append = "append",
		RuleSemantic_Remove 	= "remove";
	
	
	public static final String
		RuleBy			= "by",
		RuleBy_CSSQuery	= "CSSQuery";
	
	public static final String RuleValue			= "value";
	public static final String RuleTarget			= "target";
	
	public static final String RulePath				= "path";
	public static final String RuleSkip				= "skip";

	public static final String RuleName				= "name";
	public static final String RuleMethod			= "method";
	public static final String RuleMethod_StartsWith = "startsWith";
	public static final String RuleMethod_Containts = "containts";
	public static final String RuleMethod_Regex 	= "regex";
	

}
