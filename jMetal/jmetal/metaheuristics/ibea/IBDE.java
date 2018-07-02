//  IBEA.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package jmetal.metaheuristics.ibea;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.Ranking;
import jmetal.util.comparators.DominanceComparator;
import ProductLine.GAParams;

/**
 * This class implementing the IBEA algorithm
 */
public class IBDE extends Algorithm {

	/**
	 * Defines the number of tournaments for creating the mating pool
	 */
	public static final int TOURNAMENTS_ROUNDS = 1;

	/**
	 * Stores the value of the indicator between each pair of solutions into the
	 * solution set
	 */
	private List<List<Double>> indicatorValues_;

	/**
   *
   */
	private double maxIndicatorValue_;

	/**
	 * Constructor. Create a new IBEA instance
	 * 
	 * @param problem
	 *            Problem to solve
	 */
	public IBDE(Problem problem) {
		super(problem);
	} // Spea2

	/**
	 * calculates the hypervolume of that portion of the objective space that is
	 * dominated by individual a but not by individual b
	 */
	double calcHypervolumeIndicator(Solution p_ind_a, Solution p_ind_b, int d,
			double maximumValues[], double minimumValues[]) {
		double a, b, r, max;
		double volume = 0;
		double rho = 2.0;

		r = rho * (maximumValues[d - 1] - minimumValues[d - 1]);
		max = minimumValues[d - 1] + r;

		a = p_ind_a.getObjective(d - 1);
		if (p_ind_b == null)
			b = max;
		else
			b = p_ind_b.getObjective(d - 1);

		if (d == 1) {
			if (a < b)
				volume = (b - a) / r;
			else
				volume = 0;
		} else {
			if (a < b) {
				volume = calcHypervolumeIndicator(p_ind_a, null, d - 1,
						maximumValues, minimumValues) * (b - a) / r;
				volume += calcHypervolumeIndicator(p_ind_a, p_ind_b, d - 1,
						maximumValues, minimumValues) * (max - b) / r;
			} else {
				volume = calcHypervolumeIndicator(p_ind_a, p_ind_b, d - 1,
						maximumValues, minimumValues) * (max - b) / r;
			}
		}

		return (volume);
	}

	/**
	 * This structure store the indicator values of each pair of elements
	 */
	public void computeIndicatorValuesHD(SolutionSet solutionSet,
			double[] maximumValues, double[] minimumValues) {
		SolutionSet A, B;
		// Initialize the structures
		indicatorValues_ = new ArrayList<List<Double>>();
		maxIndicatorValue_ = -Double.MAX_VALUE;

		for (int j = 0; j < solutionSet.size(); j++) {
			A = new SolutionSet(1);
			A.add(solutionSet.get(j));

			List<Double> aux = new ArrayList<Double>();
			for (int i = 0; i < solutionSet.size(); i++) {
				B = new SolutionSet(1);
				B.add(solutionSet.get(i));

				int flag = (new DominanceComparator()).compare(A.get(0),
						B.get(0));

				double value = 0.0;
				if (flag == -1) {
					value = -calcHypervolumeIndicator(A.get(0), B.get(0),
							problem_.getNumberOfObjectives(), maximumValues,
							minimumValues);
				} else {
					value = calcHypervolumeIndicator(B.get(0), A.get(0),
							problem_.getNumberOfObjectives(), maximumValues,
							minimumValues);
				}
				// double value =
				// epsilon.epsilon(matrixA,matrixB,problem_.getNumberOfObjectives());

				// Update the max value of the indicator
				if (Math.abs(value) > maxIndicatorValue_)
					maxIndicatorValue_ = Math.abs(value);
				aux.add(value);
			}
			indicatorValues_.add(aux);
		}
	} // computeIndicatorValues

	/**
	 * Calculate the fitness for the individual at position pos
	 */
	public void fitness(SolutionSet solutionSet, int pos) {
		double fitness = 0.0;
		double kappa = 0.05;

		for (int i = 0; i < solutionSet.size(); i++) {
			if (i != pos) {
				fitness += Math
						.exp((-1 * indicatorValues_.get(i).get(pos) / maxIndicatorValue_)
								/ kappa);
			}
		}
		solutionSet.get(pos).setFitness(fitness);
	}

