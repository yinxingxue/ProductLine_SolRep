//
// Translated by CS2J (http://www.cs2j.com): 1/3/2014 9:42:01 PM
//

package ProductLine.FeatureModel;



import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Hashtable;
import java.util.Map;

import ProductLine.FeatureModel.Feature;

public class Constrain   
{
    public enum ConstrainType
    {
        REQ,
        // 0, for require
        EXC,
        // 1, for exclude
        IFF,
        //
        DNF,
    }
    // if only if <=>
    private ProductLine.FeatureModel.Constrain.ConstrainType type = ProductLine.FeatureModel.Constrain.ConstrainType.REQ;
    // map from Feature->Boolean
    private Hashtable preConFeatures;
    private Hashtable postConFeatures;
    private ProductLine.LogicFormula.LogicFormula lgcFormular;
    public ProductLine.LogicFormula.LogicFormula getLgcFormular() throws Exception {
        return lgcFormular;
    }

    public void setLgcFormular(ProductLine.LogicFormula.LogicFormula value) throws Exception {
        lgcFormular = value;
    }

    public Constrain(ProductLine.FeatureModel.Constrain.ConstrainType type, Hashtable preConFeatures, Hashtable postConFeatures) throws Exception {
        this.type = type;
        this.preConFeatures = preConFeatures;
        this.postConFeatures = postConFeatures;
    }

    public Constrain(ProductLine.FeatureModel.Constrain.ConstrainType type, ProductLine.LogicFormula.LogicFormula lgcFormular) throws Exception {
        this.type = type;
        this.lgcFormular = lgcFormular;
    }

    public ProductLine.FeatureModel.Constrain.ConstrainType getConstrainType() throws Exception {
        return this.type;
    }

    public Hashtable getPreConditionFeatures() throws Exception {
        return this.preConFeatures;
    }

    public Hashtable getPostConditionFeatures() throws Exception {
        return this.postConFeatures;
    }

    public String printConstrain() throws Exception {
        String result = "";
        if (this.lgcFormular != null)
            return this.lgcFormular.print();
         
        //TODO only support for "and"
        int counter = 0;
        for (Object __dummyForeachVar0 : preConFeatures.entrySet())
        {
            Map.Entry objDE = (Map.Entry)__dummyForeachVar0;
            counter++;
            //Console.WriteLine(objDE.Key.ToString());
            if (objDE.getKey() instanceof Feature)
            {
                Feature f = (Feature)objDE.getKey();
                boolean isPositive = (boolean)objDE.getValue();
                if (isPositive == true)
                    result += f.getName() + " ";
                else
                {
                    result += "-" + f.getName() + " ";
                } 
            }
             
            if (counter != preConFeatures.size())
                result += "& ";
             
        }
        if (type == ProductLine.FeatureModel.Constrain.ConstrainType.EXC)
            result += " EXC ";
        else if (type == ProductLine.FeatureModel.Constrain.ConstrainType.IFF)
            result += " IFF ";
        else if (type == ProductLine.FeatureModel.Constrain.ConstrainType.REQ)
            result += " REQ ";
        else if (type == ProductLine.FeatureModel.Constrain.ConstrainType.DNF)
            result += " OR ";
           
        counter = 0;
        for (Object __dummyForeachVar1 : postConFeatures.entrySet())
        {
            Map.Entry objDE = (Map.Entry)__dummyForeachVar1;
            counter++;
            //Console.WriteLine(objDE.Key.ToString());
            if (objDE.getKey() instanceof Feature)
            {
                Feature f = (Feature)objDE.getKey();
                boolean isPositive = (boolean)objDE.getValue();
                if (isPositive == true)
                    result += f.getName() + " ";
                else
                {
                    result += "-" + f.getName() + " ";
                } 
            }
             
            if (counter != postConFeatures.size())
                result += "& ";
             
        }
        return result;
    }

    public void convertToLogicFormula(LinkedHashSet<ProductLine.LogicFormula.LogicFormula> crossTreeContraintsSets) throws Exception {
        if (this.lgcFormular != null)
        {
            crossTreeContraintsSets.add(this.lgcFormular);
            return ;
        }
         
        // REQ is  f1 => f2
        if (type == ProductLine.FeatureModel.Constrain.ConstrainType.REQ)
        {
            crossTreeContraintsSets.add(convertREQtoFormulr());
        }
        else // EXC is  not(f1 and f2)
        if (type == ProductLine.FeatureModel.Constrain.ConstrainType.EXC)
        {
            crossTreeContraintsSets.add(convertEXCtoFormulr());
        }
        else // IFF is  f1 <=> f2
        if (type == ProductLine.FeatureModel.Constrain.ConstrainType.IFF)
        {
            crossTreeContraintsSets.add(convertIFFtoFormulr());
        }
           
    }

