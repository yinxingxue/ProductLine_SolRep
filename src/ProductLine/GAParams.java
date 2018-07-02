//
// Translated by CS2J (http://www.cs2j.com): 1/3/2014 9:42:00 PM
//

package ProductLine;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;
import jmetal.util.comparators.AProductLineObjectiveComparator;
import jmetal.util.wrapper.XInt;
import ProductLine.BestFix.EditDistance;
import ProductLine.BestFix.Gene;
import ProductLine.BestFix.IntArray;
import ProductLine.BestFix.LexicalDouble;
import ProductLine.FeatureModel.Feature;
import ProductLine.FeatureModel.FeatureModel;
import ProductLine.FeatureModel.FeatureModelAnalyzer;
import ProductLine.FeatureModel.GeneErrorInfo;
import ProductLine.FeatureModel.LogicFeatureModel;
import ProductLine.FeatureModel.SelectedFeature;
import ProductLine.LogicFormula.And;
import ProductLine.LogicFormula.Constraint;
import ProductLine.LogicFormula.LogicFormula;
import ProductLine.LogicFormula.Prop;

public class GAParams {
	/**
	 * =====To change:===== 1. Parameters (e.g., MutationRate, etc.) 2.
	 * FitnessFunction 3. GeneComparer
	 */
	
	public static HashMap<String, Boolean> LinuxSeed=new LinkedHashMap<String, Boolean>();
	
	// To be initialized
	public static ArrayList<Constraint> constraints = new ArrayList<Constraint>();
	public static ArrayList<ProductLine.LogicFormula.LogicFormula> formulas = new ArrayList<ProductLine.LogicFormula.LogicFormula>();
	public static HashMap<String, Integer> featuresToIntegerMap = new HashMap<String, Integer>();
	public static HashMap<Integer, String> IntegerToFeatureMap = new HashMap<Integer, String>();
	public static HashMap<String, Integer> AllFeaturesToIntegerMap = new HashMap<String, Integer>();
	public static HashMap<Integer, String> IntegerToAllFeatureMap = new HashMap<Integer, String>();
	/**
	 * OldFeature is previously use for minimal fixing, not used for feedback
	 * directed EA
	 **/
	public static LinkedHashSet<String> OldFeatures = new LinkedHashSet<String>();
	public static LinkedHashSet<String> MustNotInFeatures = new LinkedHashSet<String>();
	public static LinkedHashSet<Integer> MustNotInFeaturesI = new LinkedHashSet<Integer>();
	public static LinkedHashSet<String> MandatoryFeatures = new LinkedHashSet<String>();
	public static LinkedHashSet<Integer> MandatoryFeaturesI = new LinkedHashSet<Integer>();
	public static LinkedHashSet<String> OptionalFeatures = new LinkedHashSet<String>();

	public static FeatureModel newFeatureModel = null;
	public static Gene oldGenes = null;
	//public static Logger logger = Logger.getLogger("ProductLine");
	public static FileWriter logger;
	
	// Quality Attribute
	
	public static HashMap<Integer, Boolean> featuresToNotUseBeforeMap = new HashMap<Integer, Boolean>();
	public static HashMap<Integer, Integer> featuresToDefectsMap = new HashMap<Integer, Integer>();
	public static HashMap<Integer, Double> featuresToCostMap = new HashMap<Integer, Double>();

	public enum  CaseStudy{
		EShop, WebPortal, ChatSystem, ECos, UCLinux, LinuxX86, FreeBSD, Fiasco, Other
	};

	//
	public enum Objective {
		Correctness, MaximalFeature, NotUsedBefore, Defects, Cost, MinimalFeature, // MinimalDistance,
	};
	public static ArrayList<CaseStudy> caseStudies = new ArrayList(
	Arrays.asList(CaseStudy.LinuxX86));
	//CaseStudy.LinuxX86	
	//CaseStudy.FreeBSD, 
	
//	public static ArrayList<CaseStudy> caseStudies = new ArrayList(
//			Arrays.asList(CaseStudy.UCLinux));
	 
	
	public static final boolean temp=true;
	
	public static CaseStudy currCaseStudy=CaseStudy.LinuxX86;
	
	// record the time and evaluation to 50PercentCorrectness
	public static double timeTo50PercentCorrectness=-1;
	public static double evaluationTo50PercentCorrectness=-1;
	
	// mmToChange
	public static int MaxEvaluation = 25000;// 25000;// 25000
	public static boolean P_feedbackDirected = true;
	public static boolean P_logSolution = true;
	public static boolean P_useSeed=false;
	
	/** Is there additional testing in EvaluationDriver **/
	public static boolean P_testExtra=false;

	public static ArrayList<Objective> allObjectives = new ArrayList(Arrays.asList(Objective.Correctness,
			Objective.MaximalFeature, Objective.NotUsedBefore, Objective.Defects, Objective.Cost));
	public static ArrayList<Objective> objectives = new ArrayList(Arrays.asList(Objective.Correctness,
			Objective.MaximalFeature, Objective.NotUsedBefore, Objective.Defects, Objective.Cost));
			 
			 
			 
	/** This is for command line usage **/
	public static JCommanderArgs jCommanderArgs=null;
//	public static String noCommandDeadFile=".";
	
	

	
	/** Setting all 0 and all 1 for initial population **/
	public static boolean P_setInitialPopulation = false;	
	


