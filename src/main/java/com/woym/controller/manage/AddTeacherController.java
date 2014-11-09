package com.woym.controller.manage;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.primefaces.context.RequestContext;

import com.woym.objects.Teacher;

@ManagedBean(name="addTeacherController")
public class AddTeacherController {

	// Lehrer-Objekt
	private Teacher teacher;
	
	private String name;
	
	private String symbol;
	
	private Integer weekhours;
	
	
	@PostConstruct
	public void init() {
		teacher = new Teacher();
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
	
    public void addTeacherFromDialog() {
    	teacher.setName(name);
    	teacher.setSymbol(symbol);
    	teacher.setWeekhours(weekhours);
        RequestContext.getCurrentInstance().closeDialog(teacher);
    }
	
	
}
