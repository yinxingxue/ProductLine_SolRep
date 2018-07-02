//
// Translated by CS2J (http://www.cs2j.com): 1/3/2014 9:42:00 PM
//

package ProductLine.BestFix;

import ProductLine.GAParams;

public class EditDistance   
{
    public static int[] minimalChange(int[] old, int[] new1) throws Exception {
        for (int i = 0;i < old.length;i++)
        {
            if (old[i] != new1[i])
            {
                int oldValue = new1[i];
                new1[i] = old[i];
                if (!GAParams.isValidFeatures(new1))
                {
                    new1[i] = oldValue;
                }
                 
            }
             
        }
        return new1;
    }

    public static int levenshteinDistance(int[] old, int[] new1) {
        int count = 0;
        for (int i = 0;i < old.length;i++)
        {
            if (old[i] != new1[i])
            {
                count++;
            }
             
        }
        return count;
    }

    // <summary>
    /// Compute the distance between two strings.
    /// </summary>
    public static int levenshteinDistance(String s, String t)  {
        int n = s.length();
        int m = t.length();
        int[][] d = new int[n + 1][m + 1];
        // Step 1
        if (n == 0)
        {
            return m;
        }
         
        if (m == 0)
        {
            return n;
        }
         
        for (int i = 0;i <= n;d[i][0] = i++)
        {
        }
        for (int j = 0;j <= m;d[0][j] = j++)
        {
        }
        for (int i = 1;i <= n;i++)
        {
            for (int j = 1;j <= m;j++)
            {
                // Step 2
                // Step 3
                //Step 4
                // Step 5
                int cost = (t.charAt(j - 1) == s.charAt(i - 1)) ? 0 : 1;
                // Step 6
                d[i][ j] = Math.min(Math.min(d[i - 1][ j] + 1, d[i][ j - 1] + 1), d[i - 1][ j - 1] + cost);
            }
        }
        return d[n][ m];
    }

}


// Step 7