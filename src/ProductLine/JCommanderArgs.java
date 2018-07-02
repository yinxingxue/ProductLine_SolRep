package ProductLine;

import com.beust.jcommander.Parameter;

public class JCommanderArgs {
	 @Parameter(names ={ "--evalNum" }, description = "Number of evaluations")
	 public int evaluationNum;
	 
	 @Parameter(names = { "--errMutProb" }, description = "Error mutation probability")
	 public double mutation;
	 	 
	 @Parameter(names = { "--mutProb" }, description = "mutation probability")
	 public double errmutation;
	
	 @Parameter(names = { "--dimacs" }, description = "File path of the dimacs model")
	 public String dimacsFile;
	 
	 @Parameter(names = { "--sxfm" }, description = "File path of the sxfm model")
	 public String sxfmFile;
	 
	 @Parameter(names = { "--common" }, description = "File path of common features")
	 public String commonFeatureFile;
	 
	 @Parameter(names = { "--dead" }, description = "File path of dead features")
	 public String deadFeatureFile;
	 
	 @Parameter(names = { "--exp" }, description = "Experiments on the paper")
	 public String experimentName;
	 
}
