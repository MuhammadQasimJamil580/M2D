package com.M2D.EmissionAssessment.MultiTenancy;

import jakarta.annotation.PostConstruct;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import static org.hibernate.cfg.AvailableSettings.DEFAULT_SCHEMA;

@Component
public class DataSourceBasedMultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {
    @Autowired
    private DataSource defaultDS;
    @Autowired
    private ApplicationContext context;

    private Map<String, DataSource> map = new HashMap<>();

    boolean init = false;

    @PostConstruct
    public void load() {
        map.put(AvailableSettings.DEFAULT_SCHEMA, defaultDS);
    }

    @Override
    protected DataSource selectAnyDataSource() {
        return map.get(AvailableSettings.DEFAULT_SCHEMA);
    }

    @Override
    protected DataSource selectDataSource(String tenantIdentifier) {
        if (!init) {
            init = true;
            TenantDataSource tenantDataSource = context.getBean(TenantDataSource.class);
            try {
                map.putAll(tenantDataSource.getAll());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return map.get(tenantIdentifier) != null ? map.get(tenantIdentifier) : map.get(AvailableSettings.DEFAULT_SCHEMA);
    }
}