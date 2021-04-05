package com.foxminded.university;

import com.foxminded.university.model.Cathedra;

public class Main {

	public static void main(String[] args) {
		DataCreator data = new DataCreator();
		Cathedra cathedra = data.createData();
		Formatter formatter = new Formatter();
		/*
		System.out
				.println(formatter.format(cathedra.getTTForDay(cathedra.getGroups().get(0).getStudents().get(0), 4, 4))
						+ System.lineSeparator());
		System.out.println(
				formatter.format(cathedra.getTTForDay(cathedra.getTeachers().get(0), 8, 4)) + System.lineSeparator());
		System.out.println(formatter.format(cathedra.getTTForMonth(cathedra.getTeachers().get(1), 4)));
		*/
		MenuCreator menu = new MenuCreator(cathedra);
		menu.buildMenu();
	}
}
