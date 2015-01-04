package org.woym.logic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.woym.logic.spec.IDocuGenerator;
import org.woym.objects.Activity;
import org.woym.objects.Schoolclass;

public class DocuGeneratorSchoolClass implements IDocuGenerator {
	
	private Schoolclass group;
	private List<Activity> activitys;
	
	public DocuGeneratorSchoolClass(Schoolclass group, List<Activity> activitys){
		this.group = group;
		this.activitys = activitys;
		this.activitys = getRelatedActivitys();
	}
	
	@Override
	public File generateTxt() {
		// TODO implementieren
		return null;
	}

	@Override
	public File generateCsv() {
		// TODO implementieren
		return null;
	}

	@Override
	public File generatePdf() {
		// TODO implementieren
		return null;
	}
	
	/**
	 * sucht aus den klassen activitys eine liste der activitys zurück die mit der group der 
	 * klasse zu tun hat
	 * @return Liste aller relevanten Activitys
	 */
	private List<Activity> getRelatedActivitys(){
		List<Activity> retActivitys = new ArrayList<Activity>();
		for(Activity a:activitys){
			if (a.getSchoolclasses().contains(group)){
				retActivitys.add(a);
			}
		}
		return retActivitys;
	}
}
