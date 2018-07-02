/**
 * 
 */
package cplex.tsl.ntu.sg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cplex.tsl.ntu.sg.CplexResultComparator.MyFilter;

/**
 * @author XueYX
 *
 */
public class CplexMatlabResultComparator {

	private static final boolean DEBUG = true;
	private static final boolean ONLY_COMP_UNION = true;
	public static String FILE_SEPARATOR = System.getProperty("file.separator");
	public static String RESULT_FILE = "ProductLine.log";
	
	private String path1;
	private String path2;
	
	private int iterNum;
	private ResultCompFormat unionCompResult;
	//private ResultCompFormat median;
	private List<ResultCompFormat> compResultList;
	
	public String getPath1() {
		return path1;
	}

	public void setPath1(String path1) {
		this.path1 = path1;
	}

	public String getPath2() {
		return path2;
	}

	public void setPath2(String path2) {
		this.path2 = path2;
	}

	public int getIterNum() {
		return iterNum;
	}

	public void setIterNum(int iterNum) {
		this.iterNum = iterNum;
	}

	public  List<ResultCompFormat> getCompResultList() {
		return compResultList;
	}

	public void setCompResultList( List<ResultCompFormat> compResultList) {
		this.compResultList = compResultList;
	}

	public CplexMatlabResultComparator(String path1,  String path2){
		this.path1 = path1;
		this.path2 = path2;
		this.compResultList = new ArrayList<ResultCompFormat>();
		//this.iterNum = checkIterNum();
	}
	
	public CplexMatlabResultComparator(File subfolder1, File subfolder2) {
	  this(subfolder1.getAbsolutePath(), subfolder2.getAbsolutePath());
	}
 
	public void comparewith(File file1, File file2)
	{
		String filePath1 = file1.getAbsolutePath();
		unionCompResult = new ResultCompFormat(this.path1,this.path2);
	    readResultA(filePath1, unionCompResult.A, unionCompResult.correctInA, unionCompResult.AFrequencyMap, unionCompResult.correctnessA,
	    		unionCompResult.timeA, unionCompResult.hyperVolumnA, unionCompResult.spreadA);
	    
//		for (String name : toBeCompared) {
//			File file2 = new File(name);
//			ResultCompFormat resultComp = new ResultCompFormat(file1.getAbsolutePath(), file2.getAbsolutePath());
//			resultComp.setAbyObject(unionCompResult);
//			if (file2 != null && file2.isFile()) {
//				String filePath2 = file2.getAbsolutePath();
//				readResultA(filePath2, resultComp.B, resultComp.correctInB, resultComp.BFrequencyMap,
//						resultComp.correctnessB, resultComp.timeB, resultComp.hyperVolumnB, resultComp.spreadB);
//				unionCompResult.addBbyObject(resultComp);
//				if (ONLY_COMP_UNION) {
//					resultComp.calculateCompResult();
//					System.out.println("NonDonitated in B:" + resultComp.uniqueNonDominantInB);
//					this.compResultList.add(resultComp);
//				}
//			}
//		}
	    String filePath2 = file2.getAbsolutePath();
       	ResultCompFormat resultComp = new ResultCompFormat(file1.getAbsolutePath(),filePath2);
       	resultComp.setAbyObject(unionCompResult);
       	readResultCVS(filePath2, resultComp.B, resultComp.correctInB, resultComp.BFrequencyMap, resultComp.correctnessB, resultComp.timeB, resultComp.hyperVolumnB, resultComp.spreadB);
       	unionCompResult.addBbyObject(resultComp);
       	if (ONLY_COMP_UNION) {
       		resultComp.calculateCompResult();
 			System.out.println("NonDonitated in B:" + resultComp.uniqueNonDominantInB);
			this.compResultList.add(resultComp);
		}
       		
		Collections.sort(this.compResultList);
		//int medianIdx = (this.compResultList.size()-1) / 2;
		//median = (ResultCompFormat) this.compResultList.get(medianIdx);
		unionCompResult.calculateCompResult();
		System.out.println("NonDonitated in B:"+unionCompResult.uniqueNonDominantInB);
	}

