/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neat;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author derekgrove
 */
public class Species{
    
        public Genome mascot;
        public List<Genome> members;
        public List<Evaluator.FitnessGenome> fitnessPop;
        public float totalAdjustedFitness = 0f;
        
        public Species(Genome mascot){
            this.mascot = mascot;
            this.members = new LinkedList<Genome>();
            this.members.add(mascot);
            this.fitnessPop = new ArrayList<Evaluator.FitnessGenome>();
        }
        
        public void addAdjustedFitness(float adjustedFitness){
            this.totalAdjustedFitness += adjustedFitness;
        }
        
        public void reset(Random r){
            int newMascotIndex = r.nextInt(members.size());
            this.mascot = members.get(newMascotIndex);
            members.clear();
            fitnessPop.clear();
            totalAdjustedFitness = 0f;
        }
    }
