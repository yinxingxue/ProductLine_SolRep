/**
 * 
 */
package ncgop.cplex.tsl.ntu.sg;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import ProductLine.FeatureModel.FeatureModel;
import ProductLine.FeatureModel.LogicFeatureModel;
import cplex.tsl.ntu.sg.CplexResult;
import cplex.tsl.ntu.sg.CplexResultComparator;
import cplex.tsl.ntu.sg.CplexSolution;
import cplex.tsl.ntu.sg.MyIloCplex;
import cplex.tsl.ntu.sg.Utility;
import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloObjective;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;

/**
 * @author yinxing
 *
 */
public class NCGOP_Old {
	public static int N = 200; 
	public static int EXE_TIME = 0; 
	
	// Parameters Settings
	protected MyIloCplex cplex;
	protected IloIntVar[] xVar;
	
	protected int objNo;   // Number of objective functions
	protected  int varNv;  // Number of variables
	protected Double[][] F;
	protected Double[][] V;
	
	private List<LinkedHashMap<Short, Double>> sparseEquations;
	private List<LinkedHashMap<Short, Double>> sparseAtMostInequations;
	
	private Vector<LinkedHashMap<Short, Double>> ori_A;
	private Vector<Double> ori_B;
	
	private Vector<LinkedHashMap<Short, Double>> ori_Aeq;
	private Vector<Double> ori_Beq;
	
	private Vector<LinkedHashMap<Short, Double>> extra_A;
	private Vector<Double> extra_B;
	  
	// InequationMap inequationMap;
	protected int omittedBits;
	
	public Double[][] getF() {
		return F;
	}

	protected Set<Boolean[]> E_out;

	private UtopiaPlane utopiaPlane;
	private AlphaLimit alphaLimit;
	private SolRep pGenerator;

	public UtopiaPlane getUtopiaPlane() {
		return utopiaPlane;
	}


	public AlphaLimit getAlphaLimit() {
		return alphaLimit;
	}

	public SolRep getpGenerator() {
		return pGenerator;
	}

	public Set<Boolean[]> getE_out() {
		return E_out;
	}

	public Vector<LinkedHashMap<Short, Double>> getOri_A() {
		return ori_A;
	}


	public Vector<Double> getOri_B() {
		return ori_B;
	}


	public Vector<LinkedHashMap<Short, Double>> getOri_Aeq() {
		return ori_Aeq;
	}


	public Vector<Double> getOri_Beq() {
		return ori_Beq;
	}


	public Vector<LinkedHashMap<Short, Double>> getExtra_A() {
		return extra_A;
	}
 

	public Vector<Double> getExtra_B() {
		return extra_B;
	}
	
	public NCGOP_Old(MyIloCplex cplex, int objNo, int varNv) throws Exception {
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

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}


	public void setParas(Double[][] f2, List<LinkedHashMap<Short, Double>> sparseEquations2,
			List<LinkedHashMap<Short, Double>> sparseAtMostInequations2) {
		this.F = f2;
	    this.sparseEquations =sparseEquations2;
	    this.sparseAtMostInequations = sparseAtMostInequations2;
		
		this.extra_A = new Vector<LinkedHashMap<Short, Double>>();
		//this.extra_A.addAll(Arrays.asList(this.ori_A));
	    this.extra_B = new Vector<Double>( ); 
	    //this.extra_B.addAll(Arrays.asList(this.ori_B));
	    
	    //add contraints of  sparseEquations, sparseAtMostInequations to the cplex
	    try {
	    	builtEqualAndInequalMaps();
			initializeCplex();
		} catch (IloException e) {
			e.printStackTrace();
		}
		
	}

