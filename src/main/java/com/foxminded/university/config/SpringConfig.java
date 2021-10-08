package com.foxminded.university.config;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.format.FormatterRegistry;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import com.foxminded.university.formatter.GroupFormatter;
import com.foxminded.university.formatter.SubjectFormatter;

@Configuration
@ComponentScan("com.foxminded.university")
@PropertySource("classpath:config.properties")
@EnableWebMvc
public class SpringConfig implements WebMvcConfigurer {

	private final ApplicationContext applicationContext;

	@Value("${url}")
	public String url;
	@Value("${user}")
	public String user;
	@Value("${password}")
	public String password;
	@Value("${driverClass}")
	public String driverClass;
	@Value("classpath:schema.sql")
	Resource schema;
	@Value("classpath:data.sql")
	Resource data;

	@Autowired
	public SpringConfig(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

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

	@Bean
	public DataSourceTransactionManager txManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean
	public SpringResourceTemplateResolver templateResolver() {
		SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
		templateResolver.setApplicationContext(applicationContext);
		templateResolver.setPrefix("/WEB-INF/views/");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode(TemplateMode.HTML);
		return templateResolver;
	}

	@Bean
	public SpringTemplateEngine templateEngine() {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver());
		templateEngine.setEnableSpringELCompiler(true);
		return templateEngine;
	}

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		ThymeleafViewResolver resolver = new ThymeleafViewResolver();
		resolver.setTemplateEngine(templateEngine());
		registry.viewResolver(resolver);
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
		resolver.setFallbackPageable(PageRequest.of(0, 2));
		argumentResolvers.add(resolver);
		WebMvcConfigurer.super.addArgumentResolvers(argumentResolvers);
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addFormatter(new GroupFormatter()); //add multiply group choice on lectures/new
		registry.addFormatter(new SubjectFormatter()); //add multiply subject choice on teachers/new
	}
}
