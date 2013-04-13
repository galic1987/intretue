package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import weka.core.stemmers.*;

public class Task11 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
		
		
		// Standard arguments
		boolean stemming = Boolean.parseBoolean(args[0]);
		int lower = Integer.parseInt(args[1]);
		int upper = Integer.parseInt(args[2]);
		
	
		
		// This is stemmer working , TODO: I dont know where should we implement this? On input word query?
		LovinsStemmer s = new LovinsStemmer();
		System.out.print(s.stem("birds"));
		System.out.print(s.stem("pears"));
		System.out.print(s.stemString("what should I stem here in this house"));


		
		
		
		// 1. loop all files in the folder
		final File folder = new File("20_newsgroups_subset");
		
		// TODO: 
		HashMap<String,Term> term = new HashMap<String,Term>();
		
		HashMap<String,List> postingLists = new HashMap<String,List>();
		
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
		            	Map m = getFrequencies(filedeeper);
		            	
		            	
		            	
		            	// iterate the whole term hastable add posting lists
		            	Iterator it = m.entrySet().iterator();
		                while (it.hasNext()) {
		                    Map.Entry pairs = (Map.Entry)it.next();
//		                    System.out.println(pairs.getKey() + " = " + pairs.getValue());
		                    if(postingLists.containsKey(pairs.getKey())){
		                    	List l = postingLists.get(pairs.getKey());
		                    	l.add(filedeeper);
		                    }else{
		                    	postingLists.put((String) pairs.getKey(), new ArrayList<String>());
		                    }
		         
		                    it.remove(); // avoids a ConcurrentModificationException
		                }
		            	
		            	
	            	
	            		// total term frequencies
		            	
		            	// posting list	            		
	            		
	            	
		            	
		            	
					
	            }
	        }
	    }
		
		//System.out.println(numberOfDocuments);
		//System.out.println(postingLists.toString());

		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	

	public static Map getFrequencies(File document) throws FileNotFoundException{
		
		EmailValidator valid = new EmailValidator();
		Scanner sc = new Scanner(new FileInputStream(document));
		HashMap<String,Integer> freq = new HashMap<String,Integer>();
		
		//int count=0;
		while(sc.hasNext()){
			String n = sc.next().toLowerCase();
			
			if(!valid.validate(n)){
				n = n.replaceAll("[^A-Za-z0-9 ]", "");
			}


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
