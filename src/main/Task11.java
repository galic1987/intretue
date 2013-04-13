package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.stemmers.*;

public class Task11 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
		
		
		// Standard arguments
		//boolean stemming = Boolean.parseBoolean(args[0]);
		//int lower = Integer.parseInt(args[1]);
		//int upper = Integer.parseInt(args[2]);
		
	
		
		// This is stemmer working , TODO: I dont know where should we implement this? On input word query?
		LovinsStemmer s = new LovinsStemmer();
		System.out.print(s.stem("birds"));
		System.out.print(s.stem("pears"));
		System.out.print(s.stemString("what should I stem here in this house"));


		
		
		
		// 1. loop all files in the folder
		final File folder = new File("20_newsgroups_subset");
		
		// TODO: 
		HashMap<String,List<String>> postingLists = new HashMap<String,List<String>>();
		HashMap<String, List<Term>> documents = new HashMap<String, List<Term>>();
		
		int numberOfDocuments = 0; // N 
		
		for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            //System.out.println("Entering folder "+fileEntry.getName());
	            for (File filedeeper : fileEntry.listFiles()){
	            	numberOfDocuments++;
		           // System.out.println("Reading  file "+filedeeper.getName());
		            	// hashlist eine list von allem terms 
		            	// wie oft ein term 
		            //	System.out.println(getFrequencies(filedeeper).toString());
	            	
	            		// how often some terms come in one document
		            	Map<String, Integer> m = getFrequencies(filedeeper);
		            	List<Term> listOfTerms = new ArrayList<Term>();
		            	
		            	// iterate the whole term hastable add posting lists
		            	Iterator<Entry<String, Integer>> it = m.entrySet().iterator();
		                while (it.hasNext()) {
		                    Entry<String, Integer> pairs = it.next();
//		                    System.out.println(pairs.getKey() + " = " + pairs.getValue());
		                    if(postingLists.containsKey(pairs.getKey())){
		                    	List<String> l = postingLists.get(pairs.getKey());
		                    	l.add(filedeeper.getName());
		                    }else{
		                    	List<String> l = new ArrayList<String>();
		                    	l.add(filedeeper.getName());
		                    	postingLists.put((String) pairs.getKey(), l);
		                    }
		                    
		                    it.remove(); // avoids a ConcurrentModificationException
		                    
		                    Term t = new Term();
		                    t.setTerm(pairs.getKey());
		                    t.setFreq(pairs.getValue());
		                    listOfTerms.add(t);
		                }
	            	
	            		// total term frequencies
		            	documents.put(filedeeper.getName(), listOfTerms);		            	
	            }
	        }
	    }
		
		//System.out.println(numberOfDocuments);
		//System.out.println(postingLists.toString());
		
		
		
		FastVector atts;
		double[] vals;
		Instances data;
		// 1. set up attributes
		atts = new FastVector();
		// - numeric
		atts.addElement(new Attribute("documentname", (FastVector) null));
		//add terms
		Iterator<String> itTermPosting = postingLists.keySet().iterator();
		
		HashMap<String, Integer> attrPosition = new HashMap<String, Integer>();
		int attrI = 1;
		while (itTermPosting.hasNext()) {
			String term = itTermPosting.next();
			atts.addElement(new Attribute(term));
			attrPosition.put(term, attrI);
			attrI++;
		}
		
		data = new Instances("MyRelation", atts, 0);
		
		
		//go through all documents and compute idf
		Iterator<Entry<String, List<Term>>> itDoc = documents.entrySet().iterator();
		while(itDoc.hasNext()) {
			Entry<String, List<Term>> entry = itDoc.next();
			List<Term> listOfTerms = entry.getValue();
			String document = entry.getKey();
			
			//arff
			vals = new double[data.numAttributes()];
			vals[0] = data.attribute(0).addStringValue(document);
			
			Iterator<Term> itTerm = listOfTerms.iterator();
			while(itTerm.hasNext()) {
				Term t = itTerm.next();
				
				t.setDocFreq(postingLists.get(t.getTerm()).size());
				
				//idf = log (1 + termfrequency) * log ( N / documentfrequency)
				t.setIdf(Math.log10(1 + t.getFreq()) * Math.log10(numberOfDocuments / t.getDocFreq()));

				vals[attrPosition.get(t.getTerm())] = t.getIdf();
				//vals[attrPosition.get(t.getTerm())] = t.getFreq();
			}
			
			data.add(new Instance(1.0, vals));
		}
		
		
		System.out.print(data);

		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	

	public static Map<String, Integer> getFrequencies(File document) throws FileNotFoundException{
		Scanner sc = new Scanner(new FileInputStream(document));
		HashMap<String,Integer> freq = new HashMap<String,Integer>();
		
		//int count=0;
		while(sc.hasNext()){
			String n = sc.next().toLowerCase();
			n = n.replaceAll("[^A-Za-z0-9 ]", "");


			//System.out.println("Word " + n);
			
			if(n.length()<1){
				continue;
			}
			
			if(freq.containsKey(n)){
				int number = freq.get(n);
				number++;
				freq.remove(n);
				freq.put(n, number);

				
			}else{
				freq.put(n, 1);
			}
			
			
		    //count++;
		}
		
	return freq;
	}

}
