//
// Translated by CS2J (http://www.cs2j.com): 1/3/2014 9:42:01 PM
//

package ProductLine.LogicFormula;


import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;

public abstract class LogicFormula   
{
    public ArrayList<ProductLine.LogicFormula.LogicFormula> toCNF()  {
        return null;
    }

    //todo:implement it
    public abstract String print()  ;

    public LinkedHashSet<String> getAllProps()  {
        return null;
    }

    public String toSATString()  {
        return null;
    }

    // to sat input
    public abstract boolean evaluation(HashMap<String,Boolean> value)  ;
    
    
    public LinkedHashSet<Integer> getAllPropsI()  {
        return null;
    }
    
    
    public abstract boolean evaluationI(HashMap<Integer,Boolean> value)  ;
    
    public static void writeCNFFile(String filepath, ArrayList<String> cnfs, ArrayList<String> variables){
    	HashMap<String, String> variableMapping =new HashMap<String, String>();
    	int index=0;
    	for(String var: variables){
    	  index++;
    	  variableMapping.put(var, index+"");
    	}
    	
    	PrintWriter writer;
		try {
			writer = new PrintWriter(filepath);
			//write first part 
			for (Map.Entry<String, String> entry : variableMapping.entrySet()) {
			    writer.println("c "+entry.getValue()+" "+entry.getKey());
			}
		  
			writer.println("p cnf "+variables.size()+" "+cnfs.size());
			for(String cnf:cnfs){
				cnf=cnf.replaceAll("[(]", "");
				cnf=cnf.replaceAll("[)]", "");
				String[] props=cnf.split("[|][|]");
				for(int i=0;i<props.length;i++){
					if(props[i].trim().startsWith("!")){
						writer.print("-"+variableMapping.get(props[i].replace("!", "").trim())+" ");	
					}else{
						writer.print(variableMapping.get(props[i].trim())+" ");	
					}
				}
				writer.println("0");
			}
	    	
	    	writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
   

	public static void main(String[] args){
		ArrayList<String> variables =new ArrayList<String>();
		variables.add("a");
		variables.add("b");
		variables.add("c");
		
		LogicFormula aa=new And(new Equal(new Prop("a"),new Prop ("b")), new Prop("c"));
		ArrayList<String> result=new ArrayList<String>();
		for(LogicFormula cnf:aa.toCNF()){
			result.add(cnf.toString());
		}
		writeCNFFile("C:\\work\\SPLJava\\ProductLine\\test.txt",result,variables);
	}
}


