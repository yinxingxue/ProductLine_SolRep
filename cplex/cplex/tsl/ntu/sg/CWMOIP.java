/**
 * 
 */
package cplex.tsl.ntu.sg;

import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloObjective;
import ilog.concert.IloRange;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
 

import ProductLine.FeatureModel.FeatureModel;
import ProductLine.FeatureModel.LogicFeatureModel;

/**
 * Jan 16, 2017
 * @author XueYX
 *
 */
public class CWMOIP {

	public static int EXE_TIME = 0; 
	
	// Parameters Settings
	protected MyIloCplex cplex;
	protected IloIntVar[] xVar;
	
	protected int objNo;   // Number of objective functions
	protected  int varNv;  // Number of variables
	protected Double[][] F;
	private Double[] f_cost;
	private Byte[][] ori_A;
	private Integer[] ori_B;
	private Vector<Number[]> extra_A;
	private Vector<Number> extra_B;
	
	public Double[][] getF() {
		return F;
	}

	public Vector<Number[]> getExtraA() {
		return extra_A;
	}

	public Vector<Number> getExtraB() {
		return extra_B;
	}

	private Byte[][] A_eq;
	public Byte[][] getA_eq() {
		return A_eq;
	}

	private Integer[] B_eq;
	public Integer[] getB_eq() {
		return B_eq;
	}

	//InequationMap inequationMap;
	protected int omittedBits;
	
    protected Set<Boolean[]> E_out;
	
