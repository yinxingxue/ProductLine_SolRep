//
// Translated by CS2J (http://www.cs2j.com): 1/3/2014 9:42:01 PM
//

package ProductLine.FeatureModel;

import java.util.HashMap;
import java.util.LinkedHashSet;

import ProductLine.FeatureModel.CompositeFeature;
import ProductLine.FeatureModel.Feature;

public class RootFeature  extends CompositeFeature 
{
    public RootFeature(String name, boolean optional) throws Exception {
        super(name, optional);
        this.name = name;
        this.isOptional = false;
    }

    public void convertToLogicFormula(LinkedHashSet<ProductLine.LogicFormula.LogicFormula> treeContraintsSets, HashMap<String,Integer> VarToInt, HashMap<Integer,Feature> IntToFeature) throws Exception {
        //build indexing for root feature
        String featureName = this.getName();
        int featureIdx = VarToInt.size() + 1;
        VarToInt.put(featureName, featureIdx);
        IntToFeature.put(featureIdx, this);
        super.convertToLogicFormula(treeContraintsSets, VarToInt, IntToFeature);
    }

}


