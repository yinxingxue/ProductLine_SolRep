/**
 * 
 */
package ProductLine.FeatureModel;

import java.util.LinkedHashSet;

import ProductLine.Test.Program;

/**
 * @author yinxing
 * 
 */
public class FeatureModelAnalyzer {

	private FeatureModel featureModel;
	private LinkedHashSet<Feature> minMFSet;
	private LinkedHashSet<Feature> minMFOptChldSet;

	public FeatureModelAnalyzer(FeatureModel featureModel) {
		this.featureModel = featureModel;
	}

	public LinkedHashSet<Feature> getMinMandatoryFeatureSet() throws Exception {
		if(minMFSet!= null) return this.minMFSet;
		this.minMFSet = new LinkedHashSet<Feature>();
		RootFeature root = featureModel.getRootFeature();
		minMFSet.add(root);
		getMinMFSet(minMFSet, root);
		return this.minMFSet;
	}

	private static void getMinMFSet(LinkedHashSet<Feature> minMFSet2,
			CompositeFeature current) {

		try {
			CompositionType type = current.getChildrenCmpstType();
			LinkedHashSet<Feature> mandatorySet = new LinkedHashSet<Feature>();
			if (type == CompositionType.AND) {
				for (Object __dummyForeachVar4 : current.getChildrenFeatures()) {
					Feature child = (Feature) __dummyForeachVar4;
					minMFSet2.add(child);
					mandatorySet.add(child);
				}
			} else if (type == CompositionType.OR
					|| type == CompositionType.XOR) {
				return;
			} else {
				for (Object __dummyForeachVar5 : current.getChildrenFeatures()) {
					// partial_And
					Feature child = (Feature) __dummyForeachVar5;
					if (!child.isOptional()) {
						minMFSet2.add(child);
						mandatorySet.add(child);
					}
				}
			}
			for (Object __dummyForeachVar6 : mandatorySet) {

				Feature child = (Feature) __dummyForeachVar6;
				if (child instanceof CompositeFeature) {

					getMinMFSet(minMFSet2, (CompositeFeature) child);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public LinkedHashSet<Feature> getMinMFOptChldSet() {
		if(minMFOptChldSet!= null) return this.minMFOptChldSet;
		this.minMFOptChldSet = new LinkedHashSet<Feature>();
		try {

			for (Feature f : this.minMFSet) {
				if (f instanceof LeafFeature)
					continue;
				CompositeFeature cf = (CompositeFeature) f;
				CompositionType type = cf.getChildrenCmpstType();
				if (type == CompositionType.AND || type == CompositionType.OR
						|| type == CompositionType.XOR) {

					continue;
				} else {
					for (Object __dummyForeachVar5 : cf.getChildrenFeatures()) {
						// partial_And
						Feature child = (Feature) __dummyForeachVar5;
						if (child.isOptional()) {
							minMFOptChldSet.add(child);
						}
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.minMFOptChldSet;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
		XMLFeatureModelParserSample xmlParser = new XMLFeatureModelParserSample();
    	ProductLine.FeatureModel.FeatureModel fm = xmlParser.parse("Lib/webPortal_fm.xml");
    	Program.PrintFMAndGenerateProducts(fm);
    	FeatureModelAnalyzer fma = new FeatureModelAnalyzer(fm);
    	System.out.println("============Feature Analysis=================");
    	LinkedHashSet<Feature> minMFSet = fma.getMinMandatoryFeatureSet();
    	LinkedHashSet<Feature> minMFOptChldSet = fma.getMinMFOptChldSet();
    	System.out.println(minMFSet);
    	System.out.println(minMFOptChldSet);
		}
    	 catch (Exception e) {
 			e.printStackTrace();
 		}
	}

}
