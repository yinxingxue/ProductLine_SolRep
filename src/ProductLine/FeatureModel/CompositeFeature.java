//
// Translated by CS2J (http://www.cs2j.com): 1/3/2014 9:42:01 PM
//

package ProductLine.FeatureModel;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;

import ProductLine.FeatureModel.CompositeFeature;
import ProductLine.FeatureModel.CompositionType;
import ProductLine.FeatureModel.Feature;
import ProductLine.FeatureModel.LeafFeature;
import ProductLine.FeatureModel.RootFeature;
import ProductLine.LogicFormula.LogicFormula;
import ProductLine.LogicFormula.Prop;

public class CompositeFeature  extends Feature 
{
    private Feature parent;
    private LinkedHashSet<Feature> children = new LinkedHashSet<Feature>();
    // this is important
    private int[] cardinalities;
    private CompositionType childrenCmpstType = CompositionType.AND;
    // Constructor
    public CompositeFeature(String name, boolean optional) throws Exception {
        super(name, optional);
       // if (!(this instanceof RootFeature) && (this instanceof CompositeFeature))
           // System.out.println("need the parent feature to construct a CompositeFeature");
         
    }

    public CompositeFeature(Feature parent, String name, boolean optional) throws Exception {
        super(name, optional);
        this.parent = parent;
        this.parent.add(this);
    }

    public void add(Feature f) throws Exception {
        children.add(f);
    }

    public void remove(Feature f) throws Exception {
        children.remove(f);
    }

    public void display(int depth) throws Exception {
    	String s="";
    	for(int i=0;i<depth;i++){
    		s=s+'-';
    	}
        System.out.println(s + toString());
        for (Feature f: children)
        {
            // Recursively display child nodes
           // Feature f = (Feature)__dummyForeachVar0;
            f.display(depth + 2);
        }
    }

    public Feature getParentFeature() throws Exception {
        return this.parent;
    }

    public LinkedHashSet<Feature> getChildrenFeatures() throws Exception {
        return this.children;
    }

    public void setCardinality(int[] cardinalities) throws Exception {
        this.cardinalities = cardinalities;
    }

    public int[] getCardinality() throws Exception {
        return this.cardinalities;
    }

    public int getSubFeatureSize(int depth) throws Exception {
        int size = 1;
        int newDepth = depth - 1;
        if (newDepth >= 0)
        {
            for (Feature f : this.children)
            {
                //Feature f = (Feature)__dummyForeachVar1;
                size = size + f.getSubFeatureSize(newDepth);
            }
        }
         
        return size;
    }

    public CompositionType getChildrenCmpstType() throws Exception {
        // check the childrenFeature size
        int childrenNum = this.children.size();
        //OR case,  childrenNum >=2 && cardinaltity = [1,max]
        if (childrenNum >= 2 && this.cardinalities[0] == 1 && this.cardinalities[1] == childrenNum)
        {
            //bug here, need to double check whether all the subfeatures are optional.
            this.childrenCmpstType = CompositionType.OR;
            for (Feature f : this.children)
            {
                //Feature f = (Feature)__dummyForeachVar2;
                if (!f.isOptional())
                {
                    this.childrenCmpstType = CompositionType.PARTIAL_AND;
                    break;
                }
                 
            }
        }
        else //XOR case, childrenNum >=2 && cardinaltity = [1,1]
        if (childrenNum >= 2 && this.cardinalities[0] == 1 && this.cardinalities[1] == 1)
            this.childrenCmpstType = CompositionType.XOR;
        else //And case, caridinality = [1,1] & childrenNum = 1, or childrenNum >=2, cardinaltity = [max,max]
        if (childrenNum >= 2 && this.cardinalities[0] == childrenNum && this.cardinalities[1] == childrenNum || childrenNum == 1 && this.cardinalities[0] == 1 && this.cardinalities[1] == 1)
        {
            this.childrenCmpstType = CompositionType.AND;
        }
        else
            this.childrenCmpstType = CompositionType.PARTIAL_AND;   
        return this.childrenCmpstType;
    }

