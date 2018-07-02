//
// Translated by CS2J (http://www.cs2j.com): 1/3/2014 9:42:01 PM
//

package ProductLine.FeatureModel;

import java.security.SecureRandom;
import java.util.Random;
import ProductLine.FeatureModel.CompositeFeature;
import ProductLine.FeatureModel.CompositionType;
import ProductLine.FeatureModel.Feature;
import ProductLine.FeatureModel.ProductFeatureModel;
import ProductLine.FeatureModel.RootFeature;
import ProductLine.FeatureModel.SelectedFeature;

public class PFMGenerator   
{
    private ProductLine.FeatureModel.FeatureModel featureModel;
    public ProductLine.FeatureModel.FeatureModel getFeatureModel() throws Exception {
        return featureModel;
    }

    private ProductFeatureModel maxPFM;
    public ProductFeatureModel getMaxPFM() throws Exception {
        return maxPFM;
    }

    public void setMaxPFM(ProductFeatureModel value) throws Exception {
        maxPFM = value;
    }

    private ProductFeatureModel minPFM;
    public ProductFeatureModel getMinPFM() throws Exception {
        return minPFM;
    }

    public void setMinPFM(ProductFeatureModel value) throws Exception {
        minPFM = value;
    }

    private ProductFeatureModel randomPFM;
    public ProductFeatureModel getRandomPFM() throws Exception {
        return randomPFM;
    }

    public void setRandomPFM(ProductFeatureModel value) throws Exception {
        randomPFM = value;
    }

    public PFMGenerator(ProductLine.FeatureModel.FeatureModel featureModel) throws Exception {
        this.featureModel = featureModel;
    }

    public ProductFeatureModel createMaxPFM() throws Exception {
        RootFeature root = featureModel.getRootFeature();
        SelectedFeature rootSel = new SelectedFeature(root);
        getMaxSubFeatures(root,rootSel,null);
        this.maxPFM = new ProductFeatureModel(featureModel.getName(),"max",rootSel);
        return this.maxPFM;
    }

    private static void getMaxSubFeatures(CompositeFeature current, SelectedFeature currentSel, SelectedFeature parentSel) throws Exception {
        CompositionType type = current.getChildrenCmpstType();
        if (type == CompositionType.AND)
        {
            for (Object __dummyForeachVar0 : current.getChildrenFeatures())
            {
                Feature child = (Feature)__dummyForeachVar0;
                currentSel.add(child);
            }
        }
        else if (type == CompositionType.OR)
        {
            for (Object __dummyForeachVar1 : current.getChildrenFeatures())
            {
                Feature child = (Feature)__dummyForeachVar1;
                currentSel.add(child);
            }
        }
        else if (type == CompositionType.XOR)
        {
            selXORMaxSubFeatures(current,currentSel);
        }
        else
        {
            for (Object __dummyForeachVar2 : current.getChildrenFeatures())
            {
                // partial_or
                Feature child = (Feature)__dummyForeachVar2;
                currentSel.add(child);
            }
        }   
        for (Object __dummyForeachVar3 : currentSel.getChildrenSelFeatures())
        {
            SelectedFeature selChild = (SelectedFeature)__dummyForeachVar3;
            Feature child = selChild.getAbstractFeature();
            if (child instanceof CompositeFeature)
            {
                CompositeFeature cmpChild = (CompositeFeature)child;
                getMaxSubFeatures(cmpChild,selChild,currentSel);
            }
             
        }
    }

    public ProductFeatureModel createMinPFM() throws Exception {
        RootFeature root = featureModel.getRootFeature();
        SelectedFeature rootSel = new SelectedFeature(root);
        getMinSubFeatures(root,rootSel,null);
        this.minPFM = new ProductFeatureModel(featureModel.getName(),"min",rootSel);
        return this.minPFM;
    }

    private static void getMinSubFeatures(CompositeFeature current, SelectedFeature currentSel, SelectedFeature parentSel) throws Exception {
        CompositionType type = current.getChildrenCmpstType();
        if (type == CompositionType.AND)
        {
            for (Object __dummyForeachVar4 : current.getChildrenFeatures())
            {
                Feature child = (Feature)__dummyForeachVar4;
                currentSel.add(child);
            }
        }
        else if (type == CompositionType.OR)
        {
            selXORMinSubFeatures(current,currentSel);
        }
        else if (type == CompositionType.XOR)
        {
            selXORMinSubFeatures(current,currentSel);
        }
        else
        {
            for (Object __dummyForeachVar5 : current.getChildrenFeatures())
            {
                // partial_or
                Feature child = (Feature)__dummyForeachVar5;
                if (!child.isOptional())
                    currentSel.add(child);
                 
            }
        }   
        for (Object __dummyForeachVar6 : currentSel.getChildrenSelFeatures())
        {
            SelectedFeature selChild = (SelectedFeature)__dummyForeachVar6;
            Feature child = selChild.getAbstractFeature();
            if (child instanceof CompositeFeature)
            {
                CompositeFeature cmpChild = (CompositeFeature)child;
                getMinSubFeatures(cmpChild,selChild,currentSel);
            }
             
        }
    }

