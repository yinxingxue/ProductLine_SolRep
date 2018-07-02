//
// Translated by CS2J (http://www.cs2j.com): 1/3/2014 9:42:00 PM
//

package ProductLine.BestFix;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import ProductLine.GAParams;
import ProductLine.GAParams.CaseStudy;
import ProductLine.Utility;
import ProductLine.FeatureModel.CompositeFeature;
import ProductLine.FeatureModel.DimacsModelAdapter;
import ProductLine.FeatureModel.Feature;
import ProductLine.FeatureModel.FeatureModel;
import ProductLine.FeatureModel.LeafFeature;
import ProductLine.FeatureModel.LogicFeatureModel;
import ProductLine.FeatureModel.RootFeature;
import ProductLine.FeatureModel.XMLFeatureModelParserSample;
import ProductLine.LogicFormula.And;
import ProductLine.LogicFormula.Imply;
import ProductLine.LogicFormula.LogicFormula;
import ProductLine.LogicFormula.Not;
import ProductLine.LogicFormula.Or;
import ProductLine.LogicFormula.Prop;

public class TestDriver {
	public static ProductLine.FeatureModel.FeatureModel NewFeatureModel = null;
	/**
	 * OldFeature is previously use for minimal fixing, not used for feedback
	 * directed EA
	 **/
	public static LinkedHashSet<String> OldFeature = null;

	public static void constructTestSystem1() throws Exception {
		RootFeature rootFeature = new RootFeature("Chat", false);
		// CompositeFeature output = new CompositeFeature(rootFeature, "A",
		// true);
		LeafFeature GUI = new LeafFeature(rootFeature, "A", true);
		LeafFeature CMD = new LeafFeature(rootFeature, "B", false);
		LeafFeature CMD2 = new LeafFeature(rootFeature, "C", true);
		LeafFeature CMD3 = new LeafFeature(rootFeature, "D", true);
		// int[] car = {1, 1};
		// output.SetCardinality(car);
		int[] car3 = { 1, 4 };
		rootFeature.setCardinality(car3);
		NewFeatureModel = new ProductLine.FeatureModel.FeatureModel(
				"ChatSystem", rootFeature, 3);
		NewFeatureModel.print();
		OldFeature = new LinkedHashSet<String>();
	}

	public static void constructTestSystem2() throws Exception {
		RootFeature rootFeature = new RootFeature("Chat", false);
		// CompositeFeature output = new CompositeFeature(rootFeature, "A",
		// true);
		LeafFeature GUI = new LeafFeature(rootFeature, "A", false);
		LeafFeature CMD = new LeafFeature(rootFeature, "B", false);
		LeafFeature CMD2 = new LeafFeature(rootFeature, "C", false);
		LeafFeature CMD3 = new LeafFeature(rootFeature, "D", false);
		int[] car = { 1, 1 };
		rootFeature.setCardinality(car);
		NewFeatureModel = new ProductLine.FeatureModel.FeatureModel(
				"ChatSystem", rootFeature, 3);
		NewFeatureModel.print();
		OldFeature = new LinkedHashSet<String>();
	}
	
