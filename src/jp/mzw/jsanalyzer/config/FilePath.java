package jp.mzw.jsanalyzer.config;

public class FilePath {
	public static final String RawSrcDir 		= "/wget_raw_src";
	public static final String ExtractResult 	= "/extract";
	public static final String VerifyResult	 	= "/verify";
	
	public static final String ModelDir						= "/model";
	public static final String ExtendedCallGraphDot				= "xcg" + FileExtension.Dot;
	public static final String AbstractedCallGraphDot 			= "acg" + FileExtension.Dot;
	public static final String ExtractedFiniteStateMachineDot 	= "fsm" + FileExtension.Dot;
	public static final String ExtractedFiniteStateMachine 		= "fsm" + FileExtension.FiniteStateMachine;
	
	public static final String ExternsDir		= "res/externs";
}
