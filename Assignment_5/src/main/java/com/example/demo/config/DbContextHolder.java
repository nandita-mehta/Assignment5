package com.example.demo.config;

public class DbContextHolder {
	private static String DEFAULT_TENANT_ID = "persistence-book1";
	private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

	static {
		DbContextHolder.setCurrentDb(DEFAULT_TENANT_ID);
	}

	public static void setCurrentDb(String dbType) {
		contextHolder.set(dbType);
	}

	public static String getCurrentDb() {
		return contextHolder.get();
	}

	public static void clear() {
		contextHolder.remove();
	}
}
