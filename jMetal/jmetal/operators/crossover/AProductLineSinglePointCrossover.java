//  SinglePointCrossover.java
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

package jmetal.operators.crossover;

import java.util.HashMap;
import java.util.LinkedHashSet;

import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import ProductLine.GAParams;

/**
 * This class allows to apply a Single Point crossover operator using two parent
 * solutions.
 */
public class AProductLineSinglePointCrossover extends Crossover {
	/**
	 * Valid solution types to apply this operator
	 */
	// private static final List VALID_TYPES =
	// Arrays.asList(BinarySolutionType.class,
	// BinaryRealSolutionType.class,
	// IntSolutionType.class) ;

	// private Double crossoverProbability_ = null;

	/**
	 * Constructor Creates a new instance of the single point crossover operator
	 */
	// public SinglePointCrossover(HashMap<String, Object> parameters) {
	// super(parameters) ;
	// if (parameters.get("probability") != null)
	// crossoverProbability_ = (Double) parameters.get("probability") ;
	// } // SinglePointCrossover

	public AProductLineSinglePointCrossover(HashMap<String, Object> parameters) {
		super(parameters);
		// if (parameters.get("probability") != null)
		// crossoverProbability_ = (Double) parameters.get("probability") ;
	}

	/**
	 * Constructor Creates a new instance of the single point crossover operator
	 */
	// public SinglePointCrossover(Properties properties) {
	// this();
	// } // SinglePointCrossover

	/**
	 * Perform the crossover operation.
	 * 
	 * @param probability
	 *            Crossover probability
	 * @param parent1
	 *            The first parent
	 * @param parent2
	 *            The second parent
	 * @return An array containig the two offsprings
	 * @throws JMException
	 */
	public Solution[] doCrossover(Solution parent1, Solution parent2)
			throws JMException {
		Solution[] offSpring = new Solution[2];
		offSpring[0] = new Solution(parent1);
		offSpring[1] = new Solution(parent2);
		try {
			if (PseudoRandom.randDouble() < GAParams.CrossoverRate){
				if (!GAParams.P_useBestCrossover) {
					int crossoverPoint = PseudoRandom.randInt(0,
							parent1.numberOfVariables() - 1);
					int valueX1;
					int valueX2;
					for (int i = crossoverPoint; i < parent1
							.numberOfVariables(); i++) {
						Variable[] arr = parent1.getDecisionVariables();
						int size = arr.length;
						Variable v = arr[i];

						valueX1 = (int) parent1.getDecisionVariables()[i]
								.getValue();
						valueX2 = (int) parent2.getDecisionVariables()[i]
								.getValue();
						offSpring[0].getDecisionVariables()[i]
								.setValue(valueX2);
						offSpring[1].getDecisionVariables()[i]
								.setValue(valueX1);
					} // for
				} else {
					int[] parent1Gene = GAParams.GetGene(parent1);
					int[] parent2Gene = GAParams.GetGene(parent2);
					LinkedHashSet<Integer> ErrorPosition1 = GAParams
							.errorPosition(parent1Gene).getViolatedPropsInt();
					LinkedHashSet<Integer> ErrorPosition2 = GAParams
							.errorPosition(parent2Gene).getViolatedPropsInt();

					
					if (ErrorPosition1.size()!=0|| ErrorPosition2.size()!=0) {
						for (int i = 0; i < parent1.numberOfVariables(); i++) {

							if (!ErrorPosition1.contains(i)) {
								offSpring[1].getDecisionVariables()[i]
										.setValue(parent1Gene[i]);
							}
							if (!ErrorPosition2.contains(i)) {
								offSpring[0].getDecisionVariables()[i]
										.setValue(parent2Gene[i]);
							}
						}
					} else {
						int crossoverPoint = PseudoRandom.randInt(0,
								parent1.numberOfVariables() - 1);
						int valueX1;
						int valueX2;
						for (int i = crossoverPoint; i < parent1
								.numberOfVariables(); i++) {
							Variable[] arr = parent1.getDecisionVariables();
							int size = arr.length;
							Variable v = arr[i];

							valueX1 = (int) parent1.getDecisionVariables()[i]
									.getValue();
							valueX2 = (int) parent2.getDecisionVariables()[i]
									.getValue();
							offSpring[0].getDecisionVariables()[i]
									.setValue(valueX2);
							offSpring[1].getDecisionVariables()[i]
									.setValue(valueX1);
						}
					}

				}
			} // Int representation
		} catch (ClassCastException e1) {
			Configuration.logger_
					.severe("SinglePointCrossover.doCrossover: Cannot perfom "
							+ "SinglePointCrossover");
			Class cls = java.lang.String.class;
			String name = cls.getName();
			throw new JMException("Exception in " + name + ".doCrossover()");
		}
		return offSpring;
	} // doCrossover

	/**
	 * Executes the operation
	 * 
	 * @param object
	 *            An object containing an array of two solutions
	 * @return An object containing an array with the offSprings
	 * @throws JMException
	 */
	public Object execute(Object object) throws JMException {
		Solution[] parents = (Solution[]) object;

		// if (!(VALID_TYPES.contains(parents[0].getType().getClass()) &&
		// VALID_TYPES.contains(parents[1].getType().getClass())) ) {
		//
		// Configuration.logger_.severe("SinglePointCrossover.execute: the solutions "
		// +
		// "are not of the right type. The type should be 'Binary' or 'Int', but "
		// +
		// parents[0].getType() + " and " +
		// parents[1].getType() + " are obtained");
		//
		// Class cls = java.lang.String.class;
		// String name = cls.getName();
		// throw new JMException("Exception in " + name + ".execute()");
		// } // if

		if (parents.length < 2) {
			Configuration.logger_
					.severe("SinglePointCrossover.execute: operator "
							+ "needs two parents");
			Class cls = java.lang.String.class;
			String name = cls.getName();
			throw new JMException("Exception in " + name + ".execute()");
		}

		Solution[] offSpring;
		offSpring = doCrossover(parents[0], parents[1]);

		// -> Update the offSpring solutions
		for (int i = 0; i < offSpring.length; i++) {
			offSpring[i].setCrowdingDistance(0.0);
			offSpring[i].setRank(0);
		}
		return offSpring;
	} // execute
} // SinglePointCrossover
