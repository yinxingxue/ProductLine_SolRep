package ncgop.cplex.tsl.ntu.sg;

import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

import org.apache.commons.lang3.ArrayUtils;

import cplex.tsl.ntu.sg.CplexResult;
import cplex.tsl.ntu.sg.Utility;

public class SolRep {
	/** Input*/
	protected Double[][] y_up;
	protected Double[][] f;
	protected int varNo;
	private int n;
	private Vector<LinkedHashMap<Short, Double>> ori_A;
	private Vector<Double> ori_B;
	
	private Vector<LinkedHashMap<Short, Double>> ori_Aeq;
	private Vector<Double> ori_Beq;
	
	private Vector<LinkedHashMap<Short, Double>> extra_A;
	private Vector<Double> extra_B;
	
	private Vector<LinkedHashMap<Short, Double>> extra_Aeq;
	private Vector<Double> extra_Beq;
	
	public Double[][] getY_up() {
		return y_up;
	}

	public int getN() {
		return n;
	}

	/** Output*/
	protected Vector<Double[]> P;
	private  IloNumVar[] xVar;
	
	public SolRep(Double[][] y_up, int varNo, int n) {
		this.y_up = y_up;
		this.varNo = varNo;
		this.n = n;
		this.P = new Vector<Double[]>();
	}

