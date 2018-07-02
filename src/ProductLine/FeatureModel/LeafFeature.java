//
// Translated by CS2J (http://www.cs2j.com): 1/3/2014 9:42:01 PM
//

package ProductLine.FeatureModel;


import java.util.LinkedHashSet;

import ProductLine.FeatureModel.Feature;
import ProductLine.FeatureModel.TreeOperationException;

public class LeafFeature  extends Feature 
{
    private Feature parent;
    // Constructor
    //public LeafFeature(string name)
    //    : base(name)
    //{
    //}
    public LeafFeature(Feature parent, String name, boolean isOptional) throws Exception {
        super(name, isOptional);
        this.parent = parent;
        this.parent.add(this);
    }

    public void add(Feature f) throws Exception {
        throw new TreeOperationException("Cannot add to a leaf");
    }

    public void remove(Feature f) throws Exception {
        throw new TreeOperationException("Cannot remove from a leaf");
    }

    public void display(int depth) throws Exception {
    	String s="";
    	for(int i=0;i<depth;i++){
    		s=s+"-";
    	}
        System.out.println(s + toString());
    }

    public Feature getParentFeature() throws Exception {
        return this.parent;
    }

    public int getSubFeatureSize(int depth) throws Exception {
        return 1;
    }

    public LinkedHashSet<Feature> getSubFeatures(int depth) throws Exception {
    	LinkedHashSet<Feature> featureSet = new LinkedHashSet<Feature>();
        featureSet.add(this);
        return featureSet;
    }

    public void print() throws Exception {
        System.out.print(" ");
        if (this.isOptional == true)
        {
            System.out.print("[");
        }
         
        System.out.print(this.name);
        if (this.isOptional == true)
        {
            System.out.print("]");
        }
         
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
            if (this.isOptional == true)
            {
                toString += "]";
            }
             
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

}


