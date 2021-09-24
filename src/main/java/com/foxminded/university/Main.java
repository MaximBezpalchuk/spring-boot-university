package com.foxminded.university;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

	private final static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		logger.info("Start programm");
		MenuCreator menu = new MenuCreator();
		menu.buildMenu();
	}
}