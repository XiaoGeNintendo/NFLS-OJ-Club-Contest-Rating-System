package com.nfls.club.oj.xgn.type;

import java.util.ArrayList;
import java.util.List;

public class Contestant {
	public String name;
	public List<Record> record=new ArrayList<Record>();
	public int rating=0;
	
	public int getRating(){
		return rating;
	}
	
	public Contestant(String name){
		this.name=name;
	}
	
	public Contestant(){
		
	}

	@Override
	public String toString() {
		return "Contestant [name=" + name + ", record=" + record + "]";
	}
}