	public static int cacheMissed = 0;	
	public static boolean P_filterMandatory = true;
	public static boolean P_useCached = true;	
	public static boolean P_usingOptimalOnly = false;
	public static boolean P_addBackMandatory = true;
	

	/** Is the run for percent of correctness,
	 *  i.e., the EA stop when percent of correctness larger-1 if not **/
	public static  double P_percentageOfCorrectness=-1;//0.15;
	/** record current percentage **/
	public static double percentageOfCorrectness=0;
	
	

	
	/** Is current run generating seed **/
	public static final boolean P_SeedMode = true;
	public static HashSet<String> visitedSeed = new HashSet<String>();
	public static long seedStartTime = System.currentTimeMillis();
	
	/**
	 * parameters (whether use integer to represents variable (true) or use
	 * string to represents variable (false))
	 **/
	public static boolean P_useIntegerVar = false;

	
	// public static ArrayList<Objective> objectives = new ArrayList(
	// Arrays.asList(Objective.Correctness, Objective.MinimalFeature));

	
//	public static ArrayList<Objective> allObjectives = new ArrayList(
//			Arrays.asList(Objective.Correctness));
//	public static ArrayList<Objective> objectives = new ArrayList(
//			Arrays.asList(Objective.Correctness));

	public static String FunPath = "FUN";
	//public static String FunPath = "C:\\work\\SPLJava\\ProductLine\\FUN";

	public static EvaluationResult EvaluationResult = new EvaluationResult();




	/**
	 * If have five objectives then have five values, then mandatoryValues has
	 * five elements Each element is for the accumulative objective values for
	 * mandatory elements Later Add back when calculating hypervolume or spread
	 **/
	public static ArrayList<Double> mandatoryValues = new ArrayList<Double>();
	// public static ArrayList<Objective> objectives = new ArrayList(
	// Arrays.asList(Objective.Correctness, Objective.MaximalFeature));
	// Arrays.asList(Objective.Correctness, Objective.MaximalFeature));

	// public static ArrayList<CaseStudy> caseStudies = new ArrayList(
	// Arrays.asList(CaseStudy.EShop, CaseStudy.WebPortal,
	// CaseStudy.ChatSystem, CaseStudy.ECos, CaseStudy.UCLinux,
	// CaseStudy.LinuxX86));



	// public static boolean P_useConstraint = false;

	public static final double minCost = 5.0;
	public static final double maxCost = 15.0;
	public static final int minDefect = 0;
	public static final int maxDefect=10;

	// Cached items
	public static HashMap<IntArray, GeneErrorInfo> GeneToViolatedFeatureMap = new HashMap<IntArray, GeneErrorInfo>();

	/*-1 means not using ErrorChangeRate, other values means using*/
//	public static double ErrorChangeRate=-1;//0.01
	
	public static double MutationRate =0.0000001;// 0.0000001;// 0.01// 0.05;// 0.05
	public static double CrossoverRate =0.1;// 0.0001;//0.1// 0.95;// 0.80

	public static double ErrorMutationRate = P_feedbackDirected ? 1 : 0.05;// 0.95
	public static boolean P_useBestCrossover = P_feedbackDirected;

	// For GA
	public static int GAPopulationSize = 200;// 200
	public static int GenerationSize = 1000;// 1000

	// For IBEA
	public static int ArchiveSize = 100;// 100
	public static int IBEAPopulationSize = 100;// 100

	// For Linux X86, it does not matter on the constraints (350k), since the
	// size here refer to the size of population
	public static int MaxCacheSize = 8000;

	public static int GenomeSize;
	public static Random rand = new Random();
	
	
//	public static final double P_ConstraintIncrementSize=1000000;
//	public static double constraintCurrentCnt=0;

	/**
	 * Special init means to create initial population using max, min, avg, etc.
	 **/
	public static boolean UseCustomizedInitPopulation_ForGAOnly = false;
	// length of the gene
	public static boolean Elitism_ForGAOnly = true;
	
//	public static int stagnantViolationCnt=0;
//	public static int stagnantViolatedMin=Integer.MAX_VALUE;
//	public static int stagnantTryIndex=0;
//	public static double[] stagnantTryValues= {0.005,-1};
//	public static double[] stagnantTryValues1= {0.0000001,0.0000001};

	public static void main(String[] args) throws JMException, IOException,
			ClassNotFoundException, Exception {

	}

	public static double[][] addBackMandatory(double[][] input) {
		double[][] output = new double[input.length][];

		for (int i = 0; i < input.length; i++) {
			output[i] = new double[input[i].length];
			for (int j = 0; j < input[i].length; j++) {
				output[i][j] = input[i][j]+GAParams.mandatoryValues.get(j);
				
			}
		}
		return output;
	}

	public static double[][] removeNonCorrect(double[][] input) {

		ArrayList<double[]> output = new ArrayList<double[]>();

		for (int i = 0; i < input.length; i++) {
			double[] output1 = new double[input[i].length];
			//if (input[i][0] == 0.0) {
				for (int j = 0; j < input[i].length; j++) {

					output1[j] = input[i][j];

				}
				output.add(output1);
			//}
		}
		double[][] arr = new double[output.size()][];
		for (int i = 0; i < output.size(); i++) {
			arr[i] = output.get(i);
		}
		return arr;
	}

