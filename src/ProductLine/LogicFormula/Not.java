//
// Translated by CS2J (http://www.cs2j.com): 1/3/2014 9:42:01 PM
//

package ProductLine.LogicFormula;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;

public class Not  extends ProductLine.LogicFormula.LogicFormula 
{
    public ProductLine.LogicFormula.LogicFormula notFormula;
    public Not(ProductLine.LogicFormula.LogicFormula parameter)  {
        this.notFormula = parameter;
    }

    public ArrayList<ProductLine.LogicFormula.LogicFormula> toCNF()  {
    	ArrayList<ProductLine.LogicFormula.LogicFormula> result = new ArrayList<ProductLine.LogicFormula.LogicFormula>();
        if (notFormula instanceof ProductLine.LogicFormula.Prop)
        {
            result.add(this);
            return result;
        }
        else if (notFormula instanceof ProductLine.LogicFormula.Not)
        {
            return ((ProductLine.LogicFormula.Not)notFormula).notFormula.toCNF();
        }
        else if (notFormula instanceof ProductLine.LogicFormula.Or)
        {
        	ArrayList<ProductLine.LogicFormula.LogicFormula> andConjuncts = new ArrayList<ProductLine.LogicFormula.LogicFormula>();
            for (ProductLine.LogicFormula.LogicFormula formula : ((ProductLine.LogicFormula.Or)notFormula).disjunct)
            {
                andConjuncts.add(new ProductLine.LogicFormula.Not(formula));
            }
            ProductLine.LogicFormula.And tranformCNF = new ProductLine.LogicFormula.And(andConjuncts);
            return tranformCNF.toCNF();
        }
        else if (notFormula instanceof ProductLine.LogicFormula.Imply)
        {
            //not(A=>B)= A ^ not B
        	ArrayList<ProductLine.LogicFormula.LogicFormula> implyACNF = ((ProductLine.LogicFormula.Imply)notFormula).ImpA.toCNF();
            result = implyACNF;
            ProductLine.LogicFormula.Not implyBpart = new ProductLine.LogicFormula.Not(((ProductLine.LogicFormula.Imply)notFormula).ImpB);
            result.addAll(implyBpart.toCNF());
            return result;
        }
        else if (notFormula instanceof ProductLine.LogicFormula.And)
        {
            // NOT (A ^ B..)= Not A v Not B....
        	ArrayList<ProductLine.LogicFormula.LogicFormula> orConjuncts = new ArrayList<ProductLine.LogicFormula.LogicFormula>();
            for (ProductLine.LogicFormula.LogicFormula formula : ((ProductLine.LogicFormula.And)notFormula).conjunct)
            {
                orConjuncts.add(new ProductLine.LogicFormula.Not(formula));
            }
            ProductLine.LogicFormula.Or tranformCNF = new ProductLine.LogicFormula.Or(orConjuncts);
            return tranformCNF.toCNF();
        }
             
        return null;
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
        return "!" + notFormula.print() + "";
    }

    public String toSATString()  {
        return "-" + notFormula.toSATString();
    }

    public boolean evaluation(HashMap<String,Boolean> value)  {
        return !(notFormula.evaluation(value));
    }

    public LinkedHashSet<String> getAllProps()  {
        LinkedHashSet<String> result = notFormula.getAllProps();
        return result;
    }
    
    public boolean evaluationI(HashMap<Integer,Boolean> value)  {
        return !(notFormula.evaluationI(value));
    }

    public LinkedHashSet<Integer> getAllPropsI()  {
        LinkedHashSet<Integer> result = notFormula.getAllPropsI();
        return result;
    }

}


