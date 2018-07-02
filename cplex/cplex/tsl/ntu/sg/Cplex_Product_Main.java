/**
 * 
 */
package cplex.tsl.ntu.sg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.HashMap;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import ProductLine.AttributeGenerator;
import ProductLine.GAParams;
import ProductLine.BestFix.TestDriver;
import ProductLine.FeatureModel.InequationMap;
import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.BinaryTournament;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;
import jmetal.util.comparators.FitnessComparator;

/**
 * @author yinxing
 *
 */
public class Cplex_Product_Main {

	public final static int NUM_OF_SOLS = 1000;// Integer.MAX_VALUE;
	public static int CPLEX_SEARCH_MODE = 3; // 0 for epsilon search, 1 for binary search speedup, 2 for CWMOIP, 3 for SolRep
	public static int CPLEX_SEARCH_SPEEDUP = 128; 
	
	public static FileWriter logger_;

	public static void main(String[] args) {
		try {

			Options JDUL = new Options();

			JDUL.addOption("h", false, "Print help for JDUL");
			JDUL.addOption("i", true, "Please input the predefined attribute file!");
			JDUL.addOption("p", true, "Please assign the output directory path!");
			//JDUL.addOption("set", true, "Please choose setup type: 1 for m001_c01, 2 for m005_c02, 3 for m0083_c09!");
			JDUL.addOption("ss", true, "Please input the required size of the solution!");

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
				System.out.println(cl.getOptionValue("ss"));
			}

			GAParams.CaseStudy study = null;
			if (cl.hasOption('i')) {
				AttributeGenerator.PREDEFINE_FILE = cl.getOptionValue("i");
				if (AttributeGenerator.PREDEFINE_FILE.startsWith("ChatSystem_")) {
					study = GAParams.CaseStudy.ChatSystem;
				} else if (AttributeGenerator.PREDEFINE_FILE.startsWith("WebPortal_")) {
					study = GAParams.CaseStudy.WebPortal;
				} else if (AttributeGenerator.PREDEFINE_FILE.startsWith("EShop_")) {
					study = GAParams.CaseStudy.EShop;
				} else if (AttributeGenerator.PREDEFINE_FILE.startsWith("ECos_")) {
					study = GAParams.CaseStudy.ECos;
				} else if (AttributeGenerator.PREDEFINE_FILE.startsWith("UCLinux_")) {
					study = GAParams.CaseStudy.UCLinux;
				} else if (AttributeGenerator.PREDEFINE_FILE.startsWith("LinuxX86_")) {
					study = GAParams.CaseStudy.LinuxX86;
				}
			}
			File folder = null;
			if (cl.hasOption('p')) {
				String path = cl.getOptionValue("p");
				folder = new File(path);
				if (!folder.exists()){
					folder.mkdirs();
				}
				if (folder.exists()&& !folder.isDirectory())
					throw new IOException();
			}
			int times = 1;
			if (cl.hasOption('t')) {
				times = Integer.parseInt(cl.getOptionValue("t"));
			}

//			if (cl.hasOption("set")) {
//				int type = Integer.parseInt(cl.getOptionValue("set"));
//				if (type == 1) {
//					GAParams.MutationRate = 0.01;
//					GAParams.CrossoverRate = 0.1;
//				} else if (type == 2) {
//					GAParams.MutationRate = 0.05;
//					GAParams.CrossoverRate = 0.2;
//				} else if (type == 3) {
//					GAParams.MutationRate = 0.083;
//					GAParams.CrossoverRate = 0.9;
//				} else if (type == 4) {
//					GAParams.MutationRate = 0.0000001;
//					GAParams.CrossoverRate = 0.1;
//				} else if (type == 5) {
//					GAParams.MutationRate = 0.01;
//					GAParams.CrossoverRate = 0.1;
//				}
//			}

			if (cl.hasOption("ss")) {
				System.out.println("###############");
				int ps = Integer.parseInt(cl.getOptionValue("ss"));
				runCPLEXwithPopulation(folder, ps, times, study);
			} else {
				runCPLEXwithPopulation(folder, 100, times, study);
			}

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

	private static void runCPLEXwithPopulation(File folder, int populationSize, int times, GAParams.CaseStudy study)
			throws JMException, IOException, ClassNotFoundException, Exception {

		//GAParams.IBEAPopulationSize = populationSize;
		for (int i = 1; i <= times; i++) {
			/**
			 * very important, if times>1, you have to do the cleaning remove
			 * the "product.log" and other immediate files
			 */
			if (folder != null && i > 1)
				cleaningLogger();
			main1(study);
			if (folder != null) {
				File psFolder = new File(
						folder.getAbsolutePath() + System.getProperty("file.separator") + "ps_" + populationSize);
				if (!psFolder.exists() || psFolder.isFile())
					psFolder.mkdir();

				File subFolder = new File(psFolder.getAbsolutePath() + System.getProperty("file.separator") + i);
				if (!subFolder.exists() || subFolder.isFile())
					subFolder.mkdir();
				copyFile2(new File("FUN"), subFolder, "FUN");
				copyFile2(new File("VAR"), subFolder, "VAR");
				copyFile2(new File("CPLEX.log"), subFolder, "CPLEX.log");
				copyFile2(new File("ProductLine.log"), subFolder, "ProductLine.log");
				// do the copy and paste
			}
		}
	}

	private static void cleaningLogger() {
		new File("FUN").deleteOnExit();
		new File("VAR").deleteOnExit();
		new File("CPLEX.log").deleteOnExit();
		new File("ProductLine.log").deleteOnExit();
	}

	/**
	 * 锟斤拷锟斤拷锟侥硷拷(锟皆筹拷锟斤拷锟斤拷俣雀锟斤拷锟斤拷募锟�)
	 * 
	 * @param srcFile
	 *            源锟侥硷拷File
	 * @param destDir
	 *            目锟斤拷目录File
	 * @param newFileName
	 *            锟斤拷锟侥硷拷锟斤拷
	 * @return 实锟绞革拷锟狡碉拷锟街斤拷锟斤拷锟斤拷锟斤拷锟斤拷募锟斤拷锟侥柯硷拷锟斤拷锟斤拷凇锟斤拷募锟轿猲ull锟斤拷锟竭凤拷锟斤拷IO锟届常锟斤拷锟斤拷锟斤拷-1
	 */
	public static long copyFile2(File srcFile, File destDir, String newFileName) {
		long copySizes = 0;
		if (!srcFile.exists()) {
			System.out.println("源锟侥硷拷锟斤拷锟斤拷锟斤拷");
			copySizes = -1;
		} else if (!destDir.exists()) {
			System.out.println("目锟斤拷目录锟斤拷锟斤拷锟斤拷");
			copySizes = -1;
		} else if (newFileName == null) {
			System.out.println("锟侥硷拷锟斤拷为null");
			copySizes = -1;
		} else {
			try {
				FileChannel fcin = new FileInputStream(srcFile).getChannel();
				FileChannel fcout = new FileOutputStream(new File(destDir, newFileName)).getChannel();
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

	// Change 1- Add this
	public static void main1(GAParams.CaseStudy caseStudy)
			throws JMException, IOException, ClassNotFoundException, Exception {
		TestDriver.constructFM(caseStudy);
		GAParams.initialize(TestDriver.NewFeatureModel, TestDriver.OldFeature);
		InequationMap inmap = TestDriver.NewFeatureModel.getInEquationMap();

		Problem problem; // The problem to solve
		Algorithm algorithm; // The algorithm to use
		Operator crossover; // Crossover operator
		Operator mutation; // Mutation operator
		Operator selection; // Selection operator
		QualityIndicator indicators; // Object to get quality indicators
		HashMap parameters; // Operator parameters
		
		// Change 4- Add this
		logger_ = new FileWriter("CPLEX.log",false);
		problem = new CplexProblem(TestDriver.NewFeatureModel);
		MyIloCplex cplex = new MyIloCplex(problem);

		System.out.println("===" + GAParams.MaxEvaluation + "\t" + GAParams.IBEAPopulationSize);

		// Logger object and file to store log messages
		logger_ = new FileWriter("CPLEX.log", false);
		// Execute the Algorithm
		long initTime = System.currentTimeMillis();
		
		SolutionSet population = cplex.execute();
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
}
