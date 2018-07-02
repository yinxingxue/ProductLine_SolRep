//  Binh2.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//
//  Copyright (c) 2012 Antonio J. Nebro
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

package jmetal.problems;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.encodings.solutionType.IntSolutionType;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.util.JMException;
import jmetal.util.wrapper.XInt;
import ProductLine.GAParams;
import ProductLine.BestFix.EditDistance;

/**
 * Class representing problem Binh2
 */
public class AProductLine extends Problem {
	/**
	 * Constructor Creates a default instance of the Binh2 problem
	 * 
	 * @param solutionType
	 *            The solution type must "Real" or "BinaryReal".
	 */
	// public AProductLine(String solutionType) {
	public AProductLine() {
		numberOfVariables_ = GAParams.featuresToIntegerMap.size();

		numberOfObjectives_ = 2;
		numberOfObjectives_ = GAParams.objectives.size();
		numberOfConstraints_ = GAParams.formulas.size();
		problemName_ = "Product Line";

		lowerLimit_ = new double[numberOfVariables_];
		upperLimit_ = new double[numberOfVariables_];
		// lowerLimit_[0] = 0.0;
		// lowerLimit_[1] = 0.0;
		// upperLimit_[0] = 5.0;
		// upperLimit_[1] = 3.0;
		for (int i = 0; i < lowerLimit_.length; i++) {
			lowerLimit_[i] = 0.0;
			upperLimit_[i] = 1.0;
		}

		// if (solutionType.compareTo("BinaryReal") == 0)
		// solutionType_ = new BinaryRealSolutionType(this) ;
		// else if (solutionType.compareTo("Real") == 0)
		//solutionType_ = new RealSolutionType(this) ;  //jinghui modified here. =============================
		solutionType_ = new IntSolutionType(this);
		
		// else {
		// System.out.println("Error: solution type " + solutionType +
		// " invalid") ;
		// System.exit(-1) ;
		// }
	} // ConstrEx

