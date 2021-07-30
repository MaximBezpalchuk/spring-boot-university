package com.foxminded.university;


import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.foxminded.university.config.SpringConfig;
import com.foxminded.university.model.Cathedra;

public class Main {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		final ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
		DatabaseInitializer initializer = context.getBean("databaseInitializer", DatabaseInitializer.class);
		initializer.createDatabase();
		
		
		DataCreator data = new DataCreator();
		Cathedra cathedra = data.createData();
		//TODO: delete cathedra from method input when create all dao classes
		MenuCreator menu = new MenuCreator(cathedra);
		menu.buildMenu();
	}
}
