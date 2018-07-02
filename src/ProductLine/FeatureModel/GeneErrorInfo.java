package ProductLine.FeatureModel;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class GeneErrorInfo {
	private LinkedHashSet<Integer> violatedPropsInt = null;
	private int violationFormulaCnt=0;
	public ArrayList<Integer> violatedFormula;
	
	
	public GeneErrorInfo(ArrayList<Integer> violatedFormula, LinkedHashSet<Integer> violatedPropsInt,
			int violationFormulaCnt) {
		this.violatedPropsInt = violatedPropsInt;
		this.violationFormulaCnt = violationFormulaCnt;
		this.violatedFormula=violatedFormula;
	}
	
	
	public ArrayList<Integer> getViolatedFormula() {
		return violatedFormula;
	}
	public LinkedHashSet<Integer> getViolatedPropsInt() {
		return violatedPropsInt;
	}
	public int getViolationFormulaCnt() {
		return violationFormulaCnt;
	}
	
	
}
