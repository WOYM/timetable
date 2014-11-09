package com.woym.controller.manage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import com.woym.objects.Teacher;

@SessionScoped
@ManagedBean(name = "teacherController")
public class TeacherController implements Serializable {

	private static final long serialVersionUID = -2341971622906815080L;
	ArrayList<Teacher> teachers = new ArrayList<>();

	private Teacher selectedTeacher;

	/**
	 * Enthält nur Testdaten
	 */
	@PostConstruct
	public void init() {
		Teacher teacher1 = new Teacher();
		teacher1.setName("Herr Meyer");
		teacher1.setSymbol("MEY");
		teacher1.setWeekhours(40);
		teachers.add(teacher1);

		Teacher teacher2 = new Teacher();
		teacher2.setName("Herr Schulz");
		teacher2.setSymbol("SCH");
		teacher2.setWeekhours(40);
		teachers.add(teacher2);

		Teacher teacher3 = new Teacher();
		teacher3.setName("Herr Müller");
		teacher3.setSymbol("MUE");
		teacher3.setWeekhours(40);
		teachers.add(teacher3);
	}

	public ArrayList<Teacher> getTeachers() {
		return teachers;
	}

	public void addTeacherDialog() {

		Map<String, Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", false);
		options.put("resizable", false);
		options.put("contentHeight", 400);
		options.put("contentWidth", 600);

		RequestContext rc = RequestContext.getCurrentInstance();
		rc.openDialog("manageTeachersDialog", options, null);
	}

	public void editTeacherDialog() {

		Map<String, Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", false);
		options.put("resizable", false);
		options.put("contentHeight", 400);
		options.put("contentWidth", 600);
		
		RequestContext rc = RequestContext.getCurrentInstance();
		rc.openDialog("editTeacherDialog", options, null);
	}

	public void onTeacherAdded(SelectEvent event) {

		Teacher teacher = (Teacher) event.getObject();

		for (Teacher currentTeacher : teachers) {
			if (currentTeacher.getSymbol().equals(teacher.getSymbol())) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Lehrer existiert bereits", null);
				FacesContext.getCurrentInstance().addMessage(null, message);
				return;
			}
		}

		teachers.add(teacher);

		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Lehrer hinzugefügt", teacher.getName() + " (" + teacher.getSymbol() + ")");
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void deleteTeacher() {
		if (selectedTeacher != null) {
			if (teachers.contains(selectedTeacher)) {
				teachers.remove(selectedTeacher);
				
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Lehrer entfernt", selectedTeacher.getName() + " (" + selectedTeacher.getSymbol() + ")");
				FacesContext.getCurrentInstance().addMessage(null, message);
			}
		}
	}

	public Teacher getSelectedTeacher() {
		return selectedTeacher;
	}

	public void setSelectedTeacher(Teacher selectedTeacher) {
		this.selectedTeacher = selectedTeacher;
	}
	
	public int getAmountOfTeachers() {
		return teachers.size();
	}
}
