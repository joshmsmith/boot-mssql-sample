package com.heywoodbanks.bootmssql.health;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BootMssqlHealthCheck extends HealthCheck implements InitializingBean {

	@Autowired
	private HealthCheckRegistry healthCheckRegistry;

	@Override
	public void afterPropertiesSet() throws Exception {
		healthCheckRegistry.register(this.getClass().getSimpleName(), this);
	}
}
