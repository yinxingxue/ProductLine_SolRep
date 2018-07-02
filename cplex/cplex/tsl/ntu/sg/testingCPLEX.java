/**
 * 
 */
package cplex.tsl.ntu.sg;

import java.util.Arrays;
import java.util.Random;

import ilog.concert.IloException;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

/**
 * @author yinxing
 *
 */
public class testingCPLEX {

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		testCase1();
	}

	/**
	 * 
	 */
	public static void testCase1() {
		// TODO Auto-generated method stub
		try {
			IloCplex cplex = new IloCplex();
			double[] lb = { 0.0, 0.0, 0.0 };
			double[] ub = { 40.0, Double.MAX_VALUE, Double.MAX_VALUE };
			IloNumVar[] x = cplex.numVarArray(3, lb, ub);

			IloNumExpr expr = cplex.sum(x[0], cplex.prod(2.0, x[1]), cplex.prod(3.0, x[2]));
			cplex.addMaximize(expr);

			cplex.addLe(cplex.sum(cplex.negative(x[0]), x[1], x[2]), 20);
			cplex.addLe(cplex.sum(cplex.prod(1, x[0]), cplex.prod(-3, x[1]), cplex.prod(1, x[2])), 30);

			if(cplex.solve()){
				double objval = cplex.getObjValue(); //��ȡ�����е����о��߱����Ľ�ֵ��
				double[] xval = cplex.getValues(x);
				Double[] xvar = Utility.toObjectArray(xval);
				System.out.println("Obj: " + objval);
				System.out.println("Var: " + Arrays.asList(xvar));
			}
			
			// create model and solve it
		} catch (IloException e) {
			System.err.println("Concert exception caught: " + e);
		}
	}
	
	/**
	 * 
	 */
	public static void testCase2() {
		// TODO Auto-generated method stub
		try {
			int times = 5000;
			IloCplex cplex = new IloCplex();
			double[] lb = { 0.0, 0.0, 0.0 };
			double[] ub = { 40.0, Double.MAX_VALUE, Double.MAX_VALUE };
			//IloNumVar[] x = cplex.numVarArray(3, lb, ub);
			double[] big_lb = new  double[lb.length*times];
			double[] big_ub = new  double[ub.length*times];
			Utility.scaleCopying(times,big_lb,lb);
			Utility.scaleCopying(times,big_ub,ub);
			IloNumVar[] x = cplex.numVarArray(3*times, big_lb, big_ub);

			//IloNumExpr expr = cplex.sum(x[0], cplex.prod(2.0, x[1]), cplex.prod(3.0, x[2]));
			double[] coeffi = new 	double[3];
			coeffi[0]= 1;
			coeffi[1]= 2;
			coeffi[2]= 3;
			IloNumExpr[] goalExp = new IloNumExpr[x.length]; 
			Utility.scaleCopyingExpr(cplex,times,goalExp,x,coeffi);
			IloNumExpr expr = cplex.sum(goalExp);
			cplex.addMaximize(expr);

			//cplex.addLe(cplex.sum(cplex.negative(x[0]), x[1], x[2]), 20);
			for(int i= 0; i< times; i++)
			{
				int error = Utility.RandomInt(-3,3);
		     	cplex.addLe(cplex.sum(cplex.negative(x[coeffi.length*i]), x[coeffi.length*i+1], x[coeffi.length*i+2]), 20-error);
			}
			
			//cplex.addLe(cplex.sum(cplex.prod(1, x[0]), cplex.prod(-3, x[1]), cplex.prod(1, x[2])), 30);
			for(int i= 0; i< times; i++)
			{
				int error = Utility.RandomInt(-5,5);
				cplex.addLe(cplex.sum(cplex.prod(1, x[coeffi.length*i]), cplex.prod(-3, x[coeffi.length*i+1]), cplex.prod(1, x[coeffi.length*i+2])), 30+error);
			}
		 		if(cplex.solve()){
				double objval = cplex.getObjValue(); //��ȡ�����е����о��߱����Ľ�ֵ��
				double[] xval = cplex.getValues(x);
				Double[] xvar = Utility.toObjectArray(xval);
				System.out.println("Obj: " + objval);
				System.out.println("Var: " + Arrays.asList(xvar));
				//assert 202.5 *times == objval;
			}
			// create model and solve it
		} catch (IloException e) {
			System.err.println("Concert exception caught: " + e);
		}
	}
}
 