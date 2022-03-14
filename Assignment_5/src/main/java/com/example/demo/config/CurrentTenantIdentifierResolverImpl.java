package com.example.demo.config;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {
	private static final String DEFAULT_TENANT_ID = "persistence-book1";

	@Override
	public String resolveCurrentTenantIdentifier() {
		String tenant = DbContextHolder.getCurrentDb();
		if (tenant == null) {
			tenant = DEFAULT_TENANT_ID;
		}
		return tenant.isEmpty() || tenant.isBlank() ? DEFAULT_TENANT_ID : tenant;
	}

	@Override
	public boolean validateExistingCurrentSessions() {
		return true;
	}
}
