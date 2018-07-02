//
// Translated by CS2J (http://www.cs2j.com): 1/3/2014 9:42:01 PM
//

package ProductLine.FeatureModel;

import java.util.LinkedHashSet;

import ProductLine.FeatureModel.Feature;
import ProductLine.FeatureModel.FeatureAttribute;

public abstract class Feature   
{
    protected String name;
    protected boolean isOptional;
    protected LinkedHashSet<FeatureAttribute> attributes = new LinkedHashSet<FeatureAttribute>();
    public Feature() throws Exception {
    }

    //throw new TreeOperationException("Feature must have name!");
    // Constructor
    public Feature(String name) throws Exception {
        this.name = name;
    }

    public Feature(String name, boolean isOptional) throws Exception {
        this.name = name;
        this.isOptional = isOptional;
    }

    public abstract void add(Feature f) throws Exception ;

    public abstract void remove(Feature f) throws Exception ;

    public abstract void display(int depth) throws Exception ;

    public abstract Feature getParentFeature() throws Exception ;

    //public abstract LinkedHashSet<Feature> GetChildrenFeatures();
    public String getName() throws Exception {
        return this.name;
    }

    public boolean isOptional() throws Exception {
        return this.isOptional;
    }

    public void setFeatureAttributes(LinkedHashSet<FeatureAttribute> attributes) throws Exception {
        this.attributes = attributes;
    }

    public LinkedHashSet<FeatureAttribute> getFeatureAttributes() throws Exception {
        return this.attributes;
    }

    public abstract int getSubFeatureSize(int depth) throws Exception ;

    public abstract LinkedHashSet<Feature> getSubFeatures(int depth) throws Exception ;

    public abstract void print() throws Exception ;

}


