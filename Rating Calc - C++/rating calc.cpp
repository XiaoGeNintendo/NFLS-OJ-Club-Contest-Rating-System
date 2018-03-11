#include <iostream>
#include <algorithm>
#include <vector>
#include <set>
#include <queue>
#include <map>
#include <string.h>
#include <math.h>
#include <stdio.h>
#include <deque>
//#include "D:\C++\test_lib_projects\testlib.h"
using namespace std;
#define ll long long
#define pii pair<int,int>

bool debug=true;
/*
   Write In New Computer
    By XiaoGeNintendo
	     gwq2017
	Type:
*/

/*
void fo(int id){
	freopen((toString(id)+".txt").c_str(),"w",stdout);
}
*/

int calcRating(int r,int e,vector<int> s){
	//return (r+e*s.size())/(s.size()+1);
	if(r>e){
		return (r+e*3)/4;
	}else{
		return (r*3+e)/4;
	}
} 

string getTag(int score){
	if(score<=0){
		return "Unrated";
	}
	if(score<1200){
		return "Newbie"; 
	}
	if(score<1400){
		return "Pupil";
	}
	if(score<1600){
		return "Specialist";
	}
	if(score<1900){
		return "Expert";
	}
	if(score<2200){
		return "Candidate master";
	}
	if(score<2300){
		return "Master";
	}
	if(score<2400){
		return "International master";
	}
	if(score<2600){
		return "Grandmaster";
	}
	if(score<2900){
		return "International grandmaster";
	}
	return "Legendary Grandmaster";
}

int main(int argc,char* argv[]){
	string s;
	cout<<"Username:"<<endl;
	cin>>s;
	freopen((s+".txt").c_str(),"r",stdin);
//	int n;
//	cin>>n;
	vector<int> score;
//	for(int i=0;i<n;i++){
//		int a;
//		cin>>a;
//		score.push_back(a);
//	}
	vector<string> name;
	
	string ss;
	int a;
	while(cin>>ss){
		cin>>a;
		score.push_back(a);
		name.push_back(ss);
	}
	
	int e=1500;
	vector<int> sc;
	
	cout<<endl;
	cout<<"Profile information:"<<endl;
	cout<<s<<endl;
	for(int i=0;i<score.size();i++){
		cout<<"#"<<(i+1)<<" "<<name[i]<<" Rank:"<<score[i]<<" Rating:"<<e<<"->"<<calcRating(score[i],e,sc)<<"("<<calcRating(score[i],e,sc)-e<<")"<<endl;
		if(getTag(e)!=getTag(calcRating(score[i],e,sc))){
			cout<<"==Become  "<<getTag(calcRating(score[i],e,sc))<<"=="<<endl;
		}
		
		e=calcRating(score[i],e,sc);
		sc.push_back(score[i]);
	}
	
	cout<<"Last rating:"<<e<<" Title:"<<getTag(e)<<endl;
	return 0;
}

