/**
 * 
 */
package cplex.tsl.ntu.sg;

/**
 * @author XueYX
 *
 */
public class CplexResult {
	 Double[] xvar; 
	 public Double[] getXvar() {
		return xvar;
	}
	double FVAL;
	 public double getFVAL() {
		return FVAL;
	}
	boolean Exitflag;
	public boolean getExitflag() {
		return Exitflag;
	}
	
	public CplexResult(double fVAL,  Double[] xvar, boolean exitflag)
	{
		this.FVAL = fVAL;
		this.xvar = xvar;
		this.Exitflag = exitflag;
	}
}
