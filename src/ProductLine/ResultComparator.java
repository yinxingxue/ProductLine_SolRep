/**
 * 
 */
package ProductLine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yinxing
 *
 */
public class ResultComparator {
	
	private static final boolean DEBUG = true;
	public static String FILE_SEPARATOR = System.getProperty("file.separator");
	public static String RESULT_FILE = "ProductLine.log";
	
	private String path1;
	private String path2;
	
	private int iterNum;
	
	private List<Double> correctnessA;	
	private List<Double> timeA;
	private List<Double> hyperVolumnA;
	private List<Double> spreadA;

	private List<Double> correctnessB;
	private List<Double> timeB;
	private List<Double> hyperVolumnB;
	private List<Double> spreadB;
	
	public List<Double> getCorrectnessA() {
		return correctnessA;
	}

	public void setCorrectnessA(List<Double> correctnessA) {
		this.correctnessA = correctnessA;
	}

	public List<Double> getCorrectnessB() {
		return correctnessB;
	}

	public void setCorrectnessB(List<Double> correctnessB) {
		this.correctnessB = correctnessB;
	}

	public List<Double> getTimeA() {
		return timeA;
	}

	public void setTimeA(List<Double> time) {
		this.timeA = time;
	}

	public List<Double> getHyperVolumnA() {
		return hyperVolumnA;
	}

	public void setHyperVolumnA(List<Double> hyperVolumn) {
		this.hyperVolumnA = hyperVolumn;
	}

	public List<Double> getSpreadA() {
		return spreadA;
	}

	public void setSpreadA(List<Double> spread) {
		this.spreadA = spread;
	}

	public List<Double> getTimeB() {
		return timeB;
	}

	public void setTimeB(List<Double> timeB) {
		this.timeB = timeB;
	}

	public List<Double> getHyperVolumnB() {
		return hyperVolumnB;
	}

	public void setHyperVolumnB(List<Double> hyperVolumnB) {
		this.hyperVolumnB = hyperVolumnB;
	}

	public List<Double> getSpreadB() {
		return spreadB;
	}

	public void setSpreadB(List<Double> spreadB) {
		this.spreadB = spreadB;
	}

	private Set<String> A;
	private Set<String> B;
	private Set<String> uniqueA;
	private Set<String> uniqueB;
	private Set<String> intersect;
	
	private Map<String, Integer> AFrequencyMap;
	private Map<String, Integer> BFrequencyMap;
	
	private Set<String> correctInA;
	private Set<String> correctInB;
	private LinkedHashSet<String> intersectCorrect;
	private LinkedHashSet<String> uniqueCorrectA;
	private LinkedHashSet<String> uniqueCorrectB;
	
	private Set<String> nonDominant;
	private Set<String> nonDominantInA;
	private Set<String> nonDominantInB;
	private LinkedHashSet<String> uniqueNonDominantInA;
	private LinkedHashSet<String> uniqueNonDominantInB;
	
	public Set<String> getA() {
		return A;
	}

	public void setA(Set<String> a) {
		A = a;
	}

	public Set<String> getB() {
		return B;
	}

	public void setB(Set<String> b) {
		B = b;
	}

	public Set<String> getUniqueA() {
		return uniqueA;
	}

	public void setUniqueA(Set<String> uniqueA) {
		this.uniqueA = uniqueA;
	}

	public Set<String> getUniqueB() {
		return uniqueB;
	}

	public void setUniqueB(Set<String> uniqueB) {
		this.uniqueB = uniqueB;
	}

	public Set<String> getIntersect() {
		return intersect;
	}

	public void setIntersect(Set<String> intersect) {
		this.intersect = intersect;
	}

	public Map<String, Integer> getAFrequencyMap() {
		return AFrequencyMap;
	}

	public void setAFrequencyMap(Map<String, Integer> aFrequencyMap) {
		AFrequencyMap = aFrequencyMap;
	}

	public Map<String, Integer> getBFrequencyMap() {
		return BFrequencyMap;
	}

	public void setBFrequencyMap(Map<String, Integer> bFrequencyMap) {
		BFrequencyMap = bFrequencyMap;
	}

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

	public Set<String> getCorrectInA() {
		return correctInA;
	}

	public void setCorrectInA(Set<String> correctInA) {
		this.correctInA = correctInA;
	}

	public Set<String> getCorrectInB() {
		return correctInB;
	}

	public void setCorrectInB(Set<String> correctInB) {
		this.correctInB = correctInB;
	}
	
 

	public LinkedHashSet<String> getIntersectCorrect() {
		return intersectCorrect;
	}

	public void setIntersectCorrect(LinkedHashSet<String> intersectCorrect) {
		this.intersectCorrect = intersectCorrect;
	}

	public LinkedHashSet<String> getUniqueCorrectA() {
		return uniqueCorrectA;
	}

	public void setUniqueCorrectA(LinkedHashSet<String> uniqueCorrectA) {
		this.uniqueCorrectA = uniqueCorrectA;
	}

