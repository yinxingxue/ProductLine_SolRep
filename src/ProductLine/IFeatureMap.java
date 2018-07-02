package ProductLine;

import java.util.HashMap;

public interface IFeatureMap {
	public HashMap<String, Integer> allFeaturesToIntegerMap();
	public HashMap<Integer, String> integerToAllFeatureMap();
}
