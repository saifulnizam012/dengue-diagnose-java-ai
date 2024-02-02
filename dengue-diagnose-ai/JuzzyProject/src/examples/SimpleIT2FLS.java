/*
 * SimpleT1FLS.java
 *
 * Created on May 21st 2012
 *
 * Copyright 2012 Christian Wagner All Rights Reserved.
 */
package examples;

import generic.Input;
import generic.Output;
import generic.Tuple;
import intervalType2.sets.IntervalT2MF_Gauangle;
import intervalType2.sets.IntervalT2MF_Gaussian;
import intervalType2.sets.IntervalT2MF_Interface;
import intervalType2.sets.IntervalT2MF_Triangular;
import intervalType2.system.IT2_Antecedent;
import intervalType2.system.IT2_Consequent;
import intervalType2.system.IT2_Rule;
import intervalType2.system.IT2_Rulebase;
import java.util.TreeMap;
import tools.JMathPlotter;
import type1.sets.T1MF_Gauangle;
import type1.sets.T1MF_Gaussian;
import type1.sets.T1MF_Triangular;

/**
 * A simple example of an interval Type-2 FLS based on the "How much to tip the waiter"
 *  scenario.
 * The example is an extension of the Type-1 FLS example where we extend the MFs
 * and use the Interval Type-2 System classes. Note that in contrast to the type-1
 * case, here only two sets are used to model the service quality.
 * We have two inputs: food quality and service level and as an output we would
 * like to generate the applicable tip.
 * @author Christian Wagner
 */
public class SimpleIT2FLS 
{
    Input abdominal, muscle;    //the inputs to the FLS
    Output dengue;             //the output of the FLS
    IT2_Rulebase rulebase;   //the rulebase captures the entire FLS
    
