package com.heywoodbanks.bootmssql;

import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.servlets.HealthCheckServlet;
import com.codahale.metrics.servlets.PingServlet;
import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.web.context.support.ServletContextAttributeExporter;

import javax.servlet.ServletContext;
import javax.sql.DataSource;
import javax.xml.crypto.Data;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public ServletRegistrationBean ping() {
		return new ServletRegistrationBean(new PingServlet(), "/ping");
	}

	@Bean
	public ServletRegistrationBean health() {
		return new ServletRegistrationBean(new HealthCheckServlet(), "/health");
	}

	@Bean
	public HealthCheckRegistry healthCheckRegistry() {
		return new HealthCheckRegistry();
	}

	@Bean
	public ServletContextAttributeExporter configureMetricServlet() {

		return new ServletContextAttributeExporter() {
			@Override
			public void setServletContext(ServletContext servletContext) {
				servletContext.setAttribute(HealthCheckServlet.HEALTH_CHECK_REGISTRY, healthCheckRegistry());
			}
		};
	}

	@Bean
	public DataSource dataSource(
			@Value("${EXTERNAL_MSSQL_SERVICE_SERVICE_HOST}") String host,
			@Value("${EXTERNAL_MSSQL_SERVICE_SERVICE_PORT}") String port,
			@Value("${MSSQL_USERNAME}") String username,
			@Value("${MSSQL_PASSWORD}") String password,
			@Value("${MSSQL_DATABASE_NAME}") String database) {

		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		dataSource.setDriverClass(net.sourceforge.jtds.jdbc.Driver.class);
		dataSource.setUrl(String.format("jdbc:jtds:sqlserver://%s:%s/%s", host, port, database));
		dataSource.setUsername(username);
		dataSource.setPassword(password);

		return dataSource;
	}

	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
