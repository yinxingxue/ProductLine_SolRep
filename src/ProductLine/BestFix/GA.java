//
// Translated by CS2J (http://www.cs2j.com): 1/3/2014 9:42:00 PM
//

package ProductLine.BestFix;

//import CS2JNet.JavaSupport.language.RefSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import ProductLine.GAParams;
import ProductLine.BestFix.Gene;
import ProductLine.BestFix.GeneComparer;
import ProductLine.FeatureModel.PFMGenerator;
import ProductLine.FeatureModel.ProductFeatureModel;

//  All code copyright (c) 2003 Barry Lapthorn
//  Website:  http://www.lapthorn.net
//
//  Disclaimer:
//  All code is provided on an "AS IS" basis, without warranty. The author
//  makes no representation, or warranty, either express or implied, with
//  respect to the code, its quality, accuracy, or fitness for a specific
//  purpose. Therefore, the author shall not have any liability to you or any
//  other person or entity with respect to any liability, loss, or damage
//  caused or alleged to have been caused directly or indirectly by the code
//  provided.  This includes, but is not limited to, interruption of service,
//  loss of data, loss of profits, or consequential damages from the use of
//  this code.
//
//
//  $Author: barry $
//  $Revision: 1.1 $
//
//  $Id: GA.cs,v 1.1 2003/08/19 20:59:05 barry Exp $
/**
* =====To change:=====
* 0. Optimize()
* 1. RankPopulation()
* 2. CreateNextGeneration()
* 3. InitialPopulation()
* 4. RouletteSelection()
 * 
 * Genetic Algorithm class
 */
public class GA {
	// ===this is for internal usage===
	// Record the sum of fitness
	private double _totalFitness;
	// After call RankPopulation, it will be sorted, from worst to best
	private ArrayList _thisGeneration;
    //Add the next generation from genetic operation the element from this_generation, after adding,
	// set this_generation = next_generation
	private ArrayList _nextGeneration;
	// To record the fitness in the table, from worst to best
	private ArrayList _fitnessTable;
	// ===this is for internal usage===
	static Random m_random = new Random();

	/**
	 * Method which starts the GA executing.
	 */
	public void optimize() throws Exception {
		if (GAParams.GenomeSize == 0)
			throw new IndexOutOfBoundsException("Gene size not set");

		// Create the fitness table.
		_fitnessTable = new ArrayList();
		_thisGeneration = new ArrayList(GAParams.GenerationSize);
		_nextGeneration = new ArrayList(GAParams.GenerationSize);
		// Gene.MutationRate = GAParams.MutationRate;
		initialPopulation();
		rankPopulation();
		for (int i = 0; i < GAParams.GenerationSize; i++) {
			createNextGeneration();
			rankPopulation();
		}
	}

	/**
	 * Rank population and sort in order of fitness.
	 */
	private void rankPopulation() throws Exception {
		_totalFitness = 0;
		for (int i = 0; i < GAParams.GAPopulationSize; i++) {
			Gene g = ((Gene) _thisGeneration.get(i));
			g.Fitness = GAParams.fitnessFunction(g.Genes);
			_totalFitness += g.Fitness;
		}
		Collections.sort(_thisGeneration, new GeneComparer());
		// ;_thisGeneration.Sort(new GeneComparer());
		// now sorted in order of fitness.
		double fitness = 0.0;
		_fitnessTable.clear();
		for (int i = 0; i < GAParams.GAPopulationSize; i++) {
			fitness += ((Gene) _thisGeneration.get(i)).Fitness;
			_fitnessTable.add((double) fitness);
		}
	}

	private void createNextGeneration() throws Exception {
		_nextGeneration.clear();
		Gene g = null;
		if (GAParams.Elitism_ForGAOnly)
			g = (Gene) _thisGeneration.get(GAParams.GAPopulationSize - 1);

		for (int i = 0; i < GAParams.GAPopulationSize; i += 2) {
			int pidx1 = rouletteSelection();
			int pidx2 = rouletteSelection();
			Gene parent1, parent2, child1, child2;
			parent1 = ((Gene) _thisGeneration.get(pidx1));
			parent2 = ((Gene) _thisGeneration.get(pidx2));
			if (m_random.nextDouble() < GAParams.CrossoverRate) {
				Gene[] genes = parent1.crossover(parent2);
				child1 = genes[0];
				child2 = genes[1];
			} else {
				child1 = parent1;
				child2 = parent2;
			}
			child1.mutate();
			child2.mutate();
			_nextGeneration.add(child1);
			_nextGeneration.add(child2);
		}
		if (GAParams.Elitism_ForGAOnly && g != null)
			_nextGeneration.set(0, g);

		_thisGeneration.clear();
		for (int i = 0; i < GAParams.GAPopulationSize; i++)
			_thisGeneration.add(_nextGeneration.get(i));
	}

