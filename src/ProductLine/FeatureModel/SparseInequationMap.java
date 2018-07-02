/**
 * 
 */
package ProductLine.FeatureModel;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ProductLine.LogicFormula.And;
import ProductLine.LogicFormula.Equal;
import ProductLine.LogicFormula.Imply;
import ProductLine.LogicFormula.LogicFormula;
import ProductLine.LogicFormula.Not;
import ProductLine.LogicFormula.Or;
import ProductLine.LogicFormula.Prop;
import cplex.tsl.ntu.sg.CWMOIP;
import cplex.tsl.ntu.sg.CWMOIP_Sparse;
import cplex.tsl.ntu.sg.CplexResultComparator;
import cplex.tsl.ntu.sg.CplexSolution;
import cplex.tsl.ntu.sg.Cplex_Product_Main;
import cplex.tsl.ntu.sg.MyIloCplex;
import cplex.tsl.ntu.sg.Utility;
import ilog.concert.IloException;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloNumVarType;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex.UnknownObjectException;
import ncgop.cplex.tsl.ntu.sg.NCGOP;

/**
 * @author yinxing
 *
 */
public class SparseInequationMap extends InequationMap {
	private List<LinkedHashMap<Short, Double>> sparseEquations;
	private List<LinkedHashMap<Short, Double>> sparseAtMostInequations;
   
	public List<LinkedHashMap<Short, Double>> getSparseEquations() {
		return sparseEquations;
	}

	public void setSparseEquations(List<LinkedHashMap<Short, Double>> sparseEquations) {
		this.sparseEquations = sparseEquations;
	}

	public List<LinkedHashMap<Short, Double>> getSparseAtMostInequations() {
		return sparseAtMostInequations;
	}

	public void setSparseAtMostInequations(List<LinkedHashMap<Short, Double>> sparseAtMostInequations) {
		this.sparseAtMostInequations = sparseAtMostInequations;
	}

	public SparseInequationMap() {
		vari2Feature = new LinkedHashMap<Integer, String>();
		feature2Vari = new LinkedHashMap<String, Integer>();
		setSparseEquations(new LinkedList<LinkedHashMap<Short, Double>>());
		setSparseAtMostInequations(new LinkedList<LinkedHashMap<Short, Double>>());
	}

	public SparseInequationMap(HashMap<Integer, String> integerToAllFeatureMap) {
		vari2Feature = new LinkedHashMap<Integer, String>();
		feature2Vari = new LinkedHashMap<String, Integer>();
		setSparseEquations(new LinkedList<LinkedHashMap<Short, Double>>());
		setSparseAtMostInequations(new LinkedList<LinkedHashMap<Short, Double>>());

		for (Integer inter : integerToAllFeatureMap.keySet()) {
			vari2Feature.put(inter, integerToAllFeatureMap.get(inter));
			feature2Vari.put(integerToAllFeatureMap.get(inter), inter);
		}
	}

