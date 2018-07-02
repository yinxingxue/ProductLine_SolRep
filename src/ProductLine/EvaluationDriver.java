package ProductLine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.io.FilenameUtils;

import ProductLine.GAParams.CaseStudy;

import com.thoughtworks.xstream.XStream;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class EvaluationDriver {
	
	static XStream xstream=new XStream();	
	 
	
	 public static void main2(String[] args) throws Exception {
		 EvaluationResult r=new EvaluationResult();
		 r.ArchiveSize=1000;
		 
		
         WriteResult(r,"result.txt");
         EvaluationResult r1=ReadResult("result.txt");
       
	 }
	 
	 public static void WriteResult(EvaluationResult res, String fileName) throws Exception {
		 	
		 
		 FileOutputStream fileOut =new FileOutputStream(fileName);		
         ObjectOutputStream out = xstream.createObjectOutputStream(fileOut);
		
         out.writeObject(res);
         out.close();
         fileOut.close();
	 }
	 public static EvaluationResult ReadResult(String fileName) throws Exception {
		
		 FileInputStream inputStream = new FileInputStream(fileName);
         ObjectInputStream in = xstream.createObjectInputStream(inputStream);
         in.close();
         inputStream.close();       
         return (EvaluationResult)in.readObject();
        
	 }

	 
	enum RunningType{
		Easy,SPLOT,LVAT,LinuxSeed
	}
	static RunningType running=RunningType.SPLOT;
	
	
	
	 public static void main(String[] args) throws Exception {
		 ArrayList<Class> meoa=null;
		 GAParams.P_logSolution=false;
		 
		 GAParams.P_useCached=true;
		
		if(running==RunningType.SPLOT) {
			 
			 GAParams.caseStudies = new ArrayList(
					Arrays.asList(CaseStudy.ChatSystem));
			 GAParams.MaxEvaluation=25000;
			  meoa = new ArrayList(
						Arrays.asList(Class.forName("jmetal.metaheuristics.ibea.IBEA_ProductLine_main")							
								
								));
			 GAParams.P_testExtra=true;
		 }
		 
		 //int[] objLength= new int[]{2,3,4,5};
		 int[] objLength= new int[]{GAParams.objectives.size()};
		 
		 
		 

		  Date date = new Date() ;
			 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss") ;
			 String folderName=("Evaluation_"+dateFormat.format(date)) ;
			 File f=new File(folderName);
			 f.mkdir();
		  
		  
		 //for each objective
		 for(int currObjLength:objLength) {
			 ArrayList<GAParams.Objective> objs=new ArrayList<GAParams.Objective>();
			 for(int i=0;i<currObjLength;i++) {
				 objs.add(GAParams.allObjectives.get(i));
			 }
			 GAParams.objectives=objs;
			 
			 //for each case
			 for (GAParams.CaseStudy currCase: GAParams.caseStudies) {
				 GAParams.currCaseStudy=currCase;
				 if(running==RunningType.LVAT) {
					 if(currCase==CaseStudy.LinuxX86) {
						 GAParams.P_useSeed=false;
						 GAParams.MaxEvaluation=1000;
					 }else {
						 GAParams.MaxEvaluation=100000;
					 }
				 }
				 //foreach algorithm
				 for (Class algo : meoa) {
					
					 GAParams.MutationRate=0.0000001;
					 SetFeedBackDirected(true);
					 GAParams.P_filterMandatory=true;
					 Execute(folderName,currObjLength,algo, currCase);
					 
//					 GAParams.MutationRate=0.01;
//					 SetFeedBackDirected(false);
//					 GAParams.P_filterMandatory=true;
//					 Execute(folderName,currObjLength,algo, currCase);
				 }
			 }
		 }
	 }
	 
	 
	 public static void SetFeedBackDirected(boolean result){
		 GAParams.P_feedbackDirected=result;
		 GAParams.ErrorMutationRate = GAParams.P_feedbackDirected ? 1 : 0.05;
		 GAParams.P_useBestCrossover = GAParams.P_feedbackDirected;
	 }
	 
	 public static void Execute(String folderName, int obj, Class algo, GAParams.CaseStudy currCase)  throws Exception {
		 Execute(folderName, obj , algo, currCase, null,null) ;
	 }
	 public static void Execute(String folderName, int obj, Class algo,  GAParams.CaseStudy currCase,  String index)  throws Exception {
		 Execute(folderName, obj , algo, currCase, index,null) ;
	 }
	 
	 public static void Execute(String folderName, int obj, Class algo, GAParams.CaseStudy currCase, String index, JCommanderArgs jcomm)  throws Exception {
		
		 
		 String caseStudyName=currCase.name();
		 String algoName=algo.getName().split("\\.")[3];
		 algoName=algoName.split("_")[0];
		 
		 String name="";
		 if (!GAParams.P_feedbackDirected && !GAParams.P_filterMandatory) 
			 name="nfd";
		 if (!GAParams.P_feedbackDirected && GAParams.P_filterMandatory) 
			 name="mfd";
		 if (GAParams.P_feedbackDirected && GAParams.P_filterMandatory) 
			 name="fd";
		 String outputFileName=folderName+"/"+algoName+"_"+caseStudyName+"_"+obj+"_"+name;
		 if(index!=null) {
			 outputFileName+="_"+index;
		 }

		 System.out.println("Invoking:" + outputFileName);
		 
		 GAParams.EvaluationResult=new EvaluationResult();
		
		 if(GAParams.P_SeedMode) GAParams.seedStartTime=System.currentTimeMillis();
		 Method m = algo.getDeclaredMethod("main1",
					GAParams.CaseStudy.class);
		  	Object[] arg = new Object[1];
			arg[0] = currCase;
			if (jcomm!=null) {
				GAParams.jCommanderArgs=jcomm;
				//arg[0]=GAParams.CaseStudy.Other;
			}
			m.invoke(null, arg);		
		
			 GAParams.EvaluationResult.objs=GAParams.objectives;
			 GAParams.EvaluationResult.FeedbackDirected=GAParams.P_feedbackDirected;
			 GAParams.EvaluationResult.MaxEvaluation=GAParams.MaxEvaluation;
			 GAParams.EvaluationResult.paramPercentageOfCorrectness=GAParams.P_percentageOfCorrectness;			 
			 GAParams.EvaluationResult.fileName=outputFileName;
			 GAParams.EvaluationResult.NumOfEvaluations=GAParams.MaxEvaluation;
			 GAParams.EvaluationResult.MutationRate=GAParams.MutationRate;
			 GAParams.EvaluationResult.errorMutationRate=GAParams.ErrorMutationRate;
			 GAParams.EvaluationResult.crossOverRate=GAParams.CrossoverRate;
			 GAParams.EvaluationResult.ArchiveSize=GAParams.ArchiveSize;
			 GAParams.EvaluationResult.PopulationSize=GAParams.IBEAPopulationSize;
			 GAParams.EvaluationResult.GenomeSize=GAParams.GenomeSize;
			 GAParams.EvaluationResult.UsedCached=GAParams.P_useCached;
			 GAParams.EvaluationResult.filterMandatory=GAParams.P_filterMandatory;
			 GAParams.EvaluationResult.setInitialPopulation=GAParams.P_setInitialPopulation;
			 GAParams.EvaluationResult.timeTo50PercentCorrectness=GAParams.timeTo50PercentCorrectness;
			 GAParams.EvaluationResult.evaluationTo50PercentCorrectness=GAParams.evaluationTo50PercentCorrectness;
			 
			
			
			 
			 
		WriteResult(GAParams.EvaluationResult,outputFileName);
	 }

}
