package ProductLine;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Random;

import ProductLine.FeatureModel.FeatureModel;
import ProductLine.IAttributeFactory.MODE;

public class WebPortalFactory implements IAttributeFactory {
	private FeatureModel newFeatureModel;
	private HashMap<String, Integer> allFeaturesToIntegerMap;
	private HashMap<Integer, String> integerToAllFeatureMap;
	
	public WebPortalFactory(FeatureModel newFeatureModel,
			HashMap<String, Integer> allFeaturesToIntegerMap,
			HashMap<Integer, String> integerToAllFeatureMap) {
		this.newFeatureModel = newFeatureModel;
		this.allFeaturesToIntegerMap = allFeaturesToIntegerMap;
		this.integerToAllFeatureMap = integerToAllFeatureMap;
	}

	/* (non-Javadoc)
	 * @see ProductLine.IAttributeCreator#createCost(int, ProductLine.IAttributeCreator.MODE)
	 */
	@Override
	public double createCost(int featureID, MODE mode) {
		if(mode == MODE.RANDOM)
		{
			double data= GAParams.genRandomDouble(GAParams.minCost, GAParams.maxCost);
			BigDecimal a = new BigDecimal(data);
			return a.setScale(2,2).doubleValue() ;
		}
		else if(mode == MODE.ALL_ONE)
		{
			return 1.0;
		}
		else if(mode == MODE.PREDEFINE_ONE)
		{
			return 1.0;
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see ProductLine.IAttributeCreator#createDefect(int, ProductLine.IAttributeCreator.MODE)
	 */
	@Override
	public double createDefect(int featureID, MODE mode) {
		if(mode == MODE.RANDOM)
		{
			return GAParams.getRandIntWithBinomial(GAParams.maxDefect);
		}
		else if(mode == MODE.ALL_ONE)
		{
			return 1.0;
		}
		else if(mode == MODE.PREDEFINE_ONE)
		{
			return 1.0;
		}	
			 
		return 0;
	}

	@Override
	public boolean createNotUsedBefore(int featureID, MODE mode) {
		if(mode == MODE.RANDOM)
		{
			Random rand = new Random();
			return rand.nextBoolean();
		}
		else if(mode == MODE.ALL_ONE)
		{
			return false;
		}
		else if(mode == MODE.PREDEFINE_ONE)
		{
			return false;
		}
		return false;
	}

	@Override
	public double createCost(String name, MODE mode) {
		return createCost(allFeaturesToIntegerMap.get(name),mode);
	}

	@Override
	public double createDefect(String name, MODE mode) {
		return createDefect(allFeaturesToIntegerMap.get(name),mode);
	}

	@Override
	public boolean createNotUsedBefore(String name, MODE mode) {
		return createNotUsedBefore(allFeaturesToIntegerMap.get(name),mode);
	}

}
