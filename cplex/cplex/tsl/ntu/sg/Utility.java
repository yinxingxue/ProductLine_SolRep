/**
 * 
 */
package cplex.tsl.ntu.sg;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import ilog.concert.IloException;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

/**
 * @author yinxing
 *
 */
public class Utility {
 
	private static Double[] allZero = new Double[7000];
	static{
		for(int i=0; i<  allZero.length; i++)
		{
			allZero[i] = 0.0;
		}
	}
	
	private static Double[] allOne = new Double[7000];
	static{
		for(int i=0; i<  allOne.length; i++)
		{
			allOne[i] = 1.0;
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Double[] LB= zeros(1,100);
		Double[] UB=  ones(1,100);
		System.out.println(Arrays.asList(LB));
		System.out.println(Arrays.asList(UB));
	}

	/**
	 * 
	 */
	public static int RandomInt(int minV, int maxV) {
		int max=maxV;
        int min=minV;
        Random random = new Random();

        int s = random.nextInt(max)%(max-min+1) + min;
        return s;
	}

	/**
	 * 
	 * @param xval
	 * @return
	 */
	public static Double[] toObjectArray(double[] xval) {
		Double[] x = new Double[xval.length];
		int i=0;
		for(double xv: xval)
		{
			x[i++]= CplexResultComparator.formatDouble2(xv);
		}
		return x;
	}

	/**
	 *  
	 * @param times 500
	 * @param big_lb 1500
	 * @param lb 3
	 */
	public static void scaleCopying(int times, double[] big_lb, double[] lb) {
 
		for(int i=0; i< big_lb.length; i++)
		{
			int idx = i% lb.length;
			big_lb[i] = lb[idx];
		}
	}

	/**
	 * 
	 * @param cplex 
	 * @param times
	 * @param goalExp
	 * @param x
	 * @param coeffi
	 */
	public static void scaleCopyingExpr(IloCplex cplex, int times, IloNumExpr[] goalExp, IloNumVar[] x, double[] coeffi) {

		assert goalExp.length == times * coeffi.length;
		try {
			for (int i = 0; i < goalExp.length; i++) {
				int idx = i % coeffi.length;
				goalExp[i] = cplex.prod(coeffi[idx], x[i]);
			}
		} catch (IloException e) {
			e.printStackTrace();
		}
	}

	public static double arraySum(Double[] array) {
		double sum = 0;
		for(double value: array)
		{
			sum+= value;
		}
		return sum;
	}

	public static double ArrayProducts(Double[] xvar, Double[] effiecient, int scope) {
		assert xvar.length==effiecient.length;
		double sum =0;
	    for(int i=0;i<scope;i++ )
	    {
	    	sum+= xvar[i] * effiecient[i];
	    }
	    BigDecimal bd = new BigDecimal(sum);  
        bd = bd.setScale(8, RoundingMode.HALF_UP);  
		return bd.doubleValue();
	}

	public static Double[] zeros(int pos, int length) {
		Double[] array = new Double[length];
		System.arraycopy(allZero, 0, array, pos-1, length);
		return array;
	}

	public static Double[] ones(int pos, int length) {
		Double[] array = new Double[length];
		System.arraycopy(allOne, 0, array, pos-1, length);
		return array;
	}

	public static Double[] negArray(Double[] doubles) {
		Double[] array = new Double[doubles.length];
		for(int i=0;i<doubles.length;i++)
		{
			array[i] = -1.0 * doubles[i];
		}
		return array;
	}

	public static Double[] ArraySum(double p, Double[] a, double w, Double[] b) throws Exception {
		if(a.length!=b.length){ throw new Exception("the two arrays' length are not the same!");}
		Double[] rst = new Double[b.length];
		for(int i = 0; i< a.length;i++)
		{
			rst[i]= p* a[i]+ w*b[i];
		}
		return rst;
	}
	
	public static Double[] ArraySum(Double[] a, Double[] b) throws Exception {
		if(a.length!=b.length){ throw new Exception("the two arrays' length are not the same!");}
		Double[] rst = new Double[b.length];
		for(int i = 0; i< a.length;i++)
		{
			rst[i]=a[i]+ b[i];
		}
		return rst;
	}

	public static boolean ArrayEqual(Boolean[] a, Double[] b) {
		if(a.length!=b.length)
			return false;
		for(int i=0;i< a.length;i++)
		{
			if((a[i]==true&& b[i]==1.0) ||(a[i]==false&& b[i]==0.0) )
			{
				continue;
			}
			else
			{
				return false;
			}
		}
		return true;
	}

	public static boolean ArrayEqual(Number[] object, Double[] b) {
		 
		Number[] a = (Number[]) object;
		if(a.length!=b.length)
			return false;
		for(int i=0;i< a.length;i++)
		{
			if(a[i].doubleValue() - b[i]==0 )
			{
				continue;
			}
			else
			{
				return false;
			}
		}
		return true;
	}

	public static boolean ArrayEqual(LinkedHashMap<Short, Double> linkedHashMap, Double[] b) {
	 
		for(short i=0;i< b.length;i++)
		{
			if(!linkedHashMap.containsKey(i) && b[i] == 0)
			{
				continue;
			}
			else if(linkedHashMap.get(i) ==b[i] )
			{
				continue;
			}
			else
			{
				return false;
			}
		}
		return true;
	}

	public static Double[] MatrixProduceArray(Double[][] f_in, Double[] x) {
		Double[] f_out= new Double[f_in.length];
		int i=0;
		for(Double[] target: f_in)
		{
			f_out[i++]  = ArrayProducts(target,x, x.length);
		}
		return f_out;
	}

	public static Number[] ArraySubtraction(Number[] opa1, Number[] opa2) {
		Number[] result = new Number[opa1.length]; 
		for(int i =0;i< opa1.length; i++)
		{
			result[i] = opa1[i].doubleValue()-opa2[i].doubleValue();
		}
		return result;
	}
	
	public static Double[] ArraySubtraction(Double[] opa1, Double[] opa2) {
		Double[] result = new Double[opa1.length]; 
		for(int i =0;i< opa1.length; i++)
		{
			result[i] = opa1[i].doubleValue()-opa2[i].doubleValue();
		}
		return result;
	}

	public static Double[] ArrayProduceMatrix(Double[] v_k, Double[][] y_up) {
		// v_k: 1 * m
		// y_up: n* m
		// result: 1 * n 
		Double[] result= new Double[y_up.length];
		for(int i=0; i<y_up.length;i++ )
		{
			result[i]  = ArrayProducts(v_k,y_up[i], v_k.length);
		}
		return result;
	}

	public static int[] ArrayGetFloor(Double[] lb) {
		int[] mins = new int[lb.length];
		for(int i=0;i<lb.length;i++)
		{
			if(lb[i] == Double.NEGATIVE_INFINITY)
			{
				mins[i] = Integer.MIN_VALUE;
			}
			else 
				mins[i] = (int) Math.floor(lb[i]);
		}
		return mins;
	}

	public static int[] ArrayGetCeiling(Double[] ub) {
		int[] maxs = new int[ub.length];
		for(int i=0;i<ub.length;i++)
		{
			if(ub[i] == Double.POSITIVE_INFINITY)
			{
				maxs[i] = Integer.MAX_VALUE;
			}
			else 
				maxs[i] = (int) Math.ceil(ub[i]);
		}
		return maxs;
	}

	public static Double[] generateDummyRandomDouble(int no) {
		Double[] ini = new Double[no];
		ini[0] =  0.7339 ;
		ini[1] = 0.3320  ;
		ini[2] = 0.8397  ;
		ini[3] =  0.3717;
		return ini;
	}
	
	public static Double[] generateRandomDouble(int no) {
		Double[] ini = new Double[no];
		double sum = 0;
		for(int i=0; i< no; i++)
		{
			Random rand = new Random();
			ini[i] =  (double) rand.nextInt(1000);;
			sum+= ini[i];
		}
		for(int i=0; i< no; i++)
		{
			ini[i] = ini[i]*1.0/sum;
		}
		return ini;
	}

	public static double ArrayNorm2(Double[] v_i) {
		
		double sum = 0.0;
		for(Double v: v_i)
		{
			sum+= Math.pow(v, 2);
		}
		double norm2 = Math.sqrt(sum) ;
		return norm2;
	}

	public static Double[] ArrayDivision(Double[] v_i, double norm_2) {
		Double[] results = new Double[v_i.length];
		for(int i=0; i< v_i.length; i++)
		{
			results[i] = v_i[i]/ norm_2;
		}
		return results;
	}
	
	public static Double[] ArrayMultiply(Double[] v_i, double times) {
		Double[] results = new Double[v_i.length];
		for(int i=0; i< v_i.length; i++)
		{
			results[i] = v_i[i] * times;
		}
		return results;
	}

	public static Double[][] randDistributedArray(int row, int column) {
		Double[][] randMat = new Double[row][column];
		for(int i = 0; i< row; i++ )
		{
			for(int j = 0; j< column; j++ )
			{
				randMat[i][j]= Math.random();
			}
		}
		return randMat;
	}
	
	public static Double[][] randDummyDistributedArray(int row, int column) {
		Double[][] randMat = new Double[row][column];
		for(int i = 0; i< row; i++ )
		{
			 randMat[i][0]=    0.0441 ;
			 randMat[i][1]= 0.6867  ;
			 randMat[i][2]=  0.7338;
		}
		return randMat;
	}

	public static Double[][] MatrixTranspose(Double[][] mat) {
		Double[][] mat_trans = new Double[mat[0].length][mat.length];
		for(int i=0;i<mat.length;i++)
		{
			for(int j=0;j<mat[0].length;j++)
			{
				mat_trans[j][i]= mat[i][j];
			}
		}
		return mat_trans;
	}

	public static Double[][] zeroMatrix(int row, int column) {
		Double[][]  results = new Double[row][column];
		for(int i=0; i< row; i++ )
		{
			Double[] zeroArray = zeros(1, column); 
			results[i] = zeroArray;
		}
		return results;
	}

	public static Double[][] eyeMatrix(int rank) {
		Double[][] eyes =zeroMatrix(rank,rank);
		for(int i = 0; i < rank; i++)
		{
			eyes[i][i] = 1.0;
		}
		return eyes;
	}

	public static Double[][]  negMatrix(Double[][] mat) {
		Double[][]  results = new Double[mat.length][mat[0].length];
		int i= 0;
		for(Double[] row :  mat)
		{
			results[i++] = Utility.negArray(row);
		}
		return results;
	}

	public static Double[][] twoDemensionize(Double[] d1) {
		Double[][] d2 = new Double[1][d1.length];
		System.arraycopy(d1, 0, d2[0], 0, d1.length);
		return d2;
	}

	public static double unifrnd(double lambda_l, double lambda_u) {
		double rand= new Random().nextDouble();
		double range= lambda_u- lambda_l;
		double value= rand*range+lambda_l;
		return value;
	}
	public  static double dummyUnifrnd(double lambda_l, double lambda_u)
	{
		return   6.1165;
	}

	public static Double[][] MatrixProduction(Double[][] v, Double[][] f) {
		assert v[0].length == f.length;
		Double[][] production = new Double[v.length][f[0].length];
		Double[][] tran_f = Utility.MatrixTranspose(f);
		for(int i=0;i< v.length; i++)
		{
			for(int j=0;j< tran_f.length; j++)
			{
				production[i][j] = Utility.ArrayProducts(v[i], tran_f[j], v[i].length);
			}
		}
		return production;
	}

	public static Vector<LinkedHashMap<Short, Double>> denseMatrix2SparseMatrix(
			Double[][] aA) {
		Vector<LinkedHashMap<Short, Double>> sparseMapVector= new Vector<LinkedHashMap<Short, Double>>();
		for(int i=0;i< aA.length; i++)
		{
			LinkedHashMap<Short, Double> sparseMap= new LinkedHashMap<Short, Double>();
			for(int j=0;j< aA[i].length; j++)
			{
				if(aA[i][j]!=0.0)
				{
					sparseMap.put((short)j, aA[i][j]);
				}
			}
			sparseMapVector.addElement(sparseMap);
		}
		return sparseMapVector;
	}

	public static Vector<Double> denseArray2SparseArray(Double[] bB) {
		List<Double> bList= Arrays.asList(bB);
		Vector<Double> bBVector = new Vector<Double>(bList);
		return bBVector;
	}

	public static Double[][] getFirstItem(Double[][] f, int num) {
		Double[][] part_f = new Double[num][];
		for(int i=0; i< num; i++)
		{
			part_f[i]= f[i];
		}
		return part_f;
	}

	public static Double[] getFirstItem(Double[] p_k, int num) {
		Double[] part_p_k = new Double[num];
		for(int i=0; i< num; i++)
		{
			part_p_k[i]= p_k[i];
		}
		return part_p_k;
	}
}
