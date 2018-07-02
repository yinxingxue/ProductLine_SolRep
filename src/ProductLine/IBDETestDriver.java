/**
 * 
 */
package ProductLine;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import jmetal.metaheuristics.ibea.IBDE_ProductLine_main;
import jmetal.metaheuristics.ibea.IBEA_ProductLine_main;

/**
 * @author yinxing
 *
 */
public class IBDETestDriver {

	public static int exeTime= 30;
	
	public static boolean runIBDE = true;
	public static boolean runIBEA = false;
	public static boolean runCOMP = true;
	public static boolean runSPLOT = true;
	
	public static Map<String,String> IBDE_Eex = new LinkedHashMap<String,String>();
	public static Map<String,String> IBEA_Eex = new LinkedHashMap<String,String>();
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	   {
			String iddeResultsRootPath = args[0];
			File iddeResultRootFile = new File(iddeResultsRootPath);
			String[] paras = new String[8];
			paras[0] = "-i";
			paras[2] = "-p";
			paras[4] = "-t";
			paras[5] = String.valueOf(exeTime);
			paras[6] = "-set";
			paras[7] = "4";
			
			if(runSPLOT)
			{
				paras[1] = "EShop_RAND1";
				paras[3] = iddeResultRootFile.getAbsolutePath()
						+ ResultComparator.FILE_SEPARATOR
						+ "eshop/IBDE_Attr1/para4";
				IBDE_Eex.put(paras[1], paras[3]);
				if (runIBDE) 
				{
					Utility.clearAndCreate(paras[3]);
					IBDE_ProductLine_main.main(paras);			
				}
				
				paras[1] = "EShop_RAND2";
				paras[3] = iddeResultRootFile.getAbsolutePath()
						+ ResultComparator.FILE_SEPARATOR
						+ "eshop/IBDE_Attr2/para4";
				IBDE_Eex.put(paras[1], paras[3]);
				if (runIBDE) 
				{
					Utility.clearAndCreate(paras[3]);
					IBDE_ProductLine_main.main(paras);			
				}
				
				paras[1] = "EShop_";
				paras[3] = iddeResultRootFile.getAbsolutePath()
						+ ResultComparator.FILE_SEPARATOR
						+ "eshop/IBDE_RandAttr/para4";
				IBDE_Eex.put(paras[1], paras[3]);
				if (runIBDE) 
				{
					Utility.clearAndCreate(paras[3]);
					IBDE_ProductLine_main.main(paras);			
				}
			}

			paras[1] = "ECos_RAND1";
			paras[3] = iddeResultRootFile.getAbsolutePath()
					+ ResultComparator.FILE_SEPARATOR
					+ "ecos/IBDE_Attr1/para4";
			IBDE_Eex.put(paras[1], paras[3]);
			if (runIBDE) 
			{
				Utility.clearAndCreate(paras[3]);
				IBDE_ProductLine_main.main(paras);			
			}
			
			paras[1] = "ECos_RAND2";
			paras[3] = iddeResultRootFile.getAbsolutePath()
					+ ResultComparator.FILE_SEPARATOR
					+ "ecos/IBDE_Attr2/para4";
			IBDE_Eex.put(paras[1], paras[3]);
			if (runIBDE) 
			{
				Utility.clearAndCreate(paras[3]);
				IBDE_ProductLine_main.main(paras);			
			}
			
			paras[1] = "ECos_";
			paras[3] = iddeResultRootFile.getAbsolutePath()
					+ ResultComparator.FILE_SEPARATOR
					+ "ecos/IBDE_RandAttr/para4";
			IBDE_Eex.put(paras[1], paras[3]);
			if (runIBDE) 
			{
				Utility.clearAndCreate(paras[3]);
				IBDE_ProductLine_main.main(paras);			
			}
			
			paras[1] = "UCLinux_RAND1";
			paras[3] = iddeResultRootFile.getAbsolutePath()
					+ ResultComparator.FILE_SEPARATOR
					+ "uclinux/IBDE_Attr1/para4";
			IBDE_Eex.put(paras[1], paras[3]);
			if (runIBDE) 
			{
				Utility.clearAndCreate(paras[3]);
				IBDE_ProductLine_main.main(paras);			
			}
			
			paras[1] = "UCLinux_RAND2";
			paras[3] = iddeResultRootFile.getAbsolutePath()
					+ ResultComparator.FILE_SEPARATOR
					+ "uclinux/IBDE_Attr2/para4";
			IBDE_Eex.put(paras[1], paras[3]);
			if (runIBDE) 
			{
				Utility.clearAndCreate(paras[3]);
				IBDE_ProductLine_main.main(paras);			
			}
			
			paras[1] = "UCLinux_";
			paras[3] = iddeResultRootFile.getAbsolutePath()
					+ ResultComparator.FILE_SEPARATOR
					+ "uclinux/IBDE_RandAttr/para4";
			IBDE_Eex.put(paras[1], paras[3]);
			if (runIBDE) 
			{
				Utility.clearAndCreate(paras[3]);
				IBDE_ProductLine_main.main(paras);			
			}
		}

