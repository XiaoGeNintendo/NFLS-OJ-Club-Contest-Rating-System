package com.hhs.xgn.rating.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Contest {
	public String name;
	public int score;
	public List<String> tags=new ArrayList<String>();
	public boolean isRated;
	
	public Contest(){}
	public Contest(String a,int b){
		name=a;
		score=b;
	}
	
	public Contest(String a,int b,boolean c){
		name=a;
		score=b;
		isRated=c;
	}
	
	public Contest(String a,int b,String... c){
		name=a;
		score=b;
		tags=Arrays.asList(c);
	}
}
