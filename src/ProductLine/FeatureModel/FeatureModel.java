//
// Translated by CS2J (http://www.cs2j.com): 1/3/2014 9:42:01 PM
//

package ProductLine.FeatureModel;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;

import ProductLine.FeatureModel.Feature;
import ProductLine.FeatureModel.RootFeature;
import ProductLine.LogicFormula.And;
import ProductLine.LogicFormula.LogicFormula;
import ProductLine.LogicFormula.Or;
import ProductLine.LogicFormula.Prop;

public class FeatureModel implements Cvt2IE
{
    private String name;
    public String getName() throws Exception {
        return name;
    }

    public void setName(String value) throws Exception {
        name = value;
    }
    
    protected InequationMap inEquationMap;
    
	public InequationMap getInEquationMap() {
		return inEquationMap;
	}

	public void setInEquationMap(InequationMap inEquationMap) {
		this.inEquationMap = inEquationMap;
	}

    private int featureNum;
    private LinkedHashSet<Feature> featureSet = new LinkedHashSet<Feature>();
    private LinkedHashSet<ProductLine.FeatureModel.Constrain> constrainSet = new LinkedHashSet<ProductLine.FeatureModel.Constrain>();
    protected RootFeature rootFeature;
    private int depth;
    private HashMap<String,Integer> VarToInt = new HashMap<String,Integer>();
    private HashMap<Integer,Feature> IntToFeature = new HashMap<Integer,Feature>();
    private LinkedHashSet<ProductLine.LogicFormula.LogicFormula> treeContraintsSets = new LinkedHashSet<ProductLine.LogicFormula.LogicFormula>();
    private LinkedHashSet<ProductLine.LogicFormula.LogicFormula> crossTreeContraintsSets = new LinkedHashSet<ProductLine.LogicFormula.LogicFormula>();
    private ProductLine.LogicFormula.LogicFormula combinedLogicFormula;
    
    public FeatureModel(){

    }
 
    public FeatureModel(String name, RootFeature rootFeature, int depth) throws Exception {
        this.name = name;
        this.rootFeature = rootFeature;
        this.depth = depth;
        this.featureNum = getFeatureNum();
        this.featureSet = getFeatureSet();
    }

    public RootFeature getRootFeature() throws Exception {
        return this.rootFeature;
    }

    public void setRootFeature(RootFeature rootFeature) {
        this.rootFeature = rootFeature;
    }
    
    public int getFeatureNum() throws Exception {
        return this.rootFeature.getSubFeatureSize(this.depth - 1);
    }

    public LinkedHashSet<Feature> getFeatureSet() throws Exception {
        return this.rootFeature.getSubFeatures(this.depth - 1);
           
    }

    public void setConstrainSet(LinkedHashSet<ProductLine.FeatureModel.Constrain> constrainSet) throws Exception {
        this.constrainSet = constrainSet;
    }

    public HashMap<String,Integer> getVarToInt() throws Exception {
        return this.VarToInt;
    }

    public HashMap<Integer,Feature> getIntToFeature() throws Exception {
        return this.IntToFeature;
    }

    public LinkedHashSet<ProductLine.LogicFormula.LogicFormula> getTreeLogicFormular() throws Exception {
        return this.treeContraintsSets;
    }

    public LinkedHashSet<ProductLine.LogicFormula.LogicFormula> getCrossTreeLogicFormular() throws Exception {
        return this.crossTreeContraintsSets;
    }

    public ProductLine.LogicFormula.LogicFormula getCombinedLogicFormular() throws Exception {
        return this.combinedLogicFormula;
    }

    public LinkedHashSet<ProductLine.FeatureModel.Constrain> getConstrainSet() throws Exception {
        return this.constrainSet;
    }

