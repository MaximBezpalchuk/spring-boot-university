package com.foxminded.university.config;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@Configuration
@ComponentScan("com.foxminded.university.dao")
@PropertySource("classpath:config.properties")
@EnableTransactionManagement
public class DatabaseConfig {

	@Value("${url}")
	public String url;
	@Value("classpath:schema.sql")
	Resource schema;
	@Value("classpath:data.sql")
	Resource data;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	@Bean
	public DataSource dataSource() {
		JndiDataSourceLookup jndiDataSource = new JndiDataSourceLookup();
		jndiDataSource.setResourceRef(true);
		DataSource dataSource = jndiDataSource.getDataSource(url);
		createSchema(dataSource);
		createData(dataSource);

		return dataSource;
	}

	private void createSchema(DataSource dataSource) {
		ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(schema);
		databasePopulator.execute(dataSource);
	}

	private void createData(DataSource dataSource) {
		ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(data);
		databasePopulator.execute(dataSource);
	}

	@Bean
	public PlatformTransactionManager txManager(SessionFactory sessionFactory) {
		return new HibernateTransactionManager(sessionFactory);
	}

	@Bean
	public SessionFactory sessionFactory(DataSource dataSource) {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource);
		sessionFactory.setPackagesToScan("com.foxminded.university.model");
		sessionFactory.setHibernateProperties(hibernateProperties());
		try {
			sessionFactory.afterPropertiesSet();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sessionFactory.getObject();
	}

	private Properties hibernateProperties() {
		Properties properties = new Properties();
		properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL9Dialect");
		properties.put("hibernate.show_sql", "true");
		properties.put("logging.level.org.hibernate.type", "trace");
		properties.put("hibernate.current_session_context_class",
				"org.springframework.orm.hibernate5.SpringSessionContext");

		return properties;
	}
}