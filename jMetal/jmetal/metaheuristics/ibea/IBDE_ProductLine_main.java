//  IBEA_main.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package jmetal.metaheuristics.ibea;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.BinaryTournament;
import jmetal.problems.AProductLine;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.comparators.FitnessComparator;
import ProductLine.AttributeGenerator;
import ProductLine.GAParams;
import ProductLine.MatlabAdapter;
import ProductLine.BestFix.TestDriver;

/**
 * Class for configuring and running the DENSEA algorithm
 */
public class IBDE_ProductLine_main {

//	public static Logger logger_ ; // Logger object
//	public static FileHandler fileHandler_; // FileHandler object		

	public static FileWriter logger_;
	
	public static void main_old(String[] args) throws JMException, IOException,
			ClassNotFoundException, Exception {
		if (args.length == 2 && args[0].trim().equalsIgnoreCase("-i")) {
		    	AttributeGenerator.PREDEFINE_FILE =args[1] ;
				main1(GAParams.CaseStudy.WebPortal);
		}
	    else if (args.length == 2) {
			String path = args[0];
			int times = Integer.parseInt(args[1]);
			File folder = new File(path);
			if (!folder.exists() || !folder.isDirectory())
				throw new IOException();

			for (int i = 1; i <= times; i++) {
				main1(GAParams.CaseStudy.ChatSystem);
				File subFolder = new File(folder.getAbsolutePath()
						+ System.getProperty("file.separator") + i);
				if (!subFolder.exists() || subFolder.isFile())
					subFolder.mkdir();
				copyFile2(new File("FUN"), subFolder, "FUN");
				copyFile2(new File("VAR"), subFolder, "VAR");
				copyFile2(new File("IBEA.log"), subFolder, "IBEA.log");
				copyFile2(new File("ProductLine.log"), subFolder,
						"ProductLine.log");
				// do the copy and paste
			}
		  }  else {
			main1(GAParams.CaseStudy.WebPortal);
		}
	}
	
	public static void main(String[] args) {
		try {

			Options JDUL = new Options();

			JDUL.addOption("h", false, "Print help for JDUL");
			JDUL.addOption("i", true,
					"Please input the predefined attribute file!");
			JDUL.addOption("p", true,
					"Please assign the output directory path!");
			JDUL.addOption("t", true, "Please input the times of executions!");
			JDUL.addOption("set", true,
					"Please choose setup type: 1 for m001_c01, 2 for m005_c02, 3 for m0083_c09!");
			JDUL.addOption("ps", true,
					"Please input the population size for each generation!");

			BasicParser parser = new BasicParser();
			CommandLine cl = parser.parse(JDUL, args);

			if (cl.hasOption('h')) {
				HelpFormatter f = new HelpFormatter();
				f.printHelp("OptionsTip", JDUL);
				System.exit(0);
			} else {
				System.out.println(cl.getOptionValue("i"));
				System.out.println(cl.getOptionValue("p"));
				System.out.println(cl.getOptionValue("t"));
				System.out.println(cl.getOptionValue("set"));
				System.out.println(cl.getOptionValue("ps"));
			}

			GAParams.CaseStudy study = null;
			if (cl.hasOption('i')) {
				AttributeGenerator.PREDEFINE_FILE = cl.getOptionValue("i");
				if(AttributeGenerator.PREDEFINE_FILE.startsWith("ChatSystem_"))
				{
					study = GAParams.CaseStudy.ChatSystem;
					GAParams.MaxEvaluation = 25000;
				}
				else if(AttributeGenerator.PREDEFINE_FILE.startsWith("WebPortal_"))
				{
					study = GAParams.CaseStudy.WebPortal;
					GAParams.MaxEvaluation = 25000;
				}
				else if(AttributeGenerator.PREDEFINE_FILE.startsWith("EShop_"))
				{
					study = GAParams.CaseStudy.EShop;
					GAParams.MaxEvaluation = 25000;
				}
				else if(AttributeGenerator.PREDEFINE_FILE.startsWith("ECos_"))
				{
					study = GAParams.CaseStudy.ECos;
					GAParams.MaxEvaluation = 100000;
				}
				else if(AttributeGenerator.PREDEFINE_FILE.startsWith("UCLinux_"))
				{
					study = GAParams.CaseStudy.UCLinux;
					GAParams.MaxEvaluation = 100000;
				}
				else if(AttributeGenerator.PREDEFINE_FILE.startsWith("LinuxX86_"))
				{
					study = GAParams.CaseStudy.LinuxX86;
					GAParams.P_useSeed=true;
					if(GAParams.P_useSeed && GAParams.P_SeedMode)
					{
						GAParams.P_setInitialPopulation = true;
					}
					GAParams.MaxEvaluation = 100000;
				}
			}
			File folder = null;
			if (cl.hasOption('p')) {
				String path = cl.getOptionValue("p");
				folder = new File(path);
				if (!folder.exists() || !folder.isDirectory())
					throw new IOException();
			}
			int times = 1;
			if (cl.hasOption('t')) {
				times = Integer.parseInt(cl.getOptionValue("t"));
			}

			

			if (cl.hasOption("set")) {
				int type = Integer.parseInt(cl.getOptionValue("set"));
				if (type == 1) {
					GAParams.MutationRate = 0.01;
					GAParams.CrossoverRate = 0.1;
				} else if (type == 2) {
					GAParams.MutationRate = 0.05;
					GAParams.CrossoverRate = 0.2;
				} else if (type == 3) {
					GAParams.MutationRate = 0.083;
					GAParams.CrossoverRate = 0.9;
				}
				 else if (type == 4) {
						GAParams.MutationRate = 0.0000001;
						GAParams.CrossoverRate = 0.1;
				} else if (type == 5) {
					GAParams.MutationRate = 0.01;
					GAParams.CrossoverRate = 0.1;
				}
			}

			if (cl.hasOption("ps")) {
				System.out.println("###############");
				int ps = Integer.parseInt(cl.getOptionValue("ps"));
				runIBEAwithPopulation(folder, ps, times, study);
			} else {
				//runIBEAwithPopulation(folder, 50, times, study);
				runIBEAwithPopulation(folder, 100, times, study);
				//runIBEAwithPopulation(folder, 200, times, study);
				//runIBEAwithPopulation(folder, 400, times, study);
			}

			//if (folder != null)	MatlabAdapter.main(study, folder.getAbsolutePath());
		} catch (IOException io) {
			io.printStackTrace();
			System.exit(-1);
		} catch (ClassNotFoundException io) {
			io.printStackTrace();
			System.exit(-1);
		} catch (JMException io) {
			io.printStackTrace();
			System.exit(-1);
		} catch (Exception io) {
			io.printStackTrace();
			System.exit(-1);
		}
	}

