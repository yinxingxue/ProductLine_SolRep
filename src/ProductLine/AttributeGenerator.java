package ProductLine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ProductLine.IAttributeFactory.MODE;
import ProductLine.FeatureModel.FeatureModel;

public class AttributeGenerator implements IFeatureMap{

	public static String PREDEFINE_FILE = null;
	public static MODE mode = IAttributeFactory.MODE.RANDOM;
	private IAttributeFactory factory;
	HashMap<String, Integer>  allFeaturesToIntegerMap;
	HashMap<Integer, String> integerToAllFeatureMap;
	
	private String FMName;
	List<HashMap> attributeLists;
	
	public AttributeGenerator(FeatureModel newFeatureModel, HashMap<String, Integer> allFeaturesToIntegerMap, HashMap<Integer, String> integerToAllFeatureMap) throws Exception {
		this.allFeaturesToIntegerMap = allFeaturesToIntegerMap;
		this.integerToAllFeatureMap = integerToAllFeatureMap;
		this.FMName = newFeatureModel.getName();
		attributeLists= new ArrayList<HashMap>();
		if(newFeatureModel.getName().equals("ChatSystem"))
		{
			setFactory(new JChatFactory(newFeatureModel,allFeaturesToIntegerMap,integerToAllFeatureMap));
		}
		else if(newFeatureModel.getName().equals("WebPortal"))
		{
			setFactory(new WebPortalFactory(newFeatureModel,allFeaturesToIntegerMap,integerToAllFeatureMap));
		}
		else if(newFeatureModel.getName().equals("EShop"))
		{
			setFactory(new WebPortalFactory(newFeatureModel,allFeaturesToIntegerMap,integerToAllFeatureMap));
		}
		else if(newFeatureModel.getName().equals(GAParams.CaseStudy.ECos.toString()) ||
				newFeatureModel.getName().equals(GAParams.CaseStudy.UCLinux.toString()) ||
				newFeatureModel.getName().equals(GAParams.CaseStudy.LinuxX86.toString()))
		{
			setFactory(new WebPortalFactory(newFeatureModel,allFeaturesToIntegerMap,integerToAllFeatureMap));
		}
	}
	
	public void addDefects(HashMap<Integer, Integer> featuresToDefectsMap) {
		 for(String featureName: this.allFeaturesToIntegerMap.keySet())
		 {
			 int featureID =  this.allFeaturesToIntegerMap.get(featureName);
			 int defect = (int) Math.round(getFactory().createDefect(featureName, mode));
			 featuresToDefectsMap.put(featureID,defect);
		 }
		 attributeLists.add(featuresToDefectsMap);
	}
	
	public void addNotUseBefore(
			HashMap<Integer, Boolean> featuresToNotUseBeforeMap) {
		 for(String featureName: this.allFeaturesToIntegerMap.keySet())
		 {
			 int featureID =  this.allFeaturesToIntegerMap.get(featureName);
			 featuresToNotUseBeforeMap.put(featureID, getFactory().createNotUsedBefore(featureName, mode));
		 }
		 attributeLists.add(featuresToNotUseBeforeMap);
	}
	
	public void addCosts(HashMap<Integer, Double> featuresToCostMap) {
		 for(String featureName: this.allFeaturesToIntegerMap.keySet())
		 {
             int featureID =  this.allFeaturesToIntegerMap.get(featureName);
             featuresToCostMap.put(featureID,getFactory().createCost(featureName, mode));
		 }
		 attributeLists.add(featuresToCostMap);
	}
	
	public void generate(HashMap<Integer, Double> featuresToCostMap,
			HashMap<Integer, Integer> featuresToDefectsMap,
			HashMap<Integer, Boolean> featuresToNotUseBeforeMap) {
		File paraFile = new File(PREDEFINE_FILE);
		if (PREDEFINE_FILE == null || !paraFile.exists()) {
			this.addCosts(featuresToCostMap);
			this.addDefects(featuresToDefectsMap);
			this.addNotUseBefore(featuresToNotUseBeforeMap);
			save();
		} else {
			attributeLists = load();
		    featuresToCostMap.putAll(attributeLists.get(0));
			featuresToDefectsMap.putAll(attributeLists.get(1));
			featuresToNotUseBeforeMap.putAll(attributeLists.get(2));
		}
	}

	private List<HashMap> load() {
		 FileInputStream fis;
		try {
			fis = new FileInputStream(PREDEFINE_FILE);
			 ObjectInputStream ois = new ObjectInputStream(fis);  
			 HashMap map1 = (HashMap) ois.readObject();
			 HashMap map2 = (HashMap) ois.readObject();
			 HashMap map3 = (HashMap) ois.readObject();
			 attributeLists.add(map1);
			 attributeLists.add(map2);
			 attributeLists.add(map3);
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return attributeLists;  
	}

	public void save() {
		try {
			FileOutputStream fos = new FileOutputStream(this.FMName + "_"+ mode);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			for (HashMap list : attributeLists) {
				oos.writeObject(list);
			}
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public HashMap<String, Integer> allFeaturesToIntegerMap() {
		return this.allFeaturesToIntegerMap;
	}

	@Override
	public HashMap<Integer, String> integerToAllFeatureMap() {
		return this.integerToAllFeatureMap;
	}

	public IAttributeFactory getFactory() {
		return factory;
	}

	public void setFactory(IAttributeFactory factory) {
		this.factory = factory;
	}
}
