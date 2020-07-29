# ProductLine_SolRep
1. About the setup of using IP-based methods.
\********very important*********\
find class cplex.tsl.ntu.sg.Cplex_Product_Main, and set the following parameter for choosing which IP-based method. And in our paper, we used mode 0, 2, and 3. 
   public static int CPLEX_SEARCH_MODE = 3; // 0 for epsilon search, 1 for binary search speedup, 2 for CWMOIP, 3 for SolRep 
   For SolRep, we may need to set the number of reference points. It is configured by this variable in class "ncgop.cplex.tsl.ntu.sg.NCGOP"
   public static int N = 1000; // set the number of reference points to 1000.
   

2.  the main class of IP-methods is "cplex.tsl.ntu.sg.Cplex_Product_Main". No matter which mode you choose (CPLEX_SEARCH_MODE = 0, 2, or 3) , the arguments for running it is the same. 
	For example, for the Linux X86, feature attribute set 1, we use the following parameters: "-i" denotes the system model and parameter, "-p" denotes the output folder
	-i LinuxX86_RAND1 -p "C:\Desktop\results\LinuxX86\RAND1"

3. As you can find many files for feature attribute values under this folder, for each model, we use 2 specific feature attribute value sets:
	For ChatSystem, the two specific feature value sets:
	ChatSystem_PREDEFINE_ONE
	ChatSystem_Rand2
	For the rest model, the two specific feature value sets:
	"modelname".RAND1
	"modelname".RAND2
	Besides, you can use feature model name (e.g.,"LinuxX86_") for "-i" option to enable the random generation of feature value at runtime. For example, if you use "-i LinuxX86_ -p C:\Desktop\results\LinuxX86\RAND", then our program will generate a new random feature attribute value set, and use it for the IP-methods. 

4. About the implemenation of each IP-based method: 
 epsilon-constraint is implemented in the class "ProductLine.FeatureModel.SparseInequationMap" its method "cplexSolveFull()"
 CWMOIP is implemented in the class "cplex.tsl.ntu.sg.CWMOIP_Sparse" and the whole class is its implementation. The main processing method is "execute()". 
 SolRep is implemented in the class "ncgop.cplex.tsl.ntu.sg.NCGOP" and and the whole class is its implementation. The main processing method is "execute()".

5. Note that loading feature model is done automatically. The jcs modle and webportal models are hardcoded, the e-shop model is parsed from "Lib\eshop_fm.xml".
 The LVAT models are loaded  from the dimacs format files under folder "data".
 
========================================================================================
The below is the instruction for the tool of IBED and IBEA, from Xue et al. Applied Soft Computing 2016 paper. 
========================================================================================
1. About the setup of the IBED or IBEA 
find class ProductLine.GAParams
For the enhancement techniques:
	public static boolean P_feedbackDirected = true; (meaning the feedback-directed mechanism is enabled, "false" for disabled)
	public static boolean P_filterMandatory = true; (meaning the pre-processing method of feature pruning is enabled, "false" for disabled)
For the seeding method for Linux X86, need to set both of the following fields "true"
	public static boolean P_useSeed=false;
	public static final boolean P_SeedMode = true;

2.  the main class of IBED is "jmetal.metaheuristics.ibea.IBDE_ProductLine_main", and the arguments for running it
-i ChatSystem_  -p "C:\Desktop\temp\ibed\jcs\rand1" -t 30 -set 4  // this is for random feature attribute value, which is generated at runtime randomly
-i ChatSystem_PREDEFINE_ONE -p "C:\Desktop\temp\ibed\jcs\pred" -t 30 -set 4 // use the pre-defined feature attribute value


2.  the main class of IBEA is "jmetal.metaheuristics.ibea.IBEA_ProductLine_main", and the arguments for running it
-i ChatSystem_  -p "C:\Desktop\temp\ibea\jcs\rand1" -t 30 -set 4  // this is for random feature attribute value, which is generated at runtime randomly
-i ChatSystem_PREDEFINE_ONE -p "C:\Desktop\temp\ibea\jcs\pred" -t 30 -set 4 // use the pre-defined feature attribute value


3. You can use feature model name for "-i" option to enable the random generation of feature value at runtime.
As you can find many files for feature attribute values under this folder, for each model, we use 2 specific feature attribute value sets:
For ChatSystem, the two specific feature value sets:
ChatSystem_PREDEFINE_ONE
ChatSystem_Rand2
For the rest model, the two specific feature value sets:
"modelname".RAND1
"modelname".RAND2

4. To compare the specific Pareto domination relationship, we use the following class to summarize the results. 
The main class is "ProductLine.ResultComparator". The arguments will be two output folders:
"C:\Desktop\temp\ibed\jcs\pred" "C:\Desktop\temp\ibea\jcs\pred"
After running, refresh the "results" foler, you will see the rstComp.csv.

5. Note that loading feature model is done automatically. The jcs modle and webportal models are hardcoded, the e-shop model is parsed from "Lib\eshop_fm.xml".
 The LVAT models are loaded  from the dimacs format files under folder "data".