	private static void runIBEAwithPopulation(File folder, int populationSize,
			int times, GAParams.CaseStudy study) throws JMException, IOException,
			ClassNotFoundException, Exception {
	
		GAParams.IBEAPopulationSize = populationSize;
		for (int i = 1; i <= times; i++) {
				/**
				 * very important, if times>1, you have to do the cleaning
				 * remove the "product.log" and other immediate files 
				 */
			    if(folder!=null && i>1) cleaningLogger();
			    main1(study);
				if(folder!=null)
				{
				File psFolder = new File(folder.getAbsolutePath()+ System.getProperty("file.separator")+"ps_"+populationSize);
				if (!psFolder.exists() || psFolder.isFile())
					psFolder.mkdir();
				
				File subFolder = new File(psFolder.getAbsolutePath()+ System.getProperty("file.separator") + i);
				if (!subFolder.exists() || subFolder.isFile())
					subFolder.mkdir();
				copyFile2(new File("FUN"), subFolder, "FUN");
				copyFile2(new File("VAR"), subFolder, "VAR");
				copyFile2(new File("IBDE.log"), subFolder, "IBDE.log");
				copyFile2(new File("ProductLine.log"), subFolder,
						"ProductLine.log");
				// do the copy and paste
				}
		}
	}
	
	  private static void cleaningLogger() {
		  new File("FUN").deleteOnExit();
		  new File("VAR").deleteOnExit();
		  new File("IBDE.log").deleteOnExit();
		  new File("ProductLine.log").deleteOnExit();
	}

