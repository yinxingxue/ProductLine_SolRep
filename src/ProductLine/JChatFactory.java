/**
 * 
 */
package ProductLine;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Random;

import ProductLine.FeatureModel.FeatureModel;

/**
 * @author yinxing
 *
 */
public class JChatFactory implements IAttributeFactory {

	private FeatureModel newFeatureModel;
	private HashMap<String, Integer> allFeaturesToIntegerMap;
	private HashMap<Integer, String> integerToAllFeatureMap;
	
	public JChatFactory(FeatureModel newFeatureModel,
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
			if(featureID == 3) 			//logging  v: 3
			{
				return 3;
			}	
			else if(featureID == 1)		// cmd v: 3
			{
				return 3;
			}	
			else if(featureID == 0)		//gui v: 2 
			{
				return 2;
			}	
			else if(featureID == 4)   //author v: 4 
			{
				return 4;
			}	
			else if(featureID == 2)		//gui2 v:4
			{
				return 4;
			}	
			else if(featureID == 6)		// encription v: 6
			{
				return 6;
			}	
			else if(featureID == 10)    // chat v: 2
			{
				return 2;
			}	
			else if(featureID == 8)   	// reverse v: 6
			{
				return 6;
			}	
			else if(featureID == 11)	// output v： 1
			{
				return 1;
			}	
			else if(featureID == 7)    // cesear v: 5
			{
				return 5;
			}	
			else if(featureID == 9)		// encription_or : 1
			{
				return 1;
			}	
			else if(featureID == 5)		// color v:5 
			{
				return 5;
			}	
		}
		else if(mode == MODE.PREDEFINE_TWO)
		{
			if(featureID == 3) 			//logging  v: 11.7
			{
				return 11.7;
			}	
			else if(featureID == 1)		// cmd v: 14.8
			{
				return 14.8;
			}	
			else if(featureID == 0)		//gui v: 11.0 
			{
				return 11.0;
			}	
			else if(featureID == 4)   //author v: 5.7 
			{
				return 5.7;
			}	
			else if(featureID == 2)		//gui2 v:6.0
			{
				return 6.0;
			}	
			else if(featureID == 6)		// encription v: 7.8
			{
				return 7.8;
			}	
			else if(featureID == 10)    // chat v: 6.9
			{
				return 6.9;
			}	
			else if(featureID == 8)   	// reverse v: 14.2
			{
				return 14.2;
			}	
			else if(featureID == 11)	// output v： 7.8
			{
				return 7.8;
			}	
			else if(featureID == 7)    // cesear v: 6.2
			{
				return 6.2;
			}	
			else if(featureID == 9)		// encription_or : 14
			{
				return 14;
			}	
			else if(featureID == 5)		// color v:5.7
			{
				return 5.7;
			}	
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
			if(featureID == 3) 			//logging  v: 4
			{
				return 4;
			}	
			else if(featureID == 1)		// cmd v: 4
			{
				return 4;
			}	
			else if(featureID == 0)		//gui v: 25
			{
				return 5;
			}	
			else if(featureID == 4)   //author v: 3
			{
				return 3;
			}	
			else if(featureID == 2)		//gui2 v:3
			{
				return 3;
			}	
			else if(featureID == 6)		// encription v: 1
			{
				return 1;
			}	
			else if(featureID == 10)    // chat v: 5
			{
				return 5;
			}	
			else if(featureID == 8)   	// reverse v: 1
			{
				return 1;
			}	
			else if(featureID == 11)	// output v： 6
			{
				return 6;
			}	
			else if(featureID == 7)    // cesear v: 2
			{
				return 2;
			}	
			else if(featureID == 9)		// encription_or : 6
			{
				return 6;
			}	
			else if(featureID == 5)		// color v:2
			{
				return 2;
			}	
		}
		else if(mode == MODE.PREDEFINE_TWO)
		{
			if(featureID == 3) 			//logging  v: 3
			{
				return 3;
			}	
			else if(featureID == 1)		// cmd v: 6
			{
				return 6;
			}	
			else if(featureID == 0)		//gui v: 7
			{
				return 7;
			}	
			else if(featureID == 4)   //author v: 4
			{
				return 4;
			}	
			else if(featureID == 2)		//gui2 v:7
			{
				return 7;
			}	
			else if(featureID == 6)		// encription v: 7
			{
				return 7;
			}	
			else if(featureID == 10)    // chat v: 5
			{
				return 5;
			}	
			else if(featureID == 8)   	// reverse v: 6
			{
				return 6;
			}	
			else if(featureID == 11)	// output v： 4
			{
				return 4;
			}	
			else if(featureID == 7)    // cesear v: 5
			{
				return 5;
			}	
			else if(featureID == 9)		// encription_or : 3
			{
				return 3;
			}	
			else if(featureID == 5)		// color v:6
			{
				return 6;
			}	
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
			if(featureID == 1 || featureID == 2   // cmd, gui2
					|| featureID == 4 || featureID == 8)		// author, reverse
			{
				return true;
			}	
			else 	// others
			{
				return false;
			}	
		}
		else if(mode == MODE.PREDEFINE_TWO)
		{
			if(featureID == 1 || featureID == 4   // cmd, author
					|| featureID == 5 || featureID == 10 || featureID == 11)		// color, reverse
			{
				return true;
			}	
			else 	// others
			{
				return false;
			}	
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
