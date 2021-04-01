package com.foxminded.university;

import com.foxminded.university.model.Cathedra;

public class Main {

	public static void main(String[] args) {
		
		DataCreator data = new DataCreator();
		Cathedra cathedra = data.createData();
		Formatter formatter = new Formatter();
		formatter.printResult(cathedra.getTTForDay(cathedra.getGroups().get(0).getStudents().get(0), 4, 4));
		System.out.println();
		formatter.printResult(cathedra.getTTForDay(cathedra.getTeachers().get(0), 8, 4));
		System.out.println();
		formatter.printResult(cathedra.getTTForMonth(cathedra.getTeachers().get(1), 4));
	}

}
