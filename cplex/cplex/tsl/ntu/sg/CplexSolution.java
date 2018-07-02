/**
 * 
 */
package cplex.tsl.ntu.sg;

/**
 * @author yinxing
 *
 */
public class CplexSolution {

	double objval;// 获取数组中的所有决策变量的解值：
	int missingFeaSize;
	int notUsedFeaSize;
	double defectRound;
	Double[] xval;;
	String solutionID;
	
	public CplexSolution(double objval, int MissfeatureSize, int notUsedFeatureSize, double defects, Double[] xvar) {
		this.objval = objval;
		missingFeaSize =MissfeatureSize;
		notUsedFeaSize =notUsedFeatureSize;
		defectRound = defects;
		this.xval= xvar;
		solutionID= MissfeatureSize+"_"+notUsedFeatureSize+"_"+defects+"_"+objval;
	}
	
	public String getSolutionID()
	{
		return this.solutionID;
	}
	
	public String toString()
	{
		return solutionID;
	}
}
