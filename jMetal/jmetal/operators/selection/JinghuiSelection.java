//  DifferentialEvolutionSelection.java
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

package jmetal.operators.selection;

import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

import java.util.HashMap;

/**
 * Class implementing the selection operator used in DE: three different solutions
 * are returned from a population.
 */
public class JinghuiSelection extends Selection {

  /**
   * Constructor
   */
  JinghuiSelection(HashMap<String, Object> parameters) {
  	super(parameters) ;
  } // Constructor

  /**
   * Executes the operation
   * @param object An object containing the population and the position (index)
   *               of the current individual
   * @return An object containing the three selected parents
   */
  public Object execute(Object object) throws JMException {
    Object[] parameters = (Object[])object ;
    SolutionSet population = (SolutionSet)parameters[0];
    int         index      = (Integer)parameters[1] ;

    Solution[] parents = new Solution[5] ;
    int r1, r2, r3, r4, r5;
    if (population.size() < 5)
      throw new JMException("DifferentialEvolutionSelection: the population has less than four solutions") ;
    
    int T = 20, temp;   

    double min_correct = 1e10;
    int 	min_i = 0;
    int 	k = 0;
   
    
    for(int i = 0; i < population.size();i++){
    	if(population.get(i).getObjective(0)<= min_correct){
    		min_correct =population.get(0).getObjective(k);
    		min_i = i;
    	}
    }
    r1 = min_i;
    
    if(min_correct == 0){
    	
    //if(PseudoRandom.randDouble(0, 1) < 0.5) k = 0;
    //else k = PseudoRandom.randInt(1,4);
    	r1 = PseudoRandom.randInt(0,population.size()-1);
    }else{
    	for(int i = 0; i < T; i++){
    		do{temp = (int)(PseudoRandom.randInt(0,population.size()-1));}while(temp==index);
    		if(population.get(temp).getObjective(k) <population.get(r1).getObjective(k)) r1 = temp;
    	}
    }
    //}
   // else  do{ r1 = (int)(PseudoRandom.randInt(0,population.size()-1));}while(r1==index);
    /*
    if(population.get(0).getObjective(0) > 0){
	    for(int i = 0; i < T; i++){
	    	do{temp = (int)(PseudoRandom.randInt(0,population.size()-1));}while(temp==index);
	    	if(population.get(temp).getObjective(0) <population.get(r1).getObjective(0)) r1 = temp;
	    }
    }
    */
    
    do{ r2 = (int)(PseudoRandom.randInt(0,population.size()-1));}while(r2==index || r2==r1);
    for(int i = 0; i < 0; i++){
    	do{temp = (int)(PseudoRandom.randInt(0,population.size()-1));}while(temp==index || temp==r1);
    	if(population.get(temp).getObjective(0) <population.get(r2).getObjective(0)) r2 = temp;
    }
    
    do{ r3 = (int)(PseudoRandom.randInt(0,population.size()-1));}while(r3==index || r3==r2 || r3==r1);
    for(int i = 0; i < 0; i++){
    	do{temp = (int)(PseudoRandom.randInt(0,population.size()-1));}while(temp==index || temp == r2 || temp==r1);
    	if(population.get(temp).getObjective(0) <population.get(r3).getObjective(0)) r3 = temp;
    }
    
    do{ r4 = (int)(PseudoRandom.randInt(0,population.size()-1));}while(r4==index || r4==r3 || r4 == r2|| r4==r1);
    for(int i = 0; i < 0; i++){
    	do{temp = (int)(PseudoRandom.randInt(0,population.size()-1));}while(temp==index || temp==r1 ||temp==r2 || temp == r3);
    	if(population.get(temp).getObjective(0) <population.get(r4).getObjective(0)) r4 = temp;
    }
    
    do{ r5 = (int)(PseudoRandom.randInt(0,population.size()-1));}while(r5==index || r5 == r4 ||r5==r3 || r5 == r2|| r5==r1);
    for(int i = 0; i < 0; i++){
    	do{temp = (int)(PseudoRandom.randInt(0,population.size()-1));}while(temp==index || r5 == r4 ||temp==r1 ||temp==r2 || temp == r3);
    	if(population.get(temp).getObjective(0) <population.get(r4).getObjective(0)) r5 = temp;
    }
    
    /*
    r1 = 0;    
    do{ r1 = (int)(PseudoRandom.randInt(0,population.size()-1));}while(r1==index);
    for(int i = 0; i < T; i++){
    	do{temp = (int)(PseudoRandom.randInt(0,population.size()-1));}while(temp==index);
    	if(population.get(temp).getFitness() < population.get(r1).getFitness()) r1 = temp;
    }
    
    do{ r2 = (int)(PseudoRandom.randInt(0,population.size()-1));}while(r2==index || r2==r1);
    for(int i = 0; i < T; i++){
    	do{temp = (int)(PseudoRandom.randInt(0,population.size()-1));}while(temp==index || temp==r1);
    	if(population.get(temp).getFitness() < population.get(r2).getFitness()) r2 = temp;
    }
    
    do{ r3 = (int)(PseudoRandom.randInt(0,population.size()-1));}while(r3==index || r3==r2 || r3==r1);
    for(int i = 0; i < T; i++){
    	do{temp = (int)(PseudoRandom.randInt(0,population.size()-1));}while(temp==index || temp == r2 || temp==r1);
    	if(population.get(temp).getFitness() < population.get(r3).getFitness()) r3 = temp;
    }
    
    do{ r4 = (int)(PseudoRandom.randInt(0,population.size()-1));}while(r4==index || r4==r3 || r4 == r2|| r4==r1);
    for(int i = 0; i < T; i++){
    	do{temp = (int)(PseudoRandom.randInt(0,population.size()-1));}while(temp==index || temp==r1 ||temp==r2 || temp == r3);
    	if(population.get(temp).getFitness() < population.get(r4).getFitness()) r4 = temp;
    }
    
    do{ r5 = (int)(PseudoRandom.randInt(0,population.size()-1));}while(r5==index || r5 == r4 ||r5==r3 || r5 == r2|| r5==r1);
    for(int i = 0; i < T; i++){
    	do{temp = (int)(PseudoRandom.randInt(0,population.size()-1));}while(temp==index || r5 == r4 ||temp==r1 ||temp==r2 || temp == r3);
    	if(population.get(temp).getFitness() < population.get(r4).getFitness()) r5 = temp;
    }
    */
    parents[0] = population.get(r1) ;
    parents[1] = population.get(r2) ;
    parents[2] = population.get(r3) ;
    parents[3] = population.get(r4) ;
    parents[4] = population.get(r5) ;

    return parents ;
  } // execute
} // DifferentialEvolutionSelection
