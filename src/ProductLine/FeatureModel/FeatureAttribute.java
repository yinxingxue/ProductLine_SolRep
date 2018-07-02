//
// Translated by CS2J (http://www.cs2j.com): 1/3/2014 9:42:01 PM
//

package ProductLine.FeatureModel;


public class FeatureAttribute   
{
    public FeatureAttribute(String attributeName, String attributeValue) throws Exception {
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }

    private String attributeName;
    private String attributeValue;
    public String getAttributeName() throws Exception {
        return this.attributeName;
    }

    public String getAttributeValue() throws Exception {
        return this.attributeValue;
    }

}