	@SuppressWarnings({ "unchecked" })
	public Vector<Double[]> calculate() throws Exception {
		// TODO Auto-generated method stub
		/**  Matlab code :
		  No = size(y_up,1);                % Calculate number of objectives
		    ini = rand(1,No);
		    ini = ini/sum(ini);
		    X0 = ini*y_up;                    % get an initial point on utopia plane

		    p = X0;                           % put X0 into p
		 */
		
		int No = y_up.length;
		int Nv = this.varNo;
		
		// generate constant matrix V
		ConstantMatrix.initialize(y_up, No);
		Double[][] V = ConstantMatrix.V;
		
		//generate constant upper bounds and lower bounds
	    double[] lb = ArrayUtils.toPrimitive(Utility.zeros(1, Nv+No+1));
		 for(int k = Nv;k<lb.length; k++)
	     {
	    	 lb[k] = Double.NEGATIVE_INFINITY;
	    	 //lb = [zeros(1,Nv),-Inf*ones(1,No),-Inf];
	     }
	     double[] ub = ArrayUtils.toPrimitive(Utility.ones(1, Nv+No+1));
	     for(int k = Nv;k<ub.length; k++)
	     {
	    	 ub[k] = Double.POSITIVE_INFINITY;
			 // ub = [ones(1,Nv),Inf*ones(1,No),Inf];
	     }
	     
		
		this.extra_A = (Vector<LinkedHashMap<Short, Double>>) ori_A.clone();
		this.extra_B = (Vector<Double>) ori_B.clone();
		this.extra_Aeq =  (Vector<LinkedHashMap<Short, Double>>) ori_Aeq.clone();
		this.extra_Beq=  (Vector<Double>) ori_Beq.clone();
		Vector<LinkedHashMap<Short, Double>> Aeq1 = convertMatrixs2SparseMat(f, Utility.negMatrix(Utility.eyeMatrix(No)), Utility.zeroMatrix(No,1));
	    this.extra_Aeq.addAll(Aeq1);
	    Double[] Beq1 = Utility.zeros(1, No);
	    this.extra_Beq.addAll(Arrays.asList(Beq1));
	    IloCplex cplex = initializeCplex(Nv, No, extra_A, extra_B, extra_Aeq, extra_Beq, lb, ub);
	    
		// do the initialization of X0
		Double[] ini = Utility.generateRandomDouble(No);
		ini = Utility.ArrayDivision(ini, Utility.arraySum(ini));
		Double[] X0 = Utility.ArrayProduceMatrix(ini, Utility.MatrixTranspose(y_up));
		Double[] p = X0;
		P.add(p);
		
		/**  Matlab code :
		 %% Find the vectors
		    V = zeros(1,No);
		    for i=1:(No-1)
		        Vi = y_up(No,:)-y_up(i,:);
		        if norm(Vi) ~=0
		            Vi = Vi/norm(Vi);
		        end;
		        V = [V;Vi];
		    end
		    V = V(2:No,:);	
		*/
	
		/**  Matlab code :
		 %% Finding other N-1 points 
		    for i=1:(N-1)
		        mult = rand(1, No-1);           % uniformly distributed multiplier
		        mult = mult/norm(mult);
		        D = mult*V;                     % a random direction on utopia plane
		        
		        %% Finding the limit of lambda
		        lambda_l=0;
		        lambda_u=0;
		        load mo_sql_machine_web
		        %load original_problem
		        % [X,Y,lambda]
		        Nv = size(A,2);
		        A = [A,zeros(size(A,1),No+1)];
		        Aeq = [Aeq,zeros(size(Aeq,1),No+1)];
		        Aeq1 = [f,-eye(No),zeros(No,1)];
		        Aeq2 = [zeros(No,Nv),eye(No),-D'];
		        Aeq = [Aeq;Aeq1;Aeq2];
		        beq = [beq;zeros(No,1);X0'];
		        ff = [zeros(1,Nv+No),1];
		        intcon = [];
		        lb = [zeros(1,Nv),-Inf*ones(1,No),-Inf];
		        ub = [ones(1,Nv),Inf*ones(1,No),Inf];
		        [X_l,FVAL_l,exitflag_l] = intlinprog(ff,intcon,A,b,Aeq,beq,lb,ub);
		        [X_u,FVAL_u,exitflag_u] = intlinprog(-ff,intcon,A,b,Aeq,beq,lb,ub);
		        if exitflag_l==1
		            lambda_l = FVAL_l;
		        end
		        if exitflag_u==1
		            lambda_u = -FVAL_u;
		        end
		        
		        
		        %% randomly choose a new point
		        lambda = unifrnd(lambda_l,lambda_u);        
		        X0 = X0+lambda*D;
		        p = [p;X0];
		    end
		    */
		// Finding other N-1 points 
		for(int i =0; i< n-1;i++)
		{
			
			Double[] mult = Utility.randDistributedArray(1,No-1)[0];
			mult = Utility.ArrayDivision(mult,Utility.ArrayNorm2(mult) );
			Double[] D = Utility.ArrayProduceMatrix(mult,  Utility.MatrixTranspose(V));
			//Finding the limit of lambda
			 double lambda_l=0;
			 double lambda_u=0;
			 
		     //modify extra_Aeq
		     
		     //	  Aeq1 = [f,-eye(No),zeros(No,1)];
		     Vector<LinkedHashMap<Short, Double>> tempAeq2 = convertMatrixs2SparseMat(Utility.zeroMatrix(No,Nv),Utility.eyeMatrix(No), Utility.negMatrix(Utility.MatrixTranspose(Utility.twoDemensionize(D))));
		     //   Aeq2 = [zeros(No,Nv),eye(No),-D'];
		   
		     //this.extra_Aeq.addAll(Aeq2);
		     //   Aeq = [Aeq;Aeq1;Aeq2];
		     
		
		     Double[] Beq2 = X0;
		     Vector<Double> tempBeq2 = new Vector<Double>();
		     tempBeq2.addAll(Arrays.asList(Beq2));
		     //this.extra_Beq.addAll(Arrays.asList(Beq2));
		     //beq = [beq;zeros(No,1);X0'];
		     Double[] ff = Utility.zeros(1, Nv+No+1);
		     ff[Nv+No] = 1.0;
		     //ff = [zeros(1,Nv+No),1]; 
		     
		     //long startTime=System.currentTimeMillis();   //获取�?始时�?  
		     CplexResult positiveRst = NCGOP.mixintlinprog (cplex, this.xVar, ff,null,null,tempAeq2,tempBeq2, lb,ub);
		     //long endTime=System.currentTimeMillis(); //获取结束时间  
		     //long time1= (endTime-startTime)/1000;
		     //startTime=System.currentTimeMillis();   //获取�?始时�?  
		     CplexResult negativeRst = NCGOP.mixintlinprog (cplex, this.xVar, Utility.negArray(ff),null,null,tempAeq2,tempBeq2, lb,ub);
		     //endTime=System.currentTimeMillis(); //获取结束时间
		     //long time2= (endTime-startTime)/1000;
		     //System.out.println("for p_i�? "+ time1+"s and "+time2+"s" );  
		     
	
		     if(positiveRst.getExitflag())
		     {
		    	 lambda_l= positiveRst.getFVAL();
       	  	 }
		     if(negativeRst.getExitflag())
		     {
		    	 lambda_u = negativeRst.getFVAL() *-1.0;
		     }
		     
		     double lambda = Utility.unifrnd(lambda_l,lambda_u);
		     X0 =  Utility.ArraySum(X0,Utility.ArrayMultiply(D,lambda));
		     P.add(X0);
		}
		return P;
	}