	   {
				String ibeaResultsRootPath = args[1];
				File ibeaResultRootFile = new File(ibeaResultsRootPath);
				String[] paras = new String[8];
				paras[0] = "-i";
				paras[2] = "-p";
				paras[4] = "-t";
				paras[5] = "30";
				paras[6] = "-set";
				paras[7] = "4";
				
				if(runSPLOT)
				{
					paras[1] = "EShop_RAND1";
					paras[3] = ibeaResultRootFile.getAbsolutePath()
							+ ResultComparator.FILE_SEPARATOR
							+ "eshop/IBEA_Attr1/para4";
					IBEA_Eex.put(paras[1], paras[3]);
					if (runIBEA) 
					{
						Utility.clearAndCreate(paras[3]);
						IBEA_ProductLine_main.main(paras);
					}
					
					paras[1] = "EShop_RAND2";
					paras[3] = ibeaResultRootFile.getAbsolutePath()
							+ ResultComparator.FILE_SEPARATOR
							+ "eshop/IBEA_Attr2/para4";
					IBEA_Eex.put(paras[1], paras[3]);
					if (runIBEA) 
					{
						Utility.clearAndCreate(paras[3]);
						IBEA_ProductLine_main.main(paras);
					}
					
					paras[1] = "EShop_";
					paras[3] = ibeaResultRootFile.getAbsolutePath()
							+ ResultComparator.FILE_SEPARATOR
							+ "eshop/IBEA_RandAttr/para4";
					IBEA_Eex.put(paras[1], paras[3]);
					if (runIBEA) 
					{
						Utility.clearAndCreate(paras[3]);
						IBEA_ProductLine_main.main(paras);
					}
				}

				paras[1] = "ECos_RAND1";
				paras[3] = ibeaResultRootFile.getAbsolutePath()
						+ ResultComparator.FILE_SEPARATOR
						+ "ecos/IBEA_Attr1/para4";
				IBEA_Eex.put(paras[1], paras[3]);
				if (runIBEA) 
				{
					Utility.clearAndCreate(paras[3]);
					IBEA_ProductLine_main.main(paras);
				}
				
				paras[1] = "ECos_RAND2";
				paras[3] = ibeaResultRootFile.getAbsolutePath()
						+ ResultComparator.FILE_SEPARATOR
						+ "ecos/IBEA_Attr2/para4";
				IBEA_Eex.put(paras[1], paras[3]);
				if (runIBEA) 
				{
					Utility.clearAndCreate(paras[3]);
					IBEA_ProductLine_main.main(paras);
				}
				
				paras[1] = "ECos_";
				paras[3] = ibeaResultRootFile.getAbsolutePath()
						+ ResultComparator.FILE_SEPARATOR
						+ "ecos/IBEA_RandAttr/para4";
				IBEA_Eex.put(paras[1], paras[3]);
				if (runIBEA) 
				{
					Utility.clearAndCreate(paras[3]);
					IBEA_ProductLine_main.main(paras);
				}
				
				paras[1] = "UCLinux_RAND1";
				paras[3] = ibeaResultRootFile.getAbsolutePath()
						+ ResultComparator.FILE_SEPARATOR
						+ "uclinux/IBEA_Attr1/para4";
				IBEA_Eex.put(paras[1], paras[3]);
				if (runIBEA) 
				{
					Utility.clearAndCreate(paras[3]);
					IBEA_ProductLine_main.main(paras);
				}
				
				paras[1] = "UCLinux_RAND2";
				paras[3] = ibeaResultRootFile.getAbsolutePath()
						+ ResultComparator.FILE_SEPARATOR
						+ "uclinux/IBEA_Attr2/para4";
				IBEA_Eex.put(paras[1], paras[3]);
				if (runIBEA) 
				{
					Utility.clearAndCreate(paras[3]);
					IBEA_ProductLine_main.main(paras);
				}
				
				paras[1] = "UCLinux_";
				paras[3] = ibeaResultRootFile.getAbsolutePath()
						+ ResultComparator.FILE_SEPARATOR
						+ "uclinux/IBEA_RandAttr/para4";
				IBEA_Eex.put(paras[1], paras[3]);
				if (runIBEA) 
				{
					Utility.clearAndCreate(paras[3]);
					IBEA_ProductLine_main.main(paras);
				}
		}

		{

			String surfix = "_comp.csv";
			for(String setup: IBDE_Eex.keySet())
			{
				String ibdeResult = IBDE_Eex.get(setup);
				String ibeaResult = IBEA_Eex.get(setup);
				String[] paras = new String[2];
				paras[0]= ibdeResult;
				paras[1]= ibeaResult;
				if (runCOMP) ResultComparator.main(paras, setup+surfix);
			}
		}
	}

}
