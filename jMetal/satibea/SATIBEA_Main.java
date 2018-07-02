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

import java.io.FileReader;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import jmetal.core.Algorithm;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.core.Variable;
import jmetal.encodings.variable.Binary;
import jmetal.util.JMException;
import org.sat4j.core.VecInt;
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
import org.sat4j.specs.IVecInt;
import org.sat4j.tools.ModelIterator;

/**
 *
 * @author chris
 */
public class SATIBEA_Main{

    private static Random r = new Random();
    private static final int SATtimeout = 1000;
    private static final long iteratorTimeout = 150000;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        try {

            String fm = args[0];
            String augment = fm + ".augment";
            String dead = fm + ".dead";
            String mandatory = fm + ".mandatory";
            String seed = fm + ".richseed";

            Problem p = new SATIBEA_Problem(fm, augment, mandatory, dead, seed);

//        Algorithm a = new ASE_SettingsIBEA(p).configureASE2013(Integer.parseInt(args[1]));
            //for the new operator:
            Algorithm a = new SATIBEA_SettingsIBEA(p).configureSATIBEA(Integer.parseInt(args[1]), fm, ((SATIBEA_Problem) p).getNumFeatures(), ((SATIBEA_Problem) p).getConstraints());
            
            

            SolutionSet pop = a.execute();

            
            for (int i = 0; i < pop.size(); i++) {
                Variable v = pop.get(i).getDecisionVariables()[0];
                System.out.println("Conf" + (i + 1) + ": " + (Binary) v + " ");

            }

            for (int i = 0; i < pop.size(); i++) {
                Variable v = pop.get(i).getDecisionVariables()[0];
                for (int j = 0; j < pop.get(i).getNumberOfObjectives(); j++) {
                    System.out.print(pop.get(i).getObjective(j) + " ");
                }
                System.out.println("");
            }
        } catch (Exception e) {
            System.out.println("Usage: java -jar satibea.jar fmDimacs timeMS\nThe .augment, .dead, .mandatory and .richseed files should be in the same directory as the FM.");
        }
    }

    public static int numViolatedConstraints(Binary b) {

        //IVecInt v = bitSetToVecInt(b);
        int s = 0;
        for (List<Integer> constraint  : SATIBEA_Problem.constraints) {
            boolean sat = false;

            for (Integer i : constraint) {
                int abs = (i < 0) ? -i : i;
                boolean sign = i > 0;
                if (b.getIth(abs - 1) == sign) {
                    sat = true;
                    break;
                }
            }
            if (!sat) {
                s++;
            }

        }

        return s;
    }

    public static int numViolatedConstraints(Binary b, HashSet<Integer> blacklist) {

        //IVecInt v = bitSetToVecInt(b);
        int s = 0;
        for (List<Integer> constraint  : SATIBEA_Problem.constraints) {
            boolean sat = false;

            for (Integer i : constraint) {
                int abs = (i < 0) ? -i : i;
                boolean sign = i > 0;
                if (b.getIth(abs - 1) == sign) {
                    sat = true;
                } else {
                    blacklist.add(abs);
                }
            }
            if (!sat) {
                s++;
            }

        }

        return s;
    }

    public static int numViolatedConstraints(boolean[] b) {

        //IVecInt v = bitSetToVecInt(b);
        int s = 0;
        for (List<Integer> constraint  : SATIBEA_Problem.constraints) {

            boolean sat = false;

            for (Integer i : constraint) {
                int abs = (i < 0) ? -i : i;
                boolean sign = i > 0;
                if (b[abs - 1] == sign) {
                    sat = true;
                    break;
                }
            }
            if (!sat) {
                s++;
            }

        }

        return s;
    }

    public boolean[] randomProduct() {

        boolean[] prod = new boolean[SATIBEA_Problem.numFeatures];
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

            //dimacsSolver.reset();
            ISolver dimacsSolver2 = SolverFactory.instance().createSolverByName("MiniSAT");
            dimacsSolver2.setTimeout(SATtimeout);

            DimacsReader dr = new DimacsReader(dimacsSolver2);
            dr.parseInstance(new FileReader(SATIBEA_Problem.fm));
            ((Solver) dimacsSolver2).setOrder(order);

            ISolver solverIterator = new ModelIterator(dimacsSolver2);
            solverIterator.setTimeoutMs(iteratorTimeout);

            if (solverIterator.isSatisfiable()) {
                int[] i = solverIterator.findModel();

                for (int j = 0; j < i.length; j++) {
                    int feat = i[j];

                    int posFeat = feat > 0 ? feat : -feat;

                    if (posFeat > 0) {
                        prod[posFeat - 1] = feat > 0;
                    }

//                    else
//                    {
//                         prod[nFeat-1] = r.nextBoolean();
//                    }
                }

            }

            //solverIterator = null;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        return prod;
    }

    public static boolean[] randomProductAssume(IVecInt ivi) {

        boolean[] prod = new boolean[SATIBEA_Problem.numFeatures];
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

            //dimacsSolver.reset();
            ISolver dimacsSolver2 = SolverFactory.instance().createSolverByName("MiniSAT");
            dimacsSolver2.setTimeout(SATtimeout);

            DimacsReader dr = new DimacsReader(dimacsSolver2);
            dr.parseInstance(new FileReader(SATIBEA_Problem.fm));
            ((Solver) dimacsSolver2).setOrder(order);

            ISolver solverIterator = new ModelIterator(dimacsSolver2);
            solverIterator.setTimeoutMs(iteratorTimeout);

            if (solverIterator.isSatisfiable()) {
                int[] i = solverIterator.findModel(ivi);
                for (int j = 0; j < i.length; j++) {
                    int feat = i[j];

                    int posFeat = feat > 0 ? feat : -feat;

                    if (posFeat > 0) {
                        prod[posFeat - 1] = feat > 0;
                    }

//                    else
//                    {
//                         prod[nFeat-1] = r.nextBoolean();
//                    }
                }

            }

            //solverIterator = null;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        return prod;
    }

}