	public static void createModelThroughFM(FeatureModel fm, 
			String MandatoryPath,
			String MustNotInPath) throws Exception {
			
		ArrayList<String> featureNames = new ArrayList<String>();
		
		for(Feature fea:fm.getFeatureSet()){
			featureNames.add(fea.getName());
		}
		

		
		// mandatoryPath
				
				ArrayList<String> mandatoryFeatureNames = new ArrayList<String>();
				
				if(GAParams.P_filterMandatory) {
					if (!(MandatoryPath == null || MandatoryPath.trim() == "")) {
						mandatoryFeatureNames = Utility.readFixFeatures(MandatoryPath);
					}
				}
				// mustNotInPath
				ArrayList<String> mustNotInFeatureNames = new ArrayList<String>();
				
				if(GAParams.P_filterMandatory) {
					if (!(MustNotInPath == null || MustNotInPath.trim() == "")) {
						mustNotInFeatureNames = Utility.readFixFeatures(MustNotInPath);
					}
				}
		
				//reduce logic formulas from fixed features
				fm.convertToLogicFormula();
				LogicFormula formula=fm.getCombinedLogicFormular();
//				ArrayList<String> result=new ArrayList<String>();
//				for(LogicFormula cnf:formula.toCNF()) {
//					result.add(cnf.toString());
//				}
//				ArrayList<String> variables =new ArrayList<String>();
//				for(Feature cnf:fm.getFeatureSet()) {
//					variables.add(cnf.getName());
//				}
//				LogicFormula.writeCNFFile("D:\\C_Drive\\Desktop\\nouse\\EShop.txt",result,variables);
				
				ArrayList<LogicFormula> conjuncts=new ArrayList<LogicFormula>();
				GAParams.flattenAnd(formula, conjuncts);
				
				LinkedHashSet<LogicFormula> originalFormula=new LinkedHashSet<LogicFormula>(conjuncts);
				LinkedHashSet<LogicFormula> resultFormulae=new LinkedHashSet<LogicFormula>();
				
				if(GAParams.P_filterMandatory) {
					for(LogicFormula lf:originalFormula) {
						LinkedHashSet<String> props=lf.getAllProps();
						for(String prop:props) {
							if(!mandatoryFeatureNames.contains(prop)&& !mustNotInFeatureNames.contains(prop)) {
								resultFormulae.add(lf);
								break;
							}
						}		
					}
				}else {
					resultFormulae=originalFormula;
				}
				
				NewFeatureModel = new LogicFeatureModel(
						resultFormulae, featureNames,
						mandatoryFeatureNames, mustNotInFeatureNames);
				NewFeatureModel.setName(fm.getName());
				RootFeature root= fm.getRootFeature();
				if(root!=null)
				{
					NewFeatureModel.setRootFeature(root);
				}
				OldFeature = new LinkedHashSet<String>();
	}

	private static void createModelThroughDimacs(CaseStudy caseStudy, String featurePath,
			String MandatoryPath, String MustNotInPath) throws Exception {
		// feature Path
		DimacsModelAdapter adapter = new DimacsModelAdapter(featurePath);
		adapter.loadModel();
		LinkedHashSet<Feature> fea = adapter.getFeatureSet();
		ArrayList<String> featureNames = new ArrayList<String>();
		for (Feature f : fea) {
			featureNames.add(f.getName());
		}
		// mandatoryPath
		ArrayList<String> mandatoryFeatureNames = new ArrayList<String>();
		if (!(MandatoryPath == null || MandatoryPath.trim() == "")) {
			mandatoryFeatureNames = Utility.readFixFeatures(MandatoryPath);
		}
		// mustNotInPath
		ArrayList<String> mustNotInFeatureNames = new ArrayList<String>();
		if (!(MustNotInPath == null || MustNotInPath.trim() == "")) {
			mustNotInFeatureNames = Utility.readFixFeatures(MustNotInPath);
		}
		
		//reduce logic formulas from fixed features
		LinkedHashSet<LogicFormula> originalFormula=adapter.getCombinedLogicFormula();
		LinkedHashSet<LogicFormula> resultFormulae=new LinkedHashSet<LogicFormula>();
//		StringBuffer sb=new StringBuffer();
		for(LogicFormula fm:originalFormula) {
			LinkedHashSet<String> props=fm.getAllProps();
			for(String prop:props) {
				if(!mandatoryFeatureNames.contains(prop)&& !mustNotInFeatureNames.contains(prop)) {
					resultFormulae.add(fm);
//					sb.append(fm+"\r\n");
					break;
				}
			}		
		}
//		String path = "C:\\Users\\manman\\Desktop\\aa.txt";
//		Files.write( Paths.get(path), sb.toString().getBytes(), StandardOpenOption.CREATE);
		
		
		

		NewFeatureModel = new LogicFeatureModel(
				resultFormulae, featureNames,
				mandatoryFeatureNames, mustNotInFeatureNames);
		NewFeatureModel.setName(caseStudy.toString());
		OldFeature = new LinkedHashSet<String>();
	}

