/**
 * author: Yinxing Xue
 * data: Aug 9th 2014.
 */
package ProductLine.FeatureModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import ProductLine.LogicFormula.And;
import ProductLine.LogicFormula.LogicFormula;
import ProductLine.LogicFormula.Not;

public class DimacsModelAdapter {

	private String filePath;
	private int featureNum;
	private LinkedHashSet<Feature> featureSet;
	private RootFeature rootFeature;
	private int depth;
	private HashMap<String, Integer> VarToInt;
	private HashMap<Integer, Feature> IntToFeature;
	private LinkedHashSet<ProductLine.LogicFormula.LogicFormula> logicFormula;
	
	private LinkedHashSet<Feature> mustSelFeatureSet;
	private LinkedHashSet<Feature> mustDeSelFeatureSet;
	private LinkedHashSet<Feature> fixedFeatureSet;
	private LinkedHashMap<Integer,Set<Integer>> aImpliesB; // a -> b && c
	private LinkedHashMap<Integer,Set<Integer>> bImpliesA; // b && c -> a
	
	public DimacsModelAdapter(String filePath) {
		this.filePath = filePath;
		this.featureSet = new LinkedHashSet<Feature>();
		VarToInt = new HashMap<String, Integer>();
		IntToFeature = new HashMap<Integer, Feature>();
		logicFormula = new LinkedHashSet<ProductLine.LogicFormula.LogicFormula>();
		this.fixedFeatureSet = new LinkedHashSet<Feature>();
		this.aImpliesB = new LinkedHashMap<Integer,Set<Integer>>();
		this.bImpliesA = new LinkedHashMap<Integer,Set<Integer>>();
		
		this.mustSelFeatureSet = new LinkedHashSet<Feature>();
		this.mustDeSelFeatureSet = new LinkedHashSet<Feature>();
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public int getFeatureNum() {
		return featureNum;
	}

	public void setFeatureNum(int featureNum) {
		this.featureNum = featureNum;
	}

	public LinkedHashSet<Feature> getFeatureSet() {
		return featureSet;
	}

	public void setFeatureSet(LinkedHashSet<Feature> featureSet) {
		this.featureSet = featureSet;
	}

	public RootFeature getRootFeature() {
		return rootFeature;
	}

	public void setRootFeature(RootFeature rootFeature) {
		this.rootFeature = rootFeature;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public HashMap<String, Integer> getVarToInt() {
		return VarToInt;
	}

	public void setVarToInt(HashMap<String, Integer> varToInt) {
		VarToInt = varToInt;
	}

	public HashMap<Integer, Feature> getIntToFeature() {
		return IntToFeature;
	}

	public void setIntToFeature(HashMap<Integer, Feature> intToFeature) {
		IntToFeature = intToFeature;
	}

	public LinkedHashSet<ProductLine.LogicFormula.LogicFormula> getCombinedLogicFormula() {
		return this.logicFormula;
	}

	public void setCombinedLogicFormula(
			LinkedHashSet<ProductLine.LogicFormula.LogicFormula> combinedLogicFormula) {
		this.logicFormula = combinedLogicFormula;
	}

	public LinkedHashSet<Feature> getFixedFeatureSet() {
		return fixedFeatureSet;
	}

	public void setFixedFeatureSet(LinkedHashSet<Feature> fixedFeatureSet) {
		this.fixedFeatureSet = fixedFeatureSet;
	}

	public LinkedHashSet<Feature> getMustDeSelFeatureSet() {
		return mustDeSelFeatureSet;
	}

	public void setMustDeSelFeatureSet(LinkedHashSet<Feature> mustDeSelFeatureSet) {
		this.mustDeSelFeatureSet = mustDeSelFeatureSet;
	}

	public LinkedHashSet<Feature> getMustSelFeatureSet() {
		return mustSelFeatureSet;
	}

	public void setMustSelFeatureSet(LinkedHashSet<Feature> mustSelFeatureSet) {
		this.mustSelFeatureSet = mustSelFeatureSet;
	}

	public void loadModel() {
		try {
			File file = new File(this.filePath);

			BufferedReader bf = new BufferedReader(new FileReader(file));

			String content = "";
			StringBuilder sb = new StringBuilder();
            int featureNum=0;
            int constrainNum=0;
            
            int definedFeatureNum=0;
            int definedConstrainNum=0;
			
			while (content != null) {
				content = bf.readLine();
				if (content == null) {
					break;
				}
				content = content.trim();
				if (content.equals("")) {
					continue;
				}
				
				if(content.startsWith("c "))
				{
					featureNum++;
				    if(featureNum == 1) {
				    	String[] parts = content.split(" ");
				    	RootFeature root = new RootFeature(parts[2],false);
				    	this.featureSet.add(root);
				    	this.VarToInt.put(parts[2], featureNum);
				    	this.IntToFeature.put(featureNum, root);
				    }
				    else
				    {	String[] parts = content.split(" ");
			    	    Feature feature = new CompositeFeature(parts[2],false);
			    	    this.featureSet.add(feature);
			    	    this.VarToInt.put(parts[2], featureNum);
				    	this.IntToFeature.put(featureNum, feature);
				    }
				}
				else if(content.startsWith("p "))
				{
					String[] parts = content.split(" ");
					definedFeatureNum = Integer.parseInt(parts[2]);
			        definedConstrainNum = Integer.parseInt(parts[3]);
				}
				else
				{
					constrainNum++;
					String[] parts = content.split(" ");
					ArrayList<ProductLine.LogicFormula.LogicFormula> parameters = new ArrayList<ProductLine.LogicFormula.LogicFormula>();
					ProductLine.LogicFormula.LogicFormula result = null;  
					if(parts.length == 2){
						  ProductLine.LogicFormula.LogicFormula oneProp = null;
						  Integer idWithNot = Integer.parseInt(parts[0]); 
						  Integer id = Math.abs(idWithNot);
						  
						  ProductLine.LogicFormula.LogicFormula prop = new ProductLine.LogicFormula.Prop(this.IntToFeature.get(id).getName());
						  if(idWithNot<0)  
						  {
							  LogicFormula para = new Not(prop);
							  parameters.add(para);
							  oneProp= new And(parameters);
							  this.mustDeSelFeatureSet.add(this.IntToFeature.get(id));
						  }
						  else if(idWithNot>0)
						  {
						       parameters.add(prop);
						       oneProp= new And(parameters);
						       this.mustSelFeatureSet.add(this.IntToFeature.get(id));
						  }
						  result = oneProp;
					}
					else {
						  ProductLine.LogicFormula.LogicFormula cnf = null;
						  addedToCNFMaps(parts);
						  for(int i= 0 ; i< parts.length-1; i++)
						  {
							  Integer idWithNot = Integer.parseInt(parts[i]); 
							  Integer id = Math.abs(idWithNot);
							  
							  if(idWithNot<0)  {
								  ProductLine.LogicFormula.LogicFormula prop = new ProductLine.LogicFormula.Prop(this.IntToFeature.get(id).getName());
								  LogicFormula  oneProp= new Not(prop);
							      parameters.add(oneProp);}
							  else if(idWithNot>0){
								  ProductLine.LogicFormula.LogicFormula prop = new ProductLine.LogicFormula.Prop(this.IntToFeature.get(id).getName());
							      parameters.add(prop);
							  }  
						  }
						  cnf = new ProductLine.LogicFormula.Or(parameters);
						  result = cnf;
					}
					this.logicFormula.add(result);
				}
				sb.append(content.trim());
			}
	 
			if(featureNum!= definedFeatureNum||constrainNum!=definedConstrainNum)
			{
				//throw new Exception("The dimacs format's conten is not consistent!");
			}
			bf.close();
			sb.toString();
		} catch (IOException e) {

		} catch (Exception e) {
	 		e.printStackTrace();
		}
	}

	private void addedToCNFMaps(String[] parts) {
		 // case 1: a => b ; then -a || b
		 if(parts.length ==3)
		 {
			  Integer id1 = Integer.parseInt(parts[0]); 
			  Integer id2 = Integer.parseInt(parts[1]); 
			  if(id1 * id2 < 0) {
				  if(id1<0) {
					  putToAImpliesB(Math.abs(id1), id2);
					  //putToBImpliesA(id2,Math.abs(id1));
				  }
				  else
				  {
					  putToAImpliesB(Math.abs(id2), id1);
					  //putToBImpliesA(id1,Math.abs(id2));
				  }
			  }
			  else return;
		 }
		 
		 // case 2: a&&b => c ; then -a || -b || c
		 else if(parts.length >3)
		 {
			 int rightIndex = notImpliesConstrain(parts);  
			 if(rightIndex == -1) return;
		 
			 Integer rightID = Integer.parseInt(parts[rightIndex]); 
			 for(int i=0; i< parts.length-1; i++)
			 {
				 if(i==rightIndex) continue;
				 Integer leftID = Integer.parseInt(parts[i]); 
				 putToBImpliesA(rightID,Math.abs(leftID));
			 } 
		 }
	}

	/**
	 * check whether the formular is in a&&b&&C.. => x format 
	 * @param parts : part[0]... part[n-2] is all the props
	 * @return the index of the only positive prop which is the x, if not found, reture -1
	 */
	private int notImpliesConstrain(String[] parts) {
		int positivePos = -1;
		int countPostiveNum = 0 ;
		for(int i=0; i< parts.length-1; i++)
		{
			Integer id = Integer.parseInt(parts[i]); 
			if(id>0)
			{
				countPostiveNum++;
				positivePos = i;
			}
		}	
		if(countPostiveNum != 1) return -1;
		return positivePos;
	}

	private void putToAImpliesB(Integer idA, int idB) {
		 Set<Integer> rights = this.aImpliesB.get(idA);
		 if(rights == null)
		 {
			 rights = new LinkedHashSet<Integer>();
			 this.aImpliesB.put(idA, rights);
			 rights.add(idB);
		 }
		 else{
			 rights.add(idB);
		 }
	}
	
	private void putToBImpliesA(Integer idB, int idA) {
		 Set<Integer> lefts = this.bImpliesA.get(idB);
		 if(lefts == null)
		 {
			 lefts = new LinkedHashSet<Integer>();
			 this.bImpliesA.put(idB, lefts);
			 lefts.add(idA);
		 }
		 else{
			 lefts.add(idA);
		 }
	}

	public void calculateFixedFeatures() throws Exception {
		LinkedHashSet<Integer> startRoot = new LinkedHashSet<Integer>();
		startRoot.add(1);
		LinkedHashSet<Integer> foundFixedFeature = new LinkedHashSet<Integer>();
		foundFixedFeature.add(1);
		searchFixedFeatures(startRoot,foundFixedFeature);
		System.out.println(foundFixedFeature);
		for(Integer idx: foundFixedFeature)
		{
			this.fixedFeatureSet.add(this.IntToFeature.get(idx));
		}
		for(Feature feature: fixedFeatureSet)
		{
			System.out.println(feature.getName());
		}
//	 	this.fixedFeatureSet.add(this.IntToFeature.get(Integer.parseInt("1")));
//		try {
//			searchingAndFeature(this.fixedFeatureSet,this.fixedFeatureSet);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} 
	}

	public void calculateFixedFeatures(String rootFeatureName) throws Exception {
		LinkedHashSet<Integer> startRoot = new LinkedHashSet<Integer>();
		startRoot.add(this.VarToInt.get(rootFeatureName));
		LinkedHashSet<Integer> foundFixedFeature = new LinkedHashSet<Integer>();
		foundFixedFeature.add(this.VarToInt.get(rootFeatureName));
		searchFixedFeatures(startRoot, foundFixedFeature);
		// System.out.println(foundFixedFeature);
		this.fixedFeatureSet.clear();
		for (Integer idx : foundFixedFeature) {
			this.fixedFeatureSet.add(this.IntToFeature.get(idx));
		}
		// for(Feature feature: fixedFeatureSet)
		// {
		// System.out.print(feature.getName());
		// }
		// this.fixedFeatureSet.add(this.IntToFeature.get(Integer.parseInt("1")));
		// try {
		// searchingAndFeature(this.fixedFeatureSet,this.fixedFeatureSet);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}

	private void searchFixedFeatures(final Set<Integer> startRoot,
			Set<Integer> foundFixedFeature) {
		for (Integer root : startRoot) {

			Set<Integer> newFound = new LinkedHashSet<Integer>();

			// LinkedHashSet<Integer> newFound = new LinkedHashSet<Integer>();
			Set<Integer> rights = this.aImpliesB.get(root);
			Set<Integer> lefts = this.bImpliesA.get(root);

			// rights should not be null, otherwise root is an isolated feature with optinal children
			if(rights==null) continue;
			
			// first check each one in rights's this.aImpliesB mapping
			for (Integer right : rights) {
				Set<Integer> right_rights = this.aImpliesB.get(right);
				if (right_rights != null && right_rights.contains(root)
						&& !foundFixedFeature.contains(right)) {
					newFound.add(right);
					foundFixedFeature.add(right);
				}
			}

			// second, check the this.bImpliesA subsets
			if (lefts != null) {
				Set<Integer> newFoundFromMorethan3Prop = new LinkedHashSet<Integer>(
						lefts);
				newFoundFromMorethan3Prop.retainAll(rights);

				// intersection must be subset of lefts, then that is valid
				if (newFoundFromMorethan3Prop.toString().equals(
						lefts.toString())) {
					for (Integer idx : newFoundFromMorethan3Prop) {
						if (!foundFixedFeature.contains(idx)) {
							newFound.add(idx);
							foundFixedFeature.add(idx);
						}
					}
				}
			}

			if (newFound.size() > 0)
				searchFixedFeatures(newFound, foundFixedFeature);
		}
	}

	public static void main(String[] args) {
		try {
			DimacsModelAdapter adapter = new DimacsModelAdapter("data/freebsd-icse11.dimacs");
			//DimacsModelAdapter adapter = new DimacsModelAdapter("data/uClinux.dimacs");	
			//DimacsModelAdapter adapter = new DimacsModelAdapter("data/ecos-icse11.dimacs");	
		    //r is null!
			adapter.loadModel();
			//RootFeature r=adapter.getRootFeature();			
			//FeatureModel fm=new FeatureModel("eCos",r, 10000);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main1(String[] args) {
	 	
		try {
			//DimacsModelAdapter adapter = new DimacsModelAdapter("data/freebsd-icse11.dimacs");
			//DimacsModelAdapter adapter = new DimacsModelAdapter("data/uClinux.dimacs");	
			DimacsModelAdapter adapter = new DimacsModelAdapter("data/LinuxX86.dimacs");	
		    //load the model
			adapter.loadModel();
			//calculate the fixed features
			adapter.calculateFixedFeatures();
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void main2(String[] args) {
	 	
		try {
			DimacsModelAdapter adapter = new DimacsModelAdapter("data/LinuxX86.dimacs");
												
		    //load the model
			adapter.loadModel();
			//calculate the fixed features
			//adapter.calculateFixedFeatures("X86");
			// get all the constrains 
			LinkedHashSet<ProductLine.LogicFormula.LogicFormula> constrains = adapter.getCombinedLogicFormula();
			System.out.println("===All constrains===");
//			for(ProductLine.LogicFormula.LogicFormula fea: constrains) {
//				System.out.print(fea+",");
//			}
			System.out.println();
			
			
			//get all the features
 			LinkedHashSet<Feature> feas=adapter.getFeatureSet();
//			System.out.println("===All Features===");
//			for(Feature fea: feas) {
//				System.out.print(fea.getName()+",");
//			}
//			System.out.println();
			
			
			for(Integer key: adapter.getIntToFeature().keySet())
			{
		        // get the fixed features
				adapter.calculateFixedFeatures(adapter.getIntToFeature().get(key).getName());
			    feas=adapter.getFixedFeatureSet();
			    if(feas.size()>50)
			    {
			    	System.out.println(adapter.getIntToFeature().get(key).getName());
			    }
			    System.out.println("==="+adapter.getIntToFeature().get(key).getName() +"'s Fixed Features==="+feas.size());
		     	for(Feature fea: feas) {
				    System.out.print(fea.getName()+",");
			    }
			    System.out.println();
			}
			
			//get the feature that must be selected
			feas=adapter.getMustSelFeatureSet();
			System.out.println("===All Must Selected Features==="+feas.size());
			for(Feature fea: feas) {
				System.out.print(fea.getName()+",");
			}
			System.out.println();
			
			
			//get the feature that must not be selected
			feas=adapter.getMustDeSelFeatureSet();
			System.out.println("===All Must Not Selected Features==="+feas.size());
			for(Feature fea: feas) {
				System.out.print(fea.getName()+",");
			}
			System.out.println();
			
			
			// the variable to int map
			HashMap<String,Integer>map=adapter.getVarToInt();
			System.out.println("===Var To Int===");
			for(Map.Entry<String, Integer> entry : map.entrySet()) {
				System.out.print(entry.getKey()+"="+entry.getValue()+",");
			}
			System.out.println();
			
			
			// the int to feature map
			HashMap<Integer,Feature>map1=adapter.getIntToFeature();
			System.out.println("===Int To Feature===");
			for(Map.Entry<Integer,Feature> entry : map1.entrySet()) {
				System.out.print(entry.getKey()+"="+entry.getValue().getName()+",");
			}
			System.out.println();
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void loadModel(String modelPath, LogicFeatureModel logicFeatureModel) {
		try {
			File file = new File(modelPath);
			BufferedReader bf = new BufferedReader(new FileReader(file));
			String content = "";
			StringBuilder sb = new StringBuilder();
			while (content != null) {
				
			}
			bf.close();
			sb.toString();
		} catch (IOException e) {

		} catch (Exception e) {
	 		e.printStackTrace();
		}
	}
}
