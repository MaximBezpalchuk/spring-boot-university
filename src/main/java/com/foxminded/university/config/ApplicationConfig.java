package com.foxminded.university.config;

import org.hibernate.SessionFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@ComponentScan("com.foxminded.university")
@PropertySource("classpath:application.yml")
@EnableTransactionManagement
public class ApplicationConfig {

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        return templateResolver;
    }

    @Bean
    public SessionFactory sessionFactory(DataSource dataSource) throws IOException {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan("com.foxminded.university.model");
        sessionFactory.afterPropertiesSet();

        return sessionFactory.getObject();
    }
}