	public LinkedHashSet<String> getUniqueCorrectB() {
		return uniqueCorrectB;
	}

	public void setUniqueCorrectB(LinkedHashSet<String> uniqueCorrectB) {
		this.uniqueCorrectB = uniqueCorrectB;
	}

	public Set<String> getNonDominant() {
		return nonDominant;
	}

	public void setNonDominant(Set<String> nonDominant) {
		this.nonDominant = nonDominant;
	}

	public Set<String> getNonDominantInA() {
		return nonDominantInA;
	}

	public void setNonDominantInA(Set<String> nonDominantInA) {
		this.nonDominantInA = nonDominantInA;
	}

	public Set<String> getNonDominantInB() {
		return nonDominantInB;
	}

	public void setNonDominantInB(Set<String> nonDominantInB) {
		this.nonDominantInB = nonDominantInB;
	}

	public LinkedHashSet<String> getUniqueNonDominantInA() {
		return uniqueNonDominantInA;
	}

	public void setUniqueNonDominantInA(LinkedHashSet<String> uniqueNonDominantInA) {
		this.uniqueNonDominantInA = uniqueNonDominantInA;
	}

	public LinkedHashSet<String> getUniqueNonDominantInB() {
		return uniqueNonDominantInB;
	}

	public void setUniqueNonDominantInB(LinkedHashSet<String> uniqueNonDominantInB) {
		this.uniqueNonDominantInB = uniqueNonDominantInB;
	}

	public ResultComparator( String path1,  String path2){
		this.path1 = path1;
		this.path2 = path2;
		this.iterNum = checkIterNum();
		
		correctnessA = new ArrayList<Double>();
		correctnessB = new ArrayList<Double>();
		timeA= new ArrayList<Double>();
		timeB= new ArrayList<Double>();
		hyperVolumnA= new ArrayList<Double>();
		hyperVolumnB= new ArrayList<Double>();
		spreadA= new ArrayList<Double>();
		spreadB= new ArrayList<Double>();
		
		A = new LinkedHashSet<String>();
	    B = new LinkedHashSet<String>();
		uniqueA = new LinkedHashSet<String>();
		uniqueB = new LinkedHashSet<String>();
		intersect= new LinkedHashSet<String>();
		
	    AFrequencyMap = new LinkedHashMap<String, Integer>();
	    BFrequencyMap = new LinkedHashMap<String, Integer>();
		
		correctInA= new LinkedHashSet<String>();
	    correctInB= new LinkedHashSet<String>() ;
	}
	
	public ResultComparator(File subfolder1, File subfolder2) {
	  this(subfolder1.getAbsolutePath(), subfolder2.getAbsolutePath());
	}

	private int checkIterNum() {
		File folder1 = new File(path1);
		File folder2 = new File(path2);
		int length1 = folder1.list().length;
		int length2 = folder2.list().length;
		return Math.min(length1, length2);
	}
	
	public Integer[] checkFrequency() {
        Integer[] freq = new Integer[2];
        int counterA = 0;
        for(String key: AFrequencyMap.keySet())
        {
        	counterA += AFrequencyMap.get(key);
        }
        freq[0] = counterA;
        int counterB = 0;
        for(String key: BFrequencyMap.keySet())
        {
        	counterB += BFrequencyMap.get(key);
        }
        freq[1] = counterB;
		return freq;
	}

	public void compare()
	{
		for (int i = 1; i <= this.iterNum; i++) {
			String filePath1 = new File(this.path1).getPath() + FILE_SEPARATOR + i + FILE_SEPARATOR + RESULT_FILE;
			readResultA(filePath1, this.A, this.correctInA, this.AFrequencyMap, this.correctnessA, this.timeA,
					this.hyperVolumnA, this.spreadA);

			String filePath2 = new File(this.path2).getPath() + FILE_SEPARATOR + i + FILE_SEPARATOR + RESULT_FILE;
			readResultA(filePath2, this.B, this.correctInB, this.BFrequencyMap, this.correctnessB, this.timeB,
					this.hyperVolumnB, this.spreadB);
		}

		if(DEBUG)System.out.println( Arrays.asList(checkFrequency()));
		
		this.intersect = new LinkedHashSet<>(this.A); 
		this.intersect.retainAll(this.B);
		
		this.uniqueA = new LinkedHashSet<>(this.A); 
		this.uniqueA.removeAll(this.B);
		
		this.uniqueB = new LinkedHashSet<>(this.B); 
		this.uniqueB.removeAll(this.A);
		
		intersectCorrect = new LinkedHashSet<>(this.correctInA); 
		intersectCorrect.retainAll(this.correctInB);
		
		uniqueCorrectA = new LinkedHashSet<>(this.correctInA); 
		uniqueCorrectA.removeAll(this.correctInB);
		
		uniqueCorrectB = new LinkedHashSet<>(this.correctInB); 
		uniqueCorrectB.removeAll(this.correctInA);
		
		Set<String> union = new LinkedHashSet<>(this.correctInA); 
		union.addAll(this.correctInB);
		
		this.nonDominant = findDominantSol(union);
		this.nonDominantInA = new LinkedHashSet<>(nonDominant); 
		this.nonDominantInA.retainAll(this.correctInA);
		this.nonDominantInB = new LinkedHashSet<>(nonDominant); 
		this.nonDominantInB.retainAll(this.correctInB);
		
		uniqueNonDominantInA = new LinkedHashSet<>(this.nonDominantInA); 
		uniqueNonDominantInA.retainAll(this.uniqueA);
		uniqueNonDominantInB = new LinkedHashSet<>(this.nonDominantInB); 
		uniqueNonDominantInB.retainAll(this.uniqueB);
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
		if(DEBUG) System.out.println("The dominated sols are:"+dominatedSol);
		return nondominatedSol;
	}

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
	