    private ProductLine.LogicFormula.LogicFormula convertREQtoFormulr() throws Exception {
        //
        ProductLine.LogicFormula.LogicFormula implyFormula = null;
        ProductLine.LogicFormula.LogicFormula implyA = createAFormular();
        ProductLine.LogicFormula.LogicFormula implyB = createBFormular();
        implyFormula = new ProductLine.LogicFormula.Imply(implyA,implyB);
        return implyFormula;
    }

    private ProductLine.LogicFormula.LogicFormula convertEXCtoFormulr() throws Exception {
        ProductLine.LogicFormula.LogicFormula notFormula = null;
        ProductLine.LogicFormula.LogicFormula andFormula = null;
        ProductLine.LogicFormula.LogicFormula andA = createAFormular();
        ProductLine.LogicFormula.LogicFormula andB = createBFormular();
        ArrayList<ProductLine.LogicFormula.LogicFormula> AandB = new ArrayList<ProductLine.LogicFormula.LogicFormula>();
        AandB.add(andA);
        AandB.add(andB);
        andFormula = new ProductLine.LogicFormula.And(AandB);
            ;
        notFormula = new ProductLine.LogicFormula.Not(andFormula);
        return notFormula;
    }

    private ProductLine.LogicFormula.LogicFormula convertIFFtoFormulr() throws Exception {
        ProductLine.LogicFormula.LogicFormula equalFormula = null;
        ProductLine.LogicFormula.LogicFormula equalA = createAFormular();
        ProductLine.LogicFormula.LogicFormula equalB = createBFormular();
        equalFormula = new ProductLine.LogicFormula.Equal(equalA,equalB);
        return equalFormula;
    }

    private ProductLine.LogicFormula.LogicFormula createBFormular() throws Exception {
        ProductLine.LogicFormula.LogicFormula implyB = null;
        ArrayList<ProductLine.LogicFormula.LogicFormula> postAndCnds = new ArrayList<ProductLine.LogicFormula.LogicFormula>();
        for (Object __dummyForeachVar2 : postConFeatures.entrySet())
        {
            Map.Entry objDE = (Map.Entry)__dummyForeachVar2;
            //Console.WriteLine(objDE.Key.ToString());
            if (objDE.getKey() instanceof Feature)
            {
                Feature f = (Feature)objDE.getKey();
                boolean positive = (boolean)objDE.getValue();
                // if it is "NOT" formula
                if (positive == false)
                {
                    ProductLine.LogicFormula.LogicFormula prop = new ProductLine.LogicFormula.Prop(f.getName());
                    ProductLine.LogicFormula.LogicFormula notFormula = new ProductLine.LogicFormula.Not(prop);
                    postAndCnds.add(notFormula);
                }
                else
                {
                    ProductLine.LogicFormula.LogicFormula prop = new ProductLine.LogicFormula.Prop(f.getName());
                    postAndCnds.add(prop);
                } 
            }
             
        }
        implyB = new ProductLine.LogicFormula.And(postAndCnds);
        return implyB;
    }

    private ProductLine.LogicFormula.LogicFormula createAFormular() throws Exception {
        ProductLine.LogicFormula.LogicFormula implyA = null;
        ArrayList<ProductLine.LogicFormula.LogicFormula> preAndCnds = new ArrayList<ProductLine.LogicFormula.LogicFormula>();
        for (Object __dummyForeachVar3 : preConFeatures.entrySet())
        {
            Map.Entry objDE = (Map.Entry)__dummyForeachVar3;
            //Console.WriteLine(objDE.Key.ToString());
            if (objDE.getKey() instanceof Feature)
            {
                Feature f = (Feature)objDE.getKey();
                boolean positive = (boolean)objDE.getValue();
                // if it is "NOT" formula
                if (positive == false)
                {
                    ProductLine.LogicFormula.LogicFormula prop = new ProductLine.LogicFormula.Prop(f.getName());
                    ProductLine.LogicFormula.LogicFormula notFormula = new ProductLine.LogicFormula.Not(prop);
                    preAndCnds.add(notFormula);
                }
                else
                {
                    ProductLine.LogicFormula.LogicFormula prop = new ProductLine.LogicFormula.Prop(f.getName());
                    preAndCnds.add(prop);
                } 
            }
             
        }
        implyA = new ProductLine.LogicFormula.And(preAndCnds);
        return implyA;
    }

}


