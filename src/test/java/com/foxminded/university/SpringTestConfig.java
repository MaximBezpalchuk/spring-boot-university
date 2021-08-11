package com.foxminded.university;

import javax.sql.DataSource;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.foxminded.university.config.SpringConfig;

@Configuration
@ComponentScan("com.foxminded.university")
public class SpringTestConfig extends SpringConfig {

	@Override
	public DataSource dataSource() {
		return new EmbeddedDatabaseBuilder().addDefaultScripts().setType(EmbeddedDatabaseType.H2).build();
	}
}