	public StringBuilder printResult() {
		StringBuilder sb= new StringBuilder();
		
		sb.append(getMeanValue(this.correctnessA,this.iterNum)+",");
		sb.append(getMeanValue(this.correctnessB,this.iterNum)+",");
		
		sb.append(getMeanValue(this.timeA,this.iterNum)+",");
		sb.append(getMeanValue(this.timeB,this.iterNum)+",");
		
		sb.append(getMeanValue(this.hyperVolumnA,this.iterNum)+",");
		sb.append(getMeanValue(this.hyperVolumnB,this.iterNum)+",");
		
		sb.append(getMeanValue(this.spreadA,this.iterNum)+",");
		sb.append(getMeanValue(this.spreadB,this.iterNum)+",");
		
		sb.append(A.size()+",");
		sb.append(B.size()+",");
		sb.append(this.intersect.size()+",");
		sb.append(this.uniqueA.size()+",");
		sb.append(this.uniqueB.size()+",");
		
		sb.append(this.correctInA.size()+",");
		sb.append(this.correctInB.size()+",");
		sb.append(this.intersectCorrect.size()+",");
		sb.append(this.uniqueCorrectA.size()+",");
		sb.append(this.uniqueCorrectB.size()+",");
		
		sb.append(this.nonDominant.size()+",");
		sb.append(this.nonDominantInA.size()+",");
		sb.append(this.nonDominantInB.size()+",");
		sb.append(this.uniqueNonDominantInA.size()+",");
		sb.append(this.uniqueNonDominantInB.size()+",");
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
	

	/**
	 * @param args
	 */
	public static void main1(String[] args) {
		String path1 = args[0];
		String path2 = args[1];
		ResultComparator comparator = new ResultComparator(path1, path2);
		comparator.compare();
		comparator.printHead();
		comparator.printResult();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String path1 = args[0];
		String path2 = args[1];
		File folder1 = new File(path1);
		File folder2 = new File(path2);
		File[] subfolder2List = folder2.listFiles();
		
		StringBuilder sb = new StringBuilder();
		boolean printHead = false;
		for(File subfolder1: folder1.listFiles())
		{
			File subfolder2 = fcontainTheFoldName(subfolder1,subfolder2List);
			if(subfolder1.isDirectory() && subfolder2!=null && subfolder2.isDirectory()){
					ResultComparator comparator = new ResultComparator(subfolder1, subfolder2);
					comparator.compare();
				if (!printHead) {
					sb.append("system,");
					sb.append(comparator.printHead());
					sb.append("\n");
					printHead = true;
				}
				    sb.append(getRowName(subfolder1,subfolder2)+",");    
					sb.append(comparator.printResult());
					sb.append("\n");
			}
		}		
		
		try {
			FileWriter logger_ = new FileWriter("results/rstComp.csv",false);
			logger_.write(sb.toString());
			logger_.flush();
			logger_.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args, String resultFile) {
		String path1 = args[0];
		String path2 = args[1];
		File folder1 = new File(path1);
		File folder2 = new File(path2);
		File[] subfolder2List = folder2.listFiles();
		
		StringBuilder sb = new StringBuilder();
		boolean printHead = false;
		for(File subfolder1: folder1.listFiles())
		{
			File subfolder2 = fcontainTheFoldName(subfolder1,subfolder2List);
			if(subfolder1.isDirectory() && subfolder2!=null && subfolder2.isDirectory()){
					ResultComparator comparator = new ResultComparator(subfolder1, subfolder2);
					comparator.compare();
				if (!printHead) {
					sb.append("system,");
					sb.append(comparator.printHead());
					sb.append("\n");
					printHead = true;
				}
				    sb.append(getRowName(subfolder1,subfolder2)+",");    
					sb.append(comparator.printResult());
					sb.append("\n");
			}
		}		
		
		try {
			FileWriter logger_ = new FileWriter("results"+FILE_SEPARATOR+resultFile,false);
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

	public static Double getMeanValue(List<Double> list, int num) {
		Double mean = 0.0;
 
		for(Double value : list)
		{
			mean+=value;
		}
		return mean/num;
	}
}
