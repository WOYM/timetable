package org.woym.objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;

@Entity
@Inheritance
public abstract class Staff {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(nullable=false)
	private String name;
	
	@Column(unique=true, nullable=false)
	private String symbol;
	
	@Column(nullable=false)
	private Integer weekhours;
	
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
