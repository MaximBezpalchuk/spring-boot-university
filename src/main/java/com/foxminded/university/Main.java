package com.foxminded.university;


import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.foxminded.university.config.SpringConfig;
import com.foxminded.university.model.Cathedra;

public class Main {

	public static void main(String[] args) {
		final ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
		DataCreator data = new DataCreator();
		Cathedra cathedra = data.createData();
		MenuCreator menu = new MenuCreator(cathedra);
		menu.buildMenu();
	}
}
