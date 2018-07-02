/**
 * 
 */
package ncgop.cplex.tsl.ntu.sg;

import java.util.Vector;

import cplex.tsl.ntu.sg.Utility;

/**
 * @author yinxing
 *
 */
public class EvenPGenerator1 extends SolRep {

	public static int DIM= 5;
	
	public EvenPGenerator1(Double[][] y_up, int varNo, int n) {
		super(y_up, varNo, n);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public Vector<Double[]> calculate() throws Exception {
		/**  Matlab code :
		d = zeros(1,4);
		for i1=0:5  
		    for i2=0:5        
		        for i3=0:5         
		            for i4=0:5               
		                dd = 1/5*[i1,i2,i3,i4]*y_up;
		                d = [d;dd];
		            end
		        end
		    end
		end
		*/
		for(int i1=0; i1<=DIM;i1++)
		{
			for(int i2=0; i2<=DIM;i2++)
			{
				for(int i3=0; i3<=DIM;i3++)
				{
					for(int i4=0; i4<=DIM;i4++)
					{
						Double[] w = new Double[y_up.length];
						w[0]= (double) i1;
						w[1]= (double) i2;
						w[2]= (double) i3;
						w[3]= (double) i4;
						w = Utility.ArrayMultiply(w, 0.2);
						Double[] dd = Utility.ArrayProduceMatrix(w, Utility.MatrixTranspose(y_up));
						P.addElement(dd);
					}
				}
			}
		}
		return P;
	}
}