	public void add2Equation(Equal equal) {
		LogicFormula left = equal.EqlA;
		LogicFormula right = equal.EqlB;

		try {
			// And
			// ((Output)<=>Chat)
			if (left instanceof Prop) {
				String subFea = ((Prop) left).varname;
				if (right instanceof Prop) {
					String parentFea = ((Prop) right).varname;
					LinkedHashMap<Short, Double> equation = new LinkedHashMap<Short, Double>();
					equation.put(feature2Vari.get(subFea).shortValue(), 1.0);
					equation.put(feature2Vari.get(parentFea).shortValue(), -1.0);
					equation.put((short) feature2Vari.size(), 0.0);
					//Double[] equation = new Double[feature2Vari.size() + 1];
					//equation[feature2Vari.get(subFea)] = 1.0;
					//equation[feature2Vari.get(parentFea)] = -1.0;
					//equation[equation.length - 1] = 0.0;
					this.sparseEquations.add(equation);
				} else if (right instanceof Or) {
					Equal newFormular = new Equal(right, left);
					add2Equation(newFormular);
				}
			}
			// multiple And
			else if (left instanceof And) {
				ArrayList<LogicFormula> parameters = ((And) left).conjunct;
				for (LogicFormula parameter : parameters) {
					String subFea = ((Prop) parameter).varname;
					String parentFea = ((Prop) right).varname;
					LinkedHashMap<Short, Double> equation = new LinkedHashMap<Short, Double>();
					equation.put(feature2Vari.get(subFea).shortValue(), 1.0);
					equation.put(feature2Vari.get(parentFea).shortValue(), -1.0);
					equation.put((short) feature2Vari.size(), 0.0);
					//Double[] equation = new Double[feature2Vari.size() + 1];
					//equation[feature2Vari.get(subFea)] = 1.0;
					//equation[feature2Vari.get(parentFea)] = -1.0;
					//equation[equation.length - 1] = 0.0;
					this.sparseEquations.add(equation);
				}
			}
			// Or
			// ((Caesar||Reverse)<=>Enrcyption)
			else if (left instanceof Or) {
				ArrayList<LogicFormula> parameters = ((Or) left).disjunct;
				for (LogicFormula parameter : parameters) {
					String subFea = ((Prop) parameter).varname;
					String parentFea = ((Prop) right).varname;
					LinkedHashMap<Short, Double> inEquation = new LinkedHashMap<Short, Double>();
					inEquation.put(feature2Vari.get(subFea).shortValue(), 1.0);
					inEquation.put(feature2Vari.get(parentFea).shortValue(), -1.0);
					inEquation.put((short) feature2Vari.size(), 0.0);
					//Double[] inEquation = new Double[feature2Vari.size() + 1];
					//inEquation[feature2Vari.get(subFea)] = 1.0;
					//inEquation[feature2Vari.get(parentFea)] = -1.0;
					//inEquation[inEquation.length - 1] = 0.0;
					this.sparseAtMostInequations.add(inEquation);
				}
				//Double[] orInEquation = new Double[feature2Vari.size() + 1];
				LinkedHashMap<Short, Double> orInEquation = new LinkedHashMap<Short, Double>();
				String parentFea = ((Prop) right).varname;
				for (LogicFormula parameter : parameters) {
					String subFea = ((Prop) parameter).varname;
					//orInEquation[feature2Vari.get(subFea)] = -1.0;
					orInEquation.put(feature2Vari.get(subFea).shortValue(), -1.0);
				}
				orInEquation.put(feature2Vari.get(parentFea).shortValue(), 1.0);
				orInEquation.put((short) feature2Vari.size(), 0.0);
				//orInEquation[feature2Vari.get(parentFea)] = 1.0;
				//orInEquation[orInEquation.length - 1] = 0.0;
				this.sparseAtMostInequations.add(orInEquation);
			} else {
				throw new Exception("Unknown format in Equal formular!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void add2Equation(Prop prop) {
		String root = prop.varname;
//		Double[] equation = new Double[feature2Vari.size() + 1];
//		equation[feature2Vari.get(root)] = 1.0;
//		equation[equation.length - 1] = 1.0;
		LinkedHashMap<Short, Double> equation = new LinkedHashMap<Short, Double>();
		equation.put(feature2Vari.get(root).shortValue(), 1.0);
		equation.put((short) feature2Vari.size(), 1.0);
		this.sparseEquations.add(equation);
	}

	public void add2Equation(Not not) {
		LogicFormula notinFea = not.notFormula;
		Prop feature = (Prop)notinFea;
//		Double[] equation = new Double[feature2Vari.size() + 1];
//		equation[feature2Vari.get(feature.varname)] = 1.0;
//		equation[equation.length - 1] = 0.0;
		LinkedHashMap<Short, Double> equation = new LinkedHashMap<Short, Double>();
		equation.put(feature2Vari.get(feature.varname).shortValue(), 1.0);
		equation.put((short) feature2Vari.size(), 0.0);
		this.sparseEquations.add(equation);
	}
	
	public boolean add2AtLeastInequations(LogicFormula formula) {
		boolean freeFormula = false;
		try {
			// imply
			// Logging=>Chat
			if (formula instanceof Imply) {
				LogicFormula left = ((Imply) formula).ImpA;
				LogicFormula right = ((Imply) formula).ImpB;
				String subFea = ((Prop) left).varname;
				String parentFea = ((Prop) right).varname;
//				Double[] inEquation = new Double[feature2Vari.size() + 1];
//				inEquation[feature2Vari.get(subFea)] = 1.0;
//				inEquation[feature2Vari.get(parentFea)] = -1.0;
//				inEquation[inEquation.length - 1] = 0.0;
				LinkedHashMap<Short, Double> inEquation = new LinkedHashMap<Short, Double>();
				inEquation.put(feature2Vari.get(subFea).shortValue(), 1.0);
				inEquation.put(feature2Vari.get(parentFea).shortValue(), -1.0);
				inEquation.put((short) feature2Vari.size(), 0.0);
				this.sparseAtMostInequations.add(inEquation);
			}
			// XOR
			// (((GUI||CMD||GUI2)<=>Output)&&!(GUI&&CMD)&&!(GUI&&GUI2)&&!(CMD&&GUI2))
			else if (formula instanceof And) {
				ArrayList<LogicFormula> formular = ((And) formula).conjunct;
				for (LogicFormula lf : formular) {
					if (lf instanceof Not) {
						LogicFormula content = ((Not) lf).notFormula;

						if (content instanceof And) {
							//Double[] xorInEquation = new Double[feature2Vari.size() + 1];
							LinkedHashMap<Short, Double> xorInEquation = new LinkedHashMap<Short, Double>();
							for (LogicFormula parameter : ((And) content).conjunct) {
								String subFea = ((Prop) parameter).varname;
								xorInEquation.put(feature2Vari.get(subFea).shortValue(), 1.0);
								//xorInEquation[feature2Vari.get(subFea)] = 1.0;
							}
							xorInEquation.put((short) feature2Vari.size(), 1.0);
							//xorInEquation[xorInEquation.length - 1] = 1.0;
							this.sparseAtMostInequations.add(xorInEquation);
						} else {
							throw new Exception("Unknown format in XOR&Not formular!");
						}
					}

					else if (lf instanceof Equal) {
						add2Equation((Equal) lf);
					} else {
						throw new Exception("Unknown format in XOR formular!");
					}
				}
			}
			
			else if (formula instanceof Or) {
				Or orFormular = (Or) formula;
				//Double[] inEquation = new Double[feature2Vari.size() + 1];
				LinkedHashMap<Short, Double> inEquation = new LinkedHashMap<Short, Double>();
//				if( orFormular.disjunct.size()==2)extracTwoCNF(orFormular, inEquation);
//				else 
//					extracSeveralCNF(orFormular, inEquation);
				Set<String> skipFeas = extracSeveralCNF(orFormular, inEquation);
				if(skipFeas.size()>0) 
				{
					freeFormula = true;
				}
				if(!freeFormula) this.sparseAtMostInequations.add(inEquation);
			}
			else if (formula instanceof Not) {
				Not nfm = (Not)formula;
				if(!(nfm.notFormula instanceof And))
				{
					throw new Exception("Unknown LogicFormula format in Not inequality conversion!");
				}
				And andFormular = (And) nfm.notFormula;
				LinkedHashMap<Short, Double> inEquation = new LinkedHashMap<Short, Double>();
				//Double[] inEquation = new Double[feature2Vari.size() + 1];
				ArrayList<ProductLine.LogicFormula.LogicFormula> conjunct = andFormular.conjunct;
				for (LogicFormula para : conjunct) {
					if (para instanceof Not) {
						Not not = (Not) para;
						String subFea = ((Prop) not.notFormula).varname;
						inEquation.put(feature2Vari.get(subFea).shortValue(), -1.0);
						//inEquation[feature2Vari.get(subFea)] = -1.0;
					} else if (para instanceof Prop) {
						String parentFea = ((Prop) para).varname;
						inEquation.put(feature2Vari.get(parentFea).shortValue(), 1.0);
						//inEquation[feature2Vari.get(parentFea)] =  1.0;
					} else {
						throw new Exception("Unknown LogicFormula format for inequality conversion!");
					}
				}
				inEquation.put((short) feature2Vari.size(), 1.0);
				//inEquation[inEquation.length - 1] = 1.0;
				this.sparseAtMostInequations.add(inEquation);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return freeFormula;
	}

	private Set<String> extracSeveralCNF(Or orFormular, LinkedHashMap<Short, Double> inEquation) throws Exception {
		// TODO Auto-generated method stub
		//for constraint in EShop and LVAT: 
		//(!CYGPKG_HAL_COMMON||!CYGBLD_HAL_LINKER_GROUPED_LIBS||CYGBLD_HAL_LINKER_GROUPED_LIBS) >= 1
		ArrayList<ProductLine.LogicFormula.LogicFormula> disjunct = orFormular.disjunct;
		Set<String> skipFeature = new LinkedHashSet<String>();
		Set<String> notFeatures = new LinkedHashSet<String>();
		Set<String> yesFeatures = new LinkedHashSet<String>();
		int notCounter =0;
		for (LogicFormula para : disjunct) {
			if (para instanceof Not) {
				Not not = (Not) para;
				String subFea = ((Prop) not.notFormula).varname;
				if(skipFeature.contains(subFea))
				{
					continue;
				}
				if(notFeatures.contains(subFea))
				{
					continue;
				}
				notFeatures.add(subFea);
				short feaKey = feature2Vari.get(subFea).shortValue();
				if(inEquation.containsKey(feaKey)&& inEquation.get(feaKey) == -1.0)
				{
					inEquation.remove(feaKey);
					skipFeature.add(subFea);
					notCounter++;
					continue;
				}
				inEquation.put(feature2Vari.get(subFea).shortValue(), 1.0);
				notCounter++;
				//inEquation[feature2Vari.get(subFea)] = 1.0;
			} else if (para instanceof Prop) {
				String parentFea = ((Prop) para).varname;
				if(skipFeature.contains(parentFea))
				{
					continue;
				}
				if(yesFeatures.contains(parentFea))
				{
					continue;
				}
				yesFeatures.add(parentFea);
				short feaKey = feature2Vari.get(parentFea).shortValue();
				if(inEquation.containsKey(feaKey)&& inEquation.get(feaKey) == 1.0)
				{
					inEquation.remove(feaKey);
					skipFeature.add(parentFea);
					continue;
				}
				inEquation.put(feature2Vari.get(parentFea).shortValue(), -1.0);		
				//inEquation[feature2Vari.get(parentFea)] = -1.0;
			} else {
				throw new Exception("Unknown LogicFormula format for inequality conversion!");
			}
		}
 
		notFeatures.retainAll(yesFeatures);
		if(notFeatures.size() != skipFeature.size())
		{
			System.out.println("");
		}
		assert notFeatures.size() == skipFeature.size();
		inEquation.put((short) feature2Vari.size(), notCounter-1.0);
		return skipFeature;
	}

	private void extracTwoCNF(Or orFormular, LinkedHashMap<Short, Double> inEquation) throws Exception {
		// imply in EShop.xml
		// !Wish_list||Wish_list_save_after_session
		// 1-Wish_list+Wish_list_save_after_session>=1 , so
		// Wish_list-Wish_list_save_after_session<=0
		ArrayList<ProductLine.LogicFormula.LogicFormula> disjunct = orFormular.disjunct;
		for (LogicFormula para : disjunct) {
			if (para instanceof Not) {
				Not not = (Not) para;
				String subFea = ((Prop) not.notFormula).varname;
				inEquation.put(feature2Vari.get(subFea).shortValue(), 1.0);
				//inEquation[feature2Vari.get(subFea)] = 1.0;
			} else if (para instanceof Prop) {
				String parentFea = ((Prop) para).varname;
				inEquation.put(feature2Vari.get(parentFea).shortValue(), -1.0);
				//inEquation[feature2Vari.get(parentFea)] = -1.0;
			} else {
				throw new Exception("Unknown LogicFormula format for inequality conversion!");
			}
		}
		//inEquation[inEquation.length - 1] = 0.0;
		inEquation.put((short) feature2Vari.size(), 0.0);
	}

	public void print() {
		for (LinkedHashMap<Short, Double> inEquation  : this.sparseEquations) {
			String equal = "";
			for (Short key: inEquation.keySet()) {
//				if (inEquation.get(key) == null)
//					continue;
				if(key == this.vari2Feature.size())
				{
					equal += "=" + this.vari2Feature.get(key) ;
					continue;
				}
				String name = this.vari2Feature.get(key);			
				if (inEquation.get(key) == 1) {
					equal += "+" + name;
				} else if (inEquation.get(key)  == -1) {
					equal += "-" + name;
				}
				 
			}
			//equal += "=" + array[array.length - 1];
			System.out.println(equal);
		}

		for (LinkedHashMap<Short, Double> inEquation : this.sparseAtMostInequations) {
			String equal = "";
			//for (int i = 0; i < array.length - 1; i++) {
			for (Short key: inEquation.keySet()) {
				if(key == this.vari2Feature.size())
				{
					equal += "<=" + this.vari2Feature.get(key) ;
					continue;
				}
//				if (array[i] == null)
//					continue;
				String name = this.vari2Feature.get(key);
				if (inEquation.get(key) == 1) {
					equal += "+" + name;
				} else if (inEquation.get(key) == -1) {
					equal += "-" + name;
				}
			}
			//equal += "<=" + array[array.length - 1];
			System.out.println(equal);
		}

		System.out.println("Target function:");
		System.out.println("--Max variety:");
		for (int i = 0; i < this.varietyEffiecient.length; i++) {
			System.out.print(varietyEffiecient[i] + "*" + this.vari2Feature.get(i));
		}
		System.out.println();
		System.out.println("--Min cost:");
		for (int i = 0; i < this.costEffiecient.length; i++) {
			if (this.costEffiecient[i] > 0)
				System.out.print("+");
			System.out.print(this.costEffiecient[i] + "*" + this.vari2Feature.get(i));
		}
		System.out.println();
		System.out.println("--Min defect:");
		for (int i = 0; i < this.defectEffiecient.length; i++) {
			if (this.defectEffiecient[i] > 0)
				System.out.print("+");
			System.out.print(this.defectEffiecient[i] + "*" + this.vari2Feature.get(i));
		}
		System.out.println();
		System.out.println("--Min usedBefore:");
		for (int i = 0; i < this.usedbeforeEffiecient.length; i++) {
			if (this.usedbeforeEffiecient[i] > 0)
				System.out.print("+");
			if (usedbeforeEffiecient[i] != 0)
				System.out.print(this.usedbeforeEffiecient[i] + "*" + this.vari2Feature.get(i));
		}
	}

	public void addTargetInequations(HashMap<Integer, Double> featuresToCostMap,
			HashMap<Integer, Integer> featuresToDefectsMap, HashMap<Integer, Boolean> featuresToNotUseBeforeMap) {
		this.varietyEffiecient = new Double[feature2Vari.size()];
		this.costEffiecient = new Double[feature2Vari.size()];
		this.defectEffiecient = new Double[feature2Vari.size()];
		this.usedbeforeEffiecient = new Double[feature2Vari.size()];

		for (int i = 0; i < feature2Vari.size(); i++) {
			this.varietyEffiecient[i] = -1.0;
			costEffiecient[i] = featuresToCostMap.get(i);
			defectEffiecient[i] = featuresToDefectsMap.get(i) + 0.0;
			if (featuresToNotUseBeforeMap.get(i))
				usedbeforeEffiecient[i] = 1.0;
			else
				usedbeforeEffiecient[i] = 0.0;
		}
	}

	public void printMatlabFormat() {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter("matlab.txt");
			String s = convertToMatrix();
			fileWriter.write(s);
			fileWriter.close();

			fileWriter = new FileWriter("parameter.txt");
			String s2 = convertToTxt();
			fileWriter.write(s2);
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String convertToMatrix() {
		String content = "";
		String t1 = "f_1 = [";
		String t2 = "f_2 = [";
		String t3 = "f_3 = [";
		String t4 = "f_4 = [";

		for (int i = 0; i < feature2Vari.size(); i++) {
			t1 += this.varietyEffiecient[i];
			t2 += this.costEffiecient[i];
			t3 += this.defectEffiecient[i];
			t4 += this.usedbeforeEffiecient[i];
			if (i != feature2Vari.size() - 1) {
				t1 += ";";
				t2 += ";";
				t3 += ";";
				t4 += ";";
			}
		}
		t1 += "]; % the target functions are in the order of variety, costs, defects, not used before \n";
		t2 += "];\n";
		t3 += "];\n";
		t4 += "];\n";
		content = content + t1 + t2 + t3 + t4;

		String A = "A = [";
		int count =0;
		for (LinkedHashMap<Short, Double> array: this.sparseAtMostInequations) {
			//Double[] array = this.atMostInequations.get(j);
			String inEqual = "";
			for (short i = 0; i < feature2Vari.size(); i++) {
				if (!array.containsKey(i))
					inEqual += " 0";
				else
					inEqual += " " + array.get(i) ;
				if (i == feature2Vari.size() -1  && count != this.sparseAtMostInequations.size() - 1) {
					inEqual += ";\n";
				}
			}
			count++;
			A += inEqual;
		}
		A += "]";
		content = content + A + "\n";

		String b = "b = [";
		count =0;
		for (LinkedHashMap<Short, Double> array: this.sparseAtMostInequations) {
			//Double[] array = this.atMostInequations.get(j);
			String inEqual = "";
			if (!array.containsKey((short)feature2Vari.size()))
				inEqual += " 0";
			else
				inEqual += " " + array.get((short)feature2Vari.size());
			if (count != this.sparseAtMostInequations.size() - 1) {
				inEqual += ";";
			}
			count++;
			b += inEqual;
		}
		b += "]";
		content = content + b + "\n";

		String Aeq = "Aeq = [";
		count =0;
		for (LinkedHashMap<Short, Double> array: this.sparseEquations) {
			//Double[] array = this.equations.get(j);
			String inEqual = "";
			//for (int i = 0; i < array.length - 1; i++) {
			for (short i = 0; i < feature2Vari.size(); i++) {
				if (!array.containsKey(i))
					inEqual += " 0";
				else
					inEqual += " " + array.get(i);
				if (i == feature2Vari.size() -1  && count != this.sparseEquations.size() - 1) {
					inEqual += ";\n";
				}
			}
			count++;
			Aeq += inEqual;
		}
		Aeq += "]";
		content = content + Aeq + "\n";

		String beq = "beq = [";
		count =0;
		for (LinkedHashMap<Short, Double> array: this.sparseEquations) {
			//Double[] array = this.sparseEquations.get(j);
			String inEqual = "";
			if (!array.containsKey((short)feature2Vari.size()))
				inEqual += " 0";
			else
				inEqual += " " + array.get((short)feature2Vari.size());
			if (count!= this.sparseEquations.size() - 1) {
				inEqual += ";";
			}
			count++;
			beq += inEqual;
		}
		beq += "]";
		content = content + beq + "\n";

		String formular = "x = bintprog(f,A,b,Aeq,beq); \n x";
		content = content + formular + "\n";
		return content;
	}

	private String convertToTxt() {
		String content = "";
		String objectNames = "objectives ==\n"+"variety; costs; defects; notUsedBefore\n";
		content += objectNames;
		
		String t1 = "[";
		String t2 = "[";
		String t3 = "[";
		String t4 = "[";

		for (int i = 0; i < feature2Vari.size(); i++) {
			t1 += this.varietyEffiecient[i];
			t2 += this.costEffiecient[i];
			t3 += this.defectEffiecient[i];
			t4 += this.usedbeforeEffiecient[i];
			if (i != feature2Vari.size() - 1) {
				t1 += ";";
				t2 += ";";
				t3 += ";";
				t4 += ";";
			}
		}
		t1 += "]\n";
		t2 += "]\n";
		t3 += "]\n";
		t4 += "]\n";
		content = content + t1 + t2 + t3 + t4+"\n";

		String featureNames = "variables ==\n" + feature2Vari +"\n";
		content = content+featureNames+"\n";
		
		String A = "Inequations ==\n";
		String b = this.sparseAtMostInequations.toString();
		content = content + A+ b + "\n\n";
 
		String Aeq = "Equations ==\n";
		String beq = this.sparseEquations.toString();
		content = content + Aeq+ beq + "\n";
		return content;
	}
	
//	private void convertToMatrix(Double[][] f, Byte [][] a, Integer[] b,
//			Byte[][] a_eq, Integer[] b_eq) {
//		// TODO Auto-generated method stub
//
//		for (int i = 0; i < feature2Vari.size(); i++) {
//			f[0][i] = this.costEffiecient[i];
//			f[1][i] = this.varietyEffiecient[i];
//			f[2][i] = this.usedbeforeEffiecient[i];
//			f[3][i] = this.defectEffiecient[i]; 
//		}
// 
//		for (int j = 0; j <= this.atMostInequations.size() - 1; j++) {
//			Double[] array = this.atMostInequations.get(j);
//			for (int i = 0; i < array.length - 1; i++) {
//			
//				if (array[i] == null)
//					a[j][i]  =  0;
//				else
//				{
//					if(array[i] == 0.0)
//					{
//						a[j][i]  =  0;
//					}
//					else
//					{	a[j][i]  =  (byte) Math.round(array[i]) ;}
//				};
//			}
//		}
//	 
//		for (int j = 0; j <= this.atMostInequations.size() - 1; j++) {
//			Double[] array = this.atMostInequations.get(j);
//			if (array[array.length - 1] == null)
//				b[j] = 0;
//			else
//			    b[j] = (int) Math.round(array[array.length - 1]);
//		}
//	
//		for (int j = 0; j <= this.equations.size() - 1; j++) {
//			Double[] array = this.equations.get(j);
//			for (int i = 0; i < array.length - 1; i++) {
//				if (array[i] == null)
//					a_eq[j][i]= 0;
//				else
//					if(array[i] == 0.0)
//					{
//						a_eq[j][i]  =   0;
//					}
//					else
//					{	a_eq[j][i]  =  (byte) Math.round(array[i]) ;}
//			}
//		}
//  
//		for (int j = 0; j <= this.equations.size() - 1; j++) {
//			Double[] array = this.equations.get(j);
//		 
//			if (array[array.length - 1] == null)
//				b_eq [j] = 0;
//			else
//				b_eq [j] =  (int) Math.round(array[array.length - 1]);
//		}
//	}
//	
	public Map<String, CplexSolution> convertToCplex(MyIloCplex cplex) throws Exception {
		try {
 			Double[][] F= new Double[4][this.feature2Vari.size()];
//			Byte[][] A= new Byte[this.atMostInequations.size()][this.feature2Vari.size()];
//			Integer[] B= new Integer[this.atMostInequations.size()];
//			Byte[][] A_eq= new Byte[this.equations.size()][this.feature2Vari.size()];
//			Integer[] B_eq= new Integer[this.equations.size()];
            convertToTargetMatrix(F );
			 
			Map<String,CplexSolution> sols =  new LinkedHashMap<String,CplexSolution>();

		   if(Cplex_Product_Main.CPLEX_SEARCH_MODE == 0||Cplex_Product_Main.CPLEX_SEARCH_MODE == 1)	naiveSolving(cplex, F, sols);
			else if(Cplex_Product_Main.CPLEX_SEARCH_MODE == 2)
			{
				CWMOIP_Sparse ipExecutor= new CWMOIP_Sparse(cplex, 4,this.feature2Vari.size());
				//ipExecutor.setInequationMap(this);
				ipExecutor.setParas(F,this.sparseEquations, this.sparseAtMostInequations);
				ipExecutor.execute(ipExecutor.getExtra_A(),ipExecutor.getExtra_B(),ipExecutor.getF(),ipExecutor.getF(), 4, ipExecutor.getE_out() ,sols);
			}
			else if(Cplex_Product_Main.CPLEX_SEARCH_MODE == 3)
			{
				NCGOP ipExecutor= new NCGOP(cplex, 4,this.feature2Vari.size());
				ipExecutor.setParas(F,this.sparseEquations, this.sparseAtMostInequations);
				ipExecutor.execute(ipExecutor.getExtra_A(),ipExecutor.getExtra_B(),ipExecutor.getF(),ipExecutor.getF(), 4, ipExecutor.getE_out() ,sols);
			}
			System.out.println(sols);
			System.out.println("Inner Solving times: "+CWMOIP.EXE_TIME);
			System.out.println("The raw results of sols size: "+ sols.size()+"  content:"+sols.keySet());
			
			//Set<String> nonDominated = ResultComparator.findDominantSol(sols.keySet());
			//System.out.println(nonDominated.size());
			//System.out.println(nonDominated);
			//sols.keySet().retainAll(nonDominated);
			return sols;
		} catch (IloException e) {
			System.err.println("Concert exception caught: " + e);
		}
		return null;
	}

	private void convertToTargetMatrix(Double[][] f) {
		for (int i = 0; i < feature2Vari.size(); i++) {
			f[0][i] = this.costEffiecient[i];
			f[1][i] = this.varietyEffiecient[i];
			f[2][i] = this.usedbeforeEffiecient[i];
			f[3][i] = this.defectEffiecient[i];
		}
	}

	protected void naiveSolving(MyIloCplex cplex, Double[][] F, Map<String, CplexSolution> sols) throws IloException,
			UnknownObjectException {
		//the searching scope;
		int omittedBits = 0;
		FeatureModel fm = cplex.getNewFeatureModel() ;
		// the mustin feature cost
		double mustCost = 0;
		if(fm instanceof LogicFeatureModel)
		{
			LogicFeatureModel lfm = (LogicFeatureModel)fm;
			omittedBits = lfm.getMandatoryNames().size()+ lfm.getMustNotInNames().size();
			for(String mustFea:  lfm.getMandatoryNames())
			{
				int feaIdx =this.feature2Vari.get(mustFea);
				mustCost+= this.costEffiecient[feaIdx];
			}
		}
		
		IloNumVar[] x = cplex.numVarArray(this.feature2Vari.size(), 0, 1, IloNumVarType.Int);

		// Cost as the targets
		IloNumExpr[] objExp = new IloNumExpr[x.length];
		for (int i = 0; i < x.length; i++) {
			objExp[i] = cplex.prod(this.costEffiecient[i], x[i]);
		}
		IloNumExpr expr = cplex.sum(objExp);
		cplex.addMinimize(expr);

		// add the normal constraints
		List<IloNumExpr> inEqualconsts = new ArrayList<IloNumExpr>();
		for (LinkedHashMap<Short, Double> array: this.sparseAtMostInequations) {
			//Double[] array = this.atMostInequations.get(j);
			List<IloNumExpr> inEqual = new ArrayList<IloNumExpr>();
			for (Short key: array.keySet()) {
				    if(key == this.feature2Vari.size()) continue;
					IloNumExpr itme = cplex.prod(array.get(key), x[key]);
					inEqual.add(itme);
			}
			IloNumExpr itemsum = cplex.sum((IloNumExpr[]) inEqual.toArray(new IloNumExpr[0]));
			inEqualconsts.add(itemsum);
			cplex.addLe(itemsum, array.get((short)this.feature2Vari.size()));
		}

		// add the normal constraints
		List<IloNumExpr> equalconsts = new ArrayList<IloNumExpr>();
		for (LinkedHashMap<Short, Double> array: this.sparseEquations) {
			//Double[] array = this.equations.get(j);
			List<IloNumExpr> equal = new ArrayList<IloNumExpr>();
			for (Short key: array.keySet()) {
			        if(key == this.feature2Vari.size()) continue;
					IloNumExpr itme = cplex.prod(array.get(key), x[key]);
					equal.add(itme);
			}
			IloNumExpr itemsum = cplex.sum((IloNumExpr[]) equal.toArray(new IloNumExpr[0]));
			equalconsts.add(itemsum);
			cplex.addEq(itemsum, array.get((short)this.feature2Vari.size()));
		}

		int notUsedFeaSize =(int) Math.ceil(Utility.arraySum(this.usedbeforeEffiecient));
		int totalDefect =(int) Math.ceil(Utility.arraySum(this.defectEffiecient));
		

//			ilog.cplex.IloCplex.IntParam para1 = new IntParam();
//			ilog.cplex.IloCplex.IntParam para2 = new IntParam();
//			ilog.cplex.IloCplex.DoubleParam para3 = new DoubleParam();
		
		// feature size as the constrains
		IloNumExpr[] objContExp = new IloNumExpr[x.length];
		for (int i = 0; i < x.length; i++) {
			objContExp[i] = cplex.prod(this.varietyEffiecient[i], x[i]);
		}
		IloNumExpr temp = cplex.sum(objContExp);
		IloRange cont1 = cplex.addLe(temp,0);
 
		
		// not used feature size as the constrains
		IloNumExpr[] objContExp2 = new IloNumExpr[x.length];
		for (int i = 0; i < x.length; i++) {
			objContExp2[i] = cplex.prod(this.usedbeforeEffiecient[i], x[i]);
		}
		IloNumExpr temp2 = cplex.sum(objContExp2);
		IloRange cont2 = cplex.addLe(temp2,notUsedFeaSize);
		
		// defects as the constrains
		IloNumExpr[] objContExp3 = new IloNumExpr[x.length];
		for (int i = 0; i < x.length; i++) {
			objContExp3[i] = cplex.prod(this.defectEffiecient[i], x[i]);
		}
		IloNumExpr temp3 = cplex.sum(objContExp3);
		IloRange cont3 = cplex.addLe(temp3,totalDefect);
		 
		/**
		 * p is feature size 
		 * q is not used feature size defect coefficient
		 * sum
		 *
		 */
		cplex.setOut(null);
		cplex.setWarning(null);
		if(Cplex_Product_Main.CPLEX_SEARCH_MODE == 0)
		cplexSolveFull(cplex, sols, omittedBits, mustCost, x, notUsedFeaSize,
				totalDefect, cont1, cont2, cont3);
		else if(Cplex_Product_Main.CPLEX_SEARCH_MODE == 1)
		{
		 cplexSolve(cplex, sols, omittedBits, mustCost, x, notUsedFeaSize,
						totalDefect, cont1, cont2, cont3);
		}
	}

	public void cplexSolveFull(MyIloCplex cplex,
			Map<String, CplexSolution> sols, int omittedBits, double mustCost,
			IloNumVar[] x, int notUsedFeaSize, int totalDefect, IloRange cont1,
			IloRange cont2, IloRange cont3) throws IloException {
		 for (int p = x.length; p >= 0; p--) {
			if(sols.size()==Cplex_Product_Main.NUM_OF_SOLS) break;
			for (int q = notUsedFeaSize; q >= 0; q--) {
				if(sols.size()==Cplex_Product_Main.NUM_OF_SOLS) break;
				for (int t = totalDefect; t >= 0; t--) {
					cont1.setUB(-1.0*p); //TO-To TEST cont1.setUB(-1.0*p)
					cont1.setLB(-1.0*x.length);
					cont2.setUB(q);
					cont2.setLB(0);
					cont3.setUB(t);
					cont3.setLB(0);
					// create model and solve it
					if (cplex.solve()) {
						double objval = cplex.getObjValue(); // ��ȡ�����е����о��߱����Ľ�ֵ��
						double[] xval = cplex.getValues(x);
						Double[] xvar = Utility.toObjectArray(xval);
					 
						int missingFeatureSize = (int)(xvar.length- omittedBits+ Utility.ArrayProducts(xvar,this.varietyEffiecient, xvar.length- omittedBits));
						int notUsedFeatureSize = (int) (Utility.ArrayProducts(xvar,this.usedbeforeEffiecient,xvar.length- omittedBits));
						double defects = Utility.ArrayProducts(xvar,this.defectEffiecient, xvar.length- omittedBits);
						double costs = Utility.ArrayProducts(xvar,this.costEffiecient, xvar.length- omittedBits);
						
						CplexSolution sol = new CplexSolution(CplexResultComparator.formatDouble2(costs),missingFeatureSize,notUsedFeatureSize,CplexResultComparator.formatDouble2(defects),xvar);
						//CplexSolution sol = new CplexSolution(objval,missingFeatureSize,notUsedFeatureSize,defects,xvar); 
						if(!sols.containsKey(sol.getSolutionID())) 
						{
							sols.put(sol.getSolutionID(), sol);
						}
						System.out.println("BestObj: " + objval + "Cost: "+ costs);
						//System.out.println("Var: " + Arrays.asList(xvar));
					}
					//cplex.remove(cont1);
					//cplex.remove(cont2);
					//cplex.remove(cont3);
					if(sols.size()==Cplex_Product_Main.NUM_OF_SOLS) break;

				}	
			}
		}
	}

	public void cplexSolve(MyIloCplex cplex, Map<String, CplexSolution> sols,
			int omittedBits, double mustCost, IloNumVar[] x,
			int notUsedFeaSize, int totalDefect, IloRange cont1,
			IloRange cont2, IloRange cont3) throws IloException,
			UnknownObjectException {
		int n = 1;
		for (int p = 0; p <= x.length; p++) {
			if(sols.size()==Cplex_Product_Main.NUM_OF_SOLS) break;
			n=1;
			for (int q = notUsedFeaSize; q >= 0; q--) {
				n=1;
				if(sols.size()==Cplex_Product_Main.NUM_OF_SOLS) break;
				for (int t = totalDefect; t >= 0; t=t-n) {
 
					cont1.setUB(-1.0*p);
					cont1.setLB(-1.0*x.length);
					cont2.setUB(q);
					cont2.setLB(0);
					cont3.setUB(t);
					cont3.setLB(0);
					// create model and solve it
					if (cplex.solve()) {
						double objval = cplex.getObjValue(); // ��ȡ�����е����о��߱����Ľ�ֵ��
						double[] xval = cplex.getValues(x);
						Double[] xvar = Utility.toObjectArray(xval);
					 
						int missingFeatureSize = (int)(xvar.length- omittedBits+ Utility.ArrayProducts(xvar,this.varietyEffiecient, xvar.length- omittedBits));
						int notUsedFeatureSize = (int) (Utility.ArrayProducts(xvar,this.usedbeforeEffiecient,xvar.length- omittedBits));
						double defects = Utility.ArrayProducts(xvar,this.defectEffiecient, xvar.length- omittedBits);
					
						CplexSolution sol = new CplexSolution(CplexResultComparator.formatDouble2(objval- mustCost),missingFeatureSize,notUsedFeatureSize,CplexResultComparator.formatDouble2(defects),xvar);
						//CplexSolution sol = new CplexSolution(objval,missingFeatureSize,notUsedFeatureSize,defects,xvar); 
						if(!sols.containsKey(sol.getSolutionID())) 
						{
							sols.put(sol.getSolutionID(), sol);
							n = 1;
						}
						else
						{
						   n= n*Cplex_Product_Main.CPLEX_SEARCH_SPEEDUP;	
						}							
						System.out.println("Obj: " + objval);
						//System.out.println("Var: " + Arrays.asList(xvar));
					}
					else
					{
						n= n*Cplex_Product_Main.CPLEX_SEARCH_SPEEDUP;	
					}
					//cplex.remove(cont1);
					//cplex.remove(cont2);
					//cplex.remove(cont3);
					if(sols.size()==Cplex_Product_Main.NUM_OF_SOLS) break;
				}	
			}
		}
	}


}
