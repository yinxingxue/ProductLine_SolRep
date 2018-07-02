/**
 * 
 */
package cplex.tsl.ntu.sg;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloObjective;
import ilog.concert.IloRange;

/**
 * @author yinxing
 *
 */
public class CWMOIP_Sparse extends CWMOIP {

	public CWMOIP_Sparse(MyIloCplex cplex, int objNo, int varNv) throws Exception {
		super(cplex, objNo, varNv);
		// TODO Auto-generated constructor stub
	}
	 
	private List<LinkedHashMap<Short, Double>> sparseEquations;
	private List<LinkedHashMap<Short, Double>> sparseAtMostInequations;
	
	private Vector<LinkedHashMap<Short, Double>> extra_A;
	private Vector<Double> extra_B;
	  
	@SuppressWarnings("unchecked")
	public List<Boolean[]>  execute(Vector<LinkedHashMap<Short, Double>> a_in, Vector<Double> b_in, Double[][] f_in, Double[][] f_out, int k,  Set<Boolean[]> e_in,Map<String, CplexSolution> sols ) throws Exception
	{
		//int ind_out = ind_in;
		//List<Boolean[]> e_out = e_in;
		List<Boolean[]> e_out = new ArrayList<Boolean[]>();
		Double[] lb  = Utility.zeros(1,this.varNv);
		Double[] ub  = Utility.ones(1,this.varNv);
		boolean  feasible_flag = false;
		if (k == 1)
		{
	        // The single-objective problem
			CplexResult rslt = intlinprog (f_out[k-1],a_in,b_in,lb,ub); // solve the single objective problem       
			Double[] X = rslt.xvar;                  // Rounding

	    	if(rslt.getExitflag()) {
	    		//ind_out=ind_out+1;                       //calculte number of interations
	    		EXE_TIME++;
	    		//ME = [ME;X.'];                           // add X and its # to solution set
	    		//E_out = cat(1,E_out,ME);
	    		//E_out = unique(E_out,'rows');
	    		addSolutionXToMap(rslt,sols );
	    		addSolutionXtoList(X,e_out);
	    	}
		}
		else
		{
			Double[] fGUB=Utility.zeros(1,k);
			Double[] fGLB=Utility.zeros(1,k);
			Double[] f_range=Utility.zeros(1,k);
			double w=1;
		    for(int i=0; i <= k-1; i++)
		    {
		    	feasible_flag= true;
		    	CplexResult rslt = intlinprog (f_out[i],a_in,b_in,lb,ub);
		        if(rslt.getExitflag())
		        {
		            fGLB[i] = 1.0* Math.round(rslt.getFVAL());
		            Double[] f_out_neg = Utility.negArray(f_out[i]);
		            CplexResult rslt2 = intlinprog (f_out_neg,a_in,b_in,lb,ub);
		            fGUB[i] = -1.0 * Math.round(rslt2.getFVAL());
		            f_range[i] = fGUB[i]-fGLB[i] +1;
		            w =  w*  Math.round(f_range[i]);
		        }
		        else
		        {
		        	feasible_flag=false;
		            break;
		        }
		    } // end of for
		    
		    if (feasible_flag==true)
		    { 		 
		            w = 1.0/w;
		            double l = fGUB[k-1];

		            //ind_out=ind_in;
		            Vector<LinkedHashMap<Short, Double>> a_out = (Vector<LinkedHashMap<Short, Double>>) a_in.clone();
		            Vector<Double> b_out = (Vector<Double>) b_in.clone();
		            while (true){
		            	// Step 1: Solve the CW(k-1)OIP problem with l      
		            	List<Boolean[]>  ME;                                         // ME is the (k-1)-objective solution set
		                Double[][] f_out1 = new Double[k-1][this.varNv];
		                for(int i = 0;i< k-1;i++ ){
		                	/** Matlab code :: f_out1 = [f_out1;f_out(i,:)+w*f_out(k,:)];   // the objective functions of CW(k-1)OIP */
		                	f_out1[i] = Utility.ArraySum(1,f_out[i],w, f_out[k-1]);
		                }
		         
		                /**  if ( a_out(size(a_out,1),:)==f_in(k,:)) */
		                if (a_out.size()>0 && Utility.ArrayEqual(a_out.get(a_out.size()-1),f_in[k-1]))
		                {
		                	/** Matlab code :: b_out[b_out.length-1]= l; */
		                	b_out.set(b_out.size()-1, l);
		                }
		                else
		                {
		                  	/**  Matlab code ::  //a_out = [a_out;f_in(k,:)];   % the linear inequality constraint matrix of CW(k-1)OIP %add f_k as a new constraint  */
		                	/**  Matlab code ::  //b_out = [b_out;l];   	 	% the linear inequality constraint vector of CW(k-1)OIP */
		                	//a_out.add(f_in[k-1]);
		                	addNewContraintToVector(a_out,f_in[k-1]);
		                	b_out.add(l);
		                }
		                ME = execute(a_out,b_out,f_in,f_out1,k-1,e_in,sols);

		                /**  Matlab code ::  I = all(ME,2) | all(~ME, 2);       %delete zero-row in ME
		                /**  Matlab code ::  ME(I,:)=[];
		                /**  Matlab code :: //break until no feasible solution */
		                
		                if(ME.size()==0)
		                    break;
		                else 
		                {  
		                	// Step 2: put ME into E, find the new l
		                 	/**  Matlab code ::  e_out =  e_out.addAll(ME);
		                   	/**  Matlab code ::  e_out = unique(E_out,'rows');
		                   	/**  Matlab code ::  fits = E_out * [f;[14.44 12.07 12.45 11.03 11.4 6.65 5.7 7.8 13.22 12.81 7.71 5.61 14.72 13.04 9.87 9.1 14.89 11.79 10.09 9.2 11.18 9.89 13.12 9.41 5.59 11.46 12.99 8.01 9.73 10.52 10.07 14.78 9.48 6.92 10.43 5.54 5.62 8.02 7.04 13.84 7.87 5.3 11.52]]';
		                    /**  Matlab code ::  l=max(f_in(k,:)*ME')-1;
		                    /**  Matlab code ::  b_out(size(b_out,1),size(b_out,2))=l; */
		                	e_out.addAll(ME);
		                	e_in.addAll(ME);
		                	l = getMaxForObjKonMe(f_in[k-1],ME);
		                	b_out.set(b_out.size()-1,  l);
		                }
		            } //end of while
		    } // end of if
		}
		return e_out;
	}
  
