package com.foxminded.university.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.foxminded.university.dao")
@PropertySource("classpath:config.properties")
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
    public DataSourceTransactionManager txManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}