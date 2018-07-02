//
// Translated by CS2J (http://www.cs2j.com): 1/3/2014 9:42:01 PM
//

package ProductLine.FeatureModel;

import ProductLine.FeatureModel.SelectedFeature;

public class ProductFeatureModel   
{
    private String featureModelName;
    public String getFeatureModelName() throws Exception {
        return featureModelName;
    }

    public void setFeatureModelName(String value) throws Exception {
        featureModelName = value;
    }

    private String pFMName;
    public String getPFMName() throws Exception {
        return pFMName;
    }

    public void setPFMName(String value) throws Exception {
        pFMName = value;
    }

    private SelectedFeature rootSelFeature;
    public SelectedFeature getRootSelFeature() throws Exception {
        return rootSelFeature;
    }

    public void setRootSelFeature(SelectedFeature value) throws Exception {
        rootSelFeature = value;
    }

    public ProductFeatureModel(String featureModelName, String pFMName, SelectedFeature rootSelFeature) throws Exception {
        this.featureModelName = featureModelName;
        this.pFMName = pFMName;
        this.rootSelFeature = rootSelFeature;
    }

    public void print() throws Exception {
        this.rootSelFeature.print();
    }

    public void display() throws Exception {
        this.rootSelFeature.display(0);
    }

}


