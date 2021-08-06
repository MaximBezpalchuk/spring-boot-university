package com.foxminded.university;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.foxminded.university.config.BeanUtil;
import com.foxminded.university.config.SpringConfig;

public class Main {

	public static void main(String[] args) {
		new BeanUtil(new AnnotationConfigApplicationContext(SpringConfig.class));
		DatabaseInitializer initializer = BeanUtil.getBean(DatabaseInitializer.class);
		initializer.createDatabase();
		MenuCreator menu = new MenuCreator();
		menu.buildMenu();
	}
}
