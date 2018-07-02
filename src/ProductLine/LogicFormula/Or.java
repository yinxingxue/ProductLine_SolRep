//
// Translated by CS2J (http://www.cs2j.com): 1/3/2014 9:42:01 PM
//

package ProductLine.LogicFormula;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;

public class Or  extends ProductLine.LogicFormula.LogicFormula 
{
    public ArrayList<ProductLine.LogicFormula.LogicFormula> disjunct;
    public Or(ArrayList<ProductLine.LogicFormula.LogicFormula> parameters)  {
        disjunct = parameters;
    }

    public Or(ProductLine.LogicFormula.LogicFormula parameter1, ProductLine.LogicFormula.LogicFormula parameter2)  {
        disjunct = new ArrayList<ProductLine.LogicFormula.LogicFormula>();
        disjunct.add(parameter1);
        disjunct.add(parameter2);
    }
    
    public ArrayList<ProductLine.LogicFormula.LogicFormula> toCNF()  {
    	ArrayList<ProductLine.LogicFormula.LogicFormula> part1 = disjunct.get(0).toCNF();
    	ArrayList<ProductLine.LogicFormula.LogicFormula> part2=null;
        if (disjunct.size() > 2)
        {
        	ArrayList<ProductLine.LogicFormula.LogicFormula> part = disjunct;
            part.remove(0);
            ProductLine.LogicFormula.Or part2Formula = new ProductLine.LogicFormula.Or(part);
            part2 = part2Formula.toCNF();
        }
        else if(disjunct.size() ==2)
        {
            part2 = disjunct.get(1).toCNF();
        } 
        // So we need a CNF formula equivalent to (P1 ^ P2 ^ ... ^ Pm) v (Q1 ^ Q2 ^ ... ^ Qn).
        ArrayList<ProductLine.LogicFormula.LogicFormula> result = new ArrayList<ProductLine.LogicFormula.LogicFormula>();
        for (ProductLine.LogicFormula.LogicFormula part1conj : part1)
        {
            if(part2!=null){
        	for (ProductLine.LogicFormula.LogicFormula part2conj : part2)
            {
            	ArrayList<ProductLine.LogicFormula.LogicFormula> orpart = new ArrayList<ProductLine.LogicFormula.LogicFormula>();
                orpart.add(part1conj);
                orpart.add(part2conj);
                result.add(new ProductLine.LogicFormula.Or(orpart));
            }
            }else{
            	result.add(part1conj);
            }
        }
        return result;
    }

    //if (part1 is Not | part1 is Prop)
    //{
    //    if (part2 is Not | part2 is Prop)
    //    {
    //        List<LogicFormula> orList = new List<LogicFormula>();
    //        orList.Add(part1);
    //        orList.Add(part2);
    //        cnfResultArray.Add(new Or(orList));
    //    }
    //    else
    //    {
    //        foreach (LogicFormula part2junct in ((And)part2).conjunct)
    //        {
    //            List<LogicFormula> orList = new List<LogicFormula>();
    //            orList.Add(part1);
    //            orList.Add(part2junct);
    //            cnfResultArray.Add(new Or(orList));
    //        }
    //    }
    //}
    //else
    //{
    //    foreach (LogicFormula part1junct in ((And) part1).conjunct)
    //    {
    //        if (part2 is Not | part2 is Prop)
    //        {
    //            List<LogicFormula> orList = new List<LogicFormula>();
    //            orList.Add(part1junct);
    //            orList.Add(part2);
    //            cnfResultArray.Add(new Or(orList));
    //        }
    //        else
    //        {
    //            foreach (LogicFormula part2junct in ((And) part2).conjunct)
    //            {
    //                List<LogicFormula> orList = new List<LogicFormula>();
    //                orList.Add(part1junct);
    //                orList.Add(part2junct);
    //                cnfResultArray.Add(new Or(orList));
    //            }
    //        }
    //    }
    //}
    // return new And(cnfResultArray);
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
        for (ProductLine.LogicFormula.LogicFormula lf : disjunct)
        {
            formular += lf.print();
            count++;
            if (count != disjunct.size())
                formular += "||";
             
        }
        formular += ")";
        return formular;
    }

    public String toSATString()  {
        String satString = "";
        for (ProductLine.LogicFormula.LogicFormula junct : disjunct)
        {
            if (satString.length() == 0)
            {
                satString = satString + junct.toSATString();
            }
            else
            {
                satString = satString + " " + junct.toSATString();
            } 
        }
        return satString;
    }
    
   

    public boolean evaluation(HashMap<String,Boolean> value)  {
        for (ProductLine.LogicFormula.LogicFormula junct : disjunct)
        {
            if (junct.evaluation(value))
            {
                return true;
            }
             
        }
        return false;
    }

    public LinkedHashSet<String> getAllProps()  {
        LinkedHashSet<String> result = new LinkedHashSet<String>();
        for (ProductLine.LogicFormula.LogicFormula atom : disjunct)
        {
            result.addAll(atom.getAllProps());
        }
        return result;
    }
    
    public boolean evaluationI(HashMap<Integer,Boolean> value)  {
        for (ProductLine.LogicFormula.LogicFormula junct : disjunct)
        {
            if (junct.evaluationI(value))
            {
                return true;
            }
             
        }
        return false;
    }
    
    
     public LinkedHashSet<Integer> getAllPropsI()  {
        LinkedHashSet<Integer> result = new LinkedHashSet<Integer>();
        for (ProductLine.LogicFormula.LogicFormula atom : disjunct)
        {
            result.addAll(atom.getAllPropsI());
        }
        return result;
    }
}