    public SimpleIT2FLS()
    {
        //Define the inputs
        abdominal = new Input("Abdominal Pain", new Tuple(0,10));
        muscle = new Input("Joint muscle ache", new Tuple(0,10));
        dengue = new Output("Dengue result", new Tuple(0,30));               //a percentage for the tip

        //Set up the lower and upper membership functions (MFs) making up the 
        //overall Interval Type-2 Fuzzy Sets for each input and output
        T1MF_Triangular noAbUMF = new T1MF_Triangular("Upper MF for No Abdominal Pain",0.0, 0.0, 10.0);
        T1MF_Triangular noAbLMF = new T1MF_Triangular("Lower MF for No Abdominal Pain",0.0, 0.0, 8.0);
        IntervalT2MF_Triangular noAbMF = new IntervalT2MF_Triangular("IT2MF for No Abdominal Pain",noAbUMF,noAbLMF);
        
        T1MF_Triangular yesAbUMF = new T1MF_Triangular("Upper MF for Yes Abdominal Pain",0.0, 10.0, 10.0);
        T1MF_Triangular yesAbLMF = new T1MF_Triangular("Lower MF for Yes Abdominal Pain",2.0, 10.0, 10.0);
        IntervalT2MF_Triangular yesAbMF = new IntervalT2MF_Triangular("IT2MF for Yes Abdominal Pain",yesAbUMF,yesAbLMF);
        
        T1MF_Gauangle noMusUMF = new T1MF_Gauangle("Upper MF for No Joint Muscle Ache",0.0, 0.0, 8.0);
        T1MF_Gauangle noMusLMF = new T1MF_Gauangle("Lower MF for No Joint Muscle Ache",0.0, 0.0, 6.0);
        IntervalT2MF_Gauangle noMusMF = new IntervalT2MF_Gauangle("IT2MF for No Joint Muscle Ache ",noMusUMF,noMusLMF);

//        T1MF_Gauangle okServiceUMF = new T1MF_Gauangle("Upper MF for ok service",2.5, 5.0, 7.5);
//        T1MF_Gauangle okServiceLMF = new T1MF_Gauangle("Lower MF for ok service",4.5, 5.0, 5.5);
//        IntervalT2MF_Gauangle okServiceMF = new IntervalT2MF_Gauangle("IT2MF for ok service",okServiceUMF,okServiceLMF);

        T1MF_Gauangle yesMusUMF = new T1MF_Gauangle("Upper MF for Yes Joint Muscle Ache",2.0, 10.0, 10.0);
        T1MF_Gauangle yesMusLMF = new T1MF_Gauangle("Lower MF for Yes Joint Muscle Ache",4.0, 10.0, 10.0);
        IntervalT2MF_Gauangle yesMusMF = new IntervalT2MF_Gauangle("IT2MF for Yes Joint Muscle Ache",yesMusUMF,yesMusLMF);

        T1MF_Gaussian noDengueUMF = new T1MF_Gaussian("Upper MF No Dengue", 0.0, 6.0);
        T1MF_Gaussian noDengueLMF = new T1MF_Gaussian("Lower MF No Dengue", 0.0, 4.0);
        IntervalT2MF_Gaussian noDengueMF = new IntervalT2MF_Gaussian("IT2MF for No Dengue",noDengueUMF,noDengueLMF);

        //T1MF_Gaussian mediumTipUMF = new T1MF_Gaussian("Upper MF Medium tip", 15.0, 6.0);
        //T1MF_Gaussian mediumTipLMF = new T1MF_Gaussian("Lower MF Medium tip", 15.0, 4.0);
       // IntervalT2MF_Gaussian mediumTipMF = new IntervalT2MF_Gaussian("IT2MF for Medium tip",mediumTipUMF,mediumTipLMF);

        T1MF_Gaussian yesDengueUMF = new T1MF_Gaussian("Upper MF Yes dengue", 30.0, 6.0);
        T1MF_Gaussian yesDengueLMF = new T1MF_Gaussian("Lower MF Yes Dengue", 30.0, 4.0);
        IntervalT2MF_Gaussian yesDengueMF = new IntervalT2MF_Gaussian("IT2MF for Yes Dengue",yesDengueUMF,yesDengueLMF);

        //Set up the antecedents and consequents - note how the inputs are associated...
        IT2_Antecedent noAb = new IT2_Antecedent("No Abdominal Pain", noAbMF, abdominal);
        IT2_Antecedent yesAb = new IT2_Antecedent("Yes Abdominal Pain", yesAbMF, abdominal);

        IT2_Antecedent noMus = new IT2_Antecedent("No Joint Muscle Ache", noMusMF, muscle);
        //IT2_Antecedent okService = new IT2_Antecedent("OkService", okServiceMF, service);
        IT2_Antecedent yesMus = new IT2_Antecedent("Yes Joint Muscle Ache", yesMusMF, muscle);

        IT2_Consequent noDengue = new IT2_Consequent("No Dengue Result", noDengueMF, dengue);
        //IT2_Consequent mediumTip = new IT2_Consequent("MediumTip", mediumTipMF, tip);
        IT2_Consequent yesDengue = new IT2_Consequent("Yes Dengue Result", yesDengueMF, dengue);

        //Set up the rulebase and add rules
        rulebase = new IT2_Rulebase(4);
        rulebase.addRule(new IT2_Rule(new IT2_Antecedent[]{noAb, noMus}, noDengue));
        rulebase.addRule(new IT2_Rule(new IT2_Antecedent[]{noAb, yesMus}, yesDengue));
        rulebase.addRule(new IT2_Rule(new IT2_Antecedent[]{yesAb, noMus}, yesDengue));
        rulebase.addRule(new IT2_Rule(new IT2_Antecedent[]{yesAb, yesMus}, yesDengue));
        
        //get some outputs
        getDengue(7,8);
        getDengue(0,0);
                
        //plot some sets, discretizing each input into 100 steps.
        plotMFs("Abdominal Pain Membership Functions", new IntervalT2MF_Interface[]{noAbMF, yesAbMF}, 100); 
        plotMFs("Joint Muscle Ache Membership Functions", new IntervalT2MF_Interface[]{noMusMF, /**okServiceMF,**/ yesMusMF}, 100);
        plotMFs("Dengue Result Membership Functions", new IntervalT2MF_Interface[]{noDengueMF, yesDengueMF}, 100);
        
        //plot control surface
        //do either height defuzzification (false) or centroid d. (true)
        plotControlSurface(false, 100, 100);
        
        //print out the rules
        System.out.println("\n"+rulebase);
    }
    
