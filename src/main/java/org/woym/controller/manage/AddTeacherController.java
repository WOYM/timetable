package org.woym.controller.manage;

import java.math.BigDecimal;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.primefaces.context.RequestContext;
import org.woym.objects.Teacher;

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
    	teacher.setFirstName(name);
    	teacher.setSymbol(symbol);
    	teacher.setHoursPerWeek(new BigDecimal(weekhours));
        RequestContext.getCurrentInstance().closeDialog(teacher);
    }
	
	
}