	public static void main(String[] args) throws Exception {
		constructWebPortal();
	}

	public static void constructChatSystem() throws Exception {
		RootFeature rootFeature = new RootFeature("Chat", false);
 
		CompositeFeature output = new CompositeFeature(rootFeature, "Output",
				false);
		LeafFeature GUI = new LeafFeature(output, "GUI", false);
		LeafFeature CMD = new LeafFeature(output, "CMD", false);
		LeafFeature CMD2 = new LeafFeature(output, "GUI2", false);
 
		int[] car = { 1, 1 };
		output.setCardinality(car);
		// end of output feature
		LeafFeature logging = new LeafFeature(rootFeature, "Logging", true);
		LeafFeature authorization = new LeafFeature(rootFeature,
				"Authorization", true);
		LeafFeature color = new LeafFeature(rootFeature, "Color", true);
		// beginnig of enrcyption feature
		CompositeFeature enrcyption = new CompositeFeature(rootFeature,
				"Enrcyption", true);
		LeafFeature caesar = new LeafFeature(enrcyption, "Caesar", true);
		LeafFeature reverse = new LeafFeature(enrcyption, "Reverse", true);
		int[] car2 = { 1, 2 };
		enrcyption.setCardinality(car2);
		// end of enrcyption feature
		LeafFeature encryption_OR = new LeafFeature(rootFeature,
				"Encryption_OR", true);
		int[] car3 = { 1, 6 };
		rootFeature.setCardinality(car3);
		ProductLine.FeatureModel.FeatureModel fm = new ProductLine.FeatureModel.FeatureModel(
				"ChatSystem", rootFeature, 3);
		// create constrains for "Encryption_OR iff Caesar or Reverse;"
		// Hashtable preConFeatures = new Hashtable();
		// preConFeatures.Add(encryption_OR, false);
		// Hashtable postConFeatures = new Hashtable();
		// postConFeatures.Add(caesar, false);
		// postConFeatures.Add(reverse, false);
		ProductLine.FeatureModel.Constrain.ConstrainType type = ProductLine.FeatureModel.Constrain.ConstrainType.IFF;
		ProductLine.LogicFormula.LogicFormula left = new Prop(
				encryption_OR.getName());
		ProductLine.LogicFormula.LogicFormula right1 = new Prop(
				caesar.getName());
		ProductLine.LogicFormula.LogicFormula right2 = new Prop(
				reverse.getName());
		ArrayList<ProductLine.LogicFormula.LogicFormula> rightParas = new ArrayList<ProductLine.LogicFormula.LogicFormula>();
		rightParas.add(right1);
		rightParas.add(right2);
		ProductLine.LogicFormula.LogicFormula rightOr = new Or(rightParas);
		ProductLine.LogicFormula.LogicFormula equal = new ProductLine.LogicFormula.Equal(
				left, rightOr);
		// Constrain constrain = new Constrain(type, preConFeatures,
		// postConFeatures);
		ProductLine.FeatureModel.Constrain constrain = new ProductLine.FeatureModel.Constrain(
				type, equal);
		LinkedHashSet<ProductLine.FeatureModel.Constrain> constrainSet = new LinkedHashSet<ProductLine.FeatureModel.Constrain>();
		constrainSet.add(constrain);
		fm.setConstrainSet(constrainSet);
		fm.print();
		//NewFeatureModel = fm;
		// OldFeature = new LinkedHashSet<String>() { "Chat", "GUI", "CMD",
		// "Caesar" };// time=3.21s
		//OldFeature = new LinkedHashSet<String>(Arrays.asList("Chat", "Logging"));// time
		
		
		createModelThroughFM(fm,
				"data/Yes_ChatSystem.txt",
				"data/No_ChatSystem.txt");// =2.39s,
		// different
																					// results
		// OldFeature = new LinkedHashSet<String>();
	}

