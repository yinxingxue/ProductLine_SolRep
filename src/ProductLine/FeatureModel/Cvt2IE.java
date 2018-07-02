/**
 * 
 */
package ProductLine.FeatureModel;

import java.util.HashMap;

/**
 * @author yinxing
 *
 */
public interface Cvt2IE {
	public void convertToInequations(HashMap<Integer, String> integerToAllFeatureMap, HashMap<Integer, Double> featuresToCostMap, HashMap<Integer, Integer> featuresToDefectsMap, HashMap<Integer, Boolean> featuresToNotUseBeforeMap) throws Exception;
}
