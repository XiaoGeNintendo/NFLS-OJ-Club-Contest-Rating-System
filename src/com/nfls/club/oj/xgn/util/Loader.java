package com.nfls.club.oj.xgn.util;

import java.awt.BufferCapabilities;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import com.nfls.club.oj.xgn.type.Contest;
import com.nfls.club.oj.xgn.type.Contestant;
import com.nfls.club.oj.xgn.type.Record;

public class Loader {
	public static Contest[] readContest() throws IOException{
		File f=new File("contest");
		if(!f.exists()){
			f.mkdir();
		}
		String[] s=f.list();
		Contest[] c=new Contest[s.length];
		
		for(int i=0;i<s.length;i++){
			c[i]=readSingleContest(s[i]);
		}
		return c;
	}
	
	private static Contest readSingleContest(String path) throws IOException{
		
		Contest c=new Contest();
		File f=new File("contest/"+path);
		BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(f)));
		
		c.name=br.readLine();
		
		int cnt=Integer.parseInt(br.readLine());
		
		for(int i=0;i<cnt;i++){
			String[] s=br.readLine().split(" ");
			c.score.put(s[0], Integer.parseInt(s[1]));
			
		}
		
		br.close();
		return c;
	}
	
	public static Contestant[] readContestant() throws IOException{
		File f=new File("contestant");
		if(!f.exists()){
			f.mkdir();
		}
		String[] s=f.list();
		Contestant[] c=new Contestant[s.length];
		
		for(int i=0;i<s.length;i++){
			c[i]=readSingleContestant(s[i]);
		}
		return c;
	}
	
	private static Contestant readSingleContestant(String path) throws IOException{
		
		Contestant c=new Contestant();
		File f=new File("contestant/"+path);
		BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(f)));
		
		c.name=br.readLine();
		
		int cnt=Integer.parseInt(br.readLine());
		
		for(int i=0;i<cnt;i++){
			c.record.add(new Record(br.readLine()));
		}
		
		br.close();
		return c;
	}

	public static String[] readHold() throws NumberFormatException, IOException {
		File f=new File("hold.txt");
		BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(f)));
		
		int cnt=Integer.parseInt(br.readLine());
		String[] s=new String[cnt];
		
		for(int i=0;i<cnt;i++){
			s[i]=br.readLine();
		}
		
		br.close();
		return s;
	}
}
