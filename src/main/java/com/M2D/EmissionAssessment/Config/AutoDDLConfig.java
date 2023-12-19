//package com.M2D.EmissionAssessment.Config;//package project.rovermd.ehr.multitenancy;
//
//import io.micrometer.common.util.StringUtils;
//import org.hibernate.jpa.HibernatePersistenceProvider;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//public class AutoDDLConfig {
//    @Value("${dbusernames}")
//    private String username;
//
//    @Value("${dbpwd}")
//    private String password;
//
//    @Value("${schemaslist}")
//    private String schemasList;
//
//    @Bean
//    public void bb() {
//
//        if (StringUtils.isBlank(schemasList)) {
//            return;
//        }
//
//        String[] tenants = schemasList.split(",");
//
//        for (String tenant : tenants) {
////            tenant = tenant.trim();
//            DriverManagerDataSource dataSource = new DriverManagerDataSource();
//            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver"); // Change here to MySql Driver
//            dataSource.setSchema(tenant);
////            dataSource.setUrl("jdbc:mysql://v2.rovermd.com:3306/" + tenant
////            dataSource.setUrl("jdbc:mysql://localhost:3306/" + tenant
//            dataSource.setUrl("jdbc:mysql://v2.rovermd.com:3306/" + tenant
//                    + "?zeroDateTimeBehavior=convertToNull");
//            dataSource.setUsername(username);
//            dataSource.setPassword(password);
//
//            LocalContainerEntityManagerFactoryBean emfBean = new LocalContainerEntityManagerFactoryBean();
//            emfBean.setDataSource(dataSource);
//            emfBean.setPackagesToScan("com.M2D.EmissionAssessment*"); // Here mention JPA entity path / u can leave it scans all packages
//            emfBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
//            emfBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
//            Map<String, Object> properties = new HashMap<>();
//
//            properties.put("hibernate.hbm2ddl.auto", "update");
//            properties.put("hibernate.default_schema", tenant);
//            properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
//
//            emfBean.setJpaPropertyMap(properties);
//            emfBean.setPersistenceUnitName(dataSource.toString());
//            emfBean.afterPropertiesSet();
//        }
//
//    }
//}
