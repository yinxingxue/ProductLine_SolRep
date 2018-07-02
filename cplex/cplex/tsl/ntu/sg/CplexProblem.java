/**
 * 
 */
package cplex.tsl.ntu.sg;

import ProductLine.FeatureModel.FeatureModel;
import ProductLine.FeatureModel.InequationMap;
import jmetal.core.Solution;
import jmetal.problems.AProductLine;
import jmetal.util.JMException;

/**
 * @author yinxing
 *
 */
public class CplexProblem extends AProductLine {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3929867562845664866L;
	private InequationMap inmap;
	private FeatureModel newFeatureModel;
	public InequationMap getInmap() {
		return inmap;
	}

	public void setInmap(InequationMap inmap) {
		this.inmap = inmap;
	}

	public FeatureModel getNewFeatureModel() {
		return newFeatureModel;
	}

	public void setNewFeatureModel(FeatureModel newFeatureModel) {
		this.newFeatureModel = newFeatureModel;
	}

	public CplexProblem(FeatureModel newFeatureModel )
	{
		super();
		this.setNewFeatureModel(newFeatureModel);
		this.setInmap(newFeatureModel.getInEquationMap());
	}
	
	/* (non-Javadoc)
	 * @see jmetal.core.Problem#evaluate(jmetal.core.Solution)
	 */
	@Override
	public void evaluate(Solution solution) throws JMException {
		// TODO Auto-generated method stub
		super.evaluate(solution);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
