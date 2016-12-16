package com.heywoodbanks.bootmssql.health;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseHealthCheck extends BootMssqlHealthCheck {

	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHealthCheck.class);

	private static final String DATABASE_HEALTH_QUERY = "SELECT 1";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	protected Result check() throws Exception {
		Integer result;

		try {
			result = jdbcTemplate.queryForObject(DATABASE_HEALTH_QUERY, Integer.class);
		} catch (Exception e) {
			LOGGER.error("Unable to execute health query", e);
			return Result.unhealthy(e.getMessage());
		}

		if (result.equals(1)) {
			return Result.healthy();
		} else {
			return Result.unhealthy("Expected '1', but instead received " + result);
		}
	}
}
