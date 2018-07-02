/**
 * 
 */
package cplex.tsl.ntu.sg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ProductLine.ResultComparator;

/**
 * @author yinxing
 *
 */
public class ResultCompFormat implements Comparable<ResultCompFormat>{

	private String path1;
	private String path2;
	private static final boolean DEBUG = true;
	private int iter;
	
	/**
	 * @return the path1
	 */
	public String getPath1() {
		return path1;
	}

	/**
	 * @param path1 the path1 to set
	 */
	public void setPath1(String path1) {
		this.path1 = path1;
	}

	/**
	 * @return the path2
	 */
	public String getPath2() {
		return path2;
	}

	/**
	 * @param path2 the path2 to set
	 */
	public void setPath2(String path2) {
		this.path2 = path2;
	}

	public ResultCompFormat(String path1, String path2) {
		this.path1 = path1;
		this.path2 = path2;
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
	    iter = 1;
	}

	List<Double> correctnessA;	
	List<Double> timeA;
	List<Double> hyperVolumnA;
	List<Double> spreadA;

	List<Double> correctnessB;
	List<Double> timeB;
	List<Double> hyperVolumnB;
	List<Double> spreadB;
	
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
	
	 Set<String> A;
	 Set<String> B;
	 Set<String> uniqueA;
	 Set<String> uniqueB;
	 Set<String> intersect;
	
	 Map<String, Integer> AFrequencyMap;
	 Map<String, Integer> BFrequencyMap;
	
	 Set<String> correctInA;
	 Set<String> correctInB;
	 LinkedHashSet<String> intersectCorrect;
	 LinkedHashSet<String> uniqueCorrectA;
	 LinkedHashSet<String> uniqueCorrectB;
	
	 Set<String> nonDominant;
	 Set<String> nonDominantInA;
	 Set<String> nonDominantInB;
	 LinkedHashSet<String> uniqueNonDominantInA;
	 LinkedHashSet<String> uniqueNonDominantInB;
	
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
 
	public int compare(ResultCompFormat arg0, ResultCompFormat arg1) {
		Integer inter1 = ((ResultCompFormat) arg0).intersect.size();
	    Integer inter2 = ((ResultCompFormat) arg1).intersect.size();
	       
	   	Integer unique1 = ((ResultCompFormat) arg0).uniqueNonDominantInB.size();
	    Integer unique2 = ((ResultCompFormat) arg1).uniqueNonDominantInB.size();
	       
	    int score1 =inter1+ 5* unique1;
	    int score2 =inter2+ 5* unique2;
	       if (score1 > score2) {
	           return 1;
	       } else if (score1 < score2){
	           return -1;
	       } else {
	           return 0;
	       }
	}

	public void setAbyObject(ResultCompFormat unionCompResult) {
		this.A = unionCompResult.A;
		this.correctInA = unionCompResult.correctInA;
		this.AFrequencyMap = unionCompResult.AFrequencyMap;
		this.correctnessA = unionCompResult.correctnessA;
		this.timeA = unionCompResult.timeA;
		this.hyperVolumnA = unionCompResult.hyperVolumnA;
		this.spreadA = unionCompResult.spreadA;
	}

	public void addBbyObject(ResultCompFormat resultComp) {
		// TODO Auto-generated method stub
		this.iter++;
		this.B.addAll(resultComp.B);
		this.correctInB.addAll(resultComp.correctInB);
		//this.BFrequencyMap.putAll(resultComp.BFrequencyMap);
		for(String bKey: resultComp.BFrequencyMap.keySet())
		{
			if(!this.BFrequencyMap.keySet().contains(bKey))
			{
				this.BFrequencyMap.put(bKey, resultComp.BFrequencyMap.get(bKey));
			}
			else
			{
				int times = resultComp.BFrequencyMap.get(bKey);
				int old = this.BFrequencyMap.get(bKey);
				this.BFrequencyMap.put(bKey,times+old);
			}
		}
		this.correctnessB.addAll(resultComp.correctnessB);
		this.timeB.addAll(resultComp.timeB);
		this.hyperVolumnB.addAll(resultComp.hyperVolumnB);
		this.spreadB.addAll(resultComp.spreadB);
	}
	
	/**
	 * @param resultComp
	 */
	public void calculateCompResult() {
		if (DEBUG)
			System.out.println("The solutions in A and "+this.path2+":"+Arrays.asList(this.checkFrequency()));
		
		this.intersect = new LinkedHashSet<>(this.A); 
		this.intersect.retainAll(this.B);
		
		this.uniqueA = new LinkedHashSet<>(this.A); 
		this.uniqueA.removeAll(this.B);
		
		this.uniqueB = new LinkedHashSet<>(this.B); 
		this.uniqueB.removeAll(this.A);
		
		this.intersectCorrect = new LinkedHashSet<>(this.correctInA); 
		this.intersectCorrect.retainAll(this.correctInB);
		
		this.uniqueCorrectA = new LinkedHashSet<>(this.correctInA); 
		this.uniqueCorrectA.removeAll(this.correctInB);
		
		this.uniqueCorrectB = new LinkedHashSet<>(this.correctInB); 
		this.uniqueCorrectB.removeAll(this.correctInA);
		
		Set<String> union = new LinkedHashSet<>(this.correctInA); 
		union.addAll(this.correctInB);
		
		this.nonDominant = ResultComparator.findDominantSol(union);
		this.nonDominantInA = new LinkedHashSet<>(this.nonDominant); 
		this.nonDominantInA.retainAll(this.correctInA);
		this.nonDominantInB = new LinkedHashSet<>(this.nonDominant); 
		this.nonDominantInB.retainAll(this.correctInB);
		
		this.uniqueNonDominantInA = new LinkedHashSet<>(this.nonDominantInA); 
		this.uniqueNonDominantInA.retainAll(this.uniqueA);
		this.uniqueNonDominantInB = new LinkedHashSet<>(this.nonDominantInB); 
		this.uniqueNonDominantInB.retainAll(this.uniqueB);
	}

	@Override
	public int compareTo(ResultCompFormat that) {
		Integer inter1 = this.intersect.size();
		Integer inter2 = that.intersect.size();

		Integer unique1 = this.uniqueNonDominantInB.size();
		Integer unique2 = that.uniqueNonDominantInB.size();

		int score1 = inter1 + 5 * unique1;
		int score2 = inter2 + 5 * unique2;
		if (score1 > score2) {
			return 1;
		} else if (score1 < score2) {
			return -1;
		} else {
			return 0;
		}
	}
}
