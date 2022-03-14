package com.example.demo.config;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

@Configuration
public class DataSourceBasedMultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {
	@Autowired
	private Environment env;

	private Map<String, DataSource> dataSourcesMtApp = new TreeMap<>();

	DataSourceBasedMultiTenantConnectionProviderImpl() {}

	@Override
	protected DataSource selectAnyDataSource() {
		readDataSource();
		if (dataSourcesMtApp.isEmpty()) {
			return defaultDataSource();
		}
		return this.dataSourcesMtApp.values().iterator().next();
	}

	@Override
	protected DataSource selectDataSource(String tenantIdentifier) {
		readDataSource();
		if (!this.dataSourcesMtApp.containsKey(tenantIdentifier)) {
			return defaultDataSource();
		}
		return this.dataSourcesMtApp.get(tenantIdentifier);
	}

	private void readDataSource() {
		String[] tenantDbs = env.getProperty("tenant-names").split(Pattern.quote("|"));
		for (String tenant : tenantDbs) {
			tenant = "persistence-" + tenant;
			DataSource ds = DataSourceBuilder.create().username(env.getProperty(tenant.concat(".username")))
					.password(env.getProperty(tenant.concat(".password"))).url(env.getProperty(tenant.concat(".url")))
					.driverClassName(env.getProperty("spring.datasource.driver-class-name")).build();
			dataSourcesMtApp.put(tenant, ds);
		}
	}
	
	private DataSource defaultDataSource() {
		return DataSourceBuilder.create().username(env.getProperty("persistence-book1.username"))
				.password(env.getProperty("persistence-book1.password"))
				.url(env.getProperty("persistence-book1.url"))
				.driverClassName(env.getProperty("spring.datasource.driver-class-name")).build();
	}

	private String initializeTenantIfLost(String tenantIdentifier) {
		if (tenantIdentifier != DbContextHolder.getCurrentDb()) {
			tenantIdentifier = DbContextHolder.getCurrentDb();
		}
		return tenantIdentifier;
	}
}