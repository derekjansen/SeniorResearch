/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neat;

import java.util.Comparator;

/**
 *
 * @author derekgrove
 */
 public class FitnessGenomeComparator implements Comparator<Evaluator.FitnessGenome>{
        @Override
        public int compare(Evaluator.FitnessGenome one, Evaluator.FitnessGenome two){
            if(one.fitness > two.fitness){
                return 1;
            }else if(two.fitness > one.fitness){
                return -1;
            }
            return 0;
        }
    }  
