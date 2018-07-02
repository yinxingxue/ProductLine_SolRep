//
// Translated by CS2J (http://www.cs2j.com): 1/3/2014 9:42:01 PM
//

package ProductLine.FeatureModel;

import java.util.LinkedHashSet;


import ProductLine.FeatureModel.Feature;
import ProductLine.FeatureModel.SelectedFeature;

public class SelectedFeature  extends Feature 
{
    private SelectedFeature parent;
    private Feature abstractFeature;
    public Feature getAbstractFeature() throws Exception {
        return abstractFeature;
    }

    private LinkedHashSet<SelectedFeature> children = new LinkedHashSet<SelectedFeature>();
    boolean isRootFeature;
    public boolean getIsRootFeature() throws Exception {
        return isRootFeature;
    }

    public SelectedFeature(Feature abstractFeature) throws Exception {
        this.isRootFeature = true;
        this.abstractFeature = abstractFeature;
    }

    public SelectedFeature(Feature abstractFeature, SelectedFeature parent) throws Exception {
        this.isRootFeature = false;
        this.abstractFeature = abstractFeature;
        this.parent = parent;
    }

    // add for the selected sub-feature, f is the child feature
    public void add(Feature f) throws Exception {
        if (this.children == null)
        {
            this.children = new LinkedHashSet<SelectedFeature>();
            SelectedFeature child = new SelectedFeature(f,this);
            this.children.add(child);
        }
        else
        {
            SelectedFeature child = new SelectedFeature(f,this);
            this.children.add(child);
        } 
    }

    public void remove(Feature f) throws Exception {
        if (this.children != null)
        {
            SelectedFeature searchedFeature = null;
            for (Object __dummyForeachVar0 : this.children)
            {
                SelectedFeature cndFeature = (SelectedFeature)__dummyForeachVar0;
                if (cndFeature.abstractFeature == f)
                {
                    searchedFeature = cndFeature;
                    break;
                }
                 
            }
            children.remove(searchedFeature);
            if (children.size() == 0)
                this.children = null;
             
            searchedFeature.dispose();
        }
         
    }

    public void display(int depth) throws Exception {
    	String s="";
    	for (int i=0;i<depth;i++){
    		s=s+"-";
    	}
        System.out.println(s+ this.getAbstractFeature().getName());
        if (this.children == null)
            return ;
         
        for (Object __dummyForeachVar1 : children)
        {
            // Recursively display child nodes
            Feature f = (Feature)__dummyForeachVar1;
            f.display(depth + 2);
        }
    }

    public Feature getParentFeature() throws Exception {
        return this.parent;
    }

    public SelectedFeature getParentSelFeature() throws Exception {
        return this.parent;
    }

    public LinkedHashSet<SelectedFeature> getChildrenSelFeatures() throws Exception {
        return this.children;
    }

    public int getSubFeatureSize(int depth) throws Exception {
        return getSubSelFeatureSize(depth);
    }

    public int getSubSelFeatureSize(int depth) throws Exception {
        int size = 1;
        int newDepth = depth - 1;
        if (newDepth >= 0 && this.children != null)
        {
            for (Object __dummyForeachVar2 : this.children)
            {
                SelectedFeature f = (SelectedFeature)__dummyForeachVar2;
                size = size + f.getSubSelFeatureSize(newDepth);
            }
        }
         
        return size;
    }

    public LinkedHashSet<Feature> getSubFeatures(int depth) throws Exception {
    	LinkedHashSet<Feature> results = new LinkedHashSet<Feature>();
    	LinkedHashSet<SelectedFeature> selFeatures = getSubSelFeatures(depth);
        results.addAll(selFeatures);
        return results;
    }

    public LinkedHashSet<SelectedFeature> getSubSelFeatures(int depth) throws Exception {
    	LinkedHashSet<SelectedFeature> featureSet = new LinkedHashSet<SelectedFeature>();
        featureSet.add(this);
        int newDepth = depth - 1;
        if (newDepth >= 0 && this.children != null)
        {
            for (Object __dummyForeachVar3 : this.children)
            {
                SelectedFeature f = (SelectedFeature)__dummyForeachVar3;
                featureSet.addAll(f.getSubSelFeatures(newDepth));
            }
        }
         
        return featureSet;
    }

    public void print() throws Exception {
        System.out.print(" ");
        System.out.print(this.getAbstractFeature().getName());
        System.out.print(" {");
        if (this.children != null)
        {
            for (Object __dummyForeachVar4 : this.children)
            {
                Feature f = (Feature)__dummyForeachVar4;
                f.print();
            }
        }
         
        System.out.print("} ");
    }

    public void dispose() throws Exception {
        parent = null;
        abstractFeature = null;
        children = null;
        isRootFeature = false;
    }

}


