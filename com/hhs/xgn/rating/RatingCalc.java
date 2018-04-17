package com.hhs.xgn.rating;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.hhs.xgn.rating.type.Contest;
import com.hhs.xgn.rating.type.Contribution;
import com.hhs.xgn.rating.type.Rating;

public class RatingCalc {

	static List<Rating> lr=new ArrayList<Rating>();
	
	public static void main(String[] args) throws IOException {
		System.out.println("Loading user file...");
		
		File f=new File("user");
		File f2=new File("out");
		if(f.exists()==false){
			f.mkdirs();
			System.out.println("Directory made!");
		}
		if(f2.exists()==false){
			f2.mkdirs();
			System.out.println("D2 Made 2!");
		}
		
		for(File fs:f.listFiles()){
			System.out.println(fs.getAbsolutePath());
			
			Rating r=new Rating();
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(fs)));
			
			r.username=br.readLine();
			
			String s="";
			while((s=br.readLine())!=null){
				String[] str=s.split(" ");
				if(str[0].equals("R")){
					Contest c=new Contest(str[1],Integer.parseInt(str[2]),true);
					for(int i=3;i<str.length;i++){
						c.tags.add(str[i]);
					}
					
					r.score.add(c);
					
				}
				if(str[0].equals("C")){
					Contribution c=new Contribution(str[1],Integer.parseInt(str[2]));
					r.con.add(c);
				}
				if(str[0].equals("I")){
					Contest c=new Contest(str[1],Integer.parseInt(str[2]),false);
					r.score.add(c);
				}
				
			}
			