	protected IloCplex initializeCplex(int Nv, int No, Vector<LinkedHashMap<Short, Double>> a_in,   Vector<Double> b_in,
			Vector<LinkedHashMap<Short, Double>> aeq_in,   Vector<Double> beq_in,double[] lb, double[] ub) throws IloException {
		IloCplex cplex = new IloCplex();
		cplex.setWarning(null);
		cplex.setOut(null);
		if(Nv< 2000)
		{
			cplex.setParam(IloCplex.DoubleParam.DetTiLim, 100);
			cplex.setParam(IloCplex.DoubleParam.TiLim, 100);
		}
		else
		{
			cplex.setParam(IloCplex.DoubleParam.WorkMem ,2000.0);
			cplex.setParam(IloCplex.DoubleParam.DetTiLim, 10000);
			//cplex.setParam(IloCplex.DoubleParam.TiLim, 2000);
		}

		if(xVar ==null)
		{
 			String[] names = new String[Nv+No+1];
			for(int i=0;i<names.length;i++)
			{
				names[i]= "x_"+i;
			}
			xVar =  cplex.numVarArray(Nv+No+1, lb,ub, names); 
		}
		 
		// add the normal constraints, A * X <= B
		List<IloRange> constantConsts = new ArrayList<IloRange>();
		List<IloNumExpr> inEqualconsts = new ArrayList<IloNumExpr>();
		
		  // add the new inequality constraints	 
		for (int j =  0 ; j <a_in.size()  ; j++) {
			LinkedHashMap<Short, Double> array =  a_in.get(j);
			List<IloNumExpr> inEqual = new ArrayList<IloNumExpr>();
			for (Short key: array.keySet()) {
					IloNumExpr itme = cplex.prod(1.0*array.get(key) ,xVar[key]);
					inEqual.add(itme);
			}
			IloNumExpr itemsum = cplex.sum((IloNumExpr[]) inEqual.toArray(new IloNumExpr[0]));
			inEqualconsts.add(itemsum);
			IloRange extra1 = cplex.addLe(itemsum, (double) b_in.get(j));
			constantConsts.add(extra1);
		}
	

		// add the normal constraints  A_eq * X = B_eq
		List<IloNumExpr> equalconsts = new ArrayList<IloNumExpr>();
		// add the new equality constraints	 
		for (int j =  0 ; j <aeq_in.size()  ; j++) {
			LinkedHashMap<Short, Double> array =  aeq_in.get(j);
			List<IloNumExpr> equal = new ArrayList<IloNumExpr>();
			for (Short key: array.keySet()) {
					IloNumExpr itme = cplex.prod(1.0*array.get(key) , xVar[key]);
					equal.add(itme);
			}
			IloNumExpr itemsum = cplex.sum((IloNumExpr[]) equal.toArray(new IloNumExpr[0]));
			equalconsts.add(itemsum);
			IloRange extra2 = cplex.addEq(itemsum, (double) beq_in.get(j));
			constantConsts.add(extra2);
		}
	
        assert  constantConsts.size() == inEqualconsts.size()+equalconsts.size() ;
        return cplex;
	}
	
