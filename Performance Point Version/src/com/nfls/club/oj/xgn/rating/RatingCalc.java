package com.nfls.club.oj.xgn.rating;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map.Entry;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import com.nfls.club.oj.xgn.type.Contest;
import com.nfls.club.oj.xgn.type.Contestant;
import com.nfls.club.oj.xgn.type.Record;
import com.nfls.club.oj.xgn.util.Loader;

public class RatingCalc {

	public static void main(String[] args) throws IOException {
		Calc c = new Calc();
		c.calc();
	}

}

class Calc {
	Contest[] con;
	Contestant[] cont;
	String[] hold;

	public void calc() throws IOException {
		System.out.println("Rating Calc for NFLS Oj Club Contests");
		System.out.println("Using Performance Point.");
		System.out.println("Reading contests.");
		con = Loader.readContest();

		for (Contest c : con) {
			System.out.println(c);
		}

		System.out.println("Reading contestants.");

		cont = Loader.readContestant();

		for (Contestant c : cont) {
			System.out.println(c);
		}

		System.out.println("Reading list of contests");

		hold = Loader.readHold();

		for (String h : hold) {
			System.out.println(h);
		}

		System.out.println("Calc contest start!!!");

		for (int i = 0; i < hold.length; i++) {
			System.out.println("Running:" + hold[i]);
			CalcContest(hold[i]);
			// for(Contestant cc:cont){
			// System.out.println(cc);
			// }
		}

		System.out.println("Calc ok! Score outputting..");
		if (new File("out").exists() == false) {
			new File("out").mkdirs();
		}

		PrintWriter pw = new PrintWriter(new FileOutputStream("out/" + System.currentTimeMillis() + " - report.html"));

		pw.println("<!--Made By XGN's Rating System-->");

		pw.println("<h1>Rating Table</h1>");
		pw.println("<p>Made By XGN's Rating System</p><br/><hr/>");

		pw.println("<h2>Contestant Result</h2><hr/>");
		for (Contestant c : cont) {
			pw.println("<h3>" + c.name + "</h3>");
			pw.println("Rating now:" + c.getRating()+" "+getKyu(c.getRating()));

			pw.println("Total joined:" + c.record.size() + "<br/>");
			for (Record r : c.record) {
				pw.println("<font size=2 color=\"" + ((r.ratingAfter - r.ratingBefore) > 0 ? "green" : "red") + "\">"
						+ r.name + " score:" + r.score + " rank:" + r.rank + " rating:" + r.ratingBefore + "->"
						+ r.ratingAfter + "(" + (r.ratingAfter - r.ratingBefore) + ")</font> After contest: "
						+ getKyu(r.ratingAfter) + "<br/>");
			}
			pw.println("<hr/>");
		}

		pw.println("<h2>Contest Result</h2><hr/>");
		for (Contest c : con) {
			pw.println("<h3>" + c.name + "</h3>");
			pw.println("Total joined:" + c.score.size() + "<br/>");
			for (Entry<String, Integer> esi : c.score.entrySet()) {
				pw.println("<font size=2>" + esi.getKey() + " score:" + esi.getValue() + " rank:"
						+ c.getContestantRank(esi.getKey()) + " </font><br/>");
			}
			pw.println("<hr/>");
		}
		pw.close();
		System.out.println("OK! Please visit your out folder");
	}

	String getKyu(int rating) {

		String kyu = "Lv." + (rating / 100 + 1) + " <i>" + getKyuName(rating) + "</i>";

		return getColor(kyu, rating);

	}

	String getKyuName(int rating){
		if(rating<=100){
			return "Newbie";
		}
		if(rating<=200){
			return "Pupil";
		}
		if(rating<=300){
			return "Specialist";
		}
		if(rating<=400){
			return "Expert";
		}
		if(rating<=500){
			return "Candidate Master";
		}
		if(rating<=600){
			return "Master";
		}
		if(rating<=700){
			return "Grandmaster";
		}
		return "Legendary Grandmaster";
	}
	
	String getColor(String kyu, int rating) {
		if (rating <= 100) {
			return "<font color=\"grey\">" + kyu + "</font><br/>";
		}
		if (rating <= 200) {
			return "<font color=\"green\">" + kyu + "</font><br/>";
		}
		if (rating <= 300) {
			return "<font color=\"aqua\">" + kyu + "</font><br/>";
		}
		if (rating <= 400) {
			return "<font color=\"blue\">" + kyu + "</font><br/>";
		}
		if (rating <= 500) {
			return "<font color=\"purple\">" + kyu + "</font><br/>";
		}
		if (rating <= 600) {
			return "<font color=\"orange\">" + kyu + "</font><br/>";
		}
		if (rating <= 700) {
			return "<font color=\"red\">" + kyu + "</font><br/>";
		}

		return "<font color=\"black\">" + kyu.charAt(0) + "</font><font color=#ff0000>" + kyu.substring(1) + "</font><br/>";
	}

	Contest getContestByName(String name) {
		for (Contest c : con) {
			if (c.name.equals(name)) {
				return c;
			}
		}
		return null;
	}

	Contestant getContestantByName(String name) {
		for (Contestant c : cont) {
			if (c.name.equals(name)) {
				return c;
			}
		}
		return null;
	}

	int getContestantIDByName(String name) {
		for (int i = 0; i < cont.length; i++) {
			if (cont[i].name.equals(name)) {
				return i;
			}
		}
		return -1;
	}

	int getRecordPos(List<Record> record, String name) {
		for (int i = 0; i < record.size(); i++) {
			if (record.get(i).name.equals(name)) {
				return i;
			}
		}
		return -1;
	}

	int PP(int other, int win, int lose, int games) {

		return Math.max((other + 400 * (win - lose)) / games, 0);
	}

	void CalcContest(String name) {
		int totalRating = 0;
		Contest self = getContestByName(name);

		// Calc total rating
		for (String ct : self.score.keySet()) {
			totalRating += getContestantByName(ct).getRating();
		}

		// For each person. Calc the rank
		for (String ct : self.score.keySet()) {
			int cst = getContestantIDByName(ct);

			int other = totalRating - cont[cst].getRating();
			int lose = self.getContestantRank(ct) - 1;
			int win = self.score.size() - lose - 1;
			int games = self.score.size() - 1;

			// System.out.println("For "+ct+" we have (other lose win
			// games):"+other+","+lose+","+win+","+games);

			int pos = getRecordPos(cont[cst].record, self.name);

			cont[cst].record.set(pos, new Record(self.name, self.getContestantScore(ct), self.getContestantRank(ct),
					cont[cst].getRating(), PP(other, win, lose, games)));
			cont[cst].rating = PP(other, win, lose, games);
		}

	}

}