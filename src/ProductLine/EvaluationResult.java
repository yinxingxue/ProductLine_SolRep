package ProductLine;

import java.util.ArrayList;

public class EvaluationResult implements java.io.Serializable {
	public String fileName;
	public boolean FeedbackDirected;
	public double MutationRate;
	public double MaxEvaluation;
	public double paramPercentageOfCorrectness;
	public double HV;// ok
	public double Spread;// ok
	public double PercentageOfCorrectness;// ok
	public double timeTo50PercentCorrectness;
	public double evaluationTo50PercentCorrectness;
	

	public double cacheMissed;

	public long TimeElapsed;// ok
	public boolean isCache;
	public double NumOfEvaluations;	
	public double errorMutationRate;
	public double crossOverRate;
	public double ArchiveSize;
	public double PopulationSize;
	public double GenomeSize;
	public boolean UsedCached;
	public boolean filterMandatory;
	public boolean setInitialPopulation;
	
	public ArrayList<double[]> ParetoFront;// ok
	public ArrayList<GAParams.Objective> objs;// ok

	//
	

	public EvaluationResult() {

	}

}