	private void addNewContraintToVector(Vector<LinkedHashMap<Short, Double>> a_out, Double[] doubles) {
		LinkedHashMap<Short, Double> newConst = new LinkedHashMap<Short, Double>();
		for(short i =0; i < doubles.length; i++)
		{
			if(doubles[i] == null || doubles [i] == 0)  continue;
			newConst.put((short) i, doubles[i]);
		}
		a_out.add(newConst);
	}

	protected CplexResult intlinprog(Double[] doubles,
			Vector<LinkedHashMap<Short, Double>> a_in, Vector<Double> b_in, Double[] lb, Double[] ub) throws IloException {
		// doubles as the  coefficient for target function
		IloNumExpr[] objExp = new IloNumExpr[this.varNv];
		IloNumVar[] x = xVar;
		for (int i = 0; i < x.length; i++) {
			objExp[i] = cplex.prod(doubles[i], x[i]);
		}
		IloNumExpr expr = cplex.sum(objExp);
		IloObjective temObj  = cplex.addMinimize(expr);
		
		List<IloRange> tempConsts= new ArrayList<IloRange>();
	    // add the new inequality constraints	 
		for (int j =  0 ; j <a_in.size()  ; j++) {
			LinkedHashMap<Short, Double> array =  a_in.get(j);
			List<IloNumExpr> inEqual = new ArrayList<IloNumExpr>();
			for (Short key: array.keySet()) {
					IloNumExpr itme = cplex.prod(1.0*array.get(key) , x[key]);
					inEqual.add(itme);
			}
			IloNumExpr itemsum = cplex.sum((IloNumExpr[]) inEqual.toArray(new IloNumExpr[0]));
			IloRange extra1 = cplex.addLe(itemsum, (double) b_in.get(j));
			tempConsts.add(extra1);
		}

		boolean exitFlag = cplex.solve(); 

		CplexResult result = null;
		if (exitFlag) {
			double objval = cplex.getObjValue(); // ��ȡ�����е����о��߱����Ľ�ֵ��
			double[] xval = cplex.getValues(x);
			Double[] xvar = Utility.toObjectArray(xval);
			result = new CplexResult(objval, xvar, exitFlag);
        }
		else
		{
			result = new CplexResult(Integer.MAX_VALUE, null, exitFlag);
		}
		
		//remove the temp constraints 
        for(IloRange  extraConst: tempConsts)
        {
        	cplex.delete(extraConst);
        }
    	//remove the temp objective 
        cplex.delete(temObj);
		return result;
	}

