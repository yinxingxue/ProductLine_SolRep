/**
 * %
% This function calculate the upper and lower limit for alpha
%
% Syntax: [ alpha_l, alpha_u ] = alpha_limit( y_ub,y_lb,No,y_up )
%
% Input Parameters:
%
%       y_ub  -   The upper bound of feasible domain
%       y_lb  -   The lower bound of feasible domain
%       
%
% Output Parameters:
%
%       alpha_l  -   The lower limit of alpha
%       alpha_u  -   The upper limit of alpha
%
% Author: Yinxing
 */

package ncgop.cplex.tsl.ntu.sg;

import java.util.LinkedHashMap;
import java.util.Vector;

import cplex.tsl.ntu.sg.CplexResult;
import cplex.tsl.ntu.sg.Utility;
import ilog.concert.IloException;

public class AlphaLimit {

	/**
	 *  Input Parameters:
	 */
	private Double[] y_ub;
	private Double[] y_lb;
	private Double[][] y_up;
	 
	/**
	 *  Output Parameters:
	 */
	private  Double[] alpha_l;
	private  Double[] alpha_u;

	public Double[] getY_ub() {
		return y_ub;
	}

	public Double[] getY_lb() {
		return y_lb;
	}

	public Double[][] getY_up() {
		return y_up;
	}

	public Double[] getAlpha_l() {
		return alpha_l;
	}

	public Double[] getAlpha_u() {
		return alpha_u;
	}

	/**
	 * 
	 * @param y_ub
	 * @param y_lb
	 * @param length
	 * @param y_up
	 */
	public AlphaLimit(Double[] y_ub, Double[] y_lb, int length, Double[][] y_up) {
		this.y_ub= y_ub;
		this.y_lb = y_lb;
		this.y_up= y_up;
		//this.alpha_l = new Double[length];
		//this.alpha_u = new Double[length];
	}