	/**
	 * Calculate the fitness for the entire population.
	 **/
	public void calculateFitness(SolutionSet solutionSet) {
		// Obtains the lower and upper bounds of the population
		double[] maximumValues = new double[problem_.getNumberOfObjectives()];
		double[] minimumValues = new double[problem_.getNumberOfObjectives()];

		for (int i = 0; i < problem_.getNumberOfObjectives(); i++) {
			maximumValues[i] = -Double.MAX_VALUE; // i.e., the minus maxium
													// value
			minimumValues[i] = Double.MAX_VALUE; // i.e., the maximum value
		}

		for (int pos = 0; pos < solutionSet.size(); pos++) {
			for (int obj = 0; obj < problem_.getNumberOfObjectives(); obj++) {
				double value = solutionSet.get(pos).getObjective(obj);
				if (value > maximumValues[obj])
					maximumValues[obj] = value;
				if (value < minimumValues[obj])
					minimumValues[obj] = value;
			}
		}

		computeIndicatorValuesHD(solutionSet, maximumValues, minimumValues);
		for (int pos = 0; pos < solutionSet.size(); pos++) {
			fitness(solutionSet, pos);
		}
	}

	/**
	 * Update the fitness before removing an individual
	 */
	public Solution removeWorst(SolutionSet solutionSet) {

		// Find the worst;
		double worst = solutionSet.get(0).getFitness();
		int worstIndex = 0;
		double kappa = 0.05;

		for (int i = 1; i < solutionSet.size(); i++) {
			if (solutionSet.get(i).getFitness() > worst) {
				worst = solutionSet.get(i).getFitness();
				worstIndex = i;
			}
		}

		// if (worstIndex == -1) {
		// System.out.println("Yes " + worst);
		// }
		// System.out.println("Solution Size "+solutionSet.size());
		// System.out.println(worstIndex);

		// Update the population
		for (int i = 0; i < solutionSet.size(); i++) {
			if (i != worstIndex) {
				double fitness = solutionSet.get(i).getFitness();
				fitness -= Math
						.exp((-indicatorValues_.get(worstIndex).get(i) / maxIndicatorValue_)
								/ kappa);
				solutionSet.get(i).setFitness(fitness);
			}
		}

		// remove worst from the indicatorValues list
		indicatorValues_.remove(worstIndex); // Remove its own list
		Iterator<List<Double>> it = indicatorValues_.iterator();
		while (it.hasNext())
			it.next().remove(worstIndex);

		// remove the worst individual from the population
		Solution sol = solutionSet.get(worstIndex);
		solutionSet.remove(worstIndex);
		return sol;
		
	} // removeWorst

	//@function: check whether f1[] dominate f2.
    //@parms:  two sets of objective values: f1[], and f2[], the dimension of these two arrays is NVARS.
    //@return: if f1[] dominates f2[] return 1, otherwise, 0 equal, otherwise return -1
    int Dominate(double f1[], double f2[], int obj_n)
    {
         int i, flag = 0;
         if(f1[0] < f2[0]) return 1;
         else if(f1[0] > f2[0]) return -1;
         
         for(i = 1; i < obj_n; i++){
              if(f1[i] < f2[i]) flag = 1;
              if(f1[i] > f2[i]){
                   flag = 0;
                   break;
              }
         }
         if(flag == 1) return 1;
         
         for(i = 0; i < obj_n; i++){
              if(Math.abs(f1[i]-f2[i])> 1e-10) break;               
         }
         if(i == obj_n) return 0; //equal ====
         
         return -1;
    }
    