	private void readResultCVS(String filePath2, Set<String> b,
			Set<String> correctInB, Map<String, Integer> bFrequencyMap,
			List<Double> correctnessB, List<Double> timeB,
			List<Double> hyperVolumnB, List<Double> spreadB) {
		// TODO Auto-generated method stub
		String line = "";
		try {
			BufferedReader in = new BufferedReader(new FileReader(filePath2));
			line = in.readLine();
            int varLength= line.split(",").length -4;
			do{
				//the actual format 38.53,-4,3,15,0...0
				// the wanted  : Correctness=0.0, MissingFeature=39.0, NotUsedBefore=3.0, Defects=15.0, Cost=38.53, 
				String[] objs =  line.split(",");
				double cost = Double.parseDouble(objs[0]);
				cost = formatDouble2(cost);
				double missFeas = formatDouble2(Integer.parseInt(objs[1]) + varLength);
				double notUsedBefore =  formatDouble2(Integer.parseInt(objs[2])) ;
				double defects =  formatDouble2(Integer.parseInt(objs[3])) ;
				String key = "0.0"+"_"+missFeas+"_"+notUsedBefore+"_"+defects+"_"+cost+"_";
				b.add(key);
				correctInB.add(key);
			    addToFrequencyMap(key,bFrequencyMap);
			    line = in.readLine();
			}while (line != null);
			in.close();
		}
		catch(Exception e)
		{
			e.printStackTrace(); 
			System.exit(0);
		}
	}

	public static Set<String> findDominantSol(final Set<String> union) {
		Set<String> nondominatedSol = new LinkedHashSet<>();
		Set<String> dominatedSol = new LinkedHashSet<>(); 
		for(String sol1: union)
		{
			boolean canFindDominant = false;
			@SuppressWarnings("unused")
			int counter=0;
			for(String sol2: union)
			{
				if(sol1==sol2){
					//System.out.println("sol1:"+sol1+ " sol2:" + sol2 );
					continue;
					}
				counter++;
				//check whether exists sol2 dominates sol1
				String[] solution1= sol1.split("_");
				String[] solution2= sol2.split("_");
				
				boolean ifSol2Dominant = true;
				for(int i=0; i< solution1.length;i++)
				{
					if(Double.parseDouble(solution2[i]) > Double.parseDouble(solution1[i]))
					{
						ifSol2Dominant =false;
						break;
					}
				}
				
				if(ifSol2Dominant) 
				{
					//domiSol.add(sol1);
					canFindDominant= true;
					break;
				}
			}
			if(!canFindDominant)
			{
				nondominatedSol.add(sol1);
			}
			else
			{
				dominatedSol.add(sol1);
			}
			
		}
		if(DEBUG) System.out.println("Here is dominted sols in union:"+dominatedSol);
		return nondominatedSol;
	}

