package main;

public class Similarity implements Comparable<Similarity> {

	private String document;
	private double cosineSimilarity;
	
	public Similarity(String document, double cosineSimilarity) {
		this.document = document;
		this.cosineSimilarity = cosineSimilarity;
	}
	
	public String getDocument() {
		return document;
	}
	public void setDocument(String document) {
		this.document = document;
	}
	public double getCosineSimilarity() {
		return cosineSimilarity;
	}
	public void setCosineSimilarity(double cosineSimilarity) {
		this.cosineSimilarity = cosineSimilarity;
	}
	
	@Override
	public int compareTo(Similarity o) {
		return Double.compare(o.getCosineSimilarity(), this.getCosineSimilarity());
	}
	
	
}