    public LinkedHashSet<Feature> getSubFeatures(int depth) throws Exception {
    	LinkedHashSet<Feature> featureSet = new LinkedHashSet<Feature>();
        featureSet.add(this);
        int newDepth = depth - 1;
        if (newDepth >= 0)
        {
            for (Feature f : this.children)
            {
                //Feature f = (Feature)__dummyForeachVar3;
                featureSet.addAll(f.getSubFeatures(newDepth));
            }
        }
         
        return featureSet;
    }

    public void print() throws Exception {
        System.out.print(" ");
        if (this.isOptional == true)
        {
            System.out.print("[");
        }
         
        System.out.print(this.name);
        System.out.print("(");
        System.out.print(this.cardinalities[0]);
        System.out.print(",");
        System.out.print(this.cardinalities[1]);
        System.out.print(")");
        if (this.isOptional == true)
        {
            System.out.print("]");
        }
        System.out.print(" {");
        for (Feature f : this.children)
        {
            //Feature f = (Feature)__dummyForeachVar4;
            f.print();
        }
        System.out.print("} ");
    }

    public String toString() {
        try
        {
            String toString = "";
            if (this.isOptional == true)
            {
                toString += "[";
            }
             
            toString += this.name;
            toString += "(";
            toString += this.cardinalities[0];
            toString += ",";
            toString += this.cardinalities[1];
            toString += ")";
            if (this.isOptional == true)
            {
                toString += "]";
            }
            toString += "\n";
            return toString;
        }
        catch (RuntimeException __dummyCatchVar0)
        {
            throw __dummyCatchVar0;
        }
        catch (Exception __dummyCatchVar0)
        {
            throw new RuntimeException(__dummyCatchVar0);
        }
    
    }

