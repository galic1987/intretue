package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.Scanner;

import javax.swing.ListModel;

import org.w3c.dom.views.DocumentView;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.Saver;
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
		
		
		// 1. loop all files in the folder
		final File folder = new File("20_newsgroups_subset");
		
		// TODO: 
		HashMap<String,List<String>> postingLists = new HashMap<String,List<String>>();
		HashMap<String, HashMap<String, TermInfo>> documents = new HashMap<String, HashMap<String, TermInfo>>();
		
		int numberOfDocuments = 0; // N 
		
		for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            //System.out.println("Entering folder "+fileEntry.getName());
	            for (File filedeeper : fileEntry.listFiles()){
	            	numberOfDocuments++;
		            // System.out.println("Reading  file "+filedeeper.getName());
	            	//	System.out.println(getFrequencies(filedeeper).toString());
	            	
	            		// how often some terms come in one document
		            	Map<String, Integer> m = getFrequencies(filedeeper,stemming);
		            	HashMap<String, TermInfo> listOfTerms = new HashMap<String, TermInfo>();
		            	
		            	// iterate the whole term hastable add posting lists
		            	Iterator<Entry<String, Integer>> it = m.entrySet().iterator();
		                while (it.hasNext()) {
		                    Entry<String, Integer> pairs = it.next();
//		                    System.out.println(pairs.getKey() + " = " + pairs.getValue());
		                    
		                    // frequency thresholding
		                    int frequency = pairs.getValue();
		                    if(frequency < lower || frequency > upper){
		                    	continue;
		                    }
		                    
		                    if(postingLists.containsKey(pairs.getKey())){
		                    	List<String> l = postingLists.get(pairs.getKey());
		                    	l.add(filedeeper.getName());
		                    }else{
		                    	List<String> l = new ArrayList<String>();
		                    	l.add(filedeeper.getName());
		                    	postingLists.put(pairs.getKey(), l);
		                    }
		                    
		                    it.remove(); // avoids a ConcurrentModificationException
		                    
		                    TermInfo t = new TermInfo();
		                    t.setFreq(pairs.getValue());
		                    listOfTerms.put(pairs.getKey(), t);
		                }
	            	
	            		// total term frequencies
		            	documents.put(filedeeper.getName(), listOfTerms);		            	
	            }
	        }
	    }
		
		//System.out.println(numberOfDocuments);
		System.out.println("sdadasd     " + postingLists.size());
		
		
		
		
		 ArffSaver saver = new ArffSaver();
		 saver.setCompressOutput(false);

		 saver.setFile(new File("./data/test.arff"));
		 saver.setDestination(new File("./data/test.arff"));   // **not** necessary in 3.5.4 and later
		 saver.setRetrieval(Saver.BATCH);
		 
		 
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

		//saver.writeBatch();
		saver.setRetrieval(Saver.INCREMENTAL);
		 saver.setStructure(data);
		 
