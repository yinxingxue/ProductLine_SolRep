//
// Translated by CS2J (http://www.cs2j.com): 1/3/2014 9:42:01 PM
//

package ProductLine.LogicFormula;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;

public class Imply  extends ProductLine.LogicFormula.LogicFormula 
{
    public ProductLine.LogicFormula.LogicFormula ImpA;
    public ProductLine.LogicFormula.LogicFormula ImpB;
    //A=>B
    public Imply(ProductLine.LogicFormula.LogicFormula A, ProductLine.LogicFormula.LogicFormula B)  {
        ImpA = A;
        ImpB = B;
    }

    public ArrayList<ProductLine.LogicFormula.LogicFormula> toCNF()  {
    	ArrayList<ProductLine.LogicFormula.LogicFormula> cnfPara = new ArrayList<ProductLine.LogicFormula.LogicFormula>();
        ProductLine.LogicFormula.Not notImpA = new ProductLine.LogicFormula.Not(ImpA);
        cnfPara.add(notImpA);
        cnfPara.add(ImpB);
        ProductLine.LogicFormula.Or cnfResult = new ProductLine.LogicFormula.Or(cnfPara);
        return cnfResult.toCNF();
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
        return "(" + ImpA.print() + "=>" + ImpB.print() + ")";
    }

    public boolean evaluation(HashMap<String,Boolean> value)  {
        return (!ImpA.evaluation(value)) || ImpB.evaluation(value);
    }

    //Not notA=new Not(ImpA);
    public LinkedHashSet<String> getAllProps()  {
        LinkedHashSet<String> result = ImpA.getAllProps();
        result.addAll(ImpB.getAllProps());
        return result;
    }
    
    public boolean evaluationI(HashMap<Integer,Boolean> value)  {
        return (!ImpA.evaluationI(value)) || ImpB.evaluationI(value);
    }

    //Not notA=new Not(ImpA);
    public LinkedHashSet<Integer> getAllPropsI()  {
        LinkedHashSet<Integer> result = ImpA.getAllPropsI();
        result.addAll(ImpB.getAllPropsI());
        return result;
    }

}