	/**
	 * 
	 * @param path
	 * @param set
	 * @param correctInSet
	 * @param frequencyMap
	 * @param correctness
	 * @param time
	 * @param hyperVolumn
	 * @param spread
	 */
	private void readResultA(String path, Set<String> set, Set<String> correctInSet, Map<String, Integer> frequencyMap,  List<Double> correctness, List<Double> time, List<Double> hyperVolumn, List<Double> spread) {
		String line = "";
		try {
			BufferedReader in = new BufferedReader(new FileReader(path));
			line = in.readLine();
			boolean ifRead=false;
			do{
				if(line.startsWith("1) Correctness="))
				{
					ifRead = true;
				}	
			    else if(ifRead && line.startsWith("%Correct="))
			    {
			    	//%Correct=100.0 			%Time=2.564   			%Hypervolume=0.16544690402846426 			    			%Spread=1.6734693877551012    			%GD=0.0 		    			%IGD=0.0	    			%Epsilon=0.0
			    	ifRead = false;
			    	String correct = line.substring(line.indexOf("=")+1);
			    	correctness.add(Double.parseDouble(correct));
			    	
			    	String exetime = in.readLine();
			    	exetime = exetime.substring(exetime.indexOf("=")+1);
			    	time.add(Double.parseDouble(exetime));
			    	
			    	String hyper = in.readLine();
			    	hyper = hyper.substring(hyper.indexOf("=")+1);
			    	hyperVolumn.add(Double.parseDouble(hyper));
			    	
			    	String spr = in.readLine();
			    	spr = spr.substring(spr.indexOf("=")+1);
			    	spread.add(Double.parseDouble(spr));   	
			    }
				if(ifRead && !line.trim().equals("") &&  findSolutionResult(line))
				{
					//37) Correctness=0.0, MissingFeature=6.0, NotUsedBefore=0.0, Defects=25.0, Cost=17.0, 
				   String[] attributes = line.split(",");
				   String value="";
				   boolean isCorrect = false;
				   for(int i=0; i<= 4; i++)
				   {
					  String attr =  attributes[i];
					  double attrValue = Double.parseDouble(attr.substring(attr.indexOf("=")+"=".length()));
					  attrValue = formatDouble2(attrValue);
					  value+=attrValue+"_";		
					  if(i==0 && attrValue == 0)  isCorrect =true;
				   }
				   set.add(value);
				   if(isCorrect)  correctInSet.add(value);
				   
				   addToFrequencyMap(value,frequencyMap);
				}
				line = in.readLine();		
			}while (line != null);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	 /**
     * The BigDecimal class provides operations for arithmetic, scale manipulation, rounding, comparison, hashing, and format conversion.
     * @param d
     * @return
     */
    public static double formatDouble2(double d) {
        // 旧方法，已经不再推荐使用
//        BigDecimal bg = new BigDecimal(d).setScale(2, BigDecimal.ROUND_HALF_UP);    
        // 新方法，如果不需要四舍五入，可以使用RoundingMode.DOWN
        BigDecimal bg = new BigDecimal(d).setScale(2, RoundingMode.HALF_UP);
        return bg.doubleValue();
    }


	/**
	 * @param line
	 * @return
	 */
	public boolean findSolutionResult(String line) {
		int pos = line.indexOf(")");
		if(pos<=0) return false;
		String seq =  line.substring(0, pos);
		Pattern pattern = Pattern.compile("^\\d+$");
		Matcher matcher = pattern.matcher(seq);
		boolean isResult = matcher.find();
		return isResult;
	}

	private void addToFrequencyMap(String value,
			Map<String, Integer> frequencyMap) {
		if(!frequencyMap.containsKey(value))
		{
			frequencyMap.put(value, 1);
		}
		else
		{
			frequencyMap.put(value, frequencyMap.get(value)+1);
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String path1 = args[0];
		String path2 = args[1];
		File folder1 = new File(path1);
		File folder2 = new File(path2);
		CplexMatlabResultComparator comparator = new CplexMatlabResultComparator(path1,path2);
		
		StringBuilder sb = new StringBuilder();
		boolean printHead = false;
		File[] files = folder1.listFiles();
		File file1 = files[0];
		file1 = file1.listFiles()[0];
		MyFilter filter = new MyFilter(RESULT_FILE);
		file1 = file1.listFiles(filter)[0];

		Path startingDir = Paths.get(folder2.getAbsolutePath());
//		FindFileVisitor findJavaVisitor = new FindFileVisitor(RESULT_FILE);
//
//		try {
//			Files.walkFileTree(startingDir, findJavaVisitor);
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		List<String> toBeCompared = findJavaVisitor.getFilenameList();
		comparator.setIterNum(1);
		comparator.comparewith(file1, folder2);
	 	if (!printHead) {
				sb.append("system,");
				sb.append(comparator.printHead());
				sb.append("\n");
				printHead = true;
		}
		//sb.append(getRowName(file1, file2) + ",");
		//sb.append(comparator.printResult(comparator.median));
		//sb.append("\n");
		sb.append(comparator.printResult(comparator.unionCompResult));
		sb.append("\n");
		try {
			FileWriter logger_ = new FileWriter("results/rstComp_matlab.csv", false);
			logger_.write(sb.toString());
			logger_.flush();
			logger_.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
 

	public static String getRowName(File subfolder1, File subfolder2) {
		String path1= subfolder1.getAbsolutePath();
		String path2= subfolder2.getAbsolutePath();
		String rowName ="";
		rowName += getPathInfo(path1).replace(FILE_SEPARATOR, "_");
		rowName+="_VS_";
		rowName += getPathInfo(path2).replace(FILE_SEPARATOR, "_");
		rowName+= "_"+subfolder1.getName();
		return rowName;
	}

	/**
	 * @param path1
	 */
	private static String getPathInfo(String path1) {
		String rowName ="";
		if(path1.contains("jchat"+FILE_SEPARATOR))
		{
			int beginIndex = path1.indexOf("jchat"+FILE_SEPARATOR);
			int endIndex=  path1.indexOf(FILE_SEPARATOR+"para4"+FILE_SEPARATOR);
			rowName += path1.substring(beginIndex, endIndex);
		}
		else if(path1.contains("webportal"+FILE_SEPARATOR))
		{
			int beginIndex = path1.indexOf("webportal"+FILE_SEPARATOR);
			int endIndex=  path1.indexOf(FILE_SEPARATOR+"para4"+FILE_SEPARATOR);
			rowName += path1.substring(beginIndex, endIndex);
		}
		else if(path1.contains("eshop"+FILE_SEPARATOR))
		{
			int beginIndex = path1.indexOf("eshop"+FILE_SEPARATOR);
			int endIndex=  path1.indexOf(FILE_SEPARATOR+"para4"+FILE_SEPARATOR);
			rowName += path1.substring(beginIndex, endIndex);
		}
		else if(path1.contains("ecos"+FILE_SEPARATOR))
		{
			int beginIndex = path1.indexOf("ecos"+FILE_SEPARATOR);
			int endIndex=  path1.indexOf(FILE_SEPARATOR+"para4"+FILE_SEPARATOR);
			rowName += path1.substring(beginIndex, endIndex);
		}
		else if(path1.contains("uclinux"+FILE_SEPARATOR))
		{
			int beginIndex = path1.indexOf("uclinux"+FILE_SEPARATOR);
			int endIndex=  path1.indexOf(FILE_SEPARATOR+"para4"+FILE_SEPARATOR);
			rowName += path1.substring(beginIndex, endIndex);
		}
		else if(path1.contains("linuxx86"+FILE_SEPARATOR))
		{
			int beginIndex = path1.indexOf("linuxx86"+FILE_SEPARATOR);
			int endIndex=  path1.indexOf(FILE_SEPARATOR+"para4"+FILE_SEPARATOR);
			rowName += path1.substring(beginIndex, endIndex);
		}
		return rowName;
	}

	public static File fcontainTheFoldName(File subfolder1, File[] subfolder2List) {
		String name = subfolder1.getName();
		for(File subfolder2: subfolder2List)
		{ 
			if(subfolder2.getName().equals(name))
			{
				return subfolder2;
			}
		}
		return null;
	}

	public static Double getMeanValue(List<Double> list) {
		Double mean = 0.0;
        int num = list.size();
		for(Double value : list)
		{
			mean+=value;
		}
		return mean/num;
	}
	
	public StringBuilder printResult(ResultCompFormat resultComp) {
		StringBuilder sb= new StringBuilder();
		sb.append(resultComp.getPath2()+",");
		sb.append(getMeanValue(resultComp.correctnessA)+",");
		sb.append(getMeanValue(resultComp.correctnessB)+",");
		
		sb.append(getMeanValue(resultComp.timeA)+",");
		sb.append(getMeanValue(resultComp.timeB)+",");
		
		sb.append(getMeanValue(resultComp.hyperVolumnA)+",");
		sb.append(getMeanValue(resultComp.hyperVolumnB)+",");
		
		sb.append(getMeanValue(resultComp.spreadA)+",");
		sb.append(getMeanValue(resultComp.spreadB)+",");
		
		sb.append(resultComp.A.size()+",");
		sb.append(resultComp.B.size()+",");
		sb.append(resultComp.intersect.size()+",");
		sb.append(resultComp.uniqueA.size()+",");
		sb.append(resultComp.uniqueB.size()+",");
		
		sb.append(resultComp.correctInA.size()+",");
		sb.append(resultComp.correctInB.size()+",");
		sb.append(resultComp.intersectCorrect.size()+",");
		sb.append(resultComp.uniqueCorrectA.size()+",");
		sb.append(resultComp.uniqueCorrectB.size()+",");
		
		sb.append(resultComp.nonDominant.size()+",");
		sb.append(resultComp.nonDominantInA.size()+",");
		sb.append(resultComp.nonDominantInB.size()+",");
		sb.append(resultComp.uniqueNonDominantInA.size()+",");
		sb.append(resultComp.uniqueNonDominantInB.size()+",");
		return sb;
	}
	
	public StringBuilder printHead()
	{
		StringBuilder sb= new StringBuilder();
		
		sb.append("m_correctnessA"+",");
		sb.append("m_correctnessB"+",");
		
		
		sb.append("m_timeA"+",");
		sb.append("m_timeB"+",");
		
		sb.append("m_hyperVolumnA"+",");
		sb.append("m_hyperVolumnB"+",");
		
		sb.append("m_spreadA"+",");
		sb.append("m_spreadB"+",");
		
		sb.append("A"+",");
		sb.append("B"+",");
		sb.append("intersect"+",");
		sb.append("uniqueA"+",");
		sb.append("uniqueB"+",");
		
		sb.append("correctInA"+",");
		sb.append("correctInB"+",");
		sb.append("intersectCorrect"+",");
		sb.append("uniqueCorrectA"+",");
		sb.append("uniqueCorrectB"+",");
		
		sb.append("nonDominant"+",");
		sb.append("nonDominantInA"+",");
		sb.append("nonDominantInB"+",");
		sb.append("uniqueNonDominantInA"+",");
		sb.append("uniqueNonDominantInB"+",");
		return sb;		
	}

	static class MyFilter implements FilenameFilter {
		
		private String type;
		
		public MyFilter (String type) {
			this.type = type;
		}

		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(type);// ��Type��β
		}
		
	}

}