	@SuppressWarnings("unchecked")
	private void builtEqualAndInequalMaps() {
		// TODO Auto-generated method stub
		this.ori_A = new Vector<LinkedHashMap<Short, Double>>();
		this.ori_B = new Vector<Double>();
		
		this.ori_Aeq = new Vector<LinkedHashMap<Short, Double>>();
		this.ori_Beq = new Vector<Double>();
		
		for(int i = 0; i<this.sparseAtMostInequations.size();i++ )
		{
			LinkedHashMap<Short, Double> inequal = this.sparseAtMostInequations.get(i);
			LinkedHashMap<Short, Double> newinequal =  (LinkedHashMap<Short, Double>) inequal.clone();
			Double right = newinequal.remove((short)this.varNv);
			this.ori_A.addElement(newinequal);
			this.ori_B.addElement(right);
		}
		
		for(int i = 0; i<this.sparseEquations.size();i++ )
		{
			LinkedHashMap<Short, Double> equal = this.sparseEquations.get(i);
			LinkedHashMap<Short, Double> newequal =  (LinkedHashMap<Short, Double>) equal.clone();
			Double right = newequal.remove((short)this.varNv);
			this.ori_Aeq.addElement(newequal);
			this.ori_Beq.addElement(right);
		}
		
		assert 	this.ori_A.size() == this.ori_B.size();
		assert 	this.ori_Aeq.size() == this.ori_Beq.size();
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


	public void execute(Vector<LinkedHashMap<Short, Double>> a_in, Vector<Double> b_in, Double[][] f_in, Double[][] f_out, int k,  Set<Boolean[]> e_in,Map<String, CplexSolution> sols ) throws Exception
	{
		this.utopiaPlane = new UtopiaPlane(this.cplex,this.xVar,a_in,b_in,f_in);
		this.utopiaPlane.calculate();
		System.out.println("utopiaPlane done.");
		//System.out.println(this.utopiaPlane.getX_up());
		//System.out.println(this.utopiaPlane.getY_up());
		//System.out.println(this.utopiaPlane.getY_ub());
		//System.out.println(this.utopiaPlane.getY_lb());
		this.alphaLimit = new AlphaLimit(this.utopiaPlane.getY_ub(), this.utopiaPlane.getY_lb(), this.utopiaPlane.getY_up().length,this.utopiaPlane.getY_up());
		this.alphaLimit.calculate();
		System.out.println("alphaLimit done.");
		this.pGenerator = new EvenPGenerator1(this.utopiaPlane.getY_up(), this.varNv,N);
		this.pGenerator.setParas(this.ori_A,this.ori_B,this.ori_Aeq,this.ori_Beq,f_in);
		Vector<Double[]> points = this.pGenerator.calculate();
		System.out.println("pGenerator done.");
	    this.V = calculateV(this.utopiaPlane.getY_up(),this.objNo);
	    int counter =0;
		for(Double[] p_k: points)
		{
			System.out.println("using p_k: "+counter++);
			calculate(this.F,this.ori_A, this.ori_B, this.ori_Aeq, this.ori_Beq,this.utopiaPlane.getY_up(),p_k,this.utopiaPlane.getY_ub(), this.utopiaPlane.getY_lb(),sols);
		}
		System.out.println("Find solution num: "+sols.size());
	}
	
	private Double[][] calculateV(Double[][] y_up,int No) {
		Double[][] V= new Double[No-1][No];
		for (int i = 0; i < No; i++) {
			if (i == No - 1) {
				continue;
			}
			V[i] = Utility.ArraySubtraction(y_up[No - 1], y_up[i]);
		}
		return V;
	}


	@SuppressWarnings({ "unchecked" })
	public void calculate(Double[][] f,
			Vector<LinkedHashMap<Short, Double>> ori_A2, Vector<Double> ori_B2,
			Vector<LinkedHashMap<Short, Double>> ori_Aeq2,
			Vector<Double> ori_Beq2, Double[][] y_up, Double[] p_k,
			Double[] y_ub, Double[] y_lb, Map<String, CplexSolution> sols) throws Exception {
 
		int No = this.objNo;
		int Nv = this.varNv;
		
		/**  Matlab code :
		AA = [A;v*f];                                        % add constraints
		bb = [b;v*p_k];

		lb = zeros(1,Nv);                                      % Lower and upper bound of variables
		ub = ones(1,Nv) ;
		intcon = 1: Nv;
		*/
		
		Double[][] AA_matrix = Utility.MatrixProduction(this.V, f);
		Double[] BB_matrix= Utility.MatrixProduceArray(this.V, p_k);
		Vector<LinkedHashMap<Short, Double>> extra_A1 = Utility.denseMatrix2SparseMatrix(AA_matrix);
		Vector<Double> extra_B1 = Utility.denseArray2SparseArray(BB_matrix);
		Vector<LinkedHashMap<Short, Double>> AA = (Vector<LinkedHashMap<Short, Double>>)ori_A2.clone();
		Vector<Double> BB = (Vector<Double>) ori_B2.clone();
		AA.addAll(extra_A1);
		BB.addAll(extra_B1);
		Double[] lb = Utility.zeros(1, Nv);
		Double[] ub = Utility.ones(1, Nv);
		
		/**  Matlab code :
		for i=0:(No-1)
			    i = No-i;
			    if i==No
			        ff = f(No,:);
			    else
			        w = 1/(y_ub(i,1)-y_lb(i,1)+1);
			        ff = ff + w*f(i,:);
			    end
			    [X,FVAL,Exitflag] = intlinprog (ff,intcon,AA,bb,Aeq,beq,lb,ub);
			    if Exitflag==1
			        AA = [AA;f(i,:)];
			        bb = [bb;f(i,:)*X];
			    end
			end
		*/
		Double[] ff = new Double[No];
		CplexResult rst=null;
		for(int i =No-1; i>=0;i--)
		{
			
			if(i == No-1)
			{
				ff= f[i];
			}
			else
			{
				 double w=  1.0/(y_ub[i] - y_lb[i]+1);
				 ff= Utility.ArraySum(1.0, ff, w, f[i]);   
			}
			 rst = intlinprog (null, null, ff, AA,BB,ori_Aeq2,ori_Beq2,lb,ub);
			 if(rst.getExitflag())
			 {
				 Vector<LinkedHashMap<Short, Double>> new_A1 = Utility.denseMatrix2SparseMatrix(Utility.twoDemensionize(f[i]));
				 AA.addAll(new_A1);
				 Double new_b = Utility.ArrayProducts(f[i],rst.getXvar() , f[i].length);
				 BB.add(new_b);
			 }
		}
		
		/**  Matlab code :
		y = zeros(1,No);
		x = zeros(1,Nv);
		if Exitflag==1
		    X = round(X);
		    y = [y;(f*X)'];
		    x = [x;X'];
		end
		*/
		if(rst.getExitflag())
		{
			addSolutionXToMap(rst,sols);
		}
	}

	protected void addSolutionXToMap(CplexResult rslt, Map<String, CplexSolution> sols) {
		// f[0] = this.costEffiecient;
		// f[1] = this.varietyEffiecient;
		// f[2] = this.usedbeforeEffiecient;
		// f[3] = this.defectEffiecient; 
		Double[] xvar = rslt.getXvar();
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

	public static List<CplexResult> intlinprog(MyIloCplex cplex, IloIntVar[] xVar,  Double[] doubles,
			Vector<LinkedHashMap<Short, Double>> a_in, Vector<Double> b_in, Double[] lb, Double[] ub) throws IloException {
		// doubles as the  coefficient for target function
		IloNumExpr[] objExp = new IloNumExpr[doubles.length];
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

//		boolean exitFlag = cplex.solve(); 
		/*
	       * Since the objective value is integer, setting the absolute pool gap
	       * to 0.5 ensures that a solution worse than the incumbent (which would
	       * require its objective value to be lower than the incumbent's by 1)
	       * will be rejected. To find all feasible solutions, this and the
	       * relative pool gap (SolnPoolGap) would both be kept at their default
	       * (maximum) values.
	       */
	      cplex.setParam(IloCplex.DoubleParam.SolnPoolAGap, 0.5);
	      /*
	       * Set the pool intensity to 4 to find "all" solutions.
	       */
	      cplex.setParam(IloCplex.IntParam.SolnPoolIntensity, 4);
	      /*
	       * Set the pool capacity to hold the maximum number of solutions
	       * you really want.
	       */
	      cplex.setParam(IloCplex.IntParam.SolnPoolCapacity, 5);
	      /*
	       * Set a limit on the number of solutions to find; make it very
	       * large to find all optimal solutions. The limit needs to be larger
	       * than the pool capacity to allow for suboptimal solutions found
	       * along the way.
	       */
	      cplex.setParam(IloCplex.IntParam.PopulateLim, 10);
	      /*
	       * Set the replacement strategy to turf out suboptimal solutions
	       * if space fills up; the default is FIFO, which could keep suboptimal
	       * solutions and turf out optimal ones.
	       */
	      cplex.setParam(IloCplex.IntParam.SolnPoolReplace, 1);
	      boolean exitFlag =  cplex.populate();
	      List<CplexResult> results = new ArrayList<CplexResult>();
		//CplexResult result = null;
		if (exitFlag) {
//			double objval = cplex.getObjValue(); // ��ȡ�����е����о��߱����Ľ�ֵ��
//			double[] xval = cplex.getValues(x);
//			Double[] xvar = Utility.toObjectArray(xval);
//			result = new CplexResult(objval, xvar, exitFlag);
			results=showSolutions(cplex, x ); 
        }
		else
		{
			//result = new CplexResult(Integer.MAX_VALUE, null, exitFlag);
		}
		
		//remove the temp constraints 
        for(IloRange  extraConst: tempConsts)
        {
        	cplex.delete(extraConst);
        }
    	//remove the temp objective 
        cplex.delete(temObj);
		return results;
	}
	 
	  private static final double TOL = 1e-5;
 
	 /**
	   * Display all optimal solutions found, screening out the suboptimal ones
	   * (if any).
	 * @param cplex 
	 * @param x 
	   */
	  private static List<CplexResult> showSolutions(MyIloCplex cplex, IloNumVar[] x) throws IloException {
	    // Get the number of solutions in the pool.
	    int nsol = cplex.getSolnPoolNsolns();
	    // Create a container for the indices of optimal solutions.
	    Set<Integer> opt = new LinkedHashSet<>();
	    double best = Double.NEGATIVE_INFINITY;  // best objective value found
	    /*
	     * Check which pool solutions are truly optimal; if the pool capacity
	     * exceeds the number of optimal solutions, there may be suboptimal
	     * solutions lingering in the pool.
	     */
	    for (int i = 0; i < nsol; i++) {
	      // Get the objective value of the i-th pool solution.
	      double z = cplex.getObjValue(i);
	      if (z > best + TOL) {
	        /*
	         * If this solution is better than the previous best, the previous
	         * solutions must have been suboptimal; drop them all and count this one.
	         */
	        best = z;
	        opt.clear();
	        opt.add(i);
	      } else if (z > best - TOL) {
	        /*
	         * If this solution is within rounding tolerance of optimal, count it.
	         */
	        opt.add(i);
	      }
	    }
	    System.out.println("\n\nFound " + nsol + " solutions, of which "
	                       + opt.size() + " are optimal.");
	    
	    List<CplexResult> results = new ArrayList<CplexResult>();
	    /*
	     * Now extract the optimal solutions from the pool and print them.
	     * Note: the solution pool uses zero-based indexing.
	     */
	    int h = 1;    // cumulative index of optimal solutions
	    for (int k : opt) {  // for each index of an optimal solution ...
	      System.out.println("Solution #" + (h++) + " (obj value = " + cplex.getObjValue(k) + "):");
	      double objval = cplex.getObjValue(k); // ��ȡ�����е����о��߱����Ľ�ֵ��
	      double[] xval = cplex.getValues(x,k);
	      Double[] xvar = Utility.toObjectArray(xval);
	      CplexResult result = new CplexResult(objval, xvar, true);   
	      results.add(result);
	    }
		return results;
	  }
	  
	/**
	 * 
	 * @param cplex
	 * @param xVar
	 * @param doubles
	 * @param a_in
	 * @param b_in
	 * @param aeq_in
	 * @param beq_in
	 * @param lb
	 * @param ub
	 * @return
	 * @throws IloException
	 */
	public static CplexResult intlinprog(IloCplex cplex, IloIntVar[] xVar,  Double[] doubles,
			Vector<LinkedHashMap<Short, Double>> a_in, Vector<Double> b_in, 
			Vector<LinkedHashMap<Short, Double>> aeq_in, Vector<Double> beq_in, Double[] lb, Double[] ub) throws IloException {
		IloCplex origiCplex = cplex;
		if(cplex == null)
		{
			try {
				cplex = new IloCplex();
				cplex.setWarning(null);
				cplex.setOut(null);
				cplex.setParam(IloCplex.DoubleParam.DetTiLim, 10);
				// create model and solve it
				} catch (IloException e) {
				System.err.println("Concert exception caught: " + e);
				}
		}
		if(xVar ==null)
		{
//			IloIntVar[] intVarArray(int n,
//                    int[] min,
//                    int[] max,
//                    java.lang.String[] name)
			String[] names = new String[doubles.length];
			for(int i=0;i<names.length;i++)
			{
				names[i]= "x_"+i;
			}
			xVar =  cplex.intVarArray(doubles.length, Utility.ArrayGetFloor(lb),Utility.ArrayGetCeiling(ub), names);
		}
		
		// doubles as the  coefficient for target function
		IloNumExpr[] objExp = new IloNumExpr[doubles.length];
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
		// add the new equality constraints	 
		for (int j =  0 ; j <aeq_in.size()  ; j++) {
			LinkedHashMap<Short, Double> array =  aeq_in.get(j);
			List<IloNumExpr> equal = new ArrayList<IloNumExpr>();
			for (Short key: array.keySet()) {
					IloNumExpr itme = cplex.prod(1.0*array.get(key) , x[key]);
					equal.add(itme);
			}
			IloNumExpr itemsum = cplex.sum((IloNumExpr[]) equal.toArray(new IloNumExpr[0]));
			IloRange extra2 = cplex.addEq(itemsum, (double) beq_in.get(j));
			tempConsts.add(extra2);
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
        if(origiCplex==null)
        {
        	cplex.end();
        }
		return result;
	}
	 
	/**
	 * 
	 * @param cplex
	 * @param xVar
	 * @param doubles
	 * @param a_in
	 * @param b_in
	 * @param aeq_in
	 * @param beq_in
	 * @param lb
	 * @param ub
	 * @return
	 * @throws IloException
	 */
		public static CplexResult mixintlinprog(IloCplex cplex, IloNumVar[] xVar,  Double[] doubles,
				Vector<LinkedHashMap<Short, Double>> a_in, Vector<Double> b_in, 
				Vector<LinkedHashMap<Short, Double>> aeq_in, Vector<Double> beq_in, double[] lb, double[] ub) throws IloException {
			IloCplex origiCplex = cplex;
			if(cplex == null)
			{
				try {
					cplex = new IloCplex();
					cplex.setWarning(null);
					cplex.setOut(null);
					// create model and solve it
					} catch (IloException e) {
					System.err.println("Concert exception caught: " + e);
					}
			}
			if(xVar ==null)
			{
//				IloIntVar[] intVarArray(int n,
//	                    int[] min,
//	                    int[] max,
//	                    java.lang.String[] name)
				String[] names = new String[doubles.length];
				for(int i=0;i<names.length;i++)
				{
					names[i]= "x_"+i;
				}
				xVar =  cplex.numVarArray(doubles.length, lb,ub, names); 
				//xVar =  cplex.intVarArray(doubles.length, Utility.ArrayGetFloor(lb),Utility.ArrayGetCeiling(ub), names);
			}
			
			// doubles as the  coefficient for target function
			IloNumExpr[] objExp = new IloNumExpr[doubles.length];
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
			// add the new equality constraints	 
			for (int j =  0 ; j <aeq_in.size()  ; j++) {
				LinkedHashMap<Short, Double> array =  aeq_in.get(j);
				List<IloNumExpr> equal = new ArrayList<IloNumExpr>();
				for (Short key: array.keySet()) {
						IloNumExpr itme = cplex.prod(1.0*array.get(key) , x[key]);
						equal.add(itme);
				}
				IloNumExpr itemsum = cplex.sum((IloNumExpr[]) equal.toArray(new IloNumExpr[0]));
				IloRange extra2 = cplex.addEq(itemsum, (double) beq_in.get(j));
				tempConsts.add(extra2);
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
	        if(origiCplex==null)
	        {
	        	cplex.end();
	        }
			return result;
		}
}