			lr.add(r);
			br.close();
		}
		
		System.out.println("Sorting");
		
		lr.sort(new Comparator<Rating>() {

			@Override
			public int compare(Rating o1, Rating o2) {
				if(qc(o1)<qc(o2)){
					return 1;
				}
				if(qc(o1)>qc(o2)){
					return -1;
				}
				return 1-o1.username.compareTo(o2.username);
			}
		});
		
		
		System.out.println("Start creating");
		
		PrintWriter pw=new PrintWriter("out/report.html");
		
		pw.println("<h1>NFLS OJ Club Rating System Report</h1>");
		pw.println("<h2>Sorted by rating.</h2><hr/>");
		
		for(Rating r:lr){
			System.out.println("Creating:"+r.username);
			int rating=1500;
			
			pw.println("<h2>"+getColoredText(r.username,qc(r))+"</h2>");
			
			pw.println("<b>Rating</b><br/>");
			pw.println("Total joined:"+r.score.size()+"</br>");
			pw.println("Rating:"+getColoredText(qc(r)+"",qc(r))+"(Max. "+getColoredText(mc(r)+"",mc(r))+")<br/>");
			
			for(Contest c:r.score){
				
				if(c.isRated){
					int sc=getScore(c);
					
					int nr=Calc(rating,sc);
					
					if(nr>=rating){
						pw.println("<font color=\"green\">"+c.name+" Rating:"+rating+"->"+nr+"(+"+(nr-rating)+")</font>Rank After Change:"+getColoredText(r.username,nr)+"<br/>");
						pw.println("<i>Normal score:"+c.score+", Final score:"+sc+", Score Multipliers:"+getMutlipiers(c)+"</i><br/>");
						
					}else{
						pw.println("<font color=\"red\">"+c.name+" Rating:"+rating+"->"+nr+"("+(nr-rating)+")</font>Rank After Change:"+getColoredText(r.username,nr)+"<br/>");
						pw.println("<i>Normal score:"+c.score+", Final score:"+sc+", Score Multipliers:"+getMutlipiers(c)+"</i><br/>");
						
					}
					rating=nr;
					
				}else{
					pw.println("<i>"+c.name+" Score:"+c.score+" (Unrated) </i> <br/>");
				}
			}
			
			putScoreImage(r);
			
			pw.println("<img src=\""+r.username+".jpeg\" alt=\"Rating Graph\">");
			
			pw.println("<br/><b>Contributions</b><br/>");
			
			int a=0;
			for(Contribution c:r.con){
				a+=c.value;
			}
			
			if(a>0){
				pw.println("Contribution:<font color=\"green\">+"+a+"</font> <br/>");
			}else{
				pw.println("Contribution:<font color=\"grey\">"+a+"</font> <br/>");
			}
			
			for(Contribution c:r.con){
				pw.println(c.name+" : "+c.value+"<br/>");
			}
			
			pw.println("<hr/>");
		}
		
		pw.println("<i>Made by NFLS OJ Club Contest Rating System. By XiaoGeNintendo from Hell Hole Studios.</i>");
		pw.close();
		System.out.println("All done! see out/report.html");
		
	}

	private static void putScoreImage(Rating r) {
		JFreeChart jfc=ChartFactory.createXYLineChart("Rating graph of "+r.username, "Time", "Rating", createDataset(r),PlotOrientation.VERTICAL,true,true,false);
		
		
		final XYPlot plot = jfc.getXYPlot( );
		
		
	    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
	    renderer.setSeriesPaint( 0 , Color.RED );
	    renderer.setSeriesStroke( 0 , new BasicStroke( 4.0f ) );
	    
	    
	    plot.setRenderer( renderer );
		
	    try {
	    	
			ChartUtilities.saveChartAsJPEG(new File("out/"+r.username+".jpeg"),1.0f,jfc, 500, 250);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static XYDataset createDataset(Rating r) {
		final XYSeries xys=new XYSeries("Rating");
		
		int rating=1500;
		int x=1;
		for(Contest c:r.score){
			if(c.isRated){
				rating=Calc(rating, getScore(c));
				xys.add(x, rating);
				
			}
			
			x++;
		}
		
		final XYSeriesCollection xysc=new XYSeriesCollection();
		xysc.addSeries(xys);
		
		return xysc;
	}

	private static String getMutlipiers(Contest c) {
		String s="";
		for(String sc:c.tags){
			s+=getColoredMutliplier(sc)+" ";
		}
		return s;
	}

	private static String getColoredMutliplier(String s) {
		if(s.equals("MAIN")){
			return "<font color=#123456>OJ Club Main Round(*1.0)</font>";
		}
		if(s.equals("EDUC")){
			return "<font color=#654321>OJ Club Eductional Round(*1.0)</font>";
		}
		if(s.equals("NOIP")){
			return "<font color=#df2333>CCF NOIP(*12.0)</font>";
		}
		if(s.equals("CODE")){
			return "<font color=#FEDCBA>Codeforces Round(*1.75)</font>";
		}
		if(s.equals("ATCO")){
			return "<font color=#123FED>Atcoder(*3.0)</font>";
		}
		if(s.equals("ITSA")){
			return "<font color=#11ff11>It's a drop-score party activity(*1.05)</font>";
		}
		return "<font color=#999999>Other(*1.0)</font>";
	}

	private static int getScore(Contest c) {
		int score=c.score;
		for(String s:c.tags){
			score=(int) (score*getMultiplier(s));
		}
		return score;
	}

	/**
	 * Main round  ¡Á1.0 £¨MAIN£©
		Educational Round ¡Á1.0 £¨EDUC)
		NOIP ¡Á12.0 (NOIP)
		Codeforces ¡Á1.75 (CODE)
		Atcoder ¡Á3.0 (ATCO)
	 * @param s
	 * @return
	 */
	private static float getMultiplier(String s) {
		if(s.equals("MAIN")){
			return 1.0f;
		}
		if(s.equals("EDUC")){
			return 1.0f;
		}
		if(s.equals("NOIP")){
			return 12.0f;
		}
		if(s.equals("CODE")){
			return 1.75f;
		}
		if(s.equals("ATCO")){
			return 3.0f;
		}
		if(s.equals("ITSA")){
			return 1.05f;
		}
		return 1.0f;
	}

	private static int mc(Rating r) {
		int rating=1500;
		int mx=-1;
		for(Contest c:r.score){
			if(c.isRated){
				rating=Calc(rating,getScore(c));
				mx=Math.max(rating, mx);
			}
		}
		
		return mx;
	}

	private static String getColoredText(String kyu, int rating) {
		if (rating <= 1200) {
			return "<font color=\"grey\">" + kyu + "</font>";
		}
		if (rating <= 1400) {
			return "<font color=\"green\">" + kyu + "</font>";
		}
		if (rating <= 1600) {
			return "<font color=\"aqua\">" + kyu + "</font>";
		}
		if (rating <= 1900) {
			return "<font color=\"blue\">" + kyu + "</font>";
		}
		if (rating <= 2200) {
			return "<font color=\"purple\">" + kyu + "</font>";
		}
		if (rating <= 2400) {
			return "<font color=\"orange\">" + kyu + "</font>";
		}
		if (rating <= 2900) {
			return "<font color=\"red\">" + kyu + "</font>";
		}

		return "<font color=\"black\">" + kyu.charAt(0) + "</font><font color=#ff0000>" + kyu.substring(1) + "</font>";
	}

	private static int qc(Rating r) {
		
		int rating=1500;
		for(Contest c:r.score){
			if(c.isRated){
				rating=Calc(rating,getScore(c));
			}
		}
		
		return rating;
	}

	private static int Calc(int rating, int score) {
		
		return (score*3+rating*2)/5;
	}

}
