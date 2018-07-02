package ProductLine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jmetal.core.Variable;

public class Utility {
	
	public static String LINE_SEPERATOR= System.getProperty("line.separator");
	
	public static HashMap<String, Boolean> readSeed(String filePath) throws Exception{
		HashMap<String, Boolean> seed=new LinkedHashMap<String,Boolean>();		
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line;		
		
		while ((line = br.readLine()) != null) {
			
				String[] cols=line.split(" -> ");
				seed.put(cols[0], Boolean.valueOf(cols[1]));
			
		}
		br.close();
		
//		for(String s:res) {
//			System.out.println(s);
//		}
		return seed;
	}
	
	public static ArrayList<String> readFixFeatures(String filePath) throws Exception{
		ArrayList<String> res=new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line;		
		Pattern pattern = Pattern.compile("\\d+ \\s+ \\S+");
		while ((line = br.readLine()) != null) {
			Matcher matcher = pattern.matcher(line);
			if (matcher.matches()) {
				String[] cols=line.split(" ");
				res.add(cols[cols.length-1]);
			}
		}
		br.close();
		
//		for(String s:res) {
//			System.out.println(s);
//		}
		
		//if(res.size()==0) throw new Exception();
		
		return res;
	}
	
//	public static void main(String[] args) throws Exception {
//		readFixFeatures("data/No_ECos.txt");
//		readSeed("data/Seed_LinuxX86.txt");
//	}
	
	public static void clearAndCreate(String path)
	{
		File des = new File(path);
		if(!des.exists()|| des.isFile()) des.mkdirs();
		else{
			for(File sub: des.listFiles())
			{
				deleteFile(sub);
			}
		}
	}
	
	private static void deleteFile(File file){ 
		   if(file.exists()){ 
		    if(file.isFile()){ 
		     file.delete(); 
		    }else if(file.isDirectory()){ 
		     File files[] = file.listFiles(); 
		     for(int i=0;i<files.length;i++){ 
		       deleteFile(files[i]); 
		     } 
		    } 
		    file.delete(); 
		   }else{ 
		    System.out.println("鎵�鍒犻櫎鐨勬枃浠朵笉瀛樺湪锛�"+'\n'); 
		   } 
		} 
	
	
	public static void generateSeedFile(String inputSeedFile,String sampleSeedFile,String outputSeedFile)
	{
		File seed = new File(inputSeedFile);
		if(!seed.exists()|| !seed.isFile()) return;
		Set<String> selectedVariantFeatures= new LinkedHashSet<String>();
		Set<String> mustInFeatures= new LinkedHashSet<String>();
		Set<String> mustNotFeatures= new LinkedHashSet<String>();
		getSelectedVariantFeatures(seed,selectedVariantFeatures);
		getSelectedVariantFeatures(new File("data/inputLinuxMustInText"),mustInFeatures);
		getSelectedVariantFeatures(new File("data/inputLinuxMustNotInText"),mustNotFeatures);
		Map<String,Boolean> sampleSeedFeatures= new LinkedHashMap<String,Boolean>();
		getSampleSeedFileFormat(new File(sampleSeedFile),sampleSeedFeatures);
		generateOutput(sampleSeedFeatures,mustInFeatures, mustNotFeatures,selectedVariantFeatures,outputSeedFile);
	}

	private static void generateOutput(Map<String, Boolean>  sampleSeedFeatures,
			Set<String> mustInFeatures, Set<String> mustNotFeatures, Set<String> selectedVariantFeatures, String outputSeedFile) {
		try {
			File writename = new File(outputSeedFile); // 相对路径，如果没有则要建立一个新的output。txt文件
			writename.deleteOnExit();
			if(!writename.exists()) writename.createNewFile(); // 创建新文件
			BufferedWriter out = new BufferedWriter(new FileWriter(writename));
			// out.write("我会写入文件啦\r\n"); // \r\n即为换行
			for (String feature : sampleSeedFeatures.keySet()) {
				if (selectedVariantFeatures.contains(feature)) {
					out.write(feature + " -> " + Boolean.TRUE + LINE_SEPERATOR);
				} else if (mustInFeatures.contains(feature)){
					out.write(feature + " -> " + Boolean.TRUE + LINE_SEPERATOR);
				} else if (mustNotFeatures.contains(feature)){
					out.write(feature + " -> " + Boolean.FALSE + LINE_SEPERATOR);
				}else {
					out.write(feature + " -> " + Boolean.FALSE + LINE_SEPERATOR);
				}
			}
			out.flush(); // 把缓存区内容压入文件
			out.close(); // 最后记得关闭文件
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void getSampleSeedFileFormat(File sampleSeedFile, Map<String, Boolean> sampleSeedFeatures) {
		try {
			 
            InputStreamReader reader = new InputStreamReader(  
                    new FileInputStream(sampleSeedFile)); // 建立一个输入流对象reader  
            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言  
            String line = "";  
            while ((line =br.readLine()) != null) {              
                String[] featureMarks = line.split("->");
                featureMarks[0]= featureMarks[0].trim();
                sampleSeedFeatures.put(featureMarks[0], Boolean.TRUE);
            }  
            // 把缓存区内容压入文件  
            br.close(); // 最后记得关闭文件  
  
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
	}

	private static void getSelectedVariantFeatures(File seed, Set<String> selectedVariantFeatures) {
		try {
			 
	            InputStreamReader reader = new InputStreamReader(  
	                    new FileInputStream(seed)); // 建立一个输入流对象reader  
	            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言  
	            String line = "";  
	            while ((line =br.readLine()) != null) {  
	                String[] selFeatures = line.split(",");
	                selectedVariantFeatures.addAll(Arrays.asList(selFeatures));
	            }  
	            // 把缓存区内容压入文件  
	            br.close(); // 最后记得关闭文件  
	  
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getVariablesString(Variable[] varibles)
	{
		String result= "[";
		for(int i= 0; i< varibles.length; i++)
		{
			result+= varibles[i];
			if(i!= varibles.length-1) result+=", ";
			else  result+="]";
		}
		return result;
	}
	
	public static void main(String[] args) {
		generateSeedFile(args[0],args[1],args[2]);
	}
//	public static void UpdateConstraintUsageCnt() {
//		
//		
//		int size=GAParams.constraints.size();
//		if(GAParams.constraintCurrentCnt==size) {
//			return;
//		}
//		GAParams.constraintCurrentCnt+=GAParams.P_ConstraintIncrementSize;		
//		if(GAParams.constraintCurrentCnt>=size) {
//			GAParams.constraintCurrentCnt=size;
//		}
//		
//		GAParams.GeneToViolatedFeatureMap.clear();		
//		GAParams.visitedSeed.clear();
//	}

}