	@SuppressWarnings({ "unchecked" })
	public Vector<Double[]> calculate_old() throws Exception {
		// TODO Auto-generated method stub
		/**  Matlab code :
		  No = size(y_up,1);                % Calculate number of objectives
		    ini = rand(1,No);
		    ini = ini/sum(ini);
		    X0 = ini*y_up;                    % get an initial point on utopia plane

		    p = X0;                           % put X0 into p
		 */
		
		int No = y_up.length;
		Double[] ini = Utility.generateRandomDouble(No);
		ini = Utility.ArrayDivision(ini, Utility.arraySum(ini));
		Double[] X0 = Utility.ArrayProduceMatrix(ini, Utility.MatrixTranspose(y_up));
		Double[] p = X0;
		P.add(p);
		
		/**  Matlab code :
		 %% Find the vectors
		    V = zeros(1,No);
		    for i=1:(No-1)
		        Vi = y_up(No,:)-y_up(i,:);
		        if norm(Vi) ~=0
		            Vi = Vi/norm(Vi);
		        end;
		        V = [V;Vi];
		    end
		    V = V(2:No,:);	
		*/
		Double[][] V = new Double[No-1][No];
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
	
		/**  Matlab code :
		 %% Finding other N-1 points 
		    for i=1:(N-1)
		        mult = rand(1, No-1);           % uniformly distributed multiplier
		        mult = mult/norm(mult);
		        D = mult*V;                     % a random direction on utopia plane
		        
		        %% Finding the limit of lambda
		        lambda_l=0;
		        lambda_u=0;
		        load mo_sql_machine_web
		        %load original_problem
		        % [X,Y,lambda]
		        Nv = size(A,2);
		        A = [A,zeros(size(A,1),No+1)];
		        Aeq = [Aeq,zeros(size(Aeq,1),No+1)];
		        Aeq1 = [f,-eye(No),zeros(No,1)];
		        Aeq2 = [zeros(No,Nv),eye(No),-D'];
		        Aeq = [Aeq;Aeq1;Aeq2];
		        beq = [beq;zeros(No,1);X0'];
		        ff = [zeros(1,Nv+No),1];
		        intcon = [];
		        lb = [zeros(1,Nv),-Inf*ones(1,No),-Inf];
		        ub = [ones(1,Nv),Inf*ones(1,No),Inf];
		        [X_l,FVAL_l,exitflag_l] = intlinprog(ff,intcon,A,b,Aeq,beq,lb,ub);
		        [X_u,FVAL_u,exitflag_u] = intlinprog(-ff,intcon,A,b,Aeq,beq,lb,ub);
		        if exitflag_l==1
		            lambda_l = FVAL_l;
		        end
		        if exitflag_u==1
		            lambda_u = -FVAL_u;
		        end
		        
		        
		        %% randomly choose a new point
		        lambda = unifrnd(lambda_l,lambda_u);        
		        X0 = X0+lambda*D;
		        p = [p;X0];
		    end
		    */
		// Finding other N-1 points 
		for(int i =0; i< n-1;i++)
		{
			this.extra_A = (Vector<LinkedHashMap<Short, Double>>) ori_A.clone();
			//this.extra_B = (Vector<Double>) ori_B.clone();
			this.extra_Aeq =  (Vector<LinkedHashMap<Short, Double>>) ori_Aeq.clone();
			this.extra_Beq=  (Vector<Double>) ori_Beq.clone();
			Double[] mult = Utility.randDistributedArray(1,No-1)[0];
			mult = Utility.ArrayDivision(mult,Utility.ArrayNorm2(mult) );
			Double[] D = Utility.ArrayProduceMatrix(mult,  Utility.MatrixTranspose(V));
			//Finding the limit of lambda
			 double lambda_l=0;
			 double lambda_u=0;
			 int Nv = this.varNo;
		     //expendSparseMatrixWithZeroColumns(this.extra_A, Nv, No+1);   
		     //expendSparseMatrixWithZeroColumns(this.extra_Aeq, Nv, No+1); 
		     //modify extra_Aeq
		     
		     Vector<LinkedHashMap<Short, Double>> Aeq1 = convertMatrixs2SparseMat(f, Utility.negMatrix(Utility.eyeMatrix(No)), Utility.zeroMatrix(No,1));
		    //		 [f,-eye(No),zeros(No,1)];
		     Vector<LinkedHashMap<Short, Double>> Aeq2 = convertMatrixs2SparseMat(Utility.zeroMatrix(No,Nv),Utility.eyeMatrix(No), Utility.negMatrix(Utility.MatrixTranspose(Utility.twoDemensionize(D))));
		     //   Aeq2 = [zeros(No,Nv),eye(No),-D'];
		     this.extra_Aeq.addAll(Aeq1);
		     this.extra_Aeq.addAll(Aeq2);
		     //   Aeq = [Aeq;Aeq1;Aeq2];
		     
		     Double[] Beq1 = Utility.zeros(1, No);
		     Double[] Beq2 = X0;
		     this.extra_Beq.addAll(Arrays.asList(Beq1));
		     this.extra_Beq.addAll(Arrays.asList(Beq2));
		     //beq = [beq;zeros(No,1);X0'];
		     Double[] ff = Utility.zeros(1, Nv+No+1);
		     ff[Nv+No] = 1.0;
		     //ff = [zeros(1,Nv+No),1];
		     double[] lb = ArrayUtils.toPrimitive(Utility.zeros(1, Nv+No+1));
		     for(int k = Nv;k<lb.length; k++)
		     {
		    	 lb[k] = Double.NEGATIVE_INFINITY;
		    	 //lb = [zeros(1,Nv),-Inf*ones(1,No),-Inf];
		     }
		     double[] ub = ArrayUtils.toPrimitive(Utility.ones(1, Nv+No+1));
		     for(int k = Nv;k<ub.length; k++)
		     {
		    	 ub[k] = Double.POSITIVE_INFINITY;
				 // ub = [ones(1,Nv),Inf*ones(1,No),Inf];
		     }
		     
		     long startTime=System.currentTimeMillis();   //获取�?始时�?  
		     CplexResult positiveRst = NCGOP.mixintlinprog (null, null, ff,this.extra_A,this.ori_B,this.extra_Aeq,this.extra_Beq, lb,ub);
		     long endTime=System.currentTimeMillis(); //获取结束时间  
		     long time1= (endTime-startTime)/1000;
		     startTime=System.currentTimeMillis();   //获取�?始时�?  
		     CplexResult negativeRst = NCGOP.mixintlinprog (null, null, Utility.negArray(ff),this.extra_A,this.ori_B,this.extra_Aeq,this.extra_Beq, lb,ub);	
		     endTime=System.currentTimeMillis(); //获取结束时间
		     long time2= (endTime-startTime)/1000;
		     
		     System.out.println("for p_i�? "+ time1+"s and "+time2+"s" );  
		     
		     if(positiveRst.getExitflag())
		     {
		    	 lambda_l= positiveRst.getFVAL();
       	  	 }
		     if(negativeRst.getExitflag())
		     {
		    	 lambda_u = negativeRst.getFVAL() *-1.0;
		     }
		     
		     double lambda = Utility.unifrnd(lambda_l,lambda_u);
		     X0 =  Utility.ArraySum(X0,Utility.ArrayMultiply(D,lambda));
		     P.add(X0);
		}
		return P;
	}

