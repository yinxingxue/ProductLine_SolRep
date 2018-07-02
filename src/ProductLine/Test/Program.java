//
// Translated by CS2J (http://www.cs2j.com): 1/3/2014 9:42:01 PM
//

package ProductLine.Test;


import java.util.ArrayList;
import java.util.LinkedHashSet;

import ProductLine.BestFix.TestDriver;
import ProductLine.FeatureModel.CompositeFeature;
import ProductLine.FeatureModel.LeafFeature;
import ProductLine.FeatureModel.PFMGenerator;
import ProductLine.FeatureModel.ProductFeatureModel;
import ProductLine.FeatureModel.RootFeature;
import ProductLine.FeatureModel.XMLFeatureModelParserSample;
import ProductLine.Test.Program;

public class Program   
{
    /**
    * The main entry point for the application.
    */
    public static void main(String[] args) throws Exception {
        Program.Main();
    }

    static void Main() throws Exception {
//        chatSystem();
//        webPortal();
//        test();
//        TestDriver.testFitness();
     	  readFromXML("Lib/eshop_fm.xml");
    }

    private static void readFromXML(String path) {
    	try {
        	XMLFeatureModelParserSample xmlParser = new XMLFeatureModelParserSample();
        	ProductLine.FeatureModel.FeatureModel fm = xmlParser.parse(path);
			PrintFMAndGenerateProducts(fm);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//WebPortalError();
    private static void test() throws Exception {
        RootFeature rootFeature = new RootFeature("Chat",false);
        CompositeFeature output = new CompositeFeature(rootFeature,"Output",false);
        LeafFeature GUI = new LeafFeature(output,"GUI",false);
        LeafFeature CMD = new LeafFeature(output,"CMD",false);
        LeafFeature CMD2 = new LeafFeature(output,"CMD2",false);
        int[] car = { 1, 1 };
        output.setCardinality(car);
        int[] car3 = { 1, 1 };
        rootFeature.setCardinality(car3);
        ProductLine.FeatureModel.FeatureModel fm = new ProductLine.FeatureModel.FeatureModel("ChatSystem",rootFeature,3);
        fm.print();
        PFMGenerator generator = new PFMGenerator(fm);
        ProductFeatureModel pfm = generator.createMaxPFM();
        pfm.getRootSelFeature().getSubSelFeatures(100);
    }

    //<--Error here
    private static void chatSystem() throws Exception {
        RootFeature rootFeature = new RootFeature("Chat",false);
        //beginnig of output feature
        CompositeFeature output = new CompositeFeature(rootFeature,"Output",false);
        LeafFeature GUI = new LeafFeature(output,"GUI",false);
        LeafFeature CMD = new LeafFeature(output,"CMD",false);
        LeafFeature CMD2 = new LeafFeature(output,"CMD2",false);
        LeafFeature CMD3 = new LeafFeature(output,"CMD3",false);
        LeafFeature CMD4 = new LeafFeature(output,"CMD4",false);
        int[] car = { 1, 1 };
        output.setCardinality(car);
        //end of output feature
        LeafFeature logging = new LeafFeature(rootFeature,"Logging",true);
        LeafFeature authorization = new LeafFeature(rootFeature,"Authorization",true);
        LeafFeature color = new LeafFeature(rootFeature,"Color",true);
        //beginnig of enrcyption feature
        CompositeFeature enrcyption = new CompositeFeature(rootFeature,"Enrcyption",true);
        LeafFeature caesar = new LeafFeature(enrcyption,"Caesar",true);
        LeafFeature reverse = new LeafFeature(enrcyption,"Reverse",true);
        int[] car2 = { 1, 2 };
        enrcyption.setCardinality(car2);
        //end of enrcyption feature
        LeafFeature encryption_OR = new LeafFeature(rootFeature,"Encryption_OR",true);
        int[] car3 = { 1, 6 };
        rootFeature.setCardinality(car3);
        ProductLine.FeatureModel.FeatureModel fm = new ProductLine.FeatureModel.FeatureModel("ChatSystem",rootFeature,3);
    
        ProductLine.FeatureModel.Constrain.ConstrainType type = ProductLine.FeatureModel.Constrain.ConstrainType.IFF;
        ProductLine.LogicFormula.LogicFormula left = new ProductLine.LogicFormula.Prop(encryption_OR.getName());
        ProductLine.LogicFormula.LogicFormula right1 = new ProductLine.LogicFormula.Prop(caesar.getName());
        ProductLine.LogicFormula.LogicFormula right2 = new ProductLine.LogicFormula.Prop(reverse.getName());
        ArrayList<ProductLine.LogicFormula.LogicFormula> rightParas = new ArrayList<ProductLine.LogicFormula.LogicFormula>();
        rightParas.add(right1);
        rightParas.add(right2);
        ProductLine.LogicFormula.LogicFormula rightOr = new ProductLine.LogicFormula.Or(rightParas);
        ProductLine.LogicFormula.LogicFormula equal = new ProductLine.LogicFormula.Equal(left,rightOr);
        
        //Constrain constrain = new Constrain(type, preConFeatures, postConFeatures);
        ProductLine.FeatureModel.Constrain constrain = new ProductLine.FeatureModel.Constrain(type, equal);
        LinkedHashSet<ProductLine.FeatureModel.Constrain> constrainSet = new LinkedHashSet<ProductLine.FeatureModel.Constrain>();
        constrainSet.add(constrain);
        fm.setConstrainSet(constrainSet);
        PrintFMAndGenerateProducts(fm);
    }

    private static void webPortalError() throws Exception {
        RootFeature rootFeature = createTreeWebPortal();
        ProductLine.FeatureModel.FeatureModel fm = new ProductLine.FeatureModel.FeatureModel("WebPortal",rootFeature,5);
        fm.setConstrainSet(createCrossTreeWebPortal());
        fm.print();
        PFMGenerator generator = new PFMGenerator(fm);
        for (int i = 0;i <= 100;i++)
        {
            ProductFeatureModel rdmPFM = generator.createRdmPFM();
        }
    }

    private static void webPortal() throws Exception {
        RootFeature rootFeature = createTreeWebPortal();
        ProductLine.FeatureModel.FeatureModel fm = new ProductLine.FeatureModel.FeatureModel("WebPortal",rootFeature,5);
        fm.setConstrainSet(createCrossTreeWebPortal());
        PrintFMAndGenerateProducts(fm);
    }

	public static void PrintFMAndGenerateProducts(
			ProductLine.FeatureModel.FeatureModel fm) throws Exception {
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

    /// <summary>
    /// Manual generate the cross-tree constraints
    /// </summary>
    /// <returns>LinkedHashSet<Constrain> : the set of cross-tree constrains</returns>
    private static LinkedHashSet<ProductLine.FeatureModel.Constrain> createCrossTreeWebPortal() throws Exception {
    	LinkedHashSet<ProductLine.FeatureModel.Constrain> constrainSet = new LinkedHashSet<ProductLine.FeatureModel.Constrain>();
        ProductLine.FeatureModel.Constrain.ConstrainType type = ProductLine.FeatureModel.Constrain.ConstrainType.REQ;
        // Datatrandsfer => HTTPS
        ProductLine.FeatureModel.Constrain constrain1 = new ProductLine.FeatureModel.Constrain(type, createSimpleREQ("DataTransfer","HTTPS"));
        constrainSet.add(constrain1);
        // DB => database
        ProductLine.FeatureModel.Constrain constrain2 = new ProductLine.FeatureModel.Constrain(type, createSimpleREQ("DB","Database"));
        constrainSet.add(constrain2);
        // HTTPS => - ms
        ProductLine.FeatureModel.Constrain constrain3 = new ProductLine.FeatureModel.Constrain(type, createSimpleREQ("HTTPS","-Ms"));
        constrainSet.add(constrain3);
        //File => FTP
        ProductLine.FeatureModel.Constrain constrain4 = new ProductLine.FeatureModel.Constrain(type, createSimpleREQ("File","FTP"));
        constrainSet.add(constrain4);
        //KeywordSupport => Text
        ProductLine.FeatureModel.Constrain constrain5 = new ProductLine.FeatureModel.Constrain(type, createSimpleREQ("KeywordSupport","Text"));
        constrainSet.add(constrain5);
        //Dynamic => Active
        ProductLine.FeatureModel.Constrain constrain6 = new ProductLine.FeatureModel.Constrain(type, createSimpleREQ("Dynamic","Active"));
        constrainSet.add(constrain6);
        return constrainSet;
    }

    private static ProductLine.LogicFormula.LogicFormula createSimpleREQ(String p, String p_2) throws Exception {
        ProductLine.LogicFormula.LogicFormula left = createProp(p);
        ProductLine.LogicFormula.LogicFormula right = createProp(p_2);
        ProductLine.LogicFormula.LogicFormula imply = new ProductLine.LogicFormula.Imply(left,right);
        return imply;
    }

    private static ProductLine.LogicFormula.LogicFormula createProp(String p) throws Exception {
        ProductLine.LogicFormula.LogicFormula left;
        if (!p.startsWith("-"))
            left = new ProductLine.LogicFormula.Prop(p);
        else
        {
            String name = p.replace("-", "");
            ProductLine.LogicFormula.LogicFormula prop = new ProductLine.LogicFormula.Prop(name);
            left = new ProductLine.LogicFormula.Not(prop);
        } 
        return left;
    }

    private static RootFeature createTreeWebPortal() throws Exception {
        RootFeature rootFeature = new RootFeature("WebPortal",false);
        //beginnig of rootFeature
        CompositeFeature addServices = new CompositeFeature(rootFeature,"AdditionalServices",true);
        CompositeFeature webServer = new CompositeFeature(rootFeature,"WebServer",false);
        CompositeFeature persistence = new CompositeFeature(rootFeature,"Persistence",true);
        CompositeFeature security = new CompositeFeature(rootFeature,"Security",true);
        CompositeFeature performance = new CompositeFeature(rootFeature,"Performance",true);
        int[] card = { 1, 5 };
        rootFeature.setCardinality(card);
        //end of rootFeature
        //beginnig of addServices
        CompositeFeature siteStatistics = new CompositeFeature(addServices,"SiteStatistics",true);
        CompositeFeature siteSearch = new CompositeFeature(addServices,"SiteSearch",true);
        CompositeFeature adServer = new CompositeFeature(addServices,"AdServer",true);
        int[] card2 = { 0, 3 };
        addServices.setCardinality(card2);
        //end of addServices
        //beginnig of webServer
        CompositeFeature logging = new CompositeFeature(webServer,"Logging",true);
        CompositeFeature protocols = new CompositeFeature(webServer,"Protocols",true);
        CompositeFeature content = new CompositeFeature(webServer,"Content",false);
        int[] card3 = { 1, 3 };
        webServer.setCardinality(card3);
        //end of webServer
        //beginnig of persistence
        LeafFeature xML = new LeafFeature(persistence,"XML",false);
        LeafFeature database = new LeafFeature(persistence,"Database",false);
        int[] card4 = { 1, 1 };
        persistence.setCardinality(card4);
        //end of persistence
        //beginnig of security
        LeafFeature dataStorage = new LeafFeature(security,"DataStorage",true);
        LeafFeature dataTransfer = new LeafFeature(security,"DataTransfer",true);
        LeafFeature userAuthentication = new LeafFeature(security,"UserAuthentication",true);
        int[] card5 = { 1, 3 };
        security.setCardinality(card5);
        //end of security
        //beginnig of performance
        LeafFeature ms = new LeafFeature(performance,"Ms",false);
        LeafFeature sec = new LeafFeature(performance,"Sec",false);
        LeafFeature min = new LeafFeature(performance,"Min",false);
        int[] card6 = { 1, 1 };
        performance.setCardinality(card6);
        //end of performance
        //beginnig of siteStatistics
        LeafFeature basic = new LeafFeature(siteStatistics,"Basic",false);
        LeafFeature advanced = new LeafFeature(siteStatistics,"Advanced",true);
        int[] card7 = { 1, 2 };
        siteStatistics.setCardinality(card7);
        //end of siteStatistics
        //beginnig of siteSearch
        LeafFeature images = new LeafFeature(siteSearch,"Images",true);
        CompositeFeature text = new CompositeFeature(siteSearch,"Text",true);
        int[] card8 = { 0, 2 };
        siteSearch.setCardinality(card8);
        //end of siteSearch
        //beginnig of adServer
        LeafFeature reports = new LeafFeature(adServer,"Reports",false);
        LeafFeature popups = new LeafFeature(adServer,"Pop-ups",true);
        CompositeFeature banners = new CompositeFeature(adServer,"Banners",false);
        LeafFeature keyword = new LeafFeature(adServer,"KeywordSupport",true);
        int[] card9 = { 2, 4 };
        adServer.setCardinality(card9);
        //end of adServer
        //beginnig of logging
        LeafFeature db = new LeafFeature(logging,"DB",false);
        LeafFeature file = new LeafFeature(logging,"File",false);
        int[] card10 = { 1, 1 };
        logging.setCardinality(card10);
        //end of logging
        //beginnig of protocols
        LeafFeature nttp = new LeafFeature(protocols,"NTTP",true);
        LeafFeature ftp = new LeafFeature(protocols,"FTP",true);
        LeafFeature https = new LeafFeature(protocols,"HTTPS",true);
        int[] card11 = { 1, 3 };
        protocols.setCardinality(card11);
        //end of protocols
        //beginnig of content
        LeafFeature staticC = new LeafFeature(content,"Static",false);
        CompositeFeature active = new CompositeFeature(content,"Active",true);
        int[] card12 =  { 1, 2 };
        content.setCardinality(card12);
        //end of content
        //beginnig of text
        LeafFeature html = new LeafFeature(text,"HTML",false);
        LeafFeature dynamic = new LeafFeature(text,"Dynamic",true);
        int[] card13 = { 1, 2 };
        text.setCardinality(card13);
        //end of text
        //beginnig of banners
        LeafFeature image = new LeafFeature(banners,"Image",false);
        LeafFeature flash = new LeafFeature(banners,"Flash",true);
        int[] card14 =  { 1, 2 };
        banners.setCardinality(card14);
        //end of banners
        //beginnig of active
        LeafFeature ASP = new LeafFeature(active,"ASP",true);
        LeafFeature PHP = new LeafFeature(active,"PHP",true);
        LeafFeature JSP = new LeafFeature(active,"JSP",true);
        LeafFeature CGI = new LeafFeature(active,"CGI",true);
        int[] card15 = { 1, 4 };
        active.setCardinality(card15);
        return rootFeature;
    }

}


//end of active