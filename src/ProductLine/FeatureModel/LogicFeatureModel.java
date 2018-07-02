package ProductLine.FeatureModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import ProductLine.AttributeGenerator;
import ProductLine.GAParams;
import ProductLine.LogicFormula.LogicFormula;
import ProductLine.LogicFormula.Not;
import ProductLine.LogicFormula.Prop;

public class LogicFeatureModel extends FeatureModel {
	LinkedHashSet<ProductLine.LogicFormula.LogicFormula> logicFormula=null;
	
	ArrayList<String> featureNames=null;
	ArrayList<String> mandatoryNames=null;
	ArrayList<String> mustNotInNames=null;
	
	public void setRootFeature(RootFeature rootFeature) {
	    this.rootFeature = rootFeature;
    }
	
	public ArrayList<String> getMustNotInNames() {
		return mustNotInNames;
	}	

	public LinkedHashSet<ProductLine.LogicFormula.LogicFormula> getLogicFormula() {
		return logicFormula;
	}

	public ArrayList<String> getFeatureNames() {
		return featureNames;
	}

	public ArrayList<String> getMandatoryNames() {
		return mandatoryNames;
	}
	
	public LogicFeatureModel(LinkedHashSet<ProductLine.LogicFormula.LogicFormula> logicFormula,
			ArrayList<String> featureNames,
			ArrayList<String> mandatoryNames,
			ArrayList<String> mustNotInNames)
			throws Exception {
		
		this.logicFormula=logicFormula;
		this.featureNames=featureNames;
		this.mandatoryNames=mandatoryNames;
		this.mustNotInNames=mustNotInNames;
		// TODO Auto-generated constructor stub
	}
	
	public void convertToInequations(HashMap<Integer, String> integerToAllFeatureMap, HashMap<Integer, Double> featuresToCostMap, HashMap<Integer, Integer> featuresToDefectsMap, HashMap<Integer, Boolean> featuresToNotUseBeforeMap) throws Exception {
		//this.inEquationMap = new InequationMap(integerToAllFeatureMap);
		this.inEquationMap = new SparseInequationMap(integerToAllFeatureMap);
		System.out.println(inEquationMap.getVari2Feature());
		
		if(this.featureNames.size()< 1000) buildMapOnFormular(featuresToCostMap, featuresToDefectsMap, featuresToNotUseBeforeMap);
		
		else buildMapOnCNF(featuresToCostMap, featuresToDefectsMap, featuresToNotUseBeforeMap);

	}

	private void buildMapOnCNF(HashMap<Integer, Double> featuresToCostMap,
			HashMap<Integer, Integer> featuresToDefectsMap, HashMap<Integer, Boolean> featuresToNotUseBeforeMap) throws Exception {
		Set<LogicFormula> skipFormulars = new LinkedHashSet<LogicFormula>();
		for (ProductLine.LogicFormula.LogicFormula formula : logicFormula) {
			System.out.println(formula);
		    if (formula instanceof ProductLine.LogicFormula.Or) {
				if(inEquationMap.add2AtLeastInequations(formula))
				{
					skipFormulars.add(formula);
				}
			}
			else
			{
				throw new Exception("Unsupported Logical Formular format!");
			}
		}
 
		// add must in feature equation
		//inEquationMap.add2Equation(new ProductLine.LogicFormula.Prop(rootFeature.getName()));
		if(this.mandatoryNames.size() ==0) //if    Pruning is closed
		{
			inEquationMap.add2Equation(new ProductLine.LogicFormula.Prop(rootFeature.getName()));
		}
		for(String feature: this.mandatoryNames)
		{
			inEquationMap.add2Equation(new ProductLine.LogicFormula.Prop(feature));
		}
		// add must not in feature equation
		for(String feature: this.mustNotInNames)
		{
			Prop f = new ProductLine.LogicFormula.Prop(feature);
			Not not = new ProductLine.LogicFormula.Not(f);
			inEquationMap.add2Equation(not);
		}

		// add the target
		inEquationMap.addTargetInequations(featuresToCostMap, featuresToDefectsMap, featuresToNotUseBeforeMap);
		System.out.print("skipFormulars size"+skipFormulars.size()+":   ");
		//System.out.println(skipFormulars);
	}

	private void buildMapOnFormular(HashMap<Integer, Double> featuresToCostMap,
			HashMap<Integer, Integer> featuresToDefectsMap, HashMap<Integer, Boolean> featuresToNotUseBeforeMap)
					throws Exception {
		for (ProductLine.LogicFormula.LogicFormula formula : logicFormula) {
			System.out.println(formula);
			if (formula instanceof ProductLine.LogicFormula.Equal) {
				ProductLine.LogicFormula.Equal equal = (ProductLine.LogicFormula.Equal) formula;
				inEquationMap.add2Equation(equal);
			}
			else if (formula instanceof ProductLine.LogicFormula.Imply
					|| formula instanceof ProductLine.LogicFormula.And) {
				inEquationMap.add2AtLeastInequations(formula);
			}
			else if (formula instanceof ProductLine.LogicFormula.Not) {
				inEquationMap.add2AtLeastInequations(formula);
			}
			else if (formula instanceof ProductLine.LogicFormula.Or) {
				inEquationMap.add2AtLeastInequations(formula);
			}
			else
			{
				throw new Exception("Unsupported Logical Formular format!");
			}
		}
 
		// add must in feature equation
		//inEquationMap.add2Equation(new ProductLine.LogicFormula.Prop(rootFeature.getName()));
		if(this.mandatoryNames.size() ==0) //if    Pruning is closed
		{
			inEquationMap.add2Equation(new ProductLine.LogicFormula.Prop(rootFeature.getName()));
		}
		for(String feature: this.mandatoryNames)
		{
			inEquationMap.add2Equation(new ProductLine.LogicFormula.Prop(feature));
		}
		// add must not in feature equation
		for(String feature: this.mustNotInNames)
		{
			Prop f = new ProductLine.LogicFormula.Prop(feature);
			Not not = new ProductLine.LogicFormula.Not(f);
			inEquationMap.add2Equation(not);
		}

		// add the target
		inEquationMap.addTargetInequations(featuresToCostMap, featuresToDefectsMap, featuresToNotUseBeforeMap);
		// this.inEquationMap.print();
		this.inEquationMap.printMatlabFormat();
	}

}