	public static void setSeed(int i, int populationSize,
			Solution newSolution) throws JMException {
		if (i==0 && GAParams.currCaseStudy==GAParams.CaseStudy.LinuxX86) {
			// if(i==0)
			for (int j = 0; j < newSolution.numberOfVariables(); j++) {
				String feature=IntegerToFeatureMap.get(j);
				if(!LinuxSeed.containsKey(feature)) {
					System.out.println(feature+" not exist");
				}
				int value=LinuxSeed.get(feature)?1:0;
				newSolution.getDecisionVariables()[j].setValue(value);
			}
		}
	}
	
	public static void setIBEASeed(int i, int populationSize,
			Solution newSolution) throws Exception {
		if (i==1 && GAParams.currCaseStudy==GAParams.CaseStudy.LinuxX86) {
			// if(i==0)
			HashMap<String, Boolean> thisLinuxSeed=new LinkedHashMap<String, Boolean>();
			thisLinuxSeed = Utility.readSeed("data/Seed_LinuxX86_IBEA.txt");
			for (int j = 0; j < newSolution.numberOfVariables(); j++) {
				String feature=IntegerToFeatureMap.get(j);
				if(!thisLinuxSeed.containsKey(feature)) {
					System.out.println(feature+" not exist");
				}
				int value=thisLinuxSeed.get(feature)?1:0;
				newSolution.getDecisionVariables()[j].setValue(value);
			}
		}
	}
	
