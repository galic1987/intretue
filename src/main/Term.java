package main;

public class Term {

	
	private String term;
	private int freq;
	
	private int docFreq;

	public int getDocFreq() {
		return docFreq;
	}

	public void setDocFreq(int docFreq) {
		this.docFreq = docFreq;
	}

	public int getFreq() {
		return freq;
	}

	public void setFreq(int freq) {
		this.freq = freq;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}
	
	
}