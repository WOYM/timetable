package com.woym.objects;

import java.io.Serializable;

public class Teacher implements Serializable {

	private static final long serialVersionUID = -2846205796145565740L;

	private String name;
	
	private String symbol;
	
	private Integer weekhours;
	
	public Teacher() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Integer getWeekhours() {
		return weekhours;
	}

	public void setWeekhours(Integer weekhours) {
		this.weekhours = weekhours;
	}
}
