package com.jsf.bootstrap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.security.KeyStore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.evaluation.Prediction;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.rules.PART;
import weka.classifiers.trees.DecisionStump;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;
import weka.core.converters.ArffSaver;
import weka.core.SerializationHelper;
import weka.core.Utils;
import weka.core.converters.ConverterUtils.DataSource;

@ManagedBean @SessionScoped
public class StudentBean {

	private String firstName;
	private String lastName;
	private String standard;

	private double feverPeriod;
	private double currentTemperature;
	private String severeHeadache;
	private String eyePain;
	private String jointMuscleAche;
	private String metallicTasteInMouth;
	private String lossOfAppetite;
	private String abdominalPain;
	private String nauseaVomiting;
	private String diarrhoea;
	private String dengueTestResult;

	private String temp;
	private String rate;
	private String DengueTrainFile="C:\\Users\\ACER\\Documents\\3) TTC2013-Artificial Intelligence\\Latest mini project\\Coding\\DiagnosingDengueInfection\\src\\com\\jsf\\bootstrap\\DengueTrain.arff";
	private String DengueTestFile="C:\\Users\\ACER\\Documents\\3) TTC2013-Artificial Intelligence\\Latest mini project\\Coding\\DiagnosingDengueInfection\\src\\com\\jsf\\bootstrap\\DengueTest.arff";
	private String DengueActualARFFFile="C:\\Users\\ACER\\Documents\\3) TTC2013-Artificial Intelligence\\Latest mini project\\Coding\\DiagnosingDengueInfection\\src\\com\\jsf\\bootstrap\\DengueActualARFF.arff";
	private String DengueActualCSVFile="C:\\Users\\ACER\\Documents\\3) TTC2013-Artificial Intelligence\\Latest mini project\\Coding\\DiagnosingDengueInfection\\src\\com\\jsf\\bootstrap\\DengueActualCSV.csv";
	private String modelfile="C:\\Users\\ACER\\Documents\\3) TTC2013-Artificial Intelligence\\Latest mini project\\Coding\\DiagnosingDengueInfection\\src\\com\\jsf\\bootstrap\\modelfile.model";

	private double result = -1;
	private Instances classes;

	//Start Weather forecast Input
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public void setRate(String temp) {
		this.rate = temp;
	}

	public String getRate() {
		return rate;
	}

	public double getFeverPeriod() {
		return feverPeriod;
	}

	public void setFeverPeriod(double feverPeriod) {
		this.feverPeriod=feverPeriod;	
	}

	public double getCurrentTemperature() {
		return currentTemperature;
	}

	public void setCurrentTemperature(double currentTemperature) {
		this.currentTemperature=currentTemperature;
	}

	public String getSevereHeadache() {
		return severeHeadache;
	}

	public void setSevereHeadache(String severeHeadache) {
		this.severeHeadache=severeHeadache;
	}

	public String getEyePain() {
		return eyePain;
	}

	public void setEyePain(String eyePain) {
		this.eyePain=eyePain;
	}

	public String getJointMuscleAche() {
		return jointMuscleAche;
	}

	public void setJointMuscleAche(String jointMuscleAche) {
		this.jointMuscleAche=jointMuscleAche;
	}

	public String getMetallicTasteInMouth() {
		return metallicTasteInMouth;
	}

	public void setMetallicTasteInMouth(String metallicTasteInMouth) {
		this.metallicTasteInMouth=metallicTasteInMouth;
	}

	public String getLossOfAppetite() {
		return lossOfAppetite;
	}

	public void setLossOfAppetite(String lossOfAppetite) {
		this.lossOfAppetite=lossOfAppetite;
	}

	public String getAbdominalPain() {
		return abdominalPain;
	}

	public void setAbdominalPain(String abdominalPain) {
		this.abdominalPain=abdominalPain;
	}

	public String getNauseaVomiting() {
		return nauseaVomiting;
	}

	public void setNauseaVomiting(String nauseaVomiting) {
		this.nauseaVomiting=nauseaVomiting;
	}

	public String getDiarrhoea() {
		return diarrhoea;
	}

	public void setDiarrhoea(String diarrhoea) {
		this.diarrhoea=diarrhoea;
	}

	public String getDengueTestResult() {
		return dengueTestResult;
	}