	public void setParas(Double[][] f2, List<LinkedHashMap<Short, Double>> sparseEquations, List<LinkedHashMap<Short, Double>> sparseAtMostInequations) {
		
		this.F = f2;
	    this.sparseEquations =sparseEquations;
	    this.sparseAtMostInequations = sparseAtMostInequations;
		
		this.extra_A = new Vector<LinkedHashMap<Short, Double>>();
		//this.extra_A.addAll(Arrays.asList(this.ori_A));
	    this.extra_B = new Vector<Double>( ); 
	    //this.extra_B.addAll(Arrays.asList(this.ori_B));
	    
	    //add contraints of  sparseEquations, sparseAtMostInequations to the cplex
	    try {
			initializeCplex();
		} catch (IloException e) {
			e.printStackTrace();
		}
	}

	protected void initializeCplex() throws IloException {
		cplex.setOut(null);
		cplex.setWarning(null);
 
		List<String> varNames = new ArrayList<String>();
		for(int i = 1; i<= this.varNv; i++)
		{
			varNames.add("X_"+i);
			
		}
		IloIntVar[] x = cplex.intVarArray(varNv, 0, 1, varNames.toArray(new String[0]));
		xVar = x;
		// add the normal constraints, A * X <= B
		List<IloNumExpr> inEqualconsts = new ArrayList<IloNumExpr>();
		List<IloRange> constantConsts = new ArrayList<IloRange>();
		for (LinkedHashMap<Short, Double> array: this.sparseAtMostInequations) {
			//Byte[] array = ori_A[j];
			List<IloNumExpr> inEqual = new ArrayList<IloNumExpr>();
			for (Short key: array.keySet()) {
				    if(key == varNv) continue;
					IloNumExpr itme = cplex.prod(1.0*array.get(key) , x[key]);
					inEqual.add(itme);
			}
			IloNumExpr itemsum = cplex.sum((IloNumExpr[]) inEqual.toArray(new IloNumExpr[0]));

			inEqualconsts.add(itemsum);
			IloRange inequalConst = cplex.addLe(itemsum, array.get((short)varNv));
			constantConsts.add(inequalConst);
		}

		// add the normal constraints  A_eq * X = B_eq
		List<IloNumExpr> equalconsts = new ArrayList<IloNumExpr>();
		for (LinkedHashMap<Short, Double> array: this.sparseEquations) {
			//Byte[] array = this.A_eq[j];
			List<IloNumExpr> equal = new ArrayList<IloNumExpr>();
			for (Short key: array.keySet()) {
				    if(key == varNv) continue;
					IloNumExpr itme = cplex.prod(1.0 * array.get(key), x[key]);
					equal.add(itme);
			}
			IloNumExpr itemsum = cplex.sum((IloNumExpr[]) equal.toArray(new IloNumExpr[0]));
			// IloNumExpr inEqualconst =
			// cplex.sum(itemsum,array[array.length-1]);
			equalconsts.add(itemsum);
			IloRange equalConst =  cplex.addEq(itemsum,array.get((short)varNv));
			constantConsts.add(equalConst);
		}
	
        assert  constantConsts.size() == this.sparseEquations.size()+this.sparseAtMostInequations.size() ;
	}

	public Vector<LinkedHashMap<Short, Double>> getExtra_A() {
		return extra_A;
	}
 

	public Vector<Double> getExtra_B() {
		return extra_B;
	}
 
}
