package ProductLine;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class WriteResult {
	public static void main(String[] agrs) {
		// read result from 15 files
		String foldername = "Evaluation_2014-09-03_13-35-49" + "/";
		String objectivenum = "5";
		//result
		String eshopruntime="";
	    String eshophv="";
	    String eshopspread="";
	    String eshopcorrect="";
	    
	    String Webruntime="";
	    String Webhv="";
	    String Webspread="";
	    String Webcorrect="";
	    
	    String Chatruntime="";
	    String Chathv="";
	    String Chatspread="";
	    String Chatcorrect="";
		try {
			EvaluationResult ibeaEshopN = ReadResult(foldername + "IBEA_EShop_" + objectivenum+ "_false");
			EvaluationResult ibeaEshop = ReadResult(foldername
					+ "IBEA_Eshop_" + objectivenum + "_true");
			EvaluationResult nsgaEshopN = ReadResult(foldername + "NSGAII_EShop_" + objectivenum
							+ "_false");
			EvaluationResult nsgaEshop = ReadResult(foldername
					+ "NSGAII_EShop_" + objectivenum + "_true");
			EvaluationResult ssnsgaEshopN = ReadResult(foldername + "ssNGAII_EShop_" + objectivenum
							+ "_false");
			EvaluationResult ssnsgaEshop = ReadResult(foldername + "ssNGAII_EShop_" + objectivenum
						+ "_true");
			EvaluationResult mocellEshopN = ReadResult(foldername + "MOCell_EShop_" + objectivenum
							+ "_false");
			EvaluationResult mocellEshop =ReadResult(foldername + "MOCell_EShop_" + objectivenum
							+ "_true");
			EvaluationResult fastEshopN = ReadResult(foldername + "FastPGA_EShop_" + objectivenum
							+ "_false");
			EvaluationResult fastEshop = ReadResult(foldername
					+ "FastPGA_EShop_" + objectivenum + "_true");

			EvaluationResult ibeaWebN = ReadResult(foldername
					+ "IBEA_WebPortal_" + objectivenum + "_false");
			EvaluationResult ibeaWeb = ReadResult(foldername
					+ "IBEA_WebPortal_" + objectivenum + "_true");
			EvaluationResult nsgaWebN = ReadResult(foldername
					+ "NSGAII_WebPortal_" + objectivenum + "_false");
			EvaluationResult nsgaWeb = ReadResult(foldername
					+ "NSGAII_WebPortal_" + objectivenum + "_true");
			EvaluationResult ssnsgaWebN = ReadResult(foldername + "ssNGAII_WebPortal_"
							+ objectivenum + "_false");
			EvaluationResult ssnsgaWeb = ReadResult(foldername
					+ "ssNGAII_WebPortal_" + objectivenum + "_true");
			EvaluationResult mocellWebN = ReadResult(foldername + "MOCell_WebPortal_" + objectivenum
							+ "_false");
			EvaluationResult mocellWeb = ReadResult(foldername
					+ "MOCell_WebPortal_" + objectivenum + "_true");
			EvaluationResult fastWebN = ReadResult(foldername
					+ "FastPGA_WebPortal_" + objectivenum + "_false");
			EvaluationResult fastWeb = ReadResult(foldername
					+ "FastPGA_WebPortal_" + objectivenum + "_true");

			EvaluationResult ibeaChatN = ReadResult(foldername
					+ "IBEA_ChatSystem_" + objectivenum + "_false");
			EvaluationResult ibeaChat = ReadResult(foldername
					+ "IBEA_ChatSystem_" + objectivenum + "_true");
			EvaluationResult nsgaChatN = ReadResult(foldername
					+ "NSGAII_ChatSystem_" + objectivenum + "_false");
			EvaluationResult nsgaChat = ReadResult(foldername
					+ "NSGAII_ChatSystem_" + objectivenum + "_true");
			EvaluationResult ssnsgaChatN = ReadResult(foldername + "ssNGAII_ChatSystem_"
							+ objectivenum + "_false");
			EvaluationResult ssnsgaChat =ReadResult(foldername + "ssNGAII_ChatSystem_"
							+ objectivenum + "_true");
			EvaluationResult mocellChatN = ReadResult(foldername + "MOCell_ChatSystem_"
							+ objectivenum + "_false");
			EvaluationResult mocellChat = ReadResult(foldername + "MOCell_ChatSystem_"
							+ objectivenum + "_true");
			EvaluationResult fastChatN = ReadResult(foldername
					+ "FastPGA_ChatSystem_" + objectivenum + "_false");
			EvaluationResult fastChat = ReadResult(foldername
					+ "FastPGA_ChatSystem_" + objectivenum + "_true");

			// eshop runtime
		  eshopruntime = " & " + ibeaEshop.TimeElapsed + " & "
					+ ibeaEshopN.TimeElapsed + " & " + nsgaEshop.TimeElapsed
					+ " & " + nsgaEshopN.TimeElapsed + " & "
					+ ssnsgaEshop.TimeElapsed+" & "+ssnsgaEshopN.TimeElapsed+" & "+ mocellEshop.TimeElapsed+" & "+ mocellEshopN.TimeElapsed+" & "+fastEshop.TimeElapsed+" & "+fastEshopN.TimeElapsed;
          
           
           //eshop hv
          eshophv= " & " + String.format( "%.2f",ibeaEshop.HV) + " & "
					+ String.format( "%.2f",ibeaEshopN.HV) + " & " + String.format( "%.2f",nsgaEshop.HV)
					+ " & " + String.format( "%.2f",nsgaEshopN.HV) + " & "
					+ String.format( "%.2f",ssnsgaEshop.HV)+" & "+String.format( "%.2f",ssnsgaEshopN.HV)+" & "+ String.format( "%.2f",mocellEshop.HV)+" & "+ String.format( "%.2f",mocellEshopN.HV)+" & "+String.format( "%.2f",fastEshop.HV)+" & "+String.format( "%.2f",fastEshopN.HV);
		 
		  
		  //eshop spread
		eshopspread= " & " + String.format( "%.2f",ibeaEshop.Spread) + " & "
					+ String.format( "%.2f",ibeaEshopN.Spread) + " & " + String.format( "%.2f",nsgaEshop.Spread)
					+ " & " + String.format( "%.2f",nsgaEshopN.Spread) + " & "
					+ String.format( "%.2f",ssnsgaEshop.Spread)+" & "+String.format( "%.2f",ssnsgaEshopN.Spread)+" & "+ String.format( "%.2f",mocellEshop.Spread)+" & "+ String.format( "%.2f",mocellEshopN.Spread)+" & "+String.format( "%.2f",fastEshop.Spread)+" & "+String.format( "%.2f",fastEshopN.Spread);
		  
		  //eshop correct
		eshopcorrect= " & " + String.format( "%.2f",ibeaEshop.PercentageOfCorrectness) + " & "
					+ String.format( "%.2f",ibeaEshopN.PercentageOfCorrectness) + " & " + String.format( "%.2f",nsgaEshop.PercentageOfCorrectness)
					+ " & " + String.format( "%.2f",nsgaEshopN.PercentageOfCorrectness) + " & "
					+ String.format( "%.2f",ssnsgaEshop.PercentageOfCorrectness)+" & "+String.format( "%.2f",ssnsgaEshopN.PercentageOfCorrectness)+" & "+ String.format( "%.2f",mocellEshop.PercentageOfCorrectness)+" & "+ String.format( "%.2f",mocellEshopN.PercentageOfCorrectness)+" & "+String.format( "%.2f",fastEshop.PercentageOfCorrectness)+" & "+String.format( "%.2f",fastEshopN.PercentageOfCorrectness);
		 
		  
		  // Web runtime
		  Webruntime = " & " + ibeaWeb.TimeElapsed + " & "
					+ ibeaWebN.TimeElapsed + " & " + nsgaWeb.TimeElapsed
					+ " & " + nsgaWebN.TimeElapsed + " & "
					+ ssnsgaWeb.TimeElapsed+" & "+ssnsgaWebN.TimeElapsed+" & "+ mocellWeb.TimeElapsed+" & "+ mocellWebN.TimeElapsed+" & "+fastWeb.TimeElapsed+" & "+fastWebN.TimeElapsed;
         
         
         //Web hv
         Webhv= " & " + String.format( "%.2f",ibeaWeb.HV) + " & "
					+ String.format( "%.2f",ibeaWebN.HV) + " & " + String.format( "%.2f",nsgaWeb.HV)
					+ " & " + String.format( "%.2f",nsgaWebN.HV) + " & "
					+ String.format( "%.2f",ssnsgaWeb.HV)+" & "+String.format( "%.2f",ssnsgaWebN.HV)+" & "+ String.format( "%.2f",mocellWeb.HV)+" & "+ String.format( "%.2f",mocellWebN.HV)+" & "+String.format( "%.2f",fastWeb.HV)+" & "+String.format( "%.2f",fastWebN.HV);
		
		  
		  //Web spread
		  Webspread= " & " + String.format( "%.2f",ibeaWeb.Spread) + " & "
					+ String.format( "%.2f",ibeaWebN.Spread) + " & " + String.format( "%.2f",nsgaWeb.Spread)
					+ " & " + String.format( "%.2f",nsgaWebN.Spread) + " & "
					+ String.format( "%.2f",ssnsgaWeb.Spread)+" & "+String.format( "%.2f",ssnsgaWebN.Spread)+" & "+ String.format( "%.2f",mocellWeb.Spread)+" & "+ String.format( "%.2f",mocellWebN.Spread)+" & "+String.format( "%.2f",fastWeb.Spread)+" & "+String.format( "%.2f",fastWebN.Spread);
		
		  //Web correct
		  Webcorrect= " & " + String.format( "%.2f",ibeaWeb.PercentageOfCorrectness) + " & "
					+ String.format( "%.2f",ibeaWebN.PercentageOfCorrectness) + " & " + String.format( "%.2f",nsgaWeb.PercentageOfCorrectness)
					+ " & " + String.format( "%.2f",nsgaWebN.PercentageOfCorrectness) + " & "
					+ String.format( "%.2f",ssnsgaWeb.PercentageOfCorrectness)+" & "+String.format( "%.2f",ssnsgaWebN.PercentageOfCorrectness)+" & "+ String.format( "%.2f",mocellWeb.PercentageOfCorrectness)+" & "+ String.format( "%.2f",mocellWebN.PercentageOfCorrectness)+" & "+String.format( "%.2f",fastWeb.PercentageOfCorrectness)+" & "+String.format( "%.2f",fastWebN.PercentageOfCorrectness);
		
		  
		// Chat runtime
		  Chatruntime = " & " + ibeaChat.TimeElapsed + " & "
					+ ibeaChatN.TimeElapsed + " & " + nsgaChat.TimeElapsed
					+ " & " + nsgaChatN.TimeElapsed + " & "
					+ ssnsgaChat.TimeElapsed+" & "+ssnsgaChatN.TimeElapsed+" & "+ mocellChat.TimeElapsed+" & "+ mocellChatN.TimeElapsed+" & "+fastChat.TimeElapsed+" & "+fastChatN.TimeElapsed;
         
         
         //Chat hv
         Chathv= " & " + String.format( "%.2f",ibeaChat.HV) + " & "
					+ String.format( "%.2f",ibeaChatN.HV) + " & " + String.format( "%.2f",nsgaChat.HV)
					+ " & " + String.format( "%.2f",nsgaChatN.HV) + " & "
					+ String.format( "%.2f",ssnsgaChat.HV)+" & "+String.format( "%.2f",ssnsgaChatN.HV)+" & "+ String.format( "%.2f",mocellChat.HV)+" & "+ String.format( "%.2f",mocellChatN.HV)+" & "+String.format( "%.2f",fastChat.HV)+" & "+String.format( "%.2f",fastChatN.HV);
		  
		  
		  //Chat spread
		  Chatspread= " & " + String.format( "%.2f",ibeaChat.Spread) + " & "
					+ String.format( "%.2f",ibeaChatN.Spread) + " & " + String.format( "%.2f",nsgaChat.Spread)
					+ " & " + String.format( "%.2f",nsgaChatN.Spread) + " & "
					+ String.format( "%.2f",ssnsgaChat.Spread)+" & "+String.format( "%.2f",ssnsgaChatN.Spread)+" & "+ String.format( "%.2f",mocellChat.Spread)+" & "+ String.format( "%.2f",mocellChatN.Spread)+" & "+String.format( "%.2f",fastChat.Spread)+" & "+String.format( "%.2f",fastChatN.Spread);
		
		  //Chat correct
		  Chatcorrect= " & " + String.format( "%.2f",ibeaChat.PercentageOfCorrectness) + " & "
					+ String.format( "%.2f",ibeaChatN.PercentageOfCorrectness) + " & " + String.format( "%.2f",nsgaChat.PercentageOfCorrectness)
					+ " & " + String.format( "%.2f",nsgaChatN.PercentageOfCorrectness) + " & "
					+ String.format( "%.2f",ssnsgaChat.PercentageOfCorrectness)+" & "+String.format( "%.2f",ssnsgaChatN.PercentageOfCorrectness)+" & "+ String.format( "%.2f",mocellChat.PercentageOfCorrectness)+" & "+ String.format( "%.2f",mocellChatN.PercentageOfCorrectness)+" & "+String.format( "%.2f",fastChat.PercentageOfCorrectness)+" & "+String.format( "%.2f",fastChatN.PercentageOfCorrectness);
		  

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BufferedReader br=null;
		try {
			br = new BufferedReader(new FileReader("table.txt"));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			String tablestring = sb.toString();
			tablestring= tablestring.replaceFirst("==eshopruntime==", eshopruntime);
			tablestring= tablestring.replaceAll("==eshopHV==", eshophv);
			tablestring= tablestring.replaceAll("==eshopspread==", eshopspread);
			tablestring= tablestring.replaceAll("==eshopcorrect==", eshopcorrect);
			
			tablestring= tablestring.replaceAll("==webruntime==", Webruntime);
			tablestring= tablestring.replaceAll("==webHV==", Webhv);
			tablestring= tablestring.replaceAll("==webspread==", Webspread);
			tablestring= tablestring.replaceAll("==webcorrect==", Webcorrect);
			
			tablestring= tablestring.replaceAll("==chatruntime==", Chatruntime);
			tablestring= tablestring.replaceAll("==chatHV==", Chathv);
			tablestring= tablestring.replaceAll("==chatspread==", Chatspread);
			tablestring= tablestring.replaceAll("==chatcorrect==", Chatcorrect);
			
			System.out.println(tablestring);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public static EvaluationResult ReadResult(String filename){
		EvaluationResult result=new EvaluationResult();
		BufferedReader br=null;
		try {
			br = new BufferedReader(new FileReader(filename));
			double hv;
			double spread;
			double PercentageOfCorrectness;
			long TimeElapsed;
			String line = br.readLine();
            line = br.readLine();
            line=br.readLine();
            line=br.readLine();
			line=br.readLine();
			
			// hv
			String hvstring=line.trim();
			hv=Double.parseDouble(hvstring.substring(4, hvstring.length()-5));
			
			//spread
			line=br.readLine();
			String spreadstring=line.trim();
			spread=Double.parseDouble(spreadstring.substring(8, spreadstring.length()-9));
			
			//PercentageOfCorrectness
			line=br.readLine();
			String correctstring=line.trim();
			PercentageOfCorrectness=Double.parseDouble(correctstring.substring(25, correctstring.length()-26));
			
			//Time
			line=br.readLine();
			String timestring=line.trim();
			TimeElapsed=Long.parseLong(timestring.substring(13, timestring.length()-14));

			result.HV=hv;
			result.PercentageOfCorrectness=PercentageOfCorrectness;
			result.TimeElapsed=TimeElapsed;
			result.Spread=spread;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
}