    public void convertToLogicFormula(LinkedHashSet<ProductLine.LogicFormula.LogicFormula> treeContraintsSets, HashMap<String,Integer> VarToInt, HashMap<Integer,Feature> IntToFeature) throws Exception {
        int featureIdx = VarToInt.size();
        LinkedHashSet<Feature> childrenFeatures = this.getChildrenFeatures();
        for (Feature child : childrenFeatures)
        {
            //Feature child = (Feature)__dummyForeachVar5;
            //build indexing for children features
            featureIdx++;
            VarToInt.put(child.getName(), featureIdx);
            IntToFeature.put(featureIdx, child);
        }
        //build the LogicFormula
        CompositionType type = this.getChildrenCmpstType();
        ProductLine.LogicFormula.LogicFormula equalFormula = null;
        ProductLine.LogicFormula.LogicFormula equalA = new ProductLine.LogicFormula.Prop(this.getName());
        ProductLine.LogicFormula.LogicFormula parentalFormula = null;
        ArrayList<ProductLine.LogicFormula.LogicFormula> parameters = new ArrayList<ProductLine.LogicFormula.LogicFormula>();
        //create the LogicFormula
        if (type == CompositionType.AND)
        {
            for (Feature child : childrenFeatures)
            {
                //Feature child = (Feature)__dummyForeachVar6;
                ProductLine.LogicFormula.LogicFormula parameter = new ProductLine.LogicFormula.Prop(child.getName());
                parameters.add(parameter);
            }
//            if (parameters.size() > 0)
//                parentalFormula = new ProductLine.LogicFormula.And(parameters);
             
        }
        else if (type == CompositionType.OR)
        {
            for (Feature child : childrenFeatures)
            {
                //Feature child = (Feature)__dummyForeachVar7;
                ProductLine.LogicFormula.LogicFormula parameter = new ProductLine.LogicFormula.Prop(child.getName());
                parameters.add(parameter);
            }
            if (parameters.size() > 0)
                parentalFormula = new ProductLine.LogicFormula.Or(parameters);
             
        }
        else if (type == CompositionType.XOR)
        {
            for (Feature child : childrenFeatures)
            {
                //Feature child = (Feature)__dummyForeachVar8;
                ProductLine.LogicFormula.LogicFormula parameter = new ProductLine.LogicFormula.Prop(child.getName());
                parameters.add(parameter);
            }
            if (parameters.size() > 0)
                parentalFormula = new ProductLine.LogicFormula.Or(parameters);
             
        }
        else if (type == CompositionType.PARTIAL_AND)
        {
            for (Feature child : childrenFeatures)
            {
                //Feature child = (Feature)__dummyForeachVar9;
                // add f_{1} optional sub-feature of f: f_{1} => f
                if (child.isOptional())
                {
                    ProductLine.LogicFormula.LogicFormula childProp = new ProductLine.LogicFormula.Prop(child.getName());
                    ProductLine.LogicFormula.LogicFormula parentProp = new ProductLine.LogicFormula.Prop(this.getName());
                    ProductLine.LogicFormula.LogicFormula imply = new ProductLine.LogicFormula.Imply(childProp,parentProp);
                    treeContraintsSets.add(imply);
                }
                else
                {
                    //f_{1} mandatory sub-feature of f:	f_{1} <=> f
                    ProductLine.LogicFormula.LogicFormula parameter = new ProductLine.LogicFormula.Prop(child.getName());
                    parameters.add(parameter);
                } 
            }
//            if (parameters.size() > 0)
//                parentalFormula = new ProductLine.LogicFormula.And(parameters);
//             
//            if (parameters.size() != this.getCardinality()[0])
//                throw new Exception("Partial AND parameters are not consistent!");
             
        }
        
        if (parentalFormula == null && (type == CompositionType.AND || type == CompositionType.PARTIAL_AND))
        {
          if (parameters.size() != this.getCardinality()[0])
            throw new Exception("Partial AND or AND parameters are not consistent!");
            for(LogicFormula childFea: parameters)
            {
            	Prop child= (Prop)childFea;
            	ProductLine.LogicFormula.LogicFormula childProp = new ProductLine.LogicFormula.Prop(child.varname);
            	  ProductLine.LogicFormula.LogicFormula equal = new ProductLine.LogicFormula.Equal(childProp,equalA);
                  treeContraintsSets.add(equal);
            }
            //equalFormula = new ProductLine.LogicFormula.Equal(parentalFormula,equalA);
        }
            
        else if (parentalFormula != null && type != CompositionType.XOR)
        {
            equalFormula = new ProductLine.LogicFormula.Equal(parentalFormula,equalA);
        }
        else if (parentalFormula != null && type == CompositionType.XOR)
        {
            //  f1 || f2 || f3 <=> f
            ProductLine.LogicFormula.LogicFormula orFormular = new ProductLine.LogicFormula.Equal(parentalFormula,equalA);
            // add to XOR
            ArrayList<ProductLine.LogicFormula.LogicFormula> xorCNF = new ArrayList<ProductLine.LogicFormula.LogicFormula>();
            xorCNF.add(orFormular);
            ProductLine.LogicFormula.LogicFormula[] paras =  parameters.toArray(new  ProductLine.LogicFormula.LogicFormula[0]);
            for (int i = 0;i < paras.length;i++)
            {
                // add any two alternative
                ProductLine.LogicFormula.Prop para1 = (ProductLine.LogicFormula.Prop)paras[i];
                for (int j = i + 1;j < paras.length;j++)
                {
                    ProductLine.LogicFormula.Prop para2 = (ProductLine.LogicFormula.Prop)paras[j];
                    ArrayList<ProductLine.LogicFormula.LogicFormula> twoPara = new ArrayList<ProductLine.LogicFormula.LogicFormula>();
                    twoPara.add(para1);
                    twoPara.add(para2);
                    ProductLine.LogicFormula.LogicFormula andPara = new ProductLine.LogicFormula.And(twoPara);
                    ProductLine.LogicFormula.LogicFormula xor = new ProductLine.LogicFormula.Not(andPara);
                    xorCNF.add(xor);
                }
            }
            equalFormula = new ProductLine.LogicFormula.And(xorCNF);
        }
          
        // if all the partial-and are all optional,
        if (equalFormula != null)
            treeContraintsSets.add(equalFormula);
         
        for ( Feature child : childrenFeatures)
        {
            //Feature child = (Feature)__dummyForeachVar10;
            if (child instanceof LeafFeature)
                continue;
             
            CompositeFeature cf = (CompositeFeature)child;
            cf.convertToLogicFormula(treeContraintsSets, VarToInt, IntToFeature);
        }
    }

}


