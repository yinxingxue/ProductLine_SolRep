//  ObjectiveComparator.java
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

package jmetal.util.comparators;

import java.util.Comparator;

import jmetal.core.Solution;
import ProductLine.GAParams;

/**
 * This class implements a <code>Comparator</code> (a method for comparing
 * <code>Solution</code> objects) based on a objective values.
 */
public class AProductLineObjectiveComparator implements Comparator {

	/**
	 * Stores the index of the objective to compare
	 */
	// private int nObj;
	private boolean ascendingOrder_;

	/**
	 * Constructor.
	 * 
	 * @param nObj
	 *            The index of the objective to compare
	 */
	public AProductLineObjectiveComparator() {
		// this.nObj = nObj;
		ascendingOrder_ = false;
	} // ObjectiveComparator

	public AProductLineObjectiveComparator(boolean descendingOrder) {
		// this.nObj = nObj;
		if (descendingOrder)
			ascendingOrder_ = false;
		else
			ascendingOrder_ = true;
	} // ObjectiveComparator

	/**
	 * Compares two solutions.
	 * 
	 * @param o1
	 *            Object representing the first <code>Solution</code>.
	 * @param o2
	 *            Object representing the second <code>Solution</code>.
	 * @return -1, or 0, or 1 if o1 is less than, equal, or greater than o2,
	 *         respectively.
	 */

	// ascending_ order means smaller better
	public int compare(Object o1, Object o2) {
		if (o1 == null)
			return 1;
		else if (o2 == null)
			return -1;

		Solution s1 = (Solution) o1;
		Solution s2 = (Solution) o2;

		double weight = 1.0 / (GAParams.objectives.size() - 1);

		// double objetive1 =s1.getObjective(0)==1?0.5:0;
		// double objetive2 =s2.getObjective(0)==1?0.5:0;
		//
		// for(int i=1;i<numOfObjectives;i++){
		// objetive1+=(1.0/numOfObjectives) * s1.getObjective(i);
		// objetive2+=(1.0/numOfObjectives) * s2.getObjective(i);
		// }
		double f1 = 0, f2 = 0, nf1 = 0, nf2 = 0, objective1 = 0, objective2 = 0;

		for (int i = 0; i < GAParams.objectives.size(); i++) {
			if (GAParams.objectives.get(i) == GAParams.Objective.Correctness) {
				try {
					double e1 = GAParams.errorPosition(GAParams.GetGene(s1))
							.getViolatedPropsInt().size();
					double e2 = GAParams.errorPosition(GAParams.GetGene(s2))
							.getViolatedPropsInt().size();
					f1 = (GAParams.formulas.size() - e1)
							/ GAParams.formulas.size();

					f2 = ((double) GAParams.formulas.size() - e2)
							/ GAParams.formulas.size();
				} catch (Exception e) {
					throw new RuntimeException();
				}
			} else if (GAParams.objectives.get(i) == GAParams.Objective.MaximalFeature
					||GAParams.objectives.get(i) == GAParams.Objective.MinimalFeature
//					|| GAParams.objectives.get(i) == GAParams.Objective.MaximalFeature
//					|| GAParams.objectives.get(i) == GAParams.Objective.MinimalDistance
//					|| GAParams.objectives.get(i) == GAParams.Objective.NotUsedBefore
//					|| GAParams.objectives.get(i) == GAParams.Objective.Cost
//					|| GAParams.objectives.get(i) == GAParams.Objective.Defects
					) {
				
				double e1=s1.getObjective(i);
				double e2=s2.getObjective(i);
				double d1 = (GAParams.GenomeSize - e1)
						/ GAParams.GenomeSize;
				double d2 = (GAParams.GenomeSize - e2)
						/ GAParams.GenomeSize;
				nf1 += weight * d1;
				nf2 += weight * d2;

			}

		}

		objective1 = 0.51 * f1 + 0.49 * nf1;//0.5 * f1 + 0.5 * nf1;
		objective2 = 0.51 * f2 + 0.49 * nf2;//0.5 * f2 + 0.5 * nf2;

		if (ascendingOrder_) {
			if (objective1 < objective2) {
				return -1;
			} else if (objective1 > objective2) {
				return 1;
			} else {
				return 0;
			}
		} else {
			if (objective1 < objective2) {
				return 1;
			} else if (objective1 > objective2) {
				return -1;
			} else {
				return 0;
			}
		}
	} // compare
} // ObjectiveComparator
