//
// Translated by CS2J (http://www.cs2j.com): 1/3/2014 9:42:01 PM
//

package ProductLine.LogicFormula;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;

import ProductLine.GAParams;

public class Prop  extends ProductLine.LogicFormula.LogicFormula 
{
    public String varname;
    public int varInt=-1;
    public Prop(String name)  {
        varname = name;
    }

    public ArrayList<ProductLine.LogicFormula.LogicFormula> toCNF()  {
        //todo:implement it
    	ArrayList<ProductLine.LogicFormula.LogicFormula> result = new ArrayList<ProductLine.LogicFormula.LogicFormula>();
        result.add(this);
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
        return this.varname;
    }

    public String toSATString()  {
        return this.varname;
    }

    public boolean evaluation(HashMap<String,Boolean> value)   {
//        if (value.containsKey(varname))
//        {
//            return value.get(varname);
//        }
//        else
//        {
//            throw new RuntimeException();
//        }
    	return value.get(varname);
    }

    public LinkedHashSet<String> getAllProps()  {
        LinkedHashSet<String> result = new LinkedHashSet<String>();
        result.add(varname);
        return result;
    }
    
    private void checkI() {
    	if(varInt==-1) {
    		varInt=GAParams.AllFeaturesToIntegerMap.get(varname);
    	}
    }
    
    public boolean evaluationI(HashMap<Integer,Boolean> value)   {
//      if (value.containsKey(varname))
//      {
//          return value.get(varname);
//      }
//      else
//      {
//          throw new RuntimeException();
//      }
    	 checkI();
  	return value.get(varInt);
  }

  public LinkedHashSet<Integer> getAllPropsI()  {
	  checkI();
      LinkedHashSet<Integer> result = new LinkedHashSet<Integer>();
      result.add(varInt);
      return result;
  }

}


