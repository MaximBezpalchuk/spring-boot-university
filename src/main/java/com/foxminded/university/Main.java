package com.foxminded.university;

import com.foxminded.university.model.Cathedra;

public class Main {

	public static void main(String[] args) {
		DataCreator data = new DataCreator();
		Cathedra cathedra = data.createData();
		MenuCreator menu = new MenuCreator(cathedra);
		menu.buildMenu();
	}
}
