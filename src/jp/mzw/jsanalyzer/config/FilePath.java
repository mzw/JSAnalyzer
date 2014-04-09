package jp.mzw.jsanalyzer.config;

public class FilePath {
	/**
	 * Under your project directory
	 */
	public static final String RawSrcDir 			= "/wget_raw_src";
	public static final String ModelDir				= "/model";
	public static final String VerifyResult		 	= "/verify";
	public static final String Counterexample	 	= "/counterexample";
	public static final String Serialized		 	= "/serialized";
	public static final String LimitationDir 		= "/limitation";
	public static final String ExternsDir			= "res/externs";
	
	/**
	 * Under public html directory
	 */
	public static final String IADPPath 		 		= "/research/ex/iadp";
	public static final String IADPHome			 		= System.getProperty("user.home") + "/Sites" + FilePath.IADPPath; // If Linux, replace "Sites" with "public_html"
	public static final String IADPRepositry	 		= FilePath.IADPHome + "/files";
	public static final String IADPResult	 			= FilePath.IADPHome + "/result";
	/**
	 * HTTP path
	 */
	public static final String HttpServerPath		= "http://localhost/~" + System.getProperty("user.name");
	public static final String IADPRepositoryHttp	= FilePath.HttpServerPath + FilePath.IADPPath;
	public static final String IADPResultHttp		= FilePath.HttpServerPath + FilePath.IADPPath + "/result";

	/**
	 * Filenames
	 */
	public static final String IADPPropListFilename				= "properties" + FileExtension.XML;
	public static final String VerifyResultsSkeletonHTML		= "res/skeleton/" + "verify.results.skeleton" + FileExtension.HTML;
	public static final String ViewCounterexampleSkeletonPHP	= "res/skeleton/" + "view.counterexample.skeleton" + FileExtension.PHP;
	
	public static final String ExtendedCallGraphDot				= "xcg" + FileExtension.Dot;
	public static final String AbstractedCallGraphDot 			= "acg" + FileExtension.Dot;
	public static final String ExtractedFiniteStateMachineDot 	= "fsm" + FileExtension.Dot;
	public static final String ExtractedFiniteStateMachine 		= "fsm" + FileExtension.FiniteStateMachine;
	
	
	
}
