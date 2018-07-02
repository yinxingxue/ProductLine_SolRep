package ProductLine;

import java.text.DecimalFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class ExtractResult {

	public static void main(String[] args) throws Exception {

		String[] feedBackTypes = new String[] { "fd", "mfd", "nfd" };
		String[] algos = new String[] { "IBEA", "NSGAII", "ssNGAII", "MOCell" };
		String[] attrs = new String[] { "TimeElapsed", "HV",
				"PercentageOfCorrectness" };
		String[] caseS = new String[] { "EShop", "WebPortal", "ChatSystem" };

		String template = "Evaluation_2014-09-04_15-17-17/%s_%s_5_%s";

		StringBuilder sb = new StringBuilder();
		for (String case1 : caseS) {
			for (String attr : attrs) {
			sb = new StringBuilder();
			
				for (String algo : algos) {
					
					for (String feedbackType : feedBackTypes) {									
						String fileName=String.format(template, algo,case1,feedbackType);
					
						String myattr=getAttr(fileName, attr);
						if(attr=="HV") {
							Double d=Double.parseDouble(myattr);
							 DecimalFormat df = new DecimalFormat("#.##");
							myattr= df.format(d);
						}
						sb.append(myattr);
						
						sb.append(" & ");
					}
				}
			
			sb.setLength(sb.length() - 3);
			System.out.println(sb.toString());
			System.out.println();
			}
		}

	}

	public static String getAttr(String fileName, String attribute)
			throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(fileName);
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		String expr = xpath.evaluate("//" + attribute + "/text()",
				doc.getDocumentElement());
		return expr;
	}

}