    /**
     * Basic method that prints the output for a given set of inputs.
     * @param foodQuality
     * @param serviceLevel 
     */
    private void getDengue(double AbdominalPain, double JointMuscleAche)
    {
        //first, set the inputs
        abdominal.setInput(AbdominalPain);
        muscle.setInput(JointMuscleAche);
        
        //now execute the FLS and print output
        System.out.println("The Abdominal Pain was: "+abdominal.getInput());
        System.out.println("The Joint Muscle Ache was: "+muscle.getInput());
        System.out.println("Using center of sets type reduction, the IT2 FLS recommends a "
                + "dengue of: "+rulebase.evaluate(0).get(dengue));  
        System.out.println("Using centroid type reduction, the IT2 FLS recommends a "
                + "tip of: "+rulebase.evaluate(1).get(dengue));
        
        
        //show the output of the raw centroids
        System.out.println("Centroid of the output for DENGUE (based on centroid type reduction):");
        TreeMap<Output, Object[]> centroid = rulebase.evaluateGetCentroid(1);
        Object[] centroidTip = centroid.get(dengue);
        Tuple centroidTipXValues = (Tuple)centroidTip[0];
        double centroidTipYValues = ((Double)centroidTip[1]);
            System.out.println(centroidTipXValues+" at y= "+centroidTipYValues);        
    }
    
    private void plotMFs(String name, IntervalT2MF_Interface[] sets, int discretizationLevel)
    {
        JMathPlotter plotter = new JMathPlotter();
        plotter.plotMF(sets[0].getName(), sets[0], discretizationLevel, null, false);
       
        for (int i=1;i<sets.length;i++)
        {
            plotter.plotMF(sets[i].getName(), sets[i], discretizationLevel, null, false);
        }
        plotter.show(name);
    }

    private void plotControlSurface(boolean useCentroidDefuzzification, int input1Discs, int input2Discs)
    {
        double output;
        double[] x = new double[input1Discs];
        double[] y = new double[input2Discs];
        double[][] z = new double[y.length][x.length];
        double incrX, incrY;
        incrX = abdominal.getDomain().getSize()/(input1Discs-1.0);
        incrY = muscle.getDomain().getSize()/(input2Discs-1.0);

        //first, get the values
        for(int currentX=0; currentX<input1Discs; currentX++)
        {
            x[currentX] = currentX * incrX;        
        }
        for(int currentY=0; currentY<input2Discs; currentY++)
        {
            y[currentY] = currentY * incrY;
        }
        
        for(int currentX=0; currentX<input1Discs; currentX++)
        {
            abdominal.setInput(x[currentX]);
            for(int currentY=0; currentY<input2Discs; currentY++)
            {//System.out.println("Current x = "+currentX+"  current y = "+currentY);
                muscle.setInput(y[currentY]);
                if(useCentroidDefuzzification)
                    output = rulebase.evaluate(1).get(dengue);
                else
                    output = rulebase.evaluate(0).get(dengue);
                z[currentY][currentX] = output;
            }    
        }
        
        //now do the plotting
        JMathPlotter plotter = new JMathPlotter();
        plotter.plotControlSurface("Control Surface",
                new String[]{abdominal.getName(), muscle.getName(), "Dengue"}, x, y, z, new Tuple(0.0,30.0), true); 
        plotter.show("Interval Type-2 Fuzzy Logic System Control Surface for Dengue Example");
    }
    
    public static void main(String args[])
    {
        new SimpleIT2FLS();
    }
}