	public void setDengueTestResult(String dengueTestResult) {
		this.dengueTestResult=dengueTestResult;
	}

	public String createUserForm() {
		System.out.println("Reading Student Details - Name: "+firstName+" "+lastName+", Organization: "+standard);
		return "output";
	}

	//DengueActualARFF - Single Instance
	public void DengueActualARFF() throws Exception  {

		ArrayList<Attribute> atts = new ArrayList<Attribute>(5);

		//Declaration of Attributes
		atts.add(new Attribute("Fever Period"));
		atts.add(new Attribute("Current Temperature (C)"));

		ArrayList<String> classVal1 = new ArrayList<String>();
		classVal1.add("yes");
		classVal1.add("no");
		atts.add(new Attribute("Severe Headache", classVal1));

		ArrayList<String> classVal2 = new ArrayList<String>();
		classVal2.add("no");
		classVal2.add("yes");
		atts.add(new Attribute("Eye Pain", classVal2));

		ArrayList<String> classVal3 = new ArrayList<String>();
		classVal3.add("yes");
		classVal3.add("no");
		atts.add(new Attribute("Joint Muscle Ache", classVal3));

		ArrayList<String> classVal4 = new ArrayList<String>();
		classVal4.add("no");
		classVal4.add("yes");
		atts.add(new Attribute("Metallic Taste In Mouth", classVal4));

		ArrayList<String> classVal5 = new ArrayList<String>();
		classVal5.add("no");
		classVal5.add("yes");
		atts.add(new Attribute("Loss Of Appetite", classVal5));

		ArrayList<String> classVal6 = new ArrayList<String>();
		classVal6.add("no");
		classVal6.add("yes");
		atts.add(new Attribute("Abdominal Pain", classVal6));

		ArrayList<String> classVal7 = new ArrayList<String>();
		classVal7.add("yes");
		classVal7.add("no");
		atts.add(new Attribute("Nausea & Vomiting", classVal7));

		ArrayList<String> classVal8 = new ArrayList<String>();
		classVal8.add("no");
		classVal8.add("yes");
		atts.add(new Attribute("Diarrhoea", classVal8));

		ArrayList<String> classVal9 = new ArrayList<String>();
		classVal9.add("yes");
		classVal9.add("no");
		atts.add(new Attribute("Dengue Test Result", classVal9));

		Instances dataRaw = new Instances("TestInstances",atts,0);

		/*
		System.out.println("Before adding any instance");
		System.out.println("--------------------------");
		System.out.println(dataRaw);
		System.out.println("--------------------------");

		System.out.println(
				"Fever Period "+getFeverPeriod()+
				"\nCurrent Temperature (C) "+getCurrentTemperature()+
				"\nSevere Headache "+getSevereHeadache()+
				"\nEye Pain "+getEyePain()+
				"\nJoint Muscle Ache "+getJointMuscleAche()+
				"\nMetallic Taste In Mouth "+getMetallicTasteInMouth()+
				"\nLoss of Appetite "+getLossOfAppetite()+
				"\nAbdominal Pain "+getAbdominalPain()+
				"\nNausea & Vomiting "+getNauseaVomiting()+
				"\nDiarrhoea "+getDiarrhoea()+
				"\nDengue Test Result "+getDengueTestResult()
				);

		 */
		double[] instanceValue1 = new double[dataRaw.numAttributes()];

		//Create New Instance: This one should fall into the virginica domain
		instanceValue1[0]=getFeverPeriod();
		instanceValue1[1]=getCurrentTemperature();
		int a=0;
		if (getSevereHeadache().equals("yes")) {a=0;};
		if (getSevereHeadache().equals("no")) {a=1;};
		instanceValue1[2]=a;

		int b=0;
		if (getEyePain().equals("no")) {b=0;};
		if (getEyePain().equals("yes")) {b=1;};
		instanceValue1[3]=b;

		int c=0;
		if (getJointMuscleAche().equals("yes")) {c=0;};
		if (getJointMuscleAche().equals("no")) {c=1;};
		instanceValue1[4]=c;

		int d=0;
		if (getMetallicTasteInMouth().equals("no")) {d=0;};
		if (getMetallicTasteInMouth().equals("yes")) {d=1;};
		instanceValue1[5]=d;

		int e=0;
		if (getLossOfAppetite().equals("no")) {e=0;};
		if (getLossOfAppetite().equals("yes")) {e=1;};
		instanceValue1[6]=e;

		int f=0;
		if (getAbdominalPain().equals("no")) {f=0;};
		if (getAbdominalPain().equals("yes")) {f=1;};
		instanceValue1[7]=f;

		int g=0;
		if (getNauseaVomiting().equals("yes")) {g=0;};
		if (getNauseaVomiting().equals("no")) {g=1;};
		instanceValue1[8]=g;

		int h=0;
		if (getDiarrhoea().equals("no")) {h=0;};
		if (getDiarrhoea().equals("yes")) {h=1;};
		instanceValue1[9]=h;

		dataRaw.add(new DenseInstance(1.0, instanceValue1));
		dataRaw.setClassIndex(dataRaw.numAttributes()-1);

		System.out.println("\nAfter adding an instance");
		System.out.println("--------------------------");
		System.out.println(dataRaw);
		System.out.println("--------------------------");

		//Import ready trained model
		Classifier cls = null;
		try {
			cls = (Classifier) weka.core.SerializationHelper
					.read(modelfile);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		if (cls == null)
			return;

		//Predict new sample
		String predString = null;

		try {
			double result = cls.classifyInstance(dataRaw.lastInstance());
			classes = dataRaw;
			System.out.println("Index of predicted class label: "+result+", which corresponds to class: "+classes.classAttribute().name());
			predString = classes.classAttribute().value((int)result);
			System.out.println("prediction, " + predString);
		} 
		catch (Exception e1) {
			e1.printStackTrace();
		}

		dengueTestResult = predString;
		setDengueTestResult(dengueTestResult);

		//Save ARFF
		ArffSaver saver = new ArffSaver();
		saver.setInstances(dataRaw);

		try {
			saver.setFile(new File("D:\\Haikal Iman\\X\\University\\Materials\\Sem3\\TC2013 Introduction to AI\\Assignment\\DiagnosingDengueInfection\\src\\com\\jsf\\bootstrap\\DengueActualARFF.arff"));
			saver.writeBatch();   
		}
		catch (Exception e1) {
			FileWriter fwriter = new FileWriter(DengueActualARFFFile, true);		//true will append the new instance
			fwriter.write(dataRaw.lastInstance().toString()+"\n");					//appends the string to the file
			fwriter.close();
			//saver.writeIncremental(dataRaw.lastInstance());
		}
	}

	//DengueActualCSV - Single Instance
	public void DengueActualCSV() throws Exception {

		// Our example data
		List<List<String>> rows = Arrays.asList(
				Arrays.asList(
						String.valueOf(getFeverPeriod()),
						String.valueOf(getCurrentTemperature()),
						getSevereHeadache(),
						getEyePain(),
						getJointMuscleAche(),
						getMetallicTasteInMouth(),
						getLossOfAppetite(),
						getAbdominalPain(),
						getNauseaVomiting(),
						getDiarrhoea(),
						getDengueTestResult()) 
				);

		FileWriter csvWriter = new FileWriter(DengueActualCSVFile);
		csvWriter.append("Fever Period");
		csvWriter.append(",");
		csvWriter.append("Current Temperature (C)");
		csvWriter.append(",");
		csvWriter.append("Severe Headache");
		csvWriter.append(",");
		csvWriter.append("Eye Pain");
		csvWriter.append(",");		
		csvWriter.append("Joint Muscle Ache");	
		csvWriter.append(",");		
		csvWriter.append("Metallic Taste In Mouth");
		csvWriter.append(",");		
		csvWriter.append("Loss Of Appetite");
		csvWriter.append(",");		
		csvWriter.append("Abdominal Pain");
		csvWriter.append(",");		
		csvWriter.append("Nausea & Vomiting");
		csvWriter.append(",");		
		csvWriter.append("Diarrhoea");
		csvWriter.append(",");		
		csvWriter.append("Dengue Test Result");
		csvWriter.append("\n");

		for (List<String> rowData : rows) {
			csvWriter.append(String.join(",", rowData));
			csvWriter.append("\n");
		}

		csvWriter.flush();
		csvWriter.close();

		System.out.println(
				"Fever Period, "
						+ "Current Temperature (C), "
						+ "Severe Headache, "
						+ "Eye Pain, "
						+ "Joint Muscle Ache, "
						+ "Metallic Taste In Mouth, "
						+ "Loss Of Appetite, "
						+ "Abdominal Pain, "
						+ "Nausea & Vomiting, "
						+ "Diarrhoea, "
						+ "Dengue Test Result");
		System.out.println(
				getDengueTestResult()+", "+
						getCurrentTemperature()+", "+
						getSevereHeadache()+", "+
						getEyePain()+", "+
						getJointMuscleAche()+", "+
						getMetallicTasteInMouth()+", "+
						getLossOfAppetite()+", "+
						getAbdominalPain()+", "+
						getNauseaVomiting()+", "+
						getDiarrhoea()+", "+
						getDengueTestResult()
				);

		BufferedReader treader = new BufferedReader(new FileReader(DengueActualARFFFile)); //asal arff
		Instances test = new Instances(treader);
		test.setClassIndex(test.numAttributes()-1);
		treader.close();        //loading testing data    

	}	

	//DengueTrain - Dataset
	public void DengueTest() throws Exception { //macam terbaik, asal train tapi mcm test

		BufferedReader breader = new BufferedReader(new FileReader(DengueTrainFile));
		Instances train = new Instances(breader);
		train.setClassIndex(train.numAttributes() -1);
		breader.close();    //loading training data

		Classifier model = new J48();
		model.buildClassifier(train);

		BufferedReader treader = new BufferedReader(new FileReader(DengueTestFile));
		Instances test = new Instances(treader);
		test.setClassIndex(test.numAttributes() -1);
		treader.close();        //loading testing data

		ArrayList<Prediction> predictions = new ArrayList<>();

		Evaluation eval = new Evaluation(train);
		eval.evaluateModel(model,test);

		predictions.addAll(eval.predictions());

		dengueTestResult = String.valueOf(eval.predictions());
		setDengueTestResult(dengueTestResult);

		SerializationHelper.write(modelfile, model);		

		System.out.println(eval.toMatrixString("\nConfusion Matrix\n========\n"));   

		double accuracy = calculateAccuracy(predictions);
		System.out.println("Accuracy of " + model.getClass().getSimpleName() + ": "
				+ String.format("%.2f%%", accuracy)
				+ "\n---------------------------------");

		System.out.println(
				"Reading Weather Details - "+
						"\nFever Period "+getFeverPeriod()+
						"\nCurrent Temperature (C) "+getCurrentTemperature()+
						"\nSevere Headache "+getSevereHeadache()+
						"\nEye Pain "+getEyePain()+
						"\nJoint Muscle Ache "+getJointMuscleAche()+
						"\nMetallic Taste In Mouth "+getMetallicTasteInMouth()+
						"\nLoss of Appetite "+getLossOfAppetite()+
						"\nAbdominal Pain "+getAbdominalPain()+
						"\nNausea & Vomiting "+getNauseaVomiting()+
						"\nDiarrhoea "+getDiarrhoea()+
						"\nAccuracy: "  + accuracy
				);

		temp = String.valueOf(accuracy);
		setRate(temp);
	}

	//DengueTest - Dataset
	public void DengueTrain() throws Exception {	//macam terbalik
		Instances data;

		BufferedReader datafile = readDataFile(DengueTrainFile); //DengueTrainFile

		try {
			ArffReader arff = new ArffReader(datafile);
			data = arff.getData();
			data.setClassIndex(data.numAttributes()-1);

			//Do 10-Split Cross Validation
			Instances[][] split = crossValidationSplit(data, 10); //data, 2

			//Separate Split Into Training And Testing Arrays
			Instances[] trainingSplits = split[0];
			Instances[] testingSplits = split[1];

			//Use A Set Of Classifiers
			Classifier[] models = { 
					new J48(), // a decision tree
					//					new PART(), 
					//					new DecisionTable(),//decision table majority classifier
					//					new DecisionStump(), //one-level decision tree
					//					new BayesNet(),
			};

			//Run For Each Model
			for (int j = 0; j < models.length; j++) {

				//Collect Every Group Of Predictions For Current Model In A FastVector
				ArrayList<Prediction> predictions = new ArrayList<>();

				//For Each Training-Testing Split Pair, Train And Test The Classifier
				for (int i = 0; i < trainingSplits.length; i++) {
					Evaluation validation;
					try {
						validation = classify(models[j], trainingSplits[i], testingSplits[i]);
						predictions.addAll(validation.predictions());
						dengueTestResult = String.valueOf(validation.predictions());
						setDengueTestResult(dengueTestResult);
					} catch (Exception e) {
						e.printStackTrace();
					}

					//Uncomment To See The Summary For Each Training-Testing Pair //model file.
					System.out.println(models[j].toString());
					//To Save Model File
					SerializationHelper.write(modelfile, models[j]);		
				}

				//Calculate Overall Accuracy Of Current Classifier On All Splits
				double accuracy = calculateAccuracy(predictions);

				//Print Current Classifier's Name And Accuracy In A Complicated,
				//But Nice-Looking Way.
				System.out.println("Accuracy of " + models[j].getClass().getSimpleName() + ": "
						+ String.format("%.2f%%", accuracy)
						+ "\n---------------------------------");
				System.out.println(
						"Reading Weather Details - "+
								"\nFever Period "+getFeverPeriod()+
								"\nCurrent Temperature (C) "+getCurrentTemperature()+
								"\nSevere Headache "+getSevereHeadache()+
								"\nEye Pain "+getEyePain()+
								"\nJoint Muscle Ache "+getJointMuscleAche()+
								"\nMetallic Taste In Mouth "+getMetallicTasteInMouth()+
								"\nLoss of Appetite "+getLossOfAppetite()+
								"\nAbdominal Pain "+getAbdominalPain()+
								"\nNausea & Vomiting "+getNauseaVomiting()+
								"\nDiarrhoea "+getDiarrhoea()+
								"\nAccuracy: "  + accuracy
						);

				temp = String.valueOf(accuracy);
				setRate(temp);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static BufferedReader readDataFile(String filename) {
		BufferedReader inputReader = null;
		//To Read Data File
		try {
			inputReader = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException ex) {
			System.err.println("File not found: " + filename);
		}
		return inputReader;
	}

	public static Evaluation classify(Classifier model,Instances trainingSet, Instances testingSet) throws Exception {
		//To Evaluate The Training Dataset
		Evaluation evaluation = new Evaluation(trainingSet);

		//To Create Model Of The Classifiers Using Training Dataset
		model.buildClassifier(trainingSet);

		//To Evaluate The Created Model On The Testing Dataset
		evaluation.evaluateModel(model, (Instances) testingSet);

		return evaluation;
	}

	public static double calculateAccuracy(ArrayList<Prediction> predictions) {
		double correct = 0;

		//To Calculate The Correct Accuracy By Comparing The Predicted And Actual Values 
		for (int i = 0; i < predictions.size(); i++) {
			NominalPrediction np = (NominalPrediction) predictions.get(i);
			if (np.predicted() == np.actual()) {
				correct++;
			}
		}
		return 100 * correct / predictions.size();
	}

	public static Instances[][] crossValidationSplit(Instances data, int numberOfFolds) {
		Instances[][] split = new Instances[2][numberOfFolds];

		//To Create Data Sampling For Training And Testing Using Split Cross-Validation Based On Specific Number Of Fold 
		for (int i = 0; i < numberOfFolds; i++) {
			split[0][i] = data.trainCV(numberOfFolds, i);
			split[1][i] = data.testCV(numberOfFolds, i);
		}

		return split;
	}

	@WebServlet("/inputServlet")
	public class LoginServlet extends HttpServlet {

		protected void doPost(HttpServletRequest request,
				HttpServletResponse response) throws ServletException, IOException {

			// read form fields
			firstName = request.getParameter("firstName");
			lastName = request.getParameter("lastName");

			
			System.out.println("First Name: " + firstName);
			System.out.println("Last Name: " + lastName);

/*
			// do some processing here...

			// get response writer
			PrintWriter writer = response.getWriter();

			// build HTML code
			String htmlRespone = "<html>";
			htmlRespone += "<h2>Your username is: " + username + "<br/>";      
			htmlRespone += "Your password is: " + password + "</h2>";    
			htmlRespone += "</html>";

			// return response
			writer.println(htmlRespone);
			*/
		}
	}
}