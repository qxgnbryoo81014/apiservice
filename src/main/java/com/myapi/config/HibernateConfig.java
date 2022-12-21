package com.myapi.config;

import java.util.Properties;


import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.Assert;

import com.myapi.apiservice.dao.impl.GenericServiceA;
import com.myapi.apiservice.dao.impl.GenericServiceB;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class HibernateConfig implements ApplicationContextAware{
	private static ApplicationContext context;
	
	public static ApplicationContext getApplicationContext() {
	    Assert.notNull(context, "applicationContext must be not null");
	    return context;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		HibernateConfig.context = applicationContext;
	}
	
	@Primary
	@Bean
    @ConfigurationProperties(prefix = "poolconfig")
    public HikariConfig hikariConfig() {
		return new HikariConfig();
	}

	@Primary
	@Bean(name = "dataSourceA")
    @ConfigurationProperties(prefix = "spring.datasource.myconna")
	public DataSource dataSourceA() {
		return DataSourceBuilder.create().build();
    }
    
	@Primary
	@Bean(name = "dataSourceB")
    @ConfigurationProperties(prefix = "spring.datasource.myconnb")
    public DataSource dataSourceB() {
    	return DataSourceBuilder.create().build();
    }

	@Primary
    @Bean(name = "sessionFactoryBeanA")
    public LocalSessionFactoryBean sessionFactoryBean() {
    	LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
    	
    	HikariConfig poolConfig = hikariConfig();
    	poolConfig.setDataSource(dataSourceA());
    	HikariDataSource hikariDataSource = new HikariDataSource(poolConfig);
    	sessionFactory.setDataSource(hikariDataSource);
    	sessionFactory.setPackagesToScan(new String[]{ "com.myapi.apiservice.entity.myconna" });
    	sessionFactory.setHibernateProperties(hibernateProperties());
    	sessionFactory.getHibernateProperties();
    	return sessionFactory;
    }
    
	@Primary
    @Bean(name = "sessionFactoryBeanB")
    public LocalSessionFactoryBean sessionFactoryBean2() {
    	LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
    	
    	HikariConfig poolConfig = hikariConfig();
    	poolConfig.setDataSource(dataSourceB());
    	HikariDataSource hikariDataSource = new HikariDataSource(poolConfig);
    	sessionFactory.setDataSource(hikariDataSource);
    	sessionFactory.setPackagesToScan(new String[]{ "com.myapi.apiservice.entity.myconnb" });
    	sessionFactory.setHibernateProperties(hibernateProperties());
    	return sessionFactory;
    }
	
    @Bean(name = "sessionFactoryA")
    public SessionFactory sessionsevA(@Qualifier("sessionFactoryBeanA") LocalSessionFactoryBean factoryBean) {
    	return factoryBean.getObject();
    }
    
    @Bean(name = "sessionFactoryB")
    public SessionFactory sessionsevB(@Qualifier("sessionFactoryBeanB") LocalSessionFactoryBean factoryBean) {
    	return factoryBean.getObject();
    }
    
    @Bean("genericServiceA")
	public GenericServiceA serviceA() {
		return new GenericServiceA(
			getApplicationContext().getBean("sessionFactoryA", SessionFactory.class)
		);
	}
    
    @Bean("genericServiceB")
   	public GenericServiceB serviceB() {
   		return new GenericServiceB(
   			getApplicationContext().getBean("sessionFactoryB", SessionFactory.class)
   		);
   	}
    
    @Bean(name = "transactionManagerA")
    public PlatformTransactionManager hibernateTransactionManagerA() {
    	HibernateTransactionManager transactionManager
          = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactoryBean().getObject());
        return transactionManager;
    }
    
    @Bean(name = "transactionManagerB")
    public PlatformTransactionManager hibernateTransactionManagerB() {
    	HibernateTransactionManager transactionManager
    	= new HibernateTransactionManager();
    	transactionManager.setSessionFactory(sessionFactoryBean2().getObject());
    	return transactionManager;
    }
    
    private final Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty(
        		"hibernate.show_sql", "true");
        hibernateProperties.setProperty(
        		"hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect");
        hibernateProperties.setProperty(
        		"hibernate.jdbc.fetch_size", "50"); // Environment.STATEMENT_FETCH_SIZE
        hibernateProperties.setProperty(
        		"hibernate.jdbc.batch_size", "25");
        return hibernateProperties;
    }
}