	/** 
     * 复制文件(以超快的速度复制文件) 
     *  
     * @param srcFile 
     *            源文件File 
     * @param destDir 
     *            目标目录File 
     * @param newFileName 
     *            新文件名 
     * @return 实际复制的字节数，如果文件、目录不存在、文件为null或者发生IO异常，返回-1 
     */  
    public static long copyFile2(File srcFile, File destDir, String newFileName) {  
        long copySizes = 0;  
        if (!srcFile.exists()) {  
            System.out.println("源文件不存在");  
            copySizes = -1;  
        } else if (!destDir.exists()) {  
            System.out.println("目标目录不存在");  
            copySizes = -1;  
        } else if (newFileName == null) {  
            System.out.println("文件名为null");  
            copySizes = -1;  
        } else {  
            try {  
                FileChannel fcin = new FileInputStream(srcFile).getChannel();  
                FileChannel fcout = new FileOutputStream(new File(destDir,  
                        newFileName)).getChannel();  
                long size = fcin.size();  
                fcin.transferTo(0, fcin.size(), fcout);  
                fcin.close();  
                fcout.close();  
                copySizes = size;  
            } catch (FileNotFoundException e) {  
                e.printStackTrace();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        return copySizes;  
    }  
	/**
	 * @param args
	 *            Command line arguments.
	 * @throws JMException
	 * @throws IOException
	 * @throws SecurityException
	 *             Usage: three choices -
	 *             jmetal.metaheuristics.nsgaII.NSGAII_main -
	 *             jmetal.metaheuristics.nsgaII.NSGAII_main problemName -
	 *             jmetal.metaheuristics.nsgaII.NSGAII_main problemName
	 *             paretoFrontFile
	 */
	//Change 1- Add this
	public static void main1(GAParams.CaseStudy caseStudy) throws JMException, IOException,
			ClassNotFoundException, Exception {
		
		
		TestDriver.constructFM(caseStudy);
	
		GAParams.initialize(TestDriver.NewFeatureModel, TestDriver.OldFeature);
		
		Problem problem; // The problem to solve
		Algorithm algorithm; // The algorithm to use
		Operator crossover; // Crossover operator
		Operator mutation; // Mutation operator
		Operator selection; // Selection operator

		QualityIndicator indicators; // Object to get quality indicators

		HashMap parameters; // Operator parameters
		
		
//		 Logger object and file to store log messages
		 logger_ = new FileWriter("IBDE.log",false);
		 
		//Change 3- Comments this 
		//indicators = new QualityIndicator(problem, paretoFrontFile)
		
		// if (args.length == 1) {
		// Object [] params = {"Real"};
		// problem = (new ProblemFactory()).getProblem(args[0],params);
		// } // if
		// else if (args.length == 2) {
		// Object [] params = {"Real"};
		// problem = (new ProblemFactory()).getProblem(args[0],params);
		// indicators = new QualityIndicator(problem, args[1]) ;
		// } // if
		// else { // Default problem
		// problem = new Kursawe("Real", 3);
		// //problem = new Kursawe("BinaryReal", 3);
		// //problem = new Water("Real");
		// //problem = new ZDT1("ArrayReal", 100);
		// //problem = new ConstrEx("Real");
		// //problem = new DTLZ1("Real");
		// //problem = new OKA2("Real") ;
		// } // else
		
		
		
		//Change 4- Add this
		problem = new AProductLine();
	
		

		algorithm = new IBDE(problem);

		//Change 5- Change below
		// Algorithm parameters
		algorithm.setInputParameter("populationSize", GAParams.IBEAPopulationSize);
		algorithm.setInputParameter("archiveSize",  GAParams.ArchiveSize);
		algorithm.setInputParameter("maxEvaluations", GAParams.MaxEvaluation);
		
		System.out.println("===" + GAParams.MaxEvaluation +"\t" + GAParams.IBEAPopulationSize);

		// Mutation and Crossover for Real codification
		//Change 6- Change below
		parameters = new HashMap();
		// parameters.put("probability", 0.9) ;
		// parameters.put("distributionIndex", 20.0) ;
		crossover = CrossoverFactory.getCrossoverOperator(
				"AProductLineSinglePointCrossover", parameters);

		//Change 7- Change below
		parameters = new HashMap();
		// parameters.put("probability", 1.0/problem.getNumberOfVariables()) ;
		// parameters.put("distributionIndex", 20.0) ;
		mutation = MutationFactory.getMutationOperator(
				"AProductLineBitFlipMutation", parameters);

		/* Selection Operator */
		parameters = new HashMap();
		parameters.put("comparator", new FitnessComparator());
		selection = new BinaryTournament(parameters);

		// Add the operators to the algorithm
		algorithm.addOperator("crossover", crossover);
		algorithm.addOperator("mutation", mutation);
		algorithm.addOperator("selection", selection);

		// Execute the Algorithm
		long initTime = System.currentTimeMillis();
		SolutionSet population = algorithm.execute();
		
		System.out.println("====================="+population.size());
		

		long estimatedTime = System.currentTimeMillis() - initTime;
		
		//Change 8- Add below
		GAParams.logFinalResult(population);
		double executionTime = GAParams.PrintResult(population, estimatedTime);
	
		 logger_.write("Variables values have been writen to file VAR\n");
		 population.printVariablesToFile("VAR");
		 logger_.write("Objectives values have been writen to file FUN\n");
		 population.printObjectivesToFile("FUN");
		 		 
		//Change 9- Comment below 
		 indicators = new QualityIndicator(problem, GAParams.FunPath);

		if (indicators != null) {
			//Change 10- (end) change below 
			System.out.println("Quality indicators");
			System.out.println("Hypervolume: "
					+ indicators.getHypervolume(population));
			System.out.println("GD         : " + indicators.getGD(population));
			System.out.println("IGD        : " + indicators.getIGD(population));
			System.out.println("Spread     : "
					+ indicators.getSpread(population));
			System.out.println("Epsilon    : "
					+ indicators.getEpsilon(population));
			
			GAParams.EvaluationResult.HV=indicators.getHypervolume(population);
			GAParams.EvaluationResult.Spread=indicators.getSpread(population);
			
			GAParams.logResultQuality(population,indicators,executionTime);
		} // if
	
		
	} // main
} // IBEA_main.java
