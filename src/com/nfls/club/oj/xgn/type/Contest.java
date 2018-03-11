package com.nfls.club.oj.xgn.type;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Contest {
	public String name;
	public Map<String,Integer> score=new HashMap<String,Integer>();
	
	public int getContestantScore(String name){
		return score.get(name);
	}
	
	public boolean isContestantRegister(String name){
		return score.containsKey(name);
	}
	
	public int getContestantRank(String name){
		Set<Entry<String,Integer>> s=score.entrySet();
		int rank=1;
		int pts=score.get(name);
		
		for(Entry<String,Integer> e:s){
			if(e.getValue()>pts){
				rank++;
			}
		}
		
		return rank;
	}
	
	public Contest(){
		
	}

	@Override
	public String toString() {
		return "Contest [name=" + name + ", score=" + score + "]";
	}
	
	
}
