package examples;
	
	import generic.Input;
	import generic.Output;
	import generic.Tuple;
	import tools.JMathPlotter;
	import type1.sets.T1MF_Gauangle;
	import type1.sets.T1MF_Gaussian;
	import type1.sets.T1MF_Interface;
	import type1.sets.T1MF_Triangular;
	import type1.sets.T1MF_Trapezoidal;
	import type1.system.T1_Antecedent;
	import type1.system.T1_Consequent;
	import type1.system.T1_Rule;
	import type1.system.T1_Rulebase;

	public class miniproject 
	{
	    Input headache, temp;     //the inputs to the FLS
	    Output result;             //the output of the FLS
	    T1_Rulebase rulebase;          //the rulebase captures the entire FLS
	    
	    public miniproject()
	    {
	        //Define the inputs
	    	headache = new Input("Severre Headache", new Tuple(0,10));     
	        temp = new Input("Current Temperature", new Tuple(36,44));  
	        result = new Output("Result", new Tuple(-20,20));  
	        
	        //Eye Pain
	        double[] noHeadache = {0.0,0.0,3.0,5.0};
	        T1MF_Trapezoidal noHeadacheMF = new T1MF_Trapezoidal("no", noHeadache);
	        //T1MF_Triangular sevCalMF = new T1MF_Triangular("several", 2.0,5.0,8.0);
	        double[] yesHeadache = {5.0,7.0,10.0,10.0};
	        T1MF_Trapezoidal yesHeadacheMF = new T1MF_Trapezoidal("yes", yesHeadache);
	        
	        //Metallic Taste In Mouth
	        double[] lowTemp = {36.4,36.4,38.0,39.4};
	        T1MF_Trapezoidal lowTempMF = new T1MF_Trapezoidal("low", lowTemp);
	        //T1MF_Triangular medMassMF = new T1MF_Triangular("medium", 5.0,15.0,25.0);
	        double[] highTemp = {39.4,40.4,41.4,41.4};
	        T1MF_Trapezoidal highTempMF = new T1MF_Trapezoidal("high", highTemp);
	        
	        //Dengue Test Result
	        T1MF_Triangular noDengueMF = new T1MF_Triangular("NO", -20.0,-10.0,0.0);
	        //T1MF_Triangular susImpMF = new T1MF_Triangular("suspicious", -10.0,0.0,10.0);
	        T1MF_Triangular yesDengueMF = new T1MF_Triangular("YES", 0.0,10.0,20.0);
	 
	        //Set up the antecedents and consequents - note how the inputs are associated...
	        T1_Antecedent noHeadache2 = new T1_Antecedent("less",noHeadacheMF, headache);
	        //T1_Antecedent sevCal2 = new T1_Antecedent("several",sevCalMF, calcification);
	        T1_Antecedent yesHeadache2 = new T1_Antecedent("more",yesHeadacheMF, headache);

	        T1_Antecedent lowTemp2 = new T1_Antecedent("less",lowTempMF, temp);
	        //T1_Antecedent medMass2 = new T1_Antecedent("less",medMassMF, mass);
	        T1_Antecedent highTemp2 = new T1_Antecedent("more",highTempMF, temp);

	        T1_Consequent noDengue2 = new T1_Consequent("NO",noDengueMF, result);
	        //T1_Consequent susImp2 = new T1_Consequent("suspicious", susImpMF, impression);
	        T1_Consequent yesDengue2 = new T1_Consequent("YES", yesDengueMF, result);
	        
	        //Set up the rulebase and add rules
	        
	        rulebase = new T1_Rulebase(4);
	        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{noHeadache2, lowTemp2}, noDengue2));
	        //rulebase.addRule(new T1_Rule(new T1_Antecedent[]{lessCal2, medMass2}, benImp2));
	        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{noHeadache2, highTemp2}, noDengue2));
	        //rulebase.addRule(new T1_Rule(new T1_Antecedent[]{sevCal2, smallMass2}, susImp2));
	       // rulebase.addRule(new T1_Rule(new T1_Antecedent[]{sevCal2, medMass2}, susImp2));
	       // rulebase.addRule(new T1_Rule(new T1_Antecedent[]{sevCal2, largeMass2}, susImp2));
	        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{yesHeadache2, lowTemp2}, yesDengue2));
	        //rulebase.addRule(new T1_Rule(new T1_Antecedent[]{moreCal2, medMass2}, malImp2));
	        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{yesHeadache2, highTemp2}, yesDengue2));
	        

	        //just an example of setting the discretisation level of an output - the usual level is 100
	        result.setDiscretisationLevel(50);      
	        
	        //get some outputs
	        getimpression(1,1); //user input
	        
	        //plot some sets, discretizing each input into 100 steps.
	        plotMFs("Severe Headache Membership Functions", new T1MF_Interface[]{noHeadacheMF , yesHeadacheMF}, headache.getDomain(), 100); 
	        plotMFs("Current Temperature Membership Functions", new T1MF_Interface[]{lowTempMF, highTempMF}, temp.getDomain(), 100);
	        plotMFs("Dengue Test Result Membership Functions", new T1MF_Interface[]{noDengueMF, yesDengueMF}, result.getDomain(), 100);
	       
	        //plot control surface
	        //do either height defuzzification (false) or centroid d. (true)
	        plotControlSurface(true, 100, 100);
	        
	        //print out the rules
	        System.out.println("\n"+rulebase);        
	    }
	    
	    /**
	     * Basic method that prints the output for a given set of inputs.
	     * @param calcificationQuality
	     * @param massLevel 
	     */
	    private void getimpression(double headachelevel, double templevel)
	    {
	        //first, set the inputs
	        headache.setInput(headachelevel);
	        temp.setInput(templevel);
	        //now execute the FLS and print output
	        System.out.println("The severe headache was: " + headache.getInput());
	        System.out.println("The current temperature was: " + temp.getInput());
	        System.out.println("Using height defuzzification, the FLS recommends a result of"
	                + "result of: " + rulebase.evaluate(0).get(result)); 
	        System.out.println("Using centroid defuzzification, the FLS recommends a result of"
	                + "result of: " + rulebase.evaluate(1).get(result));     
	    }
	    
	    private void plotMFs(String name, T1MF_Interface[] sets, Tuple xAxisRange, int discretizationLevel)
	    {
	        JMathPlotter plotter = new JMathPlotter(17,17,15);
	        for (int i=0;i<sets.length;i++)
	        {
	            plotter.plotMF(sets[i].getName(), sets[i], discretizationLevel, xAxisRange, new Tuple(0.0,1.0), false);
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
	        incrX = headache.getDomain().getSize()/(input1Discs-1.0);
	        incrY = temp.getDomain().getSize()/(input2Discs-1.0);

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
	            headache.setInput(x[currentX]);
	            for(int currentY=0; currentY<input2Discs; currentY++)
	            {
	                temp.setInput(y[currentY]);
	                if(useCentroidDefuzzification)
	                    output = rulebase.evaluate(1).get(result);
	                else
	                    output = rulebase.evaluate(0).get(result);
	                z[currentY][currentX] = output;
	            }    
	        }
	        
	        //now do the plotting
	        JMathPlotter plotter = new JMathPlotter(17, 17, 14);
	        plotter.plotControlSurface("Control Surface",
	                new String[]{headache.getName(), temp.getName(), "result"}, x, y, z, new Tuple(-20.0,20.0), true);   
	       plotter.show("Type-1 Fuzzy Logic System Control Surface for result Example");
	    }
	    
	    public static void main (String args[])
	    {
	        new miniproject();
	    }
}

