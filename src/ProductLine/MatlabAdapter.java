package ProductLine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MatlabAdapter {

	public static GAParams.CaseStudy study = GAParams.CaseStudy.EShop;
	public static boolean append = false;
	
	/**
	 * @param args
	 */	public static void main(String[] args) {
		 
		study = GAParams.CaseStudy.valueOf(args[0]);
		fileList(new File(args[1]));
	}
	
	 //WebPortal
	 //ChatSystem
	 //EShop
	 public static void main(String system, String path) {
			// TODO Auto-generated method stub
			study = GAParams.CaseStudy.valueOf(system);
			fileList(new File(path));
    }
	 
	public static void main(GAParams.CaseStudy case1, String path) {
		// TODO Auto-generated method stub
		study = case1;
		fileList(new File(path));
	}

	public static void fileList(File file) {
		File[] files = file.listFiles();
		if (files != null) {
			for (File f : files) {
				if (f.isDirectory())
					fileList(f);
				else if (f.isFile() && f.getName().equals("ProductLine.log")) {
					convertToText(f);
				}
			}
		}
	}

	private static void convertToText(File f) {
		BufferedReader reader = null;
		BufferedWriter output = null;
		// output.write(s1);
		try {
			// System.out.println("ä»¥è¡Œä¸ºå�•ä½�è¯»å�–æ–‡ä»¶å†…å®¹ï¼Œä¸€æ¬¡è¯»ä¸€æ•´è¡Œï¼š");
			reader = new BufferedReader(new FileReader(f));
			String result = f.getParentFile()
					.getAbsolutePath() +System.getProperties().getProperty(
							"file.separator")+"usol.txt";
			clear(result);
			output = new BufferedWriter(new FileWriter(result,append));
			String tempString = null;
			int line = 0;
			// ä¸€æ¬¡è¯»å…¥ä¸€è¡Œï¼Œç›´åˆ°è¯»å…¥nullä¸ºæ–‡ä»¶ç»“æ�Ÿ
			while ((tempString = reader.readLine()) != null
					&& !tempString.startsWith("%Correct=")) {
				// æ˜¾ç¤ºè¡Œå�·
				if (!tempString.startsWith("1) ") && line <= 0)
					continue;
				line++;
				System.out.println("line " + line + ": " + tempString);
				// Cost Richness NotUsed Defects correctness
				String newResultFormat = castFormat(tempString);
				output.write(newResultFormat);
				output.write(System.getProperties().getProperty(
						"line.separator"));// æ�¢è¡Œç¬¦
			}
			reader.close();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
			if (output != null) {
				try {
					output.close();
				} catch (IOException e1) {
				}
			}
		}
	}

	@SuppressWarnings("unused")
	private static void convertLogToText(File f) {
		BufferedReader reader = null;
		BufferedWriter output = null;
		// output.write(s1);
		try {
			// System.out.println("ä»¥è¡Œä¸ºå�•ä½�è¯»å�–æ–‡ä»¶å†…å®¹ï¼Œä¸€æ¬¡è¯»ä¸€æ•´è¡Œï¼š");
			reader = new BufferedReader(new FileReader(f));
			String result = f.getParentFile()
					.getAbsolutePath() +System.getProperties().getProperty(
							"file.separator")+"usol.txt";
			clear(result);
			output = new BufferedWriter(new FileWriter(result,append));
			String tempString = null;
			int line = 0;
			// ä¸€æ¬¡è¯»å…¥ä¸€è¡Œï¼Œç›´åˆ°è¯»å…¥nullä¸ºæ–‡ä»¶ç»“æ�Ÿ
			while ((tempString = reader.readLine()) != null
					&& !tempString.startsWith("%Correct=")) {
				// æ˜¾ç¤ºè¡Œå�·
				if (!tempString.startsWith("INFO: 1) ") && line <= 0)
					continue;
				line++;
				System.out.println("line " + line + ": " + tempString);
				// Cost Richness NotUsed Defects correctness
				String newResultFormat = castFormat(tempString);
				output.write(newResultFormat);
				output.write(System.getProperties().getProperty(
						"line.separator"));// æ�¢è¡Œç¬¦
			}
			reader.close();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
			if (output != null) {
				try {
					output.close();
				} catch (IOException e1) {
				}
			}
		}
	}
	
	private static void clear(String result) {
		 File rlt = new File(result);
		 if(rlt.exists() && rlt.isFile()) 
		 {
			 rlt.delete();
		 }
	}

	private static String castFormat(String tempString) {
		String temp = tempString.substring(tempString.indexOf(")") + 1);
		temp = temp.substring(0, temp.length() - 1);
		// Correctness=0.0, MissingFeature=2.0, NotUsedBefore=2.0, Defects=35.0,
		// Cost=35.0
		String[] parts = temp.split(",");
		Double correctness = Double.parseDouble(parts[0].substring(parts[0]
				.indexOf("=") + 1));
		Double missingFeature = Double.parseDouble(parts[1].substring(parts[1]
				.indexOf("=") + 1));
		int totalNumFea = 0;
		if(study == GAParams.CaseStudy.ChatSystem)
		{
			totalNumFea = 12;
		}
		else if(study == GAParams.CaseStudy.WebPortal)
		{
			totalNumFea = 43;
		}
		else if(study == GAParams.CaseStudy.EShop)
		{
			totalNumFea = 290;
		}
		missingFeature = (totalNumFea - missingFeature) * -1;
		Double notUsedBefore = Double.parseDouble(parts[2].substring(parts[2]
				.indexOf("=") + 1));
		Double defects = Double.parseDouble(parts[3].substring(parts[3]
				.indexOf("=") + 1));
		Double cost = Double.parseDouble(parts[4].substring(parts[4]
				.indexOf("=") + 1));

		// Cost Richness NotUsed Defects correctness
		String newResultFormat = cost.toString() + " "
				+ missingFeature.toString() + " " + notUsedBefore.toString()
				+ " " + defects.toString() + " " + correctness.toString();
		return newResultFormat;
	}
}