	// time =2.6s, result:can not get the result
	// OldFeature = new LinkedHashSet<String>() { "Chat", "Color",
	// "Encryption_OR"
	// };//Chat,Output,GUI,Color,Enrcyption,Caesar,Encryption_OR, time=2.6s
	public static void constructWebPortal() throws Exception {
		LinkedHashSet<ProductLine.FeatureModel.Constrain> constrainSet = new LinkedHashSet<ProductLine.FeatureModel.Constrain>();
		RootFeature rootFeature = createTreeWebPortal(constrainSet);
		FeatureModel fm = new ProductLine.FeatureModel.FeatureModel(
				"WebPortal", rootFeature, 5);
		// OldFeature = new LinkedHashSet<String>(Arrays.asList( "WebPortal" ));
		//OldFeature = new LinkedHashSet<String>(Arrays.asList("WebPortal",
				//"AdditionalServices", "SiteStatistics", "Basic"));
		fm.setConstrainSet(constrainSet);
		
		createModelThroughFM(fm,
				"data/Yes_WebPortal.txt",
				"data/No_WebPortal.txt");// =2.39s,
	}

	private static FeatureModel readFromXML(String path) {
		try {
			XMLFeatureModelParserSample xmlParser = new XMLFeatureModelParserSample();
			ProductLine.FeatureModel.FeatureModel fm = xmlParser.parse(path);
			return fm;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void ConstructEShop() throws Exception {
		
		createModelThroughFM(readFromXML("./Lib/eshop_fm.xml"),
				"data/Yes_EShop.txt",
				"data/No_EShop.txt");
		//NewFeatureModel = ;
		//OldFeature = new LinkedHashSet<String>(Arrays.asList("eShop"));
	}

	private static RootFeature createTreeWebPortal(LinkedHashSet<ProductLine.FeatureModel.Constrain> constrainSet) throws Exception {
		RootFeature rootFeature = new RootFeature("WebPortal", false);
		// beginnig of rootFeature
		CompositeFeature addServices = new CompositeFeature(rootFeature,
				"AdditionalServices", true);
		CompositeFeature webServer = new CompositeFeature(rootFeature,
				"WebServer", false);
		CompositeFeature persistence = new CompositeFeature(rootFeature,
				"Persistence", true);
		CompositeFeature security = new CompositeFeature(rootFeature,
				"Security", true);
		CompositeFeature performance = new CompositeFeature(rootFeature,
				"Performance", true);
		int[] card = { 1, 5 };
		rootFeature.setCardinality(card);
		// end of rootFeature
		// beginnig of addServices
		CompositeFeature siteStatistics = new CompositeFeature(addServices,
				"SiteStatistics", true);
		CompositeFeature siteSearch = new CompositeFeature(addServices,
				"SiteSearch", true);
		CompositeFeature adServer = new CompositeFeature(addServices,
				"AdServer", true);
		int[] card2 = { 0, 3 };
		addServices.setCardinality(card2);
		// end of addServices
		// beginnig of webServer
		CompositeFeature logging = new CompositeFeature(webServer, "Logging",
				true);
		CompositeFeature protocols = new CompositeFeature(webServer,
				"Protocols", true);
		CompositeFeature content = new CompositeFeature(webServer, "Content",
				false);
		int[] card3 = { 1, 3 };
		webServer.setCardinality(card3);
		// end of webServer
		// beginnig of persistence
		LeafFeature xML = new LeafFeature(persistence, "XML", false);
		LeafFeature database = new LeafFeature(persistence, "Database", false);
		int[] card4 = { 1, 1 };
		persistence.setCardinality(card4);
		// end of persistence
		// beginnig of security
		LeafFeature dataStorage = new LeafFeature(security, "DataStorage", true);
		LeafFeature dataTransfer = new LeafFeature(security, "DataTransfer",
				true);
		LeafFeature userAuthentication = new LeafFeature(security,
				"UserAuthentication", true);
		int[] card5 = { 1, 3 };
		security.setCardinality(card5);
		// end of security
		// beginnig of performance
		LeafFeature ms = new LeafFeature(performance, "Ms", false);
		LeafFeature sec = new LeafFeature(performance, "Sec", false);
		LeafFeature min = new LeafFeature(performance, "Min", false);
		int[] card6 = { 1, 1 };
		performance.setCardinality(card6);
		// end of performance
		// beginnig of siteStatistics
		LeafFeature basic = new LeafFeature(siteStatistics, "Basic", false);
		LeafFeature advanced = new LeafFeature(siteStatistics, "Advanced", true);
		int[] card7 = { 1, 2 };
		siteStatistics.setCardinality(card7);
		// end of siteStatistics
		// beginnig of siteSearch
		LeafFeature images = new LeafFeature(siteSearch, "Images", true);
		CompositeFeature text = new CompositeFeature(siteSearch, "Text", true);
		int[] card8 = { 0, 2 };
		siteSearch.setCardinality(card8);
		// end of siteSearch
		// beginnig of adServer
		LeafFeature reports = new LeafFeature(adServer, "Reports", false);
		LeafFeature popups = new LeafFeature(adServer, "Pop-ups", true);
		CompositeFeature banners = new CompositeFeature(adServer, "Banners",
				false);
		LeafFeature keyword = new LeafFeature(adServer, "KeywordSupport", true);
		int[] card9 = { 2, 4 };
		adServer.setCardinality(card9);
		// end of adServer
		// beginnig of logging
		LeafFeature db = new LeafFeature(logging, "DB", false);
		LeafFeature file = new LeafFeature(logging, "File", false);
		int[] card10 = { 1, 1 };
		logging.setCardinality(card10);
		// end of logging
		// beginnig of protocols
		LeafFeature nttp = new LeafFeature(protocols, "NTTP", true);
		LeafFeature ftp = new LeafFeature(protocols, "FTP", true);
		LeafFeature https = new LeafFeature(protocols, "HTTPS", true);
		int[] card11 = { 1, 3 };
		protocols.setCardinality(card11);
		// end of protocols
		// beginnig of content
		LeafFeature staticC = new LeafFeature(content, "Static", false);
		CompositeFeature active = new CompositeFeature(content, "Active", true);
		int[] card12 = { 1, 2 };
		content.setCardinality(card12);
		// end of content
		// beginnig of text
		LeafFeature html = new LeafFeature(text, "HTML", false);
		LeafFeature dynamic = new LeafFeature(text, "Dynamic", true);
		int[] card13 = { 1, 2 };
		text.setCardinality(card13);
		// end of text
		// beginnig of banners
		LeafFeature image = new LeafFeature(banners, "Image", false);
		LeafFeature flash = new LeafFeature(banners, "Flash", true);
		int[] card14 = { 1, 2 };
		banners.setCardinality(card14);
		// end of banners
		// beginnig of active
		LeafFeature ASP = new LeafFeature(active, "ASP", true);
		LeafFeature PHP = new LeafFeature(active, "PHP", true);
		LeafFeature JSP = new LeafFeature(active, "JSP", true);
		LeafFeature CGI = new LeafFeature(active, "CGI", true);
		int[] card15 = { 1, 4 };
		active.setCardinality(card15);
	
		ProductLine.FeatureModel.Constrain.ConstrainType imply = ProductLine.FeatureModel.Constrain.ConstrainType.REQ;
		ProductLine.FeatureModel.Constrain.ConstrainType exclu = ProductLine.FeatureModel.Constrain.ConstrainType.EXC;
		
		//Data Transfer => HTTPS 
		ProductLine.LogicFormula.LogicFormula left1 = new Prop(dataTransfer.getName());
		ProductLine.LogicFormula.LogicFormula right1 = new Prop(https.getName());
		ProductLine.LogicFormula.LogicFormula lf1 = new Imply(left1,right1);
		ProductLine.FeatureModel.Constrain constrain1 = new ProductLine.FeatureModel.Constrain(
				imply, lf1);
		constrainSet.add(constrain1);
		
		//DB  => Database  
		ProductLine.LogicFormula.LogicFormula left2 = new Prop(db.getName());
		ProductLine.LogicFormula.LogicFormula right2 = new Prop(database.getName());
		ProductLine.LogicFormula.LogicFormula lf2 = new Imply(left2,right2);
		ProductLine.FeatureModel.Constrain constrain2 = new ProductLine.FeatureModel.Constrain(
				imply, lf2);
		constrainSet.add(constrain2);
		
		//HTTPS  EXQ Ms  
		ProductLine.LogicFormula.LogicFormula left3 = new Prop(https.getName());
		ProductLine.LogicFormula.LogicFormula right3 = new Prop(ms.getName());
		ProductLine.LogicFormula.LogicFormula not = new And(left3,right3);
		ProductLine.LogicFormula.LogicFormula lf3 = new Not(not);
		ProductLine.FeatureModel.Constrain constrain3 = new ProductLine.FeatureModel.Constrain(
				exclu, lf3);
		constrainSet.add(constrain3);
		
		// File => FTP 
		ProductLine.LogicFormula.LogicFormula left4 = new Prop(file.getName());
		ProductLine.LogicFormula.LogicFormula right4 = new Prop(ftp.getName());
		ProductLine.LogicFormula.LogicFormula lf4 = new Imply(left4,right4);
		ProductLine.FeatureModel.Constrain constrain4 = new ProductLine.FeatureModel.Constrain(
				imply, lf4);
		constrainSet.add(constrain4);
		
		//Keyword Support  => Text 
		ProductLine.LogicFormula.LogicFormula left5 = new Prop(keyword.getName());
		ProductLine.LogicFormula.LogicFormula right5 = new Prop(text.getName());
		ProductLine.LogicFormula.LogicFormula lf5 = new Imply(left5,right5);
		ProductLine.FeatureModel.Constrain constrain5 = new ProductLine.FeatureModel.Constrain(
				imply, lf5);
		constrainSet.add(constrain5);
		
		// Dynamic => Active  
		ProductLine.LogicFormula.LogicFormula left6 = new Prop(dynamic.getName());
		ProductLine.LogicFormula.LogicFormula right6 = new Prop(active.getName());
		ProductLine.LogicFormula.LogicFormula lf6 = new Imply(left6,right6);
		ProductLine.FeatureModel.Constrain constrain6 = new ProductLine.FeatureModel.Constrain(
				imply, lf6);
		constrainSet.add(constrain6);
		return rootFeature;
	}

	public static void constructFM(GAParams.CaseStudy caseStudy)
			throws Exception {
		// constructWebPortal();
		// constructChatSystem();

		// Tax_gateways,Services,Custom_tax_gateway,Debit_card,Guest_checkout,Wish_list_save_after_session,Services_fulfillment,Mail,Wish_list,
		// Mandatory:
		// EShop,Store_front,Business_management,Catalog,Buy_paths,Product_Information,Product_type,Basic_information,Shopping_cart,Checkout,Order_confirmation,Inventory_management_policy,Cart_content_page,Checkout_type,Taxation_options,Payment_options,Payment_types,Order_management,Administration,Fulfillment,Content_management,Store_administration,Product_database_management,Presentation_options,General_layout_management,Site_search,Search_engine_registration,Domain_name_setup,
		// minFeature:9(calc)+28(mand)
		GAParams.currCaseStudy = caseStudy;
		
		if (caseStudy == GAParams.CaseStudy.EShop) {
			ConstructEShop();
		} else if (caseStudy == GAParams.CaseStudy.WebPortal) {
			constructWebPortal();
		} else if (caseStudy == GAParams.CaseStudy.ChatSystem) {
			constructChatSystem();
		} else if (caseStudy == GAParams.CaseStudy.ECos) {
			constructECos();
		} else if (caseStudy == GAParams.CaseStudy.UCLinux) {
			constructUCLinux();
		} else if (caseStudy == GAParams.CaseStudy.LinuxX86) {
			constructLinuxX86();
		} else if (caseStudy == GAParams.CaseStudy.FreeBSD) {
			constructFreeBSD();
		} else if(caseStudy == GAParams.CaseStudy.Fiasco) {
			constructFiasco();
		} 
//		else if(caseStudy == GAParams.CaseStudy.Other) {
//			if (GAParams.GAParams.sxfmFile!=null) {
//				createModelThroughFM(readFromXML(GAParams.GAParams.sxfmFile),
//						null,
//						null);
//			}else {
//				createModelThroughDimacs(
//						GAParams.GAParams.dimacsFile,
//						GAParams.GAParams.commonFeatureFile,
//						GAParams.GAParams.deadFeatureFile);
//			}
//
//		}
	}
	
	
	public static void constructFreeBSD() throws Exception {
		createModelThroughDimacs(GAParams.CaseStudy.FreeBSD,
				"data/freebsd-icse11.dimacs", 
				"data/Yes_FreeBSD.txt",
				"data/No_FreeBSD.txt");
	}
	public static void constructFiasco() throws Exception {
		createModelThroughDimacs(GAParams.CaseStudy.Fiasco,
				"data/fiasco.dimacs", 
				"data/Yes_Fiasco.txt",
				"data/No_Fiasco.txt");
	}
	
	public static void constructECos() throws Exception {
		createModelThroughDimacs(GAParams.CaseStudy.ECos,
				"data/ecos-icse11.dimacs", 
				"data/Yes_ECos.txt",
				"data/No_ECos.txt");
	}
	private static void constructLinuxX86() throws Exception{
		// TODO Auto-generated method stub
		createModelThroughDimacs(GAParams.CaseStudy.LinuxX86,
				"data/LinuxX86.dimacs",
				"data/Yes_LinuxX86.txt",
				"data/No_LinuxX86.txt");
	}

	private static void constructUCLinux() throws Exception{
		// TODO Auto-generated method stub
		createModelThroughDimacs(GAParams.CaseStudy.UCLinux,
				"data/uClinux.dimacs",
				"data/Yes_uCLinux.txt",
				"data/No_uCLinux.txt");
	}

	// end of active
	public static void testFitness() throws Exception {

		constructFM(GAParams.CaseStudy.EShop);

		GAParams.initialize(NewFeatureModel, OldFeature);
		long beginTime = System.currentTimeMillis();
		GA ga = new GA();
		ga.optimize();
		int[] GABestGene;
		double fitness;

		Result res = ga.getBest();
		GABestGene = res.values;
		fitness = res.fitness;

		System.out.println(String.format("Result from GA (Fitness Value:%s)",
				fitness));
		LinkedHashSet<String> Features = GAParams.getFeatures(GABestGene);
		for (String s : Features) {

			System.out.print(s + ",");
		}
		System.out.println();
		System.out.println();
		int[] bestGene = EditDistance.minimalChange(GAParams.oldGenes.Genes,
				GABestGene);
		System.out.println("Result After Minimal Change");
		Features = GAParams.getFeatures(GABestGene);
		for (String s : Features) {

			System.out.print(s + ",");
		}
		System.out.println();
		long endTime = System.currentTimeMillis();

		long difference = endTime - beginTime;
		System.out.println((double) difference / 1000 + "s");
		System.out.println();
		System.out.println();
	}

}
