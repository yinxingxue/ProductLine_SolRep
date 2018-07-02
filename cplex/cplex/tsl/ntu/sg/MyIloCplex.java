/**
 * 
 */
package cplex.tsl.ntu.sg;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ProductLine.GAParams;
import ProductLine.ResultComparator;
import ProductLine.FeatureModel.FeatureModel;
import ProductLine.FeatureModel.InequationMap;
import ProductLine.FeatureModel.LogicFeatureModel;
import ilog.concert.IloException;
import ilog.cplex.IloCplex;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.util.JMException;
import jmetal.util.NonDominatedSolutionList;

/**
 * @author yinxing
 *
 */
public class MyIloCplex extends IloCplex {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8063201773228680766L;
	private static final boolean DEBUG_INFO  = false;
	
    private InequationMap inmap;
	private Map<String, CplexSolution> sols;
	private SolutionSet population;
	private Problem problem;
	private FeatureModel fm;
	
	public InequationMap getInmap() {
		return inmap;
	}

	public void setInmap(InequationMap inmap) {
		this.inmap = inmap;
	}
 
	public SolutionSet getSolutionPopulation() {
		return population;
	}

	public Map<String, CplexSolution> getCplexSolution() {
		return sols;
	}

	public FeatureModel getNewFeatureModel() {
		return fm;
	}

	public void setNewFeatureModel(FeatureModel fm) {
		this.fm = fm;
	}

	public MyIloCplex(Problem problem) throws IloException {
		super();
		CplexProblem cplex = (CplexProblem)problem;
		this.inmap = cplex.getInmap();
		this.problem = problem;
		this.fm = cplex.getNewFeatureModel();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}
	
	public SolutionSet execute()
	{
		try {
			sols = inmap.convertToCplex(this);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
		SolutionSet population = ConvertingToSolutionSet(sols);
		return population;
	}

	public SolutionSet ConvertingToSolutionSet(Map<String, CplexSolution> sols) {
		
		
		// Initialize the variables
		NonDominatedSolutionList solutionSet = new NonDominatedSolutionList();
		int numberIncorSol = 0;
		try {
			// -> Create the initial solutionSet
			Solution newSolution;
			for (String key : sols.keySet()) {

				newSolution = new Solution(problem);
				// Change 1
				setSolution(sols.get(key), newSolution);
				problem.evaluate(newSolution);
				double corr = newSolution.getObjective(0);
			    //assert corr ==0:"Cplex finds the incorrect solutions!";
				int errorCounter = 0;
				if(corr!=0)
				{
//					try {
//						throw new java.lang.Exception("Cplex finds the incorrect solutions!");
//					} catch (java.lang.Exception e) {
//						e.printStackTrace();
//						System.exit(-1);
//					}
					numberIncorSol++;
					System.out.println(errorCounter+++"\t Cplex finds the incorrect solutions: " + newSolution.toString());
					int[] x = GAParams.GetGene(newSolution);
					ArrayList<Integer> formular = GAParams.errorPosition(x).getViolatedFormula();
					System.out.println(formular);
					
					if (formular.size() == 1 && formular.get(0) == 3589) {
						HashSet<String> features = new HashSet<String>();
						for (int k = 0; k < x.length; k++) {
							if (x[k] == 1) {
								features.add(GAParams.IntegerToFeatureMap
										.get(k));
							}
						}
						System.out.println(features);
					}
				}
				else
				{
				problem.evaluateConstraints(newSolution);
				if(DEBUG_INFO) System.out.println("before adding" + newSolution.toString()+ " set:"+ solutionSet);
				solutionSet.add(newSolution);
				if(DEBUG_INFO) System.out.println("after  adding" + newSolution.toString()+ " set:"+ solutionSet);
				}
			}
		} catch (ClassNotFoundException | JMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// get the non-dominated sols
		Set<String> nonDominated = ResultComparator.findDominantSol(sols.keySet());
		System.out.println("The size of MyNonDomi: "+nonDominated.size());
		System.out.println(nonDominated);
		sols.keySet().retainAll(nonDominated);
				
		//assert sols.size() == solutionSet.size(): "sols.size() != solutionSet.size()";
		if(sols.size() != solutionSet.size())
		{
//			try {
//				throw new java.lang.Exception( "sols.size() != solutionSet.size()");
//			} catch (java.lang.Exception e) {
//				e.printStackTrace();
//				System.exit(-1);
//			}
			System.out.println("sols.size() != solutionSet.size()");
			System.out.println("Cplex finds the incorrect solutions num: " +numberIncorSol);
		}
		System.out.println("The size of TheNonDomi: "+solutionSet.size());
		solutionSet.printObjectives();
		return solutionSet;
	}

	public void setSolution(CplexSolution cplexSolution, Solution newSolution) {
		try {
			int omittedScope = 0;
			if(this.fm instanceof LogicFeatureModel)
			{
				LogicFeatureModel lfm = (LogicFeatureModel)fm;
				omittedScope+= lfm.getMandatoryNames().size();
				omittedScope+= lfm.getMustNotInNames().size();
			}
			assert cplexSolution.xval.length == newSolution.numberOfVariables()+omittedScope:"variable num is not consistent!";
			if(cplexSolution.xval.length != newSolution.numberOfVariables()+omittedScope)
			{
				try {
					throw new java.lang.Exception("variable num is not consistent!");
				} catch (java.lang.Exception e) {
					e.printStackTrace();
					System.exit(-1);
				}
			}
			
			for (int j = 0; j < newSolution.numberOfVariables(); j++) {
				Double value = cplexSolution.xval[j];
				newSolution.getDecisionVariables()[j].setValue(value);
			}
		} catch (JMException e) {
			e.printStackTrace();
		}
	}
}