	public static void setIBDESeed(int i, int populationSize,
			Solution newSolution) throws Exception {
		if (i==2 && GAParams.currCaseStudy==GAParams.CaseStudy.LinuxX86) {
			// if(i==0)
			HashMap<String, Boolean> thisLinuxSeed=new LinkedHashMap<String, Boolean>();
			thisLinuxSeed= Utility.readSeed("data/Seed_LinuxX86_IBDE.txt");
			for (int j = 0; j < newSolution.numberOfVariables(); j++) {
				String feature=IntegerToFeatureMap.get(j);
				if(!thisLinuxSeed.containsKey(feature)) {
					System.out.println(feature+" not exist");
				}
				int value=thisLinuxSeed.get(feature)?1:0;
				newSolution.getDecisionVariables()[j].setValue(value);
			}
		}
	}
	
	
	public static void setInitialSolution(int i, int populationSize, Solution newSolution) throws JMException {
		try {
			if (currCaseStudy != CaseStudy.LinuxX86 || !P_useSeed || !P_SeedMode) {
				randInitializeSol(i, populationSize, newSolution);
			} else if (currCaseStudy == CaseStudy.LinuxX86 || P_useSeed || P_SeedMode) {
				if (i == 0)
					setSeed(i, populationSize, newSolution);
				else if (i == 1)
					setIBEASeed(i, populationSize, newSolution);
				else if (i == 2)
					setIBDESeed(i, populationSize, newSolution);
				else
					randInitializeSol(i, populationSize, newSolution);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void randInitializeSol(int i, int populationSize, Solution newSolution) throws JMException {
		if (i < populationSize / 4) {
			// if(i==0)
			for (int j = 0; j < newSolution.numberOfVariables(); j++) {
				newSolution.getDecisionVariables()[j].setValue(0);
			}
		} else if (i >= populationSize / 4 && i < populationSize / 2) {
			// else if(i==1) {
			for (int j = 0; j < newSolution.numberOfVariables(); j++) {
				newSolution.getDecisionVariables()[j].setValue(1);
			}
		}
	}

	public static GeneErrorInfo errorPosition(int[] gene) {
		GeneErrorInfo geneErrorInfo = null;
		LinkedHashSet<Integer> violatedPropsInt = null;
		ArrayList<Integer> violatedFormula=new ArrayList<Integer>();
		int violatedCnt = 0;
		if (P_useCached)
			geneErrorInfo = GeneToViolatedFeatureMap.get(new IntArray(gene));

		if (geneErrorInfo == null) {
			if (P_useCached)
				cacheMissed++;
			violatedPropsInt = new LinkedHashSet<Integer>();

			if (!P_useIntegerVar) {
				HashMap<String, Boolean> ValueMapping = new HashMap<String, Boolean>();
				for (Entry<String, Integer> i : featuresToIntegerMap.entrySet()) {
					ValueMapping.put(i.getKey(),
							gene[i.getValue()] == 0 ? false : true);
				}
				for (String feature : MandatoryFeatures) {
					ValueMapping.put(feature, true);
				}
				for (String feature : MustNotInFeatures) {
					ValueMapping.put(feature, false);
				}
				LinkedHashSet<String> violatedProps = new LinkedHashSet<String>();
			

//				 for (LogicFormula logicFormula : formulas) {
//				 if (!logicFormula.evaluation(ValueMapping)) {
//				 violatedCnt++;
//				 violatedProps.addAll(logicFormula.getAllProps());
//				 }
//				
//				 
//				 for (String violatedProp : violatedProps) {
//				 violatedPropsInt.add(featuresToIntegerMap
//				 .get(violatedProp));
//				 }
				 
				//for (Constraint constraint : constraints) {
				for(int i=0;i<formulas.size();i++) {					
					Constraint constraint=constraints.get(i);
					if (!constraint.evaluation(ValueMapping)) {
						violatedCnt++;
						violatedPropsInt.addAll(constraint.getAllAtoms());
						violatedFormula.add(i);
					}

				}

				// } else {
				// for (int i = 0; i < formulas.size(); i++) {
				// if (!formulas.get(i).evaluation(ValueMapping)) {
				// violatedPropsInt.add(i);
				// }
				// }
				// }

			} else {

				HashMap<Integer, Boolean> ValueMapping = new HashMap<Integer, Boolean>();

				for (int i = 0; i < GAParams.GenomeSize; i++) {
					ValueMapping.put(i, gene[i] == 0 ? false : true);
				}

				for (Integer feature : MandatoryFeaturesI) {
					ValueMapping.put(feature, true);
				}

				for (Integer feature : MustNotInFeaturesI) {
					ValueMapping.put(feature, false);
				}
				
				
				for (int i = 0; i <formulas.size(); i++) {
					if (!formulas.get(i).evaluationI(ValueMapping)) {
						violatedCnt++;
						// if (!GAParams.P_useConstraint) {
						violatedPropsInt.addAll(formulas.get(i).getAllPropsI());
						violatedFormula.add(i);
						// } else {
						// violatedPropsInt.add(i);
						// }
					}

				}
			}

			geneErrorInfo = new GeneErrorInfo(violatedFormula,violatedPropsInt, violatedCnt);
			if (P_useCached) {
				if (GeneToViolatedFeatureMap.size() > GAParams.MaxCacheSize) {
					GeneToViolatedFeatureMap.clear();
				}
				GeneToViolatedFeatureMap.put(new IntArray(gene), geneErrorInfo);
			}
		}
		// System.out.println("size="+GeneToViolatedFeatureMap.size());
		// System.out.flush();
		return geneErrorInfo;
	}

	public static double PrintResult(SolutionSet population, long estimatedTime)
			throws JMException {

		EvaluationResult.TimeElapsed = estimatedTime;

		Solution bestSolution = population
				.best(new AProductLineObjectiveComparator());

		int[] bestGene = GAParams.GetGene(bestSolution);

		System.out.println("===Listing Old Features====");
		for (String feature : GAParams.OldFeatures) {
			System.out.print(feature + ",");
		}
		System.out.println();
		System.out.println("===Listing Mandatory Features====");
		System.out.println("#mandatoryItems="
				+ GAParams.MandatoryFeatures.size());
		for (String feature : GAParams.MandatoryFeatures) {
			System.out.print(feature + ",");
		}
		System.out.println();
		System.out.println("===Listing Must Not In Features====");
		System.out.println("#notInItems=" + GAParams.MustNotInFeatures.size());
		for (String feature : GAParams.MustNotInFeatures) {
			System.out.print(feature + ",");
		}
		System.out.println();
		for (int i = 0; i < bestGene.length; i++) {
			if (bestGene[i] == 1) {
				String feature = GAParams.IntegerToFeatureMap.get(i);
				System.out.print(feature + ",");
			}
		}

		System.out.println();

		System.out.println("MissedCache=" + GAParams.cacheMissed);

		// int cnt = 0;

		// for (int i = 0; i < GAParams.objectives.size(); i++) {
		// if (GAParams.objectives.get(i) == GAParams.Objective.Correctness) {
		// System.out.print("valid=");
		// System.out.println(bestSolution.getObjective(i) == 0 ? "true"
		// : "false");
		// } else if (GAParams.objectives.get(i) ==
		// GAParams.Objective.MinimalDistance) {
		// System.out.print("minDistance(not counting add/deleting of mandatory set)=");
		// System.out.println(bestSolution.getObjective(i));
		// } else if (GAParams.objectives.get(i) ==
		// GAParams.Objective.MaximalFeature) {
		// System.out.print("maxFeature=");
		// System.out.println(0-(bestSolution.getObjective(i)-GAParams.GenomeSize));
		// } else if (GAParams.objectives.get(i) ==
		// GAParams.Objective.MinimalFeature) {
		// System.out.print("minFeature=");
		// System.out.println(bestSolution.getObjective(i));
		// }
		// }
		// System.out.println();

		// Print the results
		System.out.println("Total execution time: " + estimatedTime + "ms");
		return estimatedTime*1.0/1000;
	}

	public static double GetObjective(Solution solution, Objective obj) {
		for (int i = 0; i < GAParams.objectives.size(); i++) {
			double res = 0;
			if (GAParams.objectives.get(i) == obj) {
				return solution.getObjective(i);
			}
		}
		return -1;
	}

	public static double[] GetObjectives(Solution solution) throws JMException {
		double[] objs = new double[GAParams.objectives.size()];
		for (int i = 0; i < objs.length; i++) {
			objs[i] = solution.getObjective(i);
		}
		return objs;
	}

	public static int[] GetGene(Solution solution) throws JMException {
		XInt vars = new XInt(solution);
		
		int[] x = new int[GAParams.featuresToIntegerMap.size()];
		for (int i = 0; i < GAParams.featuresToIntegerMap.size(); i++)
			x[i] = vars.getValue(i);

		return x;

	}

	public static boolean isValidFeatures(int[] gene) {
		return errorPosition(gene).getViolatedPropsInt().size() == 0;
	}

	// public static string DoubleArrToString(int[] d)
	// {
	// StringBuilder sb = new StringBuilder();
	// for (int i = 0; i < d.Length; i++)
	// {
	// sb.Append(d[i]);
	// sb.Append(",");
	// }
	// return sb.ToString();
	// }
	public static double fitnessFunction(int[] gene) throws Exception {
		double distance = EditDistance
				.levenshteinDistance(oldGenes.Genes, gene);
		double distanceValue = (GenomeSize - distance) / GenomeSize;
		if (isValidFeatures(gene)) {
			return 0.5 + 0.5 * distanceValue;
		} else {
			return 0.5 * distanceValue;
		}
	}

	// Test Functionality Correct
	// Test Non-Functional Optimality
	// if (values.GetLength(0) != 2)
	// throw new ArgumentOutOfRangeException("should only have 2 args");
	// double x = values[0];
	// double y = values[1];
	// double n = 9; // should be an int, but I don't want to waste time
	// casting.
	// double f1 = Math.Pow(15 * x * y * (1 - x) * (1 - y) * Math.Sin(n *
	// Math.PI * x) * Math.Sin(n * Math.PI * y), 2);
	// return f1;
	public static LinkedHashSet<String> getFeatures(int[] genes)
			throws Exception {
		LinkedHashSet<String> features = new LinkedHashSet<String>();
		for (int i = 0; i < genes.length; i++) {
			if (genes[i] > 0) {
				features.add(IntegerToFeatureMap.get(i));
			}
		}
		return features;
	}

	public static void initialize(FeatureModel fm, LinkedHashSet<String> oldFea)
			throws Exception {
		//SimpleFormatter sformatter = new SimpleFormatter();
		//FileHandler fileHandler = new FileHandler("ProductLine.log");
		//fileHandler.setFormatter(sformatter);
		//logger.addHandler(fileHandler);
		//logger.setUseParentHandlers(false);
		logger = new FileWriter("ProductLine.log",false);

		
		// Initialize
		timeTo50PercentCorrectness=-1;
		evaluationTo50PercentCorrectness=-1;
		cacheMissed = 0;
		percentageOfCorrectness=0;
		GeneToViolatedFeatureMap = new HashMap<IntArray, GeneErrorInfo>();
		formulas = new ArrayList<ProductLine.LogicFormula.LogicFormula>();
		constraints = new ArrayList<Constraint>();
		featuresToIntegerMap = new HashMap<String, Integer>();
		IntegerToFeatureMap = new HashMap<Integer, String>();
		AllFeaturesToIntegerMap = new HashMap<String, Integer>();
		IntegerToAllFeatureMap = new HashMap<Integer, String>();
		OldFeatures = new LinkedHashSet<String>();
		MustNotInFeatures = new LinkedHashSet<String>();
		MandatoryFeatures = new LinkedHashSet<String>();
		MandatoryFeaturesI = new LinkedHashSet<Integer>();
		visitedSeed = new HashSet<String>();
		// OptionalFeatures = new LinkedHashSet<String>();
		// newFeatureModel = null;
		oldGenes = null;
		featuresToCostMap = new HashMap<Integer, Double>();
		featuresToDefectsMap = new HashMap<Integer, Integer>();
		featuresToNotUseBeforeMap = new HashMap<Integer, Boolean>();
	
		if(currCaseStudy==CaseStudy.LinuxX86) {
			if(LinuxSeed.size()==0) {
				LinuxSeed=Utility.readSeed("data/Seed_LinuxX86.txt");
			}
		}
		// GeneToViolatedFeatureMap = new HashMap<IntArray,
		// LinkedHashSet<Integer>>();

		if (fm instanceof LogicFeatureModel) {
			LogicFeatureModel lfm = (LogicFeatureModel) fm;
			formulas.addAll(lfm.getLogicFormula());
			if (P_filterMandatory) {
				MandatoryFeatures.addAll(lfm.getMandatoryNames());
				MustNotInFeatures.addAll(lfm.getMustNotInNames());
			}
		} else {// if(fm!=null) {
			newFeatureModel = fm;
			newFeatureModel.convertToLogicFormula();
			LogicFormula newFeatureFormula = fm.getCombinedLogicFormular();
			flattenAnd(newFeatureFormula, formulas);
			formulas.add(new Prop(fm.getRootFeature().getName()));
			FeatureModelAnalyzer fma = new FeatureModelAnalyzer(fm);
			for (Feature f : fma.getMinMandatoryFeatureSet()) {
				if (P_filterMandatory) {
					MandatoryFeatures.add(f.getName());
				}
				// i_MandatoryFeatures.add(f.getName());
			}
		}

		// LinkedHashSet<String> i_MandatoryFeatures = new
		// LinkedHashSet<String>();
		// for (Feature f : fma.getMinMFOptChldSet()) {
		// OptionalFeatures.add(f.getName());
		// }

		int cnt = 0;
		LinkedHashSet<String> allNewFeatures = new LinkedHashSet<String>();

		ArrayList<String> features = new ArrayList<String>();
		if (fm instanceof LogicFeatureModel) {
			LogicFeatureModel lfm = (LogicFeatureModel) fm;
			features.addAll(lfm.getFeatureNames());
		} else {
			LinkedHashSet<Feature> feas = newFeatureModel.getRootFeature()
					.getSubFeatures(100);
			for (Feature f : feas) {
				features.add(f.getName());
			}
		}

		// for (Feature f :
		// newFeatureModel.getRootFeature().getSubFeatures(100)) {
		for (String featureName : features) {
			// String featureName = f;
			allNewFeatures.add(featureName);
			if (!MandatoryFeatures.contains(featureName)
					&& !MustNotInFeatures.contains(featureName)) {
				AllFeaturesToIntegerMap.put(featureName, cnt);
				IntegerToAllFeatureMap.put(cnt, featureName);
				featuresToIntegerMap.put(featureName, cnt);
				IntegerToFeatureMap.put(cnt, featureName);
//				System.out.println(cnt+"="+featureName);
//				
//
//				// Generate Quality Attribute
//				featuresToCostMap.put(cnt, genRandomDouble(minCost, maxCost));
//				boolean notUsed = rand.nextBoolean();
//				// if(i_MandatoryFeatures.contains(featureName)) {
//				// notUsed=false;
//				// }
//				//
//				featuresToNotUseBeforeMap.put(cnt, notUsed);
//				if (notUsed) {
//					featuresToDefectsMap.put(cnt, 0);
//				} else {
//					featuresToDefectsMap.put(cnt,
//							getRandIntWithBinomial(maxDefect));
//				}
				cnt++;
			}
		}

		// ==populate constraints==
		for (LogicFormula formula : formulas) {
			ArrayList<Integer> props = new ArrayList<Integer>();
			for (String violatedProp : formula.getAllProps()) {
				props.add(featuresToIntegerMap.get(violatedProp));
			}
			constraints.add(new Constraint(formula, props));
		}
    	// ==populate constraints==
		GenomeSize = IntegerToFeatureMap.size();

		// Add the mandatoryFeature to the mapping
		for (String f : MandatoryFeatures) {
			AllFeaturesToIntegerMap.put(f, cnt);
			IntegerToAllFeatureMap.put(cnt, f);
			MandatoryFeaturesI.add(cnt++);
		}

		for (String f : MustNotInFeatures) {
			//System.out.println(cnt+"="+f);
			AllFeaturesToIntegerMap.put(f, cnt);
			IntegerToAllFeatureMap.put(cnt, f);
			MustNotInFeaturesI.add(cnt++);
		}

		//add attribute
		//we need a factory class
		AttributeGenerator factory = new AttributeGenerator(fm,AllFeaturesToIntegerMap,IntegerToAllFeatureMap);
		factory.generate(featuresToCostMap,featuresToDefectsMap,featuresToNotUseBeforeMap);


//		// populate mandatory Values
//		if (P_filterMandatory) {
//			for (int i = 0; i < objectives.size(); i++) {
//				double count = 0;
//				if (objectives.get(i) == Objective.Cost
//						|| objectives.get(i) == Objective.Defects
//						|| objectives.get(i) == Objective.NotUsedBefore) {
//
//					for (String j : MandatoryFeatures) {
//						// int currIndex=AllFeaturesToIntegerMap.get(j);
//						if (objectives.get(i) == Objective.Cost) {
//							count += genRandomDouble(minCost, maxCost);
//						} else if (objectives.get(i) == Objective.Defects) {
//							count += getRandIntWithBinomial(maxDefect);
//						} else if (objectives.get(i) == Objective.NotUsedBefore) {
//							count += rand.nextBoolean() ? 1.0 : 0.0;
//						}
//
//					}
//				}
//				mandatoryValues.add(i, count);
//			}
//		}
		
		// populate mandatory Values
		if (P_filterMandatory) {
			for (int i = 0; i < objectives.size(); i++) {
				double count = 0;
				if (objectives.get(i) == Objective.Cost
						|| objectives.get(i) == Objective.Defects
						|| objectives.get(i) == Objective.NotUsedBefore) {

					for (String j : MandatoryFeatures) {
						// int currIndex=AllFeaturesToIntegerMap.get(j);
						if (objectives.get(i) == Objective.Cost) {
							count += featuresToCostMap.get(AllFeaturesToIntegerMap.get(j));
						} else if (objectives.get(i) == Objective.Defects) {
							count += featuresToDefectsMap.get(AllFeaturesToIntegerMap.get(j));
						} else if (objectives.get(i) == Objective.NotUsedBefore) {
							count += featuresToNotUseBeforeMap.get(AllFeaturesToIntegerMap.get(j))? 1.0 : 0.0;
						}

					}
				}
				mandatoryValues.add(i, count);
			}
		}
		
		LinkedHashSet<String> intersection = new LinkedHashSet<String>(oldFea);
		intersection.retainAll(allNewFeatures);
		OldFeatures = intersection;
		Gene g = new Gene(GenomeSize);
		g.createGenes(OldFeatures);
		oldGenes = g;

		
	    fm.convertToInequations(IntegerToAllFeatureMap,featuresToCostMap,featuresToDefectsMap,featuresToNotUseBeforeMap);
		StringBuilder sb = new StringBuilder();
		sb.append("Mandatory Feature Size=" + MandatoryFeatures.size() + "\n");
		for (String fea : MandatoryFeatures) {
			sb.append(fea + ",");
		}
		sb.append("\nMustNotIn Feature Size=" + MustNotInFeatures.size() + "\n");
		for (String fea : MustNotInFeatures) {
			sb.append(fea + ",");
		}
		sb.append("\nCost\n");
		for (int i = 0; i < featuresToCostMap.size(); i++) {
			sb.append(IntegerToAllFeatureMap.get(i) + "="
					+ featuresToCostMap.get(i) + ",");
		}
		sb.append("\nDefects\n");
		for (int i = 0; i < featuresToDefectsMap.size(); i++) {
			sb.append(IntegerToAllFeatureMap.get(i) + "="
					+ featuresToDefectsMap.get(i) + ",");
		}
		sb.append("\nNotUsedBefore\n");
		for (int i = 0; i < featuresToNotUseBeforeMap.size(); i++) {
			sb.append(IntegerToAllFeatureMap.get(i) + "="
					+ featuresToNotUseBeforeMap.get(i) + ",");
		}
		sb.append("\n");
		if (P_SeedMode) {
			sb.append("current mode is seed mode\n\n");
		}
		if (P_SeedMode||P_logSolution)
			logger.write(sb.toString());
	}

	

	// 0 to n includes (like the red dots here
	// http://en.wikipedia.org/wiki/Binomial_distribution)
	public static int getRandIntWithBinomial(int n) {
		int x = 0;
		for (int i = 0; i < n; i++) {
			if (Math.random() < 0.5)
				x++;
		}
		return x;
	}

	public static double genRandomDouble(double rangeMin, double rangeMax) {
		return rangeMin + (rangeMax - rangeMin) * rand.nextDouble();
	}

	public static LinkedHashSet<String> convert(
			LinkedHashSet<SelectedFeature> features) throws Exception {
		LinkedHashSet<String> res = new LinkedHashSet<String>();
		for (SelectedFeature selectedFeature : features) {
			res.add(selectedFeature.getAbstractFeature().getName());
		}
		return res;
	}

	public static void flattenAnd(LogicFormula lf,
			ArrayList<LogicFormula> conjuncts) {
		if (lf instanceof And) {
			And alf = (And) lf;
			for (LogicFormula conj : alf.conjunct) {

				flattenAnd(conj, conjuncts);
			}
		} else {
			conjuncts.add(lf);
		}
	}

	public static void logSolutionSet(SolutionSet union, int evaluation)
			throws JMException {
		if (!P_logSolution)
			return;
		Solution bestSolution = union
				.best(new AProductLineObjectiveComparator());
		int[] gene = GAParams.GetGene(bestSolution);
		
		GeneErrorInfo errInfo=GAParams.errorPosition(gene);

		int violatedCnt = errInfo.getViolationFormulaCnt();
		
		long Elapsed = System.currentTimeMillis()
				- GAParams.seedStartTime;
		//===%Correctness
		double correctCnt = 0;
		for (int i = 0; i < union.size(); i++) {
			Solution solution = union.get(i);
			double[] objs = GAParams.GetObjectives(solution);			
			for (int j = 0; j < objs.length; j++) {
				GAParams.Objective obj = GAParams.objectives.get(j);
				if (obj == GAParams.Objective.Correctness) {
					if(objs[j]==0) correctCnt++;
					break;
				}
			}
		}
		GAParams.percentageOfCorrectness = (correctCnt / union.size()) ;
		if(GAParams.timeTo50PercentCorrectness==-1 && 
				GAParams.percentageOfCorrectness>=0.5) {
			GAParams.timeTo50PercentCorrectness=Elapsed;
			GAParams.evaluationTo50PercentCorrectness=evaluation;
		}
		
		if (!P_logSolution && !P_SeedMode && P_percentageOfCorrectness<=0)
			return;
		if(temp) {
			String info="===evaluation " + evaluation + ";Percentage="+GAParams.percentageOfCorrectness+";violatedCnt="+violatedCnt+"\n";
//			if (stagnantViolatedMin>violatedCnt || stagnantViolatedMin==0) {
//				stagnantViolatedMin=violatedCnt;
//				stagnantViolationCnt=0;
////				stagnantTryIndex=0;
////				AProductLineBitFlipMutation.threError=0.005;
////				GAParams.MutationRate=0.0000001;
//				
//			}else {
//				stagnantViolationCnt++;
//			}
//			if(stagnantViolationCnt>=40) {
//				stagnantViolationCnt=0;
//				System.out.println("====Trying "+ GAParams.stagnantTryValues[stagnantTryIndex]);
//				logger.info("====Trying "+ GAParams.stagnantTryValues[stagnantTryIndex]);
//				AProductLineBitFlipMutation.threError=GAParams.stagnantTryValues[stagnantTryIndex];
//				GAParams.MutationRate=GAParams.stagnantTryValues1[stagnantTryIndex];
//				stagnantTryIndex=(stagnantTryIndex+1)% stagnantTryValues.length;
//			}
			//System.out.println(info);
			try {
				logger.write(info);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//===
		
		
		StringBuilder sb = new StringBuilder();
		HashSet<String> features = new HashSet<String>();
		for (int i = 0; i < gene.length; i++) {
			if (gene[i] == 1) {
				features.add(GAParams.IntegerToFeatureMap.get(i));
			}
		}
		String info=null;
		
		if(GAParams.P_logSolution || GAParams.P_SeedMode) {
			
				 
				 info= "\r\n(Time=" + 
						Elapsed 
//						",Cnt="+
//				constraintCurrentCnt
//						+","
//						+ "TotalCnt="
//						+constraints.size()
						+")";
//					sb.append("===evaluation " + evaluation + " (violated:"
//							+ violatedCnt + ";selectedfeatures=" + features.size()
//							+ ")"+info+"\n");
//
//						for (String feature : features) {
//							sb.append(feature + ",");
//						}
//						sb.append("\nViolated Formulae:\n");
//						for(Integer i:errInfo.getViolatedFormula()) {
//							sb.append(GAParams.formulas.get(i)+"\n");
//						}
//
//					sb.append("\n");
					
					try {
						logger.write(sb.toString());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		}
		
		 if(GAParams.P_SeedMode) {
			
			if (violatedCnt == 0) {				
				
				for (String feature : features) {
					sb.append(feature + ",");
				}
			

				String seedStr = sb.toString();
				if (!visitedSeed.contains(seedStr)) {
					visitedSeed.add(seedStr);	
				
					try {
						logger.write(seedStr + info + "\r\n\r\n");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}			
					
				}
				
//				Utility.UpdateConstraintUsageCnt();
			}
		}
		// for (LogicFormula logicFormula : formulae) {
		// sb.append(logicFormula.toString() + "\n");
		// }

	}

	public static void logFinalResult(SolutionSet population)
			throws JMException {

		StringBuilder sb = new StringBuilder();
		ArrayList<double[]> results = new ArrayList<double[]>();

		/*
		 * bug fixed: solution �� objs����Ҫ��map
		 */
		Map<double[], Solution> solutionMap = new LinkedHashMap<double[], Solution>();
		for (int i = 0; i < population.size(); i++) {
			Solution solution = population.get(i);
			double[] objs = GAParams.GetObjectives(solution);
			if (GAParams.P_filterMandatory) {
				for (int j = 0; j < objs.length; j++) {
					objs[j] += GAParams.mandatoryValues.get(j);
				}
			}
			results.add(objs);
			solutionMap.put(objs, solution);
		}
		//Collections.sort(results, new LexicalDouble());
		GAParams.EvaluationResult.ParetoFront = results;

		double correctCnt = 0;
		boolean flag=false;
		for (int i = 0; i < results.size(); i++) {
			flag=false;
			sb.append((i + 1) + ") ");
			double[] objs = results.get(i);

			for (int j = 0; j < objs.length; j++) {
				GAParams.Objective obj = GAParams.objectives.get(j);
				String name = obj.name();
				if (obj == GAParams.Objective.MaximalFeature) {
					name = "MissingFeature";
				} else if (obj == GAParams.Objective.Correctness
						&& objs[j] == 0) {
					correctCnt++;
					flag=true;
				}
				sb.append(name + "=" + objs[j]);
				sb.append(", ");
			}
			sb.append("\n");
			
			if(flag||!flag) {
                Solution originalSol = solutionMap.get(objs);
				int[] gene = GAParams.GetGene(originalSol);
				//int[] gene = GAParams.GetGene(population.get(i));
				HashSet<String> features = new HashSet<String>();
				for (int k = 0; k < gene.length; k++) {
					if (gene[k] == 1) {
						features.add(GAParams.IntegerToFeatureMap.get(k));
					}
				}
				for(String f:features) {
					sb.append(f+",");
				}
				sb.append("\n\n");
			}	
		}

		double percentageOfCorrectness = (correctCnt / results.size()) * 100;
		GAParams.EvaluationResult.PercentageOfCorrectness = percentageOfCorrectness;
		GAParams.EvaluationResult.isCache = GAParams.P_useCached;
		GAParams.EvaluationResult.cacheMissed = GAParams.cacheMissed;
		sb.append("%Correct=" + percentageOfCorrectness + "\n");
		if (!P_logSolution && !P_SeedMode && P_percentageOfCorrectness<=0)
			return;
		try {
			logger.write(sb.toString());
			logger.flush();
			logger.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void logResultQuality(SolutionSet population, QualityIndicator indicators, double executionTime) {
		StringBuilder sb = new StringBuilder();
		System.out.println("final population = " + population.size());
		if (indicators != null) {
			//Change 10- (end) change below 
			sb.append("%Time="+executionTime+"\n");
			sb.append("%Hypervolume="
					+ indicators.getHypervolume(population)+"\n");
			sb.append("%Spread="
					+ indicators.getSpread(population)+"\n");
			sb.append("%GD=" + indicators.getGD(population)+"\n");
			sb.append("%IGD=" + indicators.getIGD(population)+"\n");
			sb.append("%Epsilon="
					+ indicators.getEpsilon(population));
			//GAParams.EvaluationResult.HV=indicators.getHypervolume(population);
			//GAParams.EvaluationResult.Spread=indicators.getSpread(population);
		} // if
		try {
			logger = new FileWriter("ProductLine.log",true);
			logger.write(sb.toString());
			logger.flush();
			logger.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
