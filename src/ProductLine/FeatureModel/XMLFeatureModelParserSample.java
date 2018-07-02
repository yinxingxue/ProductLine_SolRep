package ProductLine.FeatureModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import ProductLine.LogicFormula.LogicFormula;
import constraints.CNFClause;
import constraints.CNFLiteral;
import constraints.PropositionalFormula;
import fm.FeatureGroup;
import fm.FeatureModel;
import fm.FeatureModelStatistics;
import fm.FeatureTreeNode;
import fm.GroupedFeature;
import fm.RootNode;
import fm.SolitaireFeature;
import fm.XMLFeatureModel;

public class XMLFeatureModelParserSample {

	private HashMap<String,String> id2Name = new HashMap<String,String> ();  
	private HashMap<String,String> name2Id = new HashMap<String,String> ();
	
	public static void main(String args[]) {
		new XMLFeatureModelParserSample().parse("Lib/webPortal_fm.xml");
	}

	public ProductLine.FeatureModel.FeatureModel parse(String filePath) {

		try {

			// String featureModelFile = "Lib/webPortal_fm.xml";
			//String featureModelFile = "Lib/eshop_fm.xml";
 
			id2Name.clear();
			name2Id.clear();

			/*
			 * Creates the Feature Model Object ********************************
			 * - Constant USE_VARIABLE_NAME_AS_ID indicates that if an ID has
			 * not been defined for a feature node in the XML file the feature
			 * name should be used as the ID. - Constant SET_ID_AUTOMATICALLY
			 * can be used to let the system create an unique ID for feature
			 * nodes without an ID specification Note: if an ID is specified for
			 * a feature node in the XML file it will always prevail
			 */
			FeatureModel featureModel = new XMLFeatureModel(filePath,
					XMLFeatureModel.USE_VARIABLE_NAME_AS_ID);

			// Load the XML file and creates the feature model
			featureModel.loadModel();

			// A feature model object contains a feature tree and a set of
			// contraints
			// Let's traverse the feature tree first. We start at the root
			// feature in depth first search.
			System.out.println("FEATURE TREE --------------------------------");
			RootFeature rootFeature = traverseDFSNew(featureModel.getRoot(), 0);
			ProductLine.FeatureModel.FeatureModel fm = new ProductLine.FeatureModel.FeatureModel(
					"WebPortal", rootFeature, 100);
			fm.setConstrainSet(traverseConstraints2(featureModel));
			return fm;
			// printTreeConstrains(fm);
			// printStatistics(featureModel);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param featureModel
	 */
	private void printStatistics(FeatureModel featureModel) {
		// Now, let's traverse the extra constraints as a CNF formula
		System.out.println("EXTRA CONSTRAINTS ---------------------------");
		traverseConstraints(featureModel);

		// Now, let's print some statistics about the feature model
		FeatureModelStatistics stats = new FeatureModelStatistics(featureModel);
		stats.update();

		stats.dump();
	}

	/**
	 * @param fm
	 * @throws Exception
	 */
	private void printTreeConstrains(ProductLine.FeatureModel.FeatureModel fm)
			throws Exception {
		fm.print();
		System.out.println("========================");
		System.out.println(fm.getName() + "'s Constrains:");
		fm.convertToLogicFormula();
		System.out.println("========================");
		System.out.println(fm.getName() + "'s tree-structure:");
		fm.getRootFeature().display(0);
		PFMGenerator generator = new PFMGenerator(fm);
		ProductFeatureModel maxPFM = generator.createMaxPFM();
		System.out.println("========================");
		System.out.println(fm.getName() + "Max PFM:");
		maxPFM.display();
		ProductFeatureModel minPFM = generator.createMinPFM();
		System.out.println("========================");
		System.out.println(fm.getName() + "Min PFM:");
		minPFM.display();
		ProductFeatureModel rdmPFM = generator.createRdmPFM();
		System.out.println("========================");
		System.out.println(fm.getName() + "Random PFM:");
		rdmPFM.display();
	}

	public RootFeature traverseDFSNew(FeatureTreeNode node, int tab) {
	
		RootFeature rootFeature = null;
		try {
			// if this is the Root node.
			if (node instanceof RootNode) {
				RootNode root = (RootNode) node;
				 
			    String uqiFeatureName = createUniFeatureName(getUpperCaseNanme(node.getName()));
				this.id2Name.put(root.getID(), uqiFeatureName);
                this.name2Id.put(uqiFeatureName, root.getID());
				
				// create the new RootFeature
				rootFeature = new RootFeature(uqiFeatureName, false);

				// if it is Feature Group, then it is composite feature
				if (root.getChildCount() == 1
						&& root.getFirstChild() instanceof FeatureGroup) {
					// set the cardinality of RootFeature
					FeatureGroup group = (FeatureGroup) node.getFirstChild();
					setCardinality(root, rootFeature);

					Enumeration<?> e = group.children();
					while (e.hasMoreElements()) {
						GroupedFeature gf = (GroupedFeature) e.nextElement();
						traverseDFSRecursive(rootFeature, gf, tab++);
					}
				}
				// if it is composite feature
				else if (root.getChildCount() != 0) {
					Enumeration<?> e = root.children();
					setCardinality(root, rootFeature);
					while (e.hasMoreElements()) {
						SolitaireFeature sf = (SolitaireFeature) e
								.nextElement();
						traverseDFSRecursive(rootFeature, sf, tab++);
					}
				}

				// if it is leaf feature
				else if (root.getChildCount() == 0) {
					int[] card = { 0, 0 };
					rootFeature.setCardinality(card);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rootFeature;
	}

	/**
	 * @param node
	 * @param compositeFeature
	 * @return
	 * @throws Exception
	 */
	private void setCardinality(FeatureTreeNode node,
			CompositeFeature compositeFeature) throws Exception {

		if (node.getChildCount() == 1
				&& node.getFirstChild() instanceof FeatureGroup) {
			FeatureGroup group = (FeatureGroup) node.getFirstChild();
			int min = group.getMin();
			int max = group.getMax();
			if (max == -1)
				max = group.getChildCount();
			int[] card = { min, max };
			compositeFeature.setCardinality(card);
		} else if (!node.isLeaf() && node.getChildCount() >= 1
				&& node.getFirstChild() instanceof SolitaireFeature) {

			Enumeration<?> e = node.children();
			int max = node.getChildCount();
			int min = 0;
			while (e.hasMoreElements()) {

				boolean optional = false;
				Object o = e.nextElement();
				if (o instanceof SolitaireFeature) {
					SolitaireFeature sf = (SolitaireFeature) o;
					optional = sf.isOptional();
				} else if (o instanceof FeatureGroup) {
					FeatureGroup sf = (FeatureGroup) o;
					throw new Exception("Unknown node " + sf.getName()
							+ " type!");
					// optional = sf.isOptional();
				} else
					throw new Exception("Unknown node " + node.getName()
							+ " type!");

				if (!optional)
					min++;
			}
			int[] card = { min, max };
			compositeFeature.setCardinality(card);
		} else
			throw new Exception("Unknown node " + node.getName() + " type!");

	}

	private void traverseDFSRecursive(CompositeFeature parentFeature,
			FeatureTreeNode node, int i) throws Exception {
		String uqiFeatureName = createUniFeatureName(getUpperCaseNanme(node.getName()));
		this.id2Name.put(node.getID(), uqiFeatureName);
		this.name2Id.put(uqiFeatureName, node.getID());
		
		// if it is Feature Group, then it is composite feature
		if (node.getChildCount() == 1
				&& node.getFirstChild() instanceof FeatureGroup) {
			// set the cardinality of RootFeature
			FeatureGroup group = (FeatureGroup) node.getFirstChild();

			// check whether it is XOR or OR feature
			int type = getParentRelationshipType(node);

			CompositeFeature compFeature = new CompositeFeature(parentFeature,
					uqiFeatureName, checkOptional(node, type));
			setCardinality(node, compFeature);

			Enumeration<?> e = group.children();
			while (e.hasMoreElements()) {
				GroupedFeature gf = (GroupedFeature) e.nextElement();
				traverseDFSRecursive(compFeature, gf, i++);
			}
		}
		// if it is composite feature
		else if (node.getChildCount() != 0) {

			// check whether it is XOR or OR feature
			int type = getParentRelationshipType(node);
			CompositeFeature compFeature = new CompositeFeature(parentFeature,
					uqiFeatureName, checkOptional(node, type));
			setCardinality(node, compFeature);

			Enumeration<?> e = node.children();
			while (e.hasMoreElements()) {

				FeatureTreeNode sf = (FeatureTreeNode) e.nextElement();
				if (sf instanceof FeatureGroup) {
					System.out.println();
				}
				traverseDFSRecursive(compFeature, sf, i++);
			}
		}

		// if it is leaf feature
		else if (node.getChildCount() == 0) {
			// check whether it is XOR or OR feature
			int type = getParentRelationshipType(node);
			LeafFeature leaf = new LeafFeature(parentFeature, uqiFeatureName,
					checkOptional(node, type));
		}
	}

	private String createUniFeatureName(String featureName) {
		String uqiFeatureName = featureName;
		int i = 1;
		while(this.name2Id.containsKey(uqiFeatureName))
		{
			uqiFeatureName = featureName+"_"+i++;
		}
		return uqiFeatureName;
	}

	private boolean checkOptional(FeatureTreeNode node, int type)
			throws Exception {

		if (type == -1) {
			throw new Exception("not known type!");
		} else if (type == 0)
			return false;
		else if (type == 1) {
			SolitaireFeature soli = (SolitaireFeature) node;
			return soli.isOptional();
		} else if (type == 2) {
			return true;
		}
		return true;
	}

	private int getParentRelationshipType(FeatureTreeNode node) {

		if (node.getParent() instanceof SolitaireFeature
				|| node.getParent() instanceof RootNode
				|| node.getParent() instanceof GroupedFeature) {
			// partial And, or And
			return 1;
		} else if (node.getParent() instanceof FeatureGroup) {
			FeatureGroup fg = (FeatureGroup) node.getParent();
			// OR
			if (fg.getMax() == -1)
				return 2;
			// XOR
			if (fg.getMax() == 1)
				return 0;
		}
		return -1;
	}

	public void traverseDFS(FeatureTreeNode node, int tab) throws Exception {
		for (int j = 0; j < tab; j++) {
			System.out.print("\t");
		}
		// Root Feature
		if (node instanceof RootNode) {
			System.out.print("Root");
		}
		// Solitaire Feature
		else if (node instanceof SolitaireFeature) {
			// Optional Feature
			if (((SolitaireFeature) node).isOptional())
				System.out.print("Optional");
			// Mandatory Feature
			else
				System.out.print("Mandatory");
		}
		// Feature Group
		else if (node instanceof FeatureGroup) {
			int minCardinality = ((FeatureGroup) node).getMin();
			int maxCardinality = ((FeatureGroup) node).getMax();
			System.out.print("Feature Group[" + minCardinality + ","
					+ maxCardinality + "]");
		}
		// Grouped feature
		else {
			System.out.print("Grouped");
		}
		System.out.print("(ID=" + node.getID() + ", NAME=" + node.getName()
				+ ")\r\n");
		for (int i = 0; i < node.getChildCount(); i++) {
			traverseDFS((FeatureTreeNode) node.getChildAt(i), tab + 1);
		}
	}

	private String getUpperCaseNanme(String id) {
		String init = id;
		init = init.replaceFirst(init.substring(0, 1), init.substring(0, 1)
				.toUpperCase());
		init = init.replace(" ", "_");
		return init;
	}

	public void traverseConstraints(FeatureModel featureModel) {
		for (PropositionalFormula formula : featureModel.getConstraints()) {
			System.out.println(formula);
		}
	}

	public LinkedHashSet<ProductLine.FeatureModel.Constrain> traverseConstraints2(
			FeatureModel featureModel) throws Exception {
		LinkedHashSet<ProductLine.FeatureModel.Constrain> constrainSet = new LinkedHashSet<ProductLine.FeatureModel.Constrain>();
		for (PropositionalFormula formula : featureModel.getConstraints()) {
			// System.out.println(formula);
			// ProductLine.FeatureModel.Constrain constrain1 = new
			// ProductLine.FeatureModel.Constrain(type,
			// createSimpleREQ("DataTransfer","HTTPS"));
			Collection<CNFClause> cnfs = formula.toCNFClauses();
			Iterator<CNFClause> clauses = cnfs.iterator();
			while (clauses.hasNext()) {
				CNFClause cnf = clauses.next();
				// System.out.print(cnfs+": ");
				// System.out.println(cnf.getLiterals());

				ProductLine.FeatureModel.Constrain.ConstrainType type = ProductLine.FeatureModel.Constrain.ConstrainType.DNF;
				// Datatrandsfer => HTTPS
				ProductLine.FeatureModel.Constrain constrain1 = new ProductLine.FeatureModel.Constrain(
						type, createSimpleOR(cnf.getLiterals()));
				constrainSet.add(constrain1);
			}
		}
		return constrainSet;
	}

	private LogicFormula createSimpleOR(List<CNFLiteral> literals) throws Exception {

		ArrayList<ProductLine.LogicFormula.LogicFormula> paras= new ArrayList<ProductLine.LogicFormula.LogicFormula>();
		for(CNFLiteral literal: literals)
		{
		   if(literal.isPositive())	
		   {
			   String id = literal.getVariable().getID();
			   if(!this.id2Name.containsKey(id)) throw new Exception("No such key "+ id+" found!");
			   ProductLine.LogicFormula.LogicFormula prop = new ProductLine.LogicFormula.Prop(this.id2Name.get(id));
			   paras.add(prop);
		   }
		   else 
		   {
			   String id = literal.getVariable().getID();
			   if(!this.id2Name.containsKey(id)) throw new Exception("No such key "+ id+" found!");
			   ProductLine.LogicFormula.LogicFormula prop = new ProductLine.LogicFormula.Prop(this.id2Name.get(id));
			   ProductLine.LogicFormula.LogicFormula not = new ProductLine.LogicFormula.Not(prop);
			   paras.add(not);
		   }
		}
		
		ProductLine.LogicFormula.LogicFormula dnf = new ProductLine.LogicFormula.Or(paras);
		return dnf;
	}

}
