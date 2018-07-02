//
// Translated by CS2J (http://www.cs2j.com): 1/3/2014 9:42:01 PM
//

package ProductLine.LogicFormula;

import java.util.ArrayList;
//import CS2JNet.System.Collections.LCC.CSList;
import java.util.HashMap;
import java.util.LinkedHashSet;

public class And  extends ProductLine.LogicFormula.LogicFormula 
{
    public ArrayList<ProductLine.LogicFormula.LogicFormula> conjunct;
    public And(ArrayList<ProductLine.LogicFormula.LogicFormula> parameters)  {
        conjunct = parameters;
    }
    
    public And(ProductLine.LogicFormula.LogicFormula parameter1, ProductLine.LogicFormula.LogicFormula parameter2)  {
    	conjunct = new ArrayList<ProductLine.LogicFormula.LogicFormula>();
    	conjunct.add(parameter1);
    	conjunct.add(parameter2);
    }
    

    public ArrayList<ProductLine.LogicFormula.LogicFormula> toCNF()  {
    	ArrayList<ProductLine.LogicFormula.LogicFormula> result = new ArrayList<ProductLine.LogicFormula.LogicFormula>();
        for (ProductLine.LogicFormula.LogicFormula conjuctPara : conjunct)
        {
        	ArrayList<ProductLine.LogicFormula.LogicFormula> cnf = conjuctPara.toCNF();
            for (ProductLine.LogicFormula.LogicFormula para : cnf)
            {
                result.add(para);
            }
        }
        return result;
    }

    public String toString() {
        try
        {
            return print();
        }
        catch (RuntimeException __dummyCatchVar0)
        {
            throw __dummyCatchVar0;
        }
        catch (Exception __dummyCatchVar0)
        {
            throw new RuntimeException(__dummyCatchVar0);
        }
    
    }

    public String print()  {
        String formular = "(";
        int count = 0;
        for (ProductLine.LogicFormula.LogicFormula lf : conjunct)
        {
            formular += lf.print();
            count++;
            if (count != conjunct.size())
                formular += "&&";
             
        }
        formular += ")";
        return formular;
    }

    public String toSATString()  {
        String satString = "";
        for (ProductLine.LogicFormula.LogicFormula junct : conjunct)
        {
            satString = satString + junct.toSATString() + " 0\r\n";
        }
        return satString;
    }

    public boolean evaluation(HashMap<String,Boolean> value)  {
        for (ProductLine.LogicFormula.LogicFormula andconjuct : conjunct)
        {
            if (!andconjuct.evaluation(value))
                return false;
             
        }
        return true;
    }

    public LinkedHashSet<String> getAllProps()  {
        LinkedHashSet<String> result = new LinkedHashSet<String>();
        for (ProductLine.LogicFormula.LogicFormula atom : conjunct)
        {
         result.addAll(atom.getAllProps());
        	//result.UnionWith(atom.getAllProps());
        }
        return result;
    }
    
    public boolean evaluationI(HashMap<Integer,Boolean> value)  {
        for (ProductLine.LogicFormula.LogicFormula andconjuct : conjunct)
        {
            if (!andconjuct.evaluationI(value))
                return false;
             
        }
        return true;
    }

    public LinkedHashSet<Integer> getAllPropsI()  {
        LinkedHashSet<Integer> result = new LinkedHashSet<Integer>();
        for (ProductLine.LogicFormula.LogicFormula atom : conjunct)
        {
         result.addAll(atom.getAllPropsI());
        	//result.UnionWith(atom.getAllPropsI());
        }
        return result;
    }

}