	public void calculate() throws IloException{
		
		/**  Matlab code ::  
		for i=2:No
		    if i == 2
		        v = y_up(1,:)-y_up(i,:);   
		    else
		        v = [v;y_up(1,:)-y_up(i,:)];
		    end
		end
		*/
		Vector<Double[]> v= new Vector<Double[]>();
		for(int i=1; i< this.y_up.length;i++)
		{
			Double[] result = (Double[]) Utility.ArraySubtraction(y_up[0],y_up[i]);
			v.addElement(result);
		}
		
		/**  Matlab code ::  
		% upper and lower bound
		lb_alpha = -inf*ones(1,No);
		lb = [y_lb',lb_alpha];
		ub = [y_ub',-lb_alpha];
		*/
		// upper and lower bound
		double[]  lb = new double[2*y_lb.length];
		double[]  ub = new double[2*y_ub.length];
		for(int i=0;  i< lb.length ;i++)
		{
			if(i<y_lb.length)
			{
				lb[i] =y_lb[i];
			}
			else
				lb[i] = Double.NEGATIVE_INFINITY;
		}
		for(int i=0;  i< ub.length ;i++)
		{
			if(i<y_ub.length)
			{
				ub[i] =y_ub[i];
			}
			else
				ub[i] = Double.POSITIVE_INFINITY;
		}
		
		//sum of alpha=1
		/**  Matlab code ::  
		%sum of alpha=1
		Aeq1 = [zeros(1,No),ones(1,No)];
		beq1 = 1;
		*/
		Vector<LinkedHashMap<Short, Double>> Aeq_in = new Vector<LinkedHashMap<Short, Double>>(); 
		Vector<Double> Beq_in = new Vector<Double>();
		LinkedHashMap<Short, Double> Aeq1 = new LinkedHashMap<Short, Double>();
		for(int i=0; i< y_up.length*2;i++)
		{
			if(i<y_up.length )
			{
				Aeq1.put((short)i, 0.0);
			}
			else
				Aeq1.put((short)i, 1.0);
		}
		Aeq_in.addElement(Aeq1);
		Beq_in.addElement(1.0);
		
		/**  Matlab code ::  
		%vk*(mu-p)
		for k=1:No-1
		    if k==1
		        Aeq22 = v(k,:)*y_up';
		    else 
		        Aeq22 = [Aeq22;v(k,:)*y_up'];
		    end
		end
		*/
		Vector<Double[]> aeq22 = new Vector<Double[]>();
		for(int k=0; k<v.size();k++)
		{
			Double[] v_k = v.get(k);
			Double[] results= Utility.ArrayProduceMatrix(v_k, y_up);
			Double[] real_results= Utility.negArray(results);
			aeq22.addElement(real_results);
		}
		
		/**  Matlab code ::  
		Aeq2 = [v,-Aeq22];
		beq2 = zeros(size(v,1),1);
		%Aeq,beq
		Aeq = [Aeq1;Aeq2];
		beq = [beq1;beq2];
		A = [];
		b = [];
		intcon = [];
		*/
		Vector<LinkedHashMap<Short, Double>> Aeq2 = joinTwoAeqs(v,aeq22);
		Vector<Double> Beq2 = new Vector<Double>();
		for(int i=0; i< Aeq2.size();i++)
		{
			Beq2.addElement(0.0);
		}
		Aeq_in.addAll(Aeq2);
		Beq_in.addAll(Beq2);
		
		/**  Matlab code ::  
		alpha_l = zeros(1,No);
		alpha_u = ones(1,No);
		for j=1:No
		    %f
		    f = zeros(1,2*No);
		    f(1,No+j) = 1;
		    [X_l,FVAL_l,exitflag_l] = intlinprog (f,intcon,A,b,Aeq,beq,lb,ub);
		    [X_u,FVAL_u,exitflag_u] = intlinprog (-f,intcon,A,b,Aeq,beq,lb,ub);
		    if exitflag_l==1
		        alpha_l(1,j) = FVAL_l;
		    end
		    if exitflag_u==1
		        FVAL_u = -FVAL_u;
		        alpha_u(1,j) = FVAL_u;
		    end
		end
		*/
	    alpha_l  = Utility.zeros(1,y_lb.length);
        alpha_u  = Utility.ones(1,y_ub.length);
        Vector<LinkedHashMap<Short, Double>> A_in = new Vector<LinkedHashMap<Short, Double>>(); 
		Vector<Double> B_in = new Vector<Double>();
        for(int j = 0; j<y_up.length; j++)
        {
        	  Double[] f = Utility.zeros(1,y_up.length*2);
        	  f[y_up.length+j] = 1.0;
        	  
        	  CplexResult positiveRst = NCGOP.mixintlinprog (null, null, f,A_in,B_in,Aeq_in,Beq_in, lb,ub);
        	  CplexResult negativeRst = NCGOP.mixintlinprog (null, null, Utility.negArray(f),A_in,B_in,Aeq_in,Beq_in, lb,ub);
        	  if(positiveRst.getExitflag())
        	  {
        		  alpha_l[j]= positiveRst.getFVAL();
        	  }
        	  if(negativeRst.getExitflag())
        	  {
        		  alpha_u[j]= negativeRst.getFVAL() *-1.0;
        	  }
        }
        
        System.out.println(alpha_l);
        System.out.println(alpha_u);
	}

	public static Vector<LinkedHashMap<Short, Double>> joinTwoAeqs(Vector<Double[]> aeq21, Vector<Double[]> aeq22) {
		Vector<LinkedHashMap<Short, Double>> Aeq2 = new Vector<LinkedHashMap<Short, Double>>();
		int length = aeq21.get(0).length;
		for(int i=0; i<aeq21.size();i++)
		{
			LinkedHashMap<Short, Double> eq= new LinkedHashMap<Short, Double>();
			for(int k=0; k < aeq21.get(i).length; k++)
			{
				eq.put((short)k, aeq21.get(i)[k]);
			}
			for(int k=0; k < aeq22.get(i).length; k++)
			{
				eq.put((short)(k+length), aeq22.get(i)[k]);
			}
			Aeq2.addElement(eq);
		}
		return Aeq2;
	}
}
