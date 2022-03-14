package com.example.demo.config;

import org.hibernate.MultiTenancyStrategy;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@ComponentScan(basePackages = { "com.example.demo.*" })
@EntityScan("com.example.demo.*")
@EnableJpaRepositories(basePackages = "com.example.demo.*", entityManagerFactoryRef = "bookEntityManager", transactionManagerRef = "bookTransactionManager")
public class MultiTenantConfig {
	@Autowired
    private Environment env;

    public MultiTenantConfig() {
        super();
    }
    
    @Bean(name = "datasourceBasedMultitenantConnectionProvider")
    public MultiTenantConnectionProvider multiTenantConnectionProvider() {
        return new DataSourceBasedMultiTenantConnectionProviderImpl();
    }
    
    @Bean(name = "currentTenantIdentifierResolver")
    public CurrentTenantIdentifierResolver currentTenantIdentifierResolver() {
        return new CurrentTenantIdentifierResolverImpl();
    }
    
    @Bean
    @ConditionalOnBean(name = "datasourceBasedMultitenantConnectionProvider")
    public LocalContainerEntityManagerFactoryBean bookEntityManager(
            @Qualifier("datasourceBasedMultitenantConnectionProvider") MultiTenantConnectionProvider connectionProvider,
            @Qualifier("currentTenantIdentifierResolver") CurrentTenantIdentifierResolver tenantResolver) {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setPackagesToScan("com.example.demo.model");
        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        final HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put(org.hibernate.cfg.Environment.MULTI_TENANT, MultiTenancyStrategy.SCHEMA);
        properties.put(org.hibernate.cfg.Environment.MULTI_TENANT_CONNECTION_PROVIDER, connectionProvider);
        properties.put(org.hibernate.cfg.Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantResolver);
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        em.setJpaPropertyMap(properties);
        return em;
    }
    
    @PostConstruct
    public DataSource userDataSource() {
        String tenantName = "";
        if(tenantName.isEmpty() || tenantName.isBlank())
            tenantName = "persistence-book1";

        return DataSourceBuilder.create()
                .username(env.getProperty(tenantName.concat(".username")))
                .password(env.getProperty(tenantName.concat(".password")))
                .url(env.getProperty(tenantName.concat(".url")))
                .driverClassName(env.getProperty("spring.datasource.driver-class-name"))
                .build();
    }

    @Bean
    public PlatformTransactionManager bookTransactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(bookEntityManager(new DataSourceBasedMultiTenantConnectionProviderImpl(), new CurrentTenantIdentifierResolverImpl()).getObject());
        return transactionManager;
    }
}