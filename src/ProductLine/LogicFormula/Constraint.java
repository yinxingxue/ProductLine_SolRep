//
// Translated by CS2J (http://www.cs2j.com): 1/3/2014 9:42:01 PM
//

package ProductLine.LogicFormula;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;

public  class Constraint
{
 
	private LogicFormula formula;
	private ArrayList<Integer> atoms;
	
	public Constraint(LogicFormula f,ArrayList<Integer> a) {
		formula=f;
		atoms=a;
	
	}
	
	public ArrayList<Integer> getAllAtoms(){
		return atoms;
	}
   
	 public  boolean evaluation(HashMap<String,Boolean> value) {
		 return formula.evaluation(value);
	 }

   

}


