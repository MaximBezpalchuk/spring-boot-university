package com.foxminded.university.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.jdbc.Sql;

@Configuration
@ComponentScan("com.foxminded.university")
public class SpringTestConfig {

	@Bean
	public JdbcTemplate jdbcTemplate(EmbeddedDatabase embeddedDB) {
		return new JdbcTemplate(embeddedDB);
	}

	@Bean
	@Sql({"/schema.sql", "/data.sql"})
	public EmbeddedDatabase embeddedDB() {
		return new EmbeddedDatabaseBuilder().addDefaultScripts().setType(EmbeddedDatabaseType.H2).build();
	}
}