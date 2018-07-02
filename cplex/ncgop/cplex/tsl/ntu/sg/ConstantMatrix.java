/**
 * 
 */
package ncgop.cplex.tsl.ntu.sg;

import cplex.tsl.ntu.sg.Utility;

/**
 * @author XueYX
 *
 */
public class ConstantMatrix {

	static Double[][] V;
 
	public static void initialize(Double[][] y_up, int No)
	{
		V = new Double[No-1][No];
		for(int i =0; i< No-1;i++)
		{
			Double[] v_i = Utility.ArraySubtraction(y_up[y_up.length-1], y_up[i]);
			double norm_2 = Utility.ArrayNorm2(v_i);
			if(norm_2!=0) 
			{
				v_i = Utility.ArrayDivision(v_i,norm_2 );
			}
			V[i] = v_i;
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