//System.out.println(saver.getOptions().toString());
		
		
		
		
		 
		//go through all documents and compute idf
		Iterator<Entry<String, HashMap<String, TermInfo>>> itDoc = documents.entrySet().iterator();
		while(itDoc.hasNext()) {
			Entry<String, HashMap<String, TermInfo>> entry = itDoc.next();
			HashMap<String, TermInfo> listOfTerms = entry.getValue();
			String document = entry.getKey();
			
			//arff
			vals = new double[data.numAttributes()];
			vals[0] = data.attribute(0).addStringValue(document);
			
			Iterator<Entry<String, TermInfo>> itTerm = listOfTerms.entrySet().iterator();
			while(itTerm.hasNext()) {
				Entry<String, TermInfo> entryTerm = itTerm.next();
				TermInfo t = entryTerm.getValue();
				String term = entryTerm.getKey();
				
				t.setDocFreq(postingLists.get(term).size());
				
				//idf = log (1 + termfrequency) * log ( N / documentfrequency)
				t.setIdf(Math.log10(1 + t.getFreq()) * Math.log10(numberOfDocuments / t.getDocFreq()));

				vals[attrPosition.get(term)] = t.getIdf();
				//vals[attrPosition.get(t.getTerm())] = t.getFreq();
			}
			
			//data.add(new Instance(1.0, vals));
			 
			//saver.writeIncremental(new Instance(1.0, vals));

		}
		
		
		/*
		 * misc.forsale/76057
		 * talk.religion.misc/83561
		 * talk.politics.mideast/75422
		 * sci.electronics/53720
		 * sci.crypt/15725
		 * misc.forsale/76165
		 * talk.politics.mideast/76261
		 * alt.atheism/53358
		 * sci.electronics/54340
		 * rec.motorcycles/104389
		 * talk.politics.guns/54328
		 * misc.forsale/76468
		 * sci.crypt/15469
		 * rec.sport.hockey/54171
		 * talk.religion.misc/84177
		 * rec.motorcycles/104727
		 * comp.sys.mac.hardware/52165
		 * sci.crypt/15379
		 * sci.space/60779
		 * sci.med/59456
		 */
		
		//first document
		String currentTopic = "76057";
		
		//get all terms for the document
		HashMap<String, TermInfo> topicTerms = documents.get(currentTopic);
		
		ArrayList as = new ArrayList(topicTerms.entrySet());  
        
		//sort terms by ifd
        Collections.sort( as , new Comparator() {  
            public int compare( Object o1 , Object o2 )  
            {
                Map.Entry e1 = (Map.Entry)o1;  
                Map.Entry e2 = (Map.Entry)o2;
                TermInfo first = (TermInfo)e1.getValue();
                TermInfo second = (TermInfo)e2.getValue();
                return Double.compare(second.getIdf(), first.getIdf());  
            }
        });
        
        Vector<Double> topicVector = new Vector<Double>();
        List<String> topicTopTerms = new ArrayList<String>();
        
        //select top 20 terms, get topic term vector
        Iterator itSorted = as.iterator();
        for (int i=0; i<20 && itSorted.hasNext(); i++) {
        	Map.Entry<String, TermInfo> sortedTerm = (Entry<String, TermInfo>) itSorted.next();
        	
        	topicVector.add(sortedTerm.getValue().getIdf());
        	topicTopTerms.add(sortedTerm.getKey());
        	
        	//System.out.println(sortedTerm.getKey() + " " + sortedTerm.getValue().getIdf());
        }
		
        
        PriorityQueue<Similarity> similarDocuments = new PriorityQueue<Similarity>();
        //compute cosine similarity between all documents
        for (Entry<String, HashMap<String, TermInfo>> documentEntry : documents.entrySet()) {
        	//skip selected document
        	if (!documentEntry.getKey().equals(currentTopic)) {
        		
        		HashMap<String, TermInfo> documentTerms = documentEntry.getValue();
        		
        		//get current document vector for top terms
        		Vector<Double> documetVector = new Vector<Double>();
        		for (String term : topicTopTerms) {
        			if (documentTerms.containsKey(term)) {
        				documetVector.add(documentTerms.get(term).getIdf());
        			} else {
        				documetVector.add(0.0);
        			}
        		}
        		
        		//compute the similarity
        		double dotProduct = 0;
        		double lengthTopic = 0;
        		double lengthDocument = 0;
        		for (int i=0; i<topicVector.size(); i++) {
        			dotProduct += topicVector.get(i) * documetVector.get(i);
        			lengthTopic += Math.pow(topicVector.get(i), 2);
        			lengthDocument += Math.pow(documetVector.get(i), 2);
        		}
        		lengthTopic = Math.sqrt(lengthTopic);
        		lengthDocument = Math.sqrt(lengthDocument);
        		
        		double cosineSimilarity = 0;
        		
        		if (lengthDocument > 0) {
        			cosineSimilarity = dotProduct / (lengthTopic * lengthDocument);
        		}
        		
        		similarDocuments.add(new Similarity(documentEntry.getKey(), cosineSimilarity));
        	}
        }

        for (Similarity s : similarDocuments) {
        	System.out.println(s.getDocument() + " " + s.getCosineSimilarity());
        }
		
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	

	public static Map<String, Integer> getFrequencies(File document, boolean stemming) throws FileNotFoundException{
		Scanner sc = new Scanner(new FileInputStream(document));
		HashMap<String,Integer> freq = new HashMap<String,Integer>();
		LovinsStemmer s = new LovinsStemmer();
		
		//int count=0;
		while(sc.hasNext()){
			String n = sc.next().toLowerCase();
			n = n.replaceAll("[^A-Za-z0-9 ]", "");
			
			if(stemming){
			n = s.stem(n);
			}

			//System.out.println("Word " + n);
			
			if(n.length()<=1){
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
