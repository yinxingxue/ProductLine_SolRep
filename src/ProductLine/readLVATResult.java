package ProductLine;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
public class readLVATResult {
	public static void main(String[] args) throws Exception {
		
		

		
		String xpath="/object-stream/ProductLine.EvaluationResult/";
		
		
		
		
		
		String[] casestudies=new String[] {"ECos","FreeBSD","UCLinux"};
		String[] configs=new String[] {"fd","fd_higherMutation","mfd"};
		String[] attrs=new String[] {"TimeElapsed","HV","PercentageOfCorrectness","evaluationTo50PercentCorrectness"};
	
		String foldername = "E:/Code_SPL_Java/ProductLine/Evaluation_2014-09-04_15-30-40 (LVAT)/";
		//EvaluationResult result=EvaluationDriver.ReadResult(foldername+"IBEA_ChatSystem_5_mfd");
		//System.out.println(result.TimeElapsed);
		
		
		
		
		XPath xPath =  XPathFactory.newInstance().newXPath();
		
		
		for (String casestudy:casestudies) {
			for (String attr:attrs) {
				
					for (String config:configs) {
						String fileName=String.format("IBEA_%s_5_%s", casestudy,config);
						File fXmlFile = new File(foldername+fileName);
						DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
						DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
						Document doc = dBuilder.parse(fXmlFile);
						doc.getDocumentElement().normalize();
						
						
						String attrValue = xPath.compile(xpath+attr).evaluate(doc);
						if (attr=="HV") {
							System.out.print(round(Double.parseDouble(attrValue),2)+"\t");
							
						}else {
							System.out.print(attrValue+"\t");
						}
					}
				
				System.out.println("");
			}
		}
		
		//read a string value

		
		
//		System.out.println(time);
//		System.out.println(round(Double.parseDouble(hv),2));
//		System.out.println(correctness);
		
		
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
}
