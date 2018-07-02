package ProductLine;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class readCacheResult {
	public static void main(String[] args) {
		// read result from 15 files
		String foldername = "Evaluation_2014-03-13_23-10-12" + "/";
		String objectivenum = "5";
		// result
		String eshopruntime = "";
		String eshophv = "";
		String eshopspread = "";
		String eshopcorrect = "";
		String eshopcach="";

		String Webruntime = "";
		String Webhv = "";
		String Webspread = "";
		String Webcorrect = "";
		String Webcach="";

		String Chatruntime = "";
		String Chathv = "";
		String Chatspread = "";
		String Chatcorrect = "";
		String Chatcach="";
		try {
			EvaluationResult ibeaEshopN = ReadResult(foldername + "IBEA_EShop_"
					+ objectivenum + "_nfd");
			EvaluationResult ibeaEshopM = ReadResult(foldername + "IBEA_EShop_"
					+ objectivenum + "_mfd");
			EvaluationResult ibeaEshop = ReadResult(foldername + "IBEA_Eshop_"
					+ objectivenum + "_fd");

			EvaluationResult nsgaEshopN = ReadResult(foldername
					+ "NSGAII_EShop_" + objectivenum + "_nfd");
			EvaluationResult nsgaEshopM = ReadResult(foldername
					+ "NSGAII_EShop_" + objectivenum + "_mfd");
			EvaluationResult nsgaEshop = ReadResult(foldername
					+ "NSGAII_EShop_" + objectivenum + "_fd");

			EvaluationResult ssnsgaEshopN = ReadResult(foldername
					+ "ssNGAII_EShop_" + objectivenum + "_nfd");
			EvaluationResult ssnsgaEshopM = ReadResult(foldername
					+ "ssNGAII_EShop_" + objectivenum + "_mfd");
			EvaluationResult ssnsgaEshop = ReadResult(foldername
					+ "ssNGAII_EShop_" + objectivenum + "_fd");

			EvaluationResult mocellEshopN = ReadResult(foldername
					+ "MOCell_EShop_" + objectivenum + "_nfd");
			EvaluationResult mocellEshopM = ReadResult(foldername
					+ "MOCell_EShop_" + objectivenum + "_mfd");
			EvaluationResult mocellEshop = ReadResult(foldername
					+ "MOCell_EShop_" + objectivenum + "_fd");

			EvaluationResult fastEshopN = ReadResult(foldername
					+ "FastPGA_EShop_" + objectivenum + "_nfd");
			EvaluationResult fastEshopM = ReadResult(foldername
					+ "FastPGA_EShop_" + objectivenum + "_mfd");
			EvaluationResult fastEshop = ReadResult(foldername
					+ "FastPGA_EShop_" + objectivenum + "_fd");

			EvaluationResult ibeaWebN = ReadResult(foldername
					+ "IBEA_WebPortal_" + objectivenum + "_nfd");
			EvaluationResult ibeaWebM = ReadResult(foldername
					+ "IBEA_WebPortal_" + objectivenum + "_mfd");
			EvaluationResult ibeaWeb = ReadResult(foldername
					+ "IBEA_WebPortal_" + objectivenum + "_fd");

			EvaluationResult nsgaWebN = ReadResult(foldername
					+ "NSGAII_WebPortal_" + objectivenum + "_nfd");
			EvaluationResult nsgaWebM = ReadResult(foldername
					+ "NSGAII_WebPortal_" + objectivenum + "_mfd");
			EvaluationResult nsgaWeb = ReadResult(foldername
					+ "NSGAII_WebPortal_" + objectivenum + "_fd");

			EvaluationResult ssnsgaWebN = ReadResult(foldername
					+ "ssNGAII_WebPortal_" + objectivenum + "_nfd");
			EvaluationResult ssnsgaWebM = ReadResult(foldername
					+ "ssNGAII_WebPortal_" + objectivenum + "_mfd");
			EvaluationResult ssnsgaWeb = ReadResult(foldername
					+ "ssNGAII_WebPortal_" + objectivenum + "_fd");

			EvaluationResult mocellWebN = ReadResult(foldername
					+ "MOCell_WebPortal_" + objectivenum + "_nfd");
			EvaluationResult mocellWebM = ReadResult(foldername
					+ "MOCell_WebPortal_" + objectivenum + "_mfd");
			EvaluationResult mocellWeb = ReadResult(foldername
					+ "MOCell_WebPortal_" + objectivenum + "_fd");

			EvaluationResult fastWebN = ReadResult(foldername
					+ "FastPGA_WebPortal_" + objectivenum + "_nfd");
			EvaluationResult fastWebM = ReadResult(foldername
					+ "FastPGA_WebPortal_" + objectivenum + "_mfd");
			EvaluationResult fastWeb = ReadResult(foldername
					+ "FastPGA_WebPortal_" + objectivenum + "_fd");

			EvaluationResult ibeaChatN = ReadResult(foldername
					+ "IBEA_ChatSystem_" + objectivenum + "_nfd");
			EvaluationResult ibeaChatM = ReadResult(foldername
					+ "IBEA_ChatSystem_" + objectivenum + "_mfd");
			EvaluationResult ibeaChat = ReadResult(foldername
					+ "IBEA_ChatSystem_" + objectivenum + "_fd");

			EvaluationResult nsgaChatN = ReadResult(foldername
					+ "NSGAII_ChatSystem_" + objectivenum + "_nfd");
			EvaluationResult nsgaChatM = ReadResult(foldername
					+ "NSGAII_ChatSystem_" + objectivenum + "_mfd");
			EvaluationResult nsgaChat = ReadResult(foldername
					+ "NSGAII_ChatSystem_" + objectivenum + "_fd");

			EvaluationResult ssnsgaChatN = ReadResult(foldername
					+ "ssNGAII_ChatSystem_" + objectivenum + "_nfd");
			EvaluationResult ssnsgaChatM = ReadResult(foldername
					+ "ssNGAII_ChatSystem_" + objectivenum + "_mfd");
			EvaluationResult ssnsgaChat = ReadResult(foldername
					+ "ssNGAII_ChatSystem_" + objectivenum + "_fd");

			EvaluationResult mocellChatN = ReadResult(foldername
					+ "MOCell_ChatSystem_" + objectivenum + "_nfd");
			EvaluationResult mocellChatM = ReadResult(foldername
					+ "MOCell_ChatSystem_" + objectivenum + "_mfd");
			EvaluationResult mocellChat = ReadResult(foldername
					+ "MOCell_ChatSystem_" + objectivenum + "_fd");

			EvaluationResult fastChatN = ReadResult(foldername
					+ "FastPGA_ChatSystem_" + objectivenum + "_nfd");
			EvaluationResult fastChatM = ReadResult(foldername
					+ "FastPGA_ChatSystem_" + objectivenum + "_mfd");
			EvaluationResult fastChat = ReadResult(foldername
					+ "FastPGA_ChatSystem_" + objectivenum + "_fd");

			// eshop runtime
			eshopruntime = " & " + ibeaEshop.TimeElapsed + " & "
					+ ibeaEshopM.TimeElapsed + " & " + ibeaEshopN.TimeElapsed
					+ " & " + nsgaEshop.TimeElapsed + " & "
					+ nsgaEshopM.TimeElapsed + " & " + nsgaEshopN.TimeElapsed
					+ " & " + ssnsgaEshop.TimeElapsed + " & "
					+ ssnsgaEshopM.TimeElapsed + " & "
					+ ssnsgaEshopN.TimeElapsed + " & "
					+ mocellEshop.TimeElapsed + " & "
					+ mocellEshopM.TimeElapsed + " & "
					+ mocellEshopN.TimeElapsed + " & " + fastEshop.TimeElapsed
					+ " & " + fastEshopM.TimeElapsed + " & "
					+ fastEshopN.TimeElapsed;

			// eshop hv
			eshophv = " & " + String.format("%.2f", ibeaEshop.HV) + " & "
					+ String.format("%.2f", ibeaEshopM.HV) + " & "
					+ String.format("%.2f", ibeaEshopN.HV) + " & "
					+ String.format("%.2f", nsgaEshop.HV) + " & "
					+ String.format("%.2f", nsgaEshopM.HV) + " & "
					+ String.format("%.2f", nsgaEshopN.HV) + " & "
					+ String.format("%.2f", ssnsgaEshop.HV) + " & "
					+ String.format("%.2f", ssnsgaEshopM.HV) + " & "
					+ String.format("%.2f", ssnsgaEshopN.HV) + " & "
					+ String.format("%.2f", mocellEshop.HV) + " & "
					+ String.format("%.2f", mocellEshopM.HV) + " & "
					+ String.format("%.2f", mocellEshopN.HV) + " & "
					+ String.format("%.2f", fastEshop.HV) + " & "
					+ String.format("%.2f", fastEshopM.HV) + " & "
					+ String.format("%.2f", fastEshopN.HV);

			// eshop spread
			eshopspread = " & " + String.format("%.2f", ibeaEshop.Spread)
					+ " & " + String.format("%.2f", ibeaEshopM.Spread) + " & "
					+ String.format("%.2f", ibeaEshopN.Spread) + " & "
					+ String.format("%.2f", nsgaEshop.Spread) + " & "
					+ String.format("%.2f", nsgaEshopM.Spread) + " & "
					+ String.format("%.2f", nsgaEshopN.Spread) + " & "
					+ String.format("%.2f", ssnsgaEshop.Spread) + " & "
					+ String.format("%.2f", ssnsgaEshopM.Spread) + " & "
					+ String.format("%.2f", ssnsgaEshopN.Spread) + " & "
					+ String.format("%.2f", mocellEshop.Spread) + " & "
					+ String.format("%.2f", mocellEshopM.Spread) + " & "
					+ String.format("%.2f", mocellEshopN.Spread) + " & "
					+ String.format("%.2f", fastEshop.Spread) + " & "
					+ String.format("%.2f", fastEshopM.Spread) + " & "
					+ String.format("%.2f", fastEshopN.Spread);

			// eshop correct
			eshopcorrect = " & "
					+ String.format("%.2f", ibeaEshop.PercentageOfCorrectness)
					+ " & "
					+ String.format("%.2f", ibeaEshopM.PercentageOfCorrectness)
					+ " & "
					+ String.format("%.2f", ibeaEshopN.PercentageOfCorrectness)
					+ " & "
					+ String.format("%.2f", nsgaEshop.PercentageOfCorrectness)
					+ " & "
					+ String.format("%.2f", nsgaEshopM.PercentageOfCorrectness)
					+ " & "
					+ String.format("%.2f", nsgaEshopN.PercentageOfCorrectness)
					+ " & "
					+ String.format("%.2f", ssnsgaEshop.PercentageOfCorrectness)
					+ " & "
					+ String.format("%.2f",
							ssnsgaEshopM.PercentageOfCorrectness)
					+ " & "
					+ String.format("%.2f",
							ssnsgaEshopN.PercentageOfCorrectness)
					+ " & "
					+ String.format("%.2f", mocellEshop.PercentageOfCorrectness)
					+ " & "
					+ String.format("%.2f",
							mocellEshopM.PercentageOfCorrectness)
					+ " & "
					+ String.format("%.2f",
							mocellEshopN.PercentageOfCorrectness) + " & "
					+ String.format("%.2f", fastEshop.PercentageOfCorrectness)
					+ " & "
					+ String.format("%.2f", fastEshopM.PercentageOfCorrectness)
					+ " & "
					+ String.format("%.2f", fastEshopN.PercentageOfCorrectness);
			
			// eshop cache missed
			eshopcach=" & "
					+ String.format("%.2f", ibeaEshop.cacheMissed)
					+ " & "
					+ String.format("%.2f", ibeaEshopM.cacheMissed)
					+ " & "
					+ String.format("%.2f", ibeaEshopN.cacheMissed)
					+ " & "
					+ String.format("%.2f", nsgaEshop.cacheMissed)
					+ " & "
					+ String.format("%.2f", nsgaEshopM.cacheMissed)
					+ " & "
					+ String.format("%.2f", nsgaEshopN.cacheMissed)
					+ " & "
					+ String.format("%.2f", ssnsgaEshop.cacheMissed)
					+ " & "
					+ String.format("%.2f",
							ssnsgaEshopM.cacheMissed)
					+ " & "
					+ String.format("%.2f",
							ssnsgaEshopN.cacheMissed)
					+ " & "
					+ String.format("%.2f", mocellEshop.cacheMissed)
					+ " & "
					+ String.format("%.2f",
							mocellEshopM.cacheMissed)
					+ " & "
					+ String.format("%.2f",
							mocellEshopN.cacheMissed) + " & "
					+ String.format("%.2f", fastEshop.cacheMissed)
					+ " & "
					+ String.format("%.2f", fastEshopM.cacheMissed)
					+ " & "
					+ String.format("%.2f", fastEshopN.cacheMissed);

			// Web runtime
			Webruntime = " & " + ibeaWeb.TimeElapsed + " & "
					+ ibeaWebM.TimeElapsed + " & " + ibeaWebN.TimeElapsed
					+ " & " + nsgaWeb.TimeElapsed + " & "
					+ nsgaWebM.TimeElapsed + " & " + nsgaWebN.TimeElapsed
					+ " & " + ssnsgaWeb.TimeElapsed + " & "
					+ ssnsgaWebM.TimeElapsed + " & " + ssnsgaWebN.TimeElapsed
					+ " & " + mocellWeb.TimeElapsed + " & "
					+ mocellWebM.TimeElapsed + " & " + mocellWebN.TimeElapsed
					+ " & " + fastWeb.TimeElapsed + " & "
					+ fastWebM.TimeElapsed + " & " + fastWebN.TimeElapsed;

			// Web hv
			Webhv = " & " + String.format("%.2f", ibeaWeb.HV) + " & "
					+ String.format("%.2f", ibeaWebM.HV) + " & "
					+ String.format("%.2f", ibeaWebN.HV) + " & "
					+ String.format("%.2f", nsgaWeb.HV) + " & "
					+ String.format("%.2f", nsgaWebM.HV) + " & "
					+ String.format("%.2f", nsgaWebN.HV) + " & "
					+ String.format("%.2f", ssnsgaWeb.HV) + " & "
					+ String.format("%.2f", ssnsgaWebM.HV) + " & "
					+ String.format("%.2f", ssnsgaWebN.HV) + " & "
					+ String.format("%.2f", mocellWeb.HV) + " & "
					+ String.format("%.2f", mocellWebM.HV) + " & "
					+ String.format("%.2f", mocellWebN.HV) + " & "
					+ String.format("%.2f", fastWeb.HV) + " & "
					+ String.format("%.2f", fastWebM.HV) + " & "
					+ String.format("%.2f", fastWebN.HV);

			// Web spread
			Webspread = " & " + String.format("%.2f", ibeaWeb.Spread) + " & "
					+ String.format("%.2f", ibeaWebM.Spread) + " & "
					+ String.format("%.2f", ibeaWebN.Spread) + " & "
					+ String.format("%.2f", nsgaWeb.Spread) + " & "
					+ String.format("%.2f", nsgaWebM.Spread) + " & "
					+ String.format("%.2f", nsgaWebN.Spread) + " & "
					+ String.format("%.2f", ssnsgaWeb.Spread) + " & "
					+ String.format("%.2f", ssnsgaWebM.Spread) + " & "
					+ String.format("%.2f", ssnsgaWebN.Spread) + " & "
					+ String.format("%.2f", mocellWeb.Spread) + " & "
					+ String.format("%.2f", mocellWebM.Spread) + " & "
					+ String.format("%.2f", mocellWebN.Spread) + " & "
					+ String.format("%.2f", fastWeb.Spread) + " & "
					+ String.format("%.2f", fastWebM.Spread) + " & "
					+ String.format("%.2f", fastWebN.Spread);

			// Web correct
			Webcorrect = " & "
					+ String.format("%.2f", ibeaWeb.PercentageOfCorrectness)
					+ " & "
					+ String.format("%.2f", ibeaWebM.PercentageOfCorrectness)
					+ " & "
					+ String.format("%.2f", ibeaWebN.PercentageOfCorrectness)
					+ " & "
					+ String.format("%.2f", nsgaWeb.PercentageOfCorrectness)
					+ " & "
					+ String.format("%.2f", nsgaWebM.PercentageOfCorrectness)
					+ " & "
					+ String.format("%.2f", nsgaWebN.PercentageOfCorrectness)
					+ " & "
					+ String.format("%.2f", ssnsgaWeb.PercentageOfCorrectness)
					+ " & "
					+ String.format("%.2f", ssnsgaWebM.PercentageOfCorrectness)
					+ " & "
					+ String.format("%.2f", ssnsgaWebN.PercentageOfCorrectness)
					+ " & "
					+ String.format("%.2f", mocellWeb.PercentageOfCorrectness)
					+ " & "
					+ String.format("%.2f", mocellWebM.PercentageOfCorrectness)
					+ " & "
					+ String.format("%.2f", mocellWebN.PercentageOfCorrectness)
					+ " & "
					+ String.format("%.2f", fastWeb.PercentageOfCorrectness)
					+ " & "
					+ String.format("%.2f", fastWebM.PercentageOfCorrectness)
					+ " & "
					+ String.format("%.2f", fastWebN.PercentageOfCorrectness);
			
			//web cach
			Webcach = " & "
					+ String.format("%.2f", ibeaWeb.cacheMissed)
					+ " & "
					+ String.format("%.2f", ibeaWebM.cacheMissed)
					+ " & "
					+ String.format("%.2f", ibeaWebN.cacheMissed)
					+ " & "
					+ String.format("%.2f", nsgaWeb.cacheMissed)
					+ " & "
					+ String.format("%.2f", nsgaWebM.cacheMissed)
					+ " & "
					+ String.format("%.2f", nsgaWebN.cacheMissed)
					+ " & "
					+ String.format("%.2f", ssnsgaWeb.cacheMissed)
					+ " & "
					+ String.format("%.2f", ssnsgaWebM.cacheMissed)
					+ " & "
					+ String.format("%.2f", ssnsgaWebN.cacheMissed)
					+ " & "
					+ String.format("%.2f", mocellWeb.cacheMissed)
					+ " & "
					+ String.format("%.2f", mocellWebM.cacheMissed)
					+ " & "
					+ String.format("%.2f", mocellWebN.cacheMissed)
					+ " & "
					+ String.format("%.2f", fastWeb.cacheMissed)
					+ " & "
					+ String.format("%.2f", fastWebM.cacheMissed)
					+ " & "
					+ String.format("%.2f", fastWebN.cacheMissed);

			// Chat runtime
			Chatruntime = " & " + ibeaChat.TimeElapsed + " & "
					+ ibeaChatM.TimeElapsed + " & " + ibeaChatN.TimeElapsed
					+ " & " + nsgaChat.TimeElapsed + " & "
					+ nsgaChatM.TimeElapsed + " & " + nsgaChatN.TimeElapsed
					+ " & " + ssnsgaChat.TimeElapsed + " & "
					+ ssnsgaChatM.TimeElapsed + " & " + ssnsgaChatN.TimeElapsed
					+ " & " + mocellChat.TimeElapsed + " & "
					+ mocellChatM.TimeElapsed + " & " + mocellChatN.TimeElapsed
					+ " & " + fastChat.TimeElapsed + " & "
					+ fastChatM.TimeElapsed + " & " + fastChatN.TimeElapsed;

			// Chat hv
			Chathv = " & " + String.format("%.2f", ibeaChat.HV) + " & "
					+ String.format("%.2f", ibeaChatM.HV) + " & "
					+ String.format("%.2f", ibeaChatN.HV) + " & "
					+ String.format("%.2f", nsgaChat.HV) + " & "
					+ String.format("%.2f", nsgaChatM.HV) + " & "
					+ String.format("%.2f", nsgaChatN.HV) + " & "
					+ String.format("%.2f", ssnsgaChat.HV) + " & "
					+ String.format("%.2f", ssnsgaChatM.HV) + " & "
					+ String.format("%.2f", ssnsgaChatN.HV) + " & "
					+ String.format("%.2f", mocellChat.HV) + " & "
					+ String.format("%.2f", mocellChatM.HV) + " & "
					+ String.format("%.2f", mocellChatN.HV) + " & "
					+ String.format("%.2f", fastChat.HV) + " & "
					+ String.format("%.2f", fastChatM.HV) + " & "
					+ String.format("%.2f", fastChatN.HV);

			// Chat spread
			Chatspread = " & " + String.format("%.2f", ibeaChat.Spread) + " & "
					+ String.format("%.2f", ibeaChatM.Spread) + " & "
					+ String.format("%.2f", ibeaChatN.Spread) + " & "
					+ String.format("%.2f", nsgaChat.Spread) + " & "
					+ String.format("%.2f", nsgaChatM.Spread) + " & "
					+ String.format("%.2f", nsgaChatN.Spread) + " & "
					+ String.format("%.2f", ssnsgaChat.Spread) + " & "
					+ String.format("%.2f", ssnsgaChatM.Spread) + " & "
					+ String.format("%.2f", ssnsgaChatN.Spread) + " & "
					+ String.format("%.2f", mocellChat.Spread) + " & "
					+ String.format("%.2f", mocellChatM.Spread) + " & "
					+ String.format("%.2f", mocellChatN.Spread) + " & "
					+ String.format("%.2f", fastChat.Spread) + " & "
					+ String.format("%.2f", fastChatM.Spread) + " & "
					+ String.format("%.2f", fastChatN.Spread);

			// Chat correct
			Chatcorrect = " & "
					+ String.format("%.2f", ibeaChat.PercentageOfCorrectness)
					+ " & "
					+ String.format("%.2f", ibeaChatM.PercentageOfCorrectness)
					+ " & "
					+ String.format("%.2f", ibeaChatN.PercentageOfCorrectness)
					+ " & "
					+ String.format("%.2f", nsgaChat.PercentageOfCorrectness)
					+ " & "
					+ String.format("%.2f", nsgaChatM.PercentageOfCorrectness)
					+ " & "
					+ String.format("%.2f", nsgaChatN.PercentageOfCorrectness)
					+ " & "
					+ String.format("%.2f", ssnsgaChat.PercentageOfCorrectness)
					+ " & "
					+ String.format("%.2f", ssnsgaChatM.PercentageOfCorrectness)
					+ " & "
					+ String.format("%.2f", ssnsgaChatN.PercentageOfCorrectness)
					+ " & "
					+ String.format("%.2f", mocellChat.PercentageOfCorrectness)
					+ " & "
					+ String.format("%.2f", mocellChatM.PercentageOfCorrectness)
					+ " & "
					+ String.format("%.2f", mocellChatN.PercentageOfCorrectness)
					+ " & "
					+ String.format("%.2f", fastChat.PercentageOfCorrectness)
					+ " & "
					+ String.format("%.2f", fastChatM.PercentageOfCorrectness)
					+ " & "
					+ String.format("%.2f", fastChatN.PercentageOfCorrectness);
			
			//chat cach
			Chatcach = " & "
					+ String.format("%.2f", ibeaChat.cacheMissed)
					+ " & "
					+ String.format("%.2f", ibeaChatM.cacheMissed)
					+ " & "
					+ String.format("%.2f", ibeaChatN.cacheMissed)
					+ " & "
					+ String.format("%.2f", nsgaChat.cacheMissed)
					+ " & "
					+ String.format("%.2f", nsgaChatM.cacheMissed)
					+ " & "
					+ String.format("%.2f", nsgaChatN.cacheMissed)
					+ " & "
					+ String.format("%.2f", ssnsgaChat.cacheMissed)
					+ " & "
					+ String.format("%.2f", ssnsgaChatM.cacheMissed)
					+ " & "
					+ String.format("%.2f", ssnsgaChatN.cacheMissed)
					+ " & "
					+ String.format("%.2f", mocellChat.cacheMissed)
					+ " & "
					+ String.format("%.2f", mocellChatM.cacheMissed)
					+ " & "
					+ String.format("%.2f", mocellChatN.cacheMissed)
					+ " & "
					+ String.format("%.2f", fastChat.cacheMissed)
					+ " & "
					+ String.format("%.2f", fastChatM.cacheMissed)
					+ " & "
					+ String.format("%.2f", fastChatN.cacheMissed);
	System.out.println("Eshop Time:    "+eshopruntime);
	System.out.println("Chat Time:    "+Chatruntime);
	System.out.println("Web Time:    "+Webruntime);
	
	System.out.println("Eshop cach:    "+eshopcach);
	System.out.println("Chat cach:    "+Chatcach);
	System.out.println("Web cache:    "+Webcach);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}



public static EvaluationResult ReadResult(String filename) {
	EvaluationResult result = new EvaluationResult();
	BufferedReader br = null;
	try {
		br = new BufferedReader(new FileReader(filename));
		double hv;
		double spread;
		double PercentageOfCorrectness;
		double cache;
		long TimeElapsed;
		String line = br.readLine();
		line = br.readLine();
		line = br.readLine();
		line = br.readLine();
		line = br.readLine();

		// hv
		String hvstring = line.trim();
		hv = Double
				.parseDouble(hvstring.substring(4, hvstring.length() - 5));

		// spread
		line = br.readLine();
		String spreadstring = line.trim();
		spread = Double.parseDouble(spreadstring.substring(8,
				spreadstring.length() - 9));

		// PercentageOfCorrectness
		line = br.readLine();
		String correctstring = line.trim();
		PercentageOfCorrectness = Double.parseDouble(correctstring
				.substring(25, correctstring.length() - 26));
        //cache
		line=br.readLine();
		String cachestring=line.trim();
		cache=Double.parseDouble(cachestring.substring(13,cachestring.length()-14));
		
		// Time
		line = br.readLine();
		String timestring = line.trim();
		TimeElapsed = Long.parseLong(timestring.substring(13,
				timestring.length() - 14));

		result.HV = hv;
		result.PercentageOfCorrectness = PercentageOfCorrectness;
		result.TimeElapsed = TimeElapsed;
		result.cacheMissed=cache;
		result.Spread = spread;
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} finally {
		try {
			if (br != null)
				br.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	return result;
}
}
