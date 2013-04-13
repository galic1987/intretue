package main;

public class TermInfo {

	private int freq;
	
	private int docFreq;
	private double idf;

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

	public double getIdf() {
		return idf;
	}

	public void setIdf(double idf) {
		this.idf = idf;
	}
	
}
