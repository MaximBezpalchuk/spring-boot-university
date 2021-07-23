package com.foxminded.university;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

@Component
public class DatabaseInitializer {

	private JdbcTemplate template;

	public DatabaseInitializer(JdbcTemplate template) {
		this.template = template;
	}

	public JdbcTemplate getTemplate() {
		return template;
	}

	public void setTemplate(JdbcTemplate template) {
		this.template = template;
	}

	public void createDatabase() {
		String sql = "";

		try {
			File schema = ResourceUtils.getFile("classpath:schema.sql");
			sql = new String(Files.readAllBytes(schema.toPath()));

		} catch (IOException e) {
			System.out.println("Reading schema failure..." + e.getMessage());
		}

		try {
			template.execute(sql);
		} catch (DataAccessException e) {
			System.out.println("Creating tables failure!" + e.getMessage());
		}

		try {
			File data = ResourceUtils.getFile("classpath:data.sql");
			sql = new String(Files.readAllBytes(data.toPath()));
		} catch (IOException e) {
			System.out.println("Reading data failure..." + e.getMessage());
		}

		try {
			template.update(sql);

		} catch (DataAccessException e) {
			System.out.println("Insert data failure!" + e.getMessage());
		}
	}

}
