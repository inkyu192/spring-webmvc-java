package spring.webmvc.infrastructure.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Testcontainers
public class MySQLTestContainerConfig {

	@Container
	private static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.4.7");

	static {
		mysql.start();
	}

	@Bean
	public DataSource dataSource() {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(mysql.getJdbcUrl());
		config.setUsername(mysql.getUsername());
		config.setPassword(mysql.getPassword());
		config.setDriverClassName("com.mysql.cj.jdbc.Driver");
		return new HikariDataSource(config);
	}
}
