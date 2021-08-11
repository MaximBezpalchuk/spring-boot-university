package com.foxminded.university.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@Configuration
@ComponentScan("com.foxminded.university")
@PropertySource("classpath:config.properties")
public class SpringConfig {

	@Value("${url}")
	public String url;
	@Value("${user}")
	public String user;
	@Value("${password}")
	public String password;
	@Value("${driverClass}")
	public String driverClass;
	@Value("schema.sql")
	Resource schema;
	@Value("data.sql")
	Resource data;

	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(driverClass);
		dataSource.setUsername(user);
		dataSource.setUrl(url);
		dataSource.setPassword(password);

		createSchema(dataSource);
		createData(dataSource);

		return dataSource;
	}

	private void createSchema(DriverManagerDataSource dataSource) {
		ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(schema);
		databasePopulator.execute(dataSource);
	}

	private void createData(DriverManagerDataSource dataSource) {
		ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(data);
		databasePopulator.execute(dataSource);
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

}