    public void print() throws Exception {
        System.out.println("=======beginning=======");
        System.out.print(this.name + ":: ");
        this.rootFeature.print();
        System.out.println();
        this.rootFeature.display(0);;
        System.out.print("Constrains" + ":: ");
        if (constrainSet != null)
            for (Object __dummyForeachVar0 : this.constrainSet)
            {
                ProductLine.FeatureModel.Constrain c = (ProductLine.FeatureModel.Constrain)__dummyForeachVar0;
                System.out.println(c.printConstrain());
            }
         
        System.out.println("==========end==========");
    }

    public void convertToLogicFormula() throws Exception {
        treeContraintsSets = new LinkedHashSet<ProductLine.LogicFormula.LogicFormula>();
        this.rootFeature.convertToLogicFormula(treeContraintsSets,VarToInt,IntToFeature);
        crossTreeContraintsSets = new LinkedHashSet<ProductLine.LogicFormula.LogicFormula>();
        if (constrainSet != null)
        {
            for (Object __dummyForeachVar1 : this.constrainSet)
            {
                ProductLine.FeatureModel.Constrain c = (ProductLine.FeatureModel.Constrain)__dummyForeachVar1;
                c.convertToLogicFormula(crossTreeContraintsSets);
            }
        }
         
       ArrayList<ProductLine.LogicFormula.LogicFormula> parameters = new ArrayList<ProductLine.LogicFormula.LogicFormula>();
        for (Object __dummyForeachVar2 : treeContraintsSets)
        {
            ProductLine.LogicFormula.LogicFormula lf = (ProductLine.LogicFormula.LogicFormula)__dummyForeachVar2;
            parameters.add(lf);
            //System.out.println(lf.print());
        }
        for (Object __dummyForeachVar3 : crossTreeContraintsSets)
        {
            ProductLine.LogicFormula.LogicFormula lf = (ProductLine.LogicFormula.LogicFormula)__dummyForeachVar3;
            parameters.add(lf);
           // System.out.println(lf.print());
        }
        combinedLogicFormula = new ProductLine.LogicFormula.And(parameters);
    }
    
    public void convertToInequations(HashMap<Integer, String> integerToAllFeatureMap, HashMap<Integer, Double> featuresToCostMap, HashMap<Integer, Integer> featuresToDefectsMap, HashMap<Integer, Boolean> featuresToNotUseBeforeMap) throws Exception {
		//this.inEquationMap = new InequationMap(integerToAllFeatureMap);
    	this.inEquationMap = new SparseInequationMap(integerToAllFeatureMap);
		System.out.println(inEquationMap.getVari2Feature());
		for (ProductLine.LogicFormula.LogicFormula formula : treeContraintsSets) {
			if (formula instanceof ProductLine.LogicFormula.Equal) {
				ProductLine.LogicFormula.Equal equal = (ProductLine.LogicFormula.Equal) formula;
				inEquationMap.add2Equation(equal);
			}

			else if (formula instanceof ProductLine.LogicFormula.Imply
					|| formula instanceof ProductLine.LogicFormula.And) {
				inEquationMap.add2AtLeastInequations(formula);
			}
		}

		for (ProductLine.LogicFormula.LogicFormula formula : crossTreeContraintsSets) {
			if (formula instanceof ProductLine.LogicFormula.Equal) {
				ProductLine.LogicFormula.Equal equal = (ProductLine.LogicFormula.Equal) formula;
				inEquationMap.add2Equation(equal);
			}

			else if (formula instanceof ProductLine.LogicFormula.Imply
					|| formula instanceof ProductLine.LogicFormula.And) {
				inEquationMap.add2AtLeastInequations(formula);
			}

			else if (formula instanceof ProductLine.LogicFormula.Or) {
				inEquationMap.add2AtLeastInequations(formula);
			}
		}
		// add root feature equation
		inEquationMap.add2Equation(new ProductLine.LogicFormula.Prop(rootFeature.getName()));

		// add the target
		inEquationMap.addTargetInequations(featuresToCostMap, featuresToDefectsMap, featuresToNotUseBeforeMap);
		// this.inEquationMap.print();
		this.inEquationMap.printMatlabFormat();
	}
}