    /**
    * Create the *initial* genomes by repeated calling the supplied fitness function
    */
	private void initialPopulation() throws Exception {
		// divide it into four parts (for max min avg and wrong)
		int size = GAParams.GAPopulationSize / 4;
		Gene maxGene = null;
		Gene minGene = null;
		for (int i = 0; i <= GAParams.GAPopulationSize; i++) {
			Gene g = null;
			if (!GAParams.UseCustomizedInitPopulation_ForGAOnly) {
				g = new Gene(GAParams.oldGenes.Genes.length);			
					for (int j = 0; j < g.Genes.length; j++) {
						g.Genes[j] = GAParams.rand.nextInt(2);
					}				
			} else {
				PFMGenerator generator = new PFMGenerator(
						GAParams.newFeatureModel);

				if (i < size) {

					g = new Gene(GAParams.oldGenes.Genes);

				} else if (i >= size && i < size * 2) {
					if (maxGene == null) {
						g = new Gene(GAParams.GenomeSize);
						ProductFeatureModel pfm = generator.createMaxPFM();
						g.createGenes(GAParams.convert(pfm.getRootSelFeature()
								.getSubSelFeatures(100)));
						maxGene = g;
					} else {

						g = new Gene(maxGene.Genes);

					}
				} else if (i >= size * 2 && i < size * 3) {
					if (minGene == null) {
						g = new Gene(GAParams.GenomeSize);
						ProductFeatureModel pfm = generator.createMinPFM();
						g.createGenes(GAParams.convert(pfm.getRootSelFeature()
								.getSubSelFeatures(100)));
						minGene = g;
					} else {

						g = new Gene(minGene.Genes);

					}
				} else {
					g = new Gene(GAParams.GenomeSize);
					ProductFeatureModel pfm = null;
					pfm = generator.createRdmPFM();
					g.createGenes(GAParams.convert(pfm.getRootSelFeature()
							.getSubSelFeatures(100)));
				}

			}
			_thisGeneration.add(g);

		}
	}

	// for (int i = 0; i < GAParams.PopulationSize; i++)
	// {
	// Gene g = new Gene(GAParams.GenomeSize);
	// g.CreateGenes();
	// _thisGeneration.Add(g);
	// }
	/**
	 * After ranking all the genomes by fitness, use a 'roulette wheel'
	 * selection method. This allocates a large probability of selection to
	 * those with the highest fitness.
	 * 
	 * @return Random individual biased towards highest fitness
	 */
	private int rouletteSelection() throws Exception {
		double randomFitness = m_random.nextDouble() * _totalFitness;
		int idx = -1;
		int mid;
		int first = 0;
		int last = GAParams.GAPopulationSize - 1;
		mid = (last - first) / 2;
		while (idx == -1 && first <= last) {
			// ArrayList's BinarySearch is for exact values only
			// so do this by hand.
			if (randomFitness < (Double) _fitnessTable.get(mid)) {
				last = mid;
			} else if (randomFitness > (Double) _fitnessTable.get(mid)) {
				first = mid;
			}

			mid = (first + last) / 2;
			// lies between i and i+1
			if ((last - first) == 1)
				idx = last;

		}
		return idx;
	}

	public Result getBest() throws Exception {
		Gene g = ((Gene) _thisGeneration.get(GAParams.GAPopulationSize - 1));

		int[] values = g.getValues();

		double fitness = ((double) g.Fitness);

		return new Result(values, fitness);
	}

	public Result getWorst() throws Exception {
		return getNthGenome(0);
	}

	public Result getNthGenome(int n) throws Exception {
		if (n < 0 || n > GAParams.GAPopulationSize - 1)
			throw new Exception("n too large, or too small");

		Gene g = ((Gene) _thisGeneration.get(n));
		int[] values = g.getValues();
		double fitness = g.Fitness;
		return new Result(values, fitness);
	}

}
