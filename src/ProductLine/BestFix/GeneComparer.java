//
// Translated by CS2J (http://www.cs2j.com): 1/3/2014 9:42:00 PM
//

package ProductLine.BestFix;

//import CS2JNet.System.ArgumentException;
import ProductLine.BestFix.Gene;
import java.util.Comparator;

public final class GeneComparer  implements Comparator<Gene> 
{
	@Override
	public int compare(Gene x, Gene y) {
	        if (((Gene)x).Fitness > ((Gene)y).Fitness)
	            return 1;
	        else if (((Gene)x).Fitness == ((Gene)y).Fitness)
	            return 0;
	        else
	            return -1;  
	}

}


