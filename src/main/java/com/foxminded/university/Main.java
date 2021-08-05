package com.foxminded.university;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.foxminded.university.config.BeanUtil;
import com.foxminded.university.config.SpringConfig;
import com.foxminded.university.model.Cathedra;

public class Main {

	public static void main(String[] args) {
		new BeanUtil(new AnnotationConfigApplicationContext(SpringConfig.class));
		DatabaseInitializer initializer = BeanUtil.getBean(DatabaseInitializer.class);
		initializer.createDatabase();

		DataCreator data = new DataCreator();
		Cathedra cathedra = data.createData();
		// TODO: delete cathedra from method input when create all dao classes
		// MenuCreator menu = new MenuCreator(cathedra);
		// menu.buildMenu();

		MenuCreator2 menu2 = new MenuCreator2(cathedra);
		menu2.buildMenu();

	}
}
