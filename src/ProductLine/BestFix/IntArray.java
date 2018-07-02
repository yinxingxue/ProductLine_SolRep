//
// Translated by CS2J (http://www.cs2j.com): 1/3/2014 9:42:00 PM
//

package ProductLine.BestFix;



public class IntArray
{
	
	public int[] x=null;
	
	

    
    public IntArray(int[] x) {
		this.x = x;
	}

	@Override
	public int hashCode()
	{
		   int result = 0;
	        int shift = 0;
	        for (int i = 0;i < x.length;i++)
	        {
	            shift = (shift + 11) % 21;
	            result ^= (((int)(x[i])) + 1024) << shift;
	        }
	        return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof IntArray))return false;
		int[] y=((IntArray) obj).x;
		 if (x == y)
	            return true;
	         
	        for (int i = 0;i < x.length;++i)
	            //if (x == null || y == null)
	            //    return false;
	            //if (x.Length != y.Length)
	            //    return false;
	            if (x[i] != y[i])
	            {
	                return false;
	            }
	             
	        return true;
	    }

}
//public boolean equals(int[] x, int[] y) throws Exception {
//if (x == y)
//  return true;
//
//for (int i = 0;i < x.length;++i)
//  //if (x == null || y == null)
//  //    return false;
//  //if (x.Length != y.Length)
//  //    return false;
//  if (x[i] != y[i])
//  {
//      return false;
//  }
//   
//return true;
//}
//
////public int GetHashCode(int[] data)
////{
////    int hc = data.Length;
////    for (int i = 0; i < data.Length; ++i)
////    {
////        hc = unchecked(hc * 11 + data[i]);
////    }
////    return hc;
////}
//public int getHashCode(int[] values) throws Exception {
//int result = 0;
//int shift = 0;
//for (int i = 0;i < values.length;i++)
//{
//  shift = (shift + 11) % 21;
//  result ^= (((int)(values[i])) + 1024) << shift;
//}
//return result;
//}

//public int GetHashCode(int[] data)
//{
//    if (data == null)
//        return 0;
//    int result = 17;
//    foreach (var value in data)
//    {
//        result += result * 23 + (int)value;
//    }
//    return result;
//}