    private static void selXORMaxSubFeatures(CompositeFeature current, SelectedFeature currentSel) throws Exception {
        Feature possibleBiggest = current.getChildrenFeatures().iterator().next();
        int subFeatureSize = possibleBiggest.getSubFeatureSize(100);
        for (Object __dummyForeachVar7 : current.getChildrenFeatures())
        {
            Feature child = (Feature)__dummyForeachVar7;
            int curChildSize = child.getSubFeatureSize(100);
            if (curChildSize > subFeatureSize)
            {
                subFeatureSize = curChildSize;
                possibleBiggest = child;
            }
             
        }
        currentSel.add(possibleBiggest);
    }

    private static void selXORMinSubFeatures(CompositeFeature current, SelectedFeature currentSel) throws Exception {
        Feature possibleSmallest = current.getChildrenFeatures().iterator().next();
        int subFeatureSize = possibleSmallest.getSubFeatureSize(100);
        for (Object __dummyForeachVar8 : current.getChildrenFeatures())
        {
            Feature child = (Feature)__dummyForeachVar8;
            int curChildSize = child.getSubFeatureSize(100);
            if (curChildSize < subFeatureSize)
            {
                subFeatureSize = curChildSize;
                possibleSmallest = child;
            }
             
        }
        currentSel.add(possibleSmallest);
    }

    public ProductFeatureModel createRdmPFM() throws Exception {
        RootFeature root = featureModel.getRootFeature();
        SelectedFeature rootSel = new SelectedFeature(root);
        getRdmSubFeatures(root,rootSel,null);
        this.randomPFM = new ProductFeatureModel(featureModel.getName(),"Random",rootSel);
        return randomPFM;
    }

    private static void getRdmSubFeatures(CompositeFeature current, SelectedFeature currentSel, SelectedFeature parentSel) throws Exception {
        CompositionType type = current.getChildrenCmpstType();
        if (type == CompositionType.AND)
        {
            for (Object __dummyForeachVar9 : current.getChildrenFeatures())
            {
                Feature child = (Feature)__dummyForeachVar9;
                currentSel.add(child);
            }
        }
        else if (type == CompositionType.OR)
        {
            // must have one
            selORRdmSubFeatures(current,currentSel);
        }
        else if (type == CompositionType.XOR)
        {
            selXORRdmSubFeatures(current,currentSel);
        }
        else
        {
            // partial_or
            selPatialAndRdmSubFeatures(current,currentSel);
        }   
        if (type == CompositionType.PARTIAL_AND && currentSel.getChildrenSelFeatures() == null)
            return ;
         
        for (Object __dummyForeachVar10 : currentSel.getChildrenSelFeatures())
        {
            SelectedFeature selChild = (SelectedFeature)__dummyForeachVar10;
            Feature child = selChild.getAbstractFeature();
            if (child instanceof CompositeFeature)
            {
                CompositeFeature cmpChild = (CompositeFeature)child;
                getRdmSubFeatures(cmpChild,selChild,currentSel);
            }
             
        }
    }

    private static void selORRdmSubFeatures(CompositeFeature current, SelectedFeature currentSel) throws Exception {
        boolean hasAnySel = false;
        do
        {
            for (Object __dummyForeachVar11 : current.getChildrenFeatures())
            {
                Feature child = (Feature)__dummyForeachVar11;
                Random seed = new Random(getRandomSeed());
                int value = seed.nextInt(2);
                if (value == 1)
                {
                    hasAnySel = true;
                    currentSel.add(child);
                }
                 
                System.out.println("randome: " + value + " for OR " + child.getName());
            }
        }
        while (!hasAnySel);
    }

    private static void selPatialAndRdmSubFeatures(CompositeFeature current, SelectedFeature currentSel) throws Exception {
        for (Object __dummyForeachVar12 : current.getChildrenFeatures())
        {
            Feature child = (Feature)__dummyForeachVar12;
            if (!child.isOptional())
                currentSel.add(child);
            else
            {
                Random seed = new Random(getRandomSeed());
                int value = seed.nextInt(2);
                if (value == 1)
                    currentSel.add(child);
                 
                System.out.println("randome: " + value + " for PartialAnd " + child.getName());
            } 
        }
    }

    private static void selXORRdmSubFeatures(CompositeFeature current, SelectedFeature currentSel) throws Exception {
        Feature[] children =current.getChildrenFeatures().toArray(new Feature[0]);
        Random seed = new Random(getRandomSeed());
        int pos = seed.nextInt(children.length);
        System.out.println("randome: " + pos + " for XOR " + children[pos].getName());
        currentSel.add(children[pos]);
    }

    static long getRandomSeed() throws Exception {
        byte[] bytes = new byte[4];
        SecureRandom rng = new java.security.SecureRandom();
        //rng.nextBytes(bytes);
        //return BitConverter.ToInt32(bytes, 0);
        return   rng.nextLong();
    }

}


