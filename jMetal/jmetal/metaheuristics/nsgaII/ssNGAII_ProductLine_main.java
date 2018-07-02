//  NSGAII_main.java
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

package jmetal.metaheuristics.nsgaII;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.problems.AProductLine;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import ProductLine.GAParams;
import ProductLine.BestFix.TestDriver;

/** 
 * Class to configure and execute the NSGA-II algorithm.  
 *     
 * Besides the classic NSGA-II, a steady-state version (ssNSGAII) is also
 * included (See: J.J. Durillo, A.J. Nebro, F. Luna and E. Alba 
 *                  "On the Effect of the Steady-State Selection Scheme in 
 *                  Multi-Objective Genetic Algorithms"
 *                  5th International Conference, EMO 2009, pp: 183-197. 
 *                  April 2009)
 */ 

public class ssNGAII_ProductLine_main {
  public static Logger      logger_ ;      // Logger object
  public static FileHandler fileHandler_ ; // FileHandler object

  public static void main(String[] args) throws JMException, IOException,
	ClassNotFoundException, Exception {
		main1(GAParams.CaseStudy.EShop);
	
	}
  
  /**
   * @param args Command line arguments.
   * @throws JMException 
   * @throws IOException 
   * @throws SecurityException 
   * Usage: three options
   *      - jmetal.metaheuristics.nsgaII.NSGAII_main
   *      - jmetal.metaheuristics.nsgaII.NSGAII_main problemName
   *      - jmetal.metaheuristics.nsgaII.NSGAII_main problemName paretoFrontFile
   */
  public static void main1(GAParams.CaseStudy caseStudy) throws 
                                  JMException, 
                                  SecurityException, 
                                  IOException, 
                                  ClassNotFoundException, Exception {
	  
	TestDriver.constructFM(caseStudy);
    GAParams.initialize(TestDriver.NewFeatureModel, TestDriver.OldFeature);
		
		
    Problem   problem   ; // The problem to solve
    Algorithm algorithm ; // The algorithm to use
    Operator  crossover ; // Crossover operator
    Operator  mutation  ; // Mutation operator
    Operator  selection ; // Selection operator
    
    HashMap  parameters ; // Operator parameters
    
    QualityIndicator indicators ; // Object to get quality indicators

    // Logger object and file to store log messages
    logger_      = Configuration.logger_ ;
    fileHandler_ = new FileHandler("NSGAII_main.log"); 
    logger_.addHandler(fileHandler_) ;
        
    indicators = null ;
//    if (args.length == 1) {
//      Object [] params = {"Real"};
//      problem = (new ProblemFactory()).getProblem(args[0],params);
//    } // if
//    else if (args.length == 2) {
//      Object [] params = {"Real"};
//      problem = (new ProblemFactory()).getProblem(args[0],params);
//      indicators = new QualityIndicator(problem, args[1]) ;
//    } // if
//    else { // Default problem
//      //problem = new Kursawe("Real", 3);
//      //problem = new Kursawe("BinaryReal", 3);
//      //problem = new Water("Real");
//      problem = new ZDT3("ArrayReal", 30);
//      //problem = new ConstrEx("Real");
//      //problem = new DTLZ1("Real");
//      //problem = new OKA2("Real") ;
//    } // else
    
	problem = new AProductLine();
	
    algorithm = new ssNSGAII(problem);
    //algorithm = new ssNSGAII(problem);

    // Algorithm parameters
    algorithm.setInputParameter("populationSize",GAParams.IBEAPopulationSize);
    algorithm.setInputParameter("maxEvaluations",GAParams.MaxEvaluation);

    // Mutation and Crossover for Real codification 
    parameters = new HashMap() ;
//    parameters.put("probability", 0.9) ;
//    parameters.put("distributionIndex", 20.0) ;
    crossover = CrossoverFactory.getCrossoverOperator("AProductLineSinglePointCrossover", parameters);                   

    parameters = new HashMap() ;
//    parameters.put("probability", 1.0/problem.getNumberOfVariables()) ;
//    parameters.put("distributionIndex", 20.0) ;
    mutation = MutationFactory.getMutationOperator("AProductLineBitFlipMutation", parameters);                    

    // Selection Operator 
    parameters = null ;
    selection = SelectionFactory.getSelectionOperator("BinaryTournament2", parameters) ;                           

    // Add the operators to the algorithm
    algorithm.addOperator("crossover",crossover);
    algorithm.addOperator("mutation",mutation);
    algorithm.addOperator("selection",selection);

    // Add the indicator object to the algorithm
    algorithm.setInputParameter("indicators", indicators) ;
    
    // Execute the Algorithm
    long initTime = System.currentTimeMillis();
    SolutionSet population = algorithm.execute();
    long estimatedTime = System.currentTimeMillis() - initTime;
    
    GAParams.logFinalResult(population);
    GAParams.PrintResult(population, estimatedTime);
    
    // Result messages 
    logger_.info("Total execution time: "+estimatedTime + "ms");
    logger_.info("Variables values have been writen to file VAR");
    population.printVariablesToFile("VAR");    
    logger_.info("Objectives values have been writen to file FUN");
    population.printObjectivesToFile("FUN");
  
    indicators = new QualityIndicator(problem, GAParams.FunPath);
    
    if (indicators != null) {
    	System.out.println("Quality indicators") ;
        System.out.println("Hypervolume: " + indicators.getHypervolume(population)) ;
        System.out.println("GD         : " + indicators.getGD(population)) ;
        System.out.println("IGD        : " + indicators.getIGD(population)) ;
        System.out.println("Spread     : " + indicators.getSpread(population)) ;
        System.out.println("Epsilon    : " + indicators.getEpsilon(population)) ;  
       
        int evaluations = ((Integer)algorithm.getOutputParameter("evaluations")).intValue();
        System.out.println("Speed      : " + evaluations + " evaluations") ;      
        
        GAParams.EvaluationResult.HV=indicators.getHypervolume(population);
		GAParams.EvaluationResult.Spread=indicators.getSpread(population);
    } // if
  } //main
} // NSGAII_main
