//
// Translated by CS2J (http://www.cs2j.com): 1/3/2014 9:42:00 PM
//

package ProductLine.BestFix;



import java.util.LinkedHashSet;
import java.util.Random;

import ProductLine.GAParams;
import ProductLine.BestFix.Gene;

//  All code copyright (c) 2003 Barry Lapthorn
//  Website:  http://www.lapthorn.net
//
//  Disclaimer:
//  All code is provided on an "AS IS" basis, without warranty. The author
//  makes no representation, or warranty, either express or implied, with
//  respect to the code, its quality, accuracy, or fitness for a specific
//  purpose. Therefore, the author shall not have any liability to you or any
//  other person or entity with respect to any liability, loss, or damage
//  caused or alleged to have been caused directly or indirectly by the code
//  provided.  This includes, but is not limited to, interruption of service,
//  loss of data, loss of profits, or consequential damages from the use of
//  this code.
//
//
//  $Author: barry $
//  $Revision: 1.1 $
//
//  $Id: Gene.cs,v 1.1 2003/08/19 20:59:05 barry Exp $
/**
* =====To change:=====
* 1. CreateGenes //This is called by initial population
* 2. Crossover
* 3. Mutate
* 
* 
* 
* Summary description for Gene.
*/
public class Gene   
{
    public int[] Genes;
    public double Fitness;
    static Random m_random = new Random();
    //public static double MutationRate;
    public Gene(int length) throws Exception {
        Genes = new int[length];
    }

    public Gene(int[] genes) throws Exception {
        Genes = new int[genes.length];
        for (Integer i = 0;i < Genes.length;i++)
            Genes[i] = genes[i];
    }

    public void createGenes(LinkedHashSet<String> Features) throws Exception {
        for (String oldFeature : Features)
        {
        	if(!GAParams.MandatoryFeatures.contains(oldFeature)){
        		 Genes[GAParams.featuresToIntegerMap.get(oldFeature)] = 1;
        	}
           
        }
    }

    //for (int i = 0; i < Genes.Length; i++)
    //    Genes[i] = m_random.NextDouble();
    public Gene[] crossover(Gene genome2) throws Exception {
    	
    	
    	Gene[] genes=new Gene[2];
    	
    	
        Integer pos = ((int)((m_random.nextDouble() * Genes.length)));
        Gene child1=new Gene(Genes.length);
        Gene child2=new Gene(Genes.length);
        for (Integer i = 0;i < Genes.length;i++)
        {
            if (i < pos)
            {
                child1.Genes[i] = Genes[i];
                child2.Genes[i] = genome2.Genes[i];
            }
            else
            {
                child1.Genes[i] = genome2.Genes[i];
                child2.Genes[i] = Genes[i];
            } 
        }
        
        genes[0]=child1;
        genes[1]=child2;
        
        return genes;
    }

    public void mutate() throws Exception {
        LinkedHashSet<Integer> ErrorPosition = GAParams.errorPosition(Genes).getViolatedPropsInt();
        for (Integer pos = 0;pos < Genes.length;pos++)
        {
            Boolean containError = ErrorPosition.contains(pos);
            if (!containError && m_random.nextDouble() < GAParams.MutationRate || containError && m_random.nextDouble() < GAParams.ErrorMutationRate)
            {
                Genes[pos] = 1 - Genes[pos];
            }
             
        }
    }

    public int[] getValues() throws Exception {
    	int[] values=new int[Genes.length];
        for (Integer i = 0;i < Genes.length;i++)
            values[i] = Genes[i];
        
        return values;
    }

}


