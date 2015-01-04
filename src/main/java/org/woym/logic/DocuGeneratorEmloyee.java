package org.woym.logic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.woym.logic.spec.IDocuGenerator;
import org.woym.objects.Activity;
import org.woym.objects.Employee;

public class DocuGeneratorEmloyee implements IDocuGenerator {
	
	private Employee employee;
	private List<Activity> activitys;
	
	public DocuGeneratorEmloyee(Employee employee, List<Activity> activitys){
		this.employee = employee;
		this.activitys = activitys;
		this.activitys = getRelatedActivitys();
	}
	
	@Override
	public File generateTxt() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File generateCsv() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File generatePdf() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private List<Activity> getRelatedActivitys(){
		List<Activity> retActivitys = new ArrayList<Activity>();
		for(Activity a:activitys){
			if (a.getSchoolclasses().contains(employee)){
				retActivitys.add(a);
			}
		}
		return retActivitys;
	}
}
