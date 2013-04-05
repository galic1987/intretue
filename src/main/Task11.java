package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Task11 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
//		boolean stemming = Boolean.getBoolean(args[0]);
//		int lower = Integer.getInteger(args[1]);
//		int upper = Integer.getInteger(args[2]);
		
		try {
		// 1. loop all files in the folder
		final File folder = new File("20_newsgroups_subset");
		
		for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            //System.out.println("Entering folder "+fileEntry.getName());
	            for (File filedeeper : fileEntry.listFiles()){
		            System.out.println("Reading  file "+filedeeper.getName());
		            
		            
		            	System.out.println(getFrequencies(filedeeper).toString());
					
	            }
	        }
	    }
		
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
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


			System.out.println("Word " + n);
			
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