	public void setParas(Vector<LinkedHashMap<Short, Double>> ori_A2, Vector<Double> ori_B2,
			Vector<LinkedHashMap<Short, Double>> ori_Aeq2, Vector<Double> ori_Beq2,
			Double[][] f_in) {
		this.ori_A= ori_A2;
		this.ori_B = ori_B2;
		this.ori_Aeq =ori_Aeq2;
		this.ori_Beq = ori_Beq2;
		//this.extra_A = new Vector<LinkedHashMap<Short, Double>>();
		//this.extra_B = new Vector<Double>();
		this.f = f_in;
		//this.extra_Aeq = new Vector<LinkedHashMap<Short, Double>>();
		//this.extra_Beq= new Vector<Double>();
	}

	public static void expendSparseMatrixWithZeroColumns(Vector<LinkedHashMap<Short, Double>> ori_A, int startPos, int length) {
		 for(LinkedHashMap<Short, Double> map: ori_A)
		 {
			 for(int i=0; i< length; i++)
			 {
				 map.put((short)(startPos+i), 0.0);
			 }
		 }
	}
	

	public static Vector<LinkedHashMap<Short, Double>> convertMatrixs2SparseMat(Double[][] f2, Double[][] negMatrix,
			Double[][] zeroMatrix) {
		assert f2.length == negMatrix.length;
		assert negMatrix.length == zeroMatrix.length;
		
		Vector<LinkedHashMap<Short, Double>> sparseMapVector = new Vector<LinkedHashMap<Short, Double>>();
		List<Double[][]> matrixList = new ArrayList<Double[][]>();
		matrixList.add(f2);
		matrixList.add(negMatrix);
		matrixList.add(zeroMatrix);
		int totalLength = 0;
		for(int i=0; i< f2.length;i++)
		{
			totalLength =0;
			//for every raw build a LinkedHashMap<Short, Double>
			LinkedHashMap<Short, Double> sparseMap = new LinkedHashMap<Short, Double>();
			for(Double[][]  matrix: matrixList)
			{
				Double[] row = matrix[i];
				for(int j=0; j< row.length; j++)
				{
					double value = row[j];
					if(value!=0.0)  sparseMap.put((short) (j+totalLength), value);
				}
				totalLength+= row.length;
			}
			sparseMapVector.add(sparseMap);
		}
		return sparseMapVector;
	}
}
