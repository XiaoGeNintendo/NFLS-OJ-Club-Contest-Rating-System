package com.nfls.club.oj.xgn.type;

public class Record {
	public String name;
	public boolean finish=false;
	
	public int score;
	public int rank;
	public int ratingBefore;
	public int ratingAfter;
	public Record(String name, int score, int rank, int ratingBefore, int ratingAfter) {
		this.name = name;
		this.score = score;
		this.rank = rank;
		this.ratingBefore = ratingBefore;
		this.ratingAfter = ratingAfter;
		this.finish=true;
	}
	
	public Record(String name){
		this.name=name;
		this.finish=false;
	}
	public Record(){
		
	}

	@Override
	public String toString() {
		
		return "Record [name=" + name + ", finish=" + finish + ", score=" + score + ", rank=" + rank + ", ratingBefore="
				+ ratingBefore + ", ratingAfter=" + ratingAfter + "]";
	}
}