	/**
	 * Runs of the IBEA algorithm.
	 * 
	 * @return a <code>SolutionSet</code> that is a set of non dominated
	 *         solutions as a result of the algorithm execution
	 * @throws JMException
	 */
	public SolutionSet execute() throws JMException, ClassNotFoundException {
		

		int populationSize, archiveSize, maxEvaluations, evaluations;
		Operator crossoverOperator, mutationOperator, selectionOperator;
		SolutionSet solutionSet, archive, offSpringSolutionSet;
		
		SolutionSet DE_solutionSet, DE_archive, DE_offSpringSolutionSet;
		

		// Read the params
		populationSize = ((Integer) getInputParameter("populationSize"))
				.intValue();
		archiveSize = ((Integer) getInputParameter("archiveSize")).intValue();
		maxEvaluations = ((Integer) getInputParameter("maxEvaluations"))
				.intValue();

		// Read the operators
		crossoverOperator = operators_.get("crossover");
		mutationOperator = operators_.get("mutation");
		selectionOperator = operators_.get("selection");

		// Initialize the variables
		solutionSet = new SolutionSet(3 * populationSize);
		archive = new SolutionSet(3 * archiveSize);
		
		DE_solutionSet = new SolutionSet(3*populationSize);
		DE_archive = new SolutionSet(3*archiveSize);
		
		evaluations = 0;

		// -> Create the initial solutionSet
		Solution newSolution;
		for (int i = 0; i < populationSize; i++) {

			newSolution = new Solution(problem_);
			//Change 1
			if (GAParams.P_setInitialPopulation) {
				GAParams.setInitialSolution(i, populationSize, newSolution);
			}
			problem_.evaluate(newSolution);
			problem_.evaluateConstraints(newSolution);
			evaluations++;
			solutionSet.add(newSolution);
		}

		
        for (int i = 0; i < populationSize; i++) {
             newSolution = new Solution(problem_);
             //Change 1
             if (GAParams.P_setInitialPopulation) {
                  GAParams.setInitialSolution(i, populationSize, newSolution);
             }
             problem_.evaluate(newSolution);
             problem_.evaluateConstraints(newSolution);
             evaluations++;
             DE_solutionSet.add(newSolution);
        }
        
		while (evaluations < maxEvaluations) {			
			SolutionSet union = ((SolutionSet) solutionSet).union(archive);	
			//===========================================================================================			
			//Change 2
			GAParams.logSolutionSet(union, evaluations);
			calculateFitness(union);
			archive = union;
			Solution sol;
			while (archive.size() > populationSize) {
				sol = removeWorst(archive);
				//if(sol.isMarked()==true) DE_solutionSet.add(sol);
			}
			
			SolutionSet DE_union = ((SolutionSet) DE_solutionSet).union(DE_archive);	
            //Change 2
            //GAParams.logSolutionSet(DE_union, DE_evaluations);
            calculateFitness(DE_union);
            DE_archive = DE_union;
           
            while (DE_archive.size() > populationSize) {
                 removeWorst(DE_archive);
            }
           
			//======================================================================================
			/*double IBEA_min = 1e10, IBDE_min = 1e10, IBDE_max = -1e10, IBEA_max = -1e10;
			int	   IBEA_min_id = 0, IBDE_min_id = 0, IBDE_max_id = 0, IBEA_max_id = 0;
            for (int i = 0; i < populationSize; i++){
                 if(archive.get(i).getObjective(0) < IBEA_min){
                	 IBEA_min = archive.get(i).getObjective(0);
                	 IBEA_min_id = i;
                 }
                 
                 if(archive.get(i).getObjective(0) > IBEA_max){
                  	 IBEA_max = archive.get(i).getObjective(0);
                  	 IBEA_max_id = i;
                }
            }
            for (int i = 0; i < populationSize; i++){
                if(DE_archive.get(i).getObjective(0) < IBDE_min){
               	 IBDE_min = DE_archive.get(i).getObjective(0);
               	 IBDE_min_id = i;
                }
                
                if(DE_archive.get(i).getObjective(0) > IBDE_max){
                  	 IBDE_max = DE_archive.get(i).getObjective(0);
                  	 IBDE_max_id = i;
                }
           }
         
           if(IBEA_min < IBDE_min){
        	   System.out.println("@@@@@@@@@@@@@@@@@@@@");
        	  // int k = PseudoRandom.randInt(0,populationSize-1);
        	   DE_archive.replace(IBDE_max_id, archive.get(IBEA_min_id));
           }else if(IBEA_min > IBDE_min){
        	   System.out.println("+++++++++++++++++++");
        	  // int k = PseudoRandom.randInt(0,populationSize-1);
        	   archive.replace(IBDE_max_id, DE_archive.get(IBDE_min_id));
           }*/
         
           //=================================================================
			// Create a new offspringPopulation
			offSpringSolutionSet = new SolutionSet(2 * populationSize);
			DE_offSpringSolutionSet = new SolutionSet(2 * populationSize);
			
			Solution[] parents = new Solution[2];
			while (offSpringSolutionSet.size() < populationSize/2) { 
				int j = 0;
				do {
					j++;	                 
					parents[0] = (Solution) selectionOperator.execute(archive); 
					
				} while (j < IBEA.TOURNAMENTS_ROUNDS); // do-while
				int k = 0;
				do {
					k++;
					parents[1] = (Solution) selectionOperator.execute(archive);
				} while (k < IBEA.TOURNAMENTS_ROUNDS); // do-while

				// make the crossover
				Solution[] offSpring = (Solution[]) crossoverOperator
						.execute(parents);
				mutationOperator.execute(offSpring[0]);
				problem_.evaluate(offSpring[0]);
				problem_.evaluateConstraints(offSpring[0]);
				offSpringSolutionSet.add(offSpring[0]);
				offSpring[0].marked();
				
				DE_offSpringSolutionSet.add(offSpring[0]);
				
				evaluations++;
			} // while
			// End Create a offSpring solutionSet
			//solutionSet = offSpringSolutionSet;  move to following process.
			
			//==========================DE ====================================			
            // Create a new offspringPopulation
           // System.out.println("DE_offSpringSolutionSet size = " + DE_offSpringSolutionSet.size());
            for (int g = 0; g < populationSize/2; g++){
            	int T = 10, temp;   
            	int i = PseudoRandom.randInt(0,  populationSize-1);                
                for(int j = 0; j < T; j++){
	               	temp = (int)(PseudoRandom.randInt(0, populationSize-1));
	               	if((DE_archive.get(temp).getObjective(0) < DE_archive.get(i).getObjective(0)) ||
	               		((DE_archive.get(temp).getObjective(0) == DE_archive.get(i).getObjective(0)) &&
	               			(DE_archive.get(temp).getFitness() < DE_archive.get(i).getFitness())
	               		 )
	               		){
	               		i = temp;
	               	}	            
                }
                
                Solution[] DE_parents = new Solution[5];
                
                
                 int r1, r2, r3,r4,r5;
                 r1 = PseudoRandom.randInt(0,  populationSize-1);                
                 for(int j = 0; j < T; j++){
	               	temp = (int)(PseudoRandom.randInt(0, populationSize-1));
	               	if((DE_archive.get(temp).getObjective(0) < DE_archive.get(r1).getObjective(0)) ||
	               		((DE_archive.get(temp).getObjective(0) == DE_archive.get(r1).getObjective(0)) &&
	               			(DE_archive.get(temp).getFitness() < DE_archive.get(r1).getFitness())
	               		 )
	               		){
	               		r1 = temp;
	               	}	            
                 }
                 
                 do{ r2 = (int)(PseudoRandom.randInt(0,populationSize-1));}while(r2==i || r2==r1);                 
                 do{ r3 = (int)(PseudoRandom.randInt(0,populationSize-1));}while(r3==i || r3==r2 || r3==r1);                 
                 do{ r4 = (int)(PseudoRandom.randInt(0,populationSize-1));}while(r4==i || r4==r3 || r4 == r2|| r4==r1);
                 do{ r5 = (int)(PseudoRandom.randInt(0,populationSize-1));}while(r5==i || r5 == r4 ||r5==r3 || r5 == r2|| r5==r1);
               //  System.out.println("----------"+ DE_archive.size()+"\t"+r1 +"\t"+ r2 +"\t"+ r3 +"\t"+ r4+"\t"+r5);
                 DE_parents[0] = DE_archive.get(r1) ;
                 DE_parents[1] = DE_archive.get(r2) ;
                 DE_parents[2] = DE_archive.get(r3) ;
                 DE_parents[3] = DE_archive.get(r4) ;
                 DE_parents[4] = DE_archive.get(r5) ;
                
                 Solution child ;
             	 
         		 child = new Solution(DE_archive.get(i)) ;
         		 LinkedHashSet<Integer> ErrorPosition = GAParams.errorPosition(GAParams.GetGene(child)).getViolatedPropsInt();
             	
         		 int numberOfVariables =child.getDecisionVariables().length;
         		 int jrand  = PseudoRandom.randInt(0, numberOfVariables - 1);
         		 double CR = PseudoRandom.randDouble(0, 1);	
         		 double F = PseudoRandom.randDouble(0, 1);
         		 double F2 = PseudoRandom.randDouble(0, 1);
         		 for (int j=0; j < numberOfVariables; j++) {
         			 
         			Boolean containError = ErrorPosition.contains(j);
         			if (PseudoRandom.randDouble(0, 1) < CR || j == jrand) {
         				int x0 = (int) DE_archive.get(i).getDecisionVariables()[j].getValue();
         				int x1 = (int) DE_parents[0].getDecisionVariables()[j].getValue();
         				int x2 = (int) DE_parents[1].getDecisionVariables()[j].getValue();
         				int x3 = (int) DE_parents[2].getDecisionVariables()[j].getValue();
         				int x4 = (int) DE_parents[3].getDecisionVariables()[j].getValue();		
         				int x5 = (int) DE_parents[4].getDecisionVariables()[j].getValue();		
         				int value;
         				double v1= 0, v2 = 0;
         				if(x2!=x3) v1 = 1;
         				if(x4!=x5) v2 = 1;
         				double phi = v1 * F + v2 * F2 - v1 * F * v2 * F2;
         				//if(containError && PseudoRandom.randDouble(0, 1) < phi ||
         				//containError && PseudoRandom.randDouble() < GAParams.ErrorMutationRate){
         				//	value = PseudoRandom.randInt(0, 1);
         				//if(PseudoRandom.randDouble(0, 1) < phi){                 					
         				if(PseudoRandom.randDouble(0, 1) < phi){
         					value = PseudoRandom.randInt(0, 1); 
         				}else{ value = x1;}				
         				child.getDecisionVariables()[j].setValue(value);
         			}
         			else {
         				int value = (int) DE_archive.get(i).getDecisionVariables()[j].getValue();
         				child.getDecisionVariables()[j].setValue(value);
         			} // else
         		}
         		
         		child.unMarked();
                 
                problem_.evaluate(child);
                problem_.evaluateConstraints(child);
                
                
                // Dominance test
                int obj_n = DE_archive.get(i).getNumberOfObjectives();
                double f1[] = new double[obj_n];
                double f2[] = new double[obj_n];
                for(int j = 0; j < obj_n; j++){
                      f2[j] = DE_archive.get(i).getObjective(j);
                      f1[j] = child.getObjective(j);
                 }
                 if(Dominate(f1, f2, obj_n) == 1){
                      DE_archive.replace(i,  child);
                 }else if(Dominate(f2, f1, obj_n) == 1){  
                	 
                	 offSpringSolutionSet.add(child);
                	 
                 }else if(Dominate(f1, f2, obj_n) == 0){ 
                	
                 }else{     
                     DE_offSpringSolutionSet.add(child);
                 }     
        		
                // offSpringSolutionSet.add(child);
                 
                 evaluations++;
                 
            } // while
            // End Create a offSpring solutionSet
            DE_solutionSet = DE_offSpringSolutionSet; 
            solutionSet = offSpringSolutionSet;            
			//==================================================================			
			//Change 3-the end
			if(evaluations%10000==0) {
				System.out.println("Current Evaluation="+evaluations);
				System.out.flush();
			}
		} // while
		
		/*
		Ranking ranking1 = new Ranking(DE_archive);
     	
		System.out.println("DE archive =====\t" + ranking1.getSubfront(0).size());
		Ranking ranking2 = new Ranking(archive);
     	for(int i = 0; i < DE_archive.size(); i++){
     		for(int j = 0; j < 5; j++){
     			System.out.print(DE_archive.get(i).getObjective(j)+"\t");
     		}
     		System.out.println("");
     	}
     	System.out.println("==================\n");
    	for(int i = 0; i < archive.size(); i++){
     		for(int j = 0; j < 5; j++){
     			System.out.print(archive.get(i).getObjective(j)+"\t");
     		}
     		System.out.println("");
     	}
     		
		System.out.println("EA archive =====\t" + ranking2.getSubfront(0).size());*/
		
		
		
		archive = archive.union(DE_archive);
		//Change 2
		GAParams.logSolutionSet(archive, evaluations);
		calculateFitness(archive);
		Solution sol;
		while (archive.size() > populationSize) {
			sol = removeWorst(archive);
		}
		
		Ranking ranking = new Ranking(archive);
		System.out.println("together = " + ranking.getSubfront(0).size());
		int count = 0;
		for(int i = 0; i < ranking.getSubfront(0).size(); i++){ if(ranking.getSubfront(0).get(i).isMarked() == true) count++;}
		System.out.println("count = " + count);return ranking.getSubfront(0);
	} // execute
} // Spea2
