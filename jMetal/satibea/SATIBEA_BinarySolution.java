/*
 * Author : Christopher Henard (christopher.henard@uni.lu)
 * Date : 01/03/14
 * Copyright 2013 University of Luxembourg â€?Interdisciplinary Centre for Security Reliability and Trust (SnT)
 * All rights reserved
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package satibea;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import jmetal.core.Problem;
import jmetal.core.Variable;
import jmetal.encodings.solutionType.BinarySolutionType;
import jmetal.encodings.variable.Binary;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.minisat.core.IOrder;
import org.sat4j.minisat.core.Solver;
import org.sat4j.minisat.orders.NegativeLiteralSelectionStrategy;
import org.sat4j.minisat.orders.PositiveLiteralSelectionStrategy;
import org.sat4j.minisat.orders.RandomLiteralSelectionStrategy;
import org.sat4j.minisat.orders.RandomWalkDecorator;
import org.sat4j.minisat.orders.VarOrderHeap;
import org.sat4j.reader.DimacsReader;
import org.sat4j.specs.ISolver;
import org.sat4j.tools.ModelIterator;

/**
 *
 * @author chris
 */
public class SATIBEA_BinarySolution extends BinarySolutionType {



    private String fm;
    private int nFeat;
    private List<Integer> mandatoryFeaturesIndices, deadFeaturesIndices;
    int n = 0;
    private List<Integer> seed;
    private static Random r = new Random();
    private static final int SATtimeout = 1000;
    private static final long iteratorTimeout = 150000;

    public SATIBEA_BinarySolution(Problem problem, int nFeat, String fm, List<Integer> mandatoryFeaturesIndices, List<Integer> deadFeaturesIndices,List<Integer> seed) {
        super(problem);
        this.fm = fm;
        this.nFeat = nFeat;
        this.mandatoryFeaturesIndices = mandatoryFeaturesIndices;
        this.deadFeaturesIndices = deadFeaturesIndices;
        this.seed = seed;
    }
    
    
    public boolean[] randomProduct() {

        boolean[] prod = new boolean[nFeat];
        for (int i = 0; i < prod.length; i++) {
            prod[i] = r.nextBoolean();

        }

        int rand = r.nextInt(3);

        try {
            IOrder order;
            if (rand == 0) {
                order = new RandomWalkDecorator(new VarOrderHeap(new NegativeLiteralSelectionStrategy()), 1);
            } else if (rand == 1) {
                order = new RandomWalkDecorator(new VarOrderHeap(new PositiveLiteralSelectionStrategy()), 1);
            } else {
                order = new RandomWalkDecorator(new VarOrderHeap(new RandomLiteralSelectionStrategy()), 1);
            }

            ISolver dimacsSolver = SolverFactory.instance().createSolverByName("MiniSAT");
            dimacsSolver.setTimeout(SATtimeout);
            DimacsReader dr = new DimacsReader(dimacsSolver);
            dr.parseInstance(new FileReader(fm));

            ((Solver) dimacsSolver).setOrder(order);

            ISolver solverIterator = new ModelIterator(dimacsSolver);
            solverIterator.setTimeoutMs(iteratorTimeout);

            if (solverIterator.isSatisfiable()) {
                int[] i = solverIterator.model();

                for (int j = 0; j < i.length; j++) {
                    int feat = i[j];

                    int posFeat = feat > 0 ? feat : -feat;
//                    

                    prod[posFeat - 1] = feat > 0;

//                    else
//                    {
//                         prod[nFeat-1] = r.nextBoolean();
//                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return prod;
    }

    public Variable[] createVariables() {
        Variable[] vars = new Variable[problem_.getNumberOfVariables()];

        for (int i = 0; i < vars.length; i++) {
            Binary bin = new Binary(nFeat);
            
            for (int j = 0; j < bin.getNumberOfBits(); j++) {
                bin.setIth(j, r.nextBoolean());
                
            }

            for (Integer f : this.mandatoryFeaturesIndices) {
                bin.setIth(f, true);
            }

            for (Integer f : this.deadFeaturesIndices) {
                bin.setIth(f, false);
            }

            vars[i] = bin;
            
            //the commented code below is for putting one "rich" seed
//            if (n == 0) {
//                for (Integer f : seed){
//                    boolean sign = f > 0;
//                    int index = f > 0? f :-f;
//                    index--;
//                    bin.setIth(index, sign);
//                }
//                n++;
//            }
            
            //putting 30 "random" diverse seeds
            
//            if (n<30){
//                
//                boolean[] randprod = randomProduct();
//                for (int j = 0; j < randprod.length; j++) {                    
//                    bin.setIth(j, randprod[j]);
//                    
//                }
//                n++;
//            }
            

        }
        return vars;
    }

}
