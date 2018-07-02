//
// Translated by CS2J (http://www.cs2j.com): 1/3/2014 9:42:01 PM
//

package ProductLine.LogicFormula;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;

public class Equal  extends ProductLine.LogicFormula.LogicFormula 
{
    public ProductLine.LogicFormula.LogicFormula EqlA;
    public ProductLine.LogicFormula.LogicFormula EqlB;
    //A<=>B
    public Equal(ProductLine.LogicFormula.LogicFormula A, ProductLine.LogicFormula.LogicFormula B)  {
        EqlA = A;
        EqlB = B;
    }

    public ArrayList<ProductLine.LogicFormula.LogicFormula> toCNF()  {
        //A<=>B : (A=>B)^(B=>A)
    	ArrayList<ProductLine.LogicFormula.LogicFormula> result = new ArrayList<ProductLine.LogicFormula.LogicFormula>();
    	ArrayList<ProductLine.LogicFormula.LogicFormula> part1 = new ArrayList<ProductLine.LogicFormula.LogicFormula>();
        part1.add(new ProductLine.LogicFormula.Not(EqlA));
        part1.add(EqlB);
        ProductLine.LogicFormula.Or part1Result = new ProductLine.LogicFormula.Or(part1);
        ArrayList<ProductLine.LogicFormula.LogicFormula> part2 = new ArrayList<ProductLine.LogicFormula.LogicFormula>();
        part2.add(new ProductLine.LogicFormula.Not(EqlB));
        part2.add(EqlA);
        ProductLine.LogicFormula.Or part2Result = new ProductLine.LogicFormula.Or(part2);
        result.addAll(part1Result.toCNF());
        result.addAll(part2Result.toCNF());
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
        return "(" + EqlA.print() + "<=>" + EqlB.print() + ")";
    }

    public boolean evaluation(HashMap<String,Boolean> value)  {
        //List<LogicFormula> part1 = new List<LogicFormula>();
        //part1.Add(new Not(EqlA));
        //part1.Add(EqlB);
        //Or part1Result = new Or(part1);
        //List<LogicFormula> part2 = new List<LogicFormula>();
        //part2.Add(new Not(EqlB));
        //part2.Add(EqlA);
        //Or part2Result = new Or(part2);
        //return part1Result.Evaluation(value) && part2Result.Evaluation(value);
        boolean valA = EqlA.evaluation(value);
        boolean valB = EqlB.evaluation(value);
        return (!valA || valB) && (!valB || valA);
    }

    public LinkedHashSet<String> getAllProps()  {
        LinkedHashSet<String> result = EqlA.getAllProps();
        result.addAll(EqlB.getAllProps());
        return result;
    }
    
    public boolean evaluationI(HashMap<Integer,Boolean> value)  {
        //List<LogicFormula> part1 = new List<LogicFormula>();
        //part1.Add(new Not(EqlA));
        //part1.Add(EqlB);
        //Or part1Result = new Or(part1);
        //List<LogicFormula> part2 = new List<LogicFormula>();
        //part2.Add(new Not(EqlB));
        //part2.Add(EqlA);
        //Or part2Result = new Or(part2);
        //return part1Result.evaluationI(value) && part2Result.evaluationI(value);
        boolean valA = EqlA.evaluationI(value);
        boolean valB = EqlB.evaluationI(value);
        return (!valA || valB) && (!valB || valA);
    }

    public LinkedHashSet<Integer> getAllPropsI()  {
        LinkedHashSet<Integer> result = EqlA.getAllPropsI();
        result.addAll(EqlB.getAllPropsI());
        return result;
    }

}


