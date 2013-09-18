package Analyzer;

public class Config {
	public static final String version = "0.3.0";
	
	// commands
	public static final String spin				= "/opt/local/bin/spin";
	public static final String gcc				= "/usr/bin/gcc";
	public static final String dot				= "/usr/local/bin/dot";
	
	
	// XML tags
	public static final String StateMachineTag 	= "StateMachine";
	public static final String stateTag 		= "State";
	public static final String transTag 		= "Transition";
	public static final String entryTag			= "Entry";
	public static final String exitTag 			= "Exit";
	

	// file extensions
	public static final String EXT_StateMachine	= ".sm";
	public static final String EXT_CallGraph	= ".cg";
	public static final String EXT_Promela		= ".pml";
	public static final String EXT_Spec			= ".spec";
	public static final String EXT_LTLProperty	= ".prp";
	public static final String EXT_NeverClaim	= ".ltl";
	public static final String EXT_SpinResult	= ".result";
	public static final String EXT_Trail		= ".trail";
	public static final String EXT_TrailResult	= ".trail_result";
	public static final String EXT_Path			= ".path.sm";
	public static final String EXT_SpinResult_HTML	= ".result.html";
	public static final String EXT_Dot			= ".dot";

	// Reserved keywords
	public static final String Lib_StartsWith 	= "startsWith(";
	
}
