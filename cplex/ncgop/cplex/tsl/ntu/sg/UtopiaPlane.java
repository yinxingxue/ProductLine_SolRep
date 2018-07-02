/**
 * 
 */
package ncgop.cplex.tsl.ntu.sg;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import cplex.tsl.ntu.sg.CplexResult;
import cplex.tsl.ntu.sg.CplexResultComparator;
import cplex.tsl.ntu.sg.CplexSolution;
import cplex.tsl.ntu.sg.MyIloCplex;
import cplex.tsl.ntu.sg.Utility;
import ilog.concert.IloException;
import ilog.concert.IloIntVar;

/**
 * 
 * % This function calculate the utopia plane of the original problem
%
% Syntax: [x_up,y_up, y_ub] = utopia_plane(f,A,Aeq,b,beq)
%
% Input Parameters:
%
%       f     -   The objective functions
%       A     -   The linear inequality constraint matrix
%       b     -   The linear inequality constraint vector
%       Aeq   -   The linear equality constraint matrix 
%       beq   -   The linear equality constraint vector 
%
% Output Parameters:
%
%       x_up  -   The variables for anchor points
%       y_up  -   The anchor points
%
% Author: yinxing
% Date:	11-Jan-2017


 * @author yinxing
 *
 */
public class UtopiaPlane {
    /*input */
	private MyIloCplex cplex;
	private Double[][] f_in;
	private Vector<LinkedHashMap<Short, Double>> a_in;
	private Vector<Double> b_in;
	/*output */
	private Double[][] x_up;
	private Double[][] y_up;
	private Double[] y_ub;
	private Double[] y_lb;
	private IloIntVar[] xVar;
	
	public Double[][] getF_in() {
		return f_in;
	}

	public Double[][] getX_up() {
		return x_up;
	}

	public Double[][] getY_up() {
		return y_up;
	}

	public Double[] getY_ub() {
		return y_ub;
	}

	public Double[] getY_lb() {
		return y_lb;
	}

	public MyIloCplex getCplex() {
		return cplex;
	}
 
	public UtopiaPlane(MyIloCplex cplex, IloIntVar[] xVar, Vector<LinkedHashMap<Short, Double>> a_in, Vector<Double> b_in, Double[][] f_in) {
		this.cplex = cplex;
		this.f_in = f_in;
		this.a_in = a_in;
		this.b_in = b_in;
		this.xVar= xVar;
		this.x_up = new Double[f_in.length][f_in[0].length];
		this.y_up = new Double[f_in.length][f_in.length];
		this.y_ub = new Double[f_in.length];
		this.y_lb = new Double[f_in.length];
	}
	
	public void calculate() throws IloException {
		
		/**  Matlab code ::  
			for i=1:No
			    ff = f(i,:);
			    X = intlinprog (ff,intcon,A,b,Aeq,beq,lb,ub);
			    X2 = intlinprog (-ff,intcon,A,b,Aeq,beq,lb,ub);
			    X = round(X);
			    X2 = round(X2);
			    y = (f*X)';
			    y1 = ff*X;
			    y2 = ff*X2;
			    if i==1
			        x_up = X';
			        y_up = y;
			        y_lb = y1
			        y_ub = y2;
			    else
			        x_up = [x_up;X'];
			        y_up = [y_up;y];
			        y_lb = [y_lb;y1];
			        y_ub = [y_ub;y2];
			    end
			end        
			*/
		Map<String, CplexSolution> utopiaSols = new LinkedHashMap<String, CplexSolution>();
		for(int i = 0; i< this.f_in.length;i++ )
		{
			Double[] target = this.f_in[i];
			List<CplexResult> results1 = NCGOP.intlinprog(cplex, xVar, target, a_in, b_in, null, null);
			CplexResult result1=results1.get(0);
			CplexSolution sol1 = resultsToSolution(result1);
			if(!utopiaSols.containsKey(sol1.getSolutionID()))
			{
				utopiaSols.put(sol1.getSolutionID(), sol1);
			}
			else if(utopiaSols.containsKey(sol1.getSolutionID()) && results1.size()>1)
			{
				for(int j=1; j<  results1.size(); j++)
				{
					result1= results1.get(j);
					CplexSolution sol2 = resultsToSolution(result1);
					if(!utopiaSols.containsKey(sol2.getSolutionID()))
					{
						utopiaSols.put(sol2.getSolutionID(), sol2);
						break;
					}
				}
			}
			Double[] X = result1.getXvar();
			System.arraycopy(X,0,x_up[i],0,X.length);
			Double[] y = Utility.MatrixProduceArray(f_in,X);		
			System.arraycopy(y,0,y_up[i],0,y.length);
			double y1 = result1.getFVAL();
			y_lb[i] = y1;
			List<CplexResult> results2 = NCGOP.intlinprog(cplex, xVar, Utility.negArray(target), a_in, b_in, null, null);
			CplexResult result2=results2.get(0);
			Double[] X2 = result2.getXvar();
			double y2 = result2.getFVAL() * (-1.0);
			y_ub[i] = y2;
		}
	}
	
	protected CplexSolution resultsToSolution(CplexResult rslt) {
		// f[0] = this.costEffiecient;
		// f[1] = this.varietyEffiecient;
		// f[2] = this.usedbeforeEffiecient;
		// f[3] = this.defectEffiecient; 
		Double[][] F = this.f_in;
		Double[] xvar = rslt.getXvar();
		double objval = rslt.getFVAL();
		int missingFeatureSize = (int)(xvar.length+ Utility.ArrayProducts(xvar,F[1], xvar.length));
		int notUsedFeatureSize = (int) (Utility.ArrayProducts(xvar,F[2],xvar.length));
		double defects = Utility.ArrayProducts(xvar,F[3], xvar.length);
		double costs = Utility.ArrayProducts(xvar,F[0], xvar.length);
		
		CplexSolution sol = new CplexSolution(CplexResultComparator.formatDouble2(costs),missingFeatureSize,notUsedFeatureSize,CplexResultComparator.formatDouble2(defects),xvar);
		return sol;
	}
}