	/**
	 * Evaluates a solution
	 * 
	 * @param solution
	 *            The solution to evaluate
	 * @throws JMException
	 */
//	public void evaluate(Solution solution) throws JMException {
//
//		// double[] fx = new double[numberOfObjectives_]; // function values
//		int[] x = GAParams.GetGene(solution);
//
//		// fx[0]= GAParams.isValidFeatures(x)?0:1;
//		// fx[0] = GAParams.errorPosition(x).size();
//		double size=GAParams.GenomeSize;
//		double errorSize= GAParams.errorPosition(x).size();
//		double errorRate =(GAParams.GenomeSize - errorSize)
//							/GAParams.GenomeSize;
//		for (int i = 0; i < GAParams.objectives.size(); i++) {
//			double res = 0;
//			
//			if (GAParams.objectives.get(i) == GAParams.Objective.Correctness) {
//				
//				res = errorRate;
//				
//			}
////			} else if (GAParams.objectives.get(i) == GAParams.Objective.MinimalDistance) {
////				
////				res = EditDistance.levenshteinDistance(GAParams.oldGenes.Genes,
////						x);
////				
////			} 
//			else if (GAParams.objectives.get(i) == GAParams.Objective.MaximalFeature
//					|| GAParams.objectives.get(i) == GAParams.Objective.MinimalFeature) {
//				
//				int cnt = 0;
//				for (int j = 0; j < x.length; j++) {
//					if (x[j] == 1)
//						cnt++;
//				}
//				if (GAParams.objectives.get(i) == GAParams.Objective.MaximalFeature) {
//					res =cnt/size;
//				} else {
//					res =(size-cnt)/size;
//				}
//				
//				
//				
//			}else if(GAParams.objectives.get(i) == GAParams.Objective.NotUsedBefore) {
//				int notUsedBefore=0;
//				for (int j = 0; j < x.length; j++) {
//					if (x[j] == 1 && GAParams.featuresToNotUseBeforeMap.get(j)) {
//						notUsedBefore++;
//					}
//				}
//				res=(size-notUsedBefore)/size;
//				
//			}else if(GAParams.objectives.get(i) == GAParams.Objective.Cost) {
//				double cost=0;
//				for (int j = 0; j < x.length; j++) {
//					if (x[j] == 1) {
//						cost+= GAParams.featuresToCostMap.get(j);
//					}
//				}
//				double maxCost=GAParams.maxCost*size;
//				double minCost=0;//not GAParams.minCost*size;, as can be no feature selected
//				
//				res=(maxCost-cost)/(maxCost-minCost);
//				
//			}else if(GAParams.objectives.get(i) == GAParams.Objective.Defects) {
//				int defects=0;
//				for (int j = 0; j < x.length; j++) {
//					if (x[j] == 1) {
//						defects+= GAParams.featuresToDefectsMap.get(j);
//					}
//				}
//				double maxDefect=GAParams.maxDefect*size;
//				double minDefect=0;//not GAParams.minDefect*size;, as can be no feature selected
//				res=(maxDefect-defects)/(maxDefect-minDefect);
//				
//			}
//			
//			if (GAParams.objectives.get(i) != GAParams.Objective.Correctness) {		
//				res = 0.5*res;
//				if(errorSize==0) res+=0.5;
//					
//			}
//
//			solution.setObjective(i, 1-res);
//
//		}
//
//	} // evaluate
	
	
	public void evaluate(Solution solution) throws JMException {

		// double[] fx = new double[numberOfObjectives_]; // function values
	
		
		int[] x = GAParams.GetGene(solution);
		
		
		
		// fx[0]= GAParams.isValidFeatures(x)?0:1;
		// fx[0] = GAParams.errorPosition(x).size();

		for (int i = 0; i < GAParams.objectives.size(); i++) {
			double res = 0;
			if (GAParams.objectives.get(i) == GAParams.Objective.Correctness) {
				
				res = GAParams.errorPosition(x).getViolatedPropsInt().size();
				
			} 
//			else if (GAParams.objectives.get(i) == GAParams.Objective.MinimalDistance) {
//				
//				res = EditDistance.levenshteinDistance(GAParams.oldGenes.Genes,
//						x);
//				
//			} 
			else if (GAParams.objectives.get(i) == GAParams.Objective.MaximalFeature
					|| GAParams.objectives.get(i) == GAParams.Objective.MinimalFeature) {
				
				int cnt = 0;
				for (int j = 0; j < x.length; j++) {
					if (x[j] == 1)
						cnt++;
				}
				if (GAParams.objectives.get(i) == GAParams.Objective.MaximalFeature) {
					res = x.length - cnt;
				} else {
					res = cnt;
				}
				
			}else if(GAParams.objectives.get(i) == GAParams.Objective.NotUsedBefore) {
				int notUsedBefore=0;
				for (int j = 0; j < x.length; j++) {
					if (x[j] == 1 && GAParams.featuresToNotUseBeforeMap.get(j)) {
						notUsedBefore++;
					}
				}
				res=notUsedBefore;
			}else if(GAParams.objectives.get(i) == GAParams.Objective.Cost) {
				double cost=0;
				for (int j = 0; j < x.length; j++) {
					if (x[j] == 1) {
						cost+= GAParams.featuresToCostMap.get(j);
					}
				}
				res=cost;
			}else if(GAParams.objectives.get(i) == GAParams.Objective.Defects) {
				int defects=0;
				for (int j = 0; j < x.length; j++) {
					if (x[j] == 1) {
						defects+= GAParams.featuresToDefectsMap.get(j);
					}
				}
				res=defects;
			}

			solution.setObjective(i, res);

		}

	} // evaluate

	/**
	 * Evaluates the constraint overhead of a solution
	 * 
	 * @param solution
	 *            The solution
	 * @throws JMException
	 */
	public void evaluateConstraints(Solution solution) throws JMException {
		// double [] constraint = new double[this.getNumberOfConstraints()];
		//
		// double x0 = solution.getDecisionVariables()[0].getValue();
		// double x1 = solution.getDecisionVariables()[1].getValue();
		//
		// constraint[0] = -1.0*(x0-5)*(x0-5) - x1*x1 + 25.0 ;
		// constraint[1] = (x0-8)*(x0-8) + (x1+3)*(x1+3) - 7.7 ;
		//
		// double total = 0.0;
		// int number = 0;
		// for (int i = 0; i < this.getNumberOfConstraints(); i++)
		// if (constraint[i]<0.0){
		// total+=constraint[i];
		// number++;
		// }
		// XInt vars = new XInt(solution) ;
		// int [] x = new int[numberOfVariables_] ;
		// for (int i = 0 ; i < numberOfVariables_; i++)
		// x[i] = vars.getValue(i) ;
//		double total=GAParams.errorPosition(GAParams.GetGene(solution)).size();
//		 solution.setOverallConstraintViolation(total);
//		 solution.setNumberOfViolatedConstraint((int)total);
	} // evaluateConstraints
} // Binh2