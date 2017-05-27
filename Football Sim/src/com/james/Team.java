package com.james;

public class Team {
	
	int id;
	String name;
	double attack;
	double defence;
	
	double penalites;
	double freekicks;
	
	public Team(int id, String name, double attack, double defence, double pens, double freekicks){
		this.id = id;
		this.name = name;
		this.attack = attack*0.25 ;
		this.defence = (ratioToProbability(defence)+1.8)/3;
		this.penalites = pens;
		this.freekicks = freekicks;
		System.out.println("Generated "+name+". Attack: "+this.attack+" Defence: "+this.defence);
	}
	
	private double ratioToProbability(double ratio){
		return (Math.exp(0.287682*ratio)/Math.exp(0.287682));
	}

}