	public Set<Boolean[]> getE_out() {
		return E_out;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("unchecked")
	public List<Boolean[]>  execute(MyIloCplex cplex, Vector<Number[]> a_in, Vector<Number> b_in, Double[][] f_in, Double[][] f_out, int k,  Set<Boolean[]> e_in,Map<String, CplexSolution> sols ) throws Exception
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
			CplexResult rslt = intlinprog (cplex,f_out[k-1],a_in,b_in,A_eq,B_eq,lb,ub); // solve the single objective problem       
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
		    	CplexResult rslt = intlinprog (cplex,f_out[i],a_in,b_in,A_eq,B_eq,lb,ub);
		        if(rslt.getExitflag())
		        {
		            fGLB[i] = 1.0* Math.round(rslt.getFVAL());
		            Double[] f_out_neg = Utility.negArray(f_out[i]);
		            CplexResult rslt2 = intlinprog (cplex,f_out_neg,a_in,b_in,A_eq,B_eq,lb,ub);
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
		            Vector<Number[]> a_out = (Vector<Number[]>) a_in.clone();
		            Vector<Number> b_out = (Vector<Number>) b_in.clone();
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
		                	a_out.add(f_in[k-1]);
		                	b_out.add(l);
		                }
		                ME = execute(cplex, a_out,b_out,f_in,f_out1,k-1,e_in,sols);

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
  

	protected double getMaxForObjKonMe(Double[] values, List<Boolean[]> mE) {
		double maxVal = Double.NEGATIVE_INFINITY;
		for(Boolean[] sol: mE)
		{
			double sum = 0;
			for(int i=0; i< sol.length; i++)
			{
				if(sol[i]) { sum+= values[i];}
			}
			if(sum> maxVal) maxVal = sum;
		}
		long result = Math.round(maxVal);
		return result-1;
	}

	protected void addSolutionXtoList(Double[] x, List<Boolean[]> e_out2) {
		Boolean[] xVar = new Boolean[x.length];
		for(int i=0; i< xVar.length;i++)
		{
			if( x[i] == 1.0) 
			{
				xVar[i] = true;
			}
			else xVar[i] = false;
		}
		e_out2.add(xVar);
	}

	protected void addSolutionXToMap(CplexResult rslt, Map<String, CplexSolution> sols) {
		// f[0] = this.costEffiecient;
		// f[1] = this.varietyEffiecient;
		// f[2] = this.usedbeforeEffiecient;
		// f[3] = this.defectEffiecient; 
		Double[] xvar = rslt.xvar;
		double objval = rslt.getFVAL();
		int missingFeatureSize = (int)(xvar.length- omittedBits+ Utility.ArrayProducts(xvar,F[1], xvar.length- omittedBits));
		int notUsedFeatureSize = (int) (Utility.ArrayProducts(xvar,F[2],xvar.length- omittedBits));
		double defects = Utility.ArrayProducts(xvar,F[3], xvar.length- omittedBits);
		double costs = Utility.ArrayProducts(xvar,F[0], xvar.length- omittedBits);
		
		CplexSolution sol = new CplexSolution(CplexResultComparator.formatDouble2(costs),missingFeatureSize,notUsedFeatureSize,CplexResultComparator.formatDouble2(defects),xvar);
		//CplexSolution sol = new CplexSolution(objval,missingFeatureSize,notUsedFeatureSize,defects,xvar); 
		if(!sols.containsKey(sol.getSolutionID())) 
		{
			sols.put(sol.getSolutionID(), sol);
		}
		System.out.println("BestObj: " + objval + " Cost: "+ costs);
		//System.out.println("Var: " + Arrays.asList(xvar));
		
	}

	protected CplexResult intlinprog(MyIloCplex cplex, Double[] doubles,
			Vector<Number[]> a_in, Vector<Number> b_in, Byte[][] a_eq2,
			Integer[] b_eq2, Double[] lb, Double[] ub) throws IloException {
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
			Double[] array = (Double[]) a_in.get(j);
			List<IloNumExpr> inEqual = new ArrayList<IloNumExpr>();
			for (int i = 0; i < array.length; i++) {
				if (array[i] == null)
					continue;
				else {
					IloNumExpr itme = cplex.prod(1.0*array[i] , x[i]);
					inEqual.add(itme);
				}
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

	/* 
	 * not used, depressed.
	 */
	public CWMOIP(MyIloCplex cplex,
			Map<String, CplexSolution> sols, int omittedBits, double mustCost,
			IloNumVar[] x, int notUsedFeaSize, int totalDefect, IloRange cont1,
			IloRange cont2, IloRange cont3,  int objNum, int iterNum) throws IloException { 
				
	}

	public CWMOIP(MyIloCplex cplex,int objNo, int varNv) throws Exception {
		this.objNo = objNo;
		this.varNv= varNv;
		FeatureModel fm = cplex.getNewFeatureModel() ;
		if(fm instanceof LogicFeatureModel)
		{
			LogicFeatureModel lfm = (LogicFeatureModel)fm;
			omittedBits = lfm.getMandatoryNames().size()+ lfm.getMustNotInNames().size();
		}
		else
		{
			omittedBits  = 0; 
			throw new Exception("input model not LogicFeatureModel");
		}
		E_out = new LinkedHashSet<Boolean[]>();
		this.cplex = cplex;
	}

	public void setParas(Double[][] f2, Byte[][] a2, Integer[] b2,
			Byte[][] a_eq2, Integer[] b_eq2) {
		
		this.F = f2;
		this.f_cost = F[0];
		this.ori_A = a2;
		this.ori_B= b2;
		this.A_eq = a_eq2;
		this.B_eq = b_eq2;
		
		this.extra_A = new Vector<Number[]>();
		//this.extra_A.addAll(Arrays.asList(this.ori_A));
	    this.extra_B= new Vector<Number>(); 
	    //this.extra_B.addAll(Arrays.asList(this.ori_B));
	    
	    //add contraints of  ori_A and ori_B, A_eq and this.B_eq  to the cplex
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
		for (int j = 0; j <  ori_A.length; j++) {
			Byte[] array = ori_A[j];
			List<IloNumExpr> inEqual = new ArrayList<IloNumExpr>();
			for (int i = 0; i < array.length; i++) {
				if (array[i] == null)
					continue;
				else {
					IloNumExpr itme = cplex.prod(1.0*array[i] , x[i]);
					inEqual.add(itme);
				}
			}
			IloNumExpr itemsum = cplex.sum((IloNumExpr[]) inEqual.toArray(new IloNumExpr[0]));

			inEqualconsts.add(itemsum);
			IloRange inequalConst = cplex.addLe(itemsum, ori_B[j]);
			constantConsts.add(inequalConst);
		}

		// add the normal constraints  A_eq * X = B_eq
		List<IloNumExpr> equalconsts = new ArrayList<IloNumExpr>();
		for (int j = 0; j < this.A_eq.length; j++) {
			Byte[] array = this.A_eq[j];
			List<IloNumExpr> equal = new ArrayList<IloNumExpr>();
			for (int i = 0; i < array.length; i++) {
				if (array[i] == null)
					continue;
				else {
					IloNumExpr itme = cplex.prod(1.0 * array[i], x[i]);
					equal.add(itme);
				}
			}
			IloNumExpr itemsum = cplex.sum((IloNumExpr[]) equal.toArray(new IloNumExpr[0]));
			// IloNumExpr inEqualconst =
			// cplex.sum(itemsum,array[array.length-1]);
			equalconsts.add(itemsum);
			IloRange equalConst =  cplex.addEq(itemsum, this.B_eq[j]);
			constantConsts.add(equalConst);
		}
	
        assert  constantConsts.size() == this.ori_B.length+this.B_eq.length ;
	